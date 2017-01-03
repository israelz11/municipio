var subtotal =0;
var total =0;
var indices = [];
$(document).ready(function() {
	$('#checkall').click(function (event){setCheckAll('chkconcepto');});
	$('#cmdguardarPedido').click(function (event){guardarPedido();});
	$('#cmdguardarPedido2').click(function (event){guardarPedido();});
	$('#cmdborrarConceptos').click(function (event){borrarLotes()});
	$('#cmdcerrar').click(function(event){cerrarPedido();});
	$('#cmdenviarPedido').click(function(event){enviarLotesPedido();});;
	$("#txtfecha").datepicker({showOn: 'button', buttonImage:"../../imagenes/cal.gif" , buttonImageOnly: true,dateFormat: "dd/mm/yy"});
	 
	 getBeneficiarios('txtprestadorservicio','CVE_BENEFI',$('#tipoBeneficiario').attr('value'));
	 $('#checkall').attr('checked', true);
	 setCheckAll('chkconcepto');
	 //Configura los tabuladores
	 $('#tabuladores').tabs();
	 $('#tabuladores').tabs('enable',0);
	 if($('#CVE_PED').attr('value')=='0'||$('#CVE_PED').attr('value')=='') $('#cboiva').val(1);
	 $('#txtcontrato').focus();
	 
	 $('#ui-datepicker-div').hide();
});

/*funcion para guardar el pedido*/
function guardarPedido(){
	var error="";
 	var titulo ='Advertencia - Informacion no válida';
	if($('#CVE_REQ').attr('value')==''||$('#CVE_REQ').attr('value')=='0') error=error+'No hay ninguna requisicion para el pedido actual</br>';
	if($('#txtfecha').attr('value')=='') error=error+'Es necesario establecer la fecha del pedido</br>','Advertencia</br>';
	if($('#txtfechaentrega').attr('value')=='') error=error+'Es necesario establecer la fecha de entrega</br>';
	if($('#CVE_BENEFI').attr('value')==0||$('#CVE_BENEFI').attr('value')=="") error=error+'Es necesario selecionar el beneficiario</br>';
	if($('#txtcondicionespago').attr('value')=='') error=error+'Es necesario escribir las condiciones de pago</br>';
	var checks = [];
	$('input[id=chkconcepto]:checked').each(function() 
		{ 
			checks.push($(this).val()); 
			if($('#txtpreciounit'+$(this).val()).attr('value')==''||$('#txtpreciounit'+$(this).val()).attr('value')=='0')
			{error=error+"El precio de algún lote en el pedido no es válido, verifique y vuelva a intentar esta operación</br>";}
			
	});	
	
	if(checks.length==0 ) error=error+'Es necesario seleccionar un lote de la requisición</br>';	
	if (error == ""){
		jConfirm('¿Confirma que desea guardar la informacion del pedido?','Confirmar', function(r){
			if(r){
				_guardarPedido();
			}
		});
	} else
	 jAlert(error,titulo);	
	return false;
}
/**/

function getEnter(n, e){
	var j=0;
	if(e.keyCode == 13)	{
		for(j=0;j < indices.length; j++){
			
			if(n==indices[j]){
				$('#txtpreciounit'+(indices[j+1])).focus();
				$('#txtpreciounit'+(indices[j+1])).select();
				e.keyCode =0;
				return false;
			}
			e.keyCode =0;
		}
	}
}

/*Metodo para cerrar un pedido*/
function cerrarPedido(){
	var contador = 0;
	$('input[id=chkconcepto]:checked').each(function() 
		{ contador++;});	
	if(contador==0) {jAlert('Para poder cerrar el pedido es necesario que exista por lo menos un lote', 'Advertencia'); return false;}
	if(isNaN($('#txtiva').attr('value'))) {jAlert('La cantidad especificada en el IVA no es válida', 'Advertencia'); return false;} 
	jConfirm('¿Confirma que desea cerrar el pedido?', 'Confirmar', function (r){
				if(r){
					_cerrarPedido($('#CVE_PED').attr('value'), $('#txtiva').attr('value'));
				}
			});
}

