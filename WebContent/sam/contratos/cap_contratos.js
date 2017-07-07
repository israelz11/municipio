var cerrar = false; 
$(document).ready(function() {
	var options = { 
        beforeSubmit:  showRequest,  
        success:       showResponse, 
        url:       '_subirArchivo.action?CVE_CONTRATO='+$('#CVE_CONTRATO').attr('value'),
        type:      'post', 
        dataType:  'json'
    }; 
	$('#forma').submit(function(){
		$(this).ajaxSubmit(options);
		return false;
	});
	
	getBeneficiarios('txtbeneficiario','CLV_BENEFI','');
	$('.tiptip a.button, .tiptip button').tipTip();
	$("#txtfechainicial").datepicker({showOn: 'button', buttonImage:"../../imagenes/cal.gif" , buttonImageOnly: true,dateFormat: "dd/mm/yy"});
	$("#txtfechatermino").datepicker({showOn: 'button', buttonImage:"../../imagenes/cal.gif" , buttonImageOnly: true,dateFormat: "dd/mm/yy"});
	$('#cmdguardar').click(function (event){guardaContrato();});
	$('#cmdcerrar').click(function (event){cierraContrato();});
	$('#cmdnuevoconcepto').click( function(event){nuevoConcepto();});
	$('#cmdnuevo').click(function(event){nuevoContrato();});
	$('#cmdagregar').click( function(event){agregarConcepto();});
	
	$('#txtnumreq').keypress(function(event){if (event.keyCode == '13'){muestraDocumento();}});
	 //Configura los tabuladores
	 $('#img_presupuesto').click(function(event){muestraPresupuesto();});
	 $('#txtproyecto').blur(function(event){__getPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible','');});
	 $('#txtpartida').blur(function(event){__getPresupuesto($('#ID_PROYECTO').attr('value'), $('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible','');});
	
	 $('#tabuladores').tabs();
	 $('#tabuladores').tabs('enable',0);
	 $('#tabuladores').tabs('option', 'disabled', [1]);
	 //if($('#txtnumreq').attr('value')!='') cargarOS($('#CVE_REQ').attr('value'));
	 if($('#CVE_CONTRATO').attr('value')!='0') {
		 getConceptos();
		 mostrarDetallesArchivos();
		 $('#tabuladores').tabs('enable',1);
	 }
	 
	 $('#ui-datepicker-div').hide();
	 $('#cbotipocontrato').change(function(event){ValidarTipoContrato();});
	 ValidarTipoContrato();
});

function subirArchivo(){
	if($('#archivo').attr('value')==''||$('#CVE_CONTRATO').val()==null|| $('#CVE_CONTRATO').val()==0)
		return false;
	ShowDelay("Subiendo archivo al servidor");
	$('#forma').submit();
}

function showRequest(formData, jqForm, options) { 
    return true; 
} 
 
function showResponse(data)  { 
 	if(data.mensaje){
		CloseDelay("Archivo guardado con éxito");
		mostrarDetallesArchivos();
		//document.location = "cap_contratos.action?cve_contrato="+$('#CVE_CONTRATO').val();
		$('#archivo').attr('value','');
	}
	else{
		_closeDelay();
		jError("No se ha podido cargar el archivo por algunas de las siguientes razones: <br>*Solo se permite un archivo por factura<br>*El nombre del archivo es muy largo<br>*El nombre del archivo contiene caracteres no válidos<br>*Formato de archivo incorrecto", "Error");
	}
}

function mostrarDetallesArchivos(){
	var cve_factura = $('#CVE_CONTRATO').val();
	quitRow("listasArchivo");
	ControladorContratosRemoto.getArchivosContrato(cve_factura, {
						callback:function(items) {
								jQuery.each(items,function(i){
									pintaTablaDetallesArchivos(this);
								});
					} 
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString,"Error");          
					}
	});
}

function pintaTablaDetallesArchivos(m){
	 var htmlRemove = "<img src=\"../../imagenes/cross.png\" style='cursor: pointer;' alt=\"Eliminar\" width=\"16\" height=\"16\" border=\"0\" onClick=\"eliminarArchivo("+m.ID_ARCHIVO+")\" >";
	appendNewRow("listasArchivo", [Td('', izquierda , '', '<div style="height:20px">&nbsp;<a href="../'+m.RUTA+'['+m.ID_ARCHIVO+'] '+m.NOMBRE+'" target="_blank">['+m.ID_ARCHIVO+'] '+m.NOMBRE+'</a></div>'),
						 Td('', centro , '', parseInt(parseInt(m.TAMANO)/1024)+' kb'),
						 Td('', centro , '', m.EXT),
						 Td('', centro , '', htmlRemove)
				]);
}

function eliminarArchivo(idArchivo){
	jConfirm('¿Confirma que desea eliminar el archivo?','Eliminar', function(r){
		if(r){
				ShowDelay("Eliminando archivo");
				ControladorContratosRemoto.eliminarArchivoContrato(idArchivo,{
						callback:function(map) {
							CloseDelay("Archivos eliminado con éxito");
							mostrarDetallesArchivos();
						},
						errorHandler:function(errorString, exception) { 
												jError(errorString, 'Error');          
						}
				});
		}
	});
}
 

function ValidarTipoContrato()
{
	var tipo = $('#cbotipocontrato').val();

	if(tipo == 7)
	{
		$('#tr_programa').hide();
		$('#tr_presupuesto').hide();
		$('#tr_importe').hide();
	}
	else
	{
		$('#tr_programa').show();
		$('#tr_presupuesto').show();
		$('#tr_importe').show();
	}
}


function buscarBeneficiario(clv_benefi){
	ControladorContratosRemoto.getBeneficiarioContrato(clv_benefi,{
	  callback:function(items) {
				$('#txtbeneficiario').attr('value', getHTML(items));
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');     
		}
	});
	
}


function nuevoContrato(){
	document.location = "cap_contratos.action";
}


function agregarConcepto(){
	if ($('#txtproyecto').attr('value')=="") {jAlert('El proyecto de concepto no es válido'); return false;};
    if ($('#txtpartida').attr('value')=="") {jAlert('La partida del concepto no es válido'); return false;}
    if ($('#cbomes').attr('value')=="") {jAlert('El mes del presupuesto no es válido'); return false;}
	if ($('#txtimporte').attr('value')=="") {jAlert('El importe del concepto no es válido'); return false;}
	
	ShowDelay('Guardando concepto','');
	ControladorContratosRemoto.guardarConcepto($('#ID_DETALLE').attr('value'), $('#CVE_CONTRATO').attr('value'), $('#ID_PROYECTO').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'), $('#txtimporte').attr('value'),{
	  callback:function(items) {
				CloseDelay('Concepto guardado con exito');
				getConceptos();
				nuevoConcepto();
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');     
		}
	});
	
}
/*funcion para mostrar el listado del presupuesto*/
function muestraPresupuesto(){
	if($('#txtproyecto').attr('value')==''||$('#txtpartida').attr('value')=='')
		$('#ID_PROYECTO').attr('value', '0');
	
		//var idUnidad = $('#cbodependencia').val();
		var idUnidad = $('#cbUnidad2').val();
	
		if(idUnidad==null||idUnidad=="") idUnidad =0;
		
		if($('#cbomes').val()==0) {jAlert('Seleccione un periodo presupuestal válido','Advertencia'); return false;}

		__listadoPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'), $('#tipoGasto').val(), idUnidad);
}

