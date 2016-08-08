<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Detalles de Orden de Pago</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>

<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-bottom: 0px;
	color:#000;
	font-size:11px;
}
a:link {
	text-decoration: none;
}
a:visited {
	text-decoration: none;
}
a:hover {
	text-decoration: none;
}
a:active {
	text-decoration: none;
}
-->
</style>
</head>

<body>  
<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="95%">
  <tr bgcolor="#889FC9">
    <th height="25" colspan="2" align="center">Número de Pedido: <strong>
    <c:out value='${op.NUM_PED}'/></strong></th>
  </tr>

       <tr>
        <th width="20%" height="25" align="left"><c:out value='${op.NUM_DOC}'/>
          Fecha</th>
        <td width="80%" height="20" align="left"><c:out value='${op.FECHA}'/></td>
  </tr>
       <tr>
         <th height="25" align="left">Unidad Administrativa</th>
         <td height="20" align="left"><c:out value='${op.UNIDAD_ELABORA}'/></td>
       </tr>
       <tr>
         <th height="25" align="left">Tipo de Gasto</th>
         <td height="20" align="left"><c:out value='${op.DIGITOS}'/> <c:out value='${op.RECURSO}'/></td>
       </tr>
       <tr>
         <th height="25" align="left">Beneficiario</th>
         <td height="20" align="left"><c:out value='${op.NCOMERCIA}'/></td>
       </tr>
       <tr>
         <th height="25" align="left">Importe</th>
         <td height="20" align="left">$<fmt:formatNumber value='${op.IMPORTE}' pattern="###,###,###.00"/></td>
       </tr>
       <tr>
         <th height="25" align="left" valign="middle">Notas</th>
         <td height="20" align="left" valign="top"><c:out value='${op.NOTA}'/></td>
       </tr>
</table>
<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="95%">
  <tr bgcolor="#889FC9">
    <th height="25" colspan="7" align="center">Movimientos</th>
  </tr>
  <tr>
    <th width="6%" height="25" align="left"><c:out value='${item.NUM_DOC}'/>
    Lote</th>
    <th width="10%" height="20" align="center"><c:out value='${item.TIPO_DOC}'/>
    Cantidad
    <th width="9%" align="center">Unidad</th>
    <th width="43%" align="center">Descripción</th>
    <th width="11%" align="center">Precio Área</th>
    <th width="11%" align="center">Precio Unitario</th>
    <th width="10%" align="center">Costo</th>
  </tr>
  <tr>
    <td height="25" align="center"><c:out value='${cont}'/></td>
    <td height="20" align="center"><c:out value='${item.PROYECTO}'/></td>
    <td height="20" align="center"><c:out value='${item.CLV_PARTID}'/></td>
    <td height="20" align="center">&nbsp;</td>
    <td height="20" align="center">&nbsp;</td>
    <td height="20" align="center">&nbsp;</td>
    <td height="20" align="center"><fmt:formatNumber value='${item.MONTO}' pattern="###,###,###.00"/></td>
  </tr>
</table>
</body>
</html>