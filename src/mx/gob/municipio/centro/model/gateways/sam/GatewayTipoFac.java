package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayTipoFac extends BaseGateway {
	
	
	public GatewayTipoFac() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Map> getTipoFac(){
		return this.getJdbcTemplate().queryForList("select * from SAM_CAT_TIPO_FACTURAS ");	
		
	}
	
	public List<Map> getTipoOredenesPagosEstatusActivos(){		
		  return this.getJdbcTemplate().queryForList("select * from SAM_CAT_TIPO_FACTURAS where status='ACTIVO' ");		
		}
	
	
	public List<Map> getTipoDocumentosUsuario(Integer idUsuario ){
  	return this.getJdbcTemplate().queryForList("select * from SAM_CAT_TIPO_FACTURAS where status='ACTIVO' and  ID_TIPO_FAC in  (select * from SAM_CAT_TIPO_FACTURAS where status='ACTIVO'  and  ID_TIPO_FAC in  (SELECT  b.id_tipo_fact  FROM SAM_GRUPO_CONFIG_USUARIO a INNER JOIN SAM_GRUPO_TIPO_FACTURA b ON a.ID_GRUPO_CONFIG = b.ID_GRUPO_CONFIG  where a.ID_USUARIO =? ) ", new Object []{idUsuario});

	
  }
}
