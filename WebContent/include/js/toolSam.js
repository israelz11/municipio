var centro =   "text-align:center";
var derecha =  "text-align:right";
var izquierda = "text-align:left";
var row_color = "";




/*funcion para obtener y seleccionar un contratos en los diversos modulos*/
/*function getcontratoDocumento(numero, clave, tipo_gto, proyecto, clv_partid, clv_benefi){
	$('#CVE_CONTRATO').attr('value', clave);
	$('#txtnumcontrato').attr('value', numero);
	$('#CCLV_PARBIT').attr('value', tipo_gto);
	$('#CPROYECTO').attr('value', proyecto);
	$('#CCLV_PARTID').attr('value', clv_partid);
	$('#CLV_PARBIT').attr('value', tipo_gto);
	$('#CCLV_BENEFI').attr('value', clv_benefi);
	$('#img_quitar_contrato').attr('src', '../../imagenes/cross.png');
	contrato = true;
	_closeDelay();
	$('#txtnumcontrato').focus();
}*/

function mostrarOpcionPDF(cve_op){
	var html = '<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="405" >'+
				'  <tr id="x1" onmouseover="color_over(\'x1\')" onmouseout="color_out(\'x1\')"> '+
				'	<td width="33" height="27" align="center" style="cursor:pointer" onclick="getReporteOP('+cve_op+')"> '+
				'	  <img src="../../imagenes/pdf.gif"/></td>' +
				'	<td width="362" height="27" align="left" style="cursor:pointer" onclick="getReporteOP('+cve_op+')">&nbsp;Reporte Normal Orden de Pago</td> '+
				'  </tr> '+
				
				'  <tr id="x2" onmouseover="color_over(\'x2\')" onmouseout="color_out(\'x2\')" onclick=""> '+
				'	  <td height="27" align="center"  style="cursor:pointer" onclick="getAnexosListaOP('+cve_op+')"><img src="../../imagenes/report.png" /></td> '+
				'	  <td height="27" align="left" style="cursor:pointer" onclick="getAnexosListaOP('+cve_op+')">&nbsp;Listar Anexos de Orden de Pago</td> '+
				'	</tr> ';
			html+='</table>';
	jWindow(html,'Opciones de Reporte Orden de Pago', '','Cerrar',1);
}

function muestraVales(){
	var clv_benefi = $('#xClaveBen').attr('value');
	var tipo_gto = $('#tipoGasto').val();
	var tipo_doc  = $('#cbotipo').val();
	var idDependencia = $('#cbodependencia').val();
	
	if(typeof(clv_benefi)=='undefined') clv_benefi =0;
	if(typeof(tipo_gto)=='undefined') tipo_gto =0;
	if(typeof(tipo_doc)=='undefined') tipo_doc =0;
	if(typeof(idDependencia)=='undefined') idDependencia =0;
	
	if($('#txtvale').attr('value')=='') $('#CVE_VALE').attr('value', 0);
	jWindow('<iframe width="750" height="350" name="ventanaVales" id="ventanaVales" frameborder="0" src="../../sam/consultas/muestra_vales.action?idVale='+$('#CVE_VALE').attr('value')+'&idDependencia='+idDependencia+'&tipo_gto='+tipo_gto+'&clv_benefi='+clv_benefi+'&tipo_doc='+tipo_doc+'"></iframe>','Listado de Vales disponibles', '','Cerrar',1);
}

/*function muestraValesx(){
	var idVale = $('#CVE_VALE').attr('value');
	var tipo_gto = $('#tipoGasto').val();
	if(typeof tipo_gto=='undefined') tipo_gto ="";
	if($('#txtproyecto').attr('value')==''||$('#txtpartida').attr('value')=='')
		$('#ID_PROYECTO').attr('value', '0');
		idUnidad =0;
	__listadoPresupuestoVale($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'), $('#tipoGasto').attr('value'), idUnidad, idVale);
}*/


function muestraContratos(){
	var idDependencia = $('#unidad2').attr('value');
	var num_contrato = $('#txtnumcontrato').attr('value');
	var tipo_gto = $('#tipoGasto').val();
	if(typeof tipo_gto=='undefined') tipo_gto ="";
	if(typeof idDependencia=='undefined') idDependencia = null;
	jWindow('<iframe width="800" height="400" name="CONTRATO" id="CONTRATO" frameborder="0" src="../../sam/consultas/muestra_contratos.action?idDependencia='+idDependencia+'&tipo_gto='+tipo_gto+'&num_contrato='+num_contrato+'"></iframe>','Listado de Contratos Disponibles', '','Cerrar',1);
}


function removerVale(){
	$('#CVE_VALE').attr('value', '0');
	$('#txtvale').attr('value', '');
	$('#img_quitar_vale').attr('src', '../../imagenes/cross2.png');
}

/*funcion para remover los elementos de un contrato*/
function removerContrato(){
	$('#CVE_CONTRATO').attr('value', '0');
	$('#txtnumcontrato').attr('value', '');
	$('#CCLV_PARBIT').attr('value', '');
	$('#CPROYECTO').attr('value', '');
	$('#CCLV_PARTID').attr('value', '');
	$('#CCLV_BENEFI').attr('value', '');
	//$('#img_quitar_contrato').attr('src', '../../imagenes/cross2.png');
	contrato = false;
}

/*funcion para el cambio de grupo de firmas*/
function cambiarGrupoFirmas(cve_doc, modulo){
	jWindow('<iframe width="750" height="350" name="grupoFirmas" id="grupoFirmas" frameborder="0" src="../../sam/utilerias/cambiarFirmas.action?modulo='+modulo+'&cve_doc='+cve_doc+'"></iframe>','Cambiar grupo de firmas', '','Cerrar',1);
}


/*funcion para editar los documentos*/
function abrirDocumento(){
	jAlert('El modulo se encuentra desarrollado y no esta disponible por el momento', 'Advertencia');
}


function cambiarBeneficiario(cve_doc, modulo){
	var beneficiario="";
	var clave="";
	if(modulo=='req'){
			controladorListadoRequisicionesRemoto.getBeneficiario(cve_doc,{
				callback:function(items) { 
						ShowDelay('Cargando padrón de beneficiarios...','');
						if(items==null){
							beneficiario = ""; clave= "";	
						}
						else{
							beneficiario = getHTML(items.BENEFICIARIO); clave= getHTML(items.CLV_BENEFI);	
						}
						html ='<table width="400" border="0" cellspacing="0" cellpadding="0" alingn="center">'+
							 '<tr>'+
							 '<td height="20"><strong>Requisición:</strong></td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20">'+items.NUM_REQ+'</td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20"><strong>Beneficiario:</strong></td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20"><input type="text" id="txtbeneficiario" value="'+beneficiario+'" style="width:400px"/><input type="hidden" id="CVE_BENE" value="'+clave+'"/></td>'+
							 '</tr>'+
							 '<tr>' +
							 '</tr>'+
							 '<td height="20">&nbsp;</td>'+
							 '<tr>'+
							 '<td align="center"><input type="button" class="botones" value="Aplicar cambios"   id="cmdaplicar" style="width:100px"/> <input type="button" class="botones" value="Cancelar" id="cmdcancelar" onclick="$.alerts._hide();" style="width:100px"/></td>'+
							 '</tr>'+
							 '</table>';
							_closeDelay();
							jWindow(html,'Cambio de beneficiario', '','',0);
							$('#cmdaplicar').click(function(event){_cambiarBeneficiarioRequisicion(cve_doc);})
							getBeneficiarios('txtbeneficiario','CVE_BENE',''); 
				}
				,
					errorHandler:function(errorString, exception) { 
					jError('No se ha podido leer el beneficiario del documento, esta opcion no es valida para las requisiciones - '+errorString,'Error');   
					}       	
			});
	}
	
	if(modulo=='ped'){
			controladorPedidos.getBeneficiario(cve_doc,{
				callback:function(items) { 
						ShowDelay('Cargando padrón de beneficiarios...','');
						if(items==null){
							beneficiario = ""; clave= "";	
						}
						else{
							beneficiario = getHTML(items.BENEFICIARIO); clave= getHTML(items.CLV_BENEFI);	
						}
						html ='<table width="400" border="0" cellspacing="0" cellpadding="0" alingn="center">'+
							 '<tr>'+
							 '<td height="20"><strong>Num. Pedido:</strong></td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20">'+items.NUM_PED+'</td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20"><strong>Beneficiario:</strong></td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20"><input type="text" id="txtbeneficiario" value="'+beneficiario+'" style="width:400px"/><input type="hidden" id="CVE_BENE" value="'+clave+'"/></td>'+
							 '</tr>'+
							 '<tr>' +
							 '</tr>'+
							 '<td height="20">&nbsp;</td>'+
							 '<tr>'+
							 '<td align="center"><input type="button" class="botones" value="Aplicar cambios"   id="cmdaplicar" style="width:100px"/> <input type="button" class="botones" value="Cancelar" id="cmdcancelar" onclick="$.alerts._hide();" style="width:100px"/></td>'+
							 '</tr>'+
							 '</table>';
							_closeDelay();
							jWindow(html,'Cambio de beneficiario', '','',0);
							$('#cmdaplicar').click(function(event){_cambiarBeneficiarioPedidos(cve_doc);})
							getBeneficiarios('txtbeneficiario','CVE_BENE',''); 
				}
				,
					errorHandler:function(errorString, exception) { 
					jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
					}       	
			});
	}
	
	if(modulo=='op'){
			controladorOrdenPagoRemoto.getBeneficiario(cve_doc,{
				callback:function(items) { 
						ShowDelay('Cargando padrón de beneficiarios...','');
						if(items==null){
							beneficiario = ""; clave= "";	
						}
						else{
							beneficiario = getHTML(items.BENEFICIARIO); clave= getHTML(items.CLV_BENEFI);	
						}
						html ='<table width="400" border="0" cellspacing="0" cellpadding="0" alingn="center">'+
						 	 '<tr>'+
							 '<td height="20"><strong>Num. Orden Pago:</strong></td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20">'+items.NUM_OP+'</td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20"><strong>Beneficiario:</strong></td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20"><input type="text" id="txtbeneficiario" value="'+beneficiario+'" style="width:400px"/><input type="hidden" id="CVE_BENE" value="'+clave+'"/></td>'+
							 '</tr>'+
							 '<tr>' +
							 '</tr>'+
							 '<td height="20">&nbsp;</td>'+
							 '<tr>'+
							 '<td align="center"><input type="button" class="botones" value="Aplicar cambios"   id="cmdaplicar" style="width:100px"/> <input type="button" class="botones" value="Cancelar" id="cmdcancelar" onclick="$.alerts._hide();" style="width:100px"/></td>'+
							 '</tr>'+
							 '</table>';
							_closeDelay();
							jWindow(html,'Cambio de beneficiario', '','',0);
							$('#cmdaplicar').click(function(event){_cambiarBeneficiarioOrdenPago(cve_doc);})
							getBeneficiarios('txtbeneficiario','CVE_BENE',''); 
				}
				,
					errorHandler:function(errorString, exception) { 
					jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
					}       	
			});
	}
	
	if(modulo=='val'){
			controladorListadoValesRemoto.getBeneficiarioVale(cve_doc,{
				callback:function(items) { 
						ShowDelay('Cargando padrón de beneficiarios...','');
						if(items==null){
							beneficiario = ""; clave= "";
						}
						else{
							beneficiario = getHTML(items.BENEFICIARIO); clave= getHTML(items.CLV_BENEFI);	
						}
						html ='<table width="400" border="0" cellspacing="0" cellpadding="0" alingn="center">'+
							 '<tr>'+
							 '<td height="20"><strong>Num. Vale:</strong></td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20">'+items.NUM_VALE+'</td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20"><strong>Beneficiario:</strong></td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20"><input type="text" id="txtbeneficiario" value="'+beneficiario+'" style="width:400px"/><input type="hidden" id="CVE_BENE" value="'+clave+'"/></td>'+
							 '</tr>'+
							 '<tr>' +
							 '</tr>'+
							 '<td height="20">&nbsp;</td>'+
							 '<tr>'+
							 '<td align="center"><input type="button" class="botones" value="Aplicar cambios"   id="cmdaplicar" style="width:100px"/> <input type="button" class="botones" value="Cancelar" id="cmdcancelar" onclick="$.alerts._hide();" style="width:100px"/></td>'+
							 '</tr>'+
							 '</table>';
							_closeDelay();
							jWindow(html,'Cambio de beneficiario', '','',0);
							$('#cmdaplicar').click(function(event){_cambiarBeneficiarioVale(cve_doc);})
							getBeneficiarios('txtbeneficiario','CVE_BENE',''); 
				}
				,
					errorHandler:function(errorString, exception) { 
					jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
					}       	
			});
	}
}

