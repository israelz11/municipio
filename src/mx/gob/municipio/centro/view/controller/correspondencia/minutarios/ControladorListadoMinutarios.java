package mx.gob.municipio.centro.view.controller.correspondencia.minutarios;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayMinutarios;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseCorrespondencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/correspondencia/minutarios/lst_minutarios.action")
public class ControladorListadoMinutarios extends ControladorBaseCorrespondencia {
	
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewayMinutarios gatewayMinutarios;
	
	public ControladorListadoMinutarios(){}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat theSDF = new SimpleDateFormat("yyyy");
		String syear = theSDF.format(new Date());
		Long idDependencia = (request.getParameter("cbodependencia")==null ? Long.parseLong(this.getSesion().getClaveUnidad()) : Long.parseLong(request.getParameter("cbodependencia").toString()));
		String x = request.getParameter("cboanio");
		int año = (request.getParameter("cboanio")==null ? Integer.parseInt(syear.toString()) : Integer.parseInt(request.getParameter("cboanio").toString()));
		Long idMinutario = (request.getParameter("cbominutario")==null ? gatewayMinutarios.getMinutarioDefault(idDependencia): Long.parseLong(request.getParameter("cbominutario").toString()));
		Long idDependenciaDestino = (request.getParameter("cbodependenciaDestino")==null ? 0L: Long.parseLong(request.getParameter("cbodependenciaDestino").toString()));
		int cve_persDestino = (request.getParameter("ID_PERSONA_DESTINO")==null ? 0: Integer.parseInt(request.getParameter("ID_PERSONA_DESTINO").toString()));
		String fechaInicial = request.getParameter("txtfechaInicial");
		String fechaFinal = request.getParameter("txtfechaFinal");
		String numero = request.getParameter("txtnumero");
		
		String nombreUnidad = this.getSesion().getUnidad();
		modelo.put("idUnidad",idDependencia);
		modelo.put("idDependenciaDestino", idDependenciaDestino);
		modelo.put("idMinutario", idMinutario);
		modelo.put("minutarios", getMinutariosCombo(idDependencia, idMinutario));
		modelo.put("cve_persDestino", cve_persDestino);
		modelo.put("year", año);
		modelo.put("numero", numero);
		modelo.put("fechaInicial", fechaInicial);
		modelo.put("fechaFinal", fechaFinal);
		modelo.put("nombreUnidad",nombreUnidad);
		modelo.put("personaDestino", getNombrePersona(cve_persDestino));
		modelo.put("listaNumeros", gatewayMinutarios.getListadoNumeros(modelo));
		return "correspondencia/minutarios/lst_minutarios.jsp";
	}
	
	public void reactivarMinutario(Long idMinutario){
		gatewayMinutarios.reactivarMinutario(idMinutario);
	}
	
	public void aperturarMinutario(Long[] idMinutario){
		gatewayMinutarios.aperturarMinutario(idMinutario);
	}
	
	public void cancelarMinutario(Long idMinutario){
		gatewayMinutarios.cancelarMinutario(idMinutario);
	}
	
	public String getNombrePersona(int cve_pers){
		if(cve_pers==0)
			return "";
		else
			return (String) this.getJdbcTemplate().queryForObject("SELECT (NOMBRE + ' '+APE_PAT+' '+APE_MAT) FROM PERSONAS WHERE CVE_PERS = ? ", new Object[]{cve_pers }, String.class);
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
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	@ModelAttribute("periodos")
	public List<Map> getPeriodos(){
		return gatewayMinutarios.getPeriodos();
	}
	
}
