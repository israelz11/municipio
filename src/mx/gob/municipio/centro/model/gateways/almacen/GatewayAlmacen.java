/**
 * @author LSC. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.Date;
import java.util.List;
import java.util.Map;
import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;

public class GatewayAlmacen extends BaseGatewayAlmacen {

	//Constructor
	public GatewayAlmacen(){}

	
	public  void actualizarPrincipal(Integer clave,String unidad,String descripcion,String estatus,Long responsable, String alias, String alarma,String email ){
		  if (clave == null) 		  
			  inserta(unidad,descripcion,estatus,responsable, alias, alarma,email);	  	  
		  else
			  actualizar(clave,descripcion,estatus,responsable, alias, alarma,email);
		}

		public void inserta( String unidad,String descripcion,String estatus,Long responsable, String alias, String alarma,String email  ){
			this.getJdbcTemplate().update("insert into ALMACEN (ID_UNIADM, ID_RESPONSABLE, ALIAS, DESCRIPCION, EMAIL, ALARMAS, ESTATUS) " +
					"VALUES (?,?,?,?,?,?,?)"
					, new Object[]{unidad,responsable, alias, descripcion,email,alarma,estatus});
		}

		public void actualizar(Integer clave, String descripcion,String estatus,Long responsable, String alias, String alarma,String email ){	
			this.getJdbcTemplate().update("update ALMACEN  set ID_RESPONSABLE=?,DESCRIPCION=?,EMAIL=?, ALARMAS=?, ESTATUS=?, ALIAS=?    where ID_ALMACEN=? "
					, new Object[]{responsable,descripcion,email,alarma,estatus, alias, clave });
		}	

		public List getAlmacenes(String  unidad ) {	 
			String clausula = (unidad.equals("")) ? "where a.ESTATUS='ACTIVO'": "where a.ID_UNIADM="+unidad;
			   return this.getJdbcTemplate().queryForList(" SELECT     a.ID_ALMACEN, a.ID_UNIADM, a.ID_RESPONSABLE, a.ALIAS, a.FECHA, a.DESCRIPCION, a.EMAIL, a.ALARMAS, a.ESTATUS, b.APE_PAT + ' ' + b.APE_MAT + ' ' + b.NOMBRE AS RESPONSABLE "+
					   " FROM         dbo.ALMACEN AS a INNER JOIN  dbo.PERSONAS AS b ON a.ID_RESPONSABLE = b.CVE_PERS "+clausula);
		}
		
		public String getAlmacen(Integer id){
			if(id==0) 
				return "";
			else 
				return (String) this.getJdbcTemplate().queryForObject("SELECT DESCRIPCION FROM ALMACEN WHERE ID_ALMACEN = ?", new Object[]{id}, String.class);
		}
		
		public List getAlmacenesActivos(int  unidad ) {	   
			   return this.getJdbcTemplate().queryForList(" select ID_ALMACEN ,ID_UNIADM,ID_RESPONSABLE,FECHA,DESCRIPCION,EMAIL,ALARMAS,ESTATUS  from ALMACEN where ESTATUS='ACTIVO' ORDER BY DESCRIPCION ASC");
		}
		
		public List getAlmacenesUnidad(Long  idDependencia ) {	   
			   return this.getJdbcTemplate().queryForList(" select ID_ALMACEN ,ID_UNIADM,ID_RESPONSABLE,FECHA,DESCRIPCION,EMAIL,ALARMAS,ESTATUS  from ALMACEN where ESTATUS='ACTIVO' AND ID_UNIADM =? ORDER BY DESCRIPCION ASC", new Object[]{idDependencia});
		}

		public void eliminar(Integer clave  ){
			this.getJdbcTemplate().update("delete from ALMACEN where ID_ALMACEN= ?  ", new Object[]{clave});
		}
		
		public List<Map> getListaLibroAlmacen(){
			return this.getJdbcTemplate().queryForList("SELECT A.*, SUBSTRING(A.CLV_PARTID,1,2) AS CLV, "+
																	"((SELECT ISNULL(SUM(CANTIDAD),0) FROM DETALLES_ENTRADAS "+ 
																	"		INNER JOIN ENTRADAS AS E ON (E.ID_ENTRADA = DETALLES_ENTRADAS.ID_ENTRADA) "+ 
																	"		WHERE DETALLES_ENTRADAS.ID_ENTRADA IN (SELECT ENTRADAS.ID_ENTRADA FROM ENTRADAS) "+
																	"		AND DETALLES_ENTRADAS.STATUS=1 "+ 
																	"		AND SUBSTRING(E.PARTIDA,1,2) = SUBSTRING(A.CLV_PARTID,1,2) "+
																	"	) "+
																	"	-" +
																	"(SELECT ISNULL(SUM(DT.CANTIDAD),0) FROM DETALLE_SALIDA  AS DT "+
																	"		INNER JOIN SALIDAS AS S ON (S.ID_SALIDA = DT.ID_SALIDA AND S.STATUS=1) "+
																	"		INNER JOIN ENTRADAS AS E ON (E.ID_ENTRADA = S.ID_ENTRADA) "+
																	"		INNER JOIN DETALLES_ENTRADAS AS DE ON (DE.ID_DETALLE_ENTRADA = DT.ID_DETALLE_ENTRADA) "+ 
																	" WHERE S.STATUS=1 "+
																	"		AND SUBSTRING(E.PARTIDA,1,2) = SUBSTRING(A.CLV_PARTID,1,2)) "+
																	") AS CANTIDAD_INV, "+
																	"'Pzs' AS UNIDAD, "+
																	"(0.00) AS COSTO_UNIT, "+
																	"(0.00) AS MONTO "+
																"FROM SUBCUENTAS_ALMACEN AS A");
		}
				
}
