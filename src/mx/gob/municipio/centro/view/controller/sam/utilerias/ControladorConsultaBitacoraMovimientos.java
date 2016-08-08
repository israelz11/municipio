/**
 * @author Ing. Israel de la Cruz Hdez.
 * @version 1.0, Date: 18/Oct/2010
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.Map;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/muestraBitacora.action")

public class ControladorConsultaBitacoraMovimientos extends ControladorBase {
	private static Logger log = Logger.getLogger(ControladorConsultaBitacoraMovimientos.class.getName());
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = RequestMethod.GET) 
	public String  requestGetControlador(Map modelo, HttpServletRequest request) {
		try {  
		// TODO Auto-generated method stub
		Long cve_doc = request.getParameter("cve_doc")==null ? 0: request.getParameter("cve_doc").equals("")? null: Long.parseLong(request.getParameter("cve_doc"));	
		Integer v = request.getParameter("v")==null ? 0: Integer.parseInt(request.getParameter("v").toString());  
		String tipo_doc = request.getParameter("tipo_doc")==null ? "": request.getParameter("tipo_doc").toString();
		List <Map> bitacora = this.getBitacora(cve_doc, tipo_doc);
		modelo.put("bitacora",bitacora);
		modelo.put("tipo_doc", tipo_doc);
		modelo.put("v", v);
		for(Map row: bitacora)
		{
			modelo.put("documento", row.get("NUM_DOC").toString());
			break;
		}
		if(tipo_doc.equals("OP")) tipo_doc = "Orden de Pago";
		if(tipo_doc.equals("PED")) tipo_doc = "Pedido";
		if(tipo_doc.equals("REQ")) tipo_doc = "Requisición";
		if(tipo_doc.equals("VAL")) tipo_doc = "Vales";
		if(tipo_doc.equals("CON")) tipo_doc = "Contrato";
		modelo.put("tipo", tipo_doc);
		modelo.put("cve_doc", cve_doc);
		
		return "sam/consultas/muestraBitacora.jsp";
		}
		catch (DataAccessException e) {	    
			log.info(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
       }
	}
	
	public ControladorConsultaBitacoraMovimientos(){}
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("muestraBitacora")
	public List <Map> getBitacora(Long cve_doc, String tipo_doc){
		try {  
			return this.getJdbcTemplate().queryForList("SELECT CVE_DOC, NUM_DOC, ID_PROYECTO, N_PROGRAMA, PARTIDA, DES_MOV, MONTO, LOGIN, DESCRIPCION, FECHA, CONVERT(varchar(10), FECHA_DOC,103) AS FECHA_DOC FROM VT_SAM_BITACORA WHERE CVE_DOC = ? AND TIPO_DOC = ? ORDER BY FECHA ASC", new Object[]{cve_doc, tipo_doc});
		}
		catch (DataAccessException e) {	    
			log.info(e.getMessage());
	        throw new RuntimeException(e.getMessage(),e);
	   }
	}
	

}
