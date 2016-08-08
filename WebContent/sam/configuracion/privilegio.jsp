<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Avances fisicos de proyectos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />
<script type="text/javascript" src="../../dwr/interface/controladorModuloRemoto.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorPrivilegioRemoto.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/util.js"></script>    
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="privilegio.js"></script>
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
<body>
<form name="forma" id="forma" method="post" action="privilegio.action" onSubmit=" return false" >
  <table width="96%" align="center">
  <tr><td><h1>Configuración - Privilegios de los sistemas</h1></td></tr></table>
<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0"  class="formulario" >
      <tr>
        <td colspan="2" align="right" >&nbsp;</td>
      </tr>
      <tr>
        <th height="25" align="left" >Sistema:
          <input type="hidden" name="id" id="id"   /></th>
        <td align="left" ><select name="sistema" class="comboBox" id="sistema" onchange="getSelectSistema()" style="width:222px;">
          <option value="0" >[Seleccione]</option>
          <c:forEach items="${sistemas}" var="item">
        <option value ='<c:out value="${item.ID_SISTEMA}"/>'  >
        <c:out value="${item.SIS_DESCRIPCION}"/>
        </option>
      </c:forEach>
        </select></td>
      </tr>
      <tr>
        <th height="25" align="left" >Modulo:</th>
        <td align="left" ><select name="modulo" class="comboBox" id="modulo" onchange="llenarTabla()" style="width:222px;">
          <option value="0" >[Seleccione]</option>
        </select></td>
      </tr>
      <tr>
        <th width="12%" height="25" align="left" >Descripci&oacute;n:</th>
        <td width="88%" align="left" ><input name="descripcion" type="text" class="input" id="descripcion" style="width:445px;"/></td>
    </tr>
      <tr>
        <th height="25" align="left" >Tipo:</th>
        <td align="left" ><label>
          <input name="tipo" type="radio" id="tipo" value="MENU" checked onClick="validarTipo()">
          Menu 
          <input type="radio" name="tipo" id="tipo" value="APLICACION" onClick="validarTipo()">
          Aplicación
        </label></td>
      </tr>
      <tr>
        <th height="25" align="left" >Url:</th>
        <td align="left" ><input name="url" type="text" class="input" id="url" style="width:445px;"/></td>
      </tr>
      <tr>
        <th height="25" align="left" >Orden:</th>
        <td align="left" ><input name="orden" type="text" class="input" id="orden" size="10" maxlength="2" onkeypress=" return keyNumbero( event );" /></td>
      </tr>
      <tr>
        <th height="25" align="left" >Estatus:</th>
        <td align="left" ><input name="estatus" type="checkbox" id="estatus" value="1" checked></td>
      </tr>
      <tr>
        <th height="25" align="left" >&nbsp;</th>
        <td align="left" ><input type="button" name="btnCancelar2" value="Guardar" class="botones" style="width:90px" onClick="guardarDatos()" />
        <input type="button" name="btnCancelar22" value="Limpiar" class="botones" style="width:90px" onClick="limpiarForma()" /></td>
      </tr>
      <tr>
        <td colspan="2" align="left" >&nbsp;</td>
      </tr>
</table><br />
  		<table width="96%" align="center" border="0" cellpadding="0" cellspacing="0" class="listas"  id="detallesTabla" >
		  <thead>
          <tr>
            <th width="3%" height="20" align="center"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminar()" style='cursor: pointer;'></th>
            <th width="4%" align="center">Orden</th>
            <th width="38%" align="center">Descripción</th>
            <th width="21%" align="center">Url</th>
            <th width="21%" align="center">Módulo</th>
            <th width="5%" align="center">Tipo</th>
            <th width="4%" align="center">Estatus</th>
            <th width="4%" align="center"></th>
          </tr>
	      </thead>
			<tbody>			
		  </tbody>
        </table>
</form>
</body>