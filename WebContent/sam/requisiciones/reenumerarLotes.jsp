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
<script type="text/javascript" src="../../dwr/interface/controladorListadoRequisicionesRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script> 
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
function ReenumerarNuevamente(){
	html ='<table width="220" border="0" cellspacing="0" cellpadding="0" alingn="center">'+
							 '<tr>'+
							 '<td height="20"><strong>Iniciar numeración apartir de:</strong></td>'+
							 '</tr>'+
							 '<tr>'+
							 '<td height="20"><input type="text" id="txtnumero" maxlength="3" value="1" style="width:200px" onkeypress="return keyNumbero(event);"/></td>'+
							 '</tr>'+
							 '<tr>' +
							 '</tr>'+
							 '<td height="20">&nbsp;</td>'+
							 '<tr>'+
							 '<td align="center"><input type="button" class="botones" value="Aceptar"   id="cmdaceptar" style="width:100px"/> <input type="button" class="botones" value="Cancelar" id="cmdcancelar" onclick="$.alerts._hide();" style="width:100px"/></td>'+
							 '</tr>'+
							 '</table>';
	jWindow(html,'Volver a reenumerar lotes', '','',0);
	$('#cmdaceptar').click(function(event){_volverReenumerarDesde();})
}

function _volverReenumerarDesde(){
	var num = $('#txtnumero').attr('value');
	var cve_doc = $('#cve_req').attr('value');
	if(num==''||num==0) {
		jAlert('Escriba un número válido para continuar', 'Advertencia'); return false;
	}
	jConfirm('¿Confirma que desea volver a reenumerar el consecutivo de lotes?','Confirmar', function(r){
			if(r){		
				ShowDelay('Reenumerando lotes...', '');
				controladorListadoRequisicionesRemoto.reenumerarLotesDesde(cve_doc, num,{
						callback:function(items) { 
							if(items){
								window.parent.mostrarTablaConceptos(cve_doc);
								actualizarDatos();
									
							}
							
						},
						errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');   
					}   
				});
			}
		});
}

function actualizarDatos(){
	$("#forma").submit();
}

function ReenumerarLotes(){
	var valLotes = [];
	var idLotes = [];
	var cont = 0;
	 $('input[name=ID_REQ_MOVTO]').each(function() {
		 	if(jQuery.inArray($('#cbolote'+$(this).val()).val(),valLotes)>-1){
				jError('Existen consecutivos de lotes duplicados, revise nuevamente para continuar con la operación','Error'); cont++; return false;
			}
		 	idLotes.push($(this).val());
			valLotes.push($('#cbolote'+$(this).val()).val());
		});	
	if(cont!=0) return false;
	jConfirm('¿Confirma que desea reenumerar los lotes de la requisicion actual?','Confirmar', function(r){
				if(r){
					window.parent.ReenumerarLotesNuevos(idLotes,valLotes, <c:out value='${cve_req}'/>);
				}
		});
}
-->
</script>
</head>
<body> 

<form  action="reenumerarLotes.action" method="post" id="forma" name="forma"> 
  <table width="95%" align="center">
    <tr>
    <td><h1>Lotes de la Requisición <strong><input type="hidden" name="cve_req" value="<c:out value='${cve_req}'/>" id="cve_req"/><input type="hidden" name="num_req" value="<c:out value='${num_req}'/>" id="num_req"/>
    <c:out value='${num_req}'/></strong></h1></td>
  </tr>
  <tr>
    <td align="left"><input type="button" value="Reenumerar" class="botones" onClick="ReenumerarLotes()" />
      <input type="button" value="Volver a reenumerar desde..." class="botones" onClick="ReenumerarNuevamente()" /></td>
  </tr>
</table>
<table border="0" align="center" cellpadding="1" cellspacing="1" width="95%" class="listas">
  <tr bgcolor="#DBDBDB">
    <td height="20" colspan="5" align="left"><strong>Nota:</strong> Determine el orden de los consecutivos en cada uno de los lotes que desea reordenar y pulse el botón &quot;Reenumerar&quot; para completar la operación; en caso contrario si desea volver a reenumerar desde un número especifico pulse &quot;Volver a reenumerar desde...&quot; e introduzca el número desde el cual se empezara a reenumerar.</td></tr>
  <tr bgcolor="#DBDBDB">
    <th width="6%" align="left" bgcolor="#C1C1C1">Cons.    
    <th width="62%" height="20" align="left" bgcolor="#C1C1C1">Concepto&nbsp;
    <th width="9%" align="left" bgcolor="#C1C1C1">Cantidad    
        <th width="11%" align="left" bgcolor="#C1C1C1">Unidad    
        <th width="12%" height="20" align="center" bgcolor="#C1C1C1">Precio Unit.</th>
    </tr>
    <c:set var="cont" value="${0}" />
  <c:forEach items="${movimientos}" var="item" varStatus="status">
    <tr bgcolor="#DBDBDB">
      <td align="center" height="20">
      	<input type="hidden" id="ID_REQ_MOVTO" name="ID_REQ_MOVTO" value="<c:out value='${item.ID_REQ_MOVTO}'/>"/>
          <select name="cbolote<c:out value='${item.ID_REQ_MOVTO}'/>" class="comboBox" id="cbolote<c:out value='${item.ID_REQ_MOVTO}'/>" style="width:80px;">
            <c:forEach items="${numlotes}" var="item2">
                <option value='<c:out value="${item2.REQ_CONS}"/>'
                  <c:if test='${item2.REQ_CONS==item.REQ_CONS}'> selected </c:if>
                  >
                  <c:out value='${item2.REQ_CONS}'/>
                </option>
            </c:forEach>
          </select>
        </td>
      <td align="left"><c:out value='${item.ARTICULO}'/>
        (
        <c:out value='${item.NOTAS}'/>
        )</td>
      <td height="20" align="center"><fmt:formatNumber value='${item.CANTIDAD}' pattern="#####"/></td>
      <td height="20" align="center"><c:out value='${item.UNIDAD}'/></td>
      <td height="20" align="center">$ <fmt:formatNumber value='${item.PRECIO_EST}' pattern="###,###,###.00"/></td>
      </tr>
      <c:set var="cont" value="${cont+1}" /> 
  </c:forEach>
  </table>
  <c:if test='${cont>9}'>
<table width="95%" align="center">
  <tr>
    <td align="left"><input type="button" value="Reenumerar" class="botones" onClick="ReenumerarLotes()" />
      <input type="button" value="Volver a reenumerar desde..." class="botones" onClick="ReenumerarNuevamente()" /></td>
  </tr>
</table>
</c:if>
</form>
</body>
</html>