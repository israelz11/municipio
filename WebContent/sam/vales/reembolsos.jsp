<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Muestra presupuesto</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/css/sweetalert2.min.css" type="text/css"/>
<link href="../../include/js/autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../dwr/interface/controladorReembolsosLiquidosValesRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script>  
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js?x=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="reembolsos.js?x=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<link rel="stylesheet" href="../../include/css/sweetalert2.min.css" type="text/css"/>

<script type="text/javascript" src="../../include/js/sweetalert2.min.js"></script>
<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
<!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->
</head>
<body  >
<table width="95%" align="center"><tr><td><h1>Vales - Captura de Reembolso Liquido</h1></td></tr></table>
<form name="forma" action="" type="POST">
  <table width="95%" border="0"  align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <th height="17">&nbsp;</th>
      <td height="17">&nbsp;</td>
    </tr>
    <tr>
      <th height="30"><input type="hidden" name="importeAnte" id="importeAnte" />
        <input type="hidden" name="cve_proyecto" id="cve_proyecto" />
        <input type="hidden" name="cve_partida" id="cve_partida" />
      <input type="hidden" name="cve_val"  id="cve_val"value=''>
      *Unidad Administrativa :</th>
      <td height="30">
        <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
          <c:out value="${nombreUnidad}"/><input type="hidden" name="unidad" id="unidad" value='<c:out value="${idUnidad}"/>' />
        </sec:authorize>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
          <select name="unidad" class="comboBox" id="unidad" style="width:445px">
            <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">                  
              <option value='<c:out value="${item.ID}"/>' <c:if test="${item.ID==idUnidad}"> selected </c:if>><c:out value="${item.DEPENDENCIA}"/></option>
            </c:forEach>
          </select>
      </sec:authorize></td>
    </tr>
    <tr>
      <td colspan="2" align="center"></td>
    </tr>
    <tr>
      <td colspan="2" ></td>
    </tr>
    <tr >
      <th width="16%" height="30"  >*Fecha de reembolso:</th>
      <td width="84%"  ><input name="fecha" type="text" class="input" id="fecha" style="width:100px"  value='<c:out value="${fechaActual}"/>'  maxlength="15" readonly="readonly">
      </td>
    </tr>
    <tr >
      <th height="30" >*Vale:</th>
      <td><input name="noVale" type="text" class="input" id="noVale" style="width:100px" value='' readonly="readonly" />
      <img id="img_presupuesto" src="../../imagenes/buscar.png" width="22" height="22" alt="Mostrar presupuesto" style="cursor:pointer" onclick="getValesDisponibles()" align="absmiddle" /></td>
    </tr>
    <tr id="flla_proyecto" >
      <th height="30" >*Programa/Partida:</th>
      <td><select name="cbovales" class="comboBox" id="cbovales" style="width:222px">
      </select></td>
    </tr>
    <tr id="fila_comprobado">
      <th height="30" >Comprobado Programa/Partida:</th>
      <td><div id="comprobado"></div></td>
    </tr>
    <tr id="fila_restante">
      <th height="30" >Restante Programa/Partida:</th>
      <td><div id="restante"></div></td>
    </tr>
    <tr id="flla_importe" >
      <th height="30" >Importe total Vale :</th>
      <td><div id="importeVale"></div></td>
    </tr>
    <tr id="flla_descuento">
      <th height="30" >Descontado total:</th>
      <td><div id="descontado"></div></td>
    </tr>
    <tr >
      <th height="30" >*Importe:</th>
      <td>
        <input name="importe" type="text" class="input" id="importe" onkeypress=" return keyNumbero( event );" style="width:100px"  value='' maxlength="20">
      </td>
    </tr>
    <tr >
      <th height="30"  >Fecha de deposito:</th>
      <td ><input name="fechaDeposito" type="text" class="input" id="fechaDeposito" style="width:100px" value='<c:out value="${fechaActual}"/>' size="15" maxlength="15" readonly="readonly"/></td>
    </tr>
    <tr >
      <th height="30"  >&nbsp;</th>
      <td ><input name="xGrabar" type="button" class="botones" onclick=" guardarRembolso();" value="Guardar reembolso" style="width:150px"/>
      <input name="xGrabar2" type="button" class="botones"   onclick="limpiar();" value="Limpiar" style="width:150px"/></td>
    </tr>
    
    <tr >
      <td height="14" colspan="2" align="center">&nbsp;</td>
    </tr>
  </table><br />
  <table width="95%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas"  id="detallesReembolsos" >
    <thead>
      <tr >
        <th width="1%" ><img src="../../imagenes/cross.png" width="16" height="16" onClick="eliminarReembolso()" style='cursor: pointer;'></th>
        <th width="20%" height="20"  align="center" >Tipo</th>
        <th width="20%"  >OP</th>
        <th width="11%"  >Programa</th>
        <th width="12%"  >Partida</th>
        <th width="11%"  >Fecha</th>
        <th width="11%"  >Fecha deposito</th>
        <th width="14%"  >Importe descontado</th>        
      </tr>
    </thead>
    <tbody>
    </tbody>
  </table>
  </form>
</body>
</html>

