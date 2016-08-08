var subtotal =0;
var total =0;
var indices = [];
$(document).ready(function() {
	 //Configura los tabuladores
	 $('#tabuladores').tabs();
	 $('#tabuladores').tabs('enable',0);
	 $('#img_programa').click(function(event){
		GetProgramasPorUnidad($('#cbodependencias').val());
	});
	$('#cmdguardarMIR').click(function(event){
		GuardarMir();
	});
});

function GuardarMir()
{
	ShowDelay('Guardando registro','');
		controladorMIRRemoto.guardarMIR($('#ID_MIR').val(), $('#cbodependencia').val(), $('#txtfecha').val(), $('#txtPrograma').val(), $('#txtClaveProgramatica').val(),{
		callback:function(items) {	
			$('#ID_MIR').val(items);
			CloseDelay("M.I.R. Guardada con éxito");
		} 					   				
		,
		errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');
		}
	},async=false ); 
}

function ObtenerProgramaUnidad(nprograma)
{
	$('#txtPrograma').val(nprograma);
	_closeDelay();
}

function GetProgramasPorUnidad(idDependencia)
{
	jWindow('<iframe width="200" height="200" id="consultaPrograma" frameborder="0" src="../../sam/mir/muestra_programas.action?IdDependencia='+idDependencia+'"></iframe>','Programas Disponibles', '','Cerrar ',1);
}

