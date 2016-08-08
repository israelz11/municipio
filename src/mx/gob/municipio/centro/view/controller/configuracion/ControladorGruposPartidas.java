/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;

import java.util.Iterator;
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
import mx.gob.municipio.centro.model.gateways.sam.GatewayCatalogoPartidas;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGrupos;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGruposPartidas;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGruposProyectos;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/configuracion/configuracion_grupos_partidas.action")
public class ControladorGruposPartidas  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorGruposPartidas.class.getName());
	
	@Autowired
	GatewayGruposPartidas gatewayGruposPartidas;
	
	@Autowired
	GatewayGruposProyectos gatewayGruposProyectos;
	@Autowired
	GatewayGrupos gatewayGrupos;
	@Autowired
	GatewayCatalogoPartidas gatewayCatalogoPartidas;
	
	public ControladorGruposPartidas(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {		
		modelo.put("capitulo", gatewayCatalogoPartidas.getCapitulos());
		modelo.put("grupos", gatewayGrupos.getGruposEstatus("ACTIVO","PARTIDAS"));
	    return "sam/configuracion/configuracion_grupos_partidas.jsp";
	}
	     
	  public void  guardarPartidaGrupo( final List<String> partidas,final Integer grupo ,final Integer capitulo ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                	getJdbcTemplate().update("DELETE FROM SAM_TEMP_USR_PROY_PART WHERE ID_GRUPO_PARTIDA = ?", new Object[]{grupo});
	                	gatewayGruposPartidas.eliminar(grupo,capitulo);
	                	for (String  partida :partidas){
	                		gatewayGruposPartidas.inserta(partida, grupo);
	                	}
	                	getJdbcTemplate().update("UPDATE SAM_GRUPO_PARTIDAS SET ID_GRUPO_PROYECTO = (SELECT TOP 1 ISNULL(A.ID_GRUPO_PROYECTO,0) AS ID FROM SAM_GRUPO_PARTIDAS A WHERE A.ID_GRUPO_CONFIG = SAM_GRUPO_PARTIDAS.ID_GRUPO_CONFIG) WHERE ID_GRUPO_CONFIG =?", new Object[]{grupo});
	                	getJdbcTemplate().update("INSERT INTO SAM_TEMP_USR_PROY_PART SELECT ID_USUARIO, ID_GRUPO_CONFIG, PROYECTO, ID_PROYECTO, CLV_PARTID, ID_DEPENDENCIA, ID_GRUPO_PROYECTO, ID_GRUPO_PARTIDA, ID_RECURSO FROM VT_SAM_USUARIO_PROYECTOS_PARTIDAS WHERE VT_SAM_USUARIO_PROYECTOS_PARTIDAS.ID_GRUPO_PARTIDA = ?", new Object[]{grupo});
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
	  
     public List getGrupoPartidas(Integer  grupo, Integer capitulo) {
    	 
      	  return gatewayGruposPartidas.getGrupoPartidas(grupo, capitulo);
   	  } 
     
     public String getGruposProyectos(Integer  grupo_part) {
    	  Map mp = this.getJdbcTemplate().queryForMap("SELECT TOP 1 ISNULL(ID_GRUPO_PROYECTO,0) AS ID_GRUPO_PROYECTO FROM SAM_GRUPO_PARTIDAS WHERE ID_GRUPO_CONFIG =?",new Object[]{grupo_part});
    	  if(mp.get("ID_GRUPO_PROYECTO")==null) mp.put("ID_GRUPO_PROYECTO", 0);
     	  List <Map> lst = this.getJdbcTemplate().queryForList("SELECT *FROM SAM_GRUPO_CONFIG where ESTATUS='ACTIVO' and  TIPO='PROYECTO' ORDER BY GRUPO_CONFIG ASC");
     	  String s = ""; 
     		  s+="<option value=0 selected>[Seleccione un grupo]</option>";
     	  for(Map m:lst){
     		  s+="<option value='"+m.get("ID_GRUPO_CONFIG").toString()+"' "+(Integer.parseInt(m.get("ID_GRUPO_CONFIG").toString())==Integer.parseInt(mp.get("ID_GRUPO_PROYECTO").toString()) ? "selected":"")+">"+m.get("GRUPO_CONFIG").toString()+"</option>";
     	  }
     	  return s;
  	  }
     
     public boolean guardarGrupoProyectoEnPartidas(Integer idGrupoProyecto, Integer idGrupoPartida){
    	 try { 
    		 this.getJdbcTemplate().update("UPDATE SAM_GRUPO_PARTIDAS SET ID_GRUPO_PROYECTO = ? WHERE ID_GRUPO_CONFIG =?", new Object[]{idGrupoProyecto, idGrupoPartida});
    		 return true;
    	 }
    	 catch (DataAccessException e) {                               
             throw new RuntimeException(e.getMessage(),e);
         }	
     }
     
}
