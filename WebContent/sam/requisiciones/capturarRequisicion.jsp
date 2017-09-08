<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>

<link rel="stylesheet" href="../../include/css/bootstrap-3.3.7.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/style-tabs.css" type="text/css"/>

<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">


<script type="text/javascript" src="../../include/js/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="../../include/js/bootstrap-3.3.7.js"></script>

<link rel="stylesheet" href="../../include/css/sweetalert2.css" type="text/css">

<!-- 

<link rel="stylesheet" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>

<script type="text/javascript" src="../../include/js/jquery.qtip-1.0/jquery.qtip-1.0.0-rc3.min.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
-->
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../dwr/interface/"> </script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorRequisicion.js"> </script>
<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>   


<script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js?x=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="../../include/js/otros/productos.js"></script>
<script type="text/javascript" src="../../include/js/sweetalert2.js"></script>
<script type="text/javascript" src="capturarRequisicion.js?x=<%=System.currentTimeMillis()%>"></script>
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js?x=<%=System.currentTimeMillis()%>"></script>

<script type="text/javascript" src="../../include/css/bootstrap-datetimepicker-master/js/moment-with-locales-2.9.0.js"></script>
<link rel="stylesheet" href="../../include/css/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker-4.15.35.css" type="text/css">
<script type="text/javascript" src="../../include/css/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker-4.15.35.js"></script>


<title>Cambiar contraseña</title>
</head>
<body>
<form name="frmcontraseña" action="" method="get">
<h1>&nbsp;Requisiciones - Captura de Requisiciones, Ordenes de Trabajo y Ordenes de Servicio</h1>



