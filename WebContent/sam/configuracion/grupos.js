/**
Descripcion: Codigo controlador para la pagina grupos.jsp
Autor      : Mauricio Hernandez
Fecha      : 26/10/2009
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/


function limpiar(){
	 		 $('#descripcion').attr('value','');
			 $('#estatus').attr('checked',true);			 
			 $('#clave').attr('value','');
}

function guardar(){	
    var error="";
	var titulo ='Advertencia - Informacion no válida';
	if ( $('#descripcion').attr('value')=="")  error += 'Descripción</br>';	
	if ( $('#tipo').attr('value')=="")  error += 'Tipo</br>';	
	if ( error=="") {	
	var estatus='ACTIVO';
	if (!$('#estatus').attr('checked'))	
	   estatus='INACTIVO';	
	ShowDelay('Guardando grupo','');
    controladorGruposRemoto.guardarGrupo($('#clave').attr('value'),$('#descripcion').attr('value'),estatus,$('#tipo').attr('value'),{
			 callback:function(items) {				 
	  			 CloseDelay("Grupo guardado con éxito", 2000, function(){pintarTablaDetalles();});
 		     }	
								,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});		}else jAlert(error,titulo);	
}

 function pintarTablaDetalles() {
	 quitRow("detallesTabla");
	var estatus='ACTIVO';
	if (!$('#estatus2').attr('checked'))	
	   estatus='INACTIVO';	
	   ShowDelay('Cargando grupos','');
	controladorGruposRemoto.getGruposEstatus(estatus,$('#tipo').attr('value'), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
				
 		     	pintaTabla( "detallesTabla", i+1 ,this.ID_GRUPO_CONFIG,this.GRUPO_CONFIG,this.ESTATUS,this.TIPO);
        }); 	
			_closeDelay();
			limpiar();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 

 }

  function pintaTabla( table, consecutivo,id,descripcion,estatus,tipo){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+id+"' >";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editar("+id+",'"+descripcion+"','"+estatus+"','"+tipo+"')\" >"; 		
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(descripcion,"","","") );	  
	row.appendChild( Td(tipo,centro,"","") );	  
	row.appendChild( Td(estatus,centro,"","") );
    row.appendChild( Td("",centro,"",htmlEdit) );	
	tabla.appendChild( row );
 }
 
 
 function editar(id,descripcion,estatus,tipo) {
		 $('#descripcion').attr('value',descripcion);
		 if (estatus=='ACTIVO')
		   $('#estatus').attr('checked',true);			 
		 else
		   $('#estatus').attr('checked',false);
		 $('#clave').attr('value',id);
		 $('#tipo').attr('value',tipo);
 }
 
  function eliminar(){
	  var checkRetenciones = [];
     $('input[name=claves]:checked').each(function() {checkRetenciones.push($(this).val());	 });	 
	 if (checkRetenciones.length > 0 ) {
		 jConfirm('¿Confirma que desea eliminar el grupo?','Confirmar', function(r){
			 	if(r){
						ShowDelay('Eliminando el grupo','');
						controladorGruposRemoto.eliminarGrupo(checkRetenciones, {
							callback:function(items) {
							   CloseDelay("Grupo eliminado con éxito", 2000, function(){pintarTablaDetalles();});
							   
							} 					   				
							,
							errorHandler:function(errorString, exception) { 
							jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
							}
						});
					}
		})
			 
	 } else 
	    jInformation("Es necesario que seleccione un elemento de la lista");
	 }