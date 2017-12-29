/**
Descripcion: Codigo controlador para la pagina grupos.jsp
Autor      : Mauricio Hernandez
Fecha      : 26/10/2009
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/


$(document).ready(function() {
	
	$('#btnGrabar').on('click', function(){
		guardar();
	});
	
	$('#btnlimpiar').on('click', function(){
		limpiar();
	});
	
	$('#grupo').on('change', function(){
		pintarTablaDetalles();
		// alert( this.value );
	});
	$('#btnEliminar').on('click', function(){
		eliminar();
	});
	
	
});

function limpiar(){
			 $('#tipoFirma').val('');			 
			 $('#representante').val('');
			 $('#cargo').val('');
			 $('#clave').val('');
}



function guardar(){			
    var error="";
	var titulo ='Advertencia';
	if ( $('#grupo').val()=="")  error += 'Grupo</br>';	
	if ( $('#tipoFirma').val()=="")  error += 'Tipo Firma</br>';	
	if ( $('#representante').val()=="")  error += 'Representante</br>';	
	if ( $('#cargo').val()=="")  error += 'Cargo</br>';	
	if ( error=="") {	
    controladorFirmasGruposRemoto.guardarFirmaGrupo($('#clave').val(),  $('#tipoFirma').val() ,$('#cargo').val(), $('#representante').val(), $('#grupo').val(),{
			 callback:function(items) {			  			  
			 if (items==true)
				 swal({title: 'La información se almaceno satisfactoriamente',type: 'info'});
			 else
				 swal({title: 'La información no se almaceno, el tipo de firma puede estar repetido',type: 'error'});
			 pintarTablaDetalles();
 		     }	
								,errorHandler:function(errorString, exception) { 
								   swal("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});		}else swal({title:error,type:titulo});	
}

 function pintarTablaDetalles() {
	 quitRow("detallesTabla");
	var grupo=$('#grupo').val();	
	if (grupo!="") {
	ShowDelay('Cargando firmas de grupos','');
	controladorFirmasGruposRemoto.getFirmasPorGrupo(grupo, {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		    pintaTabla( "detallesTabla", i+1 ,this.ID_FIRMA_GRUPO,this.TIPO,this.CARGO,this.REPRESENTANTE,this.ID_GRUPO_CONFIG);
        }); 					   						
			//limpiar();
			//_closeDelay();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           swal("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); }

 }

  function pintaTabla( table, consecutivo,id,tipo,cargo,representante,grupo){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+id+"' >";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;text-align: left;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editar("+id+",'"+tipo+"','"+cargo+"','"+representante+"',"+grupo+")\" >"; 		
	row.appendChild( Td("",centro,"",htmlCheck) );
	row.appendChild( Td(tipo,"text-align:left;","","") );	  
	row.appendChild( Td(representante,"text-align:left;","","") );
	row.appendChild( Td(cargo,"text-align:left;","","") );
    row.appendChild( Td("",centro,"",htmlEdit) );	
	tabla.appendChild( row );
 }
 
 
 function editar(id,tipo,cargo,representante,grupo) {
		  $('#grupo').val(grupo);			 
		  $('#tipoFirma').val(tipo);			 
		 $('#representante').val(representante);
		 $('#cargo').val(cargo);
		 $('#clave').val(id);
 }
 
  function eliminar(){
	  var checkRetenciones = [];
     $('input[name=claves]:checked').each(function() {checkRetenciones.push($(this).val());	 });	 
	 if (checkRetenciones.length > 0 ) {
		
		 swal({
			  title: 'Estas seguro?',
			  text: "¿Confirma que desea aliminar el elemento de firma actual?",
			  type: 'warning',
			  showCancelButton: true,
			  confirmButtonColor: '#3085d6',
			  cancelButtonColor: '#d33',
			  confirmButtonText: 'Sí, confirmar!',
			  cancelButtonText: 'No, cancelar!',
			  confirmButtonClass: 'btn btn-success',
			  cancelButtonClass: 'btn btn-danger',
			  buttonsStyling: false
			}).then(function (r) {
			  swal('Cancelado!','Grupo(s) de firma(s) eliminaoo satisfcatoriamente!','success')
			  /*clase para cencelacion*/
				if(r){
					  ShowDelay('Eliminando grupo(s) de firma(s)','');
				      controladorFirmasGruposRemoto.eliminarFirmaGrupo(checkRetenciones, {
				    	  callback:function(items) {
								   CloseDelay('Grupo(s) de firma(s) eliminaoo satisfcatoriamente', 2000, function(){
										pintarTablaDetalles();
									 });
								   
								} 					   				
								,
								errorHandler:function(errorString, exception) { 
								swal("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
					  		});
					
					}
			  /*cancelacion cirre*/
			}, function (dismiss) {
			  // dismiss can be 'cancel', 'overlay',
			  // 'close', and 'timer'
			  if (dismiss === 'cancel') {
			    swal(
			      'Cancelado',
			      'El proceso no fue ejecutado',
			      'error'
			    )
			  }
			})
		 /*
		 jConfirm('¿Confirma que desea aliminar el elemento de firma actual?','Confirmar', function(r){
			 	if(r){
						ShowDelay('Eliminando grupo(s) de firma(s)','');
						 controladorFirmasGruposRemoto.eliminarFirmaGrupo(checkRetenciones, {
							callback:function(items) {
							   CloseDelay('Grupo(s) de firma(s) eliminaoo satisfcatoriamente', 2000, function(){
									pintarTablaDetalles();
								 });
							   
							} 					   				
							,
							errorHandler:function(errorString, exception) { 
							swal("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
							}
				  		});
				}
		 
    });*/ } else 
	    	swal({title: 'Es necesario que seleccione un elemento de la lista',type: 'error'});
	    	
	 }