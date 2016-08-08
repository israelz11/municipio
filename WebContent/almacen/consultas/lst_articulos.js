$(document).ready(function() {
	$('#cmdbuscar').click(function (event){iniciarBusqueda();});
	$('#cmdcancelar').click(function(event){cancelarArticulos();});
	$('#cmdreporte').click(function(event){getReportePDF();});
	$('#cbodependencia').change(function (event){cargaraAlmacenes($('#cbodependencia').val());});
  	getBeneficiarios('txtbeneficiario','ID_PROVEEDOR');
  	getUnidad_Medidas('txtunidadmedida','ID_UNIDAD_MEDIDA');
});

function cancelarArticulos(){
	 var checkClaves = [];
     $('input[name=chkarticulos]:checked').each(function() { checkClaves.push($(this).val());});	
	 if (checkClaves.length>0){
		jConfirm('¿Confirma que desea cancelar los árticulos seleccionados en el inventario actual?','Cancelar articulos', function(r){
			if(r){
					 controladorListadoArticulosInventarioRemoto.cancelarArticulosInventario(checkClaves, {
						callback:function(items) { 		
						  CloseDelay('Articulos cancelados con exito');
						  iniciarBusqueda();
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
				    });
			}
	   },async=false );
	 
	 } 
	else 
	    jAlert('Es necesario seleccionar por lo menos un árticulo del listado', 'Advertencia');
}


function cargaraAlmacenes(idDependencia) {
     dwr.util.removeAllOptions("cboalmacen");
	 controladorListadoArticulosInventarioRemoto.getAlmacenes(idDependencia, {
        callback:function(items) { 		
			dwr.util.addOptions('cboalmacen',{ 0:'[Seleccione]'});
			dwr.util.addOptions('cboalmacen',items,"ID_ALMACEN", "DESCRIPCION");
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(errorString,'Error');          
        }
    },async=false ); 
 }
 

function iniciarBusqueda(){
	//if($('#cboalmacen').attr('value')==0) {jAlert('Seleccione un almecén para realizar esta operación', 'Advertencia'); return false};
	if($('#txtbeneficiario').attr('value')=='') $('#ID_PROVEEDOR').attr('value', '0');
	if($('#txtunidadmedida').attr('value')=='') $('#ID_UNIDAD_MEDIDA').attr('value', '0');
	var s = '?cbodependencia='+$('#cbodependencia').attr('value')+'&id_almacen='+$('#cboalmacen').attr('value')+"&descripcion="+$('#txtdescripcion').attr('value')+"&folio="+$('#txtfolio').attr('value')+"&id_familia="+$('#cbofamilia').attr('value')+"&id_proveedor="+$('#ID_PROVEEDOR').attr('value')+"&id_unidad_medida="+$('#ID_UNIDAD_MEDIDA').attr('value');
	document.location = s;
}

function showMovto(id_inventario){
	jWindow('<iframe width="750" height="410" name="consultaMovimiento" id="consultaMovimiento" frameborder="0" src="../consultas/movimientos_articulos.action?idInventario='+id_inventario+'"></iframe>','Movimientos del artículo', '','Cerrar ',1);
}

function editarArticulo(id_inventario){
	document.location = '../entradas/configura_entradaArticulos.action?id_inventario='+id_inventario;
}

function getReportePDF(){
	$('#frmreporte').attr('target',"impresion");
	$('#frmreporte').submit();
	$('#frmreporte').attr('target',"");
}

function cancelarDocumento(id_documento){
		jConfirm('&iquest;Confirma que desea cancelar el documento actual?','Cancelar documento', function(r){
			if(r){
					 controladorListadoEntradasDocumentosRemoto.cancelarDocumento(id_documento, {
						callback:function(items) {
							if(items==true) {CloseDelay('El documento fue cancelado con exito'); iniciarBusqueda();}
							else jError('Ha ocurrido un error al cancelar el documento, la operacion no se ha podido completar','Error');
						  
					 } 					   				
					 ,
					 errorHandler:function(errorString, exception) { 
						jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
					 }
				    });
			}
	   });	 

}