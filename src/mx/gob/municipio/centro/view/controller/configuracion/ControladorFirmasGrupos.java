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

import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayFirmasGrupos;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGrupos;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/configuracion/configuracion_firmas_grupos.action")
public class ControladorFirmasGrupos  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorFirmasGrupos.class.getName());
	
	@Autowired
	GatewayFirmasGrupos gatewayFirmasGrupos;
	@Autowired
	GatewayGrupos gatewayGrupos;
	
	public ControladorFirmasGrupos(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {		
		modelo.put("grupos", gatewayGrupos.getGruposEstatus("ACTIVO","FIRMA"));
		modelo.put("tipoFirmas",getTipoFirmas());		
	    return "sam/configuracion/configuracion_firmas_grupos.jsp";
	}
	
	public List getTipoFirmas(){		   
		   return this.getJdbcTemplate().queryForList("select DESCRIPCION  TIPO from SAM_CAT_TIPOS WHERE TIPO='TIPO_FIRMA' ORDER BY TIPO ASC");
	}
	public boolean  esFirmaGrupoValido(Integer clave,Integer grupo,String tipo) {
		if (clave==null)
			clave=-1;
		int resultado = this.getJdbcTemplate().queryForInt("select count(*) from SAM_GRUPO_FIRMAS where TIPO=? and ID_GRUPO_CONFIG=? and ID_FIRMA_GRUPO!=?",new Object []{tipo,grupo,clave}  );		
		return resultado == 0;
	}
	
	 public  boolean guardarFirmaGrupo(Integer clave,String tipo,String cargo,String representante,Integer grupo){
		 if (esFirmaGrupoValido(clave,grupo,tipo)) { 
		   gatewayFirmasGrupos.actualizarPrincipal(clave,tipo,cargo,representante,grupo);
		 return true;
		 }
		 else
			 return false;
     }
     
	  public void  eliminarFirmaGrupo( final List<Integer> firmasGrupos ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Integer firmagrupo :firmasGrupos)
	                		gatewayFirmasGrupos.eliminar(firmagrupo);	                			                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
	  
     public List getFirmasPorGrupo(Integer  grupo) {
      	  return gatewayFirmasGrupos.getGruposEstatus(grupo);
   	  }  
     
	
}
