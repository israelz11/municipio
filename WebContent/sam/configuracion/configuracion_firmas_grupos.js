/**
Descripcion: Codigo controlador para la pagina grupos.jsp
Autor      : Mauricio Hernandez
Fecha      : 26/10/2009
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/

function limpiar(){
			 $('#tipoFirma').attr('value','');			 
			 $('#representante').attr('value','');
			 $('#cargo').attr('value','');
			 $('#clave').attr('value','');
}

function guardar(){			
    var error="";
	var titulo ='Advertencia';
	if ( $('#grupo').attr('value')=="")  error += 'Grupo</br>';	
	if ( $('#tipoFirma').attr('value')=="")  error += 'Tipo Firma</br>';	
	if ( $('#representante').attr('value')=="")  error += 'Representante</br>';	
	if ( $('#cargo').attr('value')=="")  error += 'Cargo</br>';	
	if ( error=="") {	
    controladorFirmasGruposRemoto.guardarFirmaGrupo($('#clave').attr('value'),  $('#tipoFirma').attr('value') ,$('#cargo').attr('value'), $('#representante').attr('value'), $('#grupo').attr('value'),{
			 callback:function(items) {			  			  
			 if (items==true)
	  		   jInformation("La información se almaceno satisfactoriamente","Información");
			   else
			   jError("La información no se almaceno, el tipo de firma puede estar repetido","Error");
			 pintarTablaDetalles();
 		     }	
								,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});		}else jAlert(error,titulo);	
}

 function pintarTablaDetalles() {
	 quitRow("detallesTabla");
	var grupo=$('#grupo').attr('value');	
	if (grupo!="") {
	ShowDelay('Cargando firmas de grupos','');
	controladorFirmasGruposRemoto.getFirmasPorGrupo(grupo, {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		    pintaTabla( "detallesTabla", i+1 ,this.ID_FIRMA_GRUPO,this.TIPO,this.CARGO,this.REPRESENTANTE,this.ID_GRUPO_CONFIG);
        }); 					   						
			limpiar();
			_closeDelay();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); }

 }

  function pintaTabla( table, consecutivo,id,tipo,cargo,representante,grupo){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+id+"' >";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editar("+id+",'"+tipo+"','"+cargo+"','"+representante+"',"+grupo+")\" >"; 		
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(tipo,"","","") );	  
	row.appendChild( Td(representante,"","","") );
	row.appendChild( Td(cargo,"","","") );
    row.appendChild( Td("",centro,"",htmlEdit) );	
	tabla.appendChild( row );
 }
 
 
 function editar(id,tipo,cargo,representante,grupo) {
		  $('#grupo').attr('value',grupo);			 
		  $('#tipoFirma').attr('value',tipo);			 
		 $('#representante').attr('value',representante);
		 $('#cargo').attr('value',cargo);
		 $('#clave').attr('value',id);
 }
 
  function eliminar(){
	  var checkRetenciones = [];
     $('input[name=claves]:checked').each(function() {checkRetenciones.push($(this).val());	 });	 
	 if (checkRetenciones.length > 0 ) {
		 jConfirm('¿Confirma que desea aliminar el elemento de firma actual?','Confirmar', function(r){
			 	if(r){
						ShowDelay('Eliminando grupo(s) de firma(s)','');
						 controladorFirmasGruposRemoto.eliminarFirmaGrupo(checkRetenciones, {
							callback:function(items) {
							   CloseDelay('Grupo(s) de firma(s) eliminaoo satisfcatoriamente', 2000, function(){
									pintarTablaDetalles();
								 });
							   
							} 					   				
							,
							errorHandler:function(errorString, exception) { 
							jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
							}
				  		});
				}
		 
    }); } else 
	    jAlert("Es necesario que seleccione un elemento de la lista", "Advertencia");

	 }