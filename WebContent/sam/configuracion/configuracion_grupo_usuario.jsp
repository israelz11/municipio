<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>Avances fisicos de proyectos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorAsignacionGrupoUsuarioRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script language="javascript">
 function guardar(){			
	var idGrupoUsu= $('input[name=claves]:checked').val();
	ShowDelay('Asignando grupo seleccionado','');
    controladorAsignacionGrupoUsuarioRemoto.guardarUsuarioGrupo($('#idUsuario').attr('value'),idGrupoUsu,{
			 callback:function(items) {			  			  
	  		   CloseDelay("Grupo asignado correctamente", 2000);			 
 		     }	
								,errorHandler:function(errorString, exception) { 
								   jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
								}
			});		
}

</script>
<body >
<form name="forma" method="get" action="" onSubmit=" return false" >
<input type="hidden" name="idUsuario" id="idUsuario" value='<c:out value="${idUsuario}"/>' >
<br />
  <table width="90%" align="center"><tr><td><h1>Administración - Asignación de Grupos</h1></td></tr></table>
  <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <td height="30">
        <table width="100%"  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas"  id="detallesTabla" >
          <thead>
            <tr >
              <th width="8%" align="center" height="20">Num.</th>
              <th width="78%"  align="left">&nbsp;Grupo</th>
              <th width="14%" align="center">Selección</th>
            </tr>
             <c:forEach items="${grupos}" var="item" varStatus="status">
            <tr >
              <td align="center"><c:out value="${status.count}"/></td>
              <td align="left" >&nbsp;<c:out value="${item.GRUPO_CONFIG}"/></td>
              <td align="center"><input name="claves" type="radio" id="claves" value='<c:out value="${item.ID_GRUPO_CONFIG}"/>'  <c:if test="${item.ASIGNADO==1}"> checked </c:if> >
              </td>
            </tr>
            </c:forEach>
          </thead>
          <tbody>
          </tbody>
      </table></td>
    </tr>
    <tr>
      <td ><div align="center"> <input type="button"  class="botones"  name="btnGrabar" value="Guardar"  onClick="guardar()" /></div></td>
    </tr>
  </table>
  <br />
</form>
</body>
</html>
