/**
Descripcion: Codigo manejador para lst_devolucion_presupuestal.jsp
Autor      : Israel de la Cruz
Fecha      : 17/02/2012
*/
var nLotes = 0;
$(document).ready(function() {  
	$('#tabuladores').tabs();
	$('#tabuladores').tabs('enabled',0);
	$('#tabuladores').tabs('option', 'disabled', [1]);
	//$('#tabuladores').tabs('disabled',1);
	$('#cmdagregar').click(function(event){agregarConcepto();});
	$('#cmdcerrar').click(function(event){cerrarDevolucion();});
	$('#cmdguardar').click(function(event){guardarCambios();});
	$('#cmdcancelar').click(function (event){window.parent.compruebaVariable();});
	$('#cmdnuevo').click(function (event){limpiarDetalles();});
	$("#txtfecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
	$('#txtpartida').keypress(function(event){return keyNumbero(event);});
	$('#img_presupuesto').click(function(event){muestraPresupuesto();});
	$('#img_OrdenPago').click(function(event){mostrarOrdenPago();});
	$('#txtproyecto').blur(function(event){  __getPresupuesto($('#ID_PROYECTO').attr('value'), $('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible',$('#tipoGasto').attr('value'));});	
  	$('#txtpartida').blur(function(event){  __getPresupuesto($('#ID_PROYECTO').attr('value'), $('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible',$('#tipoGasto').attr('value'));});  
	if($('#ID_DEVOLUCION').attr('value')!=0){
		$('#tabuladores').tabs('enable', 1);
		mostrarDetalles();
	}
});

function generarDetallesOrdenPago(idMovtos)
{
	ShowDelay('Cargando movimientos','');
						ControladorDevolucionPresupuestalRemoto.cargarMovimientosOrdenPago(idMovtos, $('#ID_DEVOLUCION').attr('value'), {
						callback:function(items){
							  CloseDelay('Movimientos cargados con éxito');	
							  limpiarDetalles();
							  mostrarDetalles();	
						}
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString,'Error');          
						}
					});
}

function mostrarOrdenPago()
{
	if($('#txtcveop').attr('value')=='')
		$('#CVE_OP').attr('value', '');
		
	jWindow('<iframe width="750" height="350" name="ventanaOrdenPago" id="ventanaOrdenPago" frameborder="0" src="../../sam/consultas/muestra_OrdenPago.action?cve_op='+$('#CVE_OP').attr('value')+'&idDependencia='+$('#cbounidad2').attr('value')+'"></iframe>','Listado de Ordenes de Pago', '','Cerrar',1);
}


function cerrarDevolucion(){
	jConfirm('¿Confirma que desea cerrar la devolución presupuestal?','Confirmar', function(r){
				if(r){
						ShowDelay('Cerrando devolución presupuestal','');
						ControladorDevolucionPresupuestalRemoto.cerrarDevolucion($('#ID_DEVOLUCION').attr('value'), {
						callback:function(items){
							if(items=='') {
								CloseDelay('Devolucion presupuestal cerrada con éxito');
								//window.parent.cambiarVariable(rellenaCeros($('#ID_DEVOLUCION').attr('value'),6));	
								getReporteDevolucion($('#ID_DEVOLUCION').attr('value'));
								$('#cmdcancelar').click();
							}
							else
								jError(items,'Error');
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString,'Error');          
						}
					});
				}
	   });
}

