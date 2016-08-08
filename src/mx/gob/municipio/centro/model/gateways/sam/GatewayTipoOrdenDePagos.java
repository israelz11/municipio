/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayTipoOrdenDePagos extends BaseGateway {

	public GatewayTipoOrdenDePagos(){
		
	}
	
	public List<Map> getTipoOredenesPagos(){		
	  return this.getJdbcTemplate().queryForList("select * from SAM_TIPO_ORDEN_PAGO ");		
	}
	public List<Map> getTipoOredenesPagosEstatusActivos(){		
		  return this.getJdbcTemplate().queryForList("select * from SAM_TIPO_ORDEN_PAGO where estatus='ACTIVO' ");		
		}
	
	
	public List<Map> getTipoDocumentosUsuario(Integer idUsuario ){
    	return this.getJdbcTemplate().queryForList("select * from SAM_TIPO_ORDEN_PAGO where estatus='ACTIVO'  and  ID_TIPO_ORDEN_PAGO in  (SELECT  b.ID_TIPO_ORDEN_PAGO  FROM SAM_GRUPO_CONFIG_USUARIO a INNER JOIN SAM_GRUPO_TIPO_OP b ON a.ID_GRUPO_CONFIG = b.ID_GRUPO_CONFIG  where a.ID_USUARIO =?   )  ", new Object []{idUsuario});	
    }
	
}
