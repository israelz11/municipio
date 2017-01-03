<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Listado de Ordenes de Pago por Ejercer</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type='text/javascript' src="../../dwr/interface/controladorListadoOrdenPagoEjercidoRemoto.js"></script>
<script type='text/javascript' src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/util.js"> </script>
<script type="text/javascript" src="lista_ordenPagoEjercer.js"> </script>
<style type="text/css">
<!--
a:link {
	text-decoration: none;
}
a:visited {
	text-decoration: none;
}
a:hover {
	text-decoration: underline;
}
a:active {
	text-decoration: none;
}

p {
color:#cccccc;
line-height:22px;
padding: 0 0 10px;
margin: 20px 0 20px 0;
}

img {
border:none;
}



#container {
clear: both;
margin: 0;
padding: 0;
}

#container a{
float: right;
background: #9FC54E;
border: 1px solid #9FC54E;
-moz-border-radius-topright: 20px;
-webkit-border-top-right-radius: 20px;
-moz-border-radius-bottomleft: 20px;
-webkit-border-bottom-left-radius: 20px;
text-decoration: none;
font-size: 16px;
letter-spacing:-1px;
font-family: verdana, helvetica, arial, sans-serif;
color:#fff;
padding: 20px;
font-weight: 700;
}

#container a:hover{
float: right;
background: #a0a0a0;
border: 1px solid #cccccc;
-moz-border-radius-topright: 20px;
-webkit-border-top-right-radius: 20px;
-moz-border-radius-bottomleft: 20px;
-webkit-border-bottom-left-radius: 20px;
text-decoration: none;
font-size: 16px;
letter-spacing:-1px;
font-family: verdana, helvetica, arial, sans-serif;
color:#fff;
padding: 20px;
font-weight: 700;
}


.content {
font-style:normal;
font-family:helvetica, arial, verdana, sans-serif;
color:#ffffff;
background:#333333;
border:1px solid #444444;
-moz-border-radius-topright: 20px;
-webkit-border-top-right-radius: 20px;
-moz-border-radius-bottomleft: 20px;
-webkit-border-bottom-left-radius: 20px;
margin: 30px 0 50px;
padding: 15px 0;
}

.content p {
margin: 10px 0;
padding: 15px 20px;
}

.panel {
position: fixed;
top: 50px;
left: 0;
display: none;
background: #000000;
border:1px solid #111111;
-moz-border-radius-topright: 20px;
-webkit-border-top-right-radius: 20px;
-moz-border-radius-bottomright: 20px;
-webkit-border-bottom-right-radius: 20px;
width: auto;
height: auto;
padding: 30px 30px 30px 130px;
filter: alpha(opacity=85);
opacity: .85;
}

.panel p{
margin: 0 0 15px 0;
padding: 0;
color: #cccccc;
}

.panel a, .panel a:visited{
margin: 0;
padding: 0;
color: #9FC54E;
text-decoration: none;
border-bottom: 1px solid #9FC54E;
}

.panel a:hover, .panel a:visited:hover{
margin: 0;
padding: 0;
color: #ffffff;
text-decoration: none;
border-bottom: 1px solid #ffffff;
}

a.trigger{
position: fixed;
text-decoration: none;
top: 80px; left: 0;
font-size: 12px;
letter-spacing:-1px;
font-family: verdana, helvetica, arial, sans-serif;
color:#fff;
padding: 10px 40px 10px 7px;
font-weight: 700;
background:#333333 url(../../imagenes/plus.png) 85% 55% no-repeat;
border:2px solid #444444;
-moz-border-radius-topright: 20px;
-webkit-border-top-right-radius: 20px;
-moz-border-radius-bottomright: 20px;
-webkit-border-bottom-right-radius: 20px;
-moz-border-radius-bottomleft: 0px;
-webkit-border-bottom-left-radius: 0px;
display: block;
}

a.trigger:hover{
position: fixed;
text-decoration: none;
top: 80px; left: 0;
font-size: 12px;
letter-spacing:-1px;
font-family: verdana, helvetica, arial, sans-serif;
color:#fff;
padding: 10px 40px 10px 7px;
font-weight: 700;
background:#222222 url(../../imagenes/plus.png) 85% 55% no-repeat;
border:1px solid #444444;
-moz-border-radius-topright: 20px;
-webkit-border-top-right-radius: 20px;
-moz-border-radius-bottomright: 20px;
-webkit-border-bottom-right-radius: 20px;
-moz-border-radius-bottomleft: 0px;
-webkit-border-bottom-left-radius: 0px;
display: block;
}

