<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/ControladorListadoDevolucionPresupuestalRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="lst_devolucion_presupuestal.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Presupuesto - Listado de Devolucion Presupuestal</title>
</head>

<body>
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>" />
<form  action="lst_devolucion_presupuestal.action" id="forma" name="forma" method="post">
<table width="95%" align="center">
  <tr>
    <td><h1>Presupuesto - Listado de Devolución Presupuestal</h1><input type="hidden" name="idDevolucion" id="idDevolucion" value="0" /></td>
  </tr>
</table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th width="15%" height="13">&nbsp;</th>
    <td width="40%">&nbsp;</td>
    <td width="15%">&nbsp;</td>
    <td width="15%">&nbsp;</td>
    <td width="15%">&nbsp;</td>
  </tr>
  <tr >
    <th height="30" >Unidad Administrativa: </th>
    <td><sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      <c:out value="${nombreUnidad}"/><input type="hidden" name="unidad" id="unidad" value='<c:out value="${unidad}"/>' />
      </sec:authorize>
       <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
        <select name="unidad" class="comboBox" id="unidad" style="width:450px">
        	<option value="0" selected>[Todas las Unidades Administrativas]</option>
            <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">                  
              <option value="<c:out value="${item.ID}"/>" <c:if test="${item.ID==unidad}"> selected </c:if> ><c:out value="${item.DEPENDENCIA}"/></option>
            </c:forEach>
          </select>
       </sec:authorize></td>
    <td colspan="3" >&nbsp;</td>
  </tr>
  <tr >
    <th height="30" >Concepto o Descripción: </th>
    <td><input type="text" id="txtconcepto" name="txtconcepto" class="input" style="width:448px" value="<c:out value='${txtconcepto}'/>"/></td>
    <td colspan="3" ><input  name="btnBuscar" type="button" class="botones" id="btnBuscar"   value="Buscar" style="width:150px" /></td>
  </tr>
  <tr >
    <th height="30" >Número:</th>
    <td><input type="text" id="txtnumero" name="txtnumero" class="input" style="width:220px" value="<c:out value='${txtnumero}'/>"/></td>
    <td colspan="3" ><input  name="cmdnuevo" type="button" class="botones" id="cmdnuevo"   value="Nuevo..." style="width:150px" /></td>
  </tr>
  <tr >
    <th height="25" >Estatus: </th>
    <td><input value="0" name="estatus" type="checkbox" id="estatus" <c:if test="${fn:contains(estatus,'0')}">checked</c:if>/>Edicion <input value="1" name="estatus" type="checkbox" id="estatus" <c:if test="${fn:contains(estatus,'1')}">checked</c:if>/>Cerrado <input value="2" name="estatus" type="checkbox" id="estatus" <c:if test="${fn:contains(estatus,'2')}">checked</c:if>/>Aplicado <input value="3" name="estatus" type="checkbox" id="estatus" <c:if test="${fn:contains(estatus,'3')}">checked</c:if>/>Cancelado</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="25" >&nbsp;</th>
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
      <th height="20" width="3%">&nbsp;</th>
      <th width="9%">Número</th>
      <th width="20%">Unidad Administrativa</th>
      <th width="31%">Concepto</th>
      <th width="10%">Fecha</th>
      <th width="10%">Status</th>
      <th width="11%">Monto</th>
      <th width="6%">Opciones</th>
    </tr>
  </thead>
  <tbody>


  <c:out value="${cont}"/>
  <c:forEach items="${devoluciones}" var="item" varStatus="status">
  	<tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')"/>
    <td align="center"><input type="checkbox" id="chkdevolucion" name="chkdevolucion" value="<c:out value='${item.ID_DEVOLUCION}'/>"/></td>
    <td align="center"><c:out value='${item.NUM_DEVOLUCION}'/></td>
    <td align="left"><c:out value='${item.DEPENDENCIA}'/></td>
    <td align="left"><c:out value='${item.CONCEPTO}'/></td>
    <td align="center"><c:out value='${item.FECHA_DESC}'/></td>
    <td align="center"><c:out value='${item.STATUS_DESC}'/></td>
    <td align="right">$<fmt:formatNumber value="${item.IMPORTE}"  pattern="#,###,###,##0.00" />&nbsp;</td>
    <td align="center">&nbsp;<img style="cursor:pointer" src="../../imagenes/pdf1.png" alt="Ver Documento" border="0" width="14" height="16" onclick="getReporteDevolucion(<c:out value='${item.ID_DEVOLUCION}'/>)" />
      	<c:if test='${item.STATUS==0}'><img src="../../imagenes/page_white_edit.png" alt="" style="cursor:pointer" title="Editar / Abrir" onclick="nuevoEditarDevolucion(<c:out value='${item.ID_DEVOLUCION}'/>)" /></c:if>
        <c:if test='${item.STATUS>0}'><img src="../../imagenes/page_gray_edit.png" alt="" style="cursor:pointer" /></c:if>
     
        <c:if test='${item.STATUS==0||item.STATUS==1}'> <img style="cursor:pointer" src="../../imagenes/cross.png" title="Deshabilitar" border="0" width="16" height="16" onclick="cancelarDevoluciones(<c:out value='${item.ID_DEVOLUCION}'/>)" /></c:if>
        <c:if test='${item.STATUS>1}'> <img style="cursor:pointer" src="../../imagenes/cross2.png" title="Deshabilitar" border="0" width="16" height="16" /></c:if></td>
    
  </tr>
  <c:set var="cont" value="${cont+1}"/>
 </c:forEach>
 <tr>
      <td colspan="9" align="left" height="40" style="background-color:#FFF">
        <input type="button" value="Apertura Multiple" id="cmdaperturar" class="botones" style="width:150px"/>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APLICAR_DEVOLUCION_PRESUPUESTAL">
        <input type="button" value="Aplicar devoluciones" id="cmdaplicar" class="botones" onclick="aplicarDevoluciones()" style="width:150px"/>
        <input type="button" value="Desaplicar devoluciones" id="cmddesaplicar" class="botones" onclick="aplicarDevoluciones()" style="width:150px"/></td>
        </sec:authorize>
        </tr> 
  <tr>
          <td colspan="10" height="25" style="background-color:#FFF" align="left"><strong>Total de registros encontrados: <c:out value='${CONTADOR}'/></strong></td>
        </tr> 
          </tbody>
</table>
</form>
</body>
</html>