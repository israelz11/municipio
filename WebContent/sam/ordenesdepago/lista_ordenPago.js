$(document).ready(function() {
	$('.tiptip a.button, .tiptip button').tipTip();
  var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	
  $("#fechaInicial").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha});  
  $("#fechaFinal").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha});   
  //$('#cmdmover').click(function(event){moverOrdenpago();}); 

	
  getBeneficiarios('txtprestadorservicio','CVE_BENEFI','');
  $('#ui-datepicker-div').hide();
});


function mostrarCargarArchivosOrdenPago(cve_op, num_op){
	jWindow('<iframe width="750" height="350" name="ventanaArchivosOP" id="ventanaArchivosOP" frameborder="0" src="../../sam/ordenesdepago/muestra_anexosOPArchivos.action?cve_op='+cve_op+'"></iframe>','Archivos de Orden de Pago: '+num_op, '','Cerrar',1);
}

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

function getAnexosListaOP(cve_op){
	jWindow('<iframe width="750" height="350" name="ventanaArchivosOP" id="ventanaArchivosOP" frameborder="0" src="../../sam/consultas/muestra_anexosOP.action?cve_op='+cve_op+'"></iframe>','Listado de Anexos de OP: '+cve_op, '','Cerrar',1);
}

function getListadoOrdenPago(){
	var checkStatus = [];
     $('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});	 
	 if (checkStatus.length==0 )   {jAlert('Es necesario seleccionar al menos un status de Orden de Pago', 'Advertencia'); return false;}
	 
	$('#forma').attr('target',"impresionlistado");
	$('#forma').attr('action',"../reportes/rpt_listado_op.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lista_ordenPago.action");

}

function getOrden(){
	var checkStatus = [];
     $('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});	 
	 if (checkStatus.length==0 )   {jAlert('Es necesario seleccionar al menos un status de Orden de Pago', 'Advertencia'); return false;}
	 if ($('#fechaInicial').attr('value')=="" && $('#fechaFinal').attr('value')!="" || $('#fechaInicial').attr('value')!="" && $('#fechaFinal').attr('value')=="")  {jAlert('El rango de fechas no es válido'); return false;}
	 var s = 'lista_ordenPago.action?idUnidad='+$('#cbodependencia').attr('value')+"&fechaInicial="+$('#fechaInicial').attr('value')+"&fechaFinal="+$('#fechaFinal').attr('value')+"&status="+checkStatus+"&tipo_gto="+$('#cbotipogasto').val();
	 
	$("#forma").submit();
}

function editarOP(cve_op){
	ShowDelay('Abriendo Orden de Pago...', '');
	document.location = "orden_pago.action?cve_op="+cve_op+"&accion=edit";
}

function getReporteOP(clave) {
	_closeDelay();
	$('#cve_op').attr('value',clave);
	$('#forma').attr('action',"../reportes/formato_orden_pago.action");
	$('#forma').attr('target',"impresion");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lista_ordenPago.action");
}

function aperturarOrden(){
	 var checkClaves = [];
     $('input[name=chkordenes]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea aperturar las Ordenes de Pago seleccionadas?','Confirmar', function(r){
			if(r){
					ShowDelay('Aperturando Ordenes de Pago','');
					 controladorOrdenPagoRemoto.aperturarOrdenes(checkClaves, {
						callback:function(items) { 		
						  CloseDelay('Ordenes de Pago aperturadas con éxito', 2000, function(){
							  		 getOrden();
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
	    jAlert('Es necesario que seleccione por lo menos una Orden de Pago del listado', 'Advertencia');
}


function cancelarOrden(idOrden)
{
	
	if (idOrden!=0){
		jWindow('¿Confirma que desea cancelar la orden de pago seleccionada?<br><br><strong>*Escriba el motivo: </strong><br><textarea id="txtmotivo" style="width:500px; height:100px"></textarea><br><br><div align="center"><input style="width:100px" class="botones" value="Aceptar" id="cmdaceptarBoton" type="button">&nbsp;<input style="width:100px" class="botones" value="cancelar" id="popup_cancel" type="button"></div>','Cancelar Orden(es) de Pago' ,"Aceptar", "Cancelar", 0);
		$('#cmdaceptarBoton').click(function(event){_cancelarOrden(idOrden);});
	}
	else
		jAlert('Es necesario que seleccione por lo menos una Orden de Pago para realizar esta acción', 'Advertencia');
}

function _cancelarOrden(idOrden){
	var checkClaves = [];
	var motivo = $('#txtmotivo').val();
	checkClaves.push(idOrden);
	if(motivo=="")
	{
		jAlert('Es necesario escrtibir un motivo de cancelación', 'Advertencia');
		return false;		
	}
		/*jConfirm('¿Confirma que desea cancelar la Orden de Pago?','Confirmar', function(r){
			if(r){*/
				ShowDelay('Cancelando Orden de Pago','');
					 controladorOrdenPagoRemoto.cancelarOrden(checkClaves, motivo, {
						callback:function(items) { 		
						  CloseDelay('La Orden de Pago fue cancelada con éxito', function(){
								getOrden();
							}); 
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');      
					 }
				   /* });
			}*/
	   },async=false );	 
}

function cancelacionMultiple(){
	var checkClaves = [];
	$('input[name=chkordenes]:checked').each(function() { checkClaves.push($(this).val());});	
	if (checkClaves.length>0){
		jWindow('¿Confirma que desea cancelar la(s) ordenes de pago seleccionada(s)?<br><br><strong>*Escriba el motivo: </strong><br><textarea id="txtmotivo" style="width:500px; height:100px"></textarea><br><br><div align="center"><input style="width:100px" class="botones" value="Aceptar" id="cmdaceptarBoton" type="button">&nbsp;<input style="width:100px" class="botones" value="cancelar" id="popup_cancel" type="button"></div>','Cancelar Orden(es) de Pago' ,"Aceptar", "Cancelar", 0);
		$('#cmdaceptarBoton').click(function(event){_cancelacionMultiple(checkClaves);});
	}
	else
		jAlert('Es necesario que seleccione por lo menos una Orden de Pago para realizar esta acción', 'Advertencia');
}

function _cancelacionMultiple(checkClaves)
{
	var motivo = $('#txtmotivo').val();
	if(motivo=="")
	{
		jAlert('Es necesario escrtibir un motivo de cancelación', 'Advertencia');
		return false;		
	}
	
	ShowDelay('Cancelando Orden de Pago','');
	 controladorOrdenPagoRemoto.cancelarOrden(checkClaves,motivo,{
		callback:function(items) { 		
			  CloseDelay('Orden de Pago cancelados con éxito', function(){
						getOrden();
			  });
		 } 					   				
		 ,
		 errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');
		 }
	});	 
}