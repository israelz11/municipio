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
	clavgasto=$('#cbUnidad').selectpicker('val'); 
	clavproyecto=$('#txtproyecto').val();
	clavpartida=$('#txtpartida').val();
	
	
	//$('#CveOrdenOP').val($('#cve_op').val());
	
	$('#xidunidad').val(clavunidad);
	$('#xidgasto').val(clavgasto);
	$('#xproyecto').val(clavproyecto);
	$('#xpartida').val(clavpartida);
	
	$('#frmExcel').submit();
}