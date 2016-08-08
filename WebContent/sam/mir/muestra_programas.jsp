<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Programas por Dependencias</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script>
<!--


function ObtenerPrograma(programa){
	window.parent.ObtenerProgramaUnidad(programa);
}

-->
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
<table class="listas" border="0" align="center" cellpadding="0" cellspacing="0" width="95%">
  <tr bgcolor="#889FC9">
    <th height="21" align="left">Num.Programa</th>
  </tr>
  <c:forEach items="${programas}" var="item" varStatus="status" >
       <tr>
         <td width="10%" height="20" align="left"><a href="javascript:ObtenerPrograma('${item.N_PROGRAMA}')"><c:out value='${item.N_PROGRAMA}'/></a></td>
        </tr>
  </c:forEach> 
</table>
</body>
</html>