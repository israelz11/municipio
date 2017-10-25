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
<script type="text/javascript" src="../../include/js/jquery.form.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorOrdenPagoRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script>  
<script type="text/javascript" src="../../include/js/sweetalert2.js"></script>

<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="orden_pago.js?x=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script> 
<script type="text/javascript" src="../../include/js/fileinput.min.js"></script>


<link rel="stylesheet" href="../../include/css/bootstrap-3.3.7.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/style-tabs.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/fileinput.min.css" type="text/css"/>


<link rel="stylesheet" href="../../include/css/boostrap-select/dist/css/bootstrap-select.css" type="text/css">
<script type="text/javascript" src="../../include/css/boostrap-select/dist/js/bootstrap-select.js"></script>
<script type="text/javascript" src="../../include/css/bootstrap-datetimepicker-master/js/moment-with-locales-2.9.0.js"></script>
<link rel="stylesheet" href="../../include/css/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker-4.15.35.css" type="text/css">
<script type="text/javascript" src="../../include/css/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker-4.15.35.js"></script>
<link rel="stylesheet" href="../../include/css/sweetalert2.css" type="text/css">

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

<div id="listaOrdenesPendientes" class="well">
	<div class="form-group">
		<div class="col-sm-2 col-md-offset-1 control-label">Unidad administrativa:</div>
		<div class="col-md-3">	
		    <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      			<c:out value="${nombreUnidad}"/><input type="hidden" name="cbUnidad" id="cbUnidad" value='<c:out value="${idUnidad}"/>' />
      		</sec:authorize>
       		<sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
       			<select name="cbUnidad" class="selectpicker form-control input-sm m-b" data-live-search="true" id="cbUnidad" onChange="llenarTablaDeOrdenes();">
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
          		<input type="button" class="btn btn-primary" name="btnNuevaOp" id="btnNuevaOp" value="Nueva Orden de Pago" style="width:100%">
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
      <table border="0" class="listasDetalles table table-hover" id="listaOrdenes" style="width:100%">
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

  <div class="col-sm-12" id="tabsOrdenesEnca">
    <!-- Tabs para Ordenes de Pago-->
    <div id="tabsOrdenes">
    <div class="panel with-nav-tabs panel-primary">
        <div class="panel-heading">
          <ul class="nav nav-tabs responsive" id="tabsOrdenesPane" name="tabsOrdenesPane">
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
                            <select name="xTipo" class="selectpicker form-control input-sm m-b" data-live-search="true" id="xTipo" style="width:100%">
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
                                  <option value='<c:out value="${item.ID}"/>'
                                  	<c:if test='${item.ID_RECURSO==tipoGasto}'>selected</c:if>><c:out value='${item.RECURSO}'/>
                                  </option>
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
                       
                    </div>
                  </div>

                  <!--Beneficiario-->
                  <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-3 ">Seleccione un Beneficiario:</div>
                        <div class="form-group col-sm-6">
                            <select name="xBeneficiario" class="selectpicker form-control input-sm m-b" data-live-search="true" id="xBeneficiario" style="width:100%">
                              <c:forEach items="${beneficiarios}" var="item" varStatus="status">
                                    <option value='<c:out value="${item.CLV_BENEFI}"/>'
                                    <c:if test='${item.CLV_BENEFI==xBeneficiario}'>selected</c:if>><c:out value='${item.NCOMERCIA}'/>
                              </c:forEach>
                            </select>
                        </div>
                        
                    </div>
                  </div>

                 
            
 			  <!--Consurso-->
 			  <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-3 ">Concurso:</div>
                        <div class="form-group col-sm-3">
                            <input name="xConcurso"  type="text"  class="form-control" id="xConcurso" value="0" maxlength="30" style="width:100%" />
                        </div>
                        
                    </div>
              </div>
              <!--Reembolso-->
              <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-3 ">Reembolso al Fondo Fijo:</div>
                        <div class="form-group col-sm-1">
                            <input name="reembolso"  type="checkbox"  class="form-control" id="reembolso" value="S"/>
                        </div>
                       
                    </div>
              </div>
               <!--Concepto-->
               <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-3 ">*Concepto:</div>
                        <div class="form-group col-sm-3">
                            <textarea name="xNota"  type="textarea" wrap="virtual" cols="80" rows="4" class="form-control" id="xNota" value="0" maxlength="500" style="width:445px"/></textarea>
                        </div>
                        <div class="form-group col-sm-7">&nbsp;</div>
                    </div>
              </div>
               <!--Botones-->
               <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-3 "></div>
                        <div class="form-group col-sm-9">
                            <input class="btn btn-default" name="cmdregresar" id="cmdregresar" value="Regresar" style="width:100px" type="button">
                            <input class="btn btn-primary" name="xGrabar" id="xGrabar"  value="Nuevo" onClick="limpiarForma()"  style="width:100px" type="button">
                            <input class="btn btn-danger" name="btnCerrar" id="btnCerrar" value="Cerrar" onClick="cerrarOrden()"  style="width:100px" type="button"> 
                            <input class="btn btn-success" name="btnGrabar" id="btnGrabar"  value="Guardar" style="width:100px" type="button">
                        </div>
                        
                    </div>
              </div>
        </form>
          </div> <!--Tab Cabecera-->
            <!--Tab Conceptos-->
              <div class="tab-pane" id="tabsCon">
                <form class="form-horizontal">
                 
		        <!--Observaciones-->
		 			<div class="row">
		                <div class="form-group">
		                    <div class="control-label col-sm-3 "></div>
		            	    <div class="form-group col-sm-3">
		                        <input class="btn btn-primary btn-outline" type="button" name="cmdCargarFactura" id="cmdCargarFactura" onClick="getFacturas()" value="Generar a partir de factura..."> 
                			</div>
		                    <div class="form-group col-sm-7">&nbsp;</div>
		                </div>
		            </div>   
		                                        
                </form>
                <div>
		            <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listasDetalles table table-hover" id="listasDetallesOrdenes">
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
              </div> <!--Tab Conceptos-->
            <!--Tab Retenciones-->
              <div class="tab-pane" id="tabsRet">
            	<form class="form-horizontal">
            	 	<!--Retencion-->
            	 	<div class="row">
            	 		<div class="form-group">
		            		<div class="control-label col-sm-3 ">*Retencion:</div>
		                    <div class="form-group col-sm-6">
		                    	<select name="retencion" class="selectpicker form-control input-sm m-b" data-live-search="true" id="retencion" style="width:100%">
		                            <c:forEach items="${tipoRetenciones}" var="item" varStatus="status">
					                	<option value='<c:out value="${item.CLV_RETENC}"/>'
					                     <c:if test='${item.CLV_RETENC==retencion}'>selected</c:if>><c:out value="${item.RETENCION}"/>
					                    </option>
					               	</c:forEach>
					            </select>
					        </div>
		                    <input type="hidden" name="idRetencion" id="idRetencion">
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
                    </div>
                    <!--Importe-->
	                <div class="row">
	                    <div class="form-group">
	                        <div class="control-label col-sm-3 ">*Importe:</div>
	                        <div class="form-group col-sm-3">
	                            <input name="importeRetencion"  type="text" class="form-control" id="importeRetencion" readonly onkeypress=" return keyNumbero( event );">
	                        </div>
	                        <div class="form-group col-sm-7">&nbsp;</div>
	                    </div>
	              	</div>
	              	<div class="row">
	                    <div class="form-group">
	                        <div class="control-label col-sm-3 "></div>
	                        <div class="form-group col-sm-3" style="display:none;">
	                            <input type="button" name="cmdNuevaRetencion" id="cmdNuevaRetencion" class="btn btn-primary" style="width:150px" value="Nueva Retención">  
	                            <input type="button" name="cmdNuevaRetencion" id="cmdNuevaRetencion" onClick="guardarRetencion()" class="btn btn-success" style="width:150px" value="Guardar Retención"> 
							</div>
	                        <div class="form-group col-sm-7">&nbsp;</div>
	                    </div>
	              	</div>
	              	<div>
	              	<table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listasDetalles table table-hover" id="listasRetenciones">
			            <thead>
			              <tr >
			                <th width="3%" height="20"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminarRetencion()" style='cursor: pointer;'></th>
			                <th width="30%"  align="center">Retención</th>
			                <th width="30%" style="text-align: right;">Importe</th>
			                <th width="37%" align="center"></th>
			                </tr>
			              </thead>
			            		<tbody>
			              		</tbody>
			        </table>
			        </div>
            	</form>
            </div>
            <!--Tab Anexos-->
              <div class="tab-pane" id="tabsDoc">
            	<form class="form-horizontal" id="frmDoc" name="frmDoc" method="post" enctype="multipart/form-data">
            	<input name="CveOrdenOP" type="hidden"  id="CveOrdenOP" size="8" value="" />
            		<!--Tipo Movimiento-->
            	 	<div class="row">
            	 		<div class="form-group">
		            		<div class="control-label col-sm-3 ">*Tipo de movimiento:</div>
		                    <div class="form-group col-sm-6">
		                    	<select name="tipoMovDoc" class="selectpicker form-control input-sm m-b" data-live-search="true" id="tipoMovDoc" style="width:100%">
		                            <c:forEach items="${tipoDocumentos}" var="item" varStatus="status">
				                    <option value='<c:out value="${item.T_DOCTO}"/>'
				                    <c:if test='${item.T_DOCTO==tipoMovDoc}'>selected</c:if>><c:out value="${item.DESCR}"/>
				                      </option>
				                  </c:forEach>
				                </select>
					        </div>
		                    <input type="hidden" name="idDocumento" id="idDocumento" value="0">
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
                    </div>
                <!--Número-->
            	 	<div class="row">
            	 		<div class="form-group">
		            		<div class="control-label col-sm-3 ">*Número:</div>
		                    <div class="form-group col-sm-6">
		                    	<input name="numeroDoc" type="text" class="form-control" id="numeroDoc"  style="width:180px" >
					        </div>
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
                    </div>
                <!--Notas-->
            	 	<div class="row">
            	 		<div class="form-group">
		            		<div class="control-label col-sm-3 ">*Notas:</div>
		                    <div class="form-group col-sm-6">
                                  	<input name="notaDoc" type="text" class="form-control" id="notaDoc"  style="width:180px" >
					        </div>
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
                    </div>
                <!--Notas-->
            	 	<div class="row">
            	 		<div class="form-group">
		            		<div class="control-label col-sm-3 ">*Archivo:</div>
		                    <div class="form-group col-sm-6">
                                <input type="file" class="input-file" id="archivo" name="archivo" style="width:445px" accept="application/pdf"/>
                             
					        </div>
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
                    </div> 
                    <!--Archivos-->
            	 	<div class="row">
            	 		<div class="form-group">
		            		<div class="control-label col-sm-3 "></div>
		                    <div class="form-group col-sm-6">
                                <input type="button" class="btn btn-primary" name="cmdNuevoAnexo" id="cmdNuevoAnexo" onClick="" value="Nuevo Anexo">  
                        		<input name="BorraOs2" class="btn btn-success" id="BorraOs2" type="button" value="Guardar Anexo">  
					        </div>
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
                    </div>
                    <div class="form-group">
                    	<table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listasDetalles table table-hover" id="listasDocumentos">
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
			        </div>    
            	</form>
            </div> 
            <!--Tab Vales-->
              <div class="tab-pane" id="tabsVal">
            	<form class="form-horizontal">
            		<!--*Proyecto Cuenta:-->
            		<div class="row">
            			<div class="form-group">
            	 			<div class="control-label col-sm-3 ">*Proyecto Cuenta:</div>
		                    <div class="form-group col-sm-6">
		                    	 <select name="proyectoCuenta" class="form-control" id="proyectoCuenta" onChange="cargarVales()" style="width:222px">
		                    	  	
                				 </select>
		                    </div>
		                    <p id="demo"></p>
		                    <input type="hidden" name="idVale" id="idVale">
                  			<input type="hidden" name="claveVale" id="claveVale">
                  			<input type="hidden" name="importeAntVale" id="importeAntVale" value="0">
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
		            </div> 
		            <!--Número de Vale-->
            	 	<div class="row">
            	 		<div class="form-group">
		            		<div class="control-label col-sm-3 ">*Número de Vale:</div>
		                    <div class="form-group col-sm-6">
                                <select name="claveValeDis" class="form-control" id="claveValeDis"  style="width:222px">
                   				</select>
					        </div>
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
                    </div>
                    <!--Importe-->
            	 	<div class="row">
            	 		<div class="form-group">
		            		<div class="control-label col-sm-3 ">*Importe:</div>
		                    <div class="form-group col-sm-6">
                               <input name="importeVale" type="text" class="form-control" id="importeVale" style="width:180px" onkeypress=" return keyNumbero( event );">
					        </div>
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
                    </div> 
                     <!--Importe-->
            	 	<div class="row">
            	 		<div class="form-group">
		            		<div class="control-label col-sm-3 "></div>
		                    <div class="form-group col-sm-6">
                              <input name="cmdNuevoAnexo" id="cmdNuevoAnexo2" class="btn btn-primary" onClick="lipiarVale();" type="button" value="Nuevo Vale"> 
                        	  <input name="BorraOs2" id="BorraOs2" class="btn btn-success" onClick="guardarVale();" type="button" value="Guardar Vale">  
					        </div>
		                    <div class="form-group col-sm-3">&nbsp;</div>
		                </div>    
                    </div> 
                    <div class="form-group">
                     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="listasDetalles table table-hover" id="listasVales">
			            <thead>
			              <tr >
			                <th width="2%" height="20" style="text-align:left"><img src="../../imagenes/cross.png" alt="" width="16" height="16" onClick="eliminarVales()" style="cursor:pointer"></th>
			                <th width="14%"  style="text-align:center">Proyecto</th>
			                <th width="13%"  style="text-align:center">Partida</th>
			                <th width="15%"  style="text-align:center">Vale</th>
			                <th width="18%"  style="text-align:right">Importe Comprobado</th>
			                <th width="16%"  style="text-align:right">Importe Anterior</th>
			                <th width="15%"  style="text-align:right">Importe Pendiente</th>
			                <th width="7%"  style="text-align:right">&nbsp;</th>
			                </tr>
			              </thead>
			            <tbody>
			              </tbody>
			            </table>
                    </div>      
            	</form>
            </div>  	 	  	
          </div>
        </div>
    </div>
   </div>
  </div>
<!--------------------- Fin Tabs ---------------------------->
 
</form>
</body>
</html>
