/**
Descripcion: Codigo controlador para la pagina cap_vale.jsp
Autor      : Mauricio Hernandez, Israel de la Cruz
Fecha      : 19/01/2010
Ultima Edic: 28/02/2012
*/

var nLotes;
$(document).ready(function(){
  var options = { 
        beforeSubmit:  showRequest,  
        success:       showResponse, 
        url:       '_subirArchivo.action?CVE_VALE='+$('#cve_val').val(),
        type:      'post', 
        dataType:  'json'
    }; 
	
   $('#forma').submit(function(){
		$(this).ajaxSubmit(options);
		return false;
   });
  $('.tiptip a.button, .tiptip button').tipTip();
  $('#tabuladores').tabs();
  $('#tabuladores').tabs('enable',0);
  $('#tabuladores').tabs('option', 'disabled', [1]);
  $("#fecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
  $("#fechaFinal").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
  $("#fechaMaxima").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
  $("#fechaInicial").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
  $('#img_presupuesto').click(function(event){muestraPresupuesto();});
  $('#txtproyecto').blur(function(event){__getPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible','');});	
  $('#txtpartida').blur(function(event){__getPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible','');});
 $('#img_quitar_contrato').click(function(event){removerContrato();});
 $('#img_contrato').click(function(event){muestraContratos();});
 
  cambioTipoVale();
  if ($('#txtproyecto').attr('value')!="" && $('#txtpartida').attr('value')!="" )
     __getPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible','');

  if ($('#cve_val').attr('value')!=0&&$('#cve_val').attr('value')!='')
		$('#cmdcerrar').attr('disabled','');
		
	$('#cmdagregar').click(function(event){agregarEdidatConcepto();});
	$('#cmdnuevoconcepto').click(function(event){limpiarDetalles();});
	
	if($('#cve_val').attr('value')!=0){
		$('#tabuladores').tabs('enable', 1);
		mostrarDetallesArchivos();
		mostrarDetalles();
	}
	$('#ui-datepicker-div').hide();
	
	
	
});

function getcontratoDocumento(num_contrato, cve_contrato, idRecurso, clv_benefi, proyecto, clv_partid, importe, ncomercia)
{
	$('#CVE_CONTRATO').attr('value', cve_contrato);
	$('#txtnumcontrato').attr('value', num_contrato);
	$('#CLV_BENEFI').attr('value', clv_benefi);
	$('#claveBeneficiario').attr('value', clv_benefi);
	$('#beneficiario').attr('value', ncomercia);
	_closeDelay();
	//cargarDetallesContrato(num_contrato, cve_contrato, idRecurso, clv_benefi, proyecto, clv_partid, importe);
}

function agregarEdidatConcepto(){
	if($('#ID_PROYECTO').attr('value')==''||$('#ID_PROYECTO').attr('value')=='0') {jAlert('El programa escrito no es valido'); return false;}
	if($('#txtpartida').attr('value')=='') {jAlert('La partida escrita no es valida'); return false;}
	if($('#txtimporteDet').attr('value')=='') {jAlert('El importe escrito no es valido'); return false;}
	ShowDelay('Agregando concepto','');
	controladorValesRemoto.agregarConcepto($('#ID_DETALLE').attr('value'), $('#cve_val').attr('value'), $('#ID_PROYECTO').attr('value'), $('#txtpartida').attr('value'), $('#txtimporteDet').attr('value'), $('#txtdetalle').attr('value'), {
		callback:function(items){
					if(items=='')
						{
							CloseDelay("Concepto agregado con exito");
							limpiarDetalles();
							mostrarDetalles();
							nLotes++;
					}
					else{
						_closeDelay();
						jError(items,'Error');
					}
						
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");       
				}
		});
}

function mostrarDetalles(){
	var cont=0;
	var total=0;
	quitRow("listaDetalles");
	$('#cmdcerrar').attr('disabled',true);
	controladorValesRemoto.getDetallesVales($('#cve_val').attr('value'), {
						   callback:function(items) { 
						   		jQuery.each(items,function(i){
									$('#cmdcerrar').attr('disabled',false);
									 cont++;
									 total+= this.IMPORTE; 
									 $('#IMPORTE_TOTAL').attr('value', total);
									 pintaTablaConceptos('listaDetalles', this.ID_MOV_VALE, this.ID_DEPENDENCIA, this.DEPENDENCIA, this.ID_PROYECTO, this.CLV_PARTID, this.N_PROGRAMA, this.DESCRIPCION, this.IMPORTE);				   
									 if(items.length==cont) 
										 pintarTotalConceptos('listaDetalles', $('#IMPORTE_TOTAL').attr('value'),cont); 
									 
								});
						   }
	});
}

function pintaTablaConceptos(table, ID_MOV_VALE, ID_DEPENDENCIA, DEPENDENCIA, ID_PROYECTO, CLV_PARTID, N_PROGRAMA, DESCRIPCION, IMPORTE){
	var tabla = document.getElementById(table).tBodies[0];
 	var row =   document.createElement( "TR" );    
	var htmlCheck = "<input type='checkbox' name='chkdetalle' id='chkdetalle' value='"+ID_MOV_VALE+"'>";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt='Editar'  width=\"16\" height=\"16\" border=\"0\" onClick=\"editarConcepto("+ID_MOV_VALE+","+ID_PROYECTO+","+ID_DEPENDENCIA+",'"+N_PROGRAMA+"','"+CLV_PARTID+"','"+DESCRIPCION+"','"+IMPORTE+"')\" >"; 		
	
	row.appendChild( Td("",centro,"",htmlCheck));
	row.appendChild( Td(DEPENDENCIA,izquierda,"",""));
	row.appendChild( Td(DESCRIPCION,izquierda,"",""));
	row.appendChild( Td(N_PROGRAMA,centro,"",""));
	row.appendChild( Td(CLV_PARTID,centro,"",""));
	row.appendChild( Td(formatNumber(IMPORTE, '$'),derecha,"",""));
	row.appendChild( Td("",centro,"",htmlEdit));	
	tabla.appendChild(row);
}

function editarConcepto(idDetalle, idProyecto, idDependencia, proyecto, clv_partid, nota, importe){
	$('#ID_DETALLE').attr('value', idDetalle);
	$('#ID_PROYECTO').attr('value', idProyecto);
	$('#txtdetalle').attr('value', nota);
	$('#txtimporteDet').attr('value', getHTML(importe));
	$('#txtproyecto').attr('value', proyecto);
	$('#txtpartida').attr('value', clv_partid);
	$('#cbounidad2').val(idDependencia);
	
	__getPresupuesto($('#ID_PROYECTO').attr('value'), $('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible',$('#tipoGasto').attr('value'));
}

function pintarTotalConceptos(tabla, importe_total, cont){
	var tabla = document.getElementById(tabla).tBodies[0];
 	var row =   document.createElement( "TR" );    
	row.height = 20;
	var htmlEdit = '<strong>'+formatNumber(importe_total, '$')+'</strong>';
	row.appendChild( Td('',derecha,'','<strong >Total Vale: </strong>',5));
	row.appendChild( Td('',derecha,"",htmlEdit));
	row.appendChild( Td('',centro,'',''));	
	tabla.appendChild(row);
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

function eliminarDetalle(){
	 var chkdetalle = [];
     $('input[name=chkdetalle]:checked').each(function() { chkdetalle.push($(this).val());});	
	 if (chkdetalle.length>0){
		jConfirm('¿Confirma que desea eliminar los conceptos seleccionados del Vale?','Confirmar', function(r){
				if(r){
						ShowDelay('Eliminando conceptos','');
						controladorValesRemoto.eliminarDetalles(chkdetalle, $('#cve_val').attr('value'), {
							callback:function(items) {
								
							limpiarDetalles();
							mostrarDetalles();
							//window.parent.cambiarVariable(rellenaCeros(items.toString(),6));	
							CloseDelay('Conceptos eliminados con éxito');	
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
	    jAlert('Es necesario que seleccione por lo menos un concepto del listado', 'Advertencia');
 }

function regresar(){
  document.location ="lista_vales.action";
}

function muestraPresupuesto(){
	if($('#txtproyecto').attr('value')==''||$('#txtpartida').attr('value')=='')
		$('#ID_PROYECTO').attr('value', '0');
		var idUnidad = $('#unidad2').val();
		if(idUnidad==null||idUnidad=="") idUnidad =0;
		
		if($('#CVE_CONTRATO').attr('value')!='0'&&$('#CVE_CONTRATO').attr('value')!='')	{
			
			var tipo_gto = $('#tipoGasto').val();
			if(typeof(tipo_gto)=='undefined') tipo_gto =0;
			if($('#txtvale').attr('value')=='') $('#CVE_VALE').attr('value', '');
			//if($('#CVE_CONTRATO').attr('value')==''||$('#CVE_CONTRATO').attr('value')=='0') {jAlert('Es necesario seleccionar un Vale para mostrar su informacion presupuestal', 'Advertencia'); return false;}
			
			__listadoPresupuestoContrato($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),tipo_gto, 0, $('#CVE_CONTRATO').attr('value'));
		}
		else	
			__listadoPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'), $('#tipoGasto').val(), idUnidad);
}

function cerrarVale(){
jConfirm('¿Confirma que desea cerrar el Vale y enviarlo para su pago a Finanzas ?','Confirmar', function(r){
	if(r){
	  var checkVales = [];
     checkVales.push($('#cve_val').attr('value'));	 
	 if (checkVales.length > 0 ) {
	   	ShowDelay('Cerrando Vale', '');
       	controladorListadoValesRemoto.cerrarVale(checkVales, {
        callback:function(items) {
			jQuery.each(items,function(i){
				if(this.ESTADO=='SI'){
					
					CloseDelay('Vale cerrado con éxito', function(){limpiar();});
					getReporteVale($('#cve_val').attr('value'));
				}
				else
					jError('No se ha podido cerrar el Vale, es posible que no tenga suficiencia presupuestal en algunos de los conceptos agregados', 'Error');
			});
      } 					   				
      ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
       }); 
	 } else 
	    jAlert("Es necesario que seleccione un elemento de la lista", "Advertencia");					
	 }
  });
}

function cambioTipoVale(){
	if ($("#tipoVale").attr("value")!=1 )  {
	 $("#ejefechaCom").show();
	 $("#ejefecha").show();
	}else {
		$("#ejefechaCom").hide();
	 	$("#ejefecha").hide();
		$('#fechaInicial').attr('value','');
	 	$('#fechaFinal').attr('value','');
	 	$('#fechaMaxima').attr('value','');
	}	

	if ($("#tipoVale").attr("value")!='GC' ) 
	  $('#tipoBeneficiario').attr('value', 2);
	 else
	  $('#tipoBeneficiario').attr('value', 1);	 
	
	
	if ($("#tipoVale").attr("value")=='FR')
		$('#tipoBeneficiario').attr('value', 1);
		
	if ($("#tipoVale").attr("value")!="" ) 	
		getBeneficiarios('beneficiario','claveBeneficiario',$('#tipoBeneficiario').attr('value'));
}

function limpiar(){
	 	 $('#cve_val').attr('value','');
		 $('#fecha').attr('value','');
		 $('#tipoVale').attr('value','');
		 $('#div_vale').html('');
		 //$('#importe').attr('value','');
		 $('#claveBeneficiario').attr('value','');
		 $('#beneficiario').attr('value','');
		 $('#justificacion').attr('value','');
		 $('#txtproyecto').attr('value','');
		 $('#txtpartida').attr('value','');
		 $('#txtpresupuesto').attr('value','');
		 $('#txtdisponible').attr('value','');
		 $('#fechaInicial').attr('value','');
		 $('#fechaFinal').attr('value','');
		 $('#fechaMaxima').attr('value','');
		 $('#documentacion').attr('value','');
		 $('#tipoGasto').val(0);
		 quitRow("listaDetalles");
		 limpiarDetalles();
}


function getReporteVale(clave) {
$('#cve_val').attr('value',clave);
$('#forma').attr('action',"../reportes/formato_vale.action");
$('#forma').attr('target',"impresion");
$('#forma').submit();
$('#forma').attr('target',"");
$('#forma').attr('action',"lista_vales.action");
}

function subirArchivo(){
	
	if($('#archivo').attr('value')==''||$('#cve_val').val()==null|| $('#cve_val').val()==0)
		return false;
	_closeDelay();
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
		$('#archivo').attr('value','');
	}
	else{
		_closeDelay();
		jError("No se ha podido cargar el archivo por algunas de las siguientes razones: <br>*Solo se permite un archivo por Vale<br>*El nombre del archivo es muy largo<br>*El nombre del archivo contiene caracteres no válidos<br>*Formato de archivo incorrecto, solo se aceptan *.PDF", "Error");
	}
} 

function mostrarDetallesArchivos(){
	var cve_vale = $('#cve_val').val();
	quitRow("listasArchivo");
	controladorValesRemoto.getArchivosVale(cve_vale, {
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
				controladorValesRemoto.eliminarArchivoVale(idArchivo,{
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

function guardar(){			
		 if($('#tipoGasto').attr('value')==''){jAlert('El tipo de gasto no es valido','Advertencia'); return false;}
		 if ($('#fecha').attr('value')==""){jAlert('La Fecha del Vale no es válida','Advertencia'); return false;}	
		 if ($('#tipoVale').attr('value')=="") {jAlert('El Tipo del Vale no es válido','Advertencia');return false;}	
		 if ($('#tipoGasto').attr('value')=="0")  {jAlert('El tipo de gasto no es válido','Advertencia');return false;}	
		 if ($('#claveBeneficiario').attr('value')=="") {jAlert('El Beneficiario del Vale no es válido', 'Advertencia');return false;}	
		 if ($('#justificacion').attr('value')=="")  {jAlert('La Justificación del Vale no es válida', 'Advertencia');return false;}
		 //if ($('#txtproyecto').attr('value')=="" && $("#tipoVale").attr("value")!='FR') {jAlert('El Programa del Vale no es válido','Advertencia');return false;}	
		 //if ($('#txtpartida').attr('value')=="" && $("#tipoVale").attr("value")!='FR')  {jAlert('La Partida del Vale no es válida','Advertencia');return false;}	
		 if ($('#cbomes').attr('value')=="")  {jAlert('El Mes del Vale no es válido','Advertencia');return false;}	
		 if ((parseFloat($('#txtdisponible').attr('value')) < parseFloat($('#importe').attr('value')))&& $("#tipoVale").attr("value")!='FR')  {jAlert('El importe del Vale es mayor al presupuesto disponible','Advertencia');return false;}
		 if ($('#fechaInicial').attr('value')=="")  {jAlert('La Fecha Incial no es válida','Advertencia');return false;}	
		 if ($('#fechaFinal').attr('value')=="")  {jAlert('La Fecha final no es válida', 'Advertencia');return false;}
		 if ($('#documentacion').attr('value')=="")  {jAlert('La Documentacion comprobatoria no es válida', 'Advertencia'); return false;}

		jConfirm('¿Confirma que desea guardar la información del Vale?','Confirmar', function(r){
				if(r){
					ShowDelay('Guardando Vale','');
					 controladorValesRemoto.guardarVale( $('#cve_val').attr('value'), $('#tipoGasto').attr('value'),									   
					 $('#fecha').attr('value'),
					 $('#tipoVale').attr('value'),
					 $('#claveBeneficiario').attr('value'),
					 $('#justificacion').attr('value'),
					 
					 $('#cbomes').attr('value'),
					 $('#fechaInicial').attr('value'),
					 $('#fechaFinal').attr('value'),
					 $('#fechaMaxima').attr('value'),
					 $('#documentacion').attr('value'),
					 $('#CVE_CONTRATO').attr('value'),
					  {
						 callback:function(items) {	
						 		$('#cve_val').attr('value',items);
								$('#tabuladores').tabs('enable', 1);
								$('#div_vale').html(rellenaCeros(items.toString(), 6));
								$('#cmdcerrar').attr('disabled','');		
								$('#tabuladores').tabs('enabled',1);
								mostrarDetalles();
								subirArchivo();	 
								CloseDelay('Vale guardado satisfactoriamente', function(items){
									if ($('#cve_val').attr('value')==0) {
										getReporteVale($('#cve_val').attr('value'));
									} 
								}); 				
						 }	
						,errorHandler:function(errorString, exception) { 
							 jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
						}
					});		
				}
		});
		
}