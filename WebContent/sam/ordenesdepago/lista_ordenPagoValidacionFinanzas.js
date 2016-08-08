$(document).ready(function() {  
		 $("#txtfecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
		 $('#btnBuscar').click(function(event){buscarOpMes();})
		 $('#cmdvalidar').click(function(event){validarOP();});
		 $("#txtfechanueva").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
});

//Metodo para ejercer las ordenes de pago 
function validarOP()
{
	var now = new Date();
	var checkClaves = [];
	var fecha = $('#txtfechanueva').attr('value');
	//recuperar las claves a ejercer
     $('input[id=chkOP]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		 if(fecha=='') {jAlert('La fecha de validacion para la Orden de Pago no es válida', 'Advertencia'); return false;}
		 jConfirm('¿Confirma que desea aplicar fecha de validación a las ordenes de pago seleccionadas?','Confirmar', function(r){
			if(r){
					ShowDelay('Validando Orden(es) de Pago','');
					controladorListadoOrdenPagoEjercidoValidaFinanzasRemoto.fechaValidacionOrdenPago(checkClaves, fecha,{
						callback:function(items) { 	
							CloseDelay('Validacion Aplicada con éxito',2000, function(){setTimeout("buscarOpMes();",1000);});	 	
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
	    jAlert('Es necesario que seleccione por lo menos una Orden de Pago para realizar esta acción', 'Advertencia');
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