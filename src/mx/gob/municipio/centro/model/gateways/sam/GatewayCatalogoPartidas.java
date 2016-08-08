/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayCatalogoPartidas extends BaseGateway {

	public GatewayCatalogoPartidas(){
		
	}
	
	public List<Map> getPartidasTodos(){		
	  return this.getJdbcTemplate().queryForList("Select ID_CUENTA_PRESUPUESTAL id, clv_partid CLAVE, partida DESCRIPCION from cat_partid ");		
	}
	public List getPartidasCapitulo(Integer capitulo ) {	   
		   return this.getJdbcTemplate().queryForList(" select * from CAT_PARTID where  CLV_CAPITU=? ", new Object[]{capitulo});
	}
	
	public List getCapitulos() {	   
		   return this.getJdbcTemplate().queryForList(" select * from CAT_CAPITU ");
	}
}
