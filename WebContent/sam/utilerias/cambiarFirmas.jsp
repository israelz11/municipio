<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Bitacora de Movimientos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>

<link rel="stylesheet" href="../../include/css/sweet-alert.css" type="text/css"/>

<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>

<script type="text/javascript" src="../../include/js/sweet-alert.js"></script>


<script type="text/javascript" src="../../dwr/engine.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorCambiarGrupoFirmaRemoto.js"> </script>
<script language="javascript">

function adminFunction(cve_doc, cve_pers, modulo, fn) {
	switch(fn)
	{
		case 'cambiarGrupoFirmas': window.parent.cambiarGrupoFirmas(cve_doc, cve_pers, modulo);
			break;
	}
}

function enviar(){
	$('#forma').action = "cambiarFirmas.jsp?cve_doc"+$('#cve_doc').attr('value')+"&modulo="+$('#modulo').attr('value');
	$('#forma').submit();
}

function cambiarFirma(){
	
	swal({
		  title: "Estas seguro?",
		  width: 350,
		  padding: 100,		  
		  text: "Proceso de actualizacion, para cambio de firma de un documento!",
		  type: "warning",
		  showCancelButton: true,
		  confirmButtonColor: "#DD6B55",
		  confirmButtonText: "Si, Cambiar!",
		  cancelButtonText: "No, Cancelar!",
		  closeOnConfirm: false,
		  closeOnCancel: false
		  
		},
		function(isConfirm){
		  if (isConfirm) {
		    swal("Cambio de firma!", "Cambiando firma el documento: " + $('#cve_doc').attr('value')+".", "success");
		    controladorCambiarGrupoFirmaRemoto.cambiarGrupo($('#cve_doc').attr('value'), $('#modulo').attr('value'), $('#grupo').attr('value'));
		    
		  } else {
			    swal("Proceso Cancelado", "Tu documento: "+ $('#cve_doc').attr('value')+ " no se cambio de firma: " , "error");
		  }
		});
}


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
</style>
</head>

