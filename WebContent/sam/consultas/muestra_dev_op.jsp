
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="muestra_dev_op.js?x=<%=System.currentTimeMillis()%>"></script>
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
</script>
</head>
<body>
<table width="95%" align="center">
  <tr>
    <td><h1>Listado de Facturas
        <c:out value='${desMes}'/>
    </h1>
	  <div style="position: absolute;right: 34px;top: 30px;">
	  	<input class="btn btn-info btn-sm" name="btnGeneraOp" id="btnGeneraOp" onclick="generarOPS()" value="Generar Orden de Pago" style="width:150px" type="button">
	  </div>
    </td>
  </tr>
</table>

<div>
	<select name="idtipogasto" class="comboBox" id="idtipogasto" style="width:100%">
         <c:forEach items="${tipoGastos}" var="item" varStatus="status">                  
         <option value="<c:out value='${item.ID}'/>" <c:if test='${item.ID==tipoGasto}'> selected </c:if>><c:out value="${item.RECURSO}"/></option>
         </c:forEach>
    </select>
</div>

<table class="listas" id="listadevengado" border="0" align="center" cellpadding="0" cellspacing="0" width="95%">
  <tr bgcolor="#889FC9">
    <th width="4%" height="21" align="center">&nbsp;</th>
    <th width="20%" align="center">Num. Documento</th>
    <th width="20%" align="center">Fecha</th>
    <th width="12%" height="21" align="center">Beneficiario</th>
    <th width="35%" align="center">Descripción</th>
    <th width="15%" align="center">Importe</th>
  </tr>
    <c:set var="cont" value="${0}" />
  <c:forEach items="${listaFacturas}" var="item" varStatus="status" ><!-- listaFacturas es -->
  <c:set var="cont" value="${cont+1}" /> 
        <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
          <tr>
            <td height="20" align="center"><input type="checkbox" value="<c:out value='${item.CVE_FACTURA}'/>" id="chkfacturas" name="chkfacturas" /></td>
            <td height="20" align="left"><a href="javascript:cargarFactura(${item.CVE_FACTURA});"><c:out value='${item.NUM_FACTURA}'/></a></td>
        <td height="20" align="center"><c:out value='${item.FECHA}'/></td>
        <td width="14%" height="20" align="left"><c:out value='${item.NCOMERCIA}'/></td>
        <input name="id_beneficiario" type="hidden"  id="id_beneficiario" size="8" maxlength="6" readonly <c:out value='${item.clv_beneficiario}'/> />
        <td width="35%" height="20" align="left"><c:out value='${item.NOTAS}'/></td>
        <td width="15%" height="20" align="right">$<fmt:formatNumber value='${item.TOTAL}' pattern="###,###,###.00"/>&nbsp;</td>
        </tr>
  </c:forEach>
   <tr>
          <td height="34" style="background:#FFF" colspan="6" align="left"><c:if test="${cont>0}"></c:if> <input   name="cmdcargar" type="button" class="botones" onclick="cargarDocumentos()" value="Cargar documentos" style="width:150px" /></td>
          </tr>
</table>
</body>
</html>