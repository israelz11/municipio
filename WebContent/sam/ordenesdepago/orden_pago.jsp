<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Captura de Ordenes de pago</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link href="../../include/css/estilosam.css" rel="stylesheet" type="text/css" />
<link href="../../include/js/autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<link type="text/css" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" rel="stylesheet" />	
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorOrdenPagoRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script>  
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-ui-1.7.3.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/jquery.form.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="orden_pago.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
<!--<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">-->
<!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->

<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>

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
</head>
<body>
<form name="forma" id="forma" method="post" enctype="multipart/form-data">
<input name="id_orden" type="hidden"  id="id_orden" size="8" maxlength="6" readonly value="" />
<input name="cve_op" type="hidden"  id="cve_op" size="8" value="<c:out value='${cve_op}'/>" />
<input name="accion" type="hidden"  id="accion" size="8" value="<c:out value='${accion}'/>" />
<input type="hidden" name="estatus" id="estatus" value="-1">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" name="mes" id="mes">
<sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_CAPTURAR_ORDENES_CON_MULTIPLES_PEDIDOS"> 
<input type="hidden" name="multipliesPed" id="multipliesPed" value="NO">
</sec:authorize>
<sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CAPTURAR_ORDENES_CON_MULTIPLES_PEDIDOS">
<input type="hidden" name="multipliesPed" id="multipliesPed" value="SI">
</sec:authorize>
<sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_CAPTURAR_ORDENES_CON_MULTIPLES_OT">
<input type="hidden" name="multipliesOt" id="multipliesOt" value="NO" >
</sec:authorize>
<sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CAPTURAR_ORDENES_CON_MULTIPLES_OT">
<input type="hidden" name="multipliesOt" id="multipliesOt" value="SI" >
</sec:authorize>
<input type="hidden" name="id_ordenDetalle" id="id_ordenDetalle">
<table width="95%" align="center"><tr><td><h1>Ordenes de Pago - Captura de Ordenes de Pago</h1></td></tr></table>          
<table width="95%" align="center"><tr><td>          
<div id="listaOrdenesPendientes"  align="center">
      <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="formulario">
        <tr >
          <th colspan="3">&nbsp;</th>
          </tr>
        <tr >
          <th width="15%">Unidad administrativa:</th>
          <td width="55%" align="left">
       <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      <c:out value="${nombreUnidad}"/><input type="hidden" name="cbUnidad" id="cbUnidad" value='<c:out value="${idUnidad}"/>' />
      </sec:authorize>
       <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
       <div class="styled-select">
        <select name="cbUnidad" class="comboBox" id="cbUnidad" onChange="llenarTablaDeOrdenes();" style="width:445px">
            <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">                  
              <option value="<c:out value="${item.ID}"/>" <c:if test="${item.ID==idUnidad}"> selected </c:if> ><c:out value="${item.DEPENDENCIA}"/></option>
            </c:forEach>
          </select>
         </div>
       </sec:authorize></td>
          <td width="30%" align="left">
          <!-- MOSTRAR Y OCULTAR ESTE ELEMENTO A CONVENIENCIA -->
          <div style="display:">
          		<button name="btnNuevaOp" id="btnNuevaOp" onClick="nuevaOp();limpiarForma();" type="button" class="button blue middle"><span class="label" style="width:150px">Nueva Orden de Pago</span></button>
          </div>
          </td>
        </tr>
        <tr>
          <th colspan="3">&nbsp;</th>
          </tr>
        <tr>
          <td colspan="3" >
        <table border="0" cellpadding="0" cellspacing="0" class="listas" width="100%" id="listaOrdenes">
        <thead>
        <tr>
          <th width="5%" height="20">Numero</th>
          <th width="17%" >Tipo</th>
          <th width="10%" >Fecha</th>
          <th width="49%" >Concepto</th>
          <th width="16%" >Estado</th>
          <th width="3%" >Opc.</th>
        </tr>
        </thead>
         <tbody>
         </tbody>
        </table>
        </td>
        </tr>
      </table>
