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
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
    
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link rel="stylesheet" href="../../include/css/sweetalert2.min.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap-3.3.7.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css"/>

<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.9.1.min.js?x=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="lista_ordenPago.js?x=<%=System.currentTimeMillis()%>"> </script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/sweet-alert.min.js"></script>

<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorOrdenPagoRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js?x=<%=System.currentTimeMillis()%>"></script>
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

<script type="text/javascript" src="../../include/js/bootstrap-3.3.7.js"></script>

<!--

<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
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
}s
</style>
<body >

<form class="form-horizontal" action="lista_ordenPago.action" method="POST" id="forma" name="forma">
<input type="hidden" class="form-control" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" class="form-control" name="cve_op" id="cve_op" >
<a id="scroll-up" onclick="scrollTo(0,0)" style="display: inline; "></a>
	<div class="row col-md-offset-2">
          <h1 class="h1-encabezado"> Listado de Orden de Pago</h1>
    </div>  
    <div class="well">
    	<div class="form-group row"><!-- Unidad -->
          <label for="grupo" class="col-md-2 control-label">Unidad:</label>
          <div class="col-md-5">
   			<sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      		<c:out value="${nombreUnidad}"/><input type="hidden" name="cbodependencia" class="form-control input-sm" id="cbodependencia" value='<c:out value="${idUnidad}"/>' />
      		</sec:authorize>
       		<sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
       			<select name="cbodependencia" class="form-control input-sm" id="cbodependencia">
            		<option value="0">[Todas la Unidades Administrativas]</option>
            		<c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
              		<option value='<c:out value="${item.ID}"/>' <c:if test='${item.ID==idUnidad}'> selected </c:if>><c:out value='${item.DEPENDENCIA}'/></option>
              		</c:forEach>
          		</select>
          	
      	  	</sec:authorize>
          </div>
          <div class="col-md-1">
          		<input type="checkbox" name="status" id="status"  value="-1" <c:if test="${fn:contains(status,'-1')}" >checked</c:if>>&nbsp;Edición
          </div>
		 <div class="col-md-1">
		 	<input type="checkbox" name="status" id="status"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>> Cerrada
		 </div>
		  <div class="col-md-1">
		 	<input type="checkbox" name="status" id="status"  value="4" <c:if test="${fn:contains(status,'4')}" >checked</c:if>>Cancelada
		 </div>
        </div> <!-- Unidad -->
        <div class="form-group"><!-- Tipo de gasto -->
          <label for="grupo" class="col-md-2 control-label">Tipo de gasto:</label>
          <div class="col-md-5">
   			<select name="cbotipogasto" class="form-control input-sm" id="cbotipogasto">
			      <option value="0">[Todos los tipos de gastos]</option>
			      <c:forEach items="${tipodeGasto}" var="item" varStatus="status">
			        <option value='<c:out value="${item.ID}"/>' <c:if test='${item.ID==tipo_gto}'> selected</c:if>><c:out value='${item.RECURSO}'/></option>
			      </c:forEach>
  			  </select>
          </div>
          <div class="col-md-1">
          	<input name="status" type="checkbox" id="status"  value="6" <c:if test="${fn:contains(status,'6')}">checked</c:if>> Ejercida
   		  </div>
          <div class="col-md-1">
          		[<input type="checkbox" name="status" id="status"  value="7" <c:if test="${fn:contains(status,'7')}" >checked</c:if>>&nbsp;Pagada]
          </div>
          <div>
          	<button name="btnBuscar" id="btnBuscar" onClick="getOrden()" type="button" class="button blue middle"><span class="label" style="width:100px">Buscar</span></button>
  
          </div>
      	</div> <!-- Tipo de gasto -->
      	<div class="form-group"><!-- Beneficiario -->
          <label for="grupo" class="col-md-2 control-label">Beneficiario:</label>
          <div class="col-md-5">
   			<input type="text" id="txtprestadorservicio" name="txtprestadorservicio" class="form-control input-sm" value="<c:out value='${txtprestadorservicio}'/>"/>
      		<input type="hidden" id="CVE_BENEFI" name="CVE_BENEFI" value="<c:out value='${CVE_BENEFI}'/>" />
          </div>
          <div class="col-md-offset-2 col-md-1">
          		<button name="cmdpdf" id="cmdpdf" onClick="getListadoOrdenPago()" title="Mostrar listado en formato PDF" type="button" class="button red middle"><span class="label" style="width:100px">Imprimir PDF</span></button>
          </div>
      	</div> <!-- Beneficiario -->
      	<div class="form-group"><!-- CAPITULO -->
          <label for="grupo" class="col-md-2 control-label">Capitulo:</label>
          <div class="col-md-5">
   			<select name="cbocapitulo" class="form-control input-sm"  id="cbocapitulo">
				<option value="0"> [Todos los capitulos]</option>
			    	<c:forEach items="${capitulos}" var="item" varStatus="status">
			        <option value='<c:out value="${item.CLV_CAPITU}"/>' 
			          <c:if test='${item.CLV_CAPITU==cbocapitulo}'> selected </c:if>>
			          <c:out value='${item.CLV_CAPITU}'/>
			          -
			          <c:out value='${item.CAPITULO}'/>
			     	</option>
			      </c:forEach>
    		</select>
          </div>
          <div class="col-md-2">
          		<input type="checkbox" name="verUnidad" id="verUnidad" value="1"  <c:if test='${verUnidad==1}'>  checked </c:if>>Incluir documentos de la Unidad
          </div>
      	</div> <!-- CAPITULO -->
      	<sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODOS_LOS_DOCUMENTOS_DE_LA_UNIDAD">
      	<div class="form-group"><!-- Num. Pedido -->
	          <label for="txtnumop" class="col-md-2 control-label">Núm. Orden:</label>
	          <div class="col-md-2">
	   				<input name="txtnumop" type="text"  id="txtnumop" maxlength="50"  placeholder="Núm. Orden" class="form-control input-sm" value="<c:out value='${txtnumop}'/>">
	     	  </div>
	     	   	<!--<label for="txtpedido" class="col-md-1 control-label">Núm. Pedido:</label>  -->
	     	  <div class="col-md-2">
	     	  		<input name="txtpedido" class="form-control input-sm" type="text" id="txtpedido" placeholder="Núm. Pedido" maxlength="50"  value="<c:out value='${txtpedido}'/>">
	     	  </div>	
	     	   
     	</div> <!-- Num. Pedido -->
      		<div class="form-group"><!-- Num. Pedido -->
	         <label for="grupo" class="col-md-2 control-label">Desde:</label>
	          <div class="col-md-1">
	   				<input type="text" name="fechaInicial" id="fechaInicial" value="${fechaInicial}" size="12"maxlength="10" class="form-control input-sm" style="width:100px"  id="inputKey" placeholder="Desde">
			  </div>
	     	  <div class="col-md-1">
	   				<input type="text" name="fechaFinal"  id="fechaFinal" value="${fechaFinal}" size="12"  maxlength="10" class="form-control input-sm" style="width:100px"  id="inputValue" placeholder="Hasta">
	     	  </div>
	     	   	<!--<label for="txtpedido" class="col-md-1 control-label">Núm. Pedido:</label>  -->
	     	  <div class="col-md-6">
	     	  		<label for="cbotipo" class="col-md-2 control-label">Tipo: </label>
			                <div class="col-md-4">
			                     <select name="cbotipo" class="form-control input-sm" id="cbotipo">
								      <option value="-1">[Todos los tipos]</option>
								      <c:forEach items="${tipoDocumentosOp}" var="item" varStatus="status">
								      <option value="<c:out value='${item.ID_TIPO_ORDEN_PAGO}'/>" <c:if test='${item.ID_TIPO_ORDEN_PAGO==cbotipo}'> selected </c:if> >
								      <c:out value="${item.DESCRIPCION}"/>
								      </option>
								      </c:forEach>
		    					</select>
			                </div>
	     	  </div>		 
     	</div> <!-- Num. Pedido -->
      	</sec:authorize>
      	<div class= "form-group row">
      		<div class="col-md-12">
      			<div class="col-md-6">
      				
      			</div>
      			<div class="col-md-6">
      				
      			</div>
      		</div>
      		
      	</div>
    </div> <!-- Cierre del Well -->
    
    
    
    
    
