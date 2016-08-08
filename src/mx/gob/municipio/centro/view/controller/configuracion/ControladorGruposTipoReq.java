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
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GruposTipoReqGateway;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/configuracion/configuracion_grupos_tipo_req.action")
public class ControladorGruposTipoReq  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorGruposTipoReq.class.getName());
	
	@Autowired
	GruposTipoReqGateway gruposTipoReqGateway;	
	@Autowired
	GatewayGrupos gatewayGrupos;
	
	public ControladorGruposTipoReq(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {				
		modelo.put("grupos", gatewayGrupos.getGruposEstatus("ACTIVO","TIPO REQ"));
	    return "sam/configuracion/configuracion_grupos_tipo_req.jsp";
	}
	     
	  public void  guardarTipoReqGrupo( final List<Integer> tiposOp,final Integer grupo ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	gruposTipoReqGateway.eliminar(grupo);
	                	for (Integer  tipoOp :tiposOp)
	                		gruposTipoReqGateway.inserta(tipoOp, grupo);	                			                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
	  
     public List getGrupoTipoReq(Integer  grupo) {
      	  return gruposTipoReqGateway.getGrupoTipoOp(grupo);
   	  }    
     
}
