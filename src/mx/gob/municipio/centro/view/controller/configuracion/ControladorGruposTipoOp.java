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
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GruposTipoOpGateway;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/configuracion/configuracion_grupos_tipo_op.action")
public class ControladorGruposTipoOp  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorGruposTipoOp.class.getName());
	
	@Autowired
	GruposTipoOpGateway gruposTipoOpGateway;
	
	@Autowired
	GatewayGrupos gatewayGrupos;
	
	public ControladorGruposTipoOp(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {				
		modelo.put("grupos", gatewayGrupos.getGruposEstatus("ACTIVO","TIPO OP"));
	    return "sam/configuracion/configuracion_grupos_tipo_op.jsp";
	}
	     
	  public void  guardarTipoOpGrupo( final List<Integer> tiposOp,final Integer grupo ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	gruposTipoOpGateway.eliminar(grupo);
	                	for (Integer  tipoOp :tiposOp)
	                		gruposTipoOpGateway.inserta(tipoOp, grupo);	                			                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
	  
     public List getGrupoTipoOp(Integer  grupo) {
      	  return gruposTipoOpGateway.getGrupoTipoOp(grupo);
   	  }    
     
}
