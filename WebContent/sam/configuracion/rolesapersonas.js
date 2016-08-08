 function llenarTablaRoles() {	 
	quitRow( "detallesListas" );
	var usuario= $('#usuario' ).attr('value');
	if (usuario!="" ) {	
	ShowDelay('Cargando listado de roles','');
	controladorRolesAPersonasRemoto.buscarRolesUsuarios(usuario,  {
        callback:function(items) { 		
         for (var i=0; i<items.length; i++ )  {			
			var reg = items[i];
			var idUsuarioRol =  getHTML( reg.ID_USUARIO_ROL);
			var rol=   getHTML( reg.ROL_DESCRIPCION);
			var idRol =  getHTML( reg.ID_ROL);
	        pintaTabla( "detallesListas", i+1 ,idUsuarioRol,rol,idRol);			
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


 function pintaTabla( table, consecutivo,idUsuarioRol,rol,idRol){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row = document.createElement( "TR" );
	var valchecked="";
	if (idUsuarioRol!="")
	  valchecked="checked";
	var html =  "<input type='checkbox' name='idRolUsuario_"+idRol+"' id='idRolUsuario_"+idRol+"' value='"+idUsuarioRol+"' "+valchecked+"> <input type='hidden' name='idRol' id='idRol' value='"+idRol+"'> ";	
    row.appendChild( Td("",centro ,"", html )); 		
 	row.appendChild( Td(rol,izquierda, "", "" ) ); 
 	tabla.appendChild( row );
 }
 

function guardarDato(){			
    var error="";
   var usuario= $('#usuario' ).attr('value');
	var lista=checkboxSeleccionadosRoles();
		ShowDelay('Guardando información del rol','');
	    controladorRolesAPersonasRemoto.guardarRolUsuario(lista,usuario,{
			 callback:function(items) {	  
 	  		   CloseDelay("Rol guardada con éxito",2000, function(){
					   $('#usuario' ).attr('value','');			   
				});
				quitRow( "detallesListas");
				$('#filaPrivilegio').hide();	
				$('#filaBoton').hide();
 		     }	
								,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});

}


function checkboxSeleccionadosRoles( ) {
	var checkbox = document.forma.idRol;
	var datos    = new Array();
	var sel=0;
	if ( checkbox != null ) {	
	var checkboxLength =checkbox.length;		
	if (isNaN(checkboxLength))	
	    checkboxLength = 0;	
	if (checkbox.length > 0 ) {		
	 for( var i=0; i < checkboxLength; i++ ){
	     var vidRol = checkbox[i].value;
		 var vidRolUsuario= $('#idRolUsuario_'+vidRol).attr('value');		 
		 var vchecado=0;
		 if ($('#idRolUsuario_'+vidRol).attr('checked'))
		   vchecado=1;
		 var map = {idRol: vidRol, idRolUsuario: vidRolUsuario, checado:vchecado};
	     if(vidRolUsuario!='' || vchecado == 1) {
		    datos[sel]=map;
		    sel = sel + 1;
	       }
	   }
	}
	else {	   
		 var vidRol = checkbox.value;
		 var vidRolUsuario= $('#idRolUsuario_'+vidRol).attr('value');		 
		  var vchecado=0;
		 if ($('#idRolUsuario_'+vidRol).attr('checked'))
		   vchecado=1;
		 var map = {idRol: vidRol, idRolUsuario: vidRolUsuario, checado:vchecado};
 	     if(vidRolUsuario!='' || vchecado == 1) 
		    datos[0]=map;
	   }
	}
	return datos;
}  


function getSelectUnidad() {
	 var idUnidad=$('#unidad').attr('value'); 
	 dwr.util.removeAllOptions("usuario");
	 if (idUnidad !="" ){
	controladorRolesAPersonasRemoto.getUsuariosUnidad(idUnidad, {
        callback:function(items) { 				
		 dwr.util.addOptions('usuario',{ '':'[Seleccione]' });
		 dwr.util.addOptions('usuario',items,"CVE_PERS", "NOMBRE_COMPLETO" );		
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          				     	  
        }
    }); 
	 }
 }
 