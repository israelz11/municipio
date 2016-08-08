var guardado = false;
var folio_salida = 0;

$(document).ready(function() {
  var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	
  $('#cmdbuscar').click(function(event){iniciarBusqueda();});
  $("#txtfechaInicial").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha});  
  $("#txtfechaFinal").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha}); 
  $('#cbodependencia').change(function (event){cargaraAlmacenes($('#cbodependencia').val());});
  getBeneficiarios('txtbeneficiario','ID_PROVEEDOR');
});

function iniciarBusqueda(){
	if($('#txtbeneficiario').attr('value')=='') $('#ID_PROVEEDOR').attr('value', '0');
	if($('#txtfechaInicial').attr('value')!=''&&$('#txtfechaFinal').attr('value')==''||$('#txtfechaFinal').attr('value')!=''&&$('#txtfechaInicial').attr('value')=='') {jAlert('El rango de fecha seleccioando para la busqueda no es valido', 'Advertencia'); return false;}
	var s = '?cbodependencia='+$('#cbodependencia').attr('value')+'&id_almacen='+$('#cboalmacen').attr('value')+"&fechaInicial="+$('#txtfechaInicial').attr('value')+"&fechaFinal="+$('#txtfechaFinal').attr('value')+"&id_tipo_documento="+$('#cbotipodocumento').attr('value')+"&id_proveedor="+$('#ID_PROVEEDOR').attr('value')+"&id_pedido="+$('#txtpedido').attr('value')+"&proyecto="+$('#txtproyecto').attr('value')+"&partida="+$('#txtpartida').attr('value')+"&num_documento="+$('#txtdocumento').attr('value')+"&folio="+$('#txtfolio').attr('value');
	document.location = s;
}

function cargaraAlmacenes(idDependencia) {
     dwr.util.removeAllOptions("cboalmacen");
	 ControladorListadoSalidasAutorizacionRemoto.getAlmacenes(idDependencia, {
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

function crearSalida(id_entrada){
	var id_salida = 0;
	document.location='salidas.action?id_entrada='+id_entrada+'&id_salida='+id_salida;
	//jWindow('<iframe width="700" height="500" name="salidasAlmacen" id="salidasAlmacen" frameborder="0" src="salidas.action?id_entrada='+id_entrada+'&id_salida='+id_salida+'"></iframe>','Nueva Salida de Almacen', '','',0);
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

function getReporteEntrada(id_entrada) {
	$('#ID_ENTRADA').attr('value', id_entrada);
	$('#frmreporte').attr("target","impresion");
	$('#frmreporte').submit();
    $('#frmreporte').attr("target","");

}

function getReportePedido(cve_ped)
{
	$('#clavePedido').attr('value',cve_ped);
	$('#frmreporte').attr('target',"impresion_pedido");
	$('#frmreporte').attr("action", "../../sam/reportes/rpt_pedido.action");
	$('#frmreporte').submit();
	$('#frmreporte').attr('target',"");
	$('#frmreporte').attr('action',"../reportes/entradas.action");
}