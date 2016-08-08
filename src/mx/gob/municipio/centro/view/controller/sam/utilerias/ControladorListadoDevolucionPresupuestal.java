package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayDevolucionesPresupuestales;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/consultas/lst_devolucion_presupuestal.action")
public class ControladorListadoDevolucionPresupuestal extends ControladorBase {
	public final static  int VER_TODAS_LAS_UNIDADES = 25;
	
	public ControladorListadoDevolucionPresupuestal(){
	}
	
	@Autowired
	GatewayDevolucionesPresupuestales gatewayDevolucionesPresupuestales;
	
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);
		Integer  unidad = (request.getParameter("unidad")!=null)? Integer.parseInt(request.getParameter("unidad").toString()): 0;
		String estatus=request.getParameter("estatus")==null ? "0": this.arrayToString(request.getParameterValues("estatus"),",");
		String concepto=request.getParameter("txtconcepto");
		String numero=request.getParameter("txtnumero");
		
		if(privilegio){
			if(request.getParameter("unidad")==null)
				unidad = 0;
			if(request.getParameter("unidad")!=null)
				unidad = Integer.parseInt(request.getParameter("unidad").toString());
		}
		
		if(!privilegio){
			if(request.getParameter("unidad")==null)
				unidad = Integer.parseInt(this.getSesion().getClaveUnidad());
			if(request.getParameter("unidad")!=null)
				unidad = Integer.parseInt(request.getParameter("unidad").toString());
		}
		
		//if(unidad==0) unidad = Integer.parseInt(this.getSesion().getIdUnidad());
		
		modelo.put("txtconcepto", concepto);
		modelo.put("txtnumero", numero);
		modelo.put("estatus",estatus);
		modelo.put("nombreUnidad", this.getSesion().getUnidad());
		modelo.put("unidadesAdmiva",gatewayUnidadAdm.getUnidadAdmTodos());
		modelo.put("unidad", unidad);
		List <Map> listado = this.gatewayDevolucionesPresupuestales.getListaDevolucionesPresupuestales(concepto, numero, estatus, unidad);
		modelo.put("devoluciones", listado);
		modelo.put("CONTADOR", listado.size());
		return "sam/consultas/lst_devolucion_presupuestal.jsp";
	}
	
	public String cancelarDevolucion(Long idDevolucion){
		return gatewayDevolucionesPresupuestales.cancelarDevolucion(idDevolucion);
	}
	
	public String aplicarDevolucion(final Long[] idDevoluciones){
		return gatewayDevolucionesPresupuestales.aplicarDevolucion(idDevoluciones);
	}
	
	public String aperturarDevolucion(Long[] idDevolucion){
		return gatewayDevolucionesPresupuestales.aperturarDevolucion(idDevolucion);
	}
	
	public String desaplicarDevolucion(Long[] idDevoluciones){
		return gatewayDevolucionesPresupuestales.desaplicarDevolucion(idDevoluciones);
	}
	
	
}
