// JavaScript Document

$(document).ready(function() {
		// Launch TipTip tooltip
		$('.tiptip a.button, .tiptip button').tipTip();
		popup();
	});

function valida_datos(){
	var primer_objeto = '';	
	if ($('#j_username').attr('value') == ''){jAlert('El usuario escrito no es válido','Advertencia'); return false;}
	if ($('#j_password').attr('value') == '') {jAlert('La contraseña no es válida','Advertencia');return false;}
	return true;
}


function validacion_ingreso() {
		if (valida_datos()) {
			IntroducirCookie();
			$ ('#acciones').value="autentificarse";
			$ ('#forma').submit();
		}
}
	
	function validar(e, element, caso ) { // 1
		tecla = ( document.all ) ? e.keyCode : e.which; // 2
		if ( tecla == 13 && element.value.length > 0)  {
	      if ( caso == 1 ) 
	         $ ( '#j_password' ).focus();
		  else
		     validacion_ingreso();
		}	  
	}
	
function popup(){
	window.open('popup.html','mywindow','width=500,height=210');
}

function CojerValorCookie(indice) {   
    //indice indica el comienzo del valor   
    var galleta = document.cookie  
    //busca el final del valor, dado por ;, a partir de indice   
    var finDeCadena = galleta.indexOf(";", indice)   
    //si no existe el ;, el final del valor lo marca la longitud total de la cookie   
    if (finDeCadena == -1)   
        finDeCadena = galleta.length  
  
    return unescape(galleta.substring(indice, finDeCadena))   
    }   
  
function CojerCookie(nombre) {   
    var galleta = document.cookie  
    //construye la cadena con el nombre del valor   
    var arg = nombre + "="  
    var alen = arg.length           //longitud del nombre del valor   
    var glen = galleta.length       //longitud de la cookie   
  
    var i = 0   
    while (i < glen) {   
        var j = i + alen            //posiciona j al final del nombre del valor   
        if (galleta.substring(i, j) == arg) //si en la cookie estamo ya en nombre del valor        
            return CojerValorCookie(j)  //devuleve el valor, que esta a partir de j   
  
        i = galleta.indexOf(" ", i) + 1     //pasa al siguiente   
        if (i == 0)   
            break               //in de la cookie   
    }   
    return null                 //no se encuentra el nombre del valor   
}   
  
function GuardarCookie (nombre, valor, caducidad)
{   
    if(!caducidad) caducidad = Caduca(0)   
    //crea la cookie: incluye el nombre, la caducidad y la ruta donde esta guardada   
    //cada valor esta separado por ; y un espacio   
    document.cookie = nombre + "=" + escape(valor) + "; expires=" + caducidad + "; path=/"  
}   
  
function Caduca(dias) {   
    var hoy = new Date()                                        //coge la fecha actual   
    var msEnXDias = eval(dias) * 24 * 60 * 60 * 1000    //pasa los dias a mseg.   
  
    hoy.setTime(hoy.getTime() + msEnXDias)          //fecha de caducidad: actual + caducidad   
    return (hoy.toGMTString())   
}   
  
function BorrarCookie(nombre) {   
    //para borrar la cookie, se le pone una fecha del pasado mediante Caduca(-1)   
    document.cookie = nombre + "=; expires=" + Caduca(-1) + "; path=/"  
}   
  
function IntroducirCookie() {   
	var username = $('#j_username').attr('value');
	var password = $('#j_password').attr('value');
	var recordar = $("#chkrecordar").attr('checked');
	if (recordar){
		//establece la cookie: la caducidad es de 31 dias   
		var _dias = Caduca(31) //numero de dias de la cookie   
		if (username != "") GuardarCookie("username_sam", username, _dias);
		if (password != "") GuardarCookie("password_sam", password, _dias);
		GuardarCookie("recordar_sam", recordar, _dias);
	}
	else
	{
		//Borrar las cookies
		BorrarCookie("username_sam");
		BorrarCookie("password_sam");
		BorrarCookie("recordar_sam");
	}
}   
  
function MostrarCookie(nombre, control) {   
	var ctrl = document.getElementById(control);
    if(CojerCookie(nombre) != null)   
        ctrl.value = CojerCookie(nombre)   
}   
  
function MostrarCookieChk(nombre, control)
{
	var ctrl = document.getElementById(control);
	var cad = CojerCookie(nombre);
    if(CojerCookie(nombre) != null)
	if(cad=="true") ctrl.checked = "checked";	else ctrl.checked = "";
}

