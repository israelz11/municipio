/**
 * @author ISC. Israel de la Cruz Hernández.
 * @version: 1.0
 * @date: 15-July-2010
 * **/
package mx.gob.municipio.centro.view.controller.almacen.consultas;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import mx.gob.municipio.centro.model.gateways.almacen.GatewayInventario;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/consultas/movimientos_articulos.action")
public class ControladorConsultasMovimientos extends ControladorBaseAlmacen {

	public ControladorConsultasMovimientos(){		
	}		
	@Autowired
	GatewayInventario gatewayInventario;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	public String  requestGetControlador( Map modelo, @RequestParam("idInventario")  Long  idInventario ) {
		Map modelodatos = gatewayInventario.getArticulo(idInventario);
		modelo.put("DESCRIPCION", modelodatos.get("DESCRIPCION"));
		modelo.put("UNIDMEDIDA", modelodatos.get("UNIDMEDIDA"));
		modelo.put("EXISTENCIA", modelodatos.get("EXISTENCIA"));
		modelo.put("movimientos",gatewayInventario.getMovimientosArticulo(idInventario));
		return "almacen/consultas/movimientos_articulos.jsp";		
	}		
	
}
