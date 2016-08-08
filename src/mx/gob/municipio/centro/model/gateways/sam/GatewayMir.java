/**
 * @author Israel de la Cruz Hdez.
 * @version 1.0
 * @fecha 24/02/2016
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.dao.DataAccessException;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayMir extends BaseGateway {
	private static Logger log = Logger.getLogger(GatewayMir.class.getName());

	public GatewayMir(){}
	
	public List<Map> getProgramas(String IdDependencia){
		try  
		{
			return this.getJdbcTemplate().queryForList("SELECT DISTINCT N_PROGRAMA FROM VT_PROGRAMATICA WHERE CLV_UNIADM = ? ORDER BY N_PROGRAMA ASC", new Object []{IdDependencia});
		}
		catch ( DataAccessException e) {
			return null;
		}
	}
	
	public String Guardar(int IdMIR, int Dependencia, String Fecha, String Programa, String ClaveProgramatica, int CvePers, int Ejercicio)
	{
		try {  
			if(IdMIR ==0)
			{
				Date FechaRegistro = new Date();
				//String Folio = rellenarCeros(String.valueOf(this.getJdbcTemplate().queryForInt("SELECT MAX(ID_MIR) FROM SAM_MIR_MAESTRO").ToString() + 1),6);
				this.getJdbcTemplate().update("INSERT INTO SAM_MIR_MAESTRO (FOLIO, "
												+ "ID_DEPENDENCIA "+
									           ",ID_STATUS"+
									           ",N_PROGRAMA"+
									           ",NUM_EMP_CAPTURA"+
									           ",NUM_EMP_AUTORIZA"+
									           ",VERSION"+
									           ",EJERCICIO"+
									           ",CLAVE_PROGRAMATICA"+
									           ",FECHA_CREACION"+
									           ",FECHA_AUTORIZACION"+
									           ",FECHA_CANCELACION) "
									           + "VALUE (?,?,?,?,?,?,?,?,?,?,?,?)"
									           , new Object[]{"00000", Dependencia, 0, Programa, CvePers, null, 1, Ejercicio, ClaveProgramatica, FechaRegistro, null, null});
			}
			else
			{
				this.getJdbcTemplate().update("UPDATE SAM_MIR_MAESTRO SET N_PROGRAMA =?, CLAVE_PROGRAMATICA =? WHERE ID_MIR =?", new Object[]{Programa, ClaveProgramatica, IdMIR});
			}
			return "";
		}
		catch (DataAccessException e) {                                
			throw new RuntimeException(e.getMessage());
		}
	}
}
