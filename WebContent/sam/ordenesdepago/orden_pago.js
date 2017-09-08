var contrato = false;
var ID_DEPENDENCIA=0;

/* Carga inicial del los metodos utilizados */
$(document).ready(function(){
	/*var options = { 
        beforeSubmit:  showRequest,  
        success:       showResponse, 
        url:       '_subirArchivoAnexoOP.action',
        type:      'post', 
        dataType:  'json'
    }; 
	
	$('#forma').submit(function(){
	$(this).ajaxSubmit(options);
	return false;
	});
	
  $('#importeRetencion').click(function (){
	  alert('La retencion debe ser capturada desde el devengado, favor de cancelar y volver a capturar el devengado');
  });	
  $('#BorraOs2').click(function(event){guardarAnexos();});
  $('.tiptip a.button, .tiptip button').tipTip();
  $('#txtproyecto').bestupper(); 
  $('#tabsOrdenesEnca').hide();
  $("#fecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
  /*$('#tabsOrdenes').tabs(
  		{
			select: function(event, ui) {
				//comprobarContrato();
		}
	});  
	
	*/
	
  /*$('#txtpartida').keypress(function(event){return keyNumbero(event);});
  $('#img_presupuesto').click(function(event){muestraPresupuesto();});
  $('#img_contrato').click(function(event){jInformation('Este modulo se encuentra deshabilitado por el momento','Información');});
  $('#img_vale').click(function(event){muestraVales();});
  $('#img_quitar_contrato').click(function(event){jInformation('Este modulo se encuentra deshabilitado por el momento','Información');});
  //$('#img_quitar_vale').click(function(event){removerVale();});
  
  $('#cmdNuevaRetencion').click(function(event){limpiarRetenciones();});
  $('#cmdNuevoAnexo').click(function(event){limpiarAnexos();});
  $('#txtproyecto').blur(function(event){  __getPresupuesto($('#ID_PROYECTO').attr('value'), $('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible',$('#tipoGasto').attr('value'));});	
  $('#txtpartida').blur(function(event){  __getPresupuesto($('#ID_PROYECTO').attr('value'), $('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible',$('#tipoGasto').attr('value'));});  
  //getBeneficiarios('xBeneficiario','xClaveBen',$('#tipoBeneficiario').attr('value'));
  llenarTablaDeOrdenes(); 

  /*Buscar orden de pago si se puede editar
  if(($('#cve_op').attr('value')!=''||$('#cve_op').attr('value')!='0')&&($('#accion').attr('value')=='edit')){
	  buscarOrden($('#cve_op').attr('value'));  
  }
  //$('#tabsOrdenes').tabs('select', 0); 
  $('#fila_contrato').hide();
  $('#ui-datepicker-div').hide();
  
  /*$('#tipoMovDoc').change(function(event){
	  if($(this).val()=='XML')
	  	$('#div_archivo').html('<input type="file" class="input-file" id="archivo" name="archivo" style="width:445px" accept="text/xml" />');
	  else if($(this).val()!='VAR')
	  	$('#div_archivo').html('<input type="file" class="input-file" id="archivo" name="archivo" style="width:445px" accept="application/pdf"/>');
	  else
	   $('#div_archivo').html('<input type="file" class="input-file" id="archivo" name="archivo" style="width:445px">');
	});
*/
	$('#xTipo').on('change', function(){
		cambiarModoDetalle();
	});
	$('#fecha').datetimepicker({
		format: 'DD/MM/YYYY',
		defaultDate: new Date()
	});
	$('.selectpicker').selectpicker();
});

function getcontratoDocumento(num_contrato, cve_contrato, idRecurso, clv_benefi, proyecto, clv_partid, importe)
{
	$('#trEntrada').hide();
	$('#CVE_CONTRATO').attr('value', cve_contrato);
	$('#txtdocumento').attr('value', num_contrato);

	$('#txttotal').attr('value', formatNumber(importe).replace(',', ''));
	$('#div_total_entrada').html(formatNumber(importe, '$'));
	$('#CLV_BENEFI').attr('value', clv_benefi);
	$('#CID_PROYECTO').attr('value', proyecto);
	$('#CCLV_PARTID').attr('value', clv_partid);
	$('#CLV_PARBIT').attr('value',idRecurso);
	$('#tipoGasto').val(idRecurso);
	
	cargarDetallesContrato(num_contrato, cve_contrato, idRecurso, clv_benefi, proyecto, clv_partid, importe);
}

function cargarDetallesContrato(num_contrato, cve_contrato, idRecurso, clv_benefi, proyecto, clv_partid, importe)
{
	var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
				'<tr>'+
					'<td height="25" width="150">Numero Contrato:</td>'+
					'<td width="200">'+num_contrato+'<input type="hidden" value="'+cve_contrato+'"/></td>'+
				  '</tr>'+
				   '<tr>'+
					'<td height="25" width="150">Comprometido en Mes:</td>'+
					'<td width="200">'+formatNumber(importe,'$')+'</td>'+
				  '</tr>'+
				  '<tr>'+
					'<td height="25">Importe Orden de Pago:</td>'+
					'<td><input type="text" id="txtimporteOP" value="'+importe+'" style="width:150px" /></td>'+
				  '</tr>'+
				  '<tr>'+
					'<td height="50" align="center" colspan="2"><input type="button" value="Aceptar" id="cmdcargarOP" class="botones" style="width:80px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelarOP" class="botones" style="width:80px"/></td>'+
				  '</tr>'+
				'</table>';
	
				jWindow(html,'Presupuesto de Contrato', '','',0);
				$('#cmdcargarOP').click(function(event){generarDetalleContratos(num_contrato, cve_contrato, importe, proyecto, clv_partid);})
				$('#cmdcancelarOP').click(function(event){$.alerts._hide();})
				$('#txtimporteOP').keypress(function(event){ return validaEnter(event, function (){$('#cmdcargarOP').click();});});   
				$('#txtimporteOP').focus();
				$('#txtimporteOP').select();
}

function generarDetalleContratos(num_contrato,cve_contrato, importe, proyecto, clv_partid)
{
	var cve_op = $('#id_orden').attr('value');
	var importe_op = $('#txtimporteOP').attr('value');
	
	if(importe_op==''||parseFloat(importe_op)<=0.00){jAlert('El importe de la Orden de Pago no es válido','Advertencia');return false;}
	if(parseFloat(importe_op)>parseFloat(importe)){jAlert('El importe de la Orden de Pago no debe ser mayor al importe disponible en el presupuesto del Contrato','Advertencia'); return false;}
	
	controladorOrdenPagoRemoto.generarDetallesContrato(num_contrato, cve_contrato, cve_op, importe_op,  proyecto, clv_partid,{
		callback:function(items){	
				limpiarDetalle();
				llenarTablaDeDetallesOrdenes();	
				CloseDelay("Movimientos de Contrato(s) cargado(s) satisfactorimente");		   		   
		   
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString, 'Error');       
			}
	}); 					
}

function muestraContratosOP()
{
	var idDependencia = $('#cbUnidad').val();
	if(idDependencia==0||idDependencia=="") {jAlert('Es necesario seleccionar la Unidad Administrativa para listar los contratos'); return false;}
		
		var num_docto = $('#txtdocumento').attr('value');
		
		if(typeof(idDependencia)=='undefined') idDependencia =0;
		
		if($('#CVE_CONTRATO').attr('value')=='') $('#CVE_CONTRATO').attr('value', 0);
		jWindow('<iframe width="750" height="350" name="ventanaVales" id="ventanaVales" frameborder="0" src="../../sam/consultas/muestra_contratos.action?cve_contrato='+$('#CVE_CONTRATO').attr('value')+'&idDependencia='+idDependencia+'&num_contrato="></iframe>','Listado Contratos', '','Cerrar',1);
		
}

//Carga el listado de las facturas que se agregan en el detalle-----------------------------------------------------------------
function getFacturas(){
	
	var idDependencia = ID_DEPENDENCIA;
	if(idDependencia==0||idDependencia=="") {jAlert('Es necesario seleccionar la Unidad Administrativa para listar las facturas'); return false;}

	if(typeof(idDependencia)=='undefined') idDependencia =0;
	
	jWindow('<iframe width="800" height="400" name="FAC" id="FAC" frameborder="0" src="../../sam/consultas/muestra_facturas.action?tipoGasto='+$('#tipoGasto').attr('value')+'&unidad='+idDependencia+'&clv_benefi='+$('#xClaveBen').attr('value')+'"></iframe>','Facturas disponibles', '','Cerrar',1);
}


