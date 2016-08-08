/**
 * @author LSC. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.almacen.configuracion;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayUsuariosAlmacen;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/administracion/asignacion_usuarios_almacen.action")

public class ControladorAsignacionAlmacen extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorAsignacionAlmacen.class.getName());
	
	@Autowired
	GatewayAlmacen gatewayAlmacen;	

	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	GatewayUsuariosAlmacen gatewayUsuariosAlmacen;

	public ControladorAsignacionAlmacen() {
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {	
	    return "almacen/administracion/asignacion_usuarios_almacen.jsp";
	}
	
	public  String  guardarUsuarioAlmacen(Integer idAlmacen, Integer idUsuario ){
		String mensaje="";
		try {
			gatewayUsuariosAlmacen.inserta(idAlmacen,idUsuario);
			mensaje="La información se almaceno satisfactoriamente. ";
		}
		 catch (DataAccessException e) {
			mensaje="El usuario ya esta asignado a almacen ";
		}
		
		return mensaje;
	  }
	     
	public void  eliminarUsuarioAlmacen( final List<Integer> usuariosAlmacen, final Integer idAlmacen ) {
			  try {                 
		            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
		                @Override
		    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
		                	for (Integer idUsuario  : usuariosAlmacen)
		                		gatewayUsuariosAlmacen.eliminar(idAlmacen,idUsuario);	                			                	
		                } });
		                } catch (DataAccessException e) {            
		                    log.info("Los registros tienen relaciones con otras tablas ");	                    
		                    throw new RuntimeException(e.getMessage(),e);
		                }	                	                		  	  
		  }
	
	public List<Map> getUsuarioAlmacenes(String unidad, Integer idAlmacen){		
		return gatewayUsuariosAlmacen.getUsuarioAlmacenes(unidad,idAlmacen);
	}
	
	@ModelAttribute("unidades")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();
    }
	 
}
