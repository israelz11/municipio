<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<link rel="stylesheet" href="../include/css/estilosam.css" type="text/css">
<link href="../include/css/estilos.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../include/js/toolSam.js"></script>
<script type="text/javascript" src="../include/js/componentes/jquery.alerts.js"></script>
<script language="javascript">
<!--

/*AQUI CODIGO PARA FESTEJO
//<![CDATA[
var blimgcursor=["../imagenes/murcielago.gif", , ]
var blimgespacio=[12,12]
var blimgcursor_cuant=0

if (document.getElementById || document.all)
document.write('<div id="blimgcursorid" style="position:absolute;visibility:visible;left:0px;top:100px;width:1px;height:1px"><img border="0" src="'+blimgcursor[0]+'"></div>')
function blimgcur_activo(){
if (document.getElementById)
return document.getElementById("blimgcursorid").style
else if (document.all)
return document.all.trailimagid.style
}
function blcentro(){
return (!window.opera && document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}
function blinvis_img(){ 
blimgcur_activo().visibility="hidden"
document.onmousemove=""
}
function blimg_curseg(e){
var xcoord=blimgespacio[0]
var ycoord=blimgespacio[1]
if (typeof e != "undefined"){
xcoord+=e.pageX
ycoord+=e.pageY
}
else if (typeof window.event !="undefined"){
xcoord+=blcentro().scrollLeft+event.clientX
ycoord+=blcentro().scrollTop+event.clientY
}
var docwidth=document.all? blcentro().scrollLeft+blcentro().clientWidth : pageXOffset+window.innerWidth-15
var docheight=document.all? Math.max(blcentro().scrollHeight, blcentro().clientHeight) : Math.max(document.body.offsetHeight, window.innerHeight)
if (xcoord+blimgcursor[1]+3>docwidth || ycoord+blimgcursor[2]> docheight)
blimgcur_activo().display="none"
else
blimgcur_activo().display=""
blimgcur_activo().left=xcoord+"px"
blimgcur_activo().top=ycoord+"px"
}
document.onmousemove=blimg_curseg
if (blimgcursor_cuant>0)
setTimeout("blinvis_img()", blimgcursor_cuant*1000)
//]]>
TERMINA CODIGO DE FESTEJO*/
-->
</script>
<style type="text/css"> 
        @import url("../../include/css/calendar/calendar-win2k-cold-1.css"); 
    a:link {
	color: #00F;
	text-decoration: none;
}
a:visited {
	text-decoration: none;
	color: #603;
}
a:hover {
	text-decoration: underline;
}
a:active {
	text-decoration: none;
}

    </style>
