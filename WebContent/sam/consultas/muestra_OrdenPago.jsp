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

function cargarMovimientosOP()
{	
	var checkID = [];
	$('input[name=chkOP]:checked').each(function() { 
		checkID.push($(this).val());	
	});
	
		window.parent.generarDetallesOrdenPago(checkID);
}

function cargarOrdenPago(cve_op, num_op)
{
	
	//document.location = "muestra_OrdenPago.action?cve_op="+cve_ped+"&num_op="+num_ped+"&idDependencia="+<c:out value='${idDependencia}'/>;
}
-->
</script>

</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">  

<c:if test="${num_op==null}">
<table width="95%" align="center">
  <tr>
    <td><h1>Listado de Ordenes de Pago</h1></td>
  </tr>
</table>
<table class="listas" border="0" align="center" cellpadding="0" cellspacing="0" width="95%">
  <tr bgcolor="#889FC9">
    <th width="16%" height="21" align="center">Num. OP</th>
    <th width="16%" height="21" align="center">Fecha</th>
    <th width="51%" height="21" align="center">Nota</th>
    <th width="17%" align="center">Importe</th>
  </tr>
   <c:set var="cont" value="${0}" />
  <c:forEach items="${listado}" var="item" varStatus="status" >
  <c:set var="cont" value="${cont+1}" /> 
        <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
          <td width="16%" height="20" align="center"><a href="javascript:document.location='muestra_OrdenPago.action?cve_op='+${item.CVE_OP}+'&num_op=${item.NUM_OP}'"><c:out value='${item.NUM_OP}'/></a></td>
        <td height="20" align="center"><c:out value='${item.FECHA}'/></td>
        <td width="51%" height="20" align="left"><c:out value='${item.NOTA}'/></td>
        <td width="17%" height="20" align="right">$<fmt:formatNumber value='${item.IMPORTE}' pattern="###,###,###.00"/>&nbsp;</td>
        <input type="hidden" value="<c:out value='${item.CVE_CONTRATO}'/>" id="hdcontrato<c:out value='${item.CVE_PED}'/>">
        </tr>
  </c:forEach>
   <tr>
          <td height="27" colspan="4" align="left" style="background:#FFF"><c:if test="${cont>0}"></c:if> </td>
          </tr>
</table>
</c:if>


<c:if test="${num_op!=null}">
<table width="95%" align="center">
  <tr>
    <td><h1>Listado de Movimientos de la Orden de Pago: <strong>
    <c:out value='${num_op}'/></strong></h1></td>
  </tr>
  <tr>
    <td><input type="button"  class="botones"  name="cmdRegresar" value="Regresar"  onclick="history.go(-1)" style="width:120px"/></td>
  </tr>
</table>
<table class="listas" align="center" width="95%" cellpadding="0" cellspacing="0">
  <tr>
    <th width="9%" height="20" align="center">&nbsp;</th>
    <th width="13%" height="20" align="center">Importe</th>
    <th width="11%" align="center">Proyecto</th>
    <th width="13%" height="20" align="left">Partida</th>
    <th width="54%" height="20" align="left">Descripción</th>
  </tr>
  <c:set var="cont" value="${0}" />
  <c:forEach items="${detalles}" var="item" varStatus="status" >
  <c:set var="cont" value="${cont+1}" /> 
    <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
      <td height="20" align="center"><input type="checkbox" value="<c:out value='${item.ID_MOV_OP}'/>" id="chkOP" name="chkOP" />
       </td>  
      <td align="right">$<fmt:formatNumber value='${item.MONTO}' pattern="###,###,###.00"/>&nbsp;</td>
      <td align="center">[<c:out value='${item.ID_PROYECTO}'/>] <c:out value='${item.N_PROGRAMA}'/></td>
      <td align="center"><c:out value='${item.CLV_PARTID}'/></td>
      <td align="left"><c:out value='${item.NOTA}'/></td>
    </tr>
  </c:forEach>
</table>
<table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td height="35">&nbsp;<span style="background:#FFF"><c:if test="${fn:length(listaEntradas)>0}"><input type="button"  class="botones"  name="cmdRegresar2" value="Regresar"  onclick="history.go(-1)" style="width:120px"/>
      </c:if><input   name="cmdcargar" type="button" class="botones" onclick="cargarMovimientosOP()" value="Cargar movimientos" style="width:150px" />
      </span></td>
  </tr>
</table>
</c:if>
</body>
</html>