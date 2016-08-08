/*
Autor: ISC. Israel de la Cruz Hernandez
Fecha: 04/Octubre/2012
*/
$(document).ready(function(){  
	$('#cmdguardar').click(function (event){guardarCatMinutario();});
	$('#cmdnuevo').click(function (event){limpiarControles()});
	$('#cbodependencia').change(function(event){getSubdirecciones();})
	 //mostrarTablaSubdirecciones("detalles_Subdirecciones");
});

function getSubdirecciones(){
	$('#forma').submit();
}

/*Metodo para cancelar las requisiciones*/
function limpiarControles(){
	$('#ID_CAT_MINUTARIO').attr('value', '');
	$('#cbosubdireccion').val(0);
	$('#txtminutario').attr('value', '');
	$('#chkstatus').attr('checked', false);
}

/*funcion para guardar el pedido*/
function guardarCatMinutario(){
	var error="";
	if($('#cbodependencia').val()=='0'){jAlert('Seleccione la Unidad Administrativa','Advertencia'); return false;}
	if($('#cbosubdireccion').attr('value')=='0'){jAlert('Seleccione Área o Subdirección','Advertencia'); return false;}
	if($('#txtminutario').attr('value')==''){jAlert('El nombre de minutario no es válido','Advertencia'); return false;}
	jConfirm('¿Confirma que la informacion del minutario es correcta?','Guardar', function(r){
			if(r){
				_guardarMinutario();
			}
	});
}

function _guardarMinutario(){
	ShowDelay('Guardando Minutario','');
	controladorAdministrarMinutariosRemoto.guardarMinutario($('#ID_CAT_MINUTARIO').attr('value'), $('#cbodependencia').attr('value'), $('#cbosubdireccion').attr('value'), $('#txtminutario').attr('value'), $('#chkstatus').attr('checked'),{
			  callback:function(items){
				limpiarControles();
				CloseDelay("Minutario guardado con exito");	
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
function editarMinutario(idCatMinutario, idSubdireccion, minutario, status){
		limpiarControles();
		ShowDelay("Cargando Subdirección");
		$('#ID_CAT_MINUTARIO').attr('value', idCatMinutario);
		$('#txtminutario').attr('value', minutario);
		$('#cbosubdireccion').val(idSubdireccion);
		$('#chkstatus').attr('checked', ((status==1) ? true:false));
		_closeDelay();
}

/*funcion para borrar los conceptos seleccionados*/
function borrarMinutario(){
	var chkminutario = [];
     $('input[name=chkminutario]:checked').each(function() {chkminutario.push($(this).val());});	
	 if (chkminutario.length>0){
		jConfirm('¿Confirma que desea eliminar los minutarios seleccionados?','Eliminar', function(r){
				if(r){
				 controladorAdministrarMinutariosRemoto.eliminarMinutarios(chkminutario, {
					callback:function(items) {
					 		CloseDelay('Minutario(s) eliminado(s) con éxito', function(){$('#forma').submit();});	
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
