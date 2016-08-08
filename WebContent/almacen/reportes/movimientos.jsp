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
<script language="javascript">
function  enviar(f,op){
  f.tipo_salida.value=op;
  if (f.fecha.value=="" ) 
     alert("Seleccione una fecha de inicio para generar el reporte");
   else
   if (f.fechafin.value=="") 
     alert("Seleccione una fecha de fin para generar el reporte");
   else
   if (f.cuenta.selectedIndex <1 ) {
     alert("Seleccione una cuenta para realizar el reporte");
     f.cuenta.focus(); 
   }else
    f.submit();
}

</script>
</head>
<body>
<br />
  <table width="95%" align="center"><tr><td><h1>Movimientos</h1></td></tr></table>  
<form name="f1" method="post" action="sad_rep_detalles_resul.jsp" target="REP_MOVIMIENTO">
<input name="tipo_salida" type="hidden" id="tipo_salida" >
  <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="1" class="formulario" >
    <tr  id="fil_year">
      <td width="17%" height="25">Fecha inicial </td>
      <td width="83%"><input name="fecha" type="text" class="input"  id="fecha" value="" size="12" maxlength="12" readonly="readonly" >
      </td>
    </tr  >
    <tr  id="fil_meses" >
      <td height="25">Fecha final </td>
      <td><input name="fechafin" type="text" class="input"  id="fechafin" value="" size="12" maxlength="12" readonly="readonly" ></td>
    </tr>
    <tr >
      <td height="25">Cuenta</td>
      <td> <select name="cuenta" class="combList"  id="cuenta">
        <option value="0" >Seleccione una cuenta </option>
		<option value="Todos"  >Todas las cuentas</option>    
      </select>
      </td>
    </tr>
    <tr >
      <td height="30" colspan="2">
          <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                        <tr>
              <td width="50%" align="center"><input name="guardaCabecera" type="button" class="botones" id="guardaCabecera" value="Html" onclick="verificarFacturaProveedor()" /></td>
              <td align="center"><input name="guardaCabecera2" type="button" class="botones" id="guardaCabecera2" value="Pdf" onclick="verificarFacturaProveedor()" /></td>
            </tr>
          </table>
          </td>
    </tr>
  </table>
</form>
</body>
</html>