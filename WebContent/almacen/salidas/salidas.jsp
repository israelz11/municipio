<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Salidas</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link type="text/css" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">	
<link href="../../include/js/autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorSalidasRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="salidas.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="../../include/js/otros/productos.js"></script>
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
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="95%" align="center"><tr><td><h1>Salidas - Captura de Salidas</h1></td></tr></table>  
<form name="forma" id="forma" method="get" action="../reportes/salidas.action">
<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
    <td align="center">
<div id="tabuladores">
  <ul>
   	<li><a href="#fragment-general"><span>Información general</span></a></li>
    <li><a href="#fragment-detalles">Detalles</a></li>
  </ul>
  <div id="fragment-general" align="left">
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="formaCaptura">
    <tr>
      <th height="21">
      <input type="hidden" name="ID_ENTRADA" id="ID_ENTRADA" value="<c:out value='${datos.ID_ENTRADA}'/>">
      <input type="hidden" name="ID_SALIDA" id="ID_SALIDA" value="<c:out value='${ID_SALIDA}'/>"></th>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th height="30">Folio:</th>
      <td><c:out value='${FOLIO}'/><div id="div_folio"></div></td>
    </tr>
    <tr>
      <th height="30">Unidad Administrativa:</th>
      <td><c:out value='${datos.DEPENDENCIA}'/></td>
    </tr>
    <tr>
      <th height="30">Almacén :</th>
      <td><c:out value='${datos.ALMACEN}'/></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">Num. Pedido :</span></th>
      <td><c:out value='${datos.NUM_PED}'/></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">Num. Requisición:</span></th>
      <td><c:out value='${datos.NUM_REQ}'/></td>
    </tr>
    <tr>
      <th width="13%" height="30">Doc. Remisión:</th>
      <td><c:out value='${datos.DOCUMENTO}'/></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">Proveedor :</span></th>
      <td><c:out value='${datos.PROVEEDOR}'/></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">Programa:</span></th>
      <td><c:out value='${datos.N_PROGRAMA_DESC}'/> - <c:out value='${datos.PROG_PRESUP}'/></td>
      </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">Partida :</span></th>
      <td width="87%"><c:out value='${datos.PARTIDA}'/></td>
    </tr>
    <tr>
      <th height="30">*Tipo de Salida:</th>
      <td><span class="styled-select">
        <select name="cbotiposalida" class="comboBox" id="cbotiposalida" style="width:130px">
          <option value="0">[Seleccione]</option>
          <c:forEach items="${tiposSalidas}" var="item" varStatus="status"> <option value='<c:out value="${item.ID_TIPO_ENTRADA}"/>' 
              
            <c:if test='${item.ID_TIPO_ENTRADA==TIPO_SALIDA}'> selected </c:if>
            >
            <c:out value='${item.DESCRIPCION}'/>
            </option>
          </c:forEach>
        </select>
      </span></td>
    </tr>
    <tr>
      <th height="30">*Fecha de salida:</th>
      <td><input name="txtfecha" type="text" class="input" style="width:130px" id="txtfecha"  value="<c:out value='${FECHA_ENTREGA}'/>" ></td>
    </tr>
    <tr>
      <th height="25">*Concepto :</th>
      <td><textarea name="txtconcepto" cols="70" rows="3" style="width:400px" class="textarea" id="txtconcepto"><c:out value='${CONCEPTO}'/></textarea></td>
    </tr>
    <tr>
      <th height="15">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th height="15">&nbsp;</th>
      <td><table width="260" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="130" align="center"><div class="buttons tiptip">
          <div class="buttons tiptip">
            <button name="cmdcerrar" id="cmdcerrar" type="button" class="button red middle"><span class="label" style="width:100px">Cerrar</span></button>
          </div>
        </div></td>
        <td width="130"><button name="cmdguardar" id="cmdguardar" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span> </button></td>
      </tr>
    </table>
    </td>
  </tr>
</table>
</div>  
<div id="fragment-detalles" align="left">
	<table width="100%" border="0"  align="left" cellpadding="0" cellspacing="0" class="listas" id="listasConceptos">
        <thead>
          <tr >
            <th height="25" width="5%"  align="center"><img src="../../imagenes/cross.png" alt="Eliminar conceptos seleccionados" width="16" height="16" onClick="eliminarConceptos()" style="cursor:pointer"></th>
            <th width="6%" align="center">Cantidad solic.</th>
            <th width="6%" align="center">Cantidad disp.</th>
            <th width="12%" align="center">Unidad de medida</th>
            <th width="44%" align="center">Descripci&oacute;n</th>
            <th width="13%" align="center">Familia</th>
            <th width="11%" align="center"> Precio Unitario</th>
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
            <td width="13%" height="25" align="right" bgcolor="#FFFFFF"><strong>Subtotal:&nbsp;</strong></td>
            <td width="11%" align="right" bgcolor="#FFFFFF"><div id="div_subtotal">$0.00&nbsp;</div></td>
            </tr>
          <tr>
            <td align="center" colspan="5" height="20" bgcolor="#FFFFFF"></td>
            <td align="right" bgcolor="#FFFFFF">Descuento:</td>
            <td align="right" bgcolor="#FFFFFF"><span style="background:#FFF">
              <input type="text" id="txtdescuento" class="input" style="width:95%; text-align:right; padding-right:3px" onKeyPress="return keyNumbero(event);" value='<fmt:formatNumber value="${DESCUENTO}"  pattern="###,###,###.00" />' onBlur="getTotales()"/>
            </span></td>
            </tr>
          <tr>
            <td align="center" colspan="5" height="20" bgcolor="#FFFFFF"></td>
            <td align="right" bgcolor="#FFFFFF"><span style="border-left:none; background:#FFF">
              <select name="cboiva" class="comboBox" id="cboiva" onChange="getTotales();">
                <option value="0" <c:if test="${TIPO_IVA==0}"> selected</c:if>>Sin I.V.A</option>
                <option value="1" <c:if test="${TIPO_IVA==1}">selected</c:if>>Con 16% automatico de I.V.A</option>
                <option value="2" <c:if test="${TIPO_IVA==2}">selected</c:if>>I.V.A Personalizado</option>
              </select>
            </span></td>
            <td align="right" bgcolor="#FFFFFF"><span style="background:#FFF">
              <input type="text" id="txtiva" class="input" style="width:95%; text-align:right; padding-right:3px" value="<c:out value='${IVA}'/>"  onKeyPress="return keyNumbero(event);" onBlur="getTotalesMasIva()"/>
            </span></td>
            </tr>
          <tr id="tr_total">
            <td align="center" colspan="5" height="20" bgcolor="#FFFFFF"></td>
            <td align="right" bgcolor="#FFFFFF" height="25"><strong>Total:</strong>&nbsp;</td>
            <td align="right" bgcolor="#FFFFFF"><div id="div_total"><strong>$ <fmt:formatNumber value="${TOTAL}"  pattern="###,###,###.00" /></strong>&nbsp;</div></td>
            </tr>
            
      </table>
</div>
</div>
</td>
</tr>
</table>

</form>
</body>
</html>