function cierraContrato(){
	if(!cerrar) {jAlert('Es necesario que exista al menos un concepto de contrato para realizar esta operación','Advertencia'); return false;}
	var cve_contrato = $('#CVE_CONTRATO').attr('value');
	jConfirm('¿Confirma que desea cerrar Contrato?', 'Confirmar', function (r){
				if(r){
					ShowDelay('Cerrando contrato','');
					ControladorContratosRemoto.cerrarContrato(cve_contrato,{
										callback:function(items){
											if(getHTML(items)=="") {
												CloseDelay('Contrato cerrado con éxito', 2000, function(){
														$('#cmdcerrar').attr('disabled', true);
														 //getReporteContrato($('#CVE_CONTRATO').attr('value'));
														 document.location='lista_contratos.action';
													});
											}
									}
									,
								errorHandler:function(errorString, exception) { 
									jError(errorString, 'Error');   
									return false;
								}
					});
				}
			});s
}

function guardaContrato(){
	var valida = _validate();
	if(valida){
		jConfirm('¿Confirma que desea guardar la información de Contrato?','Guardar', function(r){
		if(r){
			
				var id_contrato = $('#CVE_CONTRATO').attr('value');
				if($('#CVE_DOC').val()==null) $('#CVE_DOC').attr('value', 0);
				ShowDelay('Guardando contrato','');
				ControladorContratosRemoto.guardarContrato(id_contrato, $('#cbodependencia').val(), $('#txtnumcontrato').attr('value'), $('#txtfechainicial').attr('value'), $('#txtfechatermino').attr('value'), $('#txtnumoficio').attr('value'), $('#txttiempoentrega').attr('value'), $('#cbotipocontrato').attr('value'), $('#txtdescripcion').attr('value'), $('#txtanticipo').attr('value'), $('#tipoGasto').val(), $('#CLV_BENEFI').val(), $('#CVE_DOC').val(), {
					callback:function(items){
							$('#CVE_CONTRATO').attr('value',items);
							$('#tabuladores').tabs('enable',1);
							getConceptos();
							subirArchivo();
							CloseDelay('Contrato guardado con exito');
					},
						errorHandler:function(errorString, exception) { 
							jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>', 'Error al guardar Pedido');   
							return false;
						}
				});
		    }
		});
		
	}
	
}

