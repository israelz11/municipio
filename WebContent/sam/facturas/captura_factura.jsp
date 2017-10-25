<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Capturar Factura</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link rel="stylesheet" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />	
 <link rel="stylesheet" href="../../include/css/sweetalert2.min.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/jquery.form.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorFacturasRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>
<script type="text/javascript" src="../../dwr/util.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="captura_factura.js?x=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="../../include/js/otros/productos.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<script type="text/javascript" src="../../include/js/sweetalert2.min.js"></script>
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
	color: #06C;
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
</head>
<body>
<br />
  <table width="95%" align="center"><tr><td><h1>Facturas - Captura de nueva factura</h1></td></tr></table>  
<form method="post" enctype="multipart/form-data" name="frmEntrada" id="frmEntrada" >
  <table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
    <td align="center">
<div id="tabuladores">
  <ul>
    <li><a href="#fragment-facturas">Encabezado</a></li>
    <li><a href="#fragment-movimientos">Movimientos</a></li>
    <li><a href="#fragment-retenciones">Retenciones</a></li>
    <li ><a href="#fragment-vales">Vales</a></li>
  </ul>

  <div id="fragment-facturas" align="left">
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" >
    <tr>
      <td width="11%" class="field_label">&nbsp;</td>
      <td width="89%" align="left">
        <input type="hidden" id="CVE_FACTURA" name="CVE_FACTURA"  value="${factura.CVE_FACTURA}"/></td>
    </tr>
    <tr>
      <th height="30" class="field_label">Unidad Administrativa:</th>
      <td>
        <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
          <c:out value="${nombreUnidad}"/>
          <input type="hidden" id="cbodependencia" name="cbodependencia" value="<c:out value='${idUnidad}'/>">
          </sec:authorize>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
          <div class="styled-select">
            <select name="cbodependencia" id="cbodependencia" style="width:400px">
              <option value="0">[Seleccione]</option>
              <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
                <option value='<c:out value="${item.ID}"/>' 
                            <c:if test='${item.ID==idUnidad}'> selected </c:if>>
                <c:out value='${item.DEPENDENCIA}'/>
                </option>
                </c:forEach>
              </select>
            </div>
          </sec:authorize> 
        </td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;*A partir de presupuesto:</th>
      <td align="left" valign="middle"><div class="styled-select">
        <select name="cbotipodocumento" class="comboBox" id="cbotipodocumento" style="width:170px">
          <option value="0" selected>[Seleccione]</option>
          <option value="4" <c:if test='${factura.CVE_CONTRATO!=null}'>selected</c:if>>CONTRATO</option>
          <option value="1" <c:if test='${factura.CVE_REQ!=null}'>selected</c:if>>O.S. y O.T.</option>
          <option value="2" <c:if test='${factura.CVE_PED!=null}'>selected</c:if>>PEDIDO</option>
          <!-- <option value="3" <c:if test='${factura.CVE_VALE!=null}'>selected</c:if>>VALE</option> -->
          <!-- <option value="5" <c:if test='${factura.CVE_REQ==null&&factura.CVE_CONTRATO==null&&factura.CVE_VALE==null&&factura.CVE_PED==null}'>selected</c:if>>LIBRE</option> -->
          <option value="5" <c:if test='${factura.CVE_REQ==null&&factura.CVE_CONTRATO==null&&factura.CVE_PED==null}'>selected</c:if>>LIBRE</option>
        </select>
      </div></td>
    </tr>
    <tr>
      <th height="30" class="field_label">*Tipo factura:</th>
      <td align="left" valign="middle">
      <div class="styled-select">
      	 <select name="cbotipoFactura" id="cbotipoFactura" style="width:170px">
              <option value="0">[Seleccione]</option>
              <c:forEach items="${tipoFacturas}" var="item" varStatus="status"> 
                <option value='<c:out value="${item.ID_TIPO_FAC}"/>' 
                 <c:if test='${item.ID_TIPO_FAC==factura.ID_TIPO}'> selected </c:if>>
                <c:out value='${item.DESCRIPCION}'/>
                </option>
                </c:forEach>
              </select>
             
       </div>
       <!-- <input name="CVE_DOC" type="hidden" id="CVE_DOC" value="${factura.CVE_PED}${factura.CVE_REQ}${factura.CVE_VALE}${factura.CVE_CONTRATO}"/> -->
       <input name="CVE_DOC" type="hidden" id="CVE_DOC" value="${factura.CVE_PED}${factura.CVE_REQ}${factura.CVE_CONTRATO}"/> 
       <input name="CLV_BENEFI" type="hidden" id="CLV_BENEFI" value="${factura.CLV_BENEFI}"/>
       <input name="ID_ENTRADA" type="hidden" id="ID_ENTRADA" value="<c:out value='${factura.ID_ENTRADA}'/>"/>
       <input name="ID_PROYECTO" type="hidden" id="ID_PROYECTO" value="${factura.ID_PROYECTO}"/>
       <input name="CLV_PARTID" type="hidden" id="CLV_PARTID" value="${factura.CLV_PARTID}"/>
      </td>
    </tr>
    
    <tr id="tr_NumDocumento">
      <th height="30" class="field_label">*Numero de documento:</th>
      <td align="left" valign="middle"><input name="txtdocumento" type="text" class="input" id="txtdocumento" maxlength="30"  style="width:170px" value="${factura.NUM_REQ}${factura.NUM_PED}${factura.NUM_VALE}${factura.NUM_CONTRATO}">
         <img src="../../imagenes/search_16.png" alt="" align="absmiddle" id="img_movimiento"   style="cursor:pointer" title="Buscar documento"/>
         <img src="../../imagenes/cross.png" alt="" align="absmiddle" width="16" height="16" id="img_detele" style="cursor:pointer">
         </td>
    </tr>
    <tr id="trEntrada">
      <th height="30" class="field_label">Numero de entrada:</th>
      <td align="left" valign="middle"><div id="div_num_entrada" align="left">${factura.FOLIO_ENTRADA}</div></td>
    </tr>
    <tr id="tr_TotalDocumento">
      <th height="30" class="field_label">Total documento:</th>
      <td align="left" valign="middle"><div id="div_total_entrada" align="left">$<fmt:formatNumber value="${factura.TOTAL_DOC}"  pattern="#,###,###,##0.00" /></div></td>
    </tr>
    
    
    <tr>
      <th height="30" class="field_label">Beneficiario:</th>
      <td align="left" valign="middle">
      	  <div id="div_beneficiario" align="left">${factura.NCOMERCIA}</div>
          <div id="div_benaficiarioFijo">
          	<input type="text" id="txtprestadorservicio" class="input" style="width:390px" value="<c:out value='${factura.NCOMERCIA}'/>"/>
          </div>
      </td>
    </tr>
    
    
    <tr>
      <th height="36" class="field_label">Fecha devengado:</th>
      <td align="left" valign="middle"><fmt:formatDate pattern="dd/MM/yyyy" value="${FECHA_DEVENGADO}" /></td>
    </tr>
    <tr>
      <th height="36" class="field_label">*Fecha recepción:</th>
      <td align="left" valign="middle"><input type="text" id="txtfecha"  name="txtfecha" class="input" style="width:100px" maxlength="10" value="${factura.FECHA_DOCUMENTO}" /></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;*N&uacute;mero de Factura:</th>
      <td align="left" valign="middle"><input name="txtnumfactura" type="text" class="input" id="txtnumfactura" maxlength="20" style="width:170px" value="<c:out value='${factura.NUM_FACTURA}'/>" /></td>
    </tr>
    <tr>
      <th height="47" class="field_label">&nbsp;Notas:</th>
      <td align="left"><textarea name="txtobservacion" cols="70" rows="3" style="width:400px" class="textarea" id="txtobservacion"><c:out value='${factura.NOTAS}'/></textarea></td>
    </tr>
    <tr id="tr_file">
      <th height="47" class="field_label">Archivo: </th>
      <td align="left"><input type="file" class="input-file" id="archivo" name="archivo" style="width:445px" /></td>
    </tr>
    <tr>
      <th height="47" class="field_label">&nbsp;</th>
      <td align="left">
      
      <table width="80%" border="0"  align="left" cellpadding="0" cellspacing="0" class="listas" id="listasArchivo">
        <thead>
          <tr >
            <th width="59%" height="20"  align="center">Archivo</th>
            <th width="16%"  align="center">Tamaño</th>
            <th width="14%"  align="center">Tipo</th>
            <th width="6%"  align="center">&nbsp;</th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table>
      
      </td>
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
            <td width="130"><button name="cmdcerrar" id="cmdcerrar" <c:if test='${factura.CVE_FACTURA==NULL}'>disabled</c:if> type="button" class="button red middle"><span class="label" style="width:100px">Cerrar</span></button></td>
            <td width="130"><button name="cmdguardar" id="cmdguardar" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button></td>
          </tr>
        </table></td>
    </tr>
    <tr >
      <td height="25"   colspan="2" class="field_label"  >&nbsp;</td>
      </tr> 
  </table>
