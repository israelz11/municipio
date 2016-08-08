/*
Autor : Mauricio Hernandez León
Version 1.1
*/
$(document).ready(function() {  
	$('#listasSolicitudes').hide();
	$('#detallesSalida').hide();
});


 function buscar() {
	quitRow("listasSolicitudes");
	if (!(($('#folio').attr("value")==""  && $('#status').attr("value")==""  && $('#year').attr("value")== "" )
	|| (($('#year').attr("value")== "0" || $('#year').attr("value")== "") && ($('#status').attr("value")=="" || $('#status').attr("value")=="TODOS" ) && $('#folio').attr("value")=="")	))	
	controladorConsultasSalidasRemoto.getSalidasPorEjemplo($('#almacen').attr("value"),$('#folio').attr("value"),$('#year').attr("value"),$('#status').attr("value"),$('#tipo').attr("value"), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTablaPendiente( "listasSolicitudes", i+1 ,this.ID_SALIDA,this.FOLIO,this.CLV_PARTID+"-"+this.PARTIDA,this.DEPTO,this.NOMBRE_COMPLETO,this.FECHA,this.FECHA_ENTREGA,this.ESTATUS);
        });
			if (items.length == 0 )
		   	  $('#listasSolicitudes').hide();
			  else
			  $('#listasSolicitudes').show();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 
	else
	 jInformation("Es necesario que los parametros esten bien filtrados para realizar la búsqueda","Guardado");

 }



function pintaTablaPendiente( table, consecutivo,id,folio,partida,depto,solicitante,fecha,fechaEntrega,estatus){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"getDatos("+id+","+folio+",'"+partida+"','"+depto+"','"+solicitante+"','"+fecha+"','"+fechaEntrega+"','"+estatus+"')\" >"; 		
	row.appendChild( Td(consecutivo,centro,"","") );
	row.appendChild( Td(folio,"","","") );	  
	row.appendChild( Td(depto,izquierda,"","") );	  
	row.appendChild( Td(solicitante,izquierda,"","") );	  
	row.appendChild( Td(estatus,centro,"","") );	  
	row.appendChild( Td(fecha,"","","") );	  
	row.appendChild( Td(fechaEntrega,"","","") );	  
    row.appendChild( Td("",centro,"",htmlEdit) );	
	tabla.appendChild( row );
 }

function regresarSol() {
	 $('#formaConsulta').show();
	 $('#listasSolicitudes').show();
	 $('#detallesSalida').hide();	 
 }


function imprimirSolicitud(){
	$('#forma').attr("action","../reportes/comprobante.action");
	$('#forma').attr("target","reporteSolicitud");
	$('#forma').submit();
    $('#forma').attr("target","");
	$('#forma').attr("action","");
}


function getDatos(id,folio,partida,depto,solicitante,fecha,fechaEntrega,estatus) {
	 $('#idSalida').attr("value",id);
	 $('#formaConsulta').hide();
	 $('#listasSolicitudes').hide();
	 $('#detallesSalida').show();
	 getDetalles();
	 $('#fpartida').text(partida);
	 $('#ffolio').text(folio);
	 $('#fdepto').text(depto);
	 $('#fusuario').text(solicitante);
	 $('#fenvio').text(fecha);
	 $('#fentrega').text(fechaEntrega);
	 $('#festatus').text(estatus);
	 if (estatus=="ENTREGADO" && $('#permisoCancelar').attr("value")=="SI" )
	    $('#btcancelar').show();
	 else
	    $('#btcancelar').hide();
}

 


function getDetalles(){
	quitRow("detalleslistasSolicitudes");
	controladorEntregaRemoto.getArticulosEntrega($('#idSalida').attr("value"), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTabla( "detalleslistasSolicitudes", i+1 ,this.ID_DETALLE_SALIDA,this.FOLIO,this.DESCRIPCION,this.UNIDMEDIDA,this.SOLICITADO,this.SURTIDO);
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
 
 
 function cancelarSolicitud(){
	 var permiso =  false;
	 if ($('#permisoCancelar').attr("value")=="SI")
	   permiso =  true;
	controladorConsultasSalidasRemoto.cancelarSolicitud($('#idSalida').attr("value"),permiso, {
        callback:function(items) { 			
          jInformation(items,"Información");		
		  regresarSol();
		  buscar();
} 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 
 }


  
