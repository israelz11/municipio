/**
Descripcion: Codigo controlador para la pagina grupos de vales
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
		$('#grupo').attr('value','');
}

 function pintarTablaDetalles() {
	 quitRow("detallesTabla");
	var grupo=$('#grupo').attr('value');	
	if (grupo!=""  ) {
	ShowDelay('Cargando tipos de vales','');
	controladorGruposValesRemoto.getGrupoVales(grupo, {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		    pintaTabla( "detallesTabla", i+1 ,this.CLAVE_VALE,this.TIPO_VALE,getHTML(this.ID_GRUPO_VALE),this.ID_TIPO_VALE);
        }); 	
		_closeDelay();				   									
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); }

 }

  function pintaTabla( table, consecutivo,clave,descripcion, idclaveGrupo,idTipoVale){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
	var selected="";
	if (idclaveGrupo!="")
	   selected="checked";
    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+idTipoVale+"' "+selected+" >";
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(clave,centro,"","") );	  
	row.appendChild( Td(descripcion,"","","") );
	tabla.appendChild( row );
 }
 
 
  function guardarValesGrupos(){
	  var checkVales = [];
     $('input[name=claves]:checked').each(function() {checkVales.push($(this).val());	 });
	 if($('#grupo').attr('value')==0) {jAlert('El grupo seleccionado no es valido','Advertencia');return false;}
	 ShowDelay('Guardando tipos de vales','');
     controladorGruposValesRemoto.guardarValeGrupo(checkVales,grupo=$('#grupo').attr('value'),{
        callback:function(items) {
			limpiar();
		    CloseDelay("Información guardada con éxito");
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); 
	 }