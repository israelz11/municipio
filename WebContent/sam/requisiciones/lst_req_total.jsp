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

<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap-3.3.4.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-2.2.1.min.js"></script>
<link rel="stylesheet" href="../../include/css/css/css3-buttons.css" type="text/css" media="screen">
<link rel="stylesheet" href="../../include/css/tiptip.css" type="text/css"  media="screen">
<script src="../../include/css/jquery.tiptip.js"></script>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorListadoRequisicionesRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/engine.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="lst_req_total.js"> </script>
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
<form  action="lst_req_total.action" method="post" id="forma" name="forma">
<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
<input type="hidden" name="claveRequisicion" id="claveRequisicion" >
<table width="95%" align="center"><tr><td><h1>Requisiciones - Listado de Requisiciones, Ordenes de Trabajo y Ordenes de Servicio</h1></td></tr></table>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario">
  <tr>
    <th height="16">&nbsp;</th>
    <td colspan="4">&nbsp;</td>
    </tr>
  <tr >
    <th  width="11%" height="25">Unidad:</th>
<td>
<sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
      <c:out value="${nombreUnidad}"/>
      <input type="hidden" name="dependencia" id="dependencia" value="<c:out value='${idUnidad}'/>">
      <input type="hidden" name="todo" id="todo" value="0">
