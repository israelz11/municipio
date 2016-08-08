/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.acceso;

import java.util.Map;
import java.util.logging.Logger;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/menu/footer.action")
public class ControladorFooter extends ControladorBase {
    
    private static Logger log =   Logger.getLogger(ControladorFooter.class.getName());
        
    public ControladorFooter() {
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)    
    public String handleRequest(Map modelo) {        
        return "menu/footer.jsp";
    }                
}
