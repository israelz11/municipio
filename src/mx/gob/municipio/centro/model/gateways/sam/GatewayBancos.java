/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayBancos extends BaseGateway {

	public GatewayBancos(){
		
	}
	
	public List<Map> getBancosTodos( ){
	    return this.getJdbcTemplate().queryForList("select CLV_BNCSUC,BANCO,PLAZA,SUCURSAL,NUM_SUC,ID from CAT_BNCSUC  ");		
	}
}
