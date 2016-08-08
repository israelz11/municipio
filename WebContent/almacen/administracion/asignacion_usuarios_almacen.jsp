<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de Almacenes</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script>  
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorAlmacenRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorAsignacionAlmacenRemoto.js"> </script>
<script type="text/javascript" src="asignacion_usuarios_almacen.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<body >
<form name="forma" method="get" action="" onSubmit=" return false" >
<br />
  <table width="90%" align="center"><tr><td><h1>Administración - Asignación de usuarios a almacenes</h1></td></tr></table>
  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <th height="15">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th width="24%" height="30">Unidad Administrativa :</th>
      <td width="76%">
      <div class="styled-select">
      <select name="unidad" class="comboBox" id="unidad" onChange="getEmpleados()" style="width:445px;">
      <option value="">[Seleccione]</option>
         <c:forEach items="${unidades}" var="item" varStatus="status">
              <option value="<c:out value='${item.ID}'/>" >
                <c:out value="${item.DEPENDENCIA}"/>
            </option>
          </c:forEach>
        </select>
        </div>
      </td>
    </tr>
    <tr>
      <th height="30">Almacen :</th>
      <td>
      <div class="styled-select">
      <select name="almacen" class="comboBox" id="almacen" onChange="pintarTablaDetalles()" style="width:445px;">
        <option value="">[Seleccione]</option>
        
      </select>
      </div>
      </td>
    </tr>
    <tr>
      <th height="30">Responsable :</th>
      <td>
      <div class="styled-select">
      <select name="responsable" class="comboBox" id="responsable" style="width:445px;">
      <option value="0">[Seleccione]</option>
      </select>
      </div></td>
    </tr>
    <tr>
      <th height="30">&nbsp;</th>
      <td>
      <table width="300" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="132" align="center"><div class="buttons tiptip">
            <div class="buttons tiptip">
              <button name="btnlimpiar" id="btnlimpiar" onClick="limpiar()" type="button" class="button red middle"><span class="label" style="width:100px">Nuevo</span></button>
            </div>
          </div></td>
          <td width="168"><button name="btnGrabar" id="btnGrabar" onClick="guardar()" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button></td>
        </tr>
      </table></td>
    </tr>
    <tr>
      <td colspan="2" align="center">&nbsp;</td>
    </tr>
  </table>
  <br />
  <table width="90%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas"  id="detallesTabla" >
    <thead>
      <tr >
        <th width="6%" height="20" ><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminar()" style='cursor: pointer;'></th>
        <th width="54%"  align="center" >Responsable</th>
        <th width="40%"  >Almacen</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
</form>
</body>
</html>
