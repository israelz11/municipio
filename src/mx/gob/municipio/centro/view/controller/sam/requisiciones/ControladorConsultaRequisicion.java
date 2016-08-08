package mx.gob.municipio.centro.view.controller.sam.requisiciones;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayMovimientosRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.view.bases.ControladorBase;
@Controller
@RequestMapping("/sam/requisiciones/consultaRequisicion.action")

public class ControladorConsultaRequisicion extends ControladorBase {

	private ControladorConsultaRequisicion(){}
	private static Logger log = Logger.getLogger(ControladorConsultaRequisicion.class.getName());
	
	@Autowired
	private GatewayRequisicion gatewayRequisicion;
	@Autowired
	private GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
	
	@RequestMapping(method = RequestMethod.GET)  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		modelo.put("cve_req", request.getParameter("cve_req"));
		modelo.put("accion", request.getParameter("accion"));
		modelo.put("requisicion",this.gatewayRequisicion.getRequisicion(Long.parseLong(modelo.get("cve_req").toString())));
		modelo.put("movimientos", this.gatewayMovimientosRequisicion.getConceptos(Long.parseLong(modelo.get("cve_req").toString())));
	    return "sam/requisiciones/consultaRequisicion.jsp";
	}
}