function _validate(){
	if($('#cbodependencia').val()==0) {jAlert('La Unidad Administrativa no es válida','Advertencia'); return false;}
	if($('#txtnumcontrato').attr('value')=='') {jAlert('El número de contrato no es válido','Advertencia'); return false;}
	if($('#cbotipocontrato').attr('value')==''){jAlert('El tipo de contrato no es válido','Advertencia'); return false;}
	if($('#tipoGasto').attr('value')==0) {jAlert('El tipo de gasto no es válido', 'Advertencia'); return false;}
	if($('#CLV_BENEFI').attr('value')=='' ||$('#CLV_BENEFI').attr('value')=='0') {jAlert('El nombre del Proveedor no es válido','Advertencia'); return false;}
	if($('#txtfechainicial').attr('value')=='') {jAlert('La fecha inicial no es válida','Advertencia'); return false;} 
	if($('#txtfechatermino').attr('value')=='') {jAlert('La fecha de termino no es válida','Advertencia'); return false;}
	if($('#txtdescripcion').attr('value')==''){jAlert('La descripcion del contrato no es válida','Advertencia'); return false;}
	
	return true;
}

//Carga el documento segun el tipo de Compromiso a capturar..............27/06/2017................................
function muestraDocumento(){
	if($('#txtbeneficiario').attr('value')=='') $('#CLV_BENEFI').attr('value', '');
	var num_req = $('#txtdocumento').attr('value');
	var idDependencia = $('#cbodependencia').val();
	var clv_benefi = $('#CLV_BENEFI').attr('value');
	
	if(idDependencia==0||idDependencia=="") {jAlert('Es necesario seleccionar la Unidad Administrativa para listar los documentos'); return false;}
	if($('#cbotipocontrato').val()==0){jAlert('Es necesario seleccionar el tipo de contrato', 'Advertencia'); return false;}


	if 	($('#cbotipocontrato').val()==7){            //Adquisicion
				jWindow('<iframe width="650" height="400" name="consultaPedido" id="consultaPedido" frameborder="0" src="../../sam/consultas/muestra_pedidos_contratos.action?idDependencia='+idDependencia+'"></iframe>','Listado de Pedidos', '','Cerrar ',1);
		}
	else if ($('#cbotipocontrato').val()==13)      //VALE
	{
		
		jWindow('<iframe width="750" height="350" name="ventanaVales" id="ventanaVales" frameborder="0" src="../../sam/consultas/muestraVales_tipo_contratos.action?idVale='+$('#CVE_DOC').attr('value')+'&idDependencia='+idDependencia+'&clv_benefi='+clv_benefi+'"></iframe>','Listado de Vales disponibles', '','Cerrar',1);
	}																																											//id_vale,num_vale,clv_benefi, comprobado,por_comprobar
		
	else
		//alert("deberia mostrar el listado de OS/OT");
		jWindow('<iframe width="800" height="400" name="DocumentoContrato" id="DocumentoContrato" frameborder="0" src="../../sam/consultas/muestra_os_contratos.action?num_req='+num_req+'&idDependencia='+idDependencia+'&clv_benefi='+clv_benefi+'"></iframe>','O.S. y REQ. Calendarizadas disponibles para contratos', '','Cerrar',1);
		

	

}

function cargarOS(cve_req, num_doc, proveedor, clv_benefi){
	$('#txtdocumento').attr('value', num_doc);
	$('#CVE_DOC').attr('value', cve_req);
	$('#txtbeneficiario').attr('value', proveedor);
	$('#CLV_BENEFI').attr('value', clv_benefi);
	_closeDelay();
}

function regresaPedido(cve_ped, num_ped, clv_benefi){
	$('#CVE_DOC').attr('value', cve_ped);
	$('#txtdocumento').attr('value', num_ped);
	$('#CLV_BENEFI').attr('value', clv_benefi);
	buscarBeneficiario($('#CLV_BENEFI').attr('value'));
	_closeDelay();
}


