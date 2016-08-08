package mx.gob.municipio.centro.view.controller.sam.requisiciones;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayMovimientosRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/requisiciones/reenumerarLotes.action")
public class ControladorReenumerarLotes extends ControladorBase {
	
	@Autowired
	private GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
	
	public static void ControladorImportarLotes() {
		// TODO Auto-generated method stub
	}
	
	@RequestMapping(method = {RequestMethod.GET , RequestMethod.POST} )   
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Long cve_req = (request.getParameter("cve_req")==null) ? 0L: Long.parseLong(request.getParameter("cve_req").toString());
		String  num_req = request.getParameter("num_req");
		
		if(num_req==null) num_req = "";
		modelo.put("num_req", num_req);
		modelo.put("cve_req", cve_req);
		modelo.put("movimientos", this.gatewayMovimientosRequisicion.getConceptos(cve_req));
		modelo.put("numlotes", this.getJdbcTemplate().queryForList("SELECT REQ_CONS FROM SAM_REQ_MOVTOS WHERE CVE_REQ = ? ORDER BY REQ_CONS ASC", new Object[]{cve_req}));
	    return "sam/requisiciones/reenumerarLotes.jsp";
	}
	
	
	
}