<script language="javascript">
<!--
function MuestraVideos(){
	html = '<table width="528" border="0" cellspacing="0" cellpadding="0">'+
			'<tr>'+
			'<td colspan="2"><strong><i>Nota:</strong> Para poder visualizar correctamente los videotutoriales necesitara tener instalado en su equipo la version mas reciente de <strong>QuickTime</strong>, misma que puede descargar desde el apartado de Software adicional.</i></td>'+
			'<tr>'+
			  '<tr>'+
				'<td width="100"><img src="../imagenes/Mp4.png" width="100" height="100" border="0"></td>'+
				'<td width="428"><table width="100%" border="0" cellspacing="0" cellpadding="0">'+
				'  <tr>'+
				'	<td><strong>Introducción al Sistema Administrativo Municipal (SAM)</strong></td>'+
				'  </tr>'+
				'  <tr>'+
				'	<td><span class="TextoNormalGris">Breve introduccion en el que se presenta y detallan los nuevos cambios incluidos en la nueva version del Sistema Administrativo Municipal.</span></td>'+
				'  </tr>'+
				'<tr>'+
				'	<td><span class="TextoNormalGris"><a href="../sam/archivos/Introduccion_al_SAM.mp4" target="_blank">Descargar y Reproducir video</a></span></td>'+
				'</tr>'+
				'</table></td>'+
			  '</tr>'+
			  '<tr>'+
				'<td><img src="../imagenes/Mp4.png" alt="" width="100" height="100"></td>'+
				'<td><table width="100%" border="0" cellspacing="0" cellpadding="0">'+
				 ' <tr>'+
				'	<td><strong>Introducción al Módulo de Requisiciones, OT, OS.</strong></td>'+
				'  </tr>'+
				'  <tr>'+
				'	<td><span class="TextoNormalGris">Introducción a los nuevos cambios realizados al módulo de Requisiciones, OT, OS y explicación con ejemplos de como crear, editar, mostrar y cancelar estos documentos.</span></td>'+
				'  </tr>'+
				'<tr>'+
				'	<td><span class="TextoNormalGris"><a href="../sam/archivos/Modulo_de_Requisiciones.mp4" target="_blank">Descargar y Reproducir video</a></span></td>'+
				'</tr>'+
				'</table></td>'+
			  '</tr>'+
			  '<tr>'+
				'<td><img src="../imagenes/Mp4.png" alt="" width="100" height="100"></td>'+
				'<td><table width="100%" border="0" cellspacing="0" cellpadding="0">'+
				 ' <tr>'+
					'<td><strong>Introducción al Módulo de Pedidos</strong></td>'+
				 ' </tr>'+
				 ' <tr>'+
					'<td><span class="TextoNormalGris">Introducción a los nuevos cambios realizados al módulo de Pedidos y explicación con ejemplos de como crear, editar, mostrar y cancelar un Pedido.</span></td>'+
				  '</tr>'+
				  '<tr>'+
				'	<td><span class="TextoNormalGris"><a href="../sam/archivos/Modulo_de_Pedidos.mp4" target="_blank">Descargar y Reproducir video</a></span></td>'+
				'</tr>'+
				'</table></td>'+
			  '</tr>'+
			  '<tr>'+
				'<td><img src="../imagenes/Mp4.png" alt="" width="100" height="100"></td>'+
				'<td><table width="100%" border="0" cellspacing="0" cellpadding="0">'+
				'  <tr>'+
				'	<td><strong>Introducción al Módulo de Ordenes de Pago</strong></td>'+
				'  </tr>'+
				'  <tr>'+
				'	<td><span class="TextoNormalGris">Introducción a los nuevos cambios realizados al módulo de Ordenes de Pago y explicación con ejemplos de como crear, editar, mostrar y cancelar un Orden de Pago. Las Ordenes de Pago que se pueden realizar (Adquisiciones, Obras, Servicios, Compensaciones, Fondo fijo, Reintegros y Combustibles)</span></td>'+
				'  </tr>'+
				'<tr>'+
				'	<td><span class="TextoNormalGris"><a href="../sam/archivos/Modulo_Ordenes_de_Pago.mp4" target="_blank">Descargar y Reproducir video</a></span></td>'+
				'</tr>'+
				'</table></td>'+
			  '</tr>'+
			   '<tr>' +
					   '<td height="44" align="center" colspan="2">&nbsp;' +
					   '<input type="button" value="Cancelar" id="cmdcancelar" class="botones" onClick="$.alerts._hide();"/></td>' +
					   '</tr>'+
			'</table>';	
	jWindow(html,'Videotutoriales del Sistema Administrativo Municipal (SAM)', '','',0);
}