</div>
<br>

  <div id="fragment-movimientos">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="formulario">
              <tr>
                <th width="17%" height="17">&nbsp;</th>
                <td>&nbsp;</td>
                <th>&nbsp;</th>
                <td>&nbsp;</td>
                <td width="50%">&nbsp;</td>
              </tr>
              <tr>
                <th height="30">Presupuesto de la Unidad</th>
                <td colspan="4">
                <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
                	<input type="hidden" id="cbUnidad2" name="cbUnidad2" value="<c:out value='${idUnidad}'/>">
          		</sec:authorize>
                <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
                    <div class="styled-select">
                    <select name="cbUnidad2" class="comboBox" id="cbUnidad2" style="width:445px">
                    <option value="0">[Seleccione]</option>
                      <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">
                        <option value="<c:out value='${item.ID}'/>" <c:if test="${item.ID==idUnidad}"> selected </c:if>>
                        <c:out value="${item.DEPENDENCIA}"/>
                        </option>
                        </c:forEach>
                    </select>
                    </div>
                </sec:authorize>
                </td>
              </tr>
              <tr >
                <th height="30">Mes: </th>
                <td colspan="2">
                  <div class="styled-select">
                    <select name="cbomes" class="comboBox" id="cbomes" style="width:140px">
                      <option value="0">[Seleccione]</option>
                      <c:forEach items="${mesesActivos}" var="item" varStatus="status">
                        <option value="<c:out value='${item.ID_MES}'/>" >
                        <c:out value="${item.DESCRIPCION}"/>
                        </option>
                      </c:forEach>
                    </select>
                  </div>
                </td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr id="tr_ProgramaPartidaPresupuesto">
                <th height="30">*Programa:</th>
                <td width="5%"><input name="txtproyecto" type="text" class="input" id="txtproyecto"  value='' size="20" maxlength="6">
                <input type="hidden" id="ID_PROYECTO" value="0"/>
                </td>
                <th width="6%">*Partida:</th>
                <td width="22%"><input name="txtpartida" type="text" class="input" id="txtpartida"  value='' size="20" maxlength="4"  >
                <img src="../../imagenes/search_16.png" alt="Mostrar presupuesto" name="img_presupuesto" onclick="muestraPresupuesto()"  id="img_presupuesto" style="cursor:pointer" align="absmiddle"/></td>
                <td>&nbsp;</td>
              </tr>
              <tr id="tr_ProyectoPartida">
                <th height="30">*Programa/Partida:</th>
                <td colspan="4"><span class="styled-select">
                  <select name="cboproyectopartida" class="comboBox" id="cboproyectopartida" style="width:445px">
                    <c:forEach items="${ProyectoPartida}" var="item" varStatus="status">
                      <option value="<c:out value='${item.ID_PROYECTO}'/>,<c:out value='${item.N_PROGRAMA}'/>,<c:out value='${item.CLV_PARTID}'/>" >
                        [<c:out value='${item.ID_PROYECTO}'/>]<c:out value='${item.N_PROGRAMA}'/> - <c:out value='${item.CLV_PARTID}'/>
                      </option>
                    </c:forEach>
                  </select>
                </span></td>
              </tr>
              <tr>
                <th height="30">Presupuesto:</th>
                <td><input name="txtpresupuesto" type="text" class="input" id="txtpresupuesto"  value="" size="20" readonly></td>
                <th>Disponible:</th>
                <td colspan="2"><input name="txtdisponible" type="text" class="input" id="txtdisponible"  value="" size="20" readonly></td>
              </tr>
              <tr>
                <th height="30">Observaciones:</th>
                <td colspan="4"><textarea  name="txtdetalle"  id="txtdetalle"  cols="80" rows="4" wrap="virtual" class="textarea" maxlength="500" style="width:445px"></textarea></td>
              </tr>
              <tr>
                <th height="30">*Importe:</th>
               
                <td colspan="4">
                  <table width="380" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><input name="txtimporteDet" type="text" class="input" id="txtimporteDet" onkeypress="return keyNumbero(event); " />
                      <input type="hidden" id="ID_DETALLE" value="0"/>
                    <input type="hidden" id="IMPORTE_TOTAL" value="0"/></td>
                    <td><button name="cmdagregar" id="cmdagregar" title="Limpia valores para continuar con nuevo Vale." type="button" class="button blue middle"><span class="label" style="width:80px">Agregar</span></button></td>
                    <td>
                    <button name="cmdnuevoconcepto" id="cmdnuevoconcepto" onClick="limpiar()" title="Limpia valores para continuar con nuevo Vale." type="button" class="button red middle"><span class="label" style="width:80px">Nuevo</span></button>
                    </td>
                  </tr>
                </table></td>
              </tr>
              <tr>
                <th height="20">&nbsp;</th>
                <td colspan="4">&nbsp;</td>
              </tr>
              <tr>
                <td height="30" colspan="5" align="left"><table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listas" id="listaDetalles">
                  <thead>
                    <tr >
                      <th width="4%" height="20"  align="center"><img src="../../imagenes/cross.png" width="16" height="16" onclick="eliminarDetalles()" style='cursor: pointer;' /></th>
                      <th width="28%"  align="center">Unidad Administrativa</th>
                      <th width="31%"  align="center">Notas</th>
                      <th width="12%"  align="center">Programa</th>
                      <th width="10%" align="center">Partida</th>
                      <th width="12%"  align="center">Importe</th>
                      <th width="3%"  align="center">&nbsp;</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table></td>
              </tr>
              <tr>
                <td height="20" colspan="5" align="left"><table width="482" border="0" align="right">
                  <tr>
                    <td width="99">&nbsp;</td>
                    <th width="184" height="30" class="field_label">IVA:</th>
                    <td width="185" align="left" class="formulario" ><input name="txtiva" type="text" class="input" id="txtiva" maxlength="20" style="width:170px" value="<fmt:formatNumber value='${factura.IVA}' pattern='##########.00' />"/></td>
                    </tr>
                  <tr>
                    <td><button name="cmdguardar2" id="cmdguardar2" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button></td>
                    <th height="30" class="field_label">Subtotal:</th>
                    <td align="left" class="formulario"><input name="txtsubtotal" type="text" class="input" id="txtsubtotal" maxlength="10" style="width:170px" value="<fmt:formatNumber value='${factura.SUBTOTAL}' pattern='##########.00' />"/></td>
                    </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <th height="30" class="field_label">Total:</th>
                    <td align="left" class="formulario"><div id="div_total">0.00</div></td>
                    </tr>
                </table></td>
          </tr>
              <tr>
                <td height="20" colspan="5" align="left">&nbsp;</td>
              </tr>
          </table>
  </div>

