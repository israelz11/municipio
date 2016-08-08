/*
Autor: ISC. Israel de la Cruz Hernandez
Fecha: 26/Junio/2010
*/
$(document).ready(function() {  
	$('#cmdguardar').click(function (event){guardarTipoDocumento();});
	$('#cmdnuevo').click(function (event){limpiarControles()});
	 mostrarTablaTiposDocumentos("detalles_Tipos_Documentos");
});

/*Metodo para cancelar las requisiciones*/
function limpiarControles(){
	 $('#ID_TIPO_DOCUMENTO').attr('value', '');
	 $('#txtdescripcion').attr('value', '');
	 $('#txtdescripcion').focus();
	 $('#chkstatus').attr('checked', false);
}

/*funcion para guardar el pedido*/
function guardarTipoDocumento(){
	var error="";
	if($('#txtdescripcion').attr('value')==''){jAlert('Escriba la descripcion del tipo de documento','Advertencia'); return false;}
	jConfirm('¿Confirma que desea guardar el tipo de documento?','Guardar tipo de documento', function(r){
			if(r){
				_guardarTipoDocumento();
			}
	});
}

/*funcion que permite guardar fisicamente el tipo de documento*/
function _guardarTipoDocumento(){
	controladorTiposDocumentosRemoto.guardarTipoDocumento($('#ID_TIPO_DOCUMENTO').attr('value'), $('#txtdescripcion').attr('value'), $('#chkstatus').attr('checked'),{
			  callback:function(items){
				  		if(items==true){
							limpiarControles();
						 	mostrarTablaTiposDocumentos('detalles_Tipos_Documentos');
						  	CloseDelay('Información guardada con éxito');	  
						}
						else
							jError('La operacion ah fallado al tratar de guardar el conjunto de datos', 'Error');
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>', 'Error al guardar el tipo de documento');   
			return false;
		}
	});	
}

/*funcion para hacer la busqueda y editar el elemento seleccionado*/
function editarTiposDocumentos(id){
		limpiarControles();
		$('#ID_TIPO_DOCUMENTO').attr('value', id);
		controladorTiposDocumentosRemoto.getTipoDocumento($('#ID_TIPO_DOCUMENTO').attr('value'),{
			  callback:function(items){
				  	$('#txtdescripcion').attr('value', items.DESCRIPCION);
					$('#chkstatus').attr('checked', items.STATUS);
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>', 'Error al guardar el tipo de documento');   
			return false;
		}
	});	
}

/*funcion para borrar los conceptos seleccionados*/
function borrarTiposDocumentos(){
	var checkConceptos = [];
     $('input[name=chkconcepto]:checked').each(function() {checkConceptos.push($(this).val());});	
	 if (checkConceptos.length>0){
		jConfirm('¿Confirma que desea eliminar los tipos de documentos seleccionados?','Eliminar tipos de documentos', function(r){
				if(r){
				 controladorTiposDocumentosRemoto.eliminarTiposDocumentos(checkConceptos, {
					callback:function(items) {
					   mostrarTablaTiposDocumentos('detalles_Tipos_Documentos');
					   CloseDelay('Registro eliminados satisfactoriamente');
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador", 'Error al eliminar elementos');          
					}
				});
	   }});
	 } 
	else 
	    jAlert('Es necesario que seleccione por lo menos un concepto del listado', 'Advertencia');
		
}

/*funcion para mostrar el listado de conceptos*/
function mostrarTablaTiposDocumentos(tabla){
	quitRow(tabla);
	controladorTiposDocumentosRemoto.getListaTiposDocumentos({
						   callback:function(items) { 
						   		jQuery.each(items,function(i){
									 pintaTablaDetalles(tabla, this);				   
								});
						   }
	});
	
}

/*Funcion para pintar las filas de los conceptos*/
function pintaTablaDetalles(table, obj){
	var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editarTiposDocumentos("+obj.ID_TIPO_DOCUMENTO+")\">";
	/*Filas de los conceptos*/
	appendNewRow(table, [Td('', centro , '', '<input type="checkbox" name="chkconcepto" id="chkconcepto" value="'+obj.ID_TIPO_DOCUMENTO+'">'),
						 Td('', izquierda , '', '&nbsp;'+obj.DESCRIPCION),
				 		 Td('', centro , '', obj.STATUS_DESC),
						 Td('', centro , '', htmlEdit)
				]);
}

