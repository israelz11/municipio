/* Carga inicial del los metodos utilizados */
$(document).ready(function() {  
  $('#tabsOrdenesEnca').hide();
  $("#fecha").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
  $('#tabsOrdenes').tabs();  
  getBeneficiarios('xBeneficiario','xClaveBen',$('#tipoBeneficiario').attr('value'));
  llenarTablaDeOrdenes();   
});

/* Captura de encabezado de orden de pago*/
function limpiarForma(){	
	$('#id_orden').attr('value','');
	$('#fecha').attr('value','');
	$('#xImporteIva').attr('value','');	
	$('#xBeneficiario').attr('value','');
	$('#xClaveBen').attr('value','');
	$('#xConcurso').attr('value','');
	$('#xNota').attr('value','');
	$('#NoOrden').text("");
	$('#NoFolio').text("");
	$('#reembolso').attr('checked',false);
	quitRow("listaPedidos");
	quitRow("listasDetallesOrdenes");
	quitRow("listasDocumentos");
	quitRow("listasRetenciones");
	$('#tabsOrdenes').tabs('option', 'disabled', [1,2,3,4,5]);
	$('#btnCerrar').attr('disabled',true);
	lipiarDocumento();	
	limpiarRetencion ();
}

function guardar ( ) {
   var error="";  
    if ($('#fecha').attr('value')=="") error+="-Fecha de la orden</br>";
    if ($('#xClaveBen').attr('value')=="") error+="-Beneficiario</br>";
    if ($('#xNota').attr('value')=="") error+="-Concepto del la orden</br>";
    if (error==""){	
	  ordenPagoMultiplePedidos.guardarOrden( $('#id_orden').attr('value'),$('#xTipo').attr('value'),$('#fecha').attr('value'),$('#xClaveBen').attr('value'),null, $('#reembolso').attr('value'),$('#xConcurso').attr('value'),$('#xNota').attr('value'),$('#estatus').attr('value'),null, $('#xImporteIva').attr('value'),$('#cbUnidad').attr('value'),{
      callback:function(items) { 	    
	  $('#id_orden').attr('value',items);
      //$('#tabsOrdenes').tabs('option', 'enabled' ,[1,3,4]); 
	  $('#tabsOrdenes').tabs( 'enable' , 1);
	  $('#tabsOrdenes').tabs( 'enable' , 3);
	  $('#tabsOrdenes').tabs( 'enable' , 4);
  	  $('#tabsOrdenes').tabs( 'enable' , 5);
	  $('#tabsOrdenes').tabs('option', 'selected', 1);
	  llenarTablaDePedidos(0);
 	  jInformation("La orden se almaceno satisfactoriamente");	  
    } 					   				
    ,
    errorHandler:function(errorString, exception) { 
	    jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");       
    }
}); }else 
	  jalert("Los siguientes datos son requeridos::<br>"+error);
	  
}

function cerrarOrden( ) {
	ordenPagoMultiplePedidos.cerrarOrden( $('#id_orden').attr('value'), {
    callback:function(items) { 	    	
	  jInformation("Se cerro satisfactoriamente la orden");
     $('#btnCerrar').attr('disabled',true);
	  limpiarForma();
    } 					   				
    ,
    errorHandler:function(errorString, exception) { 
	    jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");       
    }
});
}

