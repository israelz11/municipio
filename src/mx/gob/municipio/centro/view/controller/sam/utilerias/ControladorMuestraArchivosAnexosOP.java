package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/consultas/muestra_anexosOP.action")
public class ControladorMuestraArchivosAnexosOP extends ControladorBase {

	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		
		modelo.put("muestraArchivos", this.getDocumentosOrdenes(Long.parseLong(request.getParameter("cve_op").toString())));
		
	    return "sam/consultas/muestra_anexosOP.jsp";
	}
	
	 
	 
	 /* Documentos */
    public List getDocumentosOrdenes (Long idOrden) {
   	 return gatewayOrdenDePagos.getDocumentosOrdenes(idOrden);
    }
}
