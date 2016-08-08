<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Entradas - Consulta de documentos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<script language="JavaScript" >

function getReporteEntrada() {
	//$('#frmreporte').attr("action","../reportes/entradas.action");
	$('#forma').attr("target","impresionEntrada");
	$('#forma').submit();
    $('#forma').attr("target","");

}

function  enviar(f1,op){
 f1.acciones.value=op;
 f1.factura.value=jtrim(f1.factura.value); 
 if (f1.finicial.value=="") {
    alert("Seleccione la fecha inicial para poder generar la consulta ");
  	f1.finicial.focus();
 }else
  if (f1.ffinal.value=="") {
    alert("Seleccione la fecha final para poder generar la consulta ");
  	f1.ffinal.focus();
 }else
  if (f1.proveedor.selectedIndex < 1 ) 
   {
    alert("Seleccione una opcin de la lista de proveedores");
  	f1.proveedor.focus();
   }
   else  
     f1.submit();
}

function reenvia(f1)
{  
   f1.acciones.value="Buscar";
   f1.submit();
   
}

function modifica_fac(f,id_sal){  
   f.acciones.value="Listar";
   f.clave.value=id_sal;
   f.submit();
   
}
</script>
</head>
<body>
<br />
  <table width="95%" align="center"><tr><td><h1>Entradas - Consulta de documentos</h1></td></tr></table>
  <form ACTION="../reportes/entradas.action" METHOD="get" name="forma" id="forma" >
    <br />
    <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="listas" >
     <thead>
      <tr>
        <th height="25" colspan="2"><strong>Informaci&oacute;n general de entrada</strong><input type="hidden" id="ID_ENTRADA" name="ID_ENTRADA"  value="<c:out value='${documento.ID_ENTRADA}'/>"/></th>
       </tr>
      </thead>
    <tbody>
      <tr>
        <th height="25">&nbsp;<strong>Entrada:</strong></th>
        <td align="left"><c:out value='${documento.FOLIO}'/></td>
      </tr>
      <tr>
        <th height="25"><strong>Unidad Administrativa:</strong></th>
        <td align="left"><c:out value='${documento.DEPENDENCIA}'/></td>
      </tr>
      <tr>
        <th width="16%" height="25">&nbsp;<strong>Almac&eacute;n:</strong></th>
        <td width="84%" align="left"><c:out value='${documento.ALMACEN}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label"><strong>Número Req.:</strong></th>
        <td align="left" valign="middle"><c:out value='${documento.NUM_REQ}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label">&nbsp;<strong>N&uacute;mero de Pedido:</strong></th>
        <td align="left" valign="middle"><c:out value='${documento.NUM_PED}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label">&nbsp;<strong>N&uacute;mero de Documento:</strong></th>
        <td align="left"><c:out value='${documento.DOCUMENTO}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label">&nbsp;<strong>Tipo de documento:</strong></th>
        <td align="left"><c:out value='${documento.TIPO_DOCUMENTO}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label">&nbsp;<strong>Fecha documento:</strong></th>
        <td align="left" ><c:out value='${documento.FECHA_DOCUMENTO}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label">&nbsp;<strong>Fecha cierre:</strong></th>
        <td align="left"><c:out value='${documento.FECHA_CIERRE}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label">&nbsp;<strong>Fecha de creación:</strong></th>
        <td align="left"><c:out value='${documento.FECHA}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label">&nbsp;<strong>Programa:</strong></th>
        <td align="left"><c:out value='${documento.N_PROGRAMA_DESC}'/> - <c:out value='${documento.PROG_PRESUP}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label">&nbsp;<strong>Partida:</strong></th>
        <td align="left"><c:out value='${documento.PARTIDA}'/> - <c:out value='${documento.DESC_PART}'/></td>
      </tr>
      <tr>
        <th height="25" class="field_label">&nbsp;<strong>Proveedor:</strong></th>
        <td align="left"><c:out value='${documento.ID_PROVEEDOR}'/> - <c:out value='${documento.PROVEEDOR}'/></td>
      </tr>
      <tr>
        <th height="23" class="field_label">&nbsp;<strong>Descripcion:</strong></th>
        <td align="left"><c:out value='${documento.DESCRIPCION}'/></td>
      </tr>
      <tr >
        <td height="25" colspan="2" class="field_label">&nbsp;</td>
      </tr>
      <tr >
        <th height="20" colspan="2"><strong>Detalles de entrada</strong></th>
      </tr>
   </tbody>
    </table>
    <table width="95%" border="0" align="center" cellpadding="1" cellspacing="1" class="listas" id="tablaDetallesFacturas">
      <thead>
        <tr >
          <th width="80" height="19">Cantidad</th>
          <th width="111">Unidad de medida</th>
          <th width="545">Descripci&oacute;n</th>
          <th width="215"><div align="center">Familia</div></th>
          <th width="126">Precio </th>
        </tr>
      </thead>
      <tbody>
      <c:set var="TOTAL" value="${0.0}" />
        <c:forEach items="${detalles}" var="item" varStatus="itemStatus">
          <tr>
            <td height="20" align="center"><fmt:formatNumber value="${item.CANTIDAD}"  pattern="#,###,###,##0.00" /></td>
            <td><c:out value="${item.UNIDMEDIDA}"/></td>
            <td><c:out value="${item.ARTICULO}"/>(<c:out value="${item.DESCRIPCION}"/>)</td>
            <td align="left"><c:out value="${item.FAMILIA}"/></td>
            <td align="right"><fmt:formatNumber value="${item.PRECIO}"  pattern="$ #,###,###,##0.00" />&nbsp;</td>
          </tr>
          <c:set var="TOTAL" value="${TOTAL+(item.CANTIDAD * item.PRECIO)}" /> 
        </c:forEach>
        <tr>
          <td height="20" colspan="4" align="right" bgcolor="#FFFFFF">Subtotal:</td>
          <td align="right" bgcolor="#FFFFFF"><fmt:formatNumber value="${documento.SUBTOTAL}"  pattern="$ #,###,###,##0.00" /></td>
        </tr>
        <tr>
          <td height="20" colspan="4" align="right" bgcolor="#FFFFFF">Descuento:</td>
          <td align="right" bgcolor="#FFFFFF"><fmt:formatNumber value="${documento.DESCUENTO}"  pattern="$ #,###,###,##0.00" /></td>
        </tr>
        <tr>
          <td height="20" colspan="4" align="right" bgcolor="#FFFFFF">Iva:</td>
          <td align="right" bgcolor="#FFFFFF"><fmt:formatNumber value="${documento.IVA}"  pattern="$ #,###,###,##0.00" /></td>
        </tr>
        <tr>
            <td height="20" colspan="4" align="right" bgcolor="#FFFFFF"><strong>Total:</strong> </td>
            <td align="right" bgcolor="#FFFFFF"><strong><fmt:formatNumber value="${documento.TOTAL}"  pattern="$ #,###,###,##0.00" /></strong>&nbsp;</td>
        </tr>
      </tbody>
    </table>
    <br />
</FORM>
<div align="center">
	<div align="center">
	  <table width="200" border="0">
	    <tr>
	      <c:if test='${ban==NULL}'><td><button name="cmdregresar" id="cmdregresar" onClick="history.go(-1);" type="button" class="button red middle" ><span class="label" style="width:100px">Regresar</span></button></td> </c:if>
	      <td><button name="cmdregresar" id="cmdregresar" onClick="getReporteEntrada(<c:out value='${documento.ID_ENTRADA}'/>);" type="button" class="button red middle" ><span class="label" style="width:100px">Imprimir</span></button></td>
        </tr>
      </table>
	
	</div>
</div>
</body>
</html>
