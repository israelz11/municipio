var cve_op = 0;

$(document).ready(function() { 
				$(".trigger").click(function(){
				$(".panel").toggle("fast");
				$(this).toggleClass("active");
				return false;
			});
		 $('#div_unidades').hide();
		 $("#txtfecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
		 $('#btnBuscar').click(function(event){buscarOpMes();})
		 $('#cmdfecha').click(function(event){cambiarFecha();})
		 $('#cmdejercer').click(function(event){ejercerOP();});
		 $("#txtfechanueva").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
		 //$("#txtfechaentrada").datepicker({showOn: 'button', disabled: true, buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
		 $('#cbotiporelacion').change(function(event){cambiarTipoRelacion();});
		 $('#cborelacion').change(function(event){cargarRelacion();});
		 $('#cmdabrir').click(function(event){abrirCerrarRelacion();});
		 $('#cmdmodificar').click(function(event){modificarRelacion();});
		 $('#cmdnueva').click(function(event){nuevaRelacion();});
		 $('#cmdagregar').click(function(event){agregarOpRelacion();});
		 $('#cmdeliminar').click(function(event){eliminarOpRelacion();});
		 $('#cmdimprimir').click(function(event){imprimirRelacion();});
		 $('#cmdimprimirgeneral').click(function(event){imprimirGeneral();})
		 $('#txtnumop').keypress(function(event){if (event.keyCode == '13'){$('#cmdagregar').click();}});
		 $('#ui-datepicker-div').hide();
		 if($('#txtfecha').val()=='')
		 {
			 var fecha = new Date();
		 	$('#txtfecha').val(fecha.getDate()+'/'+(fecha.getMonth()+1)+'/'+fecha.getFullYear()); 
		 }
		 $('#cbotiporelacion').val(0);
});


function imprimirGeneral(){
	var tipo = "";
	if($('#cbotiporelacion').val()==1) 
		tipo = "ENVIO";
	else if($('#cbotiporelacion').val()==2)
		tipo = "DEVOLUCION";
	else if($('#cbotiporelacion').val()==3)
		tipo = "VALES";
	else
		tipo = "VALES_DEVOLUCION";
		
	window.open('../consultas/rpt_relacion_globalOP.action?tipo='+tipo,'mywindow2','');
}

function cambiarFechaRelacion(){
	var fecha = $('#txtfecharelacion').attr('value');
	var id_relacion = $('#cborelacion').val();
	var IdDependencia = $('#cbodependenciaM').val();
	
	//ShowDelay('Cambiando fecha a la relacion');
	if(fecha=='') {jAlert('La fecha de la relacion no es valida','Advertencia'); return false;}
	controladorListadoOrdenPagoEjercidoRemoto.cambiarFechaRelacion(fecha, id_relacion, IdDependencia,{
						callback:function(items) {
							if(items!='') 
								jError(items,'Error');
							else
								{
									//CloseDelay('Fecha cambiada con exito',2000);
									if($('#cbotiporelacion').val()==1)
										cargarRelacionEnvio(id_relacion);
									else if($('#cbotiporelacion').val()==2)
										cargarRelacionDevolucion(id_relacion);
									else if($('#cbotiporelacion').val()==4)
										cargarRelacionValesDevolucion(0);
									else
										cargarRelacionVales(id_relacion);
								}
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
}

function imprimirRelacion(){
	var id_relacion = $('#cborelacion').val();
	$('#id_relacion').attr('value',id_relacion);
	
	$('#frmreporte').attr('action',"../reportes/rpt_relacion_envio.action");

	$('#frmreporte').attr('target',"impresion");
	$('#frmreporte').submit();
	$('#frmreporte').attr('target',"");
	$('#frmreporte').attr('action',"");
	$('#cmdimprimir').attr('disabled', false);
	$('#cmdimprimir').focus();
}

function crearRelacionOP(){
	var fecha = $('#txtfecharelacion').attr('value');
	var idDependencia = $('#cbodependencia2').val();
	if(idDependencia==null) idDependencia = 0;

	//ShowDelay('Creando una nueva relacion');
	controladorListadoOrdenPagoEjercidoRemoto.nuevaRelacion(fecha, $('#cbotiporelacion').val(), idDependencia, {
						callback:function(items) {
							limpiar();
							if($('#cbotiporelacion').val()==1)
								cargarRelacionEnvio(items);
							else if($('#cbotiporelacion').attr('value')==2)
								cargarRelacionDevolucion(items);
							else if($('#cbotiporelacion').attr('value')==4)
								cargarRelacionValesDevolucion(0);
							else
								cargarRelacionVales(items);
							
							/*
							if($('#cbotiporelacion').val()==1)
								setTimeout('cargarRelacionEnvio('+items+')',1300);
							else if($('#cbotiporelacion').attr('value')==2)
								setTimeout('cargarRelacionDevolucion('+items+')',1300);
							else 
								setTimeout('cargarRelacionVales('+items+')',1300);
								*/

							//CloseDelay('Relacion creada con exito ',2000);
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
}

function nuevaRelacion(){
	var subtitulo = ($('#cbotiporelacion').val()>1) ? 'Devolucion':'Envio';
	var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
				  '<tr>'+
					'<td height="20" width="150">Formato de fecha:</td>'+
					'<td width="200">dd/mm/aaaa</td>'+
				  '</tr>'+
				  '<tr>'+
					'<td height="20">Fecha Nueva:</td>'+
					'<td><input type="text" id="txtfecharelacion" value="" style="width:100px" /></td>'+
				  '</tr>'+
				  '<tr>'+
					'<td height="50" align="center" colspan="2"><input type="button" value="Crear Relaci&oacute;n" id="cmdcrearrelacion" class="botones"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones"/></td>'+
				  '</tr>'+
				'</table>';
	jWindow(html,'Nueva Relacion de '+subtitulo, '','',0);
	$('#cmdcrearrelacion').click(function(event){crearRelacionOP();})
	$('#cmdcancelar').click(function(event){$.alerts._hide();})
	$('#txtfecharelacion').keypress(function(event){if (event.keyCode == '13'){$('#cmdcrearrelacion').click();}});

}


function cargaDetalles(){
	if($('#hddetalle').attr('value')!=0) {
		//metodo cancelar
		$('#hddetalle').attr('value',0);
		$('#txtnumop').attr('value', '');
		$('#txtarea').attr('value', '');
		$('#cmdmodificarop').attr('value', 'Modificar');
		$('#cmdeliminar').attr('disabled', false);
		$('#chkdevuelto').attr('checked', false);
		return false;
	}
	var id = parseInt($('#lstdetalles').val());
	//ShowDelay('Cargando detalle');
	controladorListadoOrdenPagoEjercidoRemoto.cargarDetalle(id, {
						callback:function(items) { 	
							$('#hddetalle').attr('value', items.ID_DETALLE);
							$('#txtnumop').attr('value', items.CVE_OP);
							$('#txtarea').attr('value', getHTML(items.OBSERVACIONES));
							$('#chkdevuelto').attr('checked', (items.DEVOLUCION=='S') ? true: false);
							$('#cmdmodificarop').attr('value', 'Cancelar');
							$('#cmdeliminar').attr('disabled', true);
							//_closeDelay();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
}

function validarList(){
	var cont=0;
	$("#lstdetalles option").each(function(){
		cont++;
	});
	if(cont>0&&($('#cbotiporelacion').val()==1||$('#cbotiporelacion').val()==2)) {
		$('#cmdmodificarop').attr('disabled', false);
		$('#txtarea').attr('disabled', false);
		$('#chkdevuelto').attr('disabled', false);
	}
	else{
		$('#cmdmodificarop').attr('disabled', true);
		$('#txtarea').attr('disabled', true);
		$('#chkdevuelto').attr('disabled', true);
	}
}

function modificarRelacion(){
	var html = '<table width="500" border="0" cellspacing="0" cellpadding="0">' +
				  '<tr>'+
					'<td height="30" width="120"><strong>Unidad Administrativa.:</strong></td>'+
					'<td width="350"><select name="cbodependenciaM"  id="cbodependenciaM" style="width:350px" class="comboBox"></select></td>'+
				  '</tr>'+
				  '<tr>'+
					'<td height="30" width="150">Formato de fecha:</td>'+
					'<td width="200">dd/mm/aaaa</td>'+
				  '</tr>'+
				  '<tr>'+
					'<td height="30"><strong>Fecha Nueva:</strong></td>'+
					'<td><input type="text" id="txtfecharelacion" value="'+$('#divfechaentrada').text()+'" style="width:100px" /></td>'+
				  '</tr>'+
				  '<tr>'+
					'<td height="50" align="center" colspan="2"><input type="button" value="Guardar cambios" id="cmdcrearrelacion" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones" style="width:100px"/></td>'+
				  '</tr>'+
				'</table>';
	jWindow(html,'Editar propiedades de la relacion', '','',0);
	$('#cmdcrearrelacion').click(function(event){cambiarFechaRelacion();})
	$('#cmdcancelar').click(function(event){$.alerts._hide();})
	$('#txtfecharelacion').keypress(function(event){if (event.keyCode == '13'){$('#cmdcrearrelacion').click();}});

	$('#cbodependenciaM').append($("#cbodependencia2 > option").clone());
	$('#cbodependenciaM').val($("#cbodependencia2").val());
	
	if($('#cbotiporelacion').val()!=2) 
		$('#cbodependenciaM').attr('disabled', true);

}

function eliminarOpRelacion(){
	var titulo = "la Orden de Pago";
	if($('#cbotiporelacion').val()==3||$('#cbotiporelacion').val()==4) titulo = "el vale";
	var op = [];
	$("#lstdetalles option").each(function(){
		if($(this).attr('selected')) op.push($(this).val());
	});
	if(op.length>0){
		jConfirm('�Confirma que desea eliminar '+titulo+' de la relaci�n actual?','Confirmar', function(r){
					if(r){
						ShowDelay('Eliminando detalles');
						controladorListadoOrdenPagoEjercidoRemoto.eliminarOpRelacion(op,{
						callback:function(items) { 	
							if(items=="") 
								CloseDelay(($('#cbotiporelacion').val()==3 || $('#cbotiporelacion').val()==4) ? 'Vales eliminados con exito':'Ordenes de Pago eliminadas con exito', 2000, cargarRelacion());
							else 
								jError(items, 'Error');
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
		jAlert('Es necesario seleccionar almenos una Orden de Pago de la lista','Advertencia');
}


function agregarOpManual(){
	var checks = [];
	var titulo = "Ordenes de Pago";
	if($('#cbotiporelacion').val()==3) titulo = "Vales";
	var texto = $('#txtarea').attr('value');
	var op = $('#txtnumop').attr('value');
	checks.push(op);
	var id_relacion = $('#cborelacion').val();
	if(op=='') {jAlert('El n&uacute;mero de Orden de Pago/Vale no es v&aacute;lido, debe seleccionar del listado o introducirlo manualmente', 'Advertencia'); $('#txtnumop').focus(); return false;}
	if($('#cbotiporelacion').val()<2)
		texto="";
	//agregar ops automaticamnte
	//ShowDelay('Agregando '+titulo);
	controladorListadoOrdenPagoEjercidoRemoto.agregarOpRelacion(checks, id_relacion, texto,{
					callback:function(items) { 	
						if(items=="") 
							CloseDelay(titulo+' agregadas con �xito', 2000, cargarRelacion());
						else 
							jError(items, 'Error');
				 } 					   				
				 ,
				 errorHandler:function(errorString, exception) { 
					jError(errorString);          
				 }
	});

}

function guardarCambiosOP(){
	var titulo = "Ordenes de Pago";
	if($('#cbotiporelacion').val()==3) titulo = "Vales";
	var id = parseInt($('#lstdetalles').val());
	var texto = $('#txtarea').attr('value');
	var devolucion = ($('#chkdevuelto').is(':checked') ? 'S':'N');
	var idDependencia = $('#cbodependencia').val();
	//ShowDelay('Guardando cambios');
	controladorListadoOrdenPagoEjercidoRemoto.guardarOpDetalle(id, texto, devolucion, idDependencia, {
						callback:function(items) { 	
							if(items==""){ 
								CloseDelay(titulo+' guardada con �xito', 2000, cargarRelacion());
								$('#hddetalle').attr('value',0);
								$('#txtnumop').attr('value', '');
								$('#txtarea').attr('value', '');
								$('#cmdmodificarop').attr('value', 'Modificar');
								$('#cmdmodificarop').attr('disabled', true);
								$('#chkdevuelto').attr('checked', false);
							}
							else 
								jError(items, 'Error');
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
}

function agregarOpRelacion(){
	var titulo = "";
	var checks = [];
	var id_relacion = $('#cborelacion').val();
	var texto = $('#txtarea').attr('value');
	if($('#hddetalle').attr('value')!=0&&$('#cbotiporelacion').val()>1) {guardarCambiosOP(); return false;}
	$('input[id=chkOP]:checked').each(function() {checks.push($(this).val()); });

	/*comporvar si es vale*/
	if($('#cbotiporelacion').val()==3||$('#cbotiporelacion').val()==4) 
		titulo = "Vales";
	else 
		titulo = "Orden de pago";
		
	if($('#cbotiporelacion').val()=="3"&&$('#txtnumop').attr('value')!=''){
		var val = $('#txtnumop').attr('value');
		checks = [];
		checks.push(val);
	}
	if(checks.length==0&&$('#cbotiporelacion').val()==3){jAlert('El n&uacute;mero de vale no es v&aacute;lido, vuelva a escribirlo','Advertencia'); return false;}
	
	if (checks.length>0){
		//agregar ops automaticamnte
		//ShowDelay('Agregando '+titulo);
		controladorListadoOrdenPagoEjercidoRemoto.agregarOpRelacion(checks, id_relacion, texto,{
						callback:function(items) { 	
							if(items=="") {
								if($('#cbotiporelacion').val()==3||$('#cbotiporelacion').val()==4)
									CloseDelay('Vale agregado con �xito', 2000, cargarRelacion());
								else
									CloseDelay(titulo+' agregados con �xito', 2000, cargarRelacion());
								//$("input[id=chkOP]:checked").attr('checked', false);
							}
							else 
								jError(items, 'Error');
					 } 	
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
	}
	else
	{
		agregarOpManual();
		
	}
}

function abrirCerrarRelacion(){
	var id_relacion = $('#cborelacion').val();
	var status = ($('#hdcerrada').attr('value')=='S') ? 'N': 'S';
	var pregunta = ($('#hdcerrada').attr('value')=='S') ? '�Confirma que desea abrir la relacion actual para su edicion?': '�Confirma que desea cerrar la relacion actual e impedir nuevos cambios?';
	jConfirm(pregunta,'Confirmar', function(r){
		if(r){
			//ShowDelay('Abriendo la relaci�n actual');
			controladorListadoOrdenPagoEjercidoRemoto.abrirRelacion(id_relacion, status,{
						callback:function(items) { 	
							if(items) CloseDelay('Relaci�n abierta con �xito', 2000, cargarRelacion());
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
		}
	});
}

/*funcion para cargar los diferentes tipo de relaciones de ordenes de pago*/
function cambiarTipoRelacion(){
	var v = $('#cbotiporelacion').val();
	$('#div_unidades').hide();
	if(v==0) inicializar();
	if(v==1) cargarRelacionEnvio(0);
	if(v==2) cargarRelacionDevolucion(0);
	if(v==3) cargarRelacionVales(0);
	if(v==4) cargarRelacionValesDevolucion(0);
}

function cargarRelacionValesDevolucion(id){
	$('#div_unidades').show();
	$('#divdetalle').html('<h3 style="color:#FFF">Detalles de Vales</h3>');
	$('#divopvale').html('<span style="color:#FFF; font-size:11px">Vale</span>');
	$('#cmdagregar').attr('value', "Agregar Vale");
	$('#cmdeliminar').attr('value', "Eliminar Vale");
	
	
	$('#div_unidades').hide();
	
	//dwr.util.addOptions('cbodependencia2',{ 3:'Direccion de Finanzas' });
	
	if(id==0){
		inicializar();
		//cargar relacion de envio
		//ShowDelay('Cargando relacion de devoluci�n vales');
		controladorListadoOrdenPagoEjercidoRemoto.cargarRelaciones('VALES_DEVOLUCION', {
						callback:function(items) { 	
							$('#cborelacion').attr('disabled', false);
						 	dwr.util.addOptions('cborelacion',items,"ID_RELACION", "DESCRIPCION");
							$('#cmdnueva').attr('disabled', false);
							_closeDelay();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
	}
	else{
		limpiar();
		$('#cborelacion').val(id);
		//cargarRelacion();
	}
}

/*reinicia los valores de los controles*/
function inicializar(){
		$('#cborelacion').attr('disabled', true);
		dwr.util.removeAllOptions('cborelacion');
		dwr.util.addOptions('cborelacion',{ 0:'[Seleccione un listado]' });
		dwr.util.removeAllOptions('lstdetalles');
		$('#divfechaentrada').text('');
		$('#cmdmodificar').attr('disabled', true);
		$('#cmdagregar').attr('disabled', true);
		$('#cmdeliminar').attr('disabled', true);
		$('#cmdimprimir').attr('disabled', true);
		$('#cmdimprimirgeneral').attr('disabled', true);
		$('#cmdmodificarop').attr('disabled', true);
		$('#divcerrada').text("");
		$('#divdevuelta').text("");
		$('#hdcerrada').attr('value', '');
		$('#hddevuelta').attr('value', '');
		$('#txtnumop').attr('value', '');
		$('#txtnumop').attr('disabled', true);
		$('#txtarea').attr('disabled', true);
		$('#txtarea').attr('value', '');
		$('#hddetalle').attr('value', 0);
		$('#cmdmodificarop').attr('value', 'Modificar');
		$('#chkdevuelto').attr('disabled', true);
		$('#chkdevuelto').attr('checked', false);
		$('#cmdnueva').attr('disabled', true);
		limpiar();
}

function limpiar(){
		dwr.util.removeAllOptions('lstdetalles');
		//$('#txtfechaentrada').attr('disabled', true);
		$('#divfechaentrada').text('');
		$('#cmdagregar').attr('disabled', true);
		$('#cmdeliminar').attr('disabled', true);
		$('#cmdimprimir').attr('disabled', true);
		$('#cmdimprimirgeneral').attr('disabled', true);
		$('#cmdmodificar').attr('disabled', true);
		$('#divcerrada').text("");
		$('#divdevuelta').text("");
		$('#hdcerrada').attr('value', '');
		$('#hddevuelta').attr('value', '');
		$('#cmdabrir').attr('disabled', true);
		$('#txtnumop').attr('value', '');
		$('#txtnumop').attr('disabled', true);
		$('#txtarea').attr('value', '');
		$('#txtarea').attr('disabled', true);
		$('#cmdmodificarop').attr('disabled', true);
		$('#hddetalle').attr('value', 0);
		$('#cmdmodificarop').attr('value', 'Modificar');
		$('#chkdevuelto').attr('disabled', true);
		$('#chkdevuelto').attr('checked', false);
}

function cargarRelacion(){
	
	var id_relacion = $('#cborelacion').attr('value');
	var cont =0;
	
	limpiar();
	
	if(id_relacion==0) return false;
	
	//ShowDelay('Cargando informcion de la relacion');
	 controladorListadoOrdenPagoEjercidoRemoto.cargarRelacionesDocumentos(id_relacion, {
						callback:function(items) { 
						jQuery.each(items,function(i) {
							$('#cmdmodificar').attr('disabled', false);
							$('#cmdagregar').attr('disabled', false);
							$('#cmdeliminar').attr('disabled', false);
							$('#cmdimprimir').attr('disabled', false);
							$('#cmdimprimirgeneral').attr('disabled', false);
							$('#lstdetalles').attr('disabled', false);
							$('#cmdabrir').attr('disabled', false);
							$('#txtfechaentrada').attr('disabled', false);
							$('#txtnumop').attr('value', '');
							$('#txtnumop').attr('disabled', false);
							$('#txtarea').attr('disabled', false);
							$('#hddetalle').attr('value', '');
							if(cont==0){
								//muestra la informacion general
								
								$('#divfechaentrada').text(this.FECHA);
								$('#divcerrada').text((this.CERRADA=='S')? 'Si': 'No');
								$('#divdevuelta').text(getHTML((this.DEVUELTO=='S')? 'Si': 'No'));
								$('#hddevuelta').attr('value', this.DEVUELTO);
								$('#hdcerrada').attr('value', this.CERRADA);
								
								(this.CERRADA=='S') ? $('#cmdabrir').attr('value', 'Abrir relacion'): $('#cmdabrir').attr('value', 'Cerrar relacion');
								//Recupera la Unidad administrativa
								$('#cbodependencia2').val(this.ID_DEPENDENCIA_DEV);
								
								cont++;
							}
							var x = this.ID_DETALLE;
							//agrega los detalles al list
							//if(this.NUM_OP!=null) dwr.util.addOptions('lstdetalles',{ 0:this.NUM_OP});
       					 });	
						 
						 dwr.util.addOptions('lstdetalles',items,"ID_DETALLE", "NUM_OP");
						 
						 if(cont==0) { 
							$('#cmdimprimir').attr('disabled', true);
						 }
						 if($('#hdcerrada').attr('value')=='S'){
								$('#cmdagregar').attr('disabled', true);
								$('#cmdeliminar').attr('disabled', true);
								$('#txtnumop').attr('value', '');
								$('#txtnumop').attr('disabled', true);
								$('#txtarea').attr('value', '');
								$('#txtarea').attr('disabled', true);
						}
							_closeDelay();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
}

/*carga la relacion de vales*/
function cargarRelacionVales(id){
	$('#divdetalle').html('<h3 style="color:#FFF">Detalles de Vales</h3>');
	$('#divopvale').html('<span style="color:#FFF; font-size:11px">Vales</span>');
	$('#cmdagregar').attr('value', "Agregar Vale");
	$('#cmdeliminar').attr('value', "Eliminar Vale");
	if(id==0){
		inicializar();
		dwr.util.removeAllOptions('cborelacion');
		dwr.util.addOptions('cborelacion',{ 0:'[Seleccione un listado]'});
		//cargar relacion de envio
		//ShowDelay('Cargando relacion de vales');
		controladorListadoOrdenPagoEjercidoRemoto.cargarRelaciones('VALES', {
						callback:function(items) { 	
						
							$('#cborelacion').attr('disabled', false);
						 	dwr.util.addOptions('cborelacion',items,"ID_RELACION", "DESCRIPCION");
							$('#cmdnueva').attr('disabled', false);
							_closeDelay();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
		$('#cmdagregar').attr('disabled', false);
	}
	else{
		limpiar();
		dwr.util.removeAllOptions('cborelacion');
		dwr.util.addOptions('cborelacion',{ 0:'[Seleccione un listado]'});
		//ShowDelay('Cargando relacion de vales');
		controladorListadoOrdenPagoEjercidoRemoto.cargarRelaciones('VALES', {
						callback:function(items) { 	
						
							$('#cborelacion').attr('disabled', false);
						 	dwr.util.addOptions('cborelacion',items,"ID_RELACION", "DESCRIPCION");
							$('#cborelacion').val(id);
							cargarRelacion();
							_closeDelay();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
		$('#cmdagregar').attr('disabled', false);
	}
}

/*carga la relacion de devoluciones de ordenes de pago*/
function cargarRelacionDevolucion(id){
	$('#div_unidades').show();
	$('#divdetalle').html('<h3 style="color:#FFF">Detalles de Ordenes de Pago</h3>');
	$('#divopvale').html('<span style="color:#FFF; font-size:11px">Orden de Pago</span>');
	$('#cmdagregar').attr('value', "Agregar OP's");
	$('#cmdeliminar').attr('value', "Eliminar OP's");
	if(id==0){
		inicializar();
		dwr.util.removeAllOptions('cborelacion');
		dwr.util.addOptions('cborelacion',{ 0:'[Seleccione un listado]'});
		//cargar relacion de envio
		ShowDelay('Cargando relacion de devolucion');
		controladorListadoOrdenPagoEjercidoRemoto.cargarRelaciones('DEVOLUCION', {
						callback:function(items) { 	
							$('#cborelacion').attr('disabled', false);
						 	dwr.util.addOptions('cborelacion',items,"ID_RELACION", "DESCRIPCION");
							$('#cmdnueva').attr('disabled', false);
							_closeDelay();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
		$('#cmdagregar').attr('disabled', false);
	}
	else{
		dwr.util.removeAllOptions('cborelacion');
		dwr.util.addOptions('cborelacion',{ 0:'[Seleccione un listado]'});
		
		ShowDelay('Cargando relacion de devolucion');
		controladorListadoOrdenPagoEjercidoRemoto.cargarRelaciones('DEVOLUCION', {
						callback:function(items) { 	
							$('#cborelacion').attr('disabled', false);
						 	dwr.util.addOptions('cborelacion',items,"ID_RELACION", "DESCRIPCION");
							$('#cmdnueva').attr('disabled', false);
							_closeDelay();
							$('#cborelacion').val(id);
							cargarRelacion();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
		
		$('#cmdagregar').attr('disabled', false);
		//cargarRelacion();
	}
}

/*carga ka relacion de envios de ordenes de pago*/
function cargarRelacionEnvio(id){
	$('#divdetalle').html('<h3 style="color:#FFF">Detalles de Ordenes de Pago</h3>');
	$('#divopvale').html('<span style="color:#FFF; font-size:11px">Orden de Pago</span>');
	$('#cmdagregar').attr('value', "Agregar OP's");
	$('#cmdeliminar').attr('value', "Eliminar OP's");

	if(id==0){
		inicializar();
		dwr.util.removeAllOptions('cborelacion');
		dwr.util.addOptions('cborelacion',{ 0:'[Seleccione un listado]'});
		//cargar relacion de envio
		ShowDelay('Cargando relacion de envio');
		controladorListadoOrdenPagoEjercidoRemoto.cargarRelaciones('ENVIO', {
						callback:function(items) { 	
							$('#cborelacion').attr('disabled', false);
						 	dwr.util.addOptions('cborelacion',items,"ID_RELACION", "DESCRIPCION" );
							$('#cmdnueva').attr('disabled', false);
							_closeDelay();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
		$('#cborelacion').val(0);
		$('#cmdagregar').attr('disabled', false);
	}
	else{
		dwr.util.removeAllOptions('cborelacion');
		dwr.util.addOptions('cborelacion',{ 0:'[Seleccione un listado]'});
		ShowDelay('Cargando relacion de envio');
		controladorListadoOrdenPagoEjercidoRemoto.cargarRelaciones('ENVIO', {
						callback:function(items) { 	
							$('#cborelacion').attr('disabled', false);
						 	dwr.util.addOptions('cborelacion',items,"ID_RELACION", "DESCRIPCION" );
							$('#cmdnueva').attr('disabled', false);
							_closeDelay();
							$('#cborelacion').val(id);
							cargarRelacion();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
		});
		$('#cmdagregar').attr('disabled', false);
	}
}
//muestra los detalles de partidas de la orden de pago
function mostrarDetallesOP(cve_op){
	jWindow('<iframe width="750" height="500" name="consultaOP" id="consultaOP" frameborder="0" src="../../sam/consultas/muestra_detalles_Op.action?cve_op='+cve_op+'"></iframe>','Detalles de Orden de Pago', '','Cerrar ',1);
}

//Metodo para ejercer las ordenes de pago 
function ejercerOP()
{
	var now = new Date();

	var checkClaves = [];
	var bfecha = $('#chkfecha').attr('checked');
	var fecha_ejerce = $('#txtfecha').attr('value');
	//if(fecha_ejerce=="") fecha_ejerce = now.getDay()+"-"+(now.getMonth()+1)+"-"+now.getYear();
	
	//recuperar las claves a ejercer
     $('input[id=chkOP]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		 jConfirm('�Confirma que desea ejercer la(s) ordenes de pago seleccionada(s)?','Ejercer Orden(es) de Pago', function(r){
			if(r){
					ShowDelay('Ejerciendo Orden(es) de Pago','');
					controladorListadoOrdenPagoEjercidoRemoto.ejercerOrdenPago(checkClaves, bfecha, fecha_ejerce, {
						callback:function(items) { 		
						 	CloseDelay('Orden(es) de Pago Ejercida(s) con exito', 2000, function (){buscarOpMes();});
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
	    jAlert('Es necesario que seleccione por lo menos una Orden de Pago para realizar esta acci�n', 'Advertencia');
}

//funcion para cambiar la fecha de la orden de pago
function cambiarFecha(){
	var checkClaves = [];
     $('input[name=chkOP]:checked').each(function(){checkClaves.push($(this).val());});	
	 if(checkClaves.length<1) {jAlert('Seleccione por lo menos una Orden de Pago del listado','Advertencia'); return false;}
	var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
				  '<tr>'+
					'<td height="20" width="150">Formato de fecha:</td>'+
					'<td width="200">dd/mm/aaaa</td>'+
				  '</tr>'+
				  '<tr>'+
					'<td height="20">Fecha Nueva:</td>'+
					'<td><input type="text" id="txtfechanueva" value="" style="width:100px" /></td>'+
				  '</tr>'+
				  '<tr>'+
					'<td height="50" align="center" colspan="2"><input type="button" value="Cambiar fecha" id="cmdcambiarfecha" class="botones"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones"/></td>'+
				  '</tr>'+
				'</table>';
	jWindow(html,'Cambiar fecha en Ordenes de Pago', '','',0);
	$('#cmdcambiarfecha').click(function(event){cambiarFechaOP();})
	$('#cmdcancelar').click(function(event){cve_op=0;$.alerts._hide();})
	$('#txtfechanueva').keypress(function(event){if (event.keyCode == '13'){$('#cmdcambiarfecha').click();}});
}

function cambiarFechaOP(){
	 var checkClaves = [];
     $('input[name=chkOP]:checked').each(function(){checkClaves.push($(this).val());});	
	 
	var fechatemp = $('#txtfechanueva').attr('value');
	if(checkClaves.length<1) {jAlert('Seleccione por lo menos una Orden de Pago del listado','Advertencia'); return false;}
	if($('#txtfechanueva').attr('value')==''){jAlert('La fecha escrita no es v�lida', 'Advertencia', function(){cambiarFecha(fecha)}); return false;}
	jConfirm('�Confirma que desea cambiar la fecha y periodo de las Ordenes de Pago?','Cambiar fecha y periodo', function(r){
			if(r){
					ShowDelay('Cambiando fecha de Ordenes de Pago','');
					controladorListadoOrdenPagoEjercidoRemoto.cambiarFechaOrdenPago(checkClaves, fechatemp, {
						callback:function(items) { 
							if(items)
						 		CloseDelay('Fecha cambiada con exito a: '+fechatemp, 2000, function (){cve_op =0;setTimeout('buscarOpMes()',1000);});
							else 
								jError('No se pudo cambiar la fecha de las Ordenes de Pago, puede que la fecha especificada no sea una fecha v�lida, verifique nuevamente','Error');  
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
				    });
			}
	});
}

//funcion para bu{-}scar ordenes de pago segun criterio del mes
function buscarOpMes(){
	var s = "?por_ejercer="+$('#chk_ejercer').attr('checked')+"&ejercidas="+$('#chk_ejercercidas').attr('checked')+"&mes="+$('#cbomes').attr('value')+"&fecha_ejercer="+$('#chkfecha').attr('checked')+"&fecha="+$('#txtfecha').attr('value');
	document.location = s;
}

function mostrarOpcionPDF(cve_op){
	var html = '<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="405" >'+
				'  <tr id="x1" onmouseover="color_over(\'x1\')" onmouseout="color_out(\'x1\')"> '+
				'	<td width="33" height="27" align="center" style="cursor:pointer" onclick="getReporteOP('+cve_op+')"> '+
				'	  <img src="../../imagenes/pdf.gif"/></td>' +
				'	<td width="362" height="27" align="left" style="cursor:pointer" onclick="getReporteOP('+cve_op+')">&nbsp;Reporte Normal Orden de Pago</td> '+
				'  </tr> '+
				
				'  <tr id="x2" onmouseover="color_over(\'x2\')" onmouseout="color_out(\'x2\')" onclick=""> '+
				'	  <td height="27" align="center"  style="cursor:pointer" onclick="getAnexosListaOP('+cve_op+')"><img src="../../imagenes/report.png" /></td> '+
				'	  <td height="27" align="left" style="cursor:pointer" onclick="getAnexosListaOP('+cve_op+')">&nbsp;Listar Anexos de Orden de Pago</td> '+
				'	</tr> ';
			html+='</table>';
	jWindow(html,'Opciones de Reporte Orden de Pago', '','Cerrar',1);
}

function getAnexosListaOP(cve_op){
	jWindow('<iframe width="750" height="350" name="ventanaArchivosOP" id="ventanaArchivosOP" frameborder="0" src="../../sam/consultas/muestra_anexosOP.action?cve_op='+cve_op+'"></iframe>','Listado de Anexos de OP: '+cve_op, '','Cerrar',1);
}


function getReporteOP(clave) {
	$('#cve_op').attr('value',clave);
	$('#forma').attr('action',"../reportes/formato_orden_pago.action");
	$('#forma').attr('target',"impresion");
	$('#forma').submit();
}