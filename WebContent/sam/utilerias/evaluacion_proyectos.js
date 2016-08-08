/**
Descripcion: Codigo controlador para la pagina chgpasswd.jsp
Autor      : Mauricio Hernandez
Fecha      : 29/08/2009
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/

var sumaAvances=0;
var antesEditar=0;
$(document).ready(function() {
  var imagen="../../imagenes/cal.gif";	
  var formatFecha="dd/mm/yy";	
  $("#fechaInicio").datepicker({showOn: 'button', buttonImage:imagen , buttonImageOnly: true,dateFormat: formatFecha});  
  $("#fechaTermino").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha});  
  $("#fechaActa").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha});  
  $("#fechaInicioContrato").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha});  
  $("#fechaTerminoContrato").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha});
  $("#fechaAvance").datepicker({showOn: 'button', buttonImage: imagen, buttonImageOnly: true,dateFormat: formatFecha});    

 /*Configura los tabuladores*/
	 $('#tabuladores').tabs();
	 $('#tabuladores').tabs('enable',0);
	 //$('#tabuladores').tabs('option', 'disabled', [1]);
	 
});


function limpiarItem(){
	$('#avance').attr('value', '');
	$('#fechaAvance').attr('value', '');
	$('#meses').attr('value',0);
	$('#meses').focus();
}


function cambiarEvaluacion(){
	if ( $('#tipoEvaluacion').attr('value')==0 ) {
	/*$('#trEva1').hide();
	$('#trEva2').hide();
	$('#trEva3').hide();
	$('#trEva4').hide();*/
	
	} else {		
		limpiarDetEvaluacion();	
		$('#trEva1').show();
		$('#trEva2').show();
		$('#trEva3').show();
		$('#trEva4').show();
		pintarTablaDetalles();
	}	
}


function guardarEvaluacionDeProyecto(){			
	if ( $('#proyecto').attr('value')=="")  {jAlert('El programa no es válido','Advertenia'); return false;};
	if ( $('#fechaInicio').attr('value')!="" &&  $('#fechaInicio').attr('value').length != 10)  {jAlert('La fecha de inicio no es válida','Advertenia'); return false;};
	if ( $('#fechaTermino').attr('value')!="" &&  $('#fechaTermino').attr('value').length!=10)  {jAlert('La fecha de termino no es válida','Advertenia'); return false;};
	if ( $('#fechaActa').attr('value')!="" &&   $('#fechaActa').attr('value').length!=10)  {jAlert('La fecha de acta no es válida','Advertenia'); return false;};
	if ( $('#fechaInicioContrato').attr('value')!="" &&  $('#fechaInicioContrato').attr('value').length!=10 )  {jAlert('La fecha de inicio de contrato no es válida','Advertenia'); return false;};
	if ( $('#fechaTerminoContrato').attr('value')!="" &&  $('#fechaTerminoContrato').attr('value').length!=10 ) {jAlert('La fecha final de contrato no es válida','Advertenia'); return false;};

		ShowDelay('Guardando Autoevaluación','');	
    	controladorEvaluacionProyectosRemoto.guardarEvaluacion($('#idEvaluacionProyecto').attr('value'),$('#proyecto').attr('value') ,$('#ejercicio').attr('value'), $('#fechaInicio').attr('value'),$('#fechaTermino').attr('value'), $('#fechaActa').attr('value'),$('#cantidad').attr('value'), $('#fechaInicioContrato').attr('value') , $('#fechaTerminoContrato').attr('value') ,{
			 callback:function(items) {
				window.parent.cambiarVariable();
			  $('#idEvaluacionProyecto').attr('value',items);
			  $('#proyecto').attr('readonly',false);
			  CloseDelay('Autoevaluación guardada con éxito');
			 
 		     }	
								,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});		
	
}



function guardarDetalleEvaluacionDeProyecto(){			
	if ( $('#avance').attr('value')==""||parseFloat($('#avance').attr('value'))>100)  {jAlert('El avance escrito no es válido, debe estar entre 1 y 100','Advertencia'); return false;}
	if ( parseFloat($('#avance').attr('value'))+sumaAvances-antesEditar > 100  )   {jAlert('El avance no puede pasar de 100%','Advertencia'); return false;}
	if ( $('#fechaAvance').attr('value') == "" )  {jAlert('Introduzca la fecha de avance', 'Advertencia'); return false;}
	if ( $('#meses').attr('value') =="" )  {jAlert('El mes del avance no es válido','Advertencia'); return false;}	
	
	ShowDelay('Guardando detalle de autoevaluación','');			
    controladorEvaluacionProyectosRemoto.guardarEvalucionDetalle($('#idAvanceDetalle').attr('value'),$('#idEvaluacionProyecto').attr('value'), $('#fechaAvance').attr('value'),$('#avance').attr('value'),$('#tipoEvaluacion').attr('value'), $('#meses').attr('value'),{
			 callback:function(items) {			  			  
			 window.parent.cambiarVariable();
			 //buscarProyecto();
				 limpiarDetEvaluacion();			 
				 pintarTablaDetalles();
			 	CloseDelay('Detalles de autoevaluación guardados con éxito');
			 
 		     }	
			,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});
	
}



 function pintarTablaDetalles() {
	 quitRow("detalleEvaluaciones");
	 var idEvaluacion=$('#idEvaluacionProyecto').attr('value');
	 var tipo=$('#tipoEvaluacion').attr('value');
	 sumaAvances=0;
	controladorEvaluacionProyectosRemoto.getDetallesEvaluacionTipo(idEvaluacion,tipo,$('#ejercicio').attr('value'), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		      pintaTabla( "detalleEvaluaciones", i+1 ,this.ID_EVALUACION_DETALLE,this.ID_EVALUACION_PROYECTO,this.MES,this.FECHA, this.AVANCE, this.TIPO,this.DESMES,this.ESTATUSEVA);
			  sumaAvances =  sumaAvances +this.AVANCE;
        }); 					   						
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 

 }

  function pintaTabla( table, consecutivo,id,idEvaluacionProyecto,mes,fecha, avance,tipo, desmes,estatusEva){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );    
	
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editarEvaluacion("+id+","+mes+",'"+fecha+"',"+avance+")\" >"; 		
	row.appendChild( Td(desmes,centro,"","") );	  
	row.appendChild( Td(fecha,centro,"","") );
	row.appendChild( Td(avance,centro,"","") );
	
	var s = $('#tipoEvaluacion').val();
	if(s=='Contraloria')
	{
		row.appendChild( Td("",centro,"",htmlEdit) );
	}
	else
	{
		if (estatusEva=="ACTIVO")
		  row.appendChild( Td("",centro,"",htmlEdit) );	
		else
		  row.appendChild( Td("","","","") );	
		
	}
	tabla.appendChild( row );
 }
 
 
 function editarEvaluacion(id,mes,fecha,avance) {
	 ShowDelay('Cargando evaluación...');
	$('#avance').attr('value',avance);
	$('#fechaAvance').attr('value',fecha);
	$('#meses').attr('value',mes);	
	$('#idAvanceDetalle').attr('value',id) ;		
	antesEditar=avance;
	_closeDelay();
 }
 
  function limpiarDetEvaluacion() {
	$('#avance').attr('value','');
	$('#fechaAvance').attr('value','');
	$('#meses').attr('value','') ;	
	$('#idAvanceDetalle').attr('value','') ;
	antesEditar=0;
 }
 
