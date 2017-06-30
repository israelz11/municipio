/**
 * @author ISC. Israel de la Cruz Hdez.
 * @version 1.0, Date: 02/Jun/2011
 * @version 2.0, Date: 03/Feb/2013
 *
 */

package mx.gob.municipio.centro.model.gateways.sam;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.controller.sam.ordenesPagos.ControladorOrdenPago;

public class GatewayContratos extends BaseGateway {

	private static Logger log = 
	        Logger.getLogger(GatewayContratos.class.getName());
	
	@Autowired
	public GatewayBitacora gatewayBitacora;
	@Autowired
	public GatewayMeses gatewayMeses;
	@Autowired
	public GatewayProyectoPartidas gatewayProyectoPartidas;
	@Autowired
	private GatewayRequisicion gatewayRequisicion;
	
	@Autowired
	private GatewayVales gatewayVales;
	
	public final static  int CON_STATUS_EDICION = 0;
	public final static int CON_STATUS_CERRADO = 1;
	public final static int CON_STATUS_CANCELADO = 2;
	public final static int CON_STATUS_TERMINADO = 3;

	public GatewayContratos() {
		// TODO Auto-generated method stub
	}
	
	public Map getContrato(Long cve_contrato){
		return this.getJdbcTemplate().queryForMap("SELECT A.*, " +
														"CASE A.ID_TIPO " +
															"WHEN 7 THEN " +
																"ISNULL(PED.NUM_PED,'') " +
															"ELSE " +
																"ISNULL(REQ.NUM_REQ,'') " +
														"END AS NUM_DOC," +
														 "CONVERT(varchar(10), A.FECHA_INICIO, 103) AS FECHA_INICIO, " +
														 "CONVERT(varchar(10), A.FECHA_TERMINO, 103) AS FECHA_TERMINO, " +
														 "CONVERT(varchar(10), A.FECHA_CERRADO, 103) AS FECHA_CERRADO, " +
														 "CONVERT(varchar(10), A.FECHA_CERRADO, 103) AS FECHA_CIERRE, " +
														 "A.FECHA_CERRADO AS FECHA_CERRADO2, "+
														 "B.NCOMERCIA AS PROVEEDOR, " +
														 "C.RECURSO, " +
														 "D.CLV_UNIADM,"+
														 "D.DEPENDENCIA, " +
														 "E.DESCRIPCION AS TIPO_CONTRATO, " +
														 "(SELECT ISNULL(SUM(M.IMPORTE),0) FROM SAM_COMP_CONTRATO AS M WHERE M.CVE_CONTRATO = A.CVE_CONTRATO) AS IMPORTE "+
												"FROM SAM_CONTRATOS AS A " +
													"INNER JOIN CAT_BENEFI  AS B ON (B.CLV_BENEFI = A.CLV_BENEFI) " +
													"INNER JOIN CAT_RECURSO AS C ON (C.ID = A.ID_RECURSO) " +
													"INNER JOIN CAT_DEPENDENCIAS AS D ON (D.ID = A.ID_DEPENDENCIA) " +
													"INNER JOIN SAM_CAT_TIPO_CONTRATOS AS E ON (E.ID_TIPO = A.ID_TIPO) " +
													"LEFT JOIN SAM_REQUISIC AS REQ ON (REQ.CVE_REQ = A.CVE_DOC) " +
													"LEFT JOIN SAM_PEDIDOS_EX AS PED ON (PED.CVE_PED = A.CVE_DOC) " +
												"WHERE A.CVE_CONTRATO = ?",new Object[]{cve_contrato});
	}
	
	public List<Map> getTiposContratos(){
		return this.getJdbcTemplate().queryForList("SELECT ID_TIPO, DESCRIPCION FROM SAM_CAT_TIPO_CONTRATOS ORDER BY DESCRIPCION ASC");
	}
	
