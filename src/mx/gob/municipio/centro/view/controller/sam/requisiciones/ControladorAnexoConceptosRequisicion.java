package mx.gob.municipio.centro.view.controller.sam.requisiciones;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayAnexoConceptosRequisiciones;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/requisiciones/capturaAnexoConceptos.action")
public class ControladorAnexoConceptosRequisicion extends ControladorBase {

	private GatewayAnexoConceptosRequisiciones gatewayAnexoConceptosRequisiciones;
	/*Metodo de errores desactivado*/
	@SuppressWarnings("unchecked")
	/*Mapeo para la pagina de donde se recibira los GET*/ 
	@RequestMapping(method = RequestMethod.GET)  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		modelo.put("id_req_movto", request.getParameter("id_req_movto"));
		modelo.put("texto", this.getAnexoConceptoRequisicion(Long.parseLong(modelo.get("id_req_movto").toString())));
	    return "sam/requisiciones/capturaAnexoConceptos.jsp";
	}
	
	public ControladorAnexoConceptosRequisicion(){}
	
	public Map getAnexoConceptoRequisicion(Long id_req_movto){
		try  
		{
			return this.getJdbcTemplate().queryForMap("SELECT TEXTO FROM SAM_REQ_ANEXO WHERE ID_REQ_MOVTO = ?", new Object []{id_req_movto});
		}
		catch ( DataAccessException e) {
			return null;
		}
	}
}