<!-- Demo para cambiar los nav tabs------------------------------------------------------------------------------------------------------------------------------------------------- -->                   
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div class="panel with-nav-tabs panel-primary">
				    	<div class="panel-heading">
				        	<ul class="nav nav-tabs responsive" id="tab_requi">
				            	<li class="active"><a href="#tab1primary" data-toggle="tab">Información general</a></li>
				                <li><a href="#tab2primary" data-toggle="tab">Lotes</a></li>
				            </ul>
				        </div>
				        <div class="panel-body">
				        	<div class="tab-content">
				            	<div class="tab-pane fade in active" id="tab1primary">
				            		<strong>Nota:</strong> La información marcada con (*) es requerida pestaña <br>
				            		<div class="container"><!-- container 2 -->
				                	<input type="hidden" id="ID_PROYECTO" value="0"/>
						            <input type="hidden" id="CVE_REQ" value="<c:out value='${cve_req}'/>"/>
						            <input type="hidden" id="CVE_CONCURSO" value="" />
						            <input type="hidden" id="CVE_BENEFI" value="0" />
						            <input type="hidden" id="MES"/>
						            <input type="hidden" name="cbodependencia2" id="cbodependencia2" value='<c:out value="${idUnidad}"/>' />
						            						            
						            <div class="form-group row">
										<label class="control-label col-sm-2" for="cbodependencia">* Unidad administrativa:</label>
									    <div class="col-sm-5">
											<sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
										    	<c:out value="${nombreUnidad}"/>
										      	<input type="hidden" name="cbodependencia" id="cbodependencia" value='<c:out value="${idUnidad}"/>' />
										    </sec:authorize>
										    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
										    	<select name="cbodependencia" class="form-control sm" id="cbodependencia">
										    		<option value="0">[Seleccione]</option>
											        	<c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
											            <option value='<c:out value="${item.ID}"/>' 
											            <c:if test='${item.ID==idUnidad}'> selected </c:if>>
											            <c:out value='${item.DEPENDENCIA}'/>
										            </option>
										                </c:forEach>
										         </select>
										       
										      </sec:authorize>    	
										</div>
									</div>
									<div class="form-group row">
										      <label class="control-label col-sm-2" for="txtrequisicion">*Requisición No:</label>
										      <div class="col-sm-2">
										      	<input placeholder="Requisición" type="text" value="" id="txtrequisicion" required class="form-control sm" maxlength="16" onBlur="upperCase(this)"/>
										      </div>
										      	<label class="control-label col-sm-1" for="txtfecha">*Fecha:</label>
										      <div class="col-sm-2">
										      	<input placeholder="Fecha" type="text" id="txtfecha"  name="txtfecha" class="form-control sm" maxlength="10" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${xfecha}" />" />
										      </div>
									</div> 
						            <div class="form-group row">
						            	<label class="control-label col-sm-2" for="cbotipo">* Tipo:</label>
						            	<div class="col-sm-5">
							                <select name="cbotipo" id="cbotipo" class="form-control sm">
						                    	<option value="0">[Seleccione]</option>
						                        	<c:forEach items="${tipoRequisicion}" var="item">
						                            <option value='<c:out value="${item.Id_TipoRequisicion}"/>' 
						                            <c:if test='${item.Id_TipoRequisicion==idTipoRequisicion}'> selected </c:if>>
						                            <c:out value='${item.Descripcion}'/>
						                         </option>
						                            </c:forEach>
						                     </select>
						                </div>     
					                </div>
						            
						            <div class="form-group row">
						            	<label class="control-label col-sm-2" for="txtnotas">*Notas:</label>
						            	<div class="col-sm-5">
					                    	<textarea id="txtnotas" rows="3" class="form-control sm" wrap="virtual" maxlength="400"></textarea>
						            	</div>
						            </div>
						            
						<div id='ocultando'>										
									<div id="div_os_presupuesto">
										<div class="form-group row">
											<label class="control-label col-sm-2" for="txtvale">Número de Vale:</label>
											<div class="col-sm-5">
												<div class="input-group">
													<input type="text" class="form-control" placeholder="Número de Vale" name="txtvale" id="txtvale" >
												      <div class="input-group-btn">
												        <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search" onBlur="comprobarVale()"></i></button>
												        <input name="CVE_VALE" type="hidden"  id="CVE_VALE" size="8" maxlength="6" value="0" />
												      </div>
												 </div>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label col-sm-2" for="txtproyecto">Proyecto:</label>
											<div class="col-sm-2">
												<input placeholder="Proyecto" type="text" class="form-control sm" id="txtproyecto" maxlength="4"/>
											</div>
											<label class="control-label col-sm-1" for="txtproyecto">*Partida:</label>
											<div class="col-sm-2">
												<input placeholder="Partida" type="text" id="txtpartida" name="txtpartida" class="form-control sm" maxlength="4"  onKeyPress=" return keyNumbero( event );"/>
											</div>
											<label class="control-label col-sm-1" for="cbomeses">*Mes:</label>
											<div class="col-sm-2">
												<select name="cbomeses" class="form-control sm" id="cbomeses">
									            	<option value="0">[Seleccione]</option>
										                <c:forEach items="${mesRequisicion}" var="item" varStatus="status">
										                <option value="<c:out value='${item.ID_MES}'/>" >
										                <c:out value="${item.DESCRIPCION}"/>
									                </option>
									                </c:forEach>
									       		 </select>
									       		 <button type="button" class="btn btn-info" id="cmdpresupuesto" name="cmdpresupuesto" onClick="muestraPresupuesto()">
      												<span class="glyphicon glyphicon-search"></span>
    											 </button>
											</div>
											
										</div>
											           <table width="100%" border="0" cellpadding="0" cellspacing="0">
									                      <tr>
									                        <td height="30" align="right">&nbsp;</td>
									                        <td width="465" rowspan="3"><table width="894" border="0" cellpadding="0" cellspacing="0">
									                      
									                          <tr>
									                            <td width="152" height="30"><input type="text" id="txtpresupuesto" class="form-control sm" style="text-align:right; width:150px" disabled="disabled" /></td>
									                            <td width="102" align="right">Disponible:&nbsp;</td>
									                            <td width="152"><input type="text" id="txtdisponible" class="form-control sm" style="text-align:right; width:150px" disabled="disabled" /></td>
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
									                            <td width="153"><input type="text" class="form-control sm" id="txtdisponiblevale" style="width:150px; text-align:right;" size="20" disabled  maxlength="4"/></td>
									                            <td width="101" align="right">Descontado Vale:</td>
									                            <td width="298"><input type="text" id="txtcomprobadovale" name="txtcomprobadovale" disabled class="form-control sm" style="width:150px; text-align:right;" maxlength="4"  onKeyPress=" return keyNumbero( event );"/></td>
									                            <td width="298">&nbsp;</td>
									                          </tr>
									                        </table></td>
									                      </tr>
									                      </table>
									</div>
									<div id="div_os_vehiculo">
									                        <table width="100%" align="left" border="0" cellpadding="0" cellspacing="0">
									                          <tr class="formulario">
									                            <td width="18%" height="30"><div align="right">Placas:&nbsp;</div></td>
									                            <td width="82%" ><table width="677" border="0" cellpadding="0" cellspacing="0">
									                              <tr>
									                                <td width="235"><input type="text" id="txtplacas" maxlength="10" class="form-control sm" style="width:150px" /></td>
									                                <td width="69" align="right">Color:&nbsp;</td>
									                                <td width="128"><input type="text" id="txtcolor" maxlength="15" class="form-control sm" style="width:100px" /></td>
									                                <td width="83" align="right">Area:&nbsp;</td>
									                                <td width="162">
									                               
									                                <select name="cboarea" id="cboarea" class="form-control sm" style="width:155px">
									                                  <option value="">[Seleccione]</option>
									                                  <c:forEach items="${areaRequisicion}" var="item">
									                                    <option value='<c:out value="${item.Id_AreaRequisicion}"/>'
									                                      <c:if test='${item.Id_AreaRequisicion==idAreaRequisicion}'> selected </c:if>
									                                      >
									                                      <c:out value='${item.Descripcion}'/>
									                                    </option>
									                                  </c:forEach>
									                                </select>
									                              
									                                </td>
									                              </tr>
									                            </table></td>
									                          </tr>
									                          </table>
									</div>
									<div id="div_os_prestador">
									     <table width="100%" border="0" cellpadding="0" cellspacing="0">
									     	<tr>
									        	<td width="18%" height="30" align="right">Prestador del servicio:&nbsp;</td>
									            <td width="963" colspan="3"><table width="689" border="0" cellpadding="0" cellspacing="0">
									            	<tr>
									                	<td width="434"><input type="text" id="txtprestadorservicio" class="form-control sm" style="width:404px"/></td>
									                    <td width="82" align="right">Concurso:</td>
									                    <td width="173"><input type="text" id="txtconcurso" class="form-control sm" style="width:150px" /></td>
									                </tr>
									      		</table></td>
									        </tr>
									     </table>
									</div>
									<div id="div_os">
                          <table width="100%" align="left" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <td width="18%" height="30"><div align="right">Tipo de bien:&nbsp;</div></td>
                              <td rowspan="2" ><table width="680" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                  <td width="239" height="30"><input type="text" id="txttipobien"  maxlength="15" class="form-control sm" style="width:150px" /></td>
                                  <td width="64" align="right">Marca:&nbsp;</td>
                                  <td width="130"><input type="text" id="txtmarca" class="form-control sm" maxlength="15" style="width:100px" /></td>
                                  <td width="82" align="right">Modelo:&nbsp;</td>
                                  <td width="165"><input type="text" id="txtmodelo" maxlength="20" class="form-control sm" style="width:150px" /></td>
                                </tr>
                                <tr>
                                  <td height="23" colspan="3"><input type="text" id="txtusuario" maxlength="50" class="form-control sm" style="width:404px" /></td>
                                  <td align="right">Num. Invent:&nbsp;</td>
                                  <td><input type="text" id="txtnuminventario" class="form-control sm" style="width:150px" /></td>
                                </tr>
                              </table></td>
                            </tr>
                            <tr>
                              <td height="30"><div align="right">Usuario:&nbsp;</div></td>
                            </tr>
                          </table>
	</div>
						</div> 	
						</div><!-- cierre del well del container 2 -->										
