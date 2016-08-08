<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Evaluaciòn de Proyectos - Listado de Proyectos</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type='text/javascript' src="../../dwr/interface/ControladorMuestraListadoAvancesFisicosRemoto.js"></script>
<script type='text/javascript' src="../../dwr/engine.js"></script>
<script type='text/javascript' src="lista_evaluacion_proyectos.js"></script>
<script type="text/javascript" src="../../include/js/jquery.qtip-1.0/jquery.qtip-1.0.0-rc3.min.js"></script>

<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>

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
<form  action="../utilerias/lista_evaluacion_proyectos.action" method="POST" id="forma" name="forma">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<table width="95%" align="center"><tr><td><h1>Autoevaluación de Programas - Listado de programas</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th height="16" >    
    <td width="11%">&nbsp;</td>
    <td colspan="2">&nbsp;</td>
    <td width="18%" >&nbsp;</td>
    <td width="20%" >&nbsp;</td>
  </tr>
  <tr >
    <th height="30" >Mes:     
    <td colspan="3"><strong>
      <div class="styled-select">
        <select class="comboBox" name="cbomes" id="cbomes" style="width:110px" onChange="buscar();">
          <option value="1" 
        <c:if test='${mes==1}'>selected</c:if>
          >Enero</option>
          <option value="2" 
        <c:if test='${mes==2}'>selected</c:if>
          >Febrero</option>
          <option value="3" 
        <c:if test='${mes==3}'>selected</c:if>
          >Marzo</option>
          <option value="4" 
        <c:if test='${mes==4}'>selected</c:if>
          >Abril</option>
          <option value="5" 
        <c:if test='${mes==5}'>selected</c:if>
          >Mayo</option>
          <option value="6" 
        <c:if test='${mes==6}'>selected</c:if>
          >Junio</option>
          <option value="7" 
        <c:if test='${mes==7}'>selected</c:if>
          >Julio</option>
          <option value="8" 
        <c:if test='${mes==8}'>selected</c:if>
          >Agosto</option>
          <option value="9" 
        <c:if test='${mes==9}'>selected</c:if>
          >Septiembre</option>
          <option value="10" 
        <c:if test='${mes==10}'>selected</c:if>
          >Octubre</option>
          <option value="11" 
        <c:if test='${mes==11}'>selected</c:if>
          >Noviembre</option>
          <option value="12" 
        <c:if test='${mes==12}'>selected</c:if>
          >Diciembre</option>
          </select>
        </div>
    </strong><strong>
      <div class="styled-select"></div>
      </strong></td>
    <td>&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="30" ><strong>&nbsp; <strong>Estatus:</strong></strong>    
    <td colspan="3"><strong><span class="styled-select">
    <select class="comboBox" name="cbostatus" id="cbostatus" style="width:110px" onChange="buscar();">
      <option value="1" 
      
        <c:if test='${estatus==1}'>selected</c:if>
  >Terminado </option>
      <option value="2" 
      
        <c:if test='${estatus==2}'>selected</c:if>
  >No Terminado </option>
      <option value="3" 
     
        <c:if test='${estatus==3}'>selected</c:if>
  >Ambos </option>
    </select>
    </span></strong></td>
    <td colspan="2" rowspan="2"><table width="273" border="0" cellspacing="0" cellpadding="0">
          <tr>
          <td width="124" height="40"><button name="btnBuscar" id="btnBuscar" onClick="actualizarProyectos()" type="button" class="button blue middle"><span class="label" style="width:100px">Actualizar</span></button></td>
          <td width="149"><div class="buttons tiptip">
            <button name="cmdpdf" id="cmdpdf" title="Mostrar formato PDF" type="button" class="button red middle" onClick="getReporteAvances()"><span class="label" style="width:100px">Imprimir... </span></button>
          </div></td>
          </tr>
        
    </table></td>
  </tr>
  <tr >
    <th width="8%" height="30" >Unidad:
      <td colspan="3">
        <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
          <c:out value="${nombreUnidad}"/><input type="hidden" name="dependencia" id="dependencia" value="<c:out value='${idUnidad}'/>">
        </sec:authorize>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
          <select name="cbodependencia" class="comboBox" id="cbodependencia" style="width:455px;" onChange="buscar();">
            <option value="0">[Seleccione]</option>
            <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
              <option value='<c:out value="${item.ID}"/>' 
                                <c:if test='${item.ID==unidad}'> selected </c:if>>
              					<c:out value='${item.DEPENDENCIA}'/>
              </option>
            </c:forEach>
          </select>
        </sec:authorize>
      </td>
    </tr>
  <tr >
    <th height="15" >    
    <td>&nbsp;</td>
    <td colspan="2">&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="15" >    
    <td colspan="5">&nbsp;</td>
    </tr>
  </table>
