<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Captura de Ordenes de pago</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link href="../../include/css/estilosam.css" rel="stylesheet" type="text/css" />
<link href="../../include/js/autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/ordenPagoMultiplePedidos.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script>  
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="ordenPagoMultiplePedidos.js"></script>
</head>
<body>
<form name="forma" method="post" action="">
<input name="id_orden" type="hidden"  id="id_orden" size="8" maxlength="6" readonly="true" value="" />
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<br>            
      <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="formulario">
        <tr>
          <td colspan="5"  align="center">Listado de Pedidos</td>
        </tr>
        <tr >
          <td colspan="5">&nbsp;</td>
        </tr>
        <tr><td colspan="5" >
        <table border="0" cellpadding="0" cellspacing="0" class="listas" width="100%" id="listaPedidos">
        <thead>
        <tr>
          <th width="7%" >Nuevo</th>
          <th width="18%" >Tipo</th>
          <th width="21%" >Numero</th>
          <th width="14%" >Fecha</th>
          <th width="24%" >No. Requisicion</th>
          <th width="16%" >Importe</th>
        </tr>
        </thead>
         <tbody>
         </tbody>
        </table>
        </td>
        </tr>
        <tr >
          <td height="35" colspan="5" align="center" ><input   name="guardar" type="button" class="botones" id="guardar" onClick="guardarPedidos();" value="Agregar pedido" /></td>
        </tr>
      </table>
   
</form>
</body>
</html>
