/*
Autor: ISC. Israel de la Cruz Hernandez
Version: 2.0
Date: 01-Jul-2010
Update: 5 Agosto 2012
*/
var subtotal=0;
var cont=0;
var total =0;
$(document).ready(function(){
	$('#cmdcerrar').click(function(event){cerrarDocumento();});  
	$('#cmdguardar').click(function(event){guardarDocumento();});
	$('#cmdguardarCantidad').click(function(event){guardarCantidadDetalles();});
	$('#cmdnuevo').click(function(event){limpiarControles();});
	//$('#cmdnuevoconcepto').click(function(event){limpiarConceptos();});
	$('#cmdguardardetalle').click(function(event){guardarDetalles();});
	 $("#txtfechadocumento").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
	 $('#img_producto').click(function(event){muestraProductos();});
	 $('#img_pedidos').click(function(event){muestraPedidos();});
	 $('#img_movimiento').click(function(event){muestraEntradas();});
	 $('#cbodependencia').change(function (event){cargaraAlmacenes($('#cbodependencia').val());});
	 
	 /*Configura los tabuladores*/
	 $('#tabuladores').tabs();
	 $('#tabuladores').tabs('option', 'selected', 0);
	 
	 if($('#ID_ENTRADA').attr('value')==''){
		$('#cmdguardar').attr('disabled',false);
		$('#cmdguardardetalle').attr('disabled',false);
		$('#tabuladores').tabs('option', 'disabled', [1]);
	}
	
	 getBeneficiarios('txtbeneficiario','ID_PROVEEDOR');
	 //getUnidad_Medidas('txtunidadmedida','CVE_UNIDAD_MEDIDA');
	 getProyectosEval('txtproyecto', 'ID_PROYECTO');
	 CargarDetalles($('#ID_ENTRADA').attr('value'));
	 validarMovimiento();
	 if($('#cbotipoentrada').val()=='1'){
		$('#cboiva').attr('disabled', true);
		$('#txtdescuento').attr('disabled', true);
		$('#txtiva').attr('disabled', true);
	}
	$('#ui-datepicker-div').hide();
});

function borrarEntrada2(){
	if($('#txtentrada2').attr('value')==''){
		$('#ID_ENTRADA2').attr('value', '');
		$('#txtentrada2').attr('value','');
	}
		
}

function __regresaEntrada(folio, ID_ENTRADA){
	$('#txtentrada2').attr('value', folio);
	$('#ID_ENTRADA2').attr('value', ID_ENTRADA);
	$.alerts._hide();
}

function validarMovimiento(){
	if($('#cbomovimiento').val()==0)
		$('#td_mov').hide();
	else if($('#cbomovimiento').val()==1)
			$('#td_mov').show();
		else
			$('#td_mov').show();
	if($('#txtentrada2').attr('value')!=''){
		$('#txtentrada2').attr('value', rellenaCeros($('#txtentrada2').attr('value').toString(),6));
	}
}

function getTotales(){
	total = 0;
	if($('#cbotipoentrada').val()=='2') subtotal=0;
	
	$("INPUT[@id='ID_DETALLE_ENTRADA'][type='hidden']").each(function(){ 
		if(typeof($('#txt'+$(this).val()).attr('value'))!='undefined'){ 												  
			subtotal = subtotal + ($('#txt'+$(this).val()).attr('value')*$('#DET_PRECIO'+$(this).val()).attr('value'));
			$('#divcosto'+$(this).val()).text('$'+formatNumber($('#txtcantidad'+$(this).val()).attr('value')*$('#DET_PRECIO'+$(this).val()).attr('value')));		
		}
	});
			
	if(isNaN($('#txtdescuento').attr('value'))){$('#txtdescuento').attr('value', '0.00'); return false;}
	var descuento = $('#txtdescuento').attr('value');
	
	/*Aplicacion del Iva si lo requiere*/
	var iva = 0.0;
	if($('#cboiva').attr('value')==0) $('#txtiva').attr('value', '0');
	if($('#cboiva').attr('value')==1) {
			iva = redondeo(subtotal*0.16);
			$('#txtiva').attr('value', iva);
	}
	if($('#cboiva').attr('value')==2){
		iva = ($('#txtiva').attr('value')*1);
		$('#txtiva').attr('value', iva);
	}
	
	/*Aplicar descuento si es requerido*/
	if($('#txtdescuento').attr('value')=='') $('#txtdescuento').attr('value', '0');
	if($('#txtdescuento').attr('value')!=''){
		if((subtotal - descuento) < 0 ) {jAlert('El monto del descuento no puede ser mayor al subtotal, vuelva a escribirlo','Advertencia'); $('#txtdescuento').attr('value', ''); $('#txtdescuento').focus(); return false;}
	}
	
	total = (subtotal-descuento) + iva;
	
	$('#div_subtotal').html('<strong>'+formatNumber(subtotal, '$')+'</strong>');
	$('#div_total').html('<strong>'+formatNumber(redondeo(total),'$')+'</strong>');

}

