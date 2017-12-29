package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayReportePresupestoDisp;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/reportes/lst_reportepresupestodis.action")
public class ControladorReportePresupuestoDisp extends ControladorBase{

public final static  int VER_TODAS_LAS_UNIDADES = 25;

	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	GatewayReportePresupestoDisp gatewayReportePresupestoDisp;
	
	@Autowired
	private GatewayPlanArbit gatewayPlanArbit;
	
	public ControladorReportePresupuestoDisp (){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {
		
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);
		
		String idUnidad=request.getParameter("cbUnidad")==null ?this.getSesion().getClaveUnidad() : request.getParameter("cbUnidad");
		Integer tipogasto = request.getParameter("cbotipogasto") != null ? Integer.parseInt(request.getParameter("cbotipogasto")): 0;
		Integer idproyecto = request.getParameter("txtproyecto")!= null ? Integer.parseInt(!request.getParameter("txtproyecto").toString().equals("") ? request.getParameter("txtproyecto").toString(): "0"): 0;
		String idpartida = request.getParameter("txtpartida")!= null ? request.getParameter("txtpartida"): "";
		
		modelo.put("idUnidad",idUnidad);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("idtipogasto",tipogasto);
		modelo.put("idproyecto",idproyecto);
		modelo.put("idpartida",idpartida);
		//txtpartida
		modelo.put("listadomovimientos",this.gatewayReportePresupestoDisp.getreparametros(modelo));
		
		return "sam/reportes/lst_reportepresupestodis.jsp";

	}
	
	@ModelAttribute("unidadesAdmiva")
	public List<Map> getUnidadesAdmivas(){
	   	return gatewayUnidadAdm.getUnidadAdmTodos();	
	}
	
	@ModelAttribute("tipodeGasto")
    public List<Map> getTiposDeGasto(){
    	return gatewayPlanArbit.getTipodeGasto();
    }
	@ModelAttribute("capitulos")
    public List<Map> getCapitulos(){
    	return gatewayUnidadAdm.getCapitulos();	
    }
}