<br>
<div id="fragment-retenciones" align="left"><br>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <th height="13">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th width="12%" height="33">*Retencion:</th>
      <td width="88%"><div class="styled-select">
        <select name="retencion" class="comboBox" id="retencion" style="width:500px">
          <c:forEach items="${tipoRetenciones}" var="item" varStatus="status">
            <option value="<c:out value='${item.CLV_RETENC}'/>">
              <c:out value="${item.RETENCION}"/>
              </option>
          </c:forEach>
        </select>
      </div>
        <input type="hidden" name="idRetencion" id="idRetencion"></td>
    </tr>
    <tr>
      <th height="31">*Importe:</th>
      <td><input name="importeRetencion" type="text" class="input" id="importeRetencion" onkeypress="return keyNumbero( event );"></td>
    </tr>
    <tr>
      <th height="56">&nbsp;</th>
      <td><table width="400" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><div class="buttons tiptip">
            <button name="cmdNuevaRetencion" id="cmdNuevaRetencion" type="button" class="button red middle"><span class="label" style="width:150px">Nueva Retención</span></button>
          </div></td>
          <td><div class="buttons tiptip">
            <button name="cmdGuardarRetencion" id="cmdGuardarRetencion" type="button" class="button blue middle"><span class="label" style="width:150px">Guardar Retención</span></button>
          </div></td>
        </tr>
      </table></td>
    </tr>
    <tr>
      <th>&nbsp;</th>
      <td><table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listas" id="listasRetenciones">
        <thead>
          <tr >
            <th width="3%" height="20"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminarRetencion()" style='cursor: pointer;'></th>
            <th width="67%"  align="center">Retención</th>
            <th width="23%" align="center">Importe</th>
            <th width="7%" align="center"></th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table></td>
    </tr>
    <tr>
      <th>&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
  </table>
  <br>
  <br>
