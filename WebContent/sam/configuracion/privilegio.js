var idModuloMof="";
function limpiarForma(){
  $('#descripcion' ).attr('value',"");
  $('#estatus').attr('checked',true);	
  $("input[name='tipo']").attr("checked","");
  $("input[name='tipo']:nth(0)").attr("checked","checked");
  $( '#id' ).attr('value',"");
  $( '#url' ).attr('value',"");
  $( '#orden' ).attr('value',"");
} 

function modificarDatos(id) {	 
	ShowDelay('Cargando opciones del módulo','');
	controladorPrivilegioRemoto.buscarPrivilegio(id,  {
        callback:function(items) { 						
		$( '#id' ).attr('value',items.ID_PRIVILEGIO);
		$( '#descripcion' ).attr('value',items.PRI_DESCRIPCION);
		$( '#sistema' ).attr('value',items.ID_SISTEMA);
		if (items.PRI_ESTATUS=='ACTIVO')
		   $('#estatus').attr('checked',true);			 
		 else
		   $('#estatus').attr('checked',false);	        		
		$("input[name='tipo']").attr("checked","");
		if (items.TIPO   =='MENU')
		  $("input[name='tipo']:nth(0)").attr("checked","checked");
		else
		 $("input[name='tipo']:nth(1)").attr("checked","checked");
		 validarTipo();
		 $( '#url' ).attr('value',getHTML(items.URL));
		 $( '#orden' ).attr('value',getHTML(items.ORDEN));
		idModuloMof=items.ID_MODULO;   
	 	getSelectSistema(); 
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
 if ($("#descripcion").attr('value')=="") error+="-Descripción\n";
 if ($("#sistema").attr('value')=="0") error+="-Sistema\n";
 if ($("#modulo").attr('value')=="0") error+="-Modulo\n";
 if ($("#url").attr('value')==""  && $('#tipo').attr('checked')  ) error+="-Url\n";
 if ($("#orden").attr('value')==""  && $('#tipo').attr('checked')  ) error+="-Orden de menu\n";
  
 if (error=="")   	{
	var descripcion= $('#descripcion' ).attr('value');
	var modulo= $('#modulo' ).attr('value');
	var estatus='ACTIVO';
	if (!$('#estatus').attr('checked'))	
	   estatus='INACTIVO';
	var tipo=$('input:radio[name=tipo]:checked').val();
	var id=  $('#id').attr('value');
	ShowDelay('Guardando opciones del módulo','');
	controladorPrivilegioRemoto.guardarPrivilegio(id,descripcion,estatus,modulo,tipo,$("#url").attr('value'),$("#orden").attr('value'), {
        callback:function(items) {
        CloseDelay("Opciones del modulo guardadas con exito", 2000, function(){
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
     $('input[name=idPrivilegios]:checked').each(function() {checados.push($(this).val());	 });	 
	if ( checados.length > 0  )  {	
			jConfirm('¿Confirma que desea eliminar la(s) opcion(es) del modulo actual?','Confirmar', function(r){
					if(r){
						ShowDelay('Eliminando opciones del modulo','');
						controladorPrivilegioRemoto.eliminarPrivilegio(checados,  {
						callback:function(items) { 		
							CloseDelay("Opciones del modulo eliminadas con éxito", 2000, function(){llenarTabla();limpiarForma();});	
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
						}
					}); 
				}
			});
				
				
	} else {
		  jAlert("Es necesario seleccionar almenos un elemento del listado para eliminarlo", "Advertencia");
	}	     	  
 }


 function llenarTabla() {	 
	quitRow( "detallesTabla" );
	var idModulo= $('#modulo' ).attr('value');
	if (idModulo!="0") {	
	ShowDelay('Cargando listado de opciones del módulo','');
	controladorPrivilegioRemoto.buscarPrivilegios(idModulo,  {
        callback:function(items) { 		
         for (var i=0; i<items.length; i++ )  {			
			var reg = items[i];
			var id = reg.ID_PRIVILEGIO;
			var descripcion=  reg.PRI_DESCRIPCION;
			var estatus = reg.PRI_ESTATUS;
			var modulo = reg.MODULO;
			var tipo = reg.TIPO;
			var url = reg.URL;
			var orden =reg.ORDEN;
		    pintaTabla( "detallesTabla", i+1 ,id,descripcion,estatus,modulo,tipo,url,orden );
        } 			
		limpiarForma();
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
 function pintaTabla( table, consecutivo,id,descripcion,estatus,modulo,tipo,url,orden ){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row = document.createElement( "TR" );		 	
	var html = "<input type='checkbox' name='idPrivilegios' id='idPrivilegios' value='"+id+"'>";
    var html2 = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='modificarDatos("+id+")' >";
    row.appendChild( Td("",centro ,"", html )); 	
	row.appendChild( Td( orden, centro,"","" ) );
 	row.appendChild( Td(descripcion,"", "", "" ) );
	row.appendChild( Td(getHTML(url),"", "", "" ) );
 	row.appendChild( Td( modulo, "" ,"","") );
 	row.appendChild( Td( tipo, "" ,"","") );
 	row.appendChild( Td( estatus, centro,"","" ) );
    row.appendChild( Td("",centro, "", html2 )); 	
 	tabla.appendChild( row );
 }


 function getSelectSistema() {
	 var idSistema=$('#sistema').attr('value'); 
	 dwr.util.removeAllOptions("modulo");
	 if (idSistema !="" ){
	ShowDelay('Cargando lista de modulos}','');
	controladorModuloRemoto.buscarModulos(idSistema, {
        callback:function(items) { 				
		 dwr.util.addOptions('modulo',{ '':'[Seleccione]' });
		 dwr.util.addOptions('modulo',items,"ID_MODULO", "MOD_DESCRIPCION" );
		 if (idModuloMof!=""){
		  $('#modulo').attr('value',idModuloMof);
		  idModuloMof="";
		  } else
		    limpiarForma();
			_closeDelay();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          				     	  
        }
    }); 
	 }
 }

function validarTipo() {
	$('#url' ).attr('value','');
  if ($('#tipo').attr('checked'))
     $('#url' ).attr('disabled',false);  
    else 
	 $('#url' ).attr('disabled',true);		
}
 
