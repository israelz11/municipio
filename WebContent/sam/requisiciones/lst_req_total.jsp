<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!-- <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<script type="text/javascript" src="lst_req_total.js"> </script>
<script type="text/javascript" src="../../include/js/jquery-2.2.1.js"></script>
<script type="text/javascript" src="../../include/js/datetimepicker/moment-with-locales.js"></script>



<link rel="stylesheet" href="../../include/css/bootstrap-3.3.6.css" type="text/css">
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css">

<link rel="stylesheet" href="../../include/js/datetimepicker/bootstrap-datetimepicker.css" type="text/css"> 
<script type="text/javascript" src="../../include/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>

<script type="text/javascript" src="../../include/js/Multiselect/bootstrap-select.js"></script>
<link rel="stylesheet" href="../../include/css/Multiselect/bootstrap-select.css" type="text/css">



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

<title>Listado de Requisiciones</title>
</head>
<body  >

<form class="form-horizontal" rol="form" action="lst_req_total.action" method="post" id="forma" name="forma">
	
	<div class="row col-md-offset-2">
        	<h1 class="h1-encabezado">Requisiciones - Listado de Requisiciones, Ordenes de Trabajo y Ordenes de Servicio</h1>
	</div>
	<div style="width:1600px; margin-left:auto; margin-right:auto" class="container">
		<div class="well">
    		<input type="hidden" name="ejercicio" id="ejercicio" value="<c:out value='${ejercicio}'/>">
        	<input type="hidden" name="claveRequisicion" id="claveRequisicion" >
    <!--bloque form-group 1 -->   
    <div class="form-group">
              <label for="dependencia" class="control-label col-md-2">Unidad:</label>
              <div class="col-md-6">
                <sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
                          <c:out value="${nombreUnidad}"/>
                          <input type="hidden" name="dependencia" id="dependencia" value="<c:out value='${idUnidad}'/>">
                          <input type="hidden" name="todo" id="todo" value="0">
                    </sec:authorize>
                    <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
                        <input type="hidden" name="todo" id="todo" value="1">
                        <select name="dependencia" id="dependencia" class="form-control input-sm" style="width:455px;height:29px; font-size:12px;">
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
            <div class="col-md-4"><!-- Revisar como llamar la funcion desde el jsp -->
         			
                <input type="checkbox" name="status" id="status" value="0" <c:if test="${fn:contains(status,'0')}" ></c:if>>
                <label style="width:100px;" for="status">&nbsp; Edición</label>
                <input name="status" id="status" value="1" type="checkbox" <c:if test="${fn:contains(status,'1')}" >checked</c:if>>
                <label style="width:100px;" for="status">&nbsp; Cerrado</label>
                <input name="status" id="status" value="2" type="checkbox" <c:if test="${fn:contains(status,'2')}" ></c:if>>
                <label style="width:100px;" for="status">&nbsp; En proceso</label>
            </div>
    </div>
    <!--bloque form-group 2 -->   
    <div class="form-group">
        <label for="dependencia" class="control-label col-md-2">Tipo de gasto:</label>
          <div class="col-md-6">
              <select name="cbotipogasto" id="cbotipogasto" class="form-control input-sm" style="width:455px;height:29px; font-size:12px;">
                  <option value="0"> [Todos los tipos de gastos]
                    <c:forEach items="${tipodeGasto}" var="item" varStatus="status">
                <option value='<c:out value="${item.ID}"/>'
                    <c:if test='${item.ID==cbotipogasto}'> selected </c:if>>
                      <c:out value='${item.RECURSO}'/>
                </option>
                      </c:forEach>
            </select>
        </div>
        <div class="col-md-4">
             
              <input name="status" type="checkbox" id="status" value="4" <c:if test="${fn:contains(status,'4')}" >checked</c:if>>
              <label style="width:100px;" for="status">&nbsp; Canceladas</label>
              <input name="status" type="checkbox" id="status" value="5" <c:if test="${fn:contains(descripcion_estatus,'finiquitado')}">checked</c:if>>
              <label style="width:100px;" for="status">&nbsp; Finiquitadas</label>
        </div>
    </div>  
    <!--bloque form-group 3 -->           
    <div class="form-group">
        <label for="txtprestadorservicio" class="control-label col-md-2">Beneficiario:</label>
            <div class="col-md-5">
                 <input type="hidden" id="CVE_BENEFI" name="CVE_BENEFI" value="<c:out value='${CVE_BENEFI}'/>" />
                  <input type="text" id="txtprestadorservicio" name="txtprestadorservicio" class="form-control input-sm" style="width:455px;" placeholder="Beneficiario" value="<c:out value='${txtprestadorservicio}'/>"/>
            </div>
              <div class="col-md-4 col-md-offset-1">
                 <div class="col-md-5">
                      <input name="cmdpdf" id="cmdpdf" title="Mostrar listado en formato PDF" type="button" class="btn btn-success btn-imprimir form-control"  value="Imprimir PDF" style="width:100px" >
                 </div>
                <div class="col-md-6">
                         
                        <input class="btn btn-success btn-sm" name="btnBuscar" id="btnBuscar"  value="Buscar" onClick="getListaReq2()" style="width:100px" type="button">
                               
                 </div>
            </div>
       </div> 
       <!--bloque form-group 4 -->       
    <div class="form-group">
        <label for="dependencia" class="control-label col-md-2">Adicional:</label>
            <div class="col-md-2">
                <select class="form-control input-sm" name="cboconOP" id="cboconOP" style="width:200px;height:29px;font-size:12px">
                      <option value="0" <c:if test='${0==cboconOP}'> selected </c:if>>[Todas las opciones]</option>
                    <option value="1" <c:if test='${1==cboconOP}'> selected </c:if>>Con Ordenes de Pago</option>
                    <option value="2" <c:if test='${2==cboconOP}'> selected </c:if>>Sin Ordenes de Pago</option>
                </select>  
            </div>
            <div class="col-md-2">
            <select name="cbotipo" id="cbotipo" class="form-control input-sm" style="width:200px; height:29px;font-size:12px">
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
              <div class="col-md-4">
                  <input type="checkbox" name="verUnidad" id="verUnidad" value="1" <c:if test='${verUnidad==1}'>  checked </c:if>>
                  <label style="width:300px;" for="verUnidad">&nbsp; Incluir documentos de  la Unidad</label>
            </div>
       </div>
       
