/**
 * @author ISC. Israel de la Cruz Hernández.
 * @version: 1.0
 * @date: 15-July-2010
 * **/
package mx.gob.municipio.centro.view.controller.almacen.consultas;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayEntradasDocumentos;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/consultas/entradas.action")
public class ControladorConsultasEntradasDocumentos extends ControladorBaseAlmacen {

	@Autowired
	GatewayEntradasDocumentos gatewayEntradasDocumentos;
	
	public ControladorConsultasEntradasDocumentos() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET) 
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		modelo.put("id_entrada", (request.getParameter("id_entrada")==null ? 0: request.getParameter("id_entrada")));
		modelo.put("ban", (request.getParameter("ban")==null ? null: request.getParameter("ban")));
		modelo.put("documento", this.gatewayEntradasDocumentos.getEntradaDocumento(Long.parseLong(modelo.get("id_entrada").toString())));
		modelo.put("detalles", this.gatewayEntradasDocumentos.getConceptos(Long.parseLong(modelo.get("id_entrada").toString())));
		
		return "almacen/consultas/entradas.jsp";
	}

}
