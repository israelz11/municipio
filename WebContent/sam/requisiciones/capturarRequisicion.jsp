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

<link rel="stylesheet" href="../../include/css/bootstrap-3.3.7.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap-select.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/style-tabs.css" type="text/css"/>

<script type="text/javascript" src="../../include/js/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="../../include/js/bootstrap-3.3.7.js"></script>
<script type="text/javascript" src="../../include/js/bootstrap-select.js"></script>
<link rel="stylesheet" href="../../include/css/sweetalert2.css" type="text/css">

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


<script type="text/javascript" src="../../include/css/bootstrap-datetimepicker-master/js/moment-with-locales-2.9.0.js"></script>
<link rel="stylesheet" href="../../include/css/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker-4.15.35.css" type="text/css">
<script type="text/javascript" src="../../include/css/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker-4.15.35.js"></script>


<title>Cambiar contraseña</title>
</head>
<body>
<form name="frmcontraseña" action="" method="get">
<h1 class="h1-encabezado">&nbsp;Requisiciones - Captura de Requisiciones, Ordenes de Trabajo y Ordenes de Servicio</h1>

<div class="col-sm-12">
	<div class="panel with-nav-tabs panel-primary">
		<div class="panel-heading">
	 		<ul class="nav nav-tabs responsive">
	 			<li class="active"><a href="#tab-requisicion" data-toggle="tab">Información general</a></li>
                <li><a href="#tab-conceptos" data-toggle="tab">Lotes</a></li>
         	</ul>
		</div><!--close panel con navtabas-->
		<div class="panel-body">
		 	<div class="tab-content">
		 		<!--fragment-requisicion-->
		 	 	<div class="tab-pane fade in active" id="tab-requisicion">
		 	 			<form class="form-horizontal">
				 	 		 	<div><strong>Nota:</strong> La información marcada con (*) es requerida pestaña 1</div>
			           		<!--Unidad administrativa-->
			                    <div class="row">
			                      <div class="form-group">
			                      	
			                      	<input type="hidden" id="ID_PROYECTO" value="0"/>
									<input type="hidden" id="CVE_REQ" value="<c:out value='${cve_req}'/>"/>
									<input type="hidden" id="CVE_CONCURSO" value="" />
									<input type="hidden" id="CVE_BENEFI" value="0" />
									<input type="hidden" id="MES"/>
									<input type="hidden" name="cbodependencia2" id="cbodependencia2" value='<c:out value="${idUnidad}"/>' />
			                        <div class="control-label col-sm-3 ">*Unidad administrativa:</div>
			                        <div class="col-sm-4 form-group">
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
			                    </div>
			                      <!--Requisición-->
			                  <div class="row" style="border-bottom:10px">
			                    <div class="form-group">
			                        <div class="control-label col-sm-3">*Requisición No:</div>
			                        <div class="col-sm-2">
										<input placeholder="Requisición" type="text" value="" name="txtrequisicion" required title="Se necesita un número de requisición" id="txtrequisicion" class="form-control sm" maxlength="16" onBlur="upperCase(this)"/>
									</div>
									<label class="control-label col-sm-1" for="txtfecha">*Fecha:</label>
									<div class="col-sm-2">
									  	<input placeholder="Fecha" type="text" id="txtfecha"  name="txtfecha" class="form-control sm" maxlength="10" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${xfecha}" />" />
									</div>
									<div class="form-group col-sm-3">&nbsp;</div>
			                    </div>
			                  </div>
			                  <!--*Tipo:-->
			                  <div class="row">
			                    <div class="form-group">
			                        <div class="control-label col-sm-3">*Tipo:</div>
			                        <div class="col-sm-2">
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
									<div class="form-group col-sm-3">&nbsp;</div>
			                    </div>
			                  </div>
			               <!--Notas-->
			                  <div class="row" id="fila_contrato">
			                    <div class="form-group">
			                        <div class="control-label col-sm-3 ">*Número contrato:</div>
			                        <div class="form-group col-sm-5">
			                            <input name="txtnumcontrato"  type="text"  class="form-control" id="txtnumcontrato" value="" maxlength="30" style="width:150px; background:#EAEAEA" onBlur="funciones()" disabled />
				                        <img src="../../imagenes/search_16.png" alt="Mostrar presupuesto" name="img_contrato"  id="img_contrato" style="cursor:pointer" align="absmiddle"/> <img src="../../imagenes/cross2.png" id="img_quitar_contrato" width="16" height="16" alt="Quitar contrato" title="Quitar contrato" align="absmiddle" style="cursor:pointer">
				                        <input name="CVE_CONTRATO" type="hidden"  id="CVE_CONTRATO" size="8" maxlength="6" readonly value="0" />
				                        <input name="CLV_PARBIT" type="hidden"  id="CLV_PARBIT" size="8" maxlength="6" readonly value="" />
				                        <input name="CPROYECTO" type="hidden"  id="CPROYECTO" size="8" maxlength="6" readonly value="" />
				                        <input name="CCLV_PARTID" type="hidden"  id="CCLV_PARTID" size="8" maxlength="6" readonly value="" />
				                      <input name="CCLV_BENEFI" type="hidden"  id="CCLV_BENEFI" size="8" maxlength="6" readonly value="" />
			                        </div>
			                    </div>
			                  </div> 
			                <!--Notas-->
			                  <div class="row">
			                    <div class="form-group">
			                        <div class="control-label col-sm-3 ">*Notas:</div>
			                        <div class="form-group col-sm-5">
			                            <textarea id="txtnotas" rows="3" class="form-control sm" wrap="virtual" maxlength="400"></textarea>
			                        </div>
			                    </div>
			                  </div> 
			 		<div id='ocultando'> <!--Ocultando Paneles-->     
			                <div id="div_os_presupuesto"> <!--div_os_presupuesto-->  
				            <!--Número de Vale-->
				                  <div class="row">
				                    <div class="form-group">
				                        <div class="control-label col-sm-3 ">Número de Vale:</div>
				                        <div class="form-group col-sm-2">
				                            <div class="input-group">
												<input type="text" class="form-control" placeholder="Número de Vale" name="txtvale" id="txtvale" >
												<div class="input-group-btn">
												    <button class="btn btn-info" type="submit"><i class="glyphicon glyphicon-search" onBlur="comprobarVale()"></i></button>
												    <input name="CVE_VALE" type="hidden"  id="CVE_VALE" size="8" maxlength="6" value="0" />
												</div>
											</div>
				                        </div>
				                        <label class="control-label col-sm-1" for="cbomeses">*Mes:</label>
										<div class="col-sm-2">
											<div class="input-group">
												<select name="cbomeses" class="form-control" name="cbomeses" id="cbomeses">
											       	<option value="0">[Seleccione]</option>
											            <c:forEach items="${mesRequisicion}" var="item" varStatus="status">
											            <option value="<c:out value='${item.ID_MES}'/>" >
											            <c:out value="${item.DESCRIPCION}"/>
											        </option>
											            </c:forEach>
												</select>
												<div class="input-group-btn">
													<button type="button" class="btn btn-info" id="cmdpresupuesto" name="cmdpresupuesto" onClick="muestraPresupuesto()">
				      								<span class="glyphicon glyphicon-search"></span>
				    							</button>
				    							</div>
				    						</div>		
										</div><!-- cierra el col-sm-2 del cbomeses -->
									</div>
				                  </div>
				            <!--Programa, Partida -->
				                  <div class="row" style="border-bottom:15px !important;">
				                    <div class="form-group">
				                        <div class="control-label col-sm-3 ">*Programa:</div>
				                         <div class="col-sm-2">
												<input placeholder="Proyecto" type="text" class="form-control sm" name="txtproyecto" id="txtproyecto" maxlength="4"/>
											</div>
											<label class="control-label col-sm-1" for="txtproyecto">*Partida:</label>
											<div class="col-sm-2">
												<input placeholder="Partida" type="text" id="txtpartida" name="txtpartida" class="form-control sm" maxlength="4"  onKeyPress=" return keyNumbero( event );"/>
											</div>
				                     </div>
				                  </div> 
				            <!--Presupuesto, Disponible-->
				                  <div class="row">
				                    <div class="form-group">
				                        <div class="control-label col-sm-3 ">Presupuesto:</div>
				                      		<div class="col-sm-2">
												<input type="text" placeholder="Presupuesto" name="txtpresupuesto" id="txtpresupuesto" class="form-control sm" disabled="disabled"  />
											</div>
											<label class="control-label col-sm-1" for="txtfecha">*Disponible:</label>
											<div class="col-sm-2">
												<input type="text" placeholder="Disponible" id="txtdisponible" name="txtdisponible" class="form-control sm" disabled="disabled" />
											</div>
				                    </div>
				                  </div>
				            <!--Total del Vale-->
				                  <div class="row" id="fila_disponibleVale">
				                    <div class="form-group">
				                        <div class="control-label col-sm-3 ">Total del Vale:</div>
				                      		<div class="col-sm-2">
												<input type="text" placeholder="txtpresupuesto" name="txtpresupuesto" id="txtpresupuesto" class="form-control sm" disabled="disabled" />
											</div>
											<label class="control-label col-sm-1" for="txtfecha">Descontado Vale:</label>
											<div class="col-sm-2">
												<input type="text" placeholder="txtcomprobadovale" id="txtcomprobadovale" name="txtcomprobadovale" disabled class="form-control sm" maxlength="4"  onKeyPress=" return keyNumbero( event );"/>
											</div>
				                    </div>
				                  </div>       
				            </div>  <!--Termina div_os_presupuesto-->  
			                <div id="div_os_vehiculo"><!--div_os_vehiculo-->  
			              		<!--Placas,Color, -->
			                  	<div class="row">
			                    <div class="form-group">
			                        <div class="control-label col-sm-3 ">*Placas:</div>
			                        <div class="form-group col-sm-5">
			                            <div class="col-sm-2">
											<input type="text" placeholder="txtplacas" id="txtplacas" maxlength="10" class="form-control sm" style="width:150px" />
										</div>
										<label class="control-label col-sm-1" for="txtproyecto">*Color:</label>
										<div class="col-sm-2">
											<input type="text" placeholder="txtcolor" id="txtcolor" maxlength="15" class="form-control sm" style="width:100px" />
										</div>
										<label class="control-label col-sm-1" for="cbomeses">*Area:</label>
										<div class="col-sm-2">
											<select name="cboarea" id="cboarea" class="selectpicker form-control input-sm m-b" style="width:155px">
												<option value="">[Seleccione]</option>
												<c:forEach items="${areaRequisicion}" var="item">
												<option value='<c:out value="${item.Id_AreaRequisicion}"/>'
												<c:if test='${item.Id_AreaRequisicion==idAreaRequisicion}'> selected </c:if>>
												<c:out value='${item.Descripcion}'/>
												</option>
												</c:forEach>
											</select>
										</div>
			                        </div>
			                     </div>
			                  </div>   	
			                </div><!--Termina div_os_vehiculo--> 
			                <div id="div_os_prestador"><!--div_os_prestador--> 
			              <!--Beneficiario, concurso -->
			                  <div class="row">
			                    <div class="form-group">
			                     	<div class="control-label col-sm-3 ">Prestador del servicio:</div>
			                        <div class="form-group col-sm-3">
			                        	<select class="selectpicker form-control input-sm" data-live-search="true" style="width:100%" id="cboSearch" name="cboSearch" title="Seleccione un Beneficiario...">
										   	<c:forEach items="${clv_benefi}" var="item" varStatus="status">
									      	<option value='<c:out value="${item.CLV_BENEFI}"/>'
									   	 	<c:if test='${item.CLV_BENEFI==cboSearch}'>selected</c:if>><c:out value='${item.NCOMERCIA}'/>
									      	</option>
											</c:forEach>    
										</select>
									</div>	  
										<label class="control-label col-sm-1" for="txtfecha">*Concurso:</label>
										<div class="col-sm-2">
											<input placeholder="Concurso" type="text" id="txtconcurso" class="form-control sm" style="width:150px" />
										</div>
									
								</div>
			                  </div>   	  	
			                </div><!--Termina div_os_prestador-->  
			                <div id="div_os"><!--div_os--> 
			              <!--Tipo de bien-->
			                  <div class="row">
			                    <div class="form-group">
			                        <div class="control-label col-sm-3 ">Tipo de bien:</div>
			                        <div class="col-sm-2">
			                           	<input type="text" placeholder="Tipo de bien" id="txttipobien"  maxlength="15" class="form-control sm" style="width:150px"/>
			                           </div>
										<!-- <label class="control-label col-sm-1" for="txtproyecto">Marca::</label> -->
										<div class="col-sm-2">
											<input type="text" placeholder="Marca" id="txtmarca" class="form-control sm" maxlength="15" style="width:100px" />
										</div>
										<!-- <label class="control-label col-sm-1" for="cbomeses">Modelo:</label> -->
										<div class="col-sm-2">
											<input type="text" placeholder="Modelo" id="txtmodelo" maxlength="20" class="form-control sm" style="width:150px" />
										</div>
			                     </div>
			                  </div> 
			                  <!--Usuario, Num. Invent-->
			                  <div class="row">
			                    <div class="form-group">
			                        <div class="control-label col-sm-3 ">Usuario:</div>
			                      		<div class="col-sm-2">
											<input type="text" placeholder="Usuario" id="txtusuario" maxlength="50" class="form-control sm"/>
										</div>
										<label class="control-label col-sm-1" for="txtfecha">Num. Invent:</label>
										<div class="col-sm-2">
											<input type="text" placeholder="No. de Inventario" id="txtnuminventario" class="form-control sm" style="width:150px" />
										</div>
			                    </div>
			                  </div>
			                </div>   <!--Termina div_os--> 
			            </div>   <!--Termina id=ocultando-->  
			             <!--Descripción-->
			             		<div class="row">&nbsp;</div>
			                  <div class="row">
			                    <div class="form-group">
			                        <div class="control-label col-sm-3 ">&nbsp;</div>
			                      		<div class="col-sm-2">
											<input type="button" class="btn btn-cerrar" name="cmdcerrar" id="cmdcerrar" value="Cerrar"/>
						       				<input type="button" class="btn btn-success" name="cmdguardarequisicion" id="cmdguardarequisicion" value="Guardar"/>
										</div>
								</div>
			                  </div>  
			          
				 	</form><!--form-horizontal-->
		 	 	</div>
				<!--fragment-conceptos-->
				<div class="tab-pane fade" id="tab-conceptos">
					
					<form class="form-horizontal">
	 	 			<div><strong>Nota:</strong> La información marcada con (*) es requerida pestaña 2</div>
	 	 			<input name="ID_REQ_MOVTO" type="hidden" id="ID_REQ_MOVTO" value="0" />
					<input name="ID_ARTICULO" type="hidden" id="ID_ARTICULO" value="0" />
					<input name="GRUPO" type="hidden" id="GRUPO" value="" />
					<input name="SUBGRUPO" type="hidden" id="SUBGRUPO" value="" />
					<input name="CLAVE" type="hidden" id="CLAVE" value="" />
					<input name="REQ_CONS" type="hidden" id="REQ_CONS" value="" />
					<input type="hidden" id="CONSECUTIVO_MOVTO" value="0" />

					<!--Artículo, Precio-->
	                  <div class="row">
	                    <div class="form-group">
	                        <div class="control-label col-sm-3">*Artículo/Producto/Servicio:&nbsp;</div>
	                      		<div class="col-sm-2">
									 <input name="txtproducto" type="text" class="form-control sm" id="txtproducto" value="" />
								     <img id="img_producto" src="../../imagenes/search_16.png" align="absmiddle" border="0" style="cursor:pointer"/>
								</div>
								<label class="control-label col-sm-1" for="txtfecha">*Precio estimado:</label>
								<div class="col-sm-2">
									<!--<sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_CAMBIAR_PRECIO_REQUISICIONES">
									</sec:authorize>-->
										<input name="txtprecioestimado" type="text" class="form-control sm" id="txtprecioestimado" value=""/>
								</div>
	                    </div>
	                  </div>
    	        <!--Artículo, Precio-->
	                  <div class="row">
	                    <div class="form-group">
	                        <div class="control-label col-sm-3 ">*Unidad de Medida:&nbsp;</div>
	                      		<div class="col-sm-2">
									 <input type="text" value="" id="txtunidadmedida" class="form-control sm" />
								     <input name="CVE_UNIDAD_MEDIDA" type="hidden" id="CVE_UNIDAD_MEDIDA" value="" />
								</div>
								<label class="control-label col-sm-1" for="txtfecha">*Cantidad:</label>
								<div class="col-sm-2">
									<input type="text" value="" id="txtcantidad" class="form-control sm" />
								</div>
	                    </div>
	                  </div>
                <!--Descripción-->
	                  <div class="row">
	                    <div class="form-group">
	                        <div class="control-label col-sm-3 ">*Descripción:&nbsp;</div>
	                      		<div class="col-sm-5">
									<textarea id="txtdescripcion" name="txtdescripcion" rows="4" class="form-control sm" wrap="virtual" maxlength="300"></textarea>
								</div>
						</div>
	                  </div>     
           		<!--Nuevo lote-->
	                  <div class="row">
	                    <div class="form-group">
	                        <div class="control-label col-sm-3 "></div>
	                      		<div class="col-sm-6">
									<button name="cmdnuevoconcepto" id="cmdnuevoconcepto" type="button" class="btn btn-warning" ><span class="label" style="width:100px">Nuevo lote</span></button>    
	                  				<button name="cmdguardarconcepto" id="cmdguardarconcepto" type="button" class="btn btn-success" ><span class="label" style="width:100px">Guardar lote</span></button>    
								</div>
						</div>
	                  </div>         
	        	<!--Botones-->
                  <div class="row">
                    <div class="form-group">
                        <div class="control-label col-sm-2"></div>
                        <div class="col-sm-8">
                        	<table class="table " id="listasConceptos">
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
                        </div>
                    </div>
                  </div>  
				</form><!--form tab2primary-->
			 	 	
		 	 	</div><!--fragment-conceptos-->
			 </div><!--tab-content-->
		</div><!--panel-body-->
	</div><!--Cierra Panel with-nav-->
</div><!--col-12-->




<button name="cmdreenumerar" id="cmdreenumerar" title="Modifique el orden numérico de lotes o reenumere" type="button" class="button red middle" ><span class="label" style="width:100px">Reenumerar ...</span></button>
<button name="cmdimportar" id="cmdimportar" title="Agregue lotes nuevos desde otra Requisición" type="button" class="button red middle" ><span class="label" style="width:100px">Importar ...</span></button>
<button name="cmdenviarlotes" id="cmdenviarlotes" title="Envie lotes a otra Requisición" type="button" class="button red middle" ><span class="label" style="width:100px">Exportar ...</span></button>

  
</form>
<form  action="../reportes/requisicion.action" method="POST" id="forma" name="forma">
<input type="hidden" name="claveRequisicion" id="claveRequisicion" >
</form>
</body>
</html>