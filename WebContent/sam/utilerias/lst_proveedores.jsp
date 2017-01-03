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
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/interface/ControladorListadoBeneficiariosRemoto.js"> </script>
<script type="text/javascript" src="lst_proveedores.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Administracion - Listado de Beneficiarios y Funcionarios</title>
</head>

<body>
<input type="hidden" name="ejercicio" id="ejercicio" value="&lt;c:out value='${ejercicio}'/&gt;" />
<input type="hidden" name="cve_contrato" id="cve_contrato" />
<form  action="lst_proveedores.action" id="forma" name="forma" method="post">
<table width="95%" align="center">
  <tr>
    <td><h1> Administración - Listado de beneficiarios y funcionarios</h1></td>
  </tr>
</table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th width="11%" height="13">&nbsp;</th>
    <td width="34%">&nbsp;</td>
    <td width="25%">&nbsp;</td>
    <td width="15%">&nbsp;</td>
    <td width="15%">&nbsp;</td>
  </tr>
  <tr >
    <th height="30" >Beneficiario: </th>
    <td><input type="text" id="txtprestadorservicio" name="txtprestadorservicio" class="input" style="width:350px" value="<c:out value='${txtprestadorservicio}'/>"/></td>
    <td colspan="3" ><input  name="btnBuscar" type="button" class="botones" id="btnBuscar"   value="Buscar" style="width:150px" /></td>
  </tr>
  <tr >
    <th height="30" >RFC:</th>
    <td><input type="text" id="txtrfc" name="txtrfc" class="input" style="width:220px" value="<c:out value='${txtrfc}'/>"/></td>
    <td colspan="3" ><input  name="cmdnuevo" type="button" class="botones" id="cmdnuevo"   value="Nuevo..." style="width:150px" /></td>
  </tr>
  <tr >
    <th height="25" >Filtrar por:</th>
    <td><select name="cbotipo" class="comboBox" id="cbotipo" style="width:150px" >
      <option value="0" <c:if test='${cbotipo==0}'> selected </c:if>>[Ambos]</option>
      <option value="1" <c:if test='${cbotipo==1}'> selected </c:if>>Beneficiarios</option>
      <option value="2" <c:if test='${cbotipo==2}'> selected </c:if>>Funcionarios</option>
    </select></td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="25" >Vigencia:</th>
    <td><input name="vigencia" type="checkbox" id="vigencia" <c:if test="${fn:contains(vigencia,'1')}">checked</c:if>/>
Activos</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
</table>
<br />
<table width="95%" class="listas" align="center" id="listaRequisiciones"  cellpadding="0" cellspacing="0">
  <thead>
    <tr>
      <th height="20" width="29%">Beneficiario</th>
      <th width="12%">RFC</th>
      <th width="12%">Telefono</th>
      <th width="28%">Domicilio Fiscal</th>
      <th width="9%">Status</th>
      <th width="6%">Opciones</th>
    </tr>
  </thead>
  <tbody>

  </tbody>
  <c:out value="${cont}"/>
  <c:forEach items="${beneficiarios}" var="item" varStatus="status">
  <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')"/>
    <td align="left">&nbsp;<c:out value='${item.NCOMERCIA}'/></td>
    <td align="center"><c:out value='${item.RFC}'/></td>
    <td align="center"><c:out value='${item.TELEFONOS}'/></td>
    <td align="left"><c:out value='${item.DOMIFISCAL}'/></td>
    <td align="center"><c:if test='${item.STATUS==1}'>Activo</c:if><c:if test='${item.STATUS==0}'>Inactivo</c:if></td>
    <td align="center">&nbsp;<img style="cursor:pointer" src="../../imagenes/pdf2.png" alt="Ver Documento" border="0" width="14" height="16" onclick="getReporteBenefi(<c:out value='${item.ID_BENEFICIARIO}'/>)" />
      <img src="../../imagenes/page_white_edit.png" alt="" style="cursor:pointer" title="Editar / Abrir" onclick="nuevoEditarBeneficiario(<c:out value='${item.ID_BENEFICIARIO}'/>)" />
     
        <c:if test='${item.STATUS==1}'> <img style="cursor:pointer" src="../../imagenes/cross.png" title="Deshabilitar" border="0" width="16" height="16" onclick="deshabilitarBeneficiario(<c:out value='${item.ID_BENEFICIARIO}'/>)" /></c:if>
        <c:if test='${item.STATUS==0}'> <img style="cursor:pointer" src="../../imagenes/accept.png" title="Reactivar" border="0" width="16" height="16" onclick="habilitarBeneficiario(<c:out value='${item.ID_BENEFICIARIO}'/>)" /></c:if>
     </td>
    
  </tr>
  <c:set var="cont" value="${cont+1}"/>
 </c:forEach>
  <tr>
          <td colspan="8" height="25" style="background-color:#FFF" align="left"><strong>Total de registros encontrados: <c:out value='${CONTADOR}'/></strong></td>
        </tr> 
  
  
</table>
</form>
</body>
</html>