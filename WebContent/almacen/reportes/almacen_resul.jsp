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

  <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF">
    <tr >
      <td height="30" colspan="9"> REPORTE DE EXISTENCIA  EN ALMAC&Eacute;N
      </td>
    </tr>
    <tr >
      <td colspan="9">CUENTA : DESCRIPCION</td>
    </tr>
    <tr >
      <td width="2%">No.</td>
      <td width="5%">Clave</td>
      <td width="44%">Descripci&oacute;n</td>
      <td width="5%">Estatus</td>
      <td width="7%">Unidad de medida </td>
      <td width="9%">Existencia m&iacute;nima </td>
      <td width="9%">Existencia</td>
      <td width="9%">Costo</td>
      <td width="9%">Costo total</td>
    </tr>
    <tr >
      <td>i</td>
      <td>ID_CLAV_ART</td>
      <td>ARTICULO</td>
      <td>ACTIVO</td>
      <td>UMEDIDA</td>
      <td>EXIS_MINIMA</td>
      <td>EXISTENCIA</td>
      <td>PREUNITARIO</td>
      <td>IMPORTE</td>
    </tr>
    <tr >
      <td colspan="8">Total</td>
      <td width="7%">totalCuen</td>
    </tr>
    <tr >
      <td colspan="9">No hay ning&uacute;n dato que mostrar para la cuenta.</td>
    </tr>
	<tr >
      <td colspan="9">&nbsp;</td>
    </tr>
  </table><br>
    <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF">
      <tr >
        <td height="30" colspan="8"> REPORTE DE ALMAC&Eacute;N DE  PRODUCTOS ACTIVOS CON EXISTENCIA M&Iacute;NIMA<BR> 
          PRODUCTOS QUE  REQUIEREN UNA NUEVA ADQUISICI&Oacute;N</td>
      </tr>
      <tr >
        <td colspan="8">CUENTA : DESCRIPCION</td>
      </tr>
      <tr >
        <td width="2%">No.</td>
        <td width="5%">Clave</td>
        <td width="44%">Descripci&oacute;n</td>
        <td width="7%">Unidad de medida </td>
        <td width="9%">Existencia m&iacute;nima </td>
        <td width="9%">Existencia</td>
        <td width="9%">Costo</td>
        <td width="9%">Costo total</td>
      </tr>
      <tr >
        <td>i</td>
        <td>ID_CLAV_ART</td>
        <td>ARTICULO</td>
        <td>UMEDIDA</td>
        <td>EXIS_MINIMA</td>
        <td>EXISTENCIA</td>
        <td>PREUNITARIO</td>
        <td>IMPORTE</td>
      </tr>
      <tr >
        <td colspan="7">Total</td>
        <td width="7%">totalCuen</td>
      </tr>
      <tr >
        <td colspan="8">No hay ning&uacute;n dato que mostrar para la cuenta.</td>
      </tr>     
	  <tr >
        <td colspan="8">&nbsp;</td>
      </tr> 
    </table>
    <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="1">
      <tr >
        <td height="30" colspan="10">
            REPORTE DE ALMAC&Eacute;N ESTAD&Iacute;STICO MENSUAL
            CORRESPONDIENTE A 
              repMes DEL repyear
        </td>
      </tr>
      <tr >
        <td colspan="10">CUENTA : DESCRIPCION</td>
      </tr>
      <tr >
        <td width="2%">No.</td>
        <td width="5%">Clave</td>
        <td width="44%">Descripci&oacute;n</td>
        <td width="5%">U. medida
        </td>
        <td width="5%">Existencia</td>
        <td width="7%">Entradas</td>
        <td width="9%">Salidas</td>
        <td width="9%">Total Acum. entradas</td>
        <td width="9%">Total&nbsp;Acum. Salidas</td>
        <td width="9%">Existencia final </td>
      </tr>
      <tr >
        <td>i</td>
        <td>ID_CLAV_ART</td>
        <td>ARTICULO</td>
        <td>UMEDIDA</td>
        <td>repExis</td>
        <td>repExisEnt</td>
        <td>repExisSal</td>
        <td>AcuEntrada</td>
        <td>AcuSalida</td>
        <td>totalArt</td>
      </tr>
      <tr >
        <td colspan="10">No hay ning&uacute;n dato que mostrar para la cuenta.</td>
      </tr>
      <tr >
        <td colspan="10">&nbsp;</td>
      </tr>	  
    </table>
	  <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF">
    <tr >
      <td height="30" colspan="9"> REPORTE DE EXISTENCIA  EN ALMAC&Eacute;N MENSUAL<BR> CORRESPONDIENTE A mes DEL repyear 
      </td>
    </tr>
    <tr >
      <td colspan="9">CUENTA : DESCRIPCION</td>
    </tr>
    <tr >
      <td width="2%">No.</td>
      <td width="5%">Clave</td>
      <td width="44%">Descripci&oacute;n</td>
      <td width="5%">Estatus</td>
      <td width="7%">Unidad de medida </td>
      <td width="9%">Existencia m&iacute;nima </td>
      <td width="9%">Existencia</td>
      <td width="9%">Costo</td>
      <td width="9%">Costo total</td>
    </tr>
    <tr >
      <td>i</td>
      <td>ID_CLAV_ART</td>
      <td>ARTICULO</td>
      <td>ACTIVO</td>
      <td>UMEDIDA</td>
      <td>EXIS_MINIMA</td>
      <td>MES</td>
      <td>PRECIO</td>
      <td>MES</td>
    </tr>
    <tr >
      <td colspan="8">Total</td>
      <td width="7%">totalCuen</td>
    </tr>
    <tr >
      <td colspan="9">No hay ning&uacute;n dato que mostrar para la cuenta.</td>
    </tr>
	<tr >
      <td colspan="9">&nbsp;</td>
    </tr>
  </table>
</body>
</html>