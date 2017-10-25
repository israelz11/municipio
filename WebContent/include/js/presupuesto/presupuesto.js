/*Metodo para mostrar el presupuesto*/
function __getPresupuesto(idproyecto, proyecto, partida, mes,  ctrl_presupuesto, ctrl_disponible, tipoGasto){
	var cve_vale = $('#CVE_VALE').attr('value');
	if(typeof(cve_vale)=='undefined') cve_vale = 0;
	//alert("entra uno")
	if(/*proyecto!=''&&*/partida!=''&&/*proyecto.length==4&&*/partida.length==4){
		
			__limpiarPresupuestoSaldo(ctrl_presupuesto, ctrl_disponible);
			/*Obtiene el presupuesto de manera normal*/
			
		
			if(cve_vale==0){
				controladorProyectoPartida.getPresupuesto(idproyecto, proyecto, partida, parseInt(mes),tipoGasto, 0, {
					callback:function(items){
						//alert("entra dos")
						if (items.length > 0)
							jQuery.each(items,function(i) {
									$('#'+ctrl_presupuesto).attr('value', formatNumber(parseFloat(this.PREACTUAL),'$'));
									$('#'+ctrl_disponible).attr('value', formatNumber(parseFloat(this.DISPONIBLE),'$'));							
							});
						else {
							$('#ID_PROYECTO').attr('value','0');
							$('#txtproyecto').attr('value','');
							$('#txtpartida').attr('value', '');
							$('#'+ctrl_presupuesto).attr('value', '');
							$('#'+ctrl_disponible).attr('value', '');
							jAlert("El programa Ã³ partida especificados no existe Ã³ no tiene privilegios para visualizarlo", "Advertencia");	  
						}
							
					}
					,
				errorHandler:function(errorString, exception) { 
					__limpiarPresupuesto(ctrl_presupuesto, ctrl_disponible);
					jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br>Consulte a su administrador');   
					return false;
				}
				});
			}
			/*Obtiene el presupuesto atravez del vale 03/04/2012*/
			if(cve_vale!=0){
				controladorProyectoPartida.getListaValesPresupuesto(cve_vale, idproyecto, partida, 0, mes, {
						callback:function(items){
						if (items.length > 0)
							jQuery.each(items,function(i) {
									$('#'+ctrl_presupuesto).attr('value', formatNumber(parseFloat(this.TOTAL),'$'));
									$('#'+ctrl_disponible).attr('value', formatNumber(parseFloat(this.DISPONIBLE),'$'));						
							});
						else {
							$('#ID_PROYECTO').attr('value','0');
							$('#txtproyecto').attr('value','');
							$('#txtpartida').attr('value', '');
							$('#'+ctrl_presupuesto).attr('value', '');
							$('#'+ctrl_disponible).attr('value', '');
							jAlert("El programa Ã³ partida especificados no existe Ã³ no tiene privilegios para visualizarlo - Vales", "Advertencia");	  
						}
							
					}
					,
					errorHandler:function(errorString, exception) { 
						__limpiarPresupuesto(ctrl_presupuesto, ctrl_disponible);
						jError('Fallo la operacion:<br>Error::'+errorString+'-message::'+exception.message+'-JavaClass::'+exception.javaClassName+'.<br>Consulte a su administrador');   
						return false;
					}
				});
			}

	}
	else{
		$('#ID_PROYECTO').attr('value','0');
		__limpiarPresupuesto(ctrl_presupuesto, ctrl_disponible);
	}
}

/*Metodo para limpiar los controles del presupuesto*/
function __limpiarPresupuesto(ctrl_presupuesto, ctrl_disponible){
	$('#txtpartida').attr('value', '');
	__limpiarPresupuestoSaldo(ctrl_presupuesto, ctrl_disponible);
}


function __limpiarPresupuestoSaldo(ctrl_presupuesto, ctrl_disponible){
	$('#'+ctrl_presupuesto).attr('value','0.00');
	$('#'+ctrl_disponible).attr('value','0.00');
}

