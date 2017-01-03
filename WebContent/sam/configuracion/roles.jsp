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
<script type="text/javascript" src="../../dwr/interface/controladorRolRemoto.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/util.js"></script>    
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="roles.js"> </script>
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
<form name="forma" id="forma" method="post" action="roles.action" onSubmit=" return false"  >
  <table width="96%" align="center">
  <tr><td><h1>Configuración - Roles del sistema</h1></td></tr></table>
<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
      <tr>
        <td colspan="2" align="right" >&nbsp;</td>
      </tr>
      <tr>
        <th width="147" height="25" align="left" ><input type="hidden" name="id" id="id" />
        Descripci&oacute;n:</th>
        <td width="671" align="left" ><input name="descripcion" type="text" class="input" id="descripcion" style="width:445px"/>
        </td>
      </tr>
      <tr>
        <th height="25" align="left" >Estatus:</th>
        <td align="left" ><input name="estatus" type="checkbox" id="estatus" value="1" checked>Activo</td>
      </tr>
      <tr>
        <th height="25" align="left" >&nbsp;</th>
        <td align="left" ><input type="button" name="btnCancelar2" value="Guardar" class="botones" style="width:90px" onClick="guardarDatos()" />
        <input type="button" name="btnCancelar22" value="Limpiar" class="botones" style="width:90px" onClick="limpiarForma()" /></td>
      </tr>
      <tr>
        <th colspan="2" align="left" >&nbsp;</th>
      </tr>
    </table>
<br />
    <table width="96%" border="0" align="center" cellpadding="0" cellspacing="0" class="listas"  id="tablaTipoRol" >
		  <thead>
          <tr id="">
            <th width="8%" height="20" align="center"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminarDatos()" style='cursor: pointer;'></th>
            <th width="62%" align="center">Descripción</th>
            <th width="23%" align="center">Estatus</th>
            <th width="7%" align="center"></th>
          </tr>
      </thead>
			<tbody>			
	      <c:forEach items="${roles}" var="item" varStatus="status" >
          <tr >
            <td align="center"><input type="checkbox" name="claves" id='claves' value="<c:out value='${item.ID_ROL}'/>" ></td>
            <td align="left"><c:out value="${item.ROL_DESCRIPCION}"/></td>
            <td align="center"><c:out value="${item.ROL_ESTADO}"/></td>
            <td align="center"><img src="../../imagenes/page_white_edit.png" style="cursor: pointer;" alt="Editar registro" width="18" height="18" border="0" onClick='modificarDatos(<c:out value="${item.ID_ROL}"/>)' ></td>
          </tr>
		  </c:forEach>
		  </tbody>
  </table>
</form>
</body>