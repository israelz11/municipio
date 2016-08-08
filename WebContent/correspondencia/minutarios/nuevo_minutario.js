//ISC. Israel de la Cruz, 08/Oct/2012
var CVE_PERS = [];
$(document).ready(function(){
	var options = { 
        //target:        '',   // target element(s) to be updated with server response 
        beforeSubmit:  showRequest,  // pre-submit callback 
        success:       showResponse,  // post-submit callback 
 
        // other available options: 
        url:       '_subirArchivo.action?ID_MINUTARIO='+$('#ID_MINUTARIO').attr('value'),  // override for form's 'action' attribute 
        type:      'post',        // 'get' or 'post', override for form's 'method' attribute 
        dataType:  'json'        // 'xml', 'script', or 'json' (expected server response type) 
        //clearForm: true        // clear all form fields after successful submit 
        //resetForm: true        // reset the form after successful submit 
 
        // $.ajax options can be used here too, for example: 
        //timeout:   3000 
    }; 
	
	$('#forma').submit(function(){
		$(this).ajaxSubmit(options);
		return false;
	});
	
	 /*Configura los tabuladores*/
	 $('#tabuladores').tabs();
	 $('#tabuladores').tabs('option', 'selected', 0);
	 $('#tabuladores').tabs('option', 'disabled', [1]);

	 $('#detalles_ccp').hide();
	 /*Manejadores de eventos*/
	 $('#cbodependencia').change(function(event){getPersonasMinutarios();});
	 $('#cbodestino').change(function(event){getPersonasMinutarioDestino(0)});
	 $('#cmdguardar').click(function(event){guardarMinutario();});
	 $('#cmdcerrar').click(function (event){cerrarMinutario();});
	 $('#cmdnuevo').click(function(event){limpiarControles();});
	 $('#cmdsubir').click(function(event){subirArchivo();});
	 //$('#cmdsubir').click(function(event){$('#forma').submit();});
	 /*autocompletado*/
	 getPersonasSubdireccion('txtccp','CVE_PERS');
	 getDocumentos('txtdocumento','ID_CORRESPONDENCIA');
	if($('#ID_MINUTARIO').attr('value')!='0'){
		getClavesCCP($('#ID_MINUTARIO').attr('value'));
		mostrarDetallesCCP();
		mostrarDetallesArchivos($('#ID_MINUTARIO').attr('value'));
		$('#tabuladores').tabs('enable',1);
		$('#cboaño').attr('disabled', true);
		$('#cmdcerrar').attr('disabled', false);
	}
	else
	{
		$('#cmdcerrar').attr('disabled', true);
	}
});


function cerrarMinutario(){
	jConfirm('¿Confirma que desea cerrar el minutario?','Cerrar', function(r){
		if(r){
			ControladorNuevoMinutarioRemoto.cerrarMinutario($('#ID_MINUTARIO').attr('value'), {
								callback:function(items) {
										CloseDelay("Minutario cerrado con éxito", function(){
												document.location = "lst_minutarios.action?txtnumero="+$('#NUMERO').attr('value')+"&cbominutario=-1";
											});
								}
								,
								errorHandler:function(errorString, exception) { 
									jError(errorString, 'Error');          
								}
				});
		}
	});
}

function subirArchivo(){
	ShowDelay("Subiendo archivo al servidor");
	$('#forma').submit();
}

function showRequest(formData, jqForm, options) { 
    return true; 
} 
 
function showResponse(data)  { 
 	if(data.mensaje){
		CloseDelay("Archivo guardado con éxito");
		mostrarDetallesArchivos($('#ID_MINUTARIO').attr('value'));
		$('#archivo').attr('value','');
	}
	else{
		_closeDelay();
		jError("No se ha podido cargar el archivo, consulte a su administrador", "Error");
	}
} 

function mostrarDetallesArchivos(idMinutario){
	quitRow("detalle_archivo");
	ControladorNuevoMinutarioRemoto.getArchivosMinutario(idMinutario, {
						callback:function(items) {
								jQuery.each(items,function(i){
									pintaTablaDetallesArchivos(this);	
								});
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString,"Error");          
					}
	});
}

