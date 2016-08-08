<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de Grupos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script>  
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorAutorizacionRemoto.js"> </script>
<script type="text/javascript" src="autorizacion.js"></script>
</head>
<body>
  <table width="95%" align="center"><tr><td><h1>Salidas - Relación de pedidos para autorizar</h1></td></tr></table>  
<form ACTION="autorizacion.jsp" METHOD="POST" name="forma" id="forma" >
  <input name="idSalida" type="hidden" id="idSalida" value="">
  <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="captura" >
<tr >
  <th width="12%" height="30" >Almacen</th>
  <td width="88%"><c:if test="${noAlmacenes==1}">  
  <c:forEach items="${almacenes}" var="item" varStatus="status">
  <input name="almacen" type="hidden" id="almacen" value='<c:out value="${item.ID_ALMACEN}"/>'> 
  <c:out value="${item.ALMACEN}"/>
  </c:forEach>
  </c:if> 
  <c:if test="${noAlmacenes > 1}">  
  <select name="almacen" id="almacen"  class="comboBox"  onChange="getSolicitudesPendientes()" style="width:445px;">
           <c:forEach items="${almacenes}" var="item" varStatus="status">
           <option value="<c:out value='${item.ID_ALMACEN}'/>" ><c:out value="${item.ALMACEN}"/>
           </option>
        </c:forEach>
        </select>
  </c:if> 
  </td>
</tr>
   <tr >
	<td height="40" colspan="2" align="center" >
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listas" id="listaPendientes" >
	<thead>
    <tr>
      <th width="3%" height="20"><img src="../../imagenes/cross.png" alt="" width="16" height="16" style='cursor: pointer;' onClick="eliminar()"></th>
      <th width="5%">Folio</th>
      <th width="15%">Partida</th>
      <th width="28%">Departamento</th>
      <th width="40%">Solicitante</th>
      <th width="7%">Fecha </th>
      <th width="2%">&nbsp;</th>
    </tr>    
    </thead>
  </table>
      </td>
    </tr>
  </table>
  <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="detallesAutorizacion" >
<tr >
        <th>Folio :</th>
        <td width="52%" id="colFolio">&nbsp;</td>
        <th width="14%">Fecha :</th>
        <td width="17%" id="colFecha">&nbsp;</td>
    </tr>
      <tr >
        <th>Responsable :</th>
        <td colspan="3" id="colResponsable">&nbsp;</td>
      </tr>
      <tr >
        <th width="17%">Departamento :</th>
        <td colspan="3" id="colDepto">&nbsp;</td>
      </tr>
       <tr >
        <th>Concepto</th>
        <td colspan="3" id="colConcepto">&nbsp;</td>
      </tr>
      <tr >
        <td colspan="4"><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listas" id="detallesSolicitud" >
          <thead>
            <tr >
              <th width="2%" height="20">No.</th>
              <th width="5%">Clave</th>
              <th width="36%">Nombre</th>
              <th width="6%">Unidad</th>
              <th width="8%">Sumistrado</th>
              <th width="7%">Pendiente </th>
              <th width="6%">No. Pedidos </th>
              <th width="8%">Sin entregar</th>
              <th width="8%">Existencia</th>
              <th width="7%">Solicitado</th>
              <th width="7%">Cantidad</th>
            </tr>
          </thead>
          <!--input name="idArticulo" type="text" class="input" onBlur="validaarti(this,'EXISTENCIA-autorizados');" value="CANT_SURT" size="7" maxlength="5"></td-->
        </table></td>
      </tr>
      
      <tr >
        <td colspan="4" align="center">
          <table width="100%"  border="0" cellspacing="1" cellpadding="0">
            <tr>
              <td align="center"><input name="sol" type="button" class="botones" id="sol" onClick="guardar('CANCELADO')" value="Cancelar"></td>
              <td align="center"><input name="sol3" type="button" class="botones" id="sol3" onClick="guardar('ENVIADO')" value="Guardar "></td>
              <td align="center"><input name="sol2" type="button" class="botones" id="sol2" onClick="guardar('AUTORIZADO')" value="Autorizar"></td>
              <td align="center"><input name="sol2" type="button" class="botones" id="sol2" onClick="regresarSol()" value="Regresar" ></td>
            </tr>
        </table></td>
      </tr>
  </table>
</FORM>
</body>
</html>
