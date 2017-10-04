<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Muestra presupuesto</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/bootstrap2.css" type="text/css"/>
<link rel="stylesheet" href="../../include/css/sweetalert2.css" type="text/css"/>

<!--

-->
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js?x=<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="../../include/js/toolSam.js?x=<%=System.currentTimeMillis()%>"></script>

<script type="text/javascript" src="../../include/js/sweetalert2.js"></script>
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css"/>
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/jquery.qtip-1.0/jquery.qtip-1.0.0-rc3.min.js"></script>
<script language="javascript">

$(document).ready(function() { 
		var i = $('#hdcont').attr('value');
		var ban = '<c:out value="${ban}"/>';
		if(ban!='') return false;
		for(x=1; x<=i;x++){
			$('#link'+x).qtip({content: {text: $('#hd'+x).attr('value'),
								title: { text: 'Informacion Adicional' }
							},
						   position: {
							  corner: {
								 	target: 'bottomRight',
      								tooltip: 'topLeft'
							  }
						   },
						    style: { 
									  border: {
										 width: 3,
										 radius: 8,
										 color: '#575757'
									  },
									  width: 400
								} 
							,
							show: { effect: 'slide' }
			});
		}
		
		
});

function regresaPresupuesto(ID, proyecto, partida, pre_actual, disponible) {
	
	window.parent.__regresaPresupuesto(ID, proyecto, partida, pre_actual, disponible);//Envia la peticion a include/presupesto/presupuesto.js
	
}

function buscar(){
	$('#forma').attr('action',"muestra_presupuesto.action?ban=0");
	$('#forma').submit();
}

function getPresupuestoPDF(){
	$('#forma').attr('target',"impresionPresupuestoPDF");
	$('#forma').attr('action',"../reportes/rpt_presupuesto_mensual.action");
	$('#forma').submit();
	$('#forma').attr('target',"");
	$('#forma').attr('action',"rpt_presupuesto_mensual.action");
}

</script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-bottom: 0px;
	color:#000;
	font-size:11px;
}
a:link {
	text-decoration: none;
}
a:visited {
	text-decoration: none;
}
a:hover {
	text-decoration: none;
}
a:active {
	text-decoration: none;
}
-->
</style>
</head>

<body>
<form  action="" method="post" id="forma" name="forma">
<c:if test="${mes!=0}">
  <table width="95%" align="center">
  	<tr>
    	<td><h1>Presupuesto - Correspondiente al mes de
            <c:out value='${desMes}'/> <c:if test="${idVale!=0}"> en Vales</c:if></h1></td>
    </tr>
   <c:if test="${ban!=null}">
    <tr>
      <td><table class="formulario" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" align="right">&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td height="25" align="right">Unidad Administrativa:&nbsp;</td>
          <td>
              <select name="unidad" class="comboBox" id="unidad" onchange="buscar();" style="width:500px">
                <c:forEach items="${unidadesAdmiva}" var="item" varStatus="status"> 
                  <option value='<c:out value="${item.ID}"/>' 
                            <c:if test='${item.ID==unidad}'> selected </c:if>>
                  <c:out value='${item.DEPENDENCIA}'/>
                  </option>
                </c:forEach>
              </select>
          </td>
        </tr>
        <tr>
          <td width="267" height="25" align="right">Tipos de gatos:&nbsp;</td>
          <td width="1408"><strong>
            <select name="tipoGasto" class="comboBox" id="tipoGasto" style="width:500px" onchange="buscar()">
              <option value="0">[Tipos de gastos]</option>
              <c:forEach items="${tipodeGasto}" var="item" varStatus="status"> <option value='<c:out value="${item.ID}"/>' 
                <c:if test='${item.ID==tipo_gasto}'> selected</c:if>
                >
                <c:out value='${item.RECURSO}'/>
                </option>
              </c:forEach>
            </select>
            &nbsp;&nbsp;
            <input  name="cmdpdf" type="button" class="botones" id="cmdpdf"   value="Imprimir en PDF" onclick="getPresupuestoPDF()" style="width:150px" />
          </strong></td>
        </tr>
        <tr>
          <td height="25" align="right">Programas:&nbsp;</td>
          <td><strong>
            <select name="idproyecto" class="comboBox" id="idproyecto" style="width:500px" onchange="buscar()">
              <option value="0">[Programas]</option>
              <c:forEach items="${programas}" var="item" varStatus="status">
                <option value='<c:out value="${item.ID_PROYECTO}"/>' 
                
                  <c:if test='${item.ID_PROYECTO==proyecto}'> selected</c:if>
                  >
                  <c:out value='${item.PROYECTO}'/> - <c:out value='${item.DECRIPCION}'/>
                  </option>
              </c:forEach>
            </select>
          </strong></td>
        </tr>
        <tr>
          <td height="25" align="right">Partidas:&nbsp;</td>
          <td><strong>
            <select name="partida" class="comboBox" id="partida" style="width:500px" onchange="buscar()">
              <option value="0">[Partidas]</option>
              <c:forEach items="${partidas}" var="item" varStatus="status">
                <option value='<c:out value="${item.CLV_PARTID}"/>' 
                
                  <c:if test='${item.CLV_PARTID==partida}'> selected</c:if>
                 >
                  <c:out value='${item.CLV_PARTID}'/> - <c:out value='${item.PARTIDA}'/>
                  </option>
              </c:forEach>
            </select>
          </strong></td>
        </tr>
        <tr>
          <td height="20" align="right">&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
      </table></td>
    </tr>
    </c:if> 
  </table>
