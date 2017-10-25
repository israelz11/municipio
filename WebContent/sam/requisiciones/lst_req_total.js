// JavaScript Document
var banStatus = false;

$(document).ready(function() {
	
	
	$('#cboFilterStatus').on('changed.bs.select', function (e) {
        

        var selected = $(this).find("option:selected").val();
        var StatusArray = ($(this).selectpicker('val') != null ? $(this).selectpicker('val').toString().split(',') : []);

        if (StatusArray.indexOf("9") != -1) {
            if (!banStatus) {
                $(this).find('option[value=9]').prop('selected', false).removeAttr('selected');
                $(this).selectpicker('refresh');
                banStatus = true;
            }
            else {
                $(this).selectpicker('deselectAll');
                $(this).find('option[value=9]').prop('selected', true);
                $(this).selectpicker('refresh');
                banStatus = false;
            }
        }
        else {
            if (StatusArray.indexOf("9") == -1) //No se encontro 0
            {
                $(this).find('option[value=9]').prop('selected', false).removeAttr('selected');
                $(this).selectpicker('refresh');
            }
            else //0 Encontrado
            {
                $(this).selectpicker('deselectAll');
                $(this).find('option[value=9]').prop('selected', true);
                $(this).selectpicker('refresh');
            }
        }
});

$('#cmdSeleccion').on('click', function (e) {;
if($('#cboFilterStatus').selectpicker('val')== null)
{
    alert('No se ha seleccionado ningun Estatus');
    return false;
}
    
alert('Los Estatus seleccionados son: ' + $('#cboFilterStatus').selectpicker('val').toString().split(','));
});    

$('#cmdClean').on('click', function(e){
$('#cboFilterStatus').selectpicker('deselectAll');
$('#cboFilterStatus').selectpicker('refresh');

});

//Checkbox para seleccionar toda la lista.... Abraham Gonzalez 12/07/2016
$("input[name=todos]").change(function(){
	$('input[type=chkrequisiciones]').each( function() {			
		if($("input[name=todos]:checked").length == 1){
			this.checked = true;
		} else {
			this.checked = false;
		}
	});
});

$('#todos').click( function (event){ $('input[name=chkrequisiciones]').attr('checked', this.checked); });


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
		    $('#fechaInicial').data("DateTimePicker").maxDate(e.date);
		});

	
    
	  //---------------------Boton para imprimir el listado de Requisiciones    28-08-17    ------------------------------------------------------------------------
	  $('#cmdpdf2').on('click', function() {
		  
		
		  swal({
			    title: 'Opciones de Reporte',
			    html:
			    	
			    	'<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="405" >'+
					'  <tr id="x1" onmouseover="color_over(\'x1\')" onmouseout="color_out(\'x1\')"> '+
					'	<td width="33" height="27" align="center" style="cursor:pointer" onclick="getListadoRequisiciones()"> '+
					'	  <img src="../../imagenes/pdf.gif"/></td>' +
					'	<td width="362" height="27" align="left" style="cursor:pointer" onclick="getListadoRequisiciones()">&nbsp;Listado de OS/OT/REQ Nomal en PDF</td> '+
					'  </tr> '+
					
					'  <tr id="x2" onmouseover="color_over(\'x2\')" onmouseout="color_out(\'x2\')" onclick=""> '+
					'	  <td height="27" align="center"  style="cursor:pointer" onclick="getListadoReqConOp()"><img src="../../imagenes/pdf.gif" /></td> '+
					'	  <td height="27" align="left" style="cursor:pointer" onclick="getListadoReqConOp()">&nbsp;Listado de OS/OT/REQ con Ordenes de Pago relacionadas en PDF</td> '+
					'	</tr> ' +
				'</table>'
			       
			   
			  })
	  });
	  
});

