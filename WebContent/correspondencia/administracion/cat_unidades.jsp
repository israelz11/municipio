<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catálogo de Unidades Administrativas/Otros</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorAdministrarUnidadesRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="cat_unidades.js"></script>
<body >
<form name="forma" method="get" action="" onSubmit=" return false" >
<br />
  <table width="95%" align="center"><tr><td><h1>Administración - Catálogo de Unidades Administrativas/Otros</h1></td></tr></table>
  <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <td height="30">&nbsp;</td>
      <td><input type="hidden" name="ID_UNIDAD" id="ID_UNIDAD" value=''/></td>      
    </tr>
    <tr>
      <th width="13%" height="30">*Nombre de la Unidad:</th>
      <td width="87%"><input name="txtunidad" type="text" class="input" id="txtunidad" value="" size="70" maxlength="150" style="width:350px"/></td>
    </tr>
    <tr>
      <th height="30">*Responsable:</th>
      <td><input name="txtresponsable" type="text" class="input" id="txtresponsable" value="" size="70" maxlength="150" style="width:350px"/></td>
    </tr>
    <tr>
      <th height="30">&nbsp;Alias de Unidad:</th>
      <td><input name="txtalias" type="text" class="input" id="txtalias" value="" size="70" maxlength="150" style="width:100px"/> 
        Por ejemplo las iniciales de la unidad.</td>
    </tr>
    <tr>
      <th height="30">&nbsp;Status</th>
      <td><input type="checkbox" name="chkstatus" id="chkstatus"></td>
    </tr>
    <tr>
      <td height="30">&nbsp;</td>
      <td rowspan="2"><input type="button"  class="botones"  name="cmdguardar" id="cmdguardar" value="Guardar" style="width:100px"/>
      <input type="button"  class="botones"  name="cmdnuevo" id="cmdnuevo" value="Nuevo"  style="width:100px"/></td>
    </tr>
    <tr>
      <td height="30"><p>&nbsp;</p></td>
    </tr>
  </table>
  <br />
  <table width="95%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas"  id="detalles_Subdirecciones" >
    <thead>
      <tr >
        <th width="3%" height="20" >
        <sec:authorize ifAllGranted="ROLE_Correspondencia_PRIVILEGIOS_ADMINISTRACION_UNIDADES_ELIMINAR">
        	<img src="../../imagenes/cross.png" width="16" height="16" onClick="borrarUnidadesAdm()" style='cursor: pointer;' alt='Eliminar Unidades'>
        </sec:authorize>
        <sec:authorize ifNotGranted="ROLE_Correspondencia_PRIVILEGIOS_ADMINISTRACION_UNIDADES_ELIMINAR">
            <img src="../../imagenes/cross2.png" width="16" height="16" onClick="" style='cursor: pointer;'>
        </sec:authorize>
        </th>
        <th width="37%"  align="left" >Unidad Administrativa</th>
        <th width="31%"  align="left" ><div align="left">Responsable</div></th>
        <th width="16%"  align="left" >Alias</th>
        <th width="8%">Status</th>
        <th width="5%">Opc.</th>
      </tr>
      <c:set var="cont" value="${0}" /> 
      <c:forEach items="${detalles}" var="item" varStatus="itemStatus">
          <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
            <td height="20" align="center"><fmt:formatNumber value="${item.CANTIDAD}"  pattern="#,###,###,##0.00" />
            <c:if test='${item.EDITABLE==1}'><input type="checkbox" id="chkunidad" name="chkunidad" value="<c:out value='${item.ID_UNIDAD}'/>"></c:if></td>
            <td><c:out value="${item.DESCRIPCION}"/></td>
            <td><c:out value="${item.RESPONSABLE}"/></td>
            <td align="center"><c:out value="${item.PREFIJO}"/></td>
            <td align="center"><c:if test='${item.STATUS==1}'>ACTIVO</c:if><c:if test='${item.STATUS==0}'>INACTIVO</c:if></td>
             <td align="center"><img src="../../imagenes/page_white_edit.png" title="Crear Pedido" style="cursor:pointer" onClick="editarUnidadAdm(<c:out value='${item.ID_UNIDAD}'/>, <c:out value='${item.EDITABLE}'/>)">&nbsp;</td>
          </tr>
          <c:set var="cont" value="${cont+1}"/>
       </c:forEach>
    </thead>
    <tbody>
    </tbody>
  </table>
</form>
</body>
</html>
