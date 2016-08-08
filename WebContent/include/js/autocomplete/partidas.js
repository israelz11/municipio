function getPartidas(edit,clave){
	autocompleteDiversosRemoto.getPartidas({
        callback:function(items) { 		
         autocomplete(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
            alert('Error:'+errorString);
            alert('Exception message:'+exception.message);            
            alert('JavaClassName:'+exception.javaClassName);            				     	  
        }
    }); 			 
}

function format(benefi) {
		return benefi.CLAVE+"-" +benefi.DESCRIPCION+"";
}

function  autocomplete(edit,clave,data) {	
	$("#"+edit).autocomplete(data, {
		matchCase:false,					 
		parse: function(data) {
			return $.map(eval(data), function(row) {
				return {
					data: row,
					value: row.CLAVE ,
					result: row.CLAVE+"(" +benefi.DESCRIPCION +")"
				}
			});
		},
		formatItem: function(item) {
			return format(item);
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.CLAVE);		
	});
}


