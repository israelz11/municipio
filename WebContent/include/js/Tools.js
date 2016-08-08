
/*
 *
 * Dependencia: Dirección General de Modernización e Innovacion Gubernamental
 */
 //variables globales
 var qString, msgError, message, nSeleccionados, ajax;
 var F = document.getElementById;
 var datos = new Array();
 
 /*
  *Esto es para deshabilitar el backhistory en caso de ser necesario descomentar el codigo 
  * */
 /*
 if (history.forward(1)){
 	location.replace(history.forward(1));
 } */

function getHTML( param ){
	if( param != null ){
		if( param == 'null')
			return '';
		else
			return param;
	}else{
		return '';
	}
}


function llenaCombo( obj_combo, valor, texto ){
	obj_combo.options[ obj_combo.length ] = new Option( texto, valor, false, false );
}

function borraCombo( obj_combo ){
	while( obj_combo.length > 0 ){
		obj_combo.remove( 0 );
	}
}

//valida Texarea

function ValidateMaxLength(evnt, str, maxLength) {
 var evntKeyCode = (window.event) ? evnt.keyCode:evnt.witch;
 // Ignora keys Delete, Backspace, Shift, Ctrl, Alt, Insert, Delete, Home, End, Page Up, Page Down and arrow keys
    var escChars = ",8,17,18,19,33,34,35,36,37,38,39,40,45,46,";
 if (escChars.indexOf(',' + evntKeyCode + ',') == -1) {
        if (str.length >= maxLength) {
            alert("No acepta mas de " + maxLength + " caracteres.");
            return false;
        }
    }
    return true;
}
//valida Texarea
function validarTexAreaOnBlur(object,maxLength) {
  	if (object.value.length > maxLength ) {
		object.value=object.value.substring(0, maxLength); 
		alert("No acepta mas de " + maxLength + " caracteres.");
	}	
}

//crea el objeto ajax
function JAjax(){
	var xmlhttp=false;
	try {
		xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
		try {
		   xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (E) {
			xmlhttp = false;
  		}
	}

	if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
		xmlhttp = new XMLHttpRequest();
	}
	return xmlhttp;
}

//elimina las filas de una tabla sin eliminar el head
function quitRow( table ){
	var tabla = document.getElementById(table).tBodies[0];
	var nRows = tabla.rows.length;

	while( tabla.rows.length > 0 ){
		index_table = tabla.rows.length - 1;
		tabla.deleteRow( index_table );
	}
}

function LTrim( value ) {	
	var re = /\s*((\S+\s*)*)/;
	return value.replace(re, "$1");	
}

function RTrim( value ) {	
	var re = /((\s*\S+)*)\s*/;
	return value.replace(re, "$1");	
}
function trim( value ) {	
	return LTrim(RTrim(value));	
}

//formateo de numeros
function formatNumber(num,prefix){
   prefix = prefix || '';
   num += '';
   var splitStr = num.split('.');
   var splitLeft = splitStr[0];
   var splitRight = splitStr.length > 1 ? '.' + splitStr[1] : '';
   var regx = /(\d+)(\d{3})/;
   while (regx.test(splitLeft)) {
      splitLeft = splitLeft.replace(regx, ' $1' + ',' + '$2');
   }
    if( splitRight.length == 0 )
	   		splitRight = ".00";
	   else if( splitRight.length == 2 )
	   		splitRight += "0";
   return prefix + splitLeft + splitRight;
}


//quita formato de numeros
function unFormatNumber( param ){
	return param.replace(/([^0-9.-])/g,'' )*1;
}

//verificar si una variable es un arreglo
/*Object.prototype.isArray = function() {
	return this.constructor == Array;
}*/

//resalta la linea al seleccionar el check
function selRow( elemento ) {
  tr = elemento.parentNode.parentNode;
  tr.className = (elemento.checked) ? "form_tipo_1" : "of";
}

