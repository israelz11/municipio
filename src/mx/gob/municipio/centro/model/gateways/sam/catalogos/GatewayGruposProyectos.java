/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam.catalogos;


import java.util.List;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayGruposProyectos extends BaseGateway {

	public GatewayGruposProyectos(){
		
	}


	public void inserta( String proyecto,Integer grupo ){
		this.getJdbcTemplate().update("insert into SAM_GRUPO_PROYECTOS (ID_GRUPO_CONFIG,ID_PROYECTO ) " +
				"VALUES (?,?)", new Object[]{grupo,proyecto});
	}

	public void eliminar(Integer grupo , Integer unidad ){
		this.getJdbcTemplate().update("delete from SAM_GRUPO_PROYECTOS where ID_GRUPO_CONFIG= ?  and ID_PROYECTO in ( select ID_PROYECTO from CEDULA_TEC where ID_DEPENDENCIA = ?   )  ", new Object[]{grupo,unidad});
	}	
	
	public List getGrupoProyectos(Integer  grupo,String unidad) {
		String sql = "SELECT CEDULA_TEC.ID_PROYECTO, CEDULA_TEC.N_PROGRAMA, ISNULL(VPROYECTO.K_PROYECTO_T, '') AS K_PROYECTO_T, CEDULA_TEC.DECRIPCION, CAT_DEPENDENCIAS.DEPENDENCIA, SAM_GRUPO_PROYECTOS.ID_PROYECTO_GRUPO, CEDULA_TEC.ID_DEPENDENCIA "+ 
							"FROM CEDULA_TEC  "+
							"LEFT OUTER JOIN SAM_GRUPO_PROYECTOS ON CEDULA_TEC.ID_PROYECTO = SAM_GRUPO_PROYECTOS.ID_PROYECTO "+ 
							"INNER JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = CEDULA_TEC.ID_DEPENDENCIA) "+
							"INNER JOIN VPROYECTO ON (VPROYECTO.ID_PROYECTO = CEDULA_TEC.ID_PROYECTO) "+
						"WHERE SAM_GRUPO_PROYECTOS.ID_GRUPO_CONFIG = ? AND CEDULA_TEC.ID_DEPENDENCIA = ? "+
					"UNION "+
					"SELECT CEDULA_TEC.ID_PROYECTO, CEDULA_TEC.N_PROGRAMA, ISNULL(VPROYECTO.K_PROYECTO_T,'') AS K_PROYECTO_T, CEDULA_TEC.DECRIPCION, CAT_DEPENDENCIAS.DEPENDENCIA, NULL ID_PROYECTO_GRUPO, CEDULA_TEC.ID_DEPENDENCIA "+ 
						"FROM CEDULA_TEC INNER JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = CEDULA_TEC.ID_DEPENDENCIA) "+
						"INNER JOIN VPROYECTO ON (VPROYECTO.ID_PROYECTO = CEDULA_TEC.ID_PROYECTO) "+
						"WHERE CEDULA_TEC.ID_PROYECTO NOT IN(SELECT CEDULA_TEC.ID_PROYECTO FROM CEDULA_TEC LEFT OUTER JOIN SAM_GRUPO_PROYECTOS ON CEDULA_TEC.ID_PROYECTO = SAM_GRUPO_PROYECTOS.ID_PROYECTO WHERE SAM_GRUPO_PROYECTOS.ID_GRUPO_CONFIG = ? AND CEDULA_TEC.ID_DEPENDENCIA = ?) AND CEDULA_TEC.ID_DEPENDENCIA = ? "+ 
					"ORDER BY CEDULA_TEC.N_PROGRAMA";
		return this.getJdbcTemplate().queryForList(sql, new Object[]{grupo, unidad, grupo, unidad, unidad});
		  
	}
	
}
