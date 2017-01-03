
/*EVALUACION DE PROYECTOS*/
function getProyectosEval(edit,clave){
	autocompleteDiversosRemoto.getProyectosEval({
        callback:function(items) {
         autocompletProyectosEval(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: autocompleteDiversosRemoto.getUnidad_Medidas');          				     	  
        }
    },async=false); 			 
}

function  autocompletProyectosEval(edit,clave,data) {
	$("#"+edit).autocomplete(data, {
		matchCase:false,	
		max: 30, 				 
		parse: function(data) { 
			return $.map(eval(data), function(row) {
				return {
					data: row,
					value: row.N_PROGRAMA ,
					result: row.N_PROGRAMA
				}
			});
		},
		formatItem: function(item) {
			return item.N_PROGRAMA+"["+item.ID_PROYECTO+"]";
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.ID_PROYECTO);	
		//$("#button").click();	
	});
}


/*UNIDADES DE MEDIDA*/
function getUnidad_Medidas(edit,clave){
	autocompleteDiversosRemoto.getUnidadMedidasTodas({
        callback:function(items) {
         autocompletUnidad_Medidas(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: autocompleteDiversosRemoto.getUnidad_Medidas');          				     	  
        }
    },async=false); 			 
}

function  autocompletUnidad_Medidas(edit,clave,data) {
	$("#"+edit).autocomplete(data, {
		matchCase:false,					 
		parse: function(data) { 
			return $.map(eval(data), function(row) {
				return {
					data: row,
					value: row.UNIDMEDIDA ,
					result: row.UNIDMEDIDA
				}
			});
		},
		formatItem: function(item) {
			return formatUnidad_Medidas(item);
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.CLV_UNIMED);		
	});
}

function formatUnidad_Medidas(unidad) {
		return unidad.UNIDMEDIDA;
}

/*USUARIOS PERSONAS*/
function getPersonas(edit,clave, fn){
	autocompleteDiversosRemoto.getPersonas({
        callback:function(items) { 
         	autocompletePersonas(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: autocompleteDiversosRemoto.getPersonas');          				     	  
        }
    },async=false); 			 
}

function  autocompletePersonas(edit,clave,data) {
	$("#"+edit).autocomplete(data, {
		matchCase:false,
		max: 10,        
		highlightItem: true,        
		multiple: true,        
		multipleSeparator: " ", 					 
		parse: function(data) {
			return $.map(eval(data), function(row) {
				return {
					data: row,
					value: row.NOMBRE,
					result: row.NOMBRE + " "+ row.APE_PAT + " "+ row.APE_MAT
				}
			}); 
		},
		formatItem: function(item) {
			return format_personas(item);
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.CVE_PERS);
		$("#nombre").attr("value", item.NOMBRE);
		$("#apaterno").attr('value', item.APE_PAT);	
		$("#amaterno").attr('value', item.APE_MAT);	
		$('#curp').attr('value', item.CURP);
		$('#rfc').attr('value', item.RFC);
		$('#profesion').attr('value', getHTML(item.TRATAMIENTO));
		$('#unidad').attr('value', item.ID_DEPENDENCIA);

		area = item.CVE_UNIDAD;
		getSelectUnidad();
		$('#usuario').attr('value',item.LOGIN);
		 if (item.ACTIVO=='S')
		   $('#estatus').attr('checked',true);			 
		 else
		   $('#estatus').attr('checked',false);
		 $('#idUsuario').attr('value',item.ID_USUARIO);
		 $('#idTrabajador').attr('value',item.ID_TRABAJADOR);
	
	});
}

function format_personas(persona){
	return persona.NOMBRE+" "+persona.APE_PAT+" "+persona.APE_MAT+" ("+persona.LOGIN+") ["+persona.DEPENDENCIA+"]";
}

/*BENEFICIARIOS*/
function getBeneficiarios(edit,clave,tipo){
	autocompleteDiversosRemoto.getBeneficiariosTodos(tipo,{
        callback:function(items) { 		
         autocompleteBeneficiario(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: autocompleteDiversosRemoto.getBeneficiarios');          				     	  
        }
    },async=false); 			 
}

function format(benefi) {
		return benefi.NCOMERCIA+"(" +benefi.TIPOBENEFI +")";
}

function  autocompleteBeneficiario(edit,clave,data) {	
	$("#"+edit).autocomplete(data, {
		matchCase:false,
		max:100,			 
		parse: function(data) {
			return $.map(eval(data), function(row) {
				return {
					data: row,
					value: row.NCOMERCIA ,
					result: row.NCOMERCIA+"(" +row.TIPOBENEFI +")"
					
				}
			});
		},
		formatItem: function(item) {
			return format(item);
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.CLV_BENEFI);		
	});
}


/* PROYECTOS*/

function getProyectosUnidad(edit,clave,unidad){
	autocompleteDiversosRemoto.getProyectosPorUnidad(unidad,{
        callback:function(items) { 		
         autocompleteProyectosUnidad(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: autocompleteDiversosRemoto.getProyectosUnidad');  				     	  
        }
    },async=false); 			 
}



function formatProyecto(items) {
		return items.PROYECTO;
}

function  autocompleteProyectosUnidad(edit,clave,data) {	
	$("#"+edit).autocomplete(data, {
		matchCase:false,					 
		parse: function(data) {
			return $.map(eval(data), function(row) {
				return {
					data: row,
					value: row.PROYECTO ,
					result: row.PROYECTO
				}
			});
		},
		formatItem: function(item) {
			return formatProyecto(item);
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.DECRIPCION);		
	});
}

/*PARTIDAS*/

function getPartidasPorProyecto(edit,clave,proyecto){
	autocompleteDiversosRemoto.getPartidasPorProyecto(proyecto,{
        callback:function(items) { 		
         autocompletePartidasPorProyecto(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: autocompleteDiversosRemoto.getPartidasPorProyecto');
            //alert('Error:'+errorString);
            //alert('Exception message:'+exception.message);            
            //alert('JavaClassName:'+exception.javaClassName);            				     	  
        }
    },async=false); 			 
}

function formatPartidaProyecto(items) {
		return items.CLV_PARTID;
}

function  autocompletePartidasPorProyecto(edit,clave,data) {	
	$("#"+edit).autocomplete(data, {
		matchCase:false,					 
		parse: function(data) {
			return $.map(eval(data), function(row) {
				return {
					data: row,
					value: row.CLV_PARTID ,
					result: row.CLV_PARTID
				}
			});
		},
		formatItem: function(item) {
			return formatPartidaProyecto(item);
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.PARTIDA);		
	});
}

/*SUCURSALES BANCOS*/

function getSucursalesBancos(edit,clave){
	autocompleteDiversosRemoto.getBancosTodos({
        callback:function(items) { 		
         autocompleteSucursalesBancos(edit,clave,items);
        }
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: autocompleteDiversosRemoto.getSucursalesBancos');          				     	  
        }
    },async=false); 			 
}

function formatBanco(sucursal) {
		return sucursal.BANCO+"(" +sucursal.SUCURSAL+")";
}

function  autocompleteSucursalesBancos(edit,clave,data) {	
	$("#"+edit).autocomplete(data, {
		matchCase:false,					 
		parse: function(data) {
			return $.map(eval(data), function(row) {
				return {
					data: row,
					value: row.BANCO ,
					result: row.BANCO+"(" +row.SUCURSAL +")"
				}
			});
		},
		formatItem: function(item) {
			return formatBanco(item);
		}
}).result(function(e, item) {
	if (clave!='')
		$("#"+clave).attr("value",item.CLV_BNCSUC);		
	});
}

/* SUCURSALES BANCOS*/


