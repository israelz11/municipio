<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
    <link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
	<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
    <script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
    <script type="text/javascript" src="../../include/js/jquery.form.js"></script>
    <script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
    <script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
    <script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script> 
    <script type="text/javascript" src="../../dwr/interface/ControladorContratosRemoto.js"> </script>
	<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
    <script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>   
    <script type="text/javascript" src="../../dwr/engine.js"> </script>
    <script type="text/javascript" src="../../include/js/jquery-ui-1.7.3.custom.min.js"></script>
    <script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
    <script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
    <script type="text/javascript" src="../../include/js/toolSam.js"></script>
    <script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js?x=<%=System.currentTimeMillis()%>"></script>
    <script type="text/javascript" src="../../include/js/otros/productos.js"></script>
    <script type="text/javascript" src="cap_contratos.js?x=<%=System.currentTimeMillis()%>"></script>
    
    <link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
    <link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
    <script src="../../include/css/jquery.tiptip.js"></script>

    <link rel="stylesheet" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" type="text/css" />
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
    a:link {
	color: #00F;
	text-decoration: none;
}
a:visited {
	text-decoration: none;
	color: #603;
}
a:hover {
	text-decoration: underline;
}
a:active {
	text-decoration: none;
}
    </style>
<title>Contratos - Captura de Contrato</title>
</head>

<body>
<form name="forma" id="forma" method="post" enctype="multipart/form-data">
<input type="hidden" name="clavePedido" id="clavePedido"  value="">
<h1>&nbsp;Contrato - Captura de Contrato 
  <c:out value='${modelo.NUM_REQ}'/></h1>
<div id="tabuladores">
  <ul>
   <li><a href="#fragment-pedidos"><span>Información general</span></a></li>
    <li ><a href="#tabsCon">Conceptos</a> </li>
  </ul>
      <div id="fragment-pedidos" align="left">
