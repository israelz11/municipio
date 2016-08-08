<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Listado de Pedidos</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="lista_vales.js"> </script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorListadoValesRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<body >
<form  action="lista_vales_finanzas.action" method="POST" id="forma" name="forma">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" name="cve_val" id="cve_val" >
<table width="95%" align="center"><tr><td><h1> Vales - Listado de Vales Finanzas</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th height="15">&nbsp;</th>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr >
    <th  width="10%" height="25">Unidad:</th>
  <td width="50%">
          <select name="cbodependencia"  id="cbodependencia" style="width:445px" class="comboBox" >
    <option value="0">[Todas la Unidades Administrativas]</option>
    <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
      <option value='<c:out value="${item.ID}"/>' <c:if test='${item.ID==idUnidad}'> selected </c:if>><c:out value='${item.DEPENDENCIA}'/></option>
      </c:forEach></select>
    </td>
    <td width="40%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><input type="radio" name="status" id="status"  value="1" <c:if test="${fn:contains(status,'1')}" >checked</c:if>
      >
      Pendientes Pago</td>
        <td><input name="status" type="radio" id="status"  value="3" <c:if test="${fn:contains(status,'3')}">checked</c:if>
      >Aprobado</td>
        <td><input type="radio" name="status" id="status"  value="4" <c:if test="${fn:contains(status,'4')}" >checked</c:if>
      >
      Pagado</td>
      </tr>
    </table>
      </td>
    </tr>
  <tr >
    <th height="25" >Tipo de gasto:
    <td colspan="2"><strong>
    <select name="cbotipogasto" id="cbotipogasto" style="width:445px" class="comboBox">
      <option value="0">[Todos los tipos de gastos]</option>
      <c:forEach items="${tipodeGasto}" var="item" varStatus="status">
        <option value='<c:out value="${item.ID}"/>' >
          <c:if test='${item.ID==tipo_gto}'> selected </c:if>
          >
          <c:out value='${item.RECURSO}'/>
          </option>
      </c:forEach>
    </select>
    </strong></td>
    </tr>
  <tr >
    <th height="25" >Por fecha de:  
    <td><strong>
    <input name="fechaInicial" type="text" id="fechaInicial" value="${fechaInicial}" style="width:100px" maxlength="10">
&nbsp;Hasta &nbsp;
<input name="fechaFinal" type="text" id="fechaFinal" value="${fechaFinal}" style="width:100px"  maxlength="10">
    </strong></td>
    <td ><input  name="btnBuscar" type="button" class="botones" id="btnBuscar"   value="Buscar" onClick="getVale()" style="width:150px"></td>
    </tr>
  <tr >
    <th height="14" >    
    <td>&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODOS_LOS_DOCUMENTOS_DE_LA_UNIDAD">       </sec:authorize>  
</table>
<br />
<table width="95%" class="listas" align="center" id="listaRequisiciones" cellpadding="0" cellspacing="0">
 <thead>
  <tr>
    <th width="3%" height="20">&nbsp;</th>
    <th width="6%">Número</th>
    <th width="5%">Fecha</th>    
    <th width="32%">Beneficiario</th>
    <th width="13%">Tipo</th>
    <th>Tipo de Gasto</th>
    <th width="11%">Importe</th>    
    <th width="5%">Opciones</th>
  </tr>
   </thead>   
<tbody>  
<c:set var="cont" value="${0}" />
<c:forEach items="${vales}" var="item" varStatus="status"> 
 <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
    <td align="center"><input type="checkbox" id="claves" name="claves" value="<c:out value='${item.CVE_VALE}'/>"/></td>
    <td align="center"><c:out value='${item.NUM_VALE}'/></td>
    <td align="center"><c:out value='${item.FECHA}'/></td>
    <td><c:out value='${item.NCOMERCIA}'/></td>
    <td align="center"><c:out value='${item.DESCRIPCION_ESTATUS}'/></td>
    <td align="center"><c:out value='${item.RECURSO}'/></td>
    <td align="right"><fmt:formatNumber value="${item.TOTAL}"  pattern="#,###,###,##0.00" /> </td>
    <td align="center">
    <img style="cursor:pointer" src="../../imagenes/pdf.gif" alt="Ver Documento" border="0" width="14" height="16" onClick="getReporteVale(<c:out value='${item.CVE_VALE}'/>)">&nbsp;
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_BITACORA_EN_VALES"><img src="../../imagenes/report_user.png" alt="" width="16" height="16" border="0" style="cursor:pointer" title="Ver bitacora de Vales" onClick="bitacoraDocumento(<c:out value='${item.CVE_VALE}'/>, 'VAL')"> </sec:authorize>
    </td>
  </tr>
  <c:set var="cont" value="${cont+1}" />
  </c:forEach> 
 <c:if test="${fn:length(vales) > 0}">  
  <tr>
    <td colspan="8" align="left" height="30" style="background:#FFF"><c:if test="${fn:contains(status,'1')}" ><input type="button" value="Aplicar pago" id="cmdaperturar" class="botones" onClick="aplicarVale()" style="width:150px"/> <input type="button" value="Rechazar" id="cmdeliminar" class="botones"  onClick="rechazarVale()" style="width:150px"/></c:if> 
    <c:if test="${fn:contains(status,'4')}" >
      <input type="button" value="Desaplicar pago" id="cmdaperturar2" class="botones" onClick="desAplicarVale()" style="width:150px"/>
      </c:if> </td>

    </tr>
    
  </c:if>  
  </tbody>  
</table>
</form>
</body>
</html>
