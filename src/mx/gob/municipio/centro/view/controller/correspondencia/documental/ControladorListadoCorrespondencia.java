package mx.gob.municipio.centro.view.controller.correspondencia.documental;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayCorrespondencia;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseCorrespondencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/correspondencia/documental/lst_busquedas.action")
public class ControladorListadoCorrespondencia extends ControladorBaseCorrespondencia {

	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	GatewayCorrespondencia gatewayCorrespondencia;
	
	public ControladorListadoCorrespondencia(){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {
		int idDependenciaFuente = (request.getParameter("cbodependencia")==null ? 0: Integer.parseInt(request.getParameter("cbodependencia").toString()));
		int idDependenciaDestino = (request.getParameter("cbodependenciaDestino")==null ? 0: Integer.parseInt(request.getParameter("cbodependenciaDestino").toString()));
		int idPersonaDestino = (request.getParameter("ID_PERSONA_DESTINO")==null ? 0: Integer.parseInt(request.getParameter("ID_PERSONA_DESTINO").toString()));
		String asunto = request.getParameter("txtasunto");
		String personaDestino = request.getParameter("txtpersonaDestino");
		String status = request.getParameter("chkstatus")==null ? "0": this.arrayToString(request.getParameterValues("status"),",");
		int idPrioridad = (request.getParameter("cboprioridad")==null ? 0: Integer.parseInt(request.getParameter("cboprioridad").toString()));
		String fechaInicial = request.getParameter("txtfechaInicial");
		String fechaFinal = request.getParameter("txtfechaFinal");
		String numero = request.getParameter("txtnumero");
		
		modelo.put("idDependenciaDestino", idDependenciaDestino);
		modelo.put("idDependenciaFuente", idDependenciaFuente);
		modelo.put("idPersonaDestino", idPersonaDestino);
		modelo.put("asunto", asunto);
		modelo.put("personaDestino", personaDestino);
		modelo.put("status", status);
		modelo.put("idPrioridad", idPrioridad);
		modelo.put("fechaInicial", fechaInicial);
		modelo.put("fechaFinal", fechaFinal);
		modelo.put("numero", numero);
		modelo.put("correspondencia", this.getListadoCorrespondencia(modelo));
		return "correspondencia/documental/lst_busquedas.jsp";
	}
	
	public List<Map> getListadoCorrespondencia(Map m){
		return gatewayCorrespondencia.getListadoCorrespondencia(m);
	}
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	@ModelAttribute("tipoCorrespondencia")
    public List<Map> getTipoCorrespondencia(){
    	return gatewayCorrespondencia.getListTipoCorrespondencia();	
    }
	
	@ModelAttribute("prioridadCorrespondencia")
	public List<Map> getListPrioridadCorrespondencia(){
		return gatewayCorrespondencia.getListPrioridadCorrespondencia();
	}
	

}
