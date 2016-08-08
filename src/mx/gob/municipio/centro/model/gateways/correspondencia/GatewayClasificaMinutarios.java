package mx.gob.municipio.centro.model.gateways.correspondencia;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGatewayCorrespondencia;

public class GatewayClasificaMinutarios extends BaseGatewayCorrespondencia {

	public GatewayClasificaMinutarios(){
		
	}
	
	public List<Map> getClasificaMinutarios(Long idDependencia){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SGD_CAT_CLASIFICACION_MINUTARIO WHERE ID_DEPENDENCIA = ?", new Object[]{idDependencia});
	}
	
	public void guardarClasificaMinutario(Long idClasifica, Long idDependencia,  String descripcion, String status){
		if(idClasifica==0){
			this.getJdbcTemplate().update("INSERT INTO SGD_CAT_CLASIFICACION_MINUTARIO(ID_DEPENDENCIA, DESCRIPCION, STATUS) VALUES(?,?,?)",
					new Object[]{idDependencia,descripcion, 1});
		}
		else{
			this.getJdbcTemplate().update("UPDATE SGD_CAT_CLASIFICACION_MINUTARIO SET ID_DEPENDENCIA=?, DESCRIPCION =?, STATUS=? WHERE ID_CAT_CLASIFICACION_MINUTARIO =?",
					new Object[]{idDependencia, descripcion, (status.equals("true") ? 1:0), idClasifica});
		}
	}
	
	public void eliminarClasificaMinutarios(Long id){
		this.getJdbcTemplate().update("DELETE FROM SGD_CAT_CLASIFICACION_MINUTARIO WHERE ID_CAT_CLASIFICACION_MINUTARIO =?", new Object[]{id});
	}
}