//Obtener la fecha actual
function obtiene_fecha() {

	var fecha_actual = new Date()

	var dia = fecha_actual.getDate()
	var mes = fecha_actual.getMonth() + 1
	var anio = fecha_actual.getFullYear()

	if (mes < 10)
	mes = '0' + mes

	if (dia < 10)
	dia = '0' + dia

	return (dia + "/" + mes + "/" + anio)
	}

	function MostrarFecha() {
	document.write ( obtiene_fecha() )
	}
	
	
	
//--------------------------------------Generar la Orden de Pago desde una lista de facturas------18/05/2017	
function generarOPS(checkFacturas){
	 alert ("demos del listado orden pago.js");

	   controladorOrdenPagoRemoto.geraropxfacturas($('#cbounidad').val(),checkFacturas.toString(),{
			callback:function(items){
				alert("orden de pago generada" + items);
			} 					   				
			,
			errorHandler:function(errorString, exception) { 
				jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");       
			}
		
	});
}


function guardarAnexos(){
	var file = $("#archivo").val();
	if(file=="")
		guardarDocumento();
	else
		$('#forma').submit();
}

function getValeDocumento(idVale, num_vale, disponible, comprobado){
	$('#CVE_VALE').attr('value', idVale);
	$('#txtvale').attr('value', num_vale);
	$('#txtdisponiblevale').attr('value', formatNumber(parseFloat(disponible),'$'));
	$('#txtcomprobadovale').attr('value',  formatNumber(parseFloat(comprobado),'$'));
	_closeDelay();
}

function getContrato(){
	if ($("#CVE_CONTRATO").attr('value')=='') $("#CVE_CONTRATO").attr('value', '0'); 
	return $("#CVE_CONTRATO").attr('value');
}

function getProveedor(){
	if($('#CCLV_BENEFI').attr('value')!=''&&$('#CVE_CONTRATO').attr('value')!=''){
		controladorOrdenPagoRemoto.getNombreProveedor($('#CCLV_BENEFI').attr('value'), {
							callback:function(items){
								$('#xClaveBen').attr('value',getHTML(items.CLV_BENEFI));
								$('#xBeneficiario').attr('value',getHTML(items.NCOMERCIA));
							} 					   				
							,
							errorHandler:function(errorString, exception) { 
								jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");       
							}
						
					});
	}
}