function getTotalesMasIva(){
	var iva_temp = 0.00;
	if($('#cboiva').attr('value')==1||$('#cboiva').attr('value')==2) {
		iva_temp = (1* $('#txtiva').attr('value'));
	}
	total = (subtotal-descuento) + iva_temp;
	$('#div_total').html('<strong>'+formatNumber(redondeo(total),'$')+'</strong>');
}


function guardarCantidadDetalles(){
	var exito = true;
	var ids = [];
	var cantidad = [];
	$('input[id=ID_DETALLE_ENTRADA][type="hidden"]').each(function() { 
		if(parseFloat($('#txt'+$(this).val()).attr('value'))>parseFloat($('#CANTIDAD_DET'+$(this).val()).attr('value')) || parseFloat($('#txt'+$(this).val()).attr('value'))==0){
			alert('La cantidad establecida en alguno de los lotes es mayor a la existente en el lote de Pedido');
			exito = false;
			return false;
		}
		else{
			if(typeof($('#txt'+$(this).val()).attr('value'))!='undefined'){
				ids.push($(this).val());
				cantidad.push(parseFloat($('#txt'+$(this).val()).attr('value')));
			}
		}
	});
	if(exito){
		var descuento = $('#txtdescuento').attr('value');
		var iva = $('#txtiva').attr('value');
		var tipoIva = $('#cboiva').val();
		ShowDelay('Guardando detalle de lotes','');
		controladorEntradasDocumentosRemoto.guardarCantidadDetalles($('#ID_ENTRADA').attr('value'),ids, cantidad, subtotal, descuento, iva, tipoIva,{
			  callback:function(items){
							//CargarDetalles($('#ID_ENTRADA').attr('value'));
							 CloseDelay('Detalles guardados con �xito', function(){document.location='captura_documentos.action?id_entrada='+$('#ID_ENTRADA').attr('value');});
							 
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString,'Error');   
				return false;
			}
		});	
	}
}

function cargaraAlmacenes(idDependencia) {
     dwr.util.removeAllOptions("cboalmacen");
	 controladorEntradasDocumentosRemoto.getAlmacenes(idDependencia, {
        callback:function(items) { 		
			dwr.util.addOptions('cboalmacen',{ 0:'[Seleccione]'});
			dwr.util.addOptions('cboalmacen',items,"ID_ALMACEN", "DESCRIPCION");
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(errorString,'Error');          
        }
    },async=false ); 
 }
 

function validarTipo(){
	var tipo = $('#cbotipodocumento').val();
	if(tipo==1){
			//$('#txtdocumento').attr('value','');
			//$('#txtdocumento').attr('disabled', false);
			//$('#txtpedido').attr('value','');
			//$('#txtpedido').attr('disabled', true);
			$('#txtproyecto').attr('value','');
			$('#txtproyecto').attr('disabled', false);
			$('#txtpartida').attr('value','');
			$('#txtpartida').attr('disabled', false);
			$('#txtbeneficiario').attr('value','');
			$('#txtbeneficiario').attr('disabled', false);
	}
	if(tipo==2){
			//$('#txtdocumento').attr('value','');
			//$('#txtdocumento').attr('disabled', true);
			//$('#txtpedido').attr('value','');
			//$('#txtpedido').attr('disabled', true);
			$('#txtproyecto').attr('value','');
			$('#txtproyecto').attr('disabled', true);
			$('#txtpartida').attr('value','');
			$('#txtpartida').attr('disabled', true);
			$('#txtbeneficiario').attr('value','');
			$('#txtbeneficiario').attr('disabled', true);
		}
}

function Redirect(id_entrada){
	document.location = '../consultas/entradas.action?ban=1&id_entrada='+id_entrada;
}