<body>
<form name="forma" id="forma" method="get" action="">
<table width="689" class="listas" border="0" cellspacing="0" cellpadding="0" align="center">
    <th height="20" colspan="5">Grupo de firma actual del documento</th>
  <tr>
    <td height="24" colspan="4" align="center"><c:out value='${GRUPO_ACTUAL_DOC}'/>
      <input type="hidden" name="cve_doc" id="cve_doc" value="<c:out value='${cve_doc}'/>" />
      <input type="hidden" name="modulo" id="modulo" value="<c:out value='${modulo}'/>" /></td>
  </tr>
        <th colspan="5" height="20">    Catálogo de firmas disponibles</th>
  <tr>
    <td colspan="4" align="center">
    <select name="grupo" size="1" class="comboBox" id="grupo" onchange="enviar();" style="width:445px;">
      <option value="0">[Seleccione un grupo de firmas]</option>
      <c:forEach items="${grupos}" var="item" varStatus="status">
        <option value="<c:out value='${item.ID_GRUPO_CONFIG}'/>" <c:if test="${item.ID_GRUPO_CONFIG==grupo}"> selected </c:if>>
          <c:out value="${item.GRUPO_CONFIG}"/>
          </option>
      </c:forEach>
    </select>
    <input name="button" type="button" class="botones" id="button" value="Cambiar firmas" onclick="cambiarFirma()" style="width:100px"/></td>
  </tr>
  <tr>
    <th colspan="5" height="20">Orden de Servicio.  
  </th>
  <tr>
    <td width="173" height="66" align="center"><table width="165" border="1"  bordercolor="#030303"  cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Solicitó</strong><br /><c:out value='${REQ_OS_SOLICITO_NOMBRE}'/><br />
          <c:out value='${REQ_OS_SOLICITO_CARGO}'/></span></td>
      </tr>
    </table></td>
    <td width="173" align="center"><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Vo. Bo.</strong><br />
          <c:out value='${REQ_OS_VOBO_NOMBRE}'/>
          <br />
          <c:out value='${REQ_OS_VOBO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td width="175" align="center"><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Autorizó</strong><br />
          <c:out value='${REQ_OS_AUTORIZO_NOMBRE}'/>
          <br />
          <c:out value='${REQ_OS_AUTORIZO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td width="168" align="center">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="4">&nbsp;</td>
  </tr>
    <tr>
    <th colspan="5" height="20">Orden de Trabajo.  
  </th>
  <tr>
    <td width="173" height="66" align="center"><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Solicitó</strong><br />
          <c:out value='${REQ_OT_SOLICITO_NOMBRE}'/>
          <br />
          <c:out value='${REQ_OT_SOLICITO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td width="173" align="center"><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Vo. Bo.</strong><br />
          <c:out value='${REQ_OT_VOBO_NOMBRE}'/>
          <br />
          <c:out value='${REQ_OT_VOBO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td width="175" align="center"><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Autorizó</strong><br />
          <c:out value='${REQ_OT_AUTORIZO_NOMBRE}'/>
          <br />
          <c:out value='${REQ_OT_AUTORIZO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td width="168" align="center">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="4">&nbsp;</td>
  </tr>
  <th colspan="5" height="20">
    Requisiciones  
  </th>
  <tr>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Vo. Bo. Administrativo</strong><br />
          <c:out value='${REQ_VOBOADMIVO_NOMBRE}'/>
          <br />
          <c:out value='${REQ_VOBOADMIVO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Solicitó</strong><br />
          <c:out value='${REQ_SOLICITO_NOMBRE}'/>
          <br />
          <c:out value='${REQ_SOLICITO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Vo. Bo.</strong><br />
          <c:out value='${REQ_VOBO_NOMBRE}'/>
          <br />
          <c:out value='${REQ_VOBO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Autorizó</strong><br />
          <c:out value='${REQ_AUTORIZO_NOMBRE}'/>
          <br />
          <c:out value='${REQ_AUTORIZO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <th colspan="5" height="20">
    Pedidos  
  </th>
  <tr>
    <td>&nbsp;</td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Elaboró</strong><br />
          <c:out value='${PED_ELABORO_NOMBRE}'/>
          <br />
          <c:out value='${PED_ELABORO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Autorizó</strong><br />
          <c:out value='${PED_AUTORIZO_NOMBRE}'/>
          <br />
          <c:out value='${PED_AUTORIZO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <th colspan="5" height="20">
    Ordenes de Pago  
  </th>
  <tr>
    <td height="60"><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>El titular del Ramo</strong><br />
          <c:out value='${OP_TITULAR_NOMBRE}'/>
          <br />
          <c:out value='${OP_TITULAR_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td height="60">&nbsp;</td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Vo. Bo.</strong><br />
          <c:out value='${OP_VOBO_NOMBRE}'/>
          <br />
          <c:out value='${OP_VOBO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Revisó</strong><br />
          <c:out value='${OP_REVISO_NOMBRE}'/>
          <br />
          <c:out value='${OP_REVISO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Suficiencia Presupuestal</strong><br />
          <c:out value='${OP_PROGRAMACION_NOMBRE}'/>
          <br />
          <c:out value='${OP_PROGRAMACION_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Pagador</strong><br />
          <c:out value='${OP_PAGADOR_NOMBRE}'/>
          <br />
          <c:out value='${OP_PAGADOR_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <th colspan="4" height="20">Vales</th>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Vo. Bo. Suficiencia Presupuest.</strong><br />
          <c:out value='${VAL_SOLICITANTE_NOMBRE}'/>
          <br />
          <c:out value='${VAL_SOLICITANTE_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Vo. Bo. Suficiencia Presupuest.</strong><br />
          <c:out value='${VAL_REVISO_NOMBRE}'/>
          <br />
          <c:out value='${VAL_REVISO_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Vo. Bo. Suficiencia Presupuest.</strong><br />
          <c:out value='${VAL_VOBO_PROGRAMACION_NOMBRE}'/>
          <br />
          <c:out value='${VAL_VOBO_PROGRAMACION_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td><table width="165" border="1" bordercolor="#030303" cellspacing="0" cellpadding="0">
      <tr>
        <td width="161" height="52" align="center"><span style="font-size:10px"><strong>Páguese</strong><br />
          <c:out value='${VAL_PAGUESE_NOMBRE}'/>
          <br />
          <c:out value='${VAL_PAGUESE_CARGO}'/>
        </span></td>
      </tr>
    </table></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
</form>
</body>
</html>