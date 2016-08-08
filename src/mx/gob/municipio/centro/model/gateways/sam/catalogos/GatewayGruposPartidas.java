/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam.catalogos;


import java.util.List;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayGruposPartidas extends BaseGateway {

	public GatewayGruposPartidas(){
	}

	public void inserta( String partida,Integer grupo ){		
		this.getJdbcTemplate().update("insert into SAM_GRUPO_PARTIDAS (ID_GRUPO_CONFIG,CLV_PARTID ) " +
				"VALUES (?,?)", new Object[]{grupo,partida});
	}

	public void eliminar(Integer grupo , Integer capitulo ){
		this.getJdbcTemplate().update("delete from SAM_GRUPO_PARTIDAS where ID_GRUPO_CONFIG= ?  and CLV_PARTID in ( select CLV_PARTID from CAT_PARTID where CLV_CAPITU = ?   )  ", new Object[]{grupo,capitulo});
	}	
	
	public List getGrupoPartidas(Integer  grupo,Integer capitulo) {	   
		   return this.getJdbcTemplate().queryForList("SELECT     SAM_GRUPO_PARTIDAS.ID_PARTIDAS_GRUPO, CAT_PARTID.CLV_PARTID, CAT_PARTID.PARTIDA "+
				   	  " FROM         CAT_PARTID LEFT OUTER JOIN "+
                      " SAM_GRUPO_PARTIDAS ON SAM_GRUPO_PARTIDAS.CLV_PARTID = CAT_PARTID.CLV_PARTID AND "+ 
                      " SAM_GRUPO_PARTIDAS.ID_GRUPO_CONFIG = ? "+
                      " WHERE     (CAT_PARTID.CLV_CAPITU = ?)  ORDER BY CAT_PARTID.CLV_PARTID ASC", new Object[]{grupo,capitulo});
	}
	
}
