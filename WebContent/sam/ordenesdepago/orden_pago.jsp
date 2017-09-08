<%@page import="java.util.Date"%>
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
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<script type="text/javascript" src="../../include/js/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="../../include/js/bootstrap-3.3.7.js"></script>
<!--<script type="text/javascript" src="../../include/js/jquery-ui-1.12.1.js"></script>-->
<!--<link href="../../include/js/autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<link type="text/css" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" rel="stylesheet" />	
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">-->
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorOrdenPagoRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script>  
<!--
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-ui-1.7.3.custom.min.js"></script>

<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/jquery.form.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
-->
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="orden_pago.js?x=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script> 
<!--<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>-->

<!--  <link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">  
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>-->
<link rel="stylesheet" href="../../include/css/bootstrap-3.3.7.css?x=<%=System.currentTimeMillis()%>" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css?=<%=System.currentTimeMillis()%>" type="text/css"/>
<link rel="stylesheet" href="../../include/css/style-tabs.css" type="text/css"/>



<!--<link rel="stylesheet" href="../../include/css/sweet-alert.css" type="text/css">
<script src="../../include/js/sweet-alert.js"></script>
-->
<link rel="stylesheet" href="../../include/css/boostrap-select/dist/css/bootstrap-select.css" type="text/css">
<script type="text/javascript" src="../../include/css/boostrap-select/dist/js/bootstrap-select.js"></script>
<script type="text/javascript" src="../../include/css/bootstrap-datetimepicker-master/js/moment-with-locales-2.9.0.js"></script>
<link rel="stylesheet" href="../../include/css/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker-4.15.35.css" type="text/css">
<script type="text/javascript" src="../../include/css/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker-4.15.35.js"></script>

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
.ui-widget-content{
  border: none;
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

<h1 class="h1-encabezado">Ordenes de Pago - Captura de Ordenes de Pago</h1>

<div id="DivHeadDependency" class="well">
	<div class="form-group">
		<div class="col-sm-2 col-md-offset-1 control-label">Unidad administrativa:</div>
		<div class="col-md-3">	
		    <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      			<c:out value="${nombreUnidad}"/><input type="hidden" name="cbUnidad" id="cbUnidad" value='<c:out value="${idUnidad}"/>' />
      		</sec:authorize>
       		<sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
       			<select name="cbUnidad" class="form-control input-sm" id="cbUnidad" onChange="llenarTablaDeOrdenes();">
            		<c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">                  
              			<option value="<c:out value="${item.ID}"/>" 
              			<c:if test="${item.ID==idUnidad}"> selected </c:if> >
             			<c:out value="${item.DEPENDENCIA}"/></option>
           			</c:forEach>
          		</select>
        	</sec:authorize>
       </div>
       <div class="col-md-3 form-group">
       <div class="col-md-10">
		      <div>
          		<button class="btn btn-primary" name="btnNuevaOp" id="btnNuevaOp" onClick="nuevaOp();limpiarForma();" value="Nueva Orden" style="width:100%">Nueva Orden de Pago</button>
          </div>
        </div>   
		</div>
	</div>
	<br/>
	<br/>
	<br/>

</div>

<!-- Cierra el WELL -->
  <div class="col-sm-12">
    <div id="DivOPResults" class="form-gorup">
      <table border="0" class="table table-hover" id="listaOrdenes" style="width:100%">
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
    </div>    
  </div>

  <div class="col-sm-12">
    <!-- Tabs para Ordenes de Pago-->
    <div class="panel with-nav-tabs panel-primary">
        <div class="panel-heading">
          <ul class="nav nav-tabs responsive" id="tab_requi" name="tab_requi">
                <li class="active"><a href="#tabsCabe" data-toggle="tab">Información general</a></li>
                <li><a href="#tabsCon" data-toggle="tab">Conceptos</a></li>
                <li><a href="#tabsRet" data-toggle="tab">Retenciones</a></li>
                <li><a href="#tabsDoc" data-toggle="tab">Anexos</a></li>
                <li><a href="#tabsVal" data-toggle="tab">Vales</a></li>
            </ul>
        </div>
        <div class="panel-body">
          <div class="tab-content">
            <!--Tab Encabezado-->
              <div class="tab-pane fade in active" id="tabsCabe">
                  <form class="form-horizontal">
                    <!-- Numero de Orde de Pago -->
                    <div class="row">
                      <div class="form-group" style="padding-bottom:15px;padding-top: 10px;">
                        <div class="control-label col-sm-3">No. Orden:</div>
                        <div class="col-sm-9">
                          <div id="NoOrden">&nbsp;</div>
                        </div>
                      </div>
                    </div>
                    <!-- Tipo de Orden de Pago -->
                    <div class="row">
                      <div class="form-group">
                        <div class="control-label col-sm-3 ">*Tipo:</div>
                        <div class="col-sm-3 form-group">
                            <select name="xTipo" class="selectpicker form-control input-sm m-b" id="xTipo" style="width:100%">
                              <c:forEach items="${tipoDocumentosOp}" var="item" varStatus="status">
                                <option value="<c:out value='${item.ID_TIPO_ORDEN_PAGO}'/>">
                                  <c:out value="${item.DESCRIPCION}"/>
                                  </option>
                                </c:forEach>
                            </select>
                        </div>
                      </div>
                    </div>
                    <!-- Tipo de Gasto-->
                    <div class="row">
                      <div class="form-group">
                        <div class="control-label col-sm-3">*Tipo de Gasto:</div>
                        <div class="col-sm-6 form-group">
                            <select name="tipoGasto" class="selectpicker form-control input-sm m-b" data-live-search="true" id="tipoGasto" style="width:100%">
                                <c:forEach items="${tipoGastos}" var="item" varStatus="status">                  
                                  <option value="<c:out value='${item.ID}'/>"><c:out value="${item.RECURSO}"/></option>
                                  </c:forEach>
                            </select>
                        </div>
                      </div>
                    </div>
                    <!-- Contrato -->
                    <div class="form-group" style="display: none;">
                      <div class="col-sm-3 control-label">Numero de Contrato:</div>
                      <div class="col-sm-9 form-group">
                          <input name="txtnumcontrato" disabled  type="text"  class="input" id="txtnumcontrato" value="" maxlength="30" style="width:222px; background:#C0C0C0" onBlur="getProveedor()" />
                          <img src="../../imagenes/buscar.png" alt="Mostrar presupuesto" name="img_contrato" width="22" height="22" id="img_contrato" style="cursor:pointer" align="absmiddle"/>
                          <img src="../../imagenes/cross2.png" id="img_quitar_contrato" width="16" height="16" alt="Quitar contrato" title="Quitar contrato" align="absmiddle" style="cursor:pointer">
                          <input name="CVE_CONTRATO" type="hidden"  id="CVE_CONTRATO" size="8" maxlength="6" readonly value="0" />
                          <input name="CLV_PARBIT" type="hidden"  id="CLV_PARBIT" size="8" maxlength="6" readonly value="" />
                          <input name="CID_PROYECTO" type="hidden"  id="CID_PROYECTO" size="8" maxlength="6" readonly value="" />
                          <input name="CCLV_PARTID" type="hidden"  id="CCLV_PARTID" size="8" maxlength="6" readonly value="" />
                          <input name="CCLV_BENEFI" type="hidden"  id="CCLV_BENEFI" size="8" maxlength="6" readonly value="" />
                      </div>
                    </div>
                  <!--Periodo-->
                  <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-3">*Periodo Presupuestal:</div>
                        <div class="form-group col-sm-3">
                            <select name="cbomes" class="selectpicker form-control input-sm m-b" id="cbomes" style="width:100%">
                              <c:forEach items="${meses}" var="item" varStatus="status">
                                <option value="<c:out value="${item.mes}"/>">
                                <c:out value="${item.DESCRIPCION}"/>
                              </c:forEach>
                            </select>
                        </div>
                        <div class="form-group col-sm-6">&nbsp;</div>
                    </div>
                  </div>
                  <!--Fecha-->
                  <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-3 ">*Fecha de la OP:</div>
                        <div class="form-group col-sm-3">
                          <div class="input-group date">
                              <input name="fecha" type="text" class="form-control" id="fecha" value="" style="width:100%" maxlength="10"/>
                              <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                              </span>
                          </div>
                          <input name="fecha2" type="hidden" class="input" id="fecha2" value=<%=new java.util.Date()%> style="width:111px" maxlength="10"/>
                        </div>
                        <div class="form-group col-sm-6">&nbsp;</div>
                    </div>
                  </div>
                  <!--Generar IVA-->
                  <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-3 ">Importe IVA:</div>
                        <div class="form-group col-sm-3">
                            <input name="xImporteIva"  type="text"  class="form-control" id="xImporteIva" value="0" maxlength="30" onkeypress=" return keyNumbero( event );" style="width:100%" />
                        </div>
                        <div class="form-group col-sm-7">&nbsp;</div>
                    </div>
                  </div>

                  <!--Beneficiario-->
                  <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-3 ">Seleccione un Beneficiario:</div>
                        <div class="form-group col-sm-6">
                            <select name="xBeneficiario" class="selectpicker form-control input-sm m-b" id="xBeneficiario" style="width:100%">
                              <c:forEach items="${Beneficiario}" var="item" varStatus="status">
                                <option value="<c:out value="${item.mes}"/>">
                                <c:out value="${item.DESCRIPCION}"/>
                              </c:forEach>
                            </select>
                        </div>
                        <div class="form-group col-sm-3">&nbsp;</div>
                    </div>
                  </div>

                  </form>
              </div>

              <!--Tab Conceptos-->
              <div class="tab-pane" id="tabsCon">
                <form class="form-horizontal">
                  <!--Generar IVA-->
                  
                </form>

              </div>
          </div>
        </div>
    </div>
  </div>
<!--Fin Tabs-->

<div id="tabsOrdenesEnca">
<div id="tabsOrdenes">
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
					<!-- <button name="cmdregresar" id="cmdregresar" onClick="regresar()" title="Volver al inicio para crear nueva Orden de Pago por unidad adm." type="button" class="btn tn-default"><span class="label" style="width:100px">Regresar</span></button> -->
					<input class="btn btn-default" name="cmdregresar" id="cmdregresar" onClick="regresar()"  value="Regresar" style="width:100px" type="button">
                </div></td>
                
                <td><div class="buttons tiptip">
					<!--<button name="xGrabar" id="xGrabar" onClick="limpiarForma()" title="Limpia valores para continuar con nueva Orden de Pago." type="button" class="btn btn-info"><span class="label" style="width:100px">Nuevo</span></button>-->
					<input class="btn btn-primary" name="xGrabar" id="xGrabar"  value="Nuevo" onClick="limpiarForma()"  style="width:100px" type="button">
                </div></td>
                <td><div class="buttons tiptip">
                		
                <button class="btn btn-danger" name="btnCerrar" id="btnCerrar" value="Cerrar" onClick="cerrarOrden()"  style="width:100px">
                       	<!-- <span class="glyphicon glyphicon-search"></span> Buscar  -->Cerrar  
                </button>
					<!-- <button name="btnCerrar" id="btnCerrar" onClick="cerrarOrden()" title="Cierra para comprometer el importe de la Orden de Pago." type="button" class="btn btn-cerrar"><span class="label" style="width:100px">Cerrar</span></button> -->
                </div></td>
                <td><div class="buttons tiptip">
                	<input class="btn btn-success btn-sm" name="btnGrabar" id="btnGrabar"  value="Guardar" onClick="guardar()"  style="width:100px" type="button">
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
                <td><input name="importeRetencion" readonly type="text" class="input" id="importeRetencion" onkeypress=" return keyNumbero( event );"></td>
              </tr>
             
              <tr>
                <th height="56">&nbsp;</th>
                <td><table width="400" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><div class="buttons tiptip">
                    
						<button name="cmdNuevaRetencion" id="cmdNuevaRetencion" type="button" class="btn btn-info"><span class="label" style="width:150px">Nueva Retención</span></button>
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
  
  </div><!-- Cierra tabsOrdenes -->
  </div><!-- Cierra tabsOrdenesEnca -->
  </td></tr></table>
</form>
</body>
</html>
