<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../dwr/interface/"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorRequisicion.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>   
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="../../include/js/otros/productos.js"></script>
<link rel="stylesheet" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />

<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<script type="text/javascript" src="../../include/js/jquery.qtip-1.0/jquery.qtip-1.0.0-rc3.min.js"></script>
<script type="text/javascript" src="capturarRequisicion.js"></script>

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

<title>Cambiar contraseña</title>
</head>
<body>
<form name="frmcontraseña" action="" method="get">
<h1>&nbsp;Requisiciones - Captura de Requisiciones, Ordenes de Trabajo y Ordenes de Servicio</h1>
<div id="tabuladores">
  <ul>
   <li><a href="#fragment-requisicion"><span>Información general</span></a></li>
   <li><a href="#fragment-conceptos"><span>Lotes</span></a></li>
  </ul>
  <div id="fragment-requisicion" align="left">
                <table border="0" align="left" cellpadding="0" cellspacing="0" class="formulario" width="100%">
                  <tr bgcolor="#FFFFFF">
                    <td colspan="2" align="left"></td>
                  </tr>
                  <tr>
                    <td colspan="2" align="left">&nbsp;<strong>Nota:</strong> La información marcada con (*) es requerida. </td>
                  </tr>
                  <tr>
                    <th>
                    </th>
                    <td>
                    <input type="hidden" id="ID_PROYECTO" value="0"/>
                    <input type="hidden" id="CVE_REQ" value="<c:out value='${cve_req}'/>"/>
                    <input type="hidden" id="CVE_CONCURSO" value="" />
                    <input type="hidden" id="CVE_BENEFI" value="0" />
                    <input type="hidden" id="MES" />
                                        
                    <input type="hidden" name="cbodependencia2" id="cbodependencia2" value='<c:out value="${idUnidad}"/>' /></td>
                  </tr>
                  <tr>
                    <th width="18%">*Unidad Administrativa:</th>
                    <td>
                      <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      <c:out value="${nombreUnidad}"/>
      <input type="hidden" name="cbodependencia" id="cbodependencia" value='<c:out value="${idUnidad}"/>' />
                      </sec:authorize>
       <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
       	<div class="styled-select">
            <select name="cbodependencia" id="cbodependencia" style="width:670px">
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
                    <th height="30">*Requisición No: </th>
                    <td align="left"><table width="701" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="244"><input type="text" value="" id="txtrequisicion" class="input" style="width:150px" maxlength="16" onBlur="upperCase(this)"/></td>
                        <td width="63" align="right">*Fecha:</td>
                        <td width="122"><input type="text" id="txtfecha"  name="txtfecha" class="input" style="width:100px" maxlength="10" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${xfecha}" />" /></td>
                        <td width="81" align="right">*Tipo:</td>
                        <td width="191">
                        <div class="styled-select">
                            <select name="cbotipo" id="cbotipo" style="width:160px">
                              <option value="0">[Seleccione]</option>
                              <c:forEach items="${tipoRequisicion}" var="item">
                                <option value='<c:out value="${item.Id_TipoRequisicion}"/>' 
                                  <c:if test='${item.Id_TipoRequisicion==idTipoRequisicion}'> selected </c:if>
                                  >
                                  <c:out value='${item.Descripcion}'/>
                                </option>
                              </c:forEach>
                            </select>
                           </div>
                         </td>
                      </tr>
                    </table></td>
                  </tr>
                  <tr id="fila_contrato">
                    <td align="right" height="30">Número contrato: </td>
                    <td align="left"><input name="txtnumcontrato"  type="text"  class="input" id="txtnumcontrato" value="" maxlength="30" style="width:150px; background:#EAEAEA" onBlur="funciones()" disabled />
                      <img src="../../imagenes/search_16.png" alt="Mostrar presupuesto" name="img_contrato"  id="img_contrato" style="cursor:pointer" align="absmiddle"/> <img src="../../imagenes/cross2.png" id="img_quitar_contrato" width="16" height="16" alt="Quitar contrato" title="Quitar contrato" align="absmiddle" style="cursor:pointer">
                      <input name="CVE_CONTRATO" type="hidden"  id="CVE_CONTRATO" size="8" maxlength="6" readonly="true" value="0" />
                      <input name="CLV_PARBIT" type="hidden"  id="CLV_PARBIT" size="8" maxlength="6" readonly="true" value="" />
                      <input name="CPROYECTO" type="hidden"  id="CPROYECTO" size="8" maxlength="6" readonly="true" value="" />
                      <input name="CCLV_PARTID" type="hidden"  id="CCLV_PARTID" size="8" maxlength="6" readonly="true" value="" />
                    <input name="CCLV_BENEFI" type="hidden"  id="CCLV_BENEFI" size="8" maxlength="6" readonly="true" value="" /></td>
                  </tr>
                  <tr>
                    <td align="right"><div align="right">Notas:&nbsp;</div></td>
                    <td align="left"><textarea id="txtnotas" rows="4" class="textarea" style="width:665px" wrap="virtual" maxlength="400"></textarea></td>
                  </tr>
                  <tr>
                    <td colspan="2">
                    <div id="div_os">
                          <table width="100%" align="left" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <td width="18%" height="30"><div align="right">Tipo de bien:&nbsp;</div></td>
                              <td rowspan="2" ><table width="680" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                  <td width="239" height="30"><input type="text" id="txttipobien"  maxlength="15" class="input" style="width:150px" /></td>
                                  <td width="64" align="right">Marca:&nbsp;</td>
                                  <td width="130"><input type="text" id="txtmarca" class="input" maxlength="15" style="width:100px" /></td>
                                  <td width="82" align="right">Modelo:&nbsp;</td>
                                  <td width="165"><input type="text" id="txtmodelo" maxlength="20" class="input" style="width:150px" /></td>
                                </tr>
                                <tr>
                                  <td height="23" colspan="3"><input type="text" id="txtusuario" maxlength="50" class="input" style="width:404px" /></td>
                                  <td align="right">Num. Invent:&nbsp;</td>
                                  <td><input type="text" id="txtnuminventario" class="input" style="width:150px" /></td>
                                </tr>
                              </table></td>
                            </tr>
                            <tr>
                              <td height="30"><div align="right">Usuario:&nbsp;</div></td>
                            </tr>
                          </table>
                    </div>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="2">
                    <div id="div_os_vehiculo">
                        <table width="100%" align="left" border="0" cellpadding="0" cellspacing="0">
                          <tr class="formulario">
                            <td width="18%" height="30"><div align="right">Placas:&nbsp;</div></td>
                            <td width="82%" ><table width="677" border="0" cellpadding="0" cellspacing="0">
                              <tr>
                                <td width="235"><input type="text" id="txtplacas" maxlength="10" class="input" style="width:150px" /></td>
                                <td width="69" align="right">Color:&nbsp;</td>
                                <td width="128"><input type="text" id="txtcolor" maxlength="15" class="input" style="width:100px" /></td>
                                <td width="83" align="right">Area:&nbsp;</td>
                                <td width="162">
                                <div class="styled-select">
                                <select name="cboarea" id="cboarea" style="width:155px">
                                  <option value="">[Seleccione]</option>
                                  <c:forEach items="${areaRequisicion}" var="item">
                                    <option value='<c:out value="${item.Id_AreaRequisicion}"/>'
                                      <c:if test='${item.Id_AreaRequisicion==idAreaRequisicion}'> selected </c:if>
                                      >
                                      <c:out value='${item.Descripcion}'/>
                                    </option>
                                  </c:forEach>
                                </select>
                                </div>
                                </td>
                              </tr>
                            </table></td>
                          </tr>
                          </table>
                    </div>
                    </td>
                  </tr>
                  
                  <tr>
                    <td colspan="2" align="left">
                    <div id="div_os_prestador">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="18%" height="30" align="right">Prestador del servicio:&nbsp;</td>
                        <td width="963" colspan="3"><table width="689" border="0" cellpadding="0" cellspacing="0">
                          <tr>
                            <td width="434"><input type="text" id="txtprestadorservicio" class="input" style="width:404px"/></td>
                            <td width="82" align="right">Concurso:</td>
                            <td width="173"><input type="text" id="txtconcurso" class="input" style="width:150px" /></td>
                          </tr>
                        </table></td>
                      </tr>
                      </table>
                    </div>
                    </td>
                    </tr>
                  
                  <tr>
                    <td colspan="2">
                    <div id="div_os_presupuesto">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td height="30" align="right">Número de Vale:&nbsp;</td>
                        <td width="465" rowspan="3"><table width="894" border="0" cellpadding="0" cellspacing="0">
                          <tr>
                            <td height="30" colspan="5"><div class="buttons tiptip"><input name="txtvale"  type="text"  class="input" id="txtvale" value="" maxlength="30" style="width:150px;" onBlur="comprobarVale()"/>
                            <img src="../../imagenes/search_16.png" border="0" name="img_vale" style="cursor:pointer" id="img_vale" align="absmiddle"/></a>
                            <input name="CVE_VALE" type="hidden"  id="CVE_VALE" size="8" maxlength="6" value="0" /></div></td>
                          </tr>
                          <tr>
                            <td height="30"><input type="text" class="input" id="txtproyecto" style="width:150px" size="20" maxlength="4"/></td>
                            <td align="right">*Partida:</td>
                            <td><input type="text" id="txtpartida" name="txtpartida" class="input" style="width:150px" maxlength="4"  onKeyPress=" return keyNumbero( event );"/></td>
                            <td align="right">*Mes:</td>
                            <td>
                            <div class="styled-select">
                            <select name="cbomes" class="comboBox" id="cbomes" style="width:155px">
                              <option value="0">[Seleccione]</option>
                              <c:forEach items="${mesRequisicion}" var="item" varStatus="status">
                                <option value="<c:out value='${item.ID_MES}'/>" >
                                  <c:out value="${item.DESCRIPCION}"/>
                                </option>
                              </c:forEach>
                              </select>
                              <img id="img_presupuesto" src="../../imagenes/search_16.png" style="cursor:pointer" align="absmiddle" /></td>
                                </div>
                          </tr>
                          <tr>
                            <td width="152" height="30"><input type="text" id="txtpresupuesto" class="input" style="text-align:right; width:150px" disabled="disabled" /></td>
                            <td width="102" align="right">Disponible:&nbsp;</td>
                            <td width="152"><input type="text" id="txtdisponible" class="input" style="text-align:right; width:150px" disabled="disabled" /></td>
                            <td width="108" align="right">&nbsp;</td>
                            <td width="380">&nbsp;</td>
                          </tr>
                        </table></td>
                      </tr>
                      <tr>
                        <td height="30" width="18%" align="right">*Programa:&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="18%" height="30" align="right">Presupuesto:&nbsp;</td>
                      </tr>
                      <tr id="fila_disponibleVale">
                        <td height="30" align="right">Total del Vale:&nbsp;</td>
                        <td><table width="850" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="153"><input type="text" class="input" id="txtdisponiblevale" style="width:150px; text-align:right;" size="20" disabled  maxlength="4"/></td>
                            <td width="101" align="right">Descontado Vale:</td>
                            <td width="298"><input type="text" id="txtcomprobadovale" name="txtcomprobadovale" disabled class="input" style="width:150px; text-align:right;" maxlength="4"  onKeyPress=" return keyNumbero( event );"/></td>
                            <td width="298">&nbsp;</td>
                          </tr>
                        </table></td>
                      </tr>
                      </table>
                    
                    </div>
                    </td>
                  </tr>
                  <tr>
                    <td height="45">&nbsp;</td>
                    <td align="left" >
                        <table width="500" border="0" cellspacing="0" cellpadding="0">
                           <tr>
                             <td width="169"><button name="cmdcerrar" id="cmdcerrar" type="button" class="button red middle" ><span class="label" style="width:140px">Cerrar</span></button></td>
                             <td width="331"> <button name="cmdguardarequisicion" id="cmdguardarequisicion" type="button" class="button blue middle" ><span class="label" style="width:140px">Guardar</span></button></td>
                           </tr>
                         </table>
                    </td>
                  </tr>
                </table>
		    </div>