function comprobarContrato(){
	var selected = $("#tabsOrdenes").tabs("option","selected");
	if(contrato==true){
		if($('#CLV_PARBIT').attr('value')!=$('#tipoGasto').val()){
			jAlert('El tipo de gasto de la Orden de Pago no es valido o es diferente al del Contrato: '+$('#CLV_PARBIT').attr('value')+" - "+$('#tipoGasto').val(),'Advertencia');
			return false;
		}
		if($('#CVE_CONTRATO').attr('value')!='0'&&$('#CVE_CONTRATO').attr('value')!=''){
			$('#txtproyecto').attr('value', $('#CPROYECTO').attr('value'));
			$('#txtpartida').attr('value', $('#CCLV_PARTID').attr('value'));
			$('#txtproyecto').attr('disabled',true);
			$('#txtpartida').attr('disabled',true);
			 __getPresupuesto($('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible',$('#tipoGasto').attr('value'));
			 contrato = false;
		}
		
	}
	else{
			if($('#CVE_CONTRATO').attr('value')==''||$('#CVE_CONTRATO').attr('value')=='0'){
				$('#txtproyecto').attr('disabled',false);
				$('#txtpartida').attr('disabled',false);
				$('#txtproyecto').attr('value', '');
				$('#txtpartida').attr('value', '');
				$('#txtpresupuesto').attr('value', '');
				$('#txtdisponible').attr('value', '');
				contrato = false;
			}
		}
}

function muestraPresupuesto(){
	var idUnidad = $('#cbUnidad2').attr('value');
		if(idUnidad==null||idUnidad=="") idUnidad =0;
		
	if($('#txtproyecto').attr('value')==''||$('#txtpartida').attr('value')=='')
		$('#ID_PROYECTO').attr('value', '0');
		
	if($('#xTipo').val()!=11){
		__listadoPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'), $('#tipoGasto').attr('value'), idUnidad);
	}
	else{
			if($('#txtvale').attr('value')=='') $('#CVE_VALE').attr('value', '');
			if($('#CVE_VALE').attr('value')!=''&&$('#CVE_VALE').attr('value')!='0'){
				//con presupuesto de vales
				if($('#CVE_VALE').attr('value')==''||$('#CVE_VALE').attr('value')=='0') {jAlert('Es necesario seleccionar un Vale para mostrar su informacion presupuestal.', 'Advertencia'); return false;}
					__listadoPresupuestoVale($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'), $('#tipoGasto').attr('value'), 0, $('#CVE_VALE').attr('value'));
			}
			else
			{
				//con presupuesto normal
				__listadoPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'), $('#tipoGasto').attr('value'), idUnidad);
			}

	}
}

function limpiarRetenciones(){
	$('#idRetencion').attr('value', '');
	$('#retencion').val(0);	
	$('#importeRetencion').attr('value','');
	$('#retencion').focus();
}

function limpiarAnexos(){
	$('#idDocumento').attr('value', '0');
	$('#tipoMovDoc').val(0);	
	$('#numeroDoc').attr('value','');
	$('#notaDoc').val('');
	$('#tipoMovDoc').focus();
	$('#archivo').val("");
}

function limpiarVales(){
	$('#idVale').attr('value', '');
	$('#claveVale').attr('value', '');
	$('#importeAntVale').attr('value', '');
	$('#proyectoCuenta').val(0);	
	$('#claveValeDis').val(0);
	$('#importeVale').attr('value','');
	$('#proyectoCuenta').focus();
}

/* Captura de encabezado de orden de pago*/
function limpiarForma(){	
	$('#id_orden').attr('value','');
	$('#fecha').attr('value','');
	$('#xImporteIva').attr('value','');	
	$('#xBeneficiario').attr('value','');
	$('#xClaveBen').attr('value','');
	$('#xConcurso').attr('value','');
	$('#xNota').attr('value','');
	$('#NoOrden').text("");
	//$('#NoFolio').text("");
	$('#reembolso').attr('checked',false);
	quitRow("listasDetallesOrdenes");
	quitRow("listasDocumentos");
	quitRow("listasRetenciones");
	$('#tabsOrdenes').tabs('option', 'disabled', [1,2,3,4]);
	$('#btnCerrar').attr('disabled',true);
    $('#tipoGasto').attr('disabled',false);
	$('#xTipo').attr('disabled',false);
	lipiarDocumento();	
	limpiarRetencion ();
	removerContrato();
}

//Guardar la Orden de Pago

function guardar () {
   
   var tipo_gto = $('#tipoGasto').val();
    if ($('#fecha').attr('value')=="") {jAlert('La fecha de la Orden de Pago no es válida'); return false;};
    if ($('#xClaveBen').attr('value')=="") {jAlert('El Beneficiario seleccionado no es válido'); return false;}
    if ($('#xNota').attr('value')=="") {jAlert('El concepto de la Orden de Pago no es válido'); return false;}
   	/*Comprobar el tipo de gasto al guardar*/
	jConfirm('¿Confirma que desea guardar la Orden de Pago?','Guardar', function(r){
				if(r){
							ShowDelay('Guardando Orden de Pago','');
							controladorOrdenPagoRemoto.validarTipoGasto($('#id_orden').attr('value'), tipo_gto, "", {
							callback:function(items){
								if(items=='')
									_guardarOP();
								else
									jError(items,'Error');
							} 					   				
							,
							errorHandler:function(errorString, exception) { 
								jError(errorString, 'Error');       
							}
						
					});
				}
			});	  
}

function _guardarOP(){
	var idOrden=$('#id_orden').attr('value');
	  var check = $('#reembolso').is(':checked');
	  var temp_check = "";
	  if(check) temp_check = "S"; else temp_check = "N";
	  				 
	  controladorOrdenPagoRemoto.guardarOrden( $('#id_orden').attr('value'),$('#xTipo').attr('value'),$('#fecha').attr('value'),$('#xClaveBen').attr('value'),null,temp_check,$('#xConcurso').attr('value'),$('#xNota').attr('value'),$('#estatus').attr('value'),null, $('#xImporteIva').attr('value'),$('#cbUnidad').attr('value'),$('#cbomes').attr('value'),$('#tipoGasto').attr('value'), $('#CVE_CONTRATO').attr('value'),{
	  callback:function(items) {
		  ID_DEPENDENCIA = $('#cbUnidad').attr('value');
		  $('#id_orden').attr('value',items); 
		  $('#cve_op').attr('value', items);     
		  $('#NoOrden').html(rellenaCeros(items.toString(), 6));
		  $('#tabsOrdenes').tabs( 'enable' , 1);
		  $('#tabsOrdenes').tabs( 'enable' , 2);
		  $('#tabsOrdenes').tabs( 'enable' , 3);	  
		  if (parseInt($('#xTipo').attr("value")) !=5 )
			  $('#tabsOrdenes').tabs( 'enable' , 4);
		  if(idOrden==0) $('#tabsOrdenes').tabs('option', 'selected', 1);
		  cambiarModoDetalle();	  
		  CloseDelay("Orden de Pago guardada con éxito");	  
	} 					   				
	,
	errorHandler:function(errorString, exception) { 
		jError(errorString, 'Error');      
	}
},async=false );
}

function cerrarOrden3( ) {
	var tipo_gto = $('#tipoGasto').val();
	jConfirm('¿Confirma que desea cerrar la Orden de Pago?','Cerrar Orden Pago', function(r){
			if(r){			
				ShowDelay('Cerrando Orden de Pago','');	
				controladorOrdenPagoRemoto.validarTipoGasto($('#id_orden').attr('value'), tipo_gto, "x", {
							callback:function(items){
								if(items=='')
									_cerrarOrden();
								else
									jError(items,'Advertencia');
							} 					   				
							,
							errorHandler:function(errorString, exception) { 
								jError(errorString, 'Error'); 
							}
						
			});
		}
	});
}
function _cerrarOrden3(){
	controladorOrdenPagoRemoto.cerrarOrden( $('#id_orden').attr('value'), {
				callback:function(items) {
					if (items != "exito")
							jAlert(items, 'Advertencia');
					   else {
						   getReporteOP($('#id_orden').attr('value'));
						   limpiarForma();
						   $('#btnCerrar').attr('disabled',true);	
						   CloseDelay("Orden de Pago cerrada con éxito");
					   }	  
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError(errorString, 'Error');     
				}
			
		});
}
function cerrarOrden() {
	var tipo_gto = $('#tipoGasto').val();
	swal({
		  title: "¿Cerrar Orden de Pago?",
		  text: "Una vez cerrado el documento no podra ser modificado!",
		  type: "warning",
		  showCancelButton: true,
		  confirmButtonColor: "#DD6B55",
		  confirmButtonText: "Si, Cerrar",
		  cancelButtonText: "No, Deshacer",
		  closeOnConfirm: false,
		  closeOnCancel: false
		},
		function(isConfirm){
		  if (isConfirm) {
		    swal("Cerrada!", "Orden de Pago cerrada con exito!.", "success");
		    controladorOrdenPagoRemoto.validarTipoGasto($('#id_orden').attr('value'), tipo_gto, "x");
		    controladorOrdenPagoRemoto.cerrarOrden( $('#id_orden').attr('value'));
		    limpiarForma();
			   $('#btnCerrar').attr('disabled',true);	
		  } else {
			    swal("Cancelar", "El documento no será cerrado!:)", "error");
		  }
		});
	/*
	jConfirm('¿Confirma que desea cerrar la Orden de Pago?','Cerrar Orden Pago', function(r){
			if(r){			
				ShowDelay('Cerrando Orden de Pago','');	
				controladorOrdenPagoRemoto.validarTipoGasto($('#id_orden').attr('value'), tipo_gto, "x", {
							callback:function(items){
								if(items=='')
									_cerrarOrden();
								else
									jError(items,'Advertencia');
							} 					   				
							,
							errorHandler:function(errorString, exception) { 
								jError(errorString, 'Error'); 
							}
						
			});
		}
	});*/
}

function _cerrarOrden2(){
	controladorOrdenPagoRemoto.cerrarOrden( $('#id_orden').attr('value'), {
				callback:function(items) {
					if (items != "exito")
							jAlert(items, 'Advertencia');
					   else {
						   getReporteOP($('#id_orden').attr('value'));
						   limpiarForma();
						   $('#btnCerrar').attr('disabled',true);	
						   CloseDelay("Orden de Pago cerrada con éxito");
					   }	  
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError(errorString, 'Error');     
				}
			
		});
}

function getReporteOP(clave) {
	$('#cve_op').attr('value',clave);
	$('#forma').attr('action',"../reportes/formato_orden_pago.action");
	$('#forma').attr('target',"impresion");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"");
	}


function cambiarModoDetalle(){
	$('#filaFacturas').hide();
	if ($('#xTipo').attr('value')!=0 && $('#xTipo').attr('value')!=2){
		$('#filaGuardar').show();
		$('#img_presupuesto').show();
	}
	else{
		$('#filaGuardar').hide();
		$('#img_presupuesto').hide();		
	}
	
	
	if ($('#xTipo').attr('value')==0 )
		$('#filaPedido').show();
	else
		$('#filaPedido').hide();
		
	if ($('#xTipo').attr('value')==2 )
		$('#filaReq').show();
	else	
		$('#filaReq').hide();
		
	if ($('#xTipo').attr('value')==2 || $('#xTipo').attr('value')==0  ){
		$('#filaSelectVale').hide();
		$('#filaDisponibleVale').hide();
		$('#filaUnidadProyPart').hide();
		$('#filaDetProyPart').hide();
		$('#filaDetPres').hide();
		$('#filaDetNota').hide();
		$('#filaDetImporte').hide();
		$('#btnNuevo').hide();
		$('#filaFacturas').hide();
		$('#filaContrato').hide();
	}else {
		$('#filaSelectVale').hide();
		$('#filaDisponibleVale').hide();
		$('#filaUnidadProyPart').show();
		$('#filaDetProyPart').show();
		$('#filaDetPres').show();
		$('#filaDetNota').show();
		$('#filaDetImporte').show();
		$('#btnNuevo').show();
		$('#filaContrato').hide();
		$('#filaFacturas').hide();
	}
	
	if ($('#xTipo').attr('value')==11)
	{
		$('#filaSelectVale').show();
		$('#filaDisponibleVale').show();
		$('#filaUnidadProyPart').hide();
		$('#filaDetProyPart').show();
		$('#filaDetPres').show();
		$('#filaDetNota').show();
		$('#filaDetImporte').show();
		$('#btnNuevo').show();
		$('#filaContrato').hide();
		$('#filaFacturas').hide();
	}
	
	if($('#xTipo').attr('value')==12)
	{
		$('#filaSelectVale').hide();
		$('#filaDisponibleVale').hide();
		$('#filaUnidadProyPart').hide();
		$('#filaDetProyPart').hide();
		$('#filaDetPres').hide();
		$('#filaDetNota').hide();
		$('#filaDetImporte').hide();
		$('#btnNuevo').hide();
		$('#filaGuardar').hide();
		$('#filaContrato').hide();
		$('#filaFacturas').show();
	}
	if($('#xTipo').attr('value')==13)
	{
		$('#filaSelectVale').hide();
		$('#filaDisponibleVale').hide();
		$('#filaUnidadProyPart').hide();
		$('#filaDetProyPart').hide();
		$('#filaDetPres').hide();
		$('#filaDetNota').hide();
		$('#filaDetImporte').hide();
		$('#btnNuevo').hide();
		$('#filaGuardar').hide();
		$('#filaFacturas').hide();
		$('#filaContrato').show();
	}
	
}

function buscarOrden( idOrden ) {	
	ShowDelay('Cargando Orden de Pago','');
	controladorOrdenPagoRemoto.getOrden(idOrden,{
    callback:function(items) {
			if(items.STATUS==-1){
				_closeDelay();	   
				$('#id_orden').attr('value',items.CVE_OP);
				$('#idUnidad').attr('value', items.ID_DEPENDENCIA);
				ID_DEPENDENCIA = items.ID_DEPENDENCIA;
				$('#NoOrden').text(items.NUM_OP);
				//$('#NoFolio').text(items.FOLIO);
				$('#xTipo').attr('value',items.TIPO);
				$('#cbomes').attr('value',items.PERIODO);	
				$('#fecha').attr('value',items.FECHA);
				$('#xClaveBen').attr('value',items.CLV_BENEFI);
				$('#xBeneficiario').attr('value',items.NCOMERCIA);	
				if (items.REEMBOLSOF=='S')
					$('#reembolso').attr('checked',true);
				$('#xConcurso').attr('value',items.CONCURSO);
				$('#xNota').attr('value',items.NOTA);
				$('#estatus').attr('value',items.STATUS);
				$('#xImporteIva').attr('value',items.IMPORTE_IVA);		
				
				nuevaOp();
				$('#tipoGasto').val(items.ID_RECURSO);	 	  	    	
				$('#tabsOrdenes').tabs('option', 'disabled', [4]);	
				if ( parseInt($('#xTipo').attr("value")) != 5  ) {
					 $('#tabsOrdenes').tabs( 'enable' , 4);			  
				}
				$('#txtnumcontrato').attr('value', getHTML(items.NUM_CONTRATO));
				$('#CVE_CONTRATO').attr('value', getHTML(items.CVE_CONTRATO));
				if($('#CVE_CONTRATO').attr('value')!='0'&&$('#CVE_CONTRATO').attr('value')!='') {
					$('#img_quitar_contrato').attr('src', '../../imagenes/cross.png');
					$('#CPROYECTO').attr('value', getHTML(items.CPROYECTO));
					$('#CCLV_PARTID').attr('value', getHTML(items.CCLV_PARTID));
					$('#CLV_PARBIT').attr('value', getHTML(items.CLV_PARBIT2));
					$('#txtproyecto').attr('value', $('#CPROYECTO').attr('value'));
					$('#txtpartida').attr('value', $('#CCLV_PARTID').attr('value'));
					contrato = true;
				}
				else{
					$('#img_quitar_contrato').attr('src', '../../imagenes/cross2.png');
					$('#CPROYECTO').attr('value', '');
					$('#CCLV_PARTID').attr('value', '');
					$('#CLV_PARBIT').attr('value', '');
					$('#txtproyecto').attr('value', '');
					$('#txtpartida').attr('value','');
					contrato = false;
				}
				llenarTablaDeDetallesOrdenes();
				llenarTablaDeRetenciones();
				llenarTablaDeDocumentos();
				llenarTablaDeVales();
				cambiarModoDetalle();
			}
			else jAlert('La Orden de Pago <strong>'+items.NUM_OP+'</strong> no esta preparada para su edición, verifique su estatus ó vuelva a aperturela y reintente la operación','Advertencia');
    } 					   				
    ,
    errorHandler:function(errorString, exception) { 
        jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
}
},async=false );
}

 function regresar(){
    $('#tabsOrdenesEnca').hide(); 
	$('#listaOrdenesPendientes').show();
	$('#DivOPResults').show();	
	$('#DivHeadDependency').show();	
	limpiarForma();
	llenarTablaDeOrdenes();
 }
 
/*Inicio Listado de ordenes*/
 function llenarTablaDeOrdenes() {
	 quitRow("listaOrdenes");
	 var tipo=$('#xTipo').attr('value');
	 var cveUnidad=$('#cbUnidad').attr('value');
	 var ejercicio=$('#ejercicio').attr('value');
	 var estatus=$('#estatus').attr('value');
	controladorOrdenPagoRemoto.getOrdenesTipo(cveUnidad ,ejercicio,estatus, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaOrdenes( "listaOrdenes", i+1,this.CVE_OP,this.NUM_OP,this.FECHA,this.DESCRIPCION_ESTATUS,this.NOTA,this.TIPO_DOC);
        });
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    },async=false ); 
 }
 
  function pintaOrdenes( table, consecutivo,id,numOp,fecha,estatus,nota,tipo){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );    
    var html = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='buscarOrden("+id+")' >"; 	
	row.appendChild( Td(numOp,centro) );
	row.appendChild( Td(tipo,izquierda) );
	row.appendChild( Td(fecha,centro) );
	row.appendChild( Td(nota,izquierda));
	row.appendChild( Td(estatus) );
	row.appendChild( Td("",centro,"",html) );
	tabla.appendChild( row );
 } 
 //Manda a generar la nueva Orden de Pago
 function nuevaOp(){
	$('#cve_op').attr('value','');
	$('#accion').attr('value','');
    $('#tabsOrdenesEnca').show(); 
	$('#DivOPResults').hide();
	$('#listaOrdenesPendientes').hide();
	$('#DivHeadDependency').hide();
	getDevenOP();
 }
