package mx.gob.municipio.centro.view.controller.correspondencia.documental;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayCorrespondencia;
import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayMinutarios;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseCorrespondencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/correspondencia/documental/correspondencia.action")
public class ControladorCorrespondencia extends ControladorBaseCorrespondencia {
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	GatewayCorrespondencia gatewayCorrespondencia;
	@Autowired 
	GatewayMinutarios gatewayMinutarios;
	
	public ControladorCorrespondencia(){}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat theSDF = new SimpleDateFormat("yyyy");
		String syear = theSDF.format(new Date());
		Long idDependencia = (request.getParameter("cbodependencia")==null ? Long.parseLong(this.getSesion().getClaveUnidad()) : Long.parseLong(request.getParameter("cbodependencia").toString()));
		
		Long idCorrespondencia = (request.getParameter("ID_CORRESPONDENCIA")==null ? 0l: Long.parseLong(request.getParameter("ID_CORRESPONDENCIA").toString()));
		Long idDependenciaDestino = (request.getParameter("cbodependenciaDestino")==null ? 0L: Long.parseLong(request.getParameter("cbodependenciaDestino").toString()));
		int cve_persDestino = (request.getParameter("cbopersonaDestino")==null ? 0: Integer.parseInt(request.getParameter("cbopersonaDestino").toString()));
		String fechaRecepcion = request.getParameter("txtfechaRecepcion");
		String fechaDocumento = request.getParameter("txtfechaDocumento");
		String fechaLimite = request.getParameter("txtfechaLimite");
		
		int idTipoCorrespondencia = (request.getParameter("cbotipoDocumento")==null ? 0: Integer.parseInt(request.getParameter("cbotipoDocumento").toString()));
		int idPrioridad = (request.getParameter("cboprioridad")==null ? 0: Integer.parseInt(request.getParameter("cboprioridad").toString()));
		int idOrigen = (request.getParameter("cboorigen")==null ? 0: Integer.parseInt(request.getParameter("cboorigen").toString()));
		int idTipoAviso = (request.getParameter("cbotipoAviso")==null ? 0: Integer.parseInt(request.getParameter("cbotipoAviso").toString()));
				
		String numero = request.getParameter("txtnumero");
		
		SimpleDateFormat fformat = new SimpleDateFormat("dd/MM/yyyy");
		String sfecha = fformat.format(new Date());
		
		if(fechaRecepcion==null) fechaRecepcion = sfecha;
		if(fechaDocumento==null) fechaDocumento = sfecha;
		if(fechaLimite==null) fechaLimite = sfecha;
		
		if(idTipoCorrespondencia==0) idTipoCorrespondencia = 1;
		if(idPrioridad==0) idPrioridad=1;
		if(idOrigen==0) idOrigen=2;
		if(idTipoAviso==0) idTipoAviso=1;
		
			
		String nombreUnidad = this.getSesion().getUnidad();
		modelo.put("idUnidad",idDependencia);
		modelo.put("idDependenciaDestino", idDependenciaDestino);
		modelo.put("ID_CORRESPONDENCIA", idCorrespondencia);
		modelo.put("cve_persDestino", cve_persDestino);
		modelo.put("numero", numero);
		modelo.put("fechaRecepcion", fechaRecepcion);
		modelo.put("fechaDocumento", fechaDocumento);
		modelo.put("fechaLimite", fechaLimite);
		modelo.put("idTipoCorrespondencia",idTipoCorrespondencia);
		modelo.put("idPrioridad", idPrioridad);
		modelo.put("idOrigen", idOrigen);
		modelo.put("idTipoAviso", idTipoAviso);
		
		modelo.put("nombreUnidad",nombreUnidad);
		modelo.put("personasDestino", gatewayCorrespondencia.gePersonasUnidadCombo(idDependenciaDestino, cve_persDestino));
		return "correspondencia/documental/correspondencia.jsp";
	}
	
	public Map guardarCorrespondencia(Long idCorrespondencia, String numeroDocumento, int idDependenciaFuente, int idDependenciaDestino, int cve_persDestino, int idTipoDocumento, int idPrioridad, int idOrigen, int idTipoAviso, String asunto, String acuerdo, String fechaRecepcion, String fechaDocumento, String fechaLimite, String  CVE_PERS ){
		return gatewayCorrespondencia.guardarCorrespondencia(idCorrespondencia, numeroDocumento, idDependenciaFuente, idDependenciaDestino, cve_persDestino, idTipoDocumento, idPrioridad, idOrigen, idTipoAviso, asunto, acuerdo, fechaRecepcion, fechaDocumento, fechaLimite, this.getSesion().getIdUsuario(), CVE_PERS);
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
	
	@ModelAttribute("tipoOrigen")
	public List<Map> getListTipoOrigen(){
		return gatewayCorrespondencia.getListTipoOrigen();
	}
	
	@ModelAttribute("tipoAvisos")
	public List<Map> getListTipoAvisos(){
		return gatewayCorrespondencia.getListTipoAvisos();
	}
	
	public String gePersonasUnidadCombo(Long idDependenciaDestino, int cve_persDestino){
		return gatewayCorrespondencia.gePersonasUnidadCombo(idDependenciaDestino, cve_persDestino);
	}
	
	public List<Map> getPersonasSubdireccion(){
		return gatewayMinutarios.getPersonasCorrespondencia();
	}
	
	public List<Map> getListaPersonas(String[] cve_pers){
		return gatewayMinutarios.getPersonasLista(cve_pers);
	}
	
	public String agregarDestinatario(int cve_pers, Long idMinutario){
		return gatewayMinutarios.agregarCCP(cve_pers, idMinutario);
	}
	
	public List<Map> getArchivosCorrespondencia(Long idCorrespondencia){
		return gatewayCorrespondencia.getArchivosCorrespondencia(idCorrespondencia);
	}
	
	public void eliminarArchivoCorrespondencia(Long idArchivo, HttpServletRequest request){
		gatewayCorrespondencia.eliminarArchivoCorrespondencia(this.getSesion().getIdUsuario(), idArchivo, request);
	}
	
	public void eliminarDestinatario(int cve_pers, Long idCorrespondencia){
		gatewayCorrespondencia.eliminarDestinatario(cve_pers, idCorrespondencia);
	}
	
	public void enviarCorrespondencia(Long idCorrespondencia){
		gatewayCorrespondencia.enviarCorrespondencia(idCorrespondencia);
	}
}
