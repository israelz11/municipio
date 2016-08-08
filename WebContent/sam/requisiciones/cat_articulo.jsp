<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Avances fisicos de proyectos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorArticulosRemoto.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>   
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/util.js"></script>    
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="cat_articulo.js"> </script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
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
<body>
<form name="forma" id="forma" method="post" action="" onSubmit="return false;">
<input type="hidden" name="id" id="id"   />
<table width="96%" align="center"><tr><td><h1>Administración - Catálogo General de Artículos</h1></td></tr></table>
<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario"   >
      <tr>
        <td colspan="2" align="right" >&nbsp;</td>
      </tr>
      <tr>
        <th width="15%" height="30"  align="left" >Descripci&oacute;n :</th>
        <td width="85%" height="30"  align="left" ><input name="descripcion" type="text" class="input" id="descripcion" style="width:445px" onBlur="upperCase(this)" /></td>
      </tr>
      <tr>
        <th height="30" align="left" >Ultima Unidad de Medida :</th>
        <td height="30" align="left" ><input name="txtunidadmedida" type="text" class="input" id="txtunidadmedida" style="width:200px" value="" />
        <input name="CVE_UNIDAD_MEDIDA" type="hidden" id="CVE_UNIDAD_MEDIDA" value="" /></td>
      </tr>
      <tr>
        <th height="30" align="left" > Ultimo Precio :</th>
        <td height="30" align="left" ><input name="precio" type="text" class="input" id="precio" /></td>
      </tr>
      <tr>
        <th height="30" align="left" >Ultimo Beneficiario :</th>
        <td height="30" align="left" ><input type="text" id="txtprestadorservicio" class="input" style="width:445px"/><input type="hidden" id="CVE_BENEFI" value="0" /></td>
      </tr>
      <tr>
        <th height="30" align="left" > Inventariable :</th>
        <td height="30" align="left" ><input name="inventariable" type="checkbox" id="inventariable" value="1" checked>
        Si</td>
      </tr>
      <tr>
        <th height="27" align="left" >Consumible</th>
        <td height="27" align="left" ><input name="consumible" type="checkbox" id="consumible" value="1" checked>
          Si</td>
      </tr>
      <tr>
        <th height="26" align="left" >Estatus</th>
        <td height="26" align="left" ><input name="estatus" type="checkbox" id="estatus" value="1" checked></td>
      </tr>
      <tr>
        <th height="30" align="left" >&nbsp;</th>
        <td height="30" align="left" ><input name="btnGuardar" type="button" class="botones" id="btnGuardar" style="width:90px" onClick="guardarDatos()" value="Guardar" />
        <input name="btnLimpiar" type="button" class="botones" id="btnLimpiar" style="width:90px" onClick="limpiarForma()" value="Limpiar" /></td>
      </tr>
      <tr>
        <td height="21" colspan="2" align="left" >&nbsp;</td>
      </tr>
  </table>
<br />
<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario"   >
<tr>
  <th height="14">&nbsp;</th>
  <td>&nbsp;</td>
</tr>
<tr> <th width="15%" height="20">Descripción :</th>
  <td width="85%"><label>
    <input type="text" name="alfabetico" id="alfabetico" style="width:200px">
    <input name="btnGuardar2" type="button" class="botones" id="btnGuardar2" style="width:90px" onClick="llenarTabla()" value="Buscar" />
  </label></td>
</tr>
<tr>
  <th height="13">&nbsp;</th>
  <td>&nbsp;</td>
</tr>
<tr> <td colspan="2">
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listas"  id="tablaArticulos"  >
    <thead>
      <tr id="">
        <th width="3%" height="20" align="center"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminarDato()" style='cursor: pointer;'></th>
        <th width="33%" align="center">Descripción</th>
        <th width="21%" align="center">Ultimo Beneficiario</th>
        <th width="15%" align="center">Unida de Medida</th>
        <th width="12%" align="center">Precio</th>
        <th width="10%" align="center">Estatus</th>
        <th width="6%" align="center"></th>
        </tr>
      </thead>
    <tbody>				     
      </tbody>
    </table>
</td>
  </tr>
</table>
</form>
</body>