//agregar todos los parametros a una variable para enviarlas como url
function setString( nForm ){
	qString = "";
	var oForm = document.forms[ nForm ];
	var nElementos = oForm.elements.length;
	
	for( var i=0; i < nElementos; i++ ){
		if( i < nElementos - 1 ){
			qString += oForm.elements[ i ].name + '='
					+ encodeURIComponent( oForm.elements[ i ].value )+ '&';
		}
		else {
			qString += oForm.elements[ i ].name + '='
					+ encodeURIComponent( oForm.elements[ i ].value );
		}
	} 
}
//agregar todos parametros a una variable tomando en cuenta si un elemento es un check debe estar marcado
function setParam( nForm ){
	qString 		= "";
	var oForm		=	document.forms[ nForm ];
	var nElementos	=	oForm.elements.length;
	
	for( var i=0;i < nElementos; i++ ){ 
		if( oForm.elements[ i ].type != "checkbox" && oForm.elements[i].type != "radio") {
			if( i < nElementos -1 ) {
				qString += oForm.elements[ i ].name + "="
						+ encodeURIComponent( oForm.elements[ i ].value )+ "&";
			}
			else{
				qString	+= oForm.elements[ i ].name + "="
							+ encodeURIComponent( oForm.elements[ i ].value );
			}
		}
		else if( oForm.elements[ i ].checked == true ){
			if( i < nElementos -1 ) {
				qString += oForm.elements[ i ].name + "="
						+ encodeURIComponent( oForm.elements[ i ].value )+ "&";
			}
			else{
				qString	+= oForm.elements[ i ].name + "="
							+ encodeURIComponent( oForm.elements[ i ].value );
			}
		}
	}
}
function nSeleccionados( nForm ) {
	var sel = 0;
	for( var i = 0; i < document.forms[ nForm ].elements.length; i++ ){
		if( document.forms[ nForm ].elements[i].type == "checkbox" ){
			if( document.forms[ nForm ].elements[i].checked == true ){
				sel = sel + 1;
			}
		}
	}
	return sel;
}

function checkboxSeleccionados( checkbox ) {
	var sel      = 0;
	var datos    = new Array();
	if ( checkbox != null ) {	
	var checkboxLength =checkbox.length;		
	if (isNaN(checkboxLength))	
	    checkboxLength = 0;	
	if (checkbox.length > 0 ) {		
	 for( var i=0; i < checkboxLength; i++ )
	   if( checkbox[i].checked == true ) {
		    datos[sel] = checkbox[i].value;
		    sel = sel + 1;
	    }
	}
	else 
	   if (checkbox.checked )
		 datos[0] = checkbox.value;		   	  
	}
	return datos;
}


