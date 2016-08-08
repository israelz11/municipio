<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Listado de Documentos</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../dwr/util.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/ControladorListadoMinutariosRemoto.js"> </script>
<script type="text/javascript" src="lst_busquedas.js"> </script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
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
<form action="lst_minutarios.action"  method="get" id="frm" name="frm">
<table width="95%" align="center"><tr><td><h1>Documental - Listado de documentos</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr >
    <th height="25">&nbsp;</th>
    <td colspan="3">
      <input name="ID_PERSONA_DESTINO" type="hidden" id="ID_PERSONA_DESTINO" value="${cve_persDestino}" />
      </td>
    <td>&nbsp;</td>
  </tr>
  <tr >
    <th height="30">Dependencia fuente:</th>
    <td colspan="3">
    <sec:authorize ifNotGranted="ROLE_Almacen_PRIVILEGIOS_VER_UNIDADES_ADMINISTRATIVAS">
      	<c:out value="${nombreUnidad}"/>
        <input type="hidden" id="cbodependencia" name="cbodependencia" value="<c:out value='${cbodependencia}'/>">
    </sec:authorize>
    <sec:authorize ifAllGranted="ROLE_Almacen_PRIVILEGIOS_VER_UNIDADES_ADMINISTRATIVAS">
    	<div class="styled-select">
            <select name="cbodependencia" id="cbodependencia" style="width:445px">
                          <option value="0">[Seleccione]</option>
                          <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
                          <option value='<c:out value="${item.ID}"/>' 
                            <c:if test='${item.ID==idUnidad}'> selected </c:if>>
                            <c:out value='${item.DEPENDENCIA}'/>
                            </option>
                          </c:forEach>
             </select>
          </div>
       </sec:authorize> 
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr >
    <th height="30">Dependencia destino:</th>
    <td colspan="3"><span class="styled-select">
      <select name="cbodependenciaDestino" id="cbodependenciaDestino" style="width:445px">
        <option value="0">[Seleccione]</option>
        <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">
          <option value='<c:out value="${item.ID}"/>' 
            <c:if test='${item.ID==idDependenciaDestino}'> selected </c:if>
            >
            <c:out value='${item.DEPENDENCIA}'/>
            </option>
        </c:forEach>
      </select>
      </span></td>
    <td width="194" rowspan="6">    
      <table width="140" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="40">
            <button name="cmdbuscar" id="cmdbuscar" type="button" class="button blue middle"><span class="label" style="width:100px">Buscar</span></button>
            </td>
          </tr>
        <tr>
          <td>
            <div class="buttons tiptip">
              <button name="cmdpdf" id="cmdpdf" title="Mostrar listado en formato PDF" type="button" class="button red middle"><span class="label" style="width:100px">Imprimir... </span></button>
              </div>
            </td>
          </tr>
      </table>    </td>
  </tr>
  <tr >
    <th height="30">Asunto:</th>
    <td colspan="3"><input name="txtasunto" type="text" class="input"  id="txtasunto" style="width:445px"  value="<c:out value='${asunto}'/>" ></td>
  </tr>
  <tr >
    <th height="30">Destinatario: </th>
    <td colspan="3"><input onBlur="resetPersona()" name="txtpersonaDestino" type="text" class="input"  id="txtpersonaDestino"  value="<c:out value='${personaDestino}'/>" style="width:440px; padding-left:5px"></td>
  </tr>
  <tr >
    <th  width="168" height="30">      Estatus:</th>
    <td colspan="3"><input type="checkbox" name="chkstatus" id="status"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>
    >&nbsp;Nuevo <input type="checkbox" name="chkstatus" id="chkstatus"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>>&nbsp;Pendiente
    <input type="checkbox" name="chkstatus" id="chkstatus"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>>&nbsp;Enviado
    <input type="checkbox" name="chkstatus" id="chkstatus"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>>&nbsp;Revisado
    <input type="checkbox" name="chkstatus" id="chkstatus"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>>&nbsp;En proceso
    <input type="checkbox" name="chkstatus" id="stachkstatustus"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>>&nbsp;Cancelado
    <input type="checkbox" name="chkstatus" id="chkstatus"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>>&nbsp;Concluido
    </td>
  </tr>
  <tr >
    <th height="30" >Prioridad:    
      <td colspan="3"><span class="styled-select">
        <select name="cboprioridad" class="comboBox" id="cboprioridad" style="width:120px;">
      		<option value="0">[Seleccione]</option>
                <c:forEach items="${prioridadCorrespondencia}" var="item" varStatus="status">
                  <option value='<c:out value="${item.ID}"/>' 
                    <c:if test='${item.ID_CAT_PRIORIDAD==idPrioridadCorrespondencia}'> selected </c:if>
                    >
                    <c:out value='${item.DESCRIPCION}'/>
                    </option>
                </c:forEach>
    	</select>
      </span></td>
  </tr>
  <tr >
    <th height="30" >Por fecha desde:
      <td width="204"><input name="txtfechaInicial" type="text" class="input"  id="txtfechaInicial"  value="<c:out value='${fechaInicial}'/>" ></td>
    <td width="91"  align="right">&nbsp;<strong>Hasta:</strong> &nbsp;</td>
    <td width="1016"><input name="txtfechaFinal" type="text" class="input"  id="txtfechaFinal"  value="<c:out value='${fechaFinal}'/>"></td>
  </tr>
  <tr >
    <th height="30" >Número:
      <td><input name="txtnumero" type="text" class="input"  id="txtnumero"  value="<c:out value='${numero}'/>" ></td>
    <td align="right">&nbsp;</td>
    <td>&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <td height="25" >&nbsp; </td>
    <td height="25" colspan="4" >&nbsp;</td>
    </tr>  