/*Fin Listado de ordenes*/
 
 /*Inicio Listado de detalles de ordenes*/
function llenarTablaDeDetallesOrdenes() {
	var c =0;
	var imp_total = 0;
	 quitRow("listasDetallesOrdenes");
	 var idOrden=$('#id_orden').attr('value');
	 controladorOrdenPagoRemoto.getDetallesOrdenes(idOrden, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
			c++;
			imp_total+= this.MONTO;
 		    pintaDetallesOrdenes( "listasDetallesOrdenes", i+1,this.DEPENDENCIA,this.ID_PROYECTO, this.N_PROGRAMA,this.CLV_PARTID,this.MONTO,this.NOTA,this.ID_MOV_OP,this.TIPO, this.CVE_VALE);
			 if(items.length==c) pintarTotalConceptos("listasDetallesOrdenes", imp_total);
        });
		getSelectGrados(items);
		 
		if (items.length > 0) {
			$('#tabsOrdenes').tabs( 'enable' , 1);
		    $('#btnCerrar').attr('disabled',false);
		    $('#tipoGasto').attr('disabled',true);
			$('#xTipo').attr('disabled',true);
		}
		else {		 
		   $('#btnCerrar').attr('disabled',true);
		   $('#tipoGasto').attr('disabled',false);
		   $('#xTipo').attr('disabled',false);
		}
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    },async=false ); 

 }
 
 function pintarTotalConceptos(tabla, importe_total){
	var tabla = document.getElementById(tabla).tBodies[0];
 	var row =   document.createElement( "TR" );    
	var htmlEdit = '<strong>'+formatNumber(importe_total, '$')+'</strong>';
	row.appendChild( Td('',centro,'',''));
	row.appendChild( Td('',centro,'',''));
	row.appendChild( Td('',centro,'',''));
	row.appendChild( Td('',centro,'',''));
	row.appendChild( Td('',centro,'',''));
	row.appendChild( Td('',centro,'',''));
	row.appendChild( Td('',derecha,"",htmlEdit));
	row.appendChild( Td('',centro,'',''));	
	tabla.appendChild(row);
}
 
function getSelectGrados(datos) {		
        lipiarVale();
		dwr.util.removeAllOptions("proyectoCuenta");
		dwr.util.addOptions('proyectoCuenta',{ 0:'Seleccione' });
		dwr.util.addOptions('proyectoCuenta',datos,"ID_PROYECTO", "PROYECTOPARTIDA" );		
 }