function _cambiarBeneficiarioVale(cve_doc){
	var cve_benefi = $('#CVE_BENE').attr('value');
	if(cve_benefi==''){jAlert('Es necesario especificar el nuevo beneficiario para continuar','Alerta'); return false;}	
	jConfirm('¿Confirma que desea cambiar el beneficiario del documento actual?','Confirmar', function(r){
			if(r){		
				ShowDelay('Cambiando beneficiario...', '');
				controladorListadoValesRemoto.cambiarBeneficiario(cve_doc, cve_benefi,{
						callback:function(items) {
							if(items!=null){
								CloseDelay('Se ha cambiado el beneficiario con exito', 3000, function(){
										setTimeout('getVale()', 1000);
									});
							}
							
						},
						errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');   
					}   
				});
			}
		});
}

function _cambiarBeneficiarioOrdenPago(cve_doc){
	var cve_benefi = $('#CVE_BENE').attr('value');
	if(cve_benefi==''){jAlert('Es necesario especificar el nuevo beneficiario para continuar','Alerta'); return false;}	
	jConfirm('¿Confirma que desea cambiar el beneficiario del documento actual?','Confirmar', function(r){
			if(r){		
				ShowDelay('Cambiando beneficiario...', '');
				controladorOrdenPagoRemoto.cambiarBeneficiario(cve_doc, cve_benefi,{
						callback:function(items) {
							if(items!=null){
								CloseDelay('Se ha cambiado el beneficiario con exito', 3000, function(){
										setTimeout('getOrden()', 1000);
									});
							}
							
						},
						errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');   
					}   
				});
			}
		});
}

function _cambiarBeneficiarioPedidos(cve_doc){
	var cve_benefi = $('#CVE_BENE').attr('value');
	if(cve_benefi==''){jAlert('Es necesario especificar el nuevo beneficiario para continuar','Alerta'); return false;}	
	jConfirm('¿Confirma que desea cambiar el beneficiario del documento actual?','Confirmar', function(r){
			if(r){		
				ShowDelay('Cambiando beneficiario...', '');
				controladorPedidos.cambiarBeneficiario(cve_doc, cve_benefi,{
						callback:function(items) {
							if(items!=null){
								CloseDelay('Se ha cambiado el beneficiario con exito', 3000, function(){
										setTimeout('getPedidos()', 1000);
									});
							}
							
						},
						errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');   
					}   
				});
			}
		});
}

