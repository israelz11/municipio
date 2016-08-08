/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam.catalogos;


import java.util.List;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GruposTipoReqGateway extends BaseGateway {

	public GruposTipoReqGateway(){
	}

	public void inserta( Integer tipoOp,Integer grupo ){		
		this.getJdbcTemplate().update("insert into SAM_GRUPO_TIPO_REQ (ID_GRUPO_CONFIG,ID_TIPO_REQ ) " +
				"VALUES (?,?)", new Object[]{grupo,tipoOp});
	}

	public void eliminar(Integer grupo  ){
		this.getJdbcTemplate().update("delete from SAM_GRUPO_TIPO_REQ where ID_GRUPO_CONFIG= ?     ", new Object[]{grupo});
	}	
	
	public List getGrupoTipoOp(Integer  grupo) {	   
		   return this.getJdbcTemplate().queryForList("SELECT     SAM_GRUPO_TIPO_REQ.ID_GRUPO_TIPO_REQ,  SAM_CAT_TIPO_REQ.DESCRIPCION , SAM_CAT_TIPO_REQ.ID_TIPOREQUISICION " + 
				    " FROM         SAM_CAT_TIPO_REQ LEFT OUTER JOIN   " +
				    " SAM_GRUPO_TIPO_REQ ON SAM_GRUPO_TIPO_REQ.ID_TIPO_REQ = SAM_CAT_TIPO_REQ.Id_TipoRequisicion AND "+  
				    " SAM_GRUPO_TIPO_REQ.ID_GRUPO_CONFIG =? WHERE SAM_CAT_TIPO_REQ.STATUS='True'  ", new Object[]{grupo});
	}
	
}
