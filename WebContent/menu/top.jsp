<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<link href="../include/css/estilos.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../include/js/menu/dmenu.js"></script>
<script type="text/javascript" language="JavaScript1.2">dmWorkPath = '../include/js/menu/';</script>
<script type="text/javascript" src="../include/js/menu/menu_estilo.js"></script>
<script type="text/javascript" src="../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript">


function cambioMain(menu,mainDesc){
 $("#forma").attr("target","topFrame");
 $("#main").attr('value',menu); 
 $("#mainDesc").attr('value',mainDesc); 
 $("#forma").attr("action","top.action");
 $("#forma").submit();
} 

$(document).ready(function() {
  $("#forma").attr("target","mainFrame");
  $("#forma").attr("action","main.action");
  $("#forma").submit();
  
});

</script>
</head>
<body>
<form action="" id="forma" method="post" >
<input name="main" id="main" type="hidden" value="<c:out value="${main}"/>" />
<input name="mainDesc" id="mainDesc" type="hidden" value="<c:out value="${mainDesc}"/>" />
<div id="header_fondo">
<div id="header_logo"><img src="../imagenes/head_administracion.jpg" width="450" height="80" id="logoIzquierdo"/></div>

  <div id="header_nav">
    <ul id="navsalir"><li><a href="../j_spring_security_logout" target="_parent"  ></a></li></ul>         
     <c:forEach items="${sistemas}" var="item" varStatus="status"> 
  	 <ul id="nav<c:out value="${item.SIS_DESCRIPCION}"/>"><li><a href="javaScript:cambioMain('<c:out value="${item.ID_SISTEMA}"/>','<c:out value="${item.SIS_DESCRIPCION}"/>')"  ></a></li></ul>
     </c:forEach> 
     <ul><li><div style="padding-top:20px"><strong><c:out value="${unidad}"/><br /><c:out value="${usuario}"/></strong></div></li></ul>
 </div> 
</div>

<div id="header_mainDesc" class="header_menu"  >
<script type="text/javascript" >
var menuItems = [
    ["Inicio","javascript:cambioMain('<c:out value="${main}"/>','<c:out value="${mainDesc}"/>')","inicio.png" , "", "Inicio", "mainFrame", "0","0" , , ],
     <c:set var="anterior" value=""  />
	 <c:forEach items="${menus}" var="item" varStatus="status"> 
	 <c:if test='${item.MOD_DESCRIPCION != anterior }'>
            ["<c:out value="${item.MOD_DESCRIPCION}"/>","","<c:out value="${item.IMAGEN}"/>" , , , , "0", , , ],
	</c:if>	
	<c:set var="anterior" value="${item.MOD_DESCRIPCION}"  />
	 ["|<c:out value="${item.PRIVILEGIO}"/>","<c:out value="${item.URL}"/>",,,"<c:out value="${item.PRIVILEGIO}"/>","mainFrame","1",,,],	
     </c:forEach>
];
dm_initFrame("<c:out value="${mainDesc}"/>", 0, 1, 0);	
</script> 
</div>
 
</form>
</body>
</html>