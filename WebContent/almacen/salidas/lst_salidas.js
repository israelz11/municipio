var guardado = false;
var folio_salida = 0;

$(document).ready(function() {
  var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	
  $('#cmdbuscar').click(function(event){iniciarBusqueda();});
  $("#txtfechaInicial").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha});  
  $("#txtfechaFinal").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha}); 
  $('#cmdautorizar').click(function(event){autorizarSalidas();});
  $('#cmdinvalidar').click(function(event){invalidarSalidas();});
  $('#cbodependencia').change(function (event){cargaraAlmacenes($('#cbodependencia').val());});
  getBeneficiarios('txtbeneficiario','ID_PROVEEDOR');
});

function iniciarBusqueda(){
	if($('#txtbeneficiario').attr('value')=='') $('#ID_PROVEEDOR').attr('value', '0');
	if($('#txtfechaInicial').attr('value')!=''&&$('#txtfechaFinal').attr('value')==''||$('#txtfechaFinal').attr('value')!=''&&$('#txtfechaInicial').attr('value')=='') {jAlert('El rango de fecha seleccioando para la busqueda no es valido', 'Advertencia'); return false;}
	var s = '?cbodependencia='+$('#cbodependencia').attr('value')+'&id_almacen='+$('#cboalmacen').attr('value')+"&fechaInicial="+$('#txtfechaInicial').attr('value')+"&fechaFinal="+$('#txtfechaFinal').attr('value')+"&id_tipo_documento="+$('#cbotipodocumento').attr('value')+"&id_proveedor="+$('#ID_PROVEEDOR').attr('value')+"&id_pedido="+$('#txtpedido').attr('value')+"&proyecto="+$('#txtproyecto').attr('value')+"&partida="+$('#txtpartida').attr('value')+"&num_documento="+$('#txtdocumento').attr('value')+"&folio="+$('#txtfolio').attr('value');
	document.location = s;
}

function editarDocumento(idEntrada, idSalida){
	document.location = "salidas.action?id_entrada="+idEntrada+"&id_salida="+idSalida;
	//jWindow('<iframe width="700" height="500" name="salidasAlmacen" id="salidasAlmacen" frameborder="0" src="salidas.action?id_entrada='+idEntrada+'&id_salida='+idSalida+'"></iframe>','Nueva Salida de Almacen', '','',0);
}

function cambiarValores(folio){
	guardado = true;
	folio_salida = folio;
	
}

function compruebaVariable(){
	if(guardado)
		document.location = "lst_salidas.action?folio="+folio_salida;
	else
		_closeDelay();
		
}

function cargaraAlmacenes(idDependencia) {
     dwr.util.removeAllOptions("cboalmacen");
	 ControladorListadoSalidasRemoto.getAlmacenes(idDependencia, {
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

function getReporteSalida(id_salida) {
	$('#ID_SALIDA').attr('value', id_salida);
	$('#frmreporte').attr('action',"../reportes/salidas.action")
	$('#frmreporte').attr("target","impresionSalida");
	$('#frmreporte').submit();
    $('#frmreporte').attr("target","");
	$('#frmreporte').attr('action',"lst_salidas.action");
}

function getReportePedido(cve_ped)
{
	$('#clavePedido').attr('value',cve_ped);
	$('#frmreporte').attr('target',"impresion_pedido");
	$('#frmreporte').attr("action", "../../sam/reportes/rpt_pedido.action");
	$('#frmreporte').submit();
	$('#frmreporte').attr('target',"");
	$('#frmreporte').attr('action',"../reportes/lst_salidas.action");
}

function getRequisicion(claveReq)   {
	$('#claveRequisicion').attr('value',claveReq);
	$('#frmreporte').attr('target',"impresion");
	$('#frmreporte').attr('action',"../../sam/reportes/requisicion.action");
	$('#frmreporte').submit();
	$('#frmreporte').attr('target',"");
	$('#frmreporte').attr('action',"lst_salidas.action");
}

function autorizarSalidas(){
	 var checkClaves = [];
     $('input[name=chksalidas]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea autorizar para entrega la(s) salida(s) seleccionada(s)?','Confirmar', function(r){
			if(r){
					ShowDelay('Autorizando Salida(s)','');
					ControladorListadoSalidasRemoto.autorizarSalidas(checkClaves, {
						callback:function(items) { 		
						 	CloseDelay('Salida(s) autorizada(s) con éxito');
						 	iniciarBusqueda();
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
	    jAlert('Es necesario seleccionar por lo menos una Salida del listado', 'Advertencia');
}

function invalidarSalidas(){
	 var checkClaves = [];
     $('input[name=chksalidas]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea invalidar la(s) salida(s) seleccionada(s)?','Confirmar', function(r){
			if(r){
					ShowDelay('Invalidando Salida(s)','');
					ControladorListadoSalidasRemoto.invalidarSalidas(checkClaves, {
						callback:function(items) { 		
						 	CloseDelay('Salida(s) invalidada(s) con éxito');
						 	iniciarBusqueda();
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
	    jAlert('Es necesario seleccionar por lo menos una Salida del listado', 'Advertencia');
}

function cancelarDocumento(id_documento){
		jConfirm('¿Confirma que desea cancelar la salida actual?','Cancelar salida', function(r){
			if(r){
					 ControladorListadoSalidasRemoto.cancelarDocumento(id_documento, {
						callback:function(items) {
							CloseDelay('Salida cancelada con exito');
							 iniciarBusqueda();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
				    });
			}
	   });	 

}