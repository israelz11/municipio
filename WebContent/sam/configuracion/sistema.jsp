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
<script type="text/javascript" src="../../dwr/interface/controladorSistemaRemoto.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/util.js"></script>    
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="sistema.js"> </script>
<body>
<form name="forma" id="forma" method="post" action="" onSubmit=" return false"  >
<input type="hidden" name="id" id="id"   />
<table width="96%" align="center"><tr><td><h1>Configuración - Sistemas</h1></td></tr></table>
<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario"   >
      <tr>
        <td colspan="2" align="right" >&nbsp;</td>
      </tr>
      <tr>
        <th width="147" align="left" ><div align="right">Descripci&oacute;n:</div></th>
        <td width="671" align="left" ><input name="descripcion" type="text" class="input" id="descripcion" style="width:500px"/></td>
      </tr>
      <tr>
        <th height="23" align="left" >Estatus:</th>
        <td align="left" ><input name="estatus" type="checkbox" id="estatus" value="1" checked>
Activo          </td>
      </tr>
      <tr>
        <td align="left" >&nbsp;</td>
        <td align="left" ><input type="button" name="btnCancelar2" value="Guardar" class="botones" style="width:100px" onClick="guardarDatos()" />
        <input type="button" name="btnCancelar22" value="Limpiar" class="botones" style="width:100px" onClick="limpiarForma()" /></td>
      </tr>
      <tr>
        <td height="17" colspan="2" align="left" >&nbsp;</td>
      </tr>
    </table>
<br />
<table width="96%" border="0" align="center" cellpadding="0" cellspacing="0" class="listas"  id="tablaSistema"  >
		  <thead>
          <tr id="">
            <th width="9%" height="20"  align="center"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminarDato()" style='cursor: pointer;'></th>
            <th width="51%" align="center">Descripcion</th>
            <th width="24%" align="center">Estatus</th>
            <th width="16%" align="center"></th>
          </tr>
		    </thead>
			<tbody>			
	      <c:forEach items="${sistemas}" var="item" varStatus="status" >
          <tr >
            <td align="center"><input type="checkbox" name="idSistemas" id='idSistemas' value="<c:out value='${item.ID_SISTEMA}'/>" ></td>
            <td><c:out value="${item.SIS_DESCRIPCION}"/></td>
            <td align="center"><c:out value="${item.SIS_ESTATUS}"/></td>
            <td align="center"><img src="../../imagenes/page_white_edit.png" style="cursor: pointer;" alt="Editar registro" width="18" height="18" border="0" onClick='modificarDato(<c:out value="${item.ID_SISTEMA}"/>)' ></td>
          </tr>
		  </c:forEach>
		  </tbody>
        </table>
</form>
</body>