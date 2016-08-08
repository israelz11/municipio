/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.seguridad;


import java.util.Map;

import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/utilerias/cambioPassword.action")
public class ControladorCambioPassword  extends ControladorBase {

	@Autowired
	public GatewayUsuarios gatewayUsuarios;
	
	/*Metodo para activar el depurador*/
	private static Logger log = Logger.getLogger(ControladorCambioPassword.class.getName());
	
	public 	ControladorCambioPassword () {	
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {	
	    return "sam/utilerias/cambioPassword.jsp";
	}
	
	public boolean guardarPassword(String passwordAnterior ,String  passwordNuevo ){
		return gatewayUsuarios.cambiarPassword(passwordAnterior, passwordNuevo, this.getSesion().getIdUsuario());
	}

}
