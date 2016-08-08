<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="content-style-type" content="text/css">
    <meta http-equiv="content-language" content="en-gb">
    <meta http-equiv="imagetoolbar" content="no">
    <meta name="resource-type" content="document">
    <meta name="keywords" content="">
    <meta name="description" content=""><title>Municipio 2016 | Login</title>
    <link href="include/css/print.css" rel="stylesheet" type="text/css" media="print" title="printonly">
    <link href="include/css/general.css" rel="stylesheet" type="text/css" media="screen, projection">
    <link href="include/css/estilosam.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="include/js/componentes/jquery.alerts.css" type="text/css">
    <script type="text/javascript" src="include/js/jquery-1.3.2.min.js"></script>
    <link rel="stylesheet" href="include/css/css/css3-buttons.css" type="text/css" media="screen">
    <link rel="stylesheet" href="include/css/tiptip.css" type="text/css"  media="screen">
    <script src="include/css/jquery.tiptip.js"></script>
    
    <script type="text/javascript" src="include/js/componentes/jquery.alerts.js"></script>
    <script src="index.js"></script>
</head>

<body>
<div align="center" id="loginbackground" style="height:150px">
  <div align="center" id="loginlogo"><img src="imagenes/logotipo_horizontal_rgb.jpg" width="500" height="100"></div>
</div>

<div id="wraplogin">
  <div id="page-body" align="center" style="">
 <form method="post"  name="forma" id="forma" action="j_spring_security_check" >
		<table border="0" cellpadding="0" cellspacing="0" style="width:350px">
  <tr>
    <td width="105" align="right" id="tableemptywhite">Clave de Usuario:</td>
    <td width="16" align="right" id="tableemptywhite">&nbsp;</td>
    <td width="229" id="tableemptywhite" align="left"><input name="j_username" id="j_username" width="200px" size="20" class="input" title="Usuario" onKeyPress="validar(event, this,1)"  style="width:200px; font-size:13px" type="text" value=""></td>
    </tr>
  <tr>
    <td height="32" align="right" id="tableemptywhite"><span class="quick-login">
      <label for="label"> Contrase&ntilde;a:</label>
      </span></td>
    <td height="32" align="right" id="tableemptywhite">&nbsp;</td>
    <td id="tableemptywhite" align="left"><input name="j_password" id="j_password" size="20" width="200px" class="input" title="Password" onKeyPress="validar(event, this,2)" style="width:200px; font-size:13px" type="password" value="" ></td>
  </tr>
   <tr>
    <td height="32" align="right" id="tableemptywhite">&nbsp;</td>
    <td height="32" align="right" id="tableemptywhite">&nbsp;</td>
    <td id="tableemptywhite" align="left"><input type="checkbox" id="chkrecordar" name="chkrecordar">
      Recordar mi cuenta de usuario</td>
  </tr>
  <tr>
    <td align="center" id="tableemptywhite">&nbsp;</td>
    <td align="center" id="tableemptywhite">&nbsp;</td>
    <td id="tableemptywhite" align="left"><button name="login" class="action blue" onClick="validacion_ingreso()"><span class="label" style="width:100px">Iniciar Sesión</span></button>
</td>
  </tr>
    
</table>
	  <p>&nbsp;</p>
      <p>&nbsp;</p>
   
	  <table width="40%" border="0" cellspacing="0" cellpadding="0" >
		  <tr>
		    <td id="tableemptywhite" height="27" colspan="6" align="left"><strong style="color:#666">Disponible para los navegadores web m&aacute;s comunes:</strong></td>
	      </tr>
		  <tr>
		    <td id="tableemptywhite" align="center"><a href="sam/archivos/InternetExplorer.exe"><img src="imagenes/IE.png" width="70" height="70" border="0" title="Descargar Internet Explorer 9.0" /></a></td>
		    <td id="tableemptywhite" align="center"><a href="sam/archivos/Firefox.exe"><img src="imagenes/Firefox.png" width="70" height="70" border="0" title="Descargar Mozilla Firefox 4.0"/></a></td>
		    <td id="tableemptywhite" align="center"><a href="sam/archivos/GoogleChrome.exe"><img src="imagenes/Chrome.png" width="70" height="70" border="0" title="Descargar Google Crome 10.0" /></a></td>
		    <td id="tableemptywhite" align="center"><a href="sam/archivos/Netscape.exe"><img src="imagenes/Netscape.png" width="70" height="70" border="0" title="Descargar Netscape 9.0" /></a></td>
		    <td id="tableemptywhite" align="center"><a href="sam/archivos/Safari.exe"><img src="imagenes/Safari.png" width="70" height="70" border="0" title="Descargar Safari 5.2" /></a></td>
		    <td id="tableemptywhite" align="center"><a href="sam/archivos/Opera.exe"><img src="imagenes/Opera.png" width="70" height="70" border="0" title="Descargar Opera 11.0" /></a></td>
	      </tr>
		  <tr>
		    <td id="tableemptywhite" align="center"><span style="color:#666">Internet Explorer</span></td>
		    <td id="tableemptywhite" align="center"><span style="color:#666">Mozilla Firefox</span></td>
		    <td id="tableemptywhite" align="center"><span style="color:#666">Google Crome</span></td>
		    <td id="tableemptywhite" align="center"><span style="color:#666">Netscape</span></td>
		    <td id="tableemptywhite" align="center"><span style="color:#666">Safari</span></td>
		    <td id="tableemptywhite" align="center"><span style="color:#666">Opera</span></td>
	      </tr>
	  </table>
	  
	</form>
  </div>
	<h3 style="padding-bottom:15px"></h3>
	<div class="copyright">H. Ayuntamiento Constitucional de Centro &copy; 2016. Todos los derechos reservados.<br>
    ISC. Israel de la Cruz Hernandez, Senior Developer </div>
    
</div>
</body>
</html>
<c:if  test="${message != 0 }" > 
	<script>jError('Usuario ó contraseña incorrecto, vuelva a intentarlo', 'Error de acceso');</script>
</c:if>
<script>MostrarCookie("username_sam", "j_username");</script>
<script>MostrarCookie("password_sam", "j_password");</script>
<script>MostrarCookieChk("recordar_sam", "chkrecordar");</script>
<!--
Credits: 
ISC. Israel de la Cruz Hernandez, Diseñador & Desarrollador, israelz11@hotmail.com. 
LSC. Mauricio Hernández León, Desarrollador, avefenix_x@hotmail.com
ISC. Manuel Moises Carrillo Carrillo, Analista, mcarrillo_carril@hotmail.com.
Ver 1.0 Oct 2009 - Dic 2010 Java+Spring Framework+Apache Tomcat 6+SQL Server 2005.
Ver 2.0 Septiembre 2011 - Enero de 2012 Java+Spring Framework+Apache Tomcat 6+SQL Server 2008.
-->