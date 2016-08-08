/**
 * @author LSC. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.List;
import java.util.Map;
import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;

public class GatewayPartidasAgregadas extends BaseGatewayAlmacen {

	//Constructor
	public GatewayPartidasAgregadas(){}

	
	
		public void inserta( String partida ){
			this.getJdbcTemplate().update("insert into PARTIDASAGREGADAS (CLV_PARTID) " +
					"VALUES (?)"
					, new Object[]{partida});
		}

		public List getPartidasAgregadas() {	   
			   return this.getJdbcTemplate().queryForList("select a.CLV_PARTID, b.PARTIDA  from PARTIDASAGREGADAS a ,CAT_PARTID b where a.CLV_PARTID=b.CLV_PARTID ");
		}

		public void eliminar(String partida){
			this.getJdbcTemplate().update("delete from PARTIDASAGREGADAS where CLV_PARTID= ?  ", new Object[]{partida});
		}
				
				
}