//IMPRIME EL LISTADO DE LAS REQUISICIONES
function mostrarOpcionPDF(){
	var html = '<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="405" >'+
				'  <tr id="x1" onmouseover="color_over(\'x1\')" onmouseout="color_out(\'x1\')"> '+
				'	<td width="33" height="27" align="center" style="cursor:pointer" onclick="getListadoRequisiciones()"> '+
				'	  <img src="../../imagenes/pdf.gif"/></td>' +
				'	<td width="362" height="27" align="left" style="cursor:pointer" onclick="getListadoRequisiciones()">&nbsp;Listado de OS/OT/REQ Nomal en PDF</td> '+
				'  </tr> '+
				
				'  <tr id="x2" onmouseover="color_over(\'x2\')" onmouseout="color_out(\'x2\')" onclick=""> '+
				'	  <td height="27" align="center"  style="cursor:pointer" onclick="getListadoReqConOp()"><img src="../../imagenes/pdf.gif" /></td> '+
				'	  <td height="27" align="left" style="cursor:pointer" onclick="getListadoReqConOp()">&nbsp;Listado de OS/OT/REQ con Ordenes de Pago relacionadas en PDF</td> '+
				'	</tr> ';
			if($('#REPORTE_ESPECIAL_2').attr('value')=='1'){
				html+=	'  <tr id="x3" onmouseover="color_over(\'x3\')" onmouseout="color_out(\'x3\')" onclick=""> '+
						'	  <td height="27" align="center"  style="cursor:pointer" onclick="getListadoRequisicionExcel()"><img src="../../imagenes/excel.png"height="18"  /></td> '+
						'	  <td height="27" align="left" style="cursor:pointer" onclick="getListadoRequisicionExcel()">&nbsp;Listado de Requisiciones con lotes relacionados en XLS</td> '+
						'	</tr> ';
			}
			html+='</table>';
	jWindow(html,'Opciones de Reporte', '','Cerrar',1);
}

//Checkbox para seleccionar toda la lista.... Abraham Gonzalez 12/07/2016
$("input[name=todos]").change(function(){
	$('input[type=chkrequisiciones]').each( function() {			
		if($("input[name=todos]:checked").length == 1){
			this.checked = true;
		} else {
			this.checked = false;
		}
	});
});

function reembolsos(cve_req, modulo){
	controladorListadoRequisicionesRemoto.getReembolsoRequisiciones(cve_req, {
						callback:function(items) { 		
							var html = '<table border="0" align="center" cellpadding="1" cellspacing="2" width="400">'+
										  '<tr>'+
											'<td width="100"><strong>Num. Req:</strong></td>'+
											'<td width="300"><strong>'+items.NUM_REQ+'</strong></td>'+
										  '</tr>'+
										  '<tr>'+
											'<td>&nbsp;</td>'+
											'<td><input type="radio" value="0" id="rdOpcion1" name="rdOpcion1"/>Reembolso Automatico</td>'+
										  '</tr>'+
										  '<tr>'+
											'<td>&nbsp;</td>'+
											'<td><input type="radio" value="1" id="rdOpcion2" name="rdOpcion2"/>Reembolso Personalizado</td>'+
										  '</tr>'+
										   '<tr>'+
											'<td><strong>Reembolso:</strong></td>'+
											'<td><input type="text" id="txtreembolso" value="'+items.REEMBOLSO_LIQ+'" style="width:140px" /></td>'+
										  '</tr>'+
										  '<tr><td><strong>Reembolsado:</strong></td><td>'+formatNumber(items.REEMBOLSO, '$')+'</td></tr>'+
										  '<tr>'+
											'<td>&nbsp;</td>'+
											'<td>&nbsp;</td>'+
										  '</tr>'+
										  '<tr>'+
											'<td colspan="2" align="center"><input type="button" value="Quitar" id="cmdquitar" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Aplicar" id="cmdaplicar" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones" style="width:100px"/></td>'+
										  '</tr>'+
										'</table>';
										
										jWindow(html,'Reembolso a Requisiciones', '','',0)
						
										$('#cmdquitar').click(function(event){_quitarReembolso(cve_req);});
										$('#cmdaplicar').click(function(event){_reembolsoRequisicion(cve_req);})
										$('#cmdcancelar').click(function(event){$.alerts._hide();})
										$('#txtreembolso').attr('disabled', true);
										$('#rdOpcion1').click(function(event){ 
											if ($(this).is(':checked'))
											{
												$('#txtreembolso').attr('disabled', true);
											}
											$('#rdOpcion2').attr('checked', false);
										});
										
										$('#rdOpcion2').click(function(event){ 
											if ($(this).is(':checked'))
											{
												$('#txtreembolso').attr('disabled', false);
												$('#txtreembolso').focus();
											}
											$('#rdOpcion1').attr('checked', false);
										});
										
										if(parseFloat(items.REEMBOLSO)>0)
										{
											$('#cmdaplicar').attr('disabled', true);
											$('#cmdquitar').attr('disabled', false);
										}
										else{
											$('#cmdaplicar').attr('disabled', false);
											$('#cmdquitar').attr('disabled', true);
										}
											
										$('#rdOpcion1').attr('checked', true);
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, "Error");          
					 }
	});
}


function _quitarReembolso(cve_ped){
	jConfirm('¿Confirma que desea quitar el reembolso de la Requisición?','Quitar Reembolso', function(r){
			if(r){
					ShowDelay("Quitando reembolso");
					controladorListadoRequisicionesRemoto.quitarReembolso(cve_ped, {
										callback:function(items){
											var cve = cve_ped;
												CloseDelay('Reembolso quitado con éxito');
										} 					   				
									 ,
									 errorHandler:function(errorString, exception) { 
										jError(errorString, "Error");          
									 }
					});
			}
	});
}

