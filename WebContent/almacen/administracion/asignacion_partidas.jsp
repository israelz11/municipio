<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de Almacenes</title>
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
<script type="text/javascript" src="../../dwr/interface/controladorAlmacenRemoto.js"> </script>
<script type="text/javascript" > 
function guardar(tipo){
	$('#accion').attr("value",tipo);
	if ($('#accion').attr("value")=='agregar' && $('#partida').attr("value")=='') 
	  jInformation("Es necesario que seleccione un elemento de la lista de partidas","Información");
	else
	if ($('#accion').attr("value")=='quitar' && $('#partidasagregada').attr("value")=='' ) 
	  jInformation("Es necesario que seleccione un elemento de la lista de partidas agregadas","Información");	
	else
	 $('#forma').submit();
}


function publicarMensaje(){
 CloseDelay("Partidas agregadas correctamente");		
}
</script>

<body >
<form name="forma" id="forma" method="post" action="asignacion_partidas.action"  >
<br />
  <table width="90%" align="center"><tr><td><h1>Administración - Asignación de partidas</h1></td></tr></table>
  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <th height="21">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th height="30"><input type="hidden" name="accion" id="accion">
      Partidas :</th>
      <td><select name="partida" size="10" class="" id="partida" style="width:600px" >        
         <c:forEach items="${partidas}" var="item" varStatus="status">
              <option value="<c:out value='${item.CLAVE}'/>" >
                <c:out value='${item.CLAVE}'/>-<c:out value="${item.DESCRIPCION}"/>
            </option>
          </c:forEach>
        </select>
      </td>
    </tr>
    <tr>
      <th width="24%" height="30">&nbsp;</th>
      <td width="76%"><input type="button"  class="botones"  name="btnGrabar" value="Agregar &gt;&gt;"  onClick="guardar('agregar')" style="width:120px"/></td>
    </tr>
    <tr>
      <th height="30">Partidas Agregadas :</th>
      <td><select name="partidasagregada" size="10" class="" id="partidasagregada" style="width:600px">
        <c:forEach items="${partidasAsignadas}" var="item" varStatus="status">
          <option value="<c:out value='${item.CLV_PARTID}'/>" >
            <c:out value='${item.CLV_PARTID}'/>-<c:out value="${item.PARTIDA}"/>
            </option>
        </c:forEach>
      </select></td>
    </tr>
    <tr>
      <th height="30">&nbsp;</th>
      <td><input type="button"  class="botones"  name="btnlimpiar" value="&lt;&lt; Quitar"  onClick="guardar('quitar')" style="width:120px"/></td>
    </tr>
    <tr>
      <th height="30">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
  </table>
  <br />
</form>
<c:if test='${mensaje==true}'> 
<script language="javascript">
 publicarMensaje();
</script>
</c:if>>
</body>
</html>
