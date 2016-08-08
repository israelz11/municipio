$(document).ready(function() { 
	getUnidad_Medidas('txtunidadmedida','CVE_UNIDAD_MEDIDA');	
	getBeneficiarios('txtprestadorservicio','CVE_BENEFI',$('#tipoBeneficiario').attr('value'));				   
});

function limpiarForma(){  
  $('#estatus').attr('checked',true);
  $( '#id' ).attr('value',"");
//  $('#grupo' ).attr('value',"");
//  $('#grupo' ).attr('value',"");
  $('#descripcion' ).attr('value',"");
  $('#txtunidadmedida' ).attr('value',"");
  $('#precio' ).attr('value',"");
  $('#CVE_UNIDAD_MEDIDA' ).attr('value',"");  
  $('#beneficiario' ).text('');
  $('#inventariable' ).attr('checked',false);
  $('#consumible' ).attr('checked',false);
  $('#CVE_BENEFI').attr('value',0);
  $('#txtprestadorservicio').attr('value','');
} 

function modificarDato(id) {
	ShowDelay('Cargando artículo para editar información','');	 
	controladorArticulosRemoto.getArticulo(id,  {
        callback:function(items) { 						
		$( '#id' ).attr('value',items.ID_CAT_ARTICULO);
//		$( '#partida' ).attr('value',items.GRUPO);
		
		if(items.ULT_BENEFI!=null)
			$('#CVE_BENEFI').attr('value',items.ULT_BENEFI);
		else
			$('#CVE_BENEFI').attr('value',0);
			
		if(items.NCOMERCIA!=null)
			$('#txtprestadorservicio').attr('value',items.NCOMERCIA);		
		else 
			$('#txtprestadorservicio').attr('value','');
			
		$('#CVE_UNIDAD_MEDIDA').attr('value',getHTML(items.CLV_UNIMED));
		$('#descripcion').attr('value',items.DESCRIPCION);
		$('#precio').attr('value',getHTML(items.ULT_PRECIO));		
		$('#txtunidadmedida').attr('value',getHTML(items.UNIDMEDIDA));		
		if (items.INVENT!='')
		  $('#inventariable').attr('checked',true);			 
		else
		  $('#inventariable').attr('checked',false);			 		
		if (items.CONSUM!='')
		  $('#consumible').attr('checked',true);			 
		else
		  $('#consumible').attr('checked',false);			 
		
		 if (items.ESTATUS=='ACTIVO')
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
	if ($("#descripcion").attr('value')==""){jAlert('La Descripcion del artículo no es válida', 'Advertencia'); return false;}
	if ($("#CVE_UNIDAD_MEDIDA").attr('value')=="") {jAlert('La Unidad de Medida no es válida','Advertencia'); return false;}
	if ($("#precio").attr('value')=="") {jAlert('El Precio del artículo no es válido', 'Advertencia'); return false;}
	/*buscar si ya esta usandose en otros documentos*/
	if($('#id').attr('value')!=''&&$('#id').attr('value')!='0'){
		controladorArticulosRemoto.getExistenciaDocumentos($('#id').attr('value'), {
				callback:function(items){
					if(items) {
						alert('El artículo ó producto actual ya se encuentra relacionado a una Orde de Trabajo/Orden de Servicio si desea realizar algun cambio en la descripción afectara la relación existente, no se recomienda realizar esta acción, en su lugar puede crear un nuevo artículo');
						guardar();
					}
					else
						guardar();
					
				}
			});
	}
	else{
						guardar();
					}


 }
 
 function guardar(){
	 jConfirm('¿Confirma que desea guardar la información del artículo?','Confirmar', function(r){
		if(r){
			var inventariable=0;
			var consumible=0;
			var estatus='ACTIVO';
			if (!$('#estatus').attr('checked'))	
			   estatus='INACTIVO';	
			if ($('#inventariable').attr('checked'))	
			   inventariable=1;	
			if ($('#consumible').attr('checked'))	
			   consumible=1;	   
			ShowDelay('Guardando articulo','');
			controladorArticulosRemoto.guardarArticulo($('#id').attr('value'),$("#CVE_UNIDAD_MEDIDA").attr('value'),$("#descripcion").attr('value'),$('#CVE_BENEFI').attr('value'),$("#precio").attr('value'),inventariable,consumible,estatus,{
				callback:function(items) {
					CloseDelay("Artículo guardado satisfactoriamente",2000, function(){
							$('#alfabetico').attr('value',$('#descripcion').attr('value'))
							limpiarForma(); 		
							llenarTabla();
					});	
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
				   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
				}
			}); 
		}
	}); 	  
 }
 
 function eliminarDato() {	 
	  var checados= [];
     $('input[name=idArticulos]:checked').each(function() {checados.push($(this).val());	 });	 
	if(checados.length>0){
		jConfirm('¿Confirma que desea eliminar los artículos seleccionados?','Confirmar', function(r){
			if(r){
				ShowDelay('Eliminando artículos','');		
				controladorArticulosRemoto.eliminarArticulo(checados,  {
					callback:function(items) { 		
						CloseDelay("Articulo(s) eliminado(s) con éxito", 2000, function(){
								llenarTabla();
								limpiarForma()
						});	
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
					}
				});	
			}
		});
	} else {
		  jAlert("Seleccione almenos un elemento de la lista para eliminarlo", "Advertencia");
	}	     	  
 }

 function llenarTabla() {	 
	quitRow("tablaArticulos");
	if ($("#alfabetico").attr('value')!='')  {
	ShowDelay('Cargando la lista de artículos','');
	controladorArticulosRemoto.getArticulosTodos($("#alfabetico").attr('value'),  {
        callback:function(items) { 		
         for (var i=0; i<items.length; i++ )  {			
			var reg = items[i];
			var id = reg.ID_CAT_ARTICULO;
			var descripcion=  reg.DESCRIPCION;
	  		var txtunidadmedida=  getHTML( reg.UNIDMEDIDA);
  			var precio=  getHTML(reg.ULT_PRECIO);  
			var estatus = reg.ESTATUS;
			var beneficiario = getHTML( reg.NCOMERCIA);			
		    pintaTabla("tablaArticulos", i+1 ,id,descripcion,txtunidadmedida,precio,estatus,beneficiario );			
        }
		_closeDelay();
		
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); 
	}	
	else 
	      jAlert("No se puede realizar esta operación si no se ha especificado ninguna búsqueda", "Advertencia");
 }

//pinta en pantalla el resultado obtenido 
 function pintaTabla( table, consecutivo,id,descripcion,txtunidadmedida,precio,estatus,beneficiario ){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row = document.createElement( "TR" );		 	
	var html = "<input type='checkbox' name='idArticulos' id='idArticulos' value='"+id+"'>";
    var html2 = "<img style='cursor: pointer;' src=\"../../imagenes/page_white_edit.png\" class=\"imagen_cursor\" alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='modificarDato("+id+")' >";
    row.appendChild( Td("", centro,"", html )); 	
 	row.appendChild( Td( descripcion,"", "","" ) );
 	row.appendChild( Td( beneficiario,"", "","" ) );
 	row.appendChild( Td( txtunidadmedida,centro, "","" ) );
 	row.appendChild( Td( '$ '+formatNumber(precio),centro, "","" ) );
	row.appendChild( Td( estatus,centro, "","" ) );
    row.appendChild( Td("", centro,"", html2 )); 	
 	tabla.appendChild( row );
 }


