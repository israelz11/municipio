/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayPlanArbit extends BaseGateway {

	public GatewayPlanArbit(){
		
	}
	
	public List<Map> getTipodeGasto(){		
	  return this.getJdbcTemplate().queryForList("SELECT DISTINCT R.ID, R.RECURSO FROM CAT_RECURSO AS R INNER JOIN CEDULA_TEC AS C ON (C.ID_RECURSO = R.ID ) ORDER BY R.RECURSO ASC");		
	}
}