/*funcion para cerrar el documento*/
function cerrarDocumento(){
	if(cont==0) { jError('Imposible cerrar la entrada si no se han cargado por lo menos un lote de Pedido','Error'); return false;}
	jConfirm('�Confirma que desea cerrar la entrada de almacen actual?','Cerrar', function(r){
				if(r){
						  controladorEntradasDocumentosRemoto.cerrarEntradaDocumento($('#ID_ENTRADA').attr('value'),{
						  callback:function(items){ 	    
							  if(items!="") 
								jError('Error inesperado al cerrar el documento actual','Ha ocurrido un problema'); 
							  else{
									$('#cmdcerrar').attr('disabled',true);
									$('#cmdguardar').attr('disabled',true);
									$('#cmdguardardetalle').attr('disabled',true);
									CargarDetalles($('#ID_ENTRADA').attr('value'));
									CloseDelay('Entrada cerrada con �xito');
									Redirect($('#ID_ENTRADA').attr('value'));
									
										  
							  }
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');   
							return false;
						}
					});
				}																			   
	});
}




/*funcion para guardar los detalles*/
function guardarDetalles(){
	var v = validarDetalles();
	if(v) return false;
	
						var id_entrada = $('#ID_ENTRADA').attr('value');
						var id_detalle_entrada = $('#ID_DETALLE_ENTRADA').attr('value');
						var id_articulo = $('#ID_ARTICULO').attr('value');
						var id_unidad_medida =  $('#CVE_UNIDAD_MEDIDA').attr('value');

						  controladorEntradasDocumentosRemoto.guardarDetallesEntradaDocumentos(id_entrada, id_detalle_entrada, id_articulo, id_unidad_medida, $('#cbofamilia').attr('value'), $('#txtcantidad').attr('value'), $('#txtprecioestimado').attr('value'), $('#txtdescripcion').attr('value'),{
						  callback:function(items){ 	    
						  CargarDetalles($('#ID_ENTRADA').attr('value'));
						  limpiarConceptos();
						  if(items==false) 
						  	jError('Error inesperado al guardar el detalle del documento actual','Ha ocurrido un problema'); 
						  else
						  	CloseDelay('Detalles guardados con �xito');	  
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
							return false;
						}
					});
																					   
	
	
}


/*fucnion para eliminar conceptos*/
function eliminarConceptos(){
	var checkClaves = [];
     $('input[name=chkconcepto]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('&iquest;Confirma que desea eliminar los conceptos seleccionados?','Eliminar conceptos', function(r){
			if(r){
					 controladorEntradasDocumentosRemoto.eliminarConceptos(checkClaves, {
						callback:function(items) { 	
						limpiarConceptos();
						CargarDetalles($('#ID_ENTRADA').attr('value'));
						CloseDelay('Conceptos eliminados con �xito');
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
				    });
			}
	   });
	 
	 } 
	else 
	    jAlert('Es necesario que seleccione por lo menos un concepto de listado', 'Advertencia');
}

/*funcion para validar los detalles de los conceptos*/
function validarDetalles(){
	if($('#ID_ARTICULO').attr('value')==''||$('#ID_ARTICULO').attr('value')==0) {jAlert('Es necesesario seleccionar el <strong>art&iacute;culo</strong>','Advertencia'); return true;}
	if($('#CVE_UNIDAD_MEDIDA').attr('value')==''||$('#CVE_UNIDAD_MEDIDA').attr('value')==0) {jAlert('Es necesario selecionar la <strong>unidad de medida</strong> del articulo', 'Advertencia'); return true;}
	if($('#txtdescripcion').attr('value')=='') {jAlert('Es necesario teclear una <strong>descripcion</strong> para el art&iacute;culo', 'Advertencia'); return true;}
	if($('#txtcantidad').attr('value')==''){jAlert('Es necesario especificar la <strong>cantidad</strong> del art&iacute;culo','Advertencia'); return true;}
	if($('#txtprecioestimado').attr('value')==''){jAlert('Es necesario especificar el <strong>precio unitario</strong> unitario del art&iacute;culo', 'Advertencia'); return true;}
	return false;
}

function muestraEntradas(){
	var idDependencia = $('#cbodependencia').val();
	jWindow('<iframe width="650" height="400" name="consultaPedido" id="consultaPedido" frameborder="0" src="../consultas/muestra_entradas.action?idDependencia='+idDependencia+'"></iframe>','Listado de Entradas', '','Cerrar ',1);
}

