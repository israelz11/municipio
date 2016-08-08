<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Personas Usuarios</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../dwr/interface/controladorUsuariosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script> 
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="personas_usuarios.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script> 
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
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
<body>
<form name="forma" id="forma" method="post" action="" onSubmit=" return false" >
<input type="hidden" name="clave" id="clave" value="" />
<input type="hidden" name="idUsuario" id="idUsuario"  value=""/>
<input type="hidden" name="idTrabajador" id="idTrabajador"  value=""/>
<br />
  <table width="80%" align="center"><tr><td><h1>Configuración - Administración personas y usuarios</h1></td></tr></table>
  <table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <td colspan="4" class="TituloFormulario">Datos Personales</td>
    </tr>
    <tr>
      <th width="182" height="26" align="right">*Nombre completo:</th>
      <td colspan="3"><input name="nombre" class="input" type="text" id="nombre" maxlength="50" onBlur="upperCase(this)" style="width:300px;"></td>
    </tr>
    <tr>
      <th height="25" align="right">*Apellido Paterno:</th>
      <td colspan="2"><input name="apaterno" type="text" class="input" id="apaterno" value="" maxlength="20" onBlur="upperCase(this)"  style="width:300px;"/></td>
      <td><input  name="btnBuscar" type="button"  class="botones" id="btnBuscar"  onClick="buscar()" value="Buscar" /></td>
    </tr>
    <tr>
      <th height="25" align="right">*Apellido Materno:</th>
      <td colspan="2"><input name="amaterno" type="text" class="input" id="amaterno" value="" maxlength="20" onBlur="upperCase(this)"  style="width:300px;"/></td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th height="25" align="right">CURP:</th>
      <td width="406"><input name="curp" type="text" class="input" id="curp" value="" maxlength="20" onBlur="upperCase(this)"  style="width:150px;"/></td>
      <th width="423" align="right">&nbsp;</th>
      <td width="343">&nbsp;</td>
    </tr>
    <tr>
      <th height="25" align="right">RFC:</th>
      <td colspan="3"><input name="rfc" type="text" class="input" id="rfc" value=""  maxlength="15" onBlur="upperCase(this)" style="width:150px;" /></td>
    </tr>
    <tr>
      <th height="25" align="right">Profesión:</th>
      <td colspan="3"><select name="profesion"  class="comboBox" id="profesion" style="width:111px;">
        <option value="">[Seleccione]</option>
        <c:forEach items="${profesiones}" var="item" varStatus="status">
          <option value="<c:out value='${item.PROFESION}'/>" ><c:out value="${item.PROFESION}"/></option>
          </c:forEach>
      </select></td>
    </tr>
    <tr>
      <td colspan="4" class="TituloFormulario">Datos Laborales</td>
    </tr>
    <tr>
      <th height="25" align="right">*Unidades:</th>
      <td colspan="3"><select name="unidad" class="comboBox" id="unidad"  onChange="getSelectUnidad()" style="width:500px;">
        <option value="">[Seleccione]</option>
        <c:forEach items="${unidades}" var="item" varStatus="status">          
        <option value="<c:out value='${item.ID}'/>" ><c:out value="${item.DEPENDENCIA}"/></option>
        </c:forEach>
      </select></td>
    </tr>
    <tr>
      <th height="25" align="right">Areas:</th>
      <td colspan="3"><select name="area" class="comboBox" id="area"  style="width:500px;" disabled>
        <option value="">[Seleccione]</option>
      </select></td>
    </tr>
    <tr>
      <td colspan="4" class="TituloFormulario">Cuenta de usuario </td>
    </tr>
    <tr>
      <th height="25" align="right">*Usuario:</th>
      <td><input name="usuario" type="text" class="input" id="usuario"  value="" maxlength="20" style="width:150px;"/></td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th height="25" align="right">*Contraseña:</th>
      <td><input name="pass1" type="password" class="input" id="pass1" value="" maxlength="20" style="width:150px;"/></td>      <th align="right">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th height="25" align="right">*Confirmar Contraseña :</th>
      <td><input name="pass2" type="password" class="input" id="pass2" value="" maxlength="20"  style="width:150px;"/></td>
      <th align="right">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th height="25" align="right">Estatus:</th>
      <td colspan="3"><input name="estatus" type="checkbox" id="estatus" value="1" checked>Activo</td>
    </tr>
    <tr>
      <th height="25" align="right">&nbsp;</th>
      <td colspan="3"><input type="button"  class="botones"  name="btnGrabar" value="Guardar"  onClick="guardar()" />
      <input type="button"  class="botones"  name="btnlimpiar" value="Nuevo"  onClick="limpiar()" /></td>
    </tr>
    <tr>
      <td colspan="4" align="center">&nbsp;</td>
    </tr>
  </table>
  <br />
  <table width="80%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas"  id="detallesTabla" >
    <thead>
      <tr >
        <th width="2%" height="20">ID</th>
        <th width="11%"  align="center" >Profesion</th>
        <th width="45%"  align="center" >Nombre</th>
        <th width="19%"  >Unidad</th>
        <th width="18%"  >Estatus</th>
        <th width="5%" >&nbsp;</th>
      </tr>
   </thead>
    <tbody>
    </tbody>
  </table>
</form>
</body>
</html>