</table>
<br />
<table width="95%" border="0" cellpadding="0" cellspacing="0" class="listas" align="center" id="listaDocumentos">
 <thead>
  <tr>
    <th width="3%" height="20"></th>
    <th width="9%">Número</th>
    <th width="26%">De</th>
    <th width="26%">Para</th>
    <th width="24%">Asunto</th>
    <th width="8%">Status</th>
    <th width="4%">Opciones</th>
  </tr>
   </thead>   
<tbody>  
<c:set var="cont" value="${0}" /> 
<tr id='f<c:out value="${cont}"/>' onMouseOver="color_over('f<c:out value="${cont}"/>')" onMouseOut="color_out('f<c:out value="${cont}"/>')">
<c:forEach items="${correspondencia}" var="item" varStatus="status"> 
    <tr>
      <td height="25" align="center">
    <c:if test='${item.STATUS==1}'>
    	<input type="checkbox" id="chkMinutario" name="chkMinutario" value="<c:out value='${item.ID_CORRESPONDENCIA}'/>"/>
    </c:if>
    </td>
    <td height="25" align="center"><c:out value='${item.NUMERO}'/></td>
    <td align="center"><c:out value='${item.PERSONA_FUENTE}'/><br><strong><c:out value='${item.DEPENDENCIA_FUENTE}'/></strong></td>
    <td align="center"><c:out value='${item.PERSONA_DESTINO}'/><br><strong><c:out value='${item.DEPENDENCIA_DESTINO}'/></strong></td>
    <td align="left"><c:out value='${item.ASUNTO}'/></td>
    <td align="center"><c:out value='${item.STATUS_CORRESPONDENCIA}'/></td>
    <td align="center"><c:if test='${item.ID_CAT_STATUS==0}'><img src="../../imagenes/page_white_edit.png" title="Editar / Abrir" style="cursor:pointer" onClick="editarDocumento(<c:out value='${item.ID_CORRESPONDENCIA}'/>)"></c:if>
      <c:if test='${item.STATUS>0}'><img src="../../imagenes/page_gray_edit.png" style="cursor:pointer"></c:if>
      <c:if test='${item.STATUS!=6}'>
        &nbsp;<img style="cursor:pointer" src="../../imagenes/cross.png" title="Cancelar Documento" border="0" width="16" height="16" onClick="cancelarMinutario(<c:out value='${item.ID_MINUTARIO}'/>)"> 
        </c:if>
      <c:if test='${item.STATUS==6}'>
        &nbsp;<img src="../../imagenes/cross2.png" title="Cancelar Documento" border="0" width="16" height="16"> 
        </c:if>
    </td>
  </tr>
  <c:set var="cont" value="${cont+1}"/> 
  </c:forEach>
  </tbody>  
</table>
<table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td height="35" align="left">
    <table width="140" border="0" cellpadding="0" cellspacing="0">
      <tr>
      <sec:authorize ifAllGranted="ROLE_Almacen_PRIVILEGIOS_APERTURAR_ENTRADAS">
        <td width="140" align="center"><div class="buttons tiptip">
              <div class="buttons tiptip">
                <button name="cmdAperturar" id="cmdAperturar" type="button" class="button red middle"><span class="label" style="width:100px">Aperturar</span></button>
              </div></div>
        </td>
        </sec:authorize>
        
      </tr>
    </table>
    </td>
  </tr>
</table>
</form>
</body>
</html>
