<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de Grupos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
</head>
<body>
<br />
<table width="95%" align="center"><tr><td><h1>Movimientos de artículos</h1></td></tr></table>    
  <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" >

    <tr >
      <th height="20">Artículo : </th>
      <td><c:out value='${DESCRIPCION}'/></td>
    </tr>
    <tr >
      <th height="20">Unidad de medida :</th>
      <td ><c:out value='${UNIDMEDIDA}'/></td>
    </tr>
    <tr >
      <th width="23%" height="20">Existencia en almacen :</th>
      <td width="77%" ><c:out value='${EXISTENCIA}'/></td>
    </tr>
    <tr >
    <td colspan="2">
      <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="listas" >
      <thead>
    <tr >
      <th width="3%" height="20">No.</th>
      <th width="9%">Movimiento</th>
      <th width="48%">Solicitante / Proveedor</th>
      <th width="5%">Folio</th>
      <th width="10%">Fecha</th>
      <th width="12%">Precio</th>
      <th width="12%">Cantidad</th>
      <th width="12%">Saldo</th>
    </tr>
    </thead>
    <c:set var="suma" value="${0.0}" /> 
    <c:forEach items="${movimientos}" var="item" varStatus="status"> 
      <c:if test="${item.tipo=='SALIDA'}">  
       <c:set var="suma" value="${suma-item.CANTIDAD}" /> 
    </c:if>
          <c:if test="${item.tipo=='ENTRADA'}">  
       <c:set var="suma" value="${suma+item.CANTIDAD}" /> 
    </c:if>

    <tr >
      <td><c:out value='${status.count}'/></td>
      <td><c:out value='${item.TIPO}'/></td>
      <td><c:out value='${item.NOMBRE}'/></td>
      <td><c:out value='${item.FOLIO}'/></td>
      <td><c:out value='${item.FECHA}'/></td>
      <td><fmt:formatNumber value="${item.PRECIO}"  pattern="$ #,###,###,##0.00" /></td>
      <td><fmt:formatNumber value="${item.CANTIDAD}"  pattern=" #,###,###,##0.00" /></td>
      <td><fmt:formatNumber value="${suma}"  pattern=" #,###,###,##0.00" /></td>
    </tr>
 </c:forEach>    
    </table>
    </td>
    </tr>
</table>
  
</body>
</html>