/*funcion para mostrar el listado de pedidos de la direccion*/
function muestraPedidos(){
	var idDependencia = $('#cbodependencia').val();
	var tipo = $('#cbotipodocumento').val();
	
	if(idDependencia==0||idDependencia=="") {jAlert('Es necesario seleccionar la Unidad Administrativa para listar los Pedidos'); return false;}
	swal({
		  title: 'Listado de Pedidos disponibles',
		  text: 'Seleccione la factura que desea comprobar',
		  html:
			  '<iframe width="650" height="400" name="consultaPedido" id="consultaPedido" frameborder="0" src="../consultas/muestra_pedidos.action?idDependencia='+idDependencia+'"></iframe>',
		  width: 800,
		  padding: 10,
		  animation: false
		})
	
}

/*funcion para mostrar el listado de productos*/
function muestraProductos(){	
	if($('#txtpartida').attr('value')=='') {jAlert('Es necesario especificar la partida para mostrar el listado de art�culos'); return;}
	__listadoProductos($('#txtproducto').attr('value'), $('#txtpartida').attr('value'));
}

/*Funcion para capturar un nuevo concepto*/
function limpiarConceptos(){
	$('#ID_DETALLE_ENTRADA').attr('value', '');
	$('#ID_ARTICULO').attr('value', '');
	$('#CVE_UNIDAD_MEDIDA').attr('value', '');
	$('#cbofamilia').val(0);
	$('#txtproducto').attr('value', '');
	$('#txtunidadmedida').attr('value', '');
	$('#txtdescripcion').attr('value', '');
	$('#txtcantidad').attr('value', '');
	$('#txtprecioestimado').attr('value', '');
}

/*Reestablece los controles del formulario*/
function limpiarControles()
{
	$('#ID_ENTRADA').attr('value', '');
	$('#ID_DETALLE_ENTRADA').attr('value', '');
	$('#ID_PEDIDO').attr('value','');
	$('#ID_PROVEEDOR').attr('value', '');
	$('#CVE_UNIDAD_MEDIDA').attr('value', '');
	$('#ID_ARTICULO').attr('value', '');
	$('#cboalmacen').val(0);
	$('#txtpedido').attr('value','');
	$('#txtdocumento').attr('value', '');
	$('#cbotipodocumento').val(0);
	$('#txtfechadocumento').attr('value', '');
	$('#txtproyecto').attr('value', '');
	$('#txtpartida').attr('value', '');
	$('#txtbeneficiario').attr('value', '');
	$('#txtobservacion').attr('value', '');
	$('#cbofamilia').val(0);
	
	$('#txtproducto').attr('value', '');
	$('#txtunidadmedida').attr('value', '');
	$('#txtdescripcion').attr('value', '');
	$('#txtcantidad').attr('value', '');
	$('#txtprecioestimado').attr('value', '');
	$('#div_numEntrada').html("");
	quitRow("listasConceptos");
}

/*funcion que permite obtener los datos del listado de pedidos*/
function __regresaPedido(num, id, id_proyecto, programa, clv_partid, clv_benefi, ncomercia, notas)
{
	$('#txtpedido').attr('value', num);
	$('#ID_PEDIDO').attr('value', id);
	$('#ID_PROYECTO').attr('value', id_proyecto);
	$('#txtproyecto').attr('value', programa+'['+id_proyecto+']');
	$('#txtpartida').attr('value', clv_partid);
	$('#ID_PROVEEDOR').attr('value',clv_benefi);
	$('#txtbeneficiario').attr('value', ncomercia);
	$('#txtobservacion').attr('value', notas);
	$('#txtdocumento').focus();
	$.alerts._hide();
}

