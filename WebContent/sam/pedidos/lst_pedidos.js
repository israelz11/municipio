var modulo = "Pedidos";
$(document).ready(function() {
  $('.tiptip a.button, .tiptip button').tipTip();
  var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	
  $("#fechaInicial").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha});  
  $("#fechaFinal").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha});  
  getBeneficiarios('txtprestadorservicio','CVE_BENEFI','');  
   $('#cmdpdf').click(function (event){mostrarOpcionPDF();});  
   
   $('#ui-datepicker-div').hide(); 
});

function reembolsos(cve_ped, modulo){
	controladorPedidos.getReembolsoPedido(cve_ped, {
						callback:function(items) { 		
							var html = '<table border="0" align="center" cellpadding="1" cellspacing="2" width="400">'+
										  '<tr>'+
											'<td width="100"><strong>Num. Ped:</strong></td>'+
											'<td width="300"><strong>'+rellenaCeros(cve_ped.toString(), 6)+'</strong></td>'+
										  '</tr>'+
										  '<tr>'+
											'<td>&nbsp;</td>'+
											'<td><input type="radio" value="0" id="rdOpcion1" name="rdOpcion1"/>Reembolso Automatico</td>'+
										  '</tr>'+
										  '<tr>'+
											'<td>&nbsp;</td>'+
											'<td><input type="radio" value="1" id="rdOpcion2" name="rdOpcion2"/>Reembolso Personalizado</td>'+
										  '</tr>'+
										   '<tr>'+
											'<td><strong>Reembolso:</strong></td>'+
											'<td><input type="text" id="txtreembolso" value="'+items.REEMBOLSO_LIQ+'" style="width:140px" /></td>'+
										  '</tr>'+
										  '<tr><td><strong>Reembolsado:</strong></td><td>'+formatNumber(items.REEMBOLSO, '$')+'</td></tr>'+
										  '<tr>'+
											'<td>&nbsp;</td>'+
											'<td>&nbsp;</td>'+
										  '</tr>'+
										  '<tr>'+
											'<td colspan="2" align="center"><input type="button" value="Quitar" id="cmdquitar" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Aplicar" id="cmdaplicar" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones" style="width:100px"/></td>'+
										  '</tr>'+
										'</table>';
										
										jWindow(html,'Reembolso a Pedidos', '','',0)
										$('#cmdquitar').click(function(event){_quitarReembolso(cve_ped);});
										$('#cmdaplicar').click(function(event){_reembolsoPedidos(cve_ped);})
										$('#cmdcancelar').click(function(event){$.alerts._hide();})
										$('#txtreembolso').attr('disabled', true);
										$('#rdOpcion1').click(function(event){ 
											if ($(this).is(':checked'))
											{
												$('#txtreembolso').attr('disabled', true);
											}
											$('#rdOpcion2').attr('checked', false);
										});
										
										$('#rdOpcion2').click(function(event){ 
											if ($(this).is(':checked'))
											{
												$('#txtreembolso').attr('disabled', false);
												$('#txtreembolso').focus();
											}
											$('#rdOpcion1').attr('checked', false);
										});
										
										if(parseFloat(items.REEMBOLSO)>0)
										{
											$('#cmdaplicar').attr('disabled', true);
											$('#cmdquitar').attr('disabled', false);
										}
										else{
											$('#cmdaplicar').attr('disabled', false);
											$('#cmdquitar').attr('disabled', true);
										}
											
										$('#rdOpcion1').attr('checked', true);
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, "Error");          
					 }
	});
}

function _quitarReembolso(cve_ped){
	jConfirm('¿Confirma que desea quitar el reembolso del Pedido?','Quitar Reembolso', function(r){
			if(r){
					ShowDelay("Quitando reembolso");
					controladorPedidos.quitarReembolso(cve_ped, {
										callback:function(items){
											var cve = cve_ped;
												CloseDelay('Reembolso quitado con éxito');
										} 					   				
									 ,
									 errorHandler:function(errorString, exception) { 
										jError(errorString, "Error");          
									 }
					});
			}
	});
}

function _reembolsoPedidos(cve_ped){
	var operacion;
	if($('#rdOpcion1').is(':checked'))
	{
		operacion = 0;
	}
	if($('#rdOpcion2').is(':checked')){
		operacion = $('#txtreembolso').attr('value');
	}
	
	if(parseFloat($('#txtreembolso').attr('value'))==0.00){
		jAlert('No se puede aplicar un reeembolso valido a este documento'); return false;
	}
	
	jConfirm('¿Confirma que desea guardar el reembolso del Pedido?','Guardar Reembolso', function(r){
			if(r){
					controladorPedidos.guardarReembolsoPedido(cve_ped, operacion, {
										callback:function(items){
												CloseDelay('Reembolso guardado con éxito');
										} 					   				
									 ,
									 errorHandler:function(errorString, exception) { 
										jError(errorString, "Error");          
									 }
					});
			}
		});
}

