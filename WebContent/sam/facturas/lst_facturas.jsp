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
<script type="text/javascript" src="lst_facturas.js?x=<%=System.currentTimeMillis()%>"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorListadoFacturasRemoto.js"> </script>
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
<form action=""  method="post" id="frmreporte" name="frmreporte">
<table width="95%" align="center"><tr><td><h1>Facturas - Listado de facturas</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th width="242" height="25">&nbsp;</th>
    <td colspan="3">
      <input name="clv_benefi" type="hidden" id="clv_benefi" value="<c:out value='${clv_benefi}'/>" />
      <input name="CVE_FACTURA" type="hidden" id="CVE_FACTURA" value="" />
      </td>
    <td width="235">&nbsp;</td>
  </tr>
  <tr >
    <th height="30">Unidad Administrativa:</th>
    <td colspan="3">
      <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
        <c:out value="${nombreUnidad}"/>
        <input type="hidden" id="cbodependencia" name="cbodependencia" value="<c:out value='${cbodependencia}'/>">
        </sec:authorize>
      <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
        <div class="styled-select">
          <select name="cbodependencia" id="cbodependencia" style="width:445px">
            <option value="0">[Todas las Dependencias]</option>
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
    <th height="30" >Por fecha desde:
      <td width="180"><input name="txtfechaInicial" type="text" class="input"  id="txtfechaInicial"  value="<c:out value='${fechaInicial}'/>" ></td>
    <td width="110"  align="right">&nbsp;<strong>Hasta:</strong> &nbsp;</td>
    <td width="840"><input name="txtfechaFinal" type="text" class="input"  id="txtfechaFinal"  value="<c:out value='${fechaFinal}'/>"></td>
    <td >    <div class="buttons tiptip">
      <button name="cmdbuscar" id="cmdbuscar" type="button" class="button red middle"><span class="label" style="width:100px">Buscar</span></button>
      </div></td>
  </tr>
  <tr >
    <th height="30" >Proveedor:   
      <td colspan="3"><input name="txtbeneficiario" type="text" class="input"  id="txtbeneficiario"  value="<c:out value='${beneficiario}'/>" style="width:445px"></td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="30" >Número Requisición:  
    <td><input name="txtnumreq" type="text" class="input"  id="txtnumreq"  value="<c:out value='${numreq}'/>" ></td>
    <td align="right"><strong>Num. de Pedido: </strong>&nbsp;</td>
    <td><strong>
      <input name="txtpedido" type="text" class="input"  id="txtpedido"  value="<c:out value='${numped}'/>" >
    </strong></td>
    <td >&nbsp;</td>
    </tr>
  <tr >
    <th height="30" >Número de factura:
    <td><input name="txtfactura" type="text" class="input"  id="txtfactura"  value="<c:out value='${numfactura}'/>" ></td>
    <td colspan="2" align="right">&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="25" >Estatus: </th>
    <td height="25" colspan="4" ><input type="checkbox" name="status" id="status"  value="0" <c:if test="${fn:contains(estatus,'0')}">checked</c:if>>Edición &nbsp; <input type="checkbox" name="status" id="status"  value="1" <c:if test="${fn:contains(estatus,'1')}">checked</c:if>>Cerrardo &nbsp; <input type="checkbox" name="status" id="status"  value="2" <c:if test="${fn:contains(estatus,'2')}">checked</c:if>>Cacncelado&nbsp; <input type="checkbox" name="status" id="status"  value="3" <c:if test="${fn:contains(estatus,'3')}">checked</c:if>>Finiquitado</td>
    </tr>
  <tr >
    <th height="25" >&nbsp;</th>
    <td height="25" colspan="4" >&nbsp;</td>
  </tr>  
