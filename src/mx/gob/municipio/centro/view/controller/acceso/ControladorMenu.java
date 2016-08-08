/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.acceso;


import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/menu/index.action")
public class ControladorMenu extends ControladorBase   {
    
    private static Logger log =   Logger.getLogger(ControladorMenu.class.getName());
    @Autowired
    private GatewayUsuarios gatewayUsuarios;
    public ControladorMenu() {
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)  
    public String  handleRequest(Map modelo, HttpServletRequest request) {
        String a=request.getRemoteUser();
        if (getSesion()!=null) {
          this.getSesion().setHost(request.getRemoteHost());
          this.getSesion().setIp(request.getRemoteAddr());
         }         
            return "menu/index.jsp";
    }                
}