function _cambiarBeneficiarioRequisicion(cve_doc){
	var cve_benefi = $('#CVE_BENE').attr('value');
	if(cve_benefi==''){jAlert('Es necesario especificar el nuevo beneficiario para continuar','Alerta'); return false;}	
	jConfirm('¿Confirma que desea cambiar el beneficiario del documento actual?','Confirmar', function(r){
			if(r){		
				ShowDelay('Cambiando beneficiario...', '');
				controladorListadoRequisicionesRemoto.cambiarBeneficiario(cve_doc, cve_benefi,{
						callback:function(items) {
							if(items!=null){
								CloseDelay('Se ha cambiado el beneficiario con exito', 3000, function(){
										setTimeout('getListaReq()', 1000);
									});
							}
							
						},
						errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');   
					}   
				});
			}
		});
	 
}
/*function para cambiar el usuario de un documento*/
function cambiarUsuarioDocumento(cve_doc, modulo, cve_pers){
	
	var smodulo = "";
	if(modulo=='req') smodulo = "Requisiciones";
	if(modulo=='ped') smodulo = "Pedidos";
	if(modulo=='op') smodulo = "Ordenes de Pago";
	if(modulo=='val') smodulo = "Vales";
	
	if(modulo=='req'){
		var chkReq = [];
		var chkNumReq = [];
		$('input[name=chkrequisiciones]:checked').each(function(){chkReq.push($(this).val()); chkNumReq.push($(this).attr('alt')); });
		
		if(chkReq.length<=0) {
			$('input[name=chkrequisiciones]').each(function(){if($(this).val()==cve_doc) {chkNumReq.push($(this).attr('alt')); return false;} });
			chkReq.push(cve_doc); 
		}
		
		controladorListadoRequisicionesRemoto.getListUsuarios(cve_pers,{
			callback:function(items) { 
				if(items!=null) {
					html = '<table width="500" border="0" cellspacing="0" cellpadding="0">'+
						  '<tr>'+
						  '	<td width="474"><span style="font-size:12px"><I><strong>Nota:</strong> Los documentos seleccionados se van a transferir a otro usario, esto puede hacer que deje de visualizarlos en los listados que le corresponden.</span></I></td>'+
						  ' </tr>'+
						  '<tr>'+
								'<td height="20"><strong>'+((chkReq.length==1) ? 'Número de Requisición':'Grupo de Requisiciones:')+'</strong></td>'+
						 '</tr>'+
						 '<tr>'+
								'<td height="20">'+((chkNumReq.length==0) ? 'CVE_REQ: '+cve_doc:chkNumReq)+'</td>'+
						 '</tr>'+
						  ' <tr>'+
						  '<td><strong>Seleccione un usuario de destino:</strong></td>'+
						  '</tr>'+
						  '<tr>'+
							'<td>'+
							'<select id="cbousuarios" style="width:500px">'+items+
						'	</select>'+
						'	</td>'+
						 ' </tr>'+
						  '<tr>'+
							'<td>&nbsp;</td>'+
						'  </tr>'+
						 ' <tr>'+
						'	<td align="center"><input type="button" class="botones" value="Aplicar cambios"   id="cmdaplicar" style="width:100px"/> <input type="button" class="botones" value="Cancelar" id="cmdcancelar" onclick="$.alerts._hide();" style="width:100px"/></td>'+
						'  </tr>'+
						'</table>';
						jWindow(html,'Mover documento a otro usuario', '','',0);
						$('#cmdaplicar').click(function(event){_cambiarUsuarioRequisicion(chkReq,cve_doc);})
						
				}
				
			}
			,
				errorHandler:function(errorString, exception) { 
				jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
				}       	
		});
	}
	
	if(modulo=='ped'){
		var chkPed = [];
		var chkNumPed = [];
		$('input[name=chkpedidos]:checked').each(function(){chkPed.push($(this).val()); chkNumPed.push($(this).attr('alt')); });
		
		if(chkPed.length<=0) {
			$('input[name=chkpedidos]').each(function(){if($(this).val()==cve_doc) {chkNumPed.push($(this).attr('alt')); return false;} });
			chkPed.push(cve_doc); 
		}
		
		controladorPedidos.getListUsuarios(cve_pers,{
			callback:function(items) { 
				if(items!=null) {
					html = '<table width="500" border="0" cellspacing="0" cellpadding="0">'+
						  '<tr>'+
						  '	<td width="474"><span style="font-size:12px"><I><strong>Nota:</strong> Los documentos seleccionados se van a transferir a otro usario, esto puede hacer que deje de visualizarlos en los listados que le corresponden.</span></I></td>'+
						  ' </tr>'+
						  '<tr>'+
								'<td height="20"><strong>'+((chkPed.length==1) ? 'Número de Pedido':'Grupo de Pedidos:')+'</strong></td>'+
						 '</tr>'+
						 '<tr>'+
								'<td height="20">'+((chkNumPed.length==0) ? 'CVE_PED: '+cve_doc:chkNumPed)+'</td>'+
						 '</tr>'+
						  ' <tr>'+
						  '<td><strong>Seleccione un usuario de destino:</strong></td>'+
						  '</tr>'+
						  '<tr>'+
							'<td>'+
							'<select id="cbousuarios" style="width:500px">'+items+
						'	</select>'+
						'	</td>'+
						 ' </tr>'+
						  '<tr>'+
							'<td>&nbsp;</td>'+
						'  </tr>'+
						 ' <tr>'+
						'	<td align="center"><input type="button" class="botones" value="Aplicar cambios"   id="cmdaplicar" style="width:100px"/> <input type="button" class="botones" value="Cancelar" id="cmdcancelar" onclick="$.alerts._hide();" style="width:100px"/></td>'+
						'  </tr>'+
						'</table>';
						jWindow(html,'Mover documento a otro usuario', '','',0);
						$('#cmdaplicar').click(function(event){_cambiarUsuarioPedidos(chkPed,cve_doc);})
						
				}
				
			}
			,
				errorHandler:function(errorString, exception) { 
				jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
				}       	
		});
	}
	
	if(modulo=='op'){
		var chkOp = [];
		var chkNumOp = [];
		$('input[name=chkordenes]:checked').each(function(){chkOp.push($(this).val()); chkNumOp.push($(this).attr('alt')); });
		
		if(chkOp.length<=0) {
			$('input[name=chkordenes]').each(function(){if($(this).val()==cve_doc) {chkNumOp.push($(this).attr('alt')); return false;} });
			chkOp.push(cve_doc); 
		}
		
		controladorOrdenPagoRemoto.getListUsuarios(cve_pers,{
			callback:function(items) { 
				if(items!=null) {
					html = '<table width="500" border="0" cellspacing="0" cellpadding="0">'+
						  '<tr>'+
						  '	<td width="474"><span style="font-size:12px"><I><strong>Nota:</strong> Los documentos seleccionados se van a transferir a otro usario, esto puede hacer que deje de visualizarlos en los listados que le corresponden.</span></I></td>'+
						  ' </tr>'+
						  '<tr>'+
								'<td height="20"><strong>'+((chkOp.length==1) ? 'Orden de Pago':'Grupo de Orden(es) de Pago:')+'</strong></td>'+
						 '</tr>'+
						 '<tr>'+
								'<td height="20">'+((chkNumOp.length==0) ? 'CVE_OP: '+cve_doc:chkNumOp)+'</td>'+
						 '</tr>'+
						  ' <tr>'+
						  '<td><strong>Seleccione un usuario de destino:</strong></td>'+
						  '</tr>'+
						  '<tr>'+
							'<td>'+
							'<select id="cbousuarios" style="width:500px">'+items+
						'	</select>'+
						'	</td>'+
						 ' </tr>'+
						  '<tr>'+
							'<td>&nbsp;</td>'+
						'  </tr>'+
						 ' <tr>'+
						'	<td align="center"><input type="button" class="botones" value="Aplicar cambios"   id="cmdaplicar" style="width:100px"/> <input type="button" class="botones" value="Cancelar" id="cmdcancelar" onclick="$.alerts._hide();" style="width:100px"/></td>'+
						'  </tr>'+
						'</table>';
						jWindow(html,'Mover documento a otro usuario', '','',0);
						$('#cmdaplicar').click(function(event){_cambiarUsuarioOrdenPago(chkOp,cve_doc);})

				}
				
			}
			,
				errorHandler:function(errorString, exception) { 
				jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
				}       	
		});
	}
	
	if(modulo=='val'){
		var chkVal = [];
		var chkNumVal = [];
		$('input[name=claves]:checked').each(function(){chkVal.push($(this).val()); chkNumVal.push($(this).attr('alt')); });
		
		if(chkVal.length<=0) {
			$('input[name=claves]').each(function(){if($(this).val()==cve_doc) {chkNumVal.push($(this).attr('alt')); return false;} });
			chkVal.push(cve_doc); 
		}
		
		controladorListadoValesRemoto.getListUsuarios(cve_pers,{
			callback:function(items) { 
				if(items!=null) {
					html = '<table width="500" border="0" cellspacing="0" cellpadding="0">'+
						  '<tr>'+
						  '	<td width="474"><span style="font-size:12px"><I><strong>Nota:</strong> Los documentos seleccionados se van a transferir a otro usario, esto puede hacer que deje de visualizarlos en los listados que le corresponden.</span></I></td>'+
						  ' </tr>'+
						  '<tr>'+
								'<td height="20"><strong>'+((chkVal.length==1) ? 'Número de Vale':'Grupo de Vales:')+'</strong></td>'+
						 '</tr>'+
						 '<tr>'+
								'<td height="20">'+((chkNumVal.length==0) ? 'CVE_VALE: '+cve_doc:chkNumVal)+'</td>'+
						 '</tr>'+
						  ' <tr>'+
						  '<td><strong>Seleccione un usuario de destino:</strong></td>'+
						  '</tr>'+
						  '<tr>'+
							'<td>'+
							'<select id="cbousuarios" style="width:500px">'+items+
						'	</select>'+
						'	</td>'+
						 ' </tr>'+
						  '<tr>'+
							'<td>&nbsp;</td>'+
						'  </tr>'+
						 ' <tr>'+
						'	<td align="center"><input type="button" class="botones" value="Aplicar cambios"   id="cmdaplicar" style="width:100px"/> <input type="button" class="botones" value="Cancelar" id="cmdcancelar" onclick="$.alerts._hide();" style="width:100px"/></td>'+
						'  </tr>'+
						'</table>';
						jWindow(html,'Mover documento a otro usuario', '','',0);
						$('#cmdaplicar').click(function(event){_cambiarUsuarioVales(chkVal,cve_doc);})

				}
				
			}
			,
				errorHandler:function(errorString, exception) { 
				jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
				}       	
		});
	}
	
}

function _cambiarUsuarioVales(chkVal, cve_doc){
	var cve_pers_dest = $('#cbousuarios').val();
	if(cve_pers_dest==0){jAlert('Es necesario seleccionar un usuario para realizar esta operación', 'Advertencia'); return false;}
	jConfirm('¿Confirma que desea mover los Vales seleccionados al usuario especificado?', 'Confirmar', function(r){
			if(r){		
				ShowDelay('Moviendo documentos...', '');
				controladorListadoValesRemoto.moverVales(chkVal, cve_pers_dest,{
						callback:function(items) {
							if(items!=null){
								CloseDelay('Se han movido los documentos con exito', 3000, function(){
										setTimeout('getVale()', 1000);
									});
							}
							else
								jError('La operacion ha fallado al mover los documentos a otro usuario', 'Error inesperado');
						},
						errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
					}   
				});
			}
		});
}

function _cambiarUsuarioPedidos(chkPed, cve_doc){
	var cve_pers_dest = $('#cbousuarios').val();
	if(cve_pers_dest==0){jAlert('Es necesario seleccionar un usuario para realizar esta operación', 'Advertencia'); return false;}
	jConfirm('¿Confirma que desea mover los Pedidos seleccionados al usuario especificado?', 'Confirmar', function(r){
			if(r){		
				ShowDelay('Moviendo documentos...', '');
				controladorPedidos.moverPedidos(chkPed, cve_pers_dest,{
						callback:function(items) {
							if(items!=null){
								CloseDelay('Se han movido los documentos con exito', 3000, function(){
										setTimeout('getPedidos()', 1000);
									});
							}
							else
								jError('La operacion ha fallado al mover los documentos a otro usuario', 'Error inesperado');
						},
						errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
					}   
				});
			}
		});
}

function _cambiarUsuarioRequisicion(chkReq, cve_doc){
	var cve_pers_dest = $('#cbousuarios').val();
	if(cve_pers_dest==0){jAlert('Es necesario seleccionar un usuario para realizar esta operación', 'Advertencia'); return false;}
	jConfirm('¿Confirma que desea mover las Requisiciones seleccionadas al usuario especificado?', 'Confirmar', function(r){
			if(r){		
				ShowDelay('Moviendo documentos...', '');
				controladorListadoRequisicionesRemoto.moverRequisiciones(chkReq, cve_pers_dest,{
						callback:function(items) {
							if(items!=null) {
								CloseDelay('Se han movido los documentos con exito', 3000, function(){
										setTimeout('getListaReq()', 1000);
									});
							}
							else
								jError('La operacion ha fallado al mover los documentos a otro usuario', 'Error inesperado');
						},
						errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
			}   
				});
			}
		});
}

