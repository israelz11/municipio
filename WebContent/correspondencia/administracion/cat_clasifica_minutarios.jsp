<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorAdministrarClasificaMinutariosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script> 
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="cat_clasifica_minutarios.js"></script>

<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<body >
<form name="forma" id="forma" method="get" action="cat_clasifica_minutarios.action">
<br />
  <table width="95%" align="center"><tr><td><h1>Administración - Catálogo de Clasificación de Minutarios</h1></td></tr></table>
  <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <td width="14%" height="19">&nbsp;</td>
      <td width="86%"><input type="hidden" id="ID_CAT_CLASIFICACION_MINUTARIO" name="ID_CAT_CLASIFICACION_MINUTARIO" value="0"></td>      
    </tr>
    <tr>
      <th height="30">*Unidad Administrativa:</th>
      <td>
      <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      		<c:out value="${nombreUnidad}"/><input type="hidden" name="cbodependencia" id="cbodependencia" value='<c:out value="${idUnidad}"/>' />
      </sec:authorize>
      <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
          <span class="styled-select">
            <select name="cbodependencia" class="comboBox" id="cbodependencia" style="width:450px">
              <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> <option value='<c:out value="${item.ID}"/>' 
                
                <c:if test="${item.ID==idUnidad}"> selected </c:if>
                >
                <c:out value="${item.DEPENDENCIA}"/>
                </option>
              </c:forEach>
            </select>
          </span>
      </sec:authorize>
    </td>
    </tr>
    <tr>
      <th height="30">*Descripción:</th>
      <td><input name="txtclasifica" type="text" class="input" id="txtclasifica" value="" size="70" maxlength="150" style="width:450px"/></td>
    </tr>
    <tr>
      <th height="30">&nbsp;Status</th>
      <td><input type="checkbox" name="chkstatus" id="chkstatus"></td>
    </tr>
    <tr>
      <td height="30">&nbsp;</td>
      <td rowspan="2"><table width="252" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="124"><div class="buttons tiptip">
            <button name="cmdnuevo" id="cmdnuevo" type="button" class="button red middle"><span class="label" style="width:100px">Nuevo</span></button>
          </div></td>
          <td width="128"><div class="buttons tiptip">
            <button name="cmdguardar" id="cmdguardar" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button>
          </div></td>
        </tr>
      </table></td>
    </tr>
    <tr>
      <td height="30"><p>&nbsp;</p></td>
    </tr>
  </table>
  <br />
  <table width="95%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas"  id="detalles_Subdirecciones" >
    <thead>
      <tr >
        <th width="3%" height="20"><img src="../../imagenes/cross.png" width="16" height="16" onClick="borrarClasificaMinutario()" style='cursor: pointer;' alt='Eliminar'></th>
        <th width="74%"  align="left" >Descripción</th>
        <th width="15%">Status</th>
        <th width="5%">Opc.</th>
      </tr>
      <c:set var="cont" value="${0}" /> 
  <c:forEach items="${detalles}" var="item" varStatus="itemStatus">
          <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
            <td height="20" align="center">
           <input type="checkbox" id="chkClasificaMinutario" name="chkClasificaMinutario" value="<c:out value='${item.ID_CAT_CLASIFICACION_MINUTARIO}'/>"></td>
            <td><c:out value="${item.DESCRIPCION}"/></td>
            <td align="center"><c:if test='${item.STATUS==1}'>ACTIVO</c:if><c:if test='${item.STATUS==0}'>INACTIVO</c:if></td>
             <td align="center"><img src="../../imagenes/page_white_edit.png" title="Editar" style="cursor:pointer" onClick="editarClasificaMinutario(<c:out value='${item.ID_CAT_CLASIFICACION_MINUTARIO}'/>, <c:out value='${item.ID_DEPENDENCIA}'/>, '<c:out value="${item.DESCRIPCION}"/>', <c:out value='${item.STATUS}'/>)">&nbsp;</td>
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