a.active.trigger {
background:#222222 url(../../imagenes/minus.png) 85% 55% no-repeat;
}
-->
</style></head>
<body class="Fondo" >
<form  action="../reportes/requisicion.action" method="POST" id="forma" name="forma">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" name="cve_op" id="cve_op">
<table width="95%" align="center"><tr><td><h1>Ordenes de Pago - Listado de Ordenes de Pago para Ejercer y Ejercidas</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th height="15">&nbsp;</th>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <th width="8%" height="25">&nbsp;</th>
    <td><input type="checkbox" name="chk_ejercer" id="chk_ejercer" <c:if test="${fn:contains(por_ejercer,'true')}" >checked</c:if>> 
      Ordenes de Pago por Ejercer</td>
    <td><input type="checkbox" name="chk_ejercercidas" id="chk_ejercercidas" 
      <c:if test="${fn:contains(ejercidas,'true')}" >checked</c:if>
      >
Ordenes de Pago Ejercidas </td>
    <td width="11%">&nbsp;</td>
    <td width="11%">&nbsp;</td>
    <td width="11%">&nbsp;</td>
  </tr>
  <tr >
    <th height="25" >Mes :
      <td width="14%"><strong>
        <select name="cbomes" id="cbomes" style="width:110px" class="comboBox">
          <option value="1" <c:if test='${mes==1}'>selected</c:if>>Enero</option>
          <option value="2" <c:if test='${mes==2}'>selected</c:if>>Febrero</option>
          <option value="3" <c:if test='${mes==3}'>selected</c:if>>Marzo</option>
          <option value="4" <c:if test='${mes==4}'>selected</c:if>>Abril</option>
          <option value="5" <c:if test='${mes==5}'>selected</c:if>>Mayo</option>
          <option value="6" <c:if test='${mes==6}'>selected</c:if>>Junio</option>
          <option value="7" <c:if test='${mes==7}'>selected</c:if>>Julio</option>
          <option value="8" <c:if test='${mes==8}'>selected</c:if>>Agosto</option>
          <option value="9" <c:if test='${mes==9}'>selected</c:if>>Septiembre</option>
          <option value="10" <c:if test='${mes==10}'>selected</c:if>>Octubre</option>
          <option value="11" <c:if test='${mes==11}'>selected</c:if>>Noviembre</option>
          <option value="12" <c:if test='${mes==12}'>selected</c:if>>Diciembre</option>
        </select>
      </strong></td>
    <td colspan="2"><input type="checkbox" name="chkfecha" id="chkfecha"  <c:if test="${fn:contains(fecha_ejercer,'true')}" >checked</c:if>>
      &nbsp;Ejercer con fecha: 
      <input name="txtfecha" type="text" id="txtfecha" style="width:100px" maxlength="10" value="<c:out value="${fecha}"/>">
      &nbsp;&nbsp;<input  name="btnBuscar" type="button" class="botones" id="btnBuscar" value="Buscar" style="width:100px"></td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="13" >    
    <td>&nbsp;</td>
    <td colspan="2">&nbsp;</td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  </table>
<br />
<table width="95%" class="listas" align="center" id="listaRequisiciones" cellpadding="0" cellspacing="0">
 <thead>
  <tr>
    <th width="2%" height="20">&nbsp;</th>
    <th width="2%">N°</th>
    <th width="7%">Número</th>
    <th width="7%">Fecha OP</th>
   
    <th width="28%">Beneficiario</th>
    <th width="33%">Concepto</th>
    <th width="10%">Importe</th>
    <th width="4%">Opciones</th>
  </tr>
</thead>   
<tbody>  
<c:set var="cont" value="${0}" /> 
  <c:forEach items="${lstOrdenesPagoEjercer}" var="item" varStatus="status"> 
  <c:set var="cont" value="${cont+1}" /> 
  <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
    <td align="center" height="20"><c:if test='${item.STATUS==0}'><input type="checkbox" id="chkOP" name="chkOP" value="<c:out value='${item.CVE_OP}'/>"/></c:if></td>
    <td align="center"><c:out value='${cont}'/></td>
    <td align="center"><a href="javascript:mostrarDetallesOP(<c:out value='${item.CVE_OP}'/>)"><c:out value='${item.NUM_OP}'/></a></td>
    <td align="center"><c:out value='${item.FECHA}'/></td>
    
    <td align="left"><c:out value='${item.NCOMERCIA}'/></td>
    <td><c:out value='${item.NOTA}'/></td>
    <td align="right"><fmt:formatNumber value="${item.IMPORTE}"  pattern="#,###,###,##0.00" /> </td>
    <td align="center">
    <img style="cursor:pointer" src="../../imagenes/pdf.gif" alt="Ver Documento" border="0" width="14" height="16" onClick="mostrarOpcionPDF(<c:out value='${item.CVE_OP}'/>)"> <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_BITACORA_EN_ORDENES_DE_PAGO">
        	&nbsp;<img style="cursor:pointer" src="../../imagenes/report_user.png" border="0" width="16" height="16" title="Ver bitacora de Orden de Pago" onClick="bitacoraDocumento(<c:out value='${item.CVE_OP}'/>, 'OP')">     
    </sec:authorize>
    </td>
  </tr>
  
 </c:forEach>
  <tr>
    <td colspan="8" align="left"><input type="button" value="Ejercer OP" id="cmdejercer" class="botones" style="width:130px"/>
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CAMBIAR_FECHA_ORDEN_DE_PAGO">
      <input type="button" value="Cambiar fecha" id="cmdfecha" class="botones" style="width:130px"/>
    </sec:authorize>
      </td>
  </tr>
