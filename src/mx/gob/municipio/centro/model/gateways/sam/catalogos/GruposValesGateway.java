/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam.catalogos;


import java.util.List;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GruposValesGateway extends BaseGateway {

	public GruposValesGateway(){
	}

	public void inserta( Integer tipoVale,Integer grupo ){		
		this.getJdbcTemplate().update("insert into SAM_GRUPO_VALE (ID_GRUPO_CONFIG,ID_TIPO_VALE ) " +
				"VALUES (?,?)", new Object[]{grupo,tipoVale});
	}

	public void eliminar(Integer grupo  ){
		this.getJdbcTemplate().update("delete from SAM_GRUPO_VALE where ID_GRUPO_CONFIG= ?    ", new Object[]{grupo});
	}	
	
	public List getGrupoVales(Integer  grupo) {	   
		   return this.getJdbcTemplate().queryForList("SELECT     SAM_GRUPO_VALE.ID_GRUPO_VALE, SAM_CAT_TIPO_VALE.CLAVE_VALE, SAM_CAT_TIPO_VALE.TIPO_VALE , SAM_CAT_TIPO_VALE.ID_TIPO_VALE "+
				   " FROM         SAM_CAT_TIPO_VALE LEFT OUTER JOIN "+ 
				   " SAM_GRUPO_VALE ON SAM_GRUPO_VALE.ID_TIPO_VALE = SAM_CAT_TIPO_VALE.ID_TIPO_VALE AND "+ 
				   " SAM_GRUPO_VALE.ID_GRUPO_CONFIG =?  ", new Object[]{grupo});
	}
	
}
