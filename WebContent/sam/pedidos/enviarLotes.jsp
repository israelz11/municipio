<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script language="javascript">
<!--
function _enviarLotePedido(){
	window.parent.$.alerts._hide();
	window.parent._enviarLotesPedido();
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>

<title>Enviar lotes a pedido</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style></head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td height="27" align="center"><span class="TextoNegritaTahomaGris">Introduzca el número del pedido:</span> <input type="text" value="" id="txtpedido" class="input" maxlength="6"></td>
  </tr>
  <tr>
    <td height="44" align="center"><input type="button" value="Cancelar" id="cmdborrarConceptos" class="botones" onClick="window.parent.$.alerts._hide();" style="width:100px"/>
    <input type="button" value="Enviar" id="cmdEnviarLotes" class="botones" style="width:100px"/></td>
  </tr>
</table>
</body>
</html>