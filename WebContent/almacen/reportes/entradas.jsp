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
<script language="JavaScript" >
var activa=true;
var contarlote=0;
 function cambia(che) {
  if (che.checked!=activa )   {
	 if (che.checked)
	   contarlote++; 
	 else
	   contarlote--; 
  }
}
 function estatus_lis(che){
 if (che.checked==true)
   activa=true;
  else
   activa=false;
}


 
function  enviar(f1,op){
 f1.tipo_salida.value=op;
 f1.target="REPORT_DETAL";
 if (contarlote==0)
    alert("Seleccione un tipo de afectacn"); 
 else
   if (f1.proveedor.selectedIndex < 1 ) {
    alert("Seleccione una opcin de la lista de proveedores");
  	f1.proveedor.focus();
   }
   else  
 if (f1.finicial.value=="") {
    alert("Seleccione una fecha inicial para poder generar la consulta ");
  	f1.finicial.focus();
 }else
  if (f1.ffinal.value=="") {
    alert("Seleccione una fecha final para poder generar la consulta ");
  	f1.ffinal.focus();
 }else { 
     f1.desprove.value=f1.proveedor[f1.proveedor.selectedIndex].text;
     f1.submit();
	 }
}
</script>
</head>
<body>
<br />
  <table width="95%" align="center"><tr><td><h1>Entradas</h1></td></tr></table>  
  <form ACTION="sad_rep_entradas_resul.jsp" METHOD="POST" name="f1" id="f1" >
  <input name="tipo_salida" type="hidden" id="tipo_salida" >
    <table width="80%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF" class="formulario">
      <tr >
        <td height="30" colspan="2"><div align="center"><span class="TITULO"> REPORTE DE&nbsp;ENTRADAS&nbsp;POR&nbsp;FACTURAS</span></div></td>
      </tr>
      <tr >
        <td>Proveedor</td>
        <td><select name="proveedor" class="combList"  id="proveedor">
          <option value="0" >Seleccione un proveedor </option>
          <option value="Todos" >Todos</option>        
        </select>
        <input name="desprove" type="hidden" id="desprove" ></td>
      </tr>
      <tr >
        <td width="17%">Fecha inicial</td>
        <td width="83%"><input name="finicial" type="text" class="input" id="finicial"  value="fechActual" ></td>
      </tr>
      <tr >
        <td>Fecha final</td>
        <td><input name="ffinal" type="text" class="input"  id="ffinal"  value="fechActual" ></td>
      </tr>
      <tr >
        <td colspan="2">
          <table width="100%"  border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="46%"><div align="center">
                <input name="guardaCabecera" type="button" class="botones" id="guardaCabecera" value="Html" onclick="verificarFacturaProveedor()" />
              </div></td>
              <td width="54%"><div align="center">
                <input name="guardaCabecera2" type="button" class="botones" id="guardaCabecera2" value="Pdf" onclick="verificarFacturaProveedor()" />
              </div></td>
            </tr>
          </table>
          </td>
      </tr>
    </table>	
</FORM>
</body>
</html>
