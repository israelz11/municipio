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


import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGrupos;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGruposProyectos;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/configuracion/configuracion_grupos_proyectos.action")
public class ControladorGruposProyectos  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorGruposProyectos.class.getName());
	
	@Autowired
	GatewayGruposProyectos gatewayGruposProyectos;
	@Autowired
	GatewayGrupos gatewayGrupos;
	@Autowired
	public GatewayUnidadAdm gatewayUnidadAdm;
	
	public ControladorGruposProyectos(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {		
		modelo.put("grupos", gatewayGrupos.getGruposEstatus("ACTIVO","PROYECTO"));	
	    return "sam/configuracion/configuracion_grupos_proyectos.jsp";
	}
	     
	  public void  guardarProyectoGrupo( final List<String> proyectos,final Integer grupo ,final Integer unidad ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	gatewayGruposProyectos.eliminar(grupo,unidad);
	                	getJdbcTemplate().update("DELETE FROM SAM_TEMP_USR_PROY_PART WHERE ID_GRUPO_CONFIG = ?", new Object[]{grupo});
	                	for (String  proyecto :proyectos){
	                		gatewayGruposProyectos.inserta(proyecto, grupo);
	                	}
	                	getJdbcTemplate().update("INSERT INTO SAM_TEMP_USR_PROY_PART SELECT ID_USUARIO, ID_GRUPO_CONFIG, PROYECTO, ID_PROYECTO, CLV_PARTID, ID_DEPENDENCIA, ID_GRUPO_PROYECTO, ID_GRUPO_PARTIDA, ID_RECURSO FROM VT_SAM_USUARIO_PROYECTOS_PARTIDAS WHERE VT_SAM_USUARIO_PROYECTOS_PARTIDAS.ID_GRUPO_CONFIG = ?", new Object[]{grupo});
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
	  
     public List getGrupoProyectos(Integer  grupo, String unidad) {
      	  return gatewayGruposProyectos.getGrupoProyectos(grupo, unidad);
   	  }
     
     @ModelAttribute("unidadesAdmiva")
     public List<Map> getUnidadesAdmivas(){
     	return gatewayUnidadAdm.getUnidadAdmTodos();	
     }
}