<!--bloque form-group 5   
    
   --> 
   <div class="form-group">
   		<label for="date_timepicker_start" class="control-label col-md-2">Por fecha de :</label>
   		<div class="container">
    		<div class='col-md-2'>
        		<div class="form-group">
            		<div class='input-group date' id='datetimepicker6'>
                		<input type='text' class="form-control input-sm" name="fechaInicial" value="<c:out value='${fechaInicial}'/>" />
 						<span class="input-group-addon">
                    		<span class="glyphicon glyphicon-calendar"></span>
                		</span>
            		</div>
        		</div>
    		</div>
    	<div class='col-md-3'>
    		<label for="datepicker" class="control-label col-md-5">hasta:</label>
        	<div class="form-group">
            	<div class='input-group date' id='datetimepicker7'>
                	<input type='text' class="form-control input-sm" name="fechaFinal" value="<c:out value='${fechaFinal}'/>" />
                	<span class="input-group-addon">
                    	<span class="glyphicon glyphicon-calendar"></span>
                	</span>
            	</div>
        	</div>
    	</div>
		</div>
   </div>
   
  
   
   
<!--bloque form-group 6 -->   
     <div class="form-group row">
         <label for="txtrequisicion" class="control-label col-md-2">Núm. Requisición:</label>
         <div class="col-md-4">
             <input name="txtrequisicion" type="text" id="txtrequisicion" maxlength="50" style="width:150px; height:29px;font-size:12px" class="form-control col-md-2" value="<c:out value='${txtrequisicion}'/>"  placeholder="No. requisición">
            <label class="control-label col-md-2" for="txtproyecto">Proyecto:</label>
            <input name="txtproyecto" type="text" id="txtproyecto" maxlength="4" class="form-control col-md-2"    style="width:80px; height:29px;font-size:12px" value="<c:out value='${txtproyecto}'/>" placeholder="Proyecto">
            <label for="txtproyecto" class="control-label col-md-2">Partida:</label>
            <input class="form-control" name="txtproyecto" id="txtproyecto" maxlength="4" style="width:70px; height:29px; font-size:12px;" onKeyPress="return keyNumbero(event);" value="<c:out value='${txtpartida}'/>" type="text" placeholder="Partida">
           
           
         </div>
          <div class="col-md-4 col-xs-offset-2">
                   <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_LISTAR_LOTES_DE_REQUISICIONES_EN_EXCEL_ADMON">
                       <input name="verUnidad" id="verUnidad" value="1" type="checkbox">
                     <label style="width:300px;" for="status">&nbsp;  Consultar solo Requisiciones agregadas en listado</label>
                   </sec:authorize>
          </div>
       </div>
       <!--bloque form-group 7 -->      
       <div class="form-group">
              <label for="comment" class="control-label col-md-2">Requisiciones en lista:</label>
              <div class="col-md-5">
                  <textarea class="form-control" name="txtlistado" rows="3" wrap="virtual" rows="5"  id="txtlistado" style="width:455px"> <c:out value='${txtlistado}'/> </textarea>
              </div>
              <div class="form-group">
      		 	 	<label for="estatusr" class="col-md-2 control-label">Estatus</label>
     		  		<div class="styled-select col-md-offset-2">
						
					</div>
					<div class="form-group">
							<div class= "well">
								<select class="selectpicker" multiple id="cbostatusok">
								<optgroup><option value=0>Todos los estatus</option></optgroup >
								  <option>Edicion</option>
								  <option>Cerrado</option>
								  <option>Proceso</option>
								  <option>Cancelado</option>
								  <option>Finiquitado</option>
								</select>
								
							</div>
						
						
   					</div>
			 </div>
			 
		</div><!-- Termina Bloque 7 -->
		
		 <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODOS_LOS_DOCUMENTOS_DE_LA_UNIDAD">
   			 <input type="hidden" value="1" id="REPORTE_ESPECIAL_2">
         </sec:authorize>
    	 
 	</div> <!-- Cierra Well -->
 
 
  
 
 <!-- pruebas de entradas y salidas -->
 
 
  
   
   
