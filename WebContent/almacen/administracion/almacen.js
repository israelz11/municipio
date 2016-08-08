/**
Descripcion: Codigo controlador para de almacenes
Autor      : Mauricio Hernandez
Fecha      : 25/06/2010
*/


$(document).ready(function() {
    pintarTablaDetalles();
});


function limpiar(){
	 		 $('#descripcion').attr('value','');
			 $('#estatus').attr('checked',true);
			 $('#alarma').attr('checked',false);
			 $('#clave').attr('value','');
			 $('#email').attr('value','');
			 $('#responsable').attr('value','');
}

function guardar(){			
   
	var alarma='NO';
	if ($('#alarma').attr('checked'))	
	   alarma='SI';	
    if ( $('#unidad').attr('value')=="")  {jAlert('La Unidad Administrativa no es válido'); return false;}	
	if ( $('#descripcion').attr('value')=="")  {jAlert('Es necesario escribir el nombre del almacén'); return false;}		
	if ( $('#responsable').attr('value')=="")  {jAlert('Seleccione un responsable para administrar el almacén'); return false;}	
	if ( alarma=='SI'  && $('#email').attr('value')=="")  {jAlert('Email no válido, la alarma esta activada'); return false;}	
	
	var estatus='ACTIVO';
	if (!$('#estatus').attr('checked'))	
	   estatus='INACTIVO';	
	   ShowDelay('Guardando almacén','');	
    	controladorAlmacenRemoto.guardarAlmacen($('#clave').attr('value'),$('#unidad').attr('value'),$('#descripcion').attr('value'),estatus,$('#responsable').attr('value'), $('#txtalias').attr('value'),alarma,$('#email').attr('value'),{
			 callback:function(items) {				 
	  		 	CloseDelay("Almacén de guardado con éxito");	
			 limpiar();
 			 pintarTablaDetalles();			 
 		     }	
								,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});		
}

 function pintarTablaDetalles() {
	quitRow("detallesTabla");
	controladorAlmacenRemoto.getAlmacenes($('#unidad').attr('value'), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTabla( "detallesTabla", i+1 ,this.ID_UNIADM, this.ID_ALMACEN,this.DESCRIPCION, this.RESPONSABLE,this.ALIAS, this.ESTATUS,this.ALARMAS,this.EMAIL,this.ID_RESPONSABLE, this.ALIAS);
        }); 					   									
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 

 }

  function pintaTabla( table, consecutivo,idDependencia,id,descripcion,responsable,alias,estatus,alarmas,email,idResponsable, alias){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+id+"' >";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editar("+id+",'"+descripcion+"','"+estatus+"','"+alarmas+"','"+email+"',"+idResponsable+",'"+alias+"', "+idDependencia+")\" >"; 		
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(descripcion,"","","") );	  
	row.appendChild( Td(responsable,"","","") );
	row.appendChild( Td(getHTML(alias),centro,"","") );	  
	row.appendChild( Td(alarmas,centro,"","") );
	row.appendChild( Td(estatus,centro,"","") );
    row.appendChild( Td("",centro,"",htmlEdit) );	
	tabla.appendChild( row );
 }
 
 
 function editar(id,descripcion,estatus,alarma,email,idResponsable, alias, idDependencia) {
		 $('#descripcion').attr('value',descripcion);
		 $('#txtalias').attr('value', getHTML(alias));
		 $('#unidad').val(idDependencia);
		 getEmpleados();
		 if (estatus=='ACTIVO')
		   $('#estatus').attr('checked',true);			 
		 else
		   $('#estatus').attr('checked',false);
		 $('#clave').attr('value',id);
		 $('#email').attr('value',email);
		 if (alarma=='SI')
		   $('#alarma').attr('checked',true);			 
		 else
		   $('#alarma').attr('checked',false);
		 $('#responsable').attr('value',idResponsable);
 }
 
  function eliminar(){
	  var chkAlmacen = [];
     $('input[name=claves]:checked').each(function() {chkAlmacen.push($(this).val());	 });	 
	 if (chkAlmacen.length > 0 ) {
		 jConfirm('¿Confirma que desea eliminar el almacén seleccionado?','Eliminar', function(r){
				if(r){
							 controladorAlmacenRemoto.eliminarAlmacen(chkAlmacen, {
								callback:function(items) {
								   CloseDelay("Almacen(nes) aliminado(s) con exito");
									limpiar();
								   pintarTablaDetalles();
								} 					   				
								,
								errorHandler:function(errorString, exception) { 
								jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
							}); 
				}
		});	

   }
  
}
	 
function getEmpleados() {
 var unidad = $('#unidad').attr('value'); 
	 dwr.util.removeAllOptions("responsable");
	 if (unidad !="" ){
	controladorAlmacenRemoto.getResponsables(unidad, {
        callback:function(items) { 				
		 dwr.util.addOptions('responsable',{ '':'[Seleccione]' });
		 dwr.util.addOptions('responsable',items,"CVE_PERS", "NOMBRE_COMPLETO" );
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          				     	  
        }
    },async=false); 	
	 }
	 pintarTablaDetalles();
}