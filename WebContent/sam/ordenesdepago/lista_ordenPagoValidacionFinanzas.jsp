<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Listado de Ordenes de Pago por Ejercer</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type='text/javascript' src="../../dwr/interface/controladorListadoOrdenPagoEjercidoValidaFinanzasRemoto.js"></script>
<script type='text/javascript' src="../../dwr/engine.js"></script>
<script type="text/javascript" src="lista_ordenPagoValidacionFinanzas.js"> </script>
<style type="text/css">
<!--
a:link {
	text-decoration: none;
}
a:visited {
	text-decoration: none;
}
a:hover {
	text-decoration: underline;
}
a:active {
	text-decoration: none;
}
-->
</style></head>
<body class="Fondo" >
<form  action="../reportes/requisicion.action" method="POST" id="forma" name="forma">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" name="cve_op" id="cve_op">
<table width="95%" align="center"><tr><td><h1>Ordenes de Pago - Listado de Ordenes de Pago Ejercidas para Validación Finanzas</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th height="16" >    
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th width="13%" height="31" >Mes :
      <td width="13%"><strong>
        <select name="cbomes" id="cbomes" style="width:110px">
          <option value="1" <c:if test='${mes==1}'>selected</c:if>>Enero</option>
          <option value="2" <c:if test='${mes==2}'>selected</c:if>>Febrero</option>
          <option value="3" <c:if test='${mes==3}'>selected</c:if>>Marzo</option>
          <option value="4" <c:if test='${mes==4}'>selected</c:if>>Abril</option>
          <option value="5" <c:if test='${mes==5}'>selected</c:if>>Mayo</option>
          <option value="6" <c:if test='${mes==6}'>selected</c:if>>Junio</option>
          <option value="7" <c:if test='${mes==7}'>selected</c:if>>Julio</option>
          <option value="8" <c:if test='${mes==8}'>selected</c:if>>Agosto</option>
          <option value="9" <c:if test='${mes==9}'>selected</c:if>>Septiembre</option>
          <option value="10" <c:if test='${mes==10}'>selected</c:if>>Octubre</option>
          <option value="11" <c:if test='${mes==11}'>selected</c:if>>Noviembre</option>
          <option value="12" <c:if test='${mes==12}'>selected</c:if>>Diciembre</option>
        </select>
      </strong></td>
    <td width="34%">&nbsp;&nbsp;<input  name="btnBuscar" type="button" class="botones" id="btnBuscar" value="Buscar" style="width:100px"></td>
    <td width="20%" >&nbsp;</td>
    <td width="20%" >&nbsp;</td>
  </tr>
  <tr >
    <th height="15" >Fecha de Validación:    
    <td><input name="txtfechanueva" type="text" id="txtfechanueva" style="width:100px" maxlength="10" value="<c:out value="${fecha}"/>"/></td>
    <td>&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="15" >    
    <td colspan="4"><strong>Nota:</strong> Las Ordenes de Pago marcadas con (*) no se podran desejercer hasta que se solicite se remueva el seguro de validación al area de Finanzas con el Sr. Peredo.</td>
    </tr>
  </table>
<br />
<table width="95%" class="listas" align="center" id="listaRequisiciones" cellpadding="0" cellspacing="0">
 <thead>
  <tr>
    <th width="2%" height="17">&nbsp;</th>
    <th width="2%">N°</th>
    <th width="6%">Número</th>
    <th width="7%">Fecha</th>
    <th width="8%">Fecha Validación</th>
    <th width="28%">Beneficiario</th>
    <th width="33%">Concepto</th>
    <th width="9%">Importe</th>
    <th width="6%">Opciones</th>
  </tr>
   </thead>   
<tbody>  
<c:set var="cont" value="${0}" /> 
<c:forEach items="${lstOrdenesPagoDesejercer}" var="item" varStatus="status"> 
<c:set var="cont" value="${cont+1}" /> 
  <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
    <tr>
      <td align="center"><c:if test="${item.FEINI_TSOR!=NULL}">*</c:if><c:if test="${item.FEINI_TSOR==NULL||item.FEINI_TSOR==''}"><input type="checkbox" id="chkOP" name="chkOP" value="<c:out value='${item.CVE_OP}'/>"/></c:if></td>
    <td align="center"><c:out value='${cont}'/></td>
    <td align="center"><c:out value='${item.NUM_OP}'/></td>
    <td align="center"><c:out value='${item.FECHA}'/></td>
    <td align="center"><c:out value='${item.FEINI_TSOR}'/></td>
    <td align="left"><c:out value='${item.NCOMERCIA}'/></td>
    <td><c:out value='${item.NOTA}'/></td>
    <td align="right"><fmt:formatNumber value="${item.IMPORTE}"  pattern="#,###,###,##0.00" /> </td>
    <td align="center">
    <img style="cursor:pointer" src="../../imagenes/pdf.gif" alt="Ver Documento" border="0" width="14" height="16" onClick="getReporteOP(<c:out value='${item.CVE_OP}'/>)">
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_BITACORA_EN_ORDENES_DE_PAGO">
        	&nbsp;<img style="cursor:pointer" src="../../imagenes/report_user.png" border="0" width="16" height="16" title="Ver bitacora de Orden de Pago" onClick="bitacoraDocumento(<c:out value='${item.CVE_OP}'/>, 'OP')">     
    </sec:authorize>
    </td>
  </tr>
  </c:forEach>
  <tr>
    <td colspan="9" align="left"><input type="button" value="Validar OP" id="cmdvalidar" class="botones"/></td>
  </tr>
  </tbody>  
</table>
</form>
</body>
</html>