function pintaDetallesOrdenes( table, consecutivo,unidad, idproyecto, proyecto,partida,monto,nota,idDetalle,tipo, idVale){
	if(idVale==''||idVale==null) idVale=0;
    var htmlEdit = '<img src=\"../../imagenes/page_white_edit.png\" style="cursor: pointer;" alt="Editar registro" width="16" height="16" border="0" onClick="editarDetalle('+idDetalle+','+idVale+','+idproyecto+',\''+proyecto+'\', \''+partida+'\',\''+monto+'\',\''+nota+'\')">';
	//,\''+proyecto+'\','+partida+'\',\''+monto+'\',\''+nota+'\')>"'; 	
    var htmlCheck = "<input type='checkbox' name='clavesDetalles' id='clavesDetalles' value='"+idDetalle+"' >";	  
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement("TR");    
	row.appendChild(Td("",centro,"",htmlCheck));
	row.appendChild(Td(unidad,izquierda));
	row.appendChild(Td(nota,izquierda));
	row.appendChild(Td(proyecto,centro));
	row.appendChild(Td(partida,centro));
	row.appendChild(Td(getHTML(tipo),centro));
	row.appendChild(Td(formatNumber(monto,"$"),derecha) );
	row.appendChild(Td("",centro,"",htmlEdit));
	tabla.appendChild(row);
 }
 
 function editarDetalle (idDetalle,idVale,idproyecto, proyecto,partida,monto,nota) {
	 if($('#xTipo').val()==0||$('#xTipo').val()==2) {jAlert('No se puede editar el movimiento de la Orden de Pago, el tipo de codumento no permite realizar esta operación', 'Advertencia'); return false;}
	 if ( parseFloat(monto)  <  0  )
	    monto = parseFloat( monto )* -1;
	$('#ID_PROYECTO').attr('value', idproyecto);	
	$('#txtproyecto').attr('value',proyecto);
    $('#txtpartida').attr('value',partida);	
    $('#notaDetalle').attr('value',nota);
	$('#importeDetalle').attr('value',monto);
	$('#id_ordenDetalle').attr('value',idDetalle);							  
	$('#txtproyecto').attr('disabled',true);
    $('#txtpartida').attr('disabled',true);
	$('#txtvale').attr('value', rellenaCeros(idVale.toString(), 6))
	$('#CVE_VALE').attr('value', idVale);
	$('#img_presupuesto').hide();
	__getPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible',$('#tipoGasto').attr('value'));
 } 
 
  function limpiarDetalle() {
	$('#ID_PROYECTO').attr('value',0);
	$('#txtproyecto').attr('value','');
    $('#txtpartida').attr('value','');	
    $('#notaDetalle').attr('value','');
	$('#importeDetalle').attr('value','');
	$('#id_ordenDetalle').attr('value','');
	$('#txtproyecto').attr('disabled',false);
    $('#txtpartida').attr('disabled',false);
	$('#txtpresupuesto').attr('value','');							  
	$('#txtdisponible').attr('value','');
	//$('#txtdisponiblevale').attr('value', '');
	//$('#txtcomprobadovale').attr('value', '');
	if ($('#xTipo').attr('value')!=0 && $('#xTipo').attr('value')!=2)
  	    $('#img_presupuesto').show();
 } 
 
 
  function guardarDetalle(){   
	var error="";  
 	var idOrden=$('#id_orden').attr('value');
	var idOrdenDetalle=$('#id_ordenDetalle').attr('value');
	var idVale = $('#CVE_VALE').attr('value');
	if(idVale=='') idVale = 0;
	//if ($('#xTipo').attr('value')==11&&($('#CVE_VALE').attr('value')==''||$('#CVE_VALE').attr('value')=='0')) {jAlert('Es necesario seleccionar un Vale para agregar el concepto','Advetencia'); return false;}
    if ($('#txtproyecto').attr('value')=="") {jAlert('El proyecto de concepto no es válido'); return false;};
    if ($('#txtpartida').attr('value')=="") {jAlert('La partida del concepto no es válido'); return false;}
    if ($('#cbomes').attr('value')=="") {jAlert('El mes del presupuesto no es válido'); return false;}
	if ($('#importeDetalle').attr('value')=="" || parseFloat($('#importeDetalle').attr('value')) <= 0 ) {jAlert('El importe del concepto no es válido'); return false;}
 
 	/*AGREGADO AQUI VALIDACION PARA NO METER ORDENES DE PAGO DE COMBUSTIBLES Y ELECTRICIDAD SI ES FONDO FIJO, 13 FEB 2013 ISRAEL*/
	if(($('#txtpartida').attr('value')=="3111"||$('#txtpartida').attr('value')=="2611")&&$('#xTipo').val()==9) {jAlert('El tipo de Orden de Pago ya no acepta la partida que intenta agregar, consulte a su administrador','Advertencia'); return false;}
	/*jConfirm('¿Confirma que desea guardar el concepto de la Orden de Pago?','Guardar concepto', function(r){
			if(r){*/
					ShowDelay('Guardando concepto','');
					 controladorOrdenPagoRemoto.guardarDetalles(idOrdenDetalle, idOrden, $('#ID_PROYECTO').attr('value'),$('#txtpartida').attr('value'),$('#notaDetalle').attr('value'),$('#importeDetalle').attr('value'), idVale,{
					  callback:function(items) {
						if (items==true) {
							llenarTablaDeDetallesOrdenes();
							limpiarDetalle();
							CloseDelay("Concepto guardado con éxito", 2000, function(){
								if($('#CVE_CONTRATO').attr('value')!='0'&&$('#CVE_CONTRATO').attr('value')!=''){
									contrato=true;
									comprobarContrato();
								}
							});
						  }
						  else
						   jAlert("La combinación proyecto partida no debe de duplicarse");	    
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');     
					}
				},async=false );
			/*}
		});		*/	
 }
 
 
 function eliminarDetalle(){
	  var checkDetalles = [];
     $('input[name=clavesDetalles]:checked').each(function() {  checkDetalles.push($(this).val());	 });	 
	 if (checkDetalles.length > 0 ) {
   	 var idOrden=$('#id_orden').attr('value');
	 jConfirm('¿Confirma que desea eliminar los detalles seleccionados?', 'Confirmar', function(r){
		 	if(r){
					controladorOrdenPagoRemoto.eliminarDetalle(checkDetalles,idOrden, {
					callback:function(items) { 		
					   if (items==true) {
						   $('#CVE_CONTRATO').attr('value', '0');
						   limpiarDetalle();
						   llenarTablaDeDetallesOrdenes();	
						   CloseDelay("Se eliminaron satisfactoriamente los detalles",function(){
								if($('#CVE_CONTRATO').attr('value')!='0'&&$('#CVE_CONTRATO').attr('value')!=''){
									contrato=true;
									comprobarContrato();
									//contrato==false;
								}	
							});
					   }
					   else
					   jAlert("No se han podido eliminar los movimientos de la Orden de Pago, es posible que sean erroneos", "Advertencia");
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');        
					}
				},async=false ); 
			}
		 });
     
	 } else 
	    jAlert("Es necesario que seleccione un elemento de la lista", "Advertencia");
 }
 
 /*Fin Listado de detalles de ordenes*/
 
 
 /*Inicio de retenciones*/
   function llenarTablaDeRetenciones() {
	 quitRow("listasRetenciones");
	 var idOrden=$('#id_orden').attr('value');
	 controladorOrdenPagoRemoto.getTodasRetencionesOrdenes(idOrden, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaRetenciones( "listasRetenciones", i+1,this.RET_CONS,this.CLV_RETENC,this.RETENCION,this.IMPORTE);
        });
				  
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');
        }
    },async=false ); 

 }
 
  function pintaRetenciones( table, consecutivo,idRetencion,idTipoRetencion,retencion,importe){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );    
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='editarRetencion("+idRetencion+",\""+idTipoRetencion+"\","+importe+")' >"; 	
    var htmlCheck = "<input type='checkbox' name='clavesRetencion' id='clavesRetencion' value='"+idRetencion+"' >";
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(retencion,izquierda) );
	row.appendChild( Td(formatNumber(importe,"$"),derecha) );
	row.appendChild( Td("",centro,"",htmlEdit) );
	tabla.appendChild( row );
 }
 
 function editarRetencion (idRetencion,idTipoRetencion,importe) {
 	$('#idRetencion').attr('value',idRetencion);
	$('#retencion').val(idTipoRetencion);
	if (importe < 0 )
	  importe=importe*-1;
	$('#importeRetencion').attr('value',importe);
 } 
 
 
 function limpiarRetencion () {
 	$('#idRetencion').attr('value','');
	$('#importeRetencion').attr('value',"");
 }
 
 //VALIDAR LA RETENCION A SOLO LECTURA.........................
 function guardarRetencion(){
	var error="";  
  	var cveParbit=$('#tipoGasto').attr('value');
 	var idOrden=$('#id_orden').attr('value');
    if($('#retencion').attr('value')=="") {jAlert('El tipo de Retención no es válido','Advertencia'); return false;}
    if($('#importeRetencion').attr('value')=="") {jAlert('El Importe de la Retención no es válido','Advertencia'); return false;}
	ShowDelay('Guardando retención','');
	controladorOrdenPagoRemoto.guardarRetencion( $('#idRetencion').attr('value'),$('#retencion').attr('value'),$('#importeRetencion').attr('value'),cveParbit,idOrden,{
	callback:function(items) { 	 
		  if (items==true) {
			llenarTablaDeRetenciones();
			CloseDelay("Retención guardada con éxito", 2000, function(){
				limpiarRetencion();	
			});	
		  } else
			jInformation("La retención ya se encuentra en la Orden de Pago");	  	  	  
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error'); 
		}
			
	}); 
 }
 
 function eliminarRetencion(){
	  var checkRetenciones = [];
     $('input[name=clavesRetencion]:checked').each(function() {checkRetenciones.push($(this).val());	 });	 
 	 var cveParbit=$('#tipoGasto').attr('value');
  	 var idOrden=$('#id_orden').attr('value');
	 if (checkRetenciones.length > 0 ) {
		 jConfirm('¿Confirma que desea eliminar la retención?','Eliminar retención', function(r){
			 if(r){
				 	ShowDelay('Eliminando retencione(s)','');
					controladorOrdenPagoRemoto.eliminarRetenciones(checkRetenciones,idOrden, {
					callback:function(items) { 	
						llenarTablaDeRetenciones();	
					   CloseDelay("Retencion(es) eliminada(s) con éxito");
					   
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');
					}
				},async=false );
			}
		});
     	 } else 
	    jAlert('Es necesario que seleccione un elemento de la lista', 'Advertencia');
	 }
 /*Fin de retenciones*/
 
 
 /*Inicio de Documentos*/
 function llenarTablaDeDocumentos() {
	 quitRow("listasDocumentos");
	 var idOrden=$('#id_orden').attr('value');
	 controladorOrdenPagoRemoto.getDocumentosOrdenes(idOrden, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaDocumentos( "listasDocumentos", i+1,this.ANX_CONS,this.T_DOCTO,this.NUMERO,this.NOTAS,this.DESCR, this.FILENAME, this.FILELENGTH, this.FILETYPE, this.FILEPATH);			
        });		
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    },async=false ); 
 }
 
  function pintaDocumentos( table, consecutivo,idDocumento,tipoMovimiento,numero,nota,desTipoDoc, filename, filelength, filetype, filepath){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );  
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='editarDocumento("+idDocumento+",\""+tipoMovimiento+"\",\""+numero+"\",\""+nota+"\")' >"; 	
    var htmlCheck = "<input type='checkbox' name='clavesDocumentos' id='clavesDocumentos' value='"+idDocumento+"' >";
	row.appendChild(Td("",centro,"",htmlCheck) );
	row.appendChild(Td(desTipoDoc,izquierda) );
	row.appendChild(Td(numero,centro) );
	row.appendChild(Td(nota,izquierda) );
	row.appendChild(Td("", izquierda, "", "<strong><a href='"+filepath+filename+"' target='_blank'>"+getHTML(filename)+"</a></strong>"));
	row.appendChild(Td( (filelength==null) ? "": parseInt(parseInt(getHTML(filelength))/1024)+' kb', centro));
	row.appendChild(Td(getHTML(filetype), centro));
	row.appendChild(Td("",centro,"",htmlEdit) );
	tabla.appendChild( row );
 }
 
 function editarDocumento(idDocumento,tipoMovimiento,numero,nota){
	$('#idDocumento').attr('value',idDocumento);
	$('#tipoMovDoc').val(tipoMovimiento);
	$('#numeroDoc').attr('value',numero);
	$('#notaDoc').attr('value',nota);
 } 
 
 function lipiarDocumento(){
	$('#idDocumento').attr('value',"");
	$('#numeroDoc').attr('value',"");
	$('#notaDoc').attr('value',"");
 }
 
 function subirArchivo(){
	ShowDelay('Guardando anexo','');
	$('#forma').submit();
}

