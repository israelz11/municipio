<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Configuración de Firmas</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!--<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">-->
<link rel="stylesheet" href="../../include/css/bootstrap.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	

<script type="text/javascript" src="../../include/js/bootstrap-3.3.6.js"></script>
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorGruposTipoOpRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="configuracion_grupos_tipo_op.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
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
<form class="form-horizontal" name="forma" method="get" action="" onSubmit=" return false" >
<br />
	<div style="width:1200px; margin-left:auto; margin-right:auto" class="container"> 
	   <div class="row col-md-offset-2">
    	   <h1 class="h1-encabezado"> Configuración - Grupos de tipos de Ordenes de Pago</h1>
       </div>  
       <div class="well">
       <br>
       		<div class="form-group">
	          <label for="grupo" class="col-md-2 control-label">Grupo:</label>
    	      <div class="col-md-5">
   				 <select name="grupo" size="1" class="comboBox form-control small" id="grupo" onChange="pintarTablaDetalles()" style="width:445px;">
			       <option value="">[Seleccione]</option>
			          <c:forEach items="${grupos}" var="item" varStatus="status">
		              <option value="<c:out value='${item.ID_GRUPO_CONFIG}'/>" >
        		      <c:out value="${item.GRUPO_CONFIG}"/>
                	</option>
			          </c:forEach>
		      	 </select>
	          </div>
    	    </div> 
          	<div class="form-group">
	          <div class="col-md-2 col-md-offset-2">
 				 <input type="button"  class="btn btn-success btn-sm"  name="btnGrabar" value="Guardar"  onClick="guardarValesGrupos()" style="width:100px"/>
        	  </div>
		    </div>  
  		</div>
        <div>
        	
    			<table width="100%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="table table-hover table table-condensed"  id="detallesTabla" >
			        <thead class="thead-inverse">
				        <tr >
	                        
            				<th width="20%" style="text-align: right;" height="20"><input type="checkbox" name="todos" id="todos" ></th>
                            <th width="30%"  >Descripción</th>
                            <th width="30%" align="left"></th>
            			</tr>
			        </thead>
			        <tbody>
			        </tbody>
		      </table>
           
		</div>
	</div>
<br />
</form>
</body>
</html>
