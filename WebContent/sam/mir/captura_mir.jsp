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
    <script type="text/javascript" src="../../dwr/interface/controladorMIRRemoto.js"> </script>
	<script type="text/javascript" src="../../dwr/interface/controladorProyectoPartida.js"> </script>
    <script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>   
    <script type="text/javascript" src="../../dwr/engine.js"> </script>
    <script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
    <script type="text/javascript" src="../../include/js/jquery.maxlength.js"></script>
    <script type="text/javascript" src="../../include/js/jquery.bestupper.min.js"></script>
    <script type="text/javascript" src="../../include/js/toolSam.js"></script>
    <script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
    <script type="text/javascript" src="../../include/js/otros/productos.js"></script>
    <script type="text/javascript" src="captura_mir.js"></script>
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
<title>Captura MIR</title>
</head>

<body>
<form name="forma" id="forma" method="get" action="../reportes/rpt_pedido.action">
<input type="hidden" name="clavePedido" id="clavePedido"  value="">
<h1>&nbsp;MIR - Captura de Matriz de Indicadores No. 
  <c:out value='${modelo.NUM_REQ}'/></h1>
<div id="tabuladores">
  <ul>
   <li><a href="#fragment-pedidos"><span>Información General</span></a></li>
   <li><a href="#fragment-conceptos"><span>Niveles</span></a></li>
  </ul>
      <div id="fragment-pedidos" align="left">
      	<table border="0" cellspacing="0" cellpadding="0" class="formulario" width="100%">
                   <tr>
                    <td colspan="4">&nbsp;<strong>Nota:</strong> La información marcada con (*) es requerida. 
                    	<input type="hidden" id="ID_MIR"  name="ID_MIR" value="0"/>
                    </td>
                  </tr>
                  <tr>
                    <th height="30">No. M.I.R.:</th>
                    <td colspan="3"><div  id="cve_pedido" style="font-weight:bold"><c:out value='${modelo.ID_MIR}'/></div></td>
                  </tr>
                   <tr>
                    <th width="18%">*Unidad Administrativa:</th>
                    <td colspan="3">
                    	<sec:authorize ifNotGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
                      		<c:out value="${nombreUnidad}"/>
                      		<input type="hidden"  id="cbodependencia" value='<c:out value="${idUnidad}"/>' />
                      	</sec:authorize>
                       <sec:authorize ifAllGranted="ROLE_Sam_PRIVILEGIOS_VER_TODAS_LAS_UNIDADES">
                        <div class="styled-select">
                            <select id="cbodependencia" style="width:670px">
                                  <option value="0">[Seleccione]</option>
                                  <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
                                      <option value='<c:out value="${item.CLV_UNIADM}"/>'>
                                        <c:out value='${item.DEPENDENCIA}'/>
                                    </option>
                                  </c:forEach>
                             </select>
                          </div>
      					</sec:authorize>
                    </td>
                  </tr>
                  <tr>
                     <th height="30">*Fecha captura:</th>
                     <td><input type="text" class="input" id="txtfecha" style="width:100px" value="<c:if test="${cve_ped==0||cve_ped==NULL}"><fmt:formatDate pattern="dd/MM/yyyy" value="${fecha}" /></c:if><c:out value='${map.FECHA_PED}'/>" size="12" maxlength="10" /></td>
                     <th colspan="2">&nbsp;</th>
          </tr>
                  <tr>
                    <th height="30">*Programa:&nbsp;&nbsp;</th>
                    <td colspan="3">
                    	<input id="txtPrograma" type="text" class="input" style="width:100px" size="12" maxlength="10" />
                        <img src="../../imagenes/search_16.png" alt="Mostrar presupuesto" name="img_programa"  id="img_programa" style="cursor:pointer" align="absmiddle"/> <img src="../../imagenes/cross2.png" id="img_quitar_contrato" width="16" height="16" alt="Quitar contrato" title="Quitar contrato" align="absmiddle" style="cursor:pointer">
                    </td>
                  </tr>
                  <tr>
                    <th height="30">*Clave Programatica:&nbsp;&nbsp;</th>
                    <td><input id="txtClaveProgramatica" type="text" class="input"  value="<c:out value='${map.CONTRATO}'/>" style="width:200px" maxlength="8" /></td>
                    <th width="20%"><div style="display:none"> Concurso:</div></th>
                    <td width="43%"><div style="display:none"><input name="txtconcurso" type="text" class="input" id="txtconcurso" style="width:100px"  value="<c:out value='${map.CVE_CONCURSO}'/>"  /></div></td>
                  </tr>
                  <tr>
                    <td height="17" colspan="4"></td>
                  </tr>
                  <tr>
                    <td width="18%">&nbsp;</td>
                    <td colspan="3">
                    
                      <table width="350" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td><div class="buttons tiptip">
                            <button name="cmdcerrar" id="cmdcerrar" onClick="" type="button" class="button red middle"><span class="label" style="width:150px" <c:if test="${cve_ped==0||cve_ped==NULL}">disabled</c:if>>Enviar a Validaci&oacute;n</span></button>
                          </div></td>
                          <td><div class="buttons tiptip">
                            <button id="cmdguardarMIR" type="button" class="button blue middle" <c:if test="${cve_ped==0}">disabled</c:if>><span class="label" style="width:150px">Guardar</span></button>
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
    	<table border="0" cellspacing="0" cellpadding="0" class="formulario" width="100%">
        	<tr>
            	
            </tr>
        </table>
    </div>
</div>
</form>
</body>
</html>