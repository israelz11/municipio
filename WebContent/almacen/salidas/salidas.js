// JavaScript Document
var cont=0;
var subtotal = 0;
$(document).ready(function() {  
var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	
	 $('#tabuladores').tabs();
	 $('#tabuladores').tabs('enable',0);
	 $('#cmdcerrar').click(function (event){cerrarSalida();});
  	 $('#cmdguardar').click(function(event){guardar();});
	 $("#txtfecha").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha, minDate: 0});  
	CargarDetalles($('#ID_SALIDA').attr('value'));
	if($('#cbotiposalida').val()=='1'){
		$('#cboiva').attr('disabled', true);
		$('#txtdescuento').attr('disabled', true);
		$('#txtiva').attr('disabled', true);
	}
	
});

function getTotales(){
	total = 0;
	//if($('#cbotiposalida').val()=='1') return false;
	if($('#cbotiposalida').val()=='2') subtotal=0;
	
	$("INPUT[@id='ID_DETALLE_SALIDA'][type='hidden']").each(function(){ 
		if(typeof($('#txt'+$(this).val()).attr('value'))!='undefined'){ 												  
			subtotal = subtotal + ($('#txt'+$(this).val()).attr('value')*$('#DET_PRECIO'+$(this).val()).attr('value'));
			$('#divcosto'+$(this).val()).text('$'+formatNumber($('#txtcantidad'+$(this).val()).attr('value')*$('#DET_PRECIO'+$(this).val()).attr('value')));		
		}
	});
			
	if(isNaN($('#txtdescuento').attr('value'))){$('#txtdescuento').attr('value', '0.00'); return false;}
	var descuento = $('#txtdescuento').attr('value');
	
	/*Aplicacion del Iva si lo requiere*/
	var iva = 0.0;
	if($('#cboiva').attr('value')==0) $('#txtiva').attr('value', '0');
	if($('#cboiva').attr('value')==1) {
			iva = redondeo(subtotal*0.16);
			$('#txtiva').attr('value', iva);
	}
	if($('#cboiva').attr('value')==2){
		iva = ($('#txtiva').attr('value')*1);
		$('#txtiva').attr('value', iva);
	}
	
	/*Aplicar descuento si es requerido*/
	if($('#txtdescuento').attr('value')=='') $('#txtdescuento').attr('value', '0');
	if($('#txtdescuento').attr('value')!=''){
		if((subtotal - descuento) < 0 ) {jAlert('El monto del descuento no puede ser mayor al subtotal, vuelva a escribirlo','Advertencia'); $('#txtdescuento').attr('value', ''); $('#txtdescuento').focus(); return false;}
	}
	
	total = (subtotal-descuento) + iva;
	
	$('#div_subtotal').html('<strong>'+formatNumber(subtotal, '$')+'</strong>');
	$('#div_total').html('<strong>'+formatNumber(redondeo(total),'$')+'</strong>');

}

function getTotalesMasIva(){
	var iva_temp = 0.00;
	if($('#cboiva').attr('value')==1||$('#cboiva').attr('value')==2) {
		iva_temp = (1* $('#txtiva').attr('value'));
	}
	total = (subtotal-descuento) + iva_temp;
	$('#div_total').html('<strong>'+formatNumber(redondeo(total),'$')+'</strong>');
}



function cerrarSalida(){
	if($('#ID_SALIDA').attr('value')==''||$('#ID_SALIDA').attr('value')=='0'){jAlert('No se puede crear la salida, guarde los cambios primero y vuelva a intentar esta operación','Advertencia'); return false;}
	jConfirm('¿Confirma que desea cerrar la Salida?','Cerrar', function(r){
				if(r){
					ShowDelay('Cerrando salida','');
					controladorSalidasRemoto.cerrarSalida($('#ID_SALIDA').attr('value'),{
						  callback:function(items){
							  			mostrarReporteSalida($('#ID_SALIDA').attr('value'));
										 CloseDelay('Salida cerrada con exito', function(){
													document.location = "lst_salidas.action";
											 });
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString,'Error');   
							return false;
						}
					});	
				}																			   
	});		
}

