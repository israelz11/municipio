package mx.gob.municipio.centro.view.controller.sam.utilerias;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/muestra_OT_OS.action")
public class ControladorMuestraOTOS extends ControladorBase {

	/**
	 * @param args
	 */
	public ControladorMuestraOTOS() {
		// TODO Auto-generated method stub

	}
	@Autowired
	public GatewayOrdenDePagos gatewayOrdenDePagos;
	@Autowired
	private GatewayMeses gatewayMeses;
	
	@RequestMapping(method = {RequestMethod.GET , RequestMethod.POST} )   
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		String  idtipoGasto = request.getParameter("tipoGasto");
		String  unidad = request.getParameter("unidad");
		String  proyecto = request.getParameter("proyecto");
		String  clv_partid = request.getParameter("clv_partid");
		String  claveBeneficiario = request.getParameter("clv_benefi");
		if(proyecto==null) proyecto ="";
		if(clv_partid ==null) clv_partid="";
		modelo.put("documentos",gatewayOrdenDePagos.getOTPorUnidad(idtipoGasto, unidad, claveBeneficiario, this.getSesion().getIdUsuario(), proyecto, clv_partid));
	    return "sam/consultas/muestra_OT_OS.jsp";
	}
	
	

}
