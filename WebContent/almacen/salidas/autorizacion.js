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
	controladorAutorizacionRemoto.getSolicitudesPorEstatus($('#almacen').attr("value"), {
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
 function validaarti(obj,importe){	
   var  num = parseFloat(obj.value);
   	if (num > importe || num<0 || isNaN(obj.value) || obj.value=="" ) 
	{ alert("error.. La existencia es mayor que la disponibilidad o número no válido");
	  obj.focus();
	}    
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
	controladorAutorizacionRemoto.getArticulosSalida($('#idSalida').attr("value"), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTabla( "detallesSolicitud", i+1 ,this.ID_DETALLE_SALIDA,this.FOLIO,this.DESCRIPCION,this.UNIDMEDIDA,this.ENTREGADOS,this.ENVIADO, this.NO_PEDIDOS,this.AUTORIZADO,this.EXISTENCIA,this.SOLICITADO,this.SURTIDO);
        });	
			
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 
 }

  function pintaTabla( table, consecutivo,id,clave,descripcion,unidadMedida,entregado,enviado,noPedidos,autorizado,existencia,solicitado,surtido){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
	var cantidad=solicitado;
	if( solicitado > (existencia-autorizado) )
	   cantidad=0;
	if( surtido !=null && surtido!=0 )
	    cantidad=surtido;
    var htmlCheck = "<input type='hidden' name='claves' id='claves' value='"+id+"' >"+"<input type='input' name='cantidad_"+id+"' id='cantidad_"+id+"' size='12' maxlength='10' class='inputNumero' onblur='validaarti(this,"+(existencia-autorizado)+")' onkeypress=' return keyNumbero( event );'  value='"+cantidad+"' >";
	row.appendChild( Td(consecutivo,centro,"","") );
	row.appendChild( Td(clave,"","","") );	  
	row.appendChild( Td(descripcion,izquierda,"","") );	  
	row.appendChild( Td(unidadMedida,centro,"","") );	  
	row.appendChild( Td(entregado,centro,"","") );	  
	row.appendChild( Td(enviado,centro,"","") );	  
	row.appendChild( Td(noPedidos,centro,"","") );	  
	row.appendChild( Td(autorizado,centro,"","") );	  
	row.appendChild( Td(existencia,centro,"","") );	  
	row.appendChild( Td(solicitado,centro,"","") );	  
    row.appendChild( Td("",centro,"",htmlCheck) );	
	tabla.appendChild( row );
 }
 
 function regresarSol() {
	 $('#detallesAutorizacion').hide();
	 $('#captura').show();
	 $('#idSalida').attr("value","");
	 getSolicitudesEnviadas();
 }
 
 function guardar(tipo) {
	 var lista=checkboxSeleccionados();
		 controladorAutorizacionRemoto.guardarSolicitudAutoricion(lista,tipo,$('#idSalida').attr("value"),{
			 callback:function(items) {			  			  
 	  		   jInformation("La información se almaceno satisfactoriamente","Guardado");
			   regresarSol();
 		     }	
		,errorHandler:function(errorString, exception) { 
		   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
		}
	});		 
	 
 } 
 
 function checkboxSeleccionados(){
	var listaArt = [];
	$('input[name=claves]').each(function() {
	     var vIdArt= $(this).val();
		 var vIdArticulo= $('#cantidad_'+vIdArt).attr('value');		 
		 var map = {idArt: vIdArt, cantidad: vIdArticulo};
	     listaArt.push( map);		 
	});	 
	return listaArt;
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
	 