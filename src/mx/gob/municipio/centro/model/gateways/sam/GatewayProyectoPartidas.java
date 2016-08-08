/**
 * @author Lsc. Mauricio Hernandez Leon � Israel de la Cruz.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.dao.DataAccessException;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.view.seguridad.Sesion;

public class GatewayProyectoPartidas extends BaseGateway {

	private String[] Meses = {"Ene","Feb","Mar","Abr","May","Jun","Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
    
	private static Logger log = Logger.getLogger(GatewayProyectoPartidas.class.getName());
	
	public GatewayProyectoPartidas(){
		
	}
	
	public String getPartidaCompleta(String clv_partid){
		return (String)this.getJdbcTemplate().queryForObject("SELECT PARTIDA FROM CAT_PARTID WHERE CLV_PARTID = ?",new Object[]{clv_partid}, String.class);
	}
	
	public String getProyectoCompleto(String proyecto){
		return (String)this.getJdbcTemplate().queryForObject("SELECT PROG_PRESUP FROM VPROYECTO WHERE ID_PROYECTO = ?",new Object[]{proyecto}, String.class);
	}
	
	public List<Map> getPartidasPorProyectos(String proyecto){
		return this.getJdbcTemplate().queryForList("select b.CLV_PARTID, b.PARTIDA  from SPARTIDAS a, CAT_PARTID b where  a.clv_partid= b.clv_partid and a.PROYECTO=? ", new Object []{proyecto});
	}
	
	public List<Map> getPartidas(){
		return this.getJdbcTemplate().queryForList("select CLV_PARTID,PARTIDA  from  CAT_PARTID  order by CLV_PARTID ");
	}
	
	public List<Map> getPresupuestoValeDetalle(Long idproyecto, String clv_partid, int periodo, Long cve_doc){
		return this.getJdbcTemplate().queryForList("SELECT *FROM VT_COMPROMISOS WHERE TIPO_DOC ='VAL' AND ID_PROYECTO = ? AND CLV_PARTID = ? AND CONSULTA = 'COMPROMETIDO' AND PERIODO = ? AND CVE_DOC = ?", new Object[]{idproyecto, clv_partid, periodo, cve_doc});
	}
	
	public List<Map> getPresupuestoContrato(Long idproyecto, String proyecto, String partida, Integer mes, Integer idUsuario, int unidad, int tipoGasto, Long cve_contrato){		
		String SQL = "";

		Map<String,Object> parametros = new HashMap<String,Object>();
		parametros.put("usuario", idUsuario);
		parametros.put("idproyecto",idproyecto);
		parametros.put("proyecto",proyecto+"%");
		parametros.put("partida", partida);		
		parametros.put("unidad", unidad);
		parametros.put("mes", mes);
		parametros.put("tipoGasto", tipoGasto);
		parametros.put("cve_contrato", cve_contrato);
		
		SQL += "SELECT DISTINCT "+
					"c.ID_PROYECTO, "+
					"c.N_PROGRAMA,  "+
					"p.CLV_PARTID,  "+
					"VP.RECURSO,"+
					"(VP.CLV_ACTINST+' '+VP.ACT_INST) AS ACTIVIDAD_INST,"+
					"(VP.CLV_LOCALIDAD+' '+VP.LOCALIDAD) AS LOCALIDAD, "+
					"c.DECRIPCION,  "+
					"cp.PARTIDA, "+
				    "ISNULL(c.PROY_BOLSA, '') AS BOLSA,  "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL(( p.ENEPREINI + p.ENEPREAMP - p.ENEPRERED), 0) "+
					"	WHEN 2 THEN ISNULL(( p.FEBPREINI + p.FEBPREAMP - p.FEBPRERED), 0) "+
					"	WHEN 3 THEN ISNULL(( p.MARPREINI + p.MARPREAMP - p.MARPRERED), 0) "+
					"	WHEN 4 THEN ISNULL(( p.ABRPREINI + p.ABRPREAMP - p.ABRPRERED), 0) "+
					"	WHEN 5 THEN ISNULL(( p.MAYPREINI + p.MAYPREAMP - p.MAYPRERED), 0) "+
					"	WHEN 6 THEN ISNULL(( p.JUNPREINI + p.JUNPREAMP - p.JUNPRERED), 0) "+
					"	WHEN 7 THEN ISNULL(( p.JULPREINI + p.JULPREAMP - p.JULPRERED), 0) "+
					"	WHEN 8 THEN ISNULL(( p.AGOPREINI + p.AGOPREAMP - p.AGOPRERED), 0) "+
					"	WHEN 9 THEN ISNULL(( p.SEPPREINI + p.SEPPREAMP - p.SEPPRERED), 0) "+
					"	WHEN 10 THEN ISNULL(( p.OCTPREINI + p.OCTPREAMP - p.OCTPRERED), 0) "+
					"	WHEN 11 THEN ISNULL(( p.NOVPREINI + p.NOVPREAMP - p.NOVPRERED), 0) "+
					"	WHEN 12 THEN ISNULL(( p.DICPREINI + p.DICPREAMP - p.DICPRERED), 0) "+
					"	ELSE 0 END) AS PREACTUAL,  "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL(( p.ENEPRECOM), 0) "+
					"	WHEN 2 THEN ISNULL(( p.FEBPRECOM), 0) "+
					"	WHEN 3 THEN ISNULL(( p.MARPRECOM), 0) "+
					"	WHEN 4 THEN ISNULL(( p.ABRPRECOM), 0) "+
					"	WHEN 5 THEN ISNULL(( p.MAYPRECOM), 0) "+
					"	WHEN 6 THEN ISNULL(( p.JUNPRECOM), 0) "+
					"	WHEN 7 THEN ISNULL(( p.JULPRECOM), 0) "+
					"	WHEN 8 THEN ISNULL(( p.AGOPRECOM), 0) "+
					"	WHEN 9 THEN ISNULL(( p.SEPPRECOM), 0) "+
					"	WHEN 10 THEN ISNULL(( p.OCTPRECOM), 0) "+
					"	WHEN 11 THEN ISNULL(( p.NOVPRECOM), 0) "+
					"	WHEN 12 THEN ISNULL(( p.DICPRECOM), 0) "+
					"	ELSE 0 END) AS PRECOM, "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL( p.ENEPREEJE, 0) "+
					"	WHEN 2 THEN ISNULL( p.FEBPREEJE, 0)  "+
					"	WHEN 3 THEN ISNULL( p.MARPREEJE, 0) "+
					"	WHEN 4 THEN ISNULL( p.ABRPREEJE, 0) "+
					"	WHEN 5 THEN ISNULL( p.MAYPREEJE, 0) "+
					"	WHEN 6 THEN ISNULL( p.JUNPREEJE, 0) "+
					"	WHEN 7 THEN ISNULL( p.JULPREEJE, 0) "+
					"	WHEN 8 THEN ISNULL( p.AGOPREEJE, 0) "+
					"	WHEN 9 THEN ISNULL( p.SEPPREEJE, 0) "+
					"	WHEN 10 THEN ISNULL( p.OCTPREEJE, 0) "+
					"	WHEN 11 THEN ISNULL( p.NOVPREEJE, 0) "+
					"	WHEN 12 THEN ISNULL( p.DICPREEJE, 0) "+
					"	ELSE 0 END) AS PREEJER, "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL( ENEPREREQ, 0) "+
					"	WHEN 2 THEN ISNULL( FEBPREREQ, 0)  "+
					"	WHEN 3 THEN ISNULL( MARPREREQ, 0) "+
					"	WHEN 4 THEN ISNULL( ABRPREREQ, 0) "+
					"	WHEN 5 THEN ISNULL( MAYPREREQ, 0) "+
					"	WHEN 6 THEN ISNULL( JUNPREREQ, 0) "+
					"	WHEN 7 THEN ISNULL( JULPREREQ, 0) "+
					"	WHEN 8 THEN ISNULL( AGOPREREQ, 0) "+
					"	WHEN 9 THEN ISNULL( SEPPREREQ, 0) "+
					"	WHEN 10 THEN ISNULL( OCTPREREQ, 0) "+
					"	WHEN 11 THEN ISNULL( NOVPREREQ, 0) "+
					"	WHEN 12 THEN ISNULL( DICPREREQ, 0) "+
					"	ELSE 0 END) AS PREREQ  "+
				"FROM  dbo.SPARTIDAS AS p LEFT JOIN "+
				"      dbo.PART_EXT2 AS pe ON p.ID_PROYECTO = pe.ID_PROYECTO AND p.CLV_PARTID = pe.CLV_PARTID INNER JOIN "+
				"      dbo.CEDULA_TEC AS c ON c.ID_PROYECTO = p.ID_PROYECTO INNER JOIN "+
				"      dbo.CAT_PARTID AS cp ON cp.CLV_PARTID = p.CLV_PARTID "+
				"	   INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = c.ID_PROYECTO) "+
				"WHERE "+
				"CAST(c.ID_PROYECTO AS NVARCHAR)+CAST(p.CLV_PARTID AS NVARCHAR) IN (SELECT CAST(ID_PROYECTO AS NVARCHAR)+CAST(CLV_PARTID AS VARCHAR) FROM SAM_COMP_CONTRATO WHERE PERIODO = :mes AND  CVE_CONTRATO =:cve_contrato)";
				//"	CAST(c.ID_PROYECTO AS NVARCHAR)+CAST(p.CLV_PARTID AS NVARCHAR) IN (SELECT CAST(ID_PROYECTO AS NVARCHAR)+CAST(CLV_PARTID AS VARCHAR) FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = :usuario) ";
				 
		if(idproyecto!=null)
			if(idproyecto!=0)
				SQL +=  "AND c.ID_PROYECTO =:idproyecto ";
		
		if (proyecto!=null && !proyecto.equals("0")&& !proyecto.equals(""))
			if(idproyecto==0)
				SQL +=  "AND UPPER(c.N_PROGRAMA) LIKE :proyecto ";
		if (partida!=null && !partida.equals("0")&& !partida.equals(""))
			SQL +="AND p.CLV_PARTID = :partida  ";
		if (tipoGasto!=0)
				SQL +="AND  c.ID_RECURSO = :tipoGasto ";
		/*if(unidad!=0){
			//comprobar que tiene acceso al presupuesto de esa unidad
			boolean acceso = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = ? AND ID_DEPENDENCIA =?", new Object[]{idUsuario, unidad})!=0;
			if(acceso) 
				SQL +="AND  c.ID_DEPENDENCIA = :unidad ";
			else
				SQL +="AND  c.ID_DEPENDENCIA = 0 ";
			
		}*/
			
		
		SQL +="  ORDER BY c.N_PROGRAMA, p.CLV_PARTID ASC";
		
		List<Map> resultado =  this.getNamedJdbcTemplate().queryForList(SQL,parametros);
		BigDecimal disponible = new BigDecimal("0.00");
		for (Map result :resultado ) {
			disponible = ((BigDecimal)result.get("PREACTUAL")).subtract((BigDecimal)result.get("PREREQ")).subtract((BigDecimal)result.get("PREEJER")).subtract((BigDecimal)result.get("PRECOM"));
			result.put("DISPONIBLE", disponible);	
			disponible = new BigDecimal("0.00");
		}
		return resultado;
	}

	
	public List<Map> getPresupuestoVale(Long idproyecto, String proyecto, String partida, Integer mes, Integer idUsuario, int unidad, int tipoGasto, Long idVale){		
		String SQL = "";

		Map<String,Object> parametros = new HashMap<String,Object>();
		parametros.put("usuario", idUsuario);
		parametros.put("idproyecto",idproyecto);
		parametros.put("proyecto",proyecto+"%");
		parametros.put("partida", partida);		
		parametros.put("unidad", unidad);
		parametros.put("mes", mes);
		parametros.put("tipoGasto", tipoGasto);
		parametros.put("idVale", idVale);
		
		SQL += "SELECT DISTINCT "+
					"c.ID_PROYECTO, "+
					"c.N_PROGRAMA,  "+
					"p.CLV_PARTID,  "+
					"VP.RECURSO,"+
					"(VP.CLV_ACTINST+' '+VP.ACT_INST) AS ACTIVIDAD_INST,"+
					"(VP.CLV_LOCALIDAD+' '+VP.LOCALIDAD) AS LOCALIDAD, "+
					"c.DECRIPCION,  "+
					"cp.PARTIDA, "+
				    "ISNULL(c.PROY_BOLSA, '') AS BOLSA,  "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL(( p.ENEPREINI + p.ENEPREAMP - p.ENEPRERED), 0) "+
					"	WHEN 2 THEN ISNULL(( p.FEBPREINI + p.FEBPREAMP - p.FEBPRERED), 0) "+
					"	WHEN 3 THEN ISNULL(( p.MARPREINI + p.MARPREAMP - p.MARPRERED), 0) "+
					"	WHEN 4 THEN ISNULL(( p.ABRPREINI + p.ABRPREAMP - p.ABRPRERED), 0) "+
					"	WHEN 5 THEN ISNULL(( p.MAYPREINI + p.MAYPREAMP - p.MAYPRERED), 0) "+
					"	WHEN 6 THEN ISNULL(( p.JUNPREINI + p.JUNPREAMP - p.JUNPRERED), 0) "+
					"	WHEN 7 THEN ISNULL(( p.JULPREINI + p.JULPREAMP - p.JULPRERED), 0) "+
					"	WHEN 8 THEN ISNULL(( p.AGOPREINI + p.AGOPREAMP - p.AGOPRERED), 0) "+
					"	WHEN 9 THEN ISNULL(( p.SEPPREINI + p.SEPPREAMP - p.SEPPRERED), 0) "+
					"	WHEN 10 THEN ISNULL(( p.OCTPREINI + p.OCTPREAMP - p.OCTPRERED), 0) "+
					"	WHEN 11 THEN ISNULL(( p.NOVPREINI + p.NOVPREAMP - p.NOVPRERED), 0) "+
					"	WHEN 12 THEN ISNULL(( p.DICPREINI + p.DICPREAMP - p.DICPRERED), 0) "+
					"	ELSE 0 END) AS PREACTUAL,  "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL(( p.ENEPRECOM), 0) "+
					"	WHEN 2 THEN ISNULL(( p.FEBPRECOM), 0) "+
					"	WHEN 3 THEN ISNULL(( p.MARPRECOM), 0) "+
					"	WHEN 4 THEN ISNULL(( p.ABRPRECOM), 0) "+
					"	WHEN 5 THEN ISNULL(( p.MAYPRECOM), 0) "+
					"	WHEN 6 THEN ISNULL(( p.JUNPRECOM), 0) "+
					"	WHEN 7 THEN ISNULL(( p.JULPRECOM), 0) "+
					"	WHEN 8 THEN ISNULL(( p.AGOPRECOM), 0) "+
					"	WHEN 9 THEN ISNULL(( p.SEPPRECOM), 0) "+
					"	WHEN 10 THEN ISNULL(( p.OCTPRECOM), 0) "+
					"	WHEN 11 THEN ISNULL(( p.NOVPRECOM), 0) "+
					"	WHEN 12 THEN ISNULL(( p.DICPRECOM), 0) "+
					"	ELSE 0 END) AS PRECOM, "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL( p.ENEPREEJE, 0) "+
					"	WHEN 2 THEN ISNULL( p.FEBPREEJE, 0)  "+
					"	WHEN 3 THEN ISNULL( p.MARPREEJE, 0) "+
					"	WHEN 4 THEN ISNULL( p.ABRPREEJE, 0) "+
					"	WHEN 5 THEN ISNULL( p.MAYPREEJE, 0) "+
					"	WHEN 6 THEN ISNULL( p.JUNPREEJE, 0) "+
					"	WHEN 7 THEN ISNULL( p.JULPREEJE, 0) "+
					"	WHEN 8 THEN ISNULL( p.AGOPREEJE, 0) "+
					"	WHEN 9 THEN ISNULL( p.SEPPREEJE, 0) "+
					"	WHEN 10 THEN ISNULL( p.OCTPREEJE, 0) "+
					"	WHEN 11 THEN ISNULL( p.NOVPREEJE, 0) "+
					"	WHEN 12 THEN ISNULL( p.DICPREEJE, 0) "+
					"	ELSE 0 END) AS PREEJER, "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL( ENEPREREQ, 0) "+
					"	WHEN 2 THEN ISNULL( FEBPREREQ, 0)  "+
					"	WHEN 3 THEN ISNULL( MARPREREQ, 0) "+
					"	WHEN 4 THEN ISNULL( ABRPREREQ, 0) "+
					"	WHEN 5 THEN ISNULL( MAYPREREQ, 0) "+
					"	WHEN 6 THEN ISNULL( JUNPREREQ, 0) "+
					"	WHEN 7 THEN ISNULL( JULPREREQ, 0) "+
					"	WHEN 8 THEN ISNULL( AGOPREREQ, 0) "+
					"	WHEN 9 THEN ISNULL( SEPPREREQ, 0) "+
					"	WHEN 10 THEN ISNULL( OCTPREREQ, 0) "+
					"	WHEN 11 THEN ISNULL( NOVPREREQ, 0) "+
					"	WHEN 12 THEN ISNULL( DICPREREQ, 0) "+
					"	ELSE 0 END) AS PREREQ  "+
				"FROM  dbo.SPARTIDAS AS p LEFT JOIN "+
				"      dbo.PART_EXT2 AS pe ON p.ID_PROYECTO = pe.ID_PROYECTO AND p.CLV_PARTID = pe.CLV_PARTID INNER JOIN "+
				"      dbo.CEDULA_TEC AS c ON c.ID_PROYECTO = p.ID_PROYECTO INNER JOIN "+
				"      dbo.CAT_PARTID AS cp ON cp.CLV_PARTID = p.CLV_PARTID "+
				"	   INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = c.ID_PROYECTO) "+
				"WHERE "+
				"CAST(c.ID_PROYECTO AS NVARCHAR)+CAST(p.CLV_PARTID AS NVARCHAR) IN (SELECT CAST(ID_PROYECTO AS NVARCHAR)+CAST(CLV_PARTID AS VARCHAR) FROM SAM_MOV_VALES WHERE CVE_VALE =:idVale)";
				//"	CAST(c.ID_PROYECTO AS NVARCHAR)+CAST(p.CLV_PARTID AS NVARCHAR) IN (SELECT CAST(ID_PROYECTO AS NVARCHAR)+CAST(CLV_PARTID AS VARCHAR) FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = :usuario) ";
				 
		if(idproyecto!=null)
			if(idproyecto!=0)
				SQL +=  "AND c.ID_PROYECTO =:idproyecto ";
		
		if (proyecto!=null && !proyecto.equals("0")&& !proyecto.equals(""))
			if(idproyecto==0)
				SQL +=  "AND UPPER(c.N_PROGRAMA) LIKE :proyecto ";
		if (partida!=null && !partida.equals("0")&& !partida.equals(""))
			SQL +="AND p.CLV_PARTID = :partida  ";
		if (tipoGasto!=0)
				SQL +="AND  c.ID_RECURSO = :tipoGasto ";
		/*if(unidad!=0){
			//comprobar que tiene acceso al presupuesto de esa unidad
			boolean acceso = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = ? AND ID_DEPENDENCIA =?", new Object[]{idUsuario, unidad})!=0;
			if(acceso) 
				SQL +="AND  c.ID_DEPENDENCIA = :unidad ";
			else
				SQL +="AND  c.ID_DEPENDENCIA = 0 ";
			
		}*/
			
		
		SQL +="  ORDER BY c.N_PROGRAMA, p.CLV_PARTID ASC";
		
		List<Map> resultado =  this.getNamedJdbcTemplate().queryForList(SQL,parametros);
		BigDecimal disponible = new BigDecimal("0.00");
		for (Map result :resultado ) {
			disponible = ((BigDecimal)result.get("PREACTUAL")).subtract((BigDecimal)result.get("PREREQ")).subtract((BigDecimal)result.get("PREEJER")).subtract((BigDecimal)result.get("PRECOM"));
			result.put("DISPONIBLE", disponible);	
			disponible = new BigDecimal("0.00");
		}
		return resultado;
	}
	
	public List<Map> getPresupuesto(Long idproyecto, String proyecto, String partida, Integer mes, Integer idUsuario, int unidad, int tipoGasto){		
		String SQL = "";
		String sActual        = "";
		String sEjercido      = "";
		String sComprometido  = "";
		String sRequerido     = "";
		Map<String,Object> parametros = new HashMap<String,Object>();
		parametros.put("usuario", idUsuario);
		parametros.put("idproyecto",idproyecto);
		parametros.put("proyecto",proyecto+"%");
		parametros.put("partida", partida);		
		parametros.put("unidad", unidad);
		parametros.put("mes", mes);
		parametros.put("tipoGasto", tipoGasto);		
		
		 
		String[] prefijoMes   = {"ene","feb","mar","abr","may","jun","jul","ago","sep","oct","nov","dic"};
		SQL += "SELECT DISTINCT "+
					"c.ID_PROYECTO, "+
					"c.N_PROGRAMA,  "+
					"p.CLV_PARTID,  "+
					"VP.RECURSO,"+
					"(VP.CLV_ACTINST+' '+VP.ACT_INST) AS ACTIVIDAD_INST,"+
					"(VP.CLV_LOCALIDAD+' '+VP.LOCALIDAD) AS LOCALIDAD, "+
					"c.DECRIPCION,  "+
					"cp.PARTIDA, "+
				    "ISNULL(c.PROY_BOLSA, '') AS BOLSA,  "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL(( p.ENEPREINI + p.ENEPREAMP - p.ENEPRERED), 0) "+
					"	WHEN 2 THEN ISNULL(( p.FEBPREINI + p.FEBPREAMP - p.FEBPRERED), 0) "+
					"	WHEN 3 THEN ISNULL(( p.MARPREINI + p.MARPREAMP - p.MARPRERED), 0) "+
					"	WHEN 4 THEN ISNULL(( p.ABRPREINI + p.ABRPREAMP - p.ABRPRERED), 0) "+
					"	WHEN 5 THEN ISNULL(( p.MAYPREINI + p.MAYPREAMP - p.MAYPRERED), 0) "+
					"	WHEN 6 THEN ISNULL(( p.JUNPREINI + p.JUNPREAMP - p.JUNPRERED), 0) "+
					"	WHEN 7 THEN ISNULL(( p.JULPREINI + p.JULPREAMP - p.JULPRERED), 0) "+
					"	WHEN 8 THEN ISNULL(( p.AGOPREINI + p.AGOPREAMP - p.AGOPRERED), 0) "+
					"	WHEN 9 THEN ISNULL(( p.SEPPREINI + p.SEPPREAMP - p.SEPPRERED), 0) "+
					"	WHEN 10 THEN ISNULL(( p.OCTPREINI + p.OCTPREAMP - p.OCTPRERED), 0) "+
					"	WHEN 11 THEN ISNULL(( p.NOVPREINI + p.NOVPREAMP - p.NOVPRERED), 0) "+
					"	WHEN 12 THEN ISNULL(( p.DICPREINI + p.DICPREAMP - p.DICPRERED), 0) "+
					"	ELSE 0 END) AS PREACTUAL,  "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL(( p.ENEPRECOM), 0) "+
					"	WHEN 2 THEN ISNULL(( p.FEBPRECOM), 0) "+
					"	WHEN 3 THEN ISNULL(( p.MARPRECOM), 0) "+
					"	WHEN 4 THEN ISNULL(( p.ABRPRECOM), 0) "+
					"	WHEN 5 THEN ISNULL(( p.MAYPRECOM), 0) "+
					"	WHEN 6 THEN ISNULL(( p.JUNPRECOM), 0) "+
					"	WHEN 7 THEN ISNULL(( p.JULPRECOM), 0) "+
					"	WHEN 8 THEN ISNULL(( p.AGOPRECOM), 0) "+
					"	WHEN 9 THEN ISNULL(( p.SEPPRECOM), 0) "+
					"	WHEN 10 THEN ISNULL(( p.OCTPRECOM), 0) "+
					"	WHEN 11 THEN ISNULL(( p.NOVPRECOM), 0) "+
					"	WHEN 12 THEN ISNULL(( p.DICPRECOM), 0) "+
					"	ELSE 0 END) AS PRECOM, "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL(( p.ENEPREDEV), 0) "+
					"	WHEN 2 THEN ISNULL(( p.FEBPREDEV), 0) "+
					"	WHEN 3 THEN ISNULL(( p.MARPREDEV), 0) "+
					"	WHEN 4 THEN ISNULL(( p.ABRPREDEV), 0) "+
					"	WHEN 5 THEN ISNULL(( p.MAYPREDEV), 0) "+
					"	WHEN 6 THEN ISNULL(( p.JUNPREDEV), 0) "+
					"	WHEN 7 THEN ISNULL(( p.JULPREDEV), 0) "+
					"	WHEN 8 THEN ISNULL(( p.AGOPREDEV), 0) "+
					"	WHEN 9 THEN ISNULL(( p.SEPPREDEV), 0) "+
					"	WHEN 10 THEN ISNULL(( p.OCTPREDEV), 0) "+
					"	WHEN 11 THEN ISNULL(( p.NOVPREDEV), 0) "+
					"	WHEN 12 THEN ISNULL(( p.DICPREDEV), 0) "+
					"	ELSE 0 END) AS PREDEV, "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL( p.ENEPREEJE, 0) "+
					"	WHEN 2 THEN ISNULL( p.FEBPREEJE, 0)  "+
					"	WHEN 3 THEN ISNULL( p.MARPREEJE, 0) "+
					"	WHEN 4 THEN ISNULL( p.ABRPREEJE, 0) "+
					"	WHEN 5 THEN ISNULL( p.MAYPREEJE, 0) "+
					"	WHEN 6 THEN ISNULL( p.JUNPREEJE, 0) "+
					"	WHEN 7 THEN ISNULL( p.JULPREEJE, 0) "+
					"	WHEN 8 THEN ISNULL( p.AGOPREEJE, 0) "+
					"	WHEN 9 THEN ISNULL( p.SEPPREEJE, 0) "+
					"	WHEN 10 THEN ISNULL( p.OCTPREEJE, 0) "+
					"	WHEN 11 THEN ISNULL( p.NOVPREEJE, 0) "+
					"	WHEN 12 THEN ISNULL( p.DICPREEJE, 0) "+
					"	ELSE 0 END) AS PREEJER, "+
					"(CASE (:mes) "+
					"	WHEN 1 THEN ISNULL( ENEPREREQ, 0) "+
					"	WHEN 2 THEN ISNULL( FEBPREREQ, 0)  "+
					"	WHEN 3 THEN ISNULL( MARPREREQ, 0) "+
					"	WHEN 4 THEN ISNULL( ABRPREREQ, 0) "+
					"	WHEN 5 THEN ISNULL( MAYPREREQ, 0) "+
					"	WHEN 6 THEN ISNULL( JUNPREREQ, 0) "+
					"	WHEN 7 THEN ISNULL( JULPREREQ, 0) "+
					"	WHEN 8 THEN ISNULL( AGOPREREQ, 0) "+
					"	WHEN 9 THEN ISNULL( SEPPREREQ, 0) "+
					"	WHEN 10 THEN ISNULL( OCTPREREQ, 0) "+
					"	WHEN 11 THEN ISNULL( NOVPREREQ, 0) "+
					"	WHEN 12 THEN ISNULL( DICPREREQ, 0) "+
					"	ELSE 0 END) AS PREREQ  "+
				"FROM  dbo.SPARTIDAS AS p LEFT JOIN "+
				"      dbo.PART_EXT2 AS pe ON p.ID_PROYECTO = pe.ID_PROYECTO AND p.CLV_PARTID = pe.CLV_PARTID INNER JOIN "+
				"      dbo.CEDULA_TEC AS c ON c.ID_PROYECTO = p.ID_PROYECTO INNER JOIN "+
				"      dbo.CAT_PARTID AS cp ON cp.CLV_PARTID = p.CLV_PARTID "+
				"	   INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = c.ID_PROYECTO) "+
				/*"    INNER JOIN   dbo.SAM_TEMP_USR_PROY_PART u ON u.PROYECTO = c.PROYECTO AND  "+
				"      u.CLV_PARTID = p.CLV_PARTID "+*/
				"WHERE  "+
				"	CAST(c.ID_PROYECTO AS NVARCHAR)+CAST(p.CLV_PARTID AS NVARCHAR) IN (SELECT CAST(ID_PROYECTO AS NVARCHAR)+CAST(CLV_PARTID AS VARCHAR) FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = :usuario) ";
					/*" u.ID_USUARIO =:usuario ";*/ 
		if(idproyecto!=null)
			if(idproyecto!=0)
				SQL +=  "AND c.ID_PROYECTO =:idproyecto ";
		
		if (proyecto!=null && !proyecto.equals("0")&& !proyecto.equals(""))
			if(idproyecto==0)
				SQL +=  "AND UPPER(c.N_PROGRAMA) LIKE :proyecto ";
		if (partida!=null && !partida.equals("0")&& !partida.equals(""))
			SQL +="AND p.CLV_PARTID = :partida  ";
		if (tipoGasto!=0)
				SQL +="AND  c.ID_RECURSO = :tipoGasto ";
		if(unidad!=0){
			//comprobar que tiene acceso al presupuesto de esa unidad
			boolean acceso = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = ? AND ID_DEPENDENCIA =?", new Object[]{idUsuario, unidad})!=0;
			if(acceso) 
				SQL +="AND  c.ID_DEPENDENCIA = :unidad ";
			else
				SQL +="AND  c.ID_DEPENDENCIA = 0 ";
			
		}
			
		
		SQL +="  ORDER BY c.N_PROGRAMA, p.CLV_PARTID ASC";
		
		List<Map> resultado =  this.getNamedJdbcTemplate().queryForList(SQL,parametros);
		BigDecimal disponible = new BigDecimal("0.00");
		for (Map result :resultado ) {
			disponible = ((BigDecimal)result.get("PREACTUAL")).subtract((BigDecimal)result.get("PREREQ")).subtract((BigDecimal)result.get("PREEJER")).subtract((BigDecimal)result.get("PRECOM")).subtract((BigDecimal)result.get("PREDEV"));
			result.put("DISPONIBLE", disponible);	
			disponible = new BigDecimal("0.00");
		}
		return resultado;
	}
		
	
	public boolean verificarPresupuesto(Long idproyecto, String proyecto, String partida, int mes, Integer idUsuario, double importe ){
		boolean bol = false;
		List <Map> resultado = getPresupuesto(idproyecto, proyecto, partida, mes, idUsuario, 0,0);
		for (Map result :resultado ) {
			if(((BigDecimal)result.get("DISPONIBLE")).doubleValue()  >=importe) 
				bol=true; 
			else 
				bol=false; 
		}
		return bol;
	}
	
	public double getComprometidoMes(Long proyecto, String partida, int mes) {
		Double comprometido = (Double)this.getJdbcTemplate().queryForObject("SELECT "+Meses[mes-1]+"PRECOM FROM SPARTIDAS WHERE  ID_PROYECTO=? and CLV_PARTID=? ",new Object[]{proyecto,partida},Double.class); 
		return comprometido.doubleValue();		
	}
	
	public double getPreComprometidoMes(Long proyecto, String partida, int mes) {
		try{
			int reg= this.getJdbcTemplate().queryForInt("select count(*) from PART_EXT2  where PROYECTO=? and CLV_PARTID=?  ", new Object []{proyecto,partida});
			Double comprometido=new Double(0);
			Map dt = this.getJdbcTemplate().queryForMap("SELECT ("+Meses[mes-1]+"PREREQ) AS DISP FROM PART_EXT2 WHERE  PROYECTO=? and CLV_PARTID=? ",new Object[]{proyecto,partida});
			//if (reg ==0 )
			//	reg=0;
				//Modificado aqui Israel 16/Nov/2010
				//this.getJdbcTemplate().update("insert into PART_EXT2  (PROYECTO,CLV_PARTID,ENEPREREQ,FEBPREREQ,MARPREREQ,ABRPREREQ,MAYPREREQ,JUNPREREQ,JULPREREQ,AGOPREREQ,SEPPREREQ,OCTPREREQ,NOVPREREQ,DICPREREQ,ENEPREPED,FEBPREPED,MARPREPED,ABRPREPED,MAYPREPED,JUNPREPED,JULPREPED,AGOPREPED,SEPPREPED,OCTPREPED,NOVPREPED,DICPREPED,clv_uniant ) values (?,?,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)  ", new Object []{proyecto,partida} );
			//  else				
			     comprometido = (dt.get("DISP")!=null)? Double.parseDouble(dt.get("DISP").toString()): 0.0D;
			     return comprometido.doubleValue();
		}
		     catch ( DataAccessException e) {
					return 0.0D;
		}
	}
	
	public void actualizarPreCompromisoPresupuesto(String proyecto, String partida, int mes, double importe,String  tipo ){
		
	//double importeComprometido= getPreComprometidoMes(proyecto, partida, mes);
	if ("REDUCCION".equals(tipo)) {
		
		/*if ( importeComprometido>=importe  ){
			//Modificado aqui Israel 16/Nov/2010
		     //this.getJdbcTemplate().update("update PART_EXT2  set "+Meses[mes-1]+"PREREQ="+Meses[mes-1]+"PREREQ-?  where PROYECTO=? and CLV_PARTID=? ", new Object []{importe,proyecto,partida} );		     
		}else			
			 throw new RuntimeException("El compromiso no existe, no se puede reducir ");*/
	}
	if ("COMPROMETER".equals(tipo)) {
		//Modificado aqui Israel 16/Nov/2010
		   //this.getJdbcTemplate().update("update PART_EXT2 set "+Meses[mes-1]+"PREREQ="+Meses[mes-1]+"PREREQ+?  where PROYECTO=? and CLV_PARTID=? ", new Object []{importe,proyecto,partida} );	
	}
		
		
	}
	
	public void comprometerPresupuesto(String proyecto, String partida, int mes, double importe,String  tipo ){
		 //double importeComprometido= getComprometidoMes(proyecto, partida, mes);
		if ("REDUCCION".equals(tipo)) {
			/*if ( importeComprometido>=importe  ){
				//Modificado aqui Israel 16/Nov/2010
			     //this.getJdbcTemplate().update("update SPARTIDAS  set "+Meses[mes-1]+"PRECOM="+Meses[mes-1]+"PRECOM-?  where PROYECTO=? and CLV_PARTID=? ", new Object []{importe,proyecto,partida} );		     
			}else
				throw new RuntimeException("El compromiso no existe, no se puede reducir ");*/
		}
		if ("COMPROMETER".equals(tipo)) {
			//Modificado aqui Israel 16/Nov/2010
			   //this.getJdbcTemplate().update("update SPARTIDAS set "+Meses[mes-1]+"PRECOM="+Meses[mes-1]+"PRECOM+?  where PROYECTO=? and CLV_PARTID=? ", new Object []{importe,proyecto,partida} );	
		}
			
		
		}
	
	public List getPresupuestoCaledarizado (int unidad , String tipoGasto , Integer usuario, int unidad_usuario, Long idproyecto, String clv_partid) {		
		Map<String,Object> parametros = new HashMap<String,Object>();
		parametros.put("id_usuario", usuario);
		String SQL = "SELECT DISTINCT "+ 
						"	p.*, "+
						"	VP.LOCALIDAD,"+
						"	VP.K_PROYECTO_T,"+
						"	c.N_PROGRAMA, " +	
						"	ISNULL(pe.eneprereq,0) AS eneprereq, "+
						"	ISNULL(pe.febprereq,0) AS febprereq, "+
						"	ISNULL(pe.marprereq, 0) AS marprereq, "+
						"	ISNULL(pe.abrprereq,0) AS abrprereq, "+
						"	ISNULL(pe.mayprereq, 0) AS mayprereq, "+ 
						"	ISNULL(pe.junprereq,0) AS junprereq, "+
						"	ISNULL(pe.julprereq, 0) AS julprereq, "+
						"	ISNULL(pe.agoprereq, 0) AS agoprereq, "+
						"	ISNULL(pe.sepprereq, 0) AS sepprereq, "+
						"	ISNULL(pe.octprereq,0) AS octprereq, "+
						"	ISNULL(pe.novprereq, 0) AS novprereq, "+
						"	ISNULL(pe.dicprereq,0) AS dicprereq, "+
						"	c.decripcion, "+
						"	pp.partida "+
						"FROM SPARTIDAS p "+
						"	INNER JOIN CEDULA_TEC c ON (p.ID_PROYECTO = c.ID_PROYECTO) "+
						"	INNER JOIN VPROYECTO AS VP ON(VP.ID_PROYECTO = C.ID_PROYECTO) "+
						"	INNER JOIN CAT_PARTID pp ON (p.CLV_PARTID = pp.CLV_PARTID) "+
						"	LEFT JOIN PART_EXT2 pe ON (p.ID_PROYECTO=pe.ID_PROYECTO AND p.CLV_PARTID = pe.CLV_PARTID) "+
						"WHERE ";
		
		SQL+= " CAST(p.ID_PROYECTO AS NVARCHAR)+CAST(p.CLV_PARTID AS NVARCHAR) IN (SELECT CAST(ID_PROYECTO AS NVARCHAR)+CAST(CLV_PARTID AS VARCHAR) FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO =:id_usuario) ";
		
		if(unidad!=0){
			//comprobar que tiene acceso al presupuesto de esa unidad
			boolean acceso = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = ? AND ID_DEPENDENCIA =?", new Object[]{usuario, unidad})!=0;
			if(acceso) 
				SQL +="AND  c.ID_DEPENDENCIA ="+unidad+" ";
			else
				SQL +="AND  c.ID_DEPENDENCIA = 0 ";
			
		}
		
		
		
		if(tipoGasto!=null)
			if(!tipoGasto.equals("")&&!tipoGasto.equals("0"))
			SQL +=" AND c.ID_RECURSO = '"+tipoGasto+"'";
		
		if(idproyecto!=null)
			if(idproyecto!=0)
				SQL +=" AND p.ID_PROYECTO = "+idproyecto+" ";
		
		if(clv_partid!=null)
			if(!clv_partid.equals("")&&!clv_partid.equals("0"))
				SQL +=" AND p.CLV_PARTID = '"+clv_partid+"'";
		
		SQL +=" ORDER BY p.ID_PROYECTO, p.CLV_PARTID ASC";

		List<Map> resultado =  this.getNamedJdbcTemplate().queryForList(SQL,parametros);
		
		
		return resultado;
	}
	
	public Map getPresupuestoProyectoPartida(String proyecto , String partida ) {		
		Map<String,Object> parametros = new HashMap<String,Object>();
		parametros.put("proyecto", proyecto);
		parametros.put("partida",partida);				
		String sql =" select p.*, pe.ENEPREREQ, pe.FEBPREREQ, pe.MARPREREQ, pe.ABRPREREQ, pe.MAYPREREQ, c.decripcion, pp.partida, " +
                 	" pe.JUNPREREQ, pe.JULPREREQ, pe.AGOPREREQ, pe.SEPPREREQ, pe.OCTPREREQ, pe.NOVPREREQ, pe.DICPREREQ" +
                 	" from SPARTIDAS p inner join cedula_tec c on (p.id_proyecto=c.id_proyecto) " +
                    " inner join cat_partid pp on (p.clv_partid=pp.clv_partid) " +
                    " left join part_ext2 pe on (p.id_proyecto=pe.id_proyecto and p.clv_partid=pe.clv_partid) " +
                    " where p.id_proyecto=:proyecto  and  p.clv_partid=:partida   ";
		Map resultado =  this.getNamedJdbcTemplate().queryForMap(sql,parametros);
		return resultado;
	}
	
	/*Metodo que devuelve la informacion presupuestal del pedido*/
	public Map getLightPartidaPresupuestal(String proyecto, String clv_partid){
		try  
		{
			String sql = "SELECT "+
									"V.*, "+
									"C.* "+
							"FROM VPROYECTO AS V "+ 
									"INNER JOIN CAT_PARTID AS C ON (C.CLV_PARTID = ?) "+
							"WHERE V.ID_PROYECTO = ?";
			Map resultado = this.getJdbcTemplate().queryForMap(sql, new Object []{clv_partid, proyecto});
			return resultado;
			
		}
		catch ( DataAccessException e) {
			return null;
		}
	}

	/*Retorna los proyectos asignados a un area para los avances fisicos */
	public List<Map> getProyectosAvances(int cve_pers){
		String sql = "SELECT CEDULA_TEC.PROYECTO, DECRIPCION,  CEDULA_TEC.CLV_UNIADM, EVAL_PROY_MENSUAL.ID_EVALUACION_PROYECTO FROM CEDULA_TEC "+
			"LEFT JOIN EVAL_PROY_MENSUAL ON (EVAL_PROY_MENSUAL.PROYECTO = CEDULA_TEC.PROYECTO) "+
			"INNER JOIN PRESUPUEST ON (CEDULA_TEC.PROYECTO = PRESUPUEST.PROYECTO) "+
			"WHERE SUBSTRING(CEDULA_TEC.PROYECTO, 1, 2)<> 'GC' AND PRESUPUEST.APROBADO ='S' AND PRESUPUEST.TERMINADO ='N' AND CEDULA_TEC.CLV_UNIADM IN(SELECT DISTINCT CLV_UNIADM FROM CEDULA_TEC WHERE PROYECTO IN (SELECT bb.PROYECTO FROM SAM_GRUPO_CONFIG_USUARIO aa INNER JOIN  SAM_GRUPO_PROYECTOS bb ON aa.ID_GRUPO_CONFIG = bb.ID_GRUPO_CONFIG  where aa.ID_USUARIO=?))  AND CEDULA_TEC.PROYECTO IN((SELECT PROYECTO FROM SAM_GRUPO_CONFIG_USUARIO INNER JOIN  SAM_GRUPO_PROYECTOS ON (SAM_GRUPO_PROYECTOS.ID_GRUPO_CONFIG = SAM_GRUPO_CONFIG_USUARIO.ID_GRUPO_CONFIG) WHERE ID_USUARIO =?))";
		return this.getJdbcTemplate().queryForList(sql, new Object[]{cve_pers,cve_pers});
	}
	
	/* 26 DE JULIO DE 2012*/
	public List<Map> getTodosProyectos(){
		String sql = "SELECT CEDULA_TEC.ID_PROYECTO, CEDULA_TEC.N_PROGRAMA FROM CEDULA_TEC "+
			"INNER JOIN PRESUPUEST ON (CEDULA_TEC.ID_PROYECTO = PRESUPUEST.ID_PROYECTO) "+
			"WHERE PRESUPUEST.APROBADO ='S' AND PRESUPUEST.TERMINADO ='N'";
		return this.getJdbcTemplate().queryForList(sql);
	}
	  
	public BigDecimal getDisponibleMes(int mes, Long proyecto, String clv_partid){
		try  
		{
			return (BigDecimal) this.getJdbcTemplate().queryForObject("SELECT  ISNULL(dbo.getDisponible(?,?,?),0) AS DISPONIBLE", new Object[]{mes, proyecto, clv_partid}, BigDecimal.class);
		}
		catch ( DataAccessException e) {
			return new BigDecimal("0");
		}
	}
	  
}