/*Metodo interno para el cierra del pedido*/
function _cerrarPedido(cve_ped, iva){
	ShowDelay('Cerrando pedido','');
	controladorPedidos.cerrarPedido(cve_ped, $('#TIPO_REQ').attr('value'), iva,{
						callback:function(items){
							
								CloseDelay('Pedido cerrado con éxito', 2000, function(){
										$('#cmdcerrar').attr('disabled', true);
										$('#cmdguardarPedido').attr('disabled', true);
										getReportePedido($('#CVE_PED').attr('value'));
										document.location='lst_pedidos.action';
									});
							
						},
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');   
							return false;
						}
					});
}

/*Metodo para mostrar enviar lotes a otro pedido*/
function enviarLotesPedido(){
	var html = "";
	controladorPedidos.getComboPedidosRequisicion($('#CVE_REQ').attr('value'),$('#CVE_PED').attr('value'), {
		  callback:function(i){
				html = '<table width="350" border="0" cellspacing="0" cellpadding="0" align="center">' +
					   '<tr>' +
					   '<td height="27" align="center"><span class="TextoNegritaTahomaGris">Seleccione el n&uacute;mero de pedido:</span> '+ i+'</td>' +
					   '</tr>' +
					   '<tr>' +
					   '<td height="44" align="center">&nbsp;<input type="button" value="Exportar" id="cmdEnviarLotes" onClick="_enviarLotesPedido();" class="botones"/>&nbsp;' +
					   '<input type="button" value="Cancelar" id="cmdborrarConceptos" class="botones" onClick="$.alerts._hide();"/></td>' +
					   '</tr>'+
					   '</table>';	
				jWindow(html,'Exportar lotes a pedido', '','',0);
		  }
	});
}

/*Metodo para enviar los lotes seleccionados a otro pedido*/
function _enviarLotesPedido(){
	if($('#cbopedidos').attr('value')=='0') {alert('En número de pedido no es válido, seleccione un pedido');return false;}
	var id_ped_movto = [];
	var cve_ped_dest = $('#cbopedidos').attr('value');
	$('input[id=chkconcepto]:checked').each(function() { 
		id_ped_movto.push($(this).val());
	});
	$.alerts._hide();
	jConfirm('¿Confirma que desea exportar los lotes seleccionados al pedido <strong>'+cve_ped_dest+'</strong>?', 'Confirmar', function(r){
				if(r){
					ShowDelay('Exportando lotes','');
					controladorPedidos.moverLotes(id_ped_movto, $('#CVE_PED').attr('value'), cve_ped_dest, $('#txtdescuento').attr('value'), {
												  callback:function(i){
													  		CloseDelay('Lotes exportados con exito', 2000, function(){
																document.location='capturarPedidos.action?cve_ped='+$('#CVE_PED').attr('value');
															});
													  		
												  }
									});
				}
		});
}

/*Metodo para mostrar el reporte PDF del pedido*/
function getReportePedido(clavePed) {
	$('#clavePedido').attr('value',clavePed);
	$('#forma').attr('target',"impresion");
	$('#forma').submit();
	$('#forma').attr('target',"");
}

