<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
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
</style>
<script language="javascript">
<!--
function cargar(){
	var checkStatus = [];
     $('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});	 
	 if (checkStatus.length==0 ) {jAlert('Es nercesario seleccionar un estatus'); return false;}
	 document.location = "muestraImportar.action?num_req="+$('#txtrequisicion').attr('value')+"&status="+checkStatus+"&idUnidad="+$('#cbodependencia').val();
}

function setCheckAll(check){	
	$("INPUT[@name='"+check+"'][type='checkbox']").attr('checked', $('#checkall').is(':checked'));
}

function cargarLotes(){
	var checkLotes = [];
     $('input[name=chklotes]:checked').each(function() {checkLotes.push($(this).val());});	
	 if (checkLotes.length==0 ) {jAlert('Es nercesario seleccionar por lo menos un lote para continuar'); return false;}
	jConfirm('¿Confirma que desea cargar nuevos lotes a la requisicion actual?','Confirmar', function(r){
				if(r){
					window.parent.CargarLotesNuevos(checkLotes,<c:out value='${cve_req}'/>);
				}
		});
}
-->
</script>
</head>
<body> 
<form  action="muestraImportar.action" method="post" id="forma" name="forma"> 
<c:if test='${accion==null}'>
<table width="95%" align="center">
  <tr>
    <td><h1>Listado de  Requisiciones
      <c:out value='${desMes}'/>
    de la Unidad</h1></td>
  </tr>
</table>
<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="95%">
  <tr bgcolor="#889FC9">
    <td height="32" colspan="4" align="left">Unidad Administrativa: <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      <c:out value="${nombreUnidad}"/> 
      <input type="hidden" name="cbodependencia" id="cbodependencia" value='<c:out value="${idUnidad}"/>' />
      </sec:authorize>
      <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
        <select name="cbodependencia" class="comboBox" id="cbodependencia" style="width:480px">
          <option value="0">[Todas la Unidades Administrativas]</option>
          <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
            <option value='<c:out value="${item.ID}"/>' 
                        <c:if test='${item.ID==idUnidad}'> selected </c:if>>
            <c:out value='${item.DEPENDENCIA}'/>
            </option>
            </c:forEach>
          </select>
        </sec:authorize>
      </td>
  </tr>
  <tr bgcolor="#889FC9">
    <td height="31" colspan="4" align="left" valign="middle">Numero de Documento: 
      <input type="text" id="txtrequisicion" class="input" style="width:150px" value="<c:out value='${num_req}'/>" maxlength="16" onblur="upperCase(this)"/> 
      <input type="checkbox" value="1" id="status" name="status" <c:if test="${fn:contains(status,'1')}">checked</c:if>/>
      Cerrados 
        <input type="checkbox" value="2" id="status" name="status" <c:if test="${fn:contains(status,'2')}">checked</c:if>/>
      En proceso 
      <input type="checkbox" value="5" id="status" name="status" <c:if test="${fn:contains(status,'5')}">checked</c:if>/>
      Finiquitadas 
      <input type="button" value="Buscar todo" id="cmdbuscar" class="botones" onclick="cargar()" /></td>
  </tr>
  <tr bgcolor="#889FC9">
    <th width="15%" height="21" align="center">Num. Documento</th>
    <th width="10%" height="21" align="center">Programa / Partida</th>
    <th width="64%" height="21" align="center">Concepto</th>
    <th width="11%" align="center">Importe</th>
  </tr>
   <c:set var="cont" value="${0}" />
  <c:forEach items="${documentos}" var="item" varStatus="status" >
    <c:set var="cont" value="${cont+1}" /> 
    <tr>
      <td width="15%" height="20" align="left"><a href="muestraImportar.action?accion=lotes&cve_req=<c:out value='${item.CVE_REQ}'/>&num_req=<c:out value='${item.NUM_REQ}'/>"><c:out value='${item.NUM_REQ}'/></a></td>
      <td height="20" align="center"><c:out value='${item.N_PROGRAMA}'/>
      / <c:out value='${item.CLV_PARTID}'/></td>
      <td width="64%" height="20" align="left"><c:out value='${item.OBSERVA}'/></td>
      <td width="11%" height="20" align="right">$<fmt:formatNumber value='${item.IMPORTE}' pattern="###,###,###.00"/>&nbsp;</td>
      <input type="hidden" value="<c:out value='${item.CVE_CONTRATO}'/>" id="hdcontrato<c:out value='${item.CVE_PED}'/>">
      </tr>
  </c:forEach>
</table>
</c:if>
<c:if test='${accion!=null}'>
  <table width="95%" align="center">
    <tr>
    <td><h1>Lotes de la Requisición <strong>
    <c:out value='${num_req}'/></strong></h1></td>
  </tr>
  <tr>
    <td><input type="button" value="Regresar" id="cmdregresar" class="botones" onClick="history.go(-1)" />
      <input type="button" value="Cargar lotes" id="cmdcargar1" class="botones" onClick="cargarLotes()" /></td>
  </tr>
</table>
<table border="0" align="center" cellpadding="1" cellspacing="1" width="95%" class="listas">
  <tr bgcolor="#DBDBDB">
    <th width="3%" height="20" align="left" bgcolor="#C1C1C1"><input type="checkbox" id="checkall" value="0" name="checkall" onClick="setCheckAll('chklotes')"/>
    <th width="5%" align="left" bgcolor="#C1C1C1">Cons.    
    <th width="49%" height="20" align="left" bgcolor="#C1C1C1">Concepto&nbsp;
    <th width="10%" align="left" bgcolor="#C1C1C1">Cantidad    
        <th width="10%" align="left" bgcolor="#C1C1C1">Unidad    
        <th width="12%" height="16%" align="center" bgcolor="#C1C1C1">Precio Unit.</th>
    <th width="11%" height="16%" align="center" bgcolor="#C1C1C1">&nbsp;Importe</th>
    </tr>
    <c:set var="cont" value="${0}" />
  <c:forEach items="${movimientos}" var="item" varStatus="status" >
    <tr bgcolor="#DBDBDB">
      <td height="16%" align="center"><input type="checkbox" id="chklotes" name="chklotes" value="<c:out value='${item.ID_REQ_MOVTO}'/>"/></td>
      <td height="16%" align="center"><c:out value='${item.REQ_CONS}'/></td>
      <td align="left"><c:out value='${item.ARTICULO}'/>
        (
        <c:out value='${item.NOTAS}'/>
        )</td>
      <td height="16%" align="center"><fmt:formatNumber value='${item.CANTIDAD}' pattern="#####"/></td>
      <td height="16%" align="center"><c:out value='${item.UNIDAD}'/></td>
      <td height="16%" align="center">$
        <fmt:formatNumber value='${item.PRECIO_EST}' pattern="###,###,###.00"/></td>
      <td height="16%" align="right">$
        <fmt:formatNumber value='${item.IMPORTE}' pattern="###,###,###.00"/></td>
      </tr>
      <c:set var="cont" value="${cont+1}" /> 
  </c:forEach>
  </table>
  </c:if>
  <c:if test='${cont>9&&accion!=null}'>
<table width="95%" align="center">
  <tr>
    <td><input type="button" value="Regresar" id="cmdregresar2" class="botones" onClick="history.go(-1)" />
      <input type="button" value="Cargar lotes" id="cmdcargar2" class="botones" onClick="cargarLotes()" /></td>
  </tr>
</table>
</c:if>
</form>
</body>
</html>