<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de Subdirecciones</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorSubdireccionesRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="cat_subdirecciones.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<body >
<form name="forma" method="get" action="" onSubmit=" return false" >
<br />
  <table width="90%" align="center"><tr><td><h1>Administración - Catálogo de subdirecciones</h1></td></tr></table>
  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <td height="30">&nbsp;</td>
      <td><input type="hidden" name="ID_SUBDIRECCION" id="ID_SUBDIRECCION" value=''/></td>      
    </tr>
    <tr>
      <th height="30">&nbsp;Unidad Administrativa:</th>
      <td><strong style="color:#06F"><c:out value="${nombreUnidad}"/>
        <input type="hidden" name="ID_UNIDAD" id="ID_UNIDAD" value='<c:out value="${idUnidad}"/>'/>
      </strong></td>
    </tr>
    <tr>
      <th width="13%" height="30">&nbsp;Nombre de la subdirección:</th>
      <td width="87%"><input name="txtdescripcion" type="text" class="input" id="txtdescripcion" value="" size="70" maxlength="150" style="width:350px"/></td>
    </tr>
    <tr>
      <th height="30">&nbsp;Encargado de la Sub.:</th>
      <td><input name="txtencargado" type="text" class="input" id="txtencargado" value="" size="70" maxlength="150" style="width:350px"/></td>
    </tr>
    <tr>
      <th height="30">&nbsp;Puesto del encargado:</th>
      <td><input name="txtpuesto" type="text" class="input" id="txtpuesto" value="" size="70" maxlength="150" style="width:350px"/></td>
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
              <button name="cmdnuevo" id="cmdnuevo" type="button" class="button red middle"><span class="label" style="width:100px">Nuevo</span></button>
            </div>
          </div></td>
          <td width="168"><button name="cmdguardar" id="cmdguardar" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button></td>
        </tr>
      </table></td>
    </tr>
    <tr>
      <td height="30"><p>&nbsp;</p></td>
    </tr>
  </table>
  <br />
  <table width="90%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas"  id="detalles_Subdirecciones" >
    <thead>
      <tr >
        <th width="4%" height="20" ><img src="../../imagenes/cross.png" width="16" height="16" onClick="borrarSubdirecciones()" style='cursor: pointer;' alt='Deshabilitar tipo(s) de documento(s)'></th>
        <th width="37%"  align="left" ><div align="left">&nbsp;Subdirección</div></th>
        <th width="24%"  align="left" ><div align="left">&nbsp;Encargado</div></th>
        <th width="23%"  align="left" ><div align="left">&nbsp;Puesto</div></th>
        <th width="8%">Status</th>
        <th width="4%">Opc.</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
</form>
</body>
</html>
