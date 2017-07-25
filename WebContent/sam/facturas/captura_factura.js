/*
Autor: ISC. Israel de la Cruz Hernandez
Version: 1.0
Date: 11-01-2013
Update: 
*/

var entraVale =0;

$(document).ready(function(){
	var options = { 
        beforeSubmit:  showRequest,  
        success:       showResponse, 
        url:       '_subirArchivo.action?CVE_FACTURA='+$('#CVE_FACTURA').attr('value'),
        type:      'post', 
        dataType:  'json'
    }; 
	
	$('#frmEntrada').submit(function(){
		$(this).ajaxSubmit(options);
		return false;
	});
	
	 $('#cmdNuevaRetencion').click(function (event){limpiarRetenciones();});
	 $('#cmdGuardarRetencion').click(function (event){guardarRetencion();}); 
	 //$('#cbotipoFactura').change(function(event){tipoFacturasDeductivas();});
	 $('#cmdnuevo').click(function(event){document.location='captura_factura.action';});
	 $('#cmdcerrar').attr('disabled', true);
	 $('#cmdcerrar').click(function(event){cerrarDocumento();});
	 $('#img_movimiento').click(function(event){muestraTiposDocumento();});
	 $('#img_detele').click(function(event){deleteDocumento();});
	 $('#cmdguardar').click(function (event){guardarFactura();});
	 $('#cmdguardar2').click(function (event){guardarFactura();});
	 $('#cmdagregar').click(function (event){guardarDetalle();});
	 $('#cboproyectopartida').blur(function (event) {obtenerProyectoPartida();});
	 $('#cmdnuevoconcepto').click(function (event){limpiarDetalles();});
	 $('#txtproyecto').blur(function(event){__getPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible','');});
	 $('#txtpartida').blur(function(event){__getPresupuesto($('#ID_PROYECTO').attr('value'), $('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible','');});
	 $('#img_presupuesto').click(function(event){muestraPresupuesto();});
	 getBeneficiarios('txtprestadorservicio','CLV_BENEFI',$('#tipoBeneficiario').attr('value'));
	 
	 $("#txtfecha").datepicker({showOn: 'button', buttonImage:"../../imagenes/cal.gif" , buttonImageOnly: true,dateFormat: "dd/mm/yy"});
	 /*Configura los tabuladores*/
	 $('#tabuladores').tabs();
	 $('#tabuladores').tabs('option', 'selected', 0);;
	 $('#tabuladores').tabs('option', 'disabled', [1,2,3]);
	 $('#tr_file').hide();
	 
	 if($('#CVE_FACTURA').val()!=null||$('#CVE_FACTURA').val()!=0){
		 
		 $('#tr_file').show();

		 
		 $('#cmdcerrar').attr('disabled', false);
		 if($('#cbotipodocumento').val()=='1')
		 {
		 	cargarBeneficiarioyPresupuestoOSOT($('#CLV_BENEFI').val(), $('#CVE_DOC').val());
			cargarDetallePresupuestalDoc($('#CVE_FACTURA').val());
		 }
		 if($('#cbotipodocumento').val()=='2')
		 {
			cargarBeneficiarioyPresupuestoPedidos($('#CLV_BENEFI').val(), $('#CVE_DOC').val());
			cargarDetallePresupuestalDoc($('#CVE_FACTURA').val());
		 }
		 /*
		 if($('#cbotipodocumento').val()=='3')
			cargarBeneficiarioyPresupuestoVale();*/
		 if($('#cbotipodocumento').val()=='4')
		 {
			PresupuestoBeneficiarioContrato($('#CLV_BENEFI').val(), $('#CVE_DOC').val());
			cargarDetallePresupuestalDoc($('#CVE_FACTURA').val());
		 }

		mostrarDetallesArchivos();
		llenarTablaDeRetenciones();
		tipoFacturasDeductivas();
		llenarTablaDeVales();
		mostrarDetalles();
		
	}
	$('#ui-datepicker-div').hide();
	getMesRequisicion($('#cbomes').val());
	
	$('#cbomes').attr('disabled', 'disabled');
	$('#tr_Programa').hide();
	$('#tr_PresupuestoLibre').hide();
});

function obtenerProyectoPartida()
{
	var arreglo = $('#cboproyectopartida').val().split(',');
	$('#ID_PROYECTO').attr('value', arreglo[0]);
	$('#txtproyecto').attr('value',arreglo[1]);
	$('#txtpartida').attr('value', arreglo[2]);
	
	__getPresupuesto($('#ID_PROYECTO').attr('value'),arreglo[1],arreglo[2], $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible','');
	
}

/*Metodo para obtener el mes de la requsicion*/
function getMesRequisicion(mes){
	if(mes==0){
		controladorFacturasRemoto.getMesActivo({
			callback:function(items){
				$('#cbomes').val(items);
			}
			,
			errorHandler:function(errorString, exception) { 
				jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
				return false;
			}
		});
	}
}

/*funcion para mostrar el listado del presupuesto*/
function muestraPresupuesto(){
	if($('#txtproyecto').attr('value')==''||$('#txtpartida').attr('value')=='')
		$('#ID_PROYECTO').attr('value', '0');
		
		
	var idUnidad = $('#cbUnidad2').val();
	if(idUnidad==null||idUnidad=="") idUnidad =0;
	
	if($('#cbomes').val()==0) {jAlert('Seleccione un periodo presupuestal válido','Advertencia'); return false;}

	__listadoPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'), 0, idUnidad);
}

 function eliminarVales(){
	  var checkVales = [];
     $('input[name=chkVale]:checked').each(function() { checkVales.push($(this).val());	 });	 
	 if (checkVales.length > 0 ) {
   	 var idOrden=$('#id_orden').attr('value');
	 jConfirm('¿Confirma que desea aliminar la comprobación de Vale?', 'Eliminar Vale', function(r){
		 		ShowDelay('Eliminando vale(s)','');
				controladorFacturasRemoto.eliminarVales(checkVales, {
				callback:function(items) {	
					llenarTablaDeVales();
				   CloseDelay("Registros eliminados con éxito");
				   
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError(errorString, 'Error');
				}
			},async=false ); 
		 });
     
	 } else 
	    jAlert('Es necesario que seleccione un elemento de la lista', 'Advertencia');
 }
 
 function guardarDetalle()
 {
	 if($('#ID_PROYECTO').attr('value')==''||$('#ID_PROYECTO').attr('value')=='0') {jAlert('El programa escrito no es valido'); return false;}
	if($('#txtpartida').attr('value')=='') {jAlert('La partida escrita no es valida'); return false;}
	 if($('#txtimporteDet').val()=='') {jAlert('Es necesario especificar un importe', 'Advertencia'); return false;}
	 ShowDelay('Agregando movimiento','');
				controladorFacturasRemoto.agregarMovimiento($('#CVE_FACTURA').val(), $('#ID_PROYECTO').val(), $('#txtpartida').val(), $('#txtimporteDet').val(), $('#txtdetalle').val(), {
				callback:function(items) {	
					llenarTablaDeVales();
					limpiarDetalles();
					mostrarDetalles();
				   	CloseDelay("Movimiento agregado con éxito");
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError(errorString, 'Error');
				}
			},async=false ); 
 }
 
 function mostrarDetalles()
 {
	  controladorFacturasRemoto.getDetallesFactura($('#CVE_FACTURA').val(), {
        callback:function(items) { 
			quitRow("listaDetalles");		
			var total = 0;
			dwr.util.removeAllOptions("cboproyectocuenta");
			dwr.util.addOptions('cboproyectocuenta',{ 0:'Seleccione' });
			lipiarVale();
			jQuery.each(items,function(i) {
				var htmlCheck = "<input type='checkbox' name='chkdetalle' id='chkdetalle' value='"+this.ID_PROYECTO+"-"+this.CLV_PARTID+"'>";
				total += this.IMPORTE;
				appendNewRow('listaDetalles', 
						[Td('', centro , '', htmlCheck),
						 Td('', izquierda , '', this.DEPENDENCIA),
						 Td('', izquierda  , '', getHTML(this.NOTAS)),
						 Td('', centro  , '', this.N_PROGRAMA),
						 Td('', centro  , '', this.CLV_PARTID),
						 Td('', derecha , '', formatNumber(this.IMPORTE, '$')+"&nbsp;"),
				 		 Td('', derecha , '', '')
						 
				]);
				dwr.util.addOptions('cboproyectocuenta',[{ID_PROYECTO:this.ID_PROYECTO, PROYECTOPARTIDA:this.N_PROGRAMA+" - "+ this.CLV_PARTID}],"ID_PROYECTO", "PROYECTOPARTIDA" );		
			}); 
			
			$('#div_total').html(formatNumber(total, '$')+"&nbsp;");
        },
        errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');
        }
    });
 }
 
 function eliminarDetalles()
 {
	 var checkDetalles = [];
     $('input[name=chkdetalle]:checked').each(function() {checkDetalles.push($(this).val()); });	 
	 if (checkDetalles.length > 0 ) {
		 jConfirm('¿Confirma que desea eliminar los movimientos?','Eliminar movimiento', function(r){
			 if(r){
				 controladorFacturasRemoto.eliminarDetalles($('#CVE_FACTURA').val(), checkDetalles, {
        			callback:function(items) { 
						mostrarDetalles();
					},
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');
					}
				});
			 }
		 });
	 }
 }
 
 function limpiarDetalles(){
	$('#txtproyecto').attr('value','');
	$('#txtpartida').attr('value','');
	$('#ID_PROYECTO').attr('value', '0');
	$('#ID_DETALLE').attr('value','0');
	$('#txtdetalle').attr('value', '');
	$('#txtimporteDet').attr('value', '');
	$('#txtpresupuesto').attr('value','');
	$('#txtdisponible').attr('value','');
}

function llenarTablaDeVales() {
	 quitRow("listasVales");
	 
	 controladorFacturasRemoto.getListaVales($('#CVE_FACTURA').val(), {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaVales( "listasVales", i+1,this.ID_PROYECTO,this.N_PROGRAMA,this.CLV_PARTID,this.CVE_VALE,this.NUM_VALE,this.IMPORTE, this.ID_MOVIMIENTO);
        });
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    },async=false ); 
 }
 
  function pintaVales( table, consecutivo,idproyecto, proyecto,partida,vale,numeroVale,importe, idMovVale){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );  
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='editarVale("+idMovVale+","+vale+","+importe+",\""+proyecto+"-"+partida+"\")' >"; 	
    var htmlCheck = "<input type='checkbox' name='chkVale' id='chkVale' value='"+idMovVale+"' >";
	row.appendChild( Td("",centro,"",htmlCheck));
	row.appendChild( Td(proyecto,centro));
	row.appendChild( Td(partida,centro));
	row.appendChild( Td(numeroVale,centro));
	row.appendChild( Td(formatNumber(importe,"$"),derecha) );
	row.appendChild( Td("",centro,"",htmlEdit) );
	tabla.appendChild(row);
 }
 
 function editarVale(idVale,cve_vale,importe,proyectoCuenta){
	$('#idVale').attr('value',idVale);
	$('#CVE_VALE').attr('value',cve_vale);
	//$('#cboproyectocuenta').val(proyectoCuenta);
	$("#cboproyectocuenta option").filter(function() {
		//may want to use $.trim in here
		return (jQuery.trim($(this).text().replace(' ','')).replace(' ', '') )== jQuery.trim(proyectoCuenta); 
	}).attr('selected', true);
	$('#txtimporteVale').attr('value',importe);	
	cargarVales(cve_vale);
	
 } 
 
 function cargarVales(cve_vale) {
     dwr.util.removeAllOptions("cboVales");
     if ($('#cboproyectocuenta').val()!=0) {
		 var datos= $('#cboproyectocuenta :selected').text().split('-');	 
		 var proyecto = $('#cboproyectocuenta').val();//datos[0];
		 var partida = jQuery.trim(datos[1]);
		 var idDependencia = $('#cbodependencia').val();
		 controladorFacturasRemoto.getValesDisponibles(idDependencia, proyecto, jQuery.trim(partida), {
			callback:function(items) { 
				dwr.util.addOptions('cboVales',{ 0:'[Seleccione]'});
				dwr.util.addOptions('cboVales',items,"CVE_VALE", "DATOVALE");
				$('#cboVales').val(cve_vale);
			} 					   				
			,
			errorHandler:function(errorString, exception) { 
			jError(errorString, "Error");          
			}
		},async=false ); 
	} 
 }

