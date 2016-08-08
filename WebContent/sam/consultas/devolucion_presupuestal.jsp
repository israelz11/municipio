<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de Grupos</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link type="text/css" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">	
<link href="../../include/js/autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/ControladorDevolucionPresupuestalRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="devolucion_presupuestal.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="../../include/js/otros/productos.js"></script>
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
<!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<form name="forma" id="forma" method="get" action="" onSubmit=" return false" >
<input type="hidden" name="idDevolucion" id="idDevolucion" value="0" />
<div id="tabuladores">
  <ul>
   	<li><a href="#fragment-general"><span>Información general</span></a></li>
   	<li><a href="#fragment-conceptos"><span>Conceptos</span></a></li>
  </ul>
  <div id="fragment-general" align="left">
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="formaCaptura">
    <tr>
      <th height="30">&nbsp;</th>
      <td width="80%">Nota: (*) Información requerida para continuar.</td>
    </tr>
    <tr>
      <th height="30">Num. Devolución:   <input type="hidden" id="ID_DEVOLUCION" value="<c:out value='${idDevolucion}'/>"/></th>
      <td><div align="left" id="div_devolucion"><strong><c:out value='${num_devolucion}'/></strong></div></td>
    </tr>
    <tr>
      <th height="30">Unidad Administrativa:</th>
      <td>
      <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      <c:out value="${nombreUnidad}"/><input type="hidden" name="cbounidadx" id="cbounidadx" value='<c:out value="${unidad}"/>' />
      </sec:authorize>
       <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
       <div class="styled-select">
        <select name="cbounidadx" class="comboBox" id="cbounidadx" style="width:450px">
            <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">                  
              <option value="<c:out value="${item.ID}"/>" <c:if test="${item.ID==(devolucion.ID_DEPENDENCIA!=null ? devolucion.ID_DEPENDENCIA:unidad)}"> selected </c:if> ><c:out value="${item.DEPENDENCIA}"/></option>
            </c:forEach>
          </select>
         </div>
       </sec:authorize>
      </td>
    </tr>
    <tr>
      <th height="30">*Tipo de gasto:</th>
      <td>
      <div class="styled-select">
      <select name="tipoGasto" class="comboBox" id="tipoGasto" style="width:450px">
        <option value="0">[Tipo de gasto]</option>
        <c:forEach items="${tipodeGasto}" var="item" varStatus="status">
          <option value='<c:out value="${item.ID}"/>'
            <c:if test='${item.ID==devolucion.ID_RECURSO}'> selected </c:if>
            >
            <c:out value='${item.RECURSO}'/>
            </option>
        </c:forEach>
      </select>
      </div>
      </td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*Periodo:</span></th>
      <td>
      <div class="styled-select">
      <select name="cbomes" class="comboBox" id="cbomes" style="width:150px">
       <option value="1" <c:if test='${1==devolucion.PERIODO}'> selected </c:if>>
          Enero</option>
          <option value="2" <c:if test='${2==devolucion.PERIODO}'> selected </c:if>>
          Febrero</option>
          <option value="3" <c:if test='${3==devolucion.PERIODO}'> selected </c:if>>
          Marzo</option>
          <option value="4" <c:if test='${4==devolucion.PERIODO}'> selected </c:if>>
          Abril</option>
          <option value="5" <c:if test='${5==devolucion.PERIODO}'> selected </c:if>>
          Mayo</option>
          <option value="6" <c:if test='${6==devolucion.PERIODO}'> selected </c:if>>
          Junio</option>
          <option value="7" <c:if test='${7==devolucion.PERIODO}'> selected </c:if>>
          Julio</option>
          <option value="8" <c:if test='${8==devolucion.PERIODO}'> selected </c:if>>
          Agosto</option>
          <option value="9" <c:if test='${9==devolucion.PERIODO}'> selected </c:if>>
          Septiembre</option>
          <option value="10" <c:if test='${10==devolucion.PERIODO}'> selected </c:if>>
          Octubre</option>
          <option value="11" <c:if test='${11==devolucion.PERIODO}'> selected </c:if>>
          Noviembre</option>
          <option value="12" <c:if test='${12==devolucion.PERIODO}'> selected </c:if>>
          Diciembre</option>
      </select>
      </div>
      </td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*Fecha:</span></th>
      <td><input name="txtfecha" type="text" class="input" id="txtfecha" value="<c:out value='${devolucion.FECHA_DESC}'/>" style="width:148px" maxlength="10"/></td>
    </tr>
    <tr>
      <th width="20%" height="30"><span class="Texto_Forma">*Concepto:</span></th>
      <td><input name="txtconcepto" type="text" class="input" id="txtconcepto" value="<c:out value='${devolucion.CONCEPTO}'/>" style="width:446px"  maxlength="100" onBlur="upperCase(this)" /></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*Descripción:</span></th>
      <td><textarea  name="txtdescripcion"  id="txtdescripcion" onBlur="upperCase(this)"  cols="80" rows="4" wrap="virtual" class="textarea" maxlength="500" style="width:445px"><c:out value='${devolucion.DESCRIPCION}'/></textarea></td>
    </tr>
    <tr>
      <th height="21">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th height="30">&nbsp;</th>
      <td><span style="padding:3px">
        <input disabled  name="cmdcerrar" type="button" class="botones" id="cmdcerrar" value="Cerrar" style="width:150px" />
        <input  name="cmdguardar" type="button" class="botones" id="cmdguardar"   value="Guardar" style="width:150px" />
      </span></td>
    </tr>
    <tr>
      <th height="30">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    </table>
