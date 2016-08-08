<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link rel="stylesheet" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery.form.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script> 
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/ControladorNuevoMinutarioRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>

<script type="text/javascript" src="nuevo_minutario.js"></script>

<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script></script>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
<style type="text/css">
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
</style>
<body >
<form name="forma" id="forma" method="post" enctype="multipart/form-data">
<br />
  <table width="95%" align="center"><tr><td><h1>Minutario - <c:if test="${item.ID_MINUTARIO==0}">Nuevo número</c:if><c:if test="${item.ID_MINUTARIO==0}">Editar número</c:if></h1></td></tr>
  <tr>
  <td>
  
  <div id="tabuladores">
      <ul>
        <li><a href="#fragment-principal">Encabezado</a></li>
        <li><a href="#fragment-detalles">Archivos</a></li>
      </ul>
      
      <div id="fragment-principal" align="left">
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" >
    <tr>
      <td width="12%" class="field_label">&nbsp;</td>
      <td width="88%" align="left">
      <input type="hidden" id="ID_MINUTARIO" name="ID_MINUTARIO"  value="<c:out value='${ID_MINUTARIO}'/>"/></td>
      </tr>
    <tr>
      <th height="30" class="field_label">Consecutivo:</th>
      <td align="left"><div id="div_minutario"><c:out value="${NUMERO}"/></div><input type="hidden" id="NUMERO" value="<c:out value="${NUMERO}"/>"></td>
    </tr>
    <tr>
      <th height="30" class="field_label">*Año:</th>
      <td align="left"><span class="styled-select">
        <select name="cboaño" class="comboBox" id="cboaño" style="width:110px">
           <option value="0">[Seleccione]</option>
           <c:forEach items="${periodos}" var="item" varStatus="status"> <option value='<c:out value="${item.ID_PERIODO}"/>' 
            <c:if test='${item.ID_PERIODO==year}'> selected </c:if>
            >
            <c:out value='${item.DESCRIPCION}'/>
            </option>
          </c:forEach>
        </select>
      </span></td>
    </tr>
    <tr>
      <th height="30" class="field_label">Unidad fuente.</th>
      <td align="left">     <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      		<c:out value="${nombreUnidad}"/><input type="hidden" name="cbodependencia" id="cbodependencia" value='<c:out value="${idUnidad}"/>' />
      </sec:authorize>
      <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
          <span class="styled-select">
            <select name="cbodependencia" class="comboBox" id="cbodependencia" style="width:450px">
            <option value="0">[Seleccione]</option>
              <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> <option value='<c:out value="${item.ID}"/>' 
                
                <c:if test="${item.ID==idUnidad}"> selected </c:if>
                >
                <c:out value="${item.DEPENDENCIA}"/>
                </option>
              </c:forEach>
            </select>
          </span>
      </sec:authorize></td>
    </tr>
    <tr>
      <th height="30" class="field_label">*Minutario:</th>
      <td align="left">
      <sec:authorize ifNotGranted="ROLE_Almacen_PRIVILEGIOS_VER_UNIDADES_ADMINISTRATIVAS">
      	<c:out value="${nombreUnidad}"/>
        <input type="hidden" id="cbodependencia" name="cbodependencia" value="<c:out value='${cbodependencia}'/>">
     </sec:authorize>
     <sec:authorize ifAllGranted="ROLE_Almacen_PRIVILEGIOS_VER_UNIDADES_ADMINISTRATIVAS">
      	<div class="styled-select">
            <select name="cbominutario" id="cbominutario" style="width:450px">
            <option value="0">[Seleccione]</option>
                          ${minutarios}
            </select>
          </div>
     </sec:authorize> 
      </td>
    </tr>
    
    <tr>
      <th height="30" class="field_label">&nbsp;*Unidad Destino:</th>
      <td align="left" valign="middle"><span class="styled-select">
        <select name="cbodestino" id="cbodestino" style="width:450px">
          <option value="0">[Seleccione]</option>
          <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> <option value='<c:out value="${item.ID}"/>' 
                            
            <c:if test='${item.ID==ID_UNIDAD_DESTINO}'> selected </c:if>
            >
            <c:out value='${item.DEPENDENCIA}'/>
            </option>
          </c:forEach>
        </select>
      </span></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;</th>
      <td align="left" valign="middle"><span class="styled-select">
        <select name="cbosubdestino" id="cbosubdestino" style="width:450px">
          
        </select>
      </span></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;Clasificación:</th>
      <td align="left" valign="middle"><span class="styled-select">
        <select name="cboclasifica" class="comboBox" id="cboclasifica" style="width:210px">
          <option value="0">[Seleccione]</option>
          <c:forEach items="${clasificacion}" var="item" varStatus="status"> <option value='<c:out value="${item.ID_CAT_CLASIFICACION_MINUTARIO}"/>' 
            <c:if test='${item.ID_CAT_CLASIFICACION_MINUTARIO==ID_CAT_CLASIFICACION_MINUTARIO}'> selected </c:if>
            >
            <c:out value='${item.DESCRIPCION}'/>
            </option>
          </c:forEach>
        </select>
      </span></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;Documento Relacionado:</th>
      <td align="left" valign="middle"><input name="txtdocumento" type="text" class="input" id="txtdocumento" maxlength="20" style="width:210px" value="<c:out value='${NUMERO_CORRESPONDENCIA}'/>" />
        <input name="ID_CORRESPONDENCIA" type="hidden" id="ID_CORRESPONDENCIA" value="<c:out value='${ID_CORRESPONDENCIA}'/>"/></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;*Asunto:</th>
      <td align="left" ><textarea name="txtasunto" cols="70" rows="4" style="width:450px" class="textarea" id="txtasunto"><c:out value='${ASUNTO}'/></textarea></td>
    </tr>
    <tr>
      <th height="30" class="field_label">&nbsp;C.c.p.:</th>
      <td align="left"><input name="txtccp" type="text" class="input" id="txtccp" style="width:450px" value="" />
        <input name="CVE_PERS" type="hidden" id="CVE_PERS" value="<c:out value='${CCP}'/>"/>
        </td>
    </tr>
    <tr>
      <td class="field_label">&nbsp;</td>
      <td align="left"><table width="0"  border="0" align="left" cellpadding="0" cellspacing="0" class="listas" id="detalles_ccp" >
        <thead>
          <tr >
            <th width="312" height="20">Nombre completo</th>
            <th width="496">Unidad Adm./Subdirección</th>
            <th width="23">&nbsp;</th>
          </tr>
        <thead>
        <tbody>
        </tbody>   
        </table></td>
    </tr>
    <tr>
      <td class="field_label">&nbsp;</td>
      <td align="left">&nbsp;</td>
    </tr>
    <tr>
      <td class="field_label">&nbsp;</td>
      <td align="left">
        <table width="405" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="136" align="center"><div class="buttons tiptip">
              <div class="buttons tiptip">
                <button name="cmdnuevo" id="cmdnuevo" type="button" class="button red middle"><span class="label" style="width:100px">Nuevo</span></button>
              </div>
            </div></td>
            <td width="135" align="center"><div class="buttons tiptip">
              <div class="buttons tiptip">
                <button name="cmdcerrar" id="cmdcerrar" type="button" class="button red middle"><span class="label" style="width:100px">Cerrar</span></button>
              </div>
            </div></td>
            <td width="134"><button name="cmdguardar" id="cmdguardar" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button></td>
          </tr>
        </table></td>
    </tr>
    <tr >
      <td height="25"   colspan="2" class="field_label"  >&nbsp;</td>
      </tr> 
  </table>
      </div>
    <div id="fragment-detalles" align="left">
   		<table width="100%" class="formulario" cellpadding="0" cellspacing="0">
        <tr>
          <th height="17">&nbsp;</th>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
        <tr>
           <th width="3%" height="36">&nbsp;</th>
           <td width="38%">
           		<input type="file" class="input-file" id="archivo" name="archivo" style="width:350px" />
</td>
           <td width="59%"><button name="cmdsubir" id="cmdsubir" type="button" class="button blue middle"><span class="label" style="width:100px">Subir</span></button></td>
        </tr>
        <tr>
     
        </tr>
        <tr><td align="center">&nbsp;</td>
          <td colspan="2" align="left"><table width="0"  border="0" align="left" cellpadding="0" cellspacing="0" class="listas" id="detalle_archivo" >
            <thead>
              <tr >
                <th width="418" height="20">Archivo</th>
                <th width="103">Extensión</th>
                <th width="112">Tamaño</th>
                <th width="33">&nbsp;</th>
              </tr>
            <thead>
            <tbody>
            </tbody>
          </table></td>
        </tr>
        <tr>
          <td colspan="3" align="center">&nbsp;</td>
        </tr>
        </table>
    </div> 

  </div>
  </td>
  </tr>
  </table>
  <br />
</form>
</body>
</html>
