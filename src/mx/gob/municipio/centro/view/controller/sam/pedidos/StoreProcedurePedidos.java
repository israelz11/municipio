package mx.gob.municipio.centro.view.controller.sam.pedidos;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

public class StoreProcedurePedidos extends StoredProcedure {
	private static final String SPROC_NAME = "Cnt_Pedido"; 
	
	public StoreProcedurePedidos( DataSource datasource ){
		super( datasource, SPROC_NAME );
		declareParameter( new SqlParameter( "CvePed", Types.INTEGER) );
		declareParameter( new SqlParameter( "TipoOper", Types.INTEGER) ); 
		//declareParameter( new SqlOutParameter( "Valor", Types.INTEGER ) ); 
		compile();
	}
	
	public Map execute(Long cve_ped, int tipo){ 
		Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("CvePed", cve_ped);
        parameters.put("TipoOper", tipo);
        parameters.put("Valor", null);
        Map<String,Object> results = super.execute(parameters);
        return results;
	}
	


	
}
