 var checkPresupuesto = new Array();
 var ID_PED = 0;
/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/
$(document).ready(function() {  
	
	/*Inhabilita todos los divs*/	
	$('#div_os').hide();
	$('#div_os_vehiculo').hide();
	$('#div_os_prestador').hide();
	$('#div_os_presupuesto').hide();
	$('#cbomeses').attr('disabled',true);
			
	$("#tab2primary").hide();
	
	$('#cbotipo').on('change',function(event){//El metodo on asigna uno o mas controladores de eventos para los elementos seleccionados.
		tipoRequisiciones();
	});
	
	/*define los eventos para los controles*/
	
	$('#txtproyecto').bestupper(); 
	//$('#cmdnuevorequisicion').click(function (event){nuevaRequisicion();});
	$('#cmdenviarlotes').click(function(event){mostrarEnviarLotesPedido();});
	$('#cmdnuevoconcepto').click(function (event){nuevoConcepto();});
	$('#cmdguardarequisicion').click(function(event){guardarRequisicionPrincipal();});
	$('#cmdguardarconcepto').click(function(event){guardarConceptoRequisicion();});
	$('#cmdcerrar').click(function(event){cerrarRequisicion();});
	$('#cmdcerrar').prop('disabled',true);
	$('#img_contrato').click(function(event){jInformation('Este modulo se encuentra deshabilitado por el momento','InformaciÃ³n');/*muestraContratos();*/});
  	$('#img_quitar_contrato').click(function(event){jInformation('Este modulo se encuentra deshabilitado por el momento','InformaciÃ³n');/*removerContrato();funciones();*/});
	$('#cmdenviarlotes').prop('disabled',true);
	$('#cmdreenumerar').prop('disabled',true);
	$('#cmdimportar').prop('disabled',true);
	$('#txtpartida').keypress(function(event){return keyNumbero(event);});
	$('#txtcantidad').keypress(function(event){return keyNumbero(event);});
	$('#txtprecioestimado').keypress(function(event){return keyNumbero(event);});	
	$('#txtproyecto').blur(function(event){__getPresupuesto($('#ID_PROYECTO').val(),$('#txtproyecto').val(),$('#txtpartida').val(), $('#cbomeses').val(),  'txtpresupuesto','txtdisponible','');});
	$('#txtpartida').blur(function(event){__getPresupuesto($('#ID_PROYECTO').val(), $('#txtproyecto').val(),$('#txtpartida').val(), $('#cbomeses').val(),  'txtpresupuesto','txtdisponible','');});
	
	$('#img_presupuesto').click(function(event){muestraPresupuesto();});
	$('#img_producto').click(function(event){muestraProductos();});
	$('#cmdimportar').click(function(event){muestraImportarLotes();});
	$('#cmdreenumerar').click(function(event){muestraReenumerar();});
	$('#img_vale').click(function(event){antesMuestraVales();});
	 /*Configura los tabuladores*/
	 //$('#tabuladores').tabs();
	 //$('#tabuladores').tabs('enable',0);
	 //$('#tabuladores').tabs('option', 'disabled', [1]);
	 //$("#txtfecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
	 /*funciones*/
	 if($('#CVE_REQ').val()==0) tipoRequisiciones();
	 
	//getBeneficiarios('txtprestadorservicio','CVE_BENEFI',$('#tipoBeneficiario').val());
	//getUnidad_Medidas('txtunidadmedida','CVE_UNIDAD_MEDIDA');
	 mostrarRequisicion($('#CVE_REQ').val());
	 
	 getMesRequisicion($('#cbomeses').val());
	 
	 $('#txtprecioestimado').val('readonly', '');
	 $('#fila_contrato').hide();
	 $('#fila_disponibleVale').hide();
	
	 $('#txtfecha').datetimepicker({
			format: 'DD/MM/YYYY'
		});
	 
});

function tipoRequisiciones(){
	

	var tiporequi = $('#cbotipo').val();
	//alert("Opcion seleccionada por cbotipo en val: "+demovalor2);
	
	/*Retorna si vale cero*/
	if(tiporequi=='0') return false;
	
	switch(tiporequi){
	
		case '1': /*Para bienes*/
				$('#div_os_presupuesto').show();
				$('#div_os').hide();
		break;
		case '2': /*Para servicios*/
				$('#div_os').show();
				$('#div_os_prestador').show();
				$('#div_os_presupuesto').show();
				$('#div_os_prestador').show();
				$('#div_os_vehiculo').hide();
		break;
		case '3': /*Para servicio a vehiculos*/
				$('#div_os').show();
				$('#div_os_prestador').show();
				$('#div_os_presupuesto').show();
				$('#div_os_prestador').show();
				$('#div_os_vehiculo').show();
		break;
		case '4': /*Para maquinaria pesada*/
				$('#div_os').show();
				$('#div_os_prestador').show();
				$('#div_os_presupuesto').show();
				$('#div_os_prestador').show();
				$('#div_os_vehiculo').show();
		break;
		case '5': /*Para servicio a bombas*/
				$('#div_os').show();
				$('#div_os_prestador').show();
				$('#div_os_presupuesto').show();
				$('#div_os_prestador').show();
				$('#div_os_vehiculo').hide();
		break;
		case '6': /*Para paquetes*/
				$('#div_os_presupuesto').show();
				$('#div_os').hide();
				$('#div_os_prestador').hide();
				$('#div_os_prestador').hide();
				$('#div_os_vehiculo').hide();
		break;
		case '7': /*Para Req. calendarizada*/
				$('#div_os_presupuesto').show();
				$('#div_os').hide();
				$('#div_os_prestador').hide();
				$('#div_os_prestador').hide();
				$('#div_os_vehiculo').hide();
				/*activa el combo de meses*/
				$('#cbomeses').prop('disabled',false);
		break;
		case '8': /*Para O.S calendarizada*/
				$('#div_os').show();
				$('#div_os_prestador').show();
				$('#div_os_presupuesto').show();
				$('#div_os_prestador').show();
				$('#div_os_vehiculo').hide();
				/*activa el combo de meses*/
				$('#cbomeses').prop('disabled',false);
		break;
	}
}


function antesMuestraVales(){
	if($('#cbotipo').val()==7||$('#cbotipo').val()==8) {jAlert('No es posible usar vales para documentos de tipo calendarizados', 'Advertencia'); return false;}
	muestraVales();
}

function comprobarVale(){
	if($('#txtvale').val()=='') {
		$('#CVE_VALE').val('0');
		$('#txtdisponiblevale').val();
		$('#txtcomprobadovale').val();
		$('#fila_disponibleVale').hide();
	}
}

function getValeDocumento(idVale, num_vale, disponible, comprobado){
	$('#CVE_VALE').val(idVale);
	$('#txtvale').val(num_vale);
	$('#txtdisponiblevale').val(formatNumber(parseFloat(disponible),'$'));
	$('#txtcomprobadovale').val(formatNumber(parseFloat(comprobado),'$'));
	$('#fila_disponibleVale').show();
	
	muestraPresupuesto();
	//_closeDelay();
	
}