function _cambiarUsuarioOrdenPago(chkOp, cve_doc){
	var cve_pers_dest = $('#cbousuarios').val();
	if(cve_pers_dest==0){jAlert('Es necesario seleccionar un usuario para realizar esta operación', 'Advertencia'); return false;}
	jConfirm('¿Confirma que desea mover las Ordenes de Pago seleccionadas al usuario especificado?', 'Confirmar', function(r){
			if(r){		
				ShowDelay('Moviendo documentos...', '');
				controladorOrdenPagoRemoto.moverOrdenesPago(chkOp, cve_pers_dest,{
						callback:function(items) {
							if(items!=null) {
								CloseDelay('Se han movido los documentos con exito', 3000, function(){
										setTimeout('getOrden()', 1000);
									});
							}
							else
								jError('La operacion ha fallado al mover los documentos a otro usuario', 'Error inesperado');
						},
						errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");   
			}   
				});
			}
		});
}



/*funcion para el cambio de fecha y periodo*/
function cambiarFechaPeriodo(cve_doc, modulo){
	var smodulo = "";
	if(modulo=='req') smodulo = "Requisiciones";
	if(modulo=='ped') smodulo = "Pedidos";
	if(modulo=='op') smodulo ="Ordenes de Pago";
	if(modulo=='val') smodulo ="Vales";
	/*investigar el periodo y fecha actual del documento*/
	if(modulo=='req'){
		controladorListadoRequisicionesRemoto.getFechaPeriodoRequisicion(cve_doc, {
						callback:function(items) { 		
							var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
							'<tr>'+
								'<td height="20">Número Requisición:</td>'+
								'<td><strong>'+items.NUM_REQ+'</strong></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="30" width="150">Periodo actual: </td>'+
								'<td width="200"  height="30"><select name ="cboperiodo" id="cboperiodo" style="width:140px">'+
									'<option value="1" '+((items.PERIODO==1) ? 'Selected':'')+'>(01) Enero</option>'+
									'<option value="2" '+((items.PERIODO==2) ? 'Selected':'')+'>(02) Febrero</option>'+
									'<option value="3" '+((items.PERIODO==3) ? 'Selected':'')+'>(03) Marzo</option>'+
									'<option value="4" '+((items.PERIODO==4) ? 'Selected':'')+'>(04) Abril</option>'+
									'<option value="5" '+((items.PERIODO==5) ? 'Selected':'')+'>(05) Mayo</option>'+
									'<option value="6" '+((items.PERIODO==6) ? 'Selected':'')+'>(06) Junio</option>'+
									'<option value="7" '+((items.PERIODO==7) ? 'Selected':'')+'>(07) Julio</option>'+
									'<option value="8" '+((items.PERIODO==8) ? 'Selected':'')+'>(08) Agosto</option>'+
									'<option value="9" '+((items.PERIODO==9) ? 'Selected':'')+'>(09) Septiembre</option>'+
									'<option value="10" '+((items.PERIODO==10) ? 'Selected':'')+'>(10) Octubre</option>'+
									'<option value="11" '+((items.PERIODO==11) ? 'Selected':'')+'>(11) Noviembre</option>'+
									'<option value="12" '+((items.PERIODO==12) ? 'Selected':'')+'>(12) Diciembre</option>'+
								'</select></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="30">Fecha actual dd/mm/aaaa:</td>'+
								'<td><input type="text" id="txtfechaactual" value="'+items.FECHA+'" style="width:140px" /></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="50" align="center" colspan="2"><input type="button" value="Aplicar cambios" id="cmdaplicar" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones" style="width:100px"/></td>'+
							  '</tr>'+
							'</table>';
	
							jWindow(html,'Cambio de fecha y periodo en '+smodulo, '','',0);
							if(modulo=='req')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoRequisicion(cve_doc);})
							if(modulo=='op')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoOp(cve_doc);})
							$('#cmdcancelar').click(function(event){$.alerts._hide();})
							$('#txtfechaactual').keypress(function(event){if (event.keyCode == '13'){$('#cmdaplicar').click();}});	
							
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});
	}
	if(modulo=='ped'){
		controladorPedidos.getFechaPeriodoPedido(cve_doc, {
						callback:function(items) { 		
							var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
							'<tr>'+
								'<td height="30">Número Pedido:</td>'+
								'<td><strong>'+items.NUM_PED+'</strong></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="30">Fecha actual dd/mm/aaaa:</td>'+
								'<td><input type="text" id="txtfechaactual" value="'+items.FECHA_PED+'" style="width:140px" /></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="50" align="center" colspan="2"><input type="button" value="Aplicar cambios" id="cmdaplicar" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones" style="width:100px"/></td>'+
							  '</tr>'+
							'</table>';
	
							jWindow(html,'Cambio de fecha en '+smodulo, '','',0);
							if(modulo=='req')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoRequisicion(cve_doc);})
							if(modulo=='ped')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoPedido(cve_doc);})
							if(modulo=='op')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoOp(cve_doc);})
							$('#cmdcancelar').click(function(event){$.alerts._hide();})
							$('#txtfechaactual').keypress(function(event){if (event.keyCode == '13'){$('#cmdaplicar').click();}});	
							
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});
	}
	if(modulo=='op'){
		controladorOrdenPagoRemoto.getFechaPeriodoOp(cve_doc, {
						callback:function(items) { 		
							var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
							  '<tr>'+
								'<td height="30">Número Orden de Pago:</td>'+
								'<td><strong>'+items.NUM_OP+'</strong></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="30" width="150">Periodo actual: </td>'+
								'<td width="200"><select name ="cboperiodo" id="cboperiodo" style="width:140px">'+
									'<option value="1" '+((items.PERIODO==1) ? 'Selected':'')+'>(01) Enero</option>'+
									'<option value="2" '+((items.PERIODO==2) ? 'Selected':'')+'>(02) Febrero</option>'+
									'<option value="3" '+((items.PERIODO==3) ? 'Selected':'')+'>(03) Marzo</option>'+
									'<option value="4" '+((items.PERIODO==4) ? 'Selected':'')+'>(04) Abril</option>'+
									'<option value="5" '+((items.PERIODO==5) ? 'Selected':'')+'>(05) Mayo</option>'+
									'<option value="6" '+((items.PERIODO==6) ? 'Selected':'')+'>(06) Junio</option>'+
									'<option value="7" '+((items.PERIODO==7) ? 'Selected':'')+'>(07) Julio</option>'+
									'<option value="8" '+((items.PERIODO==8) ? 'Selected':'')+'>(08) Agosto</option>'+
									'<option value="9" '+((items.PERIODO==9) ? 'Selected':'')+'>(09) Septiembre</option>'+
									'<option value="10" '+((items.PERIODO==10) ? 'Selected':'')+'>(10) Octubre</option>'+
									'<option value="11" '+((items.PERIODO==11) ? 'Selected':'')+'>(11) Noviembre</option>'+
									'<option value="12" '+((items.PERIODO==12) ? 'Selected':'')+'>(12) Diciembre</option>'+
								'</select></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="30">Fecha actual dd/mm/aaaa:</td>'+
								'<td><input type="text" id="txtfechaactual" value="'+items.FECHA+'" style="width:140px" /></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="50" align="center" colspan="2"><input type="button" value="Aplicar cambios" id="cmdaplicar" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones" style="width:100px"/></td>'+
							  '</tr>'+
							'</table>';
	
							jWindow(html,'Cambio de fecha y periodo en '+smodulo, '','',0);
							if(modulo=='req')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoRequisicion(cve_doc);})
							if(modulo=='op')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoOp(cve_doc);})
							$('#cmdcancelar').click(function(event){$.alerts._hide();})
							$('#txtfechaactual').keypress(function(event){if (event.keyCode == '13'){$('#cmdaplicar').click();}});	
							
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});
	}
	
	if(modulo=='val'){
		controladorListadoValesRemoto.getFechaPeriodoVale(cve_doc, {
						callback:function(items) { 		
							var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
							  '<tr>'+
								'<td height="30">Numero Vale:</td>'+
								'<td><strong>'+items.NUM_VALE+'</strong></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="30" width="150">Periodo actual: </td>'+
								'<td width="200"><select name ="cboperiodo" id="cboperiodo" style="width:140px">'+
									'<option value="1" '+((items.MES==1) ? 'Selected':'')+'>(01) Enero</option>'+
									'<option value="2" '+((items.MES==2) ? 'Selected':'')+'>(02) Febrero</option>'+
									'<option value="3" '+((items.MES==3) ? 'Selected':'')+'>(03) Marzo</option>'+
									'<option value="4" '+((items.MES==4) ? 'Selected':'')+'>(04) Abril</option>'+
									'<option value="5" '+((items.MES==5) ? 'Selected':'')+'>(05) Mayo</option>'+
									'<option value="6" '+((items.MES==6) ? 'Selected':'')+'>(06) Junio</option>'+
									'<option value="7" '+((items.MES==7) ? 'Selected':'')+'>(07) Julio</option>'+
									'<option value="8" '+((items.MES==8) ? 'Selected':'')+'>(08) Agosto</option>'+
									'<option value="9" '+((items.MES==9) ? 'Selected':'')+'>(09) Septiembre</option>'+
									'<option value="10" '+((items.MES==10) ? 'Selected':'')+'>(10) Octubre</option>'+
									'<option value="11" '+((items.MES==11) ? 'Selected':'')+'>(11) Noviembre</option>'+
									'<option value="12" '+((items.MES==12) ? 'Selected':'')+'>(12) Diciembre</option>'+
								'</select></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="30">Fecha actual dd/mm/aaaa:</td>'+
								'<td><input type="text" id="txtfechaactual" value="'+items.FECHA+'" style="width:140px" /></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="50" align="center" colspan="2"><input type="button" value="Aplicar cambios" id="cmdaplicar" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones" style="width:100px"/></td>'+
							  '</tr>'+
							'</table>';
	
							jWindow(html,'Cambio de fecha y periodo en '+smodulo, '','',0);
							if(modulo=='req')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoRequisicion(cve_doc);})
							if(modulo=='op')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoOp(cve_doc);})
							if(modulo=='val')
								$('#cmdaplicar').click(function(event){_cambiarFechaPeriodoVal(cve_doc);})
							$('#cmdcancelar').click(function(event){$.alerts._hide();})
							$('#txtfechaactual').keypress(function(event){if (event.keyCode == '13'){$('#cmdaplicar').click();}});	
							
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});
	}
}

