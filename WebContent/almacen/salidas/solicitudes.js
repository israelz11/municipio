/*
Autor : Mauricio Hernandez León
Version 1.1
*/
$(document).ready(function() {  
	$('#solicitudes').hide();
	$('#solicitudesPendientes').hide();
	getSolicitudesPendientes();
});

 function getSolicitudesPendientes() {
	quitRow("solicitudesPendientes");
	controladorSolicitudesRemoto.getSolicitudesPendientes($('#cboalmacen').attr("value"), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTablaPendiente( "solicitudesPendientes", i+1 ,this.ID_SALIDA,this.CONCEPTO,this.CLV_PARTID);
        });
			if (items.length > 0 )
			$('#solicitudesPendientes').show();
			else
			$('#solicitudesPendientes').hide();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 

 }

function pintaTablaPendiente( table, consecutivo,id,descripcion,partida){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='checkbox' name='clavesSal' id='clavesSal' value='"+id+"' >";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editar("+id+",'"+descripcion+"',"+partida+")\" >"; 		
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(descripcion,"","","") );	  
	row.appendChild( Td(partida,"","","") );	  
    row.appendChild( Td("",centro,"",htmlEdit) );	
	tabla.appendChild( row );
 }

function editar(id,descripcion,partida) {
	$('#clave').attr("value",id);	
	$('#concepto').attr("value",descripcion);
	$('#partida').attr("value",partida);
	$('#solicitudes').show();
	$('#captura').hide();
	$('#solicitudesPendientes').hide();
	getArticulos();
}

function realizarPedido() {
	 if ($('#partida').attr("value") !="" ) {
		$('#solicitudes').show();
		$('#captura').hide();
 		$('#solicitudesPendientes').hide();	 
		$('#concepto').attr("value",'');
		$('#almacen2').attr('value', $('#cboalmacen').attr('value'));
		getArticulos(); 
	} 
	else
	jAlert("Es necesario que selecciones una partida","Información");
	
 }
  
function getArticulos() {
	quitRow("lista_articulos");
	controladorSolicitudesRemoto.getArticulosPartida($('#almacen2').attr("value"),$('#partida').attr("value"),$('#clave').attr("value"), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTabla( "lista_articulos", i+1 ,this.ID_INVENTARIO,this.FOLIO,this.DESCRIPCION,this.UNIDMEDIDA,this.CANTIDAD,this.PRECIO);
        });	
			if (items.length == 0 ) {
			 $('#guardarSol').hide();
			 $('#enviarSol').hide();
			}
			else {
				$('#guardarSol').show();
				$('#enviarSol').show();
			}
			
			
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 
 }

  function pintaTabla( table, consecutivo,id,clave,descripcion,unidadMedida,cantidad,precio){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='hidden' name='claves' id='claves' value='"+id+"' >"+"<input type='input' name='cantidad_"+id+"' id='cantidad_"+id+"' size='12' maxlength='10' class='input'  onkeypress=' return keyNumbero( event );'  value='"+getHTML(cantidad)+"' ><input type='hidden' name='precio_"+id+"' id='precio_"+id+"' value='"+precio+"' > ";
	row.appendChild( Td(consecutivo,centro,"","") );
	row.appendChild( Td(clave,"","","") );	  
	row.appendChild( Td(descripcion,izquierda,"","") );	  
	row.appendChild( Td(unidadMedida,centro,"","") );	  
    row.appendChild( Td("",centro,"",htmlCheck) );	
	tabla.appendChild( row );
 }
 
 function regresarSol() {
	 $('#solicitudes').hide();
	 $('#captura').show();
	 $('#solicitudesPendientes').show();
	 $('#clave').attr("value","");
	 getSolicitudesPendientes(); 	 
 }
 
 function guardar(tipo) {
	 var lista=checkboxSeleccionados();
	 if ($('#concepto').attr("value")!="") {
	 if (lista.length >0 ) {
		 controladorSolicitudesRemoto.guardarSolicitud(lista,$('#almacen').attr("value"),$('#partida').attr("value"),$('#concepto').attr("value"),tipo,$('#clave').attr("value"),{
			 callback:function(items) {			  			  
 	  		   jInformation("La información se almaceno satisfactoriamente","Informacion");
			   regresarSol();
 		     }	
		,errorHandler:function(errorString, exception) { 
		   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
		}
	});		 
	 } else 
	   jAlert("Para realizar la operación es necesario solicitar algo ","Advertencia");	 
	 }else
	   jAlert("El concepto es obligatorio","Advertencia");	 
	 
 } 
 
 function checkboxSeleccionados(){
	var listaArt = [];
	$('input[name=claves]').each(function() {
	     var vIdArt= $(this).val();
		 var vIdArticulo= $('#cantidad_'+vIdArt).attr('value');
		 var vprecio= $('#precio_'+vIdArt).attr('value');
		 var map = {idArt: vIdArt, cantidad: vIdArticulo, precio:vprecio};
		 if(vIdArticulo!='' && parseFloat(vIdArticulo) > 0 ) {
		    listaArt.push( map);		 
		 }
	});	 
	return listaArt;
}  

 function eliminar(){
	  var checkSolicitud = [];
     $('input[name=clavesSal]:checked').each(function() {checkSolicitud.push($(this).val());	 });	 
	 if (checkSolicitud.length > 0 ) {
     controladorSolicitudesRemoto.cancelarSolicitud(checkSolicitud, {
        callback:function(items) {
		   jInformation("Se eliminaron satisfactoriamente los movimientos","Informacion");
		  getSolicitudesPendientes();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); } else 
	    jAlert("Es necesario seleccionar un elemento de la lista", "Advertencia");

	 }
	 