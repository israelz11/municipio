/**
 * @author LSC. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;
import mx.gob.municipio.centro.view.controller.almacen.entradas.ControladorEntradasDocumentos;

public class GatewaySalidas extends BaseGatewayAlmacen {
	private static Logger log = Logger.getLogger(GatewaySalidas.class.getName());
	
	@Autowired
	GatewayEntradasDocumentos gatewayEntradasDocumentos;

	
	public Long idSalida = 0l;
	
	public GatewaySalidas(){}
	
	public List  getSolicitudesPorEstatus(Integer idAlmacen, String estatus) {
		  return this.getJdbcTemplate().queryForList("SELECT     A.ID_SALIDA, A.CONCEPTO, A.CLV_PARTID, A.ID_PERSONA_SOLICITA, A.FOLIO, B.PARTIDA, "+ 
				  " C.NOMBRE + ' ' + C.APE_PAT + ' ' + C.APE_MAT AS NOMBRE_COMPLETO, e.NOMBRE AS DEPTO ,CONVERT(varchar(10), A.FECHA, 103) AS FECHA "+
				  " FROM         dbo.SALIDAS AS A INNER JOIN "+
				  " dbo.CAT_PARTID AS B ON A.CLV_PARTID = B.CLV_PARTID INNER JOIN "+
				  " dbo.PERSONAS AS C ON A.ID_PERSONA_SOLICITA = C.CVE_PERS INNER JOIN "+
				  " dbo.TRABAJADOR AS D ON C.CVE_PERS = D.CVE_PERS INNER JOIN "+
				  " dbo.UNIDAD_ADM AS e ON d.CVE_UNIDAD = e.CVE_UNIDAD WHERE a.ESTATUS = ? AND a.ID_ALMACEN = ?    ",new Object []{estatus,idAlmacen} );
		}

	public Map getSolicitud(Long idSalida) {
		return this.getJdbcTemplate().queryForMap("SELECT     A.ID_SALIDA, A.CONCEPTO, A.CLV_PARTID, A.ID_PERSONA_SOLICITA, A.FOLIO, B.PARTIDA, "+ 
					  " C.NOMBRE + ' ' + C.APE_PAT + ' ' + C.APE_MAT AS NOMBRE_COMPLETO, e.NOMBRE AS DEPTO, CONVERT(varchar(10), A.FECHA, 103) AS FECHA, "+ 
                      " f.NOMBRE + ' ' + f.APE_PAT + ' ' + f.APE_MAT AS ENTREGO, h.NOMBRE AS DEPTO_ENTREGO "+
                      " FROM         dbo.TRABAJADOR AS g RIGHT OUTER JOIN "+
                      " dbo.PERSONAS AS f ON g.CVE_PERS = f.CVE_PERS LEFT OUTER JOIN "+
                      " dbo.UNIDAD_ADM AS h ON g.CVE_UNIDAD = h.CVE_UNIDAD RIGHT OUTER JOIN "+
                      " dbo.SALIDAS AS A INNER JOIN "+
                      " dbo.CAT_PARTID AS B ON A.CLV_PARTID = B.CLV_PARTID INNER JOIN "+
                      " dbo.PERSONAS AS C ON A.ID_PERSONA_SOLICITA = C.CVE_PERS INNER JOIN "+
                      " dbo.TRABAJADOR AS D ON C.CVE_PERS = D.CVE_PERS INNER JOIN "+
                      " dbo.UNIDAD_ADM AS e ON D.CVE_UNIDAD = e.CVE_UNIDAD ON f.CVE_PERS = A.ID_PERSONA_ENTREGA "+
                      " WHERE     A.ID_SALIDA = ?  ", new Object []{idSalida});	
	}
	
	/*public List  getDetallesSalida( Long idSalida) {	
	return this.getJdbcTemplate().queryForList("SELECT   f.ID_DETALLE_SALIDA, F.SURTIDO, a.ID_INVENTARIO, c.DESCRIPCION, e.UNIDMEDIDA, a.FOLIO, f.CANTIDAD AS SOLICITADO, a.PRECIO, a.CANTIDAD AS EXISTENCIA "+				
			" FROM         DETALLE_SALIDA AS f INNER JOIN "+
			" INVENTARIO AS a INNER JOIN "+
            " CAT_PROD AS b ON a.ID_ARTICULO = b.ID_ARTICULO INNER JOIN "+
            " SAM_CAT_ARTICULO AS c ON b.ID_CAT_ARTICULO = c.ID_CAT_ARTICULO INNER JOIN "+
            " CAT_PARTID AS d ON b.GRUPO = d.CLV_PARTID INNER JOIN "+
            " CAT_UNIMED AS e ON a.ID_UNIDAD_MEDIDA = e.CLV_UNIMED ON f.ID_INVENTARIO = a.ID_INVENTARIO INNER JOIN "+
            " SALIDAS AS G ON f.ID_SALIDA = G.ID_SALIDA "+
			" WHERE     f.ID_SALIDA = ? ",new Object []{idSalida} );
	}*/
	
	public List  getEjercicios( ) {	
		return this.getJdbcTemplate().queryForList(" SELECT distinct  DATENAME(year,FECHA_ENTREGA ) as YEAR  from salidas where FECHA_ENTREGA is not null   " );
		}
	
	public List  getEstatusSalida( ) {	
		return this.getJdbcTemplate().queryForList(" select 'AUTORIZADO' AS ESTATUS union all  select 'CANCELADO' AS ESTATUS union all select 'ENTREGADO' AS ESTATUS union all select 'PENDIENTE' AS ESTATUS union all  select 'ENVIADO'AS ESTATUS " );
		}
	
	public String  cancelarSalida(Long idSalida) {
		
		String mensaje="La salida se cancelo con exito.";		
		return mensaje;	
		
	}
	
	public Map getSalida(Long id_salida){
			try  
			{
				return this.getJdbcTemplate().queryForMap("SELECT ENTRADAS.ID_ENTRADA, "+
																	"ENTRADAS.ID_DEPENDENCIA," + 
																	"ENTRADAS.PARTIDA,"+
																	"ENTRADAS.FOLIO AS FOLIO_ENTRADA,"+
																	"SALIDAS.ID_GRUPO,"+
																	"SALIDAS.TIPO_IVA, "+
																	"SALIDAS.STATUS,"+
																	"ISNULL(SALIDAS.DESCUENTO, '0.00') AS DESCUENTO, "+
																	"ISNULL(SALIDAS.IVA, '0.00') AS IVA, "+
																	"ISNULL(SALIDAS.SUBTOTAL, '0.00') AS SUBTOTAL, "+
																	"ISNULL(SALIDAS.TOTAL, '0.00') AS TOTAL, "+
																	"SALIDAS.ID_SALIDA,  "+
																	"SALIDAS.ID_ENTRADA, "+
																	"SALIDAS.ID_TIPO_SALIDA,"+
																	"(SELECT DESCRIPCION FROM TIPO_ENTRADA WHERE ID_TIPO_ENTRADA=SALIDAS.ID_TIPO_SALIDA) AS TIPO_SALIDA,"+
																	"CONVERT(varchar(10), SALIDAS.FECHA_ENTREGA,103) AS FECHA_ENTREGA,  "+
																	"SALIDAS.FOLIO,  "+
																	"SALIDAS.CONCEPTO, "+
																	"ALMACEN.DESCRIPCION AS ALMACEN, "+
																	"ISNULL(ALMACEN.ALIAS,'') AS ALIAS,"+
																	"SAM_PEDIDOS_EX.NUM_PED, "+
																	"SAM_REQUISIC.NUM_REQ ,"+
																	"ENTRADAS.DOCUMENTO, "+
																	"VPROYECTO.N_PROGRAMA+'['+CONVERT(VARCHAR, ENTRADAS.ID_PROYECTO)+']' AS N_PROGRAMA_DESC,  "+
																	"VPROYECTO.PROG_PRESUP, "+
																	"CAT_BENEFI.NCOMERCIA AS PROVEEDOR, "+
																	"CAT_DEPENDENCIAS.DEPENDENCIA "+
																"FROM ENTRADAS  "+
																	"INNER JOIN VPROYECTO ON (VPROYECTO.ID_PROYECTO = ENTRADAS.ID_PROYECTO)  "+
																	"INNER JOIN CAT_PARTID ON (CAT_PARTID.CLV_PARTID = ENTRADAS.PARTIDA)  "+
																	"LEFT JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = ENTRADAS.ID_DEPENDENCIA) "+
																	"LEFT JOIN SAM_PEDIDOS_EX ON (SAM_PEDIDOS_EX.CVE_PED = ENTRADAS.ID_PEDIDO)  "+
																	"LEFT JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_PEDIDOS_EX.CVE_REQ)  "+
																	"LEFT JOIN TIPO_ENTRADA ON (TIPO_ENTRADA.ID_TIPO_ENTRADA = ENTRADAS.ID_TIPO_ENTRADA) "+
																	"LEFT JOIN SALIDAS ON (SALIDAS.ID_ENTRADA = ENTRADAS.ID_ENTRADA) "+
																	"LEFT JOIN ALMACEN ON (ALMACEN.ID_ALMACEN = ENTRADAS.ID_ALMACEN) "+
																	"INNER JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI = ENTRADAS.ID_PROVEEDOR) "+
																"WHERE SALIDAS.ID_SALIDA = ?", new Object[]{id_salida});
			}
			catch ( DataAccessException e) {
				return null;
			}
		}
	
	public Long getNumeroSiguienteSalida(int idDependencia, int idAlmacen){
		if(idAlmacen==0)
			return (this.getJdbcTemplate().queryForLong("SELECT COUNT(*) AS N FROM SALIDAS INNER JOIN ENTRADAS ON (ENTRADAS.ID_ENTRADA = SALIDAS.ID_ENTRADA) WHERE ID_DEPENDENCIA = ?", new Object[]{idDependencia}))+1;
		else
			return (this.getJdbcTemplate().queryForLong("SELECT COUNT(*) AS N FROM SALIDAS INNER JOIN ENTRADAS ON (ENTRADAS.ID_ENTRADA = SALIDAS.ID_ENTRADA) WHERE ID_ALMACEN = ?", new Object[]{idAlmacen}))+1;
	}
	
	public Long guardarSalida(final Long id_salida, final Long id_entrada, final String fecha, final String concepto, final int cve_pers, final int tipo_salida, final Double subtotal, final Double descuento, final Double iva, final int tipoIva){
		try
		{
			idSalida = id_salida;
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {	 
	            	Double subtotal2 =subtotal;
					Double descuento2 = descuento;
					Double iva2 = iva;
					int tipoIva2 = tipoIva;
					Double total = 0.00;
	            	
				if(id_salida==0)
				{
					Map entrada = gatewayEntradasDocumentos.getEntradaDocumento(id_entrada);
					//Validar la fecha de entrada con la de salida
					Date fecha_creacion = new Date();
				
					
					Double existencia =0.0;
					Long folio = getNumeroSiguienteSalida(Integer.parseInt(entrada.get("ID_DEPENDENCIA").toString()),Integer.parseInt(entrada.get("ID_ALMACEN").toString()));
					String num_folio = rellenarCeros(folio.toString(), 6);
					//obtener los datos de la entrada si es salida total
					if(tipo_salida==1){
						Map salida = getJdbcTemplate().queryForMap("SELECT ISNULL(SUBTOTAL, '0') AS SUBTOTAL, ISNULL(DESCUENTO, '0') AS DESCUENTO, ISNULL(IVA, '0') AS IVA, ISNULL(TIPO_IVA, '0') AS TIPO_IVA  FROM ENTRADAS WHERE ID_ENTRADA = ?", new Object[]{id_entrada});
						subtotal2 =  Double.parseDouble(salida.get("SUBTOTAL").toString());
						descuento2 = Double.parseDouble(salida.get("DESCUENTO").toString());
						iva2 = Double.parseDouble(salida.get("IVA").toString());
						tipoIva2 = Integer.parseInt(salida.get("TIPO_IVA").toString());
						
						 total = (subtotal2-descuento2)+iva2;
					}
					//guardar una nueva salida
					int idGrupo =getJdbcTemplate().queryForInt("SELECT B.ID_GRUPO_CONFIG FROM SAM_GRUPO_CONFIG_USUARIO A JOIN  SAM_GRUPO_CONFIG B ON A.ID_GRUPO_CONFIG = B.ID_GRUPO_CONFIG and ID_USUARIO=? where b.ESTATUS='ACTIVO' and B.TIPO='FIRMA' AND A.ASIGNADO =1", new Object[]{cve_pers});
					
					getJdbcTemplate().update("INSERT INTO SALIDAS(FOLIO, ID_ENTRADA, ID_TIPO_SALIDA, ID_GRUPO, ID_PERSONA, CONCEPTO, FECHA, FECHA_ENTREGA, SUBTOTAL, DESCUENTO, IVA, TIPO_IVA, TOTAL, STATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
							num_folio,
							id_entrada,
							tipo_salida,
							idGrupo,
							cve_pers,
							concepto,
							fecha_creacion,
							fecha,
							subtotal2, 
							descuento2,
							iva2,
							tipoIva2,
							total,
							0
					});
					
					//obtener el id_salida
					idSalida = getJdbcTemplate().queryForLong("SELECT MAX(ID_SALIDA) FROM SALIDAS");
					log.info("Guarda nueva SALIDA "+idSalida.toString());
					//guardar los detalles de la entreda, sacarlos del inventario y ponerlos en detalles de salida
					//Obtener conceptos de la entrada
					List <Map> detalles_entrada = gatewayEntradasDocumentos.getConceptos(id_entrada);
					for(Map row: detalles_entrada){
						//obtener la existencia en el inventario del articulo
						existencia = (Double) getJdbcTemplate().queryForObject("SELECT CANTIDAD FROM INVENTARIO WHERE ID_INVENTARIO=?", new Object[]{row.get("ID_INVENTARIO")}, Double.class);
						//comprobar si el detalle de entrada existe en detalles de salidas entonces sacar las diferencias de las cantidades y si es cero no pasa
						Double dif = (Double) getJdbcTemplate().queryForObject("SELECT DISTINCT (DETALLES_ENTRADAS.CANTIDAD - ISNULL((SELECT SUM(D.CANTIDAD) FROM DETALLE_SALIDA AS D WHERE D.STATUS=1 AND D.ID_DETALLE_ENTRADA = DETALLES_ENTRADAS.ID_DETALLE_ENTRADA),0)) AS N FROM DETALLES_ENTRADAS LEFT JOIN DETALLE_SALIDA ON (DETALLE_SALIDA.ID_DETALLE_ENTRADA = DETALLES_ENTRADAS.ID_DETALLE_ENTRADA) WHERE DETALLES_ENTRADAS.ID_DETALLE_ENTRADA = ?"	, new Object[]{row.get("ID_DETALLE_ENTRADA")}, Double.class);
						log.info("Diferencia en existencia de Inventario: existencia("+existencia.toString()+") >= diferencia("+dif.toString()+")&&diferencia>0");
						//quitar del inventario la existencia de cada articulo
						if(existencia>= dif &&dif>0){
							//getJdbcTemplate().update("UPDATE INVENTARIO SET CANTIDAD = CANTIDAD - ? WHERE ID_INVENTARIO =?", new Object[]{row.get("CANTIDAD"), row.get("ID_INVENTARIO")});
							getJdbcTemplate().update("INSERT INTO DETALLE_SALIDA(ID_SALIDA, ID_DETALLE_ENTRADA, ID_INVENTARIO, CANTIDAD, PRECIO, STATUS) VALUES(?,?,?,?,?,?)", new Object[]{
									idSalida,
									row.get("ID_DETALLE_ENTRADA"),
									row.get("ID_INVENTARIO"),
									(Double.parseDouble(row.get("CANTIDAD").toString())==dif) ? row.get("CANTIDAD"): dif,
									row.get("PRECIO"),
									0
							});
							log.info("Guarda DETALLE_SALIDA > ID_DETALLE_ENTRADA = "+row.get("ID_DETALLE_ENTRADA").toString()+" ID_DETALLE_INVENTARIO="+row.get("ID_INVENTARIO").toString());		
						}	
					}	
				}
				else{
					total = (subtotal-descuento)+iva;
					if(tipo_salida==1){
						Map salida = getJdbcTemplate().queryForMap("SELECT ISNULL(SUBTOTAL, '0') AS SUBTOTAL, ISNULL(DESCUENTO, '0') AS DESCUENTO, ISNULL(IVA, '0') AS IVA, ISNULL(TIPO_IVA, '0') AS TIPO_IVA FROM ENTRADAS WHERE ID_ENTRADA = ?", new Object[]{id_entrada});
						subtotal2 =  Double.parseDouble(salida.get("SUBTOTAL").toString());
						descuento2 = Double.parseDouble(salida.get("DESCUENTO").toString());
						iva2 = Double.parseDouble(salida.get("IVA").toString());
						tipoIva2 = Integer.parseInt(salida.get("TIPO_IVA").toString());
						
						 total = (subtotal2-descuento2)+iva2;
						 if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM DETALLE_SALIDA WHERE ID_SALIDA =?", new Object[]{id_salida})==0){
							 List <Map> detalles_entrada = gatewayEntradasDocumentos.getConceptos(id_salida);
							 for(Map row: detalles_entrada){	
									getJdbcTemplate().update("INSERT INTO DETALLE_SALIDA(ID_SALIDA, ID_DETALLE_ENTRADA, ID_INVENTARIO, CANTIDAD, PRECIO, STATUS) VALUES(?,?,?,?,?,?)", new Object[]{
											id_salida,
											row.get("ID_DETALLE_ENTRADA"),
											row.get("ID_INVENTARIO"),
											row.get("CANTIDAD"),
											row.get("PRECIO"),
											0
									});
									log.info("Guarda de nuevo por problema DETALLE_SALIDA > ID_DETALLE_ENTRADA = "+row.get("ID_DETALLE_ENTRADA").toString()+" ID_DETALLE_INVENTARIO="+row.get("ID_INVENTARIO").toString());		
								}	
						 }
						 
					}
					getJdbcTemplate().update("UPDATE SALIDAS SET CONCEPTO=?, FECHA_ENTREGA=?, ID_TIPO_SALIDA=?, SUBTOTAL=?, DESCUENTO=?, IVA=?, TIPO_IVA=?, TOTAL=? WHERE ID_SALIDA =?", new Object[]{concepto, fecha, tipo_salida, subtotal2, descuento2, iva2, tipoIva2, total, id_salida});
				}
				
	            } 
	        });
		return idSalida;
		
	}
	catch ( DataAccessException e) {
		throw new RuntimeException(e.getMessage(),e);
	}
	}
	
	public void autorizarSalidas(final Long id_salida){
		try{
			
			Boolean procede = this.getJdbcTemplate().queryForInt("SELECT STATUS FROM SALIDAS WHERE ID_SALIDA=?", new Object[]{id_salida})==0;
			if(!procede)
				throw new RuntimeException("La salida ("+id_salida+") no procede para realizar esta operación, consulte a su administrador");
			
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                	Double existencia =0.0;
					Long id_entrada = getJdbcTemplate().queryForLong("SELECT ID_ENTRADA FROM SALIDAS WHERE ID_SALIDA=?", new Object[]{id_salida});
					getJdbcTemplate().update("UPDATE SALIDAS SET STATUS=1 WHERE ID_SALIDA =?", new Object[]{id_salida});
					
					List <Map> detalles = getDetallesSalida(id_salida);  //gatewayEntradasDocumentos.getConceptos(id_entrada);
					for(Map row: detalles){
						//obtener la existencia en el inventario del articulo
						existencia = (Double) getJdbcTemplate().queryForObject("SELECT CANTIDAD FROM INVENTARIO WHERE ID_INVENTARIO=?", new Object[]{row.get("ID_INVENTARIO")}, Double.class);
						//quitar del inventario la existencia de cada articulo
						if(existencia>= Double.parseDouble(row.get("CANTIDAD").toString())){
							getJdbcTemplate().update("UPDATE INVENTARIO SET CANTIDAD = CANTIDAD - ? WHERE ID_INVENTARIO =?", new Object[]{row.get("CANTIDAD"), row.get("ID_INVENTARIO")});
							getJdbcTemplate().update("UPDATE DETALLE_SALIDA SET CANTIDAD =? ,STATUS=? WHERE ID_DETALLE_SALIDA = ?", new Object[]{
									row.get("CANTIDAD"),
									1,
									row.get("ID_DETALLE_SALIDA"),
							});
						}
						else
							throw new RuntimeException("El disponible de uno de los articulos esta agotado, consulte a su administrador");
					}
                } 
            });
		}
		catch ( DataAccessException e) {
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
//------------------------------------------------CANCELAR SALIDAS--------------------------
	public void invalidarSalidas(Long id_salida){
		try{
			Double existencia =0.0;
			Boolean procede = this.getJdbcTemplate().queryForInt("SELECT STATUS FROM SALIDAS WHERE ID_SALIDA=?", new Object[]{id_salida})==1;
			if(!procede)
				throw new RuntimeException("La salida ("+id_salida+") no procede para realizar esta operación, consulte a su administrador");
			
			Long id_entrada = this.getJdbcTemplate().queryForLong("SELECT ID_ENTRADA FROM SALIDAS WHERE ID_SALIDA=?", new Object[]{id_salida});
			this.getJdbcTemplate().update("UPDATE SALIDAS SET STATUS=2 WHERE ID_SALIDA =?", new Object[]{id_salida});
			List <Map> detalles_entrada = this.getDetallesSalida(id_salida);
			for(Map row: detalles_entrada){
				this.getJdbcTemplate().update("UPDATE INVENTARIO SET CANTIDAD = CANTIDAD + ? WHERE ID_INVENTARIO =?", new Object[]{row.get("CANTIDAD"), row.get("ID_INVENTARIO")});
				this.getJdbcTemplate().update("UPDATE DETALLE_SALIDA SET STATUS = 2 WHERE ID_DETALLE_SALIDA =?", new Object[]{row.get("ID_DETALLE_SALIDA")});
			}
		}
		catch ( DataAccessException e) {
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public List <Map> getDetallesSalida(Long id_salida){
		return this.getJdbcTemplate().queryForList("SELECT DETALLE_SALIDA.*, "+
																		"(CASE ISNULL(DETALLES_ENTRADAS.ID_PED_MOVTO,0) WHEN 0 THEN DETALLES_ENTRADAS.LOTE ELSE (SELECT PED_CONS FROM SAM_PED_MOVTOS WHERE ID_PED_MOVTO = DETALLES_ENTRADAS.ID_PED_MOVTO) END) AS LOTE, "+
																		"DETALLES_ENTRADAS.ID_ARTICULO, "+
																		"DETALLES_ENTRADAS.ID_PED_MOVTO, "+
																		"DETALLES_ENTRADAS.ID_UNIDAD_MEDIDA, "+
																		"DETALLES_ENTRADAS.ID_FAMILIA, "+
																		"DETALLES_ENTRADAS.STATUS, "+
																		"FAMILIAS.DESCRIPCION AS FAMILIA, "+
																		"SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, "+
																		"DETALLES_ENTRADAS.DESCRIPCION, "+
																		"CAT_UNIMED.UNIDMEDIDA "+
																	"FROM DETALLE_SALIDA  "+
																		"INNER JOIN DETALLES_ENTRADAS ON (DETALLES_ENTRADAS.ID_DETALLE_ENTRADA = DETALLE_SALIDA.ID_DETALLE_ENTRADA) "+
																		"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = DETALLES_ENTRADAS.ID_ARTICULO)  "+
																		"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO)  "+
																		"LEFT JOIN FAMILIAS ON (FAMILIAS.ID_FAMILIA = DETALLES_ENTRADAS.ID_FAMILIA)  "+
																		"LEFT JOIN SAM_PED_MOVTOS ON (SAM_PED_MOVTOS.ID_PED_MOVTO = DETALLES_ENTRADAS.ID_PED_MOVTO)  "+
																		"LEFT JOIN SAM_PEDIDOS_EX ON (SAM_PEDIDOS_EX.CVE_PED = SAM_PED_MOVTOS.CVE_PED)  "+
																		"INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = DETALLES_ENTRADAS.ID_UNIDAD_MEDIDA)  "+
																	"WHERE DETALLE_SALIDA.ID_SALIDA=? "+
																	"ORDER BY (CASE ISNULL(DETALLES_ENTRADAS.ID_PED_MOVTO,0) WHEN 0 THEN DETALLES_ENTRADAS.LOTE ELSE (SELECT PED_CONS FROM SAM_PED_MOVTOS WHERE ID_PED_MOVTO = DETALLES_ENTRADAS.ID_PED_MOVTO) END) ASC", new Object[]{id_salida});
	}
	
	public List<Map> gatewaySalidas(Long idSalida){
		return this.getJdbcTemplate().queryForList("SELECT "+  
															"SAM_PED_MOVTOS.PED_CONS AS LOTE,"+
															"DETALLE_SALIDA.ID_SALIDA,"+
															"DETALLES_ENTRADAS.ID_ENTRADA,"+
															"DETALLE_SALIDA.ID_DETALLE_ENTRADA, "+ 
															"DETALLE_SALIDA.ID_DETALLE_SALIDA, "+
															"DETALLE_SALIDA.ID_INVENTARIO, "+
															"DETALLES_ENTRADAS.ID_ARTICULO," + 
															"DETALLES_ENTRADAS.ID_PED_MOVTO," + 
															"DETALLES_ENTRADAS.ID_UNIDAD_MEDIDA,"+  
															"DETALLES_ENTRADAS.ID_FAMILIA, "+
															"DETALLE_SALIDA.CANTIDAD,"+
															"DETALLES_ENTRADAS.CANTIDAD AS CANTIDAD_ENTRADA,"+  
															"(DETALLES_ENTRADAS.CANTIDAD - ISNULL((SELECT SUM(D.CANTIDAD) FROM DETALLE_SALIDA AS D WHERE D.STATUS=1 AND D.ID_DETALLE_ENTRADA = DETALLES_ENTRADAS.ID_DETALLE_ENTRADA),0)) AS CANTIDAD_ENT,  "+
															"DETALLES_ENTRADAS.PRECIO,"+  
															"DETALLES_ENTRADAS.DESCRIPCION,"+  
															"DETALLE_SALIDA.STATUS, "+
															"FAMILIAS.DESCRIPCION AS FAMILIA,"+  
															"SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, "+
															"SAM_PED_MOVTOS.DESCRIP AS DESC_ART, "+
															"CAT_UNIMED.UNIDMEDIDA, "+
															"SAM_PEDIDOS_EX.SUBTOTAL, "+
															"SAM_PEDIDOS_EX.IVA, "+
															"SAM_PEDIDOS_EX.DESCUENTO, "+
															"SAM_PEDIDOS_EX.TOTAL "+
														"FROM DETALLES_ENTRADAS "+
															"INNER JOIN DETALLE_SALIDA ON (DETALLE_SALIDA.ID_DETALLE_ENTRADA = DETALLES_ENTRADAS.ID_DETALLE_ENTRADA) "+
															"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = DETALLES_ENTRADAS.ID_ARTICULO) "+
															"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO) "+
															"LEFT JOIN FAMILIAS ON (FAMILIAS.ID_FAMILIA = DETALLES_ENTRADAS.ID_FAMILIA)  "+
															"LEFT JOIN SAM_PED_MOVTOS ON (SAM_PED_MOVTOS.ID_PED_MOVTO = DETALLES_ENTRADAS.ID_PED_MOVTO) "+ 
															"LEFT JOIN SAM_PEDIDOS_EX ON (SAM_PEDIDOS_EX.CVE_PED = SAM_PED_MOVTOS.CVE_PED)  "+
															"INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = DETALLES_ENTRADAS.ID_UNIDAD_MEDIDA) "+ 
												"WHERE DETALLE_SALIDA.ID_SALIDA = ?", new Object[]{idSalida});
	}
	
	public void guardarCantidadDetalles(final Long idsalida, final Long[] ids, final Double[] cantidades, final Double subtotal, final Double descuento, final Double iva, final int tipoIva){
		try {    
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
                	boolean exito = false;
                	int i=0;
                	Double total = (subtotal-descuento)+iva;
                	getJdbcTemplate().update("UPDATE SALIDAS SET SUBTOTAL=?, DESCUENTO=?, IVA=?, TIPO_IVA=?, TOTAL =? WHERE ID_SALIDA =?", new Object[]{subtotal, descuento, iva, tipoIva, total, idsalida});
                	for (Long id :ids) {
                		//comprobar que no excede la cantidad a obtener del lote de pedido
                		//if( comprobarLote(id, cantidades[i])){
                			getJdbcTemplate().update("UPDATE DETALLE_SALIDA SET CANTIDAD =? WHERE ID_DETALLE_SALIDA =?", new Object[]{cantidades[i], id});
                		/*}
                		else
                			throw new RuntimeException("No se puede completar la operación, la cantidad de lote excede al disponible en el detalle de entradas");
                			*/
                		i++;
                	}
                } 
             });
           
            } catch (DataAccessException e) {                                
                 throw new RuntimeException(e.getMessage(),e);
            }	
	}
	
	public boolean comprobarLote(Long id_detalle_entrada, Double cantidad){
		Double cant = (Double)this.getJdbcTemplate().queryForObject("SELECT DISTINCT (DETALLES_ENTRADAS.CANTIDAD - ISNULL((SELECT SUM(D.CANTIDAD) FROM DETALLE_SALIDA AS D WHERE D.STATUS=1 AND D.ID_DETALLE_ENTRADA = DETALLES_ENTRADAS.ID_DETALLE_ENTRADA),0)) AS N FROM DETALLES_ENTRADAS LEFT JOIN DETALLE_SALIDA ON (DETALLE_SALIDA.ID_DETALLE_ENTRADA = DETALLES_ENTRADAS.ID_DETALLE_ENTRADA) WHERE DETALLES_ENTRADAS.ID_DETALLE_ENTRADA = ?"	, new Object[]{id_detalle_entrada}, Double.class);
		if(cant>=cantidad)
			return true;
		else
			return false;
	}
	
	public void cerrarSalida(Long idSalida){
		try {    
			this.autorizarSalidas(idSalida);
        } catch (DataAccessException e) {                                
             throw new RuntimeException(e.getMessage(),e);
        }	
	}
	
	public void eliminarConceptos(final Long id){
		try {    
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
                	getJdbcTemplate().update("DELETE FROM DETALLE_SALIDA WHERE ID_DETALLE_SALIDA = ?", new Object[]{id});
                } 
            });
          
           } catch (DataAccessException e) {                                
                throw new RuntimeException(e.getMessage(),e);
           }	
	}
	
	public void cancelarEntradaDocumento(final Long id_salida, final int cve_pers){
		try{
			 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
						//Validar que puede cancelar las salidas
						if(!getPrivilegioEn(cve_pers, 121))
							throw new RuntimeException("No se puede cancelar la salida, su usuario no cuenta con los privilegios suficientes");
						
						invalidarSalidas(id_salida);
						getJdbcTemplate().update("UPDATE SALIDAS SET STATUS=? WHERE ID_SALIDA=?", new Object[]{2, id_salida});
						getJdbcTemplate().update("UPDATE DETALLE_SALIDA SET STATUS=? WHERE ID_SALIDA =?", new Object[]{0,id_salida});
						
	                } 
             });
		}
		catch(DataAccessException e){
			 log.info(e.getMessage());	                    
             throw new RuntimeException(e.getMessage(),e);
		}
	}
	
}
