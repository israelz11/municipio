/**
Descripcion: Codigo controlador para la pagina reembolso.jsp
Autor      : Mauricio Hernandez
Fecha      : 19/01/2010
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/
var totalDesco= 0;
var totalVale=0;

$(document).ready(function() {  
  $("#fecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
  $("#fechaDeposito").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
  $('#cbovales').change(function(event){cargarDatosVale();});
  ocultarFilas();
});

function cargarDatosVale(){
	var temp = $('#cbovales').val();
	if(temp==0) return false;
	var datos = temp.split('/');
	controladorReembolsosLiquidosValesRemoto.getDatosVale($('#cve_val').attr('value'), datos[0], datos[1],{
			callback:function(items) {				 
							$('#comprobado').text(formatNumber(items.COMPROBADO,'$ '));
							$('#restante').text(formatNumber(items.RESTANTE,'$ '));
						 }	
						,errorHandler:function(errorString, exception) { 
						   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
					}
			});	
}

function regresar(){
  document.location ="lista_reembolsos.action";
}

function limpiar(){
	 	 $('#cve_val').attr('value','');
		 $('#noVale').attr('value','');
		 $('#importe').attr('value','');
		 $('#proyecto').text("");
		 $('#partida').text("");
		 $('#cve_proyecto').attr('value',"");
		 $('#cve_partida').attr('value',"");
		 $('#importeVale').text("");	
		 ocultarFilas();
	     quitRow("detallesReembolsos");
}

function ocultarFilas(){
	if ($('#cve_val').attr('value')=="") {
		$('#flla_importe').hide();
		$('#flla_partida').hide();
		$('#flla_proyecto').hide();		
		$('#flla_descuento').hide();
		$('#fila_comprobado').hide();
		$('#fila_restante').hide();		
		
	}else {
		$('#flla_importe').show();
		$('#flla_partida').show();
		$('#flla_proyecto').show();
		$('#flla_descuento').show();
		$('#fila_comprobado').show();
		$('#fila_restante').show();		
		}
	
}

function getReporteVale() {
$('#forma').attr('action',"../reportes/formato_vale.action");
$('#forma').attr('target',"impresion");
$('#forma').submit();
$('#forma').attr('target',"");
$('#forma').attr('action',"lista_vales.action");
}

function guardarRembolso(){
	var temp = $('#cbovales').val();
	var datos = temp.split('/');
	
	if($('#cbovales').val()==0){jAlert('Es necesario seleccionar un programa y partida del vale','Advertencia'); return false;}	
	if ($('#fecha').attr('value')=="") {jAlert('La Fecha no es válida','Advertencia'); return false;}	
	if ($('#importe').attr('value')=="") {jAlert('El Importe no es válido','Advertencia');return false;}
	if ($('#fechaDeposito').attr('value')=="")  {jAlert('La Fecha de deposito no es válida','Advertencia');return false;}
	if ($('#cve_val').attr('value')=="")  {jAlert('La Clave del Vale no es válida','Advertencia');return false;}	
	if($('#cbovales').attr('value')==''){jAlert('','Advertencia');}
	//alert(''+redondeo(parseFloat($('#importe').attr('value'))+parseFloat(totalDesco)));
	if ($('#importe').attr('value')!="" && redondeo(parseFloat($('#importe').attr('value'))+parseFloat(totalDesco)) > totalVale) {jAlert('El Importe a descontar no puede ser mayor al disponible', 'Advertencia');return false;}
	
	jConfirm('¿Confirma que desea guardar el Reembolo Liquido?','Confirmar', function(r){
			if(r){
					alert($('#importe').attr('value'));
					ShowDelay('Guardando Vale de Reembolso','');
					controladorReembolsosLiquidosValesRemoto.guardarComprobacion($('#cve_val').attr('value'),$('#importe').attr('value'),$('#importeAnte').attr('value'), datos[0], datos[1],$('#fecha').attr('value'),$('#fechaDeposito').attr('value'), {
						 callback:function(items) {				 
							CloseDelay("Vale Reembolso guardado con éxito",2000, function(){
								$('#importe').attr('value', '');
								pintarTablaReembolso(); 	
								$('#importe').focus();
							});
						 }	
						,errorHandler:function(errorString, exception) { 
						   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
					}
				 });	
			}
	});
	
}

function getValesDisponibles(){
	jWindow('<iframe width="800" height="400" name="VAL" id="VAL" frameborder="0" src="../../sam/consultas/muestra_vales_reembolso.action?&unidad='+$('#unidad').attr('value')+'"></iframe>','Vales Disponibles', '','Cerrar',1);
}

function getListaValeProgramaPart(){
	controladorReembolsosLiquidosValesRemoto.getMovimientoVales($('#cve_val').attr('value'), {
        callback:function(items) {
			getValeProgramaPartida(items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    },async=false ); 
}

function getValeProgramaPartida(datos) {		
        //dwr.util.removeAllOptions("claveValeDis");
		dwr.util.removeAllOptions("cbovales");
		dwr.util.addOptions('cbovales',{ 0:'[Seleccione]' });
		dwr.util.addOptions('cbovales',datos,"VALOR", "PROYECTOPARTIDA" );		
 }
 
function getVale(numVale,proyecto,partida,importe,idVale ){
	$('#proyecto').text(proyecto);
	$('#partida').text(partida);
	$('#cve_proyecto').attr('value',proyecto);
	$('#cve_partida').attr('value',partida);
	$('#importeVale').text(formatNumber(importe,'$ '));
	$('#comprobado').text('$ 0.00');
	$('#restante').text('$ 0.00');
	totalVale=importe;
	$('#cve_val').attr('value',idVale);
	$('#noVale').attr('value',numVale);
	pintarTablaReembolso();
    $.alerts._hide();
	ocultarFilas();
	
	getListaValeProgramaPart();
}


 function pintarTablaReembolso() {
	 quitRow("detallesReembolsos");
	controladorReembolsosLiquidosValesRemoto.getComprobacionesVales($('#cve_val').attr('value'), {
        callback:function(items) { 		
		totalDesco =0;
            jQuery.each(items,function(i) {
 		     pintaTabla( "detallesReembolsos", i+1 ,this.ID_VALE,this.CVE_OP, '['+this.ID_PROYECTO+'] '+this.N_PROGRAMA, this.CLV_PARTID, this.TIPO,this.IMPORTE,this.FECHA,this.FE_DEPOSI);
			 
			 totalDesco =totalDesco+this.IMPORTE;
			 
        }); 				   									
			pintaTotalDescuento( "detallesReembolsos", totalDesco);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 

 }

function pintaTotalDescuento( table, total){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
	$('#descontado').text(formatNumber(total,'$ '));	
	var tdCol=Td("Total",derecha,"","");
	tdCol.colSpan=7;
	row.appendChild(tdCol);
	row.appendChild( Td(formatNumber(total,'$ '),derecha,"",""));
	tabla.appendChild( row );
 }
 
 
  function pintaTabla( table, consecutivo,id,claveOp, programa, partida, tipo,importe,fecha,fechaDeposito){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+id+"' >";
	
	if (tipo=='OP') {
	  row.appendChild( Td("",centro,"","") );
  	  row.appendChild( Td("Orden de pago","","","") );	  
	  
	}
	  else {
	 row.appendChild( Td("",centro,"",htmlCheck) );	
 	 row.appendChild( Td("Reembolso liquido","","","") );	
	 if(getHTML(claveOp)=='') claveOp ='No Aplica';  
	  }
	 row.appendChild( Td(getHTML(claveOp),centro,"","") );	 
	 row.appendChild( Td(programa,centro,"","") );
	 row.appendChild( Td(partida,centro,"","") ); 
	 row.appendChild( Td(fecha,centro,"","") );
	 row.appendChild( Td(getHTML(fechaDeposito),centro,"","") );
	 row.appendChild( Td(formatNumber(importe,'$ '),derecha,"","") );
	 tabla.appendChild( row );
 }
 
 
 function eliminarReembolso(){
	  var checkReembolsos = [];
	  var idVale=$('#cve_val').attr('value');
     $('input[name=claves]:checked').each(function() {checkReembolsos.push($(this).val());	 });	 
	 if (checkReembolsos.length > 0 ) {
		 jConfirm('¿Confirma que desea eliminar el Vale de Reembolso Liquido?','Confirmar', function(r){
				 ShowDelay('Eliminando registros de reembolso','');
				 controladorReembolsosLiquidosValesRemoto.eliminarReembolso(checkReembolsos,idVale, {
					callback:function(items) {
					   CloseDelay("Vale Reemblolso eliminado satisfactoriamente",2000, function(){
							pintarTablaReembolso(); 
					   });
					  
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
					jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
					}
				}); 
		});
		 
		
	} else 
	    jAlert('Es necesario seleccionar un elemento de la lista', 'Advertencia');

}
	 
 