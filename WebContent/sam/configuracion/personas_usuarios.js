/**
*Descripcion: Codigo controlador para la pagina Usuarios.jsp
*Autor      : Mauricio Hernandez
*Fecha      : 10/11/2009
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/
$(document).ready(function() {  
	getPersonas('nombre','clave');
});

var area="";
function limpiar(){	
	 		 $('#clave').attr('value','');
	 		 $('#nombre').attr('value','');
			 $('#apaterno').attr('value','');
			 $('#amaterno').attr('value','');
			 $('#curp').attr('value','');
			 $('#rfc').attr('value','');
			 $('#profesion').attr('value','');
			 $('#area').attr('value','');
			 $('#unidad').attr('value','');
			 $('#usuario').attr('value','');
			 $('#pass1').attr('value','');
			 $('#pass2').attr('value','');
			 $('#estatus').attr('checked',false);
			 $('#idUsuario').attr('value','');
			 $('#idTrabajador').attr('value','');
			 area="";
			 quitRow("detallesTabla");
}

function guardar(){			
    var error="";
	var titulo ='Advertencia - Informacion no valida';
	if ($('#nombre').attr('value')=="")  {jAlert('El nombre de usuario no es válido', 'Advertencia'); return false;}	
	if ($('#apaterno').attr('value')=="") {jAlert('El apellido paterno no es válido', 'Advertencia'); return false;}	
	if ($('#profesion').attr('value')=="") {jAlert('La profesion del usuario no es válida', 'Advertencia'); return false;}
	if ($('#unidad').attr('value')=="")  {jAlert('La unidad administrativa no es válida', 'Advertencia'); return false;}	
	//if ($('#usuario').attr('value')!="" && $('#area').attr('value')=="" ) {jAlert('El area de la unidad administrativa no es válida', 'Advertencia'); return false;}
	if ($('#usuario').attr('value')!="" && $('#pass1').attr('value')=="" && $('#clave').attr('value')=="" )  {jAlert('El password no es válido','Advertencia'); return false;}
	if ($('#usuario').attr('value')!="" && $('#pass1').attr('value')!=$('#pass2').attr('value')   )  {jAlert('Los password no coinciden','Advertencia'); return false;}
		
	var estatus='N';
	if ($('#estatus').attr('checked'))	
	   estatus='S';	
	   
	jConfirm('¿Confirma que desea guardar la información del usuarios?', 'Confirmar', function(r){
		if(r){
				ShowDelay('Guardando usuario','');
				controladorUsuariosRemoto.guardarUsuario($('#clave').attr('value'),$('#nombre').attr('value'),$('#apaterno').attr('value'),$('#amaterno').attr('value'),
						 $('#curp').attr('value'),$('#rfc').attr('value'),$('#profesion').attr('value'),0,
						 $('#unidad').attr('value'),$('#usuario').attr('value'),$('#pass1').attr('value'),
						 estatus,{
						 callback:function(items) {			  			  
							 CloseDelay("Información guardada con éxito",function(){
								limpiar(); 
							});
							ShowDelay('Recuperando infornmación','');
							getPersonas('nombre','clave');	
							_closeDelay();		 
						 
						 }	
											,errorHandler:function(errorString, exception) { 
											   jError("Fallo la operación:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
											}
						});		
		}
	});
}

 function buscar() {
	 quitRow("detallesTabla");
	 if ($('#nombre').attr('value')!="" || $('#apaterno').attr('value')!="" || $('#amaterno').attr('value')!="") {
	ShowDelay('Buscando usuario','');
	controladorUsuariosRemoto.getUsuariosPorEjemplo($('#nombre').attr('value'), $('#apaterno').attr('value'), $('#amaterno').attr('value'), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		     pintaTabla( "detallesTabla", i+1 ,this.CVE_PERS,this.NOMBRE,this.APE_PAT,getHTML(this.APE_MAT),getHTML(this.CURP),getHTML(this.RFC),getHTML(this.TRATAMIENTO),getHTML(this.ID_DEPENDENCIA),getHTML(this.ID_DEPENDENCIA),getHTML(this.LOGIN),getHTML(this.ACTIVO),getHTML(this.DEPENDENCIA),getHTML(this.ID_TRABAJADOR),getHTML(this.ID_USUARIO) );
        }); 					   						
			_closeDelay();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); }
	else
		jAlert("Es nesesario que haya un dato para realizar la busqueda","Advertencia");
	

 }

  function pintaTabla( table, consecutivo,idPersona,nombre,apaterno,amaterno,curp,rfc,profesion,idArea,idUnidad,login,estatus,unidad,idTrabajador,idUsuario){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editar("+idPersona+",'"+nombre+"','"+apaterno+"','"+amaterno+"','"+curp+"','"+rfc+"','"+profesion+"','"+idArea+"','"+idUnidad+"','"+login+"','"+estatus+"','"+idTrabajador+"','"+idUsuario+"')\" >"; 		
	row.appendChild( Td(idPersona,"","","") );	  
	row.appendChild( Td(profesion,"","","") );	  
	row.appendChild( Td(nombre+" "+apaterno+" "+amaterno,"","","") );	  
	row.appendChild( Td(unidad,centro,"","") );
	row.appendChild( Td(estatus,centro,"","") );
    row.appendChild( Td("",centro,"",htmlEdit) );	
	tabla.appendChild( row );
 }
 
 
 function editar(idPersona,nombre,apaterno,amaterno,curp,rfc,profesion,idArea,idUnidad,login,estatus,idTrabajador,idUsuario) {
		 $('#curp').attr('value',curp);
		 $('#rfc').attr('value',rfc);
		 $('#estatus').attr('checked',false);
		 $('#nombre').attr('value',nombre);
		 $('#apaterno').attr('value',apaterno);
		 $('#amaterno').attr('value',amaterno);
		 $('#profesion').attr('value',profesion);		 
		 $('#unidad').attr('value',idUnidad);
		 $('#usuario').attr('value',login);
		 if (estatus=='S')
		   $('#estatus').attr('checked',true);			 
		 else
		   $('#estatus').attr('checked',false);
		 $('#clave').attr('value',idPersona);
		 $('#idUsuario').attr('value',idUsuario);
		 $('#idTrabajador').attr('value',idTrabajador);
		 area=idArea;
		 getSelectUnidad();
 }
 
 
  function getSelectUnidad() {
	 var unidad=$('#unidad').attr('value'); 
	 dwr.util.removeAllOptions("area");
	 if (unidad !="" ){
	
	ShowDelay('Cargando información del usuario','');
	controladorUsuariosRemoto.getAreasUnidad(unidad, {
        callback:function(items) { 				
		 dwr.util.addOptions('area',{ '':'[Seleccione]' });
		 dwr.util.addOptions('area',items,"ID", "DEPENDENCIA" );
		 if (area!="")
		  $('#area').attr('value',area);
		  _closeDelay();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      				     	  
        }
    }); 
	 }
 }
 
