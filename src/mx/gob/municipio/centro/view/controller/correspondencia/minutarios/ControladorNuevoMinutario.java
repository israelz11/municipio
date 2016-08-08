
package mx.gob.municipio.centro.view.controller.correspondencia.minutarios;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayClasificaMinutarios;
import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayMinutarios;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseCorrespondencia;

import org.apache.commons.fileupload.FileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/correspondencia/minutarios/nuevo_minutario.action")
public class ControladorNuevoMinutario extends ControladorBaseCorrespondencia {
	private static Logger log = Logger.getLogger(ControladorNuevoMinutario.class.getName());
	
	@Autowired
	private GatewayMinutarios gatewayMinutarios;
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewayClasificaMinutarios gatewayClasificaMinutarios;
	
	public ControladorNuevoMinutario(){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST})  
	public String requestPostControlador(Map modelo, HttpServletRequest request, HttpServletResponse response, @RequestParam("archivo") MultipartFile file) throws IOException  {
		Long idMinutario = (request.getParameter("ID_MINUTARIO")==null ? 0L : Long.parseLong(request.getParameter("ID_MINUTARIO").toString()));
		Long idDependencia = (request.getParameter("cbodependencia")==null ? Long.parseLong(this.getSesion().getClaveUnidad()) : Long.parseLong(request.getParameter("cbodependencia").toString()));
		Integer año = (request.getParameter("cboaño")==null ? this.obtenerAnio(new Date()) : Integer.parseInt(request.getParameter("cboaño").toString()));
		Long idMinutarioDestino = (request.getParameter("cbominutario")==null ? 0L : Long.parseLong(request.getParameter("cbominutario").toString()));
		Long idDependenciaDestino = (request.getParameter("cbodestino")==null ? 0L : Long.parseLong(request.getParameter("cbodestino").toString()));
		Long cve_persDestino = (request.getParameter("cbosubdestino")==null ? 0L : Long.parseLong(request.getParameter("cbosubdestino").toString()));
		Long idClasifica = (request.getParameter("cboclasifica")==null ? 0L : Long.parseLong(request.getParameter("cboclasifica").toString()));
		Long idCorrespondencia = (request.getParameter("ID_CORRESPONDENCIA")==null ? 0L : Long.parseLong(request.getParameter("ID_CORRESPONDENCIA").toString()));
		String asunto = request.getParameter("txtasunto");
		String CCP = request.getParameter("CVE_PERS");
		String nombreUnidad = this.getSesion().getUnidad();
		String numero_correspondencia = "";

		
		if(idMinutario!=0){
			Map minutario = gatewayMinutarios.getMinutarioGeneral(idMinutario);

			numero_correspondencia = (String) this.getJdbcTemplate().queryForObject("SELECT NUMERO FROM SGD_CORRESPONDENCIA WHERE ID_CORRESPONDENCIA =?", new Object[]{minutario.get("ID_CORRESPONDENCIA")}, String.class);
			modelo.put("NUMERO", minutario.get("NUMERO").toString());
			modelo.put("ID_MINUTARIO", idMinutario);
			modelo.put("ID_CAT_MINUTARIO", minutario.get("ID_CAT_MINUTARIO").toString());
			modelo.put("ID_UNIDAD_DESTINO", idDependenciaDestino);
			modelo.put("ID_CORRESPONDENCIA", minutario.get("ID_CORRESPONDENCIA").toString());
			modelo.put("ID_CAT_CLASIFICACION_MINUTARIO", minutario.get("ID_CAT_CLASIFICACION_MINUTARIO").toString());
			modelo.put("year", minutario.get("AÑO").toString());
			modelo.put("ASUNTO", minutario.get("ASUNTO").toString());
			modelo.put("NUMERO_CORRESPONDENCIA", numero_correspondencia);
			modelo.put("idUnidad", minutario.get("ID_DEPENDENCIA_ENVIA"));
			modelo.put("nombreUnidad", (String)this.getJdbcTemplate().queryForObject("SELECT DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID = ?", new Object[]{ minutario.get("ID_DEPENDENCIA_ENVIA")}, String.class));
			modelo.put("minutarios", getMinutariosCombo(Long.parseLong(minutario.get("ID_DEPENDENCIA_ENVIA").toString()), 0L));
			modelo.put("clasificacion", gatewayClasificaMinutarios.getClasificaMinutarios(Long.parseLong(minutario.get("ID_DEPENDENCIA_ENVIA").toString())));
		}
		else
		{
			modelo.put("ID_MINUTARIO", idMinutario);
			
			modelo.put("ID_CAT_CLASIFICACION_MINUTARIO", idClasifica);
			modelo.put("year", año);
			modelo.put("ASUNTO", asunto);
			modelo.put("NUMERO_CORRESPONDENCIA", numero_correspondencia);
			
			modelo.put("idUnidad",idDependencia);
			modelo.put("nombreUnidad",nombreUnidad);
			modelo.put("minutarios", getMinutariosCombo(idDependencia, idCorrespondencia));
			modelo.put("clasificacion", gatewayClasificaMinutarios.getClasificaMinutarios(idDependencia));
			
		}
		
		return "correspondencia/minutarios/nuevo_minutario.jsp";
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET})  
	public String requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {
		Long idMinutario = (request.getParameter("ID_MINUTARIO")==null ? 0L : Long.parseLong(request.getParameter("ID_MINUTARIO").toString()));
		Long idDependencia = (request.getParameter("cbodependencia")==null ? Long.parseLong(this.getSesion().getClaveUnidad()) : Long.parseLong(request.getParameter("cbodependencia").toString()));
		Integer año = (request.getParameter("cboaño")==null ? this.obtenerAnio(new Date()) : Integer.parseInt(request.getParameter("cboaño").toString()));
		Long idMinutarioDestino = (request.getParameter("cbominutario")==null ? 0L : Long.parseLong(request.getParameter("cbominutario").toString()));
		Long idDependenciaDestino = (request.getParameter("cbodestino")==null ? 0L : Long.parseLong(request.getParameter("cbodestino").toString()));
		Long cve_persDestino = (request.getParameter("cbosubdestino")==null ? 0L : Long.parseLong(request.getParameter("cbosubdestino").toString()));
		Long idClasifica = (request.getParameter("cboclasifica")==null ? 0L : Long.parseLong(request.getParameter("cboclasifica").toString()));
		Long idCorrespondencia = (request.getParameter("ID_CORRESPONDENCIA")==null ? 0L : Long.parseLong(request.getParameter("ID_CORRESPONDENCIA").toString()));
		String asunto = request.getParameter("txtasunto");
		String CCP = request.getParameter("CVE_PERS");
		String nombreUnidad = this.getSesion().getUnidad();
		String numero_correspondencia = "";
		modelo.put("year", año);

		modelo.put("ID_MINUTARIO", idMinutario);
		modelo.put("ID_CAT_CLASIFICACION_MINUTARIO", idClasifica);
		modelo.put("ID_UNIDAD_DESTINO", idDependenciaDestino);
		modelo.put("year", año);
		modelo.put("ASUNTO", asunto);
		modelo.put("NUMERO_CORRESPONDENCIA", numero_correspondencia);
		
		modelo.put("idUnidad",idDependencia);
		modelo.put("nombreUnidad",nombreUnidad);
		modelo.put("minutarios", getMinutariosCombo(idDependencia, 0L));
		modelo.put("periodos", this.getPeriodos());
		modelo.put("clasificacion", gatewayClasificaMinutarios.getClasificaMinutarios(idDependencia));
		
		if(idMinutario!=0){
			Map minutario = gatewayMinutarios.getMinutarioGeneral(idMinutario);
			numero_correspondencia = (String) this.getJdbcTemplate().queryForObject("SELECT NUMERO FROM SGD_CORRESPONDENCIA WHERE ID_CORRESPONDENCIA =?", new Object[]{minutario.get("ID_CORRESPONDENCIA")}, String.class);
			modelo.put("NUMERO", minutario.get("NUMERO").toString());
			modelo.put("ID_MINUTARIO", idMinutario);
			modelo.put("ID_CAT_MINUTARIO", minutario.get("ID_CAT_MINUTARIO").toString());
			modelo.put("ID_UNIDAD_DESTINO", idDependenciaDestino);
			modelo.put("ID_CORRESPONDENCIA", minutario.get("ID_CORRESPONDENCIA").toString());
			modelo.put("ID_CAT_CLASIFICACION_MINUTARIO", minutario.get("ID_CAT_CLASIFICACION_MINUTARIO").toString());
			modelo.put("year", minutario.get("AÑO").toString());
			modelo.put("ASUNTO", minutario.get("ASUNTO").toString());
			modelo.put("NUMERO_CORRESPONDENCIA", numero_correspondencia);
			modelo.put("NUMERO", minutario.get("FOLIO").toString());
			modelo.put("ID_UNIDAD_DESTINO", minutario.get("ID_DEPENDENCIA_DESTINO").toString());
			modelo.put("nombreUnidad", (String)this.getJdbcTemplate().queryForObject("SELECT DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID = ?", new Object[]{ minutario.get("ID_DEPENDENCIA_DEST")}, String.class));
			modelo.put("minutarios", getMinutariosCombo(Long.parseLong(minutario.get("ID_DEPENDENCIA_FUENTE").toString()), Long.parseLong(minutario.get("ID_CAT_MINUTARIO").toString())));
			modelo.put("idUnidad", minutario.get("ID_DEPENDENCIA_FUENTE").toString());
			modelo.put("clasificacion", gatewayClasificaMinutarios.getClasificaMinutarios(Long.parseLong(minutario.get("ID_DEPENDENCIA_FUENTE").toString())));
		}
		
		return "correspondencia/minutarios/nuevo_minutario.jsp";
	}
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	public List<Map> getPeriodos(){
		return gatewayMinutarios.getPeriodos();
	}
	
    public String getMinutariosCombo(Long idDependencia, Long idCatMinutario){
    	return gatewayMinutarios.getMinutariosCombo(idDependencia, idCatMinutario);
    }
    
    public String getMinutariosDestinoCombo(Long idDependencia, int cve_persDest){
    	return gatewayMinutarios.getMinutariosDestinoCombo(idDependencia, cve_persDest);
    }
    
	public List<Map> getPersonasSubdireccion(){
		return gatewayMinutarios.getPersonasCorrespondencia();
	}
	
	public List<Map> getAutocompleteDocumentos(){
		return gatewayMinutarios.getAutocompleteDocumentos(this.getSesion().getIdUsuario());
	}
	
	public List<Map> getListaPersonas(String[] cve_pers){
		return gatewayMinutarios.getPersonasLista(cve_pers);
	}
	
	public Map guardarMinutario(Long idMinutario, int año, Long idDependenciaFuente, Long idMinutarioFuente, Long idDependenciaDestino, Long cve_persDestino, Long idClasifica, Long idCorrespondencia, String asunto, String cve_persCCP){
		return gatewayMinutarios.guardarNumeroMinutario(idMinutario, año, idDependenciaFuente, idMinutarioFuente, idDependenciaDestino, cve_persDestino, idClasifica, idCorrespondencia, asunto, cve_persCCP, this.getSesion().getIdUsuario());
	}
	
	public void cerrarMinutario(Long idMinutario){
		gatewayMinutarios.cerrarMinutario(idMinutario);
	}
	
	public String agregarCCP(int cve_pers, Long idMinutario){
		return gatewayMinutarios.agregarCCP(cve_pers, idMinutario);
	}
	
	public void eliminarCCP(int cve_pers, Long idMinutario){
		gatewayMinutarios.eliminarCCP(cve_pers, idMinutario);
	}
	
	public Map getCargarMinutario(Long idMinutario)
	{
		return gatewayMinutarios.getCargarMinutario(idMinutario);
	}
	
	public String getClavesPersonasCCP(Long idMinutario){
		return gatewayMinutarios.getClavesPersonasCCP(idMinutario);
	}
	
	public List<Map> getArchivosMinutario(Long idMinutario){
		return gatewayMinutarios.getArchivosMinutario(idMinutario);
	}
	
	public void eliminarArchivoMinutario(Long idArchivo, HttpServletRequest request){
		gatewayMinutarios.eliminarArchivoMinutario(idArchivo,request);
	}
}