</div>  
<div id="fragment-conceptos" align="left">    
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="formaCaptura">
    <tr>
      <td height="20" colspan="4">&nbsp;</td>
    </tr>
    <tr>
      <th width="24%" height="30"><span class="Texto_Forma">Presupuesto de:</span></th>
      <td colspan="3">
        <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
          <c:out value="${nombreUnidad}"/><input type="hidden" name="cbounidad2" id="cbounidad2" value='<c:out value="${unidad}"/>' />
          </sec:authorize>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
        <div class="styled-select">
          <select name="cbounidad2" class="comboBox" id="cbounidad2" onChange="" style="width:450px">
            <option value="0">[Seleccione]</option>
            <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> <option value="<c:out value='${item.ID}'/>" 
          <c:if test="${item.ID==idUnidad}"> selected </c:if>
              >
              <c:out value="${item.DEPENDENCIA}"/>
              </option>
              </c:forEach>
            </select>
            </div>
          </sec:authorize>
        </td>
    </tr>
    <tr>
      <th height="30">Orden de Pago:</th>
      <td>
        <input name="txtcveop" type="text" class="input" id="txtcveop"  value='' size="20" maxlength="6"><img src="../../imagenes/buscar.png" alt="Mostrar Ordenes de Pago" name="img_OrdenPago" width="22" height="22" id="img_OrdenPago" style="cursor:pointer" align="absmiddle" /><input type="hidden" id="CVE_OP" value="0"/></td>
      <td align="left"></td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">Programa:</span></th>
      <td width="13%"><input name="txtproyecto" type="text" class="input" id="txtproyecto"  value='' size="20" maxlength="6">
        <input type="hidden" id="ID_PROYECTO" value="0"/></td>
      <td width="8%" align="right"><strong>Partida:&nbsp;</strong></td>
      <td width="55%"><input name="txtpartida" type="text" class="input" id="txtpartida"  value='' size="20" maxlength="4"  >
        <img src="../../imagenes/buscar.png" alt="Mostrar presupuesto" name="img_presupuesto" width="22" height="22" id="img_presupuesto" style="cursor:pointer" align="absmiddle" /></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">Presupuesto:</span></th>
      <td><input name="txtpresupuesto" type="text" class="input" id="txtpresupuesto"  value="" size="20" readonly></td>
      <td align="right"><strong>Disponible:&nbsp;</strong></td>
      <td><input name="txtdisponible" type="text" class="input" id="txtdisponible"  value="" size="20" readonly></td>
      </tr>
    <tr>
      <th height="30">Creditos y Retenciones:</th>
      <td colspan="3">
      <div class="styled-select">
      <select name="cbotipo" class="comboBox" id="cbotipo" style="width:450px">
      		<option value="0">[Ninguno]</option>
        	<c:forEach items="${retenciones}" var="item" varStatus="status">                  
              <option value="<c:out value="${item.CLV_RETENC}"/>"><c:out value="${item.RETENCION}"/></option>
            </c:forEach>
      </select>
      </div>
      </td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">Descripción:</span></th>
      <td colspan="3"><textarea  name="txtdetalle"  id="txtdetalle"  cols="80" rows="4" wrap="virtual" class="textarea"  maxlength="500" style="width:445px"></textarea></td>
      </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*Importe:</span></th>
      <td colspan="3"><input name="txtimporte" type="text" class="input" id="txtimporte" onkeypress="return keyNumbero(event); ">
        <input type="hidden" id="ID_DETALLE" value="0"/>
        <input type="hidden" id="IMPORTE_TOTAL" value="0"/>
        <input  name="cmdagregar" type="button" class="botones" id="cmdagregar"   value="Agregar" style="width:100px" />
        <input  name="cmdnuevo" type="button" class="botones" id="cmdnuevo"   value="Nuevo" style="width:100px" /></td>
      </tr>
    <tr>
      <th height="30">&nbsp;</th>
      <td colspan="3">&nbsp;</td>
    </tr>
    <tr>
      <td height="30" colspan="4"><table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listas" id="listaDetalles">
        <thead>
          <tr >
            <th width="4%" height="20"  align="center"><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminarDetalle()" style='cursor: pointer;'></th>
            <th width="24%"  align="center">Unidad Administrativa</th>
            <th width="8%"  align="center">Orden Pago</th>
            <th width="27%"  align="center">Notas</th>
            <th width="12%"  align="center">Proyecto</th>
            <th width="10%" align="center">Partida</th>
            <th width="12%"  align="center">Monto</th>
            <th width="3%"  align="center">&nbsp;</th>
            </tr>
          </thead>
        <tbody>
          </tbody>
        </table></td>
    </tr>
    </table>
</div>

</div>
<div align="center" style="padding:3px"></div>
</form>

</body>
</html>
