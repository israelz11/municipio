<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Avances fisicos de proyectos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<!--<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">-->
<link rel="stylesheet" href="../../include/css/bootstrap.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorAsignacionGruposUsuariosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>
<script type="text/javascript" src="../../dwr/util.js"> </script> 
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="configuracion_grupos_usuarios.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
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
    	   <h1 class="h1-encabezado">Configuración - Asignación grupos a usuarios</h1>
       </div>  
       <div class="well">
       		<div class="form-group">
        		<label for="cbounidad" class="col-md-2 control-label">Unidad:</label>
		        <div class="col-md-5">
		            <select name="cbounidad" class="comboBox form-control small" id="cbounidad" style="width:445px;" onChange="actualizarUsuarios()">
				        <option value="0">[Seleccione]</option>
					        <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> <option value='<c:out value="${item.ID}"/>' 
                    		<c:if test='${item.ID==idUnidad}'> selected </c:if>>
				            <c:out value='${item.DEPENDENCIA}'/>
				        </option>
					        </c:forEach>
			      	</select>
         		</div>
        	</div> 
            <div class="form-group">
            	<label for="usuario" class="col-md-2 control-label">Usuario:</label>
            	<div class="col-md-5">
                	<select name="usuario" size="1" class="comboBox form-control small" id="usuario" onChange="pintarTablaDetalles()" style="width:445px;">
			          <option value="">[Seleccione]</option>
			    	      <c:forEach items="${usuarios}" var="item" varStatus="status">
            			  <option value="<c:out value='${item.CVE_PERS}'/>" >
			              <c:out value="${item.NOMBRE_COMPLETO}"/>
        	          </option>
    			        </c:forEach>
			      </select>
		         <input type="hidden" name="clave" id="clave" />
                </div>
            </div>
            <div class="form-group">
            	<label for="tipo" class="col-md-2 control-label">Tipo de grupos:</label>
            	<div class="col-md-5">
                	<select name="tipo" class="comboBox form-control small" id="tipo" onChange="pintarTablaDetalles()" style="width:222px;">      
				        <option value="">[Seleccione]</option>        
					        <c:forEach items="${tipoGrupos}" var="item" varStatus="status">
				            <option value="<c:out value='${item.TIPO}'/>" >
            			    <c:out value="${item.TIPO}"/>
		                </option>
		    		        </c:forEach>
		         	</select>
             	</div>
            </div>
            <div class="row col-md-offset-2">
            	<input type="button"  class="btn-sm btn-success"  name="btnGrabar" value="Guardar"  onClick="guardar()" style="width:100px"/>
				<input type="button"  class="btn-info btn-sm"  name="btnlimpiar" value="Nuevo"  onClick="limpiar()" style="width:100px"/>      
  			</div>
     </div>

  <table class="table">
    <tr>
      <th height="30">Grupos Disponibles:</th>
      <td><table width="100%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="table table-hover table table-condensed"  id="detallesTabla" >
        <thead class="thead-inverse">
          <tr >
            <th width="7%" height="20">&nbsp;</th>
            <th width="79%" align="center" >Grupo</th>
            <th width="14%"><div id="idGrupoTab" style="display:none">Default</div></th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table></td>
   </table>
</div>
</form>
</body>
</html>
