/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @date: 30/June/2010
 */
package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.List;
import java.util.Map;
import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;

public class GatewayFamiliasArticulos extends BaseGatewayAlmacen {

	public GatewayFamiliasArticulos() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean guardarFamiliasArticulos(int id_familia, String descripcion, boolean status)
	{
		if(id_familia!=0)
			this.getJdbcTemplate().update("UPDATE FAMILIAS SET DESCRIPCION = ?, STATUS = ? WHERE ID_FAMILIA = ?", new Object[]{descripcion, status, id_familia});
		else
			this.getJdbcTemplate().update("INSERT INTO FAMILIAS(DESCRIPCION, STATUS) VALUES(?,?)", new Object[]{descripcion, status});
		return true;
	}
	
	public List<Map> getListaFamiliasArticulos(){
	    return this.getJdbcTemplate().queryForList("SELECT ID_FAMILIA, DESCRIPCION, STATUS, CASE STATUS WHEN '1' THEN 'ACTIVO' WHEN '0' THEN 'INACTIVO' END AS STATUS_DESC FROM FAMILIAS ORDER BY DESCRIPCION ASC");		
	}
	
	public List<Map> getListadoActivo(){
	    return this.getJdbcTemplate().queryForList("SELECT ID_FAMILIA, DESCRIPCION, STATUS, CASE STATUS WHEN '1' THEN 'ACTIVO' WHEN '0' THEN 'INACTIVO' END AS STATUS_DESC FROM FAMILIAS WHERE STATUS=1 ORDER BY DESCRIPCION ASC");		
	}
	
	public Map getFamilia(int id)
	 {
		 return this.getJdbcTemplate().queryForMap("SELECT *FROM FAMILIAS WHERE ID_FAMILIA = ?", new Object[]{id});
	 }
	
	public String getFamiliaCompleta(int id)
	 {
		if(id==0) 
			return "";
		else
			return (String)this.getJdbcTemplate().queryForObject("SELECT DESCRIPCION FROM FAMILIAS WHERE ID_FAMILIA = ?", new Object[]{id}, String.class);
	 }
	
	public boolean eliminarFamiliasArticulos(Long id, int cve_pers)
	{
		this.getJdbcTemplate().update("UPDATE FAMILIAS SET STATUS = ? WHERE ID_FAMILIA = ?", new Object []{0,id});
		//deberia de haber una bitacora aqui?
		return true;
	}
	
}
