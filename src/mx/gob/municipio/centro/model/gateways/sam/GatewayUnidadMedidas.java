/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayUnidadMedidas extends BaseGateway {
	public  GatewayUnidadMedidas(){}
	
	public List<Map> getUnidadMedidasTodas(){		
		  return this.getJdbcTemplate().queryForList("select CLV_UNIMED, UNIDMEDIDA from CAT_UNIMED");		
		}
	
	public String getUnidadMedida(String id_unidad_medida){
		if(id_unidad_medida.equals("0")|| id_unidad_medida.equals("")) 
			return "";
		else
			return (String) this.getJdbcTemplate().queryForObject("SELECT UNIDMEDIDA FROM CAT_UNIMED WHERE CLV_UNIMED =?", new Object[]{id_unidad_medida},  String.class);
	}
	
}