/*Listado para el presupuesto del vale 05/03/2012*/
function __listadoPresupuestoVale(idproyecto, proyecto, partida, mes, tipoGasto, idDependencia, idVale){
	swal({
	    title: 'Informacion Presupuestal del Vale',
	    width: 800,
	    html:
	       	'<iframe width="800" height="410" name="consultaPre" id="consultaPre" frameborder="0" src="../../sam/consultas/muestra_presupuesto.action?idproyecto='+idproyecto+'&proyecto='+proyecto+'&partida='+partida+'&mes='+mes+'&tipoGasto='+tipoGasto+'&unidad='+idDependencia+'&idVale='+idVale+'"></iframe>', 
	   })
	//jWindow('<iframe width="800" height="410" name="consultaPre" id="consultaPre" frameborder="0" src="../../sam/consultas/muestra_presupuesto.action?idproyecto='+idproyecto+'&proyecto='+proyecto+'&partida='+partida+'&mes='+mes+'&tipoGasto='+tipoGasto+'&unidad='+idDependencia+'&idVale='+idVale+'"></iframe>','Informacion Presupuestal del Vale', '','Cerrar ',1);
}

/*Listado para el presupuesto del contrato 19/05/2013*/
function __listadoPresupuestoContrato(idproyecto, proyecto, partida, mes, tipoGasto, idDependencia, cve_contrato){
	swal({
	    title: 'Informacion Presupuestal del Contrato',
	    width: 800,
	    html:
	       	'<iframe width="800" height="410" name="consultaPre" id="consultaPre" frameborder="0" src="../../sam/consultas/muestra_presupuesto.action?idproyecto='+idproyecto+'&proyecto='+proyecto+'&partida='+partida+'&mes='+mes+'&tipoGasto='+tipoGasto+'&unidad='+idDependencia+'&cve_contrato='+cve_contrato+'"></iframe>', 
	   })
	//jWindow('<iframe width="800" height="410" name="consultaPre" id="consultaPre" frameborder="0" src="../../sam/consultas/muestra_presupuesto.action?idproyecto='+idproyecto+'&proyecto='+proyecto+'&partida='+partida+'&mes='+mes+'&tipoGasto='+tipoGasto+'&unidad='+idDependencia+'&cve_contrato='+cve_contrato+'"></iframe>','Informacion Presupuestal del Contrato', '','Cerrar ',1);
}

function __listadoPresupuesto(idproyecto, proyecto, partida, mes, tipoGasto, idDependencia ){
	//swal({
	   // title: 'Informacion Presupuestal',
	    //width: 800,
	    //html:
	      // 	'<iframe width="800" height="410" name="consultaPre" id="consultaPre" frameborder="0" src="../../sam/consultas/muestra_presupuesto.action?idproyecto='+idproyecto+'&proyecto='+proyecto+'&partida='+partida+'&mes='+mes+'&tipoGasto='+tipoGasto+'&unidad='+idDependencia+'"></iframe>', 
	   //})
	swal({
		  title: 'Informacion Presupuestal',
		  width: 800,
		  html: '<iframe width="800" height="410" name="consultaPre" id="consultaPre" frameborder="0" src="../../sam/consultas/muestra_presupuesto.action?idproyecto='+idproyecto+'&proyecto='+proyecto+'&partida='+partida+'&mes='+mes+'&tipoGasto='+tipoGasto+'&unidad='+idDependencia+'"></iframe>',
		})
	//jWindow('<iframe width="800" height="410" name="consultaPre" id="consultaPre" frameborder="0" src="../../sam/consultas/muestra_presupuesto.action?idproyecto='+idproyecto+'&proyecto='+proyecto+'&partida='+partida+'&mes='+mes+'&tipoGasto='+tipoGasto+'&unidad='+idDependencia+'"></iframe>','Informacion Presupuestal', '','Cerrar ',1);
}


/*Devuelve el presupuesto de la pantalla hija*/

function __regresaPresupuesto(ID, proyecto, partida, pre_actual, disponible){
	alert("demo llego hasta aqui");
	$('#txtpresupuesto').attr('value',formatNumber(pre_actual,'$')); 
	$('#txtdisponible').attr('value',formatNumber(disponible,'$')); 
	$('#txtproyecto').attr('value',proyecto);
	$('#txtpartida').attr('value',partida);
	$('#CLV_PARTID').attr('value',partida);
	$('#ID_PROYECTO').attr('value',ID);
	$(".swal2-container.swal2-fade.swal2-shown").remove();
	//$.alerts._hide();
	//costumFunction();
}
