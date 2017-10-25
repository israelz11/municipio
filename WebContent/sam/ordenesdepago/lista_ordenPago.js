$(document).ready(function() {
	
  var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	

  $('#ui-datepicker-div').hide();
  /*Inicializacion de botones*/

  $('#cmdcancelarm').on('click', function(){
		cancelacionMultiple();
	});
  $('#cmdaperturar2').on('click', function(){
		aperturarOrden();
	});
  $('#btnBuscar').on('click', function(){
	  getOrden();
	});
  $('#cmdpdf').on('click', function(){
	  getListadoOrdenPago();
	});
  
//Checkbox para seleccionar toda la lista.... 
  $("input[name=todos]").change(function(){
  	$('input[type=chkordenes]').each( function() {			
  		if($("input[name=todos]:checked").length == 1){
  			this.checked = true;
  		} else {
  			this.checked = false;
  		}
  	});
  });
  //Para seleccionar todos los checkbox Abraham Gonzalez 12/07/2016
  $('#todos').click( function (event){ $('input[name=chkordenes]').prop('checked', this.checked); });
  
//-----------Revision del filtrado por fechas en el listado de requisiciones.............
	
	
	$('#fechaInicial').datetimepicker({
		format: 'DD/MM/YYYY'
		
	});
		
	$('#fechaFinal').datetimepicker({
		format: 'DD/MM/YYYY',
	    useCurrent: false //Important! See issue #1075
	});
	$("#fechaInicial").on("dp.change", function (e) {
	    $('#fechaFinal').data("DateTimePicker").minDate(e.date);
	});
	$("#fechaFinal").on("dp.change", function (e) {
	    $('#fechaFinal').data("DateTimePicker").maxDate(e.date);
	});

//Demo se actualizo...
});


function mostrarCargarArchivosOrdenPago(cve_op, num_op){
	//jWindow('',', '','Cerrar',1);
	swal({
		  title: 'Anexos de Orden de pago',
		  text: 'Archivos de Orden de Pago: '+num_op,
		  html:
			  '<iframe width="750" height="350" name="ventanaArchivosOP" id="ventanaArchivosOP" frameborder="0" src="../../sam/ordenesdepago/muestra_anexosOPArchivos.action?cve_op='+cve_op+'"></iframe>',
		  width: 800,
		  padding: 10,
		  animation: false
		})
}

function mostrarOpcionPDF(cve_op){

	swal({
		  title: 'Opciones de Reporte Orden de Pago',
		  type: 'info',
		  html:
			  '<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="405" >'+
				'  <tr id="x1" onmouseover="color_over(\'x1\')" onmouseout="color_out(\'x1\')"> '+
				'	<td width="33" height="27" align="center" style="cursor:pointer" onclick="getReporteOP('+cve_op+')"> '+
				'	  <img src="../../imagenes/pdf.gif"/></td>' +
				'	<td width="362" height="27" align="left" style="cursor:pointer" onclick="getReporteOP('+cve_op+')">&nbsp;Reporte Normal Orden de Pago</td> '+
				'  </tr> '+
				
				'  <tr id="x2" onmouseover="color_over(\'x2\')" onmouseout="color_out(\'x2\')" onclick=""> '+
				'	  <td height="27" align="center"  style="cursor:pointer" onclick="getAnexosListaOP('+cve_op+')"><img src="../../imagenes/report.png" /></td> '+
				'	  <td height="27" align="left" style="cursor:pointer" onclick="getAnexosListaOP('+cve_op+')">&nbsp;Listar Anexos de Orden de Pago</td> '+
				'	</tr> ' +
				'</table>', 
		  showCloseButton: true,
		  showCancelButton: true,
		  focusConfirm: false,
		  
		})
	
}

function getAnexosListaOP(cve_op){
	//jWindow('<iframe width="750" height="350" name="ventanaArchivosOP" id="ventanaArchivosOP" frameborder="0" src="../../sam/consultas/muestra_anexosOP.action?cve_op='+cve_op+'"></iframe>','Listado de Anexos de OP: '+cve_op, '','Cerrar',1);
	swal({
		  title: 'ventanaArchivosOP',
		  text: 'Listado de Anexos de OP: '+cve_op,
		  html:
			  '<iframe width="750" height="350" name="ventanaArchivosOP" id="ventanaArchivosOP" frameborder="0" src="../../sam/consultas/muestra_anexosOP.action?cve_op='+cve_op+'"></iframe>',
		  width: 800,
		  padding: 10,
		  animation: false
		})
}