/*funcion que permite guardar fisicamente el pedido*/
function _guardarPedido(){
	var checks = [];
	var notas = [];
	var precio_unit = [];
	var cantidad = [];
	var num_ped = $('#cve_pedido_text').attr('value');
	$('input[id=chkconcepto]:checked').each(function() { 
		checks.push($(this).val());
		notas.push($('#txtnota'+$(this).val()).attr('value'));
		precio_unit.push($('#txtpreciounit'+$(this).val()).attr('value'));
		cantidad.push($('#txtcantidad'+$(this).val()).attr('value'));
	});
	
	ShowDelay('Guardando pedido','');
	controladorPedidos.guardarPedido($('#CVE_PED').attr('value'), $('#CVE_REQ').attr('value'), $('#txtfecha').attr('value'), $('#txtcontrato').attr('value'), $('#txtconcurso').attr('value'), $('#txtfechaentrega').attr('value'), $('#CVE_BENEFI').attr('value'), $('#txtcondicionespago').attr('value'), $('#txtlugarentrega').attr('value'),$('#txtdescripcion').attr('value'), checks, cantidad, notas, precio_unit, $('#txtiva').attr('value'), $('#cboiva').val(), $('#txtdescuento').attr('value'),{
			  callback:function(items){
				  		if(items.EVENT==true){
							$('#CVE_PED').attr('value',items.CVE_PED);
						  	$('#cve_pedido_text').text(items.NUM_PED);
							var cve = items.CVE_PED;
							 CloseDelay('Pedido guardado con éxito', function(){
										document.location='capturarPedidos.action?cve_ped='+$('#CVE_PED').attr('value');
								 });
						    
						}
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>', 'Error al guardar Pedido');   
			return false;
		}
	});	
}

/*funcion para habilitar los elementos del concepto*/
function habilitarConcepto(checked, id, bol){
	var valor =0;
	$('#cmdenviarPedido').attr('disabled', !checked);
	$('#txtenviarpedido').attr('disabled', !checked);
	$('#cmdguardarPedido').attr('disabled', false);
	$('#txtcantidad'+id).attr('disabled', !checked);
	$('#txtnota'+id).attr('disabled', !checked);
	$('#txtpreciounit'+id).attr('disabled', !checked);
	
	$("INPUT[@id='chkconcepto'][type='checkbox']").each(function(){ if($(this).val()!=0){ 														   
					if($(this).attr('checked')){
						valor++;
					}
				}
			});
	
	if(valor>=1) {
		$('#cmdenviarPedido').attr('disabled', false);
		$('#txtenviarpedido').attr('disabled', false);
	}
	else
	{
		$('#cmdenviarPedido').attr('disabled', true);
		$('#txtenviarpedido').attr('disabled', true);
	}
	
	calculatTotal(id, $('#txtcantidad'+id).attr('value'), $('#txtpreciounit'+id).attr('value'), checked); 
	
	if(!bol) getTotales();
}

/*Funcion para eleccionar todos los check del listado*/
function setCheckAll(check){	
	$("INPUT[@name='"+check+"'][type='checkbox']").attr('checked', $('#checkall').is(':checked'));
	$("INPUT[@id='"+check+"'][type='checkbox']").each(function(){ if($(this).val()!=0){															 
					habilitarConcepto($('#checkall').attr('checked'), $(this).val(), true);
				}
			});
	if(!$('#checkall').attr('checked')) {subtotal =0; $('#cmdguardarPedido').attr('disabled', true);}
	getTotales();
}

/*funcion para calcular el total del concepto*/
function calculatTotal(id, cantidad, precio, check){
	if(check) {
		$('#divcosto'+id).text('$'+formatNumber(cantidad*precio)+' ');
	}
	else{
		$('#divcosto'+id).text('$ 0.00 ');
	}
}

