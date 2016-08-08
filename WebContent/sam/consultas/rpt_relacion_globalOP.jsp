<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Relacion General de Ordenes de pago - <c:out value='${TIPO_REL}'/></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
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
<form id="frm" action="rpt_relacion_globalOP.action">
<h1> Listado de Relaciones - <c:out value='${TIPO_REL}'/></h1>
<table class="" border="0" align="center" cellpadding="1" cellspacing="2" width="95%">
  <tr>
    <th height="21" colspan="7" align="left">Mes: <strong>
      <select name="cbomes" id="cbomes" style="width:110px" class="comboBox" onchange="frm.submit();">
       <option value="0" 
        <c:if test='${mes==0}'>selected</c:if>
        >[Todos]
        </option>
        <option value="1" 
        <c:if test='${mes==1}'>selected</c:if>
        >Enero
        </option>
        <option value="2" 
        <c:if test='${mes==2}'>selected</c:if>
        >Febrero
        </option>
        <option value="3" 
        <c:if test='${mes==3}'>selected</c:if>
        >Marzo
        </option>
        <option value="4" 
        <c:if test='${mes==4}'>selected</c:if>
        >Abril
        </option>
        <option value="5" 
        <c:if test='${mes==5}'>selected</c:if>
        >Mayo
        </option>
        <option value="6" 
        <c:if test='${mes==6}'>selected</c:if>
        >Junio
        </option>
        <option value="7" 
        <c:if test='${mes==7}'>selected</c:if>
        >Julio
        </option>
        <option value="8" 
        <c:if test='${mes==8}'>selected</c:if>
        >Agosto
        </option>
        <option value="9" 
        <c:if test='${mes==9}'>selected</c:if>
        >Septiembre
        </option>
        <option value="10" 
        <c:if test='${mes==10}'>selected</c:if>
        >Octubre
        </option>
        <option value="11" 
        <c:if test='${mes==11}'>selected</c:if>
        >Noviembre
        </option>
        <option value="12" 
        <c:if test='${mes==12}'>selected</c:if>
        >Diciembre
        </option>
      </select>
    </strong><input type="hidden" name="tipo" id="tipo" value="<c:out value='${TIPO_REL}'/>" /></th>
  </tr>
  <tr bgcolor="#3333FF">
    <th height="21" align="center"><strong style="color:#FFF">Num. Doc.</strong></th>
    <th width="29%" height="21" align="center"><strong style="color:#FFF">Beneficiario</strong></th>
    <th height="21" align="center"><strong style="color:#FFF">Unidad Adm.</strong></th>
    <th align="center"><strong style="color:#FFF">Importe</strong></th>
    <th height="21" align="center"><strong style="color:#FFF">Fecha Relación</strong></th>
    <th align="center"><strong style="color:#FFF">No. Folio</strong></th>
    <th align="center"><strong style="color:#FFF">Cons.</strong></th>
  </tr>
  <c:set var="total" value="${0.0}" />
  <c:set var="cont" value="${0}" />
  <c:set var="folio_ant" value="${0}" />
  <c:set var="color" value="${'#BFDFDF'}" />
  <c:forEach items="${dat}" var="item" varStatus="status"> 
        <tr bgcolor="#BFDFDF">
          <c:set var="total" value="${total+item.IMPORTE}" />
          <c:set var="cont" value="${cont+1}" />
            <td width="7%" height="20" align="center"><strong><c:out value='${item.NUM_OP}'/></strong></td>
            <td height="20" align="left"><c:out value='${item.NCOMERCIA}'/></td>
            <td width="32%" height="20" align="left"><c:out value='${item.UNIDADADM}'/></td>
            <td width="12%" height="20" align="right">$<fmt:formatNumber value='${item.IMPORTE}' pattern="###,###,###.00"/></td>
            <td width="9%" height="20" align="center"><fmt:formatDate pattern="dd/MM/yyyy" value="${item.FECHA3}"/></td>
            <td width="8%" height="20" align="center"><strong><c:out value='${item.FOLIO}'/></strong></td>
            <td width="3%" height="20" align="center"><c:out value='${cont}'/></td>
      </tr>
      </c:forEach>
        <tr bgcolor="">
          <td height="20" colspan="3" align="right"><strong>Total General:</strong></td>
          <td height="20" align="right"><strong>$<fmt:formatNumber value='${total}' pattern="###,###,###.00"/></strong></td>
          <td height="20" colspan="3" align="center">&nbsp;</td>
        </tr>
</table>
</option>
</body>
</html>