<table border="0" cellspacing="0" cellpadding="0" class="formulario" width="100%">
                   <tr>
                    <td colspan="2" height="30">&nbsp;<strong>Nota:</strong> La información marcada con (*) es requerida. 
                    <input type="hidden" id="CVE_CONTRATO"  name="CVE_CONTRATO" value="<c:out value='${cve_contrato}'/>"/></td>
                  </tr>
                  <tr>
                     <th height="30"><span class="field_label">*Unidad Administrativa:</span></th>
                     <td>
                     	<sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
                          <c:out value="${nombreUnidad}"/>
                          <input type="hidden" id="cbodependencia" name="cbodependencia" value="<c:out value='${idUnidad}'/>">
                          </sec:authorize>
                        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
                          <div class="styled-select">
                            <select name="cbodependencia" id="cbodependencia" style="width:400px">
                              <option value="0">[Seleccione]</option>
                              <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
                                <option value='<c:out value="${item.ID}"/>' 
                                            <c:if test='${item.ID==idUnidad}'> selected </c:if>>
                                <c:out value='${item.DEPENDENCIA}'/>
                                </option>
                                </c:forEach>
                              </select>
                            </div>
                          </sec:authorize> 
                     </td>
        			</tr>
                  <tr>
                    <th height="30">*No. Contrato:</th>
                    <td><input name="txtnumcontrato" type="text" class="input" id="txtnumcontrato" style="width:195px" value="<c:out value='${Contrato.NUM_CONTRATO}'/>" maxlength="50"/></td>
                  </tr>
                  <tr>
                    <th height="30">*Tipo de Contrato:</th>
                    <td><span class="styled-select">
                      <select name="cbotipocontrato" class="comboBox" id="cbotipocontrato" style="width:195px">
                        <option value="0">[Seleccione]</option>
                        <c:forEach items="${tipoContratos}" var="item">
                          <option value='<c:out value="${item.ID_TIPO}"/>' <c:if test='${item.ID_TIPO==Contrato.ID_TIPO}'> selected </c:if>>
                            <c:out value='${item.DESCRIPCION}'/>
                          </option>
                        </c:forEach>
                      </select>
                    </span></td>
                  </tr>
                  <tr>
                    <th height="30">Tipo de gasto:</th>
                    <td><span class="styled-select">
                      <select name="tipoGasto" class="comboBox" id="tipoGasto" style="width:400px">
                      <option value="0">[Seleccione]</option>
                        <c:forEach items="${tipoGastos}" var="item" varStatus="status">
                          <option value="<c:out value='${item.ID}'/>" <c:if test='${item.ID==Contrato.ID_RECURSO}'> selected </c:if>>
                            <c:out value="${item.RECURSO}"/>
                          </option>
                        </c:forEach>
                      </select>
                    </span></td>
                  </tr>
                  <tr>
                    <th height="30">*Proveedor:</th>
                    <td><input name="txtbeneficiario" id="txtbeneficiario"  type="text" class="input" value="${Contrato.PROVEEDOR}" style="width:400px" maxlength="50" /> <input name="CLV_BENEFI" type="hidden"  id="CLV_BENEFI" size="8" maxlength="6" readonly="true" value="${Contrato.CLV_BENEFI}" /></td>
                  </tr>
                  <tr>
                    <th height="30">Num. Documento:&nbsp;</th>
                    <td><input name="txtdocumento" type="text" class="input" id="txtdocumento" style="width:165px" value="${Contrato.NUM_DOC}" maxlength="25"/>
                    <img id="img_producto" src="../../imagenes/search_16.png" border="0" style="cursor:pointer" align="absmiddle" onClick="muestraDocumento()"/>
                    <input name="CVE_DOC" type="hidden"  id="CVE_DOC" size="8" maxlength="6" readonly="true" value="${Contrato.CVE_DOC}" /></td>
                  </tr>
                  <tr>
                    <th height="30">*Fecha Inicial:&nbsp;</th>
                    <td><input name="txtfechainicial" type="text" class="input" id="txtfechainicial" style="width:100px" value="${Contrato.FECHA_INICIO}" size="12" maxlength="10" /></td>
                  </tr>
                  <tr>
                    <th height="30">*Fecha Termino: </th>
                    <td><input name="txtfechatermino" type="text" class="input" id="txtfechatermino"  value="${Contrato.FECHA_TERMINO}" style="width:100px" maxlength="8" /></td>
                  </tr>
                  <tr>
                    <th height="30">Tiempo de entrega:</th>
                    <td><input name="txttiempoentrega" type="text" class="input" id="txttiempoentrega"  value="${Contrato.TIEMPO_ENTREGA}" style="width:165px" maxlength="400" /></td>
                  </tr>
                  <tr>
                    <th height="30">Oficio Autorización: </th>
                    <td><input name="txtnumoficio" type="text" class="input" id="txtnumoficio"  value="${Contrato.OFICIO_AUT}" style="width:165px" maxlength="50" /> </td>
                  </tr>
                  <tr>
                    <th height="30">Anticipo: </th>
                    <td><input name="txtanticipo" type="text" class="input" id="txtanticipo"  value="<fmt:formatNumber value='${Contrato.ANTICIPO}'  pattern='#########0.00' />" style="width:165px" maxlength="50" onkeypress="return keyNumbero(event); " /></td>
                  </tr>
                  <tr>
                    <th>*Concepto:</th>
                    <td><textarea id="txtdescripcion" name="txtdescripcion" rows="4" class="textarea" wrap="virtual" maxlength="1900" style="width:400px">${Contrato.DESCRIPCION}</textarea>
                      </td>
                  </tr>
                  <tr>
                    <th>Archivo</th>
                    <td height="30"><input type="file" class="input-file" id="archivo" name="archivo" style="width:400px" /></td>
                  </tr>
                  <tr>
                    <th>&nbsp;</th>
                    <td height="30"><table width="80%" border="0"  align="left" cellpadding="0" cellspacing="0" class="listas" id="listasArchivo">
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
                    </table></td>
                  </tr>
                  <tr>
                    <td height="17" colspan="2"></td>
                  </tr>
                  <tr>
                    <td width="196">&nbsp;</td>
                    <td><table width="380" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><div class="buttons tiptip">
                          <button name="cmdcerrar" id="cmdcerrar" disabled="disabled" title="Cierra para comprometer el Contrato" type="button" class="button red middle"><span class="label" style="width:100px">Cerrar</span></button>
                        </div></td>
                        <td><div class="buttons tiptip">
                          <button name="cmdnuevo" id="cmdnuevo" title="Nueva captura de Contrato" type="button" class="button red middle"><span class="label" style="width:100px">Nuevo</span></button>
                        </div></td>
                        <td><div class="buttons tiptip">
                          <button name="cmdguardar" id="cmdguardar" title="Guardar la informacion del Contrato" type="button" class="button blue middle"><span class="label" style="width:100px">Guardar</span></button>
                        </div></td>
                      </tr>
                    </table></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                  </tr>
                </table>
