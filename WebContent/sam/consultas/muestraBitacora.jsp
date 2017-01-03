<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Bitacora de Movimientos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script language="javascript">
<!--
	function verDetalles(){
		if($('#chkdetalles').is(':checked'))
			document.location = "muestraBitacora.action?v=1&cve_doc=<c:out value='${cve_doc}'/>&tipo_doc=<c:out value='${tipo_doc}'/>";
		else
			document.location = "muestraBitacora.action?v=0&cve_doc=<c:out value='${cve_doc}'/>&tipo_doc=<c:out value='${tipo_doc}'/>";
			
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
</style>
</head>

<body>
<div align="center"><strong style="font-size:14px">Bitacora de <c:out value='${tipo}'/>: <c:out value='${documento}'/></strong></div>  
<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="95%">
  <tr bgcolor="#889FC9">
    <td colspan="7" style="background-color:#FFF; border-left:none; border-right:none; border-bottom:none"><input type="checkbox" id="chkdetalles" onclick="verDetalles()" <c:if test="${fn:contains(v,'1')}" >checked</c:if> />
    Ver más detalles en la descripción</td>
  </tr>
  <tr bgcolor="#889FC9">
    <th height="21" align="center">Num. Documento</th>
    <th height="21" align="center">Fecha doc.</th>
    <th width="16%" height="21" align="center">Fecha mov.</th>
    <th height="21" align="center">Programa / Partida</th>
    <th align="center">Monto</th>
    <th align="center">Login </th>
    <th align="center">Descripcion</th>
  </tr>
  <c:set var="cont" value="${0}" />
  <c:forEach items="${bitacora}" var="item" varStatus="status" >
      <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
        <td width="11%" height="16%" align="left"><c:out value='${item.NUM_DOC}'/></td>
        <td width="10%" height="16%" align="left"><c:out value='${item.FECHA_DOC}'/></td>
        <td height="16%" align="center"><fmt:formatDate pattern="dd/MM/yyyy H:mm:ss" value="${item.FECHA}" /></td>
        <td width="18%" height="16%" align="center"><c:out value='${item.N_PROGRAMA}'/> / <c:out value='${item.PARTIDA}'/></td>
        <td width="15%" height="16%" align="right">$<fmt:formatNumber value='${item.MONTO}' pattern="###,###,###.00"/>&nbsp;</td>
        <td width="11%" height="16%" align="center"><c:out value='${item.LOGIN}'/></td>
        <td width="19%" height="16%" align="left"><c:out value='${item.DES_MOV}'/><c:if test='${v==1}'>; <c:out value='${item.DESCRIPCION}'/></c:if></td>
      </tr>
      <c:set var="cont" value="${cont+1}" /> 
  </c:forEach>
  
</table>
</body>
</html>