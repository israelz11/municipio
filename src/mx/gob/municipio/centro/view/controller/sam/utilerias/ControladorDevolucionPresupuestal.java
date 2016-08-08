package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayDevolucionesPresupuestales;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/consultas/devolucion_presupuestal.action")
public class ControladorDevolucionPresupuestal extends ControladorBase{

	@Autowired
	GatewayDevolucionesPresupuestales gatewayDevolucionesPresupuestales;
	
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	GatewayPlanArbit gatewayPlanArbit;
	
	@Autowired 
	GatewayMeses gatewayMeses;
	
	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	
	public ControladorDevolucionPresupuestal(){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		Integer  unidad = (request.getParameter("unidad")!=null)? Integer.parseInt(request.getParameter("unidad").toString()): 0;
		Long  idDevolucion = (request.getParameter("idDevolucion")!=null)? Long.parseLong(request.getParameter("idDevolucion").toString()): 0;
		
		if(unidad==0) unidad = Integer.parseInt(this.getSesion().getIdUnidad());
		
		if(idDevolucion!=0) {
			Map datos = new HashMap();
			datos = gatewayDevolucionesPresupuestales.getDevolucionPresupuestal(idDevolucion);
			modelo.put("devolucion", datos);
			modelo.put("num_devolucion", datos.get("NUM_DEVOLUCION").toString());
		}
		
		modelo.put("idDevolucion", idDevolucion);
		modelo.put("unidad", unidad);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		//modelo.put("unidadesAdmiva",gatewayUnidadAdm.getUnidadAdmTodos());
		return "sam/consultas/devolucion_presupuestal.jsp";
	}
	
	@ModelAttribute("tipodeGasto")
    public List<Map> getTiposDeGasto(){
    	return gatewayPlanArbit.getTipodeGasto();
    }
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	@ModelAttribute("mesesActivos")
	public List<Map> getMesesActivos(){
		return gatewayMeses.getTodosMesesEjercicioActivos(getSesion().getEjercicio());
	}
	
	@ModelAttribute("retenciones")
	public List<Map> getRetenciones(){
		return gatewayOrdenDePagos.getTodasTipoRetencionesTodas();	
	}
	
	public Long guardarDevolucionPresupuestal(Long idDevolucion, int idUnidad, int periodo, int idRecurso, String fecha, String concepto, String descripcion){
		return gatewayDevolucionesPresupuestales.guardarDevolucionPresupuestal(idDevolucion, idUnidad, periodo, idRecurso, fecha, concepto, descripcion, this.getSesion().getIdUsuario(), this.getSesion().getEjercicio(), this.getSesion().getIdGrupo());
	}
	
	public String agregarConcepto(Long idDetalle, Long idDevolucion, Long idproyecto, String partida, Double importe, String nota, String tipo){
		return gatewayDevolucionesPresupuestales.agregarConcepto(idDetalle, idDevolucion, idproyecto, partida, importe, nota, tipo);
	}
	
	public List<Map> getDetallesDevolucion(Long idDevolucion){
		return gatewayDevolucionesPresupuestales.getDetallesDevolucion(idDevolucion);
	}
	
	public void eliminarDetallesDevolucion(final Long[] idDetalles, Long idDevolucion){
		gatewayDevolucionesPresupuestales.eliminarDetallesDevolucion(idDetalles, idDevolucion);
	}
	
	public String cerrarDevolucion(Long idDevolucion){
		return gatewayDevolucionesPresupuestales.cerrarDevolucion(idDevolucion, this.getSesion().getEjercicio());
	}
	
	public void cargarMovimientosOrdenPago(final Long[] idMovtos, final Long idDevolucion)
	{
		gatewayDevolucionesPresupuestales.cargarMovimientosOrdenPago(idMovtos, idDevolucion);
	}
	
}
