<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Detalles de Orden de Pago</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>

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
<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="95%">
  <tr bgcolor="#889FC9">
    <th height="21" colspan="2" align="center">Número de Orden de Pago: <strong><c:out value='${op.NUM_OP}'/></strong></th>
  </tr>

       <tr>
        <th width="20%" height="20" align="left"><c:out value='${op.NUM_DOC}'/>
          Fecha</th>
        <td width="80%" height="20" align="left"><c:out value='${op.FECHA}'/></td>
  </tr>
       <tr>
         <th height="20" align="left">Periodo</th>
         <td height="20" align="left"><c:out value='${op.PERIODO_TEXT}'/></td>
       </tr>
       <tr>
         <th height="20" align="left">Unidad Administrativa</th>
         <td height="20" align="left"><c:out value='${op.UNIDAD_ELABORA}'/></td>
       </tr>
       <tr>
         <th height="20" align="left">Tipo de Gasto</th>
         <td height="20" align="left"><c:out value='${op.DIGITOS}'/> <c:out value='${op.RECURSO}'/></td>
       </tr>
       <tr>
         <th height="20" align="left">Beneficiario</th>
         <td height="20" align="left"><c:out value='${op.NCOMERCIA}'/></td>
       </tr>
       <tr>
         <th height="20" align="left">Importe</th>
         <td height="20" align="left">$<fmt:formatNumber value='${op.IMPORTE}' pattern="###,###,###.00"/></td>
       </tr>
       <tr>
         <th height="20" align="left" valign="middle">Notas</th>
         <td height="20" align="left" valign="top"><c:out value='${op.NOTA}'/></td>
       </tr>
</table>
<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="95%">
  <tr bgcolor="#889FC9">
    <th height="21" colspan="10" align="center">Movimientos</th>
  </tr>
  <tr>
    <th width="6%" height="20" align="left"><c:out value='${item.NUM_DOC}'/>
      Núm.</th>
    <th width="11%" height="20" align="center"><c:out value='${item.TIPO_DOC}'/>
    Programa
    <th width="8%" align="center">Partida</th>
    <th width="11%" align="center">Pres. Actual</th>
    <th width="9%" align="center">Comprometido</th>
    <th width="10%" align="center">Devengado</th>
    <th width="9%" align="center">Ejercido</th>
    <th width="12%" align="center">Disp. Mensual</th>
    <th width="13%" align="center">Disp. Anual</th>
    <th width="11%" align="center">Importe</th>
  </tr>
  <c:set var="cont" value="${0}" />
  <c:set var="total" value="${0}" />
  <c:set var="actual" value="${0}" />
  <c:forEach items="${mov}" var="item" varStatus="status" >
  <tr>
  	<c:set var="actual" value="${item.ACTUAL}" />
    <c:set var="anual" value="${item.ANUAL}" />
  	<c:set var="comprometido" value="${item.COMPROMETIDO}" />
    <c:set var="devengado" value="${item.DEVENGADO}" />
    <c:set var="ejercido" value="${item.EJERCIDO}" />
    
    <c:set var="diponible_mes" value="${actual-(comprometido+devengado+ejercido)}" />
    <c:set var="diponible_anual" value="${anual-(comprometido+devengado+ejercido)}" />
  	<c:set var="cont" value="${cont+1}" /> 
    <td height="20" align="center"><c:out value='${cont}'/></td>
    <td height="20" align="center"><c:out value='${item.N_PROGRAMA}'/></td>
    <td height="20" align="center"><c:out value='${item.CLV_PARTID}'/></td>
    <td height="20" align="center">$<fmt:formatNumber value='${item.ACTUAL}' pattern="###,###,###.00"/></td>
    <td height="20" align="center"><c:if test="${item.COMPROMETIDO>0}"><a title="Mostrar compromisos" href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>, '<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.CLV_PARTID}'/>', <c:out value='${item.PERIODO}'/>, 'COMPROMETIDO')"></c:if>$<fmt:formatNumber value='${item.COMPROMETIDO}' pattern="###,###,###.00"/></a></td>
    <td height="20" align="center"><c:if test="${item.DEVENGADO>0}"><a title="Mostrar compromisos" href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>, '<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.CLV_PARTID}'/>', <c:out value='${item.PERIODO}'/>, 'DEVENGADO')"></c:if>$<fmt:formatNumber value='${item.DEVENGADO}' pattern="###,###,###.00"/></a></td>
    <td height="20" align="center"><c:if test="${item.EJERCIDO>0}"><a title="Mostrar ejercido" href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>, '<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.CLV_PARTID}'/>', <c:out value='${item.PERIODO}'/>, 'EJERCIDO')"></c:if>$<fmt:formatNumber value='${item.EJERCIDO}' pattern="###,###,###.00"/></a></td>
    <td height="20" align="center"><span <c:if test="${diponible_mes<0}">style="color:#F00" </c:if>>$<fmt:formatNumber value='${diponible_mes}' pattern="###,###,###.00"/></span></td>
    <td height="20" align="center"><span <c:if test="${diponible_anual<0}">style="color:#F00" </c:if>>$<fmt:formatNumber value='${diponible_anual}' pattern="###,###,###.00"/></span></td>
    <td height="20" align="center">$<fmt:formatNumber value='${item.MONTO}' pattern="###,###,###.00"/></td>
    <c:set var="total" value="${total+item.MONTO}" /> 
  </tr>
  </c:forEach> 
  <tr style="background:#FFF">
    <td height="20" colspan="9" align="left">&nbsp;</td>
    <td height="20" align="center"><strong>$<fmt:formatNumber value='${total}' pattern="###,###,###.00"/></strong></td>
  </tr>
</table>
</body>
</html>