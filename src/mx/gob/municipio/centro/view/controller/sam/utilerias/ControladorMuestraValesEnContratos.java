package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayContratos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayVales;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/sam/consultas/muestraVales_tipo_contratos.action")
public class ControladorMuestraValesEnContratos extends ControladorBase {
	
	@Autowired
	private GatewayContratos gatewayContratos;
	
	public ControladorMuestraValesEnContratos(){};

	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	
	@SuppressWarnings("unchecked") 
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		
		String num_vales = request.getParameter("num_vales");
		String clv_benefi = request.getParameter("clv_benefi");
		modelo.put("unidadesAdmiva",  this.getUnidades());
		String id_unidad = (request.getParameter("idDependencia")==null) ? "": request.getParameter("idDependencia").toString();
		modelo.put("idUnidad", id_unidad);
		modelo.put("listadovales", gatewayContratos.getListaValesContratos(num_vales, this.getSesion().getIdUsuario(), id_unidad, clv_benefi));
		
		return "sam/consultas/muestraVales_tipo_contratos.jsp";
	}

	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
}
