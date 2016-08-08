/*
Autor: ISC. Israel de la Cruz Hernandez
Fecha: 30/Junio/2010
*/
$(document).ready(function() {  
	$('#cmdguardar').click(function (event){guardarFamiliasArticulos();});
	$('#cmdnuevo').click(function (event){limpiarControles()});
	 mostrarFamiliasArticulos("detalles_FamiliasArticulos");
});

/*Metodo para cancelar las requisiciones*/
function limpiarControles(){
	 $('#ID_FAMILIA').attr('value', '');
	 $('#txtdescripcion').attr('value', '');
	 $('#txtdescripcion').focus();
	 $('#chkstatus').attr('checked', false);
}

/*funcion para guardar el pedido*/
function guardarFamiliasArticulos(){
	var error="";
	if($('#txtdescripcion').attr('value')==''){
		jAlert('Escriba el nombre de la familia','Advertencia'); return false;}

	
	jConfirm('¿Confirma que desea guardar la familia?','Guardar familias', function(r){
			if(r){
				_guardarFamiliasArticulos();
			}
	});
}

/*funcion que permite guardar fisicamente el tipo de documento*/
function _guardarFamiliasArticulos(){
	controladorFamiliasArticulosRemoto.guardarFamiliasArticulos($('#ID_FAMILIA').attr('value'), $('#txtdescripcion').attr('value'), $('#chkstatus').attr('checked'),{
			  callback:function(items){
				  		if(items==true){
							limpiarControles();
						 	mostrarFamiliasArticulos('detalles_FamiliasArticulos');
						  	CloseDelay('Información guardada satisfactoriamente');	  
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
function editarFamilias(id){
		limpiarControles();
		$('#ID_FAMILIA').attr('value', id);
		controladorFamiliasArticulosRemoto.getFamilia($('#ID_FAMILIA').attr('value'),{
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
function borrarFamiliasArticulos(){
	var checkConceptos = [];
     $('input[name=chkconcepto]:checked').each(function() {checkConceptos.push($(this).val());});	
	 if (checkConceptos.length>0){
		jConfirm('¿Confirma que desea eliminar las familias seleccionadas?','Eliminar familias', function(r){
				if(r){
				 controladorFamiliasArticulosRemoto.eliminarFamiliasArticulos(checkConceptos, {
					callback:function(items) {
					   mostrarFamiliasArticulos('detalles_FamiliasArticulos');
					   CloseDelay('Familia eliminada con éxito');
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
function mostrarFamiliasArticulos(tabla){
	quitRow(tabla);
	controladorFamiliasArticulosRemoto.getListaFamiliasArticulos({
						   callback:function(items) {
						   		jQuery.each(items,function(i){
									 pintaTablaDetalles(tabla, this);				   
								});
						   }
	});
	
}

/*Funcion para pintar las filas de los conceptos*/
function pintaTablaDetalles(table, obj){
	var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editarFamilias("+obj.ID_FAMILIA+")\">";
	/*Filas de los conceptos*/
	appendNewRow(table, [Td('', centro , '', '<input type="checkbox" name="chkconcepto" id="chkconcepto" value="'+obj.ID_FAMILIA+'">'),
						 Td('', izquierda , '', '&nbsp;'+obj.DESCRIPCION),
				 		 Td('', centro , '', obj.STATUS_DESC),
						 Td('', centro , '', htmlEdit)
				]);
}

