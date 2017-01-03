<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorCambioUnidad.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
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
<script language="javascript">

function  cambiarDato(){
	if($('#dependencia').attr('value')==0) {jAlert('La unidad administrativa seleccionada no es válida','Advertencia');return false;}
	jConfirm('¿Confirma que desea cambiarse de Unidad Administrativa?', 'Confirmar', function(r){
		if(r){
			ShowDelay('Cambiando Unidad Administrativa','');
			controladorCambioUnidad.cambioDeEntidad( $('#ejercicio').attr('value'),$('#dependencia').attr('value'),{
				  callback:function(items) { 	    
					CloseDelay("El cambio se realizo satisfactoriamente", function(){
						window.parent.frames['topFrame'].location = '../../menu/top.action';
					});
						  
				} 					   				
				,
				errorHandler:function(errorString, exception) { 
					jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador","Error");   
				}
		  });
		}
	});
}

</script>
<title>Cambiar contraseña</title>
</head>
<body>
<br />
  <table width="85%" align="center"><tr><td><h1>Administración - Cambio de Unidad Administrativa</h1></td></tr></table>
<table  border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" width="85%" >
  <tr >
    <th height="15" >&nbsp;</th>
    <td>&nbsp;</td>
  </tr>
  <tr >
    <th width="15%" height="30" >Ejercicio:</th>
    <td width="85%"><select name="ejercicio" class="comboBox" id="ejercicio" style="width:100px">
    <c:forEach items="${ejercicio}" var="item" varStatus="status">                  
              <option value="<c:out value="${item.EJERCICIO}"/>" ><c:out value="${item.EJERCICIO}"/></option>
            </c:forEach>
      </select>
    </td>
  </tr>
  <tr >
    <th  >Unidad Administrativa:</th>
    <td ><select name="dependencia" class="comboBox" id="dependencia" style="width:445px;">
    		<option value="0">[Seleccione]</option>
    		<c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">                  
              <option value="<c:out value="${item.ID}"/>" <c:if test="${item.ID==idUnidad}"> selected </c:if> ><c:out value="${item.DEPENDENCIA}"/></option>
            </c:forEach>
        </select>
      </td>
  </tr>
  <tr >
    <td height="36" >&nbsp;</td>
    <td align="left" ><input type="button" value="Cambiar unidad" id="cmdcambiarpassword" name="cmdcambiarpassword" class="botones" onclick="cambiarDato()"></td>
  </tr>
</table>
</body>
</html>