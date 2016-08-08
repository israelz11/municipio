<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Catalogo de Grupos</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link type="text/css" href="../../include/css/black-tie/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
<link rel="stylesheet" href="../../include/css/estilosam.css" type="text/css">
<link rel="stylesheet" href="../../include/js/componentes/jquery.alerts.css" type="text/css">	
<link href="../../include/js/autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../../include/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../include/js/jquery-impromptu.2.3.js"></script>
<script type="text/javascript" src="../../include/js/componentes/componentes.js"></script>
<script type="text/javascript" src="../../dwr/interface/controladorBeneficiarioRemoto.js"> </script>
<script type="text/javascript" src="../../dwr/interface/autocompleteDiversosRemoto.js"> </script>
<script type="text/javascript" src="../../include/js/utilsJquery/jquery-ui-1.7.1.custom.min.js"></script>
<script type="text/javascript" src="../../dwr/engine.js"> </script>  
<script type="text/javascript" src="../../include/js/componentes/jquery.alerts.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/jquery.autocomplete.js"></script>
<script type="text/javascript" src="../../include/js/autocomplete/autompleteVarios.js"></script>
<script type="text/javascript" src="beneficiario.js"></script>
<script type="text/javascript" src="../../include/js/toolSam.js"></script>
<script type="text/javascript" src="../../include/js/presupuesto/presupuesto.js"></script>
<script type="text/javascript" src="../../include/js/otros/productos.js"></script>
<link rel="stylesheet" href="../../include/js/autocomplete/jquery.autocomplete.css" type="text/css" />
<!--<script type="text/javascript" src="../../include/js/jquery.tabs/jquery-1.1.3.1.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.history_remote.pack.js"></script>
<script type="text/javascript" src="../../include/js/jquery.tabs/jquery.tabs.pack.js"></script>-->
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs.css" type="text/css" media="print, projection, screen">
<!-- Additional IE/Win specific style sheet (Conditional Comments) -->
<!--[if lte IE 7]>
<link rel="stylesheet" href="../../include/js/jquery.tabs/jquery.tabs-ie.css" type="text/css" media="projection, screen">
<![endif]-->
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<form name="forma" method="get" action="" onSubmit=" return false" >
<div id="tabuladores">
  <ul>
   	<li><a href="#fragment-general"><span>Información general</span></a></li>
   	<li><a href="#fragment-fiscal"><span>Domicilio fiscal</span></a></li>
    <li><a href="#fragment-bancario"><span>Datos bancarios</span></a></li>
  </ul>
  <div id="fragment-general" align="left">
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="formaCaptura">
    <tr>
      <th height="30">&nbsp;</th>
      <td>Nota: (*) Requerido para nuevo beneficiario, (**) Requerido para nuevos funcionarios</td>
    </tr>
    <tr>
      <th height="30">**Tipo :</th>
      <td><select name="tipo" class="comboBox" id="tipo" style="width:150px" >
        <option value="" <c:if test="${beneficiario.TIPOBENEFI==''}">selected</c:if>>[Seleccione]</option>
        <option value="PR" <c:if test="${beneficiario.TIPOBENEFI=='PR'||beneficiario.TIPOBENEFI=='CO'||beneficiario.TIPOBENEFI=='CM'}">selected</c:if>>Beneficiario</option>
        <option value="MP" <c:if test="${beneficiario.TIPOBENEFI=='MP'}">selected</c:if>>Funcionario</option>
      </select></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*R. F. C. :</span></th>
      <td><input name="rfc" type="text" class="input" id="rfc" value="<c:out value='${beneficiario.RFC}'/>"  maxlength="15" onBlur="upperCase(this)" style="width:150px" />
        <input type="hidden" name="idProveedor" id="idProveedor" value="<c:out value='${id}'/>">
        </td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">CURP :</span></th>
      <td><input name="curp" type="text" class="input" id="curp" value="<c:out value='${beneficiario.CURP}'/>"  maxlength="18" onBlur="upperCase(this)" style="width:150px" /></td>
    </tr>
    <tr>
      <th width="20%" height="30"><span class="Texto_Forma">**Raz&oacute;n Social :</span></th>
      <td><input name="razonSocial" type="text" class="input" id="razonSocial" value="<c:out value='${beneficiario.NCOMERCIA}'/>" style="width:350px"  maxlength="100" onBlur="upperCase(this)" /></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*Responsable 1 :</span></th>
      <td><input name="responsable" type="text" class="input" id="responsable" value="<c:out value='${beneficiario.BENEFICIAR}'/>" style="width:350px" maxlength="100" onBlur="upperCase(this)" /></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">Responsable 2 :</span></th>
      <td><input name="responsable2" type="text" class="input" id="responsable2" value="<c:out value='${beneficiario.BENEFICIA2}'/>" style="width:350px" maxlength="100" onBlur="upperCase(this)" /></td>
      </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*Tel&eacute;fonos :</span></th>
      <td width="80%"><input name="telefono" type="text" class="input" id="telefono" value="<c:out value='${beneficiario.TELEFONOS}'/>" style="width:350px" maxlength="100" onBlur="upperCase(this)" /></td>
    </tr>
    <tr>
      <th height="30">Estatus :</th>
      <td><input name="vigencia" type="checkbox" id="vigencia" value="1" <c:if test='${beneficiario.STATUS==1}'>checked</c:if>>
        Activo</td>
    </tr>
    </table>