function muestraReenumerar(){
	if($('#cbotipo').val()!=1&&$('#cbotipo').val()!=7) {jAlert('Esta opcion solo esta disponible para Requisiciones', 'Advertencia'); return false;}
	jWindow('<iframe width="850" height="400" id="ventanaReenumerar" frameborder="0" src="../../sam/requisiciones/reenumerarLotes.action?num_req='+$('#txtrequisicion').val()+'&cve_req='+$('#CVE_REQ').val()+'"></iframe>','Reenumerar consecutivo de lotes', '','Cerrar',1);
}

function muestraImportarLotes(){
	if(ID_PED>0) 
		jAlert('Existen lotes que ya se encuentran relacionados a algÃºn Pedido, si decide importar nuevos lotes estos se reenumerarÃ¡n automaticamente y se perdera el consecutivo de lote en el Pedido,\n se recomienda borrar los lotes de los Pedidos antes de realizar esta operaciÃ³n','Advertencia', function(){_muestraImportarLotes();});
	else 
		_muestraImportarLotes();
	
}

function _muestraImportarLotes(){
	if($('#cbotipo').val()!=1&&$('#cbotipo').val()!=7) {jAlert('Esta opcion solo esta disponible para Requisiciones', 'Advertencia'); return false;}
	jWindow('<iframe width="850" height="400" id="ventanaImportar" frameborder="0" src="../../sam/requisiciones/muestraImportar.action?'+'"></iframe>','Importar lotes desde una RequisiciÃ³n existente', '','Cerrar',1);
}

function getBeneficiario(cve_benefi , objeto){
	if(cve_benefi==null||cve_benefi==0||cve_benefi=='') return false;
	//accesar y buscar el beneficiario
	controladorRequisicion.getBeneficiario(cve_benefi,{
		  			callback:function(items) { 
						$('#'+objeto).val(getHTML(items));
					}
					,
					errorHandler:function(errorString, exception){ 
							jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
							return false;
					}
	});
}