function guardarVale(){
	var error="";  
	if($('#CVE_FACTURA').val()=='' || $('#CVE_FACTURA').val()==0){jAlert('No se puede guardar el Vale hasta que guarde la factura'); return false;}
    if ($('#cboproyectocuenta').attr('value')==0) {jAlert('El Proyecto/Cuenta  de Vale no es válido'); return false;}
    if ($('#cboVales').attr('value')==""||$('#cboVales').attr('value')==0) {jAlert('El Vale no es válido'); return false;}
    if ($('#txtimporteVale').attr('value')=="" || parseFloat($('#txtimporteVale').attr('value')==0)) {jAlert('El importe escrito para la comprobación de Vale no es válido'); return false;}
	
	datos= $('#cboproyectocuenta :selected').text().split('-');	 
	var proyecto = jQuery.trim($('#cboproyectocuenta').attr('value'));
	var clv_partid = jQuery.trim(datos[1]);	 	 
	datosVale = $('#cboVales :selected').text().split('>');

	var cve_vale = parseInt(jQuery.trim(datosVale[0]));
	var cve_factura = jQuery.trim($('#CVE_FACTURA').val());
	var importe = $('#txtimporteVale').attr('value');
	var idMovVale = $('#idVale').attr('value');
	  
	controladorFacturasRemoto.guardarComprobacionVale(idMovVale, cve_factura, cve_vale, proyecto, clv_partid, importe, {
			  callback:function(items){
				  lipiarVale();
				  CloseDelay("Comprobacion de Vale agregada con exito");  
				  llenarTablaDeVales();
							 
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString,'Error');   
			}
	});	
}

 function lipiarVale(){
	$('#idVale').attr('value',0);
	$('#CVE_VALE').attr('value',0);
	$('#txtimporteVale').attr('value',"");	
	$('#cboproyectocuenta').val(0);
	dwr.util.removeAllOptions("cboVales");	 
 }
 
