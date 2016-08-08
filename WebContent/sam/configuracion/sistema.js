function limpiarForma(){
  $('#descripcion' ).attr('value',"");
  $('#estatus').attr('checked',true);	
  $( '#id' ).attr('value',"");
} 

function modificarDato(id) {	
	ShowDelay('Cargando información para editar',''); 
	controladorSistemaRemoto.buscarSistema(id,  {
        callback:function(items) { 
		_closeDelay();						
		$( '#id' ).attr('value',items.ID_SISTEMA);
		$( '#descripcion' ).attr('value',items.SIS_DESCRIPCION);
		 if (items.SIS_ESTATUS=='ACTIVO')
		   $('#estatus').attr('checked',true);			 
		 else
		   $('#estatus').attr('checked',false);		
		  
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
 if (error=="")   	{
	var descripcion= $('#descripcion' ).attr('value');
	var estatus='ACTIVO';
	if (!$('#estatus').attr('checked'))	
	   estatus='INACTIVO';	
	var id=  $('#id').attr('value');
	ShowDelay('Guardando configuracion de los sistemas','');
	controladorSistemaRemoto.guardarSistema(id,descripcion,estatus, {
        callback:function(items) {
        limpiarForma(); 		
        CloseDelay("Información guardado con éxito",2000, function(){llenarTabla();});
		//llenarComboCatalogo(items,document.getElementById( 'cbSubfuncion' ));		
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); 
	} else {
		  jInformation("Los siguientes datos son requeridos\n"+error);
	}	     	  
 }
 
 function eliminarDato() {	 
	  var checados= [];
     $('input[name=idSistemas]:checked').each(function() {checados.push($(this).val());	 });	 
	if ( checados.length > 0  )  {
		ShowDelay('Eliminando sistemas seleccionados','');
	    controladorSistemaRemoto.eliminarSistema(checados,  {
        callback:function(items) { 		
        	CloseDelay("Modulo(s) eliminaodo(s) con éxito", 2000, function(){llenarTabla();});		
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); 
	} else {
		  jInformation("Tiene que seleccionar por lo menos un elemento para eliminarlo");
	}	     	  
 }


 function llenarTabla() {	 
	quitRow( "tablaSistema" );
	controladorSistemaRemoto.buscarSistemas(  {
        callback:function(items) { 		
         for (var i=0; i<items.length; i++ )  {			
			var reg = items[i];
			var id = reg.ID_SISTEMA;
			var descripcion=  reg.SIS_DESCRIPCION;
			var estatus = reg.SIS_ESTATUS;
		    pintaTabla( "tablaSistema", i+1 ,id,descripcion,estatus );
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
	var html = "<input type='checkbox' name='idSistemas' id='idSistemas' value='"+id+"'>";
    var html2 = "<img style='cursor: pointer;' src=\"../../imagenes/page_white_edit.png\" class=\"imagen_cursor\" alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='modificarDato("+id+")' >";
    row.appendChild( Td("", centro,"", html )); 	
 	row.appendChild( Td( descripcion,"", "","" ) );
 	row.appendChild( Td( estatus,centro, "","" ) );
    row.appendChild( Td("", centro,"", html2 )); 	
 	tabla.appendChild( row );
 }

