<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Listado de Requisiciones</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="../../include/css/bootstrap-3.3.7.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css">
<link rel="stylesheet" href="../../include/css/jquery.alertable.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap-select.css" type="text/css">
<script type="text/javascript" src="../../include/js/jquery-2.2.1.min.js"></script>
<script type="text/javascript" src="../../include/js/bootstrap-3.3.7.js"></script>
<script type="text/javascript" src="../../include/js/bootstrap-select.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/jquery.alertable.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery.alerts.js"></script>
<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js" type="text/css" />
<script type="text/javascript" src="../../dwr/interface/controladorListadoRequisicionesRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="lst_req_total.js?x=<%=System.currentTimeMillis()%>"> </script>
<link rel="stylesheet" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" type="text/css" />
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />


<style type="text/css">
a:link {
	text-decoration: none;
}
a:visited {
	text-decoration: none;
}
a:hover {
	text-decoration: underline;
}
a:active {
	text-decoration: none;
}
</style>
</head>
<body  >
<form  action="lst_req_total.action" class="form-horizontal" method="post" id="forma" name="forma">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" name="claveRequisicion" id="claveRequisicion" >

 <div class="row col-md-offset-2">
          <h1 class="h1-encabezado"> Requisiciones - Listado de Requisiciones, Ordenes de Trabajo y Ordenes de Servicio</h1>
 </div>  