function _cambiarFechaPeriodoVal(cve_doc){
	var periodo = $('#cboperiodo').val();
	var fecha = $('#txtfechaactual').attr('value');
	
	jConfirm('¿Confirma que desea aplicar los cambios para la fecha y periodo del Vale?','Confirmar', function(r){
		if(r){
			controladorListadoValesRemoto.cambiarFechaPeriodo(cve_doc, fecha, periodo, {
						callback:function(items) { 	
							if(items)
								CloseDelay('Fecha y periodo cambiados con éxito', 3000, setTimeout('getVale()',1000));
							else 
								jError(items, 'Error');
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});	
		}
	});
}

function _cambiarFechaPeriodoOp(cve_doc){
	var periodo = $('#cboperiodo').val();
	var fecha = $('#txtfechaactual').attr('value');
	
	jConfirm('¿Confirma que desea aplicar los cambios para la fecha y periodo de la Orden de Pago?','Confirmar', function(r){
		if(r){
			controladorOrdenPagoRemoto.cambiarFechaPeriodo(cve_doc, fecha, periodo, {
						callback:function(items) { 	
							if(items)
								CloseDelay('Fecha y periodo cambiados con éxito', 3000, setTimeout('getOrden()',1000));
							else 
								jError(items, 'Error');
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});	
		}
	});
}

function _cambiarFechaPeriodoPedido(cve_doc){
	var fecha = $('#txtfechaactual').attr('value');

	jConfirm('¿Confirma que desea aplicar los cambios para la fecha del Pedido?','Confirmar', function(r){
		if(r){
			controladorPedidos.cambiarFechaPeriodo(cve_doc, fecha, {
						callback:function(items) { 	
							if(items)
								CloseDelay('Fecha cambiada con éxito', 3000, setTimeout('getPedidos()',1000));
							else 
								jError(items, 'Error');
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});	
		}
	});
}

function _cambiarFechaPeriodoRequisicion(cve_doc){
	var periodo = $('#cboperiodo').val();
	var fecha = $('#txtfechaactual').attr('value');
	
	jConfirm('¿Confirma que desea aplicar los cambios para la fecha y periodo de la Requisicion?','Confirmar', function(r){
		if(r){
			controladorListadoRequisicionesRemoto.cambiarFechaPeriodo(cve_doc, fecha, periodo, {
						callback:function(items) { 	
							if(items)
								CloseDelay('Fecha y periodo cambiados con éxito', 3000, setTimeout('getListaReq()',1000));
							else 
								jError(items, 'Error');
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});	
		}
	});
}

/*funcion que sirve para mostrar el submenu de opciones de un asuario*/
function subOpAdm(modulo, cve_doc, cve_pers){
	var titulo = "";
	if(modulo=='req') titulo = 'Requisiciones';
	if(modulo=='ped') titulo = 'Pedidos'
	if(modulo=='op') titulo = 'Ordenes de Pago';
	if(modulo=='val') titulo = 'Vales';
	if(modulo=='con') titulo = 'Contrato';
	jWindow('<iframe width="400" height="220" name="subMenuAdmon" id="subMenuAdmon" frameborder="0" src="../../sam/utilerias/sumenuAdmon.action?modulo='+modulo+'&cve_doc='+cve_doc+'&cve_pers='+cve_pers+'"></iframe>','Submenu de opciones módulo '+titulo, '','Cerrar',1);
}

function ajustesImportes(cve_doc, modulo, num_doc)
{
	if(modulo=='ped')
		ajustesImportes_Pedido(cve_doc, modulo, num_doc);
	if(modulo=='con')
		ajustesImportes_Contrato(cve_doc, modulo, num_doc);
	if(modulo=='fac')
		ajustesImportes_Factura(cve_doc, modulo, num_doc);
}

function ajustesImportes_Pedido(cve_doc, modulo, num_doc)
{
	var proyectos = [];
	var partidas = [];
	var html = '';
		controladorPedidos.getMovimientosAjustadosPedidos(cve_doc, {
						callback:function(items) {
							html+= '<div id="divMovCaptura" style="display:none; position:absolute; top:15px; padding-bottom:10px">'
								+'<h1>Ajuste de Importe en Pedidos</h1>'
								+'<table width="400">'
									+'<tr><td><input id="ID_SAM_MOD_COMP" type="hidden" value="0"> <input id="CVE_PEDIDO" type="hidden" value="'+cve_doc+'"></td></tr>'
									+'<tr><th height="20">Proyecto:</th><td><select id="cboProyecto" style="width:200px;"></select></td></tr>'
									+'<tr><th height="20">Partidas:</th><td><select id="cboPartidas" style="width:200px;"></select></td></tr>'
									+'<tr><th height="20">Fecha:</th><td><input type="text" id="txtfechaactual" value="" style="width:197px"></td></tr>'
									+'<tr><th height="20">Importe:</th><td><input id="txtimporteMov" type="text" value="" maxlength="10" style="width:197px;" onkeypress="return keyNumbero(event);"></td></tr>'
									+'<tr><th height="20"></th></tr>'
									+'<tr><th height="20"></th><td><input type="button" style="width:100px" class="botones" value="Guardar" id="cmdGuardarAjuste">&nbsp;<input type="button" style="width:100px" class="botones" value="Cancelar" id="cmdCancelarAjuste"></td></tr>'
								+'</table>'
								+'</div>';
							html+= '<div id="divMovListado"><div style="padding-bottom:5px"><input id="cmdAgregarAjuste" style="width:200px;" type="button" value="Agregar Ajuste de Importe"></div><table class="listas" width="400"><tr><th height="20">PROYECTO</th><th>PARTIDA</th><th>FECHA</th><th>IMPORTE</th><th>Opc.</th></tr>';
							jQuery.each(items,function(i) {
								html += '<td align="center">'+this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']' +'</td><td align="center">'+this.CLV_PARTID+'</td><td>'+this.FECHA_MOVTO+'</td><td align="right">'+formatNumber(this.IMPORTE,'$')+'</td><td align="center"><img  src="../../imagenes/page_white_edit.png" width="16" height="16" style="cursor:pointer" OnClick="editarConceptoAjuste('+cve_doc+',\''+modulo+'\',\''+num_doc+'\','+this.id_sam_mod_comp+','+this.ID_PROYECTO+',\''+this.CLV_PARTID+'\',\''+this.FECHA_MOVTO+'\',\''+this.IMPORTE+'\')" > <img id="Remover" src="../../imagenes/cross.png" width="16" height="16" style="cursor:pointer" OnClick="eliminarConceptoAjuste('+cve_doc+',\''+modulo+'\',\''+num_doc+'\','+this.id_sam_mod_comp+')"></td></tr>'; 
								if(proyectos.indexOf(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']')==-1)
									proyectos.push(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']');
								if(partidas.indexOf(this.CLV_PARTID)==-1)
									partidas.push(this.CLV_PARTID);
							});
							html+='</table></div>';
							jWindow(html,'Ajuste de Importe en Factura: '+num_doc, '','Cerrar',1);
							
							//Si no hay proyectos buscar en los conceptos
							if(proyectos.length==0)
							{
								controladorPedidos.getProyectoPartidaPedido(cve_doc, {
									callback:function(items) {
											if(proyectos.indexOf(items.ID_PROYECTO+ ' ['+items.N_PROGRAMA+']')==-1)
												proyectos.push(items.ID_PROYECTO+ ' ['+items.N_PROGRAMA+']');
											if(partidas.indexOf(items.CLV_PARTID)==-1)
												partidas.push(items.CLV_PARTID);

									} 					   				
								,
								errorHandler:function(errorString, exception) { 
										jError(errorString, 'Error');          
									}
								});
							}
							
							$('#cmdGuardarAjuste').click(function(event){
								if($('#txtimporteMov').val()=='')
								{
									alert('Es necesario escribir el importe');
									return false;
								}
								else
									guardarMovimientoAjustePedido(cve_doc, modulo, num_doc);
							});
							$('#cmdCancelarAjuste').click(function(event){
								$('#divMovListado').show();
								$('#divMovCaptura').hide();
								ajustesImportes(cve_doc, modulo, num_doc);
							});
							$('#cmdAgregarAjuste').click(function(event){
								var d = new Date();
								var curr_date = d.getDate();
								var curr_month = d.getMonth()+1;
								var curr_year = d.getFullYear();
								$('#divMovListado').hide();
								$('#divMovCaptura').show();
								$('#popup_message_window').css('height','250px');
								$('#txtfechaactual').attr('value', curr_date+'/'+(parseInt(curr_month) < 10? '0'+curr_month : curr_month)+'/'+curr_year);
								$.each( proyectos, function( index, value ){
									$('#cboProyecto').append('<option value='+value+'>'+value+'</option>');
								});
									$.each( partidas, function( index, value ){
										$('#cboPartidas').append('<option value='+value+'>'+value+'</option>');
								});
							});
							
							$.each( proyectos, function( index, value ){
									$('#cboProyecto').append('<option value='+value+'>'+value+'</option>');
							});
								$.each( partidas, function( index, value ){
									$('#cboPartidas').append('<option value='+value+'>'+value+'</option>');
							});
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});
}


function ajustesImportes_Factura(cve_doc, modulo, num_doc)
{
	var proyectos = [];
	var partidas = [];
	var html = '';
		controladorListadoFacturasRemoto.getMovimientosAjustadosFactura(cve_doc, {
						callback:function(items) {
							html+= '<div id="divMovCaptura" style="display:none; position:absolute; top:15px; padding-bottom:10px">'
								+'<h1>Ajuste de Importe en Factura</h1>'
								+'<table width="400">'
									+'<tr><td><input id="ID_SAM_MOD_COMP" type="hidden" value="0"> <input id="CVE_FACTURA" type="hidden" value="'+cve_doc+'"></td></tr>'
									+'<tr><th height="20">Proyecto:</th><td><select id="cboProyecto" style="width:200px;"></select></td></tr>'
									+'<tr><th height="20">Partidas:</th><td><select id="cboPartidas" style="width:200px;"></select></td></tr>'
									+'<tr><th height="20">Fecha:</th><td><input type="text" id="txtfechaactual" value="" style="width:197px"></td></tr>'
									+'<tr><th height="20">Importe:</th><td><input id="txtimporteMov" type="text" value="" maxlength="10" style="width:197px;" onkeypress="return keyNumbero(event);"></td></tr>'
									+'<tr><th height="20"></th></tr>'
									+'<tr><th height="20"></th><td><input type="button" style="width:100px" class="botones" value="Guardar" id="cmdGuardarAjuste">&nbsp;<input type="button" style="width:100px" class="botones" value="Cancelar" id="cmdCancelarAjuste"></td></tr>'
								+'</table>'
								+'</div>';
							html+= '<div id="divMovListado"><div style="padding-bottom:5px"><input id="cmdAgregarAjuste" style="width:200px;" type="button" value="Agregar Ajuste de Importe"></div><table class="listas" width="400"><tr><th height="20">PROYECTO</th><th>PARTIDA</th><th>FECHA</th><th>IMPORTE</th><th>Opc.</th></tr>';
							jQuery.each(items,function(i) {
								html += '<td align="center">'+this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']' +'</td><td align="center">'+this.CLV_PARTID+'</td><td>'+this.FECHA_MOVTO+'</td><td align="right">'+formatNumber(this.IMPORTE,'$')+'</td><td align="center"><img  src="../../imagenes/page_white_edit.png" width="16" height="16" style="cursor:pointer" OnClick="editarConceptoAjuste('+cve_doc+',\''+modulo+'\',\''+num_doc+'\','+this.id_sam_mod_comp+','+this.ID_PROYECTO+',\''+this.CLV_PARTID+'\',\''+this.FECHA_MOVTO+'\',\''+this.IMPORTE+'\')" > <img id="Remover" src="../../imagenes/cross.png" width="16" height="16" style="cursor:pointer" OnClick="eliminarConceptoAjuste('+cve_doc+',\''+modulo+'\',\''+num_doc+'\','+this.id_sam_mod_comp+')"></td></tr>'; 
								if(proyectos.indexOf(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']')==-1)
									proyectos.push(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']');
								if(partidas.indexOf(this.CLV_PARTID)==-1)
									partidas.push(this.CLV_PARTID);
							});
							html+='</table></div>';
							jWindow(html,'Ajuste de Importe en Factura: '+num_doc, '','Cerrar',1);
							
							//Si no hay proyectos buscar en los conceptos
							if(proyectos.length==0)
							{
								controladorListadoFacturasRemoto.getConceptosFactura(cve_doc, {
									callback:function(items) {
										jQuery.each(items,function(i) {
											if(proyectos.indexOf(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']')==-1)
												proyectos.push(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']');
											if(partidas.indexOf(this.CLV_PARTID)==-1)
												partidas.push(this.CLV_PARTID);
											
										});
									} 					   				
								,
								errorHandler:function(errorString, exception) { 
										jError(errorString, 'Error');          
									}
								});
							}
							
							$('#cmdGuardarAjuste').click(function(event){
								if($('#txtimporteMov').val()=='')
								{
									alert('Es necesario escribir el importe');
									return false;
								}
								else
									guardarMovimientoAjusteFactura(cve_doc, modulo, num_doc);
							});
							$('#cmdCancelarAjuste').click(function(event){
								$('#divMovListado').show();
								$('#divMovCaptura').hide();
								ajustesImportes(cve_doc, modulo, num_doc);
							});
							$('#cmdAgregarAjuste').click(function(event){
								var d = new Date();
								var curr_date = d.getDate();
								var curr_month = d.getMonth()+1;
								var curr_year = d.getFullYear();
								$('#divMovListado').hide();
								$('#divMovCaptura').show();
								$('#popup_message_window').css('height','250px');
								$('#txtfechaactual').attr('value', curr_date+'/'+(parseInt(curr_month) < 10? '0'+curr_month : curr_month)+'/'+curr_year);
								$.each( proyectos, function( index, value ){
									$('#cboProyecto').append('<option value='+value+'>'+value+'</option>');
								});
									$.each( partidas, function( index, value ){
										$('#cboPartidas').append('<option value='+value+'>'+value+'</option>');
								});
							});
							
							$.each( proyectos, function( index, value ){
									$('#cboProyecto').append('<option value='+value+'>'+value+'</option>');
							});
								$.each( partidas, function( index, value ){
									$('#cboPartidas').append('<option value='+value+'>'+value+'</option>');
							});
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});
}


function ajustesImportes_Contrato(cve_doc, modulo, num_doc)
{
	var proyectos = [];
	var partidas = [];
	var html = '';
		controladorListadoContratosRemoto.getMovimientosAjustadosContrato(cve_doc, {
						callback:function(items) {
							html+= '<div id="divMovCaptura" style="display:none; position:absolute; top:15px; padding-bottom:10px">'
								+'<h1>Ajuste de Importe en Contrato</h1>'
								+'<table width="400">'
									+'<tr><td><input id="ID_SAM_MOD_COMP" type="hidden" value="0"> <input id="CVE_CONTRATO" type="hidden" value="'+cve_doc+'"></td></tr>'
									+'<tr><th height="20">Proyecto:</th><td><select id="cboProyecto" style="width:200px;"></select></td></tr>'
									+'<tr><th height="20">Partidas:</th><td><select id="cboPartidas" style="width:200px;"></select></td></tr>'
									+'<tr><th height="20">Fecha:</th><td><input type="text" id="txtfechaactual" value="" style="width:197px"></td></tr>'
									+'<tr><th height="20">Importe:</th><td><input id="txtimporteMov" type="text" value="" maxlength="10" style="width:197px;" onkeypress="return keyNumbero(event);"></td></tr>'
									+'<tr><th height="20"></th></tr>'
									+'<tr><th height="20"></th><td><input type="button" style="width:100px" class="botones" value="Guardar" id="cmdGuardarAjusteContrato">&nbsp;<input type="button" style="width:100px" class="botones" value="Cancelar" id="cmdCancelarAjusteContrato"></td></tr>'
								+'</table>'
								+'</div>';
							html+= '<div id="divMovListado"><div style="padding-bottom:5px"><input id="cmdAgregarAjusteContrato" style="width:200px;" type="button" value="Agregar Ajuste de Importe"></div><table class="listas" width="400"><tr><th height="20">PROYECTO</th><th>PARTIDA</th><th>FECHA</th><th>IMPORTE</th><th>Opc.</th></tr>';
							jQuery.each(items,function(i) {
								html += '<td align="center">'+this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']' +'</td><td align="center">'+this.CLV_PARTID+'</td><td>'+this.FECHA_MOVTO+'</td><td align="right">'+formatNumber(this.IMPORTE,'$')+'</td><td align="center"><img  src="../../imagenes/page_white_edit.png" width="16" height="16" style="cursor:pointer" OnClick="editarConceptoAjuste('+cve_doc+',\''+modulo+'\',\''+num_doc+'\','+this.id_sam_mod_comp+','+this.ID_PROYECTO+',\''+this.CLV_PARTID+'\',\''+this.FECHA_MOVTO+'\',\''+this.IMPORTE+'\')" > <img id="Remover" src="../../imagenes/cross.png" width="16" height="16" style="cursor:pointer" OnClick="eliminarConceptoAjuste('+cve_doc+',\''+modulo+'\',\''+num_doc+'\','+this.id_sam_mod_comp+')"></td></tr>'; 
								if(proyectos.indexOf(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']')==-1)
									proyectos.push(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']');
								if(partidas.indexOf(this.CLV_PARTID)==-1)
									partidas.push(this.CLV_PARTID);
							});
							html+='</table></div>';
							jWindow(html,'Ajuste de Importe en Contrato: '+num_doc, '','Cerrar',1);
							
							//Si no hay proyectos buscar en los conceptos
							if(proyectos.length==0)
							{
								controladorListadoContratosRemoto.getConceptosContrato(cve_doc, {
									callback:function(items) {
										jQuery.each(items,function(i) {
											if(proyectos.indexOf(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']')==-1)
												proyectos.push(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']');
											if(partidas.indexOf(this.CLV_PARTID)==-1)
												partidas.push(this.CLV_PARTID);
											
										});
									} 					   				
								,
								errorHandler:function(errorString, exception) { 
										jError(errorString, 'Error');          
									}
								});
							}
							
							$('#cmdGuardarAjusteContrato').click(function(event){
								if($('#txtimporteMov').val()=='')
								{
									alert('Es necesario escribir el importe');
									return false;
								}
								else
									guardarMovimientoAjusteContrato(cve_doc, modulo, num_doc);
							});
							$('#cmdCancelarAjusteContrato').click(function(event){
								$('#divMovListado').show();
								$('#divMovCaptura').hide();
								ajustesImportes(cve_doc, modulo, num_doc);
							});
							$('#cmdAgregarAjusteContrato').click(function(event){
								var d = new Date();
								var curr_date = d.getDate();
								var curr_month = d.getMonth()+1;
								var curr_year = d.getFullYear();
								$('#divMovListado').hide();
								$('#divMovCaptura').show();
								$('#popup_message_window').css('height','250px');
								$('#txtfechaactual').attr('value', curr_date+'/'+(parseInt(curr_month) < 10? '0'+curr_month : curr_month)+'/'+curr_year);
								$.each( proyectos, function( index, value ){
									$('#cboProyecto').append('<option value='+value+'>'+value+'</option>');
								});
									$.each( partidas, function( index, value ){
										$('#cboPartidas').append('<option value='+value+'>'+value+'</option>');
								});
							});
							
							$.each( proyectos, function( index, value ){
									$('#cboProyecto').append('<option value='+value+'>'+value+'</option>');
							});
								$.each( partidas, function( index, value ){
									$('#cboPartidas').append('<option value='+value+'>'+value+'</option>');
							});
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});
}



function editarConceptoAjuste(cve_doc, modulo, num_doc, idDetalle, idProyecto, clv_partid, fecha, importe)
{
	$('#divMovCaptura').show();
	$('#divMovListado').hide();
	$('#popup_message_window').css('height','250px');
	$('#ID_SAM_MOD_COMP').attr('value', idDetalle);
	$('#cboProyecto').val(idProyecto);
	$('#cboPartidas').val(clv_partid);
	$('#txtfechaactual').attr('value', fecha);
	$('#txtimporteMov').attr('value', importe);
	
}

function eliminarConceptoAjuste(cve_doc, modulo, num_doc, idConcepto)
{
	if(modulo=='ped')
	{
		controladorPedidos.eliminarConceptoAjustePedido(idConcepto, {
					callback:function(items) {
						ajustesImportes(cve_doc, modulo, num_doc);	
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError(errorString, 'Error');          
				}
			});
	}
	if(modulo=='fac')
	{
		controladorListadoFacturasRemoto.eliminarConceptoAjusteFactura(idConcepto, {
					callback:function(items) {
						ajustesImportes(cve_doc, modulo, num_doc);	
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError(errorString, 'Error');          
				}
			});
	}
	if(modulo=='con')
	{
			ControladorContratosRemoto.eliminarConceptoAjusteContrato(idConcepto, {
					callback:function(items) {
						ajustesImportes(cve_doc, modulo, num_doc);	
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError(errorString, 'Error');          
				}
			});
	}
}

function guardarMovimientoAjustePedido(cve_doc, modulo, num_doc)
{
	controladorPedidos.guardarAjustePedidoPeredo($('#ID_SAM_MOD_COMP').attr('value'), cve_doc, $('#cboProyecto').attr('value'),$('#cboPartidas').attr('value'), $('#txtfechaactual').attr('value'), $('#txtimporteMov').attr('value'),{
	  callback:function(items) {
				ajustesImportes(cve_doc, modulo, num_doc);
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');     
		}
	});
}


function guardarMovimientoAjusteFactura(cve_doc, modulo, num_doc)
{
	controladorListadoFacturasRemoto.guardarAjusteFacturaPeredo($('#ID_SAM_MOD_COMP').attr('value'), cve_doc, $('#cboProyecto').attr('value'),$('#cboPartidas').attr('value'), $('#txtfechaactual').attr('value'), $('#txtimporteMov').attr('value'),{
	  callback:function(items) {
				ajustesImportes(cve_doc, modulo, num_doc);
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');     
		}
	});
}

function guardarMovimientoAjusteContrato(cve_doc, modulo, num_doc)
{
	ControladorContratosRemoto.guardarAjusteContratoPeredo($('#ID_SAM_MOD_COMP').attr('value'), $('#CVE_CONTRATO').attr('value'), $('#cboProyecto').attr('value'),$('#cboPartidas').attr('value'), $('#txtfechaactual').attr('value'), $('#txtimporteMov').attr('value'),{
	  callback:function(items) {
				ajustesImportes(cve_doc, modulo, num_doc);
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');     
		}
	});
}


function reduccionAmpliacion(cve_doc, modulo, num_doc)
{
	var proyectos = [];
	var partidas = [];
	var html = '';
	if(modulo=='con')
	{
		controladorListadoContratosRemoto.getConceptosContrato(cve_doc, {
						callback:function(items) {
							html+= '<div id="divMovCaptura" style="display:none; position:absolute; top:15px; padding-bottom:10px">'
								+'<h1>Capturar movimientos de contrato</h1>'
								+'<table>'
									+'<tr><td><input id="ID_DETALLE" type="hidden" value="0"> <input id="CVE_CONTRATO" type="hidden" value="'+cve_doc+'"></td></tr>'
									+'<tr><th height="20">Tipo de Movimiento:</th><td><select id="cboMovimiento" style="width:200px;"><option value="COMPROMISO">COMPROMISO</option></select></td></tr>'
									+'<tr><th height="20">Proyecto:</th><td><select id="cboProyecto" style="width:200px;"></select></td></tr>'
									+'<tr><th height="20">Partidas:</th><td><select id="cboPartidas" style="width:200px;"></select></td></tr>'
									+'<tr><th height="20">Periodo:</th><td><select id="cboPeriodo" style="width:200px;"><option value="1">ENERO</option><option value="2">FEBRERO</option><option value="3">MARZO</option><option value="4">ABRIL</option><option value="5">MAYO</option><option value="6">JUNIO</option><option value="7">JULIO</option><option value="8">AGOSTO</option><option value="9">SEPTIEMBRE</option><option value="10">OCTUBRE</option><option value="11">NOVIEMBRE</option><option value="12">DICIEMBRE</option></select></td></tr>'
									+'<tr><th height="20">Importe:</th><td><input id="txtimporteMovCon" type="text" value="" maxlength="10" style="width:197px;" onkeypress="return keyNumbero(event);"></td></tr>'
									+'<tr><th height="20"></th></tr>'
									+'<tr><th height="20"></th><td><input type="button" style="width:100px" class="botones" value="Guardar" id="cmdGuardarMovCon">&nbsp;<input type="button" style="width:100px" class="botones" value="Cancelar" id="cmdCancelarMovCon"></td></tr>'
								+'</table>'
								+'</div>';
							html+= '<div id="divMovListado"><div style="padding-bottom:5px"><input id="cmdAgregarMovCon" style="width:160px;" type="button" value="Agregar Movimiento"></div><table class="listas" width="450"><tr><th height="20">PERIODO</th><th>PROYECTO</th><th>PARTIDA</th><th>MOVIMIENTO</th><th>IMPORTE</th><th>Opc.</th></tr>';
							jQuery.each(items,function(i) {
								html += '<tr><td height="20" align="center">'+this.DESC_PERIODO+'</td><td align="center">'+this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']' +'</td><td align="center">'+this.CLV_PARTID+'</td><td>'+this.TIPO_MOV+'</td><td align="right">'+formatNumber(this.IMPORTE,'$')+'</td><td align="center"><img  src="../../imagenes/page_white_edit.png" width="16" height="16" style="cursor:pointer" OnClick="editarConceptoMovCon('+cve_doc+',\''+modulo+'\',\''+num_doc+'\','+this.ID_DETALLE_COMPROMISO+','+this.ID_PROYECTO+',\''+this.CLV_PARTID+'\','+this.PERIODO+',\''+this.TIPO_MOV+'\',\''+this.IMPORTE+'\')" > <img id="Remover" src="../../imagenes/cross.png" width="16" height="16" style="cursor:pointer" OnClick="eliminarConcepto('+cve_doc+',\''+modulo+'\',\''+num_doc+'\','+this.ID_DETALLE_COMPROMISO+')"></td></tr>'; 
								if(proyectos.indexOf(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']')==-1)
									proyectos.push(this.ID_PROYECTO+ ' ['+this.N_PROGRAMA+']');
								if(partidas.indexOf(this.CLV_PARTID)==-1)
									partidas.push(this.CLV_PARTID);
							});
							html+='</table></div>';
							jWindow(html,'Reducción y Ampliación de Contrato: '+num_doc, '','Cerrar',1);
							
							$('#cmdGuardarMovCon').click(function(event){
								if($('#txtimporteMovCon').val()=='')
								{
									alert('Es necesario escribir el importe');
									return false;
								}
								else
									guardarMovimientoContrato(cve_doc, modulo, num_doc);
							});
							$('#cmdCancelarMovCon').click(function(event){
								$('#divMovListado').show();
								$('#divMovCaptura').hide();
								reduccionAmpliacion(cve_doc, modulo, num_doc);
							});
							$('#cmdAgregarMovCon').click(function(event){
								
								$('#divMovListado').hide();
								$('#divMovCaptura').show();
								$('#cboProyecto').attr('disable', false);
								$('#cboPartidas').attr('disable', false);
								$('#popup_message_window').css('height','300px');
							});
							
							$.each( proyectos, function( index, value ){
									$('#cboProyecto').append('<option value='+value+'>'+value+'</option>');
							});
								$.each( partidas, function( index, value ){
									$('#cboPartidas').append('<option value='+value+'>'+value+'</option>');
							});
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});
		
	}
}

function editarConceptoMovCon(cve_doc, modulo, num_doc, idDetalle, idProyecto, clv_partid, periodo, tipo_mov, importe)
{
	$('#divMovCaptura').show();
	$('#divMovListado').hide();
	$('#popup_message_window').css('height','300px');
	$('#ID_DETALLE').attr('value', idDetalle);
	$('#cboMovimiento').val(tipo_mov);
	$('#cboPeriodo').val(periodo);
	$('#cboProyecto').val(idProyecto);
	$('#cboPartidas').val(clv_partid);
	$('#txtimporteMovCon').attr('value', importe);
	$('#cboProyecto').attr('disabled', true);
	$('#cboPartidas').attr('disabled', true);
}

function eliminarConcepto(cve_doc, modulo, num_doc, idConcepto)
{
	var arrCon = [];
	arrCon.push(idConcepto);
			ControladorContratosRemoto.eliminarConceptosMovPeredo(cve_doc, arrCon, {
				callback:function(items) {
					reduccionAmpliacion(cve_doc, modulo, num_doc);	
			} 					   				
			,
			errorHandler:function(errorString, exception) { 
				jError(errorString, 'Error');          
			}
	});
}

function guardarMovimientoContrato(cve_doc, modulo, num_doc)
{
	ControladorContratosRemoto.guardarConceptoMovPeredo($('#ID_DETALLE').attr('value'), $('#CVE_CONTRATO').attr('value'), $('#cboProyecto').attr('value'),$('#cboPartidas').attr('value'), $('#cboPeriodo').attr('value'), $('#txtimporteMovCon').attr('value'),{
	  callback:function(items) {
				reduccionAmpliacion(cve_doc, modulo, num_doc);
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');     
		}
	});
}

/*funcion para mostrar los documetos comprometidos y precomprometidos*/
function mostrarConsultaCompromiso(idproyecto, proyecto, partida, periodo, consulta){
	if(proyecto==""||partida==""||consulta==""){
		jAlert('Programa y partida no validos', 'Consulta de documentos Comprometidos y Pre-comprometidos'); return false;
	}
	jWindow('<iframe width="680" height="360" id="ventadaCompromisos" frameborder="0" src="../../sam/consultas/muestra_compromisos.action?idproyecto='+idproyecto+'&proyecto='+proyecto+'&partida='+partida+"&periodo="+periodo+'&consulta='+consulta+'"></iframe>','Detalles presupuestales', '','Cerrar',1);
}

//Reacciona a la tecla scape para cerrar los dialogos emergentes
$(document).keyup(function (event) { 
	if(event.keyCode==27) $.alerts._hide();
});

/*funcion para cambiar el color en la entrada a la fila de una tabla*/
function color_over(f){
	row_color = $('#'+f).css("background-color");
	$('#'+f).css("background-color", '#FFCC66');
}

function color_out(f){
	$('#'+f).css("background-color", row_color);
	row_color = "";
}

//funcion para mostrar la bitacora dependiendo el doc.
function bitacoraDocumento(cve_doc, tipo){	
	jWindow('<iframe width="700" height="350" id="ventadaBitacora" frameborder="0" src="../../sam/consultas/muestraBitacora.action?cve_doc='+cve_doc+'&tipo_doc='+tipo+'"></iframe>','Bitacora de Movimientos', '','Cerrar',1);
}
/**funcion para agregar una fila a una tabla en especifico*/
function appendNewRow(table, param){
	var tabla = document.getElementById(table).tBodies[0];
 	var row   = document.createElement("TR");  
	 
	var i=0;
	while(i<=(param.length)-1){
		row.appendChild(param[i]); 
		i++;
	}
	tabla.appendChild(row);
	return tabla;
}

/*Funcion para construit una celda en especifico*/
function Td(texto, estilo, obj, html, colspan ){
		var cell = document.createElement( "TD" );
			cell.style.height='20px';
		if(typeof(colspan)!='undefined') 
			cell.colSpan= colspan;
		if( typeof(estilo) != 'undefined' && estilo != "" )
			cell.style.cssText = estilo;
		if( typeof(html) != 'undefined' && html != "" )
			cell.innerHTML = html;
		else if( typeof(obj) != 'undefined' && obj != "" )
			cell.appendChild( obj );
		else
			cell.appendChild( document.createTextNode( texto ) );
		return cell;
		
}

function formatNumber(num,prefix){
   num= redondeo( num );
   prefix = prefix || '';
   num += '';
   var splitStr = num.split('.');
   var splitLeft = splitStr[0];
   var splitRight = splitStr.length > 1 ? '.' + splitStr[1] : '';
   var regx = /(\d+)(\d{3})/;
   while (regx.test(splitLeft)) {
      splitLeft = splitLeft.replace(regx, ' $1' + ',' + '$2');
   }
    if( splitRight.length == 0 )
	   		splitRight = ".00";
	 else if( splitRight.length == 2 )
	   		splitRight += "0";
   return prefix + splitLeft + splitRight;
}

function quitRow( table ){
	var tabla = document.getElementById(table).tBodies[0];
	var nRows = tabla.rows.length;
	while( tabla.rows.length > 0 ){
		index_table = tabla.rows.length - 1;
		tabla.deleteRow( index_table );
	}
}

function LTrim( value ) {	
	var re = /\s*((\S+\s*)*)/;
	return value.replace(re, "$1");	
}

function RTrim( value ) {	
	var re = /((\s*\S+)*)\s*/;
	return value.replace(re, "$1");	
}

function trim( value ) {	
	return LTrim(RTrim(value));	
}

function upperCase(object) {
  object.value=trim(object.value.toUpperCase());	
}

function keyNumbero( event ){
	var key = ( window.event )? event.keyCode:event.which;
	if( ( key > 47 && key < 58 ) || key == 45 || key == 46 || key == 8 )
		return true;
	else
		return false;
}

function redondeo( valor ) {
	var resultado = Math.round(valor * 100) / 100;
	return resultado;
}

function round(value, exp) {
  if (typeof exp === 'undefined' || +exp === 0)
    return Math.round(value);

  value = +value;
  exp  = +exp;

  if (isNaN(value) || !(typeof exp === 'number' && exp % 1 === 0))
    return NaN;

  // Shift
  value = value.toString().split('e');
  value = Math.round(+(value[0] + 'e' + (value[1] ? (+value[1] + exp) : exp)));

  // Shift back
  value = value.toString().split('e');
  return +(value[0] + 'e' + (value[1] ? (+value[1] - exp) : -exp));
}


function getHTML( param ){
	if( param != null ){
		if( param == "null")
			return "";
		else
			return param;
	}else{
		return "";
	}
}

function rellenaCeros(cad, lng){
	var pattern = "00000000000000000000";
	var result = "";
	 if ( cad=="") return cad; 
	 else 
	 	result = (pattern.substring(0, lng - cad.length) + cad);
	 return result;
}

function ShowDelay(titulo, mensaje){
	 $("#dialog").remove();
	if(typeof(mensaje)=='undefined'||mensaje=='') mensaje = 'Espere un momento porfavor...';
	if(titulo=='undefined'||titulo=='') titulo = 'Procesando';
	jWindow('<strong>&nbsp;<img src="../../imagenes/spinner.gif" width="32" height="32" align="absmiddle" /> '+mensaje+'</strong>', titulo, 0);
}


function CloseDelay(mensaje, seg, fn){
	try{
		_closeDelay();
		if(isNaN(seg)) fn = seg; 
		if(typeof(seg)=='undefined'||seg==0||isNaN(seg)) seg = 3000;
			
		notify(mensaje,500,seg);
		if(typeof(fn)!='undefined') setTimeout('executeX('+fn+')',seg);
	}
	catch(err){
		err=null;
	}
}

function executeX(fn){
		fn();
	}

function _closeDelay(){
	$.alerts._hide();
}

function _closeDialog(){
	$("#dialog").dialog('close');
}

//funcion para ejecutar una funcion al pulsar enter
function keyEnter(fn){
	if (window.event.keyCode==13) {
	 	fn();
	}else{
	 	return false;
	}

}


 //Función que crea las notificaciones
   function notify(msg,speed,fadeSpeed,type){

       //Borra cualquier mensaje existente
       $('.notify').remove();

       //Si el temporizador para hacer desaparecer el mensaje está
       //activo, lo desactivamos.
       if (typeof fade != "undefined"){
           clearTimeout(fade);
       }

       //Creamos la notificación con la clase (type) y el texto (msg)
       $('body').append('<div class="notify '+type+'" style="display:none;position:fixed;left:10"><p>'+msg+'</p></div>');

       //Calculamos la altura de la notificación.
       notifyHeight = $('.notify').outerHeight();

       //Creamos la animación en la notificación con la velocidad
       //que pasamos por el parametro speed
       $('.notify').css('top',-notifyHeight).animate({top:10,opacity:'toggle'},speed);
	   
	   _closeDelay();

       //Creamos el temporizador para hacer desaparecer la notificación
       //con el tiempo almacenado en el parametro fadeSpeed
       fade = setTimeout(function(){

           $('.notify').animate({top:notifyHeight+10,opacity:'toggle'}, speed);
		   

       }, fadeSpeed);

   }