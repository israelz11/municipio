<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Listado de Requisiciones</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="lst_req_captura.js"> </script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorListadoRequisicionesPedidos.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
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
<form  action="lst_req_captura.action" method="POST" id="forma" name="forma">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" name="claveRequisicion" id="claveRequisicion" value="" >
<table width="95%" align="center"><tr><td><h1>Pedidos - Listado de Requisiciones disponibles</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th height="19">&nbsp;</th>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr >
    <th  width="11%" height="25">Unidad :</th>
  <td>
  <c:if test='${1==1}'>
  <div class="styled-select">
  <select name="cbodependencia" id="cbodependencia" style="width:455px">
    <option value="0">[Todas las Unidades Administrativas]</option>
    <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
      <option value='<c:out value="${item.ID}"/>' 
                        <c:if test='${item.ID==cbodependencia}'> selected </c:if>>
      <c:out value='${item.DEPENDENCIA}'/>
      </option>
      </c:forEach>
   </select>
   </div>
  </c:if>
  <c:if test='${1==2}'> 
  <c:out value='${nombreUnidad}'/>
  <input type="hidden" name="dependencia" id="dependencia" value="<c:out value='${idUnidad}'/>">
  </c:if>
    </td>
    <td width="15%"><input type="radio" name="status" id="status"  value="1" <c:if test="${fn:contains(status,'1')}">checked</c:if>
    >&nbsp;Cerradas</td>
    <td width="15%"><input type="radio" name="status" id="status"  value="2" <c:if test="${fn:contains(status,'2')}">checked</c:if>>&nbsp;En Proceso</td>
    <td width="15%">&nbsp;</td>
  </tr>
  <tr >
    <th height="33" >Tipo de gasto:
    <td><strong>
    <div class="styled-select">
    <select name="cbotipogasto" id="cbotipogasto" style="width:455px;">
      <option value="0"> [Todos los tipos de gastos]
        <c:forEach items="${tipodeGasto}" var="item" varStatus="status">
        <option value='<c:out value="${item.ID}"/>'
          
        
        <c:if test='${item.ID==cbotipogasto}'> selected </c:if>
  >
  <c:out value='${item.RECURSO}'/>
        </option>
      </c:forEach>
    </select>
    </div>
    </strong></td>
    <td ><input name="status" type="radio" id="status"  value="5" <c:if test="${fn:contains(status,'5')}">checked</c:if>>&nbsp;Finiquitadas</td>
    <td ><input name="status" type="radio" id="status"  value="4" <c:if test="${fn:contains(status,'4')}">checked</c:if>>&nbsp;Canceladas</td>
    <td rowspan="3" ><button name="btnBuscar" id="btnBuscar" onClick="getRequisiciones()" type="button" class="button blue middle"><span class="label" style="width:100px">Buscar</span></button></td>
  </tr>
  <tr >
    <th height="25" >Por fecha de :
      <td>
        <input name="fechaInicial" type="text" id="fechaInicial" value="<c:out value='${fechaInicial}'/>" size="12"maxlength="10">
        &nbsp;<strong>Hasta</strong> &nbsp;
        <input name="fechaFinal" type="text" id="fechaFinal" value="<c:out value='${fechaFinal}'/>" size="12"  maxlength="10">
      </td>
    <td ></td>
    <td ></td>
    </tr>
  <tr >
    <th height="25" >Núm.  Requisición:    
    <td colspan="2"><table width="631" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="171"><input name="txtrequisicion" type="text" id="txtrequisicion" maxlength="50" style="width:150px" value="<c:out value='${txtrequisicion}'/>"></td>
        <td width="67"><strong>Programa:</strong></td>
        <td width="84"><input name="txtproyecto" type="text" id="txtproyecto" maxlength="4" style="width:70px" value="<c:out value='${txtproyecto}'/>"></td>
        <td width="57"><strong>Partida:</strong></td>
        <td width="252"><input name="txtpartida" type="text" id="txtpartida" maxlength="4" style="width:70px" onKeyPress="return keyNumbero(event);" value="<c:out value='${txtpartida}'/>"></td>
      </tr>
    </table></td>
    <td >&nbsp;</td>
    </tr>
  <tr >
    <th height="25" >    
    <td>&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  </table>
<br />
<table width="95%" class="listas" align="center" id="listaRequisiciones"  cellpadding="0" cellspacing="0">
 <thead>
  <tr>
    <th width="11%" height="20">Número</th>
    <th width="8%">Fecha</th>
    <th width="7%">Estado</th>
    <th width="7%">Tipo</th>
    <th width="44%">Unidad Administrativa</th>
    <th width="10%">Programa / Partida</th>
    <th width="8%">Importe</th>
    <th width="5%">Opciones</th>
  </tr>
   </thead>   
<tbody>
<c:set var="cont" value="${0}" /> 
<c:forEach items="${requisicionesUnidad}" var="item" varStatus="status"> 
  <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
    <td height="20" align="center"><c:out value='${item.NUM_REQ}'/></td>
    <td align="center"><c:out value='${item.FECHA}'/></td>
    <td align="center"><c:out value='${item.DESCRIPCION_ESTATUS}'/></td>
    <td align="center"><c:out value='${item.TIPO_REQ}'/></td>
    <td align="left"><c:out value='${item.UNIDAD_ADM}'/></td>
    <td align="center"><c:if test='${item.N_PROGRAMA!=NULL}'><c:out value='${item.N_PROGRAMA}'/>&nbsp;/&nbsp;<c:out value='${item.CLV_PARTID}'/></c:if></td>
    <td align="right"><fmt:formatNumber value="${item.IMPORTE}"  pattern="#,###,###,##0.00" /> </td>
    <td align="center">
    <img style="cursor:pointer" src="../../imagenes/pdf.gif" title="Ver Requisicion en PDF" border="0" width="14" height="16" onClick="getRequisicion(<c:out value='${item.CVE_REQ}'/>)">
    <c:if test='${((item.TIPO==1 || item.TIPO==7 ) && (item.STATUS == 1 ||  item.STATUS == 2 )) && item.IMPORTE > 0}'><img src="../../imagenes/report_go.png" title="Crear Pedido" style="cursor:pointer" onClick="crearPedido(<c:out value='${item.CVE_REQ}'/>)"></c:if>
    <c:if test='${((item.STATUS == 3 || item.STATUS == 4 || item.STATUS == 5 )) || item.IMPORTE <= 0}'><img src="../../imagenes/report_go2.png" title="No se puede crear el Pedido" ></c:if></td>
  </tr>
  <c:set var="cont" value="${cont+1}"/>
  </c:forEach>
  </tbody>  
</table>
</form>
</body>
</html>
