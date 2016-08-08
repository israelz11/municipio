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
/*
var activa=true;
var target_pag="REPORT_ALMA";    
  
function  enviar(f,op){
  f.tipo_salida.value=op;
  f.target=target_pag+op;  	     
  if (f.year.selectedIndex <1 && f.tiporeporte[2].checked ) {
     alert("Seleccione un año para realizar el reporte");
     f.year.focus(); 
   }else
   if (f.meses.selectedIndex <1 && f.tiporeporte[2].checked ) {
     alert("Seleccione un  mes para realizar el reporte");
     f.meses.focus(); 
   }else
   if (f.cuenta.selectedIndex <1 ) {
     alert("Seleccione una cuenta para realizar el reporte");
     f.cuenta.focus(); 
   }else
    f.submit();
}

function cambia(f,tdeta) {
  if (activa!=tdeta.checked) 
     if (tdeta.value==3 || tdeta.value==4 ) {
	     ocultarFila("fil_year",true);  
		   ocultarFila("fil_meses",true);  
       target_pag="REPORT_ALMA_MEM";
		 }
	  else {
  	 ocultarFila("fil_year",false);  
		 ocultarFila("fil_meses",false);  
     if (tdeta.value==1) 
      target_pag="REPORT_ALMA";
     else
      target_pag="REPORT_ALMA_MIN";
	  }	   	 		 
}

function estatus_lis(che){
 if (che.checked==true)
   activa=true;
  else
   activa=false;
}
*/
</script>
</head>
<body>
<br />
  <table width="95%" align="center"><tr><td><h1>Reporte de Almacen</h1></td></tr></table>  
<form name="f1" method="post" action="sad_rep_almacen_resul.jsp">
  <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF" class="formulario">
    <tr >
      <td height="30" colspan="2"> REPORTES DE ALMAC&Eacute;N</td>
    </tr>
    <tr >
      <td width="17%">Tipo de reporte </td>
      <td width="83%"><input name="tiporeporte" type="radio" value="1"  onClick="cambia(this.form,this);" onMouseMove="estatus_lis(this)">      
        Existencia 
        <input name="tiporeporte" type="radio" value="2" onClick="cambia(this.form,this);" onMouseMove="estatus_lis(this)" >
        Existencia m&iacute;nima 
        <input name="tiporeporte" type="radio" value="3" onClick="cambia(this.form,this);" onMouseMove="estatus_lis(this)" >
        Mensual estad&iacute;stico 
        <input name="tiporeporte" type="radio" value="4" onClick="cambia(this.form,this);" onMouseMove="estatus_lis(this)" >
      Mensual de existencia </td></tr>
    <tr  id="fil_year">
      <td height="25">A&ntilde;o</td>
      <td><select name="year"  id="year">
        <option value="0">Seleccione un a&ntilde;o</option>
      </select>
      <input name="tipo_salida" type="hidden" id="tipo_salida" >      </td>
    </tr  >
    <tr  id="fil_meses" >
      <td height="25">Meses </td>
      <td><select name="meses"  id="meses">
        <option value="0">Seleccione un mes </option>
        <option value="01">Enero</option>
        <option value="02">Febrero</option>
        <option value="03">Marzo</option>
        <option value="04">Abril</option>
        <option value="05">Mayo</option>
        <option value="06">Junio</option>
        <option value="07">Julio</option>
        <option value="08">Agosto</option>
        <option value="09">Septiembre</option>
        <option value="10">Octubre</option>
        <option value="11">Noviembre</option>
        <option value="12">Diciembre</option>
                  </select></td>
    </tr>
    <tr >
      <td height="25">Cuenta</td>
      <td> <select name="cuenta"  id="cuenta">
        <option value="0" >Seleccione una cuenta </option>
		<option value="Todos" >Todas las cuentas</option>
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