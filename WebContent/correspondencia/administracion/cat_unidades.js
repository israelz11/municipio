/*
Autor: ISC. Israel de la Cruz Hernandez
Fecha: 19/Mayo/2011
*/
$(document).ready(function() {  
	$('#cmdguardar').click(function (event){guardarUnidad();});
	$('#cmdnuevo').click(function (event){limpiarControles()});
	 //mostrarTablaSubdirecciones("detalles_Subdirecciones");
});

/*Metodo para cancelar las requisiciones*/
function limpiarControles(){
	 $('#ID_UNIDAD').attr('value', '');
	 $('#txtunidad').attr('value', '');
	 $('#txtunidad').focus();
	 $('#txtresponsable').attr('value', '');
	 $('#txtalias').attr('value', '');
	 $('#chkstatus').attr('checked', false);
}

/*funcion para guardar el pedido*/
function guardarUnidad(){
	var error="";
	if($('#txtunidad').attr('value')==''){jAlert('Escriba el nombre de la Unidad Administrativa','Advertencia'); return false;}
	if($('#txtresponsable').attr('value')==''){jAlert('El nombre del responsable no es valido','Advertencia'); return false;}
	jConfirm('¿Confirma que la informacion es correcta?','Guardar Unidad', function(r){
			if(r){
				_guardarUnidad();
			}
	});
}

/*funcion que permite guardar fisicamente el tipo de documento*/
function _guardarUnidad(){
	ShowDelay('Guardando Unidad Administrativa','');
	controladorAdministrarUnidadesRemoto.guardarUnidadAdm($('#ID_UNIDAD').attr('value'), $('#txtunidad').attr('value'), $('#txtalias').attr('value'), $('#txtresponsable').attr('value'), $('#chkstatus').attr('checked'),{
			  callback:function(items){
					CloseDelay('Unidad Administrativa guardada satisfactoriamente', function(){document.location = "cat_unidades.action";});	  
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>', 'Error al guardar el tipo de documento');   
			return false;
		}
	});	
}

/*funcion para hacer la busqueda y editar el elemento seleccionado*/
function editarUnidadAdm(id, tipo){
		limpiarControles();
		if(tipo==1){
				ShowDelay("Cargando Unidad...");
				$('#ID_UNIDAD').attr('value', id);
				controladorAdministrarUnidadesRemoto.getUnidadAdm($('#ID_UNIDAD').attr('value'),{
					  callback:function(items){
							$('#txtunidad').attr('value', items.DESCRIPCION);
							$('#txtresponsable').attr('value', items.RESPONSABLE);
							$('#txtalias').attr('value', items.PREFIJO);
							$('#chkstatus').attr('checked', items.STATUS);
							_closeDelay();	
				} 		
						   				
				,
				errorHandler:function(errorString, exception) { 
					jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>', 'Error al guardar el tipo de documento');   
					return false;
				}
			});	
		}
		else
			jAlert('No se puede modificar el Area/Unidad Seleccionada por que es de solo lectura','Advertencia');
}

/*funcion para borrar los conceptos seleccionados*/
function borrarUnidadesAdm(){
	var chkunidad = [];
     $('input[name=chkunidad]:checked').each(function() {chkunidad.push($(this).val());});	
	 if (chkunidad.length>0){
		jConfirm('¿Confirma que desea eliminar las Unidades seleccionadas?','Eliminar Unidades', function(r){
				if(r){
				 controladorAdministrarUnidadesRemoto.eliminarUnidadAdm(chkunidad, {
					callback:function(items) {
					   //mostrarTablaSubdirecciones('detalles_Subdirecciones');
					   CloseDelay('Unidades eliminadas satisfactoriamente', function(){document.location = "cat_unidades.action";});	
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
	controladorSubdireccionesRemoto.getUnidadesAdm({
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