function getReporteDevolucion(idDevolucion) {
	$('#idDevolucion').attr('value',idDevolucion);
	$('#forma').attr('target',"impresion_devolucion");
	$('#forma').attr("action", "../reportes/rpt_devolucion_presupuestal.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"devolucion_presupuestal.action");
}

function eliminarDetalle(){
	 var chkdetalle = [];
     $('input[name=chkdetalle]:checked').each(function() { chkdetalle.push($(this).val());});	
	 if (chkdetalle.length>0){
		jConfirm('¿Confirma que desea eliminar los conceptos de la devolución?','Confirmar', function(r){
				if(r){
						ShowDelay('Eliminando lote','');
						ControladorDevolucionPresupuestalRemoto.eliminarDetallesDevolucion(chkdetalle, $('#ID_DEVOLUCION').attr('value'), {
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
 
function agregarConcepto(){
	if($('#ID_PROYECTO').attr('value')==''||$('#ID_PROYECTO').attr('value')=='0') {jAlert('El programa escrito no es valido'); return false;}
	if($('#txtpartida').attr('value')=='') {jAlert('La partida escrita no es valida'); return false;}
	if($('#txtimporte').attr('value')=='') {jAlert('El importe escrito no es valido'); return false;}
	ShowDelay('Agregando y comprobando concepto','');
	ControladorDevolucionPresupuestalRemoto.agregarConcepto($('#ID_DETALLE').attr('value'), $('#ID_DEVOLUCION').attr('value'), $('#ID_PROYECTO').attr('value'), $('#txtpartida').attr('value'), $('#txtimporte').attr('value'), $('#txtdetalle').attr('value'), $('#cbotipo').val(), {
		callback:function(items){
					if(items=='')
						{
							CloseDelay("Concepto agregado con exito");
							limpiarDetalles();
							mostrarDetalles();
							//window.parent.cambiarVariable(rellenaCeros(items.toString(),6));	
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
	ControladorDevolucionPresupuestalRemoto.getDetallesDevolucion($('#ID_DEVOLUCION').attr('value'), {
						   callback:function(items) { 
						   		jQuery.each(items,function(i){
									$('#cmdcerrar').attr('disabled',false);
									 cont++;
									 total+= this.IMPORTE; 
									 $('#IMPORTE_TOTAL').attr('value', total);
									 pintaTablaConceptos('listaDetalles', this.ID_DETALLE_DEVOLUCION, this.ID_DEPENDENCIA, this.DEPENDENCIA, this.ID_PROYECTO, this.CLV_PARTID, this.N_PROGRAMA, this.DESCRIPCION, this.IMPORTE, this.CLV_RETENC, this.NUM_OP);				   
									 if(items.length==cont) 
										 pintarTotalConceptos('listaDetalles', $('#IMPORTE_TOTAL').attr('value'),cont); 
									 
								});
						   }
	});
}

function editarConcepto(idDetalle, idProyecto, idDependencia, proyecto, clv_partid, nota, importe, clv_retenc, num_op){
	$('#ID_DETALLE').attr('value', idDetalle);
	$('#ID_PROYECTO').attr('value', idProyecto);
	$('#txtdetalle').attr('value', nota);
	$('#txtimporte').attr('value', importe);
	$('#txtproyecto').attr('value', proyecto);
	$('#txtpartida').attr('value', clv_partid);
	$('#cbounidad2').val(idDependencia);
	$('#cbotipo').val(clv_retenc);
	$('#txtcveop').attr('value', num_op);
	$('#CVE_OP').attr('value', parseInt(num_op));

	__getPresupuesto($('#ID_PROYECTO').attr('value'), $('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'),  'txtpresupuesto','txtdisponible',$('#tipoGasto').attr('value'));
}

function pintaTablaConceptos(table, ID_DETALLE_DEVOLUCION, ID_DEPENDENCIA, DEPENDENCIA, ID_PROYECTO, CLV_PARTID, N_PROGRAMA, DESCRIPCION, IMPORTE, CLV_RETENC, NUM_OP){
	var tabla = document.getElementById(table).tBodies[0];
 	var row =   document.createElement( "TR" );    
	var htmlCheck = "<input type='checkbox' name='chkdetalle' id='chkdetalle' value='"+ID_DETALLE_DEVOLUCION+"'>";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt='Editar'  width=\"16\" height=\"16\" border=\"0\" onClick=\"editarConcepto("+ID_DETALLE_DEVOLUCION+","+ID_PROYECTO+","+ID_DEPENDENCIA+",'"+N_PROGRAMA+"','"+CLV_PARTID+"','"+DESCRIPCION+"','"+IMPORTE+"', '"+CLV_RETENC+"', '"+NUM_OP+"')\" >"; 		
	
	row.appendChild( Td("",centro,"",htmlCheck));
	row.appendChild( Td(DEPENDENCIA,izquierda,"",""));
	row.appendChild( Td(getHTML(NUM_OP),centro,"",""));
	row.appendChild( Td(DESCRIPCION,izquierda,"",""));
	row.appendChild( Td(N_PROGRAMA,centro,"",""));
	row.appendChild( Td(CLV_PARTID,centro,"",""));
	row.appendChild( Td(formatNumber(IMPORTE, '$'),derecha,"",""));
	row.appendChild( Td("",centro,"",htmlEdit));
	tabla.appendChild(row);
}

function pintarTotalConceptos(tabla, importe_total, cont){
	var tabla = document.getElementById(tabla).tBodies[0];
 	var row =   document.createElement( "TR" );    
	row.height = 20;
	var htmlEdit = '<strong>'+formatNumber(importe_total, '$')+'</strong>';
	row.appendChild( Td('',derecha,'','<strong >Total devolución: </strong>',6));
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
	$('#txtimporte').attr('value', '');
	$('#txtpresupuesto').attr('value','');
	$('#txtdisponible').attr('value','');
	$('#cbotipo').val(0);
	$('#CVE_OP').attr('value', '0');
}

function guardarCambios(){
	if($('#tipoGasto').val()==0) {jAlert('Es necesario seleccionar el Tipo de gasto'); return false;}
	if($('#cbomes').val()==0) {jAlert('Es necesario seleccionar un Periodo valido'); return false;}
	if($('#txtfecha').attr('value')=='') {jAlert('Fecha no valida de periodo'); return false;}
	if($('#txtconcepto').attr('value')==''){jAlert('El Concepto no es valido, vuelva a escribirlo'); return false;}
	if($('#txtdescripcion').attr('value')==''){jAlert('La Descripcion no es valida, vuelva a escribirla'); return false;}
	//if(nLotes==0) {jAlert('Es necesario agregar al menos un conceptos presupuestal'); return false;}

	jConfirm('¿Confirma que desea guardar la devolución presupuestal?','Guardar', function(r){
				if(r){
							ShowDelay('Guardando Devolucion Presupuestal','');
							ControladorDevolucionPresupuestalRemoto.guardarDevolucionPresupuestal($('#ID_DEVOLUCION').attr('value'), $('#cbounidadx').attr('value'), $('#cbomes').val(), $('#tipoGasto').attr('value'), $('#txtfecha').attr('value'), $('#txtconcepto').attr('value'), $('#txtdescripcion').attr('value'), {
							callback:function(items){
								if(items!=''||items!='0')
									{
										CloseDelay("Devolucion guardada con exito");
										//window.parent.cambiarVariable(rellenaCeros(items.toString(),6));	
										$('#tabuladores').tabs('enabled',1);
										$('#ID_DEVOLUCION').attr('value', items);
										$('#div_devolucion').html('<strong>'+rellenaCeros(items.toString(),6)+'</strong>');
										if($('#ID_DEVOLUCION').attr('value')!=0)
											$('#tabuladores').tabs('enable', 1);
									}
								else
									jError(items,'Error');
							} 					   				
							,
							errorHandler:function(errorString, exception) { 
								jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");       
							}
						
					});
				}
			});	  
}

function muestraPresupuesto(){
	if($('#txtproyecto').attr('value')==''||$('#txtpartida').attr('value')=='')
		$('#ID_PROYECTO').attr('value', '0');
		var idUnidad = $('#cbUnidad2').attr('value');
		if(idUnidad==null||idUnidad=="") idUnidad =0;
		
	__listadoPresupuesto($('#ID_PROYECTO').attr('value'),$('#txtproyecto').attr('value'),$('#txtpartida').attr('value'), $('#cbomes').attr('value'), $('#tipoGasto').val(), $('#cbounidad2').val());
}
