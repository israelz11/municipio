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
	$('#cmdcambiarpassword').click(function () {CambioPasswod(); 
	
	});
});

/*function windows()
{
	jWindow('<iframe src="http://www.google.com" width="400"></iframe>','Nueva ventana2','Guardar','Cancelar', 2);
}*/

/**
*Funcion que permite validar la informacion para cambiar el password
*@return Un valor verdadero si no hay errores o Falso si los hay
*/
function CambioPasswod(){
	
	var passnueva=$('#txtconfirmarcontraseña').attr('value');
	var passanterior=$('#txtnuevacontraseña').attr('value');
	var error="";
	
	if ($('#txtcontraseñaanterior').attr('value')=="") {
		sweetAlert("Oops...", "No se escrito la contraseña anterior!", "error"); return false;}
	if($('#txtnuevacontraseña').attr('value')==""&&error=="") {
		sweetAlert("Oops...", "La nueva contraseña no ha sido escrita!", "error"); return false;}
	if($('#txtnuevacontraseña').attr('value').length<6&&error=="") {
		sweetAlert("Oops...", "La nueva contraseña debe tener al menos 6 caracteres alfanumericos", "error"); return false;}
		
	if($('#txtconfirmarcontraseña').attr('value').length<6&&error=="") {
		
	sweetAlert("Oops...", "La contraseña de confirmación no ha sido escrita!", "error"); return false;}
	if($('#txtnuevacontraseña').attr('value')!=$('#txtconfirmarcontraseña').attr('value')&&error=="") {
		
	sweetAlert("Oops...", "Las contraseñas escritas no coinciden, vuelva a intentarlo", "error"); return false;}
	alert("pruba wheet"&&passanterior&&passnueva);
	
	if ((passnueva==passanterior)){
		
		swal({
			  title: "Confirmar ",
			  text: "¿Confirma que desea cambiar su password actual?",
			  type: "info",
			  showCancelButton: true,
			  closeOnConfirm: false,
			  showLoaderOnConfirm: true,
			  
			},
			function(){
			  setTimeout(function(){
			    swal("La contraseña se ha cambiado satisfactoriamente!");
			  }, 2000);
			   
				controladorCambioPassword.guardarPassword($('#txtcontraseñaanterior').attr('value'), $('#txtnuevacontraseña').attr('value'));
			});
		
			}
	
	}
	
	
	


