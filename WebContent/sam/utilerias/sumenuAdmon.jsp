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
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/sweetalert2.css" type="text/css"/>

<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>

<script type="text/javascript" src="../../include/js/sweetalert2.js"></script>

<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorListadoRequisicionesRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script language="javascript">

$(document).ready(function() {
		/*solo si es pedido*/
		/*if( window.parent.modulo=='Pedidos'){
			$('#f3x').show();
		}
		else
			$('#f3x').hide();*/
});

function adminFunction(cve_doc, modulo, fn, cve_pers) {
	switch(fn)
	{
		case 'cargarArchivosOrdenPago': window.parent.mostrarCargarArchivosOrdenPago(cve_doc, $('#HD_NUM_DOC').attr('value')); 
			break;
		case 'reemplazarArchivosFacturas': window.parent.mostrarReemplazarArchivosFactura(cve_doc, $('#HD_NUM_DOC').attr('value')); 
			break;
		case 'abrirDocumento': window.parent.abrirDocumento(); 
			break;
		case 'cambiarGrupoFirmas': window.parent.cambiarGrupoFirmas(cve_doc, modulo);
			break;
		case 'cambiarFechaReqOtOs': cambiarFecha2(cve_doc, modulo);
			break;
		case 'reembolsos': window.parent.reembolsos(cve_doc, modulo);
			break;
		case 'reduccionAmpliacion': window.parent.reduccionAmpliacion(cve_doc, modulo, $('#HD_NUM_DOC').val());
			break;
		case 'ajustesImportes': window.parent.ajustesImportes(cve_doc, modulo, $('#HD_NUM_DOC').val());
			break;
		case 'cambiarFechaPeriodo': window.parent.cambiarFechaPeriodo(cve_doc, modulo);
			break;
		case 'cambiarUsuarioDocumento': window.parent.cambiarUsuarioDocumento(cve_doc, modulo, cve_pers);
			break;
		case 'cambiarBeneficiario': window.parent.cambiarBeneficiario(cve_doc, modulo);
			break;
		case 'verBitacora': window.parent.bitacoraDocumento(cve_doc, modulo.toUpperCase());
			break;
	}
}