function pintaTablaDetallesArchivos(m){
	 var htmlRemove = "<img src=\"../../imagenes/cross.png\" style='cursor: pointer;' alt=\"Eliminar\" width=\"16\" height=\"16\" border=\"0\" onClick=\"eliminarArchivoMinutario("+m.ID_ARCHIVO+")\" >";
	appendNewRow("detalle_archivo", [Td('', izquierda , '', '<a href="../'+m.RUTA+'['+m.ID_ARCHIVO+'] '+m.NOMBRE+'" target="_blank">['+m.ID_ARCHIVO+'] '+m.NOMBRE+'</a>'),
						 Td('', centro , '', m.EXT),
						  Td('', centro , '', parseInt(parseInt(m.TAMAÑO)/1024)+' kb'),
						 Td('', centro , '', htmlRemove)
				]);
}

function eliminarArchivoMinutario(idArchivo){
	
	jConfirm('¿Confirma que desea eliminar el archivo?','Eliminar', function(r){
		if(r){
				ShowDelay("Eliminando archivo");
				ControladorNuevoMinutarioRemoto.eliminarArchivoMinutario(idArchivo,{
						callback:function(map) {
							CloseDelay("Archivos eliminado con éxito");
							mostrarDetallesArchivos($('#ID_MINUTARIO').attr('value'));
						},
						errorHandler:function(errorString, exception) { 
												jError(errorString, 'Error');          
						}
				});
		}
	});
}
	
function getCargarMinutario(idMinutario){
	ControladorNuevoMinutarioRemoto.getCargarMinutario(idMinutario,{
			callback:function(map) {
				$('#div_minutario').html("<strong>"+rellenaCeros(map.NUMERO.toString(),6)+"</strong>");
				getCatalogoMinutarios(map.ID_DEPENDENCIA_ENVIA,map.ID_CAT_MINUTARIO );
				getPersonasMinutarioDestino(map.CVE_PERS_DEST);
				getClavesCCP(idMinutario);
				mostrarDetallesArchivos(idMinutario);
				$('#tabuladores').tabs('enable',1);
			},
			errorHandler:function(errorString, exception) { 
									jError(errorString, 'Error');          
			}
	});
}

function getClavesCCP(idMinutario){
	ControladorNuevoMinutarioRemoto.getClavesPersonasCCP(idMinutario,{
					callback:function(items) {
					 		CVE_PERS = [];
							if(items!="") CVE_PERS = items.split(",");
							mostrarDetallesCCP();
					}
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					}
	});
}

function getCatalogoMinutarios(idDependencia, idCatMinutario){
	ControladorNuevoMinutarioRemoto.getMinutariosCombo(idDependencia, idCatMinutario,{
					callback:function(items) {
					 		$('#cbominutario').html(items);
							_closeDelay();
					}
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					}
	});
}
	
