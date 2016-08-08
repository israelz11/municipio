<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
<script type="text/javascript" src="../../dwr/interface/controladorConsultasSalidasRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorEntregaRemoto.js"> </script>
<script type="text/javascript" src="salidas.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
</head>
<body>
  <table width="95%" align="center"><tr><td><h1>Consulta de salidas</h1></td></tr></table>  
  <form ACTION="sad_consul_pedidos.jsp" METHOD="POST" name="forma" id="forma" >  
   <sec:authorize ifAllGranted="ROLE_Almacen_Consultas_Ver_por_almacen">
        <input type="hidden" name="tipo" id="tipo" value="almacen">
      </sec:authorize>
   <sec:authorize ifAllGranted="ROLE_Almacen_Consultas_Cancelar_Salida">
        <input type="hidden" name="permisoCancelar" id="permisoCancelar" value="SI">
      </sec:authorize>
      
      

  <input name="idSalida" type="hidden" id="idSalida" value="">
<table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="formulario"  id="formaConsulta">
<tr>
        <th height="30">Almacen :</th>
        <td><c:if test="${noAlmacenes==1}">  
  <c:forEach items="${almacenes}" var="item" varStatus="status">
  <input name="almacen" type="hidden" id="almacen" value='<c:out value="${item.ID_ALMACEN}"/>'> 
  <c:out value="${item.ALMACEN}"/>
  </c:forEach>
  </c:if> 
  <c:if test="${noAlmacenes > 1}">  
  <div class="styled-select">
  <select name="almacen" id="almacen"  class="comboBox"  onChange="getSolicitudesPendientes()" style="width:445px;">
           <c:forEach items="${almacenes}" var="item" varStatus="status">
           <option value="<c:out value='${item.ID_ALMACEN}'/>" ><c:out value="${item.ALMACEN}"/>
           </option>
        </c:forEach>
        </select>
  </div>
  </c:if></td>
      </tr>
      <tr>
        <th width="13%" height="30">Folio :</th>
        <td width="87%"><input name="folio" type="text" class="input"  id="folio" onBlur="vNumerico(this);" size="6" maxlength="10" style="width:110px"></td>
      </tr>
      <tr >
        <th height="30">A&ntilde;o :</th>
        <td>
        <div class="styled-select">
        <select name="year" class="comboBox" id="year" style="width:110px">
          <option value="">[Seleccione]</option>
          <option value="0">Todos</option>
          <c:forEach items="${ejercicios}" var="item" varStatus="status">
           <option value="<c:out value='${item.YEAR}'/>" ><c:out value="${item.YEAR}"/>
           </option>
        </c:forEach>
        </select>
        </div>
        </td>
      </tr>
      <tr >
        <th height="30"><p>Estatus :</p>
        </th>
        <td>
        <div class="styled-select">
        <select name="status" class="comboBox"  id="status" style="width:110px">
            <option value="">[Seleccione]</option>
            <option value="TODOS">Todos</option>
            <c:forEach items="${estatus}" var="item" varStatus="status">
           <option value="<c:out value='${item.ESTATUS}'/>" ><c:out value="${item.ESTATUS}"/>
           </option>
        </c:forEach>
        </select>
        </div>
        </td>
      </tr>
      <tr >
        <th height="48">&nbsp;</th>
        <td> <div class="buttons tiptip">
                <button name="cmdbuscar" id="cmdbuscar" onClick="buscar()" type="button" class="button red middle"><span class="label" style="width:100px">Buscar</span></button>
              </div></td>
    </tr>
      <tr >
        <td colspan="2" align="center">
         
        </td>
      </tr>
</table>
	    <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listas" id="listasSolicitudes" >
        <thead>
      <tr >
        <th width="7%" height="20">No.</th>
        <th width="5%">Folio</th>
        <th width="24%">Departamento</th>
        <th width="37%">Responsable</th>
        <th width="10%">Estatus</th>
        <th width="10%">Fecha / solicitud </th>
        <th width="14%">Fecha / entrega </th>
        <th width="3%">&nbsp;</th>
      </tr>
      </thead>
    </table>	    
	<table width="95%"  border="0" align="center" cellpadding="0" cellspacing="1" class="formulario" id="detallesSalida" >
      <tr >
        <td colspan="8">
          <table width="100%"  border="0" cellpadding="0" cellspacing="1" >
          <tr >
              <th width="18%">Folio  : </th>
              <td width="82%" height="30" id="ffolio">&nbsp;</td>
            </tr>
            <tr >
              <th width="18%">Partida  : </th>
              <td width="82%" height="30" id="fpartida">&nbsp;</td>
            </tr>
            <tr >
              <th>Departamento :</th>
              <td id="fdepto" height="30">&nbsp;</td>
            </tr>
            <tr >
              <th>Usuario :</th>
              <td id="fusuario" height="30">&nbsp;</td>
            </tr>
            <tr >
              <th>Fecha de envio : </th>
              <td id="fenvio" height="30">&nbsp;</td>
            </tr>
            <tr >
              <th>Fecha de entrega :</th>
              <td id="fentrega" height="30">&nbsp;</td>
            </tr>
            <tr >
              <th>Estatus :</th>
              <td id="festatus" height="30">&nbsp;</td>
            </tr>
          </table></td>
      </tr>
      <tr >
        <td colspan="8" height="20"><strong>Detalles del pedido </strong></td>
      </tr>
      <tr>
      <td  colspan="8"><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listas" id="detalleslistasSolicitudes" >
        <thead>
          <tr >
            <th width="2%" height="20">No.</th>
            <th width="5%">Clave</th>
            <th width="36%">Nombre</th>
            <th width="6%">Unidad</th>
            <th width="7%">Solicitado</th>
            <th width="7%">Surtido</th>
          </tr>
        </thead>
      </table></td>
      </tr>
      <tr >
        <td height="40" colspan="8" align="center"><table width="80%" border="0" cellspacing="0" cellpadding="0">
          <tr>
           <sec:authorize ifAllGranted="ROLE_Almacen_Consultas_Ver_por_almacen">      
            <td align="center">
            <div class="buttons tiptip">
                <button name="sol3" id="sol3" onClick="imprimirSolicitud()" type="button" class="button red middle"><span class="label" style="width:200px">Imprimir Comprobante</span></button>
              </div>
            </td>
            </sec:authorize>
            <sec:authorize ifAllGranted="ROLE_Almacen_Consultas_Cancelar_por_almacen">      
            <td align="center">
            <div class="buttons tiptip">
                <button name="btcancelar" id="btcancelar" onClick="cancelarSolicitud()" type="button" class="button red middle"><span class="label" style="width:200px">Cancelar Solicitud</span></button>
              </div></td>
            </sec:authorize>
            <td align="center">
            <div class="buttons tiptip">
                <button name="sol2" id="sol2" onClick="regresarSol()" type="button" class="button red middle"><span class="label" style="width:200px">Nueva consulta</span></button>
              </div>
          </tr>
        </table></td>
      </tr>
    </table>
</FORM>
</body>
</html>
