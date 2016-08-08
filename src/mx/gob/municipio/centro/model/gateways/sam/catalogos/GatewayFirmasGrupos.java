/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam.catalogos;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayFirmasGrupos extends BaseGateway {

	public GatewayFirmasGrupos(){
		
	}

	public  void actualizarPrincipal(Integer clave,String tipo,String cargo,String representante,Integer grupo){
	  if (clave == null) 		  
		  inserta(tipo,cargo,representante,grupo);	  	  
	  else
		  actualizar(clave,tipo,cargo,representante,grupo);
	}

	public void inserta( String tipo,String cargo,String representante,Integer grupo ){
		
		this.getJdbcTemplate().update("insert into SAM_GRUPO_FIRMAS (TIPO,CARGO,REPRESENTANTE,ID_GRUPO_CONFIG ) " +
				"VALUES (?,?,?,?)"
				, new Object[]{tipo,cargo,representante,grupo});
	}

	public void actualizar(Integer clave,String tipo,String cargo,String representante,Integer grupo ){	
		this.getJdbcTemplate().update("update SAM_GRUPO_FIRMAS  set TIPO=?,CARGO=?,REPRESENTANTE=?,ID_GRUPO_CONFIG=?  where ID_FIRMA_GRUPO=? "
				, new Object[]{tipo,cargo,representante,grupo,clave});
	}	

	public List getGruposEstatus(Integer  grupo) {	   
		   return this.getJdbcTemplate().queryForList(" select * from SAM_GRUPO_FIRMAS where ID_GRUPO_CONFIG=? order by TIPO ", new Object[]{grupo});
	}

	public void eliminar(Integer clave  ){
		this.getJdbcTemplate().update("delete from SAM_GRUPO_FIRMAS where ID_FIRMA_GRUPO= ?  ", new Object[]{clave});
	}
	
	
}
