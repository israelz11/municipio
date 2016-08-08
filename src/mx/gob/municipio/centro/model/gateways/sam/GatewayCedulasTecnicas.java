/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayCedulasTecnicas extends BaseGateway {

	public GatewayCedulasTecnicas(){		
	}
	
		
	public List<Map> getProyectosPorUnidad(String claveUnidad){
		return this.getJdbcTemplate().queryForList(" select ID_PROYECTO,DECRIPCION   from CEDULA_TEC where ID_DEPENDENCIA=?  ", new Object []{claveUnidad});
	}
	
	public List<Map> getProyectosTodos(){
		return this.getJdbcTemplate().queryForList(" select  ID_PROYECTO, DECRIPCION   from CEDULA_TEC   ");
	}
		
	public int getProyectoParbit(String proyecto){
		if(proyecto!=null)
			return this.getJdbcTemplate().queryForInt(" select  ID_RECURSO  from CEDULA_TEC where  ID_PROYECTO=? ",new Object[]{proyecto});
		else 
			return 0;
	}
	
}
