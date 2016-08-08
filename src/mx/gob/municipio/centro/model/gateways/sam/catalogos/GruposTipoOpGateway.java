/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam.catalogos;


import java.util.List;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GruposTipoOpGateway extends BaseGateway {

	public GruposTipoOpGateway(){
	}

	public void inserta( Integer tipoOp,Integer grupo ){		
		this.getJdbcTemplate().update("insert into SAM_GRUPO_TIPO_OP (ID_GRUPO_CONFIG,ID_TIPO_ORDEN_PAGO ) " +
				"VALUES (?,?)", new Object[]{grupo,tipoOp});
	}

	public void eliminar(Integer grupo  ){
		this.getJdbcTemplate().update("delete from SAM_GRUPO_TIPO_OP where ID_GRUPO_CONFIG= ?     ", new Object[]{grupo});
	}	
	
	public List getGrupoTipoOp(Integer  grupo) {	   
		   return this.getJdbcTemplate().queryForList("SELECT     SAM_GRUPO_TIPO_OP.ID_TIPO_OP_GRUPO,  SAM_TIPO_ORDEN_PAGO.DESCRIPCION , SAM_TIPO_ORDEN_PAGO.ID_TIPO_ORDEN_PAGO "+
				   " FROM         SAM_TIPO_ORDEN_PAGO LEFT OUTER JOIN "+ 
				   " SAM_GRUPO_TIPO_OP ON SAM_GRUPO_TIPO_OP.ID_TIPO_ORDEN_PAGO = SAM_TIPO_ORDEN_PAGO.ID_TIPO_ORDEN_PAGO AND "+ 
				   " SAM_GRUPO_TIPO_OP.ID_GRUPO_CONFIG =? where SAM_TIPO_ORDEN_PAGO.estatus='ACTIVO'  ", new Object[]{grupo});
	}
	
}
