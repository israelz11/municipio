/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Descriopcion GateWay que administra la tabla de SGD_CAT_UNIDADES Y CAT_UNIADM
 */
package mx.gob.municipio.centro.model.gateways.correspondencia;
import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGatewayCorrespondencia;

public class GatewayUnidadesAdministrativas extends BaseGatewayCorrespondencia {

	/**
	 * @param args
	 */
	public GatewayUnidadesAdministrativas() {
		// TODO Auto-generated method stub

	}

	public List getListaUnidades(){
		return this.getJdbcTemplate().queryForList("SELECT *FROM VT_SGD_UNIDADES WHERE DESCRIPCION <> '' AND RESPONSABLE IS NOT NULL ORDER BY DESCRIPCION ASC", new Object[]{});
	}
	
	public void guardarUnidadAdm(int cve_unidad, String descripcion, String prefijo, String responsable, String status ){
		if(cve_unidad==0)
			this.getJdbcTemplate().update("INSERT INTO SGD_CAT_UNIDADES(ID_UNIDAD, RESPONSABLE, DESCRIPCION, PREFIJO, EDITABLE, STATUS) VALUES(?,?,?,?,?,?)", new Object[]{cve_unidad, responsable, descripcion, prefijo, 1, 1});
		else
			this.getJdbcTemplate().update("UPDATE SGD_CAT_UNIDADES SET RESPONSABLE = ?, DESCRIPCION = ?, PREFIJO = ?, STATUS = ? WHERE ID_UNIDAD = ? ", new Object[]{responsable, descripcion, prefijo, status, cve_unidad});
	}
	
	public Map getUnidadAdm(Long cve_unidad){
		return this.getJdbcTemplate().queryForMap("SELECT *FROM SGD_CAT_UNIDADES WHERE ID_UNIDAD = ?", new Object[]{cve_unidad});
	}
	
	public void eliminarUnidadAdm(Long idUnidad){
		this.getJdbcTemplate().update("DELETE FROM SGD_CAT_UNIDADES WHERE ID_UNIDAD = ? ", new Object[]{idUnidad});
	}
}
