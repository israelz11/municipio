/**
Descripcion: Codigo controlador para la pagina beneficiario.jsp
Autor      : Mauricio Hernandez
Fecha      : 21/05/2010
*/

/**
*Al iniciar la pagina carga los eventos a los controles del formulario
*/

$(document).ready(function() {  
	 $('#tabuladores').tabs();
	 $('#tabuladores').tabs('enable',0);
	 $('#cmdcerrar').click(function (event){window.parent.compruebaVariable();});
  	 $('#cmdguardar').click(function(event){guardar();});
});

function limpiar(){
	 		 $('#idProveedor').attr('value','');
	 		 $('#clave').attr('value','');			 			 
			 $('#razonSocial').attr('value','');
			 $('#responsable').attr('value','');
			 $('#responsable2').attr('value','');
			 $('#rfc').attr('value','');
			 $('#curp').attr('value','');
			 $('#telefono').attr('value','');
			 $('#tipo').attr('value','');
			 $('#calle').attr('value','');
			 $('#colonia').attr('value','');
			 $('#ciudad').attr('value','');
			 $('#estado').attr('value','');
			 $('#cp').attr('value','');
			 $('#banco').attr('value','');
			 $('#idBanco').attr('value','');
			 $('#noCuenta').attr('value','');
			 $('#clabeb').attr('value','');
			 $('#tipoCuenta').attr('value','');
			 $('#beneficiarioPadre').attr('value','');
			 $('#idBeneficiarioPadre').attr('value','');
			 $('#vigencia').attr('checked',true);
			 quitRow("beneficiariosHijos");
			 $('#tr_hijos').hide();				 
			 //$('#tipo').attr('value','');
}


function verificarBanco() {
	if ($('#banco').attr('value')=="") {
	   $('#idBanco').attr('value','');
	   $('#noCuenta').attr('value','');
	   $('#tipoCuenta').attr('value','');
	}
}