function funciones(){
	if($('#CVE_CONTRATO').val()=='') return false;
	if($('#CVE_CONTRATO').val()!='0'&&$('#CVE_CONTRATO').attr('value')!=''){
		$('#txtproyecto').val($('#CPROYECTO').val());
		$('#txtpartida').val($('#CCLV_PARTID').val());
		$('#txtproyecto').prop('disabled',true);
		$('#txtpartida').prop('disabled',true);
		$('#txtprestadorservicio').prop('disabled', true);
		//obtener ek nombre del beneficiario
		getBeneficiario($('#CCLV_BENEFI').val('txtprestadorservicio'));
		$('#CVE_BENEFI').val($('#CCLV_BENEFI').val());
		__getPresupuesto($('#ID_PROYECTO').val(),$('#txtproyecto').val(),$('#txtpartida').val(), $('#cbomeses').val(),  'txtpresupuesto','txtdisponible',$('#tipoGasto').val());
	}
	else{
		$('#txtproyecto').val('');
		$('#txtpartida').val('');
		$('#txtproyecto').prop('disabled',false);
		$('#txtpartida').prop('disabled',false);
		$('#txtpresupuesto').val('');
		$('#txtdisponible').val('');
		$('#txtprestadorservicio').prop('disabled', false);
		$('#txtprestadorservicio').val('');
		$('#CVE_BENEFI').val('0');
	}
}
/*-------------------------------- funcion para mostrar la requisicion ------------------------------------------------*/
/*Muestra la Requisicion en edicion llamada desde el listado de requisiciones....*/
function mostrarRequisicion(cve_req){
	
	if(cve_req!=0){
		ShowDelay('Cargando Requisicion');
		controladorRequisicion.getLightRequisicion(cve_req, {
		  			callback:function(items) { 
					jQuery.each(items,function(i){
								$('#cbodependencia').val(this.ID_DEPENDENCIA);
								$('#txtrequisicion').val(this.NUM_REQ);
								$('#txtfecha').val(getHTML(this.FECHA));
								$('#txtnotas').val(this.OBSERVA);
								$('#cbotipo').val(this.TIPO);
								$('#cbotipo').prop('disabled', true);
								$('#cbotipo').change();
								tipoRequisiciones();
								
								$('#cbomeses').val(true);
								$('#cbomeses').val(this.PERIODO);
								$('#txttipobien').val(getHTML(this.VEHICULO));
								$('#txtmarca').val(getHTML(this.MARCA));
								$('#txtmodelo').val(getHTML(this.MODELO));
								$('#txtusuario').val(getHTML(this.USUARIO));
								$('#txtnuminventario').val(getHTML(this.NUM_INV));
								$('#txtplacas').val(getHTML(this.PLACAS));
								$('#txtcolor').val(getHTML(this.COLOR));
								$('#txtprestadorservicio').val(getHTML(this.NCOMERCIA));
								$('#CVE_BENEFI').val(getHTML(this.CLV_BENEFI));
								$('#txtprestadorservicio').val(getHTML(this.PROVEEDOR));
								$('#CVE_CONCURSO').val(getHTML(this.CVE_CONCURSO));
								$('#txtnumcontrato').val(getHTML(this.NUM_CONTRATO));
								$('#CVE_CONTRATO').val(getHTML(this.CVE_CONTRATO));
								/*Cargar informacion del vale si existe 02/Abr/2012 */
								if(getHTML(this.CVE_VALE)!=''){
									//buscar el vale
									$('#txtvale').val(rellenaCeros(getHTML(this.CVE_VALE.toString()),6));
									$('#CVE_VALE').val(getHTML(this.CVE_VALE));
									controladorRequisicion.getListaValesPresupuesto($('#CVE_VALE').val(), $('#CVE_BENEFI').val(), 0, $('#cbodependencia').val(), {
										  callback:function(items){
											  jQuery.each(items,function(i) {
												getValeDocumento($('#CVE_VALE').val(), $('#txtvale').val(), this.DISPONIBLE, this.COMPROBADO);
												$('#fila_disponibleVale').show();
											  });
										} 		
										,
										errorHandler:function(errorString, exception) { 
											jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
											return false;
										}
									});

								}
								if($('#CVE_CONTRATO').val()!='0'&&$('#CVE_CONTRATO').val()!='') {
									$('#img_quitar_contrato').val('src', '../../imagenes/cross.png');
									$('#CPROYECTO').val(getHTML(this.CPROYECTO));
									$('#CCLV_PARTID').val(getHTML(this.CCLV_PARTID));
									$('#CLV_PARBIT').val(getHTML(this.CLV_PARBIT2));
									$('#txtproyecto').val($('#CPROYECTO').val());
									$('#txtpartida').val($('#CCLV_PARTID').val());
									$('#txtproyecto').val(this.N_PROGRAMA);
									$('#txtpartida').val((this.CLV_PARTID));
									$('#txtprestadorservicio').prop('disabled',false);
									//$('#txtproyecto').prop('disabled',true);
									//$('#txtpartida').prop('disabled',true);
									//$('#txtprestadorservicio').prop('disabled',true);
									
								}
								else{
									$('#img_quitar_contrato').val('src', '../../imagenes/cross2.png');
									$('#CPROYECTO').val('');
									$('#CCLV_PARTID').val('');
									$('#CLV_PARBIT').val('');
									$('#ID_PROYECTO').val(getHTML(this.ID_PROYECTO));
									$('#txtproyecto').val(getHTML(this.N_PROGRAMA));
									$('#txtpartida').val(getHTML(this.CLV_PARTID));
									$('#txtprestadorservicio').prop('disabled',false);
									
								}
					
								/*Activa o Inactiva Boton cerrar requisicion*/
								if(parseInt(this.STATUS)==0) $('#cmdcerrar').prop('disabled', false);
								/*$('#txtconcurso').attr('value',);*/
								$('#cboarea').val(this.AREA);
								//$('#tabuladores').tabs('enable',1);/******************** Revisar por los cambio realizados en los tabuladores **************************/
								$('.nav-tabs a:first').tab('show');//Al cargar muestra la primer pestaña por default
								//$('.nav-tabs a:last').tab('show') 
								//$('.nav-tabs a[href="#tab2primary"]').tab('show')
								//$('[href="#tab2primary"]').tab('show');
								mostrarTablaConceptos(cve_req);
								if($('#cbotipo').val()=='1'||$('#cbotipo').val()=='7'){
									$('#cmdimportar').prop('disabled',false);
								}
								$('#txtpartida').blur();
								getMesRequisicion($('#cbomeses').val());
								
								//COMPROBAR QUE LA REQ FUE CERRADA AL MENOS UNA VEZ PARA PERMITIR LA EDICION DEL PRECIO DEL CONCEPTO
								comprobarCierreEnBitacora();
								//_closeDelay();
							});
						}
						,
						errorHandler:function(errorString, exception) { 
							swal('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
							return false;
						}
					});
	}
	
}

function comprobarCierreEnBitacora(){
	/*controladorRequisicion.comprobarCerradoBitacora($('#CVE_REQ').attr('value'), {
		  			callback:function(items) { 
						if(items==true){
									$('#txtprecioestimado').attr('readonly', '');
								}
								else{
									$('#txtprecioestimado').attr('readonly', 'readonly');
								}
					}
					,
					errorHandler:function(errorString, exception) { 
							jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
							return false;
					}
	});*/
}
/*funcion para mostrar el listado del presupuesto*/
function muestraPresupuesto(){
	if($('#txtproyecto').val()==''||$('#txtpartida').attr('value')=='')
		$('#ID_PROYECTO').val('0');
		
		var idUnidad = $('#cbodependencia').val();
		if(idUnidad==null||idUnidad=="") idUnidad =0;
		/*Para el presupuesto con vales*/
		if($('#CVE_VALE').val()!='0'&&$('#CVE_VALE').val()!='')	{
		var tipo_gto = $('#tipoGasto').val();
		if(typeof(tipo_gto)=='undefined') tipo_gto =0;
		if($('#txtvale').val()=='') $('#CVE_VALE').val('');
		if($('#CVE_VALE').val()==''||$('#CVE_VALE').val()=='0') {jAlert('Es necesario seleccionar un Vale para mostrar su informacion presupuestal', 'Advertencia'); return false;}
		
		__listadoPresupuestoVale($('#ID_PROYECTO').val(),$('#txtproyecto').val(),$('#txtpartida').val(), $('#cbomeses').val(),tipo_gto, 0, $('#CVE_VALE').val());
	}
	else /*Para presupuesto normal*/
		__listadoPresupuesto($('#ID_PROYECTO').val(),$('#txtproyecto').val(),$('#txtpartida').val(), $('#cbomeses').val(), 0, idUnidad);
}

/*funcion principal de guardar la Requisición*/
function guardarRequisicionPrincipal(){
	var result = validate();
	var requiscion=$("#txtrequisicion").val();
	if (result==false) return false;
	alert($("#txtrequisicion").val());
	var v = parseInt($('#cbotipo').val());
	

	//if($('#txtprestadorservicio').val()==''||$('#txtprestadorservicio').val()=='0') $('#CVE_BENEFI').val('NULL');
	
	//swal.clickConfirm()
	swal.queue([{
		  title: 'Cerrar requisicion',
		  text: 'Confirma que desea guardar la Requisición',
		  confirmButtonText: 'Confirmar',
		  showLoaderOnConfirm: true,
		  preConfirm: function () {
		    return new Promise(function (resolve) {
		    	controladorRequisicion.guardarRequisicion($('#ID_PROYECTO').val(),$('#CVE_REQ').val(), $('#CVE_CONTRATO').val(), $('#CVE_VALE').val(), $('#txtrequisicion').val(), $('#cbodependencia').val(), $('#txtfecha').val(), $('#cbotipo').val(),$('#txtnotas').val(), $('#cbomeses').val(), '0', $('#txtproyecto').val(), $('#txtpartida').val(),$('#CVE_BENEFI').val(),$('#cboarea').val(), $('#txttipobien').val(),$('#txtmarca').val(), $('#txtmodelo').val(), $('#txtplacas').val(), $('#txtnuminventario').val(), $('#txtcolor').val(), $('#txtusuario').val(), $('#CVE_CONCURSO').val());
		    	$('#cmdcerrar').prop('disabled',false);
				//$('#tab2primary').tabs('enable',1);//TypeError: $(...).tabs is not a function
				if($('#CVE_REQ').attr('value')==0) $('.nav-tabs a:first').tab('show');
		    	$('.nav-tabs a[href="#tab2primary"]').tab('show')
				
		        resolve()
		    })
		  }
		}]).then(function (resolve) {
			swal({
				  title: 'Cerrando',
				  text: 'Documento cerrado con exito. '+ requiscion, 
				  timer: 3000,
				  onOpen: function () {
				    swal.showLoading()
				  }
				}).then(
				  function () {},
				  // handling the promise rejection
				  function (dismiss) {
				    if (dismiss === 'timer') {
				      console.log('Cerrado por terminar el tiempo')
				    }
				  }
				)
		})
	/*
	jConfirm('Â¿Confirma que desea guardar la RequisiciÃ³n?','Confirmar', function(r){
		if(r){
		  ShowDelay('Guardando requisiciÃ³n','');
		  controladorRequisicion.guardarRequisicion($('#ID_PROYECTO').val(),$('#CVE_REQ').val(), $('#CVE_CONTRATO').val(), $('#CVE_VALE').val(), $('#txtrequisicion').val(), $('#cbodependencia').val(), $('#txtfecha').val(), $('#cbotipo').val(),$('#txtnotas').val(), $('#cbomeses').val(), '0', $('#txtproyecto').val(), $('#txtpartida').val(),$('#CVE_BENEFI').val(),$('#cboarea').val(), $('#txttipobien').val(),$('#txtmarca').val(), $('#txtmodelo').val(), $('#txtplacas').val(), $('#txtnuminventario').val(), $('#txtcolor').val(), $('#txtusuario').val(), $('#CVE_CONCURSO').val(), {
		  callback:function(items) { 	    
		  if (items!=null && items!=0 )
		  {  
		   		$('#cmdcerrar').attr('disabled',false);
				$('#tabuladores').tabs('enable',1);
				if($('#CVE_REQ').attr('value')==0) $('#tabuladores').tabs('option', 'selected', 1);
				$('#CVE_REQ').attr('value',items);
			  CloseDelay('RequisiciÃ³n guardada con Ã©xito');
		  }
		  else 
		  	  jInformation('No se guardo la informaciÃ³n No. de RequisiciÃ³n repetido', 'InformaciÃ³n');	  
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
			return false;
		}
	});
	}
	});*/
	
}

/*funcion para validar los datos de la  requisicion antes de guardar*/
function validate(){
	var existe = false;
	var requi = $('#txtrequisicion').val();
	//swal2-validationerror
	
	if($('#cbodependencia').val()==0){jAlert('Es necesario seleccionar la <b>Unidad Administrativa</b>','Error de validacion'); return false;}
	//if($('#txtrequisicion').val()==''){swal('Es necesario escribir el numero de <b>RequisiciÃ³n</b>','Error de validacion','error')};
	
	controladorRequisicion.comprobarExistencia($('#txtrequisicion').attr('value'),{callback:function(items){existe = items;}, errorHandler:function(errorString, exception) { jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br>Consulte a su administrador');}});
	if(existe==true&&$('#CVE_REQ').val()==0){jError('El numero de requisicion que esta intentando guardar ya existe en el sistema','Error'); return false;}

	if($('#txtfecha').val()==''){jAlert('Es necesario escribir una <b>Fecha</b> vÃ¡lida','Error de validaciÃ³n'); return false;}
	if($('#cbotipo').val()==0){jAlert('Es necesario seleccionar un <b>Tipo</b> de RequisiciÃ³n','Error de validacion'); return false;}
	//if ($('#txtproyecto').val()==''||$('#ID_PROYECTO').attr('value')==''||$('#ID_PROYECTO').attr('value')=='0'){jAlert('Es necesario escribir el <b>Programa</b>','Error de validaciÃ³n'); return false;}
	if ($('#txtpartida').val()==''){jAlert('Es necesario escribir la <b>Partida</b>','Error de validaciÃ³n'); return false;}
	if ($('#cbomeses').val()==0){jAlert('El <b>Presupuesto</b> no es valido','Error de validaciÃ³n'); return false;}
}

/*Guarda los conceptos o movimientos de la requisicion*/
function guardarConceptoRequisicion(){
	var error="";
 	var titulo ='Advertencia';
	if($('#cbotipo').val()=='2'||$('#cbotipo').val()=='3'||$('#cbotipo').val()=='4'){
		if($('#ID_REQ_MOVTO').val()==0&&parseInt($('#TOTAL_CONCEPTOS').val())>=1) {jAlert('Una Orden de Servicio/Trabajo no puede contener mÃ¡s de un lote</br>','Advertencia'); return false;} 
	}
	if($('#ID_ARTICULO').val()==''||$('#ID_ARTICULO').val()=='0') {jAlert('Es necesario seleccionar un producto vÃ¡lido</br>','Advertencia'); return false;}
	if($('#txtprecioestimado').val()=='') {jAlert('Es necesario especificar un precio de producto valido</br>', 'Advertencia'); return false;}
	//if($('txtprecioestimado').val()=='') {jError('Es necesario especificar un precio de producto valido','Error de validacion'); return false;}
	if($('#CVE_UNIDAD_MEDIDA').val()=='') jAlert('Es necesario especificar la unidad de medida del producto valido</br>','Advertencia');
	if($('#txtcantidad').val()=='') {jAlert('Es necesario especificar la cantidad de productos</br>', 'Advertencia'); return false;}
//	if($('#txtdescripcion').val()=='') error += 'Es necesario una descripcion valida</br>';
	if($('#txtproyecto').val()=='') {jAlert('Es necesario establecer un Programa valido</br>','Advertencia'); return false;}
	if($('#txtpartida').val()=='') {jAlert('Es necesario establecer una partida valida</br>','Advertencia');}
	if($('#cbotipo').val()=='2'||$('#cbotipo').val()=='3'||$('#cbotipo').val()=='4'&&$('#cbotipo').val()=='5') {
			if(parseInt($('#txtcantidad').val())>1) {jAlert('Una Orden de Servicio/Trabajo no puede contener mas de una cantidad de producto</br>', 'Advertencia'); return false;}
	}
	
	/*jConfirm('¿Confirma que desea guardar el lote?','Confirmar',function (r){
			if(r){*/
					ShowDelay('Guardando lote','');
					controladorRequisicion.guardarConcepto($('#CVE_REQ').val(), $('#cbotipo').val(), $('#ID_REQ_MOVTO').val(), $('#REQ_CONS').val(),  $('#ID_ARTICULO').val(), $('#CVE_UNIDAD_MEDIDA').val(),  $('#txtproducto').val(), $('#txtprecioestimado').val(), $('#txtcantidad').val(), $('#txtdescripcion').val(), {
		  			callback:function(items) { 
							if(items) {
									nuevoConcepto();
									mostrarTablaConceptos($('#CVE_REQ').val());
									CloseDelay('Lote guardado con Ã©xito', function(){ $('#txtproducto').focus();
										});
										
								} else jError('No se ha podido guardar el lote', 'Error');
						}
						,
						errorHandler:function(errorString, exception) { 
							jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
							return false;
						}
					});
					   				
			/*	}
			});*/
}

/*funcion para eliminar un movimiebto de a requisicion*/
function eliminarMovimientos(){
	 var checkMovimientos = [];
     $('input[name=chkconsecMovimiento]:checked').each(function() { checkMovimientos.push($(this).val());});	
  	 var cve_req = $('#CVE_REQ').val();
	 if (checkMovimientos.length>0){
		jConfirm('Â¿Confirma que desea eliminar los lotes de la requisiciÃ³n?','Confirmar', function(r){
				if(r){
						ShowDelay('Eliminando lote','');
						controladorRequisicion.eliminarMovimientoRequisicion(checkMovimientos, cve_req, {
							callback:function(items) {
								nuevoConcepto(); 		
								mostrarTablaConceptos(cve_req);
								CloseDelay('Lotes eliminados con Ã©xito');	
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
						}
					});
				}
			
	   });
	 } 
	else 
	    jAlert('Es necesario que seleccione por lo menos un lote del listado', 'Advertencia');
 }
	
/*funcion que permite mostrar los datos del concepto en pantalla para editarlos*/
function editarConcepto(ID_REQ_MOVTO){
	ShowDelay('Cargando lote','');
	controladorRequisicion.getConceptoRequisicion(ID_REQ_MOVTO,{
				callback:function(Map){
					$('#ID_REQ_MOVTO').val(ID_REQ_MOVTO);
					$('#ID_ARTICULO').val(Map.ID_ARTICULO);
					$('#CVE_UNIDAD_MEDIDA').val(Map.CLV_UNIMED);
					$('#GRUPO').val(Map.GRUPO);
					$('#SUBGRUPO').val(Map.SUBGRUPO);
					$('#CLAVE').val(Map.CONSEC);
					$('#REQ_CONS').val(Map.REQ_CONS);
					$('#txtproducto').val(Map.PRODUCTO);
					$('#txtunidadmedida').val(Map.UNIDAD)
					$('#txtprecioestimado').val(Map.PRECIO_EST);
					$('#txtcantidad').val(Map.CANTIDAD);
					$('#txtdescripcion').val(Map.NOTAS);
					$('#txtproducto').focus();
					//_closeDelay();
				}
				,
				errorHandler:function(errorString, exception) { 
				jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
				return false;
			}
				
	});
}
/*funcion para mostrar el listado de productos*/
function muestraProductos(){	
	__listadoProductos($('#txtproducto').val(), $('#txtpartida').val());
}

/*funcion para mostrar el listado de conceptos*/
function mostrarTablaConceptos(cve_req){
	var cont =0;
	var total = 0;
	ID_PED = 0;
	$('#TOTAL_CONCEPTOS').val(0);
	quitRow("listasConceptos");
	$('#cmdenviarlotes').prop('disabled',true);
	$('#cmdreenumerar').prop('disabled',true);
	controladorRequisicion.getConceptosRequisicion(cve_req, {
						   callback:function(items) { 
						   		jQuery.each(items,function(i){
									 cont++;
									 total+= this.IMPORTE;
									 $('#TOTAL_CONCEPTOS').val((parseInt($('#TOTAL_CONCEPTOS').val())+1)) 
									 $('#IMPORTE_TOTAL').val(total);
									 pintaTablaConceptos('listasConceptos', this.ID_REQ_MOVTO, this.CVE_REQ, this.REQ_CONS, this.CANTIDAD, this.UNIDAD, this.NOTAS, this.IMPORTE, this.ARTICULO, this.STATUS, this.ID_PED_MOVTO);				   
									 if(items.length==cont) 
										 pintarTotalConceptos('listasConceptos', $('#IMPORTE_TOTAL').val(cont)); 
										
									 validaTipoDoc();
								});
						   }
	});
	
}

function validaTipoDoc(){
	switch($('#cbotipo').val())
	{
		case '1':
			$('#cmdenviarlotes').prop('disabled',false);
			$('#cmdreenumerar').prop('disabled',false);
			$('#cmdimportar').prop('disabled',false);
			break;
		case '7':
			$('#cmdenviarlotes').prop('disabled',false);
			$('#cmdreenumerar').prop('disabled',false);
			$('#cmdimportar').prop('disabled',false);
			break;
		default:
			$('#cmdenviarlotes').prop('disabled',true);
			$('#cmdreenumerar').prop('disabled',true);
			$('#cmdimportar').prop('disabled',true);
			break;
	}
}

/*funcion que permite mostrar el anexo de cada concepto*/
function mostrarAnexoConcepto(ID_REQ_MOVTO, consec){
	jWindow('<iframe width="550" height="180" id="ventadaAnexo" frameborder="0" src="../../sam/requisiciones/capturaAnexoConceptos.action?id_req_movto='+ID_REQ_MOVTO+'"></iframe>','Anexo del lote '+consec, '','',0);
}
/*funcion que permite pintar el total de los conceptos de la requisicion*/
function pintarTotalConceptos(tabla, importe_total, cont){
	var tabla = document.getElementById(tabla).tBodies[0];
 	var row =   document.createElement( "TR" );    
	row.height = 20;
	var htmlEdit = '<strong>'+formatNumber(importe_total, '$')+'</strong>';
	row.appendChild( Td('',izquierda,'','<strong >Total de lotes: '+cont+'</strong>',3));
	row.appendChild( Td('',centro,'',''));
	row.appendChild( Td('',izquierda,'',''));
	row.appendChild( Td('',izquierda,'',''));
	row.appendChild( Td('',derecha,"",htmlEdit));
	row.appendChild( Td('',centro,'',''));	
	tabla.appendChild(row);
}

/*funcion para agregar un elemento al listado*/
function pintaTablaConceptos(table, ID_REQ_MOVTO, CVE_REQ, CONSECUTIVO, CANTIDAD, UNIDAD_MED, DESCRIPCION, IMPORTE, ARTICULO, STATUS, ID_PED_MOVTO){
 	var tabla = document.getElementById(table).tBodies[0];
 	var row =   document.createElement( "TR" );    //onclick='mostrarAnexoConcepto("+ID_REQ_MOVTO+","+CONSECUTIVO+")' 
	var htmlCheck = "<input type='checkbox' name='chkconsecMovimiento' id='chkconsecMovimiento' value='"+ID_REQ_MOVTO+"'>";
	var htmlBoton = "<img src=\"../../imagenes/calendar_edit.png\" style='cursor: pointer;' alt=\"Modificar anexo "+CONSECUTIVO+"\" width=\"16\" height=\"16\" border=\"0\" onClick=\"mostrarAnexoConcepto("+ID_REQ_MOVTO+","+CONSECUTIVO+")\" >&nbsp;";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar lote "+CONSECUTIVO+"\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editarConcepto("+ID_REQ_MOVTO+")\" >"; 		
	var htmlEnPedido = "<a href='javascript:getInfoPedido("+ID_PED_MOVTO+")'>SÃ­</a>";
	var ban = ($('#cbotipo').val()!=1&&$('#cbotipo').val()!=7) ? "No Aplica": "No";
	
	if(ID_PED_MOVTO!=0) ID_PED =  ID_PED_MOVTO; 
	//if($('#cbotipo').attr('value')!=2) htmlBoton = "";
	row.appendChild( Td("",centro,"",htmlCheck));
	row.appendChild( Td(CONSECUTIVO,centro,"",""));
	row.appendChild( Td(CANTIDAD,centro,"",""));
	row.appendChild( Td(UNIDAD_MED,centro,"",""));
	row.appendChild( Td(ARTICULO+"("+DESCRIPCION+")",izquierda,"",""));
	row.appendChild( Td("",centro,"",(ID_PED_MOVTO!=0) ? htmlEnPedido: ban));
	row.appendChild( Td(formatNumber(IMPORTE, '$'),derecha,"",""));
	row.appendChild( Td("",centro,"",htmlBoton+htmlEdit));	
	tabla.appendChild(row);
	
 }

function getInfoPedido(ID_PED_MOVTO){
	//jInformation("Este lote se encuentra en el pedido: "+ID_PED_MOVTO,"InformaciÃ³n");	
}


/*Metodo para cerrar la requisicon*/
function cerrarRequisicion(){
	if($('#TOTAL_CONCEPTOS').val()=='0') {jAlert('No se puede cerrar la requisiciÃ³n si no existe por lo menos un lote en el detalle','Advertencia'); return false;}
	if(($('#cbotipo')==7||$('#cbotipo')==8)&&($('#CVE_VALE')!='0'||$('#CVE_VALE')!='')) {swal('No es posible cerrar una Requisicion/Orden de Servicio Calendarizada cuando se compromete a travez de un vale','Advertencia'); return false;}
	/*jConfirm('¿Confirma que desea cerrar la requisición?','Confirmar', function(r){
		if(r){
				//verificar el periodo en que se esta cerrando
				if ($("#cbotipo").val()==7||$("#cbotipo").val()==8)
				  getPresupuesto();
				 else
				  cerrarRequiFinal();
			}
		});	*/
	if($('#cbotipo').val()=='2'||$('#cbotipo').val()=='3'||$('#cbotipo').val()=='4'||$('#cbotipo').val()=='8'){
		var clave_bene=$('#cboSearch').selectpicker('val');
		
		if (clave_bene=='') {
		    	swal({
		        	  title: 'Error!',
		        	  text: 'El Beneficiario seleccionado no es válido',
		        	  type: 'error',
		        	  confirmButtonText: 'Cool'
		        	})
		    
		    return false;}
	}
	swal({
		  title: '¿Estas seguro?',
		  text: "Tu cambio no se pordrá revertir!",
		  type: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Sí, cerrar'
		}).then(function (r) {
			if(r){
				/*verificar el periodo en que se esta cerrando*/
				if ($("#cbotipo").val()==7||$("#cbotipo").val()==8)
				  getPresupuesto();
				 else
				  cerrarRequiFinal();
			}
		  swal(
		    'Cerrar!',
		    'Tu requisiicion se cerro con exito.',
		    'success'
		  )
		})
	}
	
function cerrarRequiFinal(){
	ShowDelay('Cerrando Requisicion','');
	controladorRequisicion.cerrarRequisicion($('#CVE_REQ').val(),checkPresupuesto, {
					callback:function(items){
						if(!items) 
							{ 
								jError('<strong>Imposible cerrar la requisiciÃ³n, esto puede ser debido a causa de las siguientes razones:</strong><br>- La RequisiciÃ³n supera el disponible actual. <br>- El periodo de la RequisiciÃ³n no es vÃ¡lido. <br>- Programa o partida no vÃ¡lidos','Error');
							}
						else{
							$('#cmdcerrar').prop('disabled', true);
							CloseDelay('Requisicion cerrada con exito', function(){
									//Vincular a mostrar la Requisicion en Solo lectura
									//document.location = 'consultaRequisicion.action?cve_req='+$('#CVE_REQ').attr('value')+'&accion=false';
									
									//Nueva ventada para mostrar el documento PDF
									showPDFRequisicion($('#CVE_REQ').val());
								});
								
						}
						//_closeDelay();
					},
						errorHandler:function(errorString, exception) { 
							jError(errorString,'Error');   
							return false;
					}
				});
}

/*Metodo para mostrar el documento PDf de la requisicion*/
function showPDFRequisicion(cve_req){
	$('#claveRequisicion').val(cve_req);
	$('#forma').prop('target',"impresion");
	$('#forma').submit();
	$('#forma').prop('target',"");
}

/*Metodo para obtener el mes de la requsicion*/
function getMesRequisicion(mes){
	
	if(mes==0){
		controladorRequisicion.getMesActivo({
			callback:function(items){
	
				$('#cbomeses').val(items);
			}
			,
			errorHandler:function(errorString, exception) { 
				jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
				return false;
			}
		});
	}
}

/*funcion para limpiar los datos*/
function nuevaRequisicion(){
	/*Reestablece campos ocultos*/
	$('#MES').val(0);
	$('#CVE_REQ').val(0);
	
	/*Limpiar los cuadros de texto*/
	$('#txtrequisicion').val('');
	$('#txtfecha').val('');
	$('#txtnotas').val('');
	$('#txttipobien').val('');
	$('#txtmarca').val('');
	$('#txtmodelo').val('');
	$('#txtusuario').val('');
	$('#txtnuminventario').val('');
	$('#txtplacas').val('');
	$('#txtcolor').val('');
	$('#txtprestadorservicio').val('');
	$('#txtconcurso').val('');
	$('#txtproyecto').val('');
	$('#txtpartida').val('');
	$('#txtpresupuesto').val('');
	$('#txtdisponible').val('');
	
	/*Limpiar los combos*/
	$('#cbodependencia').val(0);
	$('#cbotipo').val(0);
	$('#cboarea').val(0);
	$('#cbomeses').val(0);
	$('#ID_PROYECTO').val(0);
	/*Funciones especiales*/
	tipoRequisicion();
	nuevoConcepto();
}

/*funcion para limpiar los conceptos*/
function nuevoConcepto(){
	$('#ID_REQ_MOVTO').val(0);
	$('#ID_ARTICULO').val(0);
	$('#GRUPO').val(0);
	$('#SUBGRUPO').val(0);
	$('#CLAVE').val(0);
	$('#CVE_UNIDAD_MEDIDA').val(0);
	$('#REQ_CONS').val(0);
	//$('#ID_PROYECTO').attr('value',0);
	$('#txtproducto').val('');
	$('#txtprecioestimado').val('');
	$('#txtunidadmedida').val('');
	$('#txtcantidad').val('');
	$('#txtdescripcion').val('');
	
}

function getPresupuesto(){
        controladorRequisicion.getPresupuestoReq($('#CVE_REQ').val(), {
        callback:function(items) {
		var html = getTabla(items);
		jWindow(html,'Disponibilidad Presupuestal del Calendario', '','',0);
			$('#cmdguardarPresupuesto').prop('disabled', 'disabled');
		} 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 
}

function getTabla(items){
	var meses=["","ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"];
	var mesActivo=parseInt($('#cbomeses').val());
	var importe=parseFloat(items.importe);
	var importeDes=0;
	var tabla ="<table align='center'><tr><td><h1>Presupuesto disponible"+(($('#CVE_CONTRATO').val()!='0'&&$('#CVE_CONTRATO').val()!='') ? " en contrato":"")+"</h1></td></tr></table>";
		tabla +="<input type='hidden' name='importeReq' id='importeReq' value='"+importe+"' >";
		tabla +="<table border='0' align='center' cellpadding='0' cellspacing='0' class='formulario'>";
		tabla +="<tr><td colspan='3' height='25'>Total a Precomprometer: <strong>$ "+formatNumber(items.importe)+"</strong><input type='hidden' name='pre' id='t_importe' value='"+items.importe+"'></td></tr>";	
  	    tabla +="<tr><td  class='TituloFormulario' width='100'>Mes</td><td  class='TituloFormulario' width='100' >Disponible</td><td  class='TituloFormulario' width='100'>Importe</td></tr>";		
        
		for (i=1; i<=12; i++ ){
			//por si aplica el contrato
			if($('#CVE_CONTRATO').val()!='0'&&$('#CVE_CONTRATO').val()!=''){
				if (i>=mesActivo){
					var dispo=dispo=(eval("items."+meses[i].substring(0,3)+"PREINI")+eval("items."+meses[i].substring(0,3)+"PREAMP")-eval("items."+meses[i].substring(0,3)+"PRERED")-eval("items."+meses[i].substring(0,3)+"PRECOM")-eval("items."+meses[i].substring(0,3)+"PREREQ")-eval("items."+meses[i].substring(0,3)+"PREEJE")).toFixed(2);
					//Si hay disponibilidad
					if (dispo>0){   
						var impImp=0;
						if (importe<=dispo)
						   impImp=importe;
						else
						   impImp=dispo;
						   importe =(importe-impImp).toFixed(2);
						   tabla +="<tr ><td><input type='hidden' name='pre' id='"+i+"' value='"+dispo+"'> <input type='hidden' name='mes' id='mes' value='"+i+"' >"+meses[i]+"</td><td>"+formatNumber(dispo)+"</td><td><input type='text' name='"+i+"_importe' id='p_importe' value='"+impImp+"'  onblur='validarPre(this,"+dispo+")' onkeypress='return keyNumbero(event);validarPre(this,"+dispo+");'></td></tr>";
					}
				
				}
			}
			else
			//por si no aplica lo del contrato
			if (i>=mesActivo){
				var dispo=(eval("items."+meses[i].substring(0,3)+"PREINI")+eval("items."+meses[i].substring(0,3)+"PREAMP")-eval("items."+meses[i].substring(0,3)+"PRERED")-eval("items."+meses[i].substring(0,3)+"PRECOM")-eval("items."+meses[i].substring(0,3)+"PREREQ")-eval("items."+meses[i].substring(0,3)+"PREEJE")).toFixed(2);
				//Si hay disponibilidad
				if (dispo>0){		   
					var impImp=0;
					if (importe<=dispo)
					   impImp=importe;
					else
					   impImp=dispo;
					   importe =(importe-impImp).toFixed(2);
					   tabla +="<tr ><td><input type='hidden' name='pre' id='"+i+"' value='"+dispo+"'> <input type='hidden' name='mes' id='mes' value='"+i+"' >"+meses[i]+"</td><td>"+formatNumber(dispo)+"</td><td><input type='text' name='"+i+"_importe' id='p_importe' value='"+impImp+"'  onblur='validarPre(this,"+dispo+")' onkeypress='return keyNumbero(event);validarPre(this,"+dispo+");'></td></tr>";
				}
				
			}
		}
			
	tabla +="<tr><td align='left'>&nbsp;</td><td align='left'>&nbsp;</td><td align='left'><div id='div_total'><strong>$0.00&nbsp;</strong></div></td></tr>"
	tabla +="<tr><td align='center' height='35' colspan='6' ><input name='cmdguardarPresupuesto' id='cmdguardarPresupuesto' type='button' class='botones' onClick='getPresupuestoSelec();' value='Cerrar RequisiciÃ³n' />&nbsp;<input name='btnGuardarPedido' id='cmddistribuir' type='button' class='botones' onClick=\"setDistribuir('"+items.importe+"');\" value='Distribuir' />&nbsp;<input name='btnGuardarPedido' id='cmdcancelar' type='button' class='botones' onClick='_closeDelay();' value='Cancelar' /></td></tr>";
	return tabla +="</table> ";	
}

function setDistribuir(importe){
	var t = 0;
	var error = 0;
	var total = 0.00;
	//total de meses a repartir
	$('INPUT[id=p_importe]').each(function() {
				t++;	
	});
	//dividir el monto a cada mes
	total = eval(importe/t);
	//asignacion de los valores divididos
	$('INPUT[name=pre]').each(function() {
			if(eval(total)<=eval($(this).val())) {$('INPUT[name='+$(this).val('id')+'_importe]').val(total);} else {alert('El disponible del mes es insuficiente al realizar esta operacion'); return false;}
	});
	getTotalPre();
}


function validarPre(obj,importe){	
   var  num = parseFloat(obj.value);
   	if (num>importe||num<0 || isNaN(obj.value) || obj.value=="" ) 
	{ alert("El importe es mayor que el disponible actual del mes en el calendario Ã³ el nÃºmero no es vÃ¡lido");
	  obj.focus();
	}else
	{	
		getTotalPre();
	}
}

function getTotalPre(){
	var total = 0.00;
	var temp = 0.00;
		$('INPUT[id=p_importe]').each(function() {
				total = eval(total) + eval($(this).val());
				temp = redondeo(total);
				if(temp==$('#t_importe').val()){
					$('#cmdguardarPresupuesto').prop('disabled', false);
				}
				else{
					$('#cmdguardarPresupuesto').prop('disabled', true);
				}
				//temp = 0.00;
		});
		$('#div_total').html('<strong>$ '+formatNumber(total)+'</strong>');

		if(redondeo(total)>eval($('#t_importe').val())||total<0){
			alert('El importe que ha establecido no es vÃ¡lido, vuelva a verificarlo');
			$('#cmdguardarPresupuesto').attr('disabled', false); 
		} 

}

 function getPresupuestoSelec(){
	  checkPresupuesto = new Array();
	  var suma=0;
	  var vimporte=0;
     $('input[name=mes]').each(function() {  
		vimporte=parseFloat($("INPUT[name="+$(this).val()+"_importe]").val());							
  	    suma=suma+vimporte;
		if (vimporte > 0){			
		   var map = {mes: $(this).val(), importe: vimporte};
		   checkPresupuesto.push(map);	
		}
	});	 
	 if ( redondeo(suma) == redondeo(parseFloat($("#importeReq").val()))){		
	   		cerrarRequiFinal();
	 }
	   else{
	   checkPresupuesto = new Array();
	   	alert("Error, Verifique el presupuesto con el importe del documento");
	   }
 }

/*Metodo para mostrar enviar lotes a otro pedido*/
function mostrarEnviarLotesPedido(){
	var checks = [];
	var tipo = $('#cbotipo').val();
	$('input[id:chkconsecMovimiento]:checked').each(function(){
			checks.push($(this).val()); 
		});
	if(checks.length<=0) {jAlert('Es necesario seleccionar por lo menos un lote de listado','Advertencia'); return false;}
	if(tipo!=1&&tipo!=7) {jAlert('No se puede realizar esta operaciÃ³n en Ordenes de Trabajo/Servicio','Advertencia'); return false;}
	var html = '<table width="350" border="0" cellspacing="0" cellpadding="0" align="center">' +
			   '<tr>' +
			   '<td height="27" align="center"><span class="TextoNegritaTahomaGris">Escriba el n&uacute;mero de pedido:</span>&nbsp; <input type="text" value="" id="txtpedido" onkeypress="return keyNumbero(event);" class="input" maxlength="6"></td>' +
			   '</tr>' +
			   '<tr>' +
			   '<td height="44" align="center"><input type="button" value="Exportar" id="cmdEnviarLotes" onClick="enviarLotesPedido();" class="botones"/>&nbsp;' +
			   '<input type="button" value="Cancelar" id="cmdborrarConceptos" class="botones" onClick="$.alerts._hide();"/></td>' +
			   '</tr>'+
			   '</table>';	
		jWindow(html,'Exportar lotes a Pedido existente', '','',0);
		  
}

function enviarLotesPedido(){
	var cve_ped = $('#txtpedido').val();
	var cve_req = $('#CVE_REQ').val();
	if($('#txtpedido').attr('value')==''){jAlert('El nÃºmero de Pedido escrito no es vÃ¡lido','Advertencia'); return false;}
	var checks = [];
	$('input[id:chkconsecMovimiento]:checked').each(function(){
			checks.push($(this).val()); 
		});
	ShowDelay('Enviando lotes al Pedido', '');
	controladorRequisicion.enviarLotesPedido(checks, cve_ped, cve_req,{
			 callback:function(items){
				if(items=="")
					CloseDelay('Lotes exportados con exito', function(){
							mostrarTablaConceptos($('#CVE_REQ').val());
					});
				else
					jError(items, 'Error');
			}, 
			errorHandler:function(errorString, exception) { 
			jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>', 'Error');   
			return false;
		}
		});
}

function CargarLotesNuevos(lotes, cve_req){
	_closeDelay();
	cve_req = $('#CVE_REQ').attr('value');
	ShowDelay('Agregando nuevos lotes','');
	controladorRequisicion.importarNuevosLotes(lotes, cve_req, {
				callback:function(items) {
					if(items) {
						mostrarTablaConceptos(cve_req);
						CloseDelay('Lotes importados con Ã©xito');	
					}
					else
						jError('La operacion ha fallado al importar lotes','Error');
			} 					   				
			,
			errorHandler:function(errorString, exception) { 
				jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
			}
	});
}

function ReenumerarLotesNuevos(idLotes, valLotes, cve_req){
	_closeDelay();
	ShowDelay('Reenumerando lotes','');
	controladorRequisicion.reenumerarLotes(idLotes, valLotes, cve_req, {
				callback:function(items) {
					cve = cve_req;
					if(items) {
						mostrarTablaConceptos(cve_req);
						CloseDelay('Lotes reenumerados con Ã©xito');	
					}
					else
						jError('La operacion ha fallado al reenumerar lotes','Error');
			} 					   				
			,
			errorHandler:function(errorString, exception) { 
				jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
			}
	});
}

function mostrarLotePedido(id_ped_movto){
	_closeDelay();
	ShowDelay('Cargando informacion del Pedido','');
	controladorRequisicion.getInformacionPedido(id_ped_movto, {
				callback:function(items){
					if(items) {
						var html = '<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="95%">'+
									'  <tr bgcolor="#889FC9">'+
									'	<th height="25" colspan="2" align="center">NÃºmero de Pedido: <strong>'+items.NUM_PED+'</strong></th>'+
									'  </tr>'+
									'	   <tr>'+
									'		<th width="20%" height="25" align="left">Fecha</th>'+
									'		<td width="80%" height="20" align="left">'+items.FECHA_PED+'</td>'+
									'  </tr>'+
									'	   <tr>'+
									'		 <th height="25" align="left">Unidad Administrativa</th>'+
									'		 <td height="20" align="left">'+items.CLV_UNIADM+' '+items.DEPENEDENCIA+'</td>'+
									'	   </tr>'+
									'	   <tr>'+
									'		 <th height="25" align="left">Tipo de Gasto</th>'+
									'		 <td height="20" align="left">'+items.TIPO_GASTO+'</td>'+
									'	   </tr>'+
									'	   <tr>'+
									'		 <th height="25" align="left">Beneficiario</th>'+
									'		 <td height="20" align="left">'+item.NCOMERCIA+'</td>'+
									'	   </tr>'+
									'	   <tr>'+
									'		 <th height="25" align="left">Importe</th>'+
									'		 <td height="20" align="left">'+formatNumber(items.TOTAL, '$')+'</td>'+
									'	   </tr>'+
									'	   <tr>'+
									'		 <th height="25" align="left" valign="middle">Notas</th>'+
									'		 <td height="20" align="left" valign="top">'+items.NOTAS+'</td>'+
									'	   </tr>'+
									'</table>'+
									'<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="95%">'+
									'  <tr bgcolor="#889FC9">'+
									'	<th height="25" colspan="7" align="center">Movimientos</th>'+
									'  </tr>'+
									'  <tr>'+
									'	<th width="6%" height="25" align="left">Lote</th>'+
									'	<th width="10%" height="20" align="center">Cantidad'+
									'	<th width="9%" align="center">Unidad</th>'+
									'	<th width="43%" align="center">DescripciÃ³n</th>'+
									'	<th width="11%" align="center">Precio Ã�rea</th>'+
									'	<th width="11%" align="center">Precio Unitario</th>'+
									'	<th width="10%" align="center">Costo</th>'+
									'  </tr>'+
									'  <tr>'+
									'	<td height="25" align="center">'+items.PED_CONS+'</td>'+
									'	<td height="20" align="center">'+items.CANTIDAD+'</td>'+
									'	<td height="20" align="center">'+items.UNIDMEDIDA+'</td>'+
									'	<td height="20" align="center">'+items.ARTICULO+' '+items.DESCRIP+'</td>'+
									'	<td height="20" align="center">'+items.PRECIO_EST+'</td>'+
									'	<td height="20" align="center">'+items.PRECIO_UNIT+'</td>'+
									'	<td height="20" align="center">'+formatNumber((items.PRECIO*items.CANTIDAD),'$')+'</td>'+
									'  </tr>'+
									'</table>';
									_closeDelay();
									jWindow(html,'Informacion General del Pedido '+items.NUM_PED, '','',0);
						
					}
					else
						jError('La operacion ha fallado al obtener la informaciÃ³n de Pedido','Error');
			} 					   				
			,
			errorHandler:function(errorString, exception) { 
				jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
			}
	});
}