function _reembolsoRequisicion(cve_req){
	var operacion;
	if($('#rdOpcion1').is(':checked'))
	{
		operacion = 0;
	}
	if($('#rdOpcion2').is(':checked')){
		operacion = $('#txtreembolso').attr('value');
	}

	if(parseFloat($('#txtreembolso').attr('value'))==0.00){
		jAlert('No se puede aplicar un reeembolso valido a este documento'); return false;
	}
		jConfirm('¿Confirma que desea guardar el reembolso de la Requisición?','Guardar Reembolso', function(r){
				if(r){
						controladorListadoRequisicionesRemoto.guardarReembolsoRequisicion(cve_req, operacion, {
											callback:function(items){
													CloseDelay('Reembolso guardado con éxito');
											} 					   				
										 ,
										 errorHandler:function(errorString, exception) { 
											jError(errorString, "Error");          
										 }
						});
				}
		});
	
}

function getListadoRequisicionExcel(){
	var index=[];
	$("input[name=todos]:checked").each(function(){
		index.push($(this).val());
	});
	alert("demo " + index);
	$('#claveRequisicion').attr('value',claveReq);
	//if($('#txtlistado').attr('value')==''){jAlert('Es necesario agregar Requisiciones al listado para realizar esta operación','Advertencia'); return false;}
	$('#forma').attr('target',"impresionlistadoExcel");
	$('#forma').attr('action',"../reportes/rpt_listado_requisicionesExcel.xls");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_req_total.action");
}

function getListadoReqConOp(){
	$('#forma').attr('target',"impresionlistadoConOP");
	$('#forma').attr('action',"../reportes/rpt_listado_requisiciones.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_req_total.action");
}

//------------------------------------Corregir para imprimir seleccionadas desde los check
function agregarReqLista(){
	var checkClaves = [];
    $('input[name=chkrequisiciones]:checked').each(function() { checkClaves.push($(this).val());});	
	if (checkClaves.length>0){
		for(items in checkClaves){
			if($('#txtlistado').attr('value')=='')
				$('#txtlistado').attr('value', $('#NUM_REQ'+checkClaves[items]).attr('value'));
			else
				$('#txtlistado').attr('value', $('#txtlistado').attr('value')+', '+$('#NUM_REQ'+checkClaves[items]).attr('value'));
		}
		CloseDelay('('+checkClaves.length+ ') Requisiciones agregadas en listado');
		$("input[id=chkrequisiciones]:checked").attr('checked', false);
	}
	else 
			jAlert('Es necesario seleccionar por lo menos una Requisicion del listado', 'Advertencia');
}

function cancelarRequisicion(idReq){
		var checkClaves = [];
		checkClaves.push(idReq);	
		if (checkClaves.length>0){
			jConfirm('¿Confirma que desea cancelar la Requisición?','Confirmar', function(r){
				if(r){
						ShowDelay('Cancelando Requisición');
						 controladorListadoRequisicionesRemoto.cancelarRequisiciones(idReq, {
							callback:function(items) {
							  if(items==""){ 		
								  CloseDelay('Requisicion(es) cancelada(s) con exito', function(){getListaReq();} );
							  }
								else
									jError(items, 'Error al cancelar Requisiciones');
						 } 					   				
						 ,
						 errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');          
						 }
						});
				}
		   },async=false );	 
		  } 
		else 
			jAlert('Es necesario seleccionar por lo menos una Requisicion del listado', 'Advertencia');

}

/***********************************Metodo para cancelar las requisiciones***********************************************************************/
function cancelacionMultiple(){
	var checkClaves = [];
    $('input[name=chkrequisiciones]:checked').each(function() { checkClaves.push($(this).val());});	
	if (checkClaves.length>0){
		swal({
			  title: 'Estas seguro?',
			  text: "El cambio no podra revertirse!",
			  type: 'warning',
			  showCancelButton: true,
			  confirmButtonColor: '#3085d6',
			  cancelButtonColor: '#d33',
			  confirmButtonText: 'Si, Cancelar!',
			  cancelButtonText: 'No, Abortar!',
			  confirmButtonClass: 'btn btn-success',
			  cancelButtonClass: 'btn btn-danger',
			  buttonsStyling: false
			}).then(function () {
			  swal(
			    'Cancelado!',
			    'Tu requiscion ha sido cancelada con exito.',
			    'success',
			    controladorListadoRequisicionesRemoto.cancelarRequisiciones(checkClaves),
			    getListaReq()
			  )
			}, function (dismiss) {
			  // dismiss can be 'cancel', 'overlay',
			  // 'close', and 'timer'
			  if (dismiss === 'cancel') {
			    swal(
			      'Abortado',
			      'Tu requisicion no fue modificada :)',
			      'error'
			    )
			  }
			})
	}
	else 
		swal({
			  title: 'Error!',
			  text: 'Es necesario seleccionar por lo menos una Requisicion del listado.',
			  type: 'info',
			  timer: 2000
			}).then(
			  function () {},
			  // handling the promise rejection
			  function (dismiss) {
			    if (dismiss === 'timer') {
			      console.log('I was closed by the timer')
			    }
			  }
			)
}