/*funcion para calucar subtotales y totales*/
function getTotales(){
	subtotal =0;
	total = 0;
	var valor = 0;
	var i =0;
	var tablita = "";
	$("INPUT[@id='chkconcepto'][type='checkbox']").each(function(){ if($(this).val()!=0){ 														   
					if($(this).attr('checked')){
						
						subtotal = subtotal + ($('#txtcantidad'+$(this).val()).attr('value')*$('#txtpreciounit'+$(this).val()).attr('value'));
						$('#divcosto'+$(this).val()).text('$'+formatNumber($('#txtcantidad'+$(this).val()).attr('value')*$('#txtpreciounit'+$(this).val()).attr('value')));
						if(isNaN($('#txtpreciounit'+$(this).val()).attr('value'))) valor++;
					}
				}
			});
	/*Comprueba valores numericos*/
	$("INPUT[@id='chkconcepto'][type='checkbox']").each(function(){ if($(this).val()!=0){ 														   
					if($(this).attr('checked')){
						if(isNaN($('#txtpreciounit'+$(this).val()).attr('value'))) {
							tablita = tablita +   '<tr>'+
													'<td align="center">'+$('#Lote'+$(this).val()).attr('value')+'</td>'+
													'<td align="right">'+$('#txtpreciounit'+$(this).val()).attr('value')+'&nbsp;</td>'+
												  '</tr>';
							i++;
							$('#txtpreciounit'+$(this).val()).attr('value', '');				
						}
						if(valor==i&&valor!=0) 
							jAlert("Hay valores numericos no válidos en precios unitarios, quite los separadores de Miles(<strong>,</strong>) o simbolos especiales(¿?-/*@#$%&_;:<>{}...) .Los lotes afectados son los siguientes:<br><br><table align='center' height='22' class='listas' width='243' border='0' cellspacing='0' cellpadding='0'><tr><th width='89' align='center'>Lote</td><th width='154' align='right'>Precio Unitario&nbsp;</td>  </tr>"+tablita+"</table>","Advertencia");	
						
					}
				}
			});
	
	$('#divsubtotal').text('$'+formatNumber(subtotal));
	
	if(isNaN($('#txtdescuento').attr('value'))){jAlert('El valor númerico del descuento no es valido, vuelva a escribirlo', 'Advertencia'); return false;}
	var descuento = $('#txtdescuento').attr('value');
	
	/*Aplicacion del Iva si lo requiere*/
	var iva = 0.0;
	if($('#cboiva').attr('value')==0) $('#txtiva').attr('value', '0');
	if($('#cboiva').attr('value')==1) {
		//if($('#CVE_PED').attr('value')=='') {
			iva = redondeo(subtotal*0.16);
			$('#txtiva').attr('value', iva);
		//}
		/*else if($('#txtiva').attr('value')=='0'||$('#txtiva').attr('value')==''&&$('#CVE_PED').attr('value')!='') {
			iva = redondeo(subtotal*0.16);
			$('#txtiva').attr('value', iva);
		}
		else{
			iva = ($('#txtiva').attr('value')*1);
			$('#txtiva').attr('value', iva);
		}*/
	}
	if($('#cboiva').attr('value')==2){
		iva = ($('#txtiva').attr('value')*1);
		$('#txtiva').attr('value', iva);
	}
		
	
	
	/*Aplicar descuento si es requerido*/
	if($('#txtdescuento').attr('value')=='') $('#txtdescuento').attr('value', '0');
	if($('#txtdescuento').attr('value')!=''){
		if((subtotal - descuento) < 0 ) {jAlert('El monto del descuento no puede ser mayor al subtotal, vuelva a escribirlo','Advertencia'); $('#txtdescuento').attr('value', ''); $('#txtdescuento').focus(); return false;}
		subtotal = subtotal - descuento;
		total = subtotal + iva;
	}
	else 
		total = subtotal + iva;
		
	$('#divtotal').text('$'+formatNumber(redondeo(total)));
	/*
	var n = 1.7777;    
	n.round(2); // 1.78
	*/
}

function getTotalesMasIva(){
	var iva_temp = 0.00;
	if($('#cboiva').attr('value')==1||$('#cboiva').attr('value')==2) {
		//if($('#txtiva').attr('value')=='0'||$('#txtiva').attr('value')=='') 
			iva_temp = (1* $('#txtiva').attr('value'));
		//else 
		//	iva_temp = ($('#txtiva').attr('value')*1)+subtotal;
	}
	total = subtotal + iva_temp;
	$('#divtotal').text('$'+formatNumber(redondeo(total)));
}

