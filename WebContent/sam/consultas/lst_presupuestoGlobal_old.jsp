<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Muestra presupuesto</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script language="javascript">
<!--
function buscar(){
	$('#forma').attr('action', 'lst_presupuestoGlobal.action');
	$('#forma').submit();
}

function getPresupuestoCalenPDF(){
	$('#forma').attr('target',"impresionListaPresupuestoPDF");
	$('#forma').attr('action',"../reportes/rpt_presupuesto_calendarizado.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_presupuestoGlobal.action");
}

-->
</script>
<style type="text/css">
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
</style>
</head>
<body >
<table width="95%" align="center"><tr><td><h1>Consultas - Presupuesto Global</h1></td></tr></table>
  <form name="forma" id="forma" method="post" action="lst_presupuestoGlobal.action">
  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="listas">
    <tr  >
      <td colspan="3" align="right">&nbsp;</td>
      <td colspan="11">&nbsp;</td>
    </tr>
    <tr  >
      <td height="30" colspan="3" align="right"><input type="hidden" name="unidad2" id="unidad2" value='<c:out value="${unidad}"/>' />
      Unidad Administrativa:      </td>
      <td colspan="11">
          <select name="unidad" class="comboBox" id="unidad" onchange="javascript:ShowDelay('Cargando información presupuestal');forma.submit();" style="width:450px">
            <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
              <option value='<c:out value="${item.ID}"/>' 
                        <c:if test='${item.ID==unidad}'> selected </c:if>>
              <c:out value='${item.DEPENDENCIA}'/>
              </option>
            </c:forEach>
          </select>
	  </td>
    </tr>
    <tr  >
      <td colspan="3" height="30" align="right">Tipos de Gasto:</td>
      <td colspan="11"><select name="tipoGasto" class="comboBox" id="tipoGasto" onchange="javascript:ShowDelay('Cargando información presupuestal');forma.submit();" style="width:450px">
        <option value="0">[Tipo de gasto]</option>
       <c:forEach items="${tiposGastos}" var="item" varStatus="status"> 
                      <option value='<c:out value="${item.ID}"/>' 
                        <c:if test='${item.ID==tipoGasto}'> selected </c:if>>
                        <c:out value='${item.RECURSO}'/>
            </option>
          </c:forEach>
      </select>
        
      &nbsp;</td>
    </tr>
    <tr  >
      <td colspan="3" height="30" align="right">Programa:</td>
      <td colspan="11"><strong>
        <select name="proyecto" class="comboBox" id="proyecto" style="width:450px" onchange="javascript:forma.submit();">
          <option value="0">[Programas]</option>
          <c:forEach items="${programas}" var="item" varStatus="status">
            <option value='<c:out value="${item.ID_PROYECTO}"/>' 
              <c:if test='${item.ID_PROYECTO==proyecto}'> selected</c:if>
              >
              [<c:out value='${item.ID_PROYECTO}'/>]&nbsp;<c:out value='${item.PROYECTO}'/> - <c:out value='${item.DECRIPCION}'/>
            </option>
          </c:forEach>
        </select>
      </strong></td>
    </tr>
    <tr  >
      <td colspan="3" height="30" align="right">  Partida:</td>
      <td colspan="11"><strong>
        <select name="partida" class="comboBox" id="partida" style="width:450px" onchange="javascript:forma.submit();">
        <option value="0">[Partidas]</option>
        <c:forEach items="${partidas}" var="item" varStatus="status"> <option value='<c:out value="${item.CLV_PARTID}"/>' 
                
          <c:if test='${item.CLV_PARTID==partida}'> selected</c:if>
          >
          <c:out value='${item.CLV_PARTID}'/> - <c:out value='${item.PARTIDA}'/>
          </option>
        </c:forEach>
      </select>
      </strong></td>
    </tr>
	<tr  >
      <td colspan="14">&nbsp;</td>
    </tr>
    </table>
    <table width="90%" class="listas" align="center" cellpadding="0" cellspacing="0">
    <c:set var="CON" value="${0}" />
    <c:set var="TAM" value="${fn:length(presupuesto)}" />
    <c:set var="AUTORIZADO" value="${0}" />
    <c:set var="SUM_AUTORIZADO" value="${0}" />
    <c:set var="PRECOMPROMETIDO" value="${0}" />
    <c:set var="SUM_PRECOMPROMETIDO" value="${0}" />
    <c:set var="COMPROMETIDO" value="${0}" />
    <c:set var="SUM_COMPROMETIDO" value="${0}" />
    <c:set var="DEVENGADO" value="${0}" />
    <c:set var="SUM_DEVENGADO" value="${0}" />
    <c:set var="EJERCIDO" value="${0}" />
    <c:set var="SUM_EJERCIDO" value="${0}" />
    <c:set var="DISPONIBLE" value="${0}" />
    <c:set var="SUM_DISPONIBLE" value="${0}" />
    <c:set var="INICIO" value="${0}" />
    <c:set var="ID_PROYECTO_ACTUAL" value="${0}" />
    <c:set var="ID_PROYECTO_ANTERIOR" value="${0}" />
    
    <c:forEach items="${presupuesto}" var="item" varStatus="status">  
    	<c:set var="CON" value="${CON+1}" />
    	<c:set var="ID_PROYECTO_ACTUAL" value="${item.ID_PROYECTO}" />
       	<c:set var="AUTORIZADO" value="${(item.ENEPREINI+item.ENEPREAMP-item.ENEPRERED) + (item.FEBPREINI+item.FEBPREAMP-item.FEBPRERED) + (item.MARPREINI+item.MARPREAMP-item.MARPRERED) + (item.ABRPREINI+item.ABRPREAMP-item.ABRPRERED) + (item.MAYPREINI+item.MAYPREAMP-item.MAYPRERED) + (item.JUNPREINI+item.JUNPREAMP-item.JUNPRERED) + (item.JULPREINI+item.JULPREAMP-item.JULPRERED) + (item.AGOPREINI+item.AGOPREAMP-item.AGOPRERED) + (item.SEPPREINI+item.SEPPREAMP-item.SEPPRERED) + (item.OCTPREINI+item.OCTPREAMP-item.OCTPRERED) + (item.NOVPREINI+item.NOVPREAMP-item.NOVPRERED) + (item.DICPREINI+item.DICPREAMP-item.DICPRERED)}" />
    	<c:set var="PRECOMPROMETIDO" value="${0.0+item.ENEPREREQ + item.FEBPREREQ + item.MARPREREQ + item.ABRPREREQ + item.MAYPREREQ + item.JUNPREREQ + item.JULPREREQ + item.AGOPREREQ + item.SEPPREREQ + item.OCTPREREQ + item.NOVPREREQ + item.DICPREREQ}" />
        <c:set var="COMPROMETIDO" value="${0.0+item.ENEPRECOM + item.FEBPRECOM + item.MARPRECOM + item.ABRPRECOM + item.MAYPRECOM + item.JUNPRECOM + item.JULPRECOM + item.AGOPRECOM + item.SEPPRECOM + item.OCTPRECOM + item.NOVPRECOM + item.DICPRECOM}" />
        <c:set var="DEVENGADO" value="${0.0+item.ENEPREDEV + item.FEBPREDEV + item.MARPREDEV + item.ABRPREDEV + item.MAYPREDEV + item.JUNPREDEV + item.JULPREDEV + item.AGOPREDEV + item.SEPPREDEV + item.OCTPREDEV + item.NOVPREDEV + item.DICPREDEV}" />
        <c:set var="EJERCIDO" value="${0.0+item.ENEPREEJE + item.FEBPREEJE + item.MARPREEJE + item.ABRPREEJE + item.MAYPREEJE + item.JUNPREEJE + item.JULPREEJE + item.AGOPREEJE + item.SEPPREEJE + item.OCTPREEJE + item.NOVPREEJE + item.DICPREEJE}" />
        <c:set var="DISPONIBLE" value="${0.0+(AUTORIZADO - (PRECOMPROMETIDO + COMPROMETIDO + DEVENGADO + EJERCIDO))}" />
        
        <c:if test="${ID_PROYECTO_ACTUAL!=ID_PROYECTO_ANTERIOR}">
        	<c:if test="${INICIO==1}">
                <tr>
                  <td height="20" colspan="2" align="right"><strong>Totales: </strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_AUTORIZADO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_PRECOMPROMETIDO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_COMPROMETIDO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_DEVENGADO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_EJERCIDO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_DISPONIBLE}' type="currency" currencySymbol="$ " /><strong></td>
                </tr>
        	</c:if>
            <c:set var="SUM_AUTORIZADO" value="${0}" />
            <c:set var="SUM_PRECOMPROMETIDO" value="${0}" />
            <c:set var="SUM_COMPROMETIDO" value="${0}" />
            <c:set var="SUM_DEVENGADO" value="${0}" />
            <c:set var="SUM_EJERCIDO" value="${0}" />
            <c:set var="SUM_DISPONIBLE" value="${0}" />
        </c:if>
        
        <c:if test="${ID_PROYECTO_ACTUAL!=ID_PROYECTO_ANTERIOR}">
            <tr>
              <td height="20" colspan="8" bgcolor="#FFFFFF">&nbsp;</td>
            </tr>
            <tr>
              <th width="12%" height="20">Proyecto</th>
              <td height="20" colspan="7" align="left"><strong>[<c:out value='${item.ID_PROYECTO}'/>] <c:out value='${item.N_PROGRAMA}'/> - <c:out value='${item.DECRIPCION}'/></strong></td>
              </tr>
            <tr>
              <th height="20" colspan="2">Partida</th>
              <th width="12%">Autorizado</th>
              <th width="11%" height="20">Precomprometido</th>
              <th width="12%">Comprometido</th>
              <th width="10%">Devengado</th>
              <th width="10%">Ejercido</th>
              <th width="10%">Disponible</th>
            </tr>
        </c:if>
        <tr>
          <td height="20" colspan="2" align="left"><c:out value='${item.clv_partid}'/> - <c:out value='${item.partida}'/></td>
          <td height="20" align="right"><fmt:formatNumber value='${AUTORIZADO}' type="currency" currencySymbol="$ " /></td>
          <td height="20" align="right"><c:if test="${PRECOMPROMETIDO>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 0, 'PRECOMPROMETIDO')"></c:if><fmt:formatNumber value='${PRECOMPROMETIDO}' type="currency" currencySymbol="$ "/></a></td>
          <td height="20" align="right"><c:if test="${COMPROMETIDO>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 0, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${COMPROMETIDO}' type="currency" currencySymbol="$ "/></a></td>
          <td height="20" align="right"><c:if test="${DEVENGADO>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 0, 'DEVENGADO')"></c:if><fmt:formatNumber value='${DEVENGADO}' type="currency" currencySymbol="$ "/></a></td>
          <td height="20" align="right"><c:if test="${EJERCIDO>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 0, 'EJERCIDO')"></c:if><fmt:formatNumber value='${EJERCIDO}' type="currency" currencySymbol="$ "/></a></td>
          <td height="20" align="right"><fmt:formatNumber value='${DISPONIBLE}' type="currency" currencySymbol="$ "/></td>
        </tr>
        <c:set var="INICIO" value="${1}" />
    	<c:set var="ID_PROYECTO_ANTERIOR" value="${item.ID_PROYECTO}" />
        <c:set var="SUM_AUTORIZADO" value="${SUM_AUTORIZADO+AUTORIZADO}" />
        <c:set var="SUM_PRECOMPROMETIDO" value="${SUM_PRECOMPROMETIDO+PRECOMPROMETIDO}" />
        <c:set var="SUM_COMPROMETIDO" value="${SUM_COMPROMETIDO+COMPROMETIDO}" />
        <c:set var="SUM_DEVENGADO" value="${SUM_DEVENGADO+DEVENGADO}" />
        <c:set var="SUM_EJERCIDO" value="${SUM_EJERCIDO+EJERCIDO}" />
        <c:set var="SUM_DISPONIBLE" value="${SUM_DISPONIBLE+DISPONIBLE}" />
        
        <c:if test="${TAM==CON}">
                <tr>
                  <td height="20" colspan="2" align="right"><strong>Totales: </strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_AUTORIZADO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_PRECOMPROMETIDO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_COMPROMETIDO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_DEVENGADO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_EJERCIDO}' type="currency" currencySymbol="$ " /><strong></td>
                  <td height="20" align="right"><strong><fmt:formatNumber value='${SUM_DISPONIBLE}' type="currency" currencySymbol="$ " /><strong></td>
                </tr>
        </c:if>
            
    </c:forEach>
  </table>
  </form>
</body>
</html>
