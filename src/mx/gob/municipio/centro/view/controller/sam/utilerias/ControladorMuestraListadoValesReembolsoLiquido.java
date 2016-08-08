/**
 * @author ISC. Israel de la Cruz Hdez.
 * @version 1.0, Date: 07/Sep/2011
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayVales;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/consultas/muestra_vales_reembolso.action")
public class ControladorMuestraListadoValesReembolsoLiquido extends ControladorBase {

	/**
	 * @param args
	 */
	@Autowired
	public GatewayVales gatewayVales;
	
	public ControladorMuestraListadoValesReembolsoLiquido() {
		// TODO Auto-generated method stub

	}
	
	@RequestMapping(method = {RequestMethod.GET , RequestMethod.POST} )   
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		String  unidad = request.getParameter("unidad");
		modelo.put("documentos",gatewayVales.getValesPagadosNoComprobados(unidad,this.getSesion().getEjercicio()));
	    return "sam/consultas/muestra_vales_reembolso.jsp";
	}

}