<div class="well">
    
    
    <div class="form-group">
      <label class="control-label col-sm-1" for="email">Unidad:</label>
      <div class="col-sm-4">
        <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
			      <c:out value="${nombreUnidad}"/>
			      <input type="hidden" name="dependencia" id="dependencia" value="<c:out value='${idUnidad}'/>">
			      <input type="hidden" name="todo" id="todo" value="0">
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
				<input type="hidden" name="todo" id="todo" value="1">
				
			    	<select name="dependencia" id="dependencia" class="form-control input-sm">
			               <option value="0" <c:if test='${item.ID==0}'> selected </c:if>>[Todas las Unidades Administrativas]</option>
			    		<c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
			                <option value='<c:out value="${item.ID}"/>' 
			                <c:if test='${item.ID==idUnidad}'> selected </c:if>>
			                <c:out value='${item.DEPENDENCIA}'/>
			               </option>
			   			</c:forEach>
			   		</select>
			</sec:authorize>
		</div>
		<!--
		<div class="col-md-5 col-md-offset-2">
			<input type="checkbox" name="status" id="status" class="checkbox-inline" value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>>&nbsp;Edición
			<input type="checkbox" name="status" id="status" class="checkbox-inline"value="1" <c:if test="${fn:contains(status,'1')}" >checked</c:if>>&nbsp;Cerrado
			<input type="checkbox" name="status" id="status" class="checkbox-inline" value="2" <c:if test="${fn:contains(status,'2')}" >checked</c:if>>&nbsp;En Proceso</td>
			<input name="status" type="checkbox" id="status" class="checkbox-inline" value="4" <c:if test="${fn:contains(status,'4')}" >checked</c:if>>&nbsp;Canceladas
	  		<input name="status" type="checkbox" id="status" class="checkbox-inline" value="5" <c:if test="${fn:contains(descripcion_estatus,'finiquitado')}">checked</c:if>>&nbsp;Finiquitadas
		</div>  -->
	</div>
	 <div class="form-group">
      <label class="control-label col-sm-1" for="cbotipogasto">Tipo de gasto:</label>
      <div class="col-sm-4">          
        <select name="cbotipogasto" id="cbotipogasto " class="form-control input-sm">
      		<option value="0"> [Todos los tipos de gastos]
        		<c:forEach items="${tipodeGasto}" var="item" varStatus="status">
        		<option value='<c:out value="${item.ID}"/>'
				<c:if test='${item.ID==cbotipogasto}'> selected </c:if>>
  				<c:out value='${item.RECURSO}'/>
        	</option>
      			</c:forEach>
    	</select>
      </div>
      <label for="cboFilterStatus" class="control-label col-md-offset-2 col-sm-1">Seleccione un Estatus</label>
      <div class="col-sm-2">
	  	<select class="selectpicker form-control input-sm m-b" name="cboFilterStatus" id="cboFilterStatus" data-live-search="true" multiple data-style="btn-primary">
        	<optgroup label="">
        	
            	<option value="9" <c:if test = "${fn:contains(status, '9')}"> selected </c:if>>[Todas los Estatus]</option>
              	</optgroup>
              	<optgroup label="">
                      <option value="0"  <c:if test = "${fn:contains(status, '0')}"> selected </c:if>>EDICION</option>
                      <option value="1"  <c:if test = "${fn:contains(status, '1')}"> selected </c:if>>CERRADO</option>
                       <option value="2"  <c:if test = "${fn:contains(status,'2')}"> selected </c:if>>PROCESO</option>
                      <option value="4"  <c:if test = "${fn:contains(status, '4')}"> selected </c:if>>CANCELADO</option>
                      <option value="5"  <c:if test = "${fn:contains(status, '5')}"> selected </c:if>>FINIQUITADO</option>
              	</optgroup>
          </select>
      </div>
 	</div>
	<div class="form-group">
      <label class="control-label col-sm-1" for="email">Beneficiario:</label>
      <div class="col-sm-4">
        <input type="text" id="txtprestadorservicio" name="txtprestadorservicio" class="form-control input-sm" value="<c:out value='${txtprestadorservicio}'/>"/>
		<input type="hidden" id="CVE_BENEFI" name="CVE_BENEFI" value="<c:out value='${CVE_BENEFI}'/>" />
      </div>
       <div class="col-sm-4 col-md-offset-2">
      	<button type="button" class="btn btn-buscar btn-md" name="btnBuscar" id="btnBuscar" onClick="getListaReq()">Buscar</button>
		<button type="button" class="btn btn-imprimir btn-md"  name="cmdpdf2" id="cmdpdf2">Imprimir...</button>
      </div>
    </div> 
	<div class="form-group">
      <label class="control-label col-sm-1" for="email">Fecha:</label>
      <div class="col-sm-2">
        <input placeholder="Desde" name="fechaInicial" type="text" id="fechaInicial" maxlength="10" class="form-control input-sm" value="<c:out value='${fechaInicial}'/>">
	  </div>
      <div class="col-sm-2">
      	<input placeholder="Hasta" name="fechaFinal" type="text" id="fechaFinal" class="form-control input-sm" value="<c:out value='${fechaFinal}'/>"  maxlength="10">
      </div>
    </div> 
	<div class="form-group">
      <label class="control-label col-sm-1" for="email">Requisicion:</label>
      <div class="col-sm-2">
      	<input placeholder="No. Requisicion" name="txtrequisicion" type="text" id="txtrequisicion" class="form-control input-sm" value="<c:out value='${txtrequisicion}'/>">
      </div>
      <div class="col-sm-2">
      		<select name="cbotipo" id="cbotipo" class="form-control input-sm">
            	<option value="0">[Todos los tipos]</option>
      	        	<c:forEach items="${tipoRequisicion}" var="item">
        	      	<option value='<c:out value="${item.Id_TipoRequisicion}"/>' 
            	    <c:if test='${item.Id_TipoRequisicion==tipo}'> selected </c:if>>
                	<c:out value='${item.Descripcion}'/>
                </option>
                </c:forEach>
             </select>
      </div> 
	</div>
	<div class="form-group">
	      <label class="control-label col-sm-1" for="email">Proyecto:</label>
	      <div class="col-sm-2">
	      	<input placeholder="Proyecto" name="txtproyecto" type="text" id="txtproyecto" class="form-control input-sm" value="<c:out value='${txtproyecto}'/>">
	      </div>
	      <label for="txtpartida" class="sr-only control-label">Partida:</label>
	      <div class="col-sm-2">
	      	<input placeholder="Partida" name="txtpartida" type="text" id="txtpartida" class="form-control input-sm" onKeyPress="return keyNumbero(event);" value="<c:out value='${txtpartida}'/>">
	      </div>
	</div> 
