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
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">  
<table width="95%" align="center">
  <tr>
    <td><h1>Listado de Vales para Reembolso Liquido</h1></td>
  </tr>
</table>
<table class="listas" border="0" align="center" cellpadding="0" cellspacing="0" width="95%">
  <tr bgcolor="#889FC9">
    <th width="8%" height="21" align="center">Num. Vale</th>
    <th width="10%" height="21" align="center">Fecha</th>
    <th width="59%" align="center">Beneficiario</th>
    <th width="12%" align="center">Importe</th>
  </tr>
   <c:set var="cont" value="${0}" />
  <c:forEach items="${documentos}" var="item" varStatus="status" >
    <c:set var="cont" value="${cont+1}" /> 
    <tr>
      <td width="8%" height="20" align="center"><a href="javascript:window.parent.getVale('<c:out value='${item.NUM_VALE}'/>', '<c:out value='${item.ID_PROYECTO}'/>', '<c:out value='${item.CLV_PARTID}'/>', '<c:out value='${item.IMPORTE}'/>', <c:out value='${item.CVE_VALE}'/>)"><c:out value='${item.NUM_VALE}'/></a></td>
      <td height="20" align="center"><c:out value='${item.FECHA}'/></td>
      <td height="20" align="left"><c:out value='${item.NCOMERCIA}'/></td>
      <td width="12%" height="20" align="right">$<fmt:formatNumber value='${item.IMPORTE}' pattern="###,###,###.00"/>&nbsp;</td>
      </tr>
  </c:forEach>
</table>
</body>
</html>