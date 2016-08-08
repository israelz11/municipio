/**
Descripcion: Codigo controlador para de almacenes
Autor      : Mauricio Hernandez
Fecha      : 25/06/2010
*/

function limpiar(){
			 $('#responsable').attr('value','');			 
}

function guardar(){			
    var error="";
	var titulo ='Advertencia - Informacion no válida';
    if ( $('#almacen').attr('value')=="")  error += 'Almacen</br>';	
	if ( $('#responsable').attr('value')=="")  error += 'Usuario</br>';	
	if ( error=="") {		   
    controladorAsignacionAlmacenRemoto.guardarUsuarioAlmacen($('#almacen').attr('value'),$('#responsable').attr('value'),{
			 callback:function(items) {				 
	  		 CloseDelay("Asignación guardada con éxito");
			 pintarTablaDetalles();	
			 limpiar();		 
 		     }	
								,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});		}else jAlert(error,titulo);	
}

 function getAlmacenes() {
	dwr.util.removeAllOptions("almacen");
	controladorAlmacenRemoto.getAlmacenes($('#unidad').attr('value'), {
        callback:function(items) { 		
		  dwr.util.addOptions('almacen',{ '':'[Seleccione]' });
		  dwr.util.addOptions('almacen',items,"ID_ALMACEN", "DESCRIPCION" );		
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    },async=false); 
	
 }

 function pintarTablaDetalles() {
	quitRow("detallesTabla");
	if ($('#almacen').attr('value')!="") {
	controladorAsignacionAlmacenRemoto.getUsuarioAlmacenes($('#unidad').attr('value'),$('#almacen').attr('value'), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTabla( "detallesTabla", i+1 ,this.ALMACEN,this.USUARIO,this.ID_USUARIO);
        }); 					   									
        },
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 
	}
 }
 
  function pintaTabla( table, consecutivo,almacen,responsable,id){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+id+"' >";
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(responsable,"","","") );	  
	row.appendChild( Td(almacen,"","","") );	  
	tabla.appendChild( row );
 }
 
 
  function eliminar(){
	  var checkRetenciones = [];
     $('input[name=claves]:checked').each(function() {checkRetenciones.push($(this).val());	 });	 
	 if (checkRetenciones.length > 0 ) {
     controladorAsignacionAlmacenRemoto.eliminarUsuarioAlmacen(checkRetenciones,$('#almacen').attr('value'), {
        callback:function(items) {
		   CloseDelay("Asignación eliminada con éxito");
		    limpiar();
		   pintarTablaDetalles();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); } else 
	    jInformation("Es necesario que seleccione un elemento de la lista");

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
	 getAlmacenes();
}