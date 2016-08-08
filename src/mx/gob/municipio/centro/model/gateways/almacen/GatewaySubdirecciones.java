/**
 * @author ISC. Israel de la ruz Hernandez.
 * @version 1.0
 * @date: 29/June/2010
 */
package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;

public class GatewaySubdirecciones extends BaseGatewayAlmacen {

	public GatewaySubdirecciones() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean guardarSubdireccion(int id_subdireccion, int id_unidad, String descripcion, String encargado, String puesto, boolean status){
		if(id_subdireccion!=0)
			this.getJdbcTemplate().update("UPDATE CAT_SUBDIRECCIONES SET DESCRIPCION = ?, NOMBRE_ENCARGADO = ?, PUESTO_ENCARGADO = ?, STATUS = ? WHERE ID_SUBDIRECCION = ?", new Object[]{descripcion, encargado, puesto, status, id_subdireccion});
		else
			this.getJdbcTemplate().update("INSERT INTO CAT_SUBDIRECCIONES(ID_UNIADM, DESCRIPCION,NOMBRE_ENCARGADO,PUESTO_ENCARGADO, STATUS) VALUES(?,?,?,?,?)", new Object[]{id_unidad, descripcion,encargado,puesto, status});
		return true;
	}
	
	public List<Map> getSubdirecciones(){
	    return this.getJdbcTemplate().queryForList("SELECT ID_SUBDIRECCION, ID_UNIADM, DESCRIPCION, NOMBRE_ENCARGADO, PUESTO_ENCARGADO, STATUS, CASE STATUS WHEN '1' THEN 'ACTIVO' WHEN '0' THEN 'INACTIVO' END AS STATUS_DESC FROM CAT_SUBDIRECCIONES");		
	}
	
	public Map getSubdireccion(int id_subdireccion){
	    return this.getJdbcTemplate().queryForMap("SELECT ID_SUBDIRECCION, ID_UNIADM, DESCRIPCION, NOMBRE_ENCARGADO, PUESTO_ENCARGADO, STATUS, CASE STATUS WHEN '1' THEN 'ACTIVO' WHEN '0' THEN 'INACTIVO' END AS STATUS_DESC FROM CAT_SUBDIRECCIONES WHERE ID_SUBDIRECCION = ?", new Object []{id_subdireccion});		
	}
	
	public boolean eliminarSubdirecciones(Long id, int cve_pers)
	{
		this.getJdbcTemplate().update("UPDATE CAT_SUBDIRECCIONES SET STATUS = ? WHERE ID_SUBDIRECCION = ?", new Object []{0,id});
		//deberia de haber una bitacora aqui?
		return true;
	}

}