function getConceptos(){
	quitRow("listaConceptos");
	ControladorContratosRemoto.getConceptosContrato($('#CVE_CONTRATO').attr('value'), {
						   callback:function(items) { 
						   			
						   		if(items.length>0) {
									$('#cbotipocontrato').attr('disabled', true);
									$('#tipoGasto').attr('disabled', true);
									$('#cmdcerrar').attr('disabled', false);
									$('#tabuladores').tabs('enable',1);
									cerrar = true;
								}
								else{
									$('#cbotipocontrato').attr('disabled', false);
									$('#tipoGasto').attr('disabled', false);
									$('#cmdcerrar').attr('disabled', true);
									cerrar = false;
								}
						   		jQuery.each(items,function(i){
										pintarDetalles('listaConceptos', this);
								});
						   }
	});
}

/*funcion para pintar las filas de los subtotales*/
function pintarDetalles(table, obj){
	/*Primera fila*/
		var htmlCheck = "<input type='checkbox' name='chkConcepto' id='chkConcepto' value='"+obj.ID_DETALLE_COMPROMISO+"'>";
		appendNewRow(table,[ Td('', centro, '', '<div style ="height:18px">'+htmlCheck+"</div>"),
							 Td('', izquierda  , '', obj.DEPENDENCIA),
							 Td('', centro, '', obj.DESC_PERIODO),
							 Td('', centro, '', obj.N_PROGRAMA),
							 Td('', centro, '', obj.CLV_PARTID),
							 Td('', derecha, '', "$"+formatNumber(getHTML(obj.IMPORTE)))
						  ]);
}


function eliminarConcepto()
{
	 //if($('#CVE_DOC').attr('value')!=''&&$('#CVE_DOC').attr('value')!='0') {jAlert('No se pueden eliminar los conceptos que se agregan a travez de un documento externo','Advertencia'); return false;}
	 var checkMovimientos = [];
     $('input[name=chkConcepto]:checked').each(function() { checkMovimientos.push($(this).val());});	
	 if (checkMovimientos.length>0){
		jConfirm('¿Confirma que desea eliminar los conceptos del Contrato?','Confirmar', function(r){
				if(r){
						ShowDelay('Eliminando concepto','');
						ControladorContratosRemoto.eliminarConceptos($('#CVE_CONTRATO').attr('value'), checkMovimientos, {
							callback:function(items) {
								nuevoConcepto(); 		
								getConceptos();
								CloseDelay('Conceptos eliminados con éxito');	
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');          
						}
					});
				}
	   });
	 } 
	else 
	    jAlert('Es necesario que seleccione por lo menos un concepto del listado', 'Advertencia');
}

function nuevoConcepto()
{
	$('#txtproyecto').attr('value', '');
	$('#ID_PROYECTO').attr('value', '');
	$('#txtpartida').attr('value', '');
	$('#txtpresupuesto').attr('value', '');
	$('#txtdisponible').attr('value', '');
	$('#txtimporte').attr('value', '');
}



/*Metodo para mostrar el reporte PDF del pedido*/
function getReportePedido(clavePed) {
	$('#clavePedido').attr('value',clavePed);
	$('#forma').attr('target',"impresion");
	$('#forma').submit();
	$('#forma').attr('target',"");
}

function getValeDocumento(id_vale,num_vale,clv_benefi, comprobado,por_comprobar){
	$('#txtdocumento').val(num_vale);
	$('#CVE_DOC').attr('value', id_vale);
	_closeDelay();
} 

//Agregado por Abraham Gonzalez el 27-06-2017 para comprobacion de vales desde contratos ------------------
function costumFunction(){
	cargarBeneficiarioyPresupuestoVale();
}

function cargarBeneficiarioyPresupuestoVale()
{
	if($('#cbotipocontrato').val()==13)
		if($('#CVE_DOC').val()!=''&&$('#CVE_DOC').val()!='0')
			if($('#ID_PROYECTO').val()!=''&&$('#ID_PROYECTO').val()!='0')
				if($('#CLV_PARTID').val()!=''&&$('#CLV_PARTID').val()!='0')
				{
					//$('#trEntrada').hide();
					//buscamos el beneficiario
					ControladorContratosRemoto.getBeneficiarioContratos('VAL', $('#CVE_DOC').val(), {
							
							  callback:function(items){
											$('#txtbeneficiario').val()(items);
											 
						} 					   				
						,
							errorHandler:function(errorString, exception) { 
								jError(errorString,'Error');   
							}
						});	
						
				}
				
}