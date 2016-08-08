/**
Descripcion: Codigo controlador para la pagina grupos proyectos
Autor      : Mauricio Hernandez León
Fecha      : 04/01/2010
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/
$(document).ready(function(){ 						   
	$('#todos').click( function (event){ $('input[name=claves]').attr('checked', this.checked); });													
});						
						

function limpiar(){
		quitRow("detallesTabla");
		//$('#grupo').attr('value','');
}

function asignarGrupoProyecto(){
	var idgrupo = $('#grupo').attr('value');
	var html = "";
	controladorGruposPartidasRemoto.getGruposProyectos(idgrupo,{
        callback:function(items) {
			var texto = items;
			texto = texto.replace('"',"");
			 html= "<table width='500' border='0' cellspacing='0' cellpadding='0'>" +
				  "<tr>"+
					"<td height='20' width='150'>Grupo de Proyecto asignado:</td>"+
				  "</tr>"+
				  "<tr>"+
					"<td height='20'><select name='cbogrupos' id='cbogrupos' class='comboBox' style='width:450px'>"+texto+"</select></td>"+
				  "</tr>"+
				   "<tr>"+
				   	"<td height='25'></td>"+
				  "</tr>"+
				  "<tr>"+
					"<td height='50' align='center'><input type='button' value='Guardar' id='cmdguardarcm' class='botones'/>&nbsp;<input type='button' value='Cancelar' id='cmdcancelar' class='botones'/></td>"+
				  "</tr>"+
				"</table>";
				//alert(html);
				jWindow(html,'Grupos de Proyectos disponibles', '','',0);
				$('#cmdguardarcm').click(function(event){guardarGrupoProyecto();})
				$('#cmdcancelar').click(function(event){$.alerts._hide();})
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError(".<br><strong>Es probable que el grupo de partidas este vacío, asigne almenos una partida en el listado y vuelva a intentar esta operación.</strong><br>"+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName);    
        }
    }); 
}

function guardarGrupoProyecto(){
	var idGrupoProyecto = $('#cbogrupos').val();
	var idGrupoPartida = $('#grupo').val();
	if(idGrupoProyecto==0) {jAlert('El Grupo de Proyecto que desea asignar no es válido','Advertencia'); return false;}
	ShowDelay('Guardando Grupo de Proyectos','');
	controladorGruposPartidasRemoto.guardarGrupoProyectoEnPartidas(idGrupoProyecto, idGrupoPartida,{
        callback:function(items){
			if(items) {CloseDelay('Grupo de Proyectos guardado con éxito',2000); }
		} 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); 	
}

 function pintarTablaDetalles() {
	 quitRow("detallesTabla");
	var grupo=$('#grupo').attr('value');	
	var capitulo=$('#capitulo').attr('value');
	if (grupo!="" && capitulo !="" ) {
	ShowDelay('Cargando listado de partidas','');
	controladorGruposPartidasRemoto.getGrupoPartidas(grupo, capitulo, {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		    pintaTabla( "detallesTabla", i+1 ,this.CLV_PARTID,this.PARTIDA,getHTML(this.ID_PARTIDAS_GRUPO));
        }); 		
		_closeDelay();			   									
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); }

 }

  function pintaTabla( table, consecutivo,idcapitulo,partida, idcapituloGrupo){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
	var selected="";
	if (idcapituloGrupo!="")
	    selected="checked";
    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+idcapitulo+"' "+selected+" >";
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(idcapitulo,centro,"","") );	  
	row.appendChild( Td(partida,"","","") );
	tabla.appendChild( row );
 }

  function guardarProyectosGrupos(){
	 var checkProyectos = [];
	 var idgrupo = $('#grupo').attr('value');
     $('input[name=claves]:checked').each(function() {checkProyectos.push($(this).val());	 });
	 ShowDelay('Guardando listado de partidas del grupo','');
     controladorGruposPartidasRemoto.guardarPartidaGrupo(checkProyectos,idgrupo,$('#capitulo').attr('value'),{
     callback:function(items){
			limpiar();
		    CloseDelay("Información guardada con éxito");
			$('#grupo').val(idgrupo);
			pintarTablaDetalles();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); 
	 }