function buscarOrden( idOrden ) {	
	ordenPagoMultiplePedidos.getOrden(idOrden,{
    callback:function(items) { 	   
	$('#id_orden').attr('value',items.CVE_OP);
	$('#NoOrden').text(items.NUM_OP);
	$('#NoFolio').text(items.FOLIO);
	$('#xTipo').attr('value',items.TIPO);
	$('#fecha').attr('value',items.FECHA);
	$('#xClaveBen').attr('value',items.CLV_BENEFI);
	$('#xBeneficiario').attr('value',items.NCOMERCIA);	
	if (items.REEMBOLSOF=='S')
	    $('#reembolso').attr('checked',true);
	$('#xConcurso').attr('value',items.CONCURSO);
	$('#xNota').attr('value',items.NOTA);
	$('#estatus').attr('value',items.STATUS);
	$('#xImporteIva').attr('value',items.IMPORTE_IVA);		
	nuevaOp();
	if (items.CLV_PARBIT != null  &&  items.CLV_PARBIT != 'null'  ) 
         $('#tipoGasto').val(items.CLV_PARBIT);	 	  	    	
	//$('#tabsOrdenes').tabs('option', 'enabled' ,[1,2,3,4]); 	
	$('#tabsOrdenes').tabs( 'enable' , 1);
	$('#tabsOrdenes').tabs( 'enable' , 2);
	$('#tabsOrdenes').tabs( 'enable' , 3);
	$('#tabsOrdenes').tabs( 'enable' , 4);	
	$('#tabsOrdenes').tabs( 'enable' , 5);	
	llenarTablaDePedidos(1);	
    } 					   				
    ,
    errorHandler:function(errorString, exception) { 
        jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
}
});
}

 function regresar(){
    $('#tabsOrdenesEnca').hide(); 
	$('#listaOrdenesPendientes').show();		
//	$('#tabsOrdenes').tabs('option', 'enabled' ,[0,1,2,3,4]); 
    $('#tabsOrdenes').tabs( 'enable' , 0);
	$('#tabsOrdenes').tabs( 'enable' , 1);
	$('#tabsOrdenes').tabs( 'enable' , 2);
	$('#tabsOrdenes').tabs( 'enable' , 3);
	$('#tabsOrdenes').tabs( 'enable' , 4);	
	$('#tabsOrdenes').tabs( 'enable' , 5);	
	limpiarForma();
 }
/*Fin Captura de encabezado de orden de pago*/

/*Inicio Listado de pedidos*/
 function llenarTablaDePedidos(op) {
	 quitRow("listaPedidos");
	 var idOrden=$('#id_orden').attr('value');
	 var cveUnidad=$('#cbUnidad').attr('value');
	 var cveParbit=$('#tipoGasto').attr('value');
	 var cveBeneficiario=$('#xClaveBen').attr('value');
	 var ejercicio=$('#ejercicio').attr('value');
	ordenPagoMultiplePedidos.getPedidos(idOrden,cveUnidad ,cveParbit,cveBeneficiario,ejercicio, {
        callback:function(items) { 		
            jQuery.each(items,function(i) {
 		    pintaPedidos( "listaPedidos", i+1 ,this.CVE_PED,this.NUM_PED,this.NUM_REQ,this.FECHA,this.TOTAL,this.TIPO);
        }); 					   				
		if (op==1);	
		 llenarTablaDeDetallesOrdenes(op);		 
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
          jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); 

 }
  
  function pintaPedidos( table, consecutivo,id,numPed,numReq,fecha,total,tipo){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );    
	var checado="";
	if (tipo =='Asignado')
	   checado="checked";
    var html = "<input type='checkbox' name='claves' id='claves' value='"+id+"' "+checado+">";
	row.appendChild( Td("","","",html) );
	row.appendChild( Td(tipo,centro,"","") );
	row.appendChild( Td(numPed,"","","") );
	row.appendChild( Td(fecha,centro,"","") );
	row.appendChild( Td(numReq,centro,"","") );
	row.appendChild( Td(formatNumber(total,"$"),derecha,"","") );	
	tabla.appendChild( row );
 }


function guardarPedidos (){	 
	 var checkPedidos = [];
     $('input[name=claves]:checked').each(function() {  checkPedidos.push($(this).val());	 });	 
 	 var cveParbit=$('#tipoGasto').attr('value');
	// if (checkPedidos.length > 0 ) {
     ordenPagoMultiplePedidos.guardarPedidos(checkPedidos,$('#id_orden').attr('value'),cveParbit, {
        callback:function(items) { 		
		   jInformation("Se almacenaron satisfactoriamente los movimientos");
		   llenarTablaDeDetallesOrdenes(0);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); 
	/* } else
	    jInformation("Es necesario que seleccione un elemento de la lista");*/

}
/*Fin Listado de pedidos*/

/*Inicio Listado de ordenes*/
 function llenarTablaDeOrdenes() {
	 quitRow("listaOrdenes");
	 var tipo=$('#xTipo').attr('value');
	 var cveUnidad=$('#cbUnidad').attr('value');
	 var ejercicio=$('#ejercicio').attr('value');
	 var estatus=$('#estatus').attr('value');
	ordenPagoMultiplePedidos.getOrdenesTipo(tipo,cveUnidad ,ejercicio,estatus, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaOrdenes( "listaOrdenes", i+1,this.CVE_OP,this.NUM_OP,this.FECHA,this.DESCRIPCION_ESTATUS,this.NOTA);
        });
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); 

 }
 
  function pintaOrdenes( table, consecutivo,id,numOp,fecha,estatus,nota){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );    
    var html = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='buscarOrden("+id+")' >"; 	
	row.appendChild( Td(numOp,centro) );
	row.appendChild( Td(fecha,centro) );
	row.appendChild( Td(nota) );
	row.appendChild( Td(estatus) );
	row.appendChild( Td("","","",html) );
	tabla.appendChild( row );
 } 
 
 function nuevaOp(){
    $('#tabsOrdenesEnca').show(); 
	$('#listaOrdenesPendientes').hide();		
 }