</div>
<div id="tabsOrdenesEnca"  >
<div id="tabsOrdenes"  >
  <ul>
    <li ><a href="#tabsCabe">Cabecera</a></li>
    <li ><a href="#tabsCon">Conceptos</a> </li>
    <li ><a href="#tabsRet">Retenciones</a></li>
    <li ><a href="#tabsDoc">Anexos</a></li>
    <li ><a href="#tabsVal">Vales</a></li>
  </ul>
    <div id="tabsCabe" >
      <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="formulario">
              <tr >
                <th height="14">&nbsp;</th>
                <td ></td>
              </tr>
              <tr >
                <th width="14%" height="30">No. Orden:</th>
                <td width="86%" ><div id="NoOrden">&nbsp;</div></td>
              </tr>
              <tr >
                <th height="30">*Tipo:</th>
                <td>
                <div class="styled-select">
                <select name="xTipo" class="comboBox" id="xTipo"  onChange="cambiarModoDetalle()" style="width:222px">
                  <c:forEach items="${tipoDocumentosOp}" var="item" varStatus="status">
                    <option value="<c:out value='${item.ID_TIPO_ORDEN_PAGO}'/>">
                      <c:out value="${item.DESCRIPCION}"/>
                      </option>
                    </c:forEach>
                  </select>
                  </div>
                  </td>
              </tr>
        <tr >
          <th height="30">*Tipo de gasto:</th>
          <td>
          <div class="styled-select">
          <select name="tipoGasto" class="comboBox" id="tipoGasto" style="width:445px">
                  <c:forEach items="${tipoGastos}" var="item" varStatus="status">                  
                    <option value="<c:out value='${item.ID}'/>"><c:out value="${item.RECURSO}"/></option>
                    </c:forEach>
                  </select>
          </div>        
          </td>
        </tr>
        <tr id="fila_contrato">
          <th height="30">Número de Contrato:</th>
          <td  ><input name="txtnumcontrato" disabled  type="text"  class="input" id="txtnumcontrato" value="" maxlength="30" style="width:222px; background:#C0C0C0" onBlur="getProveedor()" />
            <img src="../../imagenes/buscar.png" alt="Mostrar presupuesto" name="img_contrato" width="22" height="22" id="img_contrato" style="cursor:pointer" align="absmiddle"/>
            <img src="../../imagenes/cross2.png" id="img_quitar_contrato" width="16" height="16" alt="Quitar contrato" title="Quitar contrato" align="absmiddle" style="cursor:pointer">
            <input name="CVE_CONTRATO" type="hidden"  id="CVE_CONTRATO" size="8" maxlength="6" readonly value="0" />
            <input name="CLV_PARBIT" type="hidden"  id="CLV_PARBIT" size="8" maxlength="6" readonly value="" />
            <input name="CID_PROYECTO" type="hidden"  id="CID_PROYECTO" size="8" maxlength="6" readonly value="" />
            <input name="CCLV_PARTID" type="hidden"  id="CCLV_PARTID" size="8" maxlength="6" readonly value="" />
            <input name="CCLV_BENEFI" type="hidden"  id="CCLV_BENEFI" size="8" maxlength="6" readonly value="" />
            </td>
        </tr>
        <tr >
          <th height="30">*Periodo:</th>
          <td>
          <div class="styled-select">
          <select name="cbomes" class="comboBox" id="cbomes" style="width:111px">
            <c:forEach items="${meses}" var="item" varStatus="status">
              <option value="<c:out value="${item.mes}"/>">
              <c:out value="${item.DESCRIPCION}"/>
            </c:forEach>
          </select>
          </div>
          </td>
        </tr>
        <tr >
          <th height="30">*Fecha:</th>
          <td  ><input name="fecha" type="text" class="input" id="fecha" value="" style="width:111px" maxlength="10"/></td>
        </tr>
        
        <tr>
          <th height="30">Genera IVA:</th>
          <td ><input name="xImporteIva"  type="text"  class="input" id="xImporteIva" value="0" maxlength="30" onkeypress=" return keyNumbero( event );" style="width:111px" /></td>
        </tr>
        <tr >
          <th height="30">*Beneficiario:</th>
          <td ><input name="xBeneficiario" id="xBeneficiario"  type="text" class="input" value="" style="width:445px" maxlength="50" />
              <input name="xClaveBen" type="hidden"  id="xClaveBen" size="8" maxlength="6" readonly value="1225" />
            <a href="javascript:buscarBeneficiarioEmer();"> <img src="../../imagenes/buscar.png" alt="Bsqueda del Beneficiario" width="22" height="22" border="0" style="display:none" /></a></td>
        </tr>
        <tr >
          <th height="30">Concurso:</th>
          <td ><input name="xConcurso" type="text"  class="input"   id="xConcurso" value="" style="width:111px" maxlength="30" /></td>
          </tr>
        <tr >
          <th height="30">Reembolso al Fondo Fijo:</th>
          <td><input name="reembolso" type="checkbox" id="reembolso" value="S"></td>
        </tr>
        <tr >
          <th height="30">*Concepto:</th>
          <td><textarea  name="xNota"  id="xNota"  cols="80" rows="4" wrap="virtual" class="textarea" maxlength="500" style="width:445px"></textarea></td>
        </tr>
        <tr >
          <td height="35"  align="center" >&nbsp;</td>
          <td height="40" align="left" ><table width="500" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td><div class="buttons tiptip">
					<button name="cmdregresar" id="cmdregresar" onClick="regresar()" title="Volver al inicio para crear nueva Orden de Pago por unidad adm." type="button" class="button red middle"><span class="label" style="width:100px">Regresar</span></button>
                </div></td>
                <td><div class="buttons tiptip">
					<button name="xGrabar" id="xGrabar" onClick="limpiarForma()" title="Limpia valores para continuar con nueva Orden de Pago." type="button" class="button red middle"><span class="label" style="width:100px">Nuevo</span></button>
                </div></td>
                <td><div class="buttons tiptip">
					<button name="btnCerrar" id="btnCerrar" onClick="cerrarOrden()" title="Cierra para comprometer el importe de la Orden de Pago." type="button" class="button red middle"><span class="label" style="width:100px">Cerrar</span></button>
                </div></td>
                <td><div class="buttons tiptip">
					<button name="btnGrabar" id="btnGrabar" onClick="guardar()" title="Guardar la informacion general de Orden de Pago." type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button>
                </div></td>
              </tr>
          </table></td>
        </tr>
      </table>
    </div>
    <div id="tabsCon">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="formulario">
              <tr>
                <th width="15%" height="17">&nbsp;</th>
                <td>&nbsp;</td>
                <th>&nbsp;</th>
                <td>&nbsp;</td>
                <td width="46%">&nbsp;</td>
              </tr>
              <tr id="filaUnidadProyPart">
                <th height="30">Presupuesto de la Unidad</th>
                <td colspan="4">
                <div class="styled-select">
                <select name="cbUnidad2" class="comboBox" id="cbUnidad2" onChange="" style="width:445px">
                <option value="0">[Seleccione]</option>
                  <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">
                    <option value="<c:out value='${item.ID}'/>" <c:if test="${item.ID==idUnidad}"> selected </c:if>>
                    <c:out value="${item.DEPENDENCIA}"/>
                    </option>
                    </c:forEach>
                </select>
                </div>
                </td>
                </tr>
              <tr id="filaSelectVale">
                <th height="30">Vale:</th>
                <td colspan="3"><input name="txtvale"  type="text" size="20"  class="input" id="txtvale" value="" maxlength="30" onBlur="getProveedor()" />
                  <img src="../../imagenes/search_16.png" alt="Mostrar presupuesto" name="img_vale" id="img_vale" style="cursor:pointer" align="absmiddle"/>&nbsp;<input type="hidden" id="CVE_VALE" value="0"/></td>
                <td>&nbsp;</td>
              </tr>
              <tr id="filaDetProyPart">
                <th height="30">*Programa:</th>
                <td width="12%"><input name="txtproyecto" style="text-align:right" type="text" class="input" id="txtproyecto"  value='' size="20" maxlength="6">
                <input type="hidden" id="ID_PROYECTO" value="0"/></td>
                <th width="6%">*Partida:</th>
                <td width="21%"><input name="txtpartida" type="text" style="text-align:right" class="input" id="txtpartida"  value='' size="20" maxlength="4" >
                  <img src="../../imagenes/search_16.png" alt="Mostrar presupuesto" name="img_presupuesto"  id="img_presupuesto" style="cursor:pointer" align="absmiddle"/></td>
                <td>&nbsp;</td>
              </tr>
              <tr id="filaDetPres">
                <th height="30">Presupuesto:</th>
                <td><input name="txtpresupuesto" type="text" style="text-align:right" class="input" id="txtpresupuesto"  value="" size="20" readonly></td>
                <th>Disponible:</th>
                <td colspan="2"><input name="txtdisponible" type="text" style="text-align:right" class="input" id="txtdisponible"  value="" size="20" readonly></td>
              </tr>
              <tr id="filaDisponibleVale">
                <th height="30">Disponible total en Vale:</th>
                <td><input name="txtdisponiblevale" type="text" class="input" id="txtdisponiblevale" style="text-align:right;"  value="" size="20" readonly></td>
                <td align="right"><strong>Comprobado:</strong></td>
                <td><input name="txtcomprobadovale" type="text" style="text-align:right;" class="input" id="txtcomprobadovale"  value="" size="20" readonly></td>
                <td>&nbsp;</td>
              </tr>
              <tr id="filaDetNota">
                <th height="30">Observaciones:</th>
                <td colspan="4"><textarea  name="notaDetalle"  id="notaDetalle"  cols="80" rows="4" wrap="virtual" class="textarea" maxlength="500" style="width:445px"></textarea></td>
              </tr>
              <tr id="filaDetImporte">
                <th height="30">*Importe:</th>
                <td colspan="4"><input name="importeDetalle" type="text" class="input" id="importeDetalle" onkeypress="return keyNumbero(event); "></td>
              </tr>
              <tr>
                <td height="30" colspan="5" align="left">
                <table width="600" border="0" cellspacing="0" cellpadding="0" align="left">
                  <tr>
                    <td width="21%" align="center" id="">&nbsp;</td>
                    <td width="21%" align="center" id="filaGuardar">
                    <div class="buttons tiptip">
						<button name="btnGuardar" id="btnGuardar" onClick="guardarDetalle()" title="Guarda el concepto en la Orden de Pago" type="button" class="button blue middle"><span class="label" style="width:150px">Guardar concepto</span></button>
                	</div>
                    </td>
                    
                     <td width="30%" align="center" id="filaContrato">
                    <div class="buttons tiptip">
						<button name="cmdContrato" id="cmdContrato" onClick="muestraContratosOP()" title="Muestra listado de Contratos para agregar a los conceptos" type="button" class="button blue middle"><span class="label" style="width:180px">Generar a partir de Contrato...</span></button>
                	</div>
                    </td>
                    
                    <td width="30%" align="center" id="filaPedido">
                    <div class="buttons tiptip">
						<button name="btnPedido" id="btnPedido" onClick="getPedidos()" title="Muestra listado de Pedidos disponibles para agregar a los conceptos" type="button" class="button blue middle"><span class="label" style="width:180px">Generar a partir de Pedido...</span></button>
                	</div>
                    </td>
                    
                    <td width="30%" align="center" id="filaFacturas">
                    <div class="buttons tiptip">
						<button name="cmdCargarFactura" id="cmdCargarFactura" onClick="getFacturas()" title="Muestra listado de Facturas disponibles para agregar a los conceptos" type="button" class="button blue middle"><span class="label" style="width:180px">Generar a partir de factura...</span></button>
                	</div>
                    </td>
                    
                    <td width="30%" align="center" id="filaReq">
                         <div class="buttons tiptip">
							<button name="btnPedido" id="btnPedido" onClick="getOrdenesDeTrabajo()" title="Muestra listado de OT/OS disponibles para agregar a los conceptos" type="button" class="button blue middle"><span class="label" style="width:180px">Generar a partir de OT/OS..</span></button>
                		</div>
                    </td>
                    <td width="19%" align="center" id="filaReq">
                    <div class="buttons tiptip">
							<button name="btnNuevo" id="btnNuevo" onClick="limpiarDetalle()" title="Limpia el formulario para un nuevo concepto" type="button" class="button red middle"><span class="label" style="width:150px">Nuevo concepto</span></button>
                	</div>
                    </td>
                    </tr>
                </table></td>
                </tr>
          </table>
      <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listas" id="listasDetallesOrdenes">
        <thead>
        <tr >
          <th width="3%" height="20"  align="center"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminarDetalle()" style='cursor: pointer;'></th>
          <th width="28%"  align="center">Unidad Administrativa</th>
          <th width="26%"  align="center">Notas</th>
          <th width="11%"  align="center">Proyecto</th>
          <th width="10%" align="center">Partida</th>
          <th width="9%"  align="center">Tipo</th>
          <th width="10%"  align="center">Monto</th>
          <th width="3%"  align="center">&nbsp;</th>
        </tr>
          </thead>
         <tbody>
         </tbody>
        </table>        
  </div>
      <div id="tabsRet">
            <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="formulario">
        <tr>
          <td colspan="5"  align="center">&nbsp;</td>
        </tr>
        <tr >
          <td colspan="5"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="formulario">
              <tr>
                <th width="12%" height="33">*Retencion:</th>
                <td width="88%">
                <div class="styled-select">
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
                <td><input name="importeRetencion" type="text" class="input" id="importeRetencion" onkeypress=" return keyNumbero( event );"></td>
              </tr>
              <tr>
                <th height="56">&nbsp;</th>
                <td><table width="400" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><div class="buttons tiptip">
						<button name="cmdNuevaRetencion" id="cmdNuevaRetencion" onClick="" type="button" class="button red middle"><span class="label" style="width:150px">Nueva Retención</span></button>
                	</div></td>
                    <td><div class="buttons tiptip">
						<button name="cmdNuevaRetencion" id="cmdNuevaRetencion" onClick="guardarRetencion()" type="button" class="button blue middle"><span class="label" style="width:150px">Guardar Retención</span></button>
                	</div></td>
                  </tr>
              </table></td>
              </tr>
              <tr>
                <th>&nbsp;</th>
                <td>&nbsp;</td>
              </tr>
          </table></td>
        </tr>
        <tr><td colspan="5" >
          <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listas" id="listasRetenciones">
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
            </table>
          </td>
        </tr>
        </table>
  </div>
      <div id="tabsDoc">
            <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="formulario">
        <tr>
          <td colspan="5"  align="center">&nbsp;</td>
        </tr>
        <tr >
          <td colspan="5"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="formulario">
              <tr>
                <th height="30">*Tipo de movimiento:</th>
                <td>
                <div class="styled-select">
                <select name="tipoMovDoc" class="comboBox" id="tipoMovDoc" style="width:180px">
                  <c:forEach items="${tipoDocumentos}" var="item" varStatus="status">
                    <option value="<c:out value='${item.T_DOCTO}'/>">
                      <c:out value="${item.DESCR}"/>
                      </option>
                  </c:forEach>
                </select>
                </div>
                  <input type="hidden" name="idDocumento" id="idDocumento" value="0"></td>
              </tr>
              <tr>
                <th height="30">*Número:</th>
                <td><input name="numeroDoc" type="text" class="input" id="numeroDoc"  style="width:180px" ></td>
              </tr>
              <tr>
                <th height="63">*Notas:</th>
                <td><textarea  name="notaDoc"  id="notaDoc"  cols="80" rows="4" wrap="virtual" class="textarea" maxlength="500" style="width:445px"></textarea></td>
              </tr>
              <tr>
                <th height="32">Archivo:</th>
                <td><div id="div_archivo"><input type="file" class="input-file" id="archivo" name="archivo" style="width:445px" accept="application/pdf"/></div></td>
              </tr>
              <tr>
                <th width="12%" height="32">&nbsp;</th>
                <td width="88%"><table width="400" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                      <td><div class="buttons tiptip">
                        <button name="cmdNuevoAnexo" id="cmdNuevoAnexo" onClick="" type="button" class="button red middle"><span class="label" style="width:150px">Nuevo Anexo</span></button>
                      </div></td>
                      <td><div class="buttons tiptip">
                        <button name="BorraOs2" id="BorraOs2" type="button" class="button blue middle"><span class="label" style="width:150px">Guardar Anexo</span></button>
                      </div></td>
                    </tr>
                </table></td>
                </tr>
              <tr>
                <th>&nbsp;</th>
                <td>&nbsp;</td>
              </tr>
          </table></td>
        </tr>
        <tr><td colspan="5" >
          <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listas" id="listasDocumentos">
            <thead>
              <tr >
                <th width="3%" height="20"  align="center"><img src="../../imagenes/cross.png" alt="" width="16" height="16" onClick="eliminarDocumentos()" style="cursor:pointer"></th>
                <th width="11%"  align="center">Tipo Movimiento</th>
                <th width="10%" align="center">Número</th>
                <th width="40%"  align="center">Nota</th>
                <th width="17%"  align="center">Archivo</th>
                <th width="7%"  align="center">Tamaño</th>
                <th width="8%"  align="center">Tipo</th>
                <th width="4%"  align="center">&nbsp;</th>
                </tr>
              </thead>
            <tbody>
              </tbody>
            </table>
          </td>
        </tr>
        </table>
  </div>
  
  <div id="tabsVal">
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
                <select name="proyectoCuenta" class="comboBox" id="proyectoCuenta" onChange="cargarVales()" style="width:222px">
                </select>
                </div>
                  <input type="hidden" name="idVale" id="idVale">
                  <input type="hidden" name="claveVale" id="claveVale">
                  <input type="hidden" name="importeAntVale" id="importeAntVale" value="0">
                  </td>
              </tr>
              <tr>
                <th height="30">*Número de Vale:</th>
                <td><div class="styled-select">
                <select name="claveValeDis" class="comboBox" id="claveValeDis"  style="width:222px">
                    </select>
                    </div>
                    </td>
              </tr>
              <tr>
                <th height="30">*Importe:</th>
                <td><input name="importeVale" type="text" class="input" id="importeVale" onkeypress=" return keyNumbero( event );"></td>
              </tr>
              <tr>
                <th width="12%" height="31">&nbsp;</th>
                <td width="88%"><table width="400" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                      <td><div class="buttons tiptip">
                        <button name="cmdNuevoAnexo" id="cmdNuevoAnexo2" onClick="lipiarVale();" type="button" class="button red middle"><span class="label" style="width:150px">Nuevo Vale</span></button>
                      </div></td>
                      <td><div class="buttons tiptip">
                        <button name="BorraOs2" id="BorraOs2" onClick="guardarVale();" type="button" class="button blue middle"><span class="label" style="width:150px">Guardar Vale</span></button>
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
                <th width="2%" height="20"  align="center"><img src="../../imagenes/cross.png" alt="" width="16" height="16" onClick="eliminarVales()" style="cursor:pointer"></th>
                <th width="14%"  align="center">Proyecto</th>
                <th width="13%" align="center">Partida</th>
                <th width="15%"  align="center">Vale</th>
                <th width="18%"  align="center">Importe Comprobado</th>
                <th width="16%"  align="center">Importe Anterior</th>
                <th width="15%"  align="center">Importe Pendiente</th>
                <th width="7%"  align="center">&nbsp;</th>
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
  </div>
  </td></tr></table>
</form>
</body>
</html>
