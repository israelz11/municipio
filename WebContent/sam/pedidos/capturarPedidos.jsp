<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
    <link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
	<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
    <script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
    <script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
    <script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script> 
    <script type="text/javascript" src="../../dwr/interface/controladorPedidos.js"> </script>
	<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
    <script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>   
    <script type="text/javascript" src="../../dwr/engine.js"> </script>
    <script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
    <script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
    <script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
    <script type="text/javascript" src="../../include/js/toolSam.js"></script>
    <script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
    <script type="text/javascript" src="../../include/js/otros/productos.js"></script>
    <script type="text/javascript" src="capturarPedidos.js"></script>
    <link rel="stylesheet" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" type="text/css" />
    <link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
    
    <link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
	<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
    <script src="../../include/css/jquery.tiptip.js"></script>
    
    <!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
    <script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
    <script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
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
<title>Pedidos - Captura de Pedido de la Requisicion No.</title>
</head>

<body>
<form name="forma" id="forma" method="get" action="../reportes/rpt_pedido.action">
<input type="hidden" name="clavePedido" id="clavePedido"  value="">
<h1>&nbsp;Pedidos - Captura de Pedido de la Requisicion No. <c:out value='${modelo.NUM_REQ}'/></h1>
<div id="tabuladores">
  <ul>
   <li><a href="#fragment-pedidos"><span>Información general</span></a></li>
   <li><a href="#fragment-conceptos"><span>Lotes</span></a></li>
  </ul>
      <div id="fragment-pedidos" align="left">
      	<table border="0" cellspacing="0" cellpadding="0" class="formulario" width="100%">
                   <tr>
                    <td colspan="4">&nbsp;<strong>Nota:</strong> La información marcada con (*) es requerida. 
                    <input type="hidden" id="CVE_REQ"  name="CVE_REQ" value="<c:out value='${cve_req}'/>"/>
                    <input type="hidden" id="CVE_PED" name="CVE_PED" value="<c:out value='${cve_ped}'/>" />
                    <input type="hidden" id="TIPO_REQ" name="TIPO_REQ" value="<c:out value='${map.TIPO}'/>" />
                    </td>
                  </tr>
                  <tr>
                    <th height="30">No. Requisici&oacute;n :</th>
                    <td colspan="3"><div  id="cve_pedido" style="font-weight:bold"><c:out value='${modelo.NUM_REQ}'/></div></td>
                  </tr>
                  <tr>
                    <th height="30">Pedido No:&nbsp;</th>
                    <td width="22%"><div  id="cve_pedido_text" style="font-weight:bold"><c:out value='${map.NUM_PED}'/></div></td>
                    <th colspan="2">&nbsp;</th>
                  </tr>
                  <tr>
                    <th height="30">*Fecha pedido:&nbsp;&nbsp;</th>
                    <td><input name="txtfecha" type="text" class="input" id="txtfecha" style="width:100px" value="<c:if test="${cve_ped==0||cve_ped==NULL}"><fmt:formatDate pattern="dd/MM/yyyy" value="${fecha}" /></c:if><c:out value='${map.FECHA_PED}'/>" size="12" maxlength="10" /></td>
                    <th colspan="2">&nbsp;</th>
                  </tr>
                  <tr>
                    <th height="30">Contrato: </th>
                    <td><input name="txtcontrato" type="text" class="input" id="txtcontrato"  value="<c:out value='${map.CONTRATO}'/>" style="width:100px" maxlength="8" /></td>
                    <th width="20%"><div style="display:none"> Concurso:</div></th>
                    <td width="43%"><div style="display:none"><input name="txtconcurso" type="text" class="input" id="txtconcurso" style="width:100px"  value="<c:out value='${map.CVE_CONCURSO}'/>"  /></div></td>
                  </tr>
                  <tr>
                    <th height="30">*Tiempo de entrega: </th>
                    <td colspan="3"><input name="txtfechaentrega" type="text" style="width:195px" class="input" id="txtfechaentrega"  value="<c:out value='${map.FECHA_ENTREGA}'/>" size="28" maxlength="25" /></td>
                  </tr>
                  <tr>
                    <th height="30">*Beneficiario:&nbsp;</th>
                    <td colspan="3"><input type="text" id="txtprestadorservicio" class="input" style="width:390px" value="<c:out value='${map.NCOMERCIA}'/>"/>
                    <input type="hidden" id="CVE_BENEFI" name="CVE_BENEFI" value="<c:out value='${map.CLV_BENEFI}'/>" /></td>
                  </tr>
                  <tr>
                    <th height="30">*Condiciones de pago:&nbsp;</th>
                    <td colspan="3"><input type="text" id="txtcondicionespago" class="input" style="width:390px" maxlength="25" value="<c:out value='${map.CONDICION_PAGO}'/>"/></td>
                  </tr>
                  <tr>
                    <th height="30">Lugar de entrega:&nbsp;</th>
                    <td colspan="3"><input type="text" id="txtlugarentrega" class="input" style="width:390px" maxlength="40" value="<c:out value='${map.ENTREGA}'/>"/></td>
                  </tr>
                  <tr>
                    <th>Notas:&nbsp;</th>
                    <td colspan="3" height="30"><textarea id="txtdescripcion" name="txtdescripcion" rows="4" class="textarea" wrap="virtual" maxlength="200" style="width:400px"><c:if test="${map.NOTAS!=null}"><c:out value='${map.NOTAS}'/></c:if><c:if test="${map.NOTAS==null}"><c:out value='${observa}'/></c:if></textarea></td>
                  </tr>
                   <tr>
                    <th height="30">Programa:&nbsp;</th>
                    <td colspan="3"><c:out value='${modelo.N_PROGRAMA}'/> - <c:out value='${modelo.DECRIPCION}'/></td>
                  </tr>
                  <tr>
                    <th height="30">Partida:&nbsp;</th>
                    <td colspan="3"><c:out value='${modelo.CLV_PARTID}'/> - <c:out value='${modelo.PARTIDA}'/></td>
                  </tr>
                  <tr>
                    <th height="30">Presupuesto: </th>
                    <td colspan="3" rowspan="2"><table width="80%" border="0" class="listas" cellpadding="0" cellspacing="0" >
                      <tr>
                        <th width="66" height="20"><strong>Mes</strong></th>
                        <th width="89"><strong>Autorizado</strong></th>
                        <th width="92"><strong>Precomprometido</strong></th>
                        <th width="101"><strong>Comprometido</strong></th>
                        <th width="88"><strong>Ejercido</strong></th>
                        <th width="78"><strong>Disponible</strong></th>

                      </tr>
                      <c:forEach items="${presupuesto}" var="item" varStatus="status"> 
                      <tr>
                        <td height="23" align="center"><c:out value='${mesActivo}'/></td>
                        <td height="23" align="right">$<fmt:formatNumber value="${item.AUTORIZADO}"  pattern="#,###,###,##0.00" />&nbsp;</td>
                        <td align="right"><c:if test="${item.PRECOMPROMETIDO>0}"><a title="Mostrar pre-compromisos" href="javascript:mostrarConsultaCompromiso(<c:out value='${modelo.ID_PROYECTO}'/>,'<c:out value='${modelo.N_PROGRAMA}'/>', '<c:out value='${modelo.CLV_PARTID}'/>', <c:out value='${mes}'/>, 'PRECOMPROMETIDO')"></c:if>$<fmt:formatNumber value='${item.PRECOMPROMETIDO}' pattern="###,###,###.00"/></a>&nbsp;</td>
                        <td align="right"><c:if test="${item.COMPROMETIDO>0}"><a title="Mostrar compromisos" href="javascript:mostrarConsultaCompromiso(<c:out value='${modelo.ID_PROYECTO}'/>,'<c:out value='${modelo.N_PROGRAMA}'/>', '<c:out value='${modelo.CLV_PARTID}'/>', <c:out value='${mes}'/>, 'COMPROMETIDO')"></c:if>$<fmt:formatNumber value='${item.COMPROMETIDO}' pattern="###,###,###.00"/></a>&nbsp;</td>
                        <td align="right"><c:if test="${item.EJERCIDO>0}"><a title="Mostrar ejercido" href="javascript:mostrarConsultaCompromiso(<c:out value='${modelo.ID_PROYECTO}'/>,'<c:out value='${modelo.N_PROGRAMA}'/>', '<c:out value='${modelo.CLV_PARTID}'/>', <c:out value='${mes}'/>, 'EJERCIDO')"></c:if>$<fmt:formatNumber value='${item.EJERCIDO}' pattern="###,###,###.00"/></a>&nbsp;</td>
                        <td align="right">$<fmt:formatNumber value="${item.DISPONIBLE}"  pattern="#,###,###,##0.00" />&nbsp;</td>
                      </tr>
                      </c:forEach>
                    </table></td>
                  </tr>
                  <tr>
                    <th height="30">&nbsp;</th>
                  </tr>
                  <tr>
                    <td height="17" colspan="4"></td>
                  </tr>
                  <tr>
                    <td width="15%">&nbsp;</td>
                    <td colspan="3">
                    
                      <table width="350" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td><div class="buttons tiptip">
                            <button name="cmdcerrar" id="cmdcerrar" onClick="" type="button" class="button red middle"><span class="label" style="width:150px" <c:if test="${cve_ped==0||cve_ped==NULL}">disabled</c:if>>Cerrar</span></button>
                          </div></td>
                          <td><div class="buttons tiptip">
                            <button name="cmdguardarPedido" id="cmdguardarPedido" type="button" class="button blue middle" <c:if test="${cve_ped==0}">disabled</c:if>><span class="label" style="width:150px">Guardar</span></button>
                          </div></td>
                        </tr>
                      </table></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td colspan="3">&nbsp;</td>
                  </tr>
                </table>
  </div>
   
     <div id="fragment-conceptos" align="left">
    	<table width="100%" border="0"  align="left" cellpadding="0" cellspacing="0" class="listas" id="listasConceptos">
                        <thead>
                          <tr >
                            <th width="2%" height="20"  align="center"><input type="checkbox" id="checkall" name="checkall" value="0"/></th>
                            <th width="3%" align="center">Lote</th>
                            <th width="6%" align="center">Cantidad</th>
                            <th width="7%" align="center">Unidad</th>
                            <th width="53%" align="center">Descripci&oacute;n del art&iacute;culo</th>
                            <th width="10%" align="center">Precio Area</th>
                            <th width="10%" align="center">Precio Unit.</th>
                            <th width="9%"  align="center">Costo</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:forEach items="${mov}" var="item" varStatus="status"> 
                          <script>
						  <!--
								indices.push(<c:if test="${cve_ped!=''}"><c:out value='${item.ID_PED_MOVTO}'/></c:if><c:if test="${cve_ped==null}"><c:out value='${item.ID_REQ_MOVTO}'/></c:if>);
						  -->
						  </script>
                          <tr >
                            <td  align="center" style="border-right:none"><input type="checkbox" onClick="habilitarConcepto(this.checked, <c:if test="${cve_ped!=''}"><c:out value='${item.ID_PED_MOVTO}'/></c:if><c:if test="${cve_ped==null}"><c:out value='${item.ID_REQ_MOVTO}'/></c:if>)" id="chkconcepto" name="chkconcepto" value="<c:if test="${cve_ped!=null}"><c:out value='${item.ID_PED_MOVTO}'/></c:if><c:if test="${cve_ped==null}"><c:out value='${item.ID_REQ_MOVTO}'/></c:if>"/></td>
                            <td align="center" style="border-right:none"><c:out value='${item.REQ_CONS}'/><input type="hidden" value="${item.REQ_CONS}" id="Lote<c:if test="${cve_ped!=null}"><c:out value='${item.ID_PED_MOVTO}'/></c:if><c:if test="${cve_ped==null}"><c:out value='${item.ID_REQ_MOVTO}'/></c:if>"></td>
                            <td align="center" style="border-right:none"><input type="text" class="input" style="width:90%;text-align:center" onBlur="getTotales()" disabled value="<fmt:formatNumber value="${item.CANTIDAD}"  pattern="##.00" />" id="txtcantidad<c:if test="${cve_ped==null}"><c:out value='${item.ID_REQ_MOVTO}'/></c:if><c:if test="${cve_ped!=null}"><c:out value='${item.ID_PED_MOVTO}'/></c:if>"> </td>
                            <td align="center" style="border-right:none">&nbsp;<c:out value='${item.UNIDMEDIDA}'/></td>
                            <td align="left" style="border-right:none"><strong><c:out value='${item.ARTICULO}'/></strong><textarea rows="3" class="textarea" maxlength="300" style="width:99%" disabled id="txtnota<c:if test="${cve_ped!=null}"><c:out value='${item.ID_PED_MOVTO}'/></c:if><c:if test="${cve_ped==null}"><c:out value='${item.ID_REQ_MOVTO}'/></c:if>"><c:if test="${cve_ped!=null}"><c:out value='${item.DESCRIP}'/></c:if><c:if test="${cve_ped==null}"><c:out value='${item.NOTAS}'/></c:if></textarea></td>
                            <td align="right" style="border-right:none">$ <fmt:formatNumber value="${item.PRECIO_EST}"  pattern="#,###,###,##0.00" />&nbsp;</td>
                            <td align="center" style="border-right:none"><input type="text" onFocus="getTotales()" onBlur="getTotales()" class="input" onKeyPress="getEnter(<c:if test="${cve_ped==null}"><c:out value='${item.ID_REQ_MOVTO}'/></c:if><c:if test="${cve_ped!=null}"><c:out value='${item.ID_PED_MOVTO}'/></c:if>, event)" style="width:90%; text-align:right; padding-right:5px" disabled value='<fmt:formatNumber value="${item.PRECIO_ULT}"  pattern="##########.00" />' id="txtpreciounit<c:if test="${cve_ped==null}"><c:out value='${item.ID_REQ_MOVTO}'/></c:if><c:if test="${cve_ped!=null}"><c:out value='${item.ID_PED_MOVTO}'/></c:if>"></td>
                            <td  align="center"><div align="right" id="divcosto<c:if test="${cve_ped==null}"><c:out value='${item.ID_REQ_MOVTO}'/></c:if><c:if test="${cve_ped!=null}"><c:out value='${item.ID_PED_MOVTO}'/></c:if>">$ 0.00&nbsp;</div></td>
                          </tr>
                           </c:forEach>
                           </tbody>
                          <tr >
                            <td colspan="8"  align="center"><table style="background-color:#FFF" width="100%" height="88" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td width="78%" height="22" align="left" style="border-right:none; border-bottom:none; background:#FFF"><c:if test="${cve_ped!=null}">
                                  <table width="600" border="0" cellspacing="0" cellpadding="0" align="left" bgcolor="#FFFFFF">
                                    <tr>
                                      <td bgcolor="#FFFFFF" width="30%" align="center" id="filaPedido"><div class="buttons tiptip">
                                        <button name="cmdborrarConceptos" id="cmdborrarConceptos" title="Muestra listado de Pedidos disponibles para agregar a los conceptos" type="button" class="button red middle"><span class="label" style="width:180px">Eliminar lotes</span></button>
                                      </div></td>
                                      <td bgcolor="#FFFFFF" width="30%" align="center" id="filaReq"><div class="buttons tiptip">
                                        <button name="cmdenviarPedido" id="cmdenviarPedido" title="Muestra listado de OT/OS disponibles para agregar a los conceptos" type="button" class="button red middle"><span class="label" style="width:180px">Exportar lotes</span></button>
                                      </div></td>
                                      <td bgcolor="#FFFFFF" width="19%" align="center" id="filaReq"><div class="buttons tiptip">
                                        <button name="cmdguardarPedido2" id="cmdguardarPedido2" title="Limpia el formulario para un nuevo concepto" type="button" class="button blue middle"><span class="label" style="width:150px">Guardar</span></button>
                                      </div></td>
                                    </tr>
                                  </table>
                                </c:if></td>
                                <td align="right" width="13%" style="background:#FFF" height="30"><strong>Subtotal:<strong></strong></strong></td>
                                <td width="9%" style="background:#FFF"><div align="right" id="divsubtotal">$ 0.00</div></td>
                              </tr>
                              <tr>
                                <td rowspan="3" align="left" style="background:#FFF"></td>
                                <td height="30" align="right" style="border-left:none; background:#FFF">Descuento:</td>
                                <td style="background:#FFF"><input type="text" id="txtdescuento" class="input" style="width:95%; text-align:right; padding-right:3px" onKeyPress="return keyNumbero(event);" value='<fmt:formatNumber value="${map.DESCUENTO}"  pattern="##########.00" />' onBlur="getTotales()"/></td>
                              </tr>
                              <tr>
                                <td height="30" align="right" style="border-left:none; background:#FFF">
                                <select name="cboiva" class="comboBox" id="cboiva" onChange="getTotales();">
                                  <option value="0" <c:if test="${map.TIPO_IVA==0}"> selected</c:if>>Sin I.V.A</option>
                                  <option value="1" <c:if test="${map.TIPO_IVA==1}">selected</c:if>>Con 16% automatico de I.V.A</option>
                                  <option value="2" <c:if test="${map.TIPO_IVA==2}">selected</c:if>>I.V.A Personalizado</option>
                                </select></td>
                                <td style="background:#FFF"><input type="text" id="txtiva" class="input" style="width:95%; text-align:right; padding-right:3px" value="${map.IVA}" onBlur="getTotalesMasIva()" onKeyPress="return keyNumbero(event);"/></td>
                              </tr>
                              <tr>
                                <td width="13%" height="30" align="right" style="border-left:none; background:#FFF"><strong>Total:</strong></td>
                                <td width="9%" style="background:#FFF"><div align="right" id="divtotal">$ 0.00</div></td>
                              </tr>
                            </table></td>
                          </tr>
                          
                     </table>
    </div>
</div>
</form>
</body>
</html>