function guardarMinutario(){
	if($('#cboaño').val()==0){jAlert('Es necesario seleccionar el año de la lista','Advertencia'); return false;}
	if($('#cbominutario').val()==0){jAlert('Es necesario seleccionar un Minutario de la lista','Advertencia'); return false;}
	if($('#cbodestino').val()==0){jAlert('Es necesario seleccionat la Unidad Administrativa destino','Advertencia'); return false;}
	if($('#txtasunto').attr('value')=='') {jAlert('Es necesario escribir el asunto del minutario','Advertencia'); return false;}
	var idMinutario = $('#ID_MINUTARIO').attr('value');
	var año = $('#cboaño').attr('value');
	var idDependenciaFuente = $('#cbodependencia').attr('value');
	var idMinutarioFuente = $('#cbominutario').attr('value');
	var idDependenciaDestino = $('#cbodestino').attr('value');
	var cve_persDestino = $('#cbosubdestino').attr('value');
	var idClasifica = $('#cboclasifica').attr('value');
	var idCorrespondencia = $('#ID_CORRESPONDENCIA').attr('value');
	var asunto = $('#txtasunto').attr('value');
	jConfirm('¿Confirma que desea guardar el minutario?','Guardar', function(r){
		if(r){
				ControladorNuevoMinutarioRemoto.guardarMinutario(idMinutario, año, idDependenciaFuente, idMinutarioFuente, idDependenciaDestino, cve_persDestino, idClasifica, idCorrespondencia, asunto, $('#CVE_PERS').attr('value'), {
								callback:function(items) {
										$('#div_minutario').html("<strong>"+items.NUMERO+"</strong>");
										$('#ID_MINUTARIO').attr('value', items.ID_MINUTARIO);
										CVE_PERS = [];
										if(items.CCP!="") CVE_PERS = items.CCP.split(",");
										mostrarDetallesCCP();
										CloseDelay("Minutario guardado con éxito", function(){
												$('#tabuladores').tabs('enable',1);
												if(idMinutario==0)
													$('#tabuladores').tabs('option', 'selected', 1);
											});
										//document.location = 'nuevo_minutario.action?idMinutario='+items.ID_MINUTARIO;
								}
								,
								errorHandler:function(errorString, exception) { 
									jError(errorString, 'Error');          
								}
				});
		}
	});
}

function limpiarControles(){
	$('#ID_MINUTARIO').attr('value', '0');
	$('#NUMERO').attr('value', '');
	$('#div_minutario').html("");
	$('#cbodependencia').val(0);
	$('#cbominutario').val(0);
	$('#cbodestino').val(0);
	$('#cboclasifica').val(0);
	$('#cbosubdestino').html("");
	$('#txtdocumento').attr('value', '');
	$('#ID_CORRESPONDENCIA').attr('value', '0');
	$('#txtasunto').attr('value', '');
	$('#txtccp').attr('value', '');
	$('#cmdcerrar').attr('disabled', true);
	$('#CVE_PERS').attr('value','');
	CVE_PERS = [];
	$('#detalles_ccp').hide();
	quitRow("detalles_ccp");
}
	
function getPersonasMinutarioDestino(idPersonaDest){
	ShowDelay("Cargando minutarios");
	var idDependencia = $('#cbodestino').val();
	ControladorNuevoMinutarioRemoto.getMinutariosDestinoCombo(idDependencia, idPersonaDest, {
					callback:function(items) {
					 		$('#cbosubdestino').html(items);
							_closeDelay();
					}
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					}
	});
}

function getPersonasMinutarios(){
	ShowDelay("Cargando minutarios");
	var idDependencia = $('#cbodependencia').val();
	ControladorNuevoMinutarioRemoto.getMinutariosCombo(idDependencia, 0, {
					callback:function(items) {
					 		$('#cbominutario').html(items);
							_closeDelay();
					}
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString, 'Error');          
					}
	});
}

function mostrarDetallesCCP(){
	$('#txtccp').attr('value','');
	quitRow("detalles_ccp");
	$('#detalles_ccp').hide();
	if(CVE_PERS.length>0){
		$('#detalles_ccp').show();
		ControladorNuevoMinutarioRemoto.getListaPersonas(CVE_PERS, {
							callback:function(items) {
									jQuery.each(items,function(i){
										pintaTablaDetallesCCP(this);	
									});
						} 					   				
						,
						errorHandler:function(errorString, exception) { 
							jError(errorString,"Error");          
						}
		});
	}
}

function addArray(valor){
	var c =0;
	var idMinutario = $('#ID_MINUTARIO').attr('value');
	var existe = false;
	if(idMinutario==0){
		if(CVE_PERS.length>0){
			jQuery.each(CVE_PERS, function(c) {
				c++;
				if(valor==this)
					existe = true;
				if(!existe&&CVE_PERS.length==c){
					CVE_PERS.push(valor);
					$('#CVE_PERS').attr('value',CVE_PERS);
				}
			});
		}
		else{
			CVE_PERS.push(valor);
			$('#CVE_PERS').attr('value',CVE_PERS);
		}
	}
	else{
		ControladorNuevoMinutarioRemoto.agregarCCP(valor,idMinutario,{
			callback:function(items) { 
				CVE_PERS = [];
				if(items!="") CVE_PERS = items.split(",");
				mostrarDetallesCCP();
			} 					   				
			,
			errorHandler:function(errorString, exception) { 
				jError(errorString, "Error");          				     	  
			}
		});
	}
}

