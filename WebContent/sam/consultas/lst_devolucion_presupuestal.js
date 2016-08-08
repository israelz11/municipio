/**
Descripcion: Codigo manejador para lst_devolucion_presupuestal.jsp
Autor      : Israel de la Cruz
Fecha      : 17/02/2012
*/
var guardado = false;
$(document).ready(function() { 
	$('#btnBuscar').click(function (event){buscarDevoluciones();});
	$('#cmdnuevo').click(function(event){nuevoEditarDevolucion(0);});;
	$('#cmdaplicar').click(function(event){aplicarDevoluciones();});
	$('#cmdaperturar').click(function(event){aperturarDevoluciones();});
	$('#cmddesaplicar').click(function(event){desaplicarDevolucion();});
	//$("#txtfecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
});

function getReporteDevolucion(idDevolucion) {
	$('#idDevolucion').attr('value',idDevolucion);
	$('#forma').attr('target',"impresion_devolucion");
	$('#forma').attr("action", "../reportes/rpt_devolucion_presupuestal.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_devolucion_presupuestal.action");
}


function compruebaVariable(){
	if(guardado) 
		buscarDevoluciones();
	else
		_closeDelay();
}

function cambiarVariable(numero){
	guardado = true;
	$('#txtnumero').attr('value', numero);
}

function buscarDevoluciones(){
	var checkStatus = [];
    $('input[name=estatus]:checked').each(function() {checkStatus.push($(this).val());});
	if (checkStatus.length==0 )  {jAlert('Debe de seleccionar un estatus de devolución', 'Advertencia'); return false;}
	
	$('#forma').attr('action', 'lst_devolucion_presupuestal.action');
	$('#forma').submit();
}

function nuevoEditarDevolucion(idDevolucion){
	var titulo = (idDevolucion==0) ? "Nueva devolucion presupuestal": "Editar devolución presupuestal";
	document.location='../../sam/consultas/devolucion_presupuestal.action?idDevolucion='+idDevolucion;
	//jWindow('<iframe width="800" height="500" name="DEV_PRESUPUEST" id="DEV_PRESUPUEST" frameborder="0" src="../../sam/consultas/devolucion_presupuestal.action?idDevolucion='+idDevolucion+'"></iframe>', titulo, '','',0, function(){compruebaVariable();});
}

function cancelarDevoluciones(idDevolucion){
	jConfirm('¿Confirma que desea cancelar la devolución presupuestal actual?','Confirmar', function(r){
				if(r){
						ShowDelay('Cancelando devolución','');
						ControladorListadoDevolucionPresupuestalRemoto.cancelarDevolucion(idDevolucion, {
							callback:function(items) {
							CloseDelay('Devolucion cerrada con exito');	
							buscarDevoluciones();
							
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
						}
					});
				}
			
	   });
}

function aplicarDevoluciones(){
	 var chkdevolucion = [];
     $('input[name=chkdevolucion]:checked').each(function() { chkdevolucion.push($(this).val());});	
	 if (chkdevolucion.length>0){
		 jConfirm('¿Confirma que desea aplicar las devoluciones seleccionadas?','Confirmar', function(r){
				if(r){
						ShowDelay('Aplicando devoluciones','');
						ControladorListadoDevolucionPresupuestalRemoto.aplicarDevolucion(chkdevolucion, {
							callback:function(items) {
								if(items==''){
									CloseDelay('Devoluciones aplicadas con éxito');	
									buscarDevoluciones();
								}
								else
									jError(items, 'Error');
							} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString,'Error');          
						}
					});
				}
	   });
	 }
	 else 
	    jAlert('Es necesario que seleccione por lo menos una devolución del listado', 'Advertencia');
}

function aperturarDevoluciones(){
	 var chkdevolucion = [];
     $('input[name=chkdevolucion]:checked').each(function() { chkdevolucion.push($(this).val());});	
	 if (chkdevolucion.length>0){
		 jConfirm('¿Confirma que desea aperturar las devoluciones seleccionadas?','Confirmar', function(r){
				if(r){
						ShowDelay('Aperturando devoluciones','');
						ControladorListadoDevolucionPresupuestalRemoto.aperturarDevolucion(chkdevolucion, {
							callback:function(items) {
								if(items==''){
									CloseDelay('Devoluciones aperturadas con éxito');	
									buscarDevoluciones();
								}
								else
									jError(items, 'Error');
							} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString,'Error');          
						}
					});
				}
	   });
	 }
	 else 
	    jAlert('Es necesario que seleccione por lo menos una devolución del listado', 'Advertencia');
}

function desaplicarDevolucion(){
	var chkdevolucion = [];
     $('input[name=chkdevolucion]:checked').each(function() { chkdevolucion.push($(this).val());});	
	 if (chkdevolucion.length>0){
		 jConfirm('¿Confirma que desea desplicar las devoluciones seleccionadas?','Confirmar', function(r){
				if(r){
						ShowDelay('Desaplicar devoluciones','');
						ControladorListadoDevolucionPresupuestalRemoto.desaplicarDevolucion(chkdevolucion, {
							callback:function(items) {
								if(items==''){
									CloseDelay('Devoluciones desaplicadas con éxito');
									buscarDevoluciones();	
								}
								else
									jError(items, 'Error');
							} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString,'Error');          
						}
					});
				}
	   });
	 }
	 else 
	    jAlert('Es necesario que seleccione por lo menos una devolución del listado', 'Advertencia');
}
