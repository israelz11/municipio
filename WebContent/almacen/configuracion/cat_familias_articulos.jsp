<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de familia de articulos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorFamiliasArticulosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="cat_familias_articulos.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<body >
<form name="forma" method="get" action="" onSubmit=" return false" >
<br />
  <table width="90%" align="center"><tr><td><h1>Administración - Catálogo de familias de artículos</h1></td></tr></table>
  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <td height="30">&nbsp;</td>
      <td><input type="hidden" value="" id="ID_FAMILIA"></td>      
    </tr>
    <tr>
      <th width="16%" height="30">&nbsp;Nombre de la familia:</th>
      <td width="84%"><input name="txtdescripcion" type="text" class="input" id="txtdescripcion" value="" size="70" maxlength="150"  /></td>
    </tr>
    <tr>
      <th height="30">&nbsp;Status</th>
      <td><input type="checkbox" name="chkstatus" id="chkstatus"></td>
    </tr>
    <tr>
      <td height="30">&nbsp;</td>
      <td rowspan="2">
      <table width="300" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="132" align="center"><div class="buttons tiptip">
            <div class="buttons tiptip">
              <button name="cmdnuevo" id="cmdnuevo" onClick="limpiar()" type="button" class="button red middle"><span class="label" style="width:100px">Nuevo</span></button>
            </div>
          </div></td>
          <td width="168"><button name="cmdguardar" id="cmdguardar" onClick="guardar()" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button></td>
        </tr>
      </table></td>
    </tr>
    <tr>
      <td height="30"><p>&nbsp;</p></td>
    </tr>
  </table>
  <br />
  <table width="90%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas"  id="detalles_FamiliasArticulos" >
    <thead>
      <tr >
        <th width="7%" height="20" ><img src="../../imagenes/cross.png" width="16" height="16" onClick="borrarFamiliasArticulos()" style='cursor: pointer;' alt='Deshabilitar tipo(s) de documento(s)'></th>
        <th width="74%"  align="center" ><div align="left">&nbsp;Familias</div></th>
        <th width="14%"  >Status</th>
        <th width="5%"  >Opc.</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
</form>
</body>
</html>
