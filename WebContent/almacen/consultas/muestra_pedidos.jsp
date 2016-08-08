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
<script language="javascript">
<!--
function regresaPedido(num, ID_PEDIDO, id_proyecto, programa, clv_partid, clv_benefi, ncomercia, nota) {
	window.parent.__regresaPedido(num, ID_PEDIDO, id_proyecto, programa, clv_partid, clv_benefi, ncomercia, nota);
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
<table class="listas" align="center" width="100%">
  <tr>
    <th width="11%" height="18%" align="center"><strong>Num. Pedido</strong></th>
    <th width="14%" height="16%" align="center"><strong>&nbsp;Fecha</strong></th>
    <th height="16%" colspan="2" align="left"><strong>Notas</strong></th>
    <th width="18%" height="16%" align="center"><strong>Importe Total</strong></th>
  </tr>
  <c:forEach items="${muestraPedidos}" var="item" varStatus="status" >
      <tr bgcolor="#DBDBDB">
        <td height="16%" align="center">
        <a href="javascript:regresaPedido('<c:out value="${item.NUM_PED}"/>', '<c:out value="${item.CVE_PED}"/>', '<c:out value="${item.ID_PROYECTO}"/>', '<c:out value="${item.N_PROGRAMA}"/>', '<c:out value="${item.CLV_PARTID}"/>', '<c:out value="${item.CLV_BENEFI}"/>', '<c:out value="${item.NCOMERCIA}"/>', '<c:out value="${item.NOTAS}"/>');"><c:out value='${item.NUM_PED}'/></a></td>        
        <td height="16%" align="center"><c:out value='${item.FECHA_PED}'/></td>
        <td height="16%" align="left" colspan="2"><c:out value='${item.NOTAS}'/></td>
        <td height="16%" align="right"><fmt:formatNumber value="${item.TOTAL}"  pattern="#,###,###,##0.00" />&nbsp;</td>
      </tr>
  </c:forEach>
</table>
</body>
</html>