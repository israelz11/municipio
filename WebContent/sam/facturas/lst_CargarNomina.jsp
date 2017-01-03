<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Listado de Nomina</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />	
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>
<script type="text/javascript" src="../../dwr/util.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorCargarNominaDeductivasRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/jquery.form.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<script type="text/javascript" src="lst_CargarNomina.js"> </script>
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
-->
</style></head>
<body class="Fondo" >
<form enctype="multipart/form-data"  method="post" id="frm" name="frm">
<table width="95%" align="center"><tr><td><h1>Nomina - Listado de Nómina</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr >
        <th height="15" >&nbsp;</th>
        <td height="25" >&nbsp;</td>
      <td width="413" rowspan="3" >
      <table width="392" border="0" cellspacing="0" cellpadding="0">
          <tr>
          
          <td width="196" bgcolor="#FFFFFF">
          <div class="buttons tiptip">
            <button name="cmdcargar" id="cmdcargar" type="button" class="button red middle"><span class="label" style="width:100px">Cargar archivos</span></button>
          </div>
          </td>
          <td width="196" bgcolor="#FFFFFF">
          <button name="cmdVaciar" id="cmdVaciar" type="button" class="button blue middle" /><span class="label" style="width:130px">Borrar datos</span></button></td>
          
          </tr>
      </table></td>
    </tr>
    <tr >
    <th width="222" height="25" >Archivo de Nomina y Deductivas: </th>
    <td width="487" height="25" >
      <input type="file" class="input-file" id="fileNomina" name="fileNomina" style="width:445px" />
    </td>
    </tr>
  <tr >
    <th height="25" >&nbsp;</th>
    <td height="25" >&nbsp;</td>
  </tr>
  <tr bgcolor="#FFFFFF">
    <th height="25" colspan="3" bgcolor="#FFFFFF">
    
    	<div id="tabuladores">
  <ul>
    <li><a href="#fragment-nomina">Nomina</a></li>
    <li><a href="#fragment-deducciones">Deducciones</a></li>
    <li><a href="#fragment-validacion">Validación Nomina</a></li>
  </ul>
