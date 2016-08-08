// JavaScript Document
$(document).ready(function() {
  var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	
  $("#fechaInicial").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha});  
  $("#fechaFinal").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha});    
  
  $('#ui-datepicker-div').hide();
});


function getRequisiciones(){
	 var checkStatus = [];
     $('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});	 
	var error="";
	var cont=0;
	var titulo ='Error de validacion';
	if (checkStatus.length==0 )  {jAlert('Debe de seleccionar un Estatus de requisicion', titulo);}
	if ($('#fechaInicial').attr('value')=="" && $('#fechaFinal').attr('value')!="" || $('#fechaInicial').attr('value')!="" && $('#fechaFinal').attr('value')==""  )  {jAlert('El rango de fechas no es valido', titulo); return false;}
	var s = 'lst_req_captura.action?dependencia='+$('#cbodependencia').attr('value')+"&tipo="+$('#cbotipo').attr('value')+"&fechaInicial="+$('#fechaInicial').attr('value')+"&fechaFinal="+$('#fechaFinal').attr('value')+"&status="+checkStatus;
	//document.location = s;
	$('#forma').submit();
}

function getRequisicion(claveReq)   {
	$('#claveRequisicion').attr('value',claveReq);
	$('#forma').attr('target',"impresion");
	$('#forma').attr('action',"../reportes/requisicion.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_req_total.action");
}

function editarRequisicion(cve_req){
	document.location = 'capturarRequisicion.action?cve_req='+cve_req;
}

function consultarRequisicion(cve_req){
	document.location = 'consultaRequisicion.action?cve_req='+cve_req+'&accion=0';
}

function crearPedido(cve_req){	
	document.location = 'capturarPedidos.action?claveRequisicion='+cve_req;
	
}