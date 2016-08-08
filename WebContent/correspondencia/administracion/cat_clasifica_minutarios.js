/*
Autor: ISC. Israel de la Cruz Hernandez
Fecha: 04/Octubre/2012
*/
$(document).ready(function(){  
	$('#cmdguardar').click(function (event){guardarCatClasificaMinutario();});
	$('#cmdnuevo').click(function (event){limpiarControles()});
	$('#cbodependencia').change(function(event){$('#forma').submit();})
	 //mostrarTablaSubdirecciones("detalles_Subdirecciones");
});

/*Metodo para cancelar las requisiciones*/
function limpiarControles(){
	$('#ID_CAT_CLASIFICACION_MINUTARIO').attr('value', '');
	$('#txtclasifica').attr('value', '');
	$('#chkstatus').attr('checked', false);
}

/*funcion para guardar el pedido*/
function guardarCatClasificaMinutario(){
	if($('#cbodependencia').val()=='0'){jAlert('Seleccione la Unidad Administrativa','Advertencia'); return false;}
	if($('#txtclasifica').attr('value')==''){jAlert('La descripción no es válida','Advertencia'); return false;}
	jConfirm('¿Confirma que la información de clasificación es correcta?','Guardar', function(r){
			if(r){
				_guardarClasificaMinutario();
			}
	});
}

function _guardarClasificaMinutario(){
	ShowDelay('Guardando Minutario','');
	controladorAdministrarClasificaMinutariosRemoto.guardarClasificaMinutario($('#ID_CAT_CLASIFICACION_MINUTARIO').attr('value'), $('#cbodependencia').attr('value'), $('#txtclasifica').attr('value'), $('#chkstatus').attr('checked'),{
			  callback:function(items){
				limpiarControles();
				CloseDelay("Detalles guardados con exito");	
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
function editarClasificaMinutario(idClasifica, idDependencia, descripcion, status){
		limpiarControles();
		ShowDelay("Cargando clasificación");
		$('#ID_CAT_CLASIFICACION_MINUTARIO').attr('value', idClasifica);
		$('#txtclasifica').attr('value', descripcion);
		$('#cbodependencia').val(idDependencia);
		$('#chkstatus').attr('checked', ((status==1) ? true:false));
		_closeDelay();
}

/*funcion para borrar los conceptos seleccionados*/
function borrarClasificaMinutario(){
	var chkClasificaMinutario = [];
     $('input[name=chkClasificaMinutario]:checked').each(function() {chkClasificaMinutario.push($(this).val());});	
	 if (chkClasificaMinutario.length>0){
		jConfirm('¿Confirma que desea eliminar los conceptos seleccionados?','Eliminar', function(r){
				if(r){
				 controladorAdministrarClasificaMinutariosRemoto.eliminarClasificaMinutarios(chkClasificaMinutario, {
					callback:function(items) {
					 		CloseDelay('Elemento(s) eliminado(s) con éxito', function(){$('#forma').submit();});	
					}
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					}
				});
	   }});
	 } 
	else 
	    jAlert('Es necesario que seleccione por lo menos un concepto del listado', 'Advertencia');
}
