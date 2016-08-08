
function limpiarForma(){
  $('#descripcion' ).attr('value',"");
   $('#estatus').attr('checked',true);	
  $('#id' ).attr('value',"");
} 

function modificarDatos(id) {	 
	ShowDelay('Cargando rol de sistema para editar','');
	controladorRolRemoto.buscarTipoRol(id,  {
        callback:function(items) { 						
		$('#id' ).attr('value',items.ID_ROL);
		$('#descripcion' ).attr('value',items.ROL_DESCRIPCION);
		if (items.ROL_ESTADO=='ACTIVO')
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
 if ($("descripcion").attr('value')=='') error+="-Descripcion\n";
 if (error=="")   	{
	var descripcion= $('#descripcion' ).attr('value');
	var estatus='ACTIVO';
	if (!$('#estatus').attr('checked'))	
	   estatus='INACTIVO';	
	var id=  $('#id').attr('value');
	ShowDelay('Guardando información del rol','');
	controladorRolRemoto.guardarTipoRol(id,descripcion,estatus, {
        callback:function(items) {
        CloseDelay("Rol guardado con éxito",2000, function(){
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
 
 function eliminarDatos() {	 
 	  var checados= [];
     $('input[name=claves]:checked').each(function() {checados.push($(this).val());	 });	 
	if ( checados.length > 0  )  {
			jConfirm('¿Confirma que desea eliminar el rol?','Confirmar', function(r){
					if(r){
							controladorRolRemoto.eliminarTipoRol(checados,  {
							callback:function(items) { 		
								CloseDelay("Rol eliminado con éxito", 2000, function(){llenarTabla();});
							} 					   				
							,
							errorHandler:function(errorString, exception) { 
							   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
							}
							}); 
					}
				})
					
	
	} else {
		  jAlert("Tiene que seleccionar por lo menos un elemento para eliminarlo", "Advertencia");
	}	     	  
 }


 function llenarTabla() {	 
	quitRow( "tablaTipoRol" );
	controladorRolRemoto.buscarTiposRoles( {
        callback:function(items) { 		
         for (var i=0; i<items.length; i++ )  {			
			var reg = items[i];
			var id = reg.ID_ROL;
			var descripcion=  reg.ROL_DESCRIPCION;
			var estatus = reg.ROL_ESTADO;			
		    pintaTabla( "tablaTipoRol", i+1 ,id,descripcion,estatus);
        } 					   				
		//llenarComboCatalogo(items,document.getElementById( 'cbSubfuncion' ));		
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    });         		     	  
 }

//pinta en pantalla el resultado obtenido 
 function pintaTabla( table, consecutivo,id,descripcion,estatus ){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row = document.createElement( "TR" );		 	
	var html = "<input type='checkbox' name='claves' id='claves' value='"+id+"'>";
    var html2 = "<img style='cursor: pointer;' src=\"../../imagenes/page_white_edit.png\" class=\"imagen_cursor\" alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='modificarDatos("+id+")' >";
    row.appendChild( Td( "",centro, "",html )); 	
 	row.appendChild( Td(descripcion,izquierda,  "","" ) );
 	row.appendChild( Td(estatus,"", "", "" ) );
    row.appendChild( Td( "",centro,"", html2 )); 	
 	tabla.appendChild( row );
 }

