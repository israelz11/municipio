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
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorRolesPrivilegiosRemoto.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="rolesPrivilegio.js"></script>
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
<form name="forma" id="forma" method="post" action="" onSubmit=" return false"  >
  <table width="96%" align="center">
  <tr><td><h1>Configuración - Asignación de privilegios a roles</h1></td></tr></table>
<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario"   >
      <tr>
        <td colspan="2" align="right" >&nbsp;</td>
      </tr>
      <tr>
        <th width="15%" height="25" align="left" >Rol :</th>
        <td width="85%" align="left" ><select name="roles" class="comboBox" id="roles" onchange="llenarTablaPrivilegios()" style="width:445px;">
          <option value="" >[Seleccione]</option>
          <c:forEach items="${roles}" var="item">
        <option value ='<c:out value="${item.ID_ROL}"/>'  >
        <c:out value="${item.ROL_DESCRIPCION}"/>
        </option>
      </c:forEach>
        </select></td>
      </tr>
      <tr>
        <th height="25" align="left" >Sistema :</th>
        <td align="left" ><select name="sistemas" class="comboBox" id="sistemas" onchange="llenarTablaPrivilegios()" style="width:222px;">
          <option value="" >[Seleccione]</option>
          <c:forEach items="${sistemas}" var="item">
        <option value ='<c:out value="${item.ID_SISTEMA}"/>'  >
        <c:out value="${item.SIS_DESCRIPCION}"/>
        </option>
      </c:forEach>
        </select></td>
      </tr>
      <tr>
        <th height="17" align="left" >&nbsp;</th>
        <td align="left" >&nbsp;</td>
      </tr>
      <tr id="filaPrivilegio" style="display:none">
        <td height="30" colspan="2" align="center" ><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listas"  id="detallesListas"  >
          <thead>
            <tr id="tr">
              <th width="2%"  bgcolor="BEC8D3" id="tablesearch" align="center">&nbsp;</th>
              <th width="3%"  bgcolor="BEC8D3" id="tablesearch">
                <input type="checkbox" name="todos" id="todos">
              </th>
              <th width="95%" height="20" bgcolor="BEC8D3" id="tablesearch">Privilegios</th>
            </tr>
          </thead>
          <tbody>
          </tbody>
        </table></td>
      </tr>
      <tr  id="filaBoton" style="display:none">
        <td height="30" colspan="2" align="center" ><input type="button" name="btnGuardar" value="Guardar selecci&oacute;n" class="botones"  onclick="guardarDato()" /></td>
      </tr>      
    </table>
    <BR />
</form>
</body>