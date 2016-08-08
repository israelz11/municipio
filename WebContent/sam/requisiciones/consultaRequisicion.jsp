<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Consulta de la Requisicion No. <c:out value='${requisicion.NUM_REQ}'/></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script language="javascript">
<!--
function imprimir(){
	$('#frm').attr('target',"impresionInforme");
	$('#frm').attr('action', '../reportes/rpt_InformeRequisicion.action');
	$('#frm').submit();
	$('#frm').attr('target',"");
	$('#frm').attr('action', 'consultaRequiscion.action');
}

-->
</script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-bottom: 0px;
	color:#000;
	font-size:11px;
}
a:link {
	text-decoration: none;
}
a:visited {
	text-decoration: none;
}
a:hover {
	text-decoration: none;
}
a:active {
	text-decoration: none;
}
-->
</style></head>

<body>
<form name="frm" id="frm" action="consultaRequisicion.action" method="post">
<input type="hidden" id="CVE_REQ" name="CVE_REQ" value="<c:out value='${requisicion.CVE_REQ}'/>" />
<input type="hidden" id="claveRequisicion" name="claveRequisicion" value="<c:out value='${requisicion.CVE_REQ}'/>" />
<table border="1" align="center" cellpadding="1" cellspacing="2" width="80%">
  <tr bgcolor="#889FC9">
    <td height="25" colspan="5" align="center"><span class="TituloFormulario"><strong>Requisicion No. <c:out value='${requisicion.NUM_REQ}'/></strong></span></td>
  </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Unidad Administrativa:</strong></td>
        <td width="65%" height="16%" align="left"><c:out value='${requisicion.NOMBRE_UNIDAD}'/></td>
        <td colspan="2" rowspan="2" align="center" bgcolor="#C1C1C1"><strong>Bienes</strong></td>
      </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Modalidad de gasto:</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.TIPO_GASTO}'/></td>
      </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Programa:</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.PROYECTO}'/>  <c:out value='${requisicion.PROG_PRESUP}'/></td>
        <td width="8%" height="16%" align="left" bgcolor="#C1C1C1"><strong>Tipo</strong></td>
        <td width="10%" height="16%" align="left"><c:out value='${requisicion.VEHICULO}'/></td>
      </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Partida:</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.CLV_PARTID}'/>  <c:out value='${requisicion.PARTIDA}'/></td>
        <td height="16%" align="left" bgcolor="#C1C1C1"><strong>Marca</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.MARCA}'/></td>
      </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Actividad Institucional:</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.CLV_ACTINST}'/>  <c:out value='${requisicion.ACT_INST}'/></td>
        <td height="16%" align="left" bgcolor="#C1C1C1"><strong>Modelo</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.MODELO}'/></td>
      </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Finalidad:</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.CLV_FINALIDAD}'/> <c:out value='${requisicion.FINALIDAD}'/></td>
        <td height="16%" align="left" bgcolor="#C1C1C1"><strong>Placas</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.PLACAS}'/></td>
      </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Función:</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.CLV_FUNCION}'/> <c:out value='${requisicion.FUNCION}'/></td>
        <td height="16%" align="left" bgcolor="#C1C1C1"><strong>No. de Inv.</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.NUM_INV}'/></td>
      </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Subfunción:</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.CLV_SUBFUNCION}'/> <c:out value='${requisicion.SUBFUNCION}'/></td>
        <td height="16%" align="left" bgcolor="#C1C1C1"><strong>Color</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.COLOR}'/></td>
      </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Localidad:</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.CLV_LOCALI}'/> <c:out value='${requisicion.LOCALIDAD}'/></td>
        <td height="16%" align="left" bgcolor="#C1C1C1"><strong>Usuario</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.USUARIO}'/></td>
      </tr>
      <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="2" align="right" bgcolor="#C1C1C1"><strong>Prestador de Servicio:</strong></td>
        <td height="16%" align="left"><c:out value='${requisicion.NCOMERCIA}'/></td>
        <td height="16%" align="left" bgcolor="#C1C1C1"><strong>Costo</strong></td>
        <td height="16%" align="right">$<fmt:formatNumber value='${requisicion.COSTO_TOTAL}' pattern="###,###,###.00"/></td>
      </tr>
   <tr bgcolor="#889FC9">
    <td height="25" colspan="5" align="center"><span class="TituloFormulario"><strong>Conceptos</strong></span></td>
  </tr>
  <tr bgcolor="#DBDBDB">
        <td width="8%" height="16%" align="center" bgcolor="#C1C1C1"><strong>Cantidad</strong></td>
        <td width="9%" height="16%" align="center" bgcolor="#C1C1C1"><strong>Unidad</strong></td>
        <td height="16%" align="left" bgcolor="#C1C1C1">&nbsp;</td>
        <td height="16%" align="center" bgcolor="#C1C1C1"><strong>Precio Unit.</strong></td>
        <td height="16%" align="center" bgcolor="#C1C1C1"><strong>&nbsp;Importe</strong></td>
  </tr>
  <c:forEach items="${movimientos}" var="item" varStatus="status" >
  <tr bgcolor="#DBDBDB">
        <td height="16%" align="center"><fmt:formatNumber value='${item.CANTIDAD}' pattern="#####"/></td>
        <td height="16%" align="left"><c:out value='${item.UNIDAD}'/></td>
        <td height="16%" align="left"><c:out value='${item.ARTICULO}'/> ( <c:out value='${item.NOTAS}'/> )</td>
        <td height="16%" align="center">$<fmt:formatNumber value='${item.PRECIO_EST}' pattern="###,###,###.00"/></td>
        <td height="16%" align="right">$<fmt:formatNumber value='${item.IMPORTE}' pattern="###,###,###.00"/></td>
  </tr>
   </c:forEach>
   <tr bgcolor="#889FC9">
    <td height="25" colspan="5" align="center"><span class="TituloFormulario"><strong>Total apartado por la Requisicion</strong></span></td>
  </tr>
  <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="5" align="center">$<fmt:formatNumber value='${requisicion.TOTAL_REQ}' pattern="###,###,###.00"/></td>
  </tr>
  <tr bgcolor="#889FC9">
    <td height="25" colspan="5" align="center"><span class="TituloFormulario"><strong>Observaciones</strong></span></td>
  </tr>
  <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="5" align="center"><c:out value='${requisicion.OBSERVA}'/></td>
  </tr>
</table>
<table width="80%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td align="center">&nbsp;</td>
  </tr>
  <tr>
    <td align="center"><c:if test='${accion==0}'> <input type="button" class="botones" id="btnregresar"   value="Regresar" onclick="history.go(-1)" />
        
    </c:if><input type="button" class="botones" id="btnregresar2"   value="Imprimir en PDF" onclick="imprimir()" style="width:150px"/></td>
  </tr>
</table>
<br />
</form>
</body>
</html>