<div id="fragment-nomina" align="left">
    <table width="95%" border="0" cellpadding="0" cellspacing="0" class="listas" align="center" id="listaDocumentos">
     <thead>
      <tr>
        <th width="6%" height="20">Cons.</th>
        <th width="6%">ID_PROYECTO</th>
        <th width="6%">CLV_PARTID</th>
        <th width="8%">ID_RECURSO</th>
        <th width="7%">CVE_PADRE</th>
        <th width="8%">ID_DEPENDENCIA</th>
        <th width="7%">TIPO_NOMINA</th>
        <th width="8%">NUM_QUINCENA</th>
        <th width="6%">MES</th>
        <th width="9%">FECHA_NOMINA</th>
        <th width="7%">IMPORTE</th>
        <th width="22%">NOTA</th>
      </tr>
       </thead>   
    <tbody>  
    <c:set var="cont" value="${0}" /> 
    <c:forEach items="${listadoNomina}" var="item" varStatus="status"> 
      <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
       <c:set var="cont" value="${cont+1}"/> 
        <tr>
        <td height="25" align="center">${cont}</td>
        <td align="center">${item.ID_PROYECTO}</td>
        <td align="center">${item.CLV_PARTID}</td>
        <td align="center">${item.ID_RECURSO}</td>
        <td align="center">${item.CLV_UNIADM}</td>
        <td align="center">${item.ID_DEPENDENCIA}</td>
        <td align="center">${item.TIPO_NOMINA}</td>
        <td align="center">${item.NUM_QUINCENA}</td>
        <td align="center">${item.MES}</td>
        <td align="center">${item.FECHA_NOMINA}</td>
        <td align="right">$<fmt:formatNumber value="${item.IMPORTE}"  pattern="#,###,###,##0.00" />&nbsp;</td>
        <td align="left">${item.NOTA}</td>
      </tr>
     
      </c:forEach>
       <tr>
         <td colspan="12" style="background-color:#FFF"></tr>
      </tbody>  
    </table>
 </div>
 <div id="fragment-deducciones" align="left">
 	<table width="95%" border="0" cellpadding="0" cellspacing="0" class="listas" align="center" id="listaDocumentos">
     <thead>
      <tr>
        <th width="6%" height="20">Cons.</th>
        <th width="6%">TIPO_NOMINA</th>
        <th width="6%">ID_RECURSO</th>
        <th width="8%">RECINTO</th>
        <th width="7%">CLV_RETENC</th>
        <th width="55%" align="left"><div align="left">RETENCIÒN</div></th>
        <th width="12%">IMPORTE</th>
        </tr>
       </thead>   
    <tbody>  
    <c:set var="cont" value="${0}" /> 
    <c:forEach items="${listadodeducciones}" var="item" varStatus="status"> 
      <tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
       <c:set var="cont" value="${cont+1}"/> 
        <tr>
        <td height="25" align="center">${cont}</td>
        <td align="center">${item.TIPO_NOM}</td>
        <td align="center">${item.ID_RECURSO}</td>
        <td align="center">${item.RECINTO}</td>
        <td align="center">${item.CLV_RETENC}</td>
        <td align="left">${item.RETENCION}</td>
        <td align="right">$<fmt:formatNumber value="${item.TOTAL}"  pattern="#,###,###,##0.00" />&nbsp;</td>
        </tr>
     
      </c:forEach>
       <tr>
         <td colspan="7" style="background-color:#FFF"></tr>
      </tbody>  
    </table>
 </div>
 
 
 <div id="fragment-validacion" align="left">
 	<table width="95%" border="0" cellpadding="0" cellspacing="0" class="listas" align="center" id="listaDocumentos">
     <thead>
      <tr>
        <th width="8%" height="20">ID_PROYECTO</th>
        <th width="8%">N_PROGRAMA</th>
        <th width="8%" align="left">TIPO NOM.</th>
        <th width="15%" align="left"><div align="left">&nbsp;DESCRIPCION PROGRAMA</div></th>
        <th width="15%">DEPENDENCIA PADRE</th>
        <th width="8%" align="left">CLV_PARTID</th>
        <th width="15%" align="left"><div align="left">&nbsp;PARTIDA DESCRIPCION</div></th>
        <th width="6%" align="left">TOTAL</th>
        <th width="6%" align="left">DISP. MES</th>
        <th width="6%">DISP.AÑO</th>
        <th width="5%">SALDO MES</th>
        </tr>
       </thead>   
    <tbody>  
     <c:set var="cont" value="${0}" /> 
    <c:set var="err" value="${0}" /> 
    <c:forEach items="${listavalidacionNomina}" var="item" varStatus="status"> 
        <c:set var="color" value="${'#D7D7D7'}" /> 
        <c:set var="cont" value="${cont+1}" /> 
    	<c:if test="${(item.DISPONIBLE_MES-item.IMPORTE)<0}">
        	<c:set var="err" value="${err+1}"/> 
        	<c:set var="color" value="${'#F27900'}" /> 
            <input type="hidden" id="hd_error" value="${item.DISPONIBLE_MES-item.IMPORTE}">
        </c:if>
      <tr>
        <tr>
        <td style="background-color:${color}" align="center" height="20">${item.ID_PROYECTO}</td>
        <td style="background-color:${color}" align="center">${item.N_PROGRAMA}</td>
        <td align="center" style="background-color:${color}">${item.TIPO_NOMINA}</td>
        <td align="left" style="background-color:${color}">${item.ACT_INST}</td>
        <td style="background-color:${color}" align="center">${item.UNIDADADM}</td>
        <td style="background-color:${color}" align="center">${item.CLV_PARTID}</td>
        <td style="background-color:${color}" align="left">${item.PARTIDA}</td>
        <td align="right" style="background-color:${color}">$<fmt:formatNumber value="${item.IMPORTE}"  pattern="#,###,###,##0.00" />&nbsp;</td>
        <td align="right" style="background-color:${color}">$<fmt:formatNumber value="${item.DISPONIBLE_MES}"  pattern="#,###,###,##0.00" />&nbsp;</td>
        <td style="background-color:${color}" align="right">$<fmt:formatNumber value="${item.DISPONIBLE_ANIO}"  pattern="#,###,###,##0.00" />&nbsp;</td>
        <td style="background-color:${color}" align="right">$<fmt:formatNumber value="${item.TOTAL}"  pattern="#,###,###,##0.00" />&nbsp;</td>
        </tr>
     
      </c:forEach>
       <tr>
         <td colspan="12" style="background-color:#FFF">
         <c:if test="${cont>0}">
         	<button name="cmdCrearFacturaOP" id="cmdCrearFacturaOP" type="button" class="button blue middle" ><span class="label" style="width:240px">Crear Facturas y Ordenes de Pago</span></button>
         </c:if>
        </tr>
      </tbody>  
    </table>
 </div>
    </th>
    </tr>
  <tr >
    <th height="25" colspan="3" >&nbsp;</th>
  </tr>  
</table>
<br />

</form>
</body>
</html>