</div>

  <div id="fragment-vales">
            <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="formulario">
        <tr>
          <td colspan="5"  align="center">&nbsp;</td>
        </tr>
        <tr >
          <td colspan="5"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="formulario">
              <tr>
                <th height="30">*Proyecto Cuenta:</th>
                <td>
                <div class="styled-select">
                <select name="cboproyectocuenta" class="comboBox" id="cboproyectocuenta" onChange="cargarVales(0)" style="width:222px">
                </select>
                </div>
                  <input type="hidden" name="idVale" id="idVale"><input type="hidden" name="CVE_VALE" id="CVE_VALE" value="0">
                  </td>
              </tr>
              <tr>
                <th height="30">*Número de Vale:</th>
                <td><div class="styled-select">
                <select name="cboVales" class="comboBox" id="cboVales"  style="width:222px">
                    </select>
                    </div>
                    </td>
              </tr>
              <tr>
                <th height="30">*Importe:</th>
                <td><input name="txtimporteVale" type="text" class="input" id="txtimporteVale" onkeypress=" return keyNumbero( event );"  style="width:222px"></td>
              </tr>
              <tr>
                <th width="12%" height="31">&nbsp;</th>
                <td width="88%"><table width="400" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                      <td><div class="buttons tiptip">
                        <button name="cmdNuevoVale" id="cmdNuevoVale" onClick="lipiarVale();" type="button" class="button red middle"><span class="label" style="width:150px">Nuevo Vale</span></button>
                      </div></td>
                      <td><div class="buttons tiptip">
                        <button name="cmdGuardarVale" id="cmdGuardarVale" onClick="guardarVale();" type="button" class="button blue middle"><span class="label" style="width:150px">Guardar Vale</span></button>
                      </div></td>
                    </tr>
              </table>
                  </td>
              </tr>
              <tr>
                <th>&nbsp;</th>
                <td>&nbsp;</td>
              </tr>
          </table></td>
        </tr>
        <tr><td colspan="5" >
          <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listas" id="listasVales">
            <thead>
              <tr >
                <th width="7%" height="20"  align="center"><img src="../../imagenes/cross.png" alt="" width="16" height="16" onClick="eliminarVales()" style="cursor:pointer"></th>
                <th width="20%"  align="center">Proyecto</th>
                <th width="21%" align="center">Partida</th>
                <th width="22%"  align="center">Numero Vale</th>
                <th width="22%"  align="center">Importe Comprobado</th>
                <th width="8%"  align="center">&nbsp;</th>
                </tr>
              </thead>
            <tbody>
              </tbody>
            </table>
          </td>
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
