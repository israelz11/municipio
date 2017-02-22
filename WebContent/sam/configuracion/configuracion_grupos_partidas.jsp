<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Configuración de Firmas</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorGruposPartidasRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>
<script type='text/javascript' src='../../dwr/util.js'></script>

<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="configuracion_grupos_partidas.js"></script>
<body >
<form name="forma" method="get" action="" onSubmit=" return false" >
<br />
  <table width="90%" align="center"><tr><td><h1>Configuración - Grupos de partidas</h1></td></tr></table>
  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr>
    <th height="16">&nbsp;</th>
    <td>&nbsp;</td>
  </tr>
  <tr>
      <th height="30">Partida:</th>
      <td><label>
        <select name="capitulo" size="1" class="comboBox" id="capitulo" onChange="pintarTablaDetalles()" style="width:445px;">
          <option value="">[Seleccione]</option>
          <c:forEach items="${capitulo}" var="item" varStatus="status">
              <option value="<c:out value='${item.CLV_CAPITU}'/>">
                <c:out value='${item.CLV_CAPITU}'/>-<c:out value="${item.CAPITULO}"/>
                </option>
            </c:forEach>
        </select>        
      </label></td>
    </tr>
    <tr>
      <th height="30">Grupo:</th>
      <td><label>
        <select name="grupo" size="1" class="comboBox" id="grupo" onChange="pintarTablaDetalles()" style="width:445px;">
          <option value="">[Seleccione]</option>
          <c:forEach items="${grupos}" var="item" varStatus="status">
              <option value="<c:out value='${item.ID_GRUPO_CONFIG}'/>" >
                <c:out value="${item.GRUPO_CONFIG}"/>
                </option>
            </c:forEach>
        </select>
        <input type="button"  class="botones"  name="cboasignar" value="Asignar grupo de proyectos..."  onClick="asignarGrupoProyecto()" style=""/>
      </label></td>
    </tr>
    <tr>
      <th width="15%" height="30">Partidas:</th>
      <td width="85%"><table width="100%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas"  id="detallesTabla" >
        <thead>
          <tr >
            <th width="4%" height="20"><input type="checkbox" name="todos" id="todos" ></th>
            <th width="14%"  align="center" >Partida</th>
            <th width="82%"  >Descripción</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
      </table></td>
    </tr>
    <tr>
      <th height="30">&nbsp;</th>
      <td><input type="button"  class="botones"  name="btnGrabar" value="Guardar"  onClick="guardarProyectosGrupos()" style="width:100px"/></td>
    </tr>
    <tr>
      <td colspan="2" align="center">&nbsp;</td>
    </tr>
  </table>
  <br />
</form>
</body>
</html>
