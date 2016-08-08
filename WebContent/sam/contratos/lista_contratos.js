$(document).ready(function() {
  var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	
  $("#fechaInicial").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha});  
  $("#fechaFinal").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha});   
  $('#cmdaperturar').click(function(event){aperturarContrato();}); 
  $('#cmdcancelar').click(function(event){cancelarContrato();});
  $('#cmdbuscar').click(function(event){getContratos();})
  $('[id=Cancelarcontra]').click(function(event){cancelarContrato();});
  
  getBeneficiarios('txtprestadorservicio','CLV_BENEFI','');
	$('#ui-datepicker-div').hide();
});

function getContratos(){
	var checkStatus = [];
     $('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});	 
	 if (checkStatus.length==0 )   {jAlert('Es necesario seleccionar por lo menos un status de Contrato', 'Advertencia'); return false;}
	 if ($('#fechaInicial').attr('value')=="" && $('#fechaFinal').attr('value')!="" || $('#fechaInicial').attr('value')!="" && $('#fechaFinal').attr('value')=="")  {jAlert('El rango de fechas no es válido'); return false;}
	 var s = 'lista_contratos.action?idUnidad='+$('#cbodependencia').attr('value')+"&fechaInicial="+$('#fechaInicial').attr('value')+"&fechaFinal="+$('#fechaFinal').attr('value')+"&status="+checkStatus+"&tipo_gto="+$('#cbotipogasto').val();
	$("#forma").submit();

}

function mostrarOpcionCONPDF(anexo, cve_con){
	var html = '<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="405" >'+
				'  <tr id="x1" onmouseover="color_over(\'x1\')" onmouseout="color_out(\'x1\')"> '+
				'	<td width="33" height="27" align="center" style="cursor:pointer" onclick="getReporteCON('+cve_con+')"> '+
				'	  <img src="../../imagenes/pdf.gif"/></td>' +
				'	<td width="362" height="27" align="left" style="cursor:pointer" onclick="getReporteCON('+cve_con+')">&nbsp;Reporte Normal de Contrato</td> '+
				'  </tr> '+
				(anexo!="" ?
					html+'<tr id="x2" onmouseover="color_over(\'x2\')" onmouseout="color_out(\'x2\')" onclick=""> '+
					'	  <td height="27" align="center"  style="cursor:pointer"><a href="archivos/'+anexo+'" target="_blank"><img src="../../imagenes/pdf.gif" /></a></td> '+
					'	  <td height="27" align="left" style="cursor:pointer">&nbsp;<a href="archivos/'+anexo+'" target="_blank">Anexos de Contrato</a></td> '+
					'	</tr> '
					:
					''
				);
			html+='</table>';
	jWindow(html,'Opciones de Reporte Contratos', '','Cerrar',1);
}

function getAnexosListaCON(anexo)
{
	window.open(anexo, "windowsCONAnexo");
}

function editarCON(cve_contrato){
	ShowDelay('Abriendo Contrato', '');
	document.location = "cap_contratos.action?cve_contrato="+cve_contrato;
}

function getReporteCON(clave) {
	$('#cve_contrato').attr('value',clave);
	$('#forma').attr('action',"../reportes/rpt_contrato.action");
	$('#forma').attr('target',"impresion");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"");
	}
	
function aperturarContrato(){
	 var checkClaves = [];
     $('input[name=chkcontratos]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea aperturar las Contratos seleccionados?','Confirmar', function(r){
			if(r){
					ShowDelay('Aperturando Contratos','');
					 controladorListadoContratosRemoto.aperturarContratos(checkClaves, {
						callback:function(items) { 
										  CloseDelay('Contratos aperturados con éxito', 2000, function(){
													 getContratos();
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
	    jAlert('Es necesario que seleccionar por lo menos un Contrato del listado', 'Advertencia');
}


function cancelarContrato(){
	 var checkClaves = [];
     $('input[name=chkcontratos]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea cancelar el Contrato?','Confirmar', function(r){
			if(r){
				ShowDelay('Cancelando contrato','');
					 controladorListadoContratosRemoto.cancelarContrato(checkClaves, {
						callback:function(items) { 	
							  CloseDelay('Contrato cancelado con exito', 2000, function(){
									getContratos();
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
	    jAlert('Es necesario que seleccionar por lo menos un Contrato del listado', 'Advertencia');

}