<br />
<table width="95%" class="table table-sm table-hover table-responsive" align="center" id="listaRequisiciones"  cellpadding="0" cellspacing="0">
 <thead>
  <tr>
    <th width="3%" height="20">&nbsp;</th>
    <th width="9%">Número</th>
    <th width="7%">Fecha</th>    
    <th width="42%">Beneficiario</th>
    <th width="4%">Unidad</th>
    <th width="8%">Tipo</th>
    <th width="7%">Estado</th>
    <th width="6%">Importe</th>    
    <th width="6%">Opciones</th>
  </tr>
   </thead>   
<tbody>  
<c:set var="cont" value="${0}" /> 
<c:forEach items="${ordenes}" var="item" varStatus="status"> 
  <tr id='f<c:out value="${cont}"/>'>
    <td align="center">
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_MOVER_ORDENES_DE_PAGO_A_OTRO_USUARIO">
    	<c:if test='${item.STATUS!=0&&item.STATUS!=4&&item.STATUS!=6}'><input alt="<c:out value='${item.NUM_OP}'/>" type="checkbox" id="chkordenes" name="chkordenes" value="<c:out value='${item.CVE_OP}'/>"/></c:if>
    </sec:authorize>
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_ORDEN_DE_PAGO">
    	<c:if test='${item.STATUS==0}'><input alt="<c:out value='${item.NUM_OP}'/>" type="checkbox" id="chkordenes" name="chkordenes" value="<c:out value='${item.CVE_OP}'/>"/></c:if>
    </sec:authorize>
    </td>
    <td align="center">
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_ORDENES_DE_PAGO">
    	<a href="javascript:subOpAdm('op', <c:out value='${item.CVE_OP}'/>, <c:out value='${item.CVE_PERS}'/>)">
    </sec:authorize>
        <c:out value='${item.NUM_OP}'/>
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_ORDENES_DE_PAGO">
    	</a>
    </sec:authorize></td>
    <td><c:out value='${item.FECHA}'/></td>
    <td  align="left"><c:out value='${item.NCOMERCIA}'/></td>
    <td><c:out value='${item.CLV_UNIADM}'/></td>
    <td><c:out value='${item.TIPO_DOC}'/></td>
    <td><c:if test='${item.FECHA_PAGO!=NULL}'>PAGADA</c:if><c:if test='${item.FECHA_PAGO==NULL}'><c:out value='${item.DESCRIPCION_ESTATUS}'/></c:if></td>
    <td><fmt:formatNumber value="${item.IMPORTE}"  pattern="#,###,###,##0.00" />&nbsp;</td>
    <td> <c:if test='${item.STATUS!=4}'><img style="cursor:pointer" src="../../imagenes/pdf.gif" alt="Ver Documento" border="0" width="14" height="16" onClick="mostrarOpcionPDF(<c:out value='${item.CVE_OP}'/>)"></c:if>
    <c:if test='${item.STATUS==4}'><img src="../../imagenes/pdf2.png" border="0" width="14" height="16"></c:if>
    <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_SOLO_IMPRESION_GENERAL">
        <c:if test='${item.STATUS==-1}'>
        <img src="../../imagenes/page_white_edit.png" title="Editar / Abrir" style="cursor:pointer" onClick="editarOP(<c:out value='${item.CVE_OP}'/>, <c:out value='${item.STATUS}'/>)">
        </c:if>
        <c:if test='${item.STATUS!=-1}'>
        <img src="../../imagenes/page_gray_edit.png"  title="Editar / Abrir">
        </c:if>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_ORDENES_DE_PAGO">
            <c:if test='${item.STATUS==-1 || item.STATUS==0}'>
                <img style="cursor:pointer" src="../../imagenes/cross.png" title="Cancelar Documento" border="0" width="16" height="16" onClick="cancelarOrden(<c:out value='${item.CVE_OP}'/>)">     
            </c:if>
            <c:if test='${item.STATUS>1}'>
                <img src="../../imagenes/cross2.png" title="Cancelar OP no disponible" border="0" width="16" height="16">     
            </c:if>
        </sec:authorize>
        <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_ORDENES_DE_PAGO">
              <img src="../../imagenes/cross2.png" title="Cancelar OP no disponible" border="0" width="16" height="16">     
        </sec:authorize>
    </sec:authorize>
    </td>
    <c:set var="cont" value="${cont+1}"/>
  </tr>
  </c:forEach> 
 <c:if test="${fn:length(ordenes) > 0}"> 
 <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_ORDEN_DE_PAGO"> 
 <c:if test="${fn:contains(status,'0')}"> 
  <tr>
    <td colspan="9" align="left" height="40" style="background-color:#FFF"><table width="269" border="0" cellspacing="0" cellpadding="0">
      <tr>
            <td width="130" bgcolor="#FFFFFF"><div class="buttons tiptip">
              <button name="cmdaperturar" id="cmdaperturar2" onClick="aperturarOrden()" title="Apertura para edicion los documentos seleccionados" type="button" class="button red middle"><span class="label" style="width:100px">Aperturar</span></button>
            </div></td>
          <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_REQUISICIONES">
            <td width="139" bgcolor="#FFFFFF"><div class="buttons tiptip">
              <button name="cmdcancelarm" id="cmdcancelarm" onClick="cancelacionMultiple()"  title="Cancela o elimina los documentos seleccionados" type="button" class="button red middle" ><span class="label" style="width:100px">Cancelar</span></button>
            </div></td>
          </sec:authorize>
        </tr>
    </table></td>
    </tr>
    </c:if>
    </sec:authorize>
  </c:if>
   <tr>
          <td colspan="8" height="25" style="background-color:#FFF" align="left"><strong>Total de registros encontrados: <c:out value='${CONTADOR}'/></strong></td>
        </tr> 
  
  </tbody>  
</table>

</form>
</body>
</html>