function getProyectosPartidasVales(datos) {		
        lipiarVale();
		dwr.util.addOptions('cboproyectocuenta',{ 0:'Seleccione'});
		dwr.util.addOptions('cboproyectocuenta',datos,"ID_PROYECTO", "PROYECTOPARTIDA");
 }
 
 function getProyectosPartidasDetalles(datos){
	 	//dwr.util.removeAllOptions("cboproyectopartida");	
		//dwr.util.addOptions('cboproyectopartida',{ 0:'Seleccione'});
		dwr.util.addOptions('cboproyectopartida',datos,"ID_PROYECTO", "PROYECTOPARTIDA");
 }

function getcontratoDocumento(num_contrato, cve_contrato, idRecurso, clv_benefi, proyecto, clv_partid, importe)
{
	$('#trEntrada').hide();
	$('#CVE_DOC').attr('value', cve_contrato);
	$('#txtdocumento').attr('value', num_contrato);

	$('#txttotal').attr('value', formatNumber(importe).replace(',', ''));
	$('#div_total_entrada').html(formatNumber(importe, '$'));
	$('#CLV_BENEFI').attr('value', clv_benefi);
	//$('#ID_PROYECTO').attr('value', proyecto);
	//$('#CLV_PARTID').attr('value', clv_partid);
	PresupuestoBeneficiarioContrato(clv_benefi,cve_contrato);
	
}

