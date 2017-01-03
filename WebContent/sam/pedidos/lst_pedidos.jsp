<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Listado de Pedidos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="lst_pedidos.js"> </script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorPedidos.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />

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
<body>
<form  action="" method="post" id="forma" name="forma">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" name="clavePedido" id="clavePedido" >
<table width="95%" align="center"><tr><td><h1>Pedidos - Listado de Pedidos</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th height="15">&nbsp;</th>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr >
    <th  width="11%" height="25">
      Unidad :</th>
  <td>
  <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      <c:out value="${nombreUnidad}"/>
      <input type="hidden" name="cbodependencia" id="cbodependencia" value="<c:out value='${idUnidad}'/>">
  </sec:authorize>
  <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
  <div class="styled-select">
   <select name="cbodependencia" id="cbodependencia" style="width:447px;">
    <option value="0" <c:if test='${item.ID==0}'> selected </c:if>>[Todas la Unidades Administrativas]</option>
    <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
      <option value='<c:out value="${item.ID}"/>' <c:if test='${item.ID==idUnidad}'> selected </c:if>>
      <c:out value='${item.DEPENDENCIA}'/>
      </option>
      </c:forEach>
      </select>
      </div>
      </sec:authorize>
    </td>
    <td width="15%"><input type="checkbox" name="status" id="status"  value="0" <c:if test="${fn:contains(status,'0')}">checked</c:if>>
      &nbsp;Edici&oacute;n</td>
    <td width="15%"><input type="checkbox" name="status" id="status"  value="1" <c:if test="${fn:contains(status,'1')}">checked</c:if>
    >
      &nbsp;Cerrado</td>
    <td width="15%"><input name="status" type="checkbox" id="status"  value="4" 
      <c:if test="${fn:contains(status,'4')}">checked</c:if>
      >
      &nbsp;Pedido por OP</td>
  </tr>
  <tr >
    <th height="25" >Tipo de gasto:
    <td><strong>
    <div class="styled-select">
    <select name="cbotipogasto" id="cbotipogasto" style="width:447px;">
     <option value="0">
      [Todos los tipos de gastos]</option>
      <c:forEach items="${tipodeGasto}" var="item" varStatus="status">
        <option value='<c:out value="${item.ID}"/>'
          <c:if test='${item.ID==tipo_gto}'> selected </c:if>
          >
          <c:out value='${item.RECURSO}'/>
          </option>
      </c:forEach>
    </select>
    </div>
    </strong></td>
    <td ><input type="checkbox" name="status" id="status"  value="3" <c:if test="${fn:contains(status,'3')}">checked</c:if>>
      &nbsp;Cancelado</td>
    <td ><input name="status" type="checkbox" id="status"  value="5" 
      <c:if test="${fn:contains(status,'5')}">checked</c:if>
      >
      &nbsp;Surtido</td>
    <td rowspan="3">
    
      <table width="500" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><button name="btnBuscar" id="btnBuscar" onClick="getPedidos()" type="button" class="button blue middle"><span class="label" style="width:100px">Buscar</span></button></td>
        </tr>
        <tr>
          <td>
          <div class="buttons tiptip">
        	<button name="cmdpdf" id="cmdpdf" title="Mostrar listado en formato PDF" type="button" class="button red middle"><span class="label" style="width:100px">Imprimir PDF</span></button>
         </div></td>
        </tr>
      </table></td>
  </tr>
  <tr >
    <th height="25" >Beneficiario:<td><input type="text" id="txtprestadorservicio" name="txtprestadorservicio" class="input" style="width:445px" value="<c:out value='${txtprestadorservicio}'/>"/>
      <input type="hidden" id="CVE_BENEFI" name="CVE_BENEFI" value="<c:out value='${CVE_BENEFI}'/>" /></td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
    </tr>
  <tr >
    <th height="25" >Capitulo:
    <td>
