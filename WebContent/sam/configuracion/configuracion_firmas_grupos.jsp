<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Configuración de Firmas</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<!--<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">-->
<link rel="stylesheet" href="../../include/css/bootstrap.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorFirmasGruposRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="configuracion_firmas_grupos.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="../../include/js/otros/productos.js"></script>
<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
<!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->
<body >
<form name="forma" method="get" action="" onSubmit=" return false" class="form-horizontal">
<br />
  <div style="width:1200px; margin-left:auto; margin-right:auto" class="container"> 
  	
  		<div class="row col-md-offset-2">
        	<h1 class="h1-encabezado"> Configuración - Grupos de Firmas</h1>
	    </div>
        <div class="well">
    	<div class="form-group" id="combos">
	    	<label for="inputName" class="control-label col-xs-2">Grupo:</label>
	        <div class="col-xs-5">
    	   		 <select name="grupo" size="1" id="grupo" class="form-control" onChange="pintarTablaDetalles()" border="0" style="width:400px;">
        	        <option value="">[Seleccione]</option>
           	        <c:forEach items="${grupos}" var="item" varStatus="status">
                	<option value="<c:out value='${item.ID_GRUPO_CONFIG}'/>" >
                    <c:out value="${item.GRUPO_CONFIG}"/>
	                </option>
    	            </c:forEach>
        	    </select>
            	<input type="hidden" name="clave" id="clave" />
	        </div>
    	</div>
	    <div class="form-group">
    		<label for="inputName" class="control-label col-xs-2">Tipo de frma:</label>
	    	<div class="col-xs-5">
    			<select name="tipoFirma" size="1" class="form-control"  id="tipoFirma" style="width:400px;">      
	        	    <option value="">[Seleccione]</option>
    	        	<c:forEach items="${tipoFirmas}" var="item" varStatus="status">
	    	        <option value="<c:out value='${item.TIPO}'/>" >	
    	    	    <c:out value="${item.TIPO}"/>
        	    	</option>
	            	</c:forEach>
    		   </select>
       		</div>	
	    </div>
    	<div class="form-group">
    		 <label for="inputName" class="control-label col-xs-2">Representante:</label>
	         <div class="col-xs-5">
    	         <input type="text" name="representante" id="representante" value="" class="form-control" placeholder="Nombre del representante" style="width:400px;">
        	 </div>
		</div>
    	<div class="form-group">
        	 <label for="inputName" class="control-label col-xs-2">Cargo:</label>
	         <div class="col-xs-5">
    	         <input type="text" name="cargo" id="cargo" value="" class="form-control" placeholder="Cargo del representante" style="width:400px;"><br>
        	     <input type="button"  class="btn btn-success btn-sm"  name="btnGrabar" value="Guardar"  onClick="guardar()" style="width:100px"/>
    	    	 <input type="button"  class="btn btn-info btn-sm"  name="btnlimpiar" value="Nuevo"  onClick="limpiar()" style="width:100px"/>
	         </div>
		</div>	
       
	</div>  
	 
  	<table class="table table-hover table table-condensed" cellpadding="0" cellspacing="0" id="detallesTabla" >
    	<thead>
	      <tr>
    	    <th width="3%" height="20" class="col-sm-1"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminar()" style='cursor: pointer;'></th>
	        <th width="24%" class="col-sm-4" style="text-align: left;">Tipo de firmas</th>
    	    <th width="40%" class="col-sm-3" style="text-align: left;">Representante</th>
        	<th width="28%" class="col-sm-3" style="text-align: left;" >Cargo</th>
	        <th width="5%" class="col-sm-1">&nbsp;</th>
    	  </tr>
	    </thead>
    	<tbody>
	    </tbody>
  </table>
</div>
</form>
</body>
</html>
