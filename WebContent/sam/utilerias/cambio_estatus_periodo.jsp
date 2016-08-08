<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorCambioEstatusPeriodosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
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

function  cambiarEstatus(tipo,idmes,estado){
	var status = "";
	if(estado=="ACTIVO")
		status= "cerrar";
	else 	
		status = "abrir"
	var meses = new Array("Enero","Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
	jConfirm('Al cerrar el periodo se aplicara el cierre interno y no se podran realizar nuevas operaciones sobre este hasta volver a reaperturarlo ¿Confirma que desea <strong>'+status+'</strong> el periodo de '+meses[idmes-1]+'?','Confirmar', function(r){
				if(r){
						ShowDelay((status =='cerrar' ? 'Cerrando el periodo: ' +meses[idmes-1]:'Abriendo el periodo: '+meses[idmes-1] ), '');
						controladorCambioEstatusPeriodosRemoto.cerrarPeriodo(idmes,estado, {
							callback:function(items) { 
									if(items==""){
								  		CloseDelay('Periodo y cierre aplicados con exito', function(idmes,estado,tipo){
											$('#idMes').attr("value",idmes);
											$('#estatus').attr("value",estado);
											$('#tipoEstatus').attr("value",tipo);
											$('#acciones').attr("value","guardar");
											$('#acciones').action = "cambio_estatus_periodo.action";
											$('#forma').submit();
									  });
									}
									else 
										jError(items, 'Error');
						  
							},		
						 	errorHandler:function(errorString, exception) { 
								jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 		}
						
						});	
				}
		});
}

function cambiarEstatusEvaluacion(idmes, estado){
	var status = "";
	if(estado=="ACTIVO")
		status= "cerrar";
	else 	
		status = "abrir"
		
	var meses = new Array("Enero","Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
	jConfirm('¿Confirma que desea <strong>'+status+'</strong> el periodo de evaluacion de proyectos del mes de '+meses[idmes-1]+'?','Confirmar', function(r){
				if(r){
						controladorCambioEstatusPeriodosRemoto.cerrarEval(idmes,estado, {
							callback:function(items) { 
									if(items==""){
								  		CloseDelay('Periodo de evaluacion de proyectos cerrado con exito', function(idmes, estado){
											$('#idMes').attr("value",idmes);
											$('#estatus').attr("value",estado);
											$('#acciones').attr("value","guardar");
											$('#acciones').action = "cambio_estatus_periodo.action";
											$('#forma').submit();
									  });
									}
									else 
										jError(items, 'Error');
						  
							},		
						 	errorHandler:function(errorString, exception) { 
								jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 		}
						
						});
						
				}
		});
}
</script>
<title>Cambiar estatus mes</title>
</head>
<body>
<br />
<form id="forma" name="forma" method="post">
  <input type="hidden" name="idMes" id="idMes"  />
  <input type="hidden" name="acciones" id="acciones"   />
  <input type="hidden" name="estatus" id="estatus"  />
  <input type="hidden" name="tipoEstatus" id="tipoEstatus"  />
<table width="85%" align="center"><tr><td><h1>Administración - Cambio de Estatus de Periodos</h1></td></tr></table>
<table  border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" width="85%" >
  <tr >
    <td  >
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listas"  id="tablaSistema"  >
      <thead>
        <tr id="tr">
          <th width="15%" height="20" align="center">Periodo</th>
          <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUPERPRIVILEGIO_CERRAR_PERIODO_PRESUPUESTAL">
              <th width="16%" align="center">Estatus</th>
              <th width="17%" align="center">Periodos</th>
              <th width="14%" align="center">Total del mes</th>
          </sec:authorize>
          
          <th width="13%" align="center">Estatus evaluación</th>          
          <th width="20%" align="center">Evaluación</th>
        </tr>
      </thead>
    <tbody> 
        <c:forEach items="${meses}" var="item" varStatus="status" >
          <tr>
            <td><c:out value="${item.DESCRIPCION}"/></td>
            <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUPERPRIVILEGIO_CERRAR_PERIODO_PRESUPUESTAL">
                <td align="center"><c:out value="${item.ESTATUS}"/></td>            
                <td align="center"><c:if test="${ item.MES >= mesactivo-1   }">            
                <input type="button" value='<c:out value="${item.ACCION}"/>' id="cmdcambiarpassword2" name="cmdcambiarpassword2" class="botones" onclick='cambiarEstatus( "DOC", <c:out value="${item.ID_MES}"/>, "<c:out value="${item.ESTATUS}"/>" )' /> </c:if></td>
                <td align="center">$0.00</td>
            </sec:authorize>
            <td align="center"><c:out value="${item.ESTATUSEVA}"/></td>
            <td align="center"><input type="button" value='<c:out value="${item.ACCIONEVA}"/>' id="cmdcambiarpassword3" name="cmdcambiarpassword3" class="botones" onclick='cambiarEstatusEvaluacion(<c:out value="${item.ID_MES}"/>, "<c:out value="${item.ESTATUSEVA}"/>" )' /></td>
          </tr> 
        </c:forEach>
      </tbody> 
    </table>
    </td>
  </tr>
</table>
  </form>
</body>
</html>