function PresupuestoBeneficiarioContrato(clv_benefi,cve_contrato)
{
	ShowDelay("Recuperando información presupuestal");
	$('#trEntrada').hide();
	//buscamos el beneficiario
	if(clv_benefi!=''){
		controladorFacturasRemoto.getBeneficiarioFactura('CON', cve_contrato, {
			  callback:function(items){
							$('#div_beneficiario').html(items);
							 
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString,'Error');   
			}
		});	
	}
	
	/*Cargamos el presupuesto
	controladorFacturasRemoto.getPresupuestoDocumento('CON', cve_contrato, {
			  callback:function(items){
				$('#div_programa').html(items.N_PROGRAMA + ' - '+ items.PROGRAMA);
				$('#div_partida').html(items.CLV_PARTID + ' - '+items.PARTIDA);
				quitRow('listaPresupuesto');
				//getProyectosPartidasVales([{ID_PROYECTO:items.ID_PROYECTO, PROYECTOPARTIDA:items.N_PROGRAMA+" - "+ items.CLV_PARTID}]);
				appendNewRow('listaPresupuesto', 
						[Td('', centro , '', items.MES),
						 Td('', derecha , '', '<div style ="height:20px">'+formatNumber(items.AUTORIZADO, '$')+'</div>'),
						 Td('', derecha  , '', ((items.PRECOMPROMETIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'PRECOMPROMETIDO\''+')">': '')+formatNumber(items.PRECOMPROMETIDO, '$')+'</a>'),
						 Td('', derecha  , '', ((items.COMPROMETIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'COMPROMETIDO\''+')">': '')+formatNumber(items.COMPROMETIDO, '$')+'</a>'),
						 Td('', derecha  , '', ((items.DEVENGADO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'DEVENGADO\''+')">': '')+formatNumber(items.DEVENGADO, '$')+'</a>'),
						 Td('', derecha , '', ((items.EJERCIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'EJERCIDO\''+')">': '')+formatNumber(items.EJERCIDO, '$')+'</a>'),
				 		 Td('', derecha , '', formatNumber(items.DISPONIBLE, '$')+"&nbsp;")
						 
				]);
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString,'Error');   
			}
		});	
		*/
		//mostrarDetallesArchivos();	
		_closeDelay();
}

