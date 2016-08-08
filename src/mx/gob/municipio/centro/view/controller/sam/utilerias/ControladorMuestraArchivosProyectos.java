package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/consultas/muestra_archivos_proyectos.action")
public class ControladorMuestraArchivosProyectos extends ControladorBase {

	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		modelo.put("muestraArchivos", this.getArchivosProyecto(Long.parseLong(request.getParameter("ID_PROYECTO").toString())));
		modelo.put("ID_PROYECTO", Long.parseLong(request.getParameter("ID_PROYECTO").toString()));
	    return "sam/consultas/muestra_archivos_proyectos.jsp";
	}
	
	@ModelAttribute("tipoDocumentos")
    public List getTipoDocumentosTodos() {	   
 	   return getJdbcTemplate().queryForList("select T_DOCTO, DESCR   from  TIPODOC_OP order by DESCR ");
 }
	
	 /* Documentos */
    public List getArchivosProyecto (Long ID_PROYECTO) {
   	 return this.getJdbcTemplate().queryForList("SELECT * FROM SAM_PROYECTOS_ARCHIVOS WHERE ID_PROYECTO =?", new Object[]{ID_PROYECTO});
    }
}
