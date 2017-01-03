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
<script type="text/javascript" src="lista_contratos.js"> </script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorListadoContratosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/ControladorContratosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />

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
<style type="text/css">
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
</style>
<body >
<form  action="lista_contratos.action" method="POST" id="forma" name="forma">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" name="cve_contrato" id="cve_contrato" >
<table width="95%" align="center"><tr><td><h1> Contratos - Listado de Contratos</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th height="13">&nbsp;</th>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <th  width="11%" height="30">Unidad :</th>
  <td>
        <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      <c:out value="${nombreUnidad}"/><input type="hidden" name="cbodependencia" id="cbodependencia" value='<c:out value="${idUnidad}"/>' />
      </sec:authorize>
       <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
       <div class="styled-select">
        <select name="cbodependencia"  id="cbodependencia" style="width:500px" class="comboBox" >
        <option value="0">[Todas la Unidades Administrativas]</option>
        	<c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
              <option value='<c:out value="${item.ID}"/>' <c:if test='${item.ID==idUnidad}'> selected </c:if>><c:out value='${item.DEPENDENCIA}'/></option>
            </c:forEach>
        </select>
        </div>
      </sec:authorize>
    </td>
    <td width="15%"><input type="checkbox" name="status" id="status"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>>
      &nbsp;Edición</td>
    <td width="15%"><input type="checkbox" name="status" id="status"  value="1" <c:if test="${fn:contains(status,'1')}" >checked</c:if>
    >Contratado</td>
    <td width="15%"><input type="checkbox" name="status" id="status"  value="2" <c:if test="${fn:contains(status,'2')}" >checked</c:if>
    >
      Cancelada</td>
  </tr>
  <tr >
    <th height="30" >Tipo de gasto:
    <td>
    <div class="styled-select">
        <select name="cbotipogasto" class="comboBox" id="cbotipogasto" style="width:500px">
          <option value="0">[Todos los tipos de gastos]</option>
          <c:forEach items="${tipodeGasto}" var="item" varStatus="status">
            <option value='<c:out value="${item.ID}"/>' <c:if test='${item.ID==tipo_gto}'> selected</c:if>><c:out value='${item.RECURSO}'/></option>
          </c:forEach>
        </select>
    </div>
    </td>
    <td ><input name="status" type="checkbox" id="status"  value="3" <c:if test="${fn:contains(status,'3')}">checked</c:if>> Terminado</td>
    <td ></td>
    <td><button name="cmdbuscar" id="cmdbuscar" type="button"  class="button red middle"><span class="label" style="width:100px">Buscar</span></button></td>
  </tr>
  <tr >
    <th height="30" >Beneficiario:    
    <td><input type="text" id="txtprestadorservicio" name="txtprestadorservicio" class="input" style="width:500px" value="<c:out value='${txtprestadorservicio}'/>"/>
      <input type="hidden" id="CLV_BENEFI" name="CLV_BENEFI" value="<c:out value='${CLV_BENEFI}'/>" /></td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="30" >Por fecha de :
    <td><strong>
    <input name="fechaInicial" type="text" id="fechaInicial" value="${fechaInicial}" size="12"maxlength="10">
&nbsp;Hasta &nbsp;
<input name="fechaFinal" type="text" id="fechaFinal" value="${fechaFinal}" size="12"  maxlength="10">
    </strong></td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODOS_LOS_DOCUMENTOS_DE_LA_UNIDAD">
  <tr >
    <td height="30" >&nbsp;</td>
    <td height="30" colspan="4" ><input type="checkbox" name="verUnidad" id="verUnidad" value="1"  <c:if test='${verUnidad==1}'>  checked </c:if> >Incluir documentos de la Unidad</td>
  </tr>
       </sec:authorize>  
</table>
<br />
<table width="95%" class="listas" align="center" id="listaRequisiciones"  cellpadding="0" cellspacing="0">
 <thead>
  <tr>
    <th width="11%" height="20">No.</th>
    <th width="9%">Número</th>
    <th width="9%">Fecha inicio</th>    
    <th width="9%">Fecha final</th>
    <th width="22%">Beneficiario</th>
    <th width="9%">Num. Doc.</th>
    <th width="8%">Tipo</th>
    <th width="8%">Status</th>
    <th width="7%">Importe</th>    
    <th width="8%">Opciones</th>
  </tr>
   </thead>   