</div>  
<div id="fragment-fiscal" align="left">    
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="formaCaptura">
    <tr>
      <td height="30" colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <th width="20%" height="30"><span class="Texto_Forma">*Calle y N&uacute;mero :</span></th>
      <td width="80%"><input name="calle" type="text" class="input" id="calle" value="<c:out value='${beneficiario.DOMIFISCAL}'/>" style="width:350px" maxlength="60" onBlur="upperCase(this)" /></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*Colonia :</span></th>
      <td><input name="colonia" type="text" class="input" id="colonia" value="<c:out value='${beneficiario.COLONIA}'/>" style="width:350px" maxlength="40" onBlur="upperCase(this)" /></td>
      </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*Estado :</span></th>
      <td><input name="estado" type="text" class="input" id="estado" value="<c:out value='${beneficiario.ESTADO}'/>" style="width:150px" maxlength="25" onBlur="upperCase(this)" /></td>
      </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*Ciudad :</span></th>
      <td><input name="ciudad" type="text" class="input" id="ciudad" value="<c:out value='${beneficiario.CIUDAD}'/>" style="width:350px" maxlength="50" onBlur="upperCase(this)" /></td>
      </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">*C&oacute;d. Postal :</span></th>
      <td><input name="cp" type="text" class="input" id="cp" value="<c:out value='${beneficiario.CODIGOPOST}'/>" style="width:150px" maxlength="5" onKeyPress=" return keyNumbero( event );" onBlur="upperCase(this)" /></td>
      </tr>
    <tr>
      <th height="30">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
    </table>
</div>
<div id="fragment-bancario" align="left">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="formulario" id="formaCaptura">
    <tr>
      <td height="30" colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <th width="20%" height="30"><span class="Texto_Forma">Banco :</span></th>
      <td width="80%"><select name="cbobanco" class="comboBox" id="cbobanco" style="width:350px" >
        <option value="0">[Seleccione]</option> 
        	<c:forEach items="${bancos}" var="item" varStatus="status">
                <option value='<c:out value="${item.CLV_BNCSUC}"/>' <c:if test='${item.CLV_BNCSUC==beneficiario.CLV_BNCSUC}'> selected</c:if>><c:out value='${item.BANCO}'/> <c:out value='${item.PLAZA}'/> <c:out value='${item.SUCURSAL}'/></option>
            </c:forEach>
      </select>        
      <input type="hidden" name="idBanco" id="idBanco" /></td>
    </tr>
    <tr>
      <th height="30"><span class="Texto_Forma">No. de Cuenta :</span></th>
      <td><input name="noCuenta" type="text" class="input" id="noCuenta" value="<c:out value='${beneficiario.NUM_CTA}'/>" style="width:150px" maxlength="150" onBlur="upperCase(this)" onkeypress=" return keyNumbero( event );" /></td>
    </tr>
    <tr>
      <th height="30">Tipo :</th>
      <td><select name="tipoCuenta" class="comboBox" id="tipoCuenta" style="width:150px">
        <option value="" <c:if test="${beneficiario.TIPO_CTA==''}">selected</c:if>>[Selecccione]</option>
        <option value="A" <c:if test="${beneficiario.TIPO_CTA=='A'}">selected</c:if>>Ahorro</option>
        <option value="C" <c:if test="${beneficiario.TIPO_CTA=='C'}">selected</c:if>>Cheques</option>
        </select></td>
    </tr>
    <tr>
      <th height="30">&nbsp;</th>
      <td>&nbsp;</td>
    </tr>
  </table>
</div>
</div>
<table width="100%%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="35" align="center"><input  name="cmdcerrar" type="button" class="botones" id="cmdcerrar"   value="Cerrar" style="width:150px" />
      <input  name="cmdguardar" type="button" class="botones" id="cmdguardar"   value="Guardar" style="width:150px" /></td>
  </tr>
</table>
</form>
</body>
</html>
