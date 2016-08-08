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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGrupos;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GruposValesGateway;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/configuracion/configuracion_grupos_vales.action")
public class ControladorGruposVales  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorGruposVales.class.getName());
	
	@Autowired
	GruposValesGateway gruposValesGateway;
	
	@Autowired
	GatewayGrupos gatewayGrupos;
	
	public ControladorGruposVales(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {				
		modelo.put("grupos", gatewayGrupos.getGruposEstatus("ACTIVO","VALES"));
	    return "sam/configuracion/configuracion_grupos_vales.jsp";
	}
	     
	  public void  guardarValeGrupo( final List<Integer> vales,final Integer grupo ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	gruposValesGateway.eliminar(grupo);
	                	for (Integer  vale :vales)
	                		gruposValesGateway.inserta(vale, grupo);	                			                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
	  
     public List getGrupoVales(Integer  grupo) {
      	  return gruposValesGateway.getGrupoVales(grupo);
   	  }    
     
}