function guardar(){			
    var error="";
	 var clave= $('#idProveedor').attr('value');			 			 
	 var razonSocial=$('#razonSocial').attr('value');
	 var responsable=$('#responsable').attr('value');
	 var responsable2=$('#responsable2').attr('value');
	 var rfc=$('#rfc').attr('value');
	 var curp=$('#curp').attr('value');
	 var telefono=$('#telefono').attr('value');
	 var tipo=$('#tipo').attr('value');
	 var calle=$('#calle').attr('value');
	 var colonia=$('#colonia').attr('value');
	 var ciudad=$('#ciudad').attr('value');
	 var estado=$('#estado').attr('value');
	 var cp=$('#cp').attr('value');
	 var idBanco=$('#cbobanco').attr('value');
	 var noCuenta=$('#noCuenta').attr('value');
	 var clabeb=$('#clabeb').attr('value');
	 var tipoCuenta=$('#tipoCuenta').attr('value');
	 var idBeneficiarioPadre=$('#idBeneficiarioPadre').attr('value');			 	 
	 if ( tipo=="") {jAlert('El Tipo de beneficiario no es válido', 'Advertencia'); return false;}
	 if(tipo=="PR"){
		 if ( rfc=="")  {jAlert('El RFC no es válido', 'Advertencia'); return false;}
		 if ( razonSocial=="")  {jAlert('La Razón Social no es válida', 'Advertencia'); return false;}
		 if ( responsable=="")  {jAlert('El Responsable no es válido', 'Advertencia'); return false;}
		 //if ( telefono=="")  {jAlert('El número de telefono no es válido', 'Advertencia'); return false;}
		 if (telefono.length!=10)  {jAlert('El número de telefono no es válido', 'Advertencia'); return false;}
		
		 if ( calle=="") {jAlert('La Calle no es válida', 'Advertencia'); return false;}
		 if ( colonia=="")  {jAlert('La Colonia no es válida', 'Advertencia'); return false;}
		 if ( estado=="")  {jAlert('El Estado no es válido', 'Advertencia'); return false;}
		 if ( ciudad=="")  {jAlert('La Ciudad no es válida', 'Advertencia'); return false;}
		 if ( cp=="")  {jAlert('El Codigo postal no es válido', 'Advertencia'); return false;}
		 if ( noCuenta=="") {alert('La Cuenta debe ser capturada.', 'Advertencia'); return false;}
		 if ( clabeb=="") {alert('La Cuenta debe ser capturada.', 'Advertencia'); return false;}
		 //if ((clabeb).val().length!=18) {alert("La CLABE debe tener 18 digitos", 'Advertencia'); return false;}
		 //if ( idBanco != "" && $('#noCuenta').attr('value') == ""  )  {jAlert('El Número de cuenta no es válido', 'Advertencia'); return false;}
		 
		
	 }
	 else
	 {
		 if ( razonSocial=="")  {jAlert('La Razón Social no es válida', 'Advertencia'); return false;}
	 }
	
	
	var vigencia='1';
	
	if (!$('#vigencia').attr('checked'))	
	   vigencia='0';
	
	ShowDelay('Guardando Beneficiario','');
    controladorBeneficiarioRemoto.guardarBeneficiario(clave,razonSocial,responsable,responsable2,rfc,curp,telefono,tipo,calle,colonia,ciudad,estado,cp,idBanco,noCuenta,tipoCuenta,idBeneficiarioPadre,vigencia,clabeb,{
			 callback:function(items) {
				 $('#idProveedor').attr('value', items);
				 window.parent.cambiarVariable(razonSocial);
				 swal("Good job!", "Beneficiario Guardado con éxito!", "success");
				 CloseDelay("Beneficiario guardado con éxito", 2000, function(){
					 limpiar();
					
					 buscarBeneficiario();
					 
				 });
 		     }	
			,errorHandler:function(errorString, exception) { 
			   jError(errorString, 'Error'); 
			}
		});		
}

 function buscarBeneficiario() {
	 if($('#razonSocial').attr('value')==''){jAlert('El nombre del beneficiario es un campo requerido, escriba un nombre válido','Advertencia'); return false;}
	 quitRow("beneficiarios");
	 var idBene;
	 ShowDelay('Buscando Beneficiario','');
	controladorBeneficiarioRemoto.getBeneficiarios($('#razonSocial').attr('value'), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
				_closeDelay();
			   idBene=this.ID_PROVEEDOR;
			 var domicilio = this.DOMIFISCAL+" "+this.COLONIA+" "+this.CIUDAD +" "+ this.ESTADO;
 		     pintaTabla( "beneficiarios", i+1 ,this.ID_PROVEEDOR,this.NCOMERCIA,this.RFC,domicilio,this.TIPOBENEFI,this.VIGENCIA);
        }); 					   						
			
			if (items.length > 0 ){ 
			  limpiar();
			  if (items.length!=1)
			   $('#formaBusqueda').show();
			    else {
				$('#formaBusqueda').hide();
			   editar(idBene);
				}
			  
			}
			else 
			      $('#formaBusqueda').hide();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 

 }

  function pintaTabla( table, consecutivo,id,nombre,rfc,domicilio,tipo,vigencia){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );
    var htmlCheck = "<input type='checkbox' name='claves' id='claves' value='"+id+"' >";
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick=\"editar("+id+")\" >"; 		
	if (table !='beneficiariosHijos')
	  row.appendChild( Td("",centro,"",htmlCheck) );
	else
   	   row.appendChild( Td(consecutivo,centro,"","") );
	row.appendChild( Td(nombre,"","","") );	  
	row.appendChild( Td(rfc,centro,"","") );	  
	row.appendChild( Td(domicilio,izquierda,"","") );
	row.appendChild( Td(tipo,centro,"","") );
	row.appendChild( Td(vigencia,centro,"","") );
	if (table !='beneficiariosHijos')
    row.appendChild( Td("",centro,"",htmlEdit) );	
	tabla.appendChild( row );
 }
 
 
 
 function buscarBeneficiarioHijos() {
	 quitRow("beneficiariosHijos");
	controladorBeneficiarioRemoto.getBeneficiariosHijos($('#clave').attr('value'), {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
		     var domicilio = this.DOMIFISCAL+" "+this.COLONIA+" "+this.CIUDAD +" "+ this.ESTADO;
 		     pintaTabla( "beneficiariosHijos", i+1 ,this.ID_PROVEEDOR,this.NCOMERCIA,this.RFC,domicilio,this.TIPOBENEFI,this.VIGENCIA);
        }); 					   						
			if (items.length > 0 ) 
			   $('#tr_hijos').show();
			else {
			   $('#tr_hijos').hide();			    
			}
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
           jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");      
        }
    }); 

 }
 
 function editar(id) {
	 ShowDelay('Cargando el beneficiario para edición','');	
	controladorBeneficiarioRemoto.getBeneficiario(id,{
    callback:function(items) { 
			 _closeDelay();	
			 $('#idProveedor').attr('value',items.ID_PROVEEDOR);
			 $('#clave').attr('value',items.CLV_BENEFI);
			 $('#razonSocial').attr('value',items.NCOMERCIA);
			 $('#responsable').attr('value',items.BENEFICIAR);
			 $('#responsable2').attr('value',getHTML(items.BENEFICIA2));
			 $('#rfc').attr('value',items.RFC);
			 $('#curp').attr('value',getHTML(items.CURP));
			 $('#telefono').attr('value',items.TELEFONOS);
			 $('#tipo').attr('value',items.TIPOBENEFI);
			 $('#calle').attr('value',items.DOMIFISCAL);
			 $('#colonia').attr('value',items.COLONIA);
			 $('#ciudad').attr('value',items.CIUDAD);
			 $('#estado').attr('value',items.ESTADO);
			 $('#cp').attr('value',items.CODIGOPOST);
			 $('#banco').attr('value',getHTML(items.BANCO));
			 $('#idBanco').attr('value',getHTML(items.CLV_BNCSUC));
			 $('#noCuenta').attr('value',getHTML(items.NUM_CTA));
			 $('#clabeb').attr('value',getHTML(items.CLABE));
			 $('#tipoCuenta').attr('value',getHTML(items.TIPO_CTA));
			 $('#beneficiarioPadre').attr('value',getHTML(items.BENEFICIARIO));
			 $('#idBeneficiarioPadre').attr('value',getHTML(items.CLAVE_PADRE));
			  if (items.VIGENCIA=='ACTIVO')
		        $('#vigencia').attr('checked',true);			 
		      else
		        $('#vigencia').attr('checked',false);		   
			    buscarBeneficiarioHijos();
    } 					   				
    ,
    errorHandler:function(errorString, exception) { 
        jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
}
},async=false );
	
}

 
 
  function eliminar(){
	  var checkBeneficiarios = [];
     $('input[name=claves]:checked').each(function() {checkBeneficiarios.push($(this).val());	 });	 
	 if (checkBeneficiarios.length > 0 ) {
	 ShowDelay('Eliminando movimientos de beneficiario','');
	 
     controladorBeneficiarioRemoto.eliminarBenificiario(checkBeneficiarios, {
        callback:function(items) {
		   CloseDelay("Se eliminaron satisfactoriamente los movimientos", 2000, function(){
			   	buscarBeneficiario();
			});
		   
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");    
        }
    }); } else 
	    jInformation("Es necesario que seleccione un elemento de la lista");

 }