<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Entradas - Configuracion de Articulos en Almacen</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/interface/controladorConfiguracionArticulosAlmacenRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="configura_entradaArticulos.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>

<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
<!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->
<body>
<form name="forma" method="get" action="" onSubmit=" return false" >
<br />
  <table width="90%" align="center"><tr><td><h1>Entradas - Configuración de Artículos en Almacén</h1></td></tr></table>
  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <td height="30">&nbsp;</td>
      <td colspan="2"><input type="hidden" name="ID_INVENTARIO" id="ID_INVENTARIO" value='<c:out value="${inventario.ID_INVENTARIO}"/>'/>
        
        <input type="hidden" name="ID_PROVEEDOR" id="ID_PROVEEDOR" value='<c:out value="${inventario.ID_PROVEEDOR}"/>'/>
        <input type="hidden" name="ID_UNIDAD_MEDIDA" id="ID_UNIDAD_MEDIDA" value='<c:out value="${inventario.ID_UNIDAD_MEDIDA}"/>'/>
      <strong>Nota:</strong> Los campos marcados con (*) son requeridos para guardar la información.</td>      
    </tr>
    <tr>
      <th align="right" height="30">Folio:&nbsp;</th>
      <td><strong style="color:#900"><c:out value='${inventario.FOLIO}'/></strong></td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th align="right" height="30">Unidad Administrativa:</th>
      <td><c:out value='${inventario.DEPENDENCIA}'/></td>
      <td></td>
    </tr>
    <tr>
      <th align="right" height="30">*Almac&eacute;n:&nbsp;</th>
      <td width="56%"><c:out value='${inventario.ALMACEN}'/></td>
      <td width="29%">&nbsp;</td>
    </tr>
    <tr>
      <th align="right" width="15%" height="30">Familia de artículos:&nbsp;</th>
      <td colspan="2"><select name="cbofamilia" id="cbofamilia" class="comboBox" style="width:445px">
        <option value="0">[Seleccione]</option>
        <c:forEach items="${familiasArticulos}" var="item" varStatus="status"> <option value='<c:out value="${item.ID_FAMILIA}"/>' 
          <c:if test='${item.ID_FAMILIA==inventario.id_familia}'> selected </c:if>
          >
          <c:out value='${item.DESCRIPCION}'/>
          </option>
        </c:forEach>
      </select></td>
    </tr>
    <tr>
      <th align="right" height="30">*Proveedor:&nbsp;</th>
      <td colspan="2"><input name="txtbeneficiario" type="text" class="input"  id="txtbeneficiario"  value="<c:out value='${inventario.PROVEEDOR}'/>" style="width:445px"></td>
    </tr>
    <tr>
      <th align="right" height="30">Descripción del artículo:&nbsp;</th>
      <td colspan="2"><c:out value='${inventario.ARTICULO}'/></td>
    </tr>
    <tr>
      <th align="right" height="30">*Unidad Medida:&nbsp;</th>
      <td colspan="2"><input name="txtunidadmedida" type="text" class="input" id="txtunidadmedida" maxlength="7" value="<c:out value='${inventario.UNIDAD_MEDIDA}'/>"/></td>
    </tr>
    <tr>
      <th align="right" height="30">*Precio:&nbsp;</th>
      <td colspan="2"><input name="txtprecio" type="text" class="input" id="txtprecio" maxlength="7" value="<fmt:formatNumber value='${inventario.PRECIO}'  pattern='#########.00'/>"/></td>
    </tr>
    <tr>
      <th align="right" height="30">Cantidad en existencia:&nbsp;</th>
      <td colspan="2"><fmt:formatNumber value="${inventario.CANTIDAD}"  pattern="###.00" /></td>
    </tr>
    <tr>
      <th align="right" height="30">Existencia minima:&nbsp;</th>
      <td colspan="2"><input name="txtexistencia_minima" type="text" class="input" id="txtexistencia_minima" maxlength="7" value="<c:out value='${inventario.EXISTENCIA_MINIMA}'/>"/></td>
    </tr>
    <tr>
      <th align="right" height="30">Alarma:&nbsp;</th>
      <td colspan="2"><input type="checkbox" id="chkalarma" name="chkalarma" <c:if test='${inventario.ALARMA==true}'>checked</c:if>/></td>
    </tr>
    <tr>
      <th align="right" height="30">*Status:&nbsp;</th>
      <td colspan="2"><input type="checkbox" id="chkstatus" name="chkstatus" <c:if test='${inventario.STATUS==true}'>checked</c:if>/></td>
    </tr>
    <tr>
      <td height="30"><p>&nbsp;</p></td>
      <td colspan="2">
      <table width="210" style="border:none">
        <tr>
          <td width="132" align="center"><div class="buttons tiptip">
            <button name="cmdregresar" id="cmdregresar" type="button" class="button red middle"><span class="label" style="width:100px">Regresar</span></button>
          </div></td>
          <td width="132" align="center"><div class="buttons tiptip">
            <button name="cmdguardar" id="cmdguardar" type="button" onClick="regresarSol" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button>
          </div></td>
        </tr>
      </table></td>
    </tr>
  </table>
  <br />
</form>
</body>
</html>
