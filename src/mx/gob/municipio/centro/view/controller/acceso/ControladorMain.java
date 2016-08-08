/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.acceso;

import java.util.Map;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/menu/main.action")
public class ControladorMain extends ControladorBase   {
    
    private static Logger log =   Logger.getLogger(ControladorMain.class.getName());
        
    public ControladorMain() {
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)    
    public String handleRequest(Map modelo ) {               
        return "menu/main.jsp";
    }   
    
    @Autowired 
    GatewayUsuarios gatewayUsuarios;
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.POST)    
    public String processSubmit( ModelMap  modelo,  HttpServletRequest request) {
    	String mainDesc=request.getParameter("mainDesc");    	
        modelo.addAttribute("imagen",mainDesc);
        modelo.put("CVE_PERS", this.getSesion().getIdUsuario());
        return "menu/main.jsp";
    }  
    
}
