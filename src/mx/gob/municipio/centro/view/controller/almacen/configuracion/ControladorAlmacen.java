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

import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/administracion/almacen.action")

public class ControladorAlmacen extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorTiposDocumentos.class.getName());
	
	@Autowired
	GatewayAlmacen gatewayAlmacen;
	
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	GatewayUsuarios gatewayUsuarios;
	
	public ControladorAlmacen() {
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {	
	    return "almacen/administracion/almacen.jsp";
	}
	 public  void guardarAlmacen(Integer clave,String unidad,String descripcion,String estatus,Long responsable, String alias, String alarma,String email){
		 gatewayAlmacen.actualizarPrincipal(clave,unidad,descripcion,estatus,responsable,alias,alarma,email);
	     }
	     
		  public void  eliminarAlmacen( final List<Integer> grupos ) {
			  try {                 
		            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
		                @Override
		    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
		                	for (Integer grupo :grupos)
		                		gatewayAlmacen.eliminar(grupo);	                			                	
		                } });
		                } catch (DataAccessException e) {            
		                    log.info("Los registros tienen relaciones con otras tablas ");	                    
		                    throw new RuntimeException(e.getMessage(),e);
		                }	                	                		  	  
		  }
		  
	     public List getAlmacenes(String unidad) {
	      	  return gatewayAlmacen.getAlmacenes(unidad);
	   	  }  
	     
	     public List getResponsables(String unidad) {
	      	  return gatewayUsuarios.getTrabajadoresUnidad(unidad);
	   	  }
	     
	     @ModelAttribute("unidades")
		    public List<Map> getUnidades(){
		    	return gatewayUnidadAdm.getUnidadAdmTodos();
		    }
	     
	 
}
