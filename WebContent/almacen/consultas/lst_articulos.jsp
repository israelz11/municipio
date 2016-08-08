<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Listado de Entradas de Documentos</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="lst_articulos.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorListadoArticulosInventarioRemoto.js"> </script>

<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/util.js"> </script>
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
<form  action="../reportes/almacen.action" method="get" id="frmreporte" name="frmreporte">
  <table width="95%" align="center">
  <tr><td><h1>Consultas - Listado de Artículos del Almac&eacute;n</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th height="25">&nbsp;</th>
    <td colspan="3">
    	<input name="ID_PROVEEDOR" type="hidden" id="ID_PROVEEDOR" value="<c:out value='${id_proveedor}'/>" />
        <input name="ID_UNIDAD_MEDIDA" type="hidden" id="ID_UNIDAD_MEDIDA" value="<c:out value='${id_unidad_medida}'/>"/>
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr >
    <th height="30">Unidad Administrativa:</th>
    <td colspan="3">
    <sec:authorize ifNotGranted="ROLE_Almacen_PRIVILEGIOS_VER_UNIDADES_ADMINISTRATIVAS">
      	<c:out value="${nombreUnidad}"/>
        <input type="hidden" id="cbodependencia" name="cbodependencia" value="<c:out value='${cbodependencia}'/>">
    </sec:authorize>
    <sec:authorize ifAllGranted="ROLE_Almacen_PRIVILEGIOS_VER_UNIDADES_ADMINISTRATIVAS">
    	<div class="styled-select">
            <select name="cbodependencia" id="cbodependencia" style="width:445px">
                          <option value="0">[Seleccione]</option>
                          <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
                          <option value='<c:out value="${item.ID}"/>' 
                            <c:if test='${item.ID==cbodependencia}'> selected </c:if>>
                            <c:out value='${item.DEPENDENCIA}'/>
                            </option>
                          </c:forEach>
             </select>
          </div>
     </sec:authorize>
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr >
    <th  width="161" height="30">      *Almacen:</th>
    <td colspan="3">
    <div class="styled-select">
        <select name="cboalmacen" class="comboBox" id="cboalmacen" style="width:445px;">
          <option value="0">[Seleccione]</option>
          <c:forEach items="${almacenes}" var="item" varStatus="status"> <option value='<c:out value="${item.ID_ALMACEN}"/>' 
            <c:if test='${item.ID_ALMACEN==id_almacen}'> selected </c:if>>
            <c:out value='${item.DESCRIPCION}'/>
            </option>
          </c:forEach>
        </select>
      </div>
      </td>
    <td width="159">&nbsp;</td>
    </tr>
  <tr >
    <th height="30" >Familia de articulos: 
      <td colspan="3">
      <div class="styled-select">
      <select name="cbofamilia" id="cbofamilia" class="comboBox" style="width:445px">
        <option value="0">[Seleccione]</option>
        <c:forEach items="${familiasArticulos}" var="item" varStatus="status"> <option value='<c:out value="${item.ID_FAMILIA}"/>' 
          
          <c:if test='${item.ID_FAMILIA==id_familia}'> selected </c:if>
          >
          <c:out value='${item.DESCRIPCION}'/>
          </option>
        </c:forEach>
      </select>
      </div></td>
    <td >&nbsp;</td>
    </tr>
  <tr >
    <th height="30" >Proveedor:
    <td colspan="3"><input name="txtbeneficiario" type="text" class="input"  id="txtbeneficiario"  value="<c:out value='${proveedor}'/>" style="width:445px"></td>
    <td ><div class="buttons tiptip">
                <button name="cmdbuscar" id="cmdbuscar" type="button" class="button red middle"><span class="label" style="width:100px">Buscar</span></button>
              </div></td>
    </tr>
  <tr >
    <th height="30" >Descripcion del articulo:
    <td colspan="3"><input name="txtdescripcion" type="text" class="input"  id="txtdescripcion"  value="<c:out value='${descripcion}'/>" style="width:445px"></td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="30" >Unidad de medida:  
      <td colspan="3"><input name="txtunidadmedida" type="text" class="input" id="txtunidadmedida" maxlength="7" value="<c:out value='${unidad_medida}'/>"/></td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="30" >Folio de articulo:
      <td width="123"><input name="txtfolio" type="text" class="input"  id="txtfolio"  value="<c:out value='${folio}'/>" ></td>
    <td width="118" align="right">&nbsp;</td>
    <td width="532">&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <td height="25" >&nbsp; </td>
    <td height="25" colspan="4" >&nbsp;</td>
    </tr>  
</table>
<br />
<table width="95%" class="listas" align="center" id="listaDocumentos" cellpadding="0" cellspacing="0" border="0">
 <thead>
  <tr>
    <th width="3%" height="20">&nbsp;</th>
    <th width="7%">Folio ID.</th>
    <th width="7%">Cantidad</th>
    <th width="9%">Unidad Medida</th>
    <th width="49%">Descripción del artículo / Familia</th>
    <th width="6%">Status Alarma</th>
    <th width="8%">Precio Unitario</th>
    <th width="5%">Status</th>
    <th width="6%">Opciones</th>
  </tr>
   </thead>   
<tbody>  
<c:forEach items="${listadoArticulos}" var="item" varStatus="status"> 
  <tr>
    <td align="center">
    <c:if test='${item.STATUS==true}'>
    <input type="checkbox" id="chkarticulos" name="chkarticulos" value="<c:out value='${item.ID_INVENTARIO}'/>"/>
    </c:if>
    </td>
    <td align="center"><c:out value='${item.FOLIO}'/></td>
    <td align="center"><fmt:formatNumber value="${item.CANTIDAD}"  pattern="###.00" /></td>
    <td align="center"><c:out value='${item.UNIDAD_MEDIDA}'/></td>
    <td align="left"><a href="javascript:MostrarDetalles(<c:out value='${item.ID_INVENTARIO}'/>)"><c:out value='${item.DESCRIPCION}'/></a> <strong>/</strong> <c:out value='${item.FAMILIA}'/></td>
    <td align="center"><c:out value='${item.ALARMA_DESC}'/></td>
    <td align="right"><fmt:formatNumber value="${item.PRECIO}"  pattern="#,###,###,##0.00" />&nbsp;</td>
    <td align="center"><c:out value='${item.STATUS_DESC}'/></td>
    <td align="center"><img src="../../imagenes/page_white_edit.png" title="Editar / Abrir" style="cursor:pointer" onClick="editarArticulo('<c:out value='${item.ID_INVENTARIO}'/>')">
    </td>
  </tr>
  </c:forEach>
    <tr>
    <td colspan="9" align="left"></td>
    </tr>
  </tbody>  
</table>
<table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td><table width="100" style="border:none">
      <tr>
         
          <td width="132" align="center"><div class="buttons tiptip">
          	<c:if test='${cbodependencia!=0}'>
            	<button name="cmdreporte" id="cmdreporte" type="button" onClick="regresarSol" class="button red middle"><span class="label" style="width:100px">Imprimir PDF</span></button>
            </c:if>
          </div></td>
          </tr>
    </table></td>
  </tr>
</table>

</form>
</body>
</html>
