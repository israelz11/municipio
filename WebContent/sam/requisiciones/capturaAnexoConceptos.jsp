<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorRequisicion.js"> </script>
<script type="text/javascript" src="../../dwr/interface/"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script language="javascript">
<!--
	function guardarAnexo(){
		if($('#txtnotas').attr('value')==''){jAlert('El anexo que intenta guardar no es valido', 'Error de validacion');return false;}		
		controladorRequisicion.guardarAnexoConceptoRequisicion($('#ID_REQ_MOVTO').attr('value'), $('#txtnotas').attr('value'), {
				callback:function(items){
					if (items==true) CloseDelay('Se ha guardado con exito'); else jError('Se ha producido un error al guardar el anexo del concepto', 'Error');
				}
				,
				errorHandler:function(errorString, exception) { 
					jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br><strong>Consulte a su administrador</strong>');   
					return false;
				}
		});
	}
-->
</script>
<title>Captura de conceptos</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-bottom: 0px;
	color:#000;
	font-size:12px;
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
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="18" valign="top"><strong>Escriba el anexo del concepto, máximo 1000 caracteres:</strong>
    <input type="hidden" value="<c:out value='${id_req_movto}'/>" id="ID_REQ_MOVTO"></td>
  </tr>
  <tr>
    <td height="104" valign="top"><textarea name="txtnotas" rows="7" wrap="virtual" class="textarea" id="txtnotas" style="width:500px" maxlength="1000"><c:out value='${texto.TEXTO}'/></textarea></td>
  </tr>
  <tr>
    <td align="center"><input type="button" value="Guardar" id="cmdguardarconcepto" class="botones" onClick="guardarAnexo()" />
      <input type="button" value="Cerrar" id="cmdcerrar" class="botones" onClick="window.parent.$.alerts._hide();" /></td>
  </tr>
</table>
</body>
</html>