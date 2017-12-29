/**
Descripcion: Codigo manejador para lst_proveedores.jsp
Autor      : Israel de la Cruz
Fecha      : 09/02/2012
*/
var guardado = false;

$(document).ready(function() {  
	//implementando manejadores de eventos
 	$('#btnBuscar').click(function (event){buscarBeneficiarios()});
 	$('#cmdnuevob').click(function (event){nuevoEditarBeneficiario(0)});
 	$('#cmdnuevor').click(function (event){nuevoEditarRepresentante(0)});
 	//getBeneficiarios('txtprestadorservicio','CVE_BENEFI','');
 	
 	
});



function compruebaVariable(){
	if(guardado) 
		buscarBeneficiarios();
	else
		_closeDelay();
}

function cambiarVariable(razonSocial){
	guardado = true;
	$('#txtprestadorservicio').attr('value', razonSocial);
	
}

function getReporteBenefi(id){
	jAlert('Operacion no disponible por el momento','Advertencia');
}

//swal2-confirm swal2-styled
function nuevoEditarBeneficiario(idBeneficiario){
	var titulo = (idBeneficiario==0) ? "Nuevo beneficiario": "Editar beneficiario";
	
	swal({
		  title: '',
		  text: '',
		  html:
			  '<iframe width="800" height="470" name="BENEFI" id="BENEFI" frameborder="0" src="../../sam/ordenesdepago/beneficiario.action?id='+idBeneficiario+'"></iframe>',
		  width: 800,
		  padding: 10,
		  animation: false,
		  confirmButtonText: 'Cerrar'
		  
		})
	
	//jWindow('<iframe width="800" height="400" name="BENEFI" id="BENEFI" frameborder="0" src="../../sam/ordenesdepago/beneficiario.action?id='+idBeneficiario+'"></iframe>', titulo, '','',0, function(){compruebaVariable();});
	
}

//swal2-confirm swal2-styled
function nuevoEditarRepresentante(idBeneficiario){
	var titulo = (idBeneficiario==0) ? "Nuevo representante": "Editar representante";
	
	swal({
		  title: '',
		  text: '',
		  html:
			  '<iframe width="800" height="470" name="BENEFI" id="BENEFI" frameborder="0" src="../../sam/ordenesdepago/beneficiario.action?id='+idBeneficiario+'"></iframe>',
		  width: 800,
		  padding: 10,
		  animation: false,
		  confirmButtonText: 'Cerrar'
		  
		})
	
	//jWindow('<iframe width="800" height="400" name="BENEFI" id="BENEFI" frameborder="0" src="../../sam/ordenesdepago/beneficiario.action?id='+idBeneficiario+'"></iframe>', titulo, '','',0, function(){compruebaVariable();});
	
}

function nuevoBeneficiario(){
	$('#nuevo_beneficiario').click(function(){
		
	});
}

/*Para dar de baja al beneficiario, anexaremos la fecha de la baja*/
function deshabilitarBeneficiario(id_beneficiario){
	
	swal({
		  title: 'Estas seguro?',
		  text: "El cambio no podra revertirse!",
		  type: 'warning',
		  showCancelButton: true,
		  confirmButtonText: 'Si, Cancelar!',
		  cancelButtonText: 'No, Abortar!',
		  confirmButtonClass: 'btn btn-success',
		  cancelButtonClass: 'btn btn-danger',
		  buttonsStyling: true
		}).then(function (r) {
		  if(r){
			  ShowDelay('Deshabilitando beneficiario','');
			  ControladorListadoBeneficiariosRemoto.deshabilitarBeneficiario(id_beneficiario,{
						  callback:function(items){
								 CloseDelay('Beneficiario deshabilitado con exito', function(){
											buscarBeneficiarios();
									 });
									
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>', 'Error al guardar Pedido');   
						return false;
					}
				});	
		}
		}, function (dismiss) {
		  // dismiss can be 'cancel', 'overlay',
		  // 'close', and 'timer'
		  if (dismiss === 'cancel') {
		    swal(
		      'Abortado',
		      'Beneficiario sin modificación :)',
		      'error'
		    )
		  }
		})
	/*
	swal({
		  title: "Estas seguro?",
		  text: "You will not be able to recover this imaginary file!",
		  type: "warning",
		  showCancelButton: true,
		  confirmButtonColor: "#DD6B55",
		  confirmButtonText: "Si, Deshabilitar!",
		  cancelButtonText: "No, Cancelar!",
		  closeOnConfirm: false,
		  closeOnCancel: false
		  
		},
		function(isConfirm){
		  if (isConfirm) {
		    swal("Deshabilitar!", "Deshabilitando beneficiario.", "success");
		    ControladorListadoBeneficiariosRemoto.deshabilitarBeneficiario(id_beneficiario),
		    function(items){buscarBeneficiarios();},
		    function(){buscarBeneficiarios();};
		    //callback:function(items)
		    
		  } else {
			    swal("Cancelado", "No se modifico el estatus :)", "error");
		  }
		});*/
}



function habilitarBeneficiario(id_beneficiario){
	jConfirm('¿Confirma que desea volver a habilitar el beneficiario?','Confirmar', function(r){
		if(r){
			ShowDelay('Habilitando beneficiario','');
			ControladorListadoBeneficiariosRemoto.habilitarBeneficiario(id_beneficiario,{
					  callback:function(items){
							 CloseDelay('Beneficiario habilitado con exito', function(){
										buscarBeneficiarios();
								 });
								
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>', 'Error al guardar Pedido');   
					return false;
				}
			});	
		}
	});
}

function buscarBeneficiarios(){
	$('#forma').attr('action', 'lst_proveedores.action');
	$('#forma').submit();
}
