<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Solicitudes de Articulos</title>
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
<script type="text/javascript" src="../../dwr/interface/controladorSolicitudesRemoto.js"> </script>
<script type="text/javascript" src="solicitudes.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
</head>
<body>
 <table width="95%" align="center"><tr><td><h1>Salidas - Solicitudes a almacén</h1></td></tr></table>
<form action="" method="post" name="forma" id="forma" >
  <input name="clave" type="hidden" id="clave" value="">  
  <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="captura" >
<tr >
  <th height="17" >&nbsp;</th>
  <td>&nbsp;</td>
</tr>
<tr >
  <th height="30" ><input name="almacen2" type="hidden" id="almacen2" value='<c:out value="${item.ID_ALMACEN}"/>'>
    Almacen:</th>
  <td>  
  <div class="styled-select">
  <select name="cboalmacen" id="cboalmacen"  class="comboBox"  onChange="getSolicitudesPendientes()" style="width:445px;">
  		<option value="0">[Seleccione]</option>
           <c:forEach items="${almacenes}" var="item" varStatus="status">
           <option value="<c:out value='${item.ID_ALMACEN}'/>" ><c:out value="${item.DESCRIPCION}"/>
           </option>
        </c:forEach>
        </select>
   </div>
  </td>
</tr>
<tr >
		      <th width="20%" height="30" >Partida:</th>
		      <td width="80%">
              <div class="styled-select">
              <select name="partida" class="comboBox" id="partida" style="width:445px;">
		        <option value="">[Seleccione]</option>
               <c:forEach items="${partidasAsignadas}" var="item" varStatus="status">
          <option value="<c:out value='${item.CLV_PARTID}'/>" >
            <c:out value='${item.CLV_PARTID}'/>-<c:out value="${item.PARTIDA}"/>
            </option>
        </c:forEach>
      </select>
      </div>
      </td>
    </tr>
<tr >
  <th height="30" >&nbsp;</th>
  <td><div class="buttons tiptip"><button name="realizarpedido" id="realizarpedido" onClick="realizarPedido()" type="button" class="button blue middle" style="width:130px"><span class="label" style="width:100px">Realizar Pedido</span></button></div>
  </td>
</tr>
		    <tr >
		      <td height="24" colspan="2" align="center" >&nbsp;</td>
    </tr>
  </table>
<br />
    <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listas" id="solicitudesPendientes" >
    <thead>
      <tr >
        <th width="6%" height="20"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminar()" style='cursor: pointer;'></th>
        <th width="59%">Concepto</th>
        <th width="31%">Partida</th>
        <th width="4%">&nbsp;</th>
      </tr>
      <thead>
    </table>
<br />
    <br />
    <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="solicitudes">
      <tr>
        <td height="26">&nbsp;&nbsp;<strong>Artículos disponibles en la partida</strong></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
          <td>&nbsp;&nbsp;Por concepto de: 
            <label>
            <input name="concepto" type="text" class="input" id="concepto"  maxlength="100" style="width:445px;">
          </label></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
        <tr>
          <td align="center"><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listas" id="lista_articulos" >
          <thead>
            <tr >
              <th width="5%" height="20">No.</th>
              <th width="10%">Clave</th>
              <th width="54%">Nombre</th>
              <th width="18%">Unidad</th>
              <th width="13%">Cantidad</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
          </table></td>
        </tr>
        <tr>
          <td align="center">                 
            <table width="450" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="132" align="center">
                  <div class="buttons tiptip">
                    <button name="enviarSol" id="enviarSol" type="button" onClick="guardar('ENVIADO')" class="button red middle"><span class="label" style="width:100px">Enviar</span></button>
                </div></td>
               <td width="132" align="center">
                  <div class="buttons tiptip">
                    <button name="regresar" id="regresar" type="button" onClick="regresarSol" class="button red middle"><span class="label" style="width:100px">Regresar</span></button>
                </div></td>
                <td width="168"><button name="guardarSol" id="guardarSol" onClick="guardar('PENDIENTE')" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button></td>
              </tr>
          </table></td>
        </tr>
  </table> 
</FORM>
</body>
</html>
