$(document).ready(function(){ 
		
	$('#idtipogasto').change(function(){
		alert("Selecccion: "+ $(this).val());
		ActulizaListado($(this).val());
	})
	
});

function generarOPS(){
	
		var chkfacturas = [];
		
	    $('input[id=chkfacturas]:checked').each(function() { 
	    	chkfacturas.push(parseInt($(this).val()));
	    });	
	 
	    if (chkfacturas.length>0){
	    	parent.generarOPS(chkfacturas);
		}else 
				jAlert('Es necesario seleccionar por lo menos una Requisicion del listado', 'Advertencia');
}
	
function ActulizaListado(id){
	
	document.location="../../sam/consultas/muestra_dev_op.action?idtipogasto="+id;
	
}