function tipoFacturasDeductivas()
{
	var tipo = $('#cbotipoFactura').val();
	$('#tr_NumDocumento').show();
	$('#trEntrada').show();
	$('#tr_TotalDocumento').show();
	//$('#tr_Programa').show();
	//$('#tr_partida').show();
	$('#div_benaficiario').hide();
	$('#tr_PresupuestoLibre').show();
	$('#tr_ProyectoPartida').show();
	$('#tr_ProgramaPartidaPresupuesto').hide();
	$('#div_benaficiarioFijo').hide();
	$('#div_beneficiario').show();
	
	switch(tipo){
		case "1"://NOMINA
			$('#tabuladores').tabs('enable',1);
			$('#tabuladores').tabs('enable',2);
			$('#div_benaficiario').show();
			//$('#tabuladores').tabs('enable',2);;
			$('#tr_NumDocumento').hide();
			$('#trEntrada').hide();
			$('#tr_TotalDocumento').hide();
			$('#tr_Programa').hide();
			$('#tr_partida').hide();
			$('#tr_PresupuestoLibre').hide();
			$('#tr_ProyectoPartida').hide();
			$('#tr_ProgramaPartidaPresupuesto').show();
			$('#div_benaficiarioFijo').show();
			$('#div_beneficiario').hide();
			break;
		case "2"://OBRAS
			$('#tabuladores').tabs('enable',1);
			$('#tabuladores').tabs('enable',2);
			$('#tabuladores').tabs('enable',3);
			break;
		case "3"://PEDIDO
			$('#tabuladores').tabs('enable',1);
			$('#tabuladores').tabs('enable',2);
			$('#tabuladores').tabs('enable',3);
			break;
		case "4"://OT/OS
			$('#tabuladores').tabs('enable',1);
			$('#tabuladores').tabs('enable',2);
			$('#tabuladores').tabs('enable',3);
			break;
		case "6"://CONTRATO
			$('#tabuladores').tabs('enable',1);
			$('#tabuladores').tabs('enable',2);
			$('#tabuladores').tabs('enable',3);
			break;
		case "7"://HONORARIOS
			$('#tabuladores').tabs('enable',1);
			$('#tabuladores').tabs('enable',2);
			$('#tabuladores').tabs('enable',3);
			break;
		case "8"://ARRENDAMIENTOS
			$('#tabuladores').tabs('enable',1);
			$('#tabuladores').tabs('enable',2);
			$('#tabuladores').tabs('enable',3);
			break;
		case "9"://FONDO FIJO
			$('#tabuladores').tabs('enable',1);
			$('#tabuladores').tabs('enable',2);
			$('#tabuladores').tabs('enable',3);
			
			break;
		case "10"://CON RETENCION
			$('#tabuladores').tabs('enable',1);
			$('#tabuladores').tabs('enable',2);
			$('#tabuladores').tabs('enable',3);
			
			break;	
		default: 
			$('#tabuladores').tabs('option', 'disabled', [1,2,3]);
			break;
	}
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
 
 function limpiarRetenciones(){
	$('#idRetencion').attr('value', '');
	$('#retencion').val(0);	
	$('#importeRetencion').attr('value','');
	$('#retencion').focus();
}
 
 function llenarTablaDeRetenciones() {
	 quitRow("listasRetenciones");

	 controladorFacturasRemoto.getRetenciones($('#CVE_FACTURA').val(), {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaRetenciones( "listasRetenciones", i+1,this.CONS,this.CLV_RETENC,this.RETENCION,this.IMPORTE);
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
 
 function guardarRetencion(){
  
    if($('#retencion').attr('value')=="") {jAlert('El tipo de Retención no es válido','Advertencia'); return false;}
    if($('#importeRetencion').attr('value')=="") {jAlert('El Importe de la Retenci�n no es v�lido','Advertencia'); return false;}
	
	ShowDelay('Guardando retenci�n','');
	controladorFacturasRemoto.guardarRetencion( $('#idRetencion').attr('value'),$('#retencion').attr('value'),$('#importeRetencion').attr('value'),$('#CVE_FACTURA').val(),{
	callback:function(items) { 	 

			llenarTablaDeRetenciones();
			limpiarRetencion();	
			CloseDelay("Retención guardada con éxito");  	  
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error'); 
		}
	}); 
 }
 
 function eliminarRetencion(){
	  var checkRetenciones = [];
     $('input[name=clavesRetencion]:checked').each(function() {checkRetenciones.push($(this).val()); });	 
	 if (checkRetenciones.length > 0 ) {
		 jConfirm('¿Confirma que desea eliminar la retención?','Eliminar retenci�n', function(r){
			 if(r){
				 	ShowDelay('Eliminando retencione(s)','');
					controladorFacturasRemoto.eliminarRetenciones(checkRetenciones,$('#CVE_FACTURA').val(), {
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

function subirArchivo(){
	if($('#archivo').attr('value')==''||$('#CVE_FACTURA').val()==null|| $('#CVE_FACTURA').val()==0)
		return false;
	ShowDelay("Subiendo archivo al servidor");
	$('#frmEntrada').submit();
}

function showRequest(formData, jqForm, options) { 
    return true; 
} 
 
function showResponse(data)  { 
 	if(data.mensaje){
		CloseDelay("Archivo guardado con éxito");
		mostrarDetallesArchivos();
		document.location = "captura_factura.action?CVE_FACTURA="+$('#CVE_FACTURA').val();
		$('#archivo').attr('value','');
	}
	else{
		_closeDelay();
		jError("No se ha podido cargar el archivo por algunas de las siguientes razones: <br>*Solo se permite un archivo por factura<br>*El nombre del archivo es muy largo<br>*El nombre del archivo contiene caracteres no válidos<br>*Formato de archivo incorrecto", "Error");
	}
} 


function mostrarDetallesArchivos(){
	var cve_factura = $('#CVE_FACTURA').val();
	quitRow("listasArchivo");
	controladorFacturasRemoto.getArchivosFactura(cve_factura, {
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
				controladorFacturasRemoto.eliminarArchivoFactura(idArchivo,{
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


function muestraTiposDocumento(){
	var idDependencia = $('#cbodependencia').val();
	if($('#cbotipodocumento').val()==1)//O.S. y O.T.
	{
		if(idDependencia==0||idDependencia=="") {jAlert('Es necesario seleccionar la Unidad Administrativa para listar las Ordenes de Servicio y Trabajo'); return false;}
			jWindow('<iframe width="650" height="400" name="consultaPedido" id="consultaPedido" frameborder="0" src="../consultas/muestra_OT_OS_facturas.action?idDependencia='+idDependencia+'"></iframe>','Listado de O.S. y O.T.', '','Cerrar ',1);
	}
	if($('#cbotipodocumento').val()==2)//PEDIDO
	{
		if(idDependencia==0||idDependencia=="") {jAlert('Es necesario seleccionar la Unidad Administrativa para listar los Pedidos'); return false;}
			jWindow('<iframe width="650" height="400" name="consultaPedido" id="consultaPedido" frameborder="0" src="../consultas/muestra_pedidos_facturas.action?idDependencia='+idDependencia+'"></iframe>','Listado de Pedidos con Entradas de Almacen', '','Cerrar ',1);
	}
	
	if($('#cbotipodocumento').val()==3)//VALE
	{
		if(idDependencia==0||idDependencia=="") {jAlert('Es necesario seleccionar la Unidad Administrativa para listar los Pedidos'); return false;}
		
		var clv_benefi;
		var tipo_gto;
		var tipo_doc;
		
		if(typeof(clv_benefi)=='undefined') clv_benefi =0;
		if(typeof(tipo_gto)=='undefined') tipo_gto =0;
		if(typeof(tipo_doc)=='undefined') tipo_doc =1; //SOLO VALES ANTICIPO OBRAS (AO)
		if(typeof(idDependencia)=='undefined') idDependencia =0;
		
		if($('#CVE_DOC').attr('value')=='') $('#CVE_DOC').attr('value', 0);
		jWindow('<iframe width="750" height="350" name="ventanaVales" id="ventanaVales" frameborder="0" src="../../sam/consultas/muestra_vales.action?idVale='+$('#CVE_DOC').attr('value')+'&idDependencia='+idDependencia+'&tipo_gto='+tipo_gto+'&clv_benefi='+clv_benefi+'&tipo_doc='+tipo_doc+'"></iframe>','Listado de Vales disponibles', '','Cerrar',1);

	}
	
	if($('#cbotipodocumento').val()==4)//CONTRATO
	{
		if(idDependencia==0||idDependencia=="") {jAlert('Es necesario seleccionar la Unidad Administrativa para listar los contratos'); return false;}
		
		var num_docto = $('#txtdocumento').attr('value');
		
		if(typeof(num_docto)=='undefined') clv_benefi =0;
		if(typeof(idDependencia)=='undefined') idDependencia =0;
		
		if($('#CVE_DOC').attr('value')=='') $('#CVE_DOC').attr('value', 0);
		jWindow('<iframe width="750" height="350" name="ventanaVales" id="ventanaVales" frameborder="0" src="../../sam/consultas/muestra_contratos.action?cve_contrato='+$('#CVE_DOC').attr('value')+'&idDependencia='+idDependencia+'&num_contrato='+num_docto+'"></iframe>','Listado Contratos', '','Cerrar',1);

	}

}


function getValeDocumento(idVale, num_vale, disponible, comprobado){
	$('#trEntrada').hide();
	$('#CVE_DOC').attr('value', idVale);
	$('#txtdocumento').attr('value', num_vale);
	//$('#txtdisponiblevale').attr('value', formatNumber(parseFloat(disponible),'$'));
	//$('#txtcomprobadovale').attr('value',  formatNumber(parseFloat(comprobado),'$'));
	//$('#fila_disponibleVale').show();
	$('#txttotal').attr('value', formatNumber(comprobado).replace(',', ''));
	$('#div_total_entrada').html(formatNumber(comprobado, '$'));
	
	muestraPresupuesto();
	//_closeDelay();
	
}



function deleteDocumento()
{
	$('#txtdocumento').attr('value', '');
	$('#ID_ENTRADA').attr('value', 0);
	$('#CVE_DOC').attr('value', 0);
	$('#ID_ENTRADA').attr('value', 0);
	$('#CLV_BENEFI').attr('value', '');
	$('#div_num_entrada').html("");
	$('#div_total_entrada').html("");
	$('#div_beneficiario').html("");
	$('#div_programa').html("");
	$('#div_partida').html("");
	$('#trEntrada').show();
	$('#txtiva').attr('value','');
	$('#txtsubtotal').attr('value', '');
	$('#txttotal').attr('value', '');
	//quitRow('listaPresupuesto');
}

function costumFunction(){
	cargarBeneficiarioyPresupuestoVale();
}

function cargarBeneficiarioyPresupuestoVale()
{
	if($('#cbotipodocumento').val()==3)
		if($('#CVE_DOC').val()!=''&&$('#CVE_DOC').val()!='0')
			if($('#ID_PROYECTO').val()!=''&&$('#ID_PROYECTO').val()!='0')
				if($('#CLV_PARTID').val()!=''&&$('#CLV_PARTID').val()!='0')
				{
					$('#trEntrada').hide();
					//buscamos el beneficiario
						controladorFacturasRemoto.getBeneficiarioFactura('VAL', $('#CVE_DOC').val(), {
							  callback:function(items){
											$('#div_beneficiario').html(items);
											 
						} 					   				
						,
							errorHandler:function(errorString, exception) { 
								jError(errorString,'Error');   
							}
						});	
						
							/*Cargamos el presupuesto
							controladorFacturasRemoto.getPresupuestoDocumento('VAL', $('#CVE_DOC').val(), {
									  callback:function(items){
										$('#div_programa').html(items.N_PROGRAMA + ' - '+ items.PROGRAMA);
										$('#div_partida').html(items.CLV_PARTID + ' - '+items.PARTIDA);
										$('#ID_PROYECTO').attr('value', items.ID_PROYECTO);
										$('#CLV_PARTID').attr('value', items.CLV_PARTID);
										//getProyectosPartidasVales([{ ID_PROYECTO:items.ID_PROYECTO , PROYECTOPARTIDA:items.N_PROGRAMA+" - "+ items.CLV_PARTID}]);
										quitRow('listaPresupuesto');
										appendNewRow('listaPresupuesto', 
												[Td('', centro , '', items.MES),
												 Td('', derecha , '', '<div style ="height:20px">'+formatNumber(items.AUTORIZADO, '$')+'</div>'),
												 Td('', derecha  , '', ((items.PRECOMPROMETIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'PRECOMPROMETIDO\''+')">': '')+formatNumber(items.PRECOMPROMETIDO, '$')+'</a>'),
												 Td('', derecha  , '', ((items.COMPROMETIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'COMPROMETIDO\''+')">': '')+formatNumber(items.COMPROMETIDO, '$')+'</a>'),
												 Td('', derecha  , '', ((items.DEVENGADO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'DEVENGADO\''+')">': '')+formatNumber(items.DEVENGADO, '$')+'</a>'),
												 Td('', derecha , '', ((items.EJERCIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'EJERCIDO\''+')">': '')+formatNumber(items.EJERCIDO, '$')+'</a>'),
												 Td('', derecha , '', formatNumber(items.DISPONIBLE, '$')+"&nbsp;")
												 
										]);
										
								} 					   				
								,
									errorHandler:function(errorString, exception) { 
										jError(errorString,'Error');   
									}
								});*/
				}
				
}

function regresaEntrada(num_ped, cve_ped, folio, ID_ENTRADA, clv_benefi, iva, subtotal, importeEntrada){
	deleteDocumento();
	$('#trEntrada').show();
	$('#txtdocumento').attr('value', num_ped);
	$('#ID_ENTRADA').attr('value', ID_ENTRADA);
	$('#CVE_DOC').attr('value', cve_ped);
	$('#ID_ENTRADA').attr('value', ID_ENTRADA);
	$('#CLV_BENEFI').attr('value', clv_benefi);
	$('#div_num_entrada').html(folio);
	$('#txtiva').attr('value', formatNumber(iva).replace(',', ''));
	$('#txtsubtotal').attr('value', formatNumber(subtotal).replace(',', ''));
	$('#txttotal').attr('value', formatNumber(importeEntrada).replace(',', ''));
	$('#div_total_entrada').html(formatNumber(importeEntrada, '$'));
	cargarBeneficiarioyPresupuestoPedidos(clv_benefi, cve_ped);
	$('#txtfecha').focus();
	$.alerts._hide();
}

function cargarBeneficiarioyPresupuestoPedidos(clv_benefi, cve_ped)
{
	ShowDelay("Recuperando información presupuestal");
	$('#trEntrada').show();
	//buscamos el beneficiario
	if(clv_benefi!=''){
		controladorFacturasRemoto.getBeneficiarioFactura('PED', cve_ped, {
			  callback:function(items){
							$('#div_beneficiario').html(items);
							 
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString,'Error');   
			}
		});	
	}
	/*Cargamos el presupuesto
	controladorFacturasRemoto.getPresupuestoDocumento('PED', cve_ped, {
			  callback:function(items){
				$('#div_programa').html(items.N_PROGRAMA + ' - '+ items.PROGRAMA);
				$('#div_partida').html(items.CLV_PARTID + ' - '+items.PARTIDA);
				$('#ID_PROYECTO').attr('value', items.ID_PROYECTO);
				$('#CLV_PARTID').attr('value', items.CLV_PARTID);
				//getProyectosPartidasVales([{ ID_PROYECTO:items.ID_PROYECTO , PROYECTOPARTIDA:items.N_PROGRAMA+" - "+ items.CLV_PARTID}]);
				quitRow('listaPresupuesto');
				appendNewRow('listaPresupuesto', 
						[Td('', centro , '', items.MES),
						 Td('', derecha , '', '<div style ="height:20px">'+formatNumber(items.AUTORIZADO, '$')+'</div>'),
						 Td('', derecha  , '', ((items.PRECOMPROMETIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'PRECOMPROMETIDO\''+')">': '')+formatNumber(items.PRECOMPROMETIDO, '$')+'</a>'),
						 Td('', derecha  , '', ((items.COMPROMETIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'COMPROMETIDO\''+')">': '')+formatNumber(items.COMPROMETIDO, '$')+'</a>'),
						 Td('', derecha  , '', ((items.DEVENGADO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'DEVENGADO\''+')">': '')+formatNumber(items.DEVENGADO, '$')+'</a>'),
						 Td('', derecha , '', ((items.EJERCIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'EJERCIDO\''+')">': '')+formatNumber(items.EJERCIDO, '$')+'</a>'),
				 		 Td('', derecha , '', formatNumber(items.DISPONIBLE, '$')+"&nbsp;")
						 
				]);
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString,'Error');   
			}
		});	
		*/
		//mostrarDetallesArchivos();
		_closeDelay();
}

function regresarOSOTFactura(num_req, cve_req, clv_benefi, total)
{
	deleteDocumento();
	$('#trEntrada').hide();
	$('#txtdocumento').attr('value', num_req);
	$('#ID_ENTRADA').attr('value', 0);
	$('#CVE_DOC').attr('value', cve_req);
	$('#ID_ENTRADA').attr('value', 0);
	$('#CLV_BENEFI').attr('value', clv_benefi);
	$('#div_num_entrada').html("");
	$('#div_total_entrada').html(formatNumber(total, '$'));
	$('#txttotal').attr('value', formatNumber(total).replace(',', ''));
	cargarBeneficiarioyPresupuestoOSOT(clv_benefi, cve_req);
		
	$('#txtfecha').focus();
	$.alerts._hide();
}

function cargarBeneficiarioyPresupuestoOSOT(clv_benefi, cve_req){
	ShowDelay("Recuperando información presupuestal");
	$('#trEntrada').hide();
	//buscamos el beneficiario
	if(clv_benefi!=''){
		controladorFacturasRemoto.getBeneficiarioFactura('REQ', cve_req, {
			  callback:function(items){
							$('#div_beneficiario').html(items);
							 
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString,'Error');   
			}
		});	
		
		//mostrarDetallesArchivos();
	}
	
	/*Cargamos el presupuesto
	controladorFacturasRemoto.getPresupuestoDocumento('REQ', cve_req, {
			  callback:function(items){
				$('#div_programa').html(items.N_PROGRAMA + ' - '+ items.PROGRAMA);
				$('#div_partida').html(items.CLV_PARTID + ' - '+items.PARTIDA);
				$('#ID_PROYECTO').attr('value', items.ID_PROYECTO);
				$('#CLV_PARTID').attr('value', items.CLV_PARTID);
				quitRow('listaPresupuesto');
				//getProyectosPartidasVales([{ ID_PROYECTO:items.ID_PROYECTO , PROYECTOPARTIDA:items.N_PROGRAMA+" - "+ items.CLV_PARTID}]);
				appendNewRow('listaPresupuesto', 
						[Td('', centro , '', items.MES),
						 Td('', derecha , '', '<div style ="height:20px">'+formatNumber(items.AUTORIZADO, '$')+'</div>'),
						 Td('', derecha  , '', ((items.PRECOMPROMETIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'PRECOMPROMETIDO\''+')">': '')+formatNumber(items.PRECOMPROMETIDO, '$')+'</a>'),
						 Td('', derecha  , '', ((items.COMPROMETIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'COMPROMETIDO\''+')">': '')+formatNumber(items.COMPROMETIDO, '$')+'</a>'),
						 Td('', derecha  , '', ((items.DEVENGADO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'DEVENGADO\''+')">': '')+formatNumber(items.DEVENGADO, '$')+'</a>'),
						 Td('', derecha , '', ((items.EJERCIDO>0) ? '<a href="javascript:mostrarConsultaCompromiso('+items.ID_PROYECTO+', \''+items.N_PROGRAMA+'\', \''+items.CLV_PARTID + '\', '+items.PERIODO+',\'EJERCIDO\''+')">': '')+formatNumber(items.EJERCIDO, '$')+'</a>'),
				 		 Td('', derecha , '', formatNumber(items.DISPONIBLE, '$')+"&nbsp;")
						 
				]);
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString,'Error');   
			}
		});	
		*/
		
		_closeDelay();
}

function guardarFactura()
{
		var v = validarDetalles();
		if(v) return false;
	
		var cve_factura = $('#CVE_FACTURA').val();
		var cve_doc = $('#CVE_DOC').val();
		var tipo_doc = $('#cbotipodocumento').val();
		var idDependencia =  $('#cbodependencia').val();
		var idTipoFactura = $('#cbotipoFactura').val();
		var clv_benefi = $('#CLV_BENEFI').val();
		var idEntrada = $('#ID_ENTRADA').val();
		var idProyecto = 0;//$('#ID_PROYECTO').val();
		var clv_partid = '';//$('#CLV_PARTID').val();
		var num_fact = $('#txtnumfactura').val();
		var iva = $('#txtiva').val();
		var subtotal = $('#txtsubtotal').val();
		var total = $('#txttotal').val();
		var observacion = $('#txtobservacion').val();
		var fecha_doc = $('#txtfecha').val();
		subtotal = subtotal.replace(',', "");
		
		jConfirm('¿Confirma que desea guardar la factura?','Guardar', function(r){
			if(r){
				
				controladorFacturasRemoto.guardarFactura(cve_factura, tipo_doc, cve_doc, idTipoFactura, idDependencia, idProyecto, clv_partid, clv_benefi, idEntrada, num_fact, iva, subtotal, total,  observacion, fecha_doc, {
					  callback:function(items){
							$('#CVE_FACTURA').attr('value',items);
							subirArchivo();
							$('#cmdcerrar').attr('disabled', false);
							desabilitarControles();;	
							$('#tabuladores').tabs('enable',1);
							tipoFacturasDeductivas();
							cargarDetallePresupuestalDoc($('#CVE_FACTURA').val());
							$('#tr_file').show();
							CloseDelay('Factura guardada con éxito');
							
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError('La operacion de guardado no se ha podido completar correctamente: '+errorString,'Error');   
						return false;
					}
				});
			}																			   
	});
}

function cargarDetallePresupuestalDoc(cve_factura)
{
	controladorFacturasRemoto.cargarDetallePresupuestal(cve_factura, {
					  callback:function(items)
					  {
						  dwr.util.removeAllOptions("cboproyectopartida");
						  dwr.util.addOptions('cboproyectopartida',{ 0:'Seleccione'});
						  jQuery.each(items,function(i) {
							getProyectosPartidasDetalles([{ ID_PROYECTO:this.ID_PROYECTO+","+this.N_PROGRAMA+","+ this.CLV_PARTID, PROYECTOPARTIDA:this.N_PROGRAMA+" - "+ this.CLV_PARTID + " = " + formatNumber (this.MONTO) }]);
						  });
					  } 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError('La operacion de guardado no se ha podido completar correctamente: '+errorString,'Error');   
						return false;
					}
				});
	
}

/*funcion para cerrar el documento*/
function cerrarDocumento(){
	jConfirm('¿Confirma que desea cerrar la factura?','Cerrar', function(r){
				if(r){
						  controladorFacturasRemoto.cerrarFactura($('#CVE_FACTURA').val(),{
						  callback:function(items){ 	    
							  $('#cmdcerrar').attr('disabled',true);
								$('#cmdguardar').attr('disabled',true);
								$('#cmdcerrar').attr('disabled',true);
								CloseDelay('Factura cerrada con éxito');
								document.location='captura_factura.action';
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');   
							return false;
						}
					});
				}																			   
	});
}


function desabilitarControles()
{
	$('#txtnumfactura').attr('disabled', true);
	$('#cbotipodocumento').attr('disabled', true);
}


/*funcion para validar los detalles de los conceptos*/
function validarDetalles(){
	if($('#cbotipodocumento').attr('value')==''||$('#cbotipodocumento').attr('value')==0) {jAlert('Es necesesario seleccionar el tipo de documento para cargar a la factura','Advertencia'); return true;}
	if(($('#CVE_DOC').attr('value')==''||$('#CVE_DOC').attr('value')==0) && $('#cbotipoFactura').val()!='1') {jAlert('Es necesario selecionar un documento de Pedido, OS/OT para continuar', 'Advertencia'); return true;}
	if($('#txtfecha').attr('value')=='') {jAlert('La facha de la factura no es válida', 'Advertencia'); return true;}
	if($('#txtnumfactura').attr('value')=='') {jAlert('El número de la factura no es valido', 'Advertencia'); return true;}
	//if($('#txtsubtotal').attr('value')==''){jAlert('El subtotal de la factura no es válido','Advertencia'); return true;}
	//if($('#txttotal').attr('value')==''){jAlert('El total de la factura no es válido', 'Advertencia'); return true;}
	return false;
}
