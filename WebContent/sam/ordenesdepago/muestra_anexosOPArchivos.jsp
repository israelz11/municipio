<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/jquery.form.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorOrdenPagoRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script> 
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
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
$(document).ready(function(){
	var options = { 
        beforeSubmit:  showRequest,  
        success:       showResponse, 
        url:       '_subirArchivoAnexoOP.action',
        type:      'post', 
        dataType:  'json'
    }; 
	
	$('#forma').submit(function(){
		$(this).ajaxSubmit(options);
		return false;
	});
	
  $('#BorraOs2').click(function(event){guardarAnexos();});
    $('#cmdNuevoAnexo').click(function(event){limpiarAnexos();});
});

function eliminarRetencion(cons){
	  var checkRetenciones = [];
	  checkRetenciones.push(cons);
    // $('input[name=clavesRetencion]:checked').each(function() {checkRetenciones.push($(this).val());	 });	 
 	 
  	 var idOrden=$('#id_orden').attr('value');
	 if (checkRetenciones.length > 0 ) {
		 jConfirm('¿Confirma que desea eliminar la retención?','Eliminar retención', function(r){
			 if(r){
				 	ShowDelay('Eliminando retencione(s)','');
					controladorOrdenPagoRemoto.eliminarDocumentos(checkRetenciones,idOrden, {
					callback:function(items) { 	
					   CloseDelay("Retencion(es) eliminada(s) con éxito");
					   document.location = "muestra_anexosOPArchivos.action?cve_op="+idOrden;
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');
					}
				},async=false );
			}
		});
     	 } else 
	    jAlert('Es necesario que seleccione un elemento de la lista', 'Advertencia');
	 }

function limpiarAnexos(){
	$('#idDocumento').attr('value', '0');
	$('#tipoMovDoc').val(0);	
	$('#numeroDoc').attr('value','');
	$('#notaDoc').val('');
	$('#tipoMovDoc').focus();
	$('#archivo').val("");
}

function guardarAnexos(){
	var file = $("#archivo").val();
	if(file=="")
		guardarDocumento();
	else
		$('#forma').submit();
}


 function subirArchivo(){
	ShowDelay('Guardando anexo','');
	$('#forma').submit();
}

function showRequest(formData, jqForm, options) { 
    return true; 
} 
 
function showResponse(data)  { 
var idOrden=$('#id_orden').attr('value');
 	if(data.mensaje){
		CloseDelay("Anexo guardado con éxito");
		$('#archivo').attr('value','');
		document.location = "muestra_anexosOPArchivos.action?cve_op="+idOrden;
		
	}
	else{
		_closeDelay();
		jError("No se ha podido cargar el archivo, consulte a su administrador", "Error");
	}
} 

 function guardarDocumento(){
	 var error="";  
    if ($('#tipoMovDoc').attr('value')=="") {jAlert('El tipo de documento no es válido'); return false;}
    if ($('#numeroDoc').attr('value')=="") {jAlert('El número de documento no es válido'); return false;}

	  var idOrden=$('#id_orden').attr('value');
						
						ShowDelay('Guardando anexo','');
						controladorOrdenPagoRemoto.guardarDocumento($('#idDocumento').attr('value'),$('#tipoMovDoc').attr('value'),$('#numeroDoc').attr('value'),$('#notaDoc').attr('value'),idOrden,{
						callback:function(items) { 	 
						
						 CloseDelay("Anexos guardados con éxito");	  
						 document.location = "muestra_anexosOPArchivos.action?cve_op="+idOrden;
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString, 'Error');         
						}
	  
	  
	}); 
	
	
 }
 
 function eliminarDocumentos(){
	  var checkDocumentos = [];
     $('input[name=clavesDocumentos]:checked').each(function() {  checkDocumentos.push($(this).val());	 });	 
	 if (checkDocumentos.length > 0 ) {
   	 var idOrden=$('#id_orden').attr('value');
	 jConfirm('¿Confirma que desea eliminar los anexos?','Eliminar anexos', function(r){
		 	if(r){
					ShowDelay('Eliminando anexo(s)','');
					controladorOrdenPagoRemoto.eliminarDocumentos(checkDocumentos,idOrden, {
					callback:function(items) { 	
						llenarTablaDeDocumentos();	
					   CloseDelay("Anexos eliminados con éxito");
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');     
					}
				},async=false ); 
			}
		 });
     
	 } else 
	    jAlert("Es necesario seleccionar almenos un elemento de la lista");
 }