</div>
<!-- Large modal -->

<div class="center"><button data-toggle="modal" data-target="#squarespaceModal" class="btn btn-primary center-block">Click Me</button></div>


<!-- line modal -->
<div class="modal fade" id="squarespaceModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
  <div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
			<h3 class="modal-title" id="lineModalLabel">My Modal</h3>
		</div>
		<div class="modal-body">
			
            <!-- content goes here -->
			<form>
              <div class="form-group">
                <label for="exampleInputEmail1">Email address</label>
                <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Enter email">
              </div>
              <div class="form-group">
                <label for="exampleInputPassword1">Password</label>
                <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
              </div>
              <div class="form-group">
                <label for="exampleInputFile">File input</label>
                <input type="file" id="exampleInputFile">
                <p class="help-block">Example block-level help text here.</p>
              </div>
              <div class="checkbox">
                <label>
                  <input type="checkbox"> Check me out
                </label>
              </div>
              <button type="submit" class="btn btn-default">Submit</button>
            </form>

		</div>
		<div class="modal-footer">
			<div class="btn-group btn-group-justified" role="group" aria-label="group button">
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-default" data-dismiss="modal"  role="button">Close</button>
				</div>
				<div class="btn-group btn-delete hidden" role="group">
					<button type="button" id="delImage" class="btn btn-default btn-hover-red" data-dismiss="modal"  role="button">Delete</button>
				</div>
				<div class="btn-group" role="group">
					<button type="button" id="saveImage" class="btn btn-default btn-hover-green" data-action="save" role="button">Save</button>
				</div>
			</div>
		</div>
	</div>
  </div>
</div>  
<!-- table table-hover table table-condensed table-striped -->
<div class="container-fluid">
<table width="95%" class="table table-hover table-sm" align="center" id="listaRequisiciones" cellpadding="0" cellspacing="0">
 <thead class="thead-inverse">
  <tr>
    <th width="4%"><input type="checkbox" name="todos" id="todos"></th>
    <th width="9%" height="20">Número</th>
    <th width="9%"> Fecha</th>
    <th width="7%">Estado</th>
    <th width="7%">Tipo</th>
    <th width="52%">Notas</th>
    <th width="10%">Programa / Partida</th>
    <th width="7%">Importe</th>
    <th width="7%">Opciones</th>
  </tr>
   </thead>   
