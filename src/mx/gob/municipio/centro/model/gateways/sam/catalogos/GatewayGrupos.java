/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam.catalogos;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayGrupos extends BaseGateway {

	public GatewayGrupos(){
		
	}

	public  void actualizarPrincipal(Integer clave, String descripcion, String estatus,String tipo){
	  if (clave == null) 		  
		  inserta(descripcion,estatus,tipo);	  	  
	  else
		  actualizar(clave,descripcion,estatus);
	}

	public void inserta( String descripcion, String estatus , String tipo ){
		this.getJdbcTemplate().update("insert into SAM_GRUPO_CONFIG (GRUPO_CONFIG, ESTATUS,TIPO ) " +
				"VALUES (?,?,?)"
				, new Object[]{descripcion,estatus,tipo});
	}

	public void actualizar(Integer clave, String descripcion, String estatus){	
		this.getJdbcTemplate().update("update SAM_GRUPO_CONFIG  set GRUPO_CONFIG=?, ESTATUS =?   where ID_GRUPO_CONFIG=? "
				, new Object[]{descripcion,estatus,clave});
	}	

	public List getGruposEstatus(String  estatus, String tipo ) {	   
		   return this.getJdbcTemplate().queryForList(" select * from SAM_GRUPO_CONFIG where ESTATUS=? and  TIPO=? ORDER BY GRUPO_CONFIG ASC", new Object[]{estatus,tipo});
	}

	public void eliminar(Integer clave  ){
		this.getJdbcTemplate().update("delete from SAM_GRUPO_CONFIG where ID_GRUPO_CONFIG= ?  ", new Object[]{clave});
	}
	
	public List getTipoGrupos( ) {	   
		   return this.getJdbcTemplate().queryForList("select DESCRIPCION  TIPO from SAM_CAT_TIPOS WHERE TIPO='TIPO_GRUPO'  ");
	}
	
	
}