/*fucnion para eliminar conceptos*/
function eliminarConceptos(){
	var checkClaves = [];
     $('input[name=chkconcepto]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea eliminar los conceptos seleccionados?','Eliminar conceptos', function(r){
			if(r){
					 controladorSalidasRemoto.eliminarConceptos(checkClaves, {
						callback:function(items) { 	
						CargarDetalles($('#ID_SALIDA').attr('value'));
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
	    jAlert('Es necesario que seleccione por lo menos un concepto de listado', 'Advertencia');
}


function mostrarReporteSalida(idSalida){
	$('#ID_SALIDA').attr('value', idSalida);
	$('#forma').attr('action',"../reportes/salidas.action")
	$('#forma').attr("target","impresionSalidas");
	$('#forma').submit();
    $('#forma').attr("target","");
	$('#forma').attr('action',"salidas.action");
}

function guardarCantidadDetalles(){
	var exito = true;
	var ids = [];
	var cantidad = [];
	$('input[id=ID_DETALLE_SALIDA][type="hidden"]').each(function() { 
		if(parseFloat($('#txt'+$(this).val()).attr('value'))>parseFloat($('#CANTIDAD_DET'+$(this).val()).attr('value')) || parseFloat($('#txt'+$(this).val()).attr('value'))==0){
			alert('La cantidad establecida en alguno de los lotes es mayor a la existente en detalle de entrada');
			exito = false;
			return false;
		}
		else{
			if(typeof($('#txt'+$(this).val()).attr('value'))!='undefined'){
				ids.push($(this).val());
				cantidad.push(parseFloat($('#txt'+$(this).val()).attr('value')));
			}
		}
	});
	if(exito){
		ShowDelay('Guardando detalle de lotes','');
		var descuento = $('#txtdescuento').attr('value');
		var iva = $('#txtiva').attr('value');
		var tipoIva = $('#cboiva').val();
		controladorSalidasRemoto.guardarCantidadDetalles($('#ID_SALIDA').attr('value'),ids, cantidad,subtotal, descuento, iva, tipoIva,{
			  callback:function(items){
							 CloseDelay('Lotes guardados con éxito', function(){
										CargarDetalles($('#ID_SALIDA').attr('value'));
								 });
		} 					   				
		,
			errorHandler:function(errorString, exception) { 
				jError(errorString,'Error');   
				return false;
			}
		});	
	}
}

function guardar(){
	jConfirm('¿Confirma que desea guardar la Salida?','Guardar', function(r){
				if(r){
						var id_entrada = $('#ID_ENTRADA').attr('value');
						var id_salida = $('#ID_SALIDA').attr('value');
						var tipo_salida = $('#cbotiposalida').attr('value');
						if(id_salida=="") id_salida=0;
						if(id_entrada=="") id_entrada=0;
						var descuento = $('#txtdescuento').attr('value');
						var iva = $('#txtiva').attr('value');
						var tipoIva = $('#cboiva').val();
						
						  controladorSalidasRemoto.guardarSalida(id_salida, id_entrada, $('#txtfecha').attr('value'), $('#txtconcepto').attr('value'), tipo_salida, subtotal, descuento, iva, tipoIva,{
						  callback:function(items){
							  $('#ID_SALIDA').attr('value',items);
							  $('#div_folio').html('&nbsp;');
							  $('#div_folio').html(rellenaCeros(items.toString(),6));
							  $('#tabuladores').tabs('enable',1);
							   $('#tabuladores').tabs('option', 'selected', 1);
							  CargarDetalles(items);
							  CloseDelay('Salida guardada satisfactoriamente');	 
							  document.location ='salidas.action?id_salida='+items+"&id_entrada="+id_entrada; 
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

/*funcion para cargar los detalles del pedido*/
function CargarDetalles(id_salida)
{ 
	total =0;
	subtotal=0;
	cont=0;
	quitRow('listasConceptos');
	
	if(id_salida!=""&&id_salida!=0)
	{
		controladorSalidasRemoto.getConceptos(id_salida,{
				callback:function(items) { 				 
				jQuery.each(items,function(i){	
						cont++;
						if(this.CANTIDAD_ENT>0) 
							pintaTablaDetalles('listasConceptos', this);
						
					});
			    },
				errorHandler:function(errorString, exception){ 
							jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
							return false;
				}
		});
	}
}

function pintaTablaDetalles(table, obj){
	var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editarConcepto("+obj.ID_DETALLE_SALIDA+")\">";
	var htmlCheck = '<input type="checkbox" name="chkconcepto" id="chkconcepto" value="'+obj.ID_DETALLE_SALIDA+'">';
	var htmltxt = '';
	htmlEdit = "";
	if($('#cbotiposalida').val()=='2'){ //entrega parcial
		htmltxt = '<input type="text" name="txt'+obj.ID_DETALLE_SALIDA+'" id="txt'+obj.ID_DETALLE_SALIDA+'" value="'+((obj.CANTIDAD==0)? obj.CANTIDAD_ENT: obj.CANTIDAD)+'" style="width:80px; text-align:center" onKeyPress="return keyNumbero(event);" onBlur="getTotales()"> ' + 
		'<input type="hidden" id="ID_DETALLE_SALIDA" name="ID_DETALLE_SALIDA"  value="'+obj.ID_DETALLE_SALIDA+'"/>' +  
		'<input type="hidden" id="DET_PRECIO'+obj.ID_DETALLE_SALIDA+'" name="DET_PRECIO'+obj.ID_DETALLE_SALIDA+'"  value="'+obj.PRECIO+'"/>' +
		'<input type="hidden" id="CANTIDAD_DET'+obj.ID_DETALLE_SALIDA+'" name="CANTIDAD_DET'+obj.ID_DETALLE_SALIDA+'"  value="'+obj.CANTIDAD_ENT+'"/>';
		$('#tb_boton').show();
	}
	else{
			htmltxt = '&nbsp;'+obj.CANTIDAD;
			$('#tb_boton').hide();
	}
	if(obj.STATUS==0&&$('#cbotiposalida').val()!='2') {htmlEdit = "";htmlCheck="";}
	if(obj.ID_DETALLE_SALIDA!=null&&obj.ID_DETALLE_SALIDA!="") {
		$('#cmdguardardetalle').attr('disabled', true); 
		//$('#div_descuento').html(formatNumber(obj.DESCUENTO, '$'));
		
	/*}
	else
		{*/
		subtotal +=  obj.CANTIDAD * obj.PRECIO;
		$('#div_subtotal').html('<strong>'+formatNumber(subtotal, '$')+'</strong>');
		//$('#div_total').html('<strong>'+formatNumber(total, '$')+'</strong>');
	}
	appendNewRow(table, [Td('', centro , '', htmlCheck),
						 Td('', centro , '', htmltxt),
						 Td('', centro , '', '&nbsp;'+obj.CANTIDAD_ENT),
						 Td('', centro , '', '&nbsp;'+obj.UNIDMEDIDA),
						 Td('', izquierda , '', '&nbsp;'+obj.ARTICULO+"("+getHTML(obj.DESCRIPCION)+")"),
						 Td('', izquierda , '','&nbsp;'+getHTML(obj.FAMILIA)),
				 		 Td('', derecha , '', formatNumber(obj.PRECIO, '$')+"&nbsp;")
						 
				]);
	
	
	
}
