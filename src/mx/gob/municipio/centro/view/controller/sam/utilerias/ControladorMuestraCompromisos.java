package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/muestra_compromisos.action")
public class ControladorMuestraCompromisos extends ControladorBase {
	private static Logger log = Logger.getLogger(ControladorMuestraCompromisos.class.getName());
	
	public ControladorMuestraCompromisos(){
		
	}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = RequestMethod.GET) 
	public String  requestGetControlador(Map modelo, HttpServletRequest request) {
		try 
		{  
			Long idproyecto = request.getParameter("idproyecto")==null ? 0L: Long.parseLong(request.getParameter("idproyecto").toString());  
			String partida = request.getParameter("partida")==null ? "": request.getParameter("partida").toString();
			Integer periodo = request.getParameter("periodo")==null ? 0: Integer.parseInt(request.getParameter("periodo").toString());
			String consulta = request.getParameter("consulta")==null ? "": request.getParameter("consulta").toString();
			
			List <Map> doc = this.getCompromisos(idproyecto, partida, periodo, consulta);
			modelo.put("documentos", doc);
			return "sam/consultas/muestra_compromisos.jsp";
		}
		catch (DataAccessException e) {	    
			log.info(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
       }
	}
	
	public List <Map> getCompromisos(Long idproyecto, String partida, Integer periodo, String consulta){
		try 
		{ 
			String sql = "SELECT *, (CASE (PERIODO) WHEN 1 THEN 'ENE' WHEN 2 THEN 'FEB' WHEN 3 THEN 'MAR' WHEN 4 THEN 'ABR' WHEN 5 THEN 'MAY' WHEN 6 THEN 'JUN' WHEN 7 THEN 'JUL' WHEN 8 THEN 'AGO' WHEN 9 THEN 'SEP' WHEN 10 THEN 'OCT' WHEN 11 THEN 'NOV' WHEN 12 THEN 'DIC' END) AS PERIODO_S FROM VT_COMPROMISOS";
			String clausula = " AND PERIODO = "+periodo;
			if(periodo==0) clausula = "";
			return this.getJdbcTemplate().queryForList(sql+" WHERE CONSULTA = ? AND ID_PROYECTO = ? AND CLV_PARTID = ? "+clausula, new Object[]{consulta, idproyecto, partida});
		}
		catch (DataAccessException e) {	    
			log.info(e.getMessage());
	        throw new RuntimeException(e.getMessage(),e);
	   }
	}

}
