/**
Descripcion: Codigo controlador para cambiar el password 
Autor      : Israel de la Cruz
Fecha      : 14/07/2009
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/
$(document).ready(function() {  
  //Define el evento click del boton cmdcambiarpassword
  $('#cmdcambiarpassword').click(function () { CambiarPassword(); });
  
});

/*function windows()
{
	jWindow('<iframe src="http://www.google.com" width="400"></iframe>','Nueva ventana2','Guardar','Cancelar', 2);
}*/

/**
*Funcion que permite validar la informacion para cambiar el password
*@return Un valor verdadero si no hay errores o Falso si los hay
*/

function CambiarPassword()
{		
	var error="";
	var titulo ='Advertencia';
	if ($('#txtcontraseñaanterior').attr('value')=="") {jAlert('La contraseña anterior no es válida</br>', titulo); return false;}
	if($('#txtnuevacontraseña').attr('value')==""&&error=="") {jAlert('La nueva contraseña no es válida</br>', titulo); return false;}
	if($('#txtnuevacontraseña').attr('value').length<6&&error=="") {jAlert('La nueva contraseña debe tener al menos 6 caracteres alfanumericos</br>', titulo); return false;}
	if($('#txtconfirmarcontraseña').attr('value').length<6&&error=="") {jAlert('La contraseña de confirmación debe tener al menos 6 caracteres alfanumericos</br>'); return false;}
	if($('#txtnuevacontraseña').attr('value')!=$('#txtconfirmarcontraseña').attr('value')&&error=="") {jAlert('Las contraseñas escritas no coinciden, vuelva a intentarlo</br>',titulo); return false;}
	
	jConfirm('¿Confirma que desea cambiar su password actual?', 'Confirmar', 
			function(r) {
					if(r){
							ShowDelay('Cambiando el password','');
							controladorCambioPassword.guardarPassword($('#txtcontraseñaanterior').attr('value'), $('#txtnuevacontraseña').attr('value'),{
								 callback:function(items) {
									if(items){
										 CloseDelay('La contraseña se ha cambiado satisfactoriamente', 2000, function(){
											
											    $('#txtcontraseñaanterior').attr('value','');
												$('#txtnuevacontraseña').attr('value','');
												$('#txtconfirmarcontraseña').attr('value','');
										 });
										
									}
									else
									{
										jError("No se ha podido cambiar el password por que la informacion proporcionada es incorrecta", "Error");   
									}
									
								 }	
								,errorHandler:function(errorString, exception) { 
									jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador", "Error fatal");   
									}
								});
						}
				});

}