/******************************************************* Cambio de Fechas ********************************************************************/
function cambiarFecha2(cve_doc, modulo){
	var smodulo = "";
	if(modulo=='req') smodulo = "Requisiciones";
	if(modulo=='ped') smodulo = "Pedidos";
	if(modulo=='op') smodulo ="Ordenes de Pago";
	if(modulo=='val') smodulo ="Vales";
	
	if(modulo=='req'){
		controladorListadoRequisicionesRemoto.getFechaIngreso(cve_doc, {
						callback:function(items) { 		
							var html = '<table width="350" border="0" cellspacing="0" cellpadding="0">' +
							'<tr>'+
								'<td height="20">Número Requisición:</td>'+
								'<td><strong>'+items.NUM_REQ+'</strong></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="30">Fecha actual dd/mm/aaaa:</td>'+
								'<td><input type="text" id="txtfechaactual" value="'+items.FECHA+'" style="width:140px" /></td>'+
							  '</tr>'+
							  '<tr>'+
								'<td height="50" align="center" colspan="2"><input type="button" value="Aplicar cambios" id="cmdaplicar" class="botones" style="width:100px"/>&nbsp;<input type="button" value="Cancelar" id="cmdcancelar" class="botones" style="width:100px"/></td>'+
							  '</tr>'+
							'</table>';
	
							jWindow(html,'Cambio de fecha de ingreso OT/OS '+smodulo, '','',0);
							if(modulo=='req')
								$('#cmdaplicar').click(function(event){_cambiarFechaIngresoRequisicion(cve_doc);})
							
							$('#cmdcancelar').click(function(event){$.alerts._hide();})
							$('#txtfechaactual').keypress(function(event){if (event.keyCode == '13'){$('#cmdaplicar').click();}});	
							
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});
	}
}
/*********************************************/
function _cambiarFechaIngresoRequisicion(cve_doc){
	var periodo = $('#cboperiodo').val();
	var fecha = $('#txtfechaactual').attr('value');
	alert("demo de cambio en requisicion..........");
	jConfirm('¿Confirma que desea cambiar la fecha de ingreso del documento actual?','Confirmar', function(r){
		if(r){
			_closeDelay();
			ShowDelay('Cambiando fecha de ingreso...','');
			controladorListadoRequisicionesRemoto.cambiarFechaIngreso(cve_doc, fecha, {
						callback:function(items) { 	
								CloseDelay('Fecha de ingreso cambiados con éxito');
						} 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					 }
		});	
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
<div align="center" style="height:20px"><strong>Opciones disponibles para el documento: 
    <c:out value='${NUM_DOC}'/></strong><input type="hidden" id="HD_NUM_DOC" value="${NUM_DOC}" /></div>
<table class="listas" border="0" align="center" cellpadding="1" cellspacing="2" width="333" >
		<c:if test="${modulo!='con'}">
            <tr id='f1' onMouseOver="color_over('f1')" onMouseOut="color_out('f1')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'cambiarGrupoFirmas')">
            <td width="27" height="27" align="center" style="cursor:pointer"><c:out value='${item.NUM_DOC}'/>
              <img src="../../imagenes/report_edit.png" width="16" height="16" /></td>
            <td width="296" height="27" align="left" style="cursor:pointer">Cambiar el grupo de firmas del documento
              <c:out value='${item.FECHA_DOC}'/></td>
            </tr>
        </c:if>
        
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_OPCION_EDITAR_DOCUMENTOS_SUBMENU">
        <tr id='f2' onMouseOver="color_over('f2')" onMouseOut="color_out('f2')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'abrirDocumento')">
          <td height="27" align="center"  style="cursor:pointer"><img src="../../imagenes/page_white_edit.png" width="18" height="18" /></td>
          <td height="27" align="left" style="cursor:pointer">Abrir el documento para su edición</td>
        </tr>
        </sec:authorize>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_REEMBOLSO_DE_PEDIDOS">
         	<c:if test="${modulo=='con'}">
                <tr id='f3x' onMouseOver="color_over('f3x')" onMouseOut="color_out('f3x')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'reduccionAmpliacion')">
                  <td height="27" align="center" style="cursor:pointer"><img src="../../imagenes/money.png" width="16" height="16" /></td>
                  <td height="27" align="left" style="cursor:pointer">Reducción y Ampliación de Conceptos</td>
                </tr>
            </c:if>
        </sec:authorize>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_REEMBOLSO_DE_PEDIDOS">
         	<c:if test="${modulo=='con'  || modulo=='fac' || modulo=='ped'}">
                <tr id='f33x' onMouseOver="color_over('f33x')" onMouseOut="color_out('f33x')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'ajustesImportes')">
                  <td height="27" align="center" style="cursor:pointer"><img src="../../imagenes/money.png" width="16" height="16" /></td>
                  <td height="27" align="left" style="cursor:pointer">Cuadrar o Ajustar Importes</td>
                </tr>
            </c:if>
        </sec:authorize>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_OPCION_CAMBIAR_FECHA_DOCUMENTOS_REQ_OT_OS_INGRESO">
        	<c:if test="${modulo=='req'}">
                <tr id='f3' onMouseOver="color_over('f3')" onMouseOut="color_out('f3')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'cambiarFechaReqOtOs')">
                  <td height="27" align="center" style="cursor:pointer"><img src="../../imagenes/calendar_edit.png" width="16" height="16" /></td>
                  <td height="27" align="left" style="cursor:pointer">Cambiar la fecha de Ingreso Req. OT. / Req. OS.</td>
                </tr>
            </c:if>
         </sec:authorize>
         <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_OPCION_CAMBIAR_FECHA_DOCUMENTOS_SUBMENU">
         	<c:if test="${modulo!='con'}">
                <tr id='f33' onMouseOver="color_over('f33')" onMouseOut="color_out('f33')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'cambiarFechaPeriodo')">
                  <td height="27" align="center" style="cursor:pointer"><img src="../../imagenes/calendar_edit.png" width="16" height="16" /></td>
                  <td height="27" align="left" style="cursor:pointer">Cambiar la fecha y periodo del documento</td>
                </tr>
            </c:if>
         </sec:authorize>
         <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_OPCION_REEMPLAZAR_ARCHIVOS_FACTURAS">
             <c:if test="${modulo=='fac'}">
                <tr id='f44' onMouseOver="color_over('f44')" onMouseOut="color_out('f44')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'reemplazarArchivosFacturas')">
                  <td height="27" align="center" style="cursor:pointer"><img src="../../imagenes/application_view_tile.png" width="16" height="16" /></td>
                  <td height="27" align="left" style="cursor:pointer">Reemplazar archivos de Facturas</td>
                </tr>
             </c:if>
         </sec:authorize>
         
         <c:if test="${modulo=='op'}">
                <tr id='f55' onMouseOver="color_over('f55')" onMouseOut="color_out('f55')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'cargarArchivosOrdenPago')">
                  <td height="27" align="center" style="cursor:pointer"><img src="../../imagenes/application_view_tile.png" width="16" height="16" /></td>
                  <td height="27" align="left" style="cursor:pointer">Cargar archivos a Ordenes de Pago</td>
                </tr>
         </c:if>
         
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_OPCION_CAMBIAR_USUARIO_DOCUMENTO_SUBMENU">
        	<c:if test="${modulo!='con'}">
                <tr id='f4' onMouseOver="color_over('f4')" onMouseOut="color_out('f4')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'cambiarUsuarioDocumento', <c:out value='${cve_pers2}'/>)">
                  <td height="27" align="center" style="cursor:pointer"><img src="../../imagenes/icon_user.gif" width="16" height="16" /></td>
                  <td height="27" align="left" style="cursor:pointer">Cambiar de usuario el documento y Unidad Administrativa</td>
                </tr>
            </c:if>
         </sec:authorize>
         <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_OPCION_CAMBIAR_BENEFICIARIO_DOCUMENTO_SUBMENU">
         	<c:if test="${modulo!='con'}">
                <tr id='f5' onMouseOver="color_over('f5')" onMouseOut="color_out('f5')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'cambiarBeneficiario')">
                  <td height="27" align="center" style="cursor:pointer"><img src="../../imagenes/user_gray.png" width="16" height="16" /></td>
                  <td height="27" align="left" style="cursor:pointer">Cambiar el beneficiario o proveedor del documento</td>
                </tr>
            </c:if>
        </sec:authorize>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_BITACORA_EN_REQUISICIONES">
        <tr id='f6' onMouseOver="color_over('f6')" onMouseOut="color_out('f6')" onclick="adminFunction(<c:out value='${cve_doc}'/>, '<c:out value='${modulo}'/>', 'verBitacora')">
          <td height="27" align="center" style="cursor:pointer"><img src="../../imagenes/report_user.png" alt="" width="16" height="16" /></td>
          <td height="27" align="left" style="cursor:pointer">Mostrar bitacora de movimientos</td>
        </tr>
        </sec:authorize>
</table>
</body>
</html>