/*funcion que permite guardar un documento*/
function guardarDocumento(){
	var v = validar();
	if(v) return false;
	jConfirm('�Confirma que desea guardar el documento?','Guardar documento', function(r){
				if(r){
						var id_entrada = $('#ID_ENTRADA').attr('value');
						var id_almacen = $('#cboalmacen').attr('value');
						var id_dependencia = $('#cbodependencia').attr('value');
						var id_proveedor =  $('#ID_PROVEEDOR').attr('value');
						var id_proyecto = $('#ID_PROYECTO').attr('value');
						var id_tipo_documento = $('#cbotipodocumento').attr('value');
						var id_tipo_entrada = $('#cbotipoentrada').attr('value');
						var movimiento = $('#cbomovimiento').val();
						var id_entrada2 = $('#ID_ENTRADA2').attr('value');
						if(id_entrada2=='') id_entrada2 = 0;
						var descuento = $('#txtdescuento').attr('value');
						var iva = $('#txtiva').attr('value');
						var tipoIva = $('#cboiva').val();
						var tipoEfecto = parseInt($('#cbotipoEfecto').val());
						  controladorEntradasDocumentosRemoto.guardarEntradaDocumento(id_entrada, id_dependencia, id_almacen, id_proveedor, $('#txtpedido').attr('value'), id_tipo_documento, $('#txtdocumento').attr('value'), id_proyecto, $('#txtpartida').attr('value'), $('#txtobservacion').attr('value'), $('#txtfechadocumento').attr('value'), id_tipo_entrada, subtotal, descuento, iva, tipoIva, tipoEfecto, movimiento, id_entrada2, {
						  callback:function(items){  
						  	  getFolioEntrada(items);
							  document.location = 'captura_documentos.action?id_entrada='+items;	 
							  $('#ID_ENTRADA').attr('value',items);
							  //$('#div_numEntrada').html(rellenaCeros(items.toString(),6));
							  /*$('#cmdcerrar').attr('disabled',false);
							  $('#tabuladores').tabs('enable',1);
							  $('#tabuladores').tabs('option', 'selected', 1);
							  CargarDetalles($('#ID_ENTRADA').attr('value'));*/
							  CloseDelay('Se ha guardado satisfactoriamente');
							   
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
							return false;
						}
					});
				}																			   
	});
}


function getFolioEntrada(id_entrada){
	controladorEntradasDocumentosRemoto.getFolioEntrada(id_entrada,{
						  callback:function(items){  
							  $('#div_numEntrada').html(items);
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
							return false;
						}
					});
}
/*funcion para validar los datos del formulario*/
function validar(){
	if($('#cbodependencia').attr('value')=='0') {jAlert('Es necesiario seleccionar una <strong>Unidad Administrativa</strong>', 'Advertencia'); return true;}
	if($('#txtdocumento').attr('value')==''&&$('#cbotipodocumento').val()==1) {jAlert('Es necesario escribir el <strong>n&uacute;mero de documento</strong>', 'Advertencia'); return true;}
	if($('#cbotipodocumento').attr('value')=='0'){jAlert('Es necesario seleccionar el <strong>tipo de documento</strong>', 'Advertencia'); return true;}
	if($('#cbotipoentrada').attr('value')=='0'){jAlert('Seleccione un tipo de entrada', 'Advertencia'); return true;}

	if($('#cbomovimiento').val()!=0&&$('#ID_ENTRADA2').attr('value')=='')
		{jAlert('El movimiento de entrada no es valido, seleccione de la lista','Advertencia'); return false;}
	
		
	if($('#txtfechadocumento').attr('value')==''){jAlert('Es necesario escribir la fecha del documento', 'Advertencia'); return true;}
	if($('#txtobservacion').attr('value')=='') {jAlert('Es necesario por lo menos una <strong>descripci&oacute;n</strong>','Advertencia'); return true;}
	//alert('Pedido nuevo: '+$('#ID_PEDIDO').attr('value')+' Pedido ant: '+$('#ID_PEDIDO_ANT').attr('value'));
	if($('#ID_PEDIDO_ANT').attr('value')!=""&$('#ID_PEDIDO').attr('value')!=$('#ID_PEDIDO_ANT').attr('value')){alert('El pedido que intenta guardar es diferente al guardado actualmente, si continua la informacion del pedido anterior sera reemplazada por el nuevo pedido');}
	return false;
}

/*funcion para buscar un concepto y editarlo*/
function editarConcepto(id_detalle_entrada){
	controladorEntradasDocumentosRemoto.getConcepto(id_detalle_entrada,{
				callback:function(Map){
					$('#ID_DETALLE_ENTRADA').attr('value', id_detalle_entrada);
					$('#ID_ARTICULO').attr('value',Map.ID_ARTICULO);
					$('#CVE_UNIDAD_MEDIDA').attr('value',Map.ID_UNIDAD_MEDIDA);
					$('#cbofamilia').val(Map.ID_FAMILIA);
					$('#txtproducto').attr('value', Map.ARTICULO);
					$('#txtunidadmedida').attr('value', Map.UNIDMEDIDA)
					$('#txtprecioestimado').attr('value', Map.PRECIO);
					$('#txtcantidad').attr('value', Map.CANTIDAD);
					if(Map.DESCRIPCION!=null) $('#txtdescripcion').attr('value', Map.DESCRIPCION);
					else $('#txtdescripcion').attr('value','');
				}
				,
				errorHandler:function(errorString, exception) { 
				jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
				return false;
			}
				
	});
}