/*Fin Listado de ordenes*/
 
 
 /*Inicio Listado de detalles de ordenes*/
 
  function llenarTablaDeDetallesOrdenes(op) {
	 quitRow("listasDetallesOrdenes");
	 var idOrden=$('#id_orden').attr('value');
	 ordenPagoMultiplePedidos.getDetallesOrdenes(idOrden, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaDetallesOrdenes( "listasDetallesOrdenes", i+1,this.PROYECTO,this.CLV_PARTID,this.MONTO);
        });
		getSelectGrados(items);
		 if (op==1) 
		   llenarTablaDeRetenciones(op);				   
		 
		if (items.length > 0) {
			$('#tabsOrdenes').tabs( 'enable' , 2);
		   //$('#tabsOrdenes').tabs('option', 'enabled' ,[2]); 
		   $('#btnCerrar').attr('disabled',false);
		   $('#tipoGasto').attr('disabled',true);
		}
		else {
		   $('#tabsOrdenes').tabs('option','disabled', [2]);
		   $('#btnCerrar').attr('disabled',true);
		   $('#tipoGasto').attr('disabled',false);		   
		}
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); 

 }
 
   function getSelectGrados(datos) {		
        lipiarVale();
		dwr.util.removeAllOptions("proyectoCuenta");
		dwr.util.addOptions('proyectoCuenta',{ 0:'Seleccione' });
		dwr.util.addOptions('proyectoCuenta',datos,"PROYECTOPARTIDA", "PROYECTOPARTIDA" );		
 }


  function pintaDetallesOrdenes( table, consecutivo,proyecto,partida,monto){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );    
	row.appendChild( Td(consecutivo,centro) );
	row.appendChild( Td(proyecto,centro) );
	row.appendChild( Td(partida,centro) );
	row.appendChild( Td(formatNumber(monto,"$"),derecha) );
	tabla.appendChild( row );
 }
 /*Fin Listado de detalles de ordenes*/
 
 
 /*Inicio de retenciones*/
   function llenarTablaDeRetenciones(op) {
	 quitRow("listasRetenciones");
	 var idOrden=$('#id_orden').attr('value');
	 ordenPagoMultiplePedidos.getTodasRetencionesOrdenes(idOrden, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaRetenciones( "listasRetenciones", i+1,this.RET_CONS,this.CLV_RETENC,this.RETENCION,this.IMPORTE);
        });
		if ( op==1 )
		  llenarTablaDeDocumentos();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); 

 }
 
  function pintaRetenciones( table, consecutivo,idRetencion,idTipoRetencion,retencion,importe){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );    
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='editarRetencion("+idRetencion+",\""+idTipoRetencion+"\","+importe+")' >"; 	
    var htmlCheck = "<input type='checkbox' name='clavesRetencion' id='clavesRetencion' value='"+idRetencion+"' >";
	row.appendChild( Td("","","",htmlCheck) );
	row.appendChild( Td(retencion,centro) );
	row.appendChild( Td(formatNumber(importe,"$"),derecha) );
	row.appendChild( Td("","","",htmlEdit) );
	tabla.appendChild( row );
 }
 
 function editarRetencion (idRetencion,idTipoRetencion,importe) {
 	$('#idRetencion').attr('value',idRetencion);
	$('#retencion').val(idTipoRetencion);
	if (importe <0 )
	  importe=importe*-1;
	$('#importeRetencion').attr('value',importe);
 } 
 
 
 function limpiarRetencion () {
 	$('#idRetencion').attr('value','');
	$('#importeRetencion').attr('value',"");
 }
 
 function guardarRetencion(){
	  var error="";  
  	 var cveParbit=$('#tipoGasto').attr('value');
 	 var idOrden=$('#id_orden').attr('value');
    if ($('#retencion').attr('value')=="") error+="-Tipo de retencion</br>";
    if ($('#importeRetencion').attr('value')=="") error+="-Importe de retencion</br>";
    if (error==""){	
	  ordenPagoMultiplePedidos.guardarRetencion( $('#idRetencion').attr('value'),$('#retencion').attr('value'),$('#importeRetencion').attr('value'),cveParbit,idOrden,{
      callback:function(items) { 	 
	  if (items==true) {
	    llenarTablaDeRetenciones(0);
 	    jInformation("La retención se almaceno satisfactoriamente");	  	  
		limpiarRetencion();
	  } else
        jInformation("La retención ya se encuentra en la relacion de la orden de pago");	  	  	  
    } 					   				
    ,
    errorHandler:function(errorString, exception) { 
	    jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");       
    }
}); }else 
	  jalert("Los siguientes datos son requeridos::<br>"+error);	  
 }
 
 function eliminarRetencion(){
	  var checkRetenciones = [];
     $('input[name=clavesRetencion]:checked').each(function() {  checkRetenciones.push($(this).val());	 });	 
 	 var cveParbit=$('#tipoGasto').attr('value');
  	 var idOrden=$('#id_orden').attr('value');
	 if (checkRetenciones.length > 0 ) {
     ordenPagoMultiplePedidos.eliminarRetenciones(checkRetenciones,idOrden, {
        callback:function(items) { 		
		   jInformation("Se eliminaron satisfactoriamente los movimientos");
		   llenarTablaDeRetenciones(0);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); } else 
	    jInformation("Es necesario que seleccione un elemento de la lista");

	 }
 /*Fin de retenciones*/
 
 
 /*Inicio de Documentos*/
    function llenarTablaDeDocumentos() {
	 quitRow("listasDocumentos");
	 var idOrden=$('#id_orden').attr('value');
	 ordenPagoMultiplePedidos.getDocumentosOrdenes(idOrden, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaDocumentos( "listasDocumentos", i+1,this.ANX_CONS,this.T_DOCTO,this.NUMERO,this.NOTAS,this.DESCR);			
        });
		llenarTablaDeVales();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); 
 }
 
  function pintaDocumentos( table, consecutivo,idDocumento,tipoMovimiento,numero,nota,desTipoDoc){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );  
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='editarDocumento("+idDocumento+",\""+tipoMovimiento+"\",\""+numero+"\",\""+nota+"\")' >"; 	
    var htmlCheck = "<input type='checkbox' name='clavesDocumentos' id='clavesDocumentos' value='"+idDocumento+"' >";
	row.appendChild( Td("","","",htmlCheck) );
	row.appendChild( Td(desTipoDoc,centro) );
	row.appendChild( Td(numero,derecha) );
	row.appendChild( Td(nota,centro) );
	row.appendChild( Td("","","",htmlEdit) );
	tabla.appendChild( row );
 }
 
 function editarDocumento(idDocumento,tipoMovimiento,numero,nota){
	$('#idDocumento').attr('value',idDocumento);
	$('#tipoMovDoc').val(tipoMovimiento);
	$('#numeroDoc').attr('value',numero);
	$('#notaDoc').attr('value',nota);
 } 
 
 function lipiarDocumento(){
	$('#idDocumento').attr('value',"");
	$('#numeroDoc').attr('value',"");
	$('#notaDoc').attr('value',"");
 }
 
 function guardarDocumento(){
	 var error="";  
    if ($('#tipoMovDoc').attr('value')=="") error+="-Tipo Documento</br>";
    if ($('#numeroDoc').attr('value')=="") error+="-Numero Documento</br>";
    if ($('#notaDoc').attr('value')=="") error+="-Nota Documento</br>";
    if (error==""){	
	  var idOrden=$('#id_orden').attr('value');
	  ordenPagoMultiplePedidos.guardarDocumento( $('#idDocumento').attr('value'),$('#tipoMovDoc').attr('value'),$('#numeroDoc').attr('value'),$('#notaDoc').attr('value'),idOrden,{
      callback:function(items) { 	    
 	  jInformation("La documentación se almaceno satisfactoriamente");	  
	  llenarTablaDeDocumentos();
	  lipiarDocumento();
    } 					   				
    ,
    errorHandler:function(errorString, exception) { 
	    jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
    }
	}); }else 
	  jalert("Los siguientes datos son requeridos::<br>"+error);	   
 }
 
 function eliminarDocumentos(){
	  var checkDocumentos = [];
     $('input[name=clavesDocumentos]:checked').each(function() {  checkDocumentos.push($(this).val());	 });	 
	 if (checkDocumentos.length > 0 ) {
   	 var idOrden=$('#id_orden').attr('value');
     ordenPagoMultiplePedidos.eliminarDocumentos(checkDocumentos,idOrden, {
        callback:function(items) { 		
		   jInformation("Se eliminaron satisfactoriamente los movimientos");
		   llenarTablaDeDocumentos();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");                
        }
    }); 
	 } else 
	    jInformation("Es necesario que seleccione un elemento de la lista");
 }
 /*Fin de Documentos*/
 
 
 /*Inicio de Vales*/
    function llenarTablaDeVales() {
	 quitRow("listasVales");
	 var idOrden=$('#id_orden').attr('value');
	 ordenPagoMultiplePedidos.getValesOrdenes(idOrden, {
        callback:function(items) { 		
		jQuery.each(items,function(i) {
 		    pintaVales( "listasVales", i+1,this.CONS_VALE,this.PROYECTO,this.CLV_PARTID,this.CVE_VALE,this.NUM_VALE,this.IMPORTE);
        });
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); 
 }
 
  function pintaVales( table, consecutivo,idVale,proyecto,partida,vale,numeroVale,importe){
 	var tabla = document.getElementById( table ).tBodies[0];
 	var row =   document.createElement( "TR" );  
    var htmlEdit = "<img src=\"../../imagenes/page_white_edit.png\" style='cursor: pointer;' alt=\"Editar registro\" width=\"16\" height=\"16\" border=\"0\" onClick='editarVale("+idVale+","+vale+","+importe+",\""+proyecto+"-"+partida+"\")' >"; 	
    var htmlCheck = "<input type='checkbox' name='clavesVales' id='clavesVales' value='"+idVale+"' >";
	row.appendChild( Td("","","",htmlCheck) );
	row.appendChild( Td(proyecto,centro) );
	row.appendChild( Td(partida,centro) );
	row.appendChild( Td(numeroVale,centro) );
	row.appendChild( Td(formatNumber(importe,"$"),derecha) );
	row.appendChild( Td("","","",htmlEdit) );
	tabla.appendChild( row );
 }
 
 function editarVale(idVale,vale,importe,proyectoCuenta){
	$('#idVale').attr('value',idVale);
	$('#proyectoCuenta').val(proyectoCuenta);
	$('#importeVale').attr('value',importe);	
	$('#importeAntVale').attr('value',importe);	
	cargarVales(vale);
 } 
 
 function lipiarVale(){
	$('#idVale').attr('value',"");
	$('#importeVale').attr('value',"");
	$('#claveVale').attr('value',"");
	$('#importeAntVale').attr('value',"0");	
	$('#proyectoCuenta').val("0");
	dwr.util.removeAllOptions("claveValeDis");
	$('#textImporteDisponible').text("");	 
 }
 
 function guardarVale(){
	var error="";  
    if ($('#proyectoCuenta').attr('value')=="") error+="-ProyectoCuenta</br>";
    if ($('#claveValeDis').attr('value')=="") error+="-Vale</br>";
    if ($('#importeVale').attr('value')=="") error+="-Importe</br>";
	var datos= $('#claveValeDis').text().split('-');	 
    var textImporteDisponible = datos[1];
	if ($('#importeVale').attr('value') > textImporteDisponible  ) error+="-Importe  no debe ser mayor que el disponible</br>";
    if (error==""){	
 	  datos= $('#proyectoCuenta').attr('value').split('-');	 
	  var proyecto = datos[0];
	  var partida = datos[1];	 	 
	  var idOrden=$('#id_orden').attr('value');
	  ordenPagoMultiplePedidos.guardarVale( $('#idVale').attr('value'),$('#claveValeDis').attr('value'),$('#importeVale').attr('value'),textImporteDisponible,idOrden,proyecto,partida,$('#importeAntVale').attr('value'), {
      callback:function(items) { 	    
 	  jInformation("La informacion se almaceno satisfactoriamente");	  
	  llenarTablaDeVales();
	  lipiarVale();
    } 					   				
    ,
    errorHandler:function(errorString, exception) { 
	    jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
    }
	}); }else 
	  jalert("Los siguientes datos son requeridos::<br>"+error);	   
 }
 
 function eliminarVales(){
	  var checkVales = [];
     $('input[name=clavesVales]:checked').each(function() {  checkVales.push($(this).val());	 });	 
	 if (checkVales.length > 0 ) {
   	 var idOrden=$('#id_orden').attr('value');
     ordenPagoMultiplePedidos.eliminarVales(checkVales,idOrden, {
        callback:function(items) { 		
		   jInformation("Se eliminaron satisfactoriamente los movimientos");
		   llenarTablaDeDocumentos();
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); 
	 } else 
	    jInformation("Es necesario que seleccione un elemento de la lista");
 }
 
  function cargarVales(vale) {
     dwr.util.removeAllOptions("claveValeDis");
     if ($('#proyectoCuenta').attr('value')!=0) {
	 var datos= $('#proyectoCuenta').attr('value').split('-');	 
	 var proyecto = datos[0];
	 var partida = datos[1];
	 ordenPagoMultiplePedidos.getValesDisponibles(proyecto,partida,vale, {
        callback:function(items) { 				
		dwr.util.addOptions('claveValeDis',{ 0:'Seleccione' });
		dwr.util.addOptions('claveValeDis',items,"CVE_VALE", "DATOVALE" );						
		dwr.util.setValue('claveValeDis', vale);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
		jError("Fallo la operacion:<br>Error::"+errorString+"-message::"+exception.message+"-JavaClass::"+exception.javaClassName+".<br>Consulte a su administrador");          
        }
    }); } 
 }
 
 
  /*function cargarInformacionVales() {
	  var idVale = "";
	  var importe = "";
	  if ($('#claveValeDis').attr('value')!=0) {
	   var datos= $('#claveValeDis').attr('value').split('-');	 
	   idVale = datos[0];
	   importe = datos[1];	
	  }
    $('#textImporteDisponible').text(importe);	 
	$('#claveVale').attr('value',idVale);	 
 }*/
 
 
 /*Fin de Vales*/