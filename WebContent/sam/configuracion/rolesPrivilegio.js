 function llenarTablaPrivilegios() {	 
	quitRow( "detallesListas" );
	var sistema= $('#sistemas' ).attr('value');
	var rol= $('#roles' ).attr('value');
	if (sistema!="" && rol!="") {	
	var anterior="";
	ShowDelay('Cargando lista de privilegios','');
	controladorRolesPrivilegiosRemoto.buscarPrivilegiosRoles(rol,sistema,  {
        callback:function(items) { 		
         for (var i=0; i<items.length; i++ )  {			
			var reg = items[i];
			var idPriviRol =  getHTML( reg.ID_ROL_PRIVILEGIO);
			var idPrivi=   getHTML( reg.ID_PRIVILEGIO);
			var modulo =  getHTML( reg.MOD_DESCRIPCION);
			var privilegio = getHTML(  reg.PRI_DESCRIPCION);
			if (anterior!=modulo)
			    pintaTabla( "detallesListas", i+1 ,idPriviRol,idPrivi,modulo,privilegio,1 );
	        pintaTabla( "detallesListas", i+1 ,idPriviRol,idPrivi,modulo,privilegio,2 );
			anterior=modulo;
			
        } 			
		$('#filaBoton').show();
	    $('#filaPrivilegio').show();
		_closeDelay();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); }  else  {
		  $('#filaBoton').hide();
		    $('#filaPrivilegio').hide();
	}
	
}

//pinta en pantalla el resultado obtenido 

 function pintaTabla( table, consecutivo,idPriviRol,idPrivi,modulo,privilegio,op ){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row = document.createElement( "TR" );
	var valchecked="";
	if (idPriviRol!="")
	  valchecked="checked";
	var html =  "<input type='checkbox' name='idPrivilegio_"+idPrivi+"' id='idPrivilegio_"+idPrivi+"' value='"+idPriviRol+"' "+valchecked+"> <input type='hidden' name='idPrivilegios' id='idPrivilegios' value='"+idPrivi+"'> ";	
	if (op!=1) {
 	row.appendChild( Td("","", "", "" ) ); 
    row.appendChild( Td("",centro ,"", html )); 		
 	row.appendChild( Td(privilegio,izquierda, "", "" ) );
	} else {
		row.appendChild( Td("","" ,"", "" ));
		row.appendChild( Td(modulo,"text-align:left; font-weight:bold; color:#000000; ", "", "",2 ) );	
	}
 	tabla.appendChild( row );
 }
 

function guardarDato(){			
    var error="";
	var rol= $('#roles' ).attr('value');
	var lista=checkboxSeleccionadosPrivilegios();
		ShowDelay('Guardando lista de privilegios','');
	    controladorRolesPrivilegiosRemoto.guardarPrivilegios(lista,rol,{
			 callback:function(items) {			  			  
 	  		   CloseDelay("Privilegios guardados con éxito",2000, function(){
					quitRow( "detallesListas" );
				   $('#filaBoton').hide();
				   $('#filaPrivilegio').hide();
				   $('#roles' ).attr('value','');
				   $('#sistemas' ).attr('value','');			   

				});
			   
			   
 		     }	
								,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});

}


function checkboxSeleccionadosPrivilegios( ) {
	var checkbox = document.forma.idPrivilegios;
	var datos    = new Array();
	var sel=0;
	if ( checkbox != null ) {	
	var checkboxLength =checkbox.length;		
	if (isNaN(checkboxLength))	
	    checkboxLength = 0;	
	if (checkbox.length > 0 ) {		
	 for( var i=0; i < checkboxLength; i++ ){
	     var vidPrivi = checkbox[i].value;
		 var vidPriviRol= $('#idPrivilegio_'+vidPrivi).attr('value');		 
		 var vchecado=0;
		 if ($('#idPrivilegio_'+vidPrivi).attr('checked'))
		   vchecado=1;
		 var map = {idPrivi: vidPrivi, idPriviRol: vidPriviRol, checado:vchecado};
	     if(vidPriviRol!='' || vchecado == 1) {
		    datos[sel]=map;
		    sel = sel + 1;
	       }
	   }
	}
	else {	   
		 var vidPrivi = checkbox.value;
		 var vidPriviRol= $('#idPrivilegio_'+vidPrivi).attr('value');		 
		  var vchecado=0;
		 if ($('#idPrivilegio_'+vidPrivi).attr('checked'))
		   vchecado=1;
		 var map = {idPrivi: vidPrivi, idPriviRol: vidPriviRol, checado:vchecado};
 	     if(vidPriviRol!='' || vchecado == 1) 
		    datos[0]=map;
	   }
	}
	return datos;
}  
