<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Configuración de Firmas</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorGruposProyectosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="configuracion_grupos_proyectos.js"></script>
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
<form class="form-horizontal" role="form" name="forma" method="get" action="" onSubmit=" return false" >

  <div style="width:1200px; margin-left:auto; margin-right:auto" class="container"> 
        <div class="row">
          <h1 class="h1"> Configuración - Grupos de proyectos</h1>
        </div>  
        <br>
        <div class="form-group">
          <label for="unidad" class="col-lg-2 control-label">Unidad:</label>
          <div class="col-lg-10">
            <select name="unidad" size="1" class="comboBox form-control"  id="unidad" onChange="pintarTablaDetalles()"  style="width:445px;">
              <option value="">[Seleccione]</option>
              <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">
              <option value="<c:out value='${item.ID}'/>">
                <c:out value="${item.DEPENDENCIA}"/>
                </option>
              </c:forEach>
            </select>
          </div>
        </div> 
        <div class="form-group">
           <label for="grupo" class="col-lg-2 control-label">Grupo:</label>
           <div class="col-lg-10">
               <select name="grupo" size="1" class="comboBox form-control" id="grupo" onChange="pintarTablaDetalles()" style="width:445px;">
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
        
    <table class="table table-hover table table-condensed" cellpadding="0" cellspacing="0" id="detallesTabla" >
      <thead>
        <tr>
          <th width="3%" height="10" class="col-sm-1"><input type="checkbox" name="todos" id="todos" ></th>
          <th width="24%"  align="center" class="col-sm-2">Proyecto</th>
          <th width="40%" class="col-sm-5" style="text-align: left;">Descripción</th>
          <th width="28%" class="col-sm-4" style="text-align: left;">Unidad Administrativa</th>
        </tr>
      </thead>
      <tbody>
       <%-- inicia el contador de lineas en 0 --%>  
              <c:set var="cont" value="${0}" /> 
            <c:forEach items="${roles}" var="item" varStatus="status" >
                <%-- pinta la primer linea--%>  
              <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
                <td align="center"><input type="checkbox" name="claves" id='claves' value="<c:out value='${item.ID_PROYECTO_GRUPO}'/>" ></td>
                <td align="left"><c:out value="${item.ID_GRUPO_CONFIG}"/></td>
                <td align="center"><c:out value="${item.PROYECTO}"/></td>
                    <td align="center"><c:out value="${item.ID_PROYECTO}"/></td>
                <td align="center"><img src="../../imagenes/page_white_edit.png" style="cursor: pointer;" alt="Editar registro" width="18" height="18" border="0" onClick='modificarDatos(<c:out value="${item.ID_PROYECTO_GRUPO}"/>)' ></td>
             </tr>
                 <%-- Incrementa el contador--%>
              <c:set var="cont" value="${cont+1}"/>
          </c:forEach>
        </tbody>
    </table>  
    <div class="form-group">
          <div class="col-lg-10">
             <input type="button"  class="btn btn-success btn-sm" name="btnGrabar" value="Guardar"  onClick="guardarProyectosGrupos()" style="width:100px"/>
          </div>
    </div>  
  </div>
  
</form>
</body>
</html>