<div class="container-fluid">
<table class="table table-hover table table-condensed table-striped" style="width=95% align=center"  id="listaRequisiciones">
 <thead class="thead-inverse">
  <tr>
    <th width="4%" style="text-align:center"><input type="checkbox" name="todos" id="todos" style="text-align:center"></th>
    <th width="9%" height="20">Número</th>
    <th width="9%"> Fecha</th>
    <th width="7%">Estado</th>
    <th width="9%">Tipo</th>
    <th width="50%" style="text-align:left;">Notas</th>
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
    <td style="border-right:none; text-align:left;" ><c:out value='${item.TIPO_REQ}'/></td>
    <td style="border-right:none; text-align:left;"><c:out value='${item.OBSERVA}'/></td>
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
   </c:forEach>
 <!-- Modificacion por Abraham Gonzalez 24/02/17 para poder aperturar las requisciones exclusivamente-->
 <c:if test="${fn:length(requisicionesUnidad) > 0}"> <!-- Aqui viene al Controlador del listado de Requisiciones-->
  <tr>
    <td height="40" colspan="9" align="left"  style="background-color:#FFF">
        <table width="389" border="0" cellspacing="0" cellpadding="0">
          <tr>
           <c:if test="${fn:contains(status,'1')||fn:contains(status,'2')||fn:contains(tipo,'1')||fn:contains(tipo,'7')}">
            <td width="129" bgcolor="#FFFFFF">
            	<div class="buttons tiptip">
					<button name="cmdaperturar" id="cmdaperturar" onClick="aperturarRequisiciones()" title="Apertura para edicion los documentos seleccionados" type="button" class="button red middle"><span class="label" style="width:100px">Aperturar</span></button>
                </div>
            </td>
            </c:if>
            <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_CANCELAR_REQUISICIONES">
            <td width="135" bgcolor="#FFFFFF">
            	<div class="buttons tiptip">
					<button name="cmdcancelarm" id="cmdcancelarm" onClick="cancelacionMultiple()"  title="Cancela o elimina los documentos seleccionados" type="button" class="button red middle" ><span class="label" style="width:100px">Cancelar</span></button>
               	</div>
           </td>
           </sec:authorize>
            <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_LISTAR_LOTES_DE_REQUISICIONES_EN_EXCEL_ADMON">
            <td width="125" bgcolor="#FFFFFF">
            	<div class="buttons tiptip">
					<button name="cmdAgregarLista" id="cmdAgregarLista" onClick="agregarReqLista()"  title="Agrega las Requisicones seleccionadas al listado especial" type="button" class="button red middle" ><span class="label" style="width:100px">Agregar a lista</span></button>
               	</div>
           </td>
           </sec:authorize>
          </tr>
        </table></td>
    </tr>
   </c:if>
  </tbody> 
</table>
    <div class="alert alert-info">
        <strong>Total de registros encontrados: <c:out value='${CONTADOR}'/></strong><br/>
        <strong>Valor checkStatus: <c:out value='${fn:contains(options,0)}'/></strong>
    </div>
    
</div>
</div><!--Termina Container  -->
</form>

</body>
</html>