/* Aqui me quede para ma�ana */
$(document).ready(function() {  
$('#filaguardar').hide();
});

function guardarDato(){			
    var error="";
    var partida= $('#partida' ).attr('value');
	var subGrupo= $('#subGrupo' ).attr('value');
	var lista=checkboxSeleccionadosRoles();
	
	jConfirm('¿Confirma que desea guardar la información del listado de artículos?', 'Confirmar', function(r){
		if(r){
					if (lista.length >0 ) {
					ShowDelay('Guardando articulos','');
					controladorArticulosPartidaRemoto.guardarArticulos(lista,partida,subGrupo,{
						 callback:function(items) {			  			  
						   CloseDelay("Información almacenada con éxito",2000, function(){
								   $('#filaguardar').hide();
								   //$('#subGrupo' ).attr('value','');
								   quitRow( "detallesListas" );
							});
						   
						 }	
											,errorHandler:function(errorString, exception) { 
											   jError("Fallo la operación:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
											}
						});
				}
		}
	});	
}


function checkboxSeleccionadosRoles( ) {
	var listaArt = [];
	$('input[name=idCatArt]').each(function() {
	var vIdArt= $(this).val();
		 var vIdArticulo= $('#idArticulos_'+vIdArt).attr('value');		 
		 var vchecado=0;
		 if ($('#idArticulos_'+vIdArt).attr('checked'))
		   vchecado=1;
 		 var vestatus="INACTIVO";  		 
		 if ($('#estatus_'+vIdArt).attr('checked'))
		   vestatus="ACTIVO";  
		 var map = {idArt: vIdArt, idArticulo: vIdArticulo, checado:vchecado,estatus:vestatus};
		 if(vIdArticulo!='' || vchecado == 1) {
		  listaArt.push( map);
		 }
		 
	});	 
	return listaArt;
}  


 
 function llenarTabla() {	 
	quitRow( "detallesListas" );
	if ($("#partida").attr('value')!="" && $("#subGrupo").attr('value')!=""  && $("#alfabetico").attr('value')!=''  ) {	
	ShowDelay('Cargando lista de artículos','');	
	controladorArticulosPartidaRemoto.getArticulosPartidaGrupo($("#partida").attr('value'),$("#alfabetico").attr('value'),  {
        callback:function(items) { 					
         for (var i=0; i<items.length; i++ )  {			
			var reg = items[i];
			var id = reg.ID_ARTICULO;
			var idCatArt = reg.ID_CAT_ARTICULO;
			var subGrupo = reg.SUBGRUPO;
			var descripcion=  reg.DESCRIPCION;
	  		var txtunidadmedida=  reg.UNIDMEDIDA+" ";
			var precio=  reg.ULT_PRECIO;			
			var estatus=  reg.ESTATUS;
		    pintaTabla( "detallesListas", i+1 ,id,idCatArt,descripcion,txtunidadmedida,estatus,precio,subGrupo );			
        } 			
		if (items.length>0 )
			$('#filaguardar').show();
			  else
		    $('#filaguardar').hide();	  
		_closeDelay();	
		//llenarComboCatalogo(items,document.getElementById( 'cbSubfuncion' ));		
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); 
	} else
	    jAlert("No se ha especificado ningun texto de búsqueda", "Advertencia");
 }

 
 function buscarArticulosPartidaGrupo() {	 
 	if($("#subGrupo").val()==""||$("#subGrupo").val()==0){jAlert('El grupo seleccionado no es válido','Advertencia'); return false;}
	quitRow( "detallesListas" );
	ShowDelay('Cargando lista de artículos','');	
	controladorArticulosPartidaRemoto.getArticulosPartidaGrupo2($("#partida").attr('value'), parseInt($("#subGrupo").val()),  {
        callback:function(items) { 					
         for (var i=0; i<items.length; i++ )  {			
			var reg = items[i];
			var id = reg.ID_ARTICULO;
			var idCatArt = reg.ID_CAT_ARTICULO;
			var subGrupo = reg.SUBGRUPO;
			var descripcion=  reg.DESCRIPCION;
	  		var txtunidadmedida=  reg.UNIDMEDIDA+" ";
			var precio=  reg.ULT_PRECIO;			
			var estatus=  reg.ESTATUS;
		    pintaTabla( "detallesListas", i+1 ,id,idCatArt,descripcion,txtunidadmedida,estatus,precio,subGrupo );			
        } 			
		if (items.length>0 )
			$('#filaguardar').show();
		else
		    $('#filaguardar').hide();	  
		_closeDelay();	
			
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); 
 }
 
//pinta en pantalla el resultado obtenido 
 function pintaTabla( table, consecutivo,id,idCatArt,descripcion,txtunidadmedida,estatus,precio, subGrupo){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row = document.createElement( "TR" );		 	
	var valchecked="";
	if(precio==null||precio=='') precio = 0.00;
	if (id != "" && id!=null )
	  valchecked="checked";
	  var valcheckedSta="";
	if (estatus == "ACTIVO")
	  valcheckedSta="checked";	  
	var html = "<input type='checkbox' name='idArticulos_"+idCatArt+"' id='idArticulos_"+idCatArt+"' value='"+getHTML(id)+"' "+valchecked+"> <input type='hidden' name='idCatArt' id='idCatArt' value='"+idCatArt+"'> ";
	var html2 = "<input type='checkbox' name='estatus_"+idCatArt+"' id='estatus_"+idCatArt+"' value='1' "+valcheckedSta+">";	

	if ( subGrupo==$("#subGrupo").attr('value') || subGrupo==null  ) {		
    row.appendChild( Td("", centro,"", html )); 	
    row.appendChild( Td("", centro,"", html2 )); 	
	}
	else{
	row.appendChild( Td("", centro,"", "Otro Grupo" )); 	
    row.appendChild( Td("", centro,"", "Otro Grupo" )); 	
	}
 	row.appendChild( Td( descripcion,izquierda, "","" ) );
 	row.appendChild( Td( getHTML(txtunidadmedida),centro, "","" ) );
	row.appendChild( Td( getHTML('$ '+formatNumber(precio)),derecha, "","" ) );
 	tabla.appendChild( row );
 }



function getSelectPartida() {
	 var idPartida=$('#partida').attr('value'); 
	 dwr.util.removeAllOptions("subGrupo");
	 if (idPartida !="" ){
	controladorArticulosPartidaRemoto.getGrupos(idPartida, {
        callback:function(items) { 				
		 dwr.util.addOptions('subGrupo',{ '':'[Seleccione]' });
		 dwr.util.addOptions('subGrupo',{ '-1':'Sin grupo' });
		 dwr.util.addOptions('subGrupo',items,"SUBGRUPO", "DESCRIP" );
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          				     	  
        }
    }); 
	 } else {
		 dwr.util.addOptions('subGrupo',{ '':'[Seleccione]' });
	 }
	  
 }