/*funcion para cargar los detalles del pedido*/
function CargarDetalles(id_entrada)
{
	subtotal = 0;
	total =0;
	cont=0;
	quitRow('listasConceptos');
	if(id_entrada!=""&&id_entrada!=0)
	{
		controladorEntradasDocumentosRemoto.getConceptos(id_entrada,{
				callback:function(items) { 										 
				jQuery.each(items,function(i){	
						cont++;
						if(this.CANTIDAD_PED>0) pintaTablaDetalles('listasConceptos', this);
						if(items.length==cont){getTotales();}
						if(items.length==cont&&$('#cbotipoentrada').val()=='2'&&this.CANTIDAD==0) jAlert('Las cantidades de los lotes aun no se han guardado, pulse el bot�n <strong>Guardar cambios</strong> cuando haya terminado de asignarlas', 'Advertencia');
					});
			    },
				errorHandler:function(errorString, exception) { 
							jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
							return false;
				}
		});
	}
}

/*Funcion para pintar las filas de los conceptos*/
function pintaTablaDetalles(table, obj){
	var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editarConcepto("+obj.ID_DETALLE_ENTRADA+")\">";
	var htmlCheck = '<input type="checkbox" name="chkconcepto" id="chkconcepto" value="'+obj.ID_DETALLE_ENTRADA+'">';
	var htmltxt = '';
	htmlEdit = "";
	if($('#cbotipoentrada').val()=='2'){ //entrega parcial
		htmltxt = '<input type="text" name="txt'+obj.ID_DETALLE_ENTRADA+'" id="txt'+obj.ID_DETALLE_ENTRADA+'" value="'+((obj.CANTIDAD==0)? obj.CANTIDAD_PED: obj.CANTIDAD)+'" style="width:80px; text-align:center" onKeyPress="return keyNumbero(event);" onBlur="getTotales()">' + 
		'<input type="hidden" id="ID_DETALLE_ENTRADA" name="ID_DETALLE_ENTRADA"  value="'+obj.ID_DETALLE_ENTRADA+'"/>' + 
		'<input type="hidden" id="DET_PRECIO'+obj.ID_DETALLE_ENTRADA+'" name="DET_PRECIO'+obj.ID_DETALLE_ENTRADA+'"  value="'+obj.PRECIO+'"/>' +
		'<input type="hidden" id="CANTIDAD_DET'+obj.ID_DETALLE_ENTRADA+'" name="CANTIDAD_DET'+obj.ID_DETALLE_ENTRADA+'"  value="'+obj.CANTIDAD_PED+'"/>';
		$('#tb_boton').show();
	}
	else{
			htmltxt = '&nbsp;'+obj.CANTIDAD;
			$('#tb_boton').hide();
	}
				
	if(obj.STATUS==0&&$('#cbotipoentrada').val()!='2') {htmlEdit = "";htmlCheck="";}
	if(obj.ID_PED_MOVTO!=null&&obj.ID_PED_MOVTO!="") {
		$('#cmdguardardetalle').attr('disabled', true); 
		$('#tr_subtotal').show();
		//$('#txtiva').attr('value',obj.IVA);
		//$('#txtdescuento').attr('value',obj.DESCUENTO);
	
		subtotal +=  obj.CANTIDAD * obj.PRECIO;
		$('#SUBTOTAL').attr('value', subtotal);
		$('#div_subtotal').html('<strong>'+formatNumber(subtotal, '$')+'</strong>');
		
	}
	appendNewRow(table, [Td('', centro , '', htmlCheck),
						 Td('', centro , '', htmltxt),
						 Td('', centro , '', '&nbsp;'+obj.CANTIDAD_PED),
						 Td('', centro , '', '&nbsp;'+obj.UNIDMEDIDA),
						 Td('', izquierda , '', '&nbsp;'+obj.ARTICULO+"("+getHTML(obj.DESCRIPCION)+")"),
						 Td('', izquierda , '','&nbsp;'+getHTML(obj.FAMILIA)),
				 		 Td('', derecha , '', formatNumber(obj.PRECIO, '$')+"&nbsp;")
						 
				]);
	
	
	
}