<strong><span class="styled-select">
      <select name="cbocapitulo" id="cbocapitulo" style="width:447px;">
        <option value="0"> [Todos los capitulos]</option>
        <c:forEach items="${capitulos}" var="item" varStatus="status">
          <option value='<c:out value="${item.CLV_CAPITU}"/>'
          <c:if test='${item.CLV_CAPITU==cbocapitulo}'> selected </c:if>
          >
          <c:out value='${item.CLV_CAPITU}'/> - <c:out value='${item.CAPITULO}'/>
          </option>
          </c:forEach>
        </select>
      </span></strong>
    </td>
    <td colspan="2" ><input type="checkbox" name="verUnidad" id="verUnidad" value="1"  <c:if test='${verUnidad==1}'>  checked </c:if> 
      >Incluir documentos de  la Unidad</td>
    </tr>
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODOS_LOS_DOCUMENTOS_DE_LA_UNIDAD">
  <tr >
    <th height="25" >Adicional:</th>
    <td height="25" ><div class="styled-select">
    <select name="cboconOP" id="cboconOP" style="width:447px;">
      <option value="0" 
        <c:if test='${0==cboconOP}'> selected </c:if>
        >[Todas las opciones]</option>
      <option value="1" 
        <c:if test='${1==cboconOP}'> selected </c:if>
        >Con Facturas</option>
      <option value="2" 
        <c:if test='${2==cboconOP}'> selected </c:if>
        >Sin Facturas</option>
    </select>
    </div>
        </td>
    <td height="25" colspan="2" ></td>
    <td height="25" >&nbsp;</td>
  </tr>
  <tr >
    <th height="25" >En almacen:</th>
    <td height="25" colspan="3" ><div class="styled-select">
      <select name="cboalmacen" id="cboalmacen" style="width:223px;">
        <option value="0" 
          <c:if test='${0==cboalmacen}'> selected </c:if>
          > [Seleccionar]</option>
        <option value="1" 
          <c:if test='${1==cboalmacen}'> selected </c:if>
          > Todos</option>
        <optgroup label="Con Entradas">
          <option value="2" 
            <c:if test='${2==cboalmacen}'> selected </c:if>
            > Completadas</option>
          <option value="3" 
            <c:if test='${3==cboalmacen}'> selected </c:if>
            > Incompletas</option>
          </optgroup>
      </select>
    </div></td>
    <td height="25" >&nbsp;</td>
  </tr>
  <tr >
    <th height="25" >Por fecha de:</th>
    <td height="25" colspan="3" ><input name="fechaInicial" type="text" class="input" id="fechaInicial" value="${fechaInicial}" size="12"maxlength="10">
&nbsp;<strong>Hasta</strong> &nbsp;
<input name="fechaFinal" type="text" class="input" id="fechaFinal" value="${fechaFinal}" size="12"  maxlength="10"></td>
    <td height="25" >&nbsp;</td>
  </tr>
  <tr >
    <th height="25" >Núm.  Pedido:</th>
    <td height="25" colspan="3" ><table width="575" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="182"><input name="txtpedido" type="text" id="txtpedido" maxlength="50" style="width:150px" class="input" value="<c:out value='${txtpedido}'/>"></td>
        <td width="112"><strong>Núm. Requisición:</strong></td>
        <td width="183"><input name="txtrequisicion" class="input" type="text" id="txtrequisicion" maxlength="50" style="width:150px" value="<c:out value='${txtrequisicion}'/>"></td>
        <td width="98">&nbsp;</td>
      </tr>
    </table></td>
    <td height="25" >&nbsp;</td> </tr>
  <tr >
    <th height="25" >&nbsp;</th>
    <td height="25" colspan="3" ></td>
    <td height="25" >&nbsp;</td>
  </tr>
  <tr >
    <th height="25" >&nbsp;</th>
    <td height="25" colspan="3" ></td>
    <td height="25" >&nbsp;</td>
  </tr>
    </sec:authorize>      
</table>
<br />
<table width="95%" class="listas" align="center" id="listaRequisiciones" cellpadding="0" cellspacing="0">
 <thead>
  <tr>
    <th width="3%" height="20">&nbsp;</th>
    <th width="9%">N&uacute;mero</th>
    <th width="9%">Fecha</th>
    <th width="6%">Estado</th>
    <th width="40%">Unidad Administrativa</th>
    <th width="4%">Almacén</th>
    <th width="9%">Requisición</th>
    <th width="9%">Importe</th>
    <th width="7%">Opciones</th>
  </tr>
   </thead>   