/*funcion para obtener un arreglo de los ids seleccionados*/
function getCheckValues(check){
	var values = Array();
	$("INPUT[@id='"+check+"']:checked").each(function(){ if($(this).val()!=0) values.push($(this).val());});
	return values.join(",");
}


/*funcion para borrar los conceptos seleccionados*/
function borrarLotes(){
	var checkConceptos = [];
     $('input[name=chkconcepto]:checked').each(function() {checkConceptos.push($(this).val());});	
  	 var cve_ped = $('#CVE_PED').attr('value');
	 var cve_req = $('#CVE_REQ').attr('value');
	 if (checkConceptos.length>0){
		jConfirm('¿Confirma que desea eliminar los lotes seleccionados del pedido?','Confirmar', function(r){
				if(r){
				 ShowDelay('Eliminando lotes del pedido','');
				 controladorPedidos.eliminarLotesPedido(checkConceptos, cve_ped, $('#txtdescuento').attr('value'), {
					callback:function(items) {
						CloseDelay('Lotes eliminados con éxito', 2000, function(){
								 mostrarTablaLotes($('#CVE_PED').attr('value'));
							});
		
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					}
				});
	   }});
	 } 
	else 
	    jAlert('Es necesario que seleccione por lo menos un lote del listado', 'Advertencia');
		
}

/*funcion para mostrar el listado de conceptos*/
function mostrarTablaLotes(cve_ped){
	var cont =0;
	var total = 0;
	quitRow("listasConceptos");
	controladorPedidos.getConceptos(cve_ped, {
						   callback:function(items) { 
						   		jQuery.each(items,function(i){
									 cont++;
									 pintaTablaLotes('listasConceptos', this);				   
									 if(items.length==cont) pintarTotalConceptos('listasConceptos', cve_ped);
								});
						   }
	});
	
}

/*funcion para pintar las filas de los subtotales*/
function pintarTotalConceptos(table, cve_ped){
	/*Primera fila*/
	//appendNewRow(table, [Td('', izquierda, '', '<input type="button" value="Borrar lotes" id="cmdborrarConceptos" class="botones" onClick="borrarLotes()" />&nbsp;<input type="button" value="Enviar lotes a otro pedido" id="cmdenviarPedido" class="botones" onClick="enviarLotesPedido()" disabled/>', 6),
	//					 ]);
}

/*Funcion para pintar las filas de los conceptos*/
function pintaTablaLotes(table, obj){
	/*Filas de los conceptos*/
	appendNewRow(table, [Td('', centro , '', '<input type="checkbox" onClick="habilitarConcepto(this.checked, '+obj.ID_PED_MOVTO+')" name="chkconcepto" id="chkconcepto" value="'+obj.ID_PED_MOVTO+'">'),
						 Td('', centro , '', '<input type="hidden" value="'+obj.PED_CONS+'" id="Lote'+obj.PED_CONS+'">'+obj.PED_CONS),
				 		 Td('', centro , '', '<input type="text" class="input" style="width:100%;text-align:center" onBlur="getTotales()" disabled value="'+obj.CANTIDAD+'" id="txtcantidad'+obj.ID_PED_MOVTO+'">'),
						 Td(obj.UNIDMEDIDA, centro , '', ''),
						 Td('', centro , '', '<textarea rows="3" class="textarea" maxlength="300" style="width:99%" disabled id="txtnota'+obj.ID_PED_MOVTO+'">'+obj.DESCRIP+'</textarea>'),
						 Td('$ '+obj.PRECIO_EST, derecha, '', ''),
						 Td('', centro , '', '<input type="text" onBlur="getTotales()" class="input" style="width:95%; text-align:right; padding-right:5px" disabled value="'+formatNumber(obj.PRECIO_ULT, '')+'" id="txtpreciounit'+obj.ID_PED_MOVTO+'">'),
						 Td('', derecha, '', '<div align="right" id="divcosto'+obj.ID_PED_MOVTO+'">'+formatNumber((obj.CANTIDAD*obj.PRECIO_ULT), '$')+'</div>')]);
}