//solo acepta numeros
function keyNumber( event ){
	var key = ( window.event )? event.keyCode:event.which;
	if( ( key > 47 && key < 58 ) || key == 46 || key == 8 )
		return true;
	else
		return false;
}
function keyImporte( event ){
	var key = ( window.event )? event.keyCode:event.which;
	if( ( key > 47 && key < 58 ) || key == 46 || key == 8 )
		return true;
	else
		return false;
}
function abreVentana( url, width , height  ){
	var top		=	( screen.height - height ) / 2;
	var left	=	( screen.width	- width )	/ 2;
	
	var caracteristicas  = "height="+height+", width="+width+",toolbar=0, location=no, directories=no" +    
						"status=0,linemenubar=0, modal=yes, left="+left+", top="+top+", dialog=yes," +        
						"resizable, scrollbars, location = 0";
						
	window.open( url, "ventana", caracteristicas );
}
function ubicaIndexCombo( elemento, valor ) {
	var longitud = document.getElementById( elemento ).length;
	var encontrado = false;
	for( var x=0; x < longitud; x++ ){
		var aux = document.getElementById( elemento ).options[x].value
		if( ( aux != "" ) && ( aux == valor) ){
			encontrado = true;
			break;
		}
	}
	if( encontrado == true )
		document.getElementById( elemento ).selectedIndex = x;
	else
		document.getElementById( elemento ).selectedIndex = 0;
}
function Td(texto,estilo,obj,html ){
		var cell = document.createElement( "TD" );
		if( typeof(estilo) != 'undefined' && estilo != "" )
			cell.style.cssText = estilo;
		if( typeof(html) != 'undefined' && html != "" )
			cell.innerHTML = html;
		else if( typeof(obj) != 'undefined' && obj != "" )
			cell.appendChild( obj );
		else
			cell.appendChild( document.createTextNode( texto ) );
		
		return cell;
}
function createImage(nameObj, src, estilo){
	var image	= 	document.createElement( "IMG" );
		image.name 	=	nameObj;
		image.id   	=	nameObj;
		image.src  	= 	src;
		if( typeof(estilo) != 'undefined' && estilo != '' )
			image.style.cssText = estilo;
		
		return image;
}
function iTxt(idObj,valor,estilo,clase){
	var txt 	= document.createElement( "INPUT" );
	txt.id		= idObj;
	txt.name 	= idObj;
	txt.type	= "text";
	if( typeof valor != 'undefined' && valor != "" )
		txt.value	= valor;
	if( typeof estilo != 'undefined' && estilo != "" )
		txt.style.cssText = estilo;
	if( typeof clase != 'undefined' && clase != "" )
		txt.className	= clase;
		
	return txt;	
}
function Check(idObj,valor,estilo,clase){
	var check 	= document.createElement( "INPUT" );
	check.id	= idObj;
	check.name 	= idObj;
	check.type	= "checkbox";
	if( typeof valor != 'undefined' && valor != "" )
		check.value	= valor;
	if( typeof estilo != 'undefined' && estilo != "" )
		check.style.cssText = estilo;
	if( typeof clase  != 'undefined' && clase != "" )
		check.className	= clase;
		
	return check;	
}
function radio(idObj,valor,estilo,clase,checked ){
	if( navigator.appName == "Netscape"){
		var iradio 	= document.createElement( "INPUT" );
		iradio.id	= idObj;
		iradio.name	= idObj;
		iradio.type	= "radio";
		if( typeof valor != 'undefined' && valor != "" )
			iradio.value = valor;
		if( typeof estilo != 'undefined' && estilo != "" )
			iradio.style.cssText = estilo;
		if( typeof clase  != 'undefined' && clase != "" )
			iradio.className	= clase;
		if( checked )
			iradio.checked = true;
		return iradio;
	}else if( navigator.appName == "Microsoft Internet Explorer"){
		if( checked )
			var iradio  = document.createElement("<input type='radio' checked='checked' name='"+ idObj+"' id = '"+idObj+"' value='"+ valor+"'/>");
		else
			var iradio  = document.createElement("<input type='radio' name='"+ idObj+"' id = '"+idObj+"' value='"+ valor+"'/>");
		if( typeof estilo != "undefined" && estilo != "")
			iradio.style.cssText = estilo;
		if( typeof clase != "undefined" && clase != "" )
			iradio.className = clase;
		return iradio; 
	}
}

function hiden(idObj,valor){
	var oculto 	= document.createElement( "INPUT" );
	oculto.id	= idObj;
	oculto.name	= idObj;
	oculto.type	= "hidden";
	oculto.value = valor;

	return oculto;
}

function convertirMay(obj){
obj.value=obj.value.toUpperCase();
} 

