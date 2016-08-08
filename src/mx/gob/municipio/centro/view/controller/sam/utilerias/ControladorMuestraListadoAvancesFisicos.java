/**
 * @author Isc. Israel de la Cruz Hernandez.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayEvaluacionProyecto;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/utilerias/lista_evaluacion_proyectos.action")
public class ControladorMuestraListadoAvancesFisicos extends ControladorBase {

	public ControladorMuestraListadoAvancesFisicos() {
		// TODO Auto-generated method stub

	}
	
	@Autowired
	public GatewayMeses gatewayMeses;
	
	@Autowired
	public GatewayEvaluacionProyecto gatewayEvaluacionProyecto;
	
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})       
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		String v = request.getParameter("cbodependencia");
		Integer  unidad = (request.getParameter("cbodependencia")!=null)? Integer.parseInt(request.getParameter("cbodependencia").toString()): 0;
		String estatus=request.getParameter("cbostatus")==null ? "3": request.getParameter("cbostatus");
		Integer mes=request.getParameter("cbomes")==null ? 0: Integer.parseInt(request.getParameter("cbomes").toString());
		if(unidad==0) unidad = Integer.parseInt(this.getSesion().getIdUnidad());
		if(mes==0) mes = gatewayMeses.getMesActivo(this.getSesion().getEjercicio());
		modelo.put("unidad", unidad);
		modelo.put("estatus", estatus);
		modelo.put("mes", mes);
		List <Map> m = gatewayEvaluacionProyecto.getProyectos(this.getSesion().getIdUsuario(), estatus, unidad,mes, this.getSesion().getClaveUnidad());
		modelo.put("CONTADOR", m.size());
		modelo.put("avances",m);
	    return "sam/utilerias/lista_evaluacion_proyectos.jsp";
	}

	 @ModelAttribute("unidadesAdmiva")
	    public List<Map> getUnidadesAdmivas(){
	    	return gatewayUnidadAdm.getUnidadAdmTodos();	
	    }
	 
	 public void actualizarProyectos(){
		 this.getJdbcTemplate().update("INSERT INTO "+
												"SAM_EVAL_PROY_MENSUAL(ID_PROYECTO, EJERCICIO, FECHA_INICIO, FECHA_TERMINO, FECHA_ACTA, ID_DEPENDENCIA) "+
												"SELECT ID_PROYECTO, "+this.getSesion().getEjercicio()+" EJERCICIO, NULL FECHA_INICIO, NULL FECHA_TERMINO, NULL FECHA_ACTA, ID_DEPENDENCIA "+ 
												"FROM CEDULA_TEC WHERE ID_PROYECTO NOT IN (SELECT ID_PROYECTO FROM SAM_EVAL_PROY_MENSUAL)");
	 }
	
	
}
