/*
Autor : Mauricio Hernandez León
Version 1.1
*/
$(document).ready(function() {  
	$('#detallesAutorizacion').hide();
	getSolicitudesEnviadas();
});

 function getSolicitudesEnviadas() {
	quitRow("listaPendientes");
	controladorEntregaRemoto.getSolicitudesPorEstatus($('#almacen').attr("value"), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTablaPendiente( "listaPendientes", i+1 ,this.ID_SALIDA,this.FOLIO,this.CLV_PARTID+"-"+this.PARTIDA,this.DEPTO,this.NOMBRE_COMPLETO,this.FECHA,this.CONCEPTO);
        });
			/*if (items.length == 0 )
		   	  $('#listaPendientes').hide();*/
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 

 }



function pintaTablaPendiente( table, consecutivo,id,folio,partida,depto,solicitante,fecha,concepto){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='checkbox' name='clavesSal' id='clavesSal' value='"+id+"' >";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"autorizar("+id+","+folio+",'"+depto+"','"+solicitante+"','"+fecha+"','"+concepto+"')\" >"; 		
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(folio,"","","") );	  
	row.appendChild( Td(partida,izquierda,"","") );	  
	row.appendChild( Td(depto,izquierda,"","") );	  
	row.appendChild( Td(solicitante,izquierda,"","") );	  
	row.appendChild( Td(fecha,"","","") );	  
    row.appendChild( Td("",centro,"",htmlEdit) );	
	tabla.appendChild( row );
 }

function autorizar(id,folio,depto,solicitante,fecha,concepto) {
	$('#colFolio').text(folio);
	$('#colFecha').text(fecha);
	$('#colResponsable').text(solicitante);
	$('#colDepto').text(depto);
	$('#colConcepto').text(concepto);
	$('#idSalida').attr("value",id);	
	$('#detallesAutorizacion').show();
	$('#captura').hide();
	getArticulos();
}

  

function getArticulos() {
	quitRow("detallesSolicitud");
	controladorEntregaRemoto.getArticulosEntrega($('#idSalida').attr("value"), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTabla( "detallesSolicitud", i+1 ,this.ID_DETALLE_SALIDA,this.FOLIO,this.DESCRIPCION,this.UNIDMEDIDA,this.SOLICITADO,this.SURTIDO);
        });				
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 
 }

  function pintaTabla( table, consecutivo,id,clave,descripcion,unidadMedida,solicitado,surtido){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
	row.appendChild( Td(consecutivo,centro,"","") );
	row.appendChild( Td(clave,"","","") );	  
	row.appendChild( Td(descripcion,izquierda,"","") );	  
	row.appendChild( Td(unidadMedida,centro,"","") );	  
	row.appendChild( Td(solicitado,centro,"","") );
	row.appendChild( Td(surtido,centro,"","") );
	tabla.appendChild( row );
 }
 
 function regresarSol() {
	 $('#detallesAutorizacion').hide();
	 $('#captura').show();
	 $('#idSalida').attr("value","");
	 getSolicitudesEnviadas();
 }
 
 function guardar(tipo) {
  controladorEntregaRemoto.guardarSolicitudEntregada(tipo,$('#idSalida').attr("value"),{
			 callback:function(items) {			  			
			 if (items){
 	  		     jInformation("La información se almaceno satisfactoriamente","Guardado");
			     imprimirSolicitud();
			     regresarSol();
			   }
			   else 
			     jInformation("La información no se almaceno, verifique los detalles de los artículos","Error");
 		     }	
		,errorHandler:function(errorString, exception) { 
		   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
		}
	});		 	 
 } 
 
 function eliminar(){
	  var checkSolicitud = [];
     $('input[name=clavesSal]:checked').each(function() {checkSolicitud.push($(this).val());	 });	 
	 if (checkSolicitud.length > 0 ) {
     controladorSolicitudesRemoto.cancelarSolicitud(checkSolicitud, {
        callback:function(items) {
		   jInformation("Se cancelaron satisfactoriamente los movimientos","Cancelación");
		  getSolicitudesEnviadas();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); } else 
	    jInformation("Es necesario que seleccione un elemento de la lista");

	 }
function imprimirSolicitud(){
	$('#forma').attr("action","../reportes/comprobante.action");
	$('#forma').attr("target","reporteSolicitud");
	$('#forma').submit();
    $('#forma').attr("target","");
	$('#forma').attr("action","");
}