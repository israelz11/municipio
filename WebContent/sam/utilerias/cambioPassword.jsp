<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorCambioPassword.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>    
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="cambioPassword.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
<!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->
<style type="text/css"> 
	@import url("../../include/css/calendar/calendar-win2k-cold-1.css"); 
</style>
<title>Cambiar contraseña</title>
</head>
<body>
<form name="frmcontraseña" action="" method="get">
<table width="85%" align="center"><tr><td><h1>Administración - Cambiar contraseña</h1></td></tr></table>
<table border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" width="85%">
  <tr>
    <td>&nbsp;</td>
    <td colspan="2" align="left">&nbsp;</td>
  </tr>
  <tr>
    <td height="15">&nbsp;</td>
    <td colspan="2" align="left"><img src="../../imagenes/topic.PNG" width="8" height="7">&nbsp;&nbsp;Tu nueva contraseña debe tener como minimo 6 caracteres. </td>
  </tr>
  <tr>
    <td height="15">&nbsp;</td>
    <td colspan="2" align="left"><img src="../../imagenes/topic.PNG" width="8" height="7">&nbsp;&nbsp;No utilices como contraseña tu login de usuario. </td>
  </tr>
  <tr>
    <td height="15">&nbsp;</td>
    <td colspan="2" align="left"><img src="../../imagenes/topic.PNG" width="8" height="7">&nbsp;&nbsp;Utiliza una combinación de letras y números.</td>
  </tr>
  <tr>
    <td height="15">&nbsp;</td>
    <td colspan="2" align="left" ><img src="../../imagenes/topic.PNG" width="8" height="7">&nbsp;&nbsp;Las contraseñas se distinguen entre masyúsculas y minúsculas. Recuerda comprobar la tecla que bloquea las mayusculas. </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td></td>
  </tr>
  <tr>
    <td></td>
    <th>Contraseña anterior:</th>
    <td align="left"><input type="password" value="" id="txtcontraseñaanterior"  class="input" style="width:250px"></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right" class="TextoNegritaTahomaGris">&nbsp;</td>
    <td align="left">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <th>Contraseña nueva:</th>
    <td align="left"><input type="password" value="" id="txtnuevacontraseña" class="input" style="width:250px"></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td class="TextoNegritaTahomaGris">&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <th>Confirmar:</th>
    <td align="left"><input type="password" value="" id="txtconfirmarcontraseña" class="input" style="width:250px"></td>
  </tr>
  <tr>
    <td height="45">&nbsp;</td>
    <td>&nbsp;</td>
    <td align="left" ><input type="button" value="Cambiar contraseña" id="cmdcambiarpassword" name="cmdcambiarpassword" class="botones"></td>
  </tr>
  </table>
</form>
</body>
</html>