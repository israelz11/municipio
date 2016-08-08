/**
Descripcion: Codigo controlador para la pagina grupos.jsp
Autor      : Mauricio Hernandez
Fecha      : 26/10/2009
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/
	var todoBien=false;
	var sel      = 0;
function limpiar(){
			 //$('#tipo').attr('value','');			 
			  quitRow("detallesTabla");
}

function actualizarUsuarios(){
	dwr.util.removeAllOptions("usuario");
	dwr.util.addOptions('usuario',{ 0:'[Seleccione un usuario]' });
	ShowDelay('Actualizando listado de usuarios...','');
	controladorAsignacionGruposUsuariosRemoto.getUsuariosPorUnidad($('#cbounidad').attr('value'),{
		callback:function(items) { 				
			dwr.util.addOptions('usuario',items,"CVE_PERS", "NOMBRE_COMPLETO");	
			_closeDelay();				
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
	});
}

function guardar(){			
    var error="";
	var titulo ='Advertencia - Informacion no válida';	
	var lista=checkboxSeleccionadosGrupos();
	var tipo=$('#tipo').attr('value');
	if ( $('#usuario').attr('value')==""||$('#usuario').attr('value')=="0")  error += 'Grupo</br>';	
	if ( lista.length==0)  error += 'Debe de seleccionar un elemento para poder realizar la operación</br>';	
	if ( todoBien==false && sel==0 && tipo=='FIRMA')  error += 'Debe de poner un grupo seleccionada por default';	
	if ( error=="") {	
	ShowDelay('Guardando grupo actual','');
    controladorAsignacionGruposUsuariosRemoto.guardarUsuariosGrupo($('#usuario').attr('value'),lista,{
			 callback:function(items) {			  			  
	  		   CloseDelay("Grupo actual asignado satisfactoriamente",2000);
			 limpiar();
			 pintarTablaDetalles();
 		     }	
								,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});		}else jAlert(error,titulo);	
}

 function pintarTablaDetalles() {
	quitRow("detallesTabla");	
	var usuario=$('#usuario').attr('value');	
	var tipo=$('#tipo').attr('value');
	if (usuario!="" && tipo!="" ) {
		
	ShowDelay('Cargando grupos disponibles','');
	controladorAsignacionGruposUsuariosRemoto.getGruposEstatus(usuario,tipo, {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		    pintaTabla( "detallesTabla", i+1 ,this.ID_GRUPO_CONFIG,this.GRUPO_CONFIG,this.ID_GRUPO_CONFIG_USUARIO,this.ASIGNADO,this.TIPO);
        }); 					   		
		_closeDelay();							
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); }

 }

  function pintaTabla( table, consecutivo,idGrupo,grupo,idFirmaGrupo,activo,tipo){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
	var selecte="";
	var selecteChek="";
	getHTML(idFirmaGrupo);
	
	if (getHTML(idFirmaGrupo)!="")
	   selecteChek=" checked ";
	if (activo==1)
		selecte=" checked ";
	if (tipo=="FIRMA")
		$('#idGrupoTab').show();
	else
		$('#idGrupoTab').hide();

    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+idGrupo+"'  "+selecteChek+" ><input type='hidden' name='clavesGpo"+idGrupo+"' id='clavesGpo"+idGrupo+"' value='"+getHTML(idFirmaGrupo)+"'  >";					
	var htmlRadio = "<input type='radio' name='clavesRadio' id='clavesRadio' value='"+idGrupo+"' "+selecte+">";
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(grupo,"","","") );	  
	if (tipo=="FIRMA")
    	row.appendChild( Td("",centro,"",htmlRadio) );	
	else
	    row.appendChild( Td("",centro,"","") );	
	tabla.appendChild( row );
 }
 
function checkboxSeleccionadosGrupos( ) {
	var checkbox= document.forma.claves;
	var idGrupoSelec = $('input[name=clavesRadio]:checked').val();
	sel      = 0;
	var datos    = new Array();
	todoBien=false;
	if ( checkbox != null ) {	
	var checkboxLength =checkbox.length;		
	if (isNaN(checkboxLength))	
	    checkboxLength = 0;	
	if (checkbox.length > 0 ) {		
	 for( var i=0; i < checkboxLength; i++ ){
	     var idGrupoMap = checkbox[i].value;
		 var idGrupoUserMap= $('#clavesGpo'+idGrupoMap).attr('value');		 
		 var idGrupoSelecCheck=checkbox[i].checked;
		 var defaultGrupoMap=0;
		 if  (idGrupoSelec==idGrupoMap && idGrupoSelecCheck) {
		     defaultGrupoMap=1;
			 todoBien=true;
		 }
		 var map = {idGrupo: idGrupoMap, idGrupoUser: idGrupoUserMap, checado : idGrupoSelecCheck , defaultGrupo:defaultGrupoMap };
	     if(idGrupoUserMap!='' || checkbox[i].checked == true ) {
		    datos[sel]=map;
		    //datos[sel] = checkbox[i].value;
		    sel = sel + 1;
	    }
		}
	}
	else {	   
		 var idGrupoMap = checkbox.value;		   	  
		 var idGrupoUserMap= $('#clavesGpo'+idGrupoMap).attr('value');		 
		 var idGrupoSelecCheck=checkbox.checked;
	     var defaultGrupoMap=0;
		 if  (idGrupoSelec==idGrupoMap && idGrupoSelecCheck) 
		     defaultGrupoMap=1;					 
		 var map = {idGrupo: idGrupoMap, idGrupoUser: idGrupoUserMap, checado : idGrupoSelecCheck,defaultGrupo:defaultGrupoMap};
		 if (idGrupoUserMap!='' || idGrupoSelecCheck==true){
		   datos[0]=map;
		    todoBien=true;
		 }
	   }
	}
	return datos;
}  