function getListadoOrdenPago(){
	var checkStatus = [];
     $('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});	 
	 if (checkStatus.length==0 )   {jAlert('Es necesario seleccionar al menos un status de Orden de Pago', 'Advertencia'); return false;}
	 
	$('#forma').attr('target',"impresionlistado");
	$('#forma').attr('action',"../reportes/rpt_listado_op.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lista_ordenPago.action");

}

function getOrden(){
	var checkStatus = [];
     $('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});	 
	 if (checkStatus.length==0 )   {
		 swal({
			  title: 'Error!',
			  text: 'Es necesario seleccionar al menos un status',
			  timer: 2000,
			  showConfirmButton:false,
			  onOpen: function () {
			    swal.showLoading()
			  }
			}).then(
			  function () {},
			  // handling the promise rejection
			  function (dismiss) {
			    if (dismiss === 'timer') {
			      console.log('Tiempo caducado')
			    }
			  }
			)
		 return false;}
	 
	 if ($('#fechaInicial').attr('value')=="" && $('#fechaFinal').attr('value')!="" || $('#fechaInicial').attr('value')!="" && $('#fechaFinal').attr('value')=="")  {
		 swal('El rango de fechas no es válido'); 
		 return false;}
	 swal({
		  title: 'Buscando',
		  text: 'En listado de ordenes de pago',
		  timer: 2000,
		  onOpen: function () {
		    swal.showLoading()
		  }
		}).then(
		  function () {},
		  // handling the promise rejection
		
		  function (dismiss) {
		    if (dismiss === 'timer') {
		      console.log('I was closed by the timer')
		      var s = 'lista_ordenPago.action?idUnidad='+$('#cbodependencia').attr('value')+"&fechaInicial="+$('#fechaInicial').attr('value')+"&fechaFinal="+$('#fechaFinal').attr('value')+"&status="+checkStatus+"&tipo_gto="+$('#cbotipogasto').val();
		      $("#forma").submit()
		    }
		  }
		)
	;
}
/*--------------------------------- Manda a cargar la op desde el listado de op -------------------------------------*/
function editarOP(cve_op){
		
	//document.location = 'orden_pago.action?cve_op='+ cve_op + '&accion=edit';
	//if (status==-1)document.location = "orden_pago.action?cve_op="+cve_op+"&accion=edit";
	//if(status==0||status==4||status==6) getConsultaOrdenPago(cve_op);
	swal({
		  title: 'Abriendo Orden de Pago...',
		  text: 'La Orden de pago para editar es: ' +cve_op,
		  timer: 2000,
		  onOpen: function () {
		    swal.showLoading()
		  }
		}).then(
		  function () {},
		  // handling the promise rejection
		  function (dismiss) {
		    if (dismiss === 'timer') {
		      console.log('I was closed by the timer')
		      document.location = 'orden_pago.action?cve_op='+ cve_op + '&accion=edit';
		    }
		  }
		)
}

function getReporteOP(clave) {
	//_closeDelay();
	$('#cve_op').val(clave);
	$('#forma').prop('action',"../reportes/formato_orden_pago.action");
	$('#forma').prop('target',"impresion");
	$('#forma').submit();
	$('#forma').prop('target',"");
	$('#forma').prop('action',"lista_ordenPago.action");
}

function aperturarOrden(){
	 var checkClaves = [];
     $('input[name=chkordenes]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		 /*
		jConfirm('¿Confirma que desea aperturar las Ordenes de Pago seleccionadas?','Confirmar', function(r){
			if(r){
					 swal.showLoading();
					 controladorOrdenPagoRemoto.aperturarOrdenes(checkClaves, {
						callback:function(items) { 		
						  CloseDelay('Ordenes de Pago aperturadas con éxito', 2000, function(){
							  		 getOrden();
							  });
						  
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						swal(errorString, 'Error');          
					 }
				    });
			}
	   },async=false );*/
		 swal({
			  title: 'Apertura de Orden de Pago',
			 
			  showCancelButton: true,
			  confirmButtonText: 'Aperturar',
			  showLoaderOnConfirm: true,
			  preConfirm: function (r) {
			    return new Promise(function (resolve, reject) {
			      setTimeout(function() {
			    	  if(r){
							 swal.showLoading();
							 controladorOrdenPagoRemoto.aperturarOrdenes(checkClaves, {
								callback:function(items) { 
								swal.disableLoading()
								  CloseDelay('Ordenes de Pago aperturadas con éxito', 2000, function(){
									  		 getOrden();
									  });
								  
							 } 					   				
							 ,
							 errorHandler:function(errorString, exception) { 
								swal(errorString, 'Error');          
							 }
						    });
					}
			    else {
			          resolve()
			        }
			      }, 2000)
			    })
			  },
			  allowOutsideClick: false
			 
			}).then(function (r) {
			  swal({
			    type: 'success',
			    title: 'Orden de pago aperturada con éxito...!',
			    
			  })
			})
	 
	 } 
	 swal('Es necesario que seleccione por lo menos una Orden de Pago del listado', 'Advertencia');
}




/*------------------------------ Cancelacion de ordenes de pago ----------------------------------------------------------------------------------------------------*/
function cancelarOrden(idOrden)
{
	var checkClaves = [];
	checkClaves.push(idOrden);
	if (idOrden!=0){
		
		/*Inicia el ciclo*/
		swal({
			  title: 'Cancelar Orden(es) de Pago',
			  text: 'Indique el motivo de cancelación',
			  input: 'textarea',
			  inputPlaceholder: 'Motivo de cancelación',
			  showCancelButton: true,
			  inputValidator: function (value) {
			    return new Promise(function (resolve, reject) {
			      if (value) {
			        resolve()
			        
			      } else {
			      		reject('Debe escribir un motivo de cancelación')
			      	
				  }
			    })
			  }
			}).then(function (text) {	
				 motivo=text;
				 /*Inicia*/
				 swal({
					  title: 'Estas seguro?',
					  text: "¿Confirma que desea cancelar la orden de pago?",
					  type: 'warning',
					  showCancelButton: true,
					  confirmButtonColor: '#3085d6',
					  cancelButtonColor: '#d33',
					  confirmButtonText: 'Sí, confirmar!',
					  cancelButtonText: 'No, cancelar!',
					  confirmButtonClass: 'btn btn-success',
					  cancelButtonClass: 'btn btn-danger',
					  buttonsStyling: false
					}).then(function (r) {
					  swal('Cancelado!','Tu documento fue cancelado con éxito!','success')
					  /*clase para cencelacion*/
							  if(r){
									swal.showLoading();
									controladorOrdenPagoRemoto.cancelarOrden(checkClaves, motivo, {
										callback:function(items) { 
											getOrden();
									  
									 } 					   				
									,
									errorHandler:function(errorString, exception) { 
										
										swal('Oops...',errorString,'error');
									}
								},async=false ); 
							
							}
					  /*cancelacion cirre*/
					}, function (dismiss) {
					  // dismiss can be 'cancel', 'overlay',
					  // 'close', and 'timer'
					  if (dismiss === 'cancel') {
					    swal(
					      'Cancelado',
					      'El proceso no fue ejecutado',
					      'error'
					    )
					  }
					})
				 /*Hasta aqui*/
			})
		/*Termina ciclo de cancelacion*/
	}
	else
		swal('Es necesario que seleccione por lo menos una Orden de Pago para realizar esta acción','warning');
}

function cancelacionMultiple(){
	var checkClaves = [];
	
	$('input[name=chkordenes]:checked').each(function() { checkClaves.push($(this).val());});	

	if (checkClaves.length>0){

		swal({
			  title: 'Cancelar Orden(es) de Pago',
			  text: 'Indique el motivo de cancelación',
			  input: 'textarea',
			  inputPlaceholder: 'Motivo de cancelación',
			  showCancelButton: true,
			  inputValidator: function (value) {
			    return new Promise(function (resolve, reject) {
			      if (value) {
			        resolve()
			        
			      } else {
			      		reject('Debe escribir un motivo de cancelación')
			      	
				  }
			    })
			  }
			}).then(function (text) {	
				 motivo=text;
				 /*Inicia*/
				 swal({
					  title: 'Estas seguro?',
					  text: "¿Confirma que desea cancelar la(s) ordenes de pago seleccionada(s)?",
					  type: 'warning',
					  showCancelButton: true,
					  confirmButtonColor: '#3085d6',
					  cancelButtonColor: '#d33',
					  confirmButtonText: 'Sí, confirmar!',
					  cancelButtonText: 'No, cancelar!',
					  confirmButtonClass: 'btn btn-success',
					  cancelButtonClass: 'btn btn-danger',
					  buttonsStyling: false
					}).then(function (r) {
					  swal('Cancelado!','Tu documento fue cancelado con éxito!','success')
					  /*clase para cencelacion*/
							  if(r){
									swal.showLoading();
									controladorOrdenPagoRemoto.cancelarOrden(checkClaves,motivo, {
									callback:function(items) { 	
										getOrden();
									} 					   				
									,
									errorHandler:function(errorString, exception) { 
										
										swal('Oops...',errorString,'error');
									}
								},async=false ); 
							
							}
					  /*cancelacion cirre*/
					}, function (dismiss) {
					  // dismiss can be 'cancel', 'overlay',
					  // 'close', and 'timer'
					  if (dismiss === 'cancel') {
					    swal(
					      'Cancelado',
					      'El proceso no fue ejecutado',
					      'error'
					    )
					  }
					})
				 /*Hasta aqui*/
			})
			
	}//cierre del if principal
	else
		swal('Es necesario que seleccione por lo menos una Orden de Pago para realizar esta acción','warning');
}
