<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="content-style-type" content="text/css">
    <meta http-equiv="content-language" content="en-gb">
    <meta http-equiv="imagetoolbar" content="no">
    <meta name="resource-type" content="document">
    <meta name="keywords" content="">
    <meta name="description" content=""><title> Municipio 2017 | Login</title>
    <!--  
    <link href="include/css/print.css" rel="stylesheet" type="text/css" media="print" title="printonly">
    <link href="include/css/general.css" rel="stylesheet" type="text/css" media="screen, projection">
    <link href="include/css/estilosam.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="include/css/css/css3-buttons.css" type="text/css" media="screen">
    <link rel="stylesheet" href="include/css/tiptip.css" type="text/css"  media="screen">
    -->
    <link href="include/css/bootstrap.css" rel="stylesheet" type="text/css">
 	<!--  <link href="include/css/estilosam2.css" rel="stylesheet" type="text/css"> -->
    <link rel="stylesheet" href="include/js/componentes/jquery.alerts.css" type="text/css">
    <script type="text/javascript" src="include/js/jquery-1.3.2.min.js"></script>
    <script src="include/css/jquery.tiptip.js"></script>
    <script type="text/javascript" src="include/js/componentes/jquery.alerts.js"></script>
    <script src="index.js"></script>
	<style>
		body {
			background-color: #f1f3f6 !important;
		}
	</style>
</head>

<body>

	
	<div class="container">
	
	<form class="form-horizontal login" method="post"  name="forma" id="forma" action="j_spring_security_check" >
		<div class="form-group" align="center">
			<img src="imagenes/logotipo_horizontal_rgb.png" class="img-responsive" style="widht:auto; height:200px">
			
		</div>
		
		<div class="form-group">
			<label for="j_username" class="control-label col-md-5">Nombre</label>
			<div class="col-md-3">
				<input type="text" class="form-control" placeholder="Usuario" style="widht:auto" name="j_username" id="j_username" onKeyPress="validar(event, this,1)">
			</div>
		</div>
		<div class="form-group">
			<label for="j_password" class="control-label col-md-5">Contraseña</label>
			<div class="control-label col-md-3">
				 <input type="password" class="form-control" size="20" style="widht:auto" name="j_password" id="j_password" title="Password" onKeyPress="validar(event, this,2)" placeholder="Contraseña">
			</div>
		</div>
		<div class="form-group">
			<div class="checkbox col-md-2 col-md-offset-5">
				<label>
					<input type="checkbox">Recuerdame
				</label>
			</div>
		</div>
		<div class="form-group">
			<div class="col-md-3 col-md-offset-5">
				<button class="btn btn-primary" style="width: 100%;">Enviar</button> 
			</div>
		</div>
		
	</form>
		
	
	</div>
</body>
</html>
<c:if  test="${message != 0 }" > 
	<script>jError('Usuario ó contraseña incorrecto, vuelva a intentarlo', 'Error de acceso');</script>
</c:if>
<script>MostrarCookie("username_sam", "j_username");</script>
<script>MostrarCookie("password_sam", "j_password");</script>
<script>MostrarCookieChk("recordar_sam", "chkrecordar");</script>