<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
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
<script language="javascript">
<!--
function cargar(cve_req, num_req, proveedor, clv_benefi){
	window.parent.cargarOS(cve_req, num_req, proveedor, clv_benefi);
}

-->
</script>

</head>

<body>  
<table width="95%" align="center">
  <tr>
    <td height="47"><h1>Listado de O.S y REQ. Calendarizadas 
      <c:out value='${desMes}'/>
    </h1></td>
  </tr>
</table>
<table class="listas" border="0" align="center" cellpadding="0" cellspacing="0" width="95%">
  <tr bgcolor="#889FC9">
    <th width="19%" height="21" align="center">Num. Documento</th>
    <th width="15%" height="21" align="center">Programa / Partida</th>
    <th width="55%" height="21" align="center">Concepto</th>
    <th width="13%" align="center">Importe</th>
  </tr>
   <c:set var="cont" value="${0}" />
  <c:forEach items="${documentos}" var="item" varStatus="status" >
    <c:set var="cont" value="${cont+1}" /> 
    <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
      <td width="19%" height="20" align="left"><a href="javascript:cargar(${item.CVE_REQ}, '${item.NUM_REQ}', '${item.N_COMERCIA}', '${item.CLV_BENEFI}')"><c:out value='${item.NUM_REQ}'/></a></td>
      <td height="20" align="center"><c:out value='${item.PROYECTO}'/>
      / <c:out value='${item.CLV_PARTID}'/></td>
      <td width="55%" height="20" align="left"><c:out value='${item.OBSERVA}'/></td>
      <td width="13%" height="20" align="right">$<fmt:formatNumber value='${item.IMPORTE}' pattern="###,###,###.00"/>&nbsp;</td>
    </tr>
  </c:forEach>
</table>
</body>
</html>