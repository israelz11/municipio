<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Muestra presupuesto</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>

<link rel="stylesheet" href="../../include/css/sweetalert2.css" type="text/css"/>
<!--  
	<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css"/>
	<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
-->
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>

<script type="text/javascript" src="../../include/js/sweetalert2.js"></script>

<script language="javascript">

function buscar(){
	$('#forma').attr('action', 'lst_presupuesto.action');
	$('#forma').submit();
}

function getPresupuestoCalenPDF(){
	$('#forma').attr('target',"impresionListaPresupuestoPDF");
	$('#forma').attr('action',"../reportes/rpt_presupuesto_calendarizado.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"lst_presupuesto.action");
}


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
<table width="95%" align="center"><tr><td><h1>Consultas - Presupuesto Calendarizado</h1></td></tr></table>
  <form name="forma" id="forma" method="post" action="lst_presupuesto.action">
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
    <tr>
      <th colspan="7" height="20">Programa</th>
      <th colspan="7" height="20">Partida</th>
    </tr>
     <c:forEach items="${presupuesto}" var="item" varStatus="status">          
    <tr >
      <td height="20" colspan="7"><strong>[<c:out value='${item.ID_PROYECTO}'/>] [ <c:out value='${item.K_PROYECTO_T}'/>] <c:out value='${item.N_PROGRAMA}'/> - <c:out value='${item.DECRIPCION}'/></strong></td>
      <td colspan="7"><strong><c:out value='${item.clv_partid}'/> - <c:out value='${item.partida}'/></strong></td>
    </tr>
    <tr  align="center">
      <th height="20">Mes</th>
      <th width="7%">Autorizado</th>
      <th width="7%">Pre-Comprometido</th>
      <th width="7%">Comprometido</th>
      <th width="7%">Devengado</th>
      <th width="7%">Ejercido</th>
      <th width="7%">Disponible</th>
      <th>Mes</th>
      <th width="8%">Autorizado</th>
      <th width="8%">Pre-Comprometido</th>
      <th width="8%">Comprometido</th>
      <th width="8%">Devengado</th>
      <th width="8%">Ejercido</th>
      <th width="8%">Disponible</th>
    </tr>    
    <tr>
      <td bgcolor="#575757" align="center" height="20" width="3%"><span style="color:#FFF">Enero</span></td>
      <td width="7%" align="right"><fmt:formatNumber value='${item.ENEPREINI+item.ENEPREAMP-item.ENEPRERED}' type="currency" currencySymbol="$ " /></td>
      <td width="7%" align="right"><c:if test="${item.ENEPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 1, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.ENEPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.ENEPREREQ}' type="currency" currencySymbol="$ "/></a></td>
      <td width="7%" align="right"><c:if test="${item.ENEPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 1, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.ENEPRECOM}' type="currency" currencySymbol="$ "/></a></td>
      <td width="7%" align="right"><c:if test="${item.ENEPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 1, 'DEVENGADO')"></c:if><c:if test="${item.ENEPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.ENEPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><c:if test="${item.ENEPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 1, 'EJERCIDO')"></c:if><c:if test="${item.ENEPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.ENEPREEJE}' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><span <c:if test="${(item.ENEPREINI+item.ENEPREAMP-item.ENEPRERED-item.ENEPRECOM-item.ENEPREDEV-item.ENEPREREQ-item.ENEPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.ENEPREINI+item.ENEPREAMP-item.ENEPRERED-item.ENEPRECOM-item.ENEPREDEV-item.ENEPREREQ-item.ENEPREEJE}' type="currency" currencySymbol="$ " /></span></td>
      <td align="center"  bgcolor="#575757" width="3%"><span style="color:#FFF">Febrero</span></td>
      <td width="8%" align="right"><fmt:formatNumber value='${item.FEBPREINI+item.FEBPREAMP-item.FEBPRERED}' type="currency" currencySymbol="$ " /></td>
      <td width="9%" align="right"><c:if test="${item.FEBPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 2, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.FEBPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.FEBPREREQ}' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><c:if test="${item.FEBPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 2, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.FEBPRECOM }' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><c:if test="${item.FEBPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 2, 'DEVENGADO')"></c:if><c:if test="${item.FEBPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.FEBPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td width="9%" align="right"><c:if test="${item.FEBPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 2, 'EJERCIDO')"></c:if><c:if test="${item.FEBPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.FEBPREEJE}' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><span <c:if test="${(item.FEBPREINI+item.FEBPREAMP-item.FEBPRERED-item.FEBPRECOM-item.FEBPREDEV-item.FEBPREREQ-item.FEBPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.FEBPREINI+item.FEBPREAMP-item.FEBPRERED-item.FEBPRECOM-item.FEBPREDEV-item.FEBPREREQ-item.FEBPREEJE}' type="currency" currencySymbol="$ " /></span></td>
    </tr>
    <tr >
      <td bgcolor="#575757" align="center" height="22" width="3%"><span style="color:#FFF">Marzo</span></td>
      <td bgcolor="#CCCCCC" width="7%" align="right"><fmt:formatNumber value='${item.MARPREINI+item.MARPREAMP-item.MARPRERED}' type="currency" currencySymbol="$ " /></td>
      <td bgcolor="#CCCCCC" width="7%" align="right"><c:if test="${item.MARPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 3, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.MARPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.MARPREREQ }' type="currency" currencySymbol="$ " /></a></td>
      <td width="7%" align="right" bgcolor="#CCCCCC"><c:if test="${item.MARPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 3, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.MARPRECOM }' type="currency" currencySymbol="$ " /></a></td>
      <td width="7%" align="right" bgcolor="#CCCCCC"><c:if test="${item.MARPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 3, 'DEVENGADO')"></c:if><c:if test="${item.MARPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.MARPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td bgcolor="#CCCCCC" width="8%" align="right"><c:if test="${item.MARPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 3, 'EJERCIDO')"></c:if><c:if test="${item.MARPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.MARPREEJE }' type="currency" currencySymbol="$ " /></a></td>
      <td bgcolor="#CCCCCC" width="8%" align="right"><span <c:if test="${(item.MARPREINI+item.MARPREAMP-item.MARPRERED-item.MARPRECOM-item.MARPREDEV-item.MARPREREQ-item.MARPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.MARPREINI+item.MARPREAMP-item.MARPRERED-item.MARPRECOM-item.MARPREDEV-item.MARPREREQ-item.MARPREEJE}' type="currency" currencySymbol="$ " /></span></td>
      <td align="center"  bgcolor="#575757" width="3%"><span style="color:#FFF">Abril</span></td>
      <td bgcolor="#CCCCCC" width="8%" align="right"><fmt:formatNumber value='${item.ABRPREINI+item.ABRPREAMP-item.ABRPRERED}' type="currency" currencySymbol="$ " /></td>
      <td bgcolor="#CCCCCC" width="9%" align="right"><c:if test="${item.ABRPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 4, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.ABRPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.ABRPREREQ }' type="currency" currencySymbol="$ " /></a></td>
      <td width="8%" align="right" bgcolor="#CCCCCC"><c:if test="${item.ABRPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 4, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.ABRPRECOM }' type="currency" currencySymbol="$ " /></a></td>
      <td width="8%" align="right" bgcolor="#CCCCCC"><c:if test="${item.ABRPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 4, 'DEVENGADO')"></c:if><c:if test="${item.ABRPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.ABRPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td bgcolor="#CCCCCC" width="9%" align="right"><c:if test="${item.ABRPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 4, 'EJERCIDO')"></c:if><c:if test="${item.ABRPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.ABRPREEJE }' type="currency" currencySymbol="$ " /></a></td>
      <td bgcolor="#CCCCCC" width="8%" align="right"><span <c:if test="${(item.ABRPREINI+item.ABRPREAMP-item.ABRPRERED-item.ABRPRECOM-item.ABRPREDEV-item.ABRPREREQ-item.ABRPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.ABRPREINI+item.ABRPREAMP-item.ABRPRERED-item.ABRPRECOM-item.ABRPREDEV-item.ABRPREREQ-item.ABRPREEJE}' type="currency" currencySymbol="$ " /></span></td>
    </tr>
    <tr >
      <td height="20"  bgcolor="#575757" align="center" width="3%"><span style="color:#FFF">Mayo</span></th>
      <td width="7%" align="right"><fmt:formatNumber value='${item.MAYPREINI+item.MAYPREAMP-item.MAYPRERED}' type="currency" currencySymbol="$ " /></td>
      <td width="7%" align="right"><c:if test="${item.MAYPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 5, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.MAYPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.MAYPREREQ }' type="currency" currencySymbol="$ "/></a></td>
      <td width="7%" align="right"><c:if test="${item.MAYPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 5, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.MAYPRECOM }' type="currency" currencySymbol="$ "/></a></td>
      <td width="7%" align="right"><c:if test="${item.MAYPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 5, 'DEVENGADO')"></c:if><c:if test="${item.MAYPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.MAYPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><c:if test="${item.MAYPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 5, 'EJERCIDO')"></c:if><c:if test="${item.MAYPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.MAYPREEJE }' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><span <c:if test="${(item.MAYPREINI+item.MAYPREAMP-item.MAYPRERED-item.MAYPRECOM-item.MAYPREDEV-item.MAYPREREQ-item.MAYPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.MAYPREINI+item.MAYPREAMP-item.MAYPRERED-item.MAYPRECOM-item.MAYPREDEV-item.MAYPREREQ-item.MAYPREEJE}' type="currency" currencySymbol="$ " /></span></td>
      <td align="center"  bgcolor="#575757" width="3%"><span style="color:#FFF">Junio</span></td>
      <td width="8%" align="right"><fmt:formatNumber value='${item.JUNPREINI+item.JUNPREAMP-item.JUNPRERED}' type="currency" currencySymbol="$ " /></td>
      <td width="9%" align="right"><c:if test="${item.JUNPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 6, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.JUNPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.JUNPREREQ }' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><c:if test="${item.JUNPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 6, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.JUNPRECOM }' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><c:if test="${item.JUNPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 6, 'DEVENGADO')"></c:if><c:if test="${item.JUNPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.JUNPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td width="9%" align="right"><c:if test="${item.JUNPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 6, 'EJERCIDO')"></c:if><c:if test="${item.JUNPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.JUNPREEJE }' type="currency" currencySymbol="$ " /></a></td>
      <td width="8%" align="right"><span <c:if test="${(item.JUNPREINI+item.JUNPREAMP-item.JUNPRERED-item.JUNPRECOM-item.JUNPREDEV-item.JUNPREREQ-item.JUNPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.JUNPREINI+item.JUNPREAMP-item.JUNPRERED-item.JUNPRECOM-item.JUNPREDEV-item.JUNPREREQ-item.JUNPREEJE}' type="currency" currencySymbol="$ " /></span></td>
    </tr>
    <tr >
      <td align="center"  bgcolor="#575757" height="20" width="3%"><span style="color:#FFF">Julio</span></th>
      <td bgcolor="#CCCCCC"  width="7%" align="right"><fmt:formatNumber value='${item.JULPREINI+item.JULPREAMP-item.JULPRERED}' type="currency" currencySymbol="$ " /></td>
      <td bgcolor="#CCCCCC"  width="7%" align="right"><c:if test="${item.JULPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 7, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.JULPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.JULPREREQ }' type="currency" currencySymbol="$ "/></a></td>
      <td  width="7%" align="right" bgcolor="#CCCCCC"><c:if test="${item.JULPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 7, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.JULPRECOM}' type="currency" currencySymbol="$ " /></a></td>
      <td  width="7%" align="right" bgcolor="#CCCCCC"><c:if test="${item.JULPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 7, 'DEVENGADO')"></c:if><c:if test="${item.JULPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.JULPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td bgcolor="#CCCCCC"  width="8%" align="right"><c:if test="${item.JULPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 7, 'EJERCIDO')"></c:if><c:if test="${item.JULPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.JULPREEJE }' type="currency" currencySymbol="$ " /></a></td>
      <td bgcolor="#CCCCCC"  width="8%" align="right"><span <c:if test="${(item.JULPREINI+item.JULPREAMP-item.JULPRERED-item.JULPRECOM-item.JULPREDEV-item.JULPREREQ-item.JULPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.JULPREINI+item.JULPREAMP-item.JULPRERED-item.JULPRECOM-item.JULPREDEV-item.JULPREREQ-item.JULPREEJE}' type="currency" currencySymbol="$ " /></span></td>
      <td align="center"  bgcolor="#575757" width="3%"><span style="color:#FFF">Agosto</span></td>
      <td bgcolor="#CCCCCC"  width="8%" align="right"><fmt:formatNumber value='${item.AGOPREINI+item.AGOPREAMP-item.AGOPRERED}' type="currency" currencySymbol="$ " /></td>
      <td bgcolor="#CCCCCC"  width="9%" align="right"><c:if test="${item.AGOPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 8, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.AGOPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.AGOPREREQ }' type="currency" currencySymbol="$ "/></a></td>
      <td  width="8%" align="right" bgcolor="#CCCCCC"><c:if test="${item.AGOPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 8, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.AGOPRECOM }' type="currency" currencySymbol="$ " /></a></td>
      <td  width="8%" align="right" bgcolor="#CCCCCC"><c:if test="${item.AGOPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 8, 'DEVENGADO')"></c:if><c:if test="${item.AGOPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.AGOPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td bgcolor="#CCCCCC"  width="9%" align="right"><c:if test="${item.AGOPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 8, 'EJERCIDO')"></c:if><c:if test="${item.AGOPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.AGOPREEJE }' type="currency" currencySymbol="$ "/></a></td>
      <td bgcolor="#CCCCCC"  width="8%" align="right"><span <c:if test="${(item.AGOPREINI+item.AGOPREAMP-item.AGOPRERED-item.AGOPRECOM-item.AGOPREDEV-item.AGOPREREQ-item.AGOPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.AGOPREINI+item.AGOPREAMP-item.AGOPRERED-item.AGOPRECOM-item.AGOPREDEV-item.AGOPREREQ-item.AGOPREEJE}' type="currency" currencySymbol="$ " /></span></td>
    </tr>
    <tr >
      <td align="center" bgcolor="#575757" height="20" width="3%"><span style="color:#FFF">Septiembre&nbsp;</span></td>
      <td width="7%" align="right"><fmt:formatNumber value='${item.SEPPREINI+item.SEPPREAMP-item.SEPPRERED}' type="currency" currencySymbol="$ " /></td>
      <td width="7%" align="right"><c:if test="${item.SEPPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 9, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.SEPPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.SEPPREREQ }' type="currency" currencySymbol="$ "/></a></td>
      <td width="7%" align="right"><c:if test="${item.SEPPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 9, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.SEPPRECOM}' type="currency" currencySymbol="$ "/></a></td>
      <td width="7%" align="right"><c:if test="${item.SEPPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 9, 'DEVENGADO')"></c:if><c:if test="${item.SEPPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.SEPPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><c:if test="${item.SEPPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 9, 'EJERCIDO')"></c:if><c:if test="${item.SEPPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.SEPPREEJE}' type="currency" currencySymbol="$ " /></a></td>
      <td width="8%" align="right"><span <c:if test="${(item.SEPPREINI+item.SEPPREAMP-item.SEPPRERED-item.SEPPRECOM-item.SEPPREDEV-item.SEPPREREQ-item.SEPPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.SEPPREINI+item.SEPPREAMP-item.SEPPRERED-item.SEPPRECOM-item.SEPPREDEV-item.SEPPREREQ-item.SEPPREEJE}' type="currency" currencySymbol="$ " /></span></td>
      <td align="center"  bgcolor="#575757" width="3%"><span style="color:#FFF">Octubre</span></td>
      <td width="8%" align="right"><fmt:formatNumber value='${item.OCTPREINI+item.OCTPREAMP-item.OCTPRERED}' type="currency" currencySymbol="$ " /></td>
      <td width="9%" align="right"><c:if test="${item.OCTPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 10, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.OCTPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.OCTPREREQ }' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><c:if test="${item.OCTPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 10, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.OCTPRECOM}' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><c:if test="${item.OCTPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 10, 'DEVENGADO')"></c:if><c:if test="${item.OCTPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.OCTPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td width="9%" align="right"><c:if test="${item.OCTPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 10, 'EJERCIDO')"></c:if><c:if test="${item.OCTPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.OCTPREEJE}' type="currency" currencySymbol="$ "/></a></td>
      <td width="8%" align="right"><span <c:if test="${(item.OCTPREINI+item.OCTPREAMP-item.OCTPRERED-item.OCTPRECOM-item.OCTPREDEV-item.OCTPREREQ-item.OCTPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.OCTPREINI+item.OCTPREAMP-item.OCTPRERED-item.OCTPRECOM-item.OCTPREDEV-item.OCTPREREQ-item.OCTPREEJE}' type="currency" currencySymbol="$ " /></span></td>
    </tr>
    <tr >
      <td align="center"  bgcolor="#575757" height="20" width="3%"><span style="color:#FFF">Noviembre</span></td>
      <td bgcolor="#CCCCCC"  width="7%" align="right"><fmt:formatNumber value='${item.NOVPREINI+item.NOVPREAMP-item.NOVPRERED}' type="currency" currencySymbol="$ " /></td>
      <td bgcolor="#CCCCCC"  width="7%" align="right"><c:if test="${item.NOVPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 11, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.NOVPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.NOVPREREQ }' type="currency" currencySymbol="$ " /></a></td>
      <td  width="7%" align="right" bgcolor="#CCCCCC"><c:if test="${item.NOVPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 11, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.NOVPRECOM }' type="currency" currencySymbol="$ "/></a></td>
      <td  width="7%" align="right" bgcolor="#CCCCCC"><c:if test="${item.NOVPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 11, 'DEVENGADO')"></c:if><c:if test="${item.NOVPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.NOVPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td bgcolor="#CCCCCC"  width="8%" align="right"><c:if test="${item.NOVPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 11, 'EJERCIDO')"></c:if><c:if test="${item.NOVPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.NOVPREEJE }' type="currency" currencySymbol="$ "/></a></td>
      <td bgcolor="#CCCCCC"  width="8%" align="right"><span <c:if test="${(item.NOVPREINI+item.NOVPREAMP-item.NOVPRERED-item.NOVPRECOM-item.NOVPREDEV-item.NOVPREREQ-item.NOVPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.NOVPREINI+item.NOVPREAMP-item.NOVPRERED-item.NOVPRECOM-item.NOVPREDEV-item.NOVPREREQ-item.NOVPREEJE}' type="currency" currencySymbol="$ " /></span></td>
      <td align="center"  bgcolor="#575757" width="3%"><span style="color:#FFF">&nbsp;Diciembre&nbsp;</span></td>
      <td bgcolor="#CCCCCC"  width="8%" align="right"><fmt:formatNumber value='${item.DICPREINI+item.DICPREAMP-item.DICPRERED}' type="currency" currencySymbol="$ " /></td>
      <td bgcolor="#CCCCCC"  width="9%" align="right"><c:if test="${item.DICPREREQ>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 12, 'PRECOMPROMETIDO')"></c:if><c:if test="${item.DICPREREQ==null}">$ 0.00</c:if><fmt:formatNumber value='${item.DICPREREQ }' type="currency" currencySymbol="$ " /></a></td>
      <td  width="8%" align="right" bgcolor="#CCCCCC"><c:if test="${item.DICPRECOM>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 12, 'COMPROMETIDO')"></c:if><fmt:formatNumber value='${item.DICPRECOM }' type="currency" currencySymbol="$ " /></a></td>
      <td  width="8%" align="right" bgcolor="#CCCCCC"><c:if test="${item.DICPREDEV>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 12, 'DEVENGADO')"></c:if><c:if test="${item.DICPREDEV==null}">$ 0.00</c:if><fmt:formatNumber value='${item.DICPREDEV}' type="currency" currencySymbol="$ "/></a></td>
      <td bgcolor="#CCCCCC"  width="9%" align="right"><c:if test="${item.DICPREEJE>0}"><a href="javascript:mostrarConsultaCompromiso(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.clv_partid}'/>', 12, 'EJERCIDO')"></c:if><c:if test="${item.DICPREEJE==null}">$ 0.00</c:if><fmt:formatNumber value='${item.DICPREEJE }' type="currency" currencySymbol="$ " /></a></td>
      <td bgcolor="#CCCCCC"  width="8%" align="right"><span <c:if test="${(item.DICPREINI+item.DICPREAMP-item.DICPRERED-item.DICPRECOM-item.DICPREDEV-item.DICPREREQ-item.DICPREEJE)<0}">style="color:#F00"</c:if>><fmt:formatNumber value='${item.DICPREINI+item.DICPREAMP-item.DICPRERED-item.DICPRECOM-item.DICPREDEV-item.DICPREREQ-item.DICPREEJE}' type="currency" currencySymbol="$ " /></span></td>
    </tr>
    <tr>
      <td style="border-left:none; border-right:none" bgcolor="#FFFFFF" colspan="14">&nbsp;</td>
      </tr>
    </c:forEach>
  </table>
  </form>
</body>
</html>
