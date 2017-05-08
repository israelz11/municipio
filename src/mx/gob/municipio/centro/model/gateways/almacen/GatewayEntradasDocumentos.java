/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @Date 07/Jun/2010
 **/
package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBitacora;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPedidos;

public class GatewayEntradasDocumentos extends BaseGatewayAlmacen {
	private static Logger log = Logger.getLogger(GatewayEntradasDocumentos.class.getName());
	@Autowired
	GatewayPedidos gatewayPedidos;
	
	@Autowired
	public GatewayBitacoraAlmacen gatewayBitacoraAlmacen;
	
	public Long Id_Entrada;
	public GatewayEntradasDocumentos() {
		// TODO Auto-generated constructor stub
	}

	public Long guardarEntradaDocumento(final Long id_entrada, final int id_dependencia, final int id_almacen, final String id_proveedor, final Long id_pedido, final int id_tipo_documento, final String num_documento, final String proyecto, final String partida, final String descripcion, final Date fecha_documento, final int tipoEntrada, final Double subtotal, final Double descuento, final Double iva, final int tipoIva, final int cve_pers, final int tipoEfecto, final int movimiento, final Long idEntrada2){
		Id_Entrada = id_entrada;
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {   
            	if(Id_Entrada==null){
            		//guardar uno nuevo
            		Id_Entrada = guardar(id_dependencia, id_almacen, id_proveedor, id_pedido, id_tipo_documento, num_documento, proyecto, partida, descripcion, fecha_documento, tipoEntrada, cve_pers, tipoEfecto, movimiento, idEntrada2);
            		//getJdbcTemplate().update("INSERT INTO HISTORY(ID_ENTRADA, ID_DEPENDENCIA, CREATEDDATE, CVE_PERS) VALUES(?,?,?,?)", new Object[]{1,1,1,2});
            		
            		if(id_pedido!=null){
            			if(id_pedido!=0)
            				guardarDetalleDocumento(Id_Entrada, id_pedido);
            			    gatewayBitacoraAlmacen.guardarBitacoraAlmacen(gatewayBitacoraAlmacen.Actualiza_Entrada,Id_Entrada, cve_pers, id_almacen, id_dependencia, id_pedido, "ENTRADA",1,Integer.parseInt(id_proveedor), new Date(), null, subtotal);
            		}
            	}
            	else
            	{
            		//modificar uno existente
            		editar(id_entrada, id_dependencia, id_almacen, id_proveedor, id_pedido, id_tipo_documento, num_documento, proyecto, partida, descripcion, fecha_documento, tipoEntrada, subtotal, descuento, iva, tipoIva, tipoEfecto, movimiento, idEntrada2);
            		//getJdbcTemplate().update("INSERT INTO HISTORY(ID_ENTRADA, ID_DEPENDENCIA, CREATEDDATE, CVE_PERS) VALUES(?,?,?,?)", new Object[]{1,1,1,2});
            		//gatewayBitacora.guardarBitacora(gatewayBitacora.OP_NUEVA_ORDEN, ejercicio, cve_pers, cveOp, folio, "OP", fecha, null, null, null, null);
            		
            		//CVE_DOC, CVE_PERS, ID_ALMACEN, ID_DEPENDENCIA, ID_PEDIDO, TIPO_DOC, ID_ARTICULO, ID_PROVEEDOR, FECHA,FECHA_DOC,DESCRIPCION,MONTO
            		
            		//comprobar que existen detalles al guardar. si es asi aun que haya pedido no se anexaran nuevos detalles
            		if(!comprobarDetalles(id_entrada)){
            			guardarDetalleDocumento(Id_Entrada, id_pedido);
            			gatewayBitacoraAlmacen.guardarBitacoraAlmacen(gatewayBitacoraAlmacen.Actualiza_Entrada,Id_Entrada, cve_pers, id_almacen, id_dependencia, id_pedido, "ENTRADA",1,Integer.parseInt(id_proveedor), new Date(), null, subtotal);
            		}
            	}
            } 
        });  
		return Id_Entrada;
	}
	
	private boolean comprobarDetalles(Long id_entrada){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM DETALLES_ENTRADAS WHERE ID_ENTRADA = ?", new Object[]{id_entrada})>0;
	}
	
	//final Long id_entrada, final int id_dependencia, final int id_almacen, final String id_proveedor, final Long id_pedido, final int id_tipo_documento, final String num_documento, final String proyecto, final String partida, final String descripcion, final Date fecha_documento, final int tipoEntrada, final Double subtotal, final Double descuento, final Double iva, final int tipoIva, final int cve_pers, final int tipoEfecto, final int movimiento, final Long idEntrada2)
	private void guardarDetalleDocumento(Long id_entrada, Long id_pedido){
		Long id_art = 0L; 
		int tipo_entrada = this.getJdbcTemplate().queryForInt("SELECT ID_TIPO_ENTRADA FROM ENTRADAS WHERE ID_ENTRADA =?", new Object[]{id_entrada});
		List <Map> resultado = this.gatewayPedidos.getConceptos(id_pedido);
		
		Date fecha = new Date();
		for(Map row: resultado){
			if(this.comprobarLoteAntes(Long.parseLong(row.get("ID_PED_MOVTO").toString()))>0){
				id_art = Long.parseLong(row.get("ID_ARTICULO").toString());
				this.getJdbcTemplate().update("INSERT INTO DETALLES_ENTRADAS(ID_ENTRADA, ID_ARTICULO, ID_PED_MOVTO, ID_UNIDAD_MEDIDA, CANTIDAD, PRECIO, DESCRIPCION, FECHA, STATUS) VALUES(?,?,?,?,?,?,?,?,?)", new Object []{id_entrada, id_art, row.get("ID_PED_MOVTO").toString() , row.get("CLV_UNIMED").toString(), ((tipo_entrada==2) ? 0: row.get("CANTIDAD").toString()), row.get("PRECIO_UNI").toString(), row.get("DESCRIP").toString(), fecha, 0});
			}
		}
	}

	private Long getIdArticulo(Long id){
		return this.getJdbcTemplate().queryForLong("SELECT TOP 1 ID_ARTICULO FROM CAT_PROD WHERE ID_CAT_ARTICULO = ?", new Object []{id});
	}
	
	public Map getConcepto(Long id_detalle_entrada){
		return this.getJdbcTemplate().queryForMap("SELECT ID_DETALLE_ENTRADA, DETALLES_ENTRADAS.ID_ARTICULO, ID_UNIDAD_MEDIDA, DETALLES_ENTRADAS.ID_FAMILIA, CANTIDAD, PRECIO, DETALLES_ENTRADAS.DESCRIPCION, DETALLES_ENTRADAS.STATUS, FAMILIAS.DESCRIPCION AS FAMILIA, SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, CAT_UNIMED.UNIDMEDIDA FROM DETALLES_ENTRADAS INNER JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = DETALLES_ENTRADAS.ID_ARTICULO) INNER JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO) INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = DETALLES_ENTRADAS.ID_UNIDAD_MEDIDA) LEFT JOIN FAMILIAS ON (FAMILIAS.ID_FAMILIA = DETALLES_ENTRADAS.ID_FAMILIA) WHERE ID_DETALLE_ENTRADA = ?", new Object[]{id_detalle_entrada});
	}
	
	public List <Map> getConceptos(long id_entrada){
		return this.getJdbcTemplate().queryForList("SELECT "+
															"ID_DETALLE_ENTRADA, "+
															"ID_INVENTARIO,  "+
															"DETALLES_ENTRADAS.ID_ARTICULO,  "+
															"DETALLES_ENTRADAS.ID_PED_MOVTO,  "+
															"ID_UNIDAD_MEDIDA,  "+
															"DETALLES_ENTRADAS.ID_FAMILIA,  "+
															"DETALLES_ENTRADAS.CANTIDAD,  "+
															"(SAM_PED_MOVTOS.CANTIDAD - ISNULL((SELECT SUM(D.CANTIDAD) FROM DETALLES_ENTRADAS AS D WHERE D.STATUS=1 AND D.ID_PED_MOVTO = DETALLES_ENTRADAS.ID_PED_MOVTO),0)) AS CANTIDAD_PED,  "+
															"DETALLES_ENTRADAS.PRECIO,  "+
															"DETALLES_ENTRADAS.DESCRIPCION,  "+
															"DETALLES_ENTRADAS.STATUS,  "+
															"FAMILIAS.DESCRIPCION AS FAMILIA,  "+
															"SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, "+
															"SAM_PED_MOVTOS.DESCRIP AS DESC_ART,  "+
															"CAT_UNIMED.UNIDMEDIDA ,"+
															"SAM_PEDIDOS_EX.SUBTOTAL,"+
															"SAM_PEDIDOS_EX.IVA, "+
															"SAM_PEDIDOS_EX.DESCUENTO," +
															"SAM_PEDIDOS_EX.TOTAL, "+
															"(CASE ISNULL(DETALLES_ENTRADAS.ID_PED_MOVTO,0) WHEN 0 THEN DETALLES_ENTRADAS.LOTE ELSE (SELECT PED_CONS FROM SAM_PED_MOVTOS WHERE ID_PED_MOVTO = DETALLES_ENTRADAS.ID_PED_MOVTO) END) AS LOTE "+
														"FROM DETALLES_ENTRADAS  "+
															"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = DETALLES_ENTRADAS.ID_ARTICULO) "+
															"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO) "+
															"LEFT JOIN FAMILIAS ON (FAMILIAS.ID_FAMILIA = DETALLES_ENTRADAS.ID_FAMILIA)  "+
															"LEFT JOIN SAM_PED_MOVTOS ON (SAM_PED_MOVTOS.ID_PED_MOVTO = DETALLES_ENTRADAS.ID_PED_MOVTO) "+
															"LEFT JOIN SAM_PEDIDOS_EX ON (SAM_PEDIDOS_EX.CVE_PED = SAM_PED_MOVTOS.CVE_PED) " +
															"INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = DETALLES_ENTRADAS.ID_UNIDAD_MEDIDA) "+
														"WHERE ID_ENTRADA = ? ORDER BY SAM_PED_MOVTOS.PED_CONS ASC", new Object[]{id_entrada});
	}
	
	private void editar(Long id_entrada, int id_dependencia, int id_almacen, String id_proveedor, Long id_pedido, int id_tipo_documento, String num_documento, String proyecto, String partida, String descripcion, Date fecha_documento, int tipoEntrada, Double subtotal, Double descuento, Double iva, int tipoIva, int tipoEfecto, int movimiento, Long idEntrada2){
		Long cve_ped = this.getJdbcTemplate().queryForLong("SELECT ISNULL(ID_PEDIDO,0) AS ID_PEDIDO FROM ENTRADAS WHERE ID_ENTRADA =? ", new Object[]{id_entrada});
		if(cve_ped!=0){
			if(tipoEntrada==1){
				Map pedido = this.getJdbcTemplate().queryForMap("SELECT ISNULL(SUBTOTAL, '0') AS SUBTOTAL, ISNULL(DESCUENTO, '0') AS DESCUENTO, ISNULL(IVA, '0') AS IVA, ISNULL(TIPO_IVA, '0') AS TIPO_IVA, ISNULL(TOTAL, '0') AS TOTAL FROM SAM_PEDIDOS_EX WHERE CVE_PED = ? ", new Object[]{id_pedido});
				subtotal = Double.parseDouble(pedido.get("SUBTOTAL").toString());
				descuento = Double.parseDouble(pedido.get("DESCUENTO").toString());
				iva = Double.parseDouble(pedido.get("IVA").toString());
				tipoIva = Integer.parseInt(pedido.get("TIPO_IVA").toString());
			}
			Double total = (subtotal-descuento)+iva;
			this.getJdbcTemplate().update("UPDATE ENTRADAS SET ID_DEPENDENCIA=?, ID_ALMACEN=?, ID_ENTRADA_CANCELADA=?, ID_ENTRADA_AGREGADA=?, ID_PROVEEDOR=?, ID_PEDIDO=?, ID_TIPO_DOCUMENTO=?, ID_TIPO_ENTRADA=?, DOCUMENTO=?, DESCRIPCION=?, ID_PROYECTO=?, PARTIDA=?, FECHA_DOCUMENTO=?, FECHA_OFICIAL=?, SUBTOTAL=?, DESCUENTO=?, IVA=?, TIPO_IVA=?, TOTAL=? WHERE ID_ENTRADA = ?", new Object[]{id_dependencia, id_almacen, (movimiento==2 ? idEntrada2: null), (movimiento==1 ? idEntrada2: null), id_proveedor, id_pedido, id_tipo_documento, tipoEntrada, num_documento, descripcion, proyecto, partida, fecha_documento, (tipoEfecto==0 ? fecha_documento: null), subtotal, descuento, iva, tipoIva, total, id_entrada});
			//es increible que no detecte que la comparacion es distinta if(cve_ped!=id_pedido){	
			if(cve_ped==id_pedido){	
				this.getJdbcTemplate().update("DELETE FROM DETALLES_ENTRADAS WHERE ID_ENTRADA = ?", new Object[]{id_entrada, });
				this.guardarDetalleDocumento(id_entrada, id_pedido);
			}
			log.info("Actualiza ENTRADA cuando hay Pedido SUBTOTAL: "+subtotal.toString()+" DESCUENTO: "+descuento.toString()+" IVA: "+iva.toString()+" TIPO_IVA: "+tipoIva+" TOTAL: "+total.toString());
		}
		else{
			Double total = 0.00;
			if(tipoEntrada==1){
				Map pedido = this.getJdbcTemplate().queryForMap("SELECT ISNULL(SUBTOTAL, '0') AS SUBTOTAL, ISNULL(DESCUENTO, '0') AS DESCUENTO, ISNULL(IVA, '0') AS IVA, ISNULL(TIPO_IVA, '0') AS TIPO_IVA, ISNULL(TOTAL, '0') AS TOTAL FROM SAM_PEDIDOS_EX WHERE CVE_PED = ? ", new Object[]{id_pedido});
				subtotal = Double.parseDouble(pedido.get("SUBTOTAL").toString());
				descuento = Double.parseDouble(pedido.get("DESCUENTO").toString());
				iva = Double.parseDouble(pedido.get("IVA").toString());
				tipoIva = Integer.parseInt(pedido.get("TIPO_IVA").toString());
			}
			total = (subtotal-descuento)+iva;
			this.getJdbcTemplate().update("UPDATE ENTRADAS SET ID_ALMACEN=?, ID_ENTRADA_CANCELADA=?, ID_ENTRADA_AGREGADA=?, ID_PROVEEDOR=?, ID_PEDIDO=?, ID_TIPO_DOCUMENTO=?, DOCUMENTO=?, DESCRIPCION=?, ID_PROYECTO=?, PARTIDA=?, FECHA_DOCUMENTO=?, FECHA_OFICIAL=?, SUBTOTAL=?, DESCUENTO=?, IVA=?, TIPO_IVA=?, TOTAL=? WHERE ID_ENTRADA = ?", new Object[]{id_almacen, (movimiento==2 ? idEntrada2: null), (movimiento==1 ? idEntrada2: null), id_proveedor, id_pedido, id_tipo_documento, num_documento, descripcion, proyecto, partida, fecha_documento, (tipoEfecto==0 ? fecha_documento: null), subtotal, descuento, iva, tipoIva, total, id_entrada});
			log.info("Actualiza ENTRADA cuando no hay Pedido SUBTOTAL: "+subtotal.toString()+" DESCUENTO: "+descuento.toString()+" IVA: "+iva.toString()+" TIPO_IVA: "+tipoIva+" TOTAL: "+total.toString());
		}
	}
	
	public Long guardar(int id_dependencia, int id_almacen, String id_proveedor, Long id_pedido, int id_tipo_documento, String num_documento, String proyecto, String partida, String descripcion, Date fecha_documento, int tipoEntrada, int cve_pers, int tipoEfecto, int movimiento, Long idEntrada2){
		Date fecha_cap = new Date();
		Long folio = getNumeroSiguiente(id_dependencia, id_almacen);
		String num_folio = this.rellenarCeros(folio.toString(), 6);
		int idGrupo = this.getJdbcTemplate().queryForInt("SELECT B.ID_GRUPO_CONFIG FROM SAM_GRUPO_CONFIG_USUARIO A JOIN  SAM_GRUPO_CONFIG B ON A.ID_GRUPO_CONFIG = B.ID_GRUPO_CONFIG and ID_USUARIO=? where b.ESTATUS='ACTIVO' and B.TIPO='FIRMA' AND A.ASIGNADO =1", new Object[]{cve_pers});
		Map pedido = this.getJdbcTemplate().queryForMap("SELECT ISNULL(SUBTOTAL, '0') AS SUBTOTAL, ISNULL(DESCUENTO, '0') AS DESCUENTO, ISNULL(IVA, '0') AS IVA, ISNULL(TIPO_IVA, '0') AS TIPO_IVA, ISNULL(TOTAL, '0') AS TOTAL FROM SAM_PEDIDOS_EX WHERE CVE_PED = ? ", new Object[]{id_pedido});
		this.getJdbcTemplate().update("INSERT INTO ENTRADAS(FOLIO, ID_DEPENDENCIA, ID_ALMACEN, ID_ENTRADA_CANCELADA, ID_ENTRADA_AGREGADA, ID_PROVEEDOR, ID_PEDIDO, ID_TIPO_DOCUMENTO, ID_TIPO_ENTRADA, ID_GRUPO, ID_PERSONA, DOCUMENTO, DESCRIPCION, ID_PROYECTO, PARTIDA, FECHA_DOCUMENTO, FECHA, FECHA_OFICIAL, SUBTOTAL, DESCUENTO, IVA, TIPO_IVA, TOTAL, STATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{num_folio, id_dependencia, id_almacen, (movimiento==2 ? idEntrada2:null),  (movimiento==1 ? idEntrada2:null), id_proveedor, id_pedido, id_tipo_documento, tipoEntrada, idGrupo, cve_pers, num_documento, descripcion, proyecto, partida, fecha_documento, fecha_cap,  (tipoEfecto==0? fecha_documento :null), pedido.get("SUBTOTAL"), pedido.get("DESCUENTO"), pedido.get("IVA"), pedido.get("TIPO_IVA"), pedido.get("TOTAL"), 1});
		log.info("Inserta nueva ENTRADA - SUBTOTAL: "+pedido.get("SUBTOTAL").toString()+" DESCUENTO: "+pedido.get("DESCUENTO").toString()+" IVA: "+pedido.get("IVA").toString()+" TIPO_IVA: "+pedido.get("TIPO_IVA").toString()+" TOTAL: "+pedido.get("TOTAL").toString());
		
		return this.getJdbcTemplate().queryForLong("SELECT MAX(ID_ENTRADA) AS N FROM ENTRADAS");
	}
	
	
	private Long getNumeroSiguiente(int id_dependencia, int id_almacen){
		if(id_almacen==0)
			return (this.getJdbcTemplate().queryForLong("SELECT COUNT(*) AS N FROM ENTRADAS WHERE ID_ALMACEN = 0"))+1;
		else
			return (this.getJdbcTemplate().queryForLong("SELECT COUNT(*) AS N FROM ENTRADAS WHERE ID_ALMACEN =? ", new Object[]{id_almacen}))+1;	
	}
	
	public boolean guardarDetallesEntradaDocumentos(Long id_entrada, Long id_detalle_entrada, int id_articulo, String id_unidad_medida, int id_familia, double cantidad, double precio, String descripcion){
		try{
			Date fecha = new Date();
			if(id_detalle_entrada==null) id_detalle_entrada =0L;
			if(id_detalle_entrada==0){
				int lote = this.getJdbcTemplate().queryForInt("SELECT MAX(LOTE) FROM DETALLES_ENTRADAS WHERE ID_ENTRADA = ?", new Object[]{id_entrada})+1;
				this.getJdbcTemplate().update("INSERT INTO DETALLES_ENTRADAS(ID_ENTRADA, ID_ARTICULO, LOTE, ID_UNIDAD_MEDIDA, ID_FAMILIA, CANTIDAD, PRECIO, FECHA, DESCRIPCION, STATUS) VALUES(?,?,?,?,?,?,?,?,?,?)", new Object[]{id_entrada, id_articulo, lote, id_unidad_medida, id_familia, cantidad, precio, fecha, descripcion, 0});
				return true;
			}
			else{
				this.getJdbcTemplate().update("UPDATE DETALLES_ENTRADAS SET ID_ARTICULO = ?, ID_UNIDAD_MEDIDA = ?, ID_FAMILIA = ?, CANTIDAD = ?, PRECIO = ?, DESCRIPCION = ? WHERE ID_DETALLE_ENTRADA = ?", new Object[]{id_articulo, id_unidad_medida, id_familia, cantidad, precio, descripcion, id_detalle_entrada});
				return true;
			}
		}
	
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			return false;
		}		
	}
	
	public Map getEntradaDocumento(Long id_entrada){
		try  
		{
			return this.getJdbcTemplate().queryForMap("SELECT ID_ENTRADA, "+
																"ENTRADAS.ID_DEPENDENCIA, "+
																"ENTRADAS.ID_ALMACEN,  "+
																"ID_PROVEEDOR, "+
																"ID_TIPO_DOCUMENTO,  "+
																"ENTRADAS.ID_GRUPO, "+
																"ENTRADAS.ID_TIPO_ENTRADA,  "+
																"ID_ENTRADA_AGREGADA, "+
																"ID_ENTRADA_CANCELADA, "+
																"ID_PEDIDO,  "+
																"ENTRADAS.FOLIO,  "+
																"ISNULL(ENTRADAS.SUBTOTAL, '0.00') AS SUBTOTAL, "+
																"ISNULL(ENTRADAS.DESCUENTO,'0.00') AS DESCUENTO, "+
																"ISNULL(ENTRADAS.IVA, '0.00') AS IVA, " +
																"ISNULL(ENTRADAS.TOTAL, '0.00') AS TOTAL,"+
																"ENTRADAS.TIPO_IVA, "+
																"DOCUMENTO, "+
																"ENTRADAS.DESCRIPCION,  "+
																"TIPO_ENTRADA.DESCRIPCION AS TIPO_ENTRADA_DESC,"+
																"SAM_REQUISIC.NUM_REQ,  "+
																"SAM_PEDIDOS_EX.NUM_PED,  "+
																"VPROYECTO.N_PROGRAMA+'['+CONVERT(VARCHAR, ENTRADAS.ID_PROYECTO)+']' AS N_PROGRAMA_DESC,  "+
																"ENTRADAS.ID_PROYECTO,  "+
																"VPROYECTO.N_PROGRAMA,  "+
																"VPROYECTO.PROG_PRESUP,  "+
																"ENTRADAS.PARTIDA,  "+
																"CAT_DEPENDENCIAS.DEPENDENCIA, "+
																"CAT_PARTID.PARTIDA AS DESC_PART,  "+
																"CONVERT(varchar(10), ENTRADAS.FECHA_DOCUMENTO,103) AS FECHA_DOCUMENTO,  "+
																"CONVERT(varchar(10), ENTRADAS.FECHA_CIERRE,103) AS FECHA_CIERRE,  "+
																"CONVERT(varchar(10), ENTRADAS.FECHA,103) AS FECHA,  "+
																"ENTRADAS.FECHA_OFICIAL,  "+
																"ENTRADAS.STATUS,  "+
																"ISNULL(ALMACEN.ALIAS,'') AS ALIAS,"+
																"(SELECT DESCRIPCION FROM ALMACEN WHERE ALMACEN.ID_ALMACEN = ENTRADAS.ID_ALMACEN) AS ALMACEN,  "+
																"(SELECT NCOMERCIA FROM CAT_BENEFI WHERE CLV_BENEFI = ENTRADAS.ID_PROVEEDOR) AS PROVEEDOR,  "+
																"(SELECT DESCRIPCION FROM TIPOS_DOCUMENTOS WHERE TIPOS_DOCUMENTOS.ID_TIPO_DOCUMENTO = ENTRADAS.ID_TIPO_DOCUMENTO) AS TIPO_DOCUMENTO  "+
															"FROM ENTRADAS  "+
																"INNER JOIN VPROYECTO ON (VPROYECTO.ID_PROYECTO = ENTRADAS.ID_PROYECTO)  "+
																"INNER JOIN CAT_PARTID ON (CAT_PARTID.CLV_PARTID = ENTRADAS.PARTIDA)  "+
																"LEFT JOIN SAM_PEDIDOS_EX ON (SAM_PEDIDOS_EX.CVE_PED = ENTRADAS.ID_PEDIDO)  "+
																"LEFT JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_PEDIDOS_EX.CVE_REQ)  "+
																"LEFT JOIN TIPO_ENTRADA ON (TIPO_ENTRADA.ID_TIPO_ENTRADA = ENTRADAS.ID_TIPO_ENTRADA) "+
																"LEFT JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = ENTRADAS.ID_DEPENDENCIA) "+
																"LEFT JOIN ALMACEN ON (ALMACEN.ID_ALMACEN = ENTRADAS.ID_ALMACEN) "+
															"WHERE ID_ENTRADA = ?", new Object[]{id_entrada});
		}
		catch ( DataAccessException e) {
			return null;
		}
	}
	
	public void eliminarConceptos(Long id){
		this.getJdbcTemplate().update("DELETE FROM DETALLES_ENTRADAS WHERE ID_DETALLE_ENTRADA = ?", new Object[]{id});
	}
	
	public String cerrarEntradaDocumento(final Long id_entrada){
		
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {   
            	Map documento = getEntradaDocumento(id_entrada);
            	List <Map> result = getConceptos(id_entrada);
            	Vector vector_ped_movtos = new Vector();
            	Date fecha = new Date();
            	for (Map row:result)
            	{
            		//comprobar la existencia de la resta en pedidos
            		if(!comprobarLote(Long.parseLong(row.get("ID_DETALLE_ENTRADA").toString()), Double.parseDouble(row.get("CANTIDAD").toString()))){
            			throw new RuntimeException("No se puede completar la operación, la cantidad de lote excede al disponible en Pedido");
            		}
            		//si existen elementos en el detalle de documentos que no han sido cargados al INVENTARIO
            		if(row.get("STATUS").toString()=="false"){
            			//buscar la existencia del articulo en INVENTARIO
            			boolean buscar = getExistenciaInventario(Long.parseLong(row.get("ID_ARTICULO").toString()), Integer.parseInt(documento.get("ID_DEPENDENCIA").toString()), Integer.parseInt(documento.get("ID_ALMACEN").toString()));
            			//de ser comprobada solo autualiza la cantidad en el INVENTARIO 
            			if(buscar){
            				Long idInventario = getIDInventario(Long.parseLong(row.get("ID_ARTICULO").toString()), Integer.parseInt(documento.get("ID_DEPENDENCIA").toString()), Integer.parseInt(documento.get("ID_ALMACEN").toString()));
            				//modificar la existencia actual EN INVENTARIO
            				getJdbcTemplate().update("UPDATE INVENTARIO SET CANTIDAD = (CANTIDAD + ?) WHERE ID_INVENTARIO = ?", new Object[]{(Double.parseDouble(row.get("CANTIDAD").toString())), idInventario});
            				//actualizar el id_inventario de la tabla DETALLES_ENTRADA
            				getJdbcTemplate().update("UPDATE DETALLES_ENTRADAS SET ID_INVENTARIO = ? WHERE ID_DETALLE_ENTRADA = ?", new Object[]{idInventario, row.get("ID_DETALLE_ENTRADA")});
            				
            				vector_ped_movtos.add(Long.parseLong(row.get("ID_PED_MOVTO").toString()));
            			}
            			else{
            				//de caso contrario guarda un nuevo elemento en el INVENTARIO
            				Long num = (getFolioAlmacen(Long.parseLong(documento.get("ID_ALMACEN").toString())))+1;
            				String folio = rellenarCeros(num.toString(),6);
            				//nuevo elemento al almacen
            				getJdbcTemplate().update("INSERT INTO INVENTARIO(ID_DEPENDENCIA, ID_ALMACEN, ID_ARTICULO, ID_UNIDAD_MEDIDA, ID_FAMILIA, ID_PROVEEDOR, FOLIO, PRECIO, CANTIDAD, EXISTENCIA_MINIMA, FECHA, ALARMA, STATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)",
            										new Object[]{documento.get("ID_DEPENDENCIA"),
            													documento.get("ID_ALMACEN"), 
            													row.get("ID_ARTICULO"),
            													row.get("ID_UNIDAD_MEDIDA").toString(),
            													row.get("ID_FAMILIA"),
            													documento.get("ID_PROVEEDOR").toString(),
            													folio,
            													row.get("PRECIO").toString(),
            													row.get("CANTIDAD").toString(),
            													1,
            													fecha,
            													0,
            													1});
            				//Obtener el ultimo id insertado en inventario
            				Long id_inv_temp = getJdbcTemplate().queryForLong("SELECT MAX(ID_INVENTARIO) AS N FROM INVENTARIO");
            				
            				//guardar el ultimo id del inventario en el detalle que genero la entrada del elemento al inventario
            				getJdbcTemplate().update("UPDATE DETALLES_ENTRADAS SET ID_INVENTARIO = ? WHERE ID_DETALLE_ENTRADA = ?", new Object[]{id_inv_temp, Long.parseLong(row.get("ID_DETALLE_ENTRADA").toString())});
            				
            				
            			}
            			
            		}
            	}
            	
            	//marcar los elemementos del documento activos lo que significa q se han cargado al almacen
    			getJdbcTemplate().update("UPDATE DETALLES_ENTRADAS SET STATUS = ? WHERE ID_ENTRADA = ?", new Object[]{1, id_entrada});
    			//Marcar la fecha de cierre del documento
    			getJdbcTemplate().update("UPDATE ENTRADAS SET FECHA_CIERRE = ?, STATUS = ? WHERE ID_ENTRADA = ?", new Object[]{fecha, 1,id_entrada});
    			//aqui cachamos los detalles del sam_ped_movtos
    			
    			//aqui cachamos los detalles del sam_ped_movtos
    			
    			//vectorDetalle.add(Long.parseLong(detalle.get("ID_DETALLE_ENTRADA").toString()));
    			for(int i=0; i<vector_ped_movtos.size(); i++){
    				finiquitar_pedido(Long.parseLong(vector_ped_movtos.get(i).toString()));
				}
            } 
        });  
		return "";
	}
	
	private Long getFolioAlmacen(Long id_almacen){
		return getJdbcTemplate().queryForLong("SELECT MAX(ID_INVENTARIO) AS N FROM INVENTARIO WHERE ID_ALMACEN = ?", new Object[]{id_almacen});
	}
	
	private boolean getExistenciaInventario(Long id_articulo, int id_dependencia, int id_almacen){
		if(id_almacen!=0) 
			return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM INVENTARIO WHERE STATUS = 1 AND ID_ALMACEN = ? AND ID_ARTICULO = ?", new Object[]{id_almacen, id_articulo})>0;
		else
			return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM INVENTARIO WHERE STATUS = 1 AND ID_DEPENDENCIA = ? AND ID_ARTICULO = ?", new Object[]{id_dependencia, id_articulo})>0;
	}
	
	private Long getIDInventario(Long id_articulo, int id_dependencia, int id_almacen){
		if(id_almacen!=0)
			return this.getJdbcTemplate().queryForLong("SELECT TOP 1 ID_INVENTARIO FROM INVENTARIO WHERE STATUS = 1 AND ID_ALMACEN = ? AND ID_ARTICULO = ?", new Object[]{id_almacen, id_articulo});
		else
			return this.getJdbcTemplate().queryForLong("SELECT TOP 1 ID_INVENTARIO FROM INVENTARIO WHERE STATUS = 1 AND ID_DEPENDENCIA = ? AND ID_ARTICULO = ?", new Object[]{id_dependencia, id_articulo});
	}
	
	private Map getMapArticulo(Long id_dependencia, Long id_almacen, Long id_articulo){
		try{
			if(id_almacen!=0)
				return this.getJdbcTemplate().queryForMap("SELECT ID_INVENTARIO, ID_ARTICULO, ID_UNIDAD_MEDIDA, FOLIO, PRECIO, CANTIDAD, STATUS FROM INVENTARIO WHERE ID_ALMACEN = ? and ID_ARTICULO = ?", new Object[]{id_almacen, id_articulo});
			else
				return this.getJdbcTemplate().queryForMap("SELECT ID_INVENTARIO, ID_ARTICULO, ID_UNIDAD_MEDIDA, FOLIO, PRECIO, CANTIDAD, STATUS FROM INVENTARIO WHERE ID_DEPENDENCIA = ? and ID_ARTICULO = ?", new Object[]{id_dependencia, id_articulo});
		}
		catch(DataAccessException e){
			return null;
		}
	}
	
	private boolean devolverArticulo(Long id_inventario, Long id_detalle_entrada){
		try{
				this.getJdbcTemplate().update("UPDATE INVENTARIO SET CANTIDAD = (CANTIDAD - (SELECT CANTIDAD FROM DETALLES_ENTRADAS WHERE ID_DETALLE_ENTRADA = ?)) WHERE ID_INVENTARIO = ?", new Object[]{id_detalle_entrada, id_inventario});
				this.getJdbcTemplate().update("UPDATE DETALLES_ENTRADAS SET STATUS = 0 WHERE ID_DETALLE_ENTRADA = ?", new Object[]{id_detalle_entrada});
				return true;
		}
		catch(DataAccessException e){
			return false;
		}
	}
	
	private boolean finiquitar_pedido(long id_ped_movto){
		try {
			this.getJdbcTemplate().update("UPDATE SAM_PED_MOVTOS SET STATUS =5 WHERE ID_PED_MOVTO = ?", new Object[]{id_ped_movto});
			return true;
		} catch (DataAccessException e) {
			return false;
		}
		
	}
	
	public void cancelarEntradaDocumento(final Long id_entrada, final int cve_pers){
		try{
			 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
						int valido =0;
						Vector vectorInventario = new Vector();
						Vector vectorDetalle = new Vector();
						String mensaje = "";
						//Validar que puede cancelar las entradas
						if(!getPrivilegioEn(cve_pers, 120))
							throw new RuntimeException("No se puede cancelar la entrada, su usuario no cuenta con los privilegios suficientes");
						
						if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM ENTRADAS WHERE ID_ENTRADA =? AND STATUS=1", new Object[]{id_entrada})>0)
							throw new RuntimeException("No se puede cancelar la entrada, invalidar primero");
						
						Map entrada = getEntradaDocumento(id_entrada);
						List <Map> conceptos = getConceptos(id_entrada);
						for(Map detalle: conceptos){
							Map detalle2 = getMapArticulo(Long.parseLong(entrada.get("ID_DEPENDENCIA").toString()), Long.parseLong(entrada.get("ID_ALMACEN").toString()), Long.parseLong(detalle.get("ID_ARTICULO").toString()));
							/*if(!detalle.get("PRECIO").toString().equals(detalle2.get("PRECIO").toString())&&detalle2.get("STATUS").toString().equals("true") || Double.parseDouble(detalle2.get("CANTIDAD").toString()) < Double.parseDouble(detalle.get("CANTIDAD").toString())) 
								valido++;
							else
								{*/
									vectorInventario.add(Long.parseLong(detalle2.get("ID_INVENTARIO").toString()));
									vectorDetalle.add(Long.parseLong(detalle.get("ID_DETALLE_ENTRADA").toString()));
								//}
						}
						/*if(valido>0) throw new RuntimeException("Imposible realiza la cancelación del documento actual por que se han modificado en el inventario los articulos ligados a este ó los artículos contenidos en el documento no se encuentran activos dentro del inventario.");
						else 
							{*/
								//Devolver aqui cada uno de los articulos del inventario hacia el documento fuente
								for(int i=0; i<vectorInventario.size(); i++){
									devolverArticulo(Long.parseLong(vectorInventario.get(i).toString()), Long.parseLong(vectorDetalle.get(i).toString()));
								}
								getJdbcTemplate().update("UPDATE ENTRADAS SET STATUS = 2 WHERE ID_ENTRADA = ?", new Object[]{id_entrada});
							//}
						
	                } 
             });
		}
		catch(DataAccessException e){
			 log.info(e.getMessage());	                    
             throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public void guardarCantidadDetalles(final Long identrada, final Long[] ids, final Double[] cantidades, final Double subtotal, final Double descuento, final Double iva, final int tipoIva){
		try {    
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
                	boolean exito = false;
                	Double total = (subtotal-descuento)+iva;
                	int i=0;
                	getJdbcTemplate().update("UPDATE ENTRADAS SET SUBTOTAL=?, DESCUENTO=?, IVA=?, TIPO_IVA=?, TOTAL=? WHERE ID_ENTRADA =?", new Object[]{subtotal, descuento, iva, tipoIva, total, identrada});
                	for (Long id :ids) {
                		//comprobar que no excede la cantidad a obtener del lote de pedido
                		if( comprobarLote(id, cantidades[i])){
                			getJdbcTemplate().update("UPDATE DETALLES_ENTRADAS SET CANTIDAD =? WHERE ID_DETALLE_ENTRADA =?", new Object[]{cantidades[i], id});
                		}
                		else
                			throw new RuntimeException("No se puede completar la operación, la cantidad de lote excede al disponible en Pedido");
                		i++;
                	}
                } 
             });
           
            } catch (DataAccessException e) {            
            	 log.info(e.getMessage());	                    
                 throw new RuntimeException(e.getMessage(),e);
            }	     
	}
	
	public boolean comprobarLote(Long id_detalle_entrada, Double cantidad){
		Double cant = (Double)this.getJdbcTemplate().queryForObject("SELECT (SAM_PED_MOVTOS.CANTIDAD - ISNULL((SELECT SUM(D.CANTIDAD) FROM DETALLES_ENTRADAS AS D WHERE D.STATUS=1 AND D.ID_PED_MOVTO = DETALLES_ENTRADAS.ID_PED_MOVTO),0)) AS N FROM DETALLES_ENTRADAS INNER JOIN SAM_PED_MOVTOS ON (SAM_PED_MOVTOS.ID_PED_MOVTO = DETALLES_ENTRADAS.ID_PED_MOVTO) WHERE ID_DETALLE_ENTRADA = ?"	, new Object[]{id_detalle_entrada}, Double.class);
		if(cant>=cantidad)
			return true;
		else
			return false;
	}
	
	public Double comprobarLoteAntes(Long id_ped_movto){
		return (Double)this.getJdbcTemplate().queryForObject("SELECT (SAM_PED_MOVTOS.CANTIDAD - ISNULL((SELECT SUM(D.CANTIDAD) FROM DETALLES_ENTRADAS AS D WHERE D.STATUS=1 AND D.ID_PED_MOVTO = SAM_PED_MOVTOS.ID_PED_MOVTO),0)) AS N FROM SAM_PED_MOVTOS WHERE SAM_PED_MOVTOS.ID_PED_MOVTO = ?"	, new Object[]{id_ped_movto}, Double.class);	
	}
	
	public void aperturarEntradas(final Long[] id_entradas){
		try{
			 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
	                	Vector vectorInventario = new Vector();
						Vector vectorDetalle = new Vector();
						for(Long idEntrada: id_entradas){
								if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SALIDAS WHERE ID_ENTRADA =? AND STATUS=1", new Object[]{idEntrada})>0)
									throw new RuntimeException("La entrada que desea aperturar se encuentra relacionada a una salida, no se puede continuar, aperture primero la salida");
								else{
									Map entrada = getEntradaDocumento(idEntrada);
									List <Map> conceptos = getConceptos(idEntrada);
									
									if(entrada.get("STATUS").toString().equals("0"))
										throw new RuntimeException("La entrada que desea aperturar no es valida o se encuentra en edición");
									
									getJdbcTemplate().update("UPDATE ENTRADAS SET STATUS = 0, FECHA_CIERRE=NULL WHERE ID_ENTRADA = ?", new Object[]{idEntrada});
									
									for(Map detalle: conceptos){
											Map detalle2 = getMapArticulo(Long.parseLong(entrada.get("ID_DEPENDENCIA").toString()), Long.parseLong(entrada.get("ID_ALMACEN").toString()), Long.parseLong(detalle.get("ID_ARTICULO").toString()));
											vectorInventario.add(Long.parseLong(detalle2.get("ID_INVENTARIO").toString()));
											vectorDetalle.add(Long.parseLong(detalle.get("ID_DETALLE_ENTRADA").toString()));
									}
									
									//Devolver aqui cada uno de los articulos del inventario hacia el documento fuente
									for(int i=0; i<vectorInventario.size(); i++){
										devolverArticulo(Long.parseLong(vectorInventario.get(i).toString()), Long.parseLong(vectorDetalle.get(i).toString()));
									}
								}	
						}
	                } 
             });
		}
		catch(DataAccessException e){
			 log.info(e.getMessage());	                    
             throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public void cancelarEntradas(final Long[] id_entradas){
		try{
			 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
	                	Vector vectorInventario = new Vector();
						Vector vectorDetalle = new Vector();
						for(Long idEntrada: id_entradas){
								if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SALIDAS WHERE ID_ENTRADA =? AND STATUS=1", new Object[]{idEntrada})>0)
									throw new RuntimeException("La entrada que desea cancelar se encuentra relacionada a una salida, no se puede cancelar");
								else{
									Map entrada = getEntradaDocumento(idEntrada);
									List <Map> conceptos = getConceptos(idEntrada);
									
									//if(entrada.get("STATUS").toString().equals("0"))
										//throw new RuntimeException("La entrada que desea cancelar no es valida o se encuentra en edición");
									
									getJdbcTemplate().update("UPDATE ENTRADAS SET STATUS = 2 WHERE ID_ENTRADA = ?", new Object[]{idEntrada});
									
									for(Map detalle: conceptos){
											Map detalle2 = getMapArticulo(Long.parseLong(entrada.get("ID_DEPENDENCIA").toString()), Long.parseLong(entrada.get("ID_ALMACEN").toString()), Long.parseLong(detalle.get("ID_ARTICULO").toString()));
											vectorInventario.add(Long.parseLong(detalle2.get("ID_INVENTARIO").toString()));
											vectorDetalle.add(Long.parseLong(detalle.get("ID_DETALLE_ENTRADA").toString()));
									}
									
									//Devolver aqui cada uno de los articulos del inventario hacia el documento fuente
									for(int i=0; i<vectorInventario.size(); i++){
										devolverArticulo(Long.parseLong(vectorInventario.get(i).toString()), Long.parseLong(vectorDetalle.get(i).toString()));
									}
								}	
						}
	                } 
             });
		}
		catch(DataAccessException e){
			 log.info(e.getMessage());	                    
             throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public void validarEntrada(Long idEntrada){
		Map m = this.getJdbcTemplate().queryForMap("SELECT FECHA_DOCUMENTO FROM ENTRADAS WHERE ID_ENTRADA = ?",new Object[]{idEntrada});
		this.getJdbcTemplate().update("UPDATE ENTRADAS SET FECHA_OFICIAL =? WHERE ID_ENTRADA =?", new Object[]{m.get("FECHA_DOCUMENTO"),idEntrada});
	}

	public List<Map> getListadoDocumentos(Map parametros){
		String sql = "SELECT ENTRADAS.*, (VPROYECTO.N_PROGRAMA + ' ' + CONVERT(varchar , ENTRADAS.ID_PROYECTO) + ' / ' + ENTRADAS.PARTIDA) AS PROGRAMA_PARTIDA, VPROYECTO.N_PROGRAMA, SAM_PEDIDOS_EX.CVE_PED, SAM_PEDIDOS_EX.NUM_PED, CONVERT(varchar(10), ENTRADAS.FECHA,103) AS FECHA_CREACION, (CASE ENTRADAS.STATUS WHEN 1 THEN 'CERRADO' WHEN 0 THEN 'CANCELADO' END) AS STATUS_DESC, CAT_BENEFI.NCOMERCIA FROM ENTRADAS LEFT JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI = ENTRADAS.ID_PROVEEDOR) INNER JOIN VPROYECTO ON (VPROYECTO.ID_PROYECTO = ENTRADAS.ID_PROYECTO) LEFT JOIN SAM_PEDIDOS_EX ON (SAM_PEDIDOS_EX.CVE_PED = ENTRADAS.ID_PEDIDO) ";
		String clausulas = "WHERE ENTRADAS.STATUS IN (0,1)";
		if(!parametros.get("cbodependencia").toString().equals("0")) clausulas += " AND ENTRADAS.ID_DEPENDENCIA =:cbodependencia";
		if(!parametros.get("id_almacen").toString().equals("0")) clausulas += " AND ENTRADAS.ID_ALMACEN =:id_almacen";
		if(!parametros.get("id_tipo_documento").toString().equals("0")) clausulas += " AND ENTRADAS.ID_TIPO_DOCUMENTO =:id_tipo_documento";
		if(!parametros.get("id_proveedor").toString().equals("0")) clausulas += " AND ENTRADAS.ID_PROVEEDOR =:id_proveedor";
		if(!parametros.get("id_pedido").toString().equals("")) clausulas += " AND ENTRADAS.ID_PEDIDO =:id_pedido";
		if(!parametros.get("fechaInicial").toString().equals("")&&!parametros.get("fechaFinal").toString().equals("")) clausulas += " AND convert(datetime,convert(varchar(10), ENTRADAS.FECHA ,103),103) BETWEEN :fechaInicial AND :fechaFinal";
		if(!parametros.get("proyecto").toString().equals("")) clausulas += " AND ENTRADAS.PROYECTO =:proyecto";
		if(!parametros.get("partida").toString().equals("")) clausulas += " AND ENTRADAS.PARTIDA =:partida";
		if(!parametros.get("num_documento").toString().equals("")) clausulas += " AND ENTRADAS.DOCUMENTO =:num_documento";
		if(!parametros.get("folio").toString().equals("")) clausulas += " AND ENTRADAS.FOLIO =:folio";
		return this.getNamedJdbcTemplate().queryForList(sql+clausulas+" ORDER BY ENTRADAS.FOLIO ASC", parametros);
	}
}
