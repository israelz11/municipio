/*
Autor: ISC. Israel de la Cruz Hernandez
Fecha: 15/Julio/2010
*/
$(document).ready(function() {  
	$('#cmdguardar').click(function (event){guardarInventario();});
	$('#cmdregresar').click(function (event){regresarPantalla()});
	 
	 getBeneficiarios('txtbeneficiario','ID_PROVEEDOR');
	 getUnidad_Medidas('txtunidadmedida','CVE_UNIDAD_MEDIDA');
});

/*funcion para guardar el pedido*/
function guardarInventario(){
	if($('#txtbeneficiario').attr('value')=='') $('#ID_PROVEEDOR').attr('value', '0');
	if($('#txtunidadmedida').attr('value')=='') $('#ID_UNIDAD_MEDIDA').attr('value', '0');
	
	if($('#ID_INVENTARIO').attr('value')==0||$('#ID_INVENTARIO').attr('value')==''){jAlert('El inventario que intenta guardar no es valido, vuelva a intentar esta operación','Advertencia'); return false;}	
	//if($('#cboalmacen').attr('value')==0){jAlert('Es necesario seleccionar un almacen valido','Advertencia'); return false;}	
	if($('#ID_PROVEEDOR').attr('value')==0||$('#ID_PROVEEDOR').attr('value')==''){jAlert('Es necesario seleccionar un proveedor valido','Advertencia'); return false;}	
	if($('#ID_UNIDAD_MEDIDA').attr('value')==0||$('#ID_UNIDAD_MEDIDA').attr('value')==''){jAlert('La unidad de medida seleccionada no es valida','Advertencia'); return false;}	
	if($('#txtprecio').attr('value')==''){jAlert('El precio del articulo no es valido','Advertencia'); return false;}
	
	jConfirm('¿Confirma que desea guardar la informacion del inventario actual?','Guardar Inventario', function(r){
			if(r){
				_guardarInventario();
			}
	});
}

function regresarPantalla(){
	//history.go(-1);
	document.location = "../consultas/lst_articulos.action";
}

/*funcion que permite guardar fisicamente el tipo de documento*/
function _guardarInventario(){
	var alarma = ($('#chkalarma').is(':checked') ? "1":"0");
	var status = ($('#chkstatus').is(':checked') ? "1":"0");	
	controladorConfiguracionArticulosAlmacenRemoto.guardarArticuloAlmacen($('#ID_INVENTARIO').attr('value'), $('#cbofamilia').attr('value'), $('#ID_PROVEEDOR').attr('value'), $('#ID_UNIDAD_MEDIDA').attr('value'), $('#txtprecio').attr('value'), $('#txtexistencia_minima').attr('value'), alarma, status, {
			  callback:function(items){
				  		if(items==true){
						  	CloseDelay('Articulo guardado con éxito');	  
						}
						else
							jError('La operacion ah fallado al tratar de guardar el conjunto de datos', 'Error');
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');   
			return false;
		}
	});	
}