function MuestraSoftware(){
	html = '<table width="528" border="0" cellspacing="0" cellpadding="0">'+
			  '<tr>'+
				'<td width="100" height="85" align="center"><img src="../imagenes/application.png"  border="0"></td>'+
				'<td width="428"><table width="100%" border="0" cellspacing="0" cellpadding="0">'+
				'  <tr>'+
				'	<td><strong>Adobe Acrobat Reader 9 US - Windows 7</strong></td>'+
				'  </tr>'+
				'  <tr>'+
				'	<td><span class="TextoNormalGris">Version para Windows 7 del visor de documentos PDF de Acrobar Reader 9 Lenguaje Ingles.</span></td>'+
				'  </tr>'+
				'<tr>'+
				'	<td><span class="TextoNormalGris"><a href="../sam/archivos/AdbeRdr1000_en_US_win7.exe" target="_blank">Descargar</a></span></td>'+
				'</tr>'+
				'</table></td>'+
			  '</tr>'+
			  '<tr>'+
				'<td width="100" height="85" align="center"><img src="../imagenes/application.png"  border="0"></td>'+
				'<td><table width="100%" border="0" cellspacing="0" cellpadding="0">'+
				 ' <tr>'+
				'	<td><strong>Adobe Acrobat Reader 9 ESP - Windows Vista</strong></td>'+
				'  </tr>'+
				'  <tr>'+
				'	<td><span class="TextoNormalGris">Version para Windows Vista del visor de documentos PDF de Acrobar Reader 9 Lenguaje Español..</span></td>'+
				'  </tr>'+
				'<tr>'+
				'	<td><span class="TextoNormalGris"><a href="../sam/archivos/AdbeRdr940_es_ES_Vista.exe" target="_blank">Descargar</a></span></td>'+
				'</tr>'+
				'</table></td>'+
			  '</tr>'+
			  '<tr>'+
				'<td width="100" height="85" align="center"><img src="../imagenes/application.png"  border="0"></td>'+
				'<td><table width="100%" border="0" cellspacing="0" cellpadding="0">'+
				 ' <tr>'+
					'<td><strong>QuickTime Installer for Windows</strong></td>'+
				 ' </tr>'+
				 ' <tr>'+
					'<td><span class="TextoNormalGris">Codec y reproductor de video QuickTime para visualizar videos en MP4 y otros formatos.</span></td>'+
				  '</tr>'+
				  '<tr>'+
				'	<td><span class="TextoNormalGris"><a href="../sam/archivos/QuickTimeInstaller.exe" target="_blank">Descargar</a></span></td>'+
				'</tr>'+
				'</table></td>'+
			  '</tr>'+
			   '<tr>' +
					   '<td height="44" align="center" colspan="2">&nbsp;' +
					   '<input type="button" value="Cancelar" id="cmdcancelar" class="botones" onClick="$.alerts._hide();"/></td>' +
					   '</tr>'+
			'</table>';	
	jWindow(html,'Herramientas de Software Adicional', '','',0);
}
-->
</script>
</head>
<script>
/*CODIGO PARA EFECTO COPO DE NIEVE

// Numero de copos, recomendados entre 30 y 40
var nieve_cantidad=35

// Colores de los copos se mostraran de forma aleatoria
var nieve_colorr=new Array("#aaaacc","#ddddFF","#ccccDD")

// Tipo de letra de los copos
var nieve_tipo=new Array("Arial Black","Arial Narrow","Times","Comic Sans MS")

// Valor o letra de los copos
var nieve_letra="*"

// velocidad de caida
var nieve_velocidad=0.6

// tamaño mas grande de los copos
var nieve_cantidadsize=50

// tamaño mas pequeño de los copos
var nieve_chico=8

// 1 toda la pagina - 2 zona izquierda - 3 centro de pagina - 4 zona derecha
var nieve_zona=1

var nieve=new Array()
var marginbottom
var marginright
var timer
var i_nieve=0
var x_mv=new Array();
var crds=new Array();
var lftrght=new Array();
var browserinfos=navigator.userAgent
var ie5=document.all&&document.getElementById&&!browserinfos.match(/Opera/)
var ns6=document.getElementById&&!document.all
var opera=browserinfos.match(/Opera/)
var browserok=ie5||ns6||opera

function aleatorio(range) {
rand=Math.floor(range*Math.random())
return rand
}

function initnieve() {
if (ie5 || opera) {
marginbottom = document.body.clientHeight
marginright = document.body.clientWidth
}
else if (ns6) {
marginbottom = window.innerHeight
marginright = window.innerWidth
}
var nievesizerange=nieve_cantidadsize-nieve_chico
for (i=0;i<=nieve_cantidad;i++) {
crds[i] = 0;
lftrght[i] = Math.random()*15;
x_mv[i] = 0.03 + Math.random()/10;
nieve[i]=document.getElementById("s"+i)
nieve[i].style.fontFamily=nieve_tipo[aleatorio(nieve_tipo.length)]
nieve[i].size=aleatorio(nievesizerange)+nieve_chico
nieve[i].style.fontSize=nieve[i].size
nieve[i].style.color=nieve_colorr[aleatorio(nieve_colorr.length)]
nieve[i].sink=nieve_velocidad*nieve[i].size/5
if (nieve_zona==1) {nieve[i].posx=aleatorio(marginright-nieve[i].size)}
if (nieve_zona==2) {nieve[i].posx=aleatorio(marginright/2-nieve[i].size)}
if (nieve_zona==3) {nieve[i].posx=aleatorio(marginright/2-nieve[i].size)+marginright/4}
if (nieve_zona==4) {nieve[i].posx=aleatorio(marginright/2-nieve[i].size)+marginright/2}
nieve[i].posy=aleatorio(2*marginbottom-marginbottom-2*nieve[i].size)
nieve[i].style.left=nieve[i].posx
nieve[i].style.top=nieve[i].posy
}
movenieve()
}

function movenieve() {
for (i=0;i<=nieve_cantidad;i++) {
crds[i] += x_mv[i];
nieve[i].posy+=nieve[i].sink
nieve[i].style.left=nieve[i].posx+lftrght[i]*Math.sin(crds[i]);
nieve[i].style.top=nieve[i].posy

if (nieve[i].posy>=marginbottom-2*nieve[i].size || parseInt(nieve[i].style.left)>(marginright-3*lftrght[i])){
if (nieve_zona==1) {nieve[i].posx=aleatorio(marginright-nieve[i].size)}
if (nieve_zona==2) {nieve[i].posx=aleatorio(marginright/2-nieve[i].size)}
if (nieve_zona==3) {nieve[i].posx=aleatorio(marginright/2-nieve[i].size)+marginright/4}
if (nieve_zona==4) {nieve[i].posx=aleatorio(marginright/2-nieve[i].size)+marginright/2}
nieve[i].posy=0
}
}
var timer=setTimeout("movenieve()",50)
}

for (i=0;i<=nieve_cantidad;i++) {
document.write("<span id='s"+i+"' style='position:absolute;top:-"+nieve_cantidadsize+"'>"+nieve_letra+"</span>")
}
if (browserok) {
window.onload=initnieve
}

TERMINA CODIGO EFECTO COPO DE NIEVE*/

</script>
<body class="wrapbody">
<c:if test='${imagen != "" &&  imagen != null }'>
  <div style="padding: 70px 0px 100px 0px; height:70%; margin:auto; text-align:center;">
<img src="../imagenes/bienvenidos_<c:out value="${imagen}"/>.jpg" width="700" height="300" border="0" />
  <table width="30%" border="0" cellspacing="0" cellpadding="0" align="center">
    <tr>
      <td width="35%" height="80" align="center"><a href="#"><img src="../imagenes/address_64.png" width="60" height="60" border="0" /></a></td>
      <td width="33%" align="center"><a href="#"><img src="../imagenes/movie_track.png" width="60" height="60" border="0" /></a></td>
      <td width="32%" align="center"><a href="#"><img src="../imagenes/gear_64.png" width="60" height="60" border="0" /></a></td>
      </tr>
    <tr>
      <td align="center" class="TextoNormalGris">Manual del Usuario</td>
      <td align="center" class="TextoNormalGris">Video tutoriales</td>
      <td align="center" class="TextoNormalGris">Software adicional</td>
      </tr>
  </table>
</div>
</c:if>	
</body>
</html>