</c:if>  
<table border="0" align="center" cellpadding="2" cellspacing="2" width="95%" style="border-color:#D5D5D5">
  <tr bgcolor="#030303" >
    <td width="190" height="21" align="center"><strong style="color:#FFF">Programa/Partida</strong></td>
    <td height="25" colspan="3" align="center"><strong style="color:#FFF">Nombre del Programa</strong></td>
    <td height="25" colspan="3" align="center"><strong style="color:#FFF">Nombre de la Partida</strong></td>
  </tr>
  <tr bgcolor="#575757">
    <td height="20" width="17%" align="center"><strong style="color:#FFF">Autorizado Mes</strong></td>
    <td width="17%" height="16%" align="center"><strong style="color:#FFF">Precomprometido</strong></td>
    <td width="17%" height="16%" align="center"><strong style="color:#FFF">Comprometido</strong></td>
    <th width="17%" colspan="2" align="center"><strong style="color:#FFF">Devengado</strong></th>
    <td width="17%" align="center"><strong style="color:#FFF">Ejercido </strong></td>
    <td width="17%" height="16%" align="center"><strong style="color:#FFF"> Disponible</strong></td>
  </tr>
  <c:set var="cont" value="${0}" />
  <c:forEach items="${muestraPresupuesto}" var="item" varStatus="status" >
  <c:set var="cont" value="${cont+1}" /> 
      <tr bgcolor="#D5D5D5">
        <td height="18" align="center"><c:if test="${ban==null}">
        <input type="hidden" id="hd<c:out value='${cont}'/>" value="<strong style='color:#0080FF'>ID_PROYECTO:</strong> <c:out value='${item.ID_PROYECTO}'/><br><strong style='color:#0080FF'>TIPO DE GASTO:</strong>&nbsp;<c:out value='${item.RECURSO}'/><br><strong style='color:#0080FF'>ACT. INSTITUCIONAL:</strong>&nbsp;<c:out value='${item.ACTIVIDAD_INST}'/><br><strong style='color:#0080FF'>LOCALIDAD:</strong>&nbsp;<c:out value='${item.LOCALIDAD}'/>" />
        <a id="link<c:out value='${cont}'/>" href="javascript:regresaPresupuesto(<c:out value='${item.ID_PROYECTO}'/>,'<c:out value='${item.N_PROGRAMA}'/>','<c:out value='${item.CLV_PARTID}'/>','<c:out value='${item.PREACTUAL}'/>','<c:out value='${item.DISPONIBLE}'/>');">
	        </c:if>
	         <strong><c:out value='${item.N_PROGRAMA}'/>-<c:out value='${item.CLV_PARTID}'/></strong>
	        <c:if test="${ban==null}">
        </a>
        </c:if>
        </td>
        <td height="16%" colspan="3" align="left"><span style="color:#666"><c:out value='${item.DECRIPCION}'/></span></td>
        <td height="16%" colspan="3" align="left"><span style="color:#666"><c:out value='${item.PARTIDA}'/></span></td>
      </tr>
      <tr bgcolor="#F2F2F2">
        <td height="18" align="right">$<fmt:formatNumber value='${item.PREACTUAL}' pattern="###,###,###.00"/></td>
        <td height="16%" align="right"><c:if test="${item.PREREQ>0}"><a title="Mostrar pre-compromisos" href="javascript:mostrarConsultaCompromiso('<c:out value='${item.ID_PROYECTO}'/>','<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.CLV_PARTID}'/>', <c:out value='${mes}'/>, 'PRECOMPROMETIDO')"></c:if>$<fmt:formatNumber value='${item.PREREQ}' pattern="###,###,###.00"/></a></td>
        <td height="16%" align="right"><c:if test="${item.PRECOM>0}"><a title="Mostrar compromisos" href="javascript:mostrarConsultaCompromiso('<c:out value='${item.ID_PROYECTO}'/>','<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.CLV_PARTID}'/>', <c:out value='${mes}'/>, 'COMPROMETIDO')"></c:if>$<fmt:formatNumber value='${item.PRECOM}' pattern="###,###,###.00"/></a></td>
        <td height="16%" colspan="2" align="right"><c:if test="${item.PREDEV>0}"><a title="Mostrar devengado" href="javascript:mostrarConsultaCompromiso('<c:out value='${item.ID_PROYECTO}'/>','<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.CLV_PARTID}'/>', <c:out value='${mes}'/>, 'DEVENGADO')"></c:if>$<fmt:formatNumber value='${item.PREDEV}' pattern="###,###,###.00"/></a></td>
        <td height="16%" align="right"><c:if test="${item.PREEJER>0}"><a title="Mostrar ejercido" href="javascript:mostrarConsultaCompromiso('<c:out value='${item.ID_PROYECTO}'/>','<c:out value='${item.N_PROGRAMA}'/>', '<c:out value='${item.CLV_PARTID}'/>', <c:out value='${mes}'/>, 'EJERCIDO')"></c:if>$<fmt:formatNumber value='${item.PREEJER}' pattern="###,###,###.00"/></a></td>
        <td height="16%" align="right"><span <c:if test="${item.DISPONIBLE<0}">style="color:#F00"</c:if>>$<fmt:formatNumber value='${item.DISPONIBLE}' pattern="###,###,###.00"/></span></td>
      </tr>
      
  </c:forEach>
  <input type="hidden" id="hdcont" value="<c:out value='${cont}'/>" />
  <c:if test="${mes==0}">
  <tr bgcolor="#DBDBDB">
        <td height="16%" colspan="7" align="left">No hay presupuesto para mostrar.<br /> No existe periodo abierto </td>
      </tr>
   </c:if>  
</table
></form>
</body>
</html>