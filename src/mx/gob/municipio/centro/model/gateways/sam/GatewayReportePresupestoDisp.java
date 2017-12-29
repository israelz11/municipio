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
		
		String sql = " SELECT VP.ID_RECURSO,VP.RECURSO,VP.ID_DEPENDENCIA,VP.DEPENDENCIA,VT_AUTOEVALUACION.ID_PROYECTO,  VT_AUTOEVALUACION.DECRIPCION, VT_AUTOEVALUACION.CLV_PARTID, PARTIDA, VT_AUTOEVALUACION.CLV_CAPITU, " +
					 " SUM(INICIAL) AS INICIAL, " +
					 " SUM(PRESUPUESTO) AS PRESUPUESTO, " +
					 "SUM(COMPROMETIDO) AS COMPROMETIDO, " +
				     "SUM(DEVENGADO) AS DEVENGADO, " +
				 	 "SUM(EJERCIDO) AS EJERCIDO, " +
				     "SUM(dbo.getPrecomprometidoAnual(VP.ID_PROYECTO,VT_AUTOEVALUACION.CLV_PARTID)) AS PRECOMPROMISO " +
				     "FROM VT_AUTOEVALUACION INNER JOIN VPROYECTO AS VP ON VP.ID_PROYECTO  = VT_AUTOEVALUACION.ID_PROYECTO " +
				     "LEFT JOIN VT_COMPROMISOS VC ON VC.ID_PROYECTO=VT_AUTOEVALUACION.ID_PROYECTO AND VT_AUTOEVALUACION.CLV_PARTID=VC.CLV_PARTID AND VC.CONSULTA LIKE 'PRECOMPROM%' " +
				     " WHERE 0=0 ";
		
		if(!modelo.get("idtipogasto").toString().equals("0"))
			sql += " AND ID_RECURSO = :idtipogasto ";
		
		if(modelo.get("idUnidad")!=null)
			if(!modelo.get("idUnidad").toString().equals("0"))
				sql += " AND VP.ID_DEPENDENCIA = :idUnidad ";
		
		if(modelo.get("idproyecto")!=null)
			if(!modelo.get("idproyecto").toString().equals("0"))
				sql += " AND VP.ID_PROYECTO = :idproyecto ";
		
		if(modelo.get("idcapitulo")!= null)
		if(!modelo.get("idcapitulo").toString().equals("0"))
			sql += " AND VT_AUTOEVALUACION.CLV_CAPITU = :idcapitulo ";
		
		if(modelo.get("idpartida")!=null)
			if(!modelo.get("idpartida").equals(""))
				sql += " AND VT_AUTOEVALUACION.CLV_PARTID = :idpartida ";
		
		//txtproyecto
		
		sql +=	 	 //"WHERE ID_PROYECO IN("+m.get("PROYECTO").toString()+")  " +
				     //"WHERE MES=1"+
				     "GROUP BY VP.ID_RECURSO,VP.RECURSO,VP.ID_DEPENDENCIA,VP.DEPENDENCIA, VT_AUTOEVALUACION.ID_PROYECTO, VT_AUTOEVALUACION.DECRIPCION, VT_AUTOEVALUACION.CLV_PARTID, PARTIDA, INICIAL, PRESUPUESTO, COMPROMETIDO, DEVENGADO, EJERCIDO, VT_AUTOEVALUACION.CLV_CAPITU  ORDER BY VP.ID_RECURSO,VP.RECURSO,VP.ID_DEPENDENCIA,VT_AUTOEVALUACION.ID_PROYECTO";
		
		
		return this.getNamedJdbcTemplate().queryForList(sql, modelo);
		
	}
	
}
