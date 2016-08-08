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
@RequestMapping("/accesoDenegado.action")
public class ControladorAccesoDenegado extends ControladorBase  {
    
    private static Logger log =   Logger.getLogger(ControladorAccesoDenegado.class.getName());
        
    public ControladorAccesoDenegado() {
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)
    public String  handleRequest(Map modelo , HttpServletRequest request  ) {
    modelo.put("ruta",request.getContextPath());
        return "accesoDenegado.jsp";
    }                
}
