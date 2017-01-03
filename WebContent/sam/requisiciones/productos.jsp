<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Avances fisicos de proyectos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />
<script type="text/javascript" src="../../dwr/interface/controladorArticulosPartidaRemoto.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/util.js"></script>    
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="productos.js"> </script>
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
<style type="text/css"> 
	@import url("../../include/css/calendar/calendar-win2k-cold-1.css"); 
</style>
<body>
<form name="forma" id="forma" method="post" action="" onSubmit=" return false"  >
<input type="hidden" name="id" id="id"   />
<table width="96%" align="center"><tr><td><h1>Administración - Catálogo de Artículos  por Partidas</h1></td></tr></table>
<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario"   >
      <tr>
        <td colspan="2" align="right" >&nbsp;</td>
      </tr>
      <tr>
        <th width="15%" height="30" align="left" >Partida :</th>
        <td width="85%" height="30" align="left" ><select name="partida" class="comboBox" id="partida" style="width:675px;" onChange="getSelectPartida();" >
          <option value="">[Seleccione]</option>
          <c:forEach items="${partidas}" var="item"> 
          <option value='<c:out value="${item.CLV_PARTID}"/>'>
            <c:out value="${item.CLV_PARTID}"/>-<c:out value='${item.PARTIDA}'/>
            </option>
          </c:forEach>
        </select></td>
      </tr>
      <tr>
        <th height="30" align="left" >Grupo :</th>
        <td height="30" align="left" ><select name="subGrupo" class="comboBox" id="subGrupo"  style="width:250px;">
          <option value="">[Seleccione]</option>
        </select>
        <input name="btnGuardar2" type="button" class="botones" id="btnGuardar2" style="width:230px" onclick="buscarArticulosPartidaGrupo()" value="Mostrar Productos de la Partida y Grupo" /></td>
      </tr>
      <tr>
        <th height="30" align="left" ><p>Buscar Productos para agregar:</p></th>
        <td height="30" align="left" ><label>
          <input type="text" name="alfabetico" id="alfabetico" onKeyPress="keyEnter(function(){llenarTabla()});" />
          <input name="btnGuardar3" type="button" class="botones" id="btnGuardar3" style="width:90px" onclick=" llenarTabla()" value="Buscar" />
        </label></td>
      </tr>
      <tr>
        <th height="30" colspan="2" align="left" ><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listas"  id="detallesListas"  >
          <thead>
            <tr id="tr">
              <th width="10%" height="20"  align="center" >En Partida </th>
              <th width="10%" align="center" >Estatus</th>
              <th width="50%" align="center" >Artículos</th>
              <th width="15%" align="center" >Ultima Unidad de Medida</th>
              <th width="15%" align="center" >Ultimo Precio</th>
            </tr>
          </thead>
          <tbody>
          </tbody>
        </table></th>
      </tr>
      <tr id="filaguardar">
        <td height="35" colspan="2" align="left" ><table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td align="center"><input name="btnGuardar" type="button" class="botones" id="btnGuardar" style="width:120px" onClick="guardarDato()" value="Guardar cambios" /></td>
          </tr>
        </table></td>
      </tr>
  </table>
<br />
</form>
</body>