function cargarOSOT(num_req, cve_req, clv_benefi, total){
	window.parent.regresarOSOTFactura(num_req, cve_req, clv_benefi, total);
}
-->
</script>

</head>

<body>
<form name="forma" id="forma" method="post" enctype="multipart/form-data">
<input type="hidden" id="id_orden" name="id_orden" value="${cve_op}" />
<input type="hidden" id="cve_op" name="cve_op" value="${cve_op}" />
<input type="hidden" name="idDocumento" id="idDocumento" value="0">
<table class="listas" border="0" align="center" cellpadding="0" cellspacing="0" width="95%">
  <tr bgcolor="#889FC9">
    <td height="21" colspan="6" align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="formulario">
      <tr>
        <th height="30">*Tipo de movimiento:</th>
        <td><div class="styled-select">
          <select name="tipoMovDoc" class="comboBox" id="tipoMovDoc" style="width:180px">
            <c:forEach items="${tipoDocumentos}" var="item" varStatus="status">
              <option value="${item.T_DOCTO}">
                <c:out value="${item.DESCR}"/>
                </option>
            </c:forEach>
          </select>
        </div>
          <input type="hidden" name="idDocumento" id="idDocumento" value="0" /></td>
      </tr>
      <tr>
        <th height="30">*Número:</th>
        <td><input name="numeroDoc" type="text" class="input" id="numeroDoc"  style="width:180px" /></td>
      </tr>
      <tr>
        <th height="63">*Notas:</th>
        <td><textarea  name="notaDoc"  id="notaDoc"  cols="80" rows="4" wrap="virtual" class="textarea" maxlength="500" style="width:445px"></textarea></td>
      </tr>
      <tr>
        <th height="32">Archivo:</th>
        <td><input type="file" class="input-file" id="archivo" name="archivo" style="width:445px" /></td>
      </tr>
      <tr>
        <th width="18%" height="32">&nbsp;</th>
        <td width="82%"><table width="400" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><div class="buttons tiptip">
              <button name="cmdNuevoAnexo" id="cmdNuevoAnexo" onclick="" type="button" class="button red middle"><span class="label" style="width:150px">Nuevo Anexo</span></button>
            </div></td>
            <td><div class="buttons tiptip">
              <button name="BorraOs2" id="BorraOs2" type="button" class="button blue middle"><span class="label" style="width:150px">Guardar Anexo</span></button>
            </div></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="20" colspan="2">&nbsp;</td>
        </tr>
    </table></td>
  </tr>
  <tr bgcolor="#889FC9">
    <th width="11%" height="21" align="center">Tipo</th>
    <th width="14%" height="21" align="center">Número</th>
    <th width="29%" height="21" align="center">Nota</th>
    <th width="34%" align="center">Archivo</th>
    <th width="9%" align="center">Formato</th>
    <th width="3%" align="center">&nbsp;</th>
  </tr>
   <c:set var="cont" value="${0}" />
  <c:forEach items="${muestraArchivos}" var="item" varStatus="status" >
  <c:set var="cont" value="${cont+1}" /> 
<tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
            <tr>
              <td width="11%" height="20" align="center"><c:out value='${item.T_DOCTO}'/></td>
        <td height="20" align="center"><c:out value='${item.NUMERO}'/></td>
        <td width="29%" height="20" align="left"><c:out value='${item.NOTAS}'/></td>
        <td width="34%" height="20" align="left"><strong><a href='${item.FILEPATH}${item.FILENAME}' target='_blank'>${item.FILENAME}</a></strong></td>
        <td width="9%" height="20" align="center">${item.FILETYPE}</td>
        <td width="3%" height="20" align="center"><img src="../../imagenes/cross.png" alt="" width="16" height="16" style='cursor: pointer;' onclick="eliminarRetencion(${item.ANX_CONS})" /></td>
        </tr>
  </c:forEach>
   <tr>
          <td height="27" style="background:#FFF" colspan="6" align="left"><c:if test="${cont>0}"></c:if> </td>
          </tr>
      
 
</table>
</form>
</body>
</html>