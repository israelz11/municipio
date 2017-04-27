<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html">
<html>
<head>

<link rel="stylesheet" href="../../include/css/sweet-alert.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/bootstrap-3.3.4.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css"/>
<!-- <link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>  -->
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css"/>
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/interface/ControladorListadoBeneficiariosRemoto.js"> </script>
<script type="text/javascript" src="lst_proveedores.js"></script>
<script type="text/javascript" src="../../include/js/bootstrap-3.3.4.js"></script>
<script type="text/javascript" src="../../include/js/sweet-alert.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>Administracion - Listado de Beneficiarios y Funcionarios</title>

</head>

<body>

<input type="hidden" name="ejercicio" id="ejercicio" value="&lt;c:out value='${ejercicio}'/&gt;" />
<input type="hidden" name="cve_contrato" id="cve_contrato" />

<form class="form-horizontal" action="lst_proveedores.action" id="forma" name="forma" method="post">

	<div class="row col-md-offset-2">
        	<h1 class="h1-encabezado">Administración - Listado de beneficiarios y funcionarios</h1>
	</div>
	<div style="width:1600px; margin-left:auto; margin-right:auto" class="container">
		<div class="well">
			<div class="form-group">
				<label for="txtprestadorservicio" class="control-label col-md-2">Beneficiario:</label>
              	<div class="col-md-4">
              		<input type="text" id="txtprestadorservicio" name="txtprestadorservicio" class="form-control"  value="<c:out value='${txtprestadorservicio}'/>"/>
              	</div>
              	<div class="col-md-3 col-md-offset-2">
              		<input  name="btnBuscar" type="button" class="btn btn-buscar" id="btnBuscar"   value="Buscar" style="width:150px" />
              	</div>
			</div>
			<div class="form-group">
				<label for="txtrfc" class="control-label col-md-2">RFC:</label>
              	<div class="col-md-2">
              		<input type="text" id="txtrfc" name="txtrfc" class="form-control" value="<c:out value='${txtrfc}'/>"/>
              	</div>
              	<div class="col-md-3 col-md-offset-4">
              		<input  name="cmdnuevo" type="button" class="btn btn-info" id="cmdnuevo"   value="Nuevo..." style="width:150px" />
              	</div>
			</div>
			<div class="form-group">
				<label for="txtrfc" class="control-label col-md-2">Filtrar por:</label>
              	<div class="col-md-2">
              		<select name="cbotipo" class="form-control" id="cbotipo" style="width:150px" >
      					<option value="0" <c:if test='${cbotipo==0}'> selected </c:if>>[Ambos]</option>
      					<option value="1" <c:if test='${cbotipo==1}'> selected </c:if>>Beneficiarios</option>
      					<option value="2" <c:if test='${cbotipo==2}'> selected </c:if>>Funcionarios</option>
      					<option value="3" <c:if test='${cbotipo==3}'> selected </c:if>>Persona Fisica</option>
      					<option value="4" <c:if test='${cbotipo==4}'> selected </c:if>>Persona Moral</option>
      					<option value="5" <c:if test='${cbotipo==5}'> selected </c:if>>Funcionario Municipal</option>
      					
    				</select>
              	</div>
			</div>
			<div class="form-group">
				<label for="txtrfc" class="control-label col-md-2">Vigencia:</label>
              	<div class="col-md-3">
              		<label class="checkbox-inline"><input type="checkbox" name="vigencia"  id="vigencia" value=""<c:if test="${fn:contains(vigencia,'1')}">checked</c:if>/>Activos</label>
              	</div>
              	
			</div>
		</div>
	
	

	
	<table width="95%" class="table table-hover table table-condensed table-striped" align="center" id="listaRequisiciones"  cellpadding="0" cellspacing="0">
  		<thead class="thead-inverse">
    		<tr>
		      <th height="20" width="29%">Beneficiario</th>
		      <th width="12%">RFC</th>
		      <th width="12%">Telefono</th>
		      <th width="28%">Domicilio Fiscal</th>
		      <th width="9%">Status</th>
		      <th width="6%">Opciones</th>
    		</tr>
  		</thead>
  		<tbody>

  		</tbody>
  		<c:out value="${cont}"/>
  		<c:forEach items="${beneficiarios}" var="item" varStatus="status">
  		<tr id='f<c:out value="${cont}"/>'>
  		<!-- <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')"/> -->
		    
		    <td style="text-align:left;">&nbsp;<c:out value='${item.NCOMERCIA}'/></td>
		    <td style="text-align:left"><c:out value='${item.RFC}'/></td>
		    <td style="text-align:left"><c:out value='${item.TELEFONOS}'/></td>
		    <td style="text-align:left"><c:out value='${item.DOMIFISCAL}'/></td>
		    <td style="text-align:left"><c:if test='${item.STATUS==1}'>Activo</c:if><c:if test='${item.STATUS==0}'>Inactivo</c:if></td>
		    
		    <td align="center">&nbsp;<img style="cursor:pointer" src="../../imagenes/pdf2.png" alt="Ver Documento" border="0" width="14" height="16" onclick="getReporteBenefi(<c:out value='${item.ID_BENEFICIARIO}'/>)" />
		    	<img src="../../imagenes/page_white_edit.png" alt="" style="cursor:pointer" title="Editar / Abrir" onclick="nuevoEditarBeneficiario(<c:out value='${item.ID_BENEFICIARIO}'/>)" />
		    	<c:if test='${item.STATUS==1}'> <img style="cursor:pointer" src="../../imagenes/cross.png" title="Deshabilitar" border="0" width="16" height="16" onclick="deshabilitarBeneficiario(<c:out value='${item.ID_BENEFICIARIO}'/>)" /></c:if>
		    	<c:if test='${item.STATUS==0}'> <img style="cursor:pointer" src="../../imagenes/accept.png" title="Reactivar" border="0" width="16" height="16" onclick="habilitarBeneficiario(<c:out value='${item.ID_BENEFICIARIO}'/>)" /></c:if>
     		</td>
    
  		</tr>
  		<c:set var="cont" value="${cont+1}"/>
 		</c:forEach>
  		
  	</table>
  	<div class="alert alert-info">
       	<strong>Total de registros encontrados: <c:out value='${CONTADOR}'/></strong><br/>
    </div>
  	
  </div><!-- Cierre del container -->
</form>
</body>
</html>