<div id="fragment-conceptos" align="left">
                <div id="div_conceptos">
                   <table align="left" border="0" cellpadding="0" cellspacing="0" class="formulario" width="100%">
                    <tr>
                      <td colspan="4">&nbsp;<strong>Nota:</strong> La información marcada con (*) es requerida.</td>
                     </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td colspan="3">
                      <input name="ID_REQ_MOVTO" type="hidden" id="ID_REQ_MOVTO" value="0" />
                      <input name="ID_ARTICULO" type="hidden" id="ID_ARTICULO" value="0" />
                      <input name="GRUPO" type="hidden" id="GRUPO" value="" />
                      <input name="SUBGRUPO" type="hidden" id="SUBGRUPO" value="" />
                      <input name="CLAVE" type="hidden" id="CLAVE" value="" />
                      <input name="REQ_CONS" type="hidden" id="REQ_CONS" value="" />
                      <input type="hidden" id="CONSECUTIVO_MOVTO" value="0" />
                      </td>
                    </tr>
                    <tr>
                      <td width="5%" height="30"><div align="right">*Artículo/Producto/Servicio:&nbsp;</div></td>
                      <td width="16%">
                        <input name="txtproducto" type="text" class="input" id="txtproducto" style="width:400px" value="" />
                        <img id="img_producto" src="../../imagenes/search_16.png" align="absmiddle" border="0" style="cursor:pointer"/></td>
                        <td width="4%">*Precio estimado:</td>
                      <td width="16%"><input name="txtprecioestimado" type="text" class="input" id="txtprecioestimado" style="width:150px; padding-right:5px; text-align:right" value="" 
                      <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_CAMBIAR_PRECIO_REQUISICIONES">
                      </sec:authorize>/></td>
                    </tr>
                    <tr>
                      <td height="30"><div align="right">*Unidad de Medida:&nbsp;</div></td>
                      <td><input type="text" value="" id="txtunidadmedida" class="input" style="width:400px" />
                      <input name="CVE_UNIDAD_MEDIDA" type="hidden" id="CVE_UNIDAD_MEDIDA" value="" /></td>
                      <td>*Cantidad:</td>
                      <td><input type="text" value="" id="txtcantidad" class="input" style="width:150px;padding-right:5px; text-align:right" /></td>
                    </tr>
                    <tr>
                      <td><div align="right">*Descripción:&nbsp;</div></td>
                      <td><textarea id="txtdescripcion" name="txtdescripcion" rows="4" class="textarea" wrap="virtual" maxlength="300" style="width:400px"></textarea></td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                     </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td><table width="400" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td width="128"><button name="cmdnuevoconcepto" id="cmdnuevoconcepto" type="button" class="button red middle" ><span class="label" style="width:100px">Nuevo lote</span></button></td>
                          <td width="272"><button name="cmdguardarconcepto" id="cmdguardarconcepto" type="button" class="button blue middle" ><span class="label" style="width:100px">Guardar lote</span></button></td>
                        </tr>
                      </table></td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td colspan="3">
                         <table width="100%" border="0"  align="left" cellpadding="0" cellspacing="0" class="listas" id="listasConceptos">
                        <thead>
                          <tr >
                            <th width="2%" height="20"  align="center"><img src="../../imagenes/cross.png" alt="Eliminar conceptos seleccionados" width="16" height="16" onClick="eliminarMovimientos()" style="cursor:pointer"></th>
                            <th width="3%"  align="center">Lote</th>
                            <th width="9%" align="center">Cantidad</th>
                            <th width="12%" align="center">Unidad de medida</th>
                            <th width="52%" align="center">Descripción<input type="hidden" id="TOTAL_CONCEPTOS" value="0"></th>
                            <th width="7%" align="center"> En Pedido</th>
                            <th width="11%" align="center"><input type="hidden" value="0" id="IMPORTE_TOTAL"/>Importe</th>
                            <th width="4%"  align="center">&nbsp;</th>
                            </tr>
                       
                          </thead>
                        <tbody>
                          </tbody>   
                         
                        </table>
           		 	 </td>
                     </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td colspan="3"> <table width="380" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td><div class="buttons tiptip"><button name="cmdreenumerar" id="cmdreenumerar" title="Modifique el orden numérico de lotes o reenumere" type="button" class="button red middle" ><span class="label" style="width:100px">Reenumerar ...</span></button></div></td>
                                  <td><div class="buttons tiptip"><button name="cmdimportar" id="cmdimportar" title="Agregue lotes nuevos desde otra Requisición" type="button" class="button red middle" ><span class="label" style="width:100px">Importar ...</span></button></div></td>
                                  <td><div class="buttons tiptip"><button name="cmdenviarlotes" id="cmdenviarlotes" title="Envie lotes a otra Requisición" type="button" class="button red middle" ><span class="label" style="width:100px">Exportar ...</span></button></div></td>
                                </tr>
                              </table></td>
                    </tr>
                  </table>
        </div>
	</div>
</div>
</form>
<form  action="../reportes/requisicion.action" method="POST" id="forma" name="forma">
<input type="hidden" name="claveRequisicion" id="claveRequisicion" >
</form>
</body>
</html>