<%@ page contentType="text/html;charset=utf-8"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<title>Catalogo de Grupos</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css"> 
<link rel="stylesheet" href="../../include/css/style-tabs.css" type="text/css"> 
<script type="text/javascript" src="../../include/js/bootstrap-responsive-tabs.js"></script>
<script type="text/javascript" src="../../include/js/jquery-2.2.1.js"></script>
<script type="text/javascript" src="../../include/js/bootstrap-3.3.4.js"></script>
<script type="text/javascript" src="beneficiario.js"></script>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<form name="forma" method="get" action="" onSubmit=" return false" class="form-inline">

	<div class="col-md-12">
            <div class="panel with-nav-tabs panel-info">
                <div class="panel-heading">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#fragment-general" data-toggle="tab">Información general</a></li>
                            <li><a href="#fragment-fiscal" data-toggle="tab">Domicilio fiscal</a></li>
                            <li><a href="#fragment-bancario" data-toggle="tab">Datos bancarios</a></li>
                             <li><a href="#tab4info" data-toggle="tab">Formatos Programáticos</a></li>
                            
                        </ul>
                </div>
                <div class="panel-body">
                    <div class="tab-content">
                        <div class="tab-pane fade in active" id="fragment-general">
                        	<div class="row">
                        		<strong>Nota:</strong> (*) Requerido para nuevo beneficiario, (**) Requerido para nuevos funcionarios
                        	</div>
                        	<div class="row">
                        		<label for="tipo" class="control-label">Tipo:</label>
                        		<select name="tipo" class="form-control input-sm" id="tipo">
								    <option value="" <c:if test="${beneficiario.TIPOBENEFI==''}">selected</c:if>>[Seleccione]</option>
								    <option value="PR" <c:if test="${beneficiario.TIPOBENEFI=='PR'||beneficiario.TIPOBENEFI=='CO'||beneficiario.TIPOBENEFI=='CM'}">selected</c:if>>Beneficiario</option>
								    <option value="MP" <c:if test="${beneficiario.TIPOBENEFI=='MP'}">selected</c:if>>Funcionario</option>
								    <option value="MUNICIPIO" <c:if test="${beneficiario.TIPOBENEFI=='MUNICIPIO'}">selected</c:if>>Funcionario Municipal</option>
								    <option value="FISCA" <c:if test="${beneficiario.TIPOBENEFI=='FISCA'}">selected</c:if>>Persona Fisica</option>
								    <option value="MORAL" <c:if test="${beneficiario.TIPOBENEFI=='MORAL'}">selected</c:if>>Persona Moral</option>
								</select>
                        	</div>
                        	<div class="row">
                        		<label for="razonSocial" class="control-label">**Raz&oacute;n Social:</label>
                        		<input name="razonSocial" type="text" class="form-control input-sm" id="razonSocial" value="<c:out value='${beneficiario.NCOMERCIA}'/>" style="width:350px"  maxlength="100" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="row">
                        		<label for="responsable" class="control-label">*Responsable:</label>
                        		<input name="responsable" type="text" class="form-control input-sm" id="responsable" value="<c:out value='${beneficiario.BENEFICIAR}'/>" style="width:350px" maxlength="100" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="row">
                        		<label for="rfc" class="control-label">*R. F. C.:</label>
                        		<input name="rfc" type="text" class="form-control input-sm" id="rfc" value="<c:out value='${beneficiario.RFC}'/>"  maxlength="15" onBlur="upperCase(this)" style="width:150px" />
        						<input type="hidden" name="idProveedor" id="idProveedor" value="<c:out value='${id}'/>">
                        	</div>
                        
                         </div>
                        <div class="tab-pane fade" id="fragment-fiscal">
                        	
                        	<div class="form-group">
                        		<span class="Texto_Forma">*Calle:</span>
                        		<input name="calle" type="text" class="form-control input-sm" id="calle" value="<c:out value='${beneficiario.DOMIFISCAL}'/>" style="width:350px" maxlength="60" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="form-group">
                        		<span class="Texto_Forma">*N&uacute;mero int.:</span>
                        		<input name="numext" type="text" class="form-control input-sm" id="numext" value="<c:out value='${beneficiario.NUM_INT}'/>" style="width:150px" maxlength="60" onBlur="upperCase(this)" />
                        		<span class="Texto_Forma">*N&uacute;mero ext.:</span>
                        		<input name="numext" type="text" class="form-control input-sm" id="numext" value="<c:out value='${beneficiario.NUM_EXT}'/>" style="width:150px" maxlength="60" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="form-group">
                        		<span class="Texto_Forma">*Colonia :</span>
                        		<input name="colonia" type="text" class="form-control input-sm" id="colonia" value="<c:out value='${beneficiario.COLONIA}'/>" style="width:350px" maxlength="40" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="form-group">
                        		<span class="Texto_Forma">*Localidad :</span>
                        		<input name="localidad" type="text" class="form-control input-sm" id="localidad" value="<c:out value='${beneficiario.LOCALIDAD}'/>" style="width:350px" maxlength="50" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="form-group">
                        		<span class="Texto_Forma">*Ciudad :</span>
                        		<input name="ciudad" type="text" class="form-control input-sm" id="ciudad" value="<c:out value='${beneficiario.CIUDAD}'/>" style="width:350px" maxlength="50" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="form-group">
                        		<span class="Texto_Forma">*Estado :</span>
                        		<input name="estado" type="text" class="input" id="estado" value="<c:out value='${beneficiario.ESTADO}'/>" style="width:150px" maxlength="25" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="form-group">
                        		<span class="Texto_Forma">*Nacionalidad :</span>
                        		<input name="nacionalidad" type="text" class="input" id="nacionalidad" value="<c:out value='${beneficiario.NACIONALIDAD}'/>" style="width:150px" maxlength="25" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="form-group">
                        		<span class="Texto_Forma">*C&oacute;d. Postal :</span>
                        		<input name="cp" type="text" class="input" id="cp" value="<c:out value='${beneficiario.CODIGOPOST}'/>" style="width:150px" maxlength="5" onKeyPress=" return keyNumbero( event );" onBlur="upperCase(this)" />
                        	</div>
                        	<div class="form-group">
                        		<span class="Texto_Forma">*Tel&eacute;fonos :</span>
                        		<input name="telefono" type="text" class="form-control input-sm" id="telefono" value="<c:out value='${beneficiario.TELEFONOS}'/>" style="width:350px" maxlength="100" onBlur="upperCase(this)" />
                        	</div>
                        </div>
                        <div class="tab-pane fade" id="fragment-bancario">
  							<label for="cbobanco" class="control-label col-md-1">Banco:</label>
                        	<select name="cbobanco" class="form-control input-sm" id="cbobanco" style="width:350px" >
        							<option value="0">[Seleccione]</option> 
        							<c:forEach items="${bancos}" var="item" varStatus="status">
                					<option value='<c:out value="${item.CLV_BNCSUC}"/>' <c:if test='${item.CLV_BNCSUC==beneficiario.CLV_BNCSUC}'> selected</c:if>><c:out value='${item.BANCO}'/> <c:out value='${item.PLAZA}'/> <c:out value='${item.SUCURSAL}'/></option>
            						</c:forEach>
      						</select>        
      						<input type="hidden" name="idBanco" id="idBanco" /></td>
						</div>
                        <div class="tab-pane fade" id="tab4info">Info 4</div>
                       
                    </div>
                </div>
            </div>
        </div>
  <div class="form-group">
	  <input  name="cmdcerrar" type="button" class="btn btn-cerrar input-sm" id="cmdcerrar"   value="Cerrar" style="width:150px" />
      <input  name="cmdguardar" type="button" class="btn btn-success input-sm" id="cmdguardar"   value="Guardar" style="width:150px" />
  </div>


</form>
</body>
</html>
