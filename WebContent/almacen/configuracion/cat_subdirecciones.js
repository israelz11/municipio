/*
Autor: ISC. Israel de la Cruz Hernandez
Fecha: 29/Junio/2010
*/
$(document).ready(function() {  
	$('#cmdguardar').click(function (event){guardarSubdireccion();});
	$('#cmdnuevo').click(function (event){limpiarControles()});
	 mostrarTablaSubdirecciones("detalles_Subdirecciones");
});

/*Metodo para cancelar las requisiciones*/
function limpiarControles(){
	 $('#ID_SUBDIRECCION').attr('value', '');
	 $('#txtdescripcion').attr('value', '');
	 $('#txtdescripcion').focus();
	 $('#txtencargado').attr('value', '');
	 $('#txtpuesto').attr('value', '');
	 $('#chkstatus').attr('checked', false);
}

/*funcion para guardar el pedido*/
function guardarSubdireccion(){
	var error="";
	if($('#txtdescripcion').attr('value')==''){jAlert('Escriba el nombre de la Subdirección','Advertencia'); return false;}
	if($('#txtencargado').attr('value')==''){jAlert('El encargado de la Subdirección no es valido','Advertencia'); return false;}
	if($('#txtpuesto').attr('value')==''){jAlert('El puesto del encargado no es valido','Advertencia'); return false;}
	jConfirm('¿Confirma que desea guardar la informacion de la Subdirección?','Guardar Subdirección', function(r){
			if(r){
				_guardarSubdireccion();
			}
	});
}

/*funcion que permite guardar fisicamente el tipo de documento*/
function _guardarSubdireccion(){
	controladorSubdireccionesRemoto.guardarSubdireccion($('#ID_SUBDIRECCION').attr('value'), $('#ID_UNIDAD').attr('value'), $('#txtdescripcion').attr('value'), $('#txtencargado').attr('value'), $('#txtpuesto').attr('value'), $('#chkstatus').attr('checked'),{
			  callback:function(items){
				  		if(items==true){
							limpiarControles();
						 	mostrarTablaSubdirecciones('detalles_Subdirecciones');
						  	CloseDelay('Subdireccion guardada con éxito');	  
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
function editarSubdireccion(id){
		limpiarControles();
		$('#ID_SUBDIRECCION').attr('value', id);
		controladorSubdireccionesRemoto.getSubdireccion($('#ID_SUBDIRECCION').attr('value'),{
			  callback:function(items){
				  	$('#txtdescripcion').attr('value', items.DESCRIPCION);
					$('#txtencargado').attr('value', items.NOMBRE_ENCARGADO);
					$('#txtpuesto').attr('value', items.PUESTO_ENCARGADO);
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
function borrarSubdirecciones(){
	var checkConceptos = [];
     $('input[name=chkconcepto]:checked').each(function() {checkConceptos.push($(this).val());});	
	 if (checkConceptos.length>0){
		jConfirm('¿Confirma que desea eliminar las Subdirecciones seleccionadas?','Eliminar Subdirecciones', function(r){
				if(r){
				 controladorSubdireccionesRemoto.eliminarSubdirecciones(checkConceptos, {
					callback:function(items) {
					   mostrarTablaSubdirecciones('detalles_Subdirecciones');
					   CloseDelay('Registro eliminado(s) con éxito', 'Informacion');
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
function mostrarTablaSubdirecciones(tabla){
	quitRow(tabla);
	controladorSubdireccionesRemoto.getListaSubdirecciones({
						   callback:function(items) { 
						   		jQuery.each(items,function(i){
									 pintaTablaDetalles(tabla, this);				   
								});
						   }
	});
	
}

/*Funcion para pintar las filas de los conceptos*/
function pintaTablaDetalles(table, obj){
	var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editarSubdireccion("+obj.ID_SUBDIRECCION+")\">";
	/*Filas de los conceptos*/
	appendNewRow(table, [Td('', centro , '', '<input type="checkbox" name="chkconcepto" id="chkconcepto" value="'+obj.ID_SUBDIRECCION+'">'),
						 Td('', izquierda , '', '&nbsp;'+obj.DESCRIPCION),
						 Td('', izquierda , '', '&nbsp;'+obj.NOMBRE_ENCARGADO),
						 Td('', izquierda , '', '&nbsp;'+obj.PUESTO_ENCARGADO),
				 		 Td('', centro , '', obj.STATUS_DESC),
						 Td('', centro , '', htmlEdit)
				]);
}

