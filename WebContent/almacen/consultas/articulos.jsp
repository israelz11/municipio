<%@ page contentType="text/html;charset=UTF-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de Grupos</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">
<link type="text/css" href="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.css" rel="stylesheet" />	
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script language="JavaScript" >
function  enviar(f1,op){
 f1.acciones.value=op;
  if (op=="Listar")  {
     f1.cuentades.value=f1.cuenta[f1.cuenta.selectedIndex].text;
	 f1.deptodes.value=f1.depto[f1.depto.selectedIndex].text;
      if (f1.depto.selectedIndex <1 ) {
       alert("Seleccione un departamento para la consulta");
       f1.depto.focus(); 
      }
     else
	 if (f1.usuario.selectedIndex <1 ) {
       alert("Seleccione un usuario para la consulta");
       f1.usuario.focus(); 
      }
     else
	 if (f1.year.selectedIndex <1 ) {
       alert("Seleccione un ao para la consulta");
       f1.year.focus(); 
      }
     else
	 if (f1.cuenta.selectedIndex <1 ) {
       alert("Seleccione una cuenta  para la consulta");
       f1.cuenta.focus(); 
      }
     else
	  f1.submit();
	
   }
  else
   f1.submit();
}


function modifica_sal(f,id_sal){  
   f.acciones.value="Listar";
   f.clave.value=id_sal;
   f.submit();
   
}

function reenviapag(f1){
f1.clave.value="";
f1.usuario.selectedIndex=-1;
f1.submit();
}

</script>
</head>
<body>
<br />
  <table width="95%" align="center"><tr><td><h1>Articulos</h1></td></tr></table>
  <form ACTION="sad_consul_articulos.jsp" METHOD="POST" name="f1" id="f1" >
    <input name="acciones" type="hidden" id="acciones">
    <input name="clave" type="hidden" id="clave" >
    <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="1" class="formulario" >
      <tr >
        <td height="30" colspan="2">CONSULTA DE  PEDIDOS</td>
      </tr>
      <tr >
        <td width="17%">Departamento</td>
        <td width="83%"><select name="depto" class="combList"  id="depto" onChange="reenviapag(this.form)">
		        <option value="0">Seleccione un departamento</option>
	          </select></td>
      </tr>
       <tr >
        <td>Usuario de pedido </td>
        <td><select name="usuario" class="combList"  id="usuario" >
		     <option value="0">Seleccione un usuario</option>
			 <option value="Todos">Todos</option>
	          </select></td>
      </tr>
      <tr >
        <td>A&ntilde;o</td>
        <td><select name="year" class="combList"  id="year">
          <option value="0">Seleccione un a&ntilde;o</option>
        </select></td>
      </tr>
      <tr >
        <td>Cuenta</td>
        <td><select name="cuenta" class="combList"  id="cuenta">
          <option value="0" >Seleccione una cuenta</option>
        </select></td>
      </tr>
      <tr >
        <td height="30" colspan="2" align="center"><input name="sol" type="button" class="botones" id="sol" onClick="enviar(this.form,'Listar')" value="Consultar">
        </td>
      </tr>
    </table>
    <br />	     				
	<table width="95%"  border="0" align="center" cellpadding="0" cellspacing="1" class="formulario" >
      <tr >
        <td height="30" colspan="16">HISTORIAL DE ART&Iacute;CULOS DEL PARTAMENTO DE
              ModDepDes ModCueDes(modYea) </td>
      </tr>
      <tr >
        <td colspan="16">datosUsuCab</td>
      </tr>
      <tr >
        <td width="3%">No.</td>
        <td width="7%">Clave</td>
        <td width="20%">Descripci&oacute;n</td>
        <td>mes[i]</td>
        <td width="5%">Total</td>
      </tr>
      <tr  >
        <td  >cont</td>
        <td  >claveArt</td>
        <td  >datosUsuCab.get(claveArt)</td>
        <td  ></td>
        <td >sumaArti</td>
      </tr>      
	  <tr >
        <td colspan="3" >Totales</td>       
        <td >Suma</td>
     <td >TOTAL</td>
	  </tr>
      <tr >
        <td height="40" colspan="16" align="center"><input name="sol2" type="button" class="botones" id="sol2" onClick="enviar(this.form,'')" value="Otra consulta" ></td>
      </tr>
    </table>    
</FORM>
</body>
</html>
