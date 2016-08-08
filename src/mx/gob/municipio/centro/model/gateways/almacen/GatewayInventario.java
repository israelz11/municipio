/**
 * @author LSC. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.dao.DataAccessException;

import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;
import mx.gob.municipio.centro.view.controller.almacen.consultas.ControladorListadoArticulosInventario;

public class GatewayInventario extends BaseGatewayAlmacen {

	//Constructor
	public GatewayInventario(){}
	private static Logger log = Logger.getLogger(GatewayInventario.class.getName());
	
		public Map getArticulo(Long  idInventario ) {	   
			try{
			   return this.getJdbcTemplate().queryForMap("SELECT INVENTARIO.*, "+
											"FAMILIAS.DESCRIPCION AS FAMILIA,  "+
											"SAM_CAT_ARTICULO.DESCRIPCION,  "+
											"CONVERT(varchar(10), INVENTARIO.FECHA,103) AS FECHA,  "+
											"(CASE INVENTARIO.STATUS WHEN 1 THEN 'ACTIVO' WHEN 0 THEN 'CANCELADO' END) AS STATUS_DESC,  "+
											"(CASE INVENTARIO.ALARMA WHEN 1 THEN 'ENCENDIDA' WHEN 0 THEN 'APAGADA' END) AS ALARMA_DESC,  "+
											"CAT_BENEFI.NCOMERCIA, CAT_UNIMED.UNIDMEDIDA AS UNIDAD_MEDIDA  "+
										"FROM INVENTARIO  "+
											"LEFT JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI = INVENTARIO.ID_PROVEEDOR)  "+
											"LEFT JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = INVENTARIO.ID_UNIDAD_MEDIDA)  "+
											"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = INVENTARIO.ID_ARTICULO) "+
											"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO)  "+
											"LEFT JOIN FAMILIAS ON (FAMILIAS.ID_FAMILIA = INVENTARIO.ID_FAMILIA) " +
										"WHERE INVENTARIO.ID_INVENTARIO=?", new Object[]{idInventario});
			}
			catch ( DataAccessException e) {
				log.info(e.getMessage());
				return null;
			}
		}
		
		public List getMovimientosArticulo(Long  idInventario) {
			return this.getJdbcTemplate().queryForList(" select * from MOVIMIENTOS_INVENTARIO where ID_INVENTARIO=?  order by FECHA",new Object []{idInventario});		
		}
		
		public void cancelarArticulo(Long id_inventario){
			this.getJdbcTemplate().update("UPDATE INVENTARIO SET STATUS =? WHERE ID_INVENTARIO = ?", new Object[]{0, id_inventario});
		}
		
		public List<Map> getListadoArticulosInventario(Map parametros){
			String sql = "SELECT INVENTARIO.*, "+
											"FAMILIAS.DESCRIPCION AS FAMILIA,  "+
											"SAM_CAT_ARTICULO.DESCRIPCION,  "+
											"CONVERT(varchar(10), INVENTARIO.FECHA,103) AS FECHA,  "+
											"(CASE INVENTARIO.STATUS WHEN 1 THEN 'ACTIVO' WHEN 0 THEN 'CANCELADO' END) AS STATUS_DESC,  "+
											"(CASE INVENTARIO.ALARMA WHEN 1 THEN 'ENCENDIDA' WHEN 0 THEN 'APAGADA' END) AS ALARMA_DESC,  "+
											"CAT_BENEFI.NCOMERCIA, CAT_UNIMED.UNIDMEDIDA AS UNIDAD_MEDIDA  "+
										"FROM INVENTARIO  "+
											"LEFT JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI = INVENTARIO.ID_PROVEEDOR)  "+
											"LEFT JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = INVENTARIO.ID_UNIDAD_MEDIDA)  "+
											"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = INVENTARIO.ID_ARTICULO) "+
											"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO)  "+
											"LEFT JOIN FAMILIAS ON (FAMILIAS.ID_FAMILIA = INVENTARIO.ID_FAMILIA) ";
			 String clausulas = "WHERE INVENTARIO.STATUS IN (0,1)";
			 //if(!parametros.get("cbodependencia").toString().equals("0")) 
				 	clausulas += " AND INVENTARIO.ID_DEPENDENCIA =:cbodependencia";
			if(!parametros.get("id_almacen").toString().equals("0")) clausulas += " AND INVENTARIO.ID_ALMACEN =:id_almacen";
			if(!parametros.get("id_familia").toString().equals("0")) clausulas += " AND INVENTARIO.ID_FAMILIA =:id_familia";
			if(!parametros.get("id_proveedor").toString().equals("0")) clausulas += " AND INVENTARIO.ID_PROVEEDOR =:id_proveedor";
			if(!parametros.get("id_unidad_medida").toString().equals("0")) clausulas += " AND INVENTARIO.ID_UNIDAD_MEDIDA =:id_unidad_medida";
			if(!parametros.get("descripcion").toString().equals("")) clausulas += " AND SAM_CAT_ARTICULO.DESCRIPCION LIKE '%"+parametros.get("descripcion").toString()+"%'";
			if(!parametros.get("folio").toString().equals("")) clausulas += " AND INVENTARIO.FOLIO =:folio";
			return this.getNamedJdbcTemplate().queryForList(sql+clausulas, parametros);
		}
		
		
		
				
							
}