<tbody> 
<c:set var="cont" value="${0}" /> 
<c:forEach items="${listadoPedidos}" var="item" varStatus="status"> 
  <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
    <tr>
      <td align="center">
     <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_PEDIDOS">&nbsp;
    <c:if test='${item.STATUS==1||item.STATUS==0}'>
    	<input type="checkbox" alt="<c:out value='${item.NUM_PED}'/>" id="chkpedidos" name="chkpedidos" value="<c:out value='${item.CVE_PED}'/>"/>
    </c:if>
    
    </sec:authorize>
    </td>
    <td align="center"><sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_PEDIDOS"><a href="javascript:subOpAdm('ped', <c:out value='${item.CVE_PED}'/>, <c:out value='${item.CVE_PERS}'/>)"></sec:authorize><c:out value='${item.NUM_PED}'/><sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_PEDIDOS"></a></sec:authorize></td>
    <td align="center"><c:out value='${item.FECHA_PED}'/></td>
    <td align="center"><c:out value='${item.STATUS_DESC}'/></td>
    <td><c:out value='${item.UNIDAD}'/></td>
    <td align="center"><c:out value='${item.ALMACEN}'/></td>
    <td align="center"><c:out value='${item.NUM_REQ}'/></td>
    <td align="right"><fmt:formatNumber value="${item.TOTAL}"  pattern="#,###,###,##0.00" /> </td>
    <td align="center">
    <c:if test='${item.STATUS!=3}'>
    	<img style="cursor:pointer" src="../../imagenes/pdf.gif" title="Ver Documento" border="0" width="14" height="16" onClick="getReportePedido(<c:out value='${item.CVE_PED}'/>)">
    </c:if> 
    <c:if test='${item.STATUS==3}'>
    	<img src="../../imagenes/pdf2.png" border="0" width="14" height="16">
    </c:if>
    
    <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_SOLO_IMPRESION_GENERAL">
        <c:if test='${item.STATUS==0}'>
        <img src="../../imagenes/page_white_edit.png" title="Editar / Abrir" style="cursor:pointer" onClick="editarPedido(<c:out value='${item.CVE_PED}'/>, <c:out value='${item.STATUS}'/>)">
        </c:if>
        <c:if test='${item.STATUS!=0}'>
        <img src="../../imagenes/page_gray_edit.png">
        </c:if>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_PEDIDOS">
            <c:if test='${item.STATUS==0||item.STATUS==1}'>
                <img style="cursor:pointer" src="../../imagenes/cross.png" title="Cancelar Pedido" border="0" width="16" height="16" onClick="cancelarPedido(<c:out value='${item.CVE_PED}'/>)">     
            </c:if>
            <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_REACTIVAR_PEDIDOS">
                <c:if test='${item.STATUS!=0&&item.STATUS!=1&&item.STATUS!=2}'>
                    <img src="../../imagenes/cross2.png" border="0" width="16" height="16">     
                </c:if>
            </sec:authorize>
            <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_REACTIVAR_PEDIDOS">
                 <c:if test='${item.STATUS==3}'>
                    <img style="cursor:pointer" src="../../imagenes/accept.png" title="Reactivar Pedido" border="0" width="16" height="16" onClick="reactivarPedido(<c:out value='${item.CVE_PED}'/>)">
                </c:if>
            </sec:authorize>
        </sec:authorize>
        <c:if test='${item.STATUS==5}'>
               <img src="../../imagenes/cross2.png" border="0" width="16" height="16">     
         </c:if>
    </sec:authorize>
    
    
    </td>
    <c:set var="cont" value="${cont+1}"/> 
  </tr>
  </c:forEach>
   <c:if test="${fn:length(listadoPedidos) > 0}"> 
      <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_PEDIDOS">
        <tr>
        <td colspan="9" align="left" height="40" style="background-color:#FFF">
        <table width="269" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <c:if test="${fn:contains(status,'1')||fn:contains(status,'2')}">
              <td width="130" bgcolor="#FFFFFF"><div class="buttons tiptip">
                <button name="cmdaperturar" id="cmdaperturar2" onClick="aperturarPedidos()" title="Apertura para edicion los documentos seleccionados" type="button" class="button red middle"><span class="label" style="width:100px">Aperturar</span></button>
              </div></td>
            </c:if>
            <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_REQUISICIONES">
              <td width="139" bgcolor="#FFFFFF"><div class="buttons tiptip">
                <button name="cmdcancelarm" id="cmdcancelarm2" onClick="cancelarPedidoMultiples()"  title="Cancela o elimina los documentos seleccionados" type="button" class="button red middle" ><span class="label" style="width:100px">Cancelar</span></button>
              </div></td>
            </sec:authorize>
          </tr>
        </table></td>
        </tr> 
        </sec:authorize> 
        <tr>
          <td colspan="9" height="25" style="background-color:#FFF" align="left"><strong>Total de registros encontrados: <c:out value='${CONTADOR}'/></strong></td>
        </tr> 
     
   </c:if>
  </tbody>  
</table>
</form>
</body>
</html>
