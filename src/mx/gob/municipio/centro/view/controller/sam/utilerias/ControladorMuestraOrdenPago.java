/**
 * @author ISC. Israel de la Cruz Hdez.
 * @version 1.0, Date: 16/Jun/2013
 *
 */

package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayContratos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/muestra_OrdenPago.action")
public class ControladorMuestraOrdenPago extends ControladorBase {

	public ControladorMuestraOrdenPago(){}
	
	@Autowired
	private GatewayOrdenDePagos gatewayOrdenDePagos;
	
	@SuppressWarnings("unchecked") 
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		String unidad = request.getParameter("idDependencia")==null ? this.getSesion().getClaveUnidad() : request.getParameter("idDependencia");
		String cve_op = request.getParameter("cve_op");
		String num_op = request.getParameter("num_op");
		
		modelo.put("listado", gatewayOrdenDePagos.getListaORdenPagoEstatus(Integer.parseInt(unidad), 6));
		if(cve_op!=null&&!cve_op.equals(""))
		{
			modelo.put("detalles", gatewayOrdenDePagos.getMovimientosOP(Long.parseLong(cve_op)));
		}
			
		modelo.put("num_op", num_op);
	    return "sam/consultas/muestra_OrdenPago.jsp";
	}
}
