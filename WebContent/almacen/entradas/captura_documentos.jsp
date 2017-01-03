<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Entradas - Captura de documentos</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link rel="stylesheet" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorEntradasDocumentosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>
<script type="text/javascript" src="../../dwr/util.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="captura_documentos.js"></script>
<script type="text/javascript" src="../../include/js/otros/productos.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen"><!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->
</head>
<body>
<br />
  <table width="95%" align="center"><tr><td><h1>Entradas - Captura de documentos/Pedidos</h1></td></tr></table>  
<form action="captura_documentos.action" method="post" name="frmEntrada" id="frmEntrada" >
  <table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
    <td align="center">
<div id="tabuladores">
  <ul>
    <li><a href="#fragment-facturas">Encabezado</a></li>
    <li><a href="#fragment-detalles">Detalles</a></li>
  </ul>

  <div id="fragment-facturas" align="left">
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" >
    <tr>
      <td width="11%" class="field_label">&nbsp;</td>
      <td width="89%" align="left">
      <input type="hidden" id="ID_ENTRADA" name="ID_ENTRADA"  value="<c:out value='${documento.ID_ENTRADA}'/>"/></td>
      </tr>
    <tr>
      <th height="30" class="field_label">Num. Entrada:</th>
      <td align="left"><div id="div_numEntrada"><c:out value="${documento.FOLIO}"/></div></td>
    </tr>
    <tr>
      <th height="30" class="field_label">Unidad Administrativa:</th>
      <td align="left">
      <sec:authorize ifNotGranted="ROLE_Almacen_PRIVILEGIOS_VER_UNIDADES_ADMINISTRATIVAS">
      	<c:out value="${nombreUnidad}"/>
        <input type="hidden" id="cbodependencia" name="cbodependencia" value="<c:out value='${cbodependencia}'/>">
     </sec:authorize>
     <sec:authorize ifAllGranted="ROLE_Almacen_PRIVILEGIOS_VER_UNIDADES_ADMINISTRATIVAS">
      	<div class="styled-select">
            <select name="cbodependencia" id="cbodependencia" style="width:400px">
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
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;Almac&eacute;n:</th>
      <td align="left">
      <div class="styled-select">
      <select name="cboalmacen" class="comboBox" id="cboalmacen" style="width:400px;">
        <option value="0">[Seleccione]</option>
        <c:forEach items="${almacenes}" var="item" varStatus="status"> <option value='<c:out value="${item.ID_ALMACEN}"/>' <c:if test='${item.ID_ALMACEN==documento.ID_ALMACEN}'> selected </c:if>>
          <c:out value='${item.DESCRIPCION}'/>
          </option>
        </c:forEach>
      </select>
      </div></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;* Tipo de documento:</th>
      <td align="left" valign="middle"><div class="styled-select">
        <select name="cbotipodocumento" class="comboBox" id="cbotipodocumento" style="width:170px" onChange="validarTipo()">
          <option value="0">[Seleccione]</option>
          <c:forEach items="${tiposDocumentos}" var="item" varStatus="status">
            <option value='<c:out value="${item.ID_TIPO_DOCUMENTO}"/>' 
              <c:if test='${item.ID_TIPO_DOCUMENTO==documento.ID_TIPO_DOCUMENTO}'> selected </c:if>
              >
              <c:out value='${item.DESCRIPCION}'/>
              </option>
          </c:forEach>
        </select>
      </div></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;* Tipo de entrada:</th>
      <td align="left" valign="middle"><span class="styled-select">
        <select name="cbotipoentrada" class="comboBox" id="cbotipoentrada" style="width:170px">
          <option value="0">[Seleccione]</option>
          <c:forEach items="${tiposEntrada}" var="item" varStatus="status"> <option value='<c:out value="${item.ID_TIPO_ENTRADA}"/>' 
              
            <c:if test='${item.ID_TIPO_ENTRADA==documento.ID_TIPO_ENTRADA}'> selected </c:if>
            >
            <c:out value='${item.DESCRIPCION}'/>
            </option>
          </c:forEach>
        </select>
      </span></td>
    </tr>
    <tr>
      <th height="30" class="field_label">*Tipo de Efecto:</th>
      <td align="left" valign="middle"><span class="styled-select">
        <select name="cbotipoEfecto" class="comboBox" id="cbotipoEfecto" style="width:170px">
          <option value="0" <c:if test='${documento.FECHA_OFICIAL!=NULL}'> selected </c:if>>Oficial</option>
          <option value="1" <c:if test='${documento.FECHA_OFICIAL==NULL}'> selected </c:if>>No Oficial</option>
        </select>
      </span></td>
    </tr>
    <tr>
      <th height="30" class="field_label">*Movimiento:</th>
      <td align="left" valign="middle"><table width="391" cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td width="178"><span class="styled-select">
              <select name="cbomovimiento" class="comboBox" id="cbomovimiento" style="width:170px" onChange="validarMovimiento()">
                <option value="0" <c:if test='${documento.ID_ENTRADA_AGREGADA==NULL&&documento.ID_ENTRADA_CANCELADA==NULL}'> selected </c:if>>Entrada Normal </option>
                <option value="1" <c:if test='${documento.ID_ENTRADA_AGREGADA!=NULL&&documento.ID_ENTRADA_CANCELADA==NULL}'> selected </c:if>>Incrementa a Entrada </option>
                <option value="2" <c:if test='${documento.ID_ENTRADA_CANCELADA!=NULL&&documento.ID_ENTRADA_AGREGADA==NULL}'> selected </c:if>>Cancela a Entrada </option>
              </select>
            </span></td>
            <td width="213" id="td_mov"><span class="styled-select">
            <input name="txtentrada2" type="text" class="input" id="txtentrada2" maxlength="20" onBlur="borrarEntrada2()" style="width:100px" value="<c:out value='${documento.FOLIO_AGREGADO}'/><c:out value='${documento.FOLIO_CANCELADO}'/>" />
            <img src="../../imagenes/search_16.png" alt="" align="absmiddle" id="img_movimiento"   style="cursor:pointer" title="Buscar entradas"/>
            <input name="ID_ENTRADA2" type="hidden" id="ID_ENTRADA2" value="<c:out value='${documento.ID_ENTRADA_AGREGADA}'/><c:out value='${documento.ID_ENTRADA_CANCELADA}'/>"/>
            </span></td>
          </tr>
      </table></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp; N&uacute;mero de Pedido:</th>
      <td align="left" valign="middle"><input name="txtpedido" type="text" class="input" id="txtpedido" maxlength="20" style="width:170px" value="<c:out value='${documento.ID_PEDIDO}'/>" />
        <img src="../../imagenes/search_16.png" alt="" align="absmiddle" id="img_pedidos"   style="cursor:pointer" title="Buscar lista de Pedidos"/>
        <input name="ID_PEDIDO" type="hidden" id="ID_PEDIDO" value="<c:out value='${documento.ID_PEDIDO}'/>"/><input name="ID_PEDIDO_ANT" type="hidden" id="ID_PEDIDO_ANT" value="<c:out value='${documento.ID_PEDIDO}'/>"/></td>
    </tr>
    <tr>
      <th height="32" class="field_label">&nbsp; N&uacute;mero de Remisión:</th>
      <td align="left"><input name="txtdocumento" type="text" class="input" id="txtdocumento" maxlength="20" style="width:170px" value="<c:out value='${documento.DOCUMENTO}'/>" /></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;* Fecha entrada:</th>
      <td align="left" ><input name="txtfechadocumento" type="text" class="input" id="txtfechadocumento" maxlength="20" style="width:170px" value="<c:out value='${documento.FECHA_DOCUMENTO}'/>"/></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;Programa:</th>
      <td align="left"><input name="txtproyecto" type="text" class="input" id="txtproyecto" maxlength="6" style="width:170px" value="<c:out value='${documento.N_PROGRAMA_DESC}'/>"/>
        <input name="ID_PROYECTO" type="hidden" id="ID_PROYECTO" value="<c:out value='${documento.ID_PROYECTO}'/>" /></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;Partida:</th>
      <td align="left"><input name="txtpartida" type="text" class="input" id="txtpartida" maxlength="4" style="width:170px" value="<c:out value='${documento.PARTIDA}'/>"/></td>
      </tr>
    <tr>
      <th height="25" class="field_label">&nbsp;Proveedor:</th>
      <td align="left"><input name="txtbeneficiario" type="text" class="input" id="txtbeneficiario" value="<c:out value='${documento.PROVEEDOR}'/>" style="width:400px" />
        <input name="ID_PROVEEDOR" type="hidden" id="ID_PROVEEDOR" value="<c:out value='${documento.ID_PROVEEDOR}'/>" /></td>
      </tr>
    <tr>
      <th height="47" class="field_label">&nbsp;*Descripcion:</th>
      <td align="left"><label>
        <textarea name="txtobservacion" cols="70" rows="3" style="width:400px" class="textarea" id="txtobservacion"><c:out value='${documento.DESCRIPCION}'/></textarea>
        </label></td>
      </tr>
    <tr>
      <td class="field_label">&nbsp;</td>
      <td align="left">&nbsp;</td>
    </tr>
    <tr>
      <td class="field_label">&nbsp;</td>
      <td align="left">
        <table width="390" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="130" align="center"><div class="buttons tiptip">
              <div class="buttons tiptip">
                <button name="cmdnuevo" id="cmdnuevo" type="button" class="button red middle"><span class="label" style="width:100px">Nuevo</span></button>
              </div>
            </div></td>
            <td width="130"><button name="cmdcerrar" id="cmdcerrar" <c:if test='${documento.ID_ENTRADA==NULL&&documento.FECHA_CIERRE==NULL}'>disabled</c:if> type="button" class="button red middle"><span class="label" style="width:100px">Cerrar</span></button></td>
            <td width="130"><button name="cmdguardar" id="cmdguardar" <c:if test='${documento.ID_ENTRADA==NULL&&documento.FECHA_CIERRE==NULL}'>disabled</c:if> type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button></td>
          </tr>
        </table></td>
    </tr>
    <tr >
      <td height="25"   colspan="2" class="field_label"  >&nbsp;</td>
      </tr> 
  </table>