</tbody>  
</table><div title="123"></div>
</form>
<div class="panel">
	<h3 style="color:#FFF">Tipo de relación de Ordenes de Pago</h3>
    <div class="styled-select">
        <select name="cbotiporelacion" class="comboBox" id="cbotiporelacion" style="width:350px">
                <option value="0">[Seleccione tipo de relación]</option>
                <option value="1">Relación de [Envío] a Dirección de Finanzas</option>
                <option value="2">Relación de [Devolución] de Ordenes de Pago a </option>
                <option value="3">Relación de [Vales] a Dirección de Finanzas</option>
                <option value="4">Relación de [Devolucion] de Vales a Dirección de Finanzas</option>
          </select>
    </div>
    <div id="div_unidades">
    	<h3 style="color:#FFF">Unidad Administrativa</h3>
        <div class="styled-select">
            <select name="cbodependencia2"  id="cbodependencia2" style="width:350px" class="comboBox">
                <option value="0" selected>[Seleccione la Unidad Administrativa]</option>
                <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
                  <option value='<c:out value="${item.ID}"/>'><c:out value='${item.DEPENDENCIA}'/></option>
                  </c:forEach>
              </select>
         </div>
    </div>
	<h3 style="color:#FFF">Lista de relaciones</h3>
    <div class="styled-select">
        <select name="cborelacion" class="comboBox" id="cborelacion" style="width:350px" disabled>
                <option value="0">[Seleccione un listado]</option>
          </select>
     </div>
  <table width="286" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="170" height="20" align="center">
            <h3 style="color:#FFF">Fecha de entrada</h3>
        </td>
        <td width="100" align="center">
            <h3 style="color:#FFF">Cerrada</h3>
        </td>
        <td width="100" align="center">
            <h3 style="color:#FFF">Devuelta</h3>
        </td>
      </tr>
      <tr>
        <td width="170" height="20" align="center">
            <div id="divfechaentrada" style="color:#FFF; font-size:12px"></div>
        </td>
        <td width="100" align="center"><input type="hidden" id="hdcerrada" value=""/>
            <div id="divcerrada" style="color:#FFF; font-size:12px"></div>
        </td>
        <td width="100" align="center"><input type="hidden" id="hddevuelta" value=""/>
            <div id="divdevuelta" style="color:#FFF; font-size:12px"></div>
        </td>
      </tr>
  </table>
  </br>
  <input  name="cmdmodificar" type="button" class="botones" id="cmdmodificar" value="Modificar Relación" style="width:175px" disabled> <input  name="cmdabrir" type="button" class="botones" id="cmdabrir" value="Abrir Relación" style="width:175px" disabled> 
  <div id="divdetalle"><h3 style="color:#FFF">Detalles de Ordenes de Pago</h3></div>
  <p><table width="310" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="170"><select name="lstdetalles" id="lstdetalles" size="10" multiple="multiple" style="width:243px; font-size:11px" onChange="validarList()" disabled></select> <input type="checkbox" id="chkdevuelto" value="" disabled/><span style="color:#FFF; font-size:11px;">Devuelto</span></td>
    <td width="116" align="center" valign="top"><div id="divopvale"><span style="color:#FFF; font-size:11px">Orden de Pago</span></div><input type="text" id="txtnumop" name="txtnumop" style="width:100px" disabled><span style="color:#FFF; font-size:11px">Observaci&oacute;n</span><br><textarea rows="5" id="txtarea" style="width:100px"></textarea></td>
  </tr>
  <tr>
  	<td width="170" colspan="2"><input type="hidden" value="0" id="hddetalle"> <input  name="cmdagregar" type="button" class="botones" id="cmdagregar" value="Agregar OP's" style="width:96px" disabled>  <input  name="cmdeliminar" type="button" class="botones" id="cmdeliminar" value="Eliminar OP's" style="width:96px" disabled> <input  name="cmdmodificarop" type="button" class="botones" id="cmdmodificarop" value="Modificar OP" style="width:100px" onClick="cargaDetalles()" disabled></td>
  </tr>
</table>
</p>
<p><input  name="cmdimprimir" type="button" class="botones" id="cmdimprimir" value="Imp. Relación" style="width:96x" disabled> <input  name="cmdimprimir" type="button" class="botones" id="cmdimprimirgeneral" value="Imp. General" style="width:96x" disabled> <input  name="cmdnueva" type="button" class="botones" id="cmdnueva" value="Nueva relación" style="width:96px" disabled></p>
  <div style="clear:both;"></div>
</div>
<a class="trigger" href="#">Relaciónes</a>
<form method="POST" id="frmreporte" name="frmreporte">
<input type="hidden" id="id_relacion" name="id_relacion" value=""/>
</form>
</body>
</html>