<tbody>  
<c:set var="cont" value="${0}" />
<c:forEach items="${requisicionesUnidad}" var="item" varStatus="status"> 
  <tr id='f<c:out value="${cont}"/>'>
  	
    <td align="center" style="border-right:none">
    <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_REQUISICIONES">
        <c:if test='${item.STATUS==1&&(item.TIPO==2||item.TIPO==3||item.TIPO==4||item.TIPO==5)}'>
            <input alt="<c:out value='${item.NUM_REQ}'/>" type="checkbox" id="chkrequisiciones" name="chkrequisiciones" value="<c:out value='${item.CVE_REQ}'/>"/>
        </c:if>
    </sec:authorize>
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_REQUISICIONES">
    	<c:if test='${item.STATUS==0}'>
            <input alt="<c:out value='${item.NUM_REQ}'/>" type="checkbox" id="chkrequisiciones" name="chkrequisiciones" value="<c:out value='${item.CVE_REQ}'/>"/>
        </c:if>
        <c:if test='${item.STATUS==1&&(item.TIPO==2||item.TIPO==3||item.TIPO==4||item.TIPO==5)}'>
            <input alt="<c:out value='${item.NUM_REQ}'/>" type="checkbox" id="chkrequisiciones" name="chkrequisiciones" value="<c:out value='${item.CVE_REQ}'/>"/>
        </c:if>
    </sec:authorize>
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_APERTURAR_REQUISICIONES">
    	<c:if test='${item.STATUS==1&&(item.TIPO==1||item.TIPO==7||item.TIPO==8)}'><input alt="<c:out value='${item.NUM_REQ}'/>" type="checkbox" id="chkrequisiciones" name="chkrequisiciones" value="<c:out value='${item.CVE_REQ}'/>"/></c:if>
        <c:if test='${item.STATUS==2&&(item.TIPO==1||item.TIPO==7||item.TIPO==8)}'><input alt="<c:out value='${item.NUM_REQ}'/>" type="checkbox" id="chkrequisiciones" name="chkrequisiciones" value="<c:out value='${item.CVE_REQ}'/>"/></c:if>
    </sec:authorize>
    </td>
    <!--border="right" -->
    <td style="border-right:none" align="center"><sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_REQUISICIONES"><a href="javascript:subOpAdm('req', <c:out value='${item.CVE_REQ}'/>, <c:out value='${item.CVE_PERS}'/>)"></sec:authorize><input type="hidden" id="NUM_REQ<c:out value='${item.CVE_REQ}'/>" value="<c:out value='${item.NUM_REQ}'/>"> <c:out value='${item.NUM_REQ}'/><sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_SUBMENU_ESPECIAL_EN_LISTADO_REQUISICIONES"></a></sec:authorize></td>
    <td align="center" style="border-right:none"><c:out value='${item.FECHA}'/></td>
    <td align="center" style="border-right:none"><c:out value='${item.DESCRIPCION_ESTATUS}'/></td>
    <td align="center" style="border-right:none"><c:out value='${item.TIPO_REQ}'/></td>
    <td style="border-right:none"><c:out value='${item.OBSERVA}'/></td>
    <td align="center" style="border-right:none"><c:if test='${item.N_PROGRAMA!=NULL}'><c:out value='${item.N_PROGRAMA}'/>&nbsp;/&nbsp;<c:out value='${item.CLV_PARTID}'/></c:if>&nbsp;</td>
    <td align="right" style="border-right:none"><c:if test='${item.IMPORTE>0}'><fmt:formatNumber value="${item.IMPORTE}"  pattern="#,###,###,##0.00" /></c:if><c:if test='${item.IMPORTE<1}'><fmt:formatNumber value="${item.IMPORTE2}"  pattern="#,###,###,##0.00" /></c:if></td>
    <td align="center" style="border-right:none"><img style="cursor:pointer" src="../../imagenes/pdf.gif" alt="Ver Documento en PDF" border="0" width="14" height="16" onClick="getRequisicion(<c:out value='${item.CVE_REQ}'/>)">
    <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_SOLO_IMPRESION_GENERAL">
        <c:if test='${item.STATUS==0||item.STATUS==1||item.STATUS==2}'>
        <img src="../../imagenes/page_white_edit.png" alt="Editar / Abrir" style="cursor:pointer" onClick="editarRequisicion(<c:out value='${item.CVE_REQ}'/>, <c:out value='${item.STATUS}'/>)">
        </c:if>
    </sec:authorize>
    <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_SOLO_IMPRESION_GENERAL">
        <c:if test='${item.STATUS!=0&&item.STATUS!=1&&item.STATUS!=2}'>
        <img src="../../imagenes/page_gray_edit.png" >
        </c:if>
        <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_REQUISICIONES">
            <c:if test='${item.STATUS==1 || item.STATUS==0 || item.STATUS==3}'>
                <img style="cursor:pointer" src="../../imagenes/cross.png" title="Cancelar Requisición" border="0" width="16" height="16" onClick="cancelarRequisicion(<c:out value='${item.CVE_REQ}'/>)">     
            </c:if>
            <c:if test='${item.STATUS!=1&&item.STATUS!=0&&item.STATUS!=3}'>
                <img src="../../imagenes/cross2.png" border="0" width="16" height="16">     
            </c:if>
    	</sec:authorize>
        <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_REQUISICIONES">
            <c:if test='${(item.STATUS==0 || item.STATUS==1 || item.STATUS==3) && (item.TIPO>1&&item.TIPO<4||(item.TIPO==7||item.TIPO==8))}'>
                    <img style="cursor:pointer" src="../../imagenes/cross.png" title="Cancelar OT/OS" border="0" width="16" height="16" onClick="cancelarRequisicion(<c:out value='${item.CVE_REQ}'/>)">     
            </c:if>
            <c:if test='${(item.STATUS==0 || item.STATUS==1 || item.STATUS==2 || item.STATUS==3 || item.STATUS==4 || item.STATUS==5)&&(item.TIPO==1)}'>
                    <img style="cursor:pointer" src="../../imagenes/cross2.png" border="0" width="16" height="16" >     
            </c:if>
            <c:if test='${(item.STATUS==4)&&(item.TIPO>1)}'>
                    <img style="cursor:pointer" src="../../imagenes/cross2.png" border="0" width="16" height="16" >     
            </c:if>

        </sec:authorize>
   </sec:authorize>
    </td>
  </tr>
  	<c:set var="cont" value="${cont+1}" /> 
 </c:forEach>
 <c:if test="${fn:length(requisicionesUnidad) > 0}"> 
  <tr>
    <td height="40" colspan="9" align="left"  style="background-color:#FFF">
    <div class="container col-xs-1">
        <table width="389" border="0" cellspacing="0" cellpadding="0">
          <tr>
           <c:if test="${fn:contains(status,'1')||fn:contains(status,'2')}">
            <td width="129" bgcolor="#FFFFFF">
            	<input type="button"  class="btn btn-sm btn-warning" value="Aperturar" name="cmdaperturar" id="cmdaperturar" onClick="aperturarRequisiciones()" title="Apertura para edicion los documentos seleccionados" style="width:100px"/>
				<!--	<button name="cmdaperturar" id="cmdaperturar" onClick="aperturarRequisiciones()" title="Apertura para edicion los documentos seleccionados" type="button" class="btn btn-sm btn-warning" style="width:100px">Aperturar</button>-->
               
            </td>
            </c:if>
            <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_REQUISICIONES">
            <td width="135" bgcolor="#FFFFFF">
            	
                	<input type="button" class="btn btn-sm btn-danger" value="Cancelar" name="cmdcancelarm" id="cmdcancelarm" onClick="cancelacionMultiple()" title="Cancela o elimina los documentos seleccionados" style="width:100px"/>
					<!--<button name="cmdcancelarm" id="cmdcancelarm" onClick="cancelacionMultiple()"  title="Cancela o elimina los documentos seleccionados" type="button" class="btn-sm btn-danger" style="150px">Cancelar</button>-->
               	
           </td>
           </sec:authorize>
            <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_LISTAR_LOTES_DE_REQUISICIONES_EN_EXCEL_ADMON">
            <td width="125" bgcolor="#FFFFFF">
            	<div class="buttons tiptip">
            		<input type="button" class="btn btn-sm btn-primary" value="Agregar a Lista" name="cmdAgregarLista" id="cmdAgregarLista" onclick="agregarReglista()" title="Agrega las Requisicones seleccionadas al listado especial" style="width: 100px"/>
					<!--<button name="cmdAgregarLista" id="cmdAgregarLista" onClick="agregarReqLista()"  title="Agrega las Requisicones seleccionadas al listado especial" type="button" class="btn-info" ><span class="label" style="width:100px">Agregar a lista</span></button>  -->
               	</div>
           </td>
           </sec:authorize>
          </tr>
        </table>
         </div>
     </td>
    </tr>
   </c:if>
  </tbody> 
 
</table>
	<div class="alert alert-info">
		<strong>Total de registros encontrados: <c:out value='${CONTADOR}'/></strong>
	</div>
</div>
</form>
</body>
</html>