function mostrarOpcionPDF(){
	var html = '<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="405" >'+
				'  <tr id="x1" onmouseover="color_over(\'x1\')" onmouseout="color_out(\'x1\')"> '+
				'	<td width="33" height="27" align="center" style="cursor:pointer" onclick="getListadoPedidos()"> '+
				'	  <img src="../../imagenes/pdf.gif"/></td>' +
				'	<td width="362" height="27" align="left" style="cursor:pointer" onclick="getListadoPedidos()">&nbsp;Listado Nomal de Pedidos en PDF</td> '+
				'  </tr> '+
				
				'  <tr id="x2" onmouseover="color_over(\'x2\')" onmouseout="color_out(\'x2\')" onclick=""> '+
				'	  <td height="27" align="center"  style="cursor:pointer" onclick="getListadoPedConOp()"><img src="../../imagenes/pdf.gif" /></td> '+
				'	  <td height="27" align="left" style="cursor:pointer" onclick="getListadoPedConOp()">&nbsp;Listado de Pedidos con Ordenes de Pago relacionadas en PDF</td> '+
				'	</tr> '+
				'</table>';
	jWindow(html,'Opciones de Reporte', '','Cerrar',1);
}

function getListadoPedConOp(){
	$('#forma').attr('target',"impresionlistadoPedConOP");
	$('#forma').attr('action',"../reportes/rpt_listado_pedidos.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_pedidos.action");
}

function getListadoPedidos(){
	var checkStatus = [];
    $('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});
	if (checkStatus.length==0 )  {jAlert('Debe de seleccionar un Estatus de Pedido', titulo); return false;}
	
	$('#forma').attr('target',"impresionlistado");
	$('#forma').attr('action',"../reportes/rpt_listado_pedidos.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_pedidos.action");
}

function getPedidos(){
	var checkStatus = [];
    $('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});	 
	var error="";
	var cont=0;
	var dependencia = $('#cbodependencia').attr('value');
	
	if(typeof(dependencia)=="undefined") dependencia = $('#cbodependencia2').attr('value');
	var titulo ='Error de validacion';
	if (checkStatus.length==0 )  {jAlert('Debe de seleccionar un Estatus de Pedido', titulo); return false;}
	if ($('#fechaInicial').attr('value')=="" && $('#fechaFinal').attr('value')!="" || $('#fechaInicial').attr('value')!="" && $('#fechaFinal').attr('value')=="")  {jAlert('El rango de fechas no es valido', titulo); return false;}
	//var s = 'lst_pedidos.action?idUnidad='+$('#cbodependencia').attr('value')+"&fechaInicial="+$('#fechaInicial').attr('value')+"&fechaFinal="+$('#fechaFinal').attr('value')+"&status="+checkStatus+"&tipo_gto="+$('#cbotipogasto').attr('value');
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_pedidos.action");
	$("#forma").submit();
	//document.location = s;
}

function reactivarPedido(cve_ped){
	jConfirm('¿Confirma que desea reactivar el Pedido?','Reactivar Pedidos', function(r){
			if(r){
					ShowDelay('Reactivando Pedido','');
					 controladorPedidos.reactivarPedido(cve_ped, {
						callback:function(items) { 		
						  CloseDelay('Pedido reativado con éxito', 3000, function(){
							  		getPedidos();
							  });

					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
				    });
			}
	   });	 
}

function editarPedido(cve_ped, status){
	var estado ="";
	ShowDelay('Abriendo Pedido...', '');
	if(status==1) estado = "PENDIENTE";
	if(status==3) estado = "CANCELADO";
	if(status==5) estado = "SURTIDO"; 
	if(status==0) document.location = 'capturarPedidos.action?cve_ped='+cve_ped;
	else jAlert('El Pedido actualmente se encuentra '+estado+', usted no esta  autorizado para editar el pedido', 'Advertencia');
}

function getReportePedido(clavePed) {
$('#clavePedido').attr('value',clavePed);
$('#forma').attr('target',"impresion_pedido");
$('#forma').attr("action", "../reportes/rpt_pedido.action");
$('#forma').submit();
$('#forma').attr('target',"");
$('#forma').attr('action',"lst_pedidos.action");
}


/*Metodo para aperturar las requisiciones*/
function aperturarPedidos(){
	 var checkClaves = [];
     $('input[name=chkpedidos]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea aparturar los Pedidos seleccionados?','Aperturar Pedido', function(r){
			if(r){
					 controladorPedidos.aperturarPedidos(checkClaves, {
						callback:function(items) { 		
							if(items)
							  CloseDelay('Pedidos  aperturados con exito', 3000, function(){
							  		getPedidos();
							  });
						  
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString,'Error'); 
						         
					 }
				    });
			}
	   },async=false );
	 
	 } 
	else 
	    jAlert('Es necesario que seleccione por lo menos un pedidos del listado', 'Advertencia');
}


function cancelarPedido(idPedido){
	var checkClaves = [];
	checkClaves.push(idPedido);
		jConfirm('¿Confirma que desea cancelar el Pedido?','Cancelar Pedidos', function(r){
			if(r){
					ShowDelay('Cancelando Pedido','');
					 controladorPedidos.cancelarPedido(idPedido, {
						callback:function(items) { 		
						  CloseDelay('Pedido cancelado con éxito', 3000, function(){
							  		getPedidos();
							  });
						  
						   
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
				    });
			}
	   },async=false );	 

}

function cancelarPedidoMultiples(){
	 var checkClaves = [];
     $('input[name=chkpedidos]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea cancelar el Pedido?','Cancelar Pedidos', function(r){
			if(r){
					ShowDelay('Cancelando Pedido','');
					 controladorPedidos.cancelarPedido(checkClaves, {
						callback:function(items) { 		
						  CloseDelay('Pedidos cancelados con éxito', 3000, function(){
							  		getPedidos();
							  });
						  
						   
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');        
					 }
				    });
			}
	   },async=false );	 
	}
	else
		jAlert('Es necesario que seleccione por lo menos un pedidos del listado', 'Advertencia');
	 

}