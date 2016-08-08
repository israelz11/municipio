package mx.gob.municipio.centro.model.gateways.sam.webservices;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;

import org.springframework.beans.factory.annotation.Autowired;

public class GatewayWebServicePeriodos extends BaseGateway {
	
	@Autowired 
	GatewayMeses gatewayMeses;
	
	public GatewayWebServicePeriodos(){}
	
	@SuppressWarnings("unchecked")
	public List<Map> getPeriodos(int ejercicio){
		return this.getJdbcTemplate().queryForList("SELECT MES AS ID, DESCRIPCION, (CASE ESTATUS WHEN 'INACTIVO' THEN 'false' ELSE 'true' END) AS STATUS_PRESUP, (CASE ESTATUSEVA WHEN 'INACTIVO' THEN 'false' ELSE 'true' END) AS STATUS_EVAL FROM MESES where ejercicio=?  ORDER BY MES ASC", new Object[]{ejercicio});
	}

	public boolean abrirCerrarPeriodos(int periodo, String tipo, int ejercicio)
	{
		int mesActivo = gatewayMeses.getMesActivo(ejercicio);
		Map periodoBuscado = this.getJdbcTemplate().queryForMap("SELECT MES AS ID, DESCRIPCION, (CASE ESTATUS WHEN 'INACTIVO' THEN 'false' ELSE 'true' END) AS STATUS_PRESUP, ESTATUSEVA FROM MESES where ejercicio=? and MES =?  ORDER BY MES ASC", new Object[]{ejercicio, periodo});

		if(tipo.equals("P")){
			//si es abierto
			if(Boolean.parseBoolean(periodoBuscado.get("STATUS_PRESUP").toString())){
				if(mesActivo == periodo)
				{//cerrar
					gatewayMeses.actializarEstatusDoc(periodo, "ACTIVO");
					return true;
				}
				else
					return false;
			}
			else{ //si es cerrado
				if((mesActivo-1) == periodo && tipo.equals("P"))
				{//abrir
					gatewayMeses.actializarEstatusDoc(periodo, "INACTIVO");
					return true;
				}
				else
					return false;
			}
			
		}
		else
		{
			gatewayMeses.actializarEstatusEva(periodo, periodoBuscado.get("ESTATUSEVA").toString());
			return true;
		}
	}
	
	public boolean reiniciarPeriodoPresupuestal(int ejercicio){
		List<Map> m = getPeriodos(ejercicio);
		for(Map item: m){
			if(Boolean.parseBoolean(item.get("STATUS_PRESUP").toString())){
				return false;
			}
		}
		//Abre todos los periodos
		for(Map item: m){
			gatewayMeses.actializarEstatusDoc(Integer.parseInt(item.get("ID").toString()), "INACTIVO");
		}
		return true;
	}
	
	public boolean reiniciarEvaluacion(int ejercicio){
		List<Map> m = getPeriodos(ejercicio);
		for(Map item: m){
			gatewayMeses.actializarEstatusEva(Integer.parseInt(item.get("ID").toString()), "INACTIVO");
		}
		return true;
	}
}
