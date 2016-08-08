/**
 * @author ISC. Israel de la Cruz Hernández.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.List;
import java.util.Map;
import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;

public class GatewayTiposDocumentos extends BaseGatewayAlmacen {

	//Constructor
	public GatewayTiposDocumentos(){}
	
	public List<Map> getDocumentos(){
	    return this.getJdbcTemplate().queryForList("SELECT ID_TIPO_DOCUMENTO, DESCRIPCION, STATUS, CASE STATUS WHEN '1' THEN 'ACTIVO' WHEN '0' THEN 'INACTIVO' END AS STATUS_DESC FROM TIPOS_DOCUMENTOS");		
	}
	
	public List<Map> getListadoActivo(){
	    return this.getJdbcTemplate().queryForList("SELECT ID_TIPO_DOCUMENTO, DESCRIPCION, STATUS, CASE STATUS WHEN '1' THEN 'ACTIVO' WHEN '0' THEN 'INACTIVO' END AS STATUS_DESC FROM TIPOS_DOCUMENTOS WHERE STATUS=1");		
	}
	
	public boolean guardarTipoDocumento(int id_tipo_documento, String descripcion, boolean status)
	{
		if(id_tipo_documento!=0)
			this.getJdbcTemplate().update("UPDATE TIPOS_DOCUMENTOS SET DESCRIPCION = ?, STATUS = ? WHERE ID_TIPO_DOCUMENTO = ?", new Object[]{descripcion, status, id_tipo_documento});
		else
			this.getJdbcTemplate().update("INSERT INTO TIPOS_DOCUMENTOS(DESCRIPCION, STATUS) VALUES(?,?)", new Object[]{descripcion, status});
		return true;
	}
	
	public boolean eliminarTiposDocumentos(Long id, int cve_pers)
	{
		this.getJdbcTemplate().update("UPDATE TIPOS_DOCUMENTOS SET STATUS = ? WHERE ID_TIPO_DOCUMENTO = ?", new Object []{0,id});
		//deberia de haber una bitacora aqui?
		return true;
	}
	
	public List<Map> getTiposEntradas(){
		return this.getJdbcTemplate().queryForList("SELECT *FROM TIPO_ENTRADA WHERE STATUS=1");
	}
}