</div>
<div id="tabsCon">
<table width="100%"  class="formulario" cellpadding="0" cellspacing="0">
  <tr>
    <th height="30">&nbsp;</th>
    <td>&nbsp;</td>
  </tr>
  
  <tr>
                <th height="30">Presupuesto de la Unidad</th>
                <td colspan="">
                <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
                	<input type="hidden" id="cbUnidad2" name="cbUnidad2" value="<c:out value='${idUnidad}'/>">
          		</sec:authorize>
                <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
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
                </sec:authorize>
                </td>
              </tr> 	
       
       
  <tr id="tr_programa">
    <th width="10%" height="30">*Programa:</th>
    <td width="90%">
    <table width="687" border="0" cellpadding="0" cellspacing="0">
      
      		
      <tr>
        <td width="156"><input name="txtproyecto" type="text" class="input" id="txtproyecto"  value='' size="20" maxlength="6">
          <input type="hidden" id="ID_PROYECTO" value="0"/></td>
       </tr>   
       
        <th width="90">Partida:</th>
        <td width="151"><input name="txtpartida" type="text" class="input" id="txtpartida"  value='' size="20" maxlength="4"  ></td>
        <input type="hidden" id="CLV_PARTID" value="0"/></td>
        <th width="62">Mes</th>
        <td width="165">
        	<div class="styled-select">
                <select name="cbomes" class="comboBox" id="cbomes" style="width:155px">
                  <option value="0">[Seleccione]</option>
                  <c:forEach items="${mesesActivos}" var="item" varStatus="status">
                    <option value="<c:out value='${item.ID_MES}'/>" >
                      <c:out value="${item.DESCRIPCION}"/>
                    </option>
                  </c:forEach>
                  </select>
        	</div>
        </td>
        <td width="63"><span class="styled-select"><img src="../../imagenes/search_16.png" alt="Mostrar presupuesto" name="img_presupuesto" onClick="muestraPresupuesto()"  id="img_presupuesto2" style="cursor:pointer" align="absmiddle"/></span></td>
      </tr>
    </table></td>
  </tr>
  <tr id="tr_presupuesto">
    <th height="30">Presupuesto:</th>
    <td><table width="429" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="155"><input name="txtpresupuesto" type="text" class="input" id="txtpresupuesto"  value="" size="20" readonly></td>
        <th width="89">Disponible:</th>
        <td width="185"><input name="txtdisponible" type="text" class="input" id="txtdisponible"  value="" size="20" readonly></td>
      </tr>
    </table></td>
  </tr>
  <tr id="tr_importe">
    <th height="30">Importe: </th>
    <td><table width="427" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="195"><input name="txtimporte" type="text" class="input" id="txtimporte" onkeypress="return keyNumbero(event); " />
          <input type="hidden" id="ID_DETALLE" value="0"/>
          <input type="hidden" id="IMPORTE_TOTAL" value="0"/></td>
        <td width="115" align="center"><button name="cmdagregar" id="cmdagregar" type="button" class="button blue middle"><span class="label" style="width:80px">Agregar</span></button></td>
        <td width="117"><button name="cmdnuevoconcepto" id="cmdnuevoconcepto" type="button" class="button red middle"><span class="label" style="width:80px">Nuevo</span></button></td>
      </tr>
    </table></td>
  </tr>
  <tr class="formulario">
    <th height="17" valign="top">&nbsp;</th>
    <td align="left">&nbsp;</td>
  </tr>
  <tr class="formulario">
    <th height="30" valign="top">&nbsp;</th>
    <td align="left">
    <table width="80%" border="0"  align="left" cellpadding="0" cellspacing="0" class="listas" id="listaConceptos">
      <thead>
        <tr >
          <th width="6%" height="20"  align="center"><img src="../../imagenes/cross.png" width="16" height="16" onclick="eliminarConcepto()" style='cursor: pointer;' /></th>
          <th width="38%"  align="center">Unidad Administrativa</th>
          <th width="15%"  align="center">Periodo</th>
          <th width="14%"  align="center">Programa</th>
          <th width="13%" align="center">Partida</th>
          <th width="14%"  align="center">Importe</th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table></td>
  </tr>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
</div>
     
</div>
</form>
</body>
</html>