function showRequest(formData, jqForm, options) { 
    return true; 
} 
 
function showResponse(data)  { 
 	if(data.mensaje){
		CloseDelay("Anexo guardado con éxito");
		llenarTablaDeDocumentos();
		limpiarAnexos();
		$('#archivo').attr('value','');
	}
	else{
		_closeDelay();
		jError("No se ha podido cargar el archivo, consulte a su administrador", "Error");
	}
} 

 function guardarDocumento(){
	 var error="";  
    if ($('#tipoMovDoc').attr('value')=="") {jAlert('El tipo de documento no es válido'); return false;}
    if ($('#numeroDoc').attr('value')=="") {jAlert('El número de documento no es válido'); return false;}

	  var idOrden=$('#id_orden').attr('value');
						
						ShowDelay('Guardando anexo','');
						controladorOrdenPagoRemoto.guardarDocumento($('#idDocumento').attr('value'),$('#tipoMovDoc').attr('value'),$('#numeroDoc').attr('value'),$('#notaDoc').attr('value'),idOrden,{
						callback:function(items) { 	 
						llenarTablaDeDocumentos();
						lipiarDocumento();	    
						 CloseDelay("Anexos guardados con éxito");	  
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');         
						}
	  
	  
	}); 
	
	
 }
 
 function eliminarDocumentos(){
	  var checkDocumentos = [];
     $('input[name=clavesDocumentos]:checked').each(function() {  checkDocumentos.push($(this).val());	 });	 
	 if (checkDocumentos.length > 0 ) {
   	 var idOrden=$('#id_orden').attr('value');
	 jConfirm('¿Confirma que desea eliminar los anexos?','Eliminar anexos', function(r){
		 	if(r){
					ShowDelay('Eliminando anexo(s)','');
					controladorOrdenPagoRemoto.eliminarDocumentos(checkDocumentos,idOrden, {
					callback:function(items) { 	
						llenarTablaDeDocumentos();	
					   CloseDelay("Anexos eliminados con éxito");
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');     
					}
				},async=false ); 
			}
		 });
     
	 } else 
	    jAlert("Es necesario seleccionar almenos un elemento de la lista");
 }
 /*Fin de Documentos*/
 
 
/*-----------------------------Inicio de Vales-------------------------------------------*/
function llenarTablaDeVales() {
	 quitRow("listasVales");
	 var idOrden=$('#id_orden').attr('value');
	 controladorOrdenPagoRemoto.getValesOrdenes(idOrden, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaVales( "listasVales", i+1,this.ID_PROYECTO,this.N_PROGRAMA,this.CLV_PARTID,this.CVE_VALE,this.NUM_VALE,this.IMPORTE, this.IMP_ANTERIOR, this.IMP_PENDIENTE, this.ID_VALE);
        });
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    },async=false ); 
 }
 
  function pintaVales( table, consecutivo,idproyecto, proyecto,partida,vale,numeroVale,importe, imp_anterior, imp_pendiente, idVale){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );  
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='editarVale("+idVale+","+vale+","+importe+",\""+proyecto+"-"+partida+"\")' >"; 	
    var htmlCheck = "<input type='checkbox' name='clavesVales' id='clavesVales' value='"+idVale+"' >";
	row.appendChild( Td("","","",htmlCheck));
	row.appendChild( Td(proyecto,centro));
	row.appendChild( Td(partida,centro));
	row.appendChild( Td(numeroVale,centro));
	row.appendChild( Td(formatNumber(importe,"$"),derecha) );
	row.appendChild( Td(formatNumber(imp_anterior,"$"),derecha) );
	row.appendChild( Td(formatNumber(imp_pendiente,"$"),derecha) );
	row.appendChild( Td("","centro","",htmlEdit) );
	tabla.appendChild(row);
 }
 
 function editarVale(idVale,vale,importe,proyectoCuenta){
	$('#idVale').attr('value',idVale);
	$('#proyectoCuenta').val(proyectoCuenta);
	$('#importeVale').attr('value',importe);	
	$('#importeAntVale').attr('value',importe);	
	cargarVales(vale);
	
 } 
 
 function lipiarVale(){
	$('#idVale').attr('value',"");
	$('#importeVale').attr('value',"");
	$('#claveVale').attr('value',"");
	$('#importeAntVale').attr('value',"0");	
	$('#proyectoCuenta').val("0");
	dwr.util.removeAllOptions("claveValeDis");
	$('#textImporteDisponible').text("");	 
 }
 
 function guardarVale(){
	var error="";  
    if ($('#proyectoCuenta').attr('value')==0) {jAlert('El Proyecto/Cuenta no es válido'); return false;}
    if ($('#claveValeDis').attr('value')==""||$('#claveValeDis').attr('value')==0) {jAlert('El Vale no es válido'); return false;}
    if ($('#importeVale').attr('value')=="" || parseFloat($('#importeVale').attr('value')==0)) {jAlert('El importe escrito para la comprobación no es válido'); return false;}
	
	var temp = $('#claveValeDis :selected').text().replace(',','');
		temp = temp.replace('$','');
		temp = temp.replace(' ','');
    var datos= temp.split('-');	 
    var textImporteDisponible = parseFloat((datos[1]).replace(',',''));
	var imp_anterior = parseFloat($('#importeAntVale').attr('value'));
	var imp_vale = parseFloat($('#importeVale').attr('value'));
	var imp_total_disp_vale = parseFloat(textImporteDisponible+imp_anterior);
	//alert("Imp. Vale: "+imp_vale+"\nDisponible: "+textImporteDisponible+"\nDis. Anterior: "+imp_anterior+"\Disponible total Vale: "+imp_total_disp_vale);
	if (imp_vale>(imp_total_disp_vale)) {jAlert('El importe de comprobación para el Vale no puede ser mayor al disponible actual del Vale'); return false;}
 	  datos= $('#proyectoCuenta :selected').text().split('-');	 
	  var proyecto = $('#proyectoCuenta').attr('value');
	  var partida = datos[1];	 	 
	  var idOrden=$('#id_orden').attr('value');
	  /*jConfirm('¿Confirma que desea guardar la comprobacion de Vale para la Orden de Pago?','Guardar Vale', function(r){
			if(r){*/
				  ShowDelay('Guardando vale','');
				  controladorOrdenPagoRemoto.guardarVale( $('#idVale').attr('value'),$('#claveValeDis').attr('value'),$('#importeVale').attr('value'),textImporteDisponible,idOrden,proyecto,partida,$('#importeAntVale').attr('value'), {
				  callback:function(items) { 	
				  if(items==""){
					  	llenarTablaDeVales();
						lipiarVale();
						CloseDelay("Vale guardado con éxito");	 
				  }
				   else{
					   jError(items,'Error');
				   }
				  
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError(errorString, 'Error'); 
			}
		});
	     
    //}
	//},async=false );
 }
 
 function eliminarVales(){
	  var checkVales = [];
     $('input[name=clavesVales]:checked').each(function() { checkVales.push($(this).val());	 });	 
	 if (checkVales.length > 0 ) {
   	 var idOrden=$('#id_orden').attr('value');
	 jConfirm('¿Confirma que desea aliminar el vale?', 'Eliminar vale', function(r){
		 		ShowDelay('Eliminando vale(s)','');
				controladorOrdenPagoRemoto.eliminarVales(checkVales, idOrden, {
				callback:function(items) {	
					llenarTablaDeVales();
				   CloseDelay("Los vales se eliminaron con éxito");
				   
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError(errorString, 'Error');
				}
			},async=false ); 
		 })
     
	 } else 
	    jAlert('Es necesario que seleccione un elemento de la lista', 'Advertencia');
 }
 
