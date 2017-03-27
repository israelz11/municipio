$(document).ready(function() {
   
        
   
	$(function() {
		       	
        $('#estatusr').multiselect({
            includeSelectAllOption: true,
            buttonWidth: '200px',
            dropRight: true,
            selectAllValue: 1,
            
        	onChange: function(option, checked, select,deselected) {
            	alert('Status:  ' + $(option).val());
        }
        });

        $("#estatusr").multiselect('selectAll', false);
        $("#estatusr").multiselect('updateButtonText');
       
    });
	
	
    	
	
	$(document).ready(function() {
	    $('#btnBuscar').click(function(){
	      
	    });
	});
});  
function getListaReq2(){
		
	$('input[name=status]:checked').each(function() {checkStatus.push($(this).val());});	
    $(document).ready( function() {
    	var checkStatus = [];
    	var options = '';
    	
    	  $('#estatusr option:selected').each(function() {
    		 
    		  $("input:checkbox[name=type]:checked").each(function(){ 
    		
    			  checkStatus.push($(this).val()); //Agrega al array el valor
    				
    		  }); 
    		  options += $(this).val() + ', ';
    	  });
    	  if (options.length==0 )
    		  
    		  alert('Debe de seleccionar un Estatus' + options.substr(0, options.length - 1));
    	  
    	  else
    		  
    	  alert('Las estatus seleccionadas son: ' +options.substr(0, options.length - 2));  
    });
	
	 
	 
}


$(function() {
	
	$('#cbostatus').multiselect();
	alert('prueba que se corre');
	  $('.cbostatus').on('change', function(){
	    
		  var selected = $(this).find("option:selected").attr("value");
		  
		  	alert(selected);
	  });
	  
	});
