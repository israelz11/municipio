$(document).ready(function() {  
		 $("#txtfecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
		 $('#btnBuscar').click(function(event){buscarOpMes();})
		 $('#cmdejercer').click(function(event){desejercerOP();});
		 $('#cmdCancelarEjercido').click(function(event){cancelarEjercido();});
});

function cancelarEjercido(cve_op)
{
	var now = new Date();
	var bfecha = $('#chkfecha').attr('checked');
	var fecha_ejerce = $('#txtfecha').attr('value');
	
	//recuperar las claves a ejercer
     	var cve_op = 0;
		$('input[id=chkOP]:checked').each(function() { cve_op = $(this).val();});	
		
	 if (cve_op!=0){
		 jWindow('¿Confirma que desea cancelar el ejercido de la orden de pago seleccionada?<br><br><strong>*Escriba el motivo de cancelación del ejercido: </strong><br><textarea id="txtmotivo" style="width:500px; height:100px"></textarea><br><br><div align="center"><input style="width:100px" class="botones" value="Aceptar" id="cmdaceptarBoton" type="button">&nbsp;<input style="width:100px" class="botones" value="cancelar" id="popup_cancel" type="button"></div>','Desejercer Orden(es) de Pago' ,"Aceptar", "Cancelar", 0);
		 $('#cmdaceptarBoton').click(function(event){_cancelarEjercido(cve_op);});
	 }
	 else 
	    jAlert('Es necesario que seleccione por lo menos una Orden de Pago para realizar esta acción', 'Advertencia');
}

function _cancelarEjercido(cve_op)
{
	var motivo = $('#txtmotivo').attr('value');
	if(motivo=='')
	{
		alert('Es necesario escribir el motivo de la cancelación del ejercido');
		return false;
	}
	
	ShowDelay('Cancelando ejercido de Orden(es) de Pago','');
	controladorListadoOrdenPagoDesejercerRemoto.cancelarEjercidoOrdenPago(cve_op, motivo,{
			callback:function(items) { 	
				CloseDelay('Ejecido de la(s) Orden(es) de Pago cancelado con éxito',2000, function(){buscarOpMes();			});	 	
		 } 					   				
		 ,
		 errorHandler:function(errorString, exception) { 
			jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
		 }
	});
}

//Metodo para ejercer las ordenes de pago 
function desejercerOP()
{
	var now = new Date();

	var checkClaves = [];
	var bfecha = $('#chkfecha').attr('checked');
	var fecha_ejerce = $('#txtfecha').attr('value');
	//if(fecha_ejerce=="") fecha_ejerce = now.getDay()+"-"+(now.getMonth()+1)+"-"+now.getYear();
	
	//recuperar las claves a ejercer
     $('input[id=chkOP]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		 jWindow('¿Confirma que desea desejercer la(s) ordenes de pago seleccionada(s)?<br><br><strong>*Escriba el motivo de desejercido: </strong><br><textarea id="txtmotivo" style="width:500px; height:100px"></textarea><br><br><div align="center"><input style="width:100px" class="botones" value="Aceptar" id="cmdaceptarBoton" type="button">&nbsp;<input style="width:100px" class="botones" value="cancelar" id="popup_cancel" type="button"></div>','Desejercer Orden(es) de Pago' ,"Aceptar", "Cancelar", 0);
		 $('#cmdaceptarBoton').click(function(event){_desejercer(checkClaves);});
	 }
	 else 
	    jAlert('Es necesario que seleccione por lo menos una Orden de Pago para realizar esta acción', 'Advertencia');
}

function _desejercer(LstCveOrdenPago)
{
	jAlert('Ya no se permiten desejercer Ordenes de Pago, consulte con su administrador del SAM','Advertencia');
	return false;
	var motivo = $('#txtmotivo').attr('value');
					if(motivo=='')
					{
						alert('Es necesario escribir el motivo de desejercido');
						return false;
					}
					
					ShowDelay('Desejerciendo Orden(es) de Pago','');
					controladorListadoOrdenPagoDesejercerRemoto.desejercerOrdenPago(LstCveOrdenPago, motivo, {
							callback:function(items) { 	
								CloseDelay('Orden(es) de Pago Desejercida(s)',2000, function(){buscarOpMes();});	 	
						 } 					   				
						 ,
						 errorHandler:function(errorString, exception) { 
							jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
						 }
				    });
					
			//}
}

//funcion para bu{-}scar ordenes de pago segun criterio del mes
function buscarOpMes(){
	var s = "?&mes="+$('#cbomes').attr('value');
	document.location = s;
}

function getReporteOP(clave) {
	$('#cve_op').attr('value',clave);
	$('#forma').attr('action',"../reportes/formato_orden_pago.action");
	$('#forma').attr('target',"impresion");
	$('#forma').submit();
}