</sec:authorize>
<sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
<input type="hidden" name="todo" id="todo" value="1">
<div class="styled-select">
    <select name="dependencia" id="dependencia" style="width:455px;">
               <option value="0" <c:if test='${item.ID==0}'> selected </c:if>>[Todas las Unidades Administrativas]</option>
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
    <td width="15%"><input type="checkbox" name="status" id="status"  value="0" <c:if test="${fn:contains(status,'0')}" >checked</c:if>
    >&nbsp;Edición</td>
    <td width="15%"><input type="checkbox" name="status" id="status"  value="1" <c:if test="${fn:contains(status,'1')}" >checked</c:if>
    >&nbsp;Cerrado</td>
    <td width="15%"><input type="checkbox" name="status" id="status" class="checkbox-inline" value="2" <c:if test="${fn:contains(status,'2')}" >checked</c:if>>&nbsp;En Proceso</td>
  </tr>
  <tr >
    <th height="36" >Tipo de gasto:    
    <td>
    <div class="styled-select">
    <strong>
    <select name="cbotipogasto" id="cbotipogasto" style="width:455px;">
      <option value="0"> [Todos los tipos de gastos]
        <c:forEach items="${tipodeGasto}" var="item" varStatus="status">
        <option value='<c:out value="${item.ID}"/>'
          
        <c:if test='${item.ID==cbotipogasto}'> selected </c:if>
  >
  <c:out value='${item.RECURSO}'/>
        </option>
      </c:forEach>
    </select>
    </strong>
    </div></td>
    <td ><input name="status" type="checkbox" id="status"  value="4" <c:if test="${fn:contains(status,'4')}" >checked</c:if>>&nbsp;Canceladas</td>
    <td ><input name="status" type="checkbox" id="status"  value="5" <c:if test="${fn:contains(descripcion_estatus,'finiquitado')}">checked</c:if>>&nbsp;Finiquitadas</td>
    <td >&nbsp;</td>
  </tr>
  <tr >
    <th height="25" >Beneficiario:<td><input type="text" id="txtprestadorservicio" name="txtprestadorservicio" class="input" style="width:453px" value="<c:out value='${txtprestadorservicio}'/>"/>
      <input type="hidden" id="CVE_BENEFI" name="CVE_BENEFI" value="<c:out value='${CVE_BENEFI}'/>" /></td>
    <td ></td>
    <td ></td>
    <td rowspan="4" ><table width="140" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40">
        <button name="btnBuscar" id="btnBuscar" onClick="getListaReq()" type="button" class="button blue middle"><span class="label" style="width:100px">Buscar</span></button>
        </td>
      </tr>
      <tr>
        <td>
        <div class="buttons tiptip">
        	<button name="cmdpdf" id="cmdpdf" title="Mostrar listado en formato PDF" type="button" class="button red middle"><span class="label" style="width:100px">Imprimir... </span></button>
        </div>
        </td>
      </tr>
    </table><br/><br/></td>
  </tr>
  <tr >
    <th height="31" >Adicional: <td>
    <div class="styled-select">
      <select name="cboconOP" id="cboconOP" style="width:455px;">
        <option value="0" <c:if test='${0==cboconOP}'> selected </c:if>>[Todas las opciones]</option>
        <option value="1" <c:if test='${1==cboconOP}'> selected </c:if>>Con Ordenes de Pago</option>
        <option value="2" <c:if test='${2==cboconOP}'> selected </c:if>>Sin Ordenes de Pago</option>
      </select>
      </div>
    </td>
    <td colspan="2" ><input type="checkbox" name="verUnidad" id="verUnidad" value="1"  <c:if test='${verUnidad==1}'>  checked </c:if> 
      >Incluir documentos de  la Unidad</td>
  </tr>
  <tr >
    <th height="25" >Por fecha de :
    <td><table width="458" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="94"><input name="fechaInicial" type="text" id="fechaInicial" maxlength="10" style="width:70px" value="<c:out value='${fechaInicial}'/>"></td>
        <td width="48">&nbsp;<strong>Hasta</strong></td>
        <td width="104"><input name="fechaFinal" type="text" id="fechaFinal" value="<c:out value='${fechaFinal}'/>"  maxlength="10" style="width:70px"></td>
        <td width="49"><strong>Tipo:</strong></td>
        <td width="163"><strong>
          <div class="styled-select">
            <select name="cbotipo" id="cbotipo" style="width:160px">
              <option value="0">[Todos los tipos]</option>
              <c:forEach items="${tipoRequisicion}" var="item">
                <option value='<c:out value="${item.Id_TipoRequisicion}"/>' 
                <c:if test='${item.Id_TipoRequisicion==tipo}'> selected </c:if>
                >
                <c:out value='${item.Descripcion}'/>
                </option>
                </c:forEach>
              </select>
            </div>
          </strong></td>
        </tr>
    </table></td>
    <td colspan="2">
    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_LISTAR_LOTES_DE_REQUISICIONES_EN_EXCEL_ADMON">
        <input type="checkbox" name="chklistar" id="chklistar" value="1"  <c:if test='${chklistar==1}'>  checked </c:if> 
          >
          Consultar solo Requisiciones agregadas en listado
     </sec:authorize>
    </td>
    </tr>
  <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODOS_LOS_DOCUMENTOS_DE_LA_UNIDAD">
  <tr >
    <th height="25" >Núm.  Requisición:</th>
    <td height="25" colspan="3" ><table width="631" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="173"><input name="txtrequisicion" type="text" id="txtrequisicion" maxlength="50" style="width:150px" value="<c:out value='${txtrequisicion}'/>"></td>
        <td width="68"><strong>Programa:</strong></td>
        <td width="86"><input name="txtproyecto" type="text" id="txtproyecto" maxlength="4" style="width:70px" value="<c:out value='${txtproyecto}'/>"></td>
        <td width="56"><strong>Partida:</strong></td>
        <td width="248"><input name="txtpartida" type="text" id="txtpartida" maxlength="4" style="width:70px" onKeyPress="return keyNumbero(event);" value="<c:out value='${txtpartida}'/>"></td>
        </tr>
    </table></td>
    </tr>
   <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_LISTAR_LOTES_DE_REQUISICIONES_EN_EXCEL_ADMON"> 
   <input type="hidden" value="1" id="REPORTE_ESPECIAL_2">
  <tr >
    <th height="25" >Requisiciones en lista:</th>
    <td height="25" colspan="3" ><textarea name="txtlistado" rows="5" wrap="virtual" class="textarea" id="txtlistado" style="width:455px"><c:out value='${txtlistado}'/></textarea></td>
    <td height="25" >&nbsp;</td> 
  </tr>
  </sec:authorize>
  <tr >
    <td height="25" ></td>
    <td height="25" colspan="3" ></td>
    <td height="25" >&nbsp;</td>
  </tr>
   </sec:authorize>      
</table>
<br />
<!-- table table-hover table table-condensed table-striped -->
<div class="container-fluid">
<table width="95%" class="listados" align="center" id="listaRequisiciones" cellpadding="0" cellspacing="0">
 <thead class="thead-inverse">
  <tr>
    <th width="4%"><input type="checkbox" name="todos" id="todos"></th>
    <th width="9%" height="20">Número</th>
    <th width="9%"> Fecha</th>
    <th width="7%">Estado</th>
    <th width="9%">Tipo</th>
    <th width="50%">Notas</th>
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
