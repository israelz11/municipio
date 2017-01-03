package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayFacturas;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/consultas/muestra_facturas.action")
public class ControladorMuestraFacturas extends ControladorBase {

	@Autowired 
	GatewayFacturas gatewayFacturas;
	
	public ControladorMuestraFacturas(){
		
	}
	
	@RequestMapping(method = {RequestMethod.GET , RequestMethod.POST} )   
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		int  idtipoGasto = Integer.parseInt(request.getParameter("tipoGasto").toString());
		int  unidad = Integer.parseInt(request.getParameter("unidad").toString());
		String  claveBeneficiario = request.getParameter("clv_benefi");

		modelo.put("listaFacturas", gatewayFacturas.getListaFacturasOrdenPago(idtipoGasto, unidad, claveBeneficiario, this.getSesion().getIdUsuario()));
	    return "sam/consultas/muestra_facturas.jsp";
	}
	
}
