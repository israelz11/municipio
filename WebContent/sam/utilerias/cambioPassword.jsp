<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Cambiar contraseña</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge">


<!--<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">-->
<link rel="stylesheet" href="../../include/css/bootstrap.min-3.3.6.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css">

<link rel="stylesheet" href="../../include/css/sweet-alert.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorCambioPassword.js"> </script><!-- Viene del Controlador ubicado en mx.gob.municipio.centro.view.seguridad/ControladorCambioPassword -->
<script type="text/javascript" src="../../dwr/engine.js"> </script>    
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="cambioPassword.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />

<script type="text/javascript" src="../../include/js/sweet-alert.min.js"></script>
<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
<!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->
<style type="text/css"> 
	@import url("../../include/css/calendar/calendar-win2k-cold-1.css"); 
</style>

</head>

<body>

<form class="form-horizontal" name="frmcontraseña" action="" method="get">
<br/>
<br/>
	<div class="row col-md-offset-4">
        	<h1 class="h1-encabezado">Administración - Cambiar contraseña</h1>
	</div>
	<div class="container">
		<div class="well">
    		<div class="alert alert-warning">
  				<strong class="col-md-offset-1">Warning!</strong> 
                <div class="col-md-offset-2">
                    <img src="../../imagenes/topic.PNG" width="8" height="7">&nbsp;&nbsp; Tu nueva contraseña debe tener como minimo 6 caracteres.</img></br>
                    <img src="../../imagenes/topic.PNG" width="8" height="7">&nbsp;&nbsp; No utilices como contraseña tu login de usuario.</br></img>
                    <img src="../../imagenes/topic.PNG" width="8" height="7">&nbsp;&nbsp; Utiliza una combinación de letras y números.</br></img>
                    <img src="../../imagenes/topic.PNG" width="8" height="7">&nbsp;&nbsp; Las contraseñas se distinguen entre masyúsculas y minúsculas. Recuerda comprobar la tecla que bloquea las mayusculas.</img>
                </div>
                
			</div>		
    	</div> 
        <br/>
		<br/>
			<div class="container">                
                <div class="form-group">
                	<label for="txtcontraseñaanterior" class="control-label col-md-offset-2 col-md-2">Contraseña anterior:</label>
                    <div class="col-md-3">
                    	<input type="password" value="" id="txtcontraseñaanterior"  class="form-control"/>
                    </div>   
                </div>
                <div class="form-group">
                    <label for="txtnuevacontraseña" class="control-label col-md-offset-2 col-md-2">Contraseña nueva:</label>
                    <div class="col-md-3">
                        <input type="password" value="" id="txtnuevacontraseña" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="txtconfirmarcontraseña" class="control-label col-md-offset-2 col-md-2">Confirmar:</label>
                    <div class="col-md-3">
                        <input type="password" value="" id="txtconfirmarcontraseña" class="form-control"/>
                        
                    </div>
                </div>  
                <div class="form-group">
                	<div class="control-label col-md-offset-4 col-md-2">
                		<input type="button" value="Cambiar contraseña" id="cmdcambiarpassword" name="cmdcambiarpassword" class="btn btn-success btn-md"/>
                	</div>
                </div>
           </div>     
  </div>    

</form>
</body>
</html>