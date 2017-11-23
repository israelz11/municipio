package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayReportePresupestoDisp extends BaseGateway{

	private static Logger log = Logger.getLogger(GatewayReportePresupestoDisp.class.getName());
	
	@Autowired
	public GatewayProyectoPartidas gatewayProyectoPartidas;
	
	public GatewayReportePresupestoDisp() {
		// TODO Auto-generated method stub
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map> getreparametros(Map modelo){
		
		String sql = " SELECT VT_AUTOEVALUACION.ID_PROYECTO, VP.ID_RECURSO, VT_AUTOEVALUACION.DECRIPCION, CLV_PARTID, PARTIDA, " +
					 " CONVERT(varchar, CAST(SUM(INICIAL) AS MONEY),1) AS INICIAL, " +
					 " CONVERT(varchar, CAST(SUM(PRESUPUESTO) AS MONEY),1) AS PRESUPUESTO, " +
					 "CONVERT(varchar, CAST(SUM(COMPROMETIDO) AS MONEY),1) AS COMPROMETIDO, " +
				     "CONVERT(varchar, CAST(SUM(DEVENGADO) AS MONEY),1) AS DEVENGADO, " +
				 	 "CONVERT(varchar, CAST(SUM(EJERCIDO) AS MONEY),1) AS EJERCIDO " +
				     "FROM VT_AUTOEVALUACION INNER JOIN VPROYECTO AS VP ON VP.ID_PROYECTO  = VT_AUTOEVALUACION.ID_PROYECTO  WHERE 0=0 ";
		
		if(!modelo.get("idtipogasto").toString().equals(0))
			sql += "AND ID_RECURSO = :idtipogasto ";
		
			
		sql +=	 	 //"WHERE ID_PROYECO IN("+m.get("PROYECTO").toString()+")  " +
				     //"WHERE MES=1"+
				     "GROUP BY VT_AUTOEVALUACION.ID_PROYECTO, VP.ID_RECURSO, VT_AUTOEVALUACION.DECRIPCION, CLV_PARTID, PARTIDA, INICIAL, PRESUPUESTO, COMPROMETIDO, DEVENGADO, EJERCIDO";
		
		/*
		if(modelo.get("cbodependencia")!=null)
			if(!modelo.get("cbodependencia").toString().equals("0"))
				sql += " AND F.ID_DEPENDENCIA =:cbodependencia";
		*/
				
		return this.getNamedJdbcTemplate().queryForList(sql, modelo);
		
	}
	
}
