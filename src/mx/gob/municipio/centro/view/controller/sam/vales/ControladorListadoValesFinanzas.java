/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.vales;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayVales;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/vales/lista_vales_finanzas.action")

public class ControladorListadoValesFinanzas extends ControladorBase {

	public ControladorListadoValesFinanzas(){}
		
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewayPlanArbit gatewayPlanArbit;
	
	@Autowired
	GatewayVales gatewayVales;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST} )  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Boolean privilegio = false;
		String unidad=request.getParameter("cbodependencia")==null ?this.getSesion().getClaveUnidad() : request.getParameter("cbodependencia");
		String estatus=request.getParameter("status")==null ? gatewayVales.getEstatusPendiente().toString(): request.getParameter("status").toString();
		String fechaIni=request.getParameter("fechaInicial");
		String fechaFin=request.getParameter("fechaFinal");
		String tipoGasto=request.getParameter("cbotipogasto");
		modelo.put("idUnidad", unidad);
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("fechaInicial",fechaIni);
		modelo.put("fechaFinal",fechaFin);
		modelo.put("status",estatus);
		modelo.put("cbotipogasto",tipoGasto );
		modelo.put("idUnidadSes",this.getSesion().getClaveUnidad());
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("vales",this.getListadoVales(unidad, estatus, fechaIni,fechaFin,this.getSesion().getEjercicio(),tipoGasto,this.getSesion().getIdUsuario(), privilegio));		
	    return "sam/vales/lista_vales_finanzas.jsp";
	}
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
		
	@ModelAttribute("tipodeGasto")
    public List<Map> getTiposDeGasto(){
    	return gatewayPlanArbit.getTipodeGasto();
    }
		
	public List <Map>getListadoVales(String unidad, String  estatus , String fechaInicial, String fechaFinal , Integer ejercicio, String tipoGasto, Integer idUsuario, Boolean privilegio){
		return this.gatewayVales.getListaDeValesPorEjemplo(unidad, estatus , this.formatoFecha(fechaInicial), this.formatoFecha(fechaFinal) , ejercicio, tipoGasto, idUsuario, "SI", privilegio);
	}
	
}
