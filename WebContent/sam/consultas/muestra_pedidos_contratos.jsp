<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
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
function regresaPedido(cve_ped, num_ped, clv_benefi)
{
	window.parent.regresaPedido(cve_ped, num_ped, clv_benefi);
}

function cargarPedidoFactura(cve_ped, num_ped, clv_benefi)
{
	document.location = "muestra_pedidos_facturas.action?CVE_PED="+cve_ped+"&NUM_PED="+num_ped+"&CLV_BENEFI="+clv_benefi+"&idDependencia="+<c:out value='${idDependencia}'/>;
}
-->
</script>

</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">  


<table width="95%" align="center">
  <tr>
    <td><h1>Listado de Pedidos disponibles</h1></td>
  </tr>
</table>
<table class="listas" border="0" align="center" cellpadding="0" cellspacing="0" width="95%">
  <tr bgcolor="#889FC9">
    <th width="16%" height="21" align="center">Num. Documento</th>
    <th width="16%" height="21" align="center">Programa / Partida</th>
    <th width="51%" height="21" align="center">Concepto</th>
    <th width="17%" align="center">Importe</th>
  </tr>
   <c:set var="cont" value="${0}" />
  <c:forEach items="${muestraPedidos}" var="item" varStatus="status" >
  <c:set var="cont" value="${cont+1}" /> 
        <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
          <td width="16%" height="20" align="center"><a href="javascript:regresaPedido(${item.CVE_PED}, '${item.NUM_PED}', '${item.CLV_BENEFI}')"><c:out value='${item.NUM_PED}'/></a></td>
        <td height="20" align="center"><c:out value='${item.N_PROGRAMA}'/> / <c:out value='${item.CLV_PARTID}'/></td>
        <td width="51%" height="20" align="left"><c:out value='${item.NOTAS}'/></td>
        <td width="17%" height="20" align="right">$<fmt:formatNumber value='${item.TOTAL}' pattern="###,###,###.00"/>&nbsp;</td>
        <input type="hidden" value="<c:out value='${item.CVE_CONTRATO}'/>" id="hdcontrato<c:out value='${item.CVE_PED}'/>">
        </tr>
  </c:forEach>
   <tr>
          <td height="27" colspan="4" align="left" style="background:#FFF"><c:if test="${cont>0}"></c:if> </td>
          </tr>
</table>

</body>
</html>