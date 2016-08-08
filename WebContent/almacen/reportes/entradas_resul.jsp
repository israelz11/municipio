<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de Grupos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
</head>
<body>
<br />
<table width="95%" align="center"><tr><td><h1>REPORTE DE FACTURAS DE : repDesProve
        DEL repFecIni AL repFecFin    Afectaci&oacute;n : </h1></td></tr></table>  
  <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listas" >
    <tr >
      <th width="4%">No.</th>
      <th width="14%">Factura</th>
      <th width="48%">Proveedor</th>	  
    <th width="12%">Tipo de factura </th>
      <th width="12%">Fecha</th>
      <th width="10%">Importe</th>
    </tr>
    <tr >
      <td width="4%">i</td>
      <td width="14%">NUMERO_FAC</td>
      <td width="48%">PROVEEDOR</td>
 	  <td width="12%">desTipFac</td>
      <td width="12%">FECHA</td>
      <td  width="10%">TOTAL</td>
    </tr>
    <tr >
      <td colspan="5" >Total</td>
      <td  width="10%">totalGene</td>
    </tr>
  </table>
</body>
</html>