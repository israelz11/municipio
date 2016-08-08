function limpiarForma(){
  $('#descripcion' ).attr('value',"");
  $('#estatus').attr('checked',true);	
  $( '#id' ).attr('value',"");
} 

function modificarDatos(id) {
	ShowDelay('Cargando módulo para editar','');
	controladorModuloRemoto.buscarModulo(id,  {
        callback:function(items) { 						
		$( '#id' ).attr('value',items.ID_MODULO);
		$( '#descripcion' ).attr('value',items.MOD_DESCRIPCION);
		$( '#sistema' ).attr('value',items.ID_SISTEMA);
		if (items.MOD_ESTATUS=='ACTIVO')
		   $('#estatus').attr('checked',true);			 
		 else
		   $('#estatus').attr('checked',false);
		   
		 _closeDelay();	
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); 	     	  
 }


function guardarDatos() {	 
  var error="";  
 if ($("#descripcion").attr('value')=="") error+="-Descripcion\n";
 if ($("#sistema").attr('value')=="0") error+="-sistema\n";
 if ($("#estatus").attr('value')=="0") error+="-Estatus\n";
 if (error=="")   	{
	var descripcion= $('#descripcion' ).attr('value');
	var sistema= $('#sistema' ).attr('value');
	var estatus='ACTIVO';
	if (!$('#estatus').attr('checked'))	
	   estatus='INACTIVO';
	var id=  $('#id').attr('value');
	ShowDelay('Guardando informacioó del módulo','');
	controladorModuloRemoto.guardarModulo(id,descripcion,estatus,sistema, {
        callback:function(items) {
        
        CloseDelay("Informacion guardada con exito",2000, function(){
				limpiarForma(); 		
		llenarTabla();
			});
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 
	} else {
		  jAlert("Los siguientes datos son requeridos\n"+error, "Advertencia");
	}	     	  
 }
 
 function eliminar() {	 
      var checados= [];
     $('input[name=idSistemas]:checked').each(function() {checados.push($(this).val());	 });	 
	if ( checados.length > 0  )  {	
		jConfirm('¿Confirma que desea eliminar el modulo selecciomado?','Confirmar', function(r){
			if(r){
					ShowDelay('Eliminando modulo(s)','');
					controladorModuloRemoto.eliminarModulo(checados,  {
					callback:function(items) { 	
					CloseDelay("Modulo(s) eliminado(s) con éxito", 2000, function(){llenarTabla();});	
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
					}
				}); 
			}

		});
	} else {
		  jAlert("Es necesario seleccionar almenos un elemento de la lista para eliminarlo", "Advertencia");
	}	     	  
 }


 function llenarTabla() {	 
	quitRow( "tablaModulo" );
	var idSistema= $('#sistema' ).attr('value');
	if (idSistema!="0") {	
	ShowDelay('Cargando listado de los módulos','');
	controladorModuloRemoto.buscarModulos(idSistema,  {
        callback:function(items) { 		
         for (var i=0; i<items.length; i++ )  {			
			var reg = items[i];
			var id = reg.ID_MODULO;
			var descripcion=  reg.MOD_DESCRIPCION;
			var estatus = reg.MOD_ESTATUS;
			var sistema = reg.SISTEMA;
			var imagen = reg.IMAGEN;
		    pintaTabla( "tablaModulo", i+1 ,id,descripcion,estatus,sistema, imagen);
        } 					   			
		_closeDelay();	
		//llenarComboCatalogo(items,document.getElementById( 'cbSubfuncion' ));		
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); }        
}

//pinta en pantalla el resultado obtenido 
 function pintaTabla( table, consecutivo,id,descripcion,estatus,sistema, imagen ){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row = document.createElement( "TR" );		 	
	var html = "<input type='checkbox' name='idSistemas' id='idSistemas' value='"+id+"'>";
    var html2 = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='modificarDatos("+id+")' >";
    row.appendChild( Td("",centro ,"", html )); 	
 	row.appendChild( Td(descripcion,"", "", "" ) );
 	row.appendChild( Td( sistema, "" ,"","") );
	row.appendChild( Td( getHTML(imagen), "" ,"","") );
 	row.appendChild( Td( estatus, centro,"","" ) );
    row.appendChild( Td("",centro, "", html2 )); 	
 	tabla.appendChild( row );
 }

