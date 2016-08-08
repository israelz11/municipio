package mx.gob.municipio.centro.view.controller.sam.mir;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayMir;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/mir/captura_mir.action")
public class ControladorMatrizIndicadoresResultados extends ControladorBase {

	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired 
	private GatewayMir gatewayMIR;
	
	public void ControladorMatrizIndicadoresResultados() {}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}) 
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		return "sam/mir/captura_mir.jsp";
	}
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	public String guardarMIR(int IdMIR, int Dependencia, String Fecha, String Programa, String ClaveProgramatica)
	{
		return gatewayMIR.Guardar(IdMIR, Dependencia, Fecha, Programa, ClaveProgramatica,this.getSesion().getIdUsuario(), this.getSesion().getEjercicio());
	}
}
