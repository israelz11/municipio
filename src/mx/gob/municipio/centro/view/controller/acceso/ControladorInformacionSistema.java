/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.acceso;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/secundaria/errorAccesoPagina.action")
public class ControladorInformacionSistema extends ControladorBase {
    
    private static Logger log =   Logger.getLogger(ControladorInformacionSistema.class.getName());
        
    public ControladorInformacionSistema() {
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)    
    public String handleRequest(Map modelo, HttpServletRequest request ) {
    	String mensaje="";
    	int op = Integer.parseInt( (String)request.getParameter("op") );
    	switch (op)  {  
        case 1: mensaje="aki va el mensaje ";
    	}
    	modelo.put("mensaje", mensaje);
        return "secundaria/errorAccesoPagina.jsp";
    }                
}
