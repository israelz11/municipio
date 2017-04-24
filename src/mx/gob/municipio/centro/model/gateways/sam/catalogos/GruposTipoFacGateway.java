package mx.gob.municipio.centro.model.gateways.sam.catalogos;

import java.util.List;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GruposTipoFacGateway extends BaseGateway{


	public GruposTipoFacGateway(){
	}
	
	public void inserta (Integer tipoFac,Integer grupo) {
		this.getJdbcTemplate().update("INSERT INTO SAM_GRUPO_TIPO_FACTURA (id_grupo_config,id_tipo_fact)" +
						"VALUES (?,?)", new Object[]{grupo,tipoFac});
	}
	
	public void eliminar(Integer grupo  ){
		this.getJdbcTemplate().update("delete from SAM_GRUPO_TIPO_FACTURA where ID_GRUPO_CONFIG= ? ", new Object[]{grupo});
	}
	
	public List getGrupoTipoFac(Integer  grupo) {
		return this.getJdbcTemplate().queryForList("SELECT SAM_CAT_TIPO_FACTURAS.ID_TIPO_FAC,SAM_CAT_TIPO_FACTURAS.DESCRIPCION,SAM_GRUPO_TIPO_FACTURA.id_tipo_fact"+ 
		" FROM SAM_CAT_TIPO_FACTURAS LEFT OUTER JOIN"+
		" SAM_GRUPO_TIPO_FACTURA ON SAM_GRUPO_TIPO_FACTURA.id_tipo_fact=SAM_CAT_TIPO_FACTURAS.ID_TIPO_FAC AND"+
		" SAM_GRUPO_TIPO_FACTURA.id_grupo_config=? WHERE SAM_CAT_TIPO_FACTURAS.STATUS='ACTIVO' ", new Object[]{grupo});	  
		
	}
}
