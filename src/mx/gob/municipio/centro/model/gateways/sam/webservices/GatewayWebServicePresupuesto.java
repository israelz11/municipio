package mx.gob.municipio.centro.model.gateways.sam.webservices;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayWebServicePresupuesto extends BaseGateway {

	public GatewayWebServicePresupuesto(){
		
	}
	
	public List<Map> getListaDependencias(int cve_pers){
		return this.getJdbcTemplate().queryForList("SELECT DISTINCT A.ID_DEPENDENCIA AS ID, B.DEPENDENCIA FROM SAM_TEMP_USR_PROY_PART AS A INNER JOIN CAT_DEPENDENCIAS AS B  ON (B.ID = A.ID_DEPENDENCIA) WHERE A.ID_USUARIO = ? ORDER BY B.DEPENDENCIA ASC", new Object[]{cve_pers});
	}
	
	public int getDependenciaUser(int cve_pers){
		return this.getJdbcTemplate().queryForInt("SELECT DISTINCT ID_DEPENDENCIA FROM TRABAJADOR WHERE CVE_PERS = ?", new Object[]{cve_pers});
	}
	
	public List<Map> getIDDependencias(int cve_pers){
		return this.getJdbcTemplate().queryForList("SELECT DISTINCT ID_DEPENDENCIA FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = ?", new Object[]{cve_pers});
	}
	
	public List<Map> getProgramas(int cve_pers, int idRecurso, int idDependencia){
		return this.getJdbcTemplate().queryForList("SELECT DISTINCT A.ID_PROYECTO AS ID, ('['+CONVERT(NVARCHAR, A.ID_PROYECTO)+'] '+A.PROYECTO) AS PROGRAMA, PROG_PRESUP AS DESCRIPCION  FROM SAM_TEMP_USR_PROY_PART AS A "+
													"	INNER JOIN VPROYECTO AS B ON (B.ID_PROYECTO = A.ID_PROYECTO) WHERE A.ID_USUARIO = ? "+(idRecurso!=0 ? "AND B.ID_RECURSO = "+idRecurso:"")+" AND A.ID_DEPENDENCIA = ? /*ORDER BY PROYECTO ASC*/", new Object[]{cve_pers, idDependencia});
	}
	
	public List<Map> getListaPartidas(int cve_pers, int idProyecto){
		return this.getJdbcTemplate().queryForList("SELECT DISTINCT A.CLV_PARTID AS ID, A.CLV_PARTID AS PARTIDA, C.PARTIDA AS DESCRIPCION FROM SAM_TEMP_USR_PROY_PART AS A "+
													"	INNER JOIN VPARTIDAS AS B ON (B.ID_PROYECTO = A.ID_PROYECTO AND A.CLV_PARTID = B.CLV_PARTID) "+
													"	INNER JOIN CAT_PARTID AS C ON (C.CLV_PARTID = B.CLV_PARTID) "+
													"WHERE A.ID_USUARIO = ? AND A.ID_PROYECTO = ? "+
													"ORDER BY A.CLV_PARTID ASC", new Object[]{cve_pers, idProyecto});
	}
	
	
	public List<Map> getDocumentosPresupuesto (int periodo, String consulta, int idProyecto, String clv_partid)
	{
		return this.getJdbcTemplate().queryForList("SELECT TIPO_DOC AS TIPO, NUM_DOC, CONVERT(VARCHAR,FECHA,103) AS FECHA, MONTO FROM VT_COMPROMISOS WHERE ID_PROYECTO = ? AND CLV_PARTID = ? AND PERIODO = ? AND CONSULTA = ? ORDER BY TIPO_DOC ASC", new Object[]{idProyecto, clv_partid, periodo, consulta});
	}
	
	public List<Map> getListaTipoGasto(){
		return this.getJdbcTemplate().queryForList("SELECT ID, RECURSO FROM CAT_RECURSO WHERE EJERCICIO=2012 ORDER BY RECURSO ASC");
	}

	
	public Map getPresupuestoGeneral(int idProyecto, String clv_partid, int periodo){
		Map m = this.getJdbcTemplate().queryForMap("SELECT C.ID_PROYECTO, C.CLV_PARTID, ('['+CONVERT(VARCHAR, C.ID_PROYECTO)+'] ' + B.PROG_PRESUP) AS PROGRAMA, ('['+C.CLV_PARTID+'] '+A.PARTIDA) AS PARTIDA, " +
													"		dbo.getAutorizado(?,?, C.ID_PROYECTO, C.CLV_PARTID) AS AUTORIZADO,  " +
													"		dbo.getPrecomprometido(?, C.ID_PROYECTO, C.CLV_PARTID) as PRECOMPROMETIDO, " +
													"		dbo.getComprometido(?, C.ID_PROYECTO, C.CLV_PARTID) as COMPROMETIDO, " +
													"		dbo.getEjercido(?, C.ID_PROYECTO, C.CLV_PARTID) as EJERCIDO " +
													"FROM VPARTIDAS AS C " +
													"	INNER JOIN VPROYECTO AS B ON (B.ID_PROYECTO = C.ID_PROYECTO)  " +
													"	INNER JOIN CAT_PARTID AS A ON (A.CLV_PARTID = C.CLV_PARTID) " +
													"WHERE C.ID_PROYECTO = ? AND C.CLV_PARTID = ?", new Object[] {periodo, periodo, periodo, periodo, periodo, idProyecto, clv_partid});
		BigDecimal disponible = (((BigDecimal) m.get("AUTORIZADO")).subtract( ((BigDecimal) m.get("PRECOMPROMETIDO")).add( ((BigDecimal) m.get("COMPROMETIDO")) ).add( ((BigDecimal) m.get("EJERCIDO")) ) ));
		m.put("DISPONIBLE", disponible);
		return m;
	}
}
