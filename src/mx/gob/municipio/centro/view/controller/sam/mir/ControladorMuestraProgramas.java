package mx.gob.municipio.centro.view.controller.sam.mir;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayMir;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/mir/muestra_programas.action")
public class ControladorMuestraProgramas extends ControladorBase {
	@Autowired
	private GatewayMir gatewayMir;
	
	public void ControladorMuestraProgramas(){}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET}) 
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		String IdDependencia = request.getParameter("IdDependencia");
		modelo.put("programas", gatewayMir.getProgramas(IdDependencia));
		return "sam/mir/muestra_programas.jsp";
	}
}
