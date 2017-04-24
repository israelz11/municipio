/**
 * @author lsc. Mauricio Hernandez Leon, ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.controller.sam.pedidos.StoreProcedurePedidos;
import mx.gob.municipio.centro.view.seguridad.Sesion;

public class GatewayPedidos extends BaseGateway {
	private static Logger log = Logger.getLogger(GatewayPedidos.class.getName());
	public static int PED_STATUS_NUEVO = 0;
	public static int PED_STATUS_PENDIENTE = 1;
	public static int PED_STATUS_EN_REVISION = 2;
	public static int PED_STATUS_CANCELADO = 3;
	public static int PED_STATUS_PEDIDO = 4;
	public static int PED_STATUS_SURTIDO = 5;
	public static int PED_STATUS_SURTIDO_PARCIAL = 5;
	
	public static int PED_MOVTO_EDICION = 0;
	public static int PED_MOVTO_PENDIENTE = 1;
	public static int PED_MOVTO_REVISION = 2;
	public static int PED_MOVTO_CANCELADO = 3;
	public static int PED_MOVTO_PEDIDO = 4;
	public static int PED_MOVTO_SURTIDO = 5;
	public static int PED_MOVTO_SURTIDO_PARCIAL = 6;
	
	public static String PED_ERROR_SUFICIENCIA1 = "No se puede cerrar el pedido por que no existe suficiencia en el Proyecto y Partida del periodo actual";
	public static String PED_ERROR_SUFICIENCIA2 = "No se puede cerrar el pedido Calendarizado por que no existe suficiencia en el Proyecto y Partida del periodo establecido";
	String res;
	@Autowired
	public GatewayBitacora gatewayBitacora;
	@Autowired
	public GatewayProyectoPartidas gatewayProyectoPartidas;
	@Autowired
	private GatewayRequisicion gatewayRequisicion;
	@Autowired
	private GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
	@Autowired
	private GatewayMeses gatewayMeses;
	/*Constructor*/
	public GatewayPedidos() {}
	
	public Map guardarEditarPedidos(Long cve_ped, Long cve_req, Date fecha, String contrato, int cve_concurso, String fecha_entrega, String cve_beneficiario, int cve_pers, String condicion_pago, String lugar_entrega, String notas, List<Long> id_req_movtos, List<Double> cantidades, List<String> conceptos, List<Double> precios_unit, Double iva, int tipo_iva, Double descuento, int ejercicio, int id_grupo){
		Map row = new HashMap();
		if(cve_ped==null){
			/*Guarda datos del nuevo pedido*/
			cve_ped = this.guardarPedido(cve_ped, cve_req, fecha, contrato, cve_concurso, fecha_entrega, cve_beneficiario, cve_pers, condicion_pago, lugar_entrega, notas, id_req_movtos, cantidades, conceptos, precios_unit, iva, tipo_iva, descuento, ejercicio, id_grupo);
			row.put("EVENT",true);
		}
		else{
			/*Actualiza un pedido existente*/
			
			row.put("EVENT", this.editarPedido(cve_ped, fecha, contrato, cve_concurso, fecha_entrega, cve_beneficiario, cve_pers, condicion_pago, lugar_entrega, notas, id_req_movtos, cantidades, conceptos, precios_unit, iva, tipo_iva, descuento, ejercicio, id_grupo));
		}
		row.put("CVE_PED", cve_ped);
		row.put("NUM_PED", this.rellenarCeros(cve_ped.toString(), 6));
		return row;
	}
	
	/*Metodo usado para guardar un pedido existente*/
	public boolean editarPedido(Long cve_ped, Date fecha_ped, String contrato, int cve_concurso, String fecha_entrega, String cve_beneficiario, int cve_pers, String condicion_pago, String lugar_entrega, String notas, List<Long> id_ped_movtos, List<Double> cantidades, List<String> conceptos, List<Double> precios_unit, Double iva, int tipo_iva, Double descuento, int ejercicio, int id_grupo){
		int i=0;
		double subtotal = 0L;
		double total = 0L;
		Date fecha_cap = new Date();
		/*recorrer el arreglo de ids de los movs apara calcular los totales*/
		for(Long val : id_ped_movtos){
			subtotal+= (cantidades.get(i)*precios_unit.get(i));
			i++;
		}
		//Aplicar el descuento
		if(descuento>0) subtotal = subtotal - descuento;
		
		subtotal = redondea(subtotal,2);
		
		//calculo del iva
		if(iva!=0) total = redondea((subtotal +iva),2); else total = redondea(subtotal,2);
		
		//cve_ped = getNumeroPedido(ejercicio)+1;
		String num_ped = this.rellenarCeros(cve_ped.toString(), 6);
		/*Actualiza el pedido*/
		String SQL = "UPDATE SAM_PEDIDOS_EX SET CLV_BENEFI = ?, CONTRATO=?, ENTREGA=?, CONDICION_PAGO=?, NOTAS=?, CVE_CONCURSO=?, SUBTOTAL=?, IVA=?, TIPO_IVA=?, DESCUENTO=?, TOTAL=?, FECHA_PED=?, FECHA_ENTREGA = ?, STATUS=?, COMPROMETE=?, EJERCE=? WHERE CVE_PED = ?";
		this.getJdbcTemplate().update(SQL, new Object[]{cve_beneficiario, contrato, lugar_entrega, condicion_pago, notas, cve_concurso, subtotal, iva, tipo_iva, descuento, total, fecha_ped, fecha_entrega, this.PED_STATUS_NUEVO, total, 0 , cve_ped});
		/*Guardar el movimiento en bitacora*/
		Map ped = this.getJdbcTemplate().queryForMap("SELECT NUM_PED, FECHA_PED, TOTAL FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{cve_ped});
		gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_PEDIDO, ejercicio, cve_pers, cve_ped, num_ped, "PED", (Date) ped.get("FECHA_PED"), null, null, null, total);
		/*Actualiza los movimientos*/
		this.editaMovimientosPedido(cve_ped, id_ped_movtos, cantidades, conceptos, precios_unit, ejercicio, cve_pers);
		return true;
	}
	
	/*Metodo usado para guardar un nuevo pedido*/
	public Long guardarPedido(Long cve_ped, Long cve_req, Date fecha, String contrato, int cve_concurso, String fecha_entrega, String cve_beneficiario, int cve_pers, String condicion_pago, String lugar_entrega, String notas, List<Long> id_req_movtos, List<Double> cantidades, List<String> conceptos, List<Double> precios_unit, Double iva, int tipo_iva, Double descuento, int ejercicio, int id_grupo){
		int i=0;
		double subtotal = 0L;
		double total = 0L;
		Date fecha_cap = new Date();
		/*recorrer el arreglo de ids de los movs apara calcular los totales*/
		for(Long val : id_req_movtos){
			subtotal+= (cantidades.get(i)*precios_unit.get(i));
			i++;
		}
		//Aplicar el descuento
		if(descuento>0) subtotal = subtotal - descuento;
		
		subtotal = redondea(subtotal,2);
		
		//calculo del iva
		if(iva!=0) total = redondea((subtotal +iva),2); else total = redondea(subtotal,2);
		
		/*guarda los datos*/
		String SQL = "INSERT INTO SAM_PEDIDOS_EX (EJERCICIO, CLV_BENEFI, CVE_PERS, SUBTOTAL, IVA, TIPO_IVA, DESCUENTO, TOTAL, CONTRATO, ENTREGA, NOTAS, FECHA_CRE, FECHA_PED, STATUS, COMPROMETE, EJERCE, CVE_CONCURSO, FECHA_ENTREGA, CONDICION_PAGO, ID_GRUPO, CVE_REQ ) " +
					 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(SQL, new Object[]{ ejercicio, cve_beneficiario, cve_pers, subtotal, iva, tipo_iva, descuento, total, contrato, lugar_entrega, notas, fecha_cap, fecha, this.PED_STATUS_NUEVO, 0, 0, cve_concurso, fecha_entrega, condicion_pago, id_grupo,cve_req});
		
		cve_ped = getNumeroPedido(ejercicio);
		String num_ped = this.rellenarCeros(cve_ped.toString(), 6);	
		
		
		/*Finiquitar aqui la requisicion*/
		this.getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS =? WHERE CVE_REQ =?", new Object []{this.gatewayRequisicion.REQ_STATUS_EN_PROCESO, cve_req});
		/*Guarda los movimientos del pedido de la requisicion*/
		this.guardarMovimientosPedido(cve_ped, cve_req, id_req_movtos, cantidades, conceptos, precios_unit, ejercicio, cve_pers);
		/*Guardar el movimiento en bitacora*/
		Map req = this.getJdbcTemplate().queryForMap("SELECT ID_PROYECTO, CLV_PARTID FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{cve_req});
		String num_req = (String) this.getJdbcTemplate().queryForObject("SELECT NUM_REQ FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{cve_req}, String.class);
		gatewayBitacora.guardarBitacora(gatewayBitacora.PEDIDO_NUEVO, ejercicio, cve_pers, cve_ped, num_ped, "PED", fecha, req.get("ID_PROYECTO").toString(), req.get("CLV_PARTID").toString(), "CVE_REQ: "+ cve_req +" NUM_REQ: "+num_req, total);
		return cve_ped;
	}
	
	/*metodo para guardar los movimientos del pedido*/
	public void guardarMovimientosPedido(Long cve_ped, Long cve_req, List<Long> id_req_movtos, List<Double> cantidades, List<String> conceptos, List<Double> precios_unit, int ejercicio, int cve_pers){
		int i=0;
		Date fecha_cap = new Date();
		String SQL = "";
		/*recorrer todos los elementos para guardarlos*/
		for(Long id : id_req_movtos){
			/*Insertar el elemento al pedido*/
			SQL = "INSERT INTO SAM_PED_MOVTOS(CVE_PED, PED_CONS, ID_REQ_MOVTO, DESCRIP, CANTIDAD, PRECIO_UNI, STATUS) VALUES (?,?,?,?,?,?,?)";
			this.getJdbcTemplate().update(SQL, new Object []{cve_ped, (i+1), id, conceptos.get(i).trim(),cantidades.get(i),precios_unit.get(i), this.PED_STATUS_NUEVO} );
			/*Cambiar tambien el status de los elementos de la requisicion*/
			gatewayRequisicion.cambiaStatusMovimientoRequisicion(cve_req, id, gatewayRequisicion.REQ_MOVTO_SOLICITADO);
			/*Guardar en bitacora*/
			gatewayBitacora.guardarBitacora(gatewayBitacora.AGREGO_MOV_PEDIDO, ejercicio, cve_pers, cve_ped, this.rellenarCeros(cve_ped.toString(), 6), "PED", fecha_cap, null, null, "Consec: " + (i+1) + " Cantidad: "+cantidades.get(i)+" Precio_Unit: "+precios_unit.get(i)+" Descripcion: "+conceptos.get(i).trim(), (Double) cantidades.get(i)* (Double) precios_unit.get(i));
			i++;
		}
		/*Guarda la bitacora del movimiento*/
		//gatewayBitacora.guardarBitacora(this.rellenarCeros(cve_ped.toString(), 6), cve_ped, "PED", fecha_cap, 0D, "N", "", "", "", ejercicio, cve_pers, gatewayBitacora.AGREGO_MOV_PEDIDO);
	}
	
	/*Metodo para actualizar los conceptos del pedido*/
	public void editaMovimientosPedido(Long cve_ped, List<Long> id_ped_movtos, List<Double> cantidades, List<String> conceptos, List<Double> precios_unit, int ejercicio, int cve_pers){
		int i=0;
		Date fecha_cap = new Date();
		String SQL = "";
		/*recorrer todos los elementos para guardarlos*/
		for(Long id : id_ped_movtos){
			/*Insertar el elemento al pedido*/
			SQL = "UPDATE SAM_PED_MOVTOS SET DESCRIP = ?, CANTIDAD=?, PRECIO_UNI=?, PED_CONS=? WHERE ID_PED_MOVTO = ?";
			this.getJdbcTemplate().update(SQL, new Object []{conceptos.get(i).trim(),cantidades.get(i),precios_unit.get(i), (i+1), id_ped_movtos.get(i)});
			/*Guardar en bitacora*/
			//gatewayBitacora.guardarBitacora(gatewayBitacora.ACTUALIZO_MOV_PED, ejercicio, cve_pers, cve_ped, this.rellenarCeros(cve_ped.toString(), 6), "PED", fecha_cap, null, null, "ID: " + id + " Cantidad: "+cantidades.get(i)+" Precio_Unit: "+precios_unit.get(i)+" Descripcion: "+conceptos.get(i).trim(), (Double) cantidades.get(i)* (Double) precios_unit.get(i));
			i++;
		}
		/*Guarda la bitacora del movimiento*/
		//gatewayBitacora.guardarBitacora(this.rellenarCeros(cve_ped.toString(), 6), cve_ped, "PED", fecha_cap, 0D, "N", "", "", "", ejercicio, cve_pers, gatewayBitacora.AGREGO_MOV_PEDIDO);
	}
	/*Metodo para recuperar los movimientos del pedido*/
	public List <Map> getConceptos(Long cve_ped){
		String sql = "SELECT ID_PED_MOVTO, CVE_PED, PED_CONS, PED_CONS AS REQ_CONS, SAM_PED_MOVTOS.ID_REQ_MOVTO, SAM_REQ_MOVTOS.ID_ARTICULO, SAM_REQ_MOVTOS.CLV_UNIMED, SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, SAM_PED_MOVTOS.DESCRIP, UNIDMEDIDA, SAM_PED_MOVTOS.CANTIDAD, SAM_REQ_MOVTOS.CANTIDAD AS CANTIDAD2, SAM_REQ_MOVTOS.CANTIDAD_TEMP, SAM_PED_MOVTOS.PRECIO_UNI, SAM_PED_MOVTOS.PRECIO_UNI AS PRECIO_ULT, (SAM_PED_MOVTOS.CANTIDAD*SAM_PED_MOVTOS.PRECIO_UNI) AS TOTAL_IM, (SELECT PRECIO_EST FROM SAM_REQ_MOVTOS WHERE ID_REQ_MOVTO = SAM_PED_MOVTOS.ID_REQ_MOVTO) AS PRECIO_EST, SAM_PED_MOVTOS.STATUS "+ 
						"FROM SAM_PED_MOVTOS "+
						"INNER JOIN SAM_REQ_MOVTOS ON (SAM_REQ_MOVTOS.ID_REQ_MOVTO = SAM_PED_MOVTOS.ID_REQ_MOVTO) "+
						"INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = SAM_REQ_MOVTOS.CLV_UNIMED) "+
						"INNER JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = SAM_REQ_MOVTOS.ID_ARTICULO) "+
						"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO) "+
						"WHERE (CVE_PED = ?) ORDER BY PED_CONS ASC ";
		
		return this.getJdbcTemplate().queryForList(sql, new Object[]{cve_ped});
	}
	/*Metodo para bscar el nuevo numero de pedido*/
	private Long getNumeroPedido(int ejercicio){
		return this.getJdbcTemplate().queryForLong("SELECT MAX(CVE_PED) AS NUMERO FROM SAM_PEDIDOS_EX WHERE EJERCICIO=?", new Object []{ejercicio});
	}
	
	/*Metodo para mostrar el listado de pedidos mediante la consulta*/
	public List <Map> getListadoPedidos(String unidad, String  status , Date fInicial, Date fFinal , String cve_benefi, String tipo_gto, Integer ejercicio, Integer idUsuario, String  verUnidad, String cve_uniusr, String numped, String numreq, boolean privilegio, String capitulo, int almacen, String cboconOP){
		Map parametros =  new HashMap<String,Object>();		
		/*Asignacion de parametros*/

		parametros.put("unidad", unidad);
		parametros.put("fechaInicial", fInicial);
		parametros.put("fechaFinal", fFinal);
		parametros.put("ejercicio", ejercicio);
		parametros.put("cve_pers", idUsuario);
		parametros.put("cve_benefi", cve_benefi);
		parametros.put("capitulo", capitulo);
		
		//parametros.put("status", status);
		parametros.put("tipo_gto", tipo_gto);
		String sql = " SELECT     C.CVE_PED, C.NUM_PED, F.NUM_REQ,C.FECHA_FINIQUITADO,C.CVE_PERS, CONVERT(varchar(10), C.FECHA_PED, 103) AS FECHA_PED, C.STATUS, D.DESCRIPCION AS STATUS_DESC, C.TOTAL, E.DEPENDENCIA AS UNIDAD, A.ID, "+
					 "(CASE (SELECT COUNT(*) FROM ENTRADAS WHERE ID_PEDIDO = C.CVE_PED AND ENTRADAS.STATUS=1) WHEN 0 THEN 'NO' ELSE 'SI' END) AS ALMACEN,"+
					 " A.RECURSO, B.N_PROGRAMA, F.CLV_PARTID, F.ID_DEPENDENCIA, F.NUM_REQ, C.CVE_REQ, C.NOTAS "+
					 "FROM    SAM_PEDIDOS_EX AS C "+
						"INNER JOIN  SAM_ESTATUS_PED AS D ON (D.ID_STATUS = C.STATUS) "+
						"INNER JOIN  SAM_REQUISIC AS F ON (F.CVE_REQ = C.CVE_REQ) "+
						"INNER JOIN  CEDULA_TEC AS B ON (B.ID_PROYECTO = F.ID_PROYECTO) "+ 
						"INNER JOIN  CAT_RECURSO AS A ON (A.ID = B.ID_RECURSO) "+
						"INNER JOIN  CAT_DEPENDENCIAS AS E  ON (E.ID = B.ID_DEPENDENCIA) "+
						"INNER JOIN  CAT_PARTID AS CP ON (CP.CLV_PARTID = F.CLV_PARTID) " +
                     " WHERE C.EJERCICIO=:ejercicio AND C.STATUS IN ("+status+") ";	
		
		if (fInicial!=null && fFinal!=null) 
			sql += " AND convert(datetime,convert(varchar(10), C.FECHA_PED ,103),103) between :fechaInicial and :fechaFinal ";
		if(!parametros.get("tipo_gto").equals("")&&!parametros.get("tipo_gto").equals("0")) 
			sql+= " AND B.ID_RECURSO=:tipo_gto ";
		
		if (almacen!=0){
			if(almacen==1)
				sql+= " AND C.CVE_PED IN (SELECT ID_PEDIDO FROM ENTRADAS WHERE ENTRADAS.STATUS=1 AND ENTRADAS.ID_PEDIDO = C.CVE_PED) ";
			else if(almacen==2)
				sql+= " AND C.CVE_PED IN (SELECT ID_PEDIDO FROM ENTRADAS WHERE ENTRADAS.STATUS=1 AND ENTRADAS.ID_PEDIDO = C.CVE_PED) AND ((SELECT ISNULL(SUM(CANTIDAD),0) AS N FROM SAM_PED_MOVTOS WHERE CVE_PED = C.CVE_PED)  - (SELECT ISNULL(SUM(D.CANTIDAD),0) AS N FROM DETALLES_ENTRADAS AS D INNER JOIN ENTRADAS AS E ON (E.ID_ENTRADA = D.ID_ENTRADA) WHERE E.ID_PEDIDO = C.CVE_PED AND E.STATUS=1 AND D.STATUS =1)) = 0 ";
			else
				sql+= " AND C.CVE_PED IN (SELECT ID_PEDIDO FROM ENTRADAS WHERE ENTRADAS.STATUS=1 AND ENTRADAS.ID_PEDIDO = C.CVE_PED) AND ((SELECT ISNULL(SUM(CANTIDAD),0) AS N FROM SAM_PED_MOVTOS WHERE CVE_PED = C.CVE_PED)  - (SELECT ISNULL(SUM(D.CANTIDAD),0) AS N FROM DETALLES_ENTRADAS AS D INNER JOIN ENTRADAS AS E ON (E.ID_ENTRADA = D.ID_ENTRADA) WHERE E.ID_PEDIDO = C.CVE_PED AND E.STATUS=1 AND D.STATUS =1)) > 0 ";
		}
			
		if(cboconOP!=null&&!cboconOP.equals("0")){
			if(cboconOP.equals("1"))
				sql+= " and C.CVE_PED IN (SELECT CVE_PED FROM SAM_OP_COMPROBACIONES WHERE CVE_PED = C.CVE_PED) ";
			if(cboconOP.equals("2"))
				sql+= " and C.CVE_PED NOT IN (SELECT CVE_PED FROM SAM_OP_COMPROBACIONES WHERE CVE_PED = C.CVE_PED) ";
		}
		
		//Comentado por pruebas del listado de pedidos
		/*if (verUnidad==null&&!privilegio)
			sql+= " AND C.CVE_PERS=:cve_pers ";*/
		
		if (verUnidad==null&&!privilegio)
			sql+= " AND (C.CVE_PERS=:cve_pers OR E.ID=:unidad) ";
		
		if(verUnidad!=null&&!privilegio)
			sql+= " AND (C.CVE_PERS=:cve_pers OR E.ID=:unidad) ";
		
		if (verUnidad==null&&privilegio&&unidad.equals("0"))
			sql+= " ";
		if (verUnidad==null&&privilegio&&!unidad.equals("0"))
			sql+= " AND  E.ID=:unidad ";
		if(verUnidad!=null&&privilegio)
			sql+= " AND (C.CVE_PERS=:cve_pers OR E.ID=:unidad) ";
		
			
		if(cve_benefi!=null&&!cve_benefi.equals("")) 
			sql+= " AND C.CLV_BENEFI=:cve_benefi ";
		
		if(capitulo!=null&&!capitulo.equals("0")) 
			sql+= " AND CP.CLV_CAPITU =:capitulo ";
		
		if(numped!=null&&!numped.equals(""))
			sql+= " AND C.NUM_PED LIKE '%"+numped+"%'";
		
		if(numreq!=null&&!numreq.equals(""))
			sql+= " AND F.NUM_REQ LIKE '%"+numreq+"%'";
		
		return this.getNamedJdbcTemplate().queryForList(sql+" ORDER BY C.CVE_PED DESC", parametros);
	}
	
	public List <Map> getListadoPedidos2(String unidad, String  status , Date fInicial, Date fFinal , String cve_benefi, String tipo_gto, Integer ejercicio, Integer idUsuario, String  verUnidad, String cve_uniusr, String numped, String numreq, boolean privilegio, String capitulo, int almacen, String cboconOP){
		Map parametros =  new HashMap<String,Object>();		
		/*Asignacion de parametros*/

		parametros.put("unidad", unidad);
		parametros.put("fechaInicial", fInicial);
		parametros.put("fechaFinal", fFinal);
		parametros.put("ejercicio", ejercicio);
		parametros.put("cve_pers", idUsuario);
		parametros.put("cve_benefi", cve_benefi);
		parametros.put("capitulo", capitulo);
		
		//parametros.put("status", status);
		parametros.put("tipo_gto", tipo_gto);
		String sql = " SELECT     C.CVE_PED, C.NUM_PED, F.NUM_REQ, C.CVE_PERS, CONVERT(varchar(10), C.FECHA_PED, 103) AS FECHA_PED, C.STATUS, D.DESCRIPCION AS STATUS_DESC, C.TOTAL, E.DEPENDENCIA AS UNIDAD, A.ID, OP.NUM_OP, CONVERT(varchar(10), OP.FECHA ,103) AS FECHA_OP, (CASE (OP.TIPO) WHEN 0 THEN 'ADQ.' WHEN 1 THEN 'OBRAS' WHEN 2 THEN 'SERV.' WHEN 4 THEN 'MULT. PROY.' WHEN 5 THEN 'DEVOL.' WHEN 6 THEN 'E. AJENOS' WHEN 7 THEN 'COMPEN.' WHEN 8 THEN 'COMBUST.' WHEN 9 THEN 'F. FIJO' WHEN 10 THEN 'MULT. PED.' WHEN 11 THEN 'VALES' END) AS TIPO_OP,  " +
					"OP.NOTA, OP.IMPORTE AS IMPORTE_OP, SOP.DESCRIPCION_ESTATUS AS STATUS_OP, " +
					 "(CASE (SELECT COUNT(*) FROM ENTRADAS WHERE ID_PEDIDO = C.CVE_PED AND ENTRADAS.STATUS=1) WHEN 0 THEN 'NO' ELSE 'SI' END) AS ALMACEN,"+
					 " A.RECURSO, B.N_PROGRAMA, F.CLV_PARTID, F.ID_DEPENDENCIA, F.NUM_REQ, C.CVE_REQ, C.NOTAS, S.DESCRIPCION as DESCRIPCION_STATUS "+
					 "FROM    SAM_PEDIDOS_EX AS C "+
						"INNER JOIN  SAM_ESTATUS_PED AS D ON (D.ID_STATUS = C.STATUS) "+
						"INNER JOIN  SAM_REQUISIC AS F ON (F.CVE_REQ = C.CVE_REQ) "+
						"INNER JOIN  CEDULA_TEC AS B ON (B.ID_PROYECTO = F.ID_PROYECTO) "+ 
						"INNER JOIN  CAT_RECURSO AS A ON (A.ID = B.ID_RECURSO) "+
						"INNER JOIN  CAT_DEPENDENCIAS AS E  ON (E.ID = B.ID_DEPENDENCIA) "+
						"INNER JOIN  CAT_PARTID AS CP ON (CP.CLV_PARTID = F.CLV_PARTID) " +
						"INNER JOIN SAM_ESTATUS_PED AS S ON (S.ID_STATUS = C.STATUS) "+
						"INNER JOIN SAM_OP_COMPROBACIONES AS COM ON (COM.CVE_PED = C.CVE_PED) " +
						"INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = COM.CVE_OP) " +
						"INNER JOIN SAM_ESTATUS_OP AS SOP ON (SOP.ID_ESTATUS = OP.STATUS) "+
                     " WHERE C.EJERCICIO=:ejercicio AND C.STATUS IN ("+status+") ";	
		
		if (fInicial!=null && fFinal!=null) 
			sql += " AND convert(datetime,convert(varchar(10), C.FECHA_PED ,103),103) between :fechaInicial and :fechaFinal ";
		if(!parametros.get("tipo_gto").equals("")&&!parametros.get("tipo_gto").equals("0")) 
			sql+= " AND B.ID_RECURSO=:tipo_gto ";
		
		if (almacen!=0){
			if(almacen==1)
				sql+= " AND C.CVE_PED IN (SELECT ID_PEDIDO FROM ENTRADAS WHERE ENTRADAS.STATUS=1 AND ENTRADAS.ID_PEDIDO = C.CVE_PED) ";
			else if(almacen==2)
				sql+= " AND C.CVE_PED IN (SELECT ID_PEDIDO FROM ENTRADAS WHERE ENTRADAS.STATUS=1 AND ENTRADAS.ID_PEDIDO = C.CVE_PED) AND ((SELECT ISNULL(SUM(CANTIDAD),0) AS N FROM SAM_PED_MOVTOS WHERE CVE_PED = C.CVE_PED)  - (SELECT ISNULL(SUM(D.CANTIDAD),0) AS N FROM DETALLES_ENTRADAS AS D INNER JOIN ENTRADAS AS E ON (E.ID_ENTRADA = D.ID_ENTRADA) WHERE E.ID_PEDIDO = C.CVE_PED AND E.STATUS=1 AND D.STATUS =1)) = 0 ";
			else
				sql+= " AND C.CVE_PED IN (SELECT ID_PEDIDO FROM ENTRADAS WHERE ENTRADAS.STATUS=1 AND ENTRADAS.ID_PEDIDO = C.CVE_PED) AND ((SELECT ISNULL(SUM(CANTIDAD),0) AS N FROM SAM_PED_MOVTOS WHERE CVE_PED = C.CVE_PED)  - (SELECT ISNULL(SUM(D.CANTIDAD),0) AS N FROM DETALLES_ENTRADAS AS D INNER JOIN ENTRADAS AS E ON (E.ID_ENTRADA = D.ID_ENTRADA) WHERE E.ID_PEDIDO = C.CVE_PED AND E.STATUS=1 AND D.STATUS =1)) > 0 ";
		}
			
		if(cboconOP!=null&&!cboconOP.equals("0")){
			if(cboconOP.equals("1"))
				sql+= " and C.CVE_PED IN (SELECT CVE_PED FROM SAM_OP_COMPROBACIONES WHERE CVE_PED = C.CVE_PED) ";
			if(cboconOP.equals("2"))
				sql+= " and C.CVE_PED NOT IN (SELECT CVE_PED FROM SAM_OP_COMPROBACIONES WHERE CVE_PED = C.CVE_PED) ";
		}
		
		
		if (verUnidad==null&&!privilegio)
			sql+= " AND C.CVE_PERS=:cve_pers ";
		if(verUnidad!=null&&!privilegio)
			sql+= " AND (C.CVE_PERS=:cve_pers OR E.ID=:unidad) ";
		
		if (verUnidad==null&&privilegio&&unidad.equals("0"))
			sql+= " ";
		if (verUnidad==null&&privilegio&&!unidad.equals("0"))
			sql+= " AND  E.ID=:unidad ";
		if(verUnidad!=null&&privilegio)
			sql+= " AND (C.CVE_PERS=:cve_pers OR E.ID=:unidad) ";
		
			
		if(cve_benefi!=null&&!cve_benefi.equals("")) 
			sql+= " AND C.CLV_BENEFI=:cve_benefi ";
		
		if(capitulo!=null&&!capitulo.equals("0")) 
			sql+= " AND CP.CLV_CAPITU =:capitulo ";
		
		if(numped!=null&&!numped.equals(""))
			sql+= " AND C.NUM_PED LIKE '%"+numped+"%'";
		
		if(numreq!=null&&!numreq.equals(""))
			sql+= " AND F.NUM_REQ LIKE '%"+numreq+"%'";
		
		return this.getNamedJdbcTemplate().queryForList(sql+" ORDER BY C.NUM_PED, OP.FECHA ASC", parametros);
	}
	
	/*Metodo para obtener un pedido*/
	public Map getPedido(Long cve_ped){		
		try  
		{
			String sql = "SELECT SAM_PEDIDOS_EX.CVE_PED, SAM_PEDIDOS_EX.NUM_PED, CONVERT(varchar(10), SAM_PEDIDOS_EX.FECHA_PED, 103) AS FECHA_PED, SAM_PEDIDOS_EX.FECHA_PED as FECHA_PED2, CONVERT(varchar(10), SAM_PEDIDOS_EX.FECHA_CIERRE, 103) AS FECHA_CIERRE, SAM_PEDIDOS_EX.FECHA_CIERRE AS FECHA_CIERRE2, "+
									"SAM_PEDIDOS_EX.cve_concurso, SAM_PEDIDOS_EX.FECHA_ENTREGA, SAM_PEDIDOS_EX.CLV_BENEFI, CAT_BENEFI.NCOMERCIA, CAT_BENEFI.COLONIA, CAT_BENEFI.CIUDAD, "+
									"CAT_BENEFI.ESTADO, CAT_BENEFI.DOMIFISCAL, CAT_BENEFI.TELEFONOS, SAM_PEDIDOS_EX.CONTRATO, SAM_ESTATUS_PED.DESCRIPCION AS STATUS_DESC, SAM_PEDIDOS_EX.CONDICION_PAGO,"+
									"SAM_PEDIDOS_EX.NOTAS, SAM_PEDIDOS_EX.STATUS, SAM_PEDIDOS_EX.ENTREGA, SAM_PEDIDOS_EX.IVA, SAM_PEDIDOS_EX.TIPO_IVA, SAM_PEDIDOS_EX.DESCUENTO, ROUND(SAM_PEDIDOS_EX.TOTAL,2) AS TOTAL, SAM_REQUISIC.CVE_REQ,"+
									"SAM_REQUISIC.NUM_REQ, SAM_REQUISIC.CVE_VALE, SAM_REQUISIC.ID_PROYECTO, SAM_REQUISIC.CLV_PARTID, SAM_REQUISIC.CVE_CONTRATO, SAM_PEDIDOS_EX.ID_GRUPO, SAM_REQUISIC.ID_DEPENDENCIA, SAM_REQUISIC.TIPO, "+
									"CAT_DEPENDENCIAS.DEPENDENCIA AS UNIDAD, CEDULA_TEC.ID_RECURSO, CAT_RECURSO.RECURSO, CAT_RECURSO.CODIGO_FF, "+
									"(LEFT(SAM_REQUISIC.CLV_PARTID,1)) AS CLV_OBJETOGASTO, "+
									"(SELECT CAPITULO FROM CAT_CAPITU WHERE CLV_CAPITU = (LEFT(SAM_REQUISIC.CLV_PARTID,1)+'000')) AS OBJETO_GASTO "+
							"FROM    CAT_BENEFI "+
								"INNER JOIN SAM_PEDIDOS_EX "+
								"INNER JOIN SAM_ESTATUS_PED ON SAM_ESTATUS_PED.ID_STATUS = SAM_PEDIDOS_EX.STATUS ON  CAT_BENEFI.CLV_BENEFI = SAM_PEDIDOS_EX.CLV_BENEFI "+
								"INNER JOIN CAT_RECURSO "+
								"INNER JOIN CAT_DEPENDENCIAS "+
								"INNER JOIN SAM_REQUISIC ON CAT_DEPENDENCIAS.ID = SAM_REQUISIC.ID_DEPENDENCIA "+
								"INNER JOIN CEDULA_TEC ON CEDULA_TEC.ID_PROYECTO = SAM_REQUISIC.ID_PROYECTO ON CAT_RECURSO.ID = CEDULA_TEC.ID_RECURSO ON   SAM_PEDIDOS_EX.CVE_REQ = SAM_REQUISIC.CVE_REQ "+  
							"WHERE SAM_PEDIDOS_EX.CVE_PED = ?";
			return this.getJdbcTemplate().queryForMap(sql, new Object []{cve_ped});

		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			return null;
		}
	}
	
	/*Metodo privado que apertura el pedido*/
  public void aperturarPedido(Long cve_ped, int cve_pers, Integer ejercicio){	
	  try { 
		  
					/*Valida aqui si el documento esta en el periodo actual para permitir aperturar*/
			      	
			      	Map pedido = getPedido(cve_ped);
			      	
			      	Date fechaCierre = new Date();
			  		fechaCierre = (Date) pedido.get("FECHA_CIERRE2");
			  		Calendar c1 = Calendar.getInstance();
			  		if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1))
			  		{
			  			throw new RuntimeException("No se puede aperturar el Pedido "+pedido.get("NUM_PED").toString()+", el periodo del documento es diferente al actual, consulte a su administrador");
			  		}
  		
                	//comprobar que no tenga orden de pago el pedido
                	if(!existeFactura(cve_ped)){
                		
                		if(pedido.get("STATUS").equals("0")) 
                			throw new RuntimeException("No se puede aperturar un Pedido que ya esta en edición");
                		//int mes=Integer.parseInt(pedido.get("PERIODO").toString());
    					Long proyecto= Long.parseLong(pedido.get("ID_PROYECTO").toString());
    					String partida = pedido.get("CLV_PARTID").toString();
    					Long idReq = new Long((Integer)pedido.get("CVE_REQ"));
    					Double importe= ((BigDecimal)pedido.get("TOTAL")).doubleValue();
                		//cambia el status del pedido
    					this.getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET STATUS=?, FECHA_CIERRE = NULL  WHERE CVE_PED = ?", new Object []{this.PED_STATUS_NUEVO, cve_ped});
                		//Pregunta si la requisicion es anualizada
                		boolean anualizada = esAnualizadaRequisicion(idReq);
                		boolean ban = true;
                		//cambia el status de los elementos del pedido y activa los lotes en requisicion
                		List <Map> lotes = getConceptos(cve_ped);
                		for(Map row: lotes){
                			//si es requisicion anualizada devolver los elementos a los lotes correspondientes de sus cantidades asi como sus status
                			if(anualizada){
                				//cambiar el status de los detalles que esten activos del pedido
                				getJdbcTemplate().update("UPDATE SAM_PED_MOVTOS SET STATUS = ? WHERE ID_PED_MOVTO = ?", new Object[]{PED_MOVTO_EDICION, Long.parseLong(row.get("ID_PED_MOVTO").toString())});
                				//regresar los lotes en cantidad a los lotes de la requisicion
                				devolverLotesRequisicion(Long.parseLong(row.get("ID_PED_MOVTO").toString()));
                				//si es anualizada dejar en estatus pendiente
                				if(ban) getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS = ?, COMPROMETE = ? WHERE CVE_REQ = ?", new Object []{gatewayRequisicion.REQ_STATUS_EN_PROCESO, 1, idReq});
                			}
                			else
                			{
                				//Cambia el status del lote
                				getJdbcTemplate().update("UPDATE SAM_PED_MOVTOS SET STATUS = ? WHERE ID_PED_MOVTO = ?", new Object[]{PED_MOVTO_EDICION, Long.parseLong(row.get("ID_PED_MOVTO").toString())});
                				//Cambia el status del lote de la requi
                				getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET STATUS = ?, COMPROMETIDO = ? WHERE ID_REQ_MOVTO = ?", new Object[]{gatewayRequisicion.REQ_MOVTO_SOLICITADO, Long.parseLong(row.get("ID_REQ_MOVTO").toString()), 1});
                				//si es anualizada dejar en estatus pendiente
                				if(ban) getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS = ?, COMPROMETE = ? WHERE CVE_REQ = ?", new Object []{gatewayRequisicion.REQ_STATUS_EN_PROCESO, 1, idReq});
                			}
                			ban = false; 
                		}
                		
                		//Si tienen contrato
                    	if(pedido.get("CVE_CONTRATO")!=null){
                    		Double total_lotes = 0D;
                    		List<Map> Lotes_Req = getJdbcTemplate().queryForList("SELECT *FROM SAM_REQ_MOVTOS WHERE ID_REQ_MOVTO IN (SELECT ID_REQ_MOVTO FROM SAM_PED_MOVTOS WHERE CVE_PED=?)", new Object[]{cve_ped});
                    		for(Map row: Lotes_Req){
                    			total_lotes+= Double.parseDouble(row.get("CANTIDAD").toString())*Double.parseDouble(row.get("PRECIO_EST").toString());
                    		}
                    		//determinar precompromiso de la requisicion sin los lotes aún en pedido
                    		Double total_req = (Double) getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = ? AND ID_REQ_MOVTO NOT IN (SELECT M.ID_REQ_MOVTO FROM SAM_PED_MOVTOS AS M INNER JOIN SAM_PEDIDOS_EX AS P ON (P.CVE_PED = M.CVE_PED) WHERE P.CVE_REQ = ?)", new Object[]{pedido.get("CVE_REQ"), pedido.get("CVE_REQ")}, Double.class);
                    		
                    		//quitar el compromiso del pedido en el contrato
                    		//getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO = ? AND TIPO_MOV = ? AND TIPO_DOC = ?", new Object[]{pedido.get("CVE_CONTRATO"), "LIBERACION", "PED"});
                    		//quitar el compromiso del de la requisicion si existen datos
                    		//getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO = ? AND TIPO_MOV = ? AND TIPO_DOC = ? AND CVE_DOC = ?", new Object[]{pedido.get("CVE_CONTRATO"), "LIBERACION", "REQ", pedido.get("CVE_REQ")});
                    		//volver a comprometer la requisicion
                    		//getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO (CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)", new Object[]{pedido.get("CVE_CONTRATO"), "LIBERACION", "REQ", pedido.get("CVE_REQ"),gatewayMeses.getMesActivo(ejercicio) ,total_lotes});
                    		//Si aun quedan lotes disponibles en requisicion precomprometer 
                    		/*if(total_req>0){
                    			getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO (CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)", new Object[]{pedido.get("CVE_CONTRATO"), "COMPROMISO", "REQ", pedido.get("CVE_REQ"), (getJdbcTemplate().queryForInt("SELECT PERIODO FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{pedido.get("CVE_REQ")})),total_req});
                    		}*/
                    	}
                		
                		//this.getJdbcTemplate().update("UPDATE PEDIDOS_EX SET STATUS=0 WHERE CVE_PED = ?", new Object []{cve_ped});
                		//Guardar en bitacora
                		Map ped = getJdbcTemplate().queryForMap("SELECT SAM_PEDIDOS_EX.*, SAM_REQUISIC.ID_PROYECTO, SAM_REQUISIC.CLV_PARTID FROM SAM_PEDIDOS_EX INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_PEDIDOS_EX.CVE_REQ) WHERE CVE_PED = ?", new Object []{cve_ped});
                		String folio=rellenarCeros(cve_ped.toString(),6);
                		descomprometerPedido(cve_ped, ejercicio,proyecto,partida,importe);
                		gatewayBitacora.guardarBitacora(gatewayBitacora.APERTURA_PEDIDO, ejercicio, cve_pers, cve_ped, folio, "PED", (Date) ped.get("FECHA_PED"), ped.get("ID_PROYECTO").toString(), ped.get("CLV_PARTID").toString(), null, Double.parseDouble(ped.get("TOTAL").toString()));
                		
                		//SE AGREGA PROCEDIMIENTO ALMACENADO DE PEREDO PARA CONTABILIZAR MODIFICACION PEDIDOS 28/JUL/2013
						StoreProcedurePedidos sp = new StoreProcedurePedidos(getJdbcTemplate().getDataSource());
						Map result = sp.execute(cve_ped, 2);
					    //TERMINA PROCEDIMIENT0
                	}
                	else{
                		throw new RuntimeException("El Pedido que intenta aperturar ya tiene factura");
                	}
                	
			  } catch (DataAccessException e) {            	                    
			      throw new RuntimeException(e.getMessage(),e);
			 }
	}
  
	
	private void descomprometerPedido(final Long idPedido, Integer ejercicio,Long proyecto,String partida,double importe){
		int mesActivo = gatewayMeses.getMesActivo(ejercicio);		
		//gatewayProyectoPartidas.comprometerPresupuesto(proyecto, partida, mesActivo,importe , "REDUCCION");
		/*Regresar pre Compromisos */
		//Integer idReq= (Integer)this.getJdbcTemplate().queryForObject("select  distinct(CVE_REQ)  from SAM_COMP_REQUISIC  where CVE_PED=? ",new Object []{idPedido}, Integer.class); 
		Integer idReq= (Integer)this.getJdbcTemplate().queryForObject("select  CVE_REQ  from SAM_PEDIDOS_EX  where CVE_PED=? ",new Object []{idPedido}, Integer.class);
		List<Map<String,Object> > compromisos= this.getJdbcTemplate().queryForList("select  * from SAM_COMP_REQUISIC  where (CVE_PED=? or  CVE_PED is null) and  CVE_REQ=? and tipo='LIBERACION'  ",new Object []{idPedido,idReq});
		BigDecimal importeComTotal=new BigDecimal("0.0");
	    for (Map<String,Object> dato:compromisos ){
	       int mesReq=(Integer)dato.get("PERIODO");
		   BigDecimal importeReq=(BigDecimal)dato.get("IMPORTE");
		   importeComTotal=importeComTotal.add(importeReq);
		   if ( mesReq >= mesActivo ){
		      //gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, mesReq,importeComTotal.doubleValue() , "COMPROMETER");		      
		      importeComTotal=new BigDecimal("0.0");
		   }
	     }
	    if (importeComTotal.equals(new BigDecimal("0.0"))){
			//gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, mesActivo,importeComTotal.doubleValue() , "COMPROMETER");
		}	
	    this.getJdbcTemplate().update("DELETE FROM SAM_COMP_REQUISIC WHERE (CVE_PED=? or CVE_PED IS NULL) AND  CVE_REQ=? AND TIPO='LIBERACION' ",new Object []{idPedido,idReq});
	    this.getJdbcTemplate().update("UPDATE SAM_REQUISIC SET PERIODO =? WHERE CVE_REQ = (SELECT CVE_REQ FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?)", new Object[]{mesActivo, idPedido});
	    this.getJdbcTemplate().update("UPDATE SAM_COMP_REQUISIC SET PERIODO =? WHERE TIPO ='COMPROMISO' AND CVE_PED IS NULL AND CVE_REQ = (SELECT CVE_REQ FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?)", new Object[]{mesActivo, idPedido});
    }
	
	/*Metodo para determinar si una requisicion es anualizada*/
	public boolean esAnualizadaRequisicion(Long cve_req){
		return this.getJdbcTemplate().queryForInt("SELECT (CASE TIPO WHEN '7' THEN 1 ELSE 0 END) AS N FROM SAM_REQUISIC WHERE CVE_REQ = ? ", new Object []{cve_req})!=0;
	}
	/*Metodo para comprobar si existe una orden de pago asignada a un pedido*/
	public boolean ordenPagoEnPedido(Long cve_ped){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_OP_COMPROBACIONES AS P INNER JOIN SAM_ORD_PAGO AS O ON (O.CVE_OP = P.CVE_OP) WHERE O.CVE_PED = ? AND O.STATUS IN (0,1) AND P.CVE_PED IS NOT NULL", new Object[]{cve_ped})!=0;
		//cont += getJdbcTemplate().queryForInt("SELECT COUNT(CVE_OP) AS N FROM ORD_PAGO WHERE PED = ? AND STATUS IN(0,1)", new Object[]{cve_ped});
	}
	
	public boolean existeFactura(Long cve_pedido){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_FACTURAS WHERE CVE_PED =? AND STATUS IN (1, 3)", new Object[]{cve_pedido})>0;
	}
	
	public void cancelaPedido(Long cve_ped, int cve_pers, int ejercicio){
		//this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            //@Override
            //protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	//Descomprometer el pedido si esta comprometiendo
		
				//Buscar si existe el Super Privilegio para Cancelar Requisiciones
				boolean privilegio = getPrivilegioEn(cve_pers, 137);
		
				/*Valida aqui si el documento esta en el periodo actual para permitir aperturar*/
		      	Map pedido = getPedido(cve_ped);
		      	
		      	Date fechaCierre = new Date();
		  		fechaCierre = (Date) pedido.get("FECHA_CIERRE2");
		  		Calendar c1 = Calendar.getInstance();
		  		if(fechaCierre!=null&&privilegio==false)
			  		if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1))
			  		{
			  			throw new RuntimeException("No se puede cancelar el Pedido "+pedido.get("NUM_PED").toString()+", el periodo del documento es diferente al actual, consulte a su administrador");
			  		}
  		
				if(existeFactura(cve_ped))
				{
					throw new RuntimeException("El Pedido que intenta cancelar esta relacionado a una factura");
				}
				
            	
            	if( pedido.get("STATUS_DESC").equals("CERRADO")){ //esta comprometiendo
            		descomprometerPedido(cve_ped, ejercicio, Long.parseLong(pedido.get("ID_PROYECTO").toString()), pedido.get("CLV_PARTID").toString(), Double.parseDouble(pedido.get("TOTAL").toString()));
            	}

            	//Descompromete momentaneamente la requisicion
            	//getJdbcTemplate().update("DELETE FROM SAM_COMP_REQUISIC WHERE CVE_REQ = ? AND TIPO = ?", new Object[]{pedido.get("CVE_REQ"), "COMPROMISO"});
            	//Cambiar el status del pedido
            	cambiarEstatusPedido(cve_ped, PED_STATUS_CANCELADO, pedido.get("STATUS").toString());
            	
            	//Obtener los lotes del pedido
            	List <Map> Lotes = getConceptos(cve_ped);
            	//Obtener la requisicion del pedido
            	Map req = gatewayRequisicion.getRequisicion(Long.parseLong(pedido.get("CVE_REQ").toString()));
            	//Comprobar si es anualizada la requisicion y de ser asi devolver los lotes que correspondan
            	//if (esAnualizadaRequisicion(Long.parseLong(pedido.get("CVE_REQ").toString())))
            	if (req.get("TIPO").equals("7"))
            	{	
            		for(Map row: Lotes){
            			devolverLotesRequisicion(Long.parseLong(row.get("ID_PED_MOVTO").toString()));
            		}
            		//Cambiar si es necesario el status de la requisicion
            		gatewayRequisicion.cambiarStatusRequisicion(Long.parseLong(pedido.get("CVE_REQ").toString()));
            		//Cerrar la requisicion
            	}
            	else
            	{		
            			/*actializa el estatus de la requisicion
               			getJdbcTemplate().update("UPDATE REQUISIC SET STATUS = ?, COMPROMETE = ? WHERE CVE_REQ = ?", new Object []{gatewayRequisicion.REQ_STATUS_PENDIENTE, 1, Long.parseLong(pedido.get("CVE_REQ").toString())});*/
            			//actualizar los lotes de la requi a penientes
            			//getJdbcTemplate().update("UPDATE REQ_MOVTOS SET STATUS = ?, COMPROMETIDO = ? WHERE CVE_REQ = ?", new Object[]{gatewayRequisicion.REQ_MOVTO_SOLICITADO, 1, Long.parseLong(pedido.get("CVE_REQ").toString())});
            			//cerrar aqui la requisicion
            			getJdbcTemplate().update("UPDATE SAM_PED_MOVTOS SET ID_REQ_MOVTO = NULL WHERE CVE_PED = ? ", new Object[]{cve_ped});
            			gatewayRequisicion.cerrarRequisicion(Long.parseLong(req.get("CVE_REQ").toString()), (Integer) req.get("CVE_PERS"), (String) req.get("CLV_UNIADM"), null);
            	}
            	
            	//Si tienen contrato
            	if(pedido.get("CVE_CONTRATO")!=null){
            		Double total_lotes = 0D;
            		List<Map> Lotes_Req = getJdbcTemplate().queryForList("SELECT *FROM SAM_REQ_MOVTOS WHERE ID_REQ_MOVTO IN (SELECT ID_REQ_MOVTO FROM SAM_PED_MOVTOS WHERE CVE_PED=?)", new Object[]{cve_ped});
            		for(Map row: Lotes_Req){
            			total_lotes+= Double.parseDouble(row.get("CANTIDAD").toString())*Double.parseDouble(row.get("PRECIO_EST").toString());
            		}
            		//determinar precompromiso de la requisicion sin los lotes aún en pedido
            		Double total_req = (Double) getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = ? AND ID_REQ_MOVTO NOT IN (SELECT M.ID_REQ_MOVTO FROM SAM_PED_MOVTOS AS M INNER JOIN SAM_PEDIDOS_EX AS P ON (P.CVE_PED = M.CVE_PED) WHERE P.CVE_REQ = ?)", new Object[]{pedido.get("CVE_REQ"), pedido.get("CVE_REQ")}, Double.class);
            		
            		//quitar el compromiso del pedido en el contrato
            		//getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO = ? AND TIPO_MOV = ? AND TIPO_DOC = ?", new Object[]{pedido.get("CVE_CONTRATO"), "LIBERACION", "PED"});
            		//quitar el compromiso del de la requisicion si existen datos
            		//getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO = ? AND TIPO_MOV = ? AND TIPO_DOC = ? AND CVE_DOC = ?", new Object[]{pedido.get("CVE_CONTRATO"), "LIBERACION", "REQ", pedido.get("CVE_REQ")});
            		//volver a comprometer la requisicion
            		//getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO (CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)", new Object[]{pedido.get("CVE_CONTRATO"), "LIBERACION", "REQ", pedido.get("CVE_REQ"),gatewayMeses.getMesActivo(ejercicio) ,total_lotes});
            		//Si aun quedan lotes disponibles en requisicion precomprometer 
            		/*if(total_req>0){
            			getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO (CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)", new Object[]{pedido.get("CVE_CONTRATO"), "COMPROMISO", "REQ", pedido.get("CVE_REQ"), (getJdbcTemplate().queryForInt("SELECT PERIODO FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{pedido.get("CVE_REQ")})),total_req});
            		}*/
            	}
            	
            	//Date FECHA = new Date();
            	//getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET FECHA_CANCELADO = ? WHERE CVE_PED = ?", new Object[]{FECHA, cve_ped});
            	//Guardar en bitacora
            	Map ped = getJdbcTemplate().queryForMap("SELECT SAM_PEDIDOS_EX.*, SAM_REQUISIC.ID_PROYECTO, SAM_REQUISIC.CLV_PARTID FROM SAM_PEDIDOS_EX INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_PEDIDOS_EX.CVE_REQ) WHERE CVE_PED = ?", new Object []{cve_ped});
            	gatewayBitacora.guardarBitacora(gatewayBitacora.CANCELA_PEDIDO, ejercicio, cve_pers, cve_ped, ped.get("NUM_PED").toString(), "PED", (Date) ped.get("FECHA_PED"), ped.get("ID_PROYECTO").toString(), ped.get("CLV_PARTID").toString(), null, Double.parseDouble(pedido.get("TOTAL").toString()));
            	//Eliminar los lotes del pedido
            	//eliminarTodosLotesPedido(cve_ped);
            	//SE AGREGA PROCEDIMIENTO ALMACENADO DE PEREDO PARA CONTABILIZAR CANCELACION PEDIDOS 28/JUL/2013
				//StoreProcedurePedidos sp = new StoreProcedurePedidos(getJdbcTemplate().getDataSource());
				//Map result = sp.execute(cve_ped, 0);
			    //TERMINA PROCEDIMIENT0
           //}
        // });
	}
	
	
	private void cambiarEstatusPedido(Long cve_ped, int estatus, String status_actual ){
		if(status_actual.equals("0"))
			this.getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET STATUS=? WHERE CVE_PED = ?", new Object []{estatus, cve_ped});
		else
			this.getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET STATUS=?, FECHA_CANCELADO=? WHERE CVE_PED = ?", new Object []{estatus, new Date(), cve_ped});
		//if(estatus==this.PED_STATUS_NUEVO||estatus==this.PED_STATUS_CANCELADO) sql = "UPDATE SAM_PEDIDOS_EX SET STATUS=?, COMPROMETE =0 WHERE CVE_PED = ?";
			
	}
	
	/*private void _guardarEnBitacoraReq(Long cve_ped, int tipo_mov, int cve_pers){
		//Guardar en bitacora
		Date fecha = new Date();
		Map requisicion = this.getJdbcTemplate().queryForMap("SELECT NUM_REQ, PROYECTO, CLV_PARTID, PERIODO, FECHA, TIPO, STATUS, EJERCICIO, (SELECT SUM(CANTIDAD*PRECIO_EST) FROM REQ_MOVTOS WHERE REQ_MOVTOS.CVE_REQ = REQUISIC.CVE_REQ ) AS IMPORTE FROM REQUISIC WHERE CVE_REQ = ? ", new Object []{cve_ped});
		gatewayBitacora.guardarBitacora(requisicion.get("NUM_REQ").toString(), cve_ped, "REQ", fecha, Double.parseDouble(requisicion.get("IMPORTE").toString()), "X", "0", requisicion.get("PROYECTO").toString(), requisicion.get("CLV_PARTID").toString(), Integer.parseInt(requisicion.get("EJERCICIO").toString()), cve_pers, tipo_mov);
	}*/
	
	//Metodo que devuelve un Map con el numero y clave de la requisicion
	public Long getNumRequisicion(Long cve_ped){
		try
		{	
			String sql = "SELECT CVE_REQ  FROM  SAM_PEDIDOS_EX  where CVE_PED =?";
			return this.getJdbcTemplate().queryForLong(sql, new Object[]{cve_ped});
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			return null;
		}
	}
	
	public void actualizarTotalesPedido(Long cve_ped, Double iva, Double descuento){
		double subtotal = 0L;
		double total = 0L;
		List <Map> resultado = this.getJdbcTemplate().queryForList("SELECT * FROM SAM_PED_MOVTOS WHERE SAM_PED_MOVTOS.STATUS=? AND SAM_PED_MOVTOS.CVE_PED = ?", new Object []{this.PED_MOVTO_EDICION, cve_ped});
		for(Map row: resultado){
			/*recorrer el arreglo de ids de los movs apara calcular los totales*/
			subtotal+= (Double.parseDouble(row.get("CANTIDAD").toString())*Double.parseDouble(row.get("PRECIO_UNI").toString()));
		}		
		if(descuento>0) subtotal = subtotal - descuento;
		total = subtotal+iva;
		if(total<0) total = 0;
		//Actualiza el pedido
		this.getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET SUBTOTAL=?, IVA=?, TOTAL=?, COMPROMETE=? WHERE CVE_PED = ?", new Object[]{subtotal, iva, total, total, cve_ped });
	}
	
	/*Metodo para  eliminar los lotes de un pedido*/
	public void eliminarLotesPedido(Long id_ped_movto, Long cve_ped, int cve_pers, int ejercicio){
		try
		{
			Map pedido = this.getPedido(cve_ped);
			Map lote = this.getConcepto(id_ped_movto);
			/*Reactivar el lote correspondiente en la requisicion*/
			this.getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET STATUS = ?, COMPROMETIDO = ? WHERE ID_REQ_MOVTO = (SELECT ID_REQ_MOVTO FROM SAM_PED_MOVTOS WHERE ID_PED_MOVTO = ?)", new Object []{this.gatewayRequisicion.REQ_MOVTO_SOLICITADO, 1, id_ped_movto});
			/*Eliminar el lote en pedidos */
			this.getJdbcTemplate().update("DELETE FROM SAM_PED_MOVTOS WHERE ID_PED_MOVTO = ?", new Object []{id_ped_movto});
			//actualiza el status de la requisicion
			//this.getJdbcTemplate().update("UPDATE REQUISIC SET STATUS = ?, COMPROMETE = ? WHERE CVE_REQ = ?", new Object[]{this.gatewayRequisicion.REQ_STATUS_EN_PROCESO , 1, this.getJdbcTemplate().queryForLong("SELECT CVE_REQ FROM PEDIDOS_EX WHERE CVE_PED = ?", new Object []{cve_ped})});
			/*Actualizar el pedido en los totales*/
			this.actualizarTotalesPedido(cve_ped, Double.parseDouble(pedido.get("IVA").toString()), Double.parseDouble(pedido.get("DESCUENTO").toString()));
			/*Guardar en bitacora*/
			gatewayBitacora.guardarBitacora(gatewayBitacora.ELIMINO_MOV_PED, ejercicio, cve_pers, cve_ped, pedido.get("NUM_PED").toString(), "PED", (Date) pedido.get("FECHA_PED2"), null, null, "ID_PED: " + id_ped_movto + " ID_REQ: "+ lote.get("ID_REQ_MOVTO").toString() +" Cantidad: "+lote.get("CANTIDAD")+" Precio_Unit: "+lote.get("PRECIO_UNI").toString(), Double.parseDouble(lote.get("CANTIDAD").toString())* Double.parseDouble(lote.get("PRECIO_UNI").toString()));
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	private void _guardarEnBitacoraMov(Long cve_ped, int tipo_mov, int cve_pers){
		//Guardar en bitacora
		Date fecha = new Date();
		String SQL = "SELECT DISTINCT "+ 
						"SAM_PEDIDOS_EX.NUM_PED, "+ 
						"SAM_REQUISIC.ID_PROYECTO, "+ 
						"SAM_REQUISIC.CLV_PARTID, "+ 
						"SAM_PEDIDOS_EX.EJERCICIO, "+ 
						"SAM_PEDIDOS_EX.FECHA_PED, "+ 
						"SAM_PEDIDOS_EX.STATUS, "+ 
						"SAM_PEDIDOS_EX.TOTAL AS IMPORTE "+
					"FROM SAM_PEDIDOS_EX "+
						"INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_PEDIDOS_EX.CVE_REQ) "+
					"WHERE SAM_PEDIDOS_EX.CVE_PED = ?";
		Map pedido = this.getJdbcTemplate().queryForMap(SQL, new Object []{cve_ped});
		//gatewayBitacora.guardarBitacora(pedido.get("NUM_PED").toString(), cve_ped, "PED", fecha, Double.parseDouble(pedido.get("IMPORTE").toString()), "X", "0", pedido.get("PROYECTO").toString(), pedido.get("CLV_PARTID").toString(), Integer.parseInt(pedido.get("EJERCICIO").toString()), cve_pers, tipo_mov);
	}
			
 public boolean tienePrecompromisoDisponible(Long proyecto, String partida, int mes, Long idReq, double importe, long cve_contrato, long cve_vale ){
	  Map Requisic = gatewayRequisicion.getRequisicion(idReq);
	  boolean tienePresupuesto = false;
	  double preCompromiso=gatewayProyectoPartidas.getPreComprometidoMes(proyecto, partida, mes);
	  double preCompromisoReq=gatewayRequisicion.getCompromisoHastaMes(idReq,mes);
	  //Obtener lo que queda disponible desl mes para luego verificar si hace falta dinero en requisicion
	  double disponiblemes = gatewayProyectoPartidas.getDisponibleMes(mes, proyecto, partida).doubleValue();
	  double apartadoReq = redondea(preCompromisoReq,2);
	  BigDecimal dispcontrato = (cve_contrato==0) ? new BigDecimal("0.00"): (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(MONTO,0) FROM VT_COMPROMISOS WHERE TIPO_DOC = ? AND CVE_DOC = ? AND PERIODO = ?", new Object[]{"CON",cve_contrato, mes}, BigDecimal.class);
	  BigDecimal dispvale = (cve_vale==0) ? new BigDecimal("0.00"): (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(MONTO,0) FROM VT_COMPROMISOS WHERE TIPO_DOC = ? AND CVE_DOC = ? AND PERIODO = ? AND ID_PROYECTO = ? AND CLV_PARTID = ?", new Object[]{"REQ",idReq, mes, proyecto, partida}, BigDecimal.class);
	  
	  //verificar si el monto en requisicion alcanza por el importe del pedido
	  if (redondea(preCompromiso,2) >= redondea(preCompromisoReq,2) && importe<=apartadoReq)
		  tienePresupuesto=true;
	  
	  if(importe<=apartadoReq && Requisic.get("TIPO").toString().equals("7"))
		  tienePresupuesto=true;
	  
	  if(cve_contrato!=0){
		  if(tienePresupuesto==false&&(redondea((apartadoReq+dispcontrato.doubleValue()),2) >= redondea(importe,2)))
			  tienePresupuesto=true;
	  }
	  else if(cve_vale!=0){
		  //Recupera el disponible del mes
		  BigDecimal disponible_presupuesto = (BigDecimal) this.getJdbcTemplate().queryForObject("SELECT ISNULL(dbo.getDisponible(?,?,?),0) AS DISPONIBLE FROM CEDULA_TEC AS CT WHERE CT.ID_PROYECTO = ? ", new Object[]{mes, proyecto, partida, proyecto}, BigDecimal.class);
		  //valida aqui que tiene dinero en el vale
		  if(redondea((dispvale.doubleValue()+disponible_presupuesto.doubleValue()),2) >= redondea(importe,2))
			  tienePresupuesto=true;
	  }
	  else{
		  //Si no hay dinero en requisicion completar con el disponible del mes para ver si alacanza
		  if(tienePresupuesto==false&&(redondea((apartadoReq+disponiblemes),2) >= redondea(importe,2)))
			  tienePresupuesto=true;
	  }
	 
	  
 return tienePresupuesto;
 }

 public void comprometerPedido(Long proyecto, String partida, int mes, Long idReq, double importe, Long idPedido ){	  
	   List<Map<String,Object>> preCompromisosReq=gatewayRequisicion.getCompromisoPorMesReq(idReq,mes);
	   double importeTemp=importe; 
	   for (Map<String,Object> dato : preCompromisosReq){		   
		   double importeReqCom =((BigDecimal)dato.get("IMPORTE")).doubleValue();
		   if ( importeTemp <=importeReqCom  )
			   importeReqCom=importeTemp;		   
		   int periodo =(Integer)dato.get("PERIODO");		   
		   gatewayRequisicion.insertarCompromisoRequisicion( idReq,importeReqCom, "LIBERACION",idPedido, periodo);
		   //gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, periodo,importeReqCom,"REDUCCION");
		   importeTemp = importeTemp - importeReqCom;
	   }
	   //gatewayProyectoPartidas.comprometerPresupuesto(proyecto, partida, mes,importe,"COMPROMETER");
 }
 
//Controlador para cerrar el pedido--------------------------------------------------------------------------------------------
	public void cerrarPedido(final Long cve_ped, final int tipo, final Double iva, final int cve_pers, final int ejercicio){
		try
		{
			/*this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	*/
					Map pedido =getPedido(cve_ped);
					int mesActivo =gatewayMeses.getMesActivo(ejercicio);
					Long proyecto= Long.parseLong(pedido.get("ID_PROYECTO").toString());
					String partida = pedido.get("CLV_PARTID").toString();
					Long idReq = new Long((Integer)pedido.get("CVE_REQ"));
					Double importe= ((BigDecimal)pedido.get("TOTAL")).doubleValue();
					Long cve_contrato = new Long((pedido.get("CVE_CONTRATO")==null) ? 0L:(Integer)pedido.get("CVE_CONTRATO"));
					Long cve_vale = new Long((pedido.get("CVE_VALE")==null) ? 0L:(Integer)pedido.get("CVE_VALE"));
						
					boolean tiene = tienePrecompromisoDisponible(proyecto, partida, mesActivo, idReq, importe, cve_contrato, cve_vale );
					
					if (tiene){	

							getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET STATUS =?, FECHA_CIERRE=? WHERE CVE_PED =?", new Object[]{PED_STATUS_PENDIENTE, new Date(), cve_ped});					
							getJdbcTemplate().update("UPDATE SAM_PED_MOVTOS SET STATUS = ? WHERE CVE_PED = ?", new Object []{PED_MOVTO_PENDIENTE, cve_ped});
							getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET STATUS = ?, COMPROMETIDO = ? WHERE ID_REQ_MOVTO IN (SELECT ID_REQ_MOVTO FROM SAM_PED_MOVTOS WHERE CVE_PED = ?)", new Object []{gatewayRequisicion.REQ_MOVTO_SURTIDO, 0, cve_ped});
							
						    comprometerPedido(proyecto, partida, mesActivo, idReq, importe, cve_ped );	
						    
						    boolean finiquitar=gatewayRequisicion.comprobarParaFiniquitar(idReq);
						    
						    if (finiquitar) {
							     getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS = ?, PERIODO =?, COMPROMETE =? WHERE CVE_REQ = ?", new Object []{gatewayRequisicion.REQ_STATUS_FINIQUITADA, gatewayMeses.getMesActivo(ejercicio), 0, idReq});
							     getJdbcTemplate().update("UPDATE SAM_COMP_REQUISIC SET PERIODO =? WHERE TIPO ='COMPROMISO' AND CVE_PED IS NULL AND CVE_REQ = ?", new Object[]{mesActivo, idReq});
							     List<Map> detallesReq =getJdbcTemplate().queryForList("Select sum(CASE TIPO  WHEN 'COMPROMISO' THEN importe else (importe*-1)  END ) as IMPORTE,PERIODO  from  SAM_COMP_REQUISIC where CVE_REQ=? GROUP BY PERIODO "+   
							    		 " HAVING sum(  CASE TIPO  WHEN 'COMPROMISO' THEN importe else (importe*-1)  END ) > 0    ",new Object[]{idReq});
							     for (Map det : detallesReq ) {
							    	 importe=((BigDecimal)det.get("IMPORTE")).doubleValue();
							    	 int periodo =(Integer)det.get("PERIODO");
							       gatewayRequisicion.insertarCompromisoRequisicion( idReq,importe , "LIBERACION",null,periodo);
							       //gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, periodo,importe,"REDUCCION");
							     }
							}
							else{
								 getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS = ?, PERIODO=? WHERE CVE_REQ = ?", new Object[]{gatewayRequisicion.REQ_STATUS_EN_PROCESO, gatewayMeses.getMesActivo(ejercicio), idReq });
								 getJdbcTemplate().update("UPDATE SAM_COMP_REQUISIC SET PERIODO =? WHERE TIPO ='COMPROMISO' AND CVE_PED IS NULL AND CVE_REQ = ?", new Object[]{mesActivo, idReq});
							}
							
						    //si la requisicion es de tipo 7 (CALENDARIZADA) descontar a los lotes de la requisicion la cantidad de elementos del lote del pedido
								if(tipo==7) descontarLotesRequisicion(cve_ped);
							//_guardarEnBitacoraMov(cve_ped, gatewayBitacora.CERRO_PEDIDO, cve_pers);
							
							/*guardar la bitacora*/
						    Map ped = getJdbcTemplate().queryForMap("SELECT SAM_PEDIDOS_EX.*, SAM_REQUISIC.ID_PROYECTO, SAM_REQUISIC.CLV_PARTID FROM SAM_PEDIDOS_EX INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_PEDIDOS_EX.CVE_REQ) WHERE CVE_PED = ?", new Object []{cve_ped});
						    gatewayBitacora.guardarBitacora(gatewayBitacora.CERRO_PEDIDO, ejercicio, cve_pers, cve_ped, ped.get("NUM_PED").toString(), "PED", (Date) ped.get("FECHA_PED"), ped.get("ID_PROYECTO").toString(), ped.get("CLV_PARTID").toString(), null, Double.parseDouble(ped.get("TOTAL").toString()));
						    
						  //SE AGREGA PROCEDIMIENTO ALMACENADO DE PEREDO PARA CONTABILIZAR PEDIDOS 28/JUL/2013
							//StoreProcedurePedidos sp = new StoreProcedurePedidos(getJdbcTemplate().getDataSource());
							//Map result = sp.execute(cve_ped, 1);
						  //TERMINA PROCEDIMIENT0
					}
					else{
						throw new RuntimeException("El monto del Pedido actual es mayor al precompromiso de la Requisición<br>- La Requisición no esta precomprometiendo el recurso del periodo actual");
					}
             /* } 
           });
           */
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	/*Metodo que se usa para descontar la cantidad de lotes a los lotes de una requisicion*/
	public void descontarLotesRequisicion(Long cve_ped){
		try
		{
			int status=0, comprometido =0;
			Double dif = 0.0;
			//obtengo los lotes del pedido
			List <Map> lotes_ped = this.getConceptos(cve_ped);
			//recorrer los elementos para operar cada lote
			for(Map row: lotes_ped){
				//verificar primero que el lote que se guarda no exeda a la cantidad en req_movtos
				if(Double.parseDouble(row.get("CANTIDAD2").toString()) >= Double.parseDouble(row.get("CANTIDAD").toString())){
					//determinar el status del lote en la requisicion, si sobra hay que dejarlo pendiente
					dif = (Double.parseDouble(row.get("CANTIDAD2").toString()) - Double.parseDouble(row.get("CANTIDAD").toString()));
					if (dif==0){ 
						status = this.gatewayRequisicion.REQ_MOVTO_SURTIDO;
						comprometido = 0;
					}
					else{
						status = this.gatewayRequisicion.REQ_MOVTO_SOLICITADO;
						comprometido = 1;
					}
					//actualiza los lotes de la requisicion
					this.getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET CANTIDAD = ?, STATUS=?, COMPROMETIDO = ? WHERE ID_REQ_MOVTO = ?", new Object[]{dif, status, comprometido, Long.parseLong(row.get("ID_REQ_MOVTO").toString())});
				}
				else
				{
					status = this.gatewayRequisicion.REQ_MOVTO_SURTIDO;
					comprometido = 0;
					//actualiza los lotes de la requisicion
					this.getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET CANTIDAD = ?, STATUS=?, COMPROMETIDO = ? WHERE ID_REQ_MOVTO = ?", new Object[]{dif, status, comprometido, Long.parseLong(row.get("ID_REQ_MOVTO").toString())});
				}
			}
			
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
		}
	}
	
	/*Metodo para recuperar la informacion de un lote de pedido*/
	public Map getConcepto(long id_ped_movto){
		try
		{
			return this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_PED_MOVTOS WHERE ID_PED_MOVTO = ?", new Object[]{id_ped_movto});
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			return null;
		}
	}
	
	/*Metodo que se usa para devolover la cantidad de lote a los lotes de una requisicion*/
	public void devolverLotesRequisicion(Long ID_MOV_PED){
		try
		{
			//obtengo el lote del pedido
			Map lote = this.getConcepto(ID_MOV_PED);
			//actualiza el lote que corresponde a la requisicion
			this.getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET CANTIDAD = (CANTIDAD_TEMP), STATUS = ?, COMPROMETIDO = ? WHERE ID_REQ_MOVTO = ?", new Object[]{this.PED_MOVTO_PENDIENTE, 1, lote.get("ID_REQ_MOVTO").toString()});
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
		}
	}
	
	public void moverLotes(Long id_ped_movto, int cve_pers, Long cve_ped_fuente, Long cve_ped_destino, int ped_cons, int ejercicio){	 
		Date fecha = new Date();	
		String SQL = "UPDATE SAM_PED_MOVTOS SET CVE_PED =?, PED_CONS=? WHERE ID_PED_MOVTO =?";
		this.getJdbcTemplate().update(SQL, new Object[]{cve_ped_destino, ped_cons, id_ped_movto});
		//Guarda en la bitacora
		//gatewayBitacora.guardarBitacora(cve_ped_fuente.toString(), cve_ped_fuente, "PED", fecha, 0D, "X", "0", "", "", ejercicio, cve_pers, gatewayBitacora.MOVIO_LOTE_PED);
	}
	
	public String getComboPedidosRequisicion(Long cve_req, Long cve_ped){
		String combo = "<select name='cbopedidos' class='comboBox' id='cbopedidos' style='width:120px'><option value='0'>[Seleccione]</option>";
		List <Map> dataset = this.getJdbcTemplate().queryForList("SELECT CVE_PED, NUM_PED FROM SAM_PEDIDOS_EX WHERE CVE_REQ = ? and CVE_PED NOT IN(?) AND STATUS IN(0)", new Object[]{cve_req, cve_ped});
		for(Map row: dataset){
			combo = combo + "<option value='"+row.get("CVE_PED").toString()+"'>"+row.get("NUM_PED").toString()+"</option>";
		}
		combo = combo + "</select>";
		return combo;
	}
	
	public void eliminarTodosLotesPedido(Long cve_ped){
		this.getJdbcTemplate().update("DELETE FROM SAM_PED_MOVTOS WHERE CVE_PED = ?", new Object []{cve_ped});	
	}
	
	public Boolean reactivarPedido(Long cve_ped, int ejercicio, int cve_pers){
		try{
			String num_ped = this.rellenarCeros(cve_ped.toString(), 6);
			/*Reactivar el pedido*/
			this.getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET STATUS = ? WHERE CVE_PED = ?", new Object[]{this.PED_STATUS_NUEVO, cve_ped});
			/*Guardar el movimiento en bitacora*/
			Map ped = this.getJdbcTemplate().queryForMap("SELECT NUM_PED, FECHA_PED, TOTAL FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{cve_ped});
			gatewayBitacora.guardarBitacora(gatewayBitacora.REACTIVA_PEDIDO, ejercicio, cve_pers, cve_ped, num_ped, "PED", (Date) ped.get("FECHA_PED"), null, null, null,null);
			return true;
		}
		catch (DataAccessException e) {            	                    
            throw new RuntimeException(e.getMessage(),e);
       }
	}

	public Map getImporteDisponiblePedido(Long cve_ped){
		return this.getJdbcTemplate().queryForMap("SELECT CVE_PED, NUM_PED, TOTAL, (TOTAL - (SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_ORD_PAGO WHERE CVE_PED = SAM_PEDIDOS_EX.CVE_PED AND STATUS IN (0,1))) AS DISPONIBLE FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{cve_ped});
	}
	
	
	public void reenumerarLotes(Long cve_ped){
		List <Map> dataset = this.getConceptos(cve_ped);
		Integer i=1;
		for(Map row: dataset){
			this.getJdbcTemplate().update("UPDATE SAM_PED_MOVTOS SET PED_CONS =? WHERE ID_PED_MOVTO =?", new Object[]{i, row.get("ID_PED_MOVTO")}); 
			i++;
		}
	}
	
	public boolean cambiarFechaPeriodo(final Long cve_ped, final String fechaNueva, final int cve_pers, final int ejercicio){
		try {   
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	Date fecha = formatoFecha(fechaNueva);
	            	/*Graba los movimientos en bitacora*/
	            	Map ped = getJdbcTemplate().queryForMap("SELECT NUM_PED, FECHA_PED, TOTAL FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{cve_ped});
	    			gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_PEDIDO, ejercicio, cve_pers, cve_ped, ped.get("NUM_PED").toString(), "PED", fecha, null, null, "Cambió fecha del Pedido de: "+ped.get("FECHA_PED")+" a "+fechaNueva,Double.parseDouble(ped.get("TOTAL").toString()));
	    			/*realiza el cambio*/
	            	getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET FECHA_PED = ? WHERE CVE_PED = ?", new Object[]{fecha, cve_ped});
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, puede que la fecha este escrita incorrectamente, verifique nuevamente el formato y vuelta a intentar esta operación",e);
	   }	
	  
	}
	
	public boolean moverPedidos(final Long[] cve_ped, final int cve_pers_fuente, final int cve_pers_dest, final int ejercicio){
		try {   
			
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	for(Long id: cve_ped){
	            		/*Graba en bitacora el cambio*/
	            		Map ped = getJdbcTemplate().queryForMap("SELECT NUM_PED, CONVERT(varchar(10),FECHA_PED,103) AS FECHA_PED, TOTAL FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{id});
	            		Map persona = getJdbcTemplate().queryForMap("SELECT TOP 1 (SELECT '('+US1.LOGIN+') '+NOMBRE+' '+APE_PAT+' '+APE_MAT FROM PERSONAS INNER JOIN USUARIOS_EX AS US1 ON (US1.CVE_PERS = PERSONAS.CVE_PERS) WHERE US1.CVE_PERS = ?) AS PERSONA1, (SELECT '('+US1.LOGIN+') '+NOMBRE+' '+APE_PAT+' '+APE_MAT FROM PERSONAS INNER JOIN USUARIOS_EX AS US1 ON (US1.CVE_PERS = PERSONAS.CVE_PERS) WHERE US1.CVE_PERS = ?) AS PERSONA2 FROM USUARIOS_EX", new Object[]{cve_pers_fuente, cve_pers_dest});
		            	gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_PEDIDO, ejercicio, cve_pers_fuente, id, ped.get("NUM_PED").toString(), "PED", formatoFecha(ped.get("FECHA_PED").toString()), null, null, "Cambio de usuario en el documento de: "+persona.get("PERSONA1").toString()+" a: "+persona.get("PERSONA2").toString(),Double.parseDouble(ped.get("TOTAL").toString()));
		            	/*Realiza el cambio*/
		            	getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET CVE_PERS = ? WHERE CVE_PED = ?", new Object[]{cve_pers_dest, id});
	            	}
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, el cambio de usuario no se ha podido realizar",e);
	   }	
	  
	}	
	
	public Map getBeneficiario(Long cve_doc){
		try {   
			return this.getJdbcTemplate().queryForMap("SELECT NUM_PED, CLV_BENEFI, (SELECT CAT_BENEFI.NCOMERCIA FROM CAT_BENEFI WHERE CAT_BENEFI.CLV_BENEFI = SAM_PEDIDOS_EX.CLV_BENEFI) AS BENEFICIARIO FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{cve_doc});
		}
		catch (DataAccessException e) {                               
	       return null;
	   }
	}
	
	
	public boolean cambiarBeneficiario(final Long cve_ped, final String clv_benefi, final int ejercicio, final int cve_pers){
		try {   
			
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            		/*Comprobar algunos datos antes*/
		            	Map ped = getJdbcTemplate().queryForMap("SELECT NUM_PED, CONVERT(varchar(10),FECHA_PED,103) AS FECHA_PED, TOTAL, SAM_PEDIDOS_EX.CLV_BENEFI, C.NCOMERCIA "+  
																	"FROM SAM_PEDIDOS_EX "+  
																		"LEFT JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = SAM_PEDIDOS_EX.CLV_BENEFI) "+
																		"WHERE SAM_PEDIDOS_EX.CVE_PED = ?", new Object[]{cve_ped});
		            	Map ben = getJdbcTemplate().queryForMap("SELECT NCOMERCIA FROM CAT_BENEFI WHERE CAT_BENEFI.CLV_BENEFI =?", new Object[]{clv_benefi});
	            		String texto ="";
	            		if(ped.get("CLV_BENEFI")!=null)
	            			texto = "Cambió el beneficiario CLV_BENEFI: ['"+ped.get("CLV_BENEFI").toString()+"' "+ped.get("NCOMERCIA").toString()+"] a: ['"+clv_benefi+"' "+ben.get("NCOMERCIA").toString()+"]";
	            		else
	            			texto = "Cambió el beneficiario CLV_BENEFI: ['' ] a: ['"+clv_benefi+"' "+ben.get("NCOMERCIA").toString()+"]";
		            	/*Graba en bitacora el cambio*/
		            	gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_PEDIDO, ejercicio, cve_pers, cve_ped, ped.get("NUM_PED").toString(), "PED", formatoFecha(ped.get("FECHA_PED").toString()), null, null, texto, Double.parseDouble(ped.get("TOTAL").toString()));
		            	/*Realiza el cambio*/
		            	getJdbcTemplate().update("UPDATE SAM_ORDEN_TRAB SET CLV_BENEFI = ? WHERE CVE_REQ = ?", new Object[]{clv_benefi, cve_ped});
		            	
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, el cambio de beneficiario no se ha podido realizar, intente editando el documento directamente",e);
	   }	
	}
	

	public Map getReembolsoPedido(Long cve_ped){
		return this.getJdbcTemplate().queryForMap("SELECT ISNULL((SELECT MONTO FROM VT_COMPROMISOS WHERE TIPO_DOC ='PED' AND CVE_DOC = SAM_PEDIDOS_EX.CVE_PED)-ISNULL(REEMBOLSO,0),0) AS REEMBOLSO_LIQ, ISNULL(REEMBOLSO,0) AS REEMBOLSO, TOTAL FROM SAM_PEDIDOS_EX WHERE CVE_PED =?", new Object[]{cve_ped});
	}
	
	public void gatewayPedidos(Long cve_ped, Double monto){
		if(monto==0){
			Map m = this.getReembolsoPedido(cve_ped);
			monto = Double.parseDouble(m.get("REEMBOLSO_LIQ").toString());
		}	
			
		this.getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET REEMBOLSO =? WHERE CVE_PED =?", new Object[]{monto, cve_ped});
	}
	
	public void quitarReembolso(Long cve_ped){
		this.getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET REEMBOLSO = ? WHERE CVE_PED =?", new Object[]{null,cve_ped});
	}
	
	public List<Map> getMovimientosAjustadosPedidos(Long cve_pedido)
	{
		return this.getJdbcTemplate().queryForList("SELECT MOV.*, CONVERT(varchar(10), MOV.FECHA_MOVTO, 103) AS FECHA_MOVTO , VP.N_PROGRAMA FROM SAM_MOD_COMP AS MOV INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = MOV.ID_PROYECTO) WHERE MOV.TIPO_DOC = ? AND MOV.DOCUMENTO =? ORDER BY MOV.FECHA_MOVTO DESC",new Object[]{2, cve_pedido});
	}
	
	public Map getProyectoPartidaPedido(Long cve_ped)
	{
		return this.getJdbcTemplate().queryForMap("SELECT R.ID_PROYECTO, R.CLV_PARTID, VP.N_PROGRAMA FROM SAM_PEDIDOS_EX AS P "+
													"INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) " +
													"INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = R.ID_PROYECTO) "+
												"WHERE P.CVE_PED = ?", new Object[]{cve_ped});
														
	}
	
	public void guardarAjustePedidoPeredo(Long id_sam_mod_comp, Long cve_factura, int idProyecto, String clv_partid, String fecha, Double importe)
	{
		if(id_sam_mod_comp==0)
			this.getJdbcTemplate().update("INSERT INTO SAM_MOD_COMP(TIPO_DOC, DOCUMENTO, ID_PROYECTO, CLV_PARTID, IMPORTE, FECHA_MOVTO) VALUES(?,?,?,?,?,?)", new Object[]{2, cve_factura, idProyecto, clv_partid, importe, this.formatoFecha(fecha)});
		else
			this.getJdbcTemplate().update("UPDATE SAM_MOD_COMP SET ID_PROYECTO=?, CLV_PARTID=?, IMPORTE=?, FECHA_MOVTO=? WHERE id_sam_mod_comp = ?", new Object[]{idProyecto, clv_partid, importe, this.formatoFecha(fecha), id_sam_mod_comp});
	}
	
	public void eliminarConceptoAjustePedido(final Long id_sam_mod_comp)
	{
		try{
			 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	             @Override
	             protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	getJdbcTemplate().update("DELETE FROM SAM_MOD_COMP WHERE id_sam_mod_comp =?", new Object[]{id_sam_mod_comp}); 
				 }
		    });
		}
		catch (DataAccessException e) {                                
          throw new RuntimeException("La operacion ha fallado, no se han podido eliminar los conceptos "+e.getMessage());
      }  
	}
}
