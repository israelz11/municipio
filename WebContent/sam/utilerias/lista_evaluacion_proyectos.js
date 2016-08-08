var guardado = false;

$(document).ready(function() { 
		var i = $('#hdcont').attr('value');
		for(x=1; x<=i;x++){
			$('#link'+x).qtip({content: {text: $('#hd'+x).attr('value'),
								title: { text: 'Informacion Adicional' }
							},
						   position: {
							  corner: {
								 	target: 'bottomRight',
      								tooltip: 'topLeft'
							  }
						   },
						    style: { 
									  border: {
										 width: 3,
										 radius: 8,
										 color: '#575757'
									  },
									  width: 400
								} 
							,
							show: { effect: 'slide' }
			});
		}
		
		
});


function cambiarVariable(){
	guardado = true;
}

function buscar(){
	
	$("#forma").submit();
}

function getAvances(id_proyecto, proyecto){
	jWindow('<iframe width="700" height="400" name="avancesFisicos" id="avancesFisicos" frameborder="0" src="../../sam/utilerias/evaluacion_proyectos.action?proyecto='+id_proyecto+'"></iframe>','Evaluación del programa ('+proyecto+")", '','Cerrar',1, function(){if(guardado) buscar();});
}

function actualizarProyectos(){
	ShowDelay('Actualizando proyectos...', '');
	ControladorMuestraListadoAvancesFisicosRemoto.actualizarProyectos({
			callback:function(items) {
				buscar();
			},
			errorHandler:function(errorString, exception) { 
			jError(errorString, 'Error');   
		}   
	});
}

function getReporteAvances() {
$('#forma').attr('target',"impresion");
$('#forma').attr("action", "../reportes/rpt_avances_fisicos.action");
$('#forma').submit();
$('#forma').attr('target',"");
$('#forma').attr('action',"lista_evaluacion_proyectos.action");
}
