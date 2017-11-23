package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.municipio.centro.model.gateways.sam.GatewayExcelReporte;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayReportePresupestoDisp;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;



@Controller
@RequestMapping("/sam/reportes/lst_reporteexcel.action")
public class ControladorExcelReporte extends ControladorBase {
	
	private static Logger log = Logger.getLogger(ControladorExcelReporte.class.getName());
	public final static  int VER_TODAS_LAS_UNIDADES = 25;

	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	GatewayReportePresupestoDisp gatewayReportePresupestoDisp;
	
	@Autowired
	private GatewayPlanArbit gatewayPlanArbit;
	
	public ControladorExcelReporte(){		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public ModelAndView requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {

		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);
		
		String idUnidad=request.getParameter("xidunidad")==null ?this.getSesion().getClaveUnidad() : request.getParameter("xidunidad");
		Integer tipogasto = request.getParameter("xidgasto") != null ? Integer.parseInt(request.getParameter("xidgasto")): 0;
		
		modelo.put("cbodependencia",idUnidad);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("idtipogasto",tipogasto);
	
		Map data = new HashMap<String, String>();
		data.put("listadomovimientos", this.gatewayReportePresupestoDisp.getreparametros(modelo));
		//modelo.put("listadomovimientos",this.gatewayReportePresupestoDisp.getreparametros(modelo));
		System.out.println("Modelo: " +data);
		//return new ModelAndView(new GatewayExcelReporte(modelo)); //"modeldata",
		return new ModelAndView("AnimalListExcel", "animalList", data);
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
