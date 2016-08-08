<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Muestra Pedidos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script language="javascript">
<!--
function regresaEntrada(folio, ID_ENTRADA) {
	window.parent.__regresaEntrada(folio, ID_ENTRADA);
}
-->
</script>
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
</style></head>

<body>
<table class="listas" align="center" width="100%" cellpadding="0" cellspacing="0">
  <tr>
    <th width="11%" height="20" align="center"><strong>N. Entrada</strong></th>
    <th width="11%" height="20" align="center"><strong>Pedido</strong></th>
    <th width="11%" align="center"><strong>&nbsp;Fecha</strong></th>
    <th width="67%" height="20" colspan="2" align="left"><strong>Descripción</strong></th>
  </tr>
  <c:set var="cont" value="${0}" /> 
  <c:forEach items="${muestraPedidos}" var="item" varStatus="status" >
     <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
        <td height="20" align="center">
        <a href="javascript:regresaEntrada('<c:out value="${item.FOLIO}"/>', '<c:out value="${item.ID_ENTRADA}"/>');"><c:out value='${item.FOLIO}'/></a></td>        
        <td align="center"><c:out value='${item.NUM_PED}'/></td>
        <td align="center"><c:out value='${item.FECHA}'/></td>
        <td align="left" colspan="2"><c:out value='${item.DESCRIPCION}'/></td>
      </tr>
      <c:set var="cont" value="${cont+1}"/>
  </c:forEach>
</table>
</body>
</html>