$(document).ready(function(){
	$('#cmdBuscar').on('click', function(){
		Buscar();
	});
	
	$('#cmdexportar').on('click', function(){
		
		GeneraExcel();
	});
});

function Buscar()
{
	$('#forma').submit();
}

function GeneraExcel()
{
	clavunidad=$('#cbUnidad').selectpicker('val');
	clavgasto=$('#cbotipogasto').selectpicker('val'); 
	clavproyecto=$('#txtproyecto').val();
	clavpartida=$('#txtpartida').val();
	
	
	//$('#CveOrdenOP').val($('#cve_op').val());
	
	$('#xidunidad').val(clavunidad);
	//alert("demo de la unidad " + clavunidad);
	$('#xidgasto').val(clavgasto);
	//alert("El tipo de gasto es" + clavgasto);
	$('#xproyecto').val(clavproyecto);
	$('#xpartida').val(clavpartida);
	
	$('#frmExcel').submit();
}