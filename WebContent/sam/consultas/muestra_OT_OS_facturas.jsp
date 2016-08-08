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
function cargarOSOT(num_req, cve_req, clv_benefi, total){
	window.parent.regresarOSOTFactura(num_req, cve_req, clv_benefi, total);
}
-->
</script>

</head>

<body>  
<table width="95%" align="center">
  <tr>
    <td><h1>Listado de Ordenes de Trabajo y Servicio
        <c:out value='${desMes}'/>
    </h1></td>
  </tr>
</table>
<table class="listas" border="0" align="center" cellpadding="0" cellspacing="0" width="95%">
  <tr bgcolor="#889FC9">
    <th width="13%" height="21" align="center">Num. Documento</th>
    <th width="15%" height="21" align="center">Programa / Partida</th>
    <th width="55%" height="21" align="center">Concepto</th>
    <th width="13%" align="center">Importe</th>
  </tr>
   <c:set var="cont" value="${0}" />
  <c:forEach items="${muestraRequisiciones}" var="item" varStatus="status" >
  <c:set var="cont" value="${cont+1}" /> 
        <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
          <td width="19%" height="20" align="left"><a href="javascript:cargarOSOT('${item.NUM_REQ}', ${item.CVE_REQ}, '${item.CLV_BENEFI}', '${item.TOTAL}');"><c:out value='${item.NUM_REQ}'/></a></td>
        <td height="20" align="center"><c:out value='${item.N_PROGRAMA}'/>
          / <c:out value='${item.CLV_PARTID}'/></td>
        <td width="49%" height="20" align="left"><c:out value='${item.OBSERVA}'/></td>
        <td width="13%" height="20" align="right">$<fmt:formatNumber value='${item.TOTAL}' pattern="###,###,###.00"/>&nbsp;</td>
        </tr>
  </c:forEach>
   <tr>
          <td height="27" style="background:#FFF" colspan="4" align="left"><c:if test="${cont>0}"></c:if> </td>
          </tr>
      
 
</table>
</body>
</html>