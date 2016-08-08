/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayMovimientosRequisicion extends BaseGateway {

		@Autowired
		public GatewayBitacora gatewayBitacora;
		
		public GatewayMovimientosRequisicion(){}
		
		/*Metodo para Insertar o actualizar un concepto de Requisicion*/
		public boolean actualizarConcepto(Long cve_req, int tipo, Long id_req_movto, int consecutivo,  int id_articulo, String cve_unidad_med, String producto, Double precio_est, Double cantidad, String descripcion, int cve_pers){
			Long id =0L;
			if(id_req_movto==0){
				Long consec = this.getJdbcTemplate().queryForLong("select max(REQ_CONS) as n from SAM_REQ_MOVTOS where cve_req = ?", new Object[]{cve_req});
				id = guardar_Concepto(cve_req, consec, id_articulo, cve_unidad_med, producto, precio_est, cantidad, descripcion, cve_pers);
				actualizarTotalesConceptosOT(cve_req, tipo);
				if(id>consec) return true; else return false;
			}
			else
			{
				actualizar_concepto(cve_req, id_req_movto, consecutivo, id_articulo, cve_unidad_med, producto, precio_est, cantidad, descripcion, cve_pers);
				actualizarTotalesConceptosOT(cve_req, tipo);
				/*Guarda en bitacora*/
				this._guardarEnBitacoraMov(cve_req, gatewayBitacora.ACTUALIZO_CONCEPTO, cve_pers, (cantidad*precio_est),  "Lote:"+consecutivo+" Cantidad: "+cantidad+" Precio Unit: "+precio_est+" Id_Art: "+id_articulo+" Clv_Unimed: "+cve_unidad_med+" Desc: "+descripcion);
				return true;
			}
		}

		
		/*Metodo para guardar un nuevo concepto*/
		public Long guardar_Concepto(Long cve_req, Long consec, int id_articulo, String cve_unidad_med, String producto, Double precio_est, Double cantidad, String descripcion, int cve_pers){
			/*Insertar el concepto o movimiento*/
	    	String SQL = "INSERT INTO SAM_REQ_MOVTOS(CVE_REQ, REQ_CONS, CLV_UNIMED, ID_ARTICULO, CANTIDAD, PRECIO_EST, NOTAS, STATUS, COMPROMETIDO) VALUES(?,?,?,?,?,?,?,?,?) ";
			this.getJdbcTemplate().update(SQL, new Object [] {cve_req, (consec+1),cve_unidad_med, id_articulo, cantidad, precio_est, descripcion, GatewayRequisicion.REQ_STATUS_NUEVO, 0});
			/*Guarda en bitacora*/
			this._guardarEnBitacoraMov(cve_req, gatewayBitacora.AGREGAR_MOV_REQ, cve_pers, (cantidad*precio_est), "Lote:"+(consec+1)+" Cantidad: "+cantidad+" Precio Unit: "+precio_est+" Id_Art: "+id_articulo+" Clv_Unimed: "+cve_unidad_med+" Desc: "+descripcion);
			return (consec+1);
		}
		
		/*Metodo para actualizar el concepto de la requisicion*/
		private boolean actualizar_concepto(Long cve_req, Long id_req_movto, int consec, int id_articulo, String cve_unidad_med, String producto, Double precio_est, Double cantidad, String descripcion, int cve_pers){
			try  
			{
				Map lotes = this.getConcepto(id_req_movto);
				this.getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET ID_ARTICULO = ?, CLV_UNIMED = ?, CANTIDAD = ?, PRECIO_EST = ?, NOTAS = ? WHERE ID_REQ_MOVTO = ?", new Object[]{id_articulo, cve_unidad_med, cantidad, precio_est, descripcion, id_req_movto});
				this.getJdbcTemplate().update("UPDATE SAM_PED_MOVTOS SET CANTIDAD = ?, PRECIO_UNI = ?, PED_CONS = ?, DESCRIP=? WHERE ID_REQ_MOVTO = ?", new Object[]{cantidad, precio_est, lotes.get("REQ_CONS"), descripcion, id_req_movto});
				/*Guardar en bitacora*/
				//this._guardarEnBitacoraMov(cve_req, gatewayBitacora.ACTUALIZO_CONCEPTO, cve_pers,  "Lote:"+consec+" Cantidad: "+cantidad+" Precio Unit: "+precio_est);
				return true;
			}
			catch ( DataAccessException e) {
				return false;
			}
		}
		
		/*Metodo para guardar un concepto existente*/
		private void actualizarTotalesConceptosOT(Long cve_req, int tipo_req){
			/*Verificar el tipo de la requisicion y actualizar total de conceptos en OT/OS*/
			if(tipo_req==2||tipo_req==3) this.getJdbcTemplate().update("UPDATE SAM_ORDEN_TRAB SET COSTO_TOTAL = (SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_ORDEN_TRAB.CVE_REQ ) FROM SAM_ORDEN_TRAB WHERE CVE_REQ = ?", new Object [] {cve_req});
		}
		
		/*Metodo para obtener todos los conceptos de una requisicion*/
		public List <Map> getConceptos(Long cve_req){
			return this.getJdbcTemplate().queryForList("SELECT SAM_REQ_MOVTOS.ID_REQ_MOVTO, SAM_REQ_MOVTOS.CVE_REQ, REQ_CONS, CANTIDAD_TEMP, CANTIDAD, NOTAS, SAM_REQ_MOVTOS.STATUS, SAM_CAT_ARTICULO.DESCRIPCION, SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, CAT_UNIMED.UNIDMEDIDA AS UNIDAD, PRECIO_EST, (CANTIDAD*PRECIO_EST) AS IMPORTE, isnull(SAM_REQ_ANEXO.TEXTO, '') AS TEXTO  FROM SAM_REQ_MOVTOS " + 
															"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = SAM_REQ_MOVTOS.ID_ARTICULO)"+
															"LEFT JOIN SAM_REQ_ANEXO ON (SAM_REQ_ANEXO.ID_REQ_MOVTO = SAM_REQ_MOVTOS.ID_REQ_MOVTO) "+
															"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO)"+ 
															"INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = SAM_REQ_MOVTOS.CLV_UNIMED) where SAM_REQ_MOVTOS.CVE_REQ=? ORDER BY REQ_CONS ASC", new Object [] {cve_req});
		}
		
		/*Metodo para obtener todos los conceptos de una requisicion normal y validar si existen conceptos que pueden mostrarse en el pedido*/
		public List <Map> getConceptos2(Long cve_req){
			return this.getJdbcTemplate().queryForList("SELECT ID_REQ_MOVTO, SAM_REQUISIC.CVE_REQ, REQ_CONS, CANTIDAD_TEMP, CANTIDAD, NOTAS, SAM_REQ_MOVTOS.STATUS, SAM_CAT_ARTICULO.DESCRIPCION, SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, CAT_UNIMED.UNIDMEDIDA, PRECIO_EST, (CANTIDAD*PRECIO_EST) AS IMPORTE  FROM SAM_REQ_MOVTOS " + 
															"INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_REQ_MOVTOS.CVE_REQ) "+
															"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = SAM_REQ_MOVTOS.ID_ARTICULO) "+
															"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO) "+ 
															"INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = SAM_REQ_MOVTOS.CLV_UNIMED) where SAM_REQ_MOVTOS.CANTIDAD > 0 AND SAM_REQ_MOVTOS.CVE_REQ=? AND SAM_REQ_MOVTOS.STATUS < 2 AND 0 = (SELECT COUNT(*) FROM SAM_PED_MOVTOS AS PM WHERE PM.ID_REQ_MOVTO = SAM_REQ_MOVTOS.ID_REQ_MOVTO) /*AND ID_REQ_MOVTO NOT IN (SELECT ID_REQ_MOVTO FROM SAM_PED_MOVTOS)*/ ORDER BY REQ_CONS ASC", new Object [] {cve_req});
		}
		
		/*Metodo para obtener todos los conceptos de una requisicion Calendarizada y validar si existen conceptos que pueden mostrarse en el pedido*/
		public List <Map> getConceptos3(Long cve_req){
			return this.getJdbcTemplate().queryForList("SELECT ID_REQ_MOVTO, SAM_REQUISIC.CVE_REQ, REQ_CONS, CANTIDAD_TEMP, CANTIDAD, NOTAS, SAM_REQ_MOVTOS.STATUS, SAM_CAT_ARTICULO.DESCRIPCION, SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, CAT_UNIMED.UNIDMEDIDA, PRECIO_EST, (CANTIDAD*PRECIO_EST) AS IMPORTE  FROM SAM_REQ_MOVTOS " + 
															"INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_REQ_MOVTOS.CVE_REQ) "+
															"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = SAM_REQ_MOVTOS.ID_ARTICULO) "+
															"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO) "+ 
															"INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = SAM_REQ_MOVTOS.CLV_UNIMED) where SAM_REQ_MOVTOS.CANTIDAD > 0 AND SAM_REQ_MOVTOS.CVE_REQ=? AND SAM_REQ_MOVTOS.STATUS < 2 ORDER BY REQ_CONS ASC", new Object [] {cve_req});
		}
		
		/*Metodo para obtener todos los conceptos de una requisicion siempre y cuando esten en edicion*/
		public List <Map> getConceptos4(Long cve_req){
			return this.getJdbcTemplate().queryForList("SELECT SAM_REQ_MOVTOS.ID_REQ_MOVTO, ISNULL(ID_PED_MOVTO,0) AS ID_PED_MOVTO, CVE_REQ, REQ_CONS, CANTIDAD_TEMP, SAM_REQ_MOVTOS.CANTIDAD, NOTAS, SAM_REQ_MOVTOS.STATUS, SAM_CAT_ARTICULO.DESCRIPCION, SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, CAT_UNIMED.UNIDMEDIDA AS UNIDAD, PRECIO_EST, (SAM_REQ_MOVTOS.CANTIDAD*PRECIO_EST) AS IMPORTE, isnull(SAM_REQ_ANEXO.TEXTO, '') AS TEXTO  FROM SAM_REQ_MOVTOS " +
															"LEFT JOIN SAM_PED_MOVTOS ON (SAM_PED_MOVTOS.ID_REQ_MOVTO = SAM_REQ_MOVTOS.ID_REQ_MOVTO) " +
															"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = SAM_REQ_MOVTOS.ID_ARTICULO)"+
															"LEFT JOIN SAM_REQ_ANEXO ON (SAM_REQ_ANEXO.ID_REQ_MOVTO = SAM_REQ_MOVTOS.ID_REQ_MOVTO) "+
															"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO)"+ 
															"INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = SAM_REQ_MOVTOS.CLV_UNIMED) where SAM_REQ_MOVTOS.STATUS IN (0,1) AND SAM_REQ_MOVTOS.CVE_REQ=? ORDER BY REQ_CONS ASC", new Object [] {cve_req});
		}
		

		/*Metodo para obtener el concepto de una requisicion*/
		public Map getConcepto(Long id_req_movto){
			try  
			{
				return this.getJdbcTemplate().queryForMap("SELECT  C.CVE_REQ,  C.REQ_CONS, C.NOTAS, B.UNIDMEDIDA AS UNIDAD, C.ID_ARTICULO, C.PRECIO_EST, C.CANTIDAD, D.GRUPO, "+ 
                        " D.SUBGRUPO, A.CLV_UNIMED , D.ID_CAT_ARTICULO, A.DESCRIPCION PRODUCTO "+
						" FROM         SAM_CAT_ARTICULO A INNER JOIN  CAT_UNIMED B ON A.CLV_UNIMED = B.CLV_UNIMED INNER JOIN "+
						" SAM_REQ_MOVTOS C INNER JOIN "+
						" SAM_CAT_PROD D ON D.ID_ARTICULO = C.ID_ARTICULO ON "+ 
						" A.ID_CAT_ARTICULO = D.ID_CAT_ARTICULO  where C.ID_REQ_MOVTO = ? ", new Object [] {id_req_movto});
			}
			catch ( DataAccessException e) {
				return null;
			}
		}

		/*Metodo para  eliminar los conceptos de una requisicion*/
		public void eliminarMovimientoRequisicion(Long id_req_movto, Long cve_req, int cve_pers){
			/*Buscar el lote*/
			Map Lote = this.getConcepto(id_req_movto);
			Integer cont =Integer.parseInt(Lote.get("REQ_CONS").toString());
			List <Map> conceptos = this.getJdbcTemplate().queryForList("SELECT *FROM SAM_REQ_MOVTOS WHERE ID_REQ_MOVTO  > ? AND CVE_REQ = ? ORDER BY REQ_CONS ASC", new Object[]{id_req_movto, cve_req});
			
			//tambien eliminar del pedido
			this.getJdbcTemplate().update("DELETE FROM SAM_PED_MOVTOS WHERE ID_REQ_MOVTO = ?", new Object[]{id_req_movto});
			/*Eliminar concepto de la requisicion*/
			this.getJdbcTemplate().update("DELETE FROM SAM_REQ_ANEXO WHERE ID_REQ_MOVTO = ?", new Object []{id_req_movto});
			this.getJdbcTemplate().update("DELETE FROM SAM_REQ_MOVTOS WHERE ID_REQ_MOVTO = ?", new Object []{id_req_movto});
			/*Reenumerar los lotes a partir del ultimo que existe hacia adelante*/
			for(Map row: conceptos){
				this.getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET REQ_CONS = ? WHERE ID_REQ_MOVTO = ?", new Object[]{cont, row.get("ID_REQ_MOVTO")});
				this.getJdbcTemplate().update("UPDATE SAM_PED_MOVTOS SET PED_CONS = ? WHERE ID_REQ_MOVTO = ?", new Object[]{cont, row.get("ID_REQ_MOVTO")});
				cont++;
			}
			/*Actualizar Orden de Trabajo / Servicio*/
			actualizarTotalesConceptosOT(cve_req, 2);
			//this.getJdbcTemplate().update("DELETE FROM REQ_MOVTOS WHERE ID_REQ_MOVTO = ?", new Object []{id_req_movto});
			/*Guardar en bitacora*/
			this._guardarEnBitacoraMov(cve_req, gatewayBitacora.ELIMINAR_MOV_REQ, cve_pers, (Double.parseDouble(Lote.get("CANTIDAD").toString())*Double.parseDouble(Lote.get("PRECIO_EST").toString())), "ID Lote: "+id_req_movto+" Lote: "+Lote.get("REQ_CONS").toString()+" Cantidad: "+ Lote.get("CANTIDAD").toString()+" Precio Unit: "+Lote.get("PRECIO_EST").toString()+ " Id_Art: "+ Lote.get("ID_ARTICULO").toString()+ " Clv_Unimed: "+ Lote.get("CLV_UNIMED").toString() +  " Desc: "+Lote.get("NOTAS").toString());
		}
		
		private void _guardarEnBitacoraMov(Long cve_req, int tipo_mov, int cve_pers, Double monto, String descrip){
			//Guardar en bitacora
			Date fecha = new Date();
			Map requisicion = this.getJdbcTemplate().queryForMap("SELECT NUM_REQ, ID_PROYECTO, CLV_PARTID, PERIODO, FECHA, TIPO, STATUS, EJERCICIO, ISNULL((SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ ),0.00) AS IMPORTE FROM SAM_REQUISIC WHERE CVE_REQ = ? ", new Object []{cve_req});
			//gatewayBitacora.guardarBitacora(tipo_mov, Integer.parseInt(requisicion.get("EJERCICIO").toString()), cve_pers, cve_req, requisicion.get("NUM_REQ").toString(), "REQ", null, requisicion.get("PROYECTO").toString(), requisicion.get("CLV_PARTID").toString(), descrip, monto);

			
		}
		
}
		
