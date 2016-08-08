/**
Descripcion: Codigo manejador para lst_proveedores.jsp
Autor      : Israel de la Cruz
Fecha      : 09/02/2012
*/
var guardado = false;

$(document).ready(function() {  
	//implementando manejadores de eventos
 	$('#btnBuscar').click(function (event){buscarBeneficiarios()});
 	$('#cmdnuevo').click(function (event){nuevoEditarBeneficiario(0)});

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

function nuevoEditarBeneficiario(idBeneficiario){
	var titulo = (idBeneficiario==0) ? "Nuevo beneficiario": "Editar beneficiario";
	jWindow('<iframe width="800" height="400" name="BENEFI" id="BENEFI" frameborder="0" src="../../sam/ordenesdepago/beneficiario.action?id='+idBeneficiario+'"></iframe>', titulo, '','',0, function(){compruebaVariable();});
}

function deshabilitarBeneficiario(id_beneficiario){
	jConfirm('¿Confirma que desea deshabilitar al beneficiario?','Confirmar', function(r){
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
	});
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
