$(document).ready(function(){ 
	
});

function generarOPS(checkFacturas){
	
	
	
	var checkDevengados = [];
	
    $('input[name=chkfacturas]:checked').each(function() { 
    	checkDevengados.push($(this).val());});	
	
    if (checkDevengados.length>0){
		
		$("input[id=chkrequisiciones]:checked").attr('checked', false);
		console.log("Facturas Selecciondas: "+checkDevengados);
		alert("Facturas Selecciondas: "+checkDevengados)
		
		//parent.generarOPS(checkFacturas);
		parent.generarOPS(checkFacturas);
		//alert("Factura: "+ cve_factura);
	}
	else 
			jAlert('Es necesario seleccionar por lo menos una Requisicion del listado', 'Advertencia');
}
	