</div><!-- cierre del tab1primary -->
	                      
		    
	  <div class="tab-pane fade" id="tab2primary">
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
							                        <input name="txtproducto" type="text" class="form-control sm" id="txtproducto" style="width:400px" value="" />
							                        <img id="img_producto" src="../../imagenes/search_16.png" align="absmiddle" border="0" style="cursor:pointer"/></td>
							                        <td width="4%">*Precio estimado:</td>
							                      <td width="16%"><input name="txtprecioestimado" type="text" class="form-control sm" id="txtprecioestimado" style="width:150px; padding-right:5px; text-align:right" value="" 
							                      <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_CAMBIAR_PRECIO_REQUISICIONES">
							                      </sec:authorize>/></td>
							                    </tr>
							                    <tr>
							                      <td height="30"><div align="right">*Unidad de Medida:&nbsp;</div></td>
							                      <td><input type="text" value="" id="txtunidadmedida" class="form-control sm" style="width:400px" />
							                      <input name="CVE_UNIDAD_MEDIDA" type="hidden" id="CVE_UNIDAD_MEDIDA" value="" /></td>
							                      <td>*Cantidad:</td>
							                      <td><input type="text" value="" id="txtcantidad" class="form-control sm" style="width:150px;padding-right:5px; text-align:right" /></td>
							                    </tr>
							                    <tr>
							                      <td><div align="right">*Descripción:&nbsp;</div></td>
							                      <td><textarea id="txtdescripcion" name="txtdescripcion" rows="4" class="form-control sm" wrap="virtual" maxlength="300" style="width:400px"></textarea></td>
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
				                     </div><!-- cierra el tab2primary -->
				                     <div class="col-md-2 col-md-offset-2"></div>
				                     <div class="form-group row">
						            	<!--<button name="cmdcerrar" id="cmdcerrar" type="button" class="button red middle" ><span class="label">Cerrar</span></button>
						            	  <button name="cmdguardarequisicion" id="cmdguardarequisicion" type="button" class="button blue middle" ><span class="label"></span></button>-->
						            	<button type="button" class="btn btn-cerrar" name="cmdcerrar" id="cmdcerrar">Cerrar</button>
						            	<button type="button" class="btn btn-success" name="cmdguardarequisicion" id="cmdguardarequisicion">Guardar</button>
						             </div>
				            </div><!--  cierre tab-content -->
				                
				 	
				        </div><!--cierre panel-body -->
				    </div><!-- Cierre del panel -->
				    
				
			
			</div>
		</div><!-- cierre container-fluid -->
                    
   
</form>
<form  action="../reportes/requisicion.action" method="POST" id="forma" name="forma">
<input type="hidden" name="claveRequisicion" id="claveRequisicion" >
</form>
</body>
</html>