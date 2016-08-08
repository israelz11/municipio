<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Muestra presupuesto</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<script language="javascript">
<!--
function regresaProducto(producto, ID_ARTICULO, GRUPO, SUBGRUPO, CLAVE, PRECIO, UNIDMEDIDA, CLV_UNIDAD) {
	window.parent.__regresaProducto(producto, ID_ARTICULO, GRUPO, SUBGRUPO, CLAVE, PRECIO, UNIDMEDIDA, CLV_UNIDAD);
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
<table class="listas" align="center" cellpadding="0" cellspacing="0" width="100%">
  <tr bgcolor="#889FC9">
    <th width="7%" height="25" align="center"><strong>No.</strong></th>
    <th width="52%"  align="left"><strong>&nbsp;Descripción</strong></th>
    <th height="16%" colspan="2" align="center"><strong>Unidad</strong></th>
    <th width="14%" align="center"><strong>Precio Unitario</strong></th>
  </tr>
  <c:forEach items="${muestraProductos}" var="item" varStatus="status" >
      <tr bgcolor="#DBDBDB">
        <td height="20" align="right"><c:out value='${status.count}'/></td>        
        <td height="16%" align="left">
        <c:if test="${item.CONSEC!='0000' }"> 
        <a href="javascript:regresaProducto('<c:out value='${item.DESCRIP}'/>',<c:out value='${item.ID_ARTICULO}'/>,'<c:out value='${item.GRUPO}'/>','<c:out value='${item.SUBGRUPO}'/>','<c:out value='${item.CONSEC}'/>','<fmt:formatNumber value='${item.ULT_PRECIO}' pattern='#########0.00'/>', '<c:out value='${item.UNIDMEDIDA}'/>', '<c:out value='${item.CLV_UNIMED}'/>');"><c:out value='${item.DESCRIP}'/></a>
        </c:if>
        <c:if test="${ item.CONSEC=='0000' }"> 
        <strong><c:out value='${item.DESCRIP}'/></strong>
        </c:if>
        </td>
        <td height="16%" align="center" colspan="2"><c:out value='${item.UNIDMEDIDA}'/>&nbsp;</td>
        <td height="16%" align="right">$<fmt:formatNumber value='${item.ULT_PRECIO}' pattern="###,###,###.00"/></td>
      </tr>
  </c:forEach>
</table>
</body>
</html>