/**
 * @author ISC. Israel de la Cruz Hdez.
 * @version 1.0, Date: 02/Jun/2011
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayContratos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/muestra_os_contratos.action")
public class ControladorMuestraOSContratos extends ControladorBase {

	@Autowired
	private GatewayContratos gatewayContratos;
	/**
	 * @param args
	 */
	public ControladorMuestraOSContratos() {
		// TODO Auto-generated method stub
	}
	
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	
	@SuppressWarnings("unchecked") 
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		String num_req = request.getParameter("num_req");
		String clv_benefi = request.getParameter("clv_benefi");
		modelo.put("unidadesAdmiva",  this.getUnidades());
		String id_unidad = (request.getParameter("idDependencia")==null) ? "": request.getParameter("idDependencia").toString();
		modelo.put("idUnidad", id_unidad);
		modelo.put("documentos", gatewayContratos.getListaOSContratos(num_req, this.getSesion().getIdUsuario(), id_unidad, clv_benefi));
	    return "sam/consultas/muestra_os_contratos.jsp";
	}

	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
}