function eliminarPersona(cve){
	var c =0;
	var idMinutario = $('#ID_MINUTARIO').attr('value');
	if(idMinutario==0){
		jQuery.each(CVE_PERS, function(c) {
			if(cve==this){
				CVE_PERS.splice(c,1);
				$('#CVE_PERS').attr('value',CVE_PERS);
			}
			c++;
		});
		mostrarDetallesCCP();
	}
	else{
		jConfirm('¿Confirma que desea guardar el minutario?','Guardar', function(r){
			if(r){
				ShowDelay('Eliminando persona en detalle C.C.P');
				ControladorNuevoMinutarioRemoto.eliminarCCP(cve, idMinutario, {
					callback:function(items) { 
						jQuery.each(CVE_PERS, function(c) {
							if(cve==this){
								CVE_PERS.splice(c,1);
								$('#CVE_PERS').attr('value',CVE_PERS);
							}
							c++;
						});
						mostrarDetallesCCP();
						CloseDelay("Persona eliminada con éxito");
					} 					   				
					,
					errorHandler:function(errorString, exception) { 
						jError(errorString,"Advertencia");          				     	  
					}
				},async=false)
			}
		});
	}
}

function pintaTablaDetallesCCP(m){
	 var htmlRemove = "<img src=\"../../imagenes/cross.png\" style='cursor: pointer;' alt=\"Eliminar\" width=\"16\" height=\"16\" border=\"0\" onClick=\"eliminarPersona("+m.CVE_PERS+")\" >";
	appendNewRow("detalles_ccp", [Td('', izquierda , '', m.NOMBRE+" "+m.APE_PAT+" "+m.APE_MAT),
						 Td('', izquierda , '', m.DEPENDENCIA+" / "+m.SUBDIRECCION),
						 Td('', centro , '', htmlRemove)
				]);
}
/*DOCUMENTO DE CORRESPONDENCIA*/
function getDocumentos(edit,clave){
	ControladorNuevoMinutarioRemoto.getAutocompleteDocumentos({
        callback:function(items) { 
         	autocompleteDocumentos(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: ControladorNuevoMinutarioRemoto.getDocumentos');          				     	  
        }
    },async=false); 			 
}

function  autocompleteDocumentos(edit,clave,data) {
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
					value: row.NUMERO,
					result: row.NUMERO 
				}
			}); 
		},
		formatItem: function(item) {
			return format_getDocumentos(item);
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.ID_CORRESPONDENCIA);
	});
}

function format_getDocumentos(doc){
	return doc.NUMERO;
}


/*USUARIOS PERSONAS CORRESPONDENCIA*/
function getPersonasSubdireccion(edit,clave){
	ControladorNuevoMinutarioRemoto.getPersonasSubdireccion({
        callback:function(items) { 
         	autocompletePersonasSubdireccion(edit,clave,items);
        } 					   				
        ,
        errorHandler:function(errorString, exception) { 
			jError(exception.message+'<br><br>'+exception.javaClassName,'Error: ControladorNuevoMinutarioRemoto.getPersonasSubdireccion');          				     	  
        }
    },async=false); 			 
}

function  autocompletePersonasSubdireccion(edit,clave,data) {
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
			return format_personasSubdireccion(item);
		}
}).result(function(e, item) {
	if (clave!='') 
		$("#"+clave).attr("value",item.CVE_PERS);
		addArray(item.CVE_PERS);
		mostrarDetallesCCP();
	});
}

function format_personasSubdireccion(persona){
	return persona.NOMBRE+" "+persona.APE_PAT+" "+persona.APE_MAT+" ["+persona.DEPENDENCIA+"/"+persona.SUBDIRECCION+"]";
}