	public Long guardarContrato(Long cve_contrato, int idDependencia, String num_contrato, String fecha_ini, String fecha_fin, String oficio, String tiempo_entrega, int tipo, String concepto, Double anticipo, int idRecurso, String clv_benefi, Long cve_doc, int ejercicio, int cve_pers, int idGrupo){
		Date fecha = new Date();
		if(cve_contrato==0){
			//verificar si el numero de contrato esta repetido
			boolean repetido = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_CONTRATOS WHERE NUM_CONTRATO LIKE '%"+num_contrato+"%' AND STATUS NOT IN (2)")>0;
			if(repetido) 
				throw new RuntimeException("Número de contrato duplicado, esta operación no puede continuar vuelva a escribir uno diferente");
			
			cve_contrato = this.getJdbcTemplate().queryForLong("SELECT MAX(CVE_CONTRATO) AS N FROM SAM_CONTRATOS")+1;
			this.getJdbcTemplate().update("INSERT INTO SAM_CONTRATOS(CVE_CONTRATO, ID_DEPENDENCIA, ID_GRUPO, ID_RECURSO, CLV_BENEFI, CVE_DOC, NUM_CONTRATO, EJERCICIO, ID_TIPO, CVE_PERS, FECHA, FECHA_INICIO, FECHA_TERMINO, OFICIO_AUT, TIEMPO_ENTREGA, DESCRIPCION, ANTICIPO, STATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{cve_contrato, idDependencia, idGrupo, idRecurso, clv_benefi, cve_doc, num_contrato, ejercicio, tipo, cve_pers, fecha, this.formatoFecha(fecha_ini), this.formatoFecha(fecha_fin), oficio, tiempo_entrega, concepto, anticipo, 0});
			
			cve_contrato = this.getJdbcTemplate().queryForLong("SELECT MAX(CVE_CONTRATO) AS N FROM SAM_CONTRATOS");
			
			//Generar el detalle presupuestal del documento si existe
			generarDetallesPresupuestales(cve_contrato, tipo, cve_doc);
			
			//guardar en bitacora
			//Map Req = this.getJdbcTemplate().queryForMap("SELECT PROYECTO, CLV_PARTID, (SELECT ISNULL(SUM(M.CANTIDAD*M.PRECIO_EST),0) AS N FROM SAM_REQ_MOVTOS AS M WHERE M.CVE_REQ = SAM_REQUISIC.CVE_REQ ) IMPORTE FROM SAM_REQUISIC WHERE CVE_REQ =? ", new Object[]{cve_req});
			//gatewayBitacora.guardarBitacora(gatewayBitacora.CON_NUEVO, ejercicio, cve_pers, cve_contrato, num_contrato, "CON", fecha, Req.get("PROYECTO").toString(), Req.get("CLV_PARTID").toString(), null, Double.parseDouble(Req.get("IMPORTE").toString()));
		}
		else{
			String num = this.rellenarCeros(cve_contrato.toString(), 6);
			Map Contrato = this.getContrato(cve_contrato);
			
			this.getJdbcTemplate().update("UPDATE SAM_CONTRATOS SET ID_TIPO = ?, ID_RECURSO=?, CLV_BENEFI=?, CVE_DOC=?, FECHA_INICIO = ?, FECHA_TERMINO = ?, OFICIO_AUT = ?, TIEMPO_ENTREGA = ?, DESCRIPCION = ?, ANTICIPO=? WHERE CVE_CONTRATO = ?", new Object[]{tipo, idRecurso, clv_benefi, cve_doc, this.formatoFecha(fecha_ini), this.formatoFecha(fecha_fin), oficio, tiempo_entrega, concepto, anticipo, cve_contrato});
			
			if(Contrato.get("CVE_DOC")!=null)
			{
				if(!Contrato.get("CVE_DOC").equals(cve_doc.toString()))
				{
					getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO =?", new Object[]{cve_contrato});
				}
			}
			
			if(this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO =?", new Object[]{cve_contrato}) == 0)
			{
				//Generar el detalle presupuestal del documento si existe
				generarDetallesPresupuestales(cve_contrato, tipo, cve_doc);
			}
			
			//Map Req = this.getJdbcTemplate().queryForMap("SELECT PROYECTO, CLV_PARTID, (SELECT ISNULL(SUM(M.CANTIDAD*M.PRECIO_EST),0) AS N FROM SAM_REQ_MOVTOS AS M WHERE M.CVE_REQ = SAM_REQUISIC.CVE_REQ ) IMPORTE FROM SAM_REQUISIC WHERE CVE_REQ =? ", new Object[]{cve_req});
			//gatewayBitacora.guardarBitacora(gatewayBitacora.CON_ACTUALIZO, ejercicio, cve_pers, cve_contrato, num, "CON", fecha, Req.get("PROYECTO").toString(), Req.get("CLV_PARTID").toString(), null, Double.parseDouble(Req.get("IMPORTE").toString()));
			
		}
		return cve_contrato;
	}
	
	public void generarDetallesPresupuestales(final Long cve_contrato, final int tipo_contrato, final Long cve_doc){
		if(cve_doc==null) return;
		if(cve_doc==0) return;
		
		try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                	getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO =?", new Object[]{cve_contrato});
	                	List<Map> conceptos = null;
	                	
	                	//Por pedido
	                	if(tipo_contrato == 7)
	                	{
	                		conceptos = getJdbcTemplate().queryForList("SELECT PERIODO,ID_PROYECTO, CLV_PARTID, SUM([MONTO]) AS TOTAL " +
									"FROM VT_COMPROMISOS   " +
										"WHERE TIPO_DOC IN ('PED') AND CONSULTA IN ('PRECOMPROMETIDO', 'COMPROMETIDO') AND CVE_DOC =  ? " +
										"GROUP BY PERIODO, ID_PROYECTO, CLV_PARTID " +
									"ORDER BY PERIODO ASC", new Object[]{cve_doc});
	                	}
	                	else
	                	{

		                	conceptos = getJdbcTemplate().queryForList("SELECT PERIODO,ID_PROYECTO, CLV_PARTID, SUM([MONTO]) AS TOTAL " +
																						"FROM VT_COMPROMISOS   " +
																							"WHERE TIPO_DOC IN ('REQ', 'O.S', 'O.T') AND CONSULTA IN ('PRECOMPROMETIDO', 'COMPROMETIDO') AND CVE_DOC =  ? " +
																							"GROUP BY PERIODO, ID_PROYECTO, CLV_PARTID " +
																						"ORDER BY PERIODO ASC", new Object[]{cve_doc});
		                	}
	                	
	                	for(Map row: conceptos){
	                		getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, ID_PROYECTO, CLV_PARTID, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)", new Object[]{cve_contrato, "COMPROMISO", row.get("ID_PROYECTO"), row.get("CLV_PARTID"), row.get("PERIODO"), row.get("TOTAL")});
	                	}
	                }
	            });
			}
            catch (DataAccessException e) {                                
                throw new RuntimeException("La operación para generar el detalle presupuestal del contrato ha fallado, consulte a su administrador");
            }	    	
	}
	
	public String cerrarContrato(final Long cve_contrato, final int ejercicio, final int cve_pers){
		String mensaje = ""; 
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                		//cargar los datos de la Orden de Servicio y el presupuesto
                		boolean tiene = true;
                		BigDecimal comprometido = new BigDecimal(0);
                		BigDecimal disponibleMes = new BigDecimal(0);
                		BigDecimal importe = new BigDecimal(0);
                		
                		Map contrato = getContrato(cve_contrato);
                		
                		List<Map> conceptos = getConceptosContratoAgrupados(cve_contrato);
                		for(Map row:conceptos )
                		{
                			if(!row.get("ID_RECURSO").toString().equals(contrato.get("ID_RECURSO").toString()))
                				throw new RuntimeException("No se puede cerrar el Contrato. El Tipo de Recurso del Programa: "+ row.get("N_PROGRAMA")+ " y Partida: "+row.get("CLV_PARTID")+" del periodo "+ row.get("DESC_PERIODO")+ " es diferente al especificado en la cabecera del Contrato.");
                		}
                		
                		if(contrato.get("CVE_DOC")!=null)
                		{
                			//Entra aqui si viene desde la Requisicion/OS.
                			Map requisicion = gatewayRequisicion.getRequisicion(Long.parseLong(contrato.get("CVE_DOC").toString()));
                			conceptos = getConceptosContratoAgrupados(cve_contrato);
                			//Entra aqui si no es requisicion de tipo calendarizada
                			if(!requisicion.get("TIPO").toString().equals("7"))
                			{
                				
	                			for(Map row: conceptos){
	                				
	                				if(contrato.get("ID_TIPO").toString().equals("7"))
	                				{
	                					//SI ES CONTRATO DE PEDIDO
	                					comprometido = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(MONTO),0) FROM VT_COMPROMISOS WHERE TIPO_DOC IN ('PED') AND CONSULTA ='COMPROMETIDO' AND CVE_DOC = ? AND ID_PROYECTO = ? AND CLV_PARTID = ? AND PERIODO = ?", new Object[]{contrato.get("CVE_DOC"), row.get("ID_PROYECTO"), row.get("CLV_PARTID"), row.get("PERIODO")}, BigDecimal.class);
	                				}
	                				if(contrato.get("ID_TIPO").toString().equals("13"))
	                				{
	                					//SI ES CONTRATO DE PEDIDO
	                					comprometido = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(MONTO),0) FROM VT_COMPROMISOS WHERE TIPO_DOC IN ('VAL') AND CONSULTA ='COMPROMETIDO' AND CVE_DOC = ? AND ID_PROYECTO = ? AND CLV_PARTID = ? AND PERIODO = ?", new Object[]{contrato.get("CVE_DOC"), row.get("ID_PROYECTO"), row.get("CLV_PARTID"), row.get("PERIODO")}, BigDecimal.class);
	                				}
	                				else
	                				{
	                					//CONTRATO DE REQ, OS, OT
	                					comprometido = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(MONTO),0) FROM VT_COMPROMISOS WHERE TIPO_DOC IN ('O.S', 'O.T', 'REQ') AND CONSULTA ='COMPROMETIDO' AND CVE_DOC = ? AND ID_PROYECTO = ? AND CLV_PARTID = ? AND PERIODO = ?", new Object[]{contrato.get("CVE_DOC"), row.get("ID_PROYECTO"), row.get("CLV_PARTID"), row.get("PERIODO")}, BigDecimal.class);
	                				}
	                				
	                				importe =  (BigDecimal) row.get("IMPORTE");
	                				
	                				if(importe.doubleValue() > comprometido.doubleValue()){
	                					tiene = false;
	                					throw new RuntimeException("El presupuesto en Programa: "+ row.get("N_PROGRAMA")+ " y Partida: "+row.get("CLV_PARTID")+" del periodo "+ row.get("DESC_PERIODO")+ " es insuficiente para cerrar el contrato");
	                				}	
	                			}
                			}
                			else
                			{
                				//Si es requisicion calendarizada entra
                				Double importeTotal = (Double) getJdbcTemplate().queryForObject("SELECT SUM(MONTO) FROM VT_COMPROMISOS WHERE TIPO_DOC = 'REQ' AND CVE_DOC = ? AND CONSULTA ='COMPROMETIDO'",new Object[]{Long.parseLong(contrato.get("CVE_DOC").toString())},Double.class);
            					for(Map row: conceptos)
            					{
            						importe = importe.add((BigDecimal) row.get("IMPORTE"));
            					}
            					
            					if(importe.doubleValue() <= importeTotal.doubleValue())
            						tiene = true;
            					else
            						tiene = false;
                					
                			}
                			
                		}
                		else
                		{
                			//Validar aqui el contrato libre sin documentos
                			//int mesActivo =gatewayMeses.getMesActivo(ejercicio);
                			conceptos = getConceptosContratoAgrupados(cve_contrato);
                			for(Map row: conceptos){
                				disponibleMes = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(dbo.getDisponible(?,?,?),0) AS DISPONIBLE ", new Object []{row.get("PERIODO"), row.get("ID_PROYECTO"), row.get("CLV_PARTID")}, BigDecimal.class);
                				importe =  (BigDecimal) row.get("IMPORTE");
                				
                				if(importe.doubleValue() > disponibleMes.doubleValue()){
                					tiene = false;
                					throw new RuntimeException("El presupuesto del periodo es insuficiente para cerrar el contrato");
                				}
                			}
                			
                		}
                		
                		Date fecha = new Date();
                		
                		//Cierra el contrato
                		if(tiene){
                			
                			List<Map> movimientos = getJdbcTemplate().queryForList("SELECT ID_PROYECTO, CLV_PARTID, SUM(IMPORTE) AS TOTAL FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO = ? GROUP BY ID_PROYECTO, CLV_PARTID", new Object[]{cve_contrato});
                			for(Map detalle : movimientos)
                			{
                				getJdbcTemplate().update("INSERT INTO SAM_COMP_MOV(TIPO_DOC, DOCUMENTO, ID_PROYECTO, CLV_PARTID, IMPORTE, FECHA_MOV) VALUES (?,?,?,?,?,?)", new Object[]{7, cve_contrato, detalle.get("ID_PROYECTO"), detalle.get("CLV_PARTID"), detalle.get("TOTAL"), fecha});
                			}
                			
                			getJdbcTemplate().update("UPDATE SAM_CONTRATOS SET STATUS = ?, FECHA_CERRADO =? WHERE CVE_CONTRATO = ?", new Object[]{CON_STATUS_CERRADO, fecha, cve_contrato});
                			getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS = ?, FECHA_FINIQUITADO =?, MES_FINALIZADO = ?, DIA_FINALIZADO = ? WHERE CVE_REQ = ?",new Object[]{gatewayRequisicion.REQ_STATUS_FINIQUITADA, fecha, fecha.getMonth()+1, fecha.getDay(), contrato.get("CVE_DOC")});
                			
                			if(contrato.get("ID_TIPO").toString().equals("7")) //CONTRAO A TRAVEZ DE PEDIDO
                			{
                				getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET STATUS = 5, FECHA_FINIQUITADO =?, MES_FINALIZADO = ?, DIA_FINALIZADO = ? WHERE CVE_PED = ?",new Object[]{fecha, fecha.getMonth()+1, fecha.getDay(), contrato.get("CVE_DOC")});
                			}
                				
                		    //guardar en bitacora
                			//Date fecha = new Date();
                			//String num = rellenarCeros(cve_contrato.toString(), 6);
                			//gatewayBitacora.guardarBitacora(gatewayBitacora.CON_CERRO, ejercicio, cve_pers, cve_contrato, num, "CON", fecha, proyecto, clv_partid, null, timporte);
                		}
                		
                	} 
               });
		       return mensaje;     
          } catch (DataAccessException e) {                                
              throw new RuntimeException("La operación para cerrar el contrato ha fallado, consulte a su administrador");
          }	    	
	}
	
	public boolean tienePrecompromisoDisponible(Long proyecto, String partida, int mes, Long idReq, double importe ){
		  boolean tienePresupuesto = false;
		  double preCompromiso=gatewayProyectoPartidas.getPreComprometidoMes(proyecto, partida, mes);
		  double preCompromisoReq=gatewayRequisicion.getCompromisoMes(idReq,mes);
		  //Obtener lo que queda disponible del mes para luego verificar si hace falta dinero en requisicion
		  double disponiblemes = gatewayProyectoPartidas.getDisponibleMes(mes, proyecto, partida).doubleValue();
		  double apartadoReq = redondea(preCompromisoReq,2);
		  //verificar si el monto en requisicion alcanza por el importe del pedido
		  if (redondea(preCompromisoReq,2) <= redondea(preCompromiso,2) && importe<=apartadoReq)
			  tienePresupuesto=true;
		  /*Si no hay dinero en requisicion completar con el disponible del mes para ver si alacanza
		  if(tienePresupuesto==false&&(redondea((apartadoReq+disponiblemes),2) >= redondea(importe,2)))
			  tienePresupuesto=true;*/
	 return tienePresupuesto;
	 }
	
	public double redondea(double numero, int decimales) 
	{ 
	  double resultado;
	  BigDecimal res;

	  res = new BigDecimal(numero).setScale(decimales, BigDecimal.ROUND_UP);
	  resultado = res.doubleValue();
	  return resultado; 
	}  
	
	public List <Map> getListaContratos(Map modelo){
		String sql = "SELECT A.*, "+ 
				 
			"(CASE E.DESCRIPCION "+
					"WHEN 'Obras' THEN A.NUM_CONTRATO "+
				    "WHEN 'Renta de Maquinaria' THEN REQ.NUM_REQ  "+
					"WHEN 'Servicios' THEN REQ.NUM_REQ  "+
					"WHEN 'Difusión' THEN REQ.NUM_REQ "+
					"WHEN 'Arrendamiento' THEN A.NUM_CONTRATO "+
					"WHEN 'Adquisicion' THEN PED.NUM_PED "+
					"WHEN 'Energia Electrica' THEN A.NUM_CONTRATO "+
					"WHEN 'Combustible' THEN A.NUM_CONTRATO "+
					"WHEN 'Nominas' THEN A.NUM_CONTRATO "+
					"WHEN 'Fondo Fijo' THEN A.NUM_CONTRATO "+
					"WHEN 'Vales' THEN VAL.NUM_VALE END) AS NUM_DOCTOS,  "+
				"CONVERT(varchar(10), A.FECHA_INICIO, 103) AS FECHA_INICIO, "+
				"CONVERT(varchar(10), A.FECHA_TERMINO, 103) AS FECHA_TERMINO, "+
				"B.NCOMERCIA AS PROVEEDOR,  "+
				"C.RECURSO, "+
				"D.DEPENDENCIA,  "+
				"E.DESCRIPCION AS TIPO_CONTRATO, "+
				"F.DESCRIPCION AS STATUS_DESC, "+
				"('['+CONVERT(VARCHAR,G.ID_ARCHIVO)+'] ' + G.NOMBRE) AS ARCHIVO_ANEXO, "+
				"(SELECT ISNULL(SUM(M.IMPORTE),0) FROM SAM_COMP_CONTRATO AS M WHERE M.CVE_CONTRATO = A.CVE_CONTRATO) AS IMPORTE "+
				"FROM SAM_CONTRATOS AS A "+
					"INNER JOIN CAT_BENEFI  AS B ON (B.CLV_BENEFI = A.CLV_BENEFI) "+
					"INNER JOIN CAT_RECURSO AS C ON (C.ID = A.ID_RECURSO) "+
					"INNER JOIN CAT_DEPENDENCIAS AS D ON (D.ID = A.ID_DEPENDENCIA) "+
					"INNER JOIN SAM_CAT_TIPO_CONTRATOS AS E ON (E.ID_TIPO = A.ID_TIPO) "+
					"INNER JOIN SAM_ESTATUS_CONTRATO AS F ON (F.ID_ESTATUS_CONTRATO = A.STATUS) "+
					"LEFT JOIN SAM_CONTRATOS_ARCHIVOS AS G ON (G.CVE_CONTRATO = A.CVE_CONTRATO) "+
					"LEFT JOIN SAM_REQUISIC AS REQ ON (REQ.CVE_REQ = A.CVE_DOC) "+
					"LEFT JOIN SAM_PEDIDOS_EX AS PED ON (PED.CVE_PED = A.CVE_DOC) "+
					"LEFT JOIN SAM_VALES_EX AS VAL ON (VAL.CVE_VALE = A.CVE_DOC) "+
				"WHERE A.STATUS IN ("+modelo.get("status")+") ";
		
		if(modelo.get("idUnidad")!=null&&!modelo.get("idUnidad").toString().equals("0"))
			sql+=" AND D.ID = '"+modelo.get("idUnidad").toString()+"'";
		if (!modelo.get("fechaInicial").toString().equals("") && !modelo.get("fechaFinal").toString().equals("") ) 
			sql += " AND CONVERT(datetime,CONVERT(varchar(10), FECHA_INICIO ,103),103) BETWEEN '"+modelo.get("fechaInicial").toString()+"' and '"+modelo.get("fechaFinal").toString()+"' ";
		if(modelo.get("tipo_gto")!=null&&!modelo.get("tipo_gto").toString().equals("0"))
			sql += " AND C.ID = '"+modelo.get("tipo_gto").toString()+"'";
		if(modelo.get("CLV_BENEFI")!="")
			sql += " AND B.CLV_BENEFI = '"+modelo.get("CLV_BENEFI").toString()+"'";
			
		return this.getJdbcTemplate().queryForList(sql);
	}
	
	public String aperturarContratos(final List<Long> lst_contratos, final int ejercicio, final int cve_pers){
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                	for(Long cve_contrato: lst_contratos){
                		
                		Map documento = getContrato(cve_contrato);
                		
                		Date fechaCierre = new Date();
	            		fechaCierre = (Date) documento.get("FECHA_CERRADO2");
	            		Calendar c1 = Calendar.getInstance();
                		if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1))
	            		{
	            			throw new RuntimeException("No se puede aperturar el contrato "+documento.get("NUM_CONTRATO").toString()+" por que el periodo es diferente");
	            		}
                		
                		//comprobar que realmente se puede aperturar
                		Map contrato = getJdbcTemplate().queryForMap("SELECT  "+
																			"(SELECT COUNT(*) FROM SAM_REQUISIC AS R WHERE R.CVE_CONTRATO = C.CVE_CONTRATO AND STATUS IN (1,2,5)) AS TOTAL_REQ, "+ 
																			"(SELECT COUNT(*) FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) WHERE R.CVE_CONTRATO = C.CVE_CONTRATO AND P.STATUS IN (1,2,5)) AS TOTAL_PED, "+ 
																			"(SELECT COUNT(*) FROM SAM_ORD_PAGO AS O WHERE O.CVE_CONTRATO = C.CVE_CONTRATO AND STATUS IN (0,1,6)) AS TOTAL_OP, "+ 
																			"(SELECT COUNT(*) FROM SAM_FACTURAS AS F WHERE F.CVE_CONTRATO = C.CVE_CONTRATO AND F.STATUS IN (1,3)) AS TOTAL_FACTURA," + 
																			"(SELECT COUNT(*) FROM SAM_VALES_EX AS V WHERE V.CVE_CONTRATO = C.CVE_CONTRATO AND STATUS IN (1,3,4)) AS TOTAL_VAL "+ 
																		"FROM SAM_CONTRATOS AS C WHERE C.CVE_CONTRATO = ?", new Object[]{cve_contrato}); 
						if(contrato.get("TOTAL_REQ").toString().equals("0")&&contrato.get("TOTAL_PED").toString().equals("0")&&contrato.get("TOTAL_OP").toString().equals("0")&&contrato.get("TOTAL_FACTURA").toString().equals("0")&&contrato.get("TOTAL_VAL").toString().equals("0")){
							//Aperturar y quitar los movimientos comprometidos del contrato
							getJdbcTemplate().update("UPDATE SAM_CONTRATOS SET STATUS = ? WHERE CVE_CONTRATO = ?", new Object[]{CON_STATUS_EDICION, cve_contrato});
							//Eliminar el detalle de lado del Sr. Peredo
							getJdbcTemplate().update("DELETE FROM SAM_COMP_MOV WHERE TIPO_DOC = ? AND DOCUMENTO =?", new Object[]{7, cve_contrato});
							
							if(documento.get("ID_TIPO").equals("7"))
								getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET STATUS = ? WHERE CVE_PED = ? ", new Object[]{4, contrato.get("CVE_DOC")});
							else
								getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS = ? WHERE CVE_REQ = ? ", new Object[]{2, contrato.get("CVE_DOC")});

							//guardar en bitacora
                			//Date fecha = new Date();
                			//String num = rellenarCeros(cve_contrato.toString(), 6);
                			//gatewayBitacora.guardarBitacora(gatewayBitacora.CON_APERTURO, ejercicio, cve_pers, cve_contrato, num, "CON", fecha, contrato.get("PROYECTO").toString(), contrato.get("CLV_PARTID").toString(), null, Double.parseDouble(contrato.get("IMPORTE").toString()));

						}
						else //No se puede aperturar, causas
							throw new RuntimeException("No se puede aperturar el contrato "+documento.get("NUM_CONTRATO").toString()+" es posible que exista algun documento relacionado a este.");
                	}
                }
            });
           return ""; 
		}
		catch (DataAccessException e) {                                
            throw new RuntimeException("Hay un problema con algunos de los contratos, no se pueden aperturar, consulte a su administrador: "+e.getMessage());
        }
			     
	}
	
	public String cancelarContrato(final List<Long> lst_contratos, final int ejercicio, final int cve_pers){
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                	for(Long cve_contrato: lst_contratos){
                		Map documento = getContrato(cve_contrato);
	    		      	
	    		      	Date fechaCierre = new Date();
	    		  		fechaCierre = (Date) documento.get("FECHA_CERRADO2");
	    		  		Calendar c1 = Calendar.getInstance();
	    		  		
	    		  		//Buscar si existe el Super Privilegio para Cancelar Vales
	    				boolean privilegio = getPrivilegioEn(cve_pers, 141);
	    				
	    				//Si el contrato no es del periodo y no tiene super-privilegio entonces no dejar cancelarlo
	    		  		if(fechaCierre!=null&&privilegio==false)
	    		  			if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1) && !documento.get("STATUS").toString().equals("0"))
		    		  		{
		    		  			throw new RuntimeException("No se puede cancelar el Contrato "+documento.get("NUM_CONTRATO").toString()+", el periodo del documento es diferente al actual, consulte a su administrador");
		        		  	}
                		
                		if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE CVE_CONTRATO =? AND STATUS IN (1,3)", new Object[]{cve_contrato})>0){
	            			throw new RuntimeException("Imposible cancelar el contrato, este ya esta relacionado a una factura");
	            		}
                		
                		if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_ORD_PAGO WHERE CVE_CONTRATO =? AND STATUS IN (0, 1, 6)", new Object[]{cve_contrato})>0){
	            			throw new RuntimeException("Imposible cancelar el contrato, este ya esta relacionado a una Orden de Pago");
	            		}
                		
                		/*if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_CONTRATOS AS C INNER JOIN SAM_COMP_CONTRATO AS M ON (M.CVE_CONTRATO = C.CVE_CONTRATO) WHERE C.CVE_CONTRATO =? AND M.PERIODO <> ?", new Object[]{cve_contrato, gatewayMeses.getMesActivo(ejercicio)})>0){
	            			throw new RuntimeException("Imposible cancelar el contrato, el periodo ya no es válido, consulte a su administrador del sistema");
	            		}*/
                		
                		//comprobar que realmente se puede aperturar
                		Map contrato = getJdbcTemplate().queryForMap("SELECT  "+
																			"(SELECT COUNT(*) FROM SAM_REQUISIC AS R WHERE R.CVE_CONTRATO = C.CVE_CONTRATO AND STATUS IN (1,2,5)) AS TOTAL_REQ, "+ 
																			"(SELECT COUNT(*) FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) WHERE R.CVE_CONTRATO = C.CVE_CONTRATO AND P.STATUS IN (1,2,5)) AS TOTAL_PED, "+ 
																			"(SELECT COUNT(*) FROM SAM_ORD_PAGO AS O WHERE O.CVE_CONTRATO = C.CVE_CONTRATO AND STATUS IN (0,1,6)) AS TOTAL_OP, "+ 
																			"(SELECT COUNT(*) FROM SAM_FACTURAS AS F WHERE F.CVE_CONTRATO = C.CVE_CONTRATO AND F.STATUS IN (1,3)) AS TOTAL_FACTURA," + 
																			"(SELECT COUNT(*) FROM SAM_VALES_EX AS V WHERE V.CVE_CONTRATO = C.CVE_CONTRATO AND STATUS IN (1,3,4)) AS TOTAL_VAL "+ 
																		"FROM SAM_CONTRATOS AS C  WHERE C.CVE_CONTRATO = ?", new Object[]{cve_contrato}); 
						if(contrato.get("TOTAL_REQ").toString().equals("0")&&contrato.get("TOTAL_PED").toString().equals("0")&&contrato.get("TOTAL_OP").toString().equals("0")&&contrato.get("TOTAL_FACTURA").toString().equals("0")&&contrato.get("TOTAL_VAL").toString().equals("0")){
							
							//Aperturar y quitar los movimientos comprometidos del contrato
							if(documento.get("STATUS").toString().equals("0"))
								getJdbcTemplate().update("UPDATE SAM_CONTRATOS SET STATUS = ? WHERE CVE_CONTRATO = ?", new Object[]{2, cve_contrato});
							else
								getJdbcTemplate().update("UPDATE SAM_CONTRATOS SET STATUS = ?, FECHA_CANCELADO=? WHERE CVE_CONTRATO = ?", new Object[]{2, new Date(), cve_contrato});
							
							//Eliminar el detalle de lado del Sr. Peredo
							//Desactivada esta linea por instrucciones de peredo 04/Dic/2013
							//getJdbcTemplate().update("DELETE FROM SAM_COMP_MOV WHERE TIPO_DOC = ? AND DOCUMENTO =?", new Object[]{7, cve_contrato});
							
							if(documento.get("ID_TIPO").equals("7"))
								getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET STATUS = ? WHERE CVE_PED = ? ", new Object[]{4, contrato.get("CVE_DOC")});
							else
								getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS = ? WHERE CVE_REQ = ? ", new Object[]{2, contrato.get("CVE_DOC")});

							//guardar en bitacora
                			//Date fecha = new Date();
                			//String num = rellenarCeros(cve_contrato.toString(), 6);
                			//gatewayBitacora.guardarBitacora(gatewayBitacora.CON_APERTURO, ejercicio, cve_pers, cve_contrato, num, "CON", fecha, contrato.get("PROYECTO").toString(), contrato.get("CLV_PARTID").toString(), null, Double.parseDouble(contrato.get("IMPORTE").toString()));

						}
						else //No se puede aperturar, causas
							throw new RuntimeException("No se puede cancelar el contrato "+documento.get("NUM_CONTRATO").toString()+" es posible que exista algun documento relacionado a este.");
                	}
                }
            });
            return ""; 
		}catch (DataAccessException e) {                                
            throw new RuntimeException("Hay un problema con el contrato, no se pueden cancelar, consulte a su administrador: "+e.getMessage());
        }         
            
	}
	
	public boolean comprobarTerminado(Long cve_contrato, int ejercicio, int cve_pers){
		Map contrato = this.getJdbcTemplate().queryForMap("SELECT "+
																"C.CVE_CONTRATO, C.STATUS, R.PROYECTO, R.CLV_PARTID, (SELECT SUM(O.CANTIDAD*O.PRECIO_EST) FROM SAM_REQ_MOVTOS AS O WHERE O.CVE_REQ = C.CVE_REQ) AS IMPORTE,"+ 
																"(SELECT ISNULL(SUM(SC.IMPORTE),0) AS TIMPORTE FROM SAM_COMP_CONTRATO AS SC WHERE SC.TIPO_MOV='COMPROMISO' AND SC.CVE_CONTRATO = C.CVE_CONTRATO) AS COMPROMISO, "+ 
																"(SELECT ISNULL(SUM(SC.IMPORTE),0) AS TIMPORTE FROM SAM_COMP_CONTRATO AS SC WHERE SC.TIPO_MOV='LIBERACION' AND SC.CVE_CONTRATO = C.CVE_CONTRATO) AS COMPROBADO, "+ 
																"((SELECT ISNULL(SUM(SC.IMPORTE),0) AS TIMPORTE FROM SAM_COMP_CONTRATO AS SC WHERE SC.TIPO_MOV='COMPROMISO' AND SC.CVE_CONTRATO = C.CVE_CONTRATO) - (SELECT ISNULL(SUM(SC.IMPORTE),0) AS TIMPORTE FROM SAM_COMP_CONTRATO AS SC WHERE SC.TIPO_MOV='LIBERACION' AND SC.CVE_CONTRATO = C.CVE_CONTRATO)) AS SALDO "+  
															"FROM SAM_CONTRATOS AS C INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = C.CVE_REQ) WHERE C.CVE_CONTRATO = ?", new Object[]{cve_contrato});
		if(contrato.get("SALDO").toString().equals("0")&&Integer.parseInt(contrato.get("STATUS").toString())== this.CON_STATUS_CERRADO){
			this.getJdbcTemplate().update("UPDATE SAM_CONTRATO SET STATUS = ? WHERE CVE_CONTRATO = ?",new Object[]{this.CON_STATUS_TERMINADO, cve_contrato});
			//guardar en bitacora
			Date fecha = new Date();
			String num = rellenarCeros(cve_contrato.toString(), 6);
			gatewayBitacora.guardarBitacora(gatewayBitacora.CON_TERMINO, ejercicio, cve_pers, cve_contrato, num, "CON", fecha, contrato.get("PROYECTO").toString(), contrato.get("CLV_PARTID").toString(), null, Double.parseDouble(contrato.get("IMPORTE").toString()));
			return true;
		}
		else
			return false;
		
	}
	
	public List <Map> getListaContratos(Long cve_contrato, String num_contrato, int idDependencia, int mesActivo, int idRecurso){
		return this.getJdbcTemplate().queryForList("SELECT  B.CVE_CONTRATO, "+
																		"B.NUM_CONTRATO,  "+
																		"B.DESCRIPCION, "+ 
																		"A.PERIODO, "+ 
																		/*"A.ID_PROYECTO, "+ 
																		"A.CLV_PARTID, "+ */
																		"B.ID_RECURSO, "+
																		"D.CLV_BENEFI, "+
																		"D.NCOMERCIA AS PROVEEDOR, "+
																		"(CASE ("+mesActivo+") WHEN 1 THEN 'ENERO' WHEN 2 THEN 'FEBRERO' WHEN 3 THEN 'MARZO' WHEN 4 THEN 'ABRIL' WHEN 5 THEN 'MAYO' WHEN 6 THEN 'JUNIO' WHEN 7 THEN 'JULIO' WHEN 8 THEN 'AGOSTO' WHEN 9 THEN 'SEPTIEMBRE' WHEN 10 THEN 'OCTUBRE' WHEN 11 THEN 'NOVIEMBRE' WHEN 12 THEN 'DICIEMBRE' END) AS DESC_PERIODO, "+
																		"SUM(C.MONTO) AS IMPORTE "+
																"FROM SAM_COMP_CONTRATO AS A "+
																	"INNER JOIN SAM_CONTRATOS AS B ON (B.CVE_CONTRATO = A.CVE_CONTRATO) "+
																	"INNER JOIN VT_COMPROMISOS  AS C ON (C.CVE_DOC = B.CVE_CONTRATO AND C.TIPO_DOC ='CON' AND C.PERIODO = A.PERIODO AND C.ID_PROYECTO = A.ID_PROYECTO AND C.CLV_PARTID = A.CLV_PARTID) "+
																	"INNER JOIN CAT_BENEFI AS D ON (D.CLV_BENEFI = B.CLV_BENEFI) "+
																"WHERE B.STATUS IN (1) AND C.PERIODO = " + mesActivo + " "
																		+(cve_contrato!=0 ? " AND B.CVE_CONTRATO ="+cve_contrato:"")
																		+(num_contrato!=null ? " AND B.NUM_CONTRATO LIKE '%"+num_contrato+"%'":"")
																		+(idDependencia!=0 ? " AND B.ID_DEPENDENCIA IN ("+idDependencia+")":"")
																		+(idRecurso!=0 ? " AND B.ID_RECURSO IN ("+idRecurso+")": "")+
																" GROUP BY B.CVE_CONTRATO, B.NUM_CONTRATO, B.DESCRIPCION, A.PERIODO, /*A.ID_PROYECTO, A.CLV_PARTID,*/ B.ID_RECURSO, D.CLV_BENEFI, D.NCOMERCIA " + 
																" ORDER BY B.NUM_CONTRATO ASC");
	}

	public List<Map> getConceptosContrato(Long cve_contrato){
		return this.getJdbcTemplate().queryForList("SELECT A.ID_DETALLE_COMPROMISO, A.CVE_CONTRATO, A.TIPO_MOV, A.ID_PROYECTO, A.CLV_PARTID, A.PERIODO, SUM(A.IMPORTE) AS IMPORTE, B.DEPENDENCIA, B.N_PROGRAMA, "+
															"(CASE PERIODO " +
																"WHEN 1 THEN 'ENERO' " +
																"WHEN 2 THEN 'FEBRERO' " +
																"WHEN 3 THEN 'MARZO' " +
																"WHEN 4 THEN 'ABRIL' " +
																"WHEN 5 THEN 'MAYO' " +
																"WHEN 6 THEN 'JUNIO' " +
																"WHEN 7 THEN 'JULIO' " +
																"WHEN 8 THEN 'AGOSTO' " +
																"WHEN 9 THEN 'SEPTIEMBRE' " +
																"WHEN 10 THEN 'OCTUBRE' " +
																"WHEN 11 THEN 'NOVIEMBRE' " +
																"WHEN 12 THEN 'DICIEMBRE' END) AS DESC_PERIODO " +
														"FROM SAM_COMP_CONTRATO AS A " +
															"INNER JOIN VPROYECTO AS B ON (B.ID_PROYECTO = A.ID_PROYECTO) " +
															"INNER JOIN CAT_PARTID AS C ON (C.CLV_PARTID = A.CLV_PARTID) " +
														"WHERE CVE_CONTRATO = ? "+
														"GROUP BY A.ID_DETALLE_COMPROMISO, A.CVE_CONTRATO, A.TIPO_MOV, A.ID_PROYECTO, A.CLV_PARTID, A.PERIODO,B.DEPENDENCIA, B.N_PROGRAMA "+
														"ORDER BY B.N_PROGRAMA, A.CLV_PARTID, A.PERIODO ASC", new Object[]{cve_contrato});
	}
	
	public Map getConceptoContrato(Long idDetalle)
	{
		return this.getJdbcTemplate().queryForMap("SELECT A.ID_DETALLE_COMPROMISO, A.CVE_CONTRATO, A.TIPO_MOV, A.ID_PROYECTO, A.CLV_PARTID, A.PERIODO, SUM(A.IMPORTE) AS IMPORTE, B.DEPENDENCIA, B.N_PROGRAMA, "+
				"(CASE PERIODO " +
					"WHEN 1 THEN 'ENERO' " +
					"WHEN 2 THEN 'FEBRERO' " +
					"WHEN 3 THEN 'MARZO' " +
					"WHEN 4 THEN 'ABRIL' " +
					"WHEN 5 THEN 'MAYO' " +
					"WHEN 6 THEN 'JUNIO' " +
					"WHEN 7 THEN 'JULIO' " +
					"WHEN 8 THEN 'AGOSTO' " +
					"WHEN 9 THEN 'SEPTIEMBRE' " +
					"WHEN 10 THEN 'OCTUBRE' " +
					"WHEN 11 THEN 'NOVIEMBRE' " +
					"WHEN 12 THEN 'DICIEMBRE' END) AS DESC_PERIODO " +
			"FROM SAM_COMP_CONTRATO AS A " +
				"INNER JOIN VPROYECTO AS B ON (B.ID_PROYECTO = A.ID_PROYECTO) " +
				"INNER JOIN CAT_PARTID AS C ON (C.CLV_PARTID = A.CLV_PARTID) " +
			"WHERE A.ID_DETALLE_COMPROMISO = ? "+
			"GROUP BY A.ID_DETALLE_COMPROMISO, A.CVE_CONTRATO, A.TIPO_MOV, A.ID_PROYECTO, A.CLV_PARTID, A.PERIODO,B.DEPENDENCIA, B.N_PROGRAMA "+
			"ORDER BY B.N_PROGRAMA, A.CLV_PARTID, A.PERIODO ASC", new Object[]{idDetalle});
	}
	
	public List<Map> getConceptosContratoAgrupados(Long cve_contrato){
		return this.getJdbcTemplate().queryForList("SELECT A.CVE_CONTRATO, A.TIPO_MOV, A.ID_PROYECTO, A.CLV_PARTID, A.PERIODO, SUM(A.IMPORTE) AS IMPORTE, B.DEPENDENCIA, B.N_PROGRAMA, B.ID_RECURSO, "+
															"dbo.getClaveProgramatica (A.ID_PROYECTO, A.CLV_PARTID) AS CLAVE, " +
															"(CASE PERIODO " +
																"WHEN 1 THEN 'ENERO' " +
																"WHEN 2 THEN 'FEBRERO' " +
																"WHEN 3 THEN 'MARZO' " +
																"WHEN 4 THEN 'ABRIL' " +
																"WHEN 5 THEN 'MAYO' " +
																"WHEN 6 THEN 'JUNIO' " +
																"WHEN 7 THEN 'JULIO' " +
																"WHEN 8 THEN 'AGOSTO' " +
																"WHEN 9 THEN 'SEPTIEMBRE' " +
																"WHEN 10 THEN 'OCTUBRE' " +
																"WHEN 11 THEN 'NOVIEMBRE' " +
																"WHEN 12 THEN 'DICIEMBRE' END) AS DESC_PERIODO " +
														"FROM SAM_COMP_CONTRATO AS A " +
															"INNER JOIN VPROYECTO AS B ON (B.ID_PROYECTO = A.ID_PROYECTO) " +
															"INNER JOIN CAT_PARTID AS C ON (C.CLV_PARTID = A.CLV_PARTID) " +
														"WHERE CVE_CONTRATO = ? "+
														"GROUP BY A.CVE_CONTRATO, A.TIPO_MOV, A.ID_PROYECTO, A.CLV_PARTID, A.PERIODO,B.DEPENDENCIA, B.N_PROGRAMA, B.ID_RECURSO "+
														"ORDER BY B.N_PROGRAMA, A.CLV_PARTID, A.PERIODO ASC", new Object[]{cve_contrato});
	}
	
	public void eliminarConcpetosContratoPeredo(final Long cve_contrato, final Long[] idDetalles)
	{
		try{
	    	 for(Long idDetalle: idDetalles)
	    	 {
	    		 Map concepto = getConceptoContrato(idDetalle);
	    		 getJdbcTemplate().update("DELETE FROM SAM_COMP_MOV WHERE DOCUMENTO = ? and TIPO_DOC = ? and ID_PROYECTO = ? and CLV_PARTID = ? and IMPORTE = ?", new Object[]{cve_contrato, 7, concepto.get("ID_PROYECTO"), concepto.get("CLV_PARTID"), concepto.get("IMPORTE")}); 
	    	 }
		}
		catch (DataAccessException e) {                                
           throw new RuntimeException("La operacion ha fallado, no se han podido eliminar los conceptos de lado de Peredo SAM_COMP_MOV: "+e.getMessage());
       }  
	}
	
	public void eliminarConcpetos(final Long cve_contrato, final Long[] idDetalles)
	{
		try{
			 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	             @Override
	             protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	 for(Long idDetalle: idDetalles)
	            	 {
	            		 getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE ID_DETALLE_COMPROMISO =?", new Object[]{idDetalle}); 
	            	 }
				 }
		    });
		}
		catch (DataAccessException e) {                                
            throw new RuntimeException("La operacion ha fallado, no se han podido eliminar los conceptos "+e.getMessage());
        }  
	}
	
	public void guardarConceptoContratoPeredo(Long idDetalle, Long cve_contrato, int idProyecto, String clv_partid, Double importe)
	{
		try
		{
			Date fecha = new Date();
			
			if(idDetalle==0)
				this.getJdbcTemplate().update("INSERT INTO SAM_COMP_MOV(DOCUMENTO, TIPO_DOC, ID_PROYECTO, CLV_PARTID, IMPORTE, FECHA_MOV) VALUES(?,?,?,?,?,?)", new Object[]{cve_contrato, 7, idProyecto, clv_partid, importe, fecha});
			else
			{
				Map concepto = getConceptoContrato(idDetalle);
				this.getJdbcTemplate().update("UPDATE SAM_COMP_MOV SET IMPORTE = ? WHERE DOCUMENTO = ? AND TIPO_DOC = ? AND ID_PROYECTO = ? AND CLV_PARTID = ? AND IMPORTE = ?", new Object[]{importe, cve_contrato, 7, idProyecto, clv_partid, concepto.get("IMPORTE")});
			}
		}
		catch (Exception e) {                                
	        throw new RuntimeException("La operacion ha fallado, no se han podido guardar el concepto de lado de Peredo en SAM_COMP_MOV: "+e.getMessage());
	    } 
	}
	
	public void guardarConcepto(Long idDetalle, Long cve_contrato, int idProyecto, String clv_partid, int mes, Double importe){
		try{
			if(idDetalle==0)
				this.getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, ID_PROYECTO, CLV_PARTID, PERIODO, IMPORTE, PERIODO_ANT, IMPORTE_ANT) VALUES(?,?,?,?,?,?,?,?)", new Object[]{cve_contrato, "COMPROMISO", idProyecto, clv_partid, mes, importe, mes, importe});
			else
				this.getJdbcTemplate().update("UPDATE SAM_COMP_CONTRATO SET ID_PROYECTO=?, CLV_PARTID=?, PERIODO=?, IMPORTE=?, PERIODO_ANT=?, IMPORTE_ANT=? WHERE ID_DETALLE_COMPROMISO=?", new Object[]{idProyecto, clv_partid, mes, importe, mes, importe, idDetalle});
		}
		catch (Exception e) {                                
	        throw new RuntimeException("La operacion ha fallado, no se han podido guardar el concepto "+e.getMessage());
	    } 
	}
	
	public List<Map> getMovimientosAjustadosContrato(Long cve_contrato){
		return this.getJdbcTemplate().queryForList("SELECT MOV.*, CONVERT(varchar(10), MOV.FECHA_MOVTO, 103) AS FECHA_MOVTO , VP.N_PROGRAMA FROM SAM_MOD_COMP AS MOV INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = MOV.ID_PROYECTO) WHERE MOV.TIPO_DOC = ? AND MOV.DOCUMENTO =? ORDER BY MOV.FECHA_MOVTO DESC",new Object[]{7, cve_contrato});
	}
	
	public String getBeneficiarioContrato(String clv_benefi){
		return (String)this.getJdbcTemplate().queryForObject("SELECT NCOMERCIA FROM CAT_BENEFI WHERE CLV_BENEFI =?", new Object[]{clv_benefi}, String.class);
	}
	
	public void guardarAjusteContratoPeredo(Long id_sam_mod_comp, Long cve_contrato, int idProyecto, String clv_partid, String fecha, Double importe)
	{
		if(id_sam_mod_comp==0)
			this.getJdbcTemplate().update("INSERT INTO SAM_MOD_COMP(TIPO_DOC, DOCUMENTO, ID_PROYECTO, CLV_PARTID, IMPORTE, FECHA_MOVTO) VALUES(?,?,?,?,?,?)", new Object[]{7, cve_contrato, idProyecto, clv_partid, importe, this.formatoFecha(fecha)});
		else
			this.getJdbcTemplate().update("UPDATE SAM_MOD_COMP SET ID_PROYECTO=?, CLV_PARTID=?, IMPORTE=?, FECHA_MOVTO=? WHERE id_sam_mod_comp = ?", new Object[]{idProyecto, clv_partid, importe, this.formatoFecha(fecha), id_sam_mod_comp});
	}
	
	public void eliminarConceptoAjusteContrato(final Long id_sam_mod_comp)
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
	
	public int getCountArchivos(Long cve_contrato)
	{
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_CONTRATOS_ARCHIVOS WHERE CVE_CONTRATO = ?", new Object[]{cve_contrato});
	}
	
	public Long guardarArchivo(Long cve_contrato, String nombreArchivo, String path, Date fecha, String ext, Long size){
		this.getJdbcTemplate().update("INSERT INTO SAM_CONTRATOS_ARCHIVOS(CVE_CONTRATO, NOMBRE, RUTA, FECHA, EXT, TAMANO) VALUES(?,?,?,?,?,?)", new Object[]{
				cve_contrato,
				nombreArchivo,
				path, 
				fecha,
				ext,
				size
		});
		
		return this.getJdbcTemplate().queryForLong("SELECT MAX(ID_ARCHIVO) AS N FROM SAM_CONTRATOS_ARCHIVOS");
	}
	
	public List<Map> getArchivosContrato(Long cve_contrato){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SAM_CONTRATOS_ARCHIVOS WHERE CVE_CONTRATO=?", new Object[]{cve_contrato});
	}
	
	public void eliminarArchivoContrato(int cve_pers, Long idArchivo, HttpServletRequest request){
		//si tiene los privilegios elimina
		//if(!getPrivilegioEn(cve_pers, 129))
		//	throw new RuntimeException("No se puede eliminar el archivo, su usuario no cuenta con los privilegios suficientes");
		
		Map archivo = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_CONTRATOS_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		/*eliminado fisico*/
		File fichero = new File(request.getSession().getServletContext().getRealPath("")+"/sam/contratos/archivos/["+archivo.get("ID_ARCHIVO")+"] "+archivo.get("NOMBRE").toString());
	   
		if (fichero.delete()){
			 System.out.println("El fichero ha sido borrado satisfactoriamente");
			 this.getJdbcTemplate().update("DELETE FROM SAM_CONTRATOS_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		}
		else
			System.out.println("El fichero no puede ser borrado");
	}
	
	
	public List<Map> getListaValesPresupuesto(Long cve_vale, String clv_benefi, int idRecurso, int tipo_doc, int cve_pers, int idDependencia){
		String clausula = "";
		String status = "STATUS IN (4)";
		
		//if(tipo_doc!=7) status = "STATUS IN (1,3)";
		if(cve_vale!=0) clausula = " AND CVE_VALE = "+cve_vale+" "+(!clv_benefi.equals("0") ? "AND CLV_BENEFI='"+clv_benefi+"'": "")+" "+(idRecurso!=0? " AND ID_RECURSO = "+idRecurso+"":"") + (tipo_doc==1 ? " AND TIPO='AO'":"");
	
		return this.getJdbcTemplate().queryForList("SELECT *,"+ 
															"CONVERT(NVARCHAR, FECHA, 103) AS FECHA, "+ 
															"(SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_MOV_VALES WHERE CVE_VALE = SAM_VALES_EX.CVE_VALE) AS TOTAL, "+ 
															"ROUND( "+
															"		(SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_MOV_VALES WHERE CVE_VALE = SAM_VALES_EX.CVE_VALE) "+ 
															"	   -( "+
															"			(SELECT ISNULL(SUM(M.MONTO),0) FROM SAM_MOV_OP AS M INNER JOIN SAM_ORD_PAGO AS O ON (O.CVE_OP = M.CVE_OP) WHERE M.CVE_VALE = SAM_VALES_EX.CVE_VALE AND O.STATUS NOT IN (-2, -1, 4)) "+
															"		   +(SELECT ISNULL(SUM(MV.MONTO),0) FROM SAM_MOV_OP AS MV INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = MV.CVE_OP) WHERE MV.CVE_VALE = SAM_VALES_EX.CVE_VALE /*AND MV.ID_PROYECTO = M.ID_PROYECTO AND MV.CLV_PARTID = M.CLV_PARTID */ AND OP.PERIODO = SAM_VALES_EX.MES AND OP.STATUS NOT IN (-1, -2, 4))  "+
															"		   +(SELECT ISNULL(SUM(P.TOTAL),0) FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) WHERE R.PERIODO = SAM_VALES_EX.MES AND R.CVE_VALE = SAM_VALES_EX.CVE_VALE AND  P.STATUS NOT IN (0, 3, 5)) " +		
															"		) "+
															"	,2) AS DISPONIBLE, "+ 
															"ROUND(( "+
																		" 0 + (SELECT ISNULL(SUM(VT.MONTO), 0) FROM VT_COMPROMISOS AS VT WHERE VT.CONSULTA IN('PRECOMPROMETIDO', 'COMPROMETIDO') AND VT.TIPO_DOC ='VAL' AND VT.CVE_DOC = SAM_VALES_EX.CVE_VALE) "+
															/*
															"			(SELECT ISNULL(SUM(M.MONTO),0) FROM SAM_MOV_OP AS M INNER JOIN SAM_ORD_PAGO AS O ON (O.CVE_OP = M.CVE_OP) WHERE M.CVE_VALE = SAM_VALES_EX.CVE_VALE AND O.STATUS NOT IN (-2, -1, 4)) "+
															"		   +(SELECT ISNULL(SUM(R.IMPORTE),0) FROM SAM_COMP_REQUISIC AS R INNER JOIN SAM_REQUISIC AS RR ON (RR.CVE_REQ = R.CVE_REQ) WHERE RR.CVE_VALE = SAM_VALES_EX.CVE_VALE AND R.PERIODO = SAM_VALES_EX.MES AND RR.STATUS NOT IN (0, 4, 5)) "+
															"		   +(SELECT ISNULL(SUM(P.TOTAL),0) FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) WHERE R.PERIODO = SAM_VALES_EX.MES AND R.CVE_VALE = SAM_VALES_EX.CVE_VALE AND  P.STATUS NOT IN (0, 3, 5)) " +
															*/
															"	),2) AS COMPROBADO "+
														"FROM SAM_VALES_EX WHERE "+status+" AND ID_DEPENDENCIA =? "+clausula, new Object[]{idDependencia});
	}
	public List<Map> getListaOSContratos(String num_req, int cve_pers, String cve_unidad, String clv_benefi){
		if(num_req==null) num_req = "";
		if(clv_benefi==null) clv_benefi = "";
		
		return this.getJdbcTemplate().queryForList("SELECT "+
				"R.CVE_REQ, "+
				"R.NUM_REQ, "+
				"R.FECHA, "+
				"R.OBSERVA, "+
				"CB.NCOMERCIA, "+
				"CB.CLV_BENEFI, " +
				"C.N_PROGRAMA AS PROYECTO, "+
				"C.PROG_PRESUP AS NOMBRE_PROYECTO, "+
				"R.CLV_PARTID, "+
				"CP.PARTIDA AS NOMBRE_PARTIDA, "+
				"C.ID_RECURSO, "+
				"P.RECURSO AS TIPO_GASTO, "+
				"ISNULL((SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS AS M WHERE M.CVE_REQ = R.CVE_REQ),0) AS IMPORTE "+
			"FROM SAM_REQUISIC AS R "+
				"LEFT JOIN SAM_ORDEN_TRAB AS O ON (O.CVE_REQ = R.CVE_REQ) "+ 
				"LEFT JOIN CAT_BENEFI AS CB ON (CB.CLV_BENEFI = O.CLV_BENEFI) " +
				"LEFT JOIN VPROYECTO AS C ON (C.ID_PROYECTO = R.ID_PROYECTO) "+
				"LEFT JOIN CAT_PARTID AS CP ON (CP.CLV_PARTID = R.CLV_PARTID) "+
				"LEFT JOIN CAT_RECURSO AS P ON (P.ID = C.ID_RECURSO) "+
			"WHERE  "+
				"R.ID_DEPENDENCIA = ? "+
				"AND R.NUM_REQ LIKE '%"+num_req+"%' "+ (!clv_benefi.equals("") ? " AND O.CLV_BENEFI = '"+clv_benefi+"'": "") +
				"AND R.STATUS IN (?)", new Object[]{cve_unidad, 1});
	}
	
	//Abraham Gonzalez para verificar 27-06-2017
	public String getBeneficiarioContratos(String tipo_doc, Long cve_doc)
	{
		if(tipo_doc.equals("PED"))
			return (String)this.getJdbcTemplate().queryForObject("SELECT C.NCOMERCIA FROM SAM_PEDIDOS_EX AS P INNER JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = P.CLV_BENEFI) WHERE P.CVE_PED = ?", new Object[]{cve_doc}, String.class);
		else
		if(tipo_doc.equals("REQ"))
			return (String)this.getJdbcTemplate().queryForObject("SELECT C.NCOMERCIA FROM SAM_ORDEN_TRAB AS P INNER JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = P.CLV_BENEFI) WHERE P.CVE_REQ = ?", new Object[]{cve_doc}, String.class);
		else 
		if(tipo_doc.equals("CON"))
			return (String)this.getJdbcTemplate().queryForObject("SELECT C.NCOMERCIA FROM SAM_CONTRATOS AS CON INNER JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = CON.CLV_BENEFI) WHERE CON.CVE_CONTRATO = ?", new Object[]{cve_doc}, String.class);
		else
			return (String)this.getJdbcTemplate().queryForObject("SELECT C.NCOMERCIA FROM SAM_VALES_EX AS V INNER JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = V.CLV_BENEFI) WHERE V.CVE_VALE = ?", new Object[]{cve_doc}, String.class);
	}
	
	// DEMO DEL LISTADO DE VALES PARA COMPROBAR DESDE CONTRATOS------------------------------------28-06-17
		@SuppressWarnings("unchecked")
		public List<Map> getListaValesContratos(String num_vales, int cve_pers, String cve_unidad, String clv_benefi){
			if(num_vales==null) num_vales = "";
			if(clv_benefi==null) clv_benefi = "";
			
			return this.getJdbcTemplate().queryForList("SELECT "+ 
						"SAM_VALES_EX.CVE_VALE,SAM_VALES_EX.NUM_VALE,CONVERT(NVARCHAR, SAM_VALES_EX.FECHA, 103) AS FECHA,SAM_VALES_EX.JUSTIF,SAM_VALES_EX.CLV_BENEFI,CAT_BENEFI.NCOMERCIA, "+
						"SAM_MOV_VALES.ID_PROYECTO,VPROYECTO.DECRIPCION,CAT_PARTID.CLV_PARTID,CAT_PARTID.PARTIDA,SAM_VALES_EX.ID_RECURSO, "+
						"CAT_RECURSO.RECURSO, ISNULL((SAM_MOV_VALES.IMPORTE),0.00)COMPROMISO,ISNULL(SUM(COMP_VALES.IMPORTE),0.00)COMPROBADO,(ISNULL((SAM_MOV_VALES.IMPORTE),0.00)-ISNULL(SUM(COMP_VALES.IMPORTE),0.00))POR_COMPROBAR "+
						"FROM SAM_MOV_VALES "+
						"INNER JOIN SAM_VALES_EX ON SAM_VALES_EX.CVE_VALE=SAM_MOV_VALES.CVE_VALE "+
						"LEFT JOIN COMP_VALES ON (COMP_VALES.CVE_VALE=SAM_VALES_EX.CVE_VALE) AND (COMP_VALES.CLV_PARTID=SAM_MOV_VALES.CLV_PARTID) AND (COMP_VALES.ID_PROYECTO=SAM_MOV_VALES.ID_PROYECTO) "+
						"LEFT JOIN CAT_BENEFI ON CAT_BENEFI.CLV_BENEFI=SAM_VALES_EX.CLV_BENEFI "+
						"LEFT JOIN VPROYECTO ON VPROYECTO.ID_PROYECTO=SAM_MOV_VALES.ID_PROYECTO "+
						"LEFT JOIN CAT_PARTID ON (CAT_PARTID.CLV_PARTID = SAM_MOV_VALES.CLV_PARTID) "+
						"LEFT JOIN CAT_RECURSO ON (CAT_RECURSO.ID = SAM_VALES_EX.ID_RECURSO) "+
						"LEFT JOIN CONTROL_PAGOS ON CONTROL_PAGOS.DOCUMENTO=SAM_VALES_EX.CVE_VALE AND CONTROL_PAGOS.FECHA_CANCEL IS NULL "+
						"WHERE "+
						"SAM_VALES_EX.STATUS=4 AND SAM_VALES_EX.ID_DEPENDENCIA = ? " + 
						"AND SAM_VALES_EX.NUM_VALE LIKE '%"+num_vales+"%' " + (!clv_benefi.equals("") ? " AND CAT_BENEFI.CLV_BENEFI = '"+clv_benefi+"'": "")+
						"GROUP BY SAM_VALES_EX.CVE_VALE,SAM_VALES_EX.NUM_VALE,SAM_VALES_EX.FECHA,SAM_VALES_EX.JUSTIF,SAM_VALES_EX.CLV_BENEFI,CAT_BENEFI.NCOMERCIA,"+
						"SAM_MOV_VALES.ID_PROYECTO,VPROYECTO.DECRIPCION,CAT_PARTID.CLV_PARTID,CAT_PARTID.PARTIDA,SAM_VALES_EX.ID_RECURSO,SAM_MOV_VALES.IMPORTE,"+
						"CAT_RECURSO.RECURSO "+
						//"HAVING (ISNULL((MV.IMPORTE),0.00)-ISNULL(SUM(CV.IMPORTE),0.00))>0"+
						"ORDER BY SAM_VALES_EX.CVE_VALE"
						, new Object[]{cve_unidad});
		}
}
