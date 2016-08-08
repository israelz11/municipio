/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayUnidadAdm extends BaseGateway {

	public GatewayUnidadAdm(){
		
	}
	
	public List<Map> getUnidadAdmTodos(){		
	  return this.getJdbcTemplate().queryForList("SELECT ID, CLV_UNIADM, CLV_DEPENDENCIA, DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE STATUS = 1 ORDER BY DEPENDENCIA ASC");		
	}
	
	public List<Map> getAreasPorUnidad(String clv_uniadm){		
		  return this.getJdbcTemplate().queryForList("SELECT ID, DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID = ? ORDER BY DEPENDENCIA ASC", new Object []{clv_uniadm});		
		}
	
	public List<Map> getCapitulos(){
		return this.getJdbcTemplate().queryForList("SELECT *FROM CAT_CAPITU");
	}
	
	public String getNombreUnidad(int idDependencia){
		return (String)this.getJdbcTemplate().queryForObject("SELECT DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID=?", new Object[]{idDependencia}, String.class);
	}
	
}
