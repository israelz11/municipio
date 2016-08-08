package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayBitacora;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/consultas/muestra_detalles_Op.action")
public class ControladorMuestraDetallesOP {
	private static Logger log = Logger.getLogger(ControladorMuestraDetallesOP.class.getName());
	public ControladorMuestraDetallesOP() {
		// TODO Auto-generated method stub

	}
	
	@Autowired
	public GatewayOrdenDePagos gatewayOrdenDePagos;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = RequestMethod.GET) 
	public String  requestGetControlador(Map modelo, HttpServletRequest request) {
		try 
		{  
			Long cve_op = request.getParameter("cve_op")==null ? 0L: Long.parseLong(request.getParameter("cve_op").toString());  
			
			Map op = gatewayOrdenDePagos.getOrden(cve_op);
			modelo.put("op", op);
			modelo.put("mov", this.gatewayOrdenDePagos.getMovimientosOPPresupuest(cve_op));
			return "sam/consultas/muestra_detalles_Op.jsp";
		}
		catch (DataAccessException e) {	    
			log.info(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
       }
	}

}
