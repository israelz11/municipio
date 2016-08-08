/*
Autor: ISC. Israel de la Cruz Hernandez
Fecha: 04/Octubre/2012
*/
$(document).ready(function(){  
	$('#cmdguardar').click(function (event){guardarSubdireccion();});
	$('#cmdnuevo').click(function (event){limpiarControles()});
	$('#cbodependencia').change(function(event){cambiarDependencia();})
	 //mostrarTablaSubdirecciones("detalles_Subdirecciones");
});

function cambiarDependencia(){
	$('#forma').submit();
}

/*Metodo para cancelar las requisiciones*/
function limpiarControles(){
	$('#ID_SUBDIRECCION').attr('value', '');
	$('#txtsubdireccion').attr('value', '');
	$('#txtresponsable').attr('value', '');
	$('#chkstatus').attr('checked', false);
}

/*funcion para guardar el pedido*/
function guardarSubdireccion(){
	var error="";
	if($('#cbodependencia').val()=='0'){jAlert('Establesca o seleccione la Unidad Administrativa','Advertencia'); return false;}
	if($('#txtsubdireccion').attr('value')==''){jAlert('Escriba el nombre del Área o Subdirección','Advertencia'); return false;}
	if($('#txtresponsable').attr('value')==''){jAlert('El nombre del responsable no es valido','Advertencia'); return false;}
	jConfirm('¿Confirma que la informacion de la Subdirección es correcta?','Guardar', function(r){
			if(r){
				_guardarSubdireccion();
			}
	});
}

function _guardarSubdireccion(){
	ShowDelay('Guardando Subdirección','');
	controladorAdministrarSubdireccionesRemoto.guardarSubdireccion($('#cbodependencia').attr('value'),$('#ID_SUBDIRECCION').attr('value'), $('#txtsubdireccion').attr('value'), $('#txtresponsable').attr('value'), $('#chkstatus').attr('checked'),{
			  callback:function(items){
				limpiarControles();
				CloseDelay("Subdireccion guardado con exito");	
				$('#forma').submit();
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');   
			return false;
		}
	});	
	_closeDelay();	
}

/*funcion para hacer la busqueda y editar el elemento seleccionado*/
function editarSubdireccion(id){
		limpiarControles();
		ShowDelay("Cargando Subdirección");
		$('#ID_SUBDIRECCION').attr('value', id);
		controladorAdministrarSubdireccionesRemoto.getSubdirecionDetalle(id,{
			  callback:function(items){
					$('#txtsubdireccion').attr('value', items.DESCRIPCION);
					$('#txtresponsable').attr('value', items.RESPONSABLE);
					$('#cbodependencia').val(items.ID_DEPENDENCIA);
					$('#chkstatus').attr('checked', items.STATUS);
					_closeDelay();
		} 
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');   
			return false;
		}
	});	
}

/*funcion para borrar los conceptos seleccionados*/
function borrarSubdireccion(){
	var chksubdireccion = [];
     $('input[name=chksubdireccion]:checked').each(function() {chksubdireccion.push($(this).val());});	
	 if (chksubdireccion.length>0){
		jConfirm('¿Confirma que desea eliminar las Subdirecciones seleccionadas?','Eliminar', function(r){
				if(r){
				 controladorAdministrarSubdireccionesRemoto.eliminarSubdireccion(chksubdireccion, {
					callback:function(items) {
					 		CloseDelay('Subdirecciones eliminadas con éxito', function(){$('#forma').submit();});	
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