</div>
<br>
<div id="fragment-detalles" align="left">
  <table width="100%" height="50%" border="0"  align="left" cellpadding="0" cellspacing="0" class="listas" id="listasConceptos">
    <thead>
          <tr >
            <th width="5%" height="20"  align="center"><img src="../../imagenes/cross.png" alt="Eliminar conceptos seleccionados"  onClick="eliminarConceptos()" style="cursor:pointer"></th>
            <th width="6%" align="center">Cantidad solic.</th>
            <th width="6%" align="center">Cantidad disp.</th>
            <th width="12%" align="center">Unidad de medida</th>
            <th width="44%" align="center">Descripci&oacute;n</th>
            <th width="13%" align="center">Familia</th>
            <th width="11%" align="center"> Precio Unitario<input type="hidden" id="SUBTOTAL" value="0"></th>
            </tr>
        </thead>
        <tbody>
        
        </tbody>
        <tr id="tr_subtotal">
            <td align="left" colspan="5"  height="20" bgcolor="#FFFFFF">
                    <table  id="tb_boton" width="150" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
                      <tr>
                       <td bgcolor="#FFFFFF" width="130"><button name="cmdguardarCantidad" onClick="guardarCantidadDetalles()" id="cmdguardarCantidad" type="button" class="button blue middle"><span class="label" style="width:150px">Guardar cambios</span></button></td>
                      </tr>
                    </table>
            </td>
            <td width="13%"  bgcolor="#FFFFFF" align="right"><strong>Subtotal:&nbsp;</strong></td>
            <td width="11%"  bgcolor="#FFFFFF" align="right"><div id="div_subtotal">$0.00&nbsp;</div></td>
            </tr>
          <tr id="tr_descuento">
            <td align="center" colspan="5"  height="20" bgcolor="#FFFFFF"></td>
            <td align="right"  bgcolor="#FFFFFF"><strong>Descuento:</strong></td>
            <td align="right"  bgcolor="#FFFFFF"><span style="background:#FFF">
              <input type="text" id="txtdescuento" class="input" style="width:95%; text-align:right; padding-right:3px" onKeyPress="return keyNumbero(event);" value='<fmt:formatNumber value="${documento.DESCUENTO}"  pattern="##########.00" />' onBlur="getTotales()"/>
            </span></td>
            </tr>
          <tr id="tr_iva">
            <td align="center" colspan="5"  height="20" bgcolor="#FFFFFF"></td>
            <td align="right"  bgcolor="#FFFFFF"><span style="border-left:none; background:#FFF">
              <select name="cboiva" class="comboBox" id="cboiva" onChange="getTotales();">
                <option value="0" <c:if test="${documento.TIPO_IVA==0}"> selected</c:if>>Sin I.V.A</option>
                <option value="1" <c:if test="${documento.TIPO_IVA==1}">selected</c:if>>Con 16% automatico de I.V.A</option>
                <option value="2" <c:if test="${documento.TIPO_IVA==2}">selected</c:if>>I.V.A Personalizado</option>
              </select>
            </span></td>
            <td align="right"  bgcolor="#FFFFFF"><span style="background:#FFF">
              <input type="text" id="txtiva" class="input" style="width:95%; text-align:right; padding-right:3px" value="${documento.IVA}"  onKeyPress="return keyNumbero(event);" onBlur="getTotalesMasIva()"/>
            </span></td>
            </tr>
          <tr id="tr_total">
            <td align="center" colspan="5" height="20" bgcolor="#FFFFFF"></td>
            <td align="right"  bgcolor="#FFFFFF"><strong>Total:</strong>&nbsp;</td>
            <td align="right"  bgcolor="#FFFFFF"><div id="div_total">$0.00&nbsp;</div></td>
            </tr>
    </table>
  <br>
  <br>
  <br>
</div>
</div>
  </td>
  </tr>
</table>
</form>
</body>
</html>
