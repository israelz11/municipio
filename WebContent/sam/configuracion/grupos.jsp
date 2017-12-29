<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<title>Catalogo de Grupos</title>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="../../include/css/bootstrap-3.3.7.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap-select.css" type="text/css">

<link rel="stylesheet" href="../../include/css/sweetalert2.css" type="text/css">
<!--<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">-->

<script type="text/javascript" src="../../include/js/jquery-2.1.3.min.js"></script>	
<script type="text/javascript" src="../../include/js/bootstrap-3.3.7.js"></script>
<script type="text/javascript" src="../../include/js/bootstrap-select.js"></script>
<script type="text/javascript" src="../../include/js/sweetalert2.js"></script>

<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>

<script type="text/javascript" src="../../dwr/interface/controladorGruposRemoto.js"> </script>




<script type="text/javascript" src="../../dwr/engine.js"> </script>  

<script type="text/javascript" src="grupos.js?x=<%=System.currentTimeMillis()%>"></script>

<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="../../include/js/otros/productos.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js?x=<%=System.currentTimeMillis()%>"></script>

<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
-->

<!-- 
	<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
	<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
	<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" type="text/css" />
	<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />
	<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
	<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
 -->
<!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->
<body >
<form class="form-horizontal" name="forma" method="get" action="" onSubmit=" return false" >
<div style="width:1200px; margin-left:auto; margin-right:auto" class="container"> 
	<div class="row col-md-offset-2">
    	<h1 class="h1-encabezado"> Configuración - Grupos</h1>
    </div>  
    <div class="well">
    <br>
    	<div class="form-group">
        	  <label for="tipo" class="col-md-2 control-label">Grupo:</label>
	          <div class="col-md-5">
   				 <select name="tipo" id="tipo" class="comboBox form-control small"  style="width:250px">
					 <option value="0">[Seleccione]</option>
			    	     <c:forEach items="${tipoGrupos}" var="item" varStatus="status">
		            	 <option value="<c:out value='${item.TIPO}'/>" >
	        	        <c:out value="${item.TIPO}"/>
            	    </option>
    			        </c:forEach>
		        </select>
			  </div>
        </div>
        <div class="form-group">
        	<label for="tipo" class="col-md-2 control-label">Descripcion:</label>
            <div class="col-md-5">
            	<input name="descripcion" type="text" class="form-control" id="descripcion" value="" maxlength="100" onBlur="upperCase(this)" style="width:400px" />
	      	    <input type="hidden" name="clave" id="clave" />
            </div>
        </div> 
        <div class="form-group">
        	<label for="gpoestatus" class="col-md-2 control-label">Estatus:</label>
				<div class="col-md-5">	
    	   			<input name="gpoestatus" type="checkbox" id="gpoestatus" value="1" checked>&nbsp;Activo
	            </div>   
		</div>
        <div class="form-group">
        	<div class="col-md-3 col-md-offset-2">
 				<input type="button"  class="btn btn-success" id="btnGrabar"  name="btnGrabar" value="Guardar" style="width:100px" />
			    <input type="button"  class="btn btn-default"  id="btnlimpiar" name="btnlimpiar" value="Nuevo" style="width:100px"/>
         	</div>
	    </div>  
</div>
<!-- width="90%"  border="0" align="center"  cellpadding="0" cellspacing="0" -->
  <table class="table table-hover table table-condensed"  id="detallesTabla" >
    <thead class="thead-inverse">
      <tr >
        <th width="4%" height="20" style="text-align:center" ><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminar()" style='cursor: pointer;'></th>
        <th width="43%" align="left">&nbsp;Nombre del grupo</th>
        <th width="21%" style="text-align:center">Tipo</th>
        <th width="21%" style="text-align:center">Estatus</th>
        <th width="11%" style="text-align:center"><input name="estatus2" type="checkbox" id="estatus2" checked onClick="pintarTablaDetalles();">
Activos</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
</div>  
</form>
</body>
</html>
