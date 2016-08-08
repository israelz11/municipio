/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam.catalogos;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayMeses extends BaseGateway {

	public GatewayMeses(){
		
	}
	
	public List<Map> getTodosMesesEjercicio(Integer ejercicio){		
	  return this.getJdbcTemplate().queryForList("select * from MESES where ejercicio=?",new Object []{ejercicio});		
	}
	
	public List<Map> getTodosMesesEjercicioActivos(Integer ejercicio){		
		  return this.getJdbcTemplate().queryForList("select * from MESES where  ESTATUS ='ACTIVO' and ejercicio=?",new Object []{ejercicio});		
		}
	
	public List<Map> getTodosMesesEjercicioEvaluacion(Integer ejercicio, boolean privilegio){	
		if(privilegio)
			return this.getJdbcTemplate().queryForList("select * from MESES where ejercicio=?",new Object []{ejercicio});
		else
		  return this.getJdbcTemplate().queryForList("select * from MESES where ESTATUSEVA ='ACTIVO' and ejercicio=?",new Object []{ejercicio});		
		}
	
	public List<Map> getMesesRequisicion(Integer ejercicio){		
		  return this.getJdbcTemplate().queryForList("select * from MESES where ejercicio=?",new Object []{ejercicio});		
		}
	
	public int getMesActivo(Integer ejercicio){
		return this.getJdbcTemplate().queryForInt("SELECT  min(MES) FROM MESES WHERE ESTATUS='ACTIVO' and ejercicio=?", new Object[]{ejercicio});
	}
	
	public int getEjercicioActivo(){
		return this.getJdbcTemplate().queryForInt("SELECT TOP 1 EJERCICIO FROM MESES WHERE ESTATUS='ACTIVO'");
	}
	
	public boolean esMesActivo(int mes, int ejercicio){				
		int activos = this.getJdbcTemplate().queryForInt("SELECT  TOP 1 MES FROM MESES WHERE ESTATUS='ACTIVO' AND ejercicio=? ", new Object[]{ejercicio});

		return activos == mes;
	}
	
	public void actializarEstatusDoc(Integer idMes, String estatus){
		/*Aplicar aqui el cierre del mes*/
		estatus = estatus.equals("ACTIVO") ? "INACTIVO": "ACTIVO";
		this.getJdbcTemplate().update("UPDATE MESES SET ESTATUS =? WHERE ID_MES =?", new Object []{estatus,idMes});	
	}
	
	public void actializarEstatusEva(Integer idMes, String estatus){
		estatus = estatus.equals("ACTIVO") ? "INACTIVO": "ACTIVO";
		this.getJdbcTemplate().update("UPDATE MESES SET ESTATUSEVA =? WHERE ID_MES =?", new Object []{estatus,idMes});	
	}
	
	
}