<br />
<table width="95%" class="listas" align="center" id="listaRequisiciones" cellpadding="0" cellspacing="0">
 <thead>
  <tr>
    <th width="6%" height="22">Programa</th>
    <th width="24%">Descripción</th>
    <th width="8%">Fecha Inicio Real</th>
    <th width="8%">Fecha Termino Real</th>
    <th width="8%">Fecha Entrega</th>
    <th width="8%">Fecha Inicio Programada</th>
    <th width="8%">Fecha Termino Programada</th>
    <th width="8%">Avance Físico</th>
    <th width="8%">Avance Contralorìa</th>
  </tr>
   </thead>   
<tbody>  
<c:set var="cont" value="${0}" /> 
<c:forEach items="${avances}" var="item" varStatus="status"> 
<c:set var="cont" value="${cont+1}" />
  <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
  	<input type="hidden" id="hd<c:out value='${cont}'/>" value="<strong style='color:#0080FF'>ID_PROYECTO:</strong> <c:out value='${item.ID_PROYECTO}'/><br><strong style='color:#0080FF'>TIPO DE GASTO:</strong>&nbsp;<c:out value='${item.RECURSO}'/><br><strong style='color:#0080FF'>ACT. INSTITUCIONAL:</strong>&nbsp;<c:out value='${item.ACTIVIDAD_INST}'/><br><strong style='color:#0080FF'>LOCALIDAD:</strong>&nbsp;<c:out value='${item.LOCALIDAD}'/>" />
    <td height="22" align="center"><sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_SOLO_IMPRESION_GENERAL"><a id="link<c:out value='${cont}'/>" href="javascript:getAvances(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>')"></sec:authorize>[<c:out value='${item.ID_PROYECTO}'/>]&nbsp;<c:out value='${item.N_PROGRAMA}'/><sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_SOLO_IMPRESION_GENERAL"></a></sec:authorize></td>
    <td align="left"><c:out value='${item.PROG_PRESUP}'/><br><c:out value='${item.K_PROYECTO_T}'/>&nbsp;<c:out value='${item.DECRIPCION}'/></td>
    <td align="center"><c:out value='${item.FECHA_INICIO}'/></td>
    <td align="center"><c:out value='${item.FECHA_TERMINO}'/></td>
    <td align="center"><c:out value='${item.FECHA_ACTA}'/></td>
    <td align="center"><c:out value='${item.FEINIPER}'/></td>
    <td align="center"><c:out value='${item.FETERPER}'/></td>
    <td align="center"><c:out value='${item.AVANCE_FISICO}'/>%</td>
    <td align="center"><c:out value='${item.AVANCE_CONTRALORIA}'/>%</td>
  </tr>
  </c:forEach>
  <input type="hidden" id="hdcont" value="<c:out value='${cont}'/>" />
  <tr>
  	<td height="22" style="background:#FFF" colspan="9"><strong>Total de registros encontrados: <c:out value='${CONTADOR}'/></strong></td>
  </tr>
  </tbody>  
</table>
</form>
</body>
</html>
