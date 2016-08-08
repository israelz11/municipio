$(document).ready(function() {
  var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	
 
  $('#cmdbuscar').click(function(event){$('#frm').submit();});
  $("#txtfechaInicial").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha});  
  $("#txtfechaFinal").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha}); 
  $('#cbodependencia').change(function (event){cargarMinutarios();});
  $('#cmdAperturar').click(function(event){aperturarMinutario();});

  getPersonasSubdireccion('txtpersonaDestino','ID_PERSONA_DESTINO');
  $('#ui-datepicker-div').hide();
});

function reactivarMinutario(id){
	jConfirm('¿Confirma que desea reactivar el minutario?','Reactivar', function(r){
		if(r){
				ShowDelay("Reactivando minutario");
				ControladorListadoMinutariosRemoto.reactivarMinutario(id,{
						callback:function() {
							CloseDelay("Minutario reactivado con éxito", function(){$('#cmdbuscar').click();});
						},
						errorHandler:function(errorString, exception) { 
												jError(errorString, 'Error');          
						}
				});
		}
	});
}

function aperturarMinutario(){
	var checkClaves = [];
     $('input[name=chkMinutario]:checked').each(function() { checkClaves.push($(this).val());});	
	 if(checkClaves.length==0) {jAlert('Es necesario seleccionar por lo menos un minutarios del listado', 'Advertencia'); return false;}
	jConfirm('¿Confirma que desea aperturar los minutarios?','Aperturar', function(r){
		if(r){
				ShowDelay("Aperturando minutarios");
				ControladorListadoMinutariosRemoto.aperturarMinutario(checkClaves,{
						callback:function() {
							CloseDelay("Minutario(s) aperturado(s) con éxito", function(){$('#cmdbuscar').click();});
						},
						errorHandler:function(errorString, exception) { 
												jError(errorString, 'Error');          
						}
				});
		}
	});
}

function cancelarMinutario(id){
	jConfirm('¿Confirma que desea cancelar el minutario?','Cancelar', function(r){
		if(r){
				ShowDelay("Cancelando minutario");
				ControladorListadoMinutariosRemoto.cancelarMinutario(id,{
						callback:function() {
							CloseDelay("Minutario cancelado con éxito", function(){$('#cmdbuscar').click();});
						},
						errorHandler:function(errorString, exception) { 
												jError(errorString, 'Error');          
						}
				});
		}
	});
}

function editarDocumento(idMinutario){
	document.location='nuevo_minutario.action?ID_MINUTARIO='+idMinutario;
}

function resetPersona(){
	if($('#txtpersonaDestino').attr('value')=='') $('#ID_PERSONA_DESTINO').attr('value', '0');
}

function cargarMinutarios(){
	ShowDelay("Cargando minutarios");
	var idDependencia = $('#cbodependencia').val();
	ControladorListadoMinutariosRemoto.getMinutariosCombo(idDependencia, 0, {
					callback:function(items) {
					 		$('#cbominutario').html(items);
							_closeDelay();
					}
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					}
	});
}

function getPersonasSubdireccion(edit,clave){
	ControladorListadoMinutariosRemoto.getPersonasSubdireccion({
        callback:function(items) { 
         	autocompletePersonasSubdireccion(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: ControladorListadoMinutariosRemoto.getPersonasSubdireccion');          				     	  
        }
    },async=false); 			 
}

function  autocompletePersonasSubdireccion(edit,clave,data) {
	$("#"+edit).autocomplete(data, {
		matchCase:false,
		max: 10,        
		highlightItem: true,        
		multiple: true,        
		multipleSeparator: " ", 					 
		parse: function(data) {
			return $.map(eval(data), function(row) {
				return {
					data: row,
					value: row.NOMBRE,
					result: row.NOMBRE + " "+ row.APE_PAT + " "+ row.APE_MAT
				}
			}); 
		},
		formatItem: function(item) {
			return format_personasSubdireccion(item);
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.CVE_PERS);
	});
}

function format_personasSubdireccion(persona){
	return persona.NOMBRE+" "+persona.APE_PAT+" "+persona.APE_MAT+" ["+persona.DEPENDENCIA+"/"+persona.SUBDIRECCION+"]";
}