function cargarVales(vale) {
     dwr.util.removeAllOptions("claveValeDis");
     if ($('#proyectoCuenta').attr('value')!=0) {
	 var datos= $('#proyectoCuenta :selected').text().split('-');	 
	 var proyecto = $('#proyectoCuenta').attr('value');//datos[0];
	 var partida = datos[1];
	 controladorOrdenPagoRemoto.getValesDisponibles(proyecto,partida, {
        callback:function(items) { 				
		dwr.util.addOptions('claveValeDis',{ 0:'[Seleccione]'});
		dwr.util.addOptions('claveValeDis',items,"CVE_VALE", "DATOVALE");
		$('#claveValeDis').val(vale);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    },async=false ); } 
 }
 
 
function getPedidos(){
	jWindow('<iframe width="800" height="400" name="PED" id="PED" frameborder="0" src="../../sam/consultas/muestra_Pedidos.action?tipoGasto='+$('#tipoGasto').attr('value')+'&unidad='+$('#cbUnidad').attr('value')+'&clv_benefi='+$('#xClaveBen').attr('value')+'&proyecto='+$('#CPROYECTO').attr('value')+'&clv_partid='+$('#CCLV_PARTID').attr('value')+'"></iframe>','Cargar Pedidos en Ordenes de Pago', '','Cerrar',1);
}

function getTabla(items,tipoElemento){
	var tabla ="<table width='90%' align='center'><tr><td><h1>Pedidos disponibles</h1></td></tr></table>";
		tabla +="<table width='90%' border='0' align='center' cellpadding='0' cellspacing='0' class='formulario'>";
  	    tabla +="<tr><td  class='TituloFormulario' width='30'>No.</td><td  class='TituloFormulario' width='70' >Pedido</td><td  class='TituloFormulario' >Proyecto/Partida</td><td  class='TituloFormulario'>Concepto</td><td  class='TituloFormulario' >Importe</td><td></td></tr>";
		jQuery.each(items,function(i) {							   
			tabla +="<tr ><td><input type='"+tipoElemento+"' name='clavesPedido' id='clavesPedido' value='"+this.CVE_PED+"' ></td><td>"+this.NUM_PED+"</td><td>"+this.PROYECTO+"- "+this.PARTIDA+"</td><td>"+this.NOTAS+"</td><td>"+formatNumber(this.TOTAL)+"</td></tr>";
        	}); 	
	tabla +="<tr><td align='center' colspan='6' ><input   name='btnGuardarPedido' id='btnGuardarPedido' type='button' class='botones' onClick='generarDetallesPedido();' value='Aceptar ' />&nbsp;<input   name='' id='' type='button' class='botones' onClick='_closeDelay();' value='Cancelar' /></td></tr>";
	return tabla +="</table>";	
}

/*CARGAR DETALLES DEL DEVENGADO A LA ORDEN DE PAGO --------------------------------, Israel 20 Ene 2013*/
function generarDetallesFactura(checkIDs){
	var cve_op = $('#id_orden').attr('value');
	if (checkIDs.length > 0 ) {
		ShowDelay("Insertando detalle de Facturas en Orden de Pago","");
		controladorOrdenPagoRemoto.guardarFacturasEnOrdenPago(cve_op,checkIDs, {//GenerarOrdenPagoxDevengo
						callback:function(items){	
								CargarIvaFactura();
								llenarTablaDeDetallesOrdenes();	
								llenarTablaDeDocumentos();
								llenarTablaDeRetenciones();
								llenarTablaDeVales();
								CloseDelay("Facturas cargadas con exito");
								
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');        
						}
					}); 
	}
	else
		jAlert('Es necesario que seleccione un elemento de la lista para realizar esta operación', 'Advertencia');
	
}

function CargarIvaFactura()
{
	var cve_op = $('#id_orden').attr('value');
	controladorOrdenPagoRemoto.obtenerIvaOrdenPago(cve_op,{
		callback:function(items){	
			if($('#xImporteIva').val() =='0' || $('#xImporteIva').val() =='')
				$('#xImporteIva').val(items);
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString, 'Error');
			}
	}); 				
}

function generarDetallePedidoParcialOP(){
	var cve_ped = $('#hdcve_ped').attr('value');
	var importe_pedido = $('#hdImportePedido').attr('value');
	var cve_op = $('#id_orden').attr('value');
	var importe_op = $('#txtimporteOP').attr('value');
	
	if(importe_op==''||parseFloat(importe_op)<=0.00){jAlert('El importe de la Orden de Pago no es válido','Advertencia');return false;}
	if(parseFloat(importe_op)>parseFloat(importe_pedido)){jAlert('El importe de la Orden de Pago no debe ser mayor al importe disponible del Pedido','Advertencia'); return false;}
	
	controladorOrdenPagoRemoto.generarDetallesPedidosParcial(cve_ped, cve_op, importe_op,{
		callback:function(items){	
			if (items==true) {
				limpiarDetalle();
				llenarTablaDeDetallesOrdenes();
				CloseDelay("Movimientos de Pedido(s) cargado(s) satisfactorimente");		   		   
		   }
		   else
				jAlert('No se han podido agregar los movimientos a la Orden de Pago, los movimientos ya existen o no es posible cargar mas de dos movimientos a la vez', 'Advertencia');	
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString, 'Error');
			}
	}); 					
}
 
 function generarDetallesPedido(checkPedidos){
	  /*var checkPedidos = [];
     $('input[name=clavesPedido]:checked').each(function() {  checkPedidos.push($(this).val());	 });	 */
	 if (checkPedidos.length > 0 ) {	
	 	 //si es solo un pedido mostrar una ventana adicional con el importe a seleccionar del pedido
		 	if(checkPedidos.length==1) {
				//jAlert('Aqui el codigo para importe de OP y pedido','Advertencia');
					controladorOrdenPagoRemoto.getImporteDisponiblePedido(checkPedidos[0], {
						callback:function(items){	
								var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
								'<tr>'+
									'<td height="22" width="150">Numero Pedido:</td>'+
									'<td width="200">'+items.NUM_PED+'<input type="hidden" id="hdcve_ped" value="'+items.CVE_PED+'"/></td>'+
								  '</tr>'+
								   '<tr>'+
									'<td height="22" width="150">Importe Real Pedido:</td>'+
									'<td width="200">'+formatNumber(items.TOTAL,'$')+'</td>'+
								  '</tr>'+
								  '<tr>'+
									'<td height="22" width="150">Importe Disp. Pedido:</td>'+
									'<td width="200"><strong>'+formatNumber(items.DISPONIBLE,'$')+'</strong><input type="hidden" id="hdImportePedido" value="'+items.DISPONIBLE+'"/></td>'+
								  '</tr>'+
								  '<tr>'+
									'<td height="22">Importe Orden de Pago:</td>'+
									'<td><input type="text" id="txtimporteOP" value="'+items.DISPONIBLE+'" style="width:150px" /></td>'+
								  '</tr>'+
								  '<tr>'+
									'<td height="50" align="center" colspan="2"><input type="button" value="Aceptar" id="cmdcargarOP" class="botones" style="width:80px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelarOP" class="botones" style="width:80px"/></td>'+
								  '</tr>'+
								'</table>';

								jWindow(html,'Determinar el importe de la Orden de Pago', '','',0);
								$('#cmdcargarOP').click(function(event){generarDetallePedidoParcialOP();})
								$('#cmdcancelarOP').click(function(event){$.alerts._hide();})
								$('#txtimporteOP').keypress(function(event){ return validaEnter(event, function (){$('#cmdcargarOP').click();});});   
								$('#txtimporteOP').focus();
								$('#txtimporteOP').select();
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');        
						}
					}); 
			} 
			else
			{
				 var idOrden=$('#id_orden').attr('value');
				 ShowDelay('Cargando Pedidos','');
				 controladorOrdenPagoRemoto.generarDetallesPedidos(checkPedidos,idOrden, {
						callback:function(items) {	
						   if (items==true) {
							   limpiarDetalle();
								llenarTablaDeDetallesOrdenes();	
								CloseDelay("Movimientos de Pedido(s) cargado(s) satisfactorimente");		   		   
						   }
						   else
								jAlert('No se han podido agregar los movimientos a la Orden de Pago, los movimientos ya existen o no es posible cargar mas de dos movimientos a la vez', 'Advertencia');		   
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error'); 
						}
					},async=false ); 
				} 
	 	}
	 else 
	    jAlert('Es necesario que seleccione un elemento de la lista para realizar esta operación', 'Advertencia');
 }
 

 
 function validaEnter(e, fn){
	 if(e.keyCode==13) 
	 	fn();
	else
		return keyNumbero(e);
 }