<tbody>  
<c:set var="cont" value="${0}" /> 
<c:forEach items="${listado}" var="item" varStatus="status"> 
  <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')"/>
      <td align="center" height="20">&nbsp;
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_CONTRATOS">
    	<c:if test='${item.STATUS==0||item.STATUS==1}'><input alt="&lt;c:out value='${item.CVE_CONTRATO}'/&gt;" type="checkbox" id="chkcontratos" name="chkcontratos" value="<c:out value='${item.CVE_CONTRATO}'/>"/></c:if>
    </sec:authorize>    </td>
    <td align="center">
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_ORDENES_DE_PAGO">
            <a href="javascript:subOpAdm('con', <c:out value='${item.CVE_CONTRATO}'/>, <c:out value='${item.CVE_PERS}'/>)">
        </sec:authorize>
        
    	<c:out value='${item.NUM_CONTRATO}'/>
        
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_ORDENES_DE_PAGO">
    		</a>
    	</sec:authorize>
        
    </td>
    <td align="center"><c:out value='${item.FECHA_INICIO}'/></td>
    <td align="center"><c:out value='${item.FECHA_TERMINO}'/></td>
    <td align="left">&nbsp;<c:out value='${item.PROVEEDOR}'/></td>
    <td align="center"><c:out value='${item.NUM_DOC}'/></td>
    <td align="center"><c:out value='${item.TIPO_CONTRATO}'/></td>
    <td align="center"><c:out value='${item.STATUS_DESC}'/></td>
    <td align="right"><fmt:formatNumber value="${item.IMPORTE}"  pattern="#,###,###,##0.00" /> </td>
    <td align="center">&nbsp;<img style="cursor:pointer" src="../../imagenes/pdf.gif" alt="Ver Documento" border="0" width="14" height="16" onClick="mostrarOpcionCONPDF('<c:out value='${item.ARCHIVO_ANEXO}'/>', ${item.CVE_CONTRATO})">
    <c:if test='${item.STATUS==0}'>
    <img src="../../imagenes/page_white_edit.png" title="Editar / Abrir" style="cursor:pointer" onClick="editarCON(<c:out value='${item.CVE_CONTRATO}'/>)"></c:if>
    <c:if test='${item.STATUS!=0}'>
    <img src="../../imagenes/page_gray_edit.png"  title="Editar / Abrir"></c:if>
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_CONTRATOS">
    	<c:if test='${item.STATUS==0||item.STATUS==1}'>
    	<img id="Cancelarcontra" src="../../imagenes/cross.png" width="16" height="16" style="cursor:pointer" alt="">
        </c:if>
    </sec:authorize>     </td>
    <c:set var="cont" value="${cont+1}"/>
  </tr>
  </c:forEach> 
 <c:if test="${fn:length(listado) > 0}"> 
 <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_ORDEN_DE_PAGO"> 
 <c:if test="${fn:contains(status,'0')||fn:contains(status,'1')}"> 
  <tr>
    <td height="37" colspan="10" align="left" style="background:#FFF"><table width="269" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <c:if test="${fn:contains(status,'1')}">
          <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_FACTURAS">
            <td width="130" bgcolor="#FFFFFF"><div class="buttons tiptip">
              <button name="cmdaperturar" id="cmdaperturar" title="Apertura para edicion los documentos seleccionados" type="button" class="button red middle"><span class="label" style="width:100px">Aperturar</span></button>
            </div></td>
          </sec:authorize>
        </c:if>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_CONTRATOS">
          <td width="139" bgcolor="#FFFFFF"><div class="buttons tiptip">
            <button name="cmdcancelar" id="cmdcancelar"  title="Cancela o elimina los documentos seleccionados" type="button" class="button red middle" ><span class="label" style="width:100px">Cancelar</span></button>
          </div></td>
        </sec:authorize>
      </tr>
    </table></td>
    </tr>
    </c:if>
    </sec:authorize>
  </c:if>
  
  </tbody>  
</table>
</form>
</body>
</html>
