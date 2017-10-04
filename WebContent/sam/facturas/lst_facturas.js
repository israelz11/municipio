
$(document).ready(function() {
	
	var imagen="../../imagenes/cal.gif";	
	var formatFecha="dd/mm/yy";
  
//Para seleccionar todos los checkbox Abraham Gonzalez 12/07/2016

  $('#cmdbuscar').click(function(event){iniciarBusqueda();});
  $('#cmdaperturar').click(function(event){aperturarFacturas();});
  $('#cmdcancelar').click(function(event){cancelarFacturas();})
  $("#txtfechaInicial").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha});  
  $("#txtfechaFinal").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha}); 
  $('#ui-datepicker-div').hide();
  getBeneficiarios('txtbeneficiario','clv_benefi');

  
  
  //Para seleccionar todos los checkbox Abraham Gonzalez 12/07/2016
  $('#checkTodos').click( function (event){ $('input[name=chkfacturas]').attr('checked', this.checked); });
});





function iniciarBusqueda(){
	if($('#txtbeneficiario').attr('value')=='') $('#clv_benefi').attr('value', '0');
	if($('#txtfechaInicial').attr('value')!=''&&$('#txtfechaFinal').attr('value')==''||$('#txtfechaFinal').attr('value')!=''&&$('#txtfechaInicial').attr('value')=='') {jAlert('El rango de fecha seleccioando para la busqueda no es valido', 'Advertencia'); return false;}
	$('#frmreporte').submit();
	//var s = '?cbodependencia='+$('#cbodependencia').attr('value')+'&id_almacen='+$('#cboalmacen').attr('value')+"&fechaInicial="+$('#txtfechaInicial').attr('value')+"&fechaFinal="+$('#txtfechaFinal').attr('value')+"&id_tipo_documento="+$('#cbotipodocumento').attr('value')+"&id_proveedor="+$('#ID_PROVEEDOR').attr('value')+"&id_pedido="+$('#txtpedido').attr('value')+"&proyecto="+$('#txtproyecto').attr('value')+"&partida="+$('#txtpartida').attr('value')+"&num_documento="+$('#txtdocumento').attr('value')+"&folio="+$('#txtfolio').attr('value');
	//document.location = s;
}

function mostrarReemplazarArchivosFactura(cve_factura, num_fac){
	jWindow("",'Archivos de factura: '+num_fac, '','Cerrar',1);
}

function getAnexosListaFactura(cve_factura){
	ShowDelay('Cargando Anexos escaneados de la factura','');
	 controladorListadoFacturasRemoto.getListaAnexosArchivosFactura(cve_factura, {
				callback:function(items) { 
					_closeDelay();		
					var html = '<table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listas" id="listasDocumentos">'+
								'	<thead>'+
								'	  <tr >'+
								'		<th width="10%"  align="center" height="20">Tipo</th>'+
								'		<th width="10%" align="center">Número</th>'+
								'		<th width="55%"  align="center">Nota</th>'+
								'		<th width="20%"  align="center">Archivo</th>'+
								'		<th width="5%"  align="center">Opc.</th>'+
								'	</tr>'+
								'	  </thead>'+
								'	<tbody>'+
								'	  </tbody>'+
								'	</table>';
					jWindow(html,'Anexos de Orden de Pago: '+cve_op, '','Cerrar',1);
					jQuery.each(items,function(i) {
						appendNewRow("listasDocumentos", [Td('', centro , '', this.DESCR),
								 Td('', centro , '', this.NUMERO),
								 Td('', izquierda , '', this.NOTAS),
								 Td('', izquierda , '', "<strong><a href='"+this.FILEPATH+this.FILENAME+"' target='_blank'>"+getHTML(this.FILENAME)+"</a></strong>"),
								 Td('', centro , '', (this.FILENAME==null) ? "":"<a href='"+this.FILEPATH+this.FILENAME+"' target='_blank'><img src='../../imagenes/application_view_tile.png'></a>")
								 
						]);
						
				});
				
			 } 					   				
			 ,
			 errorHandler:function(errorString, exception) { 
				jError(errorString, 'Error');          
			 }
	});
}


function cancelarFacturas()
{
	var checkClaves = [];
     $('input[name=chkfacturas]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea cancelar las facturas seleccionadas?','Cancelar Facturas', function(r){
			if(r){
					 controladorListadoFacturasRemoto.cancelarFacturas(checkClaves, {
						callback:function(items) { 		
							  CloseDelay('Facturas  canceladas con exito');
							  		iniciarBusqueda();
						  
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
	    jAlert('Es necesario que seleccione por lo menos una factura del listado', 'Advertencia');
}


function cancelarFactura(id)
{
	var checkClaves = [];
	checkClaves.push(id);
	
     //$('input[name=chkfacturas]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea cancelar la factura?','Cancelar Factura', function(r){
			if(r){
					 controladorListadoFacturasRemoto.cancelarFacturas(checkClaves, {
						callback:function(items) { 		
							  CloseDelay('Facturas  cancelada con exito');
							  		iniciarBusqueda();
						  
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
	    jAlert('Es necesario que seleccione por lo menos una factura del listado', 'Advertencia');
}

function aperturarFacturas()
{
	var checkClaves = [];
     $('input[name=chkfacturas]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea aparturar las facturas seleccionadas?','Aperturar Facturas', function(r){
			if(r){
					 controladorListadoFacturasRemoto.aperturarFacturas(checkClaves, {
						callback:function(items) { 		
							  CloseDelay('Facturas  aperturadas con exito');
							  		iniciarBusqueda();
						  
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
	    jAlert('Es necesario que seleccione por lo menos una factura del listado', 'Advertencia');
}

function editarFactura(cve_factura)
{
	document.location = "captura_factura.action?CVE_FACTURA="+cve_factura;
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

