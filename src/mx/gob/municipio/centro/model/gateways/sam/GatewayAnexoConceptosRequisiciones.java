/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.dao.DataAccessException;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayAnexoConceptosRequisiciones extends BaseGateway {
	private static Logger log = Logger.getLogger(GatewayAnexoConceptosRequisiciones.class.getName());

	public GatewayAnexoConceptosRequisiciones(){}
	
	public Map getAnexoConceptoRequisicion(Long id_req_movto){
		try  
		{
			return this.getJdbcTemplate().queryForMap("SELECT TEXTO FROM SAM_REQ_ANEXO FROM WHERE ID_REQ_MOVTO = ?", new Object []{id_req_movto});
		}
		catch ( DataAccessException e) {
			return null;
		}
	}
	
	private boolean buscarAnexoConcepto(Long id_req_movto){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_REQ_ANEXO WHERE ID_REQ_MOVTO = ?", new Object[]{id_req_movto})!=0;
	}
	public boolean guardarAnexoConceptoRequisicion(Long id_req_movto, String texto){
		/*comprobar si existe el anexo en la tabla */
		boolean c = this.buscarAnexoConcepto(id_req_movto);
		if(!c){
			this.getJdbcTemplate().update("INSERT INTO SAM_REQ_ANEXO(ID_REQ_MOVTO, TEXTO) VALUES(?,?)", new Object []{id_req_movto, texto});
			return true;
		}
		else{
			this.getJdbcTemplate().update("UPDATE SAM_REQ_ANEXO SET TEXTO = ? WHERE ID_REQ_MOVTO = ?", new Object []{texto, id_req_movto});
			return true;
		}
	}
}
