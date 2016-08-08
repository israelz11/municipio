<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Avances fisicos de proyectos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorEvaluacionProyectosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="evaluacion_proyectos.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />

<body >
<form name="forma" method="get" action="">
<div id="tabuladores">
  <ul>
   	<li><a href="#fragment-evaluacion"><span>Evaluación</span></a></li>
   	<li><a href="#fragment-general"><span>Información General</span></a></li>
  </ul>
  <div id="fragment-evaluacion" align="left">
      <table   border="0" align="center" cellpadding="0" cellspacing="0" class="formulario"  width="100%" >
        <tr id="trEva2" align="left">
          <th width="75%" align="left"><table width="553" border="0" align="left" cellpadding="0" cellspacing="0">
          <tr>
                  <td height="33"><span style="font-weight:bold">*Tipo de evaluación:</span></td>
                  <td align="left"><select name="tipoEvaluacion" class="comboBox" id="tipoEvaluacion" onChange="cambiarEvaluacion()" style="width:105px">
                    <option value="0">[Seleccione]</option>
                    <option value="Fisica">Fisica</option>
                    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_DAR_DE_ALTA_AVENCES_CONTRALORIA">
                      <option value="Contraloria">Contraloria</option>
                      </sec:authorize>
                  </select></td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td height="28"><span style="font-weight:normal">Mes:</span></td>
                  <td align="left"><select name="meses" class="comboBox" id="meses" style="width:105px">
                    <option value="0" >[Seleccione]</option>
                    <c:forEach items="${meses}" var="item" varStatus="status">
                      <option value="<c:out value='${item.MES}'/>" >
                        <c:out value="${item.DESCRIPCION}"/>
                        </option>
                      </c:forEach>
                  </select></td>
                  <td>&nbsp;</td>
                  <td>
                  <input type="hidden" name="proyecto" id="proyecto" value="<c:out value='${id_proyecto}'/>" />
                  <input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>" />
                  <input name="idEvaluacionProyecto" type="hidden" id="idEvaluacionProyecto" value="<c:out value='${idEvaluacionProyecto}'/>" />
                  <input type="hidden" name="idAvanceDetalle" id="idAvanceDetalle" /></td>
                </tr>
                <tr>
                  <td width="153" height="29"><span style="font-weight:normal">Avances:</span></td>
                  <td width="151" align="left"><input name="avance" type="text" class="input" id="avance" onkeypress=" return keyNumbero( event );" value="" maxlength="3" style="width:100px"/></td>
                  <td width="71"><span style="font-weight:normal">Fecha:</span></td>
                  <td width="178" align="left"><input name="fechaAvance" type="text" class="input" id="fechaAvance" value="" maxlength="10" readonly="readonly" style="width:100px"/></td>
                </tr>
                <tr>
                  <td height="31">&nbsp;</td>
                  <td colspan="3" align="left"><input type="button"  class="botones"  name="xGrabar" value="Guardar"  onClick="guardarDetalleEvaluacionDeProyecto()" style="width:120px"/>            <input type="button"  class="botones"  name="cmdlimpiarItem" value="Nuevo" id="cmdlimpiarItem"  onClick="limpiarItem();" style="width:120px"/></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="4" align="center"><table  border="0" align="center"  cellpadding="0" cellspacing="0" class="listas" width="500"  id="detalleEvaluaciones" >
                    <thead>
                      <tr >
                        <th width="33%" height="20">Mes</th>
                        <th width="35%" align="center" >Fecha</th>
                        <th width="23%" >% Avance</th>
                        <th width="9%" >Opc.</th>
                      </tr>
                    </thead>
                    <tbody>
                    </tbody>
                  </table></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="4" align="left"></td>
                </tr>
                <tr>
                  <td colspan="4" align="left"></td>
                </tr>
                <tr>
                  <td colspan="4" align="left"></td>
                </tr>
            </table>
          </th>
        </tr>
       </table>
	</div>
    <div id="fragment-general" align="left">
       <table   border="0" align="center" cellpadding="0" cellspacing="0" class="formulario"  width="100%" >
        <tr >
          <td height="20" align="left" >
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="55" height="20">&nbsp;</td>
              <td width="535">Fecha de Inicio</td>
              <td width="467">Fecha de T&eacute;rmino</td>
              <td width="550">Fecha Acta Entrega</td>
              </tr>
            <tr>
              <td height="20">&nbsp;</td>
              <td height="20"><input name="fechaInicio" type="text" class="input" id="fechaInicio" value="<c:out value='${eval.FECHA_INICIO}'/>" maxlength="10" style="width:100px"/></td>
              <td><input name="fechaTermino" type="text" class="input" id="fechaTermino" value="<c:out value='${eval.FECHA_TERMINO}'/>" maxlength="10" style="width:100px"/></td>
              <td><input name="fechaActa" type="text" class="input" id="fechaActa" value="<c:out value='${eval.FECHA_ACTA}'/>" maxlength="10" style="width:100px"/></td>
              </tr>
            <tr>
              <td height="20">&nbsp;</td>
              <td height="20">Fecha Inicio Contrato</td>
              <td>Fecha Término Contrato</td>
              <td>Monto Autorizado</td>
              </tr>
            <tr>
              <td height="20">&nbsp;</td>
              <td height="20"><input name="fechaInicioContrato" id="fechaInicioContrato" type="text" class="input" maxlength="10" value="<c:out value='${eval.FECHA_CONTRATO_INI}'/>" style="width:100px"/></td>
              <td><input name="fechaTerminoContrato" id="fechaTerminoContrato" type="text" class="input" maxlength="10" value="<c:out value='${eval.FECHA_CONTRATO_FIN}'/>" style="width:100px"/></td>
              <td>$<fmt:formatNumber value='${eval.MONTOAUTORIZADO}' pattern="###,###,###.00"/></td>
              </tr>
            <tr>
              <td height="20">&nbsp;</td>
              <td height="20">Avance Total Físico</td>
              <td>Avance Total Contraloría </td>
              <td>Monto Ejercido</td>
              </tr>
            <tr>
              <td colspan="2" align="center"><c:out value='${eval.AVANCE_FISICO}'/></td>
              <td align="center"><c:out value='${eval.AVANCE_CONTRALORIA}'/></td>
              <td height="20">$<fmt:formatNumber value='${eval.MONTOEJERCIDO}' pattern="###,###,###.00"/></td>
              </tr>
            <tr>
              <td colspan="4">&nbsp;</td>
            </tr>
            <tr>
              <td colspan="4" align="center"><input type="button"  class="botones"  id="btnGuardar"  name="btnGuardar" value="Guardar"  onClick="guardarEvaluacionDeProyecto()" style="width:120px" ></td>
              </tr>
            <tr>
              <td colspan="4" align="center">&nbsp;</td>
            </tr>
          </table>
          </td>
        </tr>
    </table>
    </div>
</div>
</form>
</body>
</html>
