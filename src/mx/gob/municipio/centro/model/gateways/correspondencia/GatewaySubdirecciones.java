/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 04/10/2012
 * @Descriopcion metodos para catalogo de subdirecciones
 */

package mx.gob.municipio.centro.model.gateways.correspondencia;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGatewayCorrespondencia;

public class GatewaySubdirecciones extends BaseGatewayCorrespondencia {
	public GatewaySubdirecciones() {
		// TODO Auto-generated method stub

	}
	
	public List <Map> getSubdirecciones(Long idDependencia){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SGD_CAT_SUBDIRECCIONES WHERE ID_DEPENDENCIA =? ORDER BY DESCRIPCION ASC", new Object[]{idDependencia});
	}
	
	public String guardarSubdireccion(Long idDependencia, Long idSubdireccion, String subdireccion, String responsable, String status){
		if(idSubdireccion==0){
			this.getJdbcTemplate().update("INSERT INTO SGD_CAT_SUBDIRECCIONES(ID_DEPENDENCIA, RESPONSABLE, DESCRIPCION, STATUS) VALUES(?,?,?,?)",
					new Object[]{idDependencia, responsable, subdireccion, 1});
		}
		else{
			this.getJdbcTemplate().update("UPDATE SGD_CAT_SUBDIRECCIONES SET ID_DEPENDENCIA = ?, RESPONSABLE =?, DESCRIPCION =?, STATUS=? WHERE ID_SUBDIRECCION =?",
					new Object[]{idDependencia, responsable, subdireccion, (status.equals("true") ? 1:0), idSubdireccion});
		}
		return "";
	}
	
	public Map getSubdirecionDetalle(Long idSubdireccion){
		return this.getJdbcTemplate().queryForMap("SELECT *FROM SGD_CAT_SUBDIRECCIONES WHERE ID_SUBDIRECCION = ?", new Object[]{idSubdireccion});
	}
	
	public void eliminarSubdireccion(Long idSubdireccion){
		this.getJdbcTemplate().update("DELETE FROM SGD_CAT_SUBDIRECCIONES WHERE ID_SUBDIRECCION =?", new Object[]{idSubdireccion});
	}
	
	public List<Map> getListaSubdirecciones(Long idDependencia){
		return this.getJdbcTemplate().queryForList("SELECT * FROM SGD_CAT_SUBDIRECCIONES WHERE STATUS=1 AND ID_DEPENDENCIA=? ORDER BY DESCRIPCION ASC", new Object[]{idDependencia});
	}
}
