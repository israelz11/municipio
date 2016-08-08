//ISC. Israel de la Cruz, 24/NOV/2012
var CVE_PERS = [];
$(document).ready(function(){
	var options = { 
        beforeSubmit:  showRequest,  
        success:       showResponse, 
        url:       '_subirArchivo.action?ID_CORRESPONDENCIA='+$('#ID_CORRESPONDENCIA').attr('value'),
        type:      'post', 
        dataType:  'json'
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
	 
	 $("#txtfechaRecepcion").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy', minDate: 0});
	 $("#txtfechaDocumento").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy'});
	 $("#txtfechaLimite").datepicker({showOn: 'button', buttonImage: '../../imagenes/cal.gif', buttonImageOnly: true,dateFormat: 'dd/mm/yy', minDate: 0});

	 /*Manejadores de eventos*/
	 $('#cbodependenciaDestino').change(function(event){getPersonasDependencia();});
	
;
	 $('#cmdguardar').click(function(event){guardarCorrespondencia();});
	 $('#cmdenviar').click(function (event){enviarCorrespondencia();});
	 $('#cmdnuevo').click(function(event){limpiarControles();});
	 $('#cmdsubir').click(function(event){subirArchivo();});
	 //$('#cmdsubir').click(function(event){$('#forma').submit();});
	 //autocompletado
	  getPersonasSubdireccion('txtccp','CVE_PERS');
	 $('#ui-datepicker-div').hide();
});

function enviarCorrespondencia(){
	var idCorrespondencia = $('#ID_CORRESPONDENCIA').val();
	jConfirm('¿Confirma que desea enviar la correspondencia?','Enviar', function(r){
		if(r){
			ControladorCorrespondenciaRemoto.enviarCorrespondencia(idCorrespondencia, {
								callback:function(items) 
								{
										CloseDelay("Correspondencia enviada con éxito");
								}
								,
								errorHandler:function(errorString, exception) { 
									jError(errorString, 'Error');          
								}
				});
		}
	});
}


function guardarCorrespondencia(){
	var idCorrespondencia = $('#ID_CORRESPONDENCIA').val();
	var numeroDocumento = $('#txtdocumento').val();
	var idDependenciaFuente = $('#cbodependencia').val();
	var idDependenciaDestino = $('#cbodependenciaDestino').val();
	var cve_persDestino = $('#cbopersonaDestino').val();
	var idTipoDocumento = $('#cbotipoDocumento').val();
	var idPrioridad = $('#cboprioridad').val();
	var idOrigen = $('#cboorigen').val();
	var idTipoAviso = $('#cbotipoAviso').val();
	var asunto = $('#txtasunto').val();
	var acuerdo = $('#txtacuerdo').val();
	var fechaRecepcion = $('#txtfechaRecepcion').val();
	var fechaDocumento = $('#txtfechaDocumento').val();
	var fechaLimite = $('#txtfechaLimite').val();
	
	if(numeroDocumento=='') {jAlert('Es necesario escribir el numero de documento', 'Advertencia'); $('#txtdocumento').focus(); return false;}
	if(fechaDocumento=='') {jAlert('Escriba una fecha de documento', 'Advertencia'); $('#txtfechaDocumento').focus(); return false;}
	if(fechaRecepcion=='') {jAlert('Escriba una fecha de recepción', 'Advertencia'); $('#txtfechaRecepcion').focus(); return false;}
	if(fechaLimite=='') {jAlert('Escriba una fecha limite para el documento','Advertencia'); $('#txtfechaLimite').focus(); return false;}
	if(idTipoDocumento==0) {jAlert('Es necesario seleccionar el tipo de documento', 'Advertencia'); $('#cbotipoDocumento').focus(); return false;}
	if(idPrioridad==0) {jAlert('Es necesario seleccionar la prioridad','Advertencia'); $('#cboprioridad').focus(); return false;}
	if(asunto=='') {jAlert('Es necesario escribir el Asunto','Advertencia'); return false;}
	
	if(idDependenciaDestino==0) {jAlert('Es necesario seleccionar la Unidad Destino','Advertencia'); $('#cbodependenciaDestino').focus(); return false;}
	if(cve_persDestino==0) {jAlert('Es necesario seleccionar una Persona Destino', 'Advertencia'); $('#cbopersonaDestino').focus(); return false;}

	jConfirm('¿Confirma que desea guardar la correspondencia?','Guardar', function(r){
		if(r){
				ControladorCorrespondenciaRemoto.guardarCorrespondencia(idCorrespondencia, numeroDocumento, idDependenciaFuente, idDependenciaDestino, cve_persDestino, idTipoDocumento, idPrioridad, idOrigen, idTipoAviso, asunto, acuerdo, fechaRecepcion, fechaDocumento, fechaLimite, CVE_PERS.join(), {
								callback:function(items) 
								{
										$('#ID_CORRESPONDENCIA').val(items.ID_CORRESPONDENCIA);
										CVE_PERS = [];
										if(items.CCP!="") CVE_PERS = items.CCP.split(",");
										mostrarDetallesCCP();
										CloseDelay("Correspondencia guardada con éxito", function(){
												$('#tabuladores').tabs('enable',1);
												$('#cmdenviar').attr('disabled', false);
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
		mostrarDetallesArchivos($('#ID_CORRESPONDENCIA').attr('value'));
		$('#archivo').attr('value','');
	}
	else{
		_closeDelay();
		jError("No se ha podido cargar el archivo, consulte a su administrador", "Error");
	}
} 

function mostrarDetallesArchivos(idCorrespondencia){
	quitRow("detalle_archivo");
	ControladorCorrespondenciaRemoto.getArchivosCorrespondencia(idCorrespondencia, {
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
	 var htmlRemove = "<img src=\"../../imagenes/cross.png\" style='cursor: pointer;' alt=\"Eliminar\" width=\"16\" height=\"16\" border=\"0\" onClick=\"eliminarArchivoCorrespondencia("+m.ID_ARCHIVO+")\" >";
	appendNewRow("detalle_archivo", [Td('', izquierda , '', '<a href="../'+m.RUTA+'['+m.ID_ARCHIVO+'] '+m.NOMBRE+'" target="_blank">['+m.ID_ARCHIVO+'] '+m.NOMBRE+'</a>'),
						 Td('', centro , '', m.EXT),
						  Td('', centro , '', parseInt(parseInt(m.TAMAÑO)/1024)+' kb'),
						 Td('', centro , '', htmlRemove)
				]);
}

function eliminarArchivoCorrespondencia(idArchivo){
	jConfirm('¿Confirma que desea eliminar el archivo?','Eliminar', function(r){
		if(r){
				ShowDelay("Eliminando archivo");
				ControladorCorrespondenciaRemoto.eliminarArchivoCorrespondencia(idArchivo,{
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
	
/*
function getCargarMinutario(idMinutario){
	ControladorCorrespondenciaRemoto.getCargarMinutario(idMinutario,{
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
*/

function getClavesCCP(idCorrespondencia){
	ControladorCorrespondenciaRemoto.getClavesPersonasCCP(idCorrespondencia,{
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
	ControladorCorrespondenciaRemoto.getMinutariosCombo(idDependencia, idCatMinutario,{
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

function limpiarControles(){
	$('#ID_CORRESPONDENCIA').val('0');
	$('#txtdocumento').val("");
	$('#txtfechaRecepcion').val("");
	$('#txtfechaDocumento').val("");
	$('#cbodependencia').val(0);
	$('#cbotipoDocumento').val(0);
	$('#cboprioridad').val(0);
	$('#cboorigen').val(0);
	$('#cbotipoAviso').val(0);
	$('#cbodependenciaDestino').val(0);
	$('#cbopersonaDestino').html("");
	$('#txtasunto').val("");
	$('#txtacuerdo').val("");
	$('#txtasunto').val("");
	$('#txtccp').val("");
	$('#txtfechaLimite').val("");
	$('#cmdenviar').attr('disabled', true);
	$('#CVE_PERS').val("");
	CVE_PERS = [];
	$('#detalles_ccp').hide();
	quitRow("detalles_ccp");
	$('#tabuladores').tabs('enable',0);
}
	
function getPersonasDependencia(){
	ShowDelay("Cargando minutarios");
	var idDependencia = $('#cbodependenciaDestino').val();
	ControladorCorrespondenciaRemoto.gePersonasUnidadCombo(idDependencia, 0, {
					callback:function(items) {
					 		$('#cbopersonaDestino').html(items);
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
		ControladorCorrespondenciaRemoto.getListaPersonas(CVE_PERS, {
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
	var IdCorrespondencia = $('#ID_CORRESPONDENCIA').attr('value');
	var existe = false;
	if(IdCorrespondencia==0){
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
		ControladorCorrespondenciaRemoto.agregarDestinatario(valor,IdCorrespondencia,{
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
	var idCorrespondencia = $('#ID_CORRESPONDENCIA').attr('value');
	if(idCorrespondencia==0){
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
				ShowDelay('Eliminando destinatario c.c.p');
				ControladorCorrespondenciaRemoto.eliminarDestinatario(cve, idCorrespondencia, {
					callback:function(items) { 
						jQuery.each(CVE_PERS, function(c) {
							if(cve==this){
								CVE_PERS.splice(c,1);
								$('#CVE_PERS').attr('value',CVE_PERS);
							}
							c++;
						});
						mostrarDetallesCCP();
						CloseDelay("Destinatario eliminado con éxito");
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
	ControladorCorrespondenciaRemoto.getPersonasSubdireccion({
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
