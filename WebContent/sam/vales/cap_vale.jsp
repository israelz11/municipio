<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Vales - Captura de Vales</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link href="../../include/js/autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" type="text/css" />	
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorValesRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorListadoValesRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../dwr/util.js"> </script>  
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/jquery.form.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../include/js/jquery-ui-1.7.3.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="cap_vale.js"></script>

<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>

</head>
<body  >
<table width="100%" align="center"><tr><td><h1>Vales - Captura de Vales</h1></td></tr></table>
<form name="forma" id="forma" method="post" enctype="multipart/form-data">
<input type="hidden" name="cve_val"  id="cve_val"value='<c:out value="${vale.CVE_VALE}"/>'>
<input type="hidden" name="claveBeneficiario" id="claveBeneficiario" value='<c:out value="${vale.CLV_BENEFI}"/>'>
<input type="hidden" name="tipoBeneficiario" id="tipoBeneficiario" />
<div id="tabuladores">
  <ul>
   <li><a href="#fragment-pedidos"><span>Información general</span></a></li>
   <li><a href="#fragment-movimientos"><span>Movimientos</span></a></li>
  </ul>
      <div id="fragment-pedidos" align="left">
  <table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="formulario">
    <tr>
      <td colspan="6" align="center"></td>
    </tr>
    <tr>
      <td colspan="6" ></td>
    </tr>
    <tr >
      <th width="16%" height="13"  >&nbsp;</th>
      <td width="84%" colspan="5"  >&nbsp;</td>
    </tr>
    <tr >
      <th height="30"  >Numero Vale:</th>
      <td colspan="5" height="30"><div align="left" id="div_vale"><c:out value="${vale.NUM_VALE}"/></div></td>
    </tr>
    <tr >
      <th height="30"  ><input type="hidden" name="unidad" id="unidad" value='<c:out value="${idUnidad}"/>' />
      Unidad Administrativa:</th>
      <td colspan="5" height="30">
      <div class="styled-select">
      <select name="unidad2" class="comboBox" id="unidad2" style="width:500px">
        <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">
          <option value='<c:out value="${item.ID}"/>' 
            <c:if test="${item.ID==idUnidad}"> selected </c:if>
            >
            <c:out value="${item.DEPENDENCIA}"/>
            </option>
          </c:forEach>
      </select>
      </div>
      </td>
    </tr>
    <tr >
      <th height="30">*Tipo de gasto:</th>
      <td colspan="5">
      <div class="styled-select">
      <select name="tipoGasto" class="comboBox" id="tipoGasto" style="width:500px">
        <option value="0">[Seleccione]</option>
        <c:forEach items="${tipodeGasto}" var="item" varStatus="status"> <option value='<c:out value="${item.ID}"/>'
            
          <c:if test='${item.ID==vale.ID_RECURSO}'> selected </c:if>
          >
          <c:out value='${item.RECURSO}'/>
          </option>
        </c:forEach>
      </select>
      </div>
    </td>
    </tr>
    <tr >
      <th height="30">*Fecha:</th>
      <td colspan="5"><table width="519" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="106"><input name="fecha" type="text" class="input" id="fecha" style="width:80px"  value='<c:out value="${vale.FECHA}"/>'  maxlength="15" readonly="readonly" /></td>
          <th width="69" align="right">*Periodo:&nbsp;</th>
          <td width="112">
          <div class="styled-select">
          <select name="cbomes" class="comboBox" style="width:103px" id="cbomes" >
            <c:forEach items="${meses}" var="item" varStatus="status">
              <option value="<c:out value='${item.mes}'/>"

                <c:if test="${item.MES==vale.MES}"> selected </c:if>
                >
                <c:out value="${item.DESCRIPCION}"/>
                </option>
              </c:forEach>
            </select>
            </div>
            </td>
          <th width="53" align="right">*Tipo:</th>
          <td width="179">
          <div class="styled-select">
          <select name="tipoVale" class="comboBox" id="tipoVale" onchange="cambioTipoVale();" style="width:160px">
            <option value="">[Seleccione]</option>
            <c:forEach items="${tiposVales}" var="item" varStatus="status">
              <option value="<c:out value='${item.clave_vale}'/>" <c:if test="${item.CLAVE_VALE==vale.TIPO}"> selected </c:if>
              ><c:out value="${item.TIPO_VALE}"/>
              </c:forEach>
            </select>
            </div>
            </td>
          </tr>
        </table></td>
    </tr>
    <tr >
      <th height="30">Contrato: </th>
      <td colspan="5"><input name="txtnumcontrato"  type="text"  class="input" id="txtnumcontrato" value="${vale.NUM_CONTRATO}" maxlength="30" style="width:150px;" />
        <img src="../../imagenes/search_16.png" alt="Mostrar presupuesto" name="img_contrato"  id="img_contrato" style="cursor:pointer" align="absmiddle"/> <img src="../../imagenes/cross.png" id="img_quitar_contrato" width="16" height="16" alt="Quitar contrato" title="Quitar contrato" align="absmiddle" style="cursor:pointer" />
        <input name="CVE_CONTRATO" type="hidden"  id="CVE_CONTRATO" value="${vale.CVE_CONTRATO}" />
        </td>
    </tr>
    <tr >
      <th height="30">*Beneficiario:</th>
      <td colspan="5"><input name="beneficiario" id="beneficiario" type="text" class="input" value='<c:out value="${vale.NCOMERCIA}"/>'style="width:500px"  maxlength="50">        
        </td>
    </tr>
    <tr >
      <th height="30" >*Justificaci&oacute;n:</th>
      <td colspan="5" >
        <input name="justificacion" type="text" class="input" id="justificacion"  value='<c:out value="${vale.JUSTIF}"/>' style="width:500px" maxlength="400">
        </td>
    </tr>
    <tr >
      <th height="30"  >*Documentación:</th>
      <td colspan="5" height="30"><textarea id="documentacion" name="documentacion" rows="4" class="textarea" wrap="virtual" maxlength="800" style="width:500px"><c:out value="${vale.DOCTO_COMP}"/></textarea></td>
    </tr>
    <tr >
      <th height="30"  ><strong>Periodo de ejecución</strong></th>
      <td colspan="5" height="30">&nbsp;</td>
    </tr>
    <tr >
      <td height="30" align="right">*Inicial:</td>
      <td colspan="5" height="30"><input name="fechaInicial" type="text" class="input" id="fechaInicial" style="width:100px" value='<c:out value="${vale.FECHA_INI}"/>' maxlength="15" readonly="readonly" /></td>
    </tr>
    <tr >
      <td align="right" height="30">*Final:</td>
      <td colspan="5" height="30"><input name="fechaFinal" type="text" class="input" id="fechaFinal" value='<c:out value="${vale.FECHA_FIN}"/>' style="width:100px"  maxlength="15" readonly="readonly" /></td>
    </tr>
    <tr >
      <td align="right" height="30"  >*Fecha máxima comprobación: </td>
      <td colspan="5" height="30"><input name="fechaMaxima" type="text" class="input" id="fechaMaxima" value='<c:out value="${vale.FECHA_MAX}"/>' style="width:100px"   maxlength="15" readonly="readonly" /></td>
    </tr>
    <tr >
      <th height="30"  >Archivo: </th>
      <td colspan="5" height="30"><input type="file" class="input-file" id="archivo" name="archivo" style="width:445px" /></td>
    </tr>
    <tr >
      <th height="30"  >&nbsp;</th>
      <td colspan="5" height="30">
        
        <table width="80%" border="0"  align="left" cellpadding="0" cellspacing="0" class="listas" id="listasArchivo">
          <thead>
            <tr >
              <th width="59%" height="20"  align="center">Archivo</th>
              <th width="16%"  align="center">Tamaño</th>
              <th width="14%"  align="center">Tipo</th>
              <th width="6%"  align="center">&nbsp;</th>
              </tr>
            </thead>
          <tbody>
            </tbody>
          </table>
        
        </td>
    </tr>
    <tr >
      <th height="30"  >&nbsp;</th>
      <td colspan="5" height="30">&nbsp;</td>
    </tr>
    <tr >
      <th height="30"  >&nbsp;</th>
      <td colspan="5" height="30"><table width="380" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>
          	<div class="buttons tiptip">
				<button name="cmdcerrar" id="cmdcerrar" onClick="cerrarVale()" disabled="disabled" title="Cierra para comprometer el importe del Vale" type="button" class="button red middle"><span class="label" style="width:100px">Cerrar</span></button>
            </div>
          </td>
          <td>
          <div class="buttons tiptip">
				<button name="cmdnuevo" id="cmdnuevo" onClick="limpiar()" title="Limpia valores para continuar con nuevo Vale." type="button" class="button red middle"><span class="label" style="width:100px">Nuevo</span></button>
           </div>
          </td>
          <td>
          <div class="buttons tiptip">
				<button name="xGrabar" id="xGrabar" onClick="guardar()" title="Guardar la informacion general del Vale" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button>
           </div>
          </td>
          </tr>
        </table></td>
    </tr>
    <tr >
      <th height="30"  >&nbsp;</th>
      <td colspan="5" height="30"></td>
    </tr>
    
    <tr >
      <td height="13" colspan="6" align="center">
        <c:if test="${regresar=='SI'}"></c:if>      </td>
    </tr>
  </table>
  </div>
  
  <div id="fragment-movimientos">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="formulario">
              <tr>
                <th width="17%" height="17">&nbsp;</th>
                <td>&nbsp;</td>
                <th>&nbsp;</th>
                <td>&nbsp;</td>
                <td width="50%">&nbsp;</td>
              </tr>
              <tr>
                <th height="30">Presupuesto de la Unidad</th>
                <td colspan="4">
                <div class="styled-select">
                <select name="cbUnidad2" class="comboBox" id="cbUnidad2" style="width:445px">
                <option value="0">[Seleccione]</option>
                  <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status">
                    <option value="<c:out value='${item.ID}'/>" <c:if test="${item.ID==idUnidad}"> selected </c:if>>
                    <c:out value="${item.DEPENDENCIA}"/>
                    </option>
                    </c:forEach>
                </select>
                </div>
                </td>
                </tr>
              <tr >
                <th height="30">*Programa:</th>
                <td width="10%"><input name="txtproyecto" type="text" class="input" id="txtproyecto"  value='' size="20" maxlength="6">
                <input type="hidden" id="ID_PROYECTO" value="0"/></td>
                <th width="6%">*Partida:</th>
                <td width="17%"><input name="txtpartida" type="text" class="input" id="txtpartida"  value='' size="20" maxlength="4"  >
                <img src="../../imagenes/search_16.png" alt="Mostrar presupuesto" name="img_presupuesto" onclick="muestraPresupuesto()"  id="img_presupuesto" style="cursor:pointer" align="absmiddle"/></td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <th height="30">Presupuesto:</th>
                <td><input name="txtpresupuesto" type="text" class="input" id="txtpresupuesto"  value="" size="20" readonly></td>
                <th>Disponible:</th>
                <td colspan="2"><input name="txtdisponible" type="text" class="input" id="txtdisponible"  value="" size="20" readonly></td>
              </tr>
              <tr>
                <th height="30">Observaciones:</th>
                <td colspan="4"><textarea  name="txtdetalle"  id="txtdetalle"  cols="80" rows="4" wrap="virtual" class="textarea" maxlength="500" style="width:445px"></textarea></td>
              </tr>
              <tr>
                <th height="30">*Importe:</th>
               
                <td colspan="4">
                  <table width="380" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><input name="txtimporteDet" type="text" class="input" id="txtimporteDet" onkeypress="return keyNumbero(event); " />
                      <input type="hidden" id="ID_DETALLE" value="0"/>
                    <input type="hidden" id="IMPORTE_TOTAL" value="0"/></td>
                    <td><button name="cmdagregar" id="cmdagregar" title="Limpia valores para continuar con nuevo Vale." type="button" class="button blue middle"><span class="label" style="width:80px">Agregar</span></button></td>
                    <td>
                    <button name="cmdnuevoconcepto" id="cmdnuevoconcepto" onClick="limpiar()" title="Limpia valores para continuar con nuevo Vale." type="button" class="button red middle"><span class="label" style="width:80px">Nuevo</span></button>
                    </td>
                  </tr>
                </table></td>
              </tr>
              <tr>
                <th height="20">&nbsp;</th>
                <td colspan="4">&nbsp;</td>
              </tr>
              <tr>
                <td height="30" colspan="5" align="left"><table width="100%" border="0"  align="center" cellpadding="0" cellspacing="0" class="listas" id="listaDetalles">
                  <thead>
                    <tr >
                      <th width="4%" height="20"  align="center"><img src="../../imagenes/cross.png" width="16" height="16" onclick="eliminarDetalle()" style='cursor: pointer;' /></th>
                      <th width="28%"  align="center">Unidad Administrativa</th>
                      <th width="31%"  align="center">Notas</th>
                      <th width="12%"  align="center">Programa</th>
                      <th width="10%" align="center">Partida</th>
                      <th width="12%"  align="center">Importe</th>
                      <th width="3%"  align="center">&nbsp;</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table></td>
              </tr>
              <tr>
                <td height="20" colspan="5" align="left">&nbsp;</td>
          </tr>
          </table>
  </div>
  
 </div>
</form>
</body>
</html>
