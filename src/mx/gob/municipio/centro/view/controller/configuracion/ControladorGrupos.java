/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;

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

import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGrupos;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/configuracion/grupos.action")
public class ControladorGrupos  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorGrupos.class.getName());
	@Autowired
	GatewayGrupos gatewayGrupos;
	
	public ControladorGrupos(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {	
	    return "sam/configuracion/grupos.jsp";
	}	
	
	 public  void guardarGrupo(Integer clave, String descripcion, String estatus,String tipo){
   		gatewayGrupos.actualizarPrincipal(clave, descripcion, estatus,tipo);
     }
     
	  public void  eliminarGrupo( final List<Integer> grupos ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Integer grupo :grupos)
	                		gatewayGrupos.eliminar(grupo);	                			                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
	  
     public List getGruposEstatus(String  estatus,String tipo) {
      	  return gatewayGrupos.getGruposEstatus(estatus,tipo);
   	  }  
     
     @ModelAttribute("tipoGrupos")
	    public List<Map> getTipoGrupos(){
	    	return 	gatewayGrupos.getTipoGrupos();
	    }
     
     
}