/***********************************Metodo para aperturar las requisiciones***********************************************************************/

function aperturarRequisiciones(){
	 var checkClaves = [];
     $('input[name=chkrequisiciones]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		 swal({
			  title: 'Estas seguro?',
			  text: "¿Confirma que desea aparturar la(s) requisicion(es) seleccionada(s)?",
			  type: 'warning',
			  showCancelButton: true,
			  confirmButtonColor: '#3085d6',
			  cancelButtonColor: '#d33',
			  confirmButtonText: 'Si, aperturar!',
			  cancelButtonText: 'No, cancelar!',
			  confirmButtonClass: 'btn btn-success',
			  cancelButtonClass: 'btn btn-danger',
			  buttonsStyling: false
			}).then(function () {
			  swal(
				  	'Apertura!!',
				    'La(s) requisicion(es) seleccionada(s) se aperturaron.',
				    'success',
				    controladorListadoRequisicionesRemoto.aperturarRequisiciones(checkClaves),
				    getListaReq()
			  )
			}, function (dismiss) {
				if (dismiss === 'cancel') {
				    swal(
					      'Proceso Cancelado',
					      'Tu requisicion no fue apertura',
					      'error'
				    )
				}
			})
	 } 
	else
		swal({
			  title: 'Error!',
			  text: 'Es necesario seleccionar por lo menos una Requisicion del listado.',
			  type: 'info',
			  timer: 2000
			}).then(
			  function () {},
			  // handling the promise rejection
			  function (dismiss) {
			    if (dismiss === 'timer') {
			      console.log('I was closed by the timer')
			    }
			  }
			)
		
}


//------------------------Filtros del listado de la requisicion para buscar desdel el btnbuscar 26-07-17 -------------------------------------------------------
function getListaReq(){
	
	 var error="";
	 var titulo ="Error de validacion";
	
	 $('#cboFilterStatus').selectpicker('val');
	 $('#cboSearch').change('val');
	 //alert("Selecccion: "+ $('#cboSearch').val());
	 //alert("Desde: " + $('#fechaInicial').val() + "  hasta:   " +$('#fechaFinal').val());
	 if ($('#fechaInicial').attr('value')=="" && $('#fechaFinal').attr('value')!="" || $('#fechaInicial').attr('value')!="" && $('#fechaFinal').attr('value')=="")  error+="El rango de fechas no es valido<br>";
	
     if (error=="")
    	 $("#forma").submit();
	 else
		 jAlert(error,titulo);
     
 	 $(":text").val("");

}

function getRequisicion(claveReq)   {
	$('#claveRequisicion').attr('value',claveReq);
	$('#forma').attr('target',"impresion");
	$('#forma').attr('action',"../reportes/requisicion.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_req_total.action");
}

function getConsultaRequisicion(claveReq)   {
	_closeDelay();
	$('#claveRequisicion').attr('value',claveReq);
	$('#forma').attr('target',"impresionConsulta");
	$('#forma').attr('action',"../reportes/rpt_InformeRequisicion.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_req_total.action");
}

//PARAMETRO QUE LLAMA EL REPORTE DEL LISTADO DE LAS REQUISICIONES.................
function getListadoRequisiciones()   {
	
$('#forma').attr('target',"impresionlistado");
$('#forma').attr('action',"../reportes/rpt_listado_requisiciones.action");
$('#forma').submit();
$('#forma').attr('target',"");
$('#forma').attr('action',"lst_req_total.action");
}

function editarRequisicion(cve_req, status){

	if (status==0) document.location = 'capturarRequisicion.action?cve_req='+cve_req;
	if(status==1||status==2||status==5) getConsultaRequisicion(cve_req); //document.location = 'consultaRequisicion.action?cve_req='+cve_req+"&accion=0";
}

function consultarRequisicion(cve_req){
	document.location = 'consultaRequisicion.action?cve_req='+cve_req+'&accion=0';
}