function getOrdenesDeTrabajo(){
	jWindow('<iframe width="800" height="400" name="OTOS" id="OTOS" frameborder="0" src="../../sam/consultas/muestra_OT_OS.action?tipoGasto='+$('#tipoGasto').attr('value')+'&unidad='+$('#cbUnidad').attr('value')+'&clv_benefi='+$('#xClaveBen').attr('value')+'&proyecto='+$('#CPROYECTO').attr('value')+'&clv_partid='+$('#CCLV_PARTID').attr('value')+'"></iframe>','Ordenes de Trabajo y Ordenes de Servicio disponibles', '','Cerrar',1);
}

 function generarDetallesOT(checkOT){
	 if (checkOT.length > 0 ) {	
	 	if (checkOT.length == 1) {
			//Generar apartir de un importe
			controladorOrdenPagoRemoto.getImporteDisponibleRequisicion(checkOT[0], {
						callback:function(items){	
								var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
								'<tr>'+
									'<td height="22" width="150">Numero Requisición:</td>'+
									'<td width="200">'+items.NUM_REQ+'<input type="hidden" id="hdcve_req" value="'+items.CVE_REQ+'"/></td>'+
								  '</tr>'+
								   '<tr>'+
									'<td height="22" width="150">Importe Real Requisición:</td>'+
									'<td width="200">'+formatNumber(items.TOTAL,'$')+'</td>'+
								  '</tr>'+
								  '<tr>'+
									'<td height="22" width="150">Importe Disp. Requisición:</td>'+
									'<td width="200"><strong>'+formatNumber(items.DISPONIBLE,'$')+'</strong><input type="hidden" id="hdImporteRequisicion" value="'+items.DISPONIBLE+'"/></td>'+
								  '</tr>'+
								  '<tr>'+
									'<td height="22">Importe Orden de Pago:</td>'+
									'<td><input type="text" id="txtimporteOP" value="'+items.DISPONIBLE+'" style="width:150px" /></td>'+
								  '</tr>'+
								  '<tr>'+
									'<td height="50" align="center" colspan="2"><input type="button" value="Aceptar" id="cmdcargarOP" class="botones" style="width:80px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelarOP" class="botones" style="width:80px"/></td>'+
								  '</tr>'+
								'</table>';

								jWindow(html,'Determinar el importe de la Orden de Pago', '','',0);
								$('#cmdcargarOP').click(function(event){generarDetalleRequisicionParcialOP();})
								$('#cmdcancelarOP').click(function(event){$.alerts._hide();})
								$('#txtimporteOP').keypress(function(event){ return validaEnter(event, function (){$('#cmdcargarOP').click();});});   
								$('#txtimporteOP').focus();
								$('#txtimporteOP').select();
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');
						}
					}); 
		}
		else{
			var idOrden=$('#id_orden').attr('value');
			 ShowDelay('Cargando Ordenes de Trabajo','');
			 controladorOrdenPagoRemoto.generarDetallesOT(checkOT,idOrden, {
					callback:function(items) {	
					   if (items==true) {
								limpiarDetalle();
								llenarTablaDeDetallesOrdenes();
								CloseDelay('Se cargaron los detalles satisfactorimente');		   		     
					   }
					   else
							jAlert('No se han podido agregar los movimientos a la Orden de Pago, los movimientos ya existen o no es posible cargar mas de dos movimientos a la vez', 'Advertencia');		   
					   
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');
					}
				},async=false ); 
		}
	 } 
	 else 
	    jAlert('Es necesario seleccionar por lo menos una Orden de Trabajo para realizar esta operación', 'Advertencia');
 }
 
 
 function generarDetalleRequisicionParcialOP(){
	var cve_req = $('#hdcve_req').attr('value');
	var importe_pedido = $('#hdImporteRequisicion').attr('value');
	var cve_op = $('#id_orden').attr('value');
	var importe_op = $('#txtimporteOP').attr('value');
	
	if(importe_op==''||parseFloat(importe_op)<=0.00){jAlert('El importe de la Orden de Pago no es válido','Advertencia');return false;}
	if(parseFloat(importe_op)>parseFloat(importe_pedido)){jAlert('El importe de la Orden de Pago no debe ser mayor al importe disponible de la Orden de Servicio/Trabajo','Advertencia'); return false;}
	
	controladorOrdenPagoRemoto.generarDetallesRequisicionParcial(cve_req, cve_op, importe_op,{
		callback:function(items){	
			if (items==true) {
				limpiarDetalle();
				llenarTablaDeDetallesOrdenes();	
				CloseDelay("Movimientos de Requisicion(es) cargado(s) satisfactorimente");		   		   
		   }
		   else
				jAlert('No se han podido agregar los movimientos a la Orden de Pago, los movimientos ya existen o no es posible cargar mas de dos movimientos a la vez', 'Advertencia');	
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString, 'Error');       
			}
	}); 					
}

function getTablaOT(items,tipoElemento){
	var tabla ="<table width='80%' align='center'><tr><td><h1>Ordenes de Trabajo y Servicio</h1></td></tr></table>";
		tabla +="<table width='80%' border='0' align='center' cellpadding='0' cellspacing='0' class='formulario'>";
  	   tabla +="<tr><td  class='TituloFormulario' >&nbsp;</td><td  class='TituloFormulario' width='105'>No. Orden</td><td  class='TituloFormulario' width='110'>Proyecto/Partida</td><td  class='TituloFormulario'>Concepto</td><td  class='TituloFormulario' width='70'>Importe</td><td></td></tr>";
	jQuery.each(items,function(i) {							   
			tabla +="<tr ><td><input type='"+tipoElemento+"' name='clavesOT' id='clavesOT' value='"+this.CVE_REQ+"' ></td><td>"+this.NUM_REQ+"</td><td>"+this.PROYECTO+"- "+this.CLV_PARTID+"</td><td>"+this.OBSERVA+"</td><td>"+formatNumber(this.IMPORTE)+"</td></tr>";
        }); 	
	tabla +="<tr><td height ='25' align='center' colspan='6' ><input name='btnGuardarOT' id='btnGuardarOT' type='button' class='botones' onClick='generarDetallesOT();' value='Aceptar ' />&nbsp;<input   name='' id='' type='button' class='botones' onClick='_closeDelay();' value='Cancelar' /></td></tr>";
	return tabla +="</table>";	
}
function generarxListaOP (){
	
}
//Obtener la fecha actual
function obtiene_fecha() {

	var fecha_actual = new Date()

	var dia = fecha_actual.getDate()
	var mes = fecha_actual.getMonth() + 1
	var anio = fecha_actual.getFullYear()

	if (mes < 10)
	mes = '0' + mes

	if (dia < 10)
	dia = '0' + dia

	return (dia + "/" + mes + "/" + anio)
	}

	function MostrarFecha() {
	document.write ( obtiene_fecha() )
	}
	
//Muestra Listado de Facturas para generar la Orden de Pago
	function getDevenOP(){
		jWindow('<iframe width="800" height="400" name="FAC" id="FAC" frameborder="0" src="../../sam/consultas/muestra_dev_op.action?idtipogasto=1"></iframe>','Devengado para la Orden de Pago', '','Cerrar',1);
	}	
	
//--------------------------------------Generar la Orden de Pago desde una lista de facturas *****------18/05/2017	
function generarOPS(checkFacturas){
	 alert ("demos del listado orden pago.js" + checkFacturas);
	
	 //alert("id unidad" + $('#cbUnidad').val());
	   controladorOrdenPagoRemoto.geraropxfacturas($('#cbUnidad').val(),checkFacturas,{
			callback:function(items){
				alert("orden de pago generada" + items);
			} 					   				
			,
			errorHandler:function(errorString, exception) { 
				jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");       
			}
		
	});
}