</table>
<br />
<table width="95%" border="0" cellpadding="0" cellspacing="0" class="listas" align="center" id="listaDocumentos">
 <thead>
  <tr>
    <th width="4%"><input type="checkbox" name="todos" id="todos"></th>
    <th width="9%">Num. Factura</th>
    <th width="7%">Pedido</th>
    <th width="7%">Requisicóon</th>
    <th width="15%">Unidad Adm.</th>
    <th width="9%">Fecha documento</th>
    <th width="28%">Descripción</th>
    <th width="7%">Importe</th>
    <th width="5%">Status</th>
    <th width="5%">Opciones</th>
  </tr>
   </thead>   
<tbody>  
<c:set var="cont" value="${0}" /> 
<c:forEach items="${listadoFacturas}" var="item" varStatus="status"> 
  <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
    <td height="25" align="center"><input type="checkbox" id="chkfacturas" name="chkfacturas" value="<c:out value='${item.CVE_FACTURA}'/>"/></td>
    <td align="center"><sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_REQUISICIONES"><a href="javascript:subOpAdm('fac', <c:out value='${item.CVE_FACTURA}'/>, <c:out value='${item.CVE_PERS}'/>)"></sec:authorize><c:out value='${item.NUM_FACTURA}'/><sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_REQUISICIONES"></a></sec:authorize></td>
    <td align="center"><c:out value='${item.NUM_PED}'/></td>
    <td align="center"><c:out value='${item.NUM_REQ}'/></td>
    <td align="left"><c:out value='${item.DEPENDENCIA}'/></td>
    <td align="center"><c:out value='${item.FECHA_DOCUMENTO}'/></td>
    <td><c:out value='${item.NOTAS}'/></td>
    <td align="right">$<fmt:formatNumber value="${item.TOTAL}"  pattern="#,###,###,##0.00" /></td>
    <td align="center"><c:out value='${item.STATUS_DESC}'/></td>
    <td align="center">
        <c:if test='${item.STATUS==0}'>
        	<img src="../../imagenes/page_white_edit.png" title="Editar / Abrir" style="cursor:pointer" onClick="editarFactura(<c:out value='${item.CVE_FACTURA}'/>)">
        </c:if>
        <c:if test='${item.STATUS!=0}'>
        	<img src="../../imagenes/page_gray_edit.png">
        </c:if>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_PEDIDOS">
            <c:if test='${item.STATUS==0||item.STATUS==1}'>
                <img style="cursor:pointer" src="../../imagenes/cross.png" title="Cancelar" border="0" width="16" height="16" onClick="cancelarFactura(<c:out value='${item.CVE_FACTURA}'/>)">     
            </c:if>
            <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_REACTIVAR_PEDIDOS">
                <c:if test='${item.STATUS!=0&&item.STATUS!=1}'>
                    <img src="../../imagenes/cross2.png" border="0" width="16" height="16">     
                </c:if>
            </sec:authorize>
        </sec:authorize>
        <c:if test='${item.STATUS==2}'>
               <img src="../../imagenes/cross2.png" border="0" width="16" height="16">     
         </c:if>
     </td>
  </tr>
  <c:set var="cont" value="${cont+1}"/> 
  </c:forEach>
   <tr>
   	<td colspan="9" style="background-color:#FFF"><table width="269" border="0" cellspacing="0" cellpadding="0">
   	  <tr>
   	   <c:if test="${fn:contains(estatus,'1')}">
       	<sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_FACTURAS"> 
   	      <td width="130" bgcolor="#FFFFFF"><div class="buttons tiptip">
   	        <button name="cmdaperturar" id="cmdaperturar" title="Apertura para edicion los documentos seleccionados" type="button" class="button red middle"><span class="label" style="width:100px">Aperturar</span></button>
 	        </div>
          </td>
        </sec:authorize>
 	   </c:if>
   	    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_FACTURAS">
   	      <td width="139" bgcolor="#FFFFFF"><div class="buttons tiptip">
   	        <button name="cmdcancelar" id="cmdcancelar"  title="Cancela o elimina los documentos seleccionados" type="button" class="button red middle" ><span class="label" style="width:100px">Cancelar</span></button>
 	        </div></td>
 	      </sec:authorize>
 	    </tr>
 	  </table>
    
    </tr>
  </tbody>  
</table>
</form>
</body>
</html>
