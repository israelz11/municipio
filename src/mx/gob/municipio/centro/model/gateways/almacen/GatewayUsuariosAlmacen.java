/**
 * @author LSC. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.List;
import java.util.Map;
import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;

public class GatewayUsuariosAlmacen extends BaseGatewayAlmacen {

	//Constructor
	public GatewayUsuariosAlmacen(){}


		public void inserta( Integer idAlmacen, Integer idUsuario){
			this.getJdbcTemplate().update("insert into USUARIOS_ALMACEN (ID_ALMACEN, ID_USUARIO) " +
					"VALUES (?,?)"
					, new Object[]{idAlmacen,idUsuario});
		}
	
		public void eliminar(Integer idAlmacen,Integer idUsuario  ){
			this.getJdbcTemplate().update("delete from USUARIOS_ALMACEN where ID_ALMACEN= ? and ID_USUARIO=? ", new Object[]{idAlmacen,idUsuario});
		}	
		
		public List<Map> getUsuarioAlmacenes(String unidad, Integer idAlmacen){		
			return this.getJdbcTemplate().queryForList("SELECT     A.ID_ALMACEN, A.ID_USUARIO, B.DESCRIPCION ALMACEN, C.APE_PAT +' '+ C.APE_MAT +' '+ C.NOMBRE USUARIO "+
							" FROM        USUARIOS_ALMACEN A INNER JOIN  ALMACEN B ON A.ID_ALMACEN = B.ID_ALMACEN INNER JOIN "+
							" PERSONAS C ON A.ID_USUARIO = C.CVE_PERS WHERE B.ID_UNIADM=? and A.ID_ALMACEN=?  ",new Object []{unidad,idAlmacen});
		}
		
		public List<Map> getAlmacenesUsuario(Integer idUsuario, String unidad ){		
			return this.getJdbcTemplate().queryForList("SELECT     A.ID_ALMACEN,  B.DESCRIPCION ALMACEN "+
					" FROM        USUARIOS_ALMACEN A INNER JOIN  ALMACEN B ON A.ID_ALMACEN = B.ID_ALMACEN INNER JOIN "+
					" PERSONAS C ON A.ID_USUARIO = C.CVE_PERS WHERE B.ID_UNIADM=? and  a.ID_USUARIO=? ",new Object []{unidad, idUsuario});
		}
		
}