function redondeo( valor ) {
	var resultado = Math.round(valor * 100 ) / 100;
	return resultado;
}
function locked(){
	var ancho		= window.screen.width
	var altura		= window.screen.height
	//var ancho		= (document.body.clientWidth  - 20); //toma en cuenta solamente el ancho del body
	//var altura	= (document.documentElement.clientHeight ); //toma en cuenta solamente el alto del body
	var left 		= (document.body.clientWidth / 2 )   - 100;
	var contenedor 	= document.createElement("DIV");
	

	contenedor.id 	= "contenedor";
	contenedor.name = "contenedor";
	contenedor.style.cssText = "width:"+ancho+"px;height:"+altura+"px;text-align:center";
	//contenedor.style.cssText = "width:100%;height:100%;text-align:center";
	contenedor.className = "contenedor";
	
	var contenedor2 	= document.createElement("DIV");
	contenedor2.id 		= "contenedor2";
	contenedor2.name 	= "contenedor2";
	contenedor2.style.cssText = "left:"+left+"px;";
	contenedor2.className = "procesando";
	
	var tabla = document.createElement("TABLE");
	tabla.id = "principal";
	tabla.width = "100%";
	tabla.align = "center";
	tabla.cellPadding = 0;
	tabla.cellSpacing = 0;
	
	var tbody = document.createElement("TBODY");
	tabla.appendChild(tbody);
	tabla.align	= "center";
	
	var tr 	= document.createElement( "TR" );
	var tr2	= document.createElement( "TR" );
	var tr3	= document.createElement( "TR" );
	var tr4	= document.createElement( "TR" );
	/*
	var boton 		= document.createElement( "INPUT");
	boton.type 		= "button";
	boton.id 		= "btn_ocultar";
	boton.value 	= "Ocultar";
	boton.onclick 	= function(){ unlocked( elemento.id )};*/
	var path 		= top.location.pathname;
	var pos			= path.indexOf("/",2);
	var mipath 		= path.substr(1,pos -1 );
	
	var hostname 	= top.location.href;
	var position 	= hostname.indexOf(mipath,1);
	var host 		= hostname.substr(0,position) + mipath + "/";	

	var img = createImage( "loading",host+"imagenes/loading.gif");	
	tr.appendChild( Td( "","text-align:center",img ) );
	tbody.appendChild( tr )
	
	tr2.appendChild( Td("PROCESANDO DATOS","text-align:center") );
	tbody.appendChild( tr2 )
	
	tr3.appendChild( Td( "ESPERE UN MOMENTO...","text-align:center") );
	tbody.appendChild( tr3 )

	tabla.appendChild( tbody );
	contenedor2.appendChild( tabla );
	contenedor.appendChild( contenedor2 );
	document.body.appendChild( contenedor );
}
/*
 * Deshabilita el boton derecho del mouse
 * */
function unlocked(){
	document.body.removeChild( document.getElementById("contenedor") );
}

 document.oncontextmenu = function() {
      return false
 }

// activar nuevamente este proyecto para cuando este en produccion

function right(e) {
	var msg = "!PROHIBIDO USAR EL CLICK DERECHO DEL MOUSE!";
    if (navigator.appName == 'Netscape' && e.which == 3) {
        //alert(msg); //Esta linea se puede quitar si no se desea mostrar un mensaje de alerta
    	return false;
    }
    else if (navigator.appName == 'Microsoft Internet Explorer' && event.button==2) {
    	//alert(msg); //Esta linea se puede quitar si no se desea mostrar un mensaje de alerta
    	return false;
    }
   	return true;
}

document.onmousedown = right;

/*
* Deshabilita la tecla f5 de los navegadores
* Cabe señalar que aun funciona el boton de actualizar de la barra de herramientas
*/


var ns4 = (navigator.appName == 'Netscape')? true:false
var ie4 = (navigator.appName == 'Microsoft Internet Explorer')? true:false

function pulsarTecla(e) {
  if (ns4) {
  	var Tecla = e.which; 
  	if( Tecla == 116 )
  		Tecla = 505;
  		
  	if( Tecla == 505 )
  		return false;
  }
  if (ie4) {
  	if(window.event && window.event.keyCode == 116){
		window.event.keyCode = 505; 
	}

	if(window.event && window.event.keyCode == 505){
		return false; 
	} 
  }
}

document.onkeydown = pulsarTecla
if (ns4) document.captureEvents(Event.KEYDOWN);


// crear elementos de una tabla
function crearTd(style, texto, html ){
 	var td	= document.createElement( "TD" );
	    if (style!="")
 	    td.style.cssText = style;
 	    td.appendChild( document.createTextNode( texto )); 				
		if (html!="")
		td.innerHTML = html ; 
		return td;
}

function ocultarVer(id_tr, ver){
    dis = ver ? '' : 'none';
    tr = document.getElementById(id_tr)
    tr.style.display = dis;
}


