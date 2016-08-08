/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 19/01/2013
 * @Descriopcion metodos para el uso del modulo de facturas
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
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class GatewayFacturas extends BaseGateway {
	@Autowired
	public GatewayMeses gatewayMeses;
	@Autowired
	public GatewayRequisicion gatewayRequisicion;
	
	private List<Map> presupuesto = new ArrayList<Map>();
	//private List<HashMap<String,String>> hashMaps = new ArrayList<HashMap<String,String>>();
	
	public GatewayFacturas() {
		// TODO Auto-generated method stub
	}
	
	public List<Map> getListEntradasFactura(Long cve_ped)
	{
		return this.getJdbcTemplate().queryForList("SELECT E.ID_ENTRADA, E.FOLIO, E.ID_PEDIDO, P.CLV_BENEFI, P.NUM_PED, CONVERT(VARCHAR(10), E.FECHA,103) AS FECHA, E.IVA, E.SUBTOTAL, E.TOTAL, E.DESCRIPCION, T.DESCRIPCION AS TIPO_ENTRADA FROM ENTRADAS AS E INNER JOIN TIPO_ENTRADA AS T ON (T.ID_TIPO_ENTRADA = E.ID_TIPO_ENTRADA) INNER JOIN SAM_PEDIDOS_EX AS P ON (P.CVE_PED = E.ID_PEDIDO) WHERE E.ID_PEDIDO = ? AND (SELECT COUNT(*) FROM SAM_FACTURAS WHERE STATUS IN (1) AND SAM_FACTURAS.ID_ENTRADA = E.ID_ENTRADA) = 0", new Object[]{cve_ped});
	}
	
	public String getBeneficiarioFactura(String tipo_doc, Long cve_doc)
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
	
	public Map getPresupuesto(String tipo_doc, Long cve_doc, int ejercicio)
	{
		Map documento = new HashMap();
		int mesActivo =gatewayMeses.getMesActivo(ejercicio);
		
		if(tipo_doc.equals("PED"))
			documento = this.getJdbcTemplate().queryForMap("SELECT R.ID_PROYECTO, VP.PROG_PRESUP AS PROGRAMA, R.CLV_PARTID, CP.PARTIDA FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = R.ID_PROYECTO) INNER JOIN CAT_PARTID AS CP ON (CP.CLV_PARTID = R.CLV_PARTID) WHERE P.CVE_PED = ?", new Object[]{cve_doc});
		else if(tipo_doc.equals("REQ"))
			documento = this.getJdbcTemplate().queryForMap("SELECT R.ID_PROYECTO, VP.PROG_PRESUP AS PROGRAMA, R.CLV_PARTID, CP.PARTIDA FROM SAM_REQUISIC AS R INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = R.ID_PROYECTO) INNER JOIN CAT_PARTID AS CP ON (CP.CLV_PARTID = R.CLV_PARTID) WHERE R.CVE_REQ = ?", new Object[]{cve_doc});
		else if(tipo_doc.equals("CON"))
			documento = this.getJdbcTemplate().queryForMap("SELECT VT.ID_PROYECTO, VP.PROG_PRESUP AS PROGRAMA, VT.CLV_PARTID, CP.PARTIDA FROM VT_COMPROMISOS AS VT INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = VT.ID_PROYECTO) INNER JOIN CAT_PARTID AS CP ON (CP.CLV_PARTID = VT.CLV_PARTID) WHERE VT.TIPO_DOC = 'CON' AND VT.PERIODO = ? AND VT.CVE_DOC = ?", new Object[]{mesActivo,cve_doc});
		else
			documento = this.getJdbcTemplate().queryForMap("SELECT TOP 1 M.ID_PROYECTO, VP.PROG_PRESUP AS PROGRAMA, M.CLV_PARTID, CP.PARTIDA FROM SAM_MOV_VALES AS M INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = M.ID_PROYECTO) INNER JOIN CAT_PARTID AS CP ON (CP.CLV_PARTID = M.CLV_PARTID) WHERE M.CVE_VALE = ?", new Object[]{cve_doc});
		
		return this.getJdbcTemplate().queryForMap("SELECT '"+this.getNombreMes(mesActivo)+"' AS MES, "+mesActivo+" AS PERIODO, ID_PROYECTO, N_PROGRAMA, '"+documento.get("PROGRAMA").toString()+"' AS PROGRAMA, '"+documento.get("CLV_PARTID").toString()+"' AS CLV_PARTID, '"+documento.get("PARTIDA").toString()+"' AS PARTIDA,  ISNULL(dbo.getAutorizado(?,?,?,?),0) AS AUTORIZADO, ISNULL(dbo.getPrecomprometido(?,?,?),0) AS PRECOMPROMETIDO, ISNULL(dbo.getComprometido(?,?,?),0) AS COMPROMETIDO,  ISNULL(dbo.getDevengado(?,?,?),0) AS DEVENGADO, ISNULL(dbo.getEjercido(?,?,?),0) AS EJERCIDO, ISNULL(dbo.getDisponible(?,?,?),0) AS DISPONIBLE FROM CEDULA_TEC AS CT WHERE CT.ID_PROYECTO = ? ", new Object[]{mesActivo, mesActivo, documento.get("ID_PROYECTO"), documento.get("CLV_PARTID"),  mesActivo, documento.get("ID_PROYECTO"), documento.get("CLV_PARTID"),  mesActivo, documento.get("ID_PROYECTO"), documento.get("CLV_PARTID"),  mesActivo, documento.get("ID_PROYECTO"), documento.get("CLV_PARTID"), mesActivo, documento.get("ID_PROYECTO"), documento.get("CLV_PARTID"), mesActivo, documento.get("ID_PROYECTO"), documento.get("CLV_PARTID"), documento.get("ID_PROYECTO")});
		
	}
	
	public String getNombreMes(int mes){
		String m = "";
		switch(mes){
		case 1: m = "Enero";
			break;
		case 2: m = "Febrero";
			break;
		case 3: m = "Marzo";
			break;
		case 4: m = "Abril";
			break;
		case 5: m = "Mayo";
			break;
		case 6: m = "Junio";
			break;
		case 7: m = "Julio";
			break;
		case 8: m = "Agosto";
			break;
		case 9: m = "Septiembre";
			break;
		case 10: m = "Octubre";
			break;
		case 11: m = "Noviembre";
			break;
		case 12: m = "Diciembre";
			break;
		}
		return m;
	}
	
	public void guardarDetalle(Long cve_factura, int idProyecto, String clv_partid, Double importe, String notas)
	{
		try
		{
			//Buscar si ya existe el proyecto o partida
			boolean existe = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURA_DETALLE WHERE CVE_FACTURA =? AND ID_PROYECTO=? AND CLV_PARTID=?", new Object[]{cve_factura, idProyecto, clv_partid})>0;
			if(existe)
			{
				Map detalle = this.getJdbcTemplate().queryForMap("SELECT * FROM SAM_FACTURA_DETALLE WHERE CVE_FACTURA =? AND ID_PROYECTO=? AND CLV_PARTID=?",  new Object[]{cve_factura, idProyecto, clv_partid});
				this.getJdbcTemplate().update("UPDATE SAM_FACTURA_DETALLE SET IMPORTE = (IMPORTE + ?) WHERE CVE_FACTURA =? AND ID_PROYECTO =? AND CLV_PARTID=?",new Object[]{importe, cve_factura, idProyecto, clv_partid});
			}
			else
			{
				this.getJdbcTemplate().update("INSERT INTO SAM_FACTURA_DETALLE(CVE_FACTURA, ID_PROYECTO, CLV_PARTID, IMPORTE, NOTAS) VALUES(?,?,?,?,?)", new Object[]{cve_factura, idProyecto, clv_partid, importe, notas});
			}
			
			//Actualizar el importe de la factura
			BigDecimal total = (BigDecimal) this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_FACTURA_DETALLE WHERE CVE_FACTURA =?", new Object[]{cve_factura}, BigDecimal.class);
			this.getJdbcTemplate().update("UPDATE SAM_FACTURAS SET TOTAL = (SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_FACTURA_DETALLE WHERE CVE_FACTURA =?) WHERE CVE_FACTURA =?", new Object[]{cve_factura, cve_factura});
			
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public List<Map> getDetallesFactura(Long cve_factura)
	{
		return this.getJdbcTemplate().queryForList("SELECT F.*, VP.N_PROGRAMA, VP.UNIDADADM AS DEPENDENCIA "+
														"FROM SAM_FACTURA_DETALLE AS F " +
														"INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = F.ID_PROYECTO) " +
														"WHERE F.CVE_FACTURA =? ORDER BY VP.N_PROGRAMA, F.CLV_PARTID ASC", new Object[]{cve_factura});
	}
	
	public void eliminarDetalles(final Long cve_factura, List<String>detalles)
	{
		for(final String row: detalles)
		{
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	final String[] det = row.split("-");
	            	getJdbcTemplate().update("DELETE FROM SAM_FACTURA_DETALLE WHERE CVE_FACTURA =? AND ID_PROYECTO =? AND CLV_PARTID =?", new Object[]{cve_factura, det[0], det[1]});
	            }
			});
		}
		
	}
	
	public Long guardarFactura(Long cve_factura, String tipo_doc, Long cve_doc, int idTipoFactura, int idDependencia, int idProyecto, String clv_partid, String clv_benefi, int idEntrada, String num_fact, Double iva, Double subtotal, Double total,  String observacion, Date fecha_doc, int ejercicio, int cve_pers)
	{
		try
		{
			String tipoDocto = "";
			
			if(tipo_doc.equals("1")) tipoDocto = "CVE_REQ";
			if(tipo_doc.equals("2")) tipoDocto = "CVE_PED";
			if(tipo_doc.equals("3")) tipoDocto = "CVE_VALE";
			if(tipo_doc.equals("4")) tipoDocto = "CVE_CONTRATO";
			
			if(cve_factura==null)
				cve_factura = 0L;
				
			if(cve_factura==0)
			{
				//Guardar una nueva factura
				if(tipo_doc.equals("5"))
				{
					this.getJdbcTemplate().update("INSERT INTO SAM_FACTURAS(NUM_FACTURA, ID_ENTRADA, CLV_BENEFI, CVE_PERS, ID_TIPO, ID_DEPENDENCIA, NOTAS, EJERCICIO, PERIODO, FECHA, FECHA_DOCUMENTO, SUBTOTAL, IVA, TOTAL, STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
							num_fact,
							idEntrada,
							clv_benefi,
							cve_pers,
							idTipoFactura,
							idDependencia,
							observacion, 
							ejercicio,
							gatewayMeses.getMesActivo(ejercicio),
							new Date(),
							fecha_doc,
							subtotal,
							iva,
							total,
							0
					});
				}
				else
					this.getJdbcTemplate().update("INSERT INTO SAM_FACTURAS("+tipoDocto+", NUM_FACTURA, ID_ENTRADA, CLV_BENEFI, CVE_PERS, ID_TIPO, ID_DEPENDENCIA, NOTAS, EJERCICIO, PERIODO, FECHA, FECHA_DOCUMENTO, SUBTOTAL, IVA, TOTAL, STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
							cve_doc,
							num_fact,
							idEntrada,
							clv_benefi,
							cve_pers,
							idTipoFactura,
							idDependencia,
							observacion, 
							ejercicio,
							gatewayMeses.getMesActivo(ejercicio),
							new Date(),
							fecha_doc,
							subtotal,
							iva,
							total,
							0
					});
				
				cve_factura = this.getJdbcTemplate().queryForLong("SELECT MAX(CVE_FACTURA) from SAM_FACTURAS");
			}
			else
			{
				if(tipo_doc.equals("5"))
				{
					this.getJdbcTemplate().update("UPDATE SAM_FACTURAS SET CLV_BENEFI=?, ID_TIPO=?, ID_DEPENDENCIA=?, ID_ENTRADA=?, NOTAS=?, FECHA_DOCUMENTO=?, SUBTOTAL=?, IVA=?, TOTAL=? WHERE CVE_FACTURA =? ", new Object[]{
							clv_benefi,
							idTipoFactura,
							idDependencia,
							idEntrada,
							observacion, 
							fecha_doc,
							subtotal,
							iva,
							total,
							cve_factura
					});
				}
				else 
					this.getJdbcTemplate().update("UPDATE SAM_FACTURAS SET "+tipoDocto+"=?, CLV_BENEFI=?, ID_TIPO=?, ID_DEPENDENCIA=?, ID_ENTRADA=?, NOTAS=?, FECHA_DOCUMENTO=?, SUBTOTAL=?, IVA=?, TOTAL=? WHERE CVE_FACTURA =? ", new Object[]{
						cve_doc,
						clv_benefi,
						idTipoFactura,
						idDependencia,
						idEntrada,
						observacion, 
						fecha_doc,
						subtotal,
						iva,
						total,
						cve_factura
				});
				
			}
			
			//Actualizar el importe de la factura
			BigDecimal importe = (BigDecimal) this.getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_FACTURA_DETALLE WHERE CVE_FACTURA =?", new Object[]{cve_factura}, BigDecimal.class);
			this.getJdbcTemplate().update("UPDATE SAM_FACTURAS SET TOTAL =? WHERE CVE_FACTURA =?", new Object[]{importe, cve_factura});
			
			return cve_factura;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public void cerrarFactura(final Long cve_factura, final int cve_pers)
	{
		try
		{
			
    		/*if(getPrivilegioEn(cve_pers, 132)){
    			throw new RuntimeException("No cuenta por los privilegios suficientes para realiar esta operación");
    		}*/
			
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	String tipoDocto = "";
	            	String consulta = "";
	            	
            		Map factura = getFactura(cve_factura);
            		List<Map> movimientos = getDetalles(cve_factura);
            				
            		if(factura.get("CVE_REQ")!=null) {tipoDocto = "CVE_REQ"; consulta = "'O.S', 'O.T', 'REQ'";}
	    			if(factura.get("CVE_PED")!=null) {tipoDocto = "CVE_PED"; consulta = "'PED'";}
	    			if(factura.get("CVE_VALE")!=null) {tipoDocto = "CVE_VALE"; consulta = "'VAL'";}
	    			if(factura.get("CVE_CONTRATO")!=null) {tipoDocto = "CVE_CONTRATO"; consulta = "'CON'";}
	    			
	    			/*Compromiso del Vale el tipo obras*/
            		BigDecimal comprobadoVale =new BigDecimal(0);
            		
            		if (factura.get("ID_TIPO").toString().equals("2"))
            			comprobadoVale = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_FACTURAS_VALES WHERE CVE_FACTURA =?", new Object[]{cve_factura}, BigDecimal.class);
            		
            		//Si es de tipo Obra entonces validar validar el presupuesto de Vales
        			if (factura.get("ID_TIPO").toString().equals("2"))
        			{
            			List<Map> Vales = getJdbcTemplate().queryForList("SELECT V.CVE_VALE, V.NUM_VALE, M.ID_PROYECTO, M.CLV_PARTID, SUM(F.IMPORTE) AS TOTAL, dbo.getDisponibleDocumento('VAL', V.CVE_VALE, M.ID_PROYECTO, M.CLV_PARTID) AS DISPONIBLE "+
																				"FROM "+
																				"	SAM_FACTURA_DETALLE AS M "+
																				"	INNER JOIN SAM_FACTURAS_VALES AS F ON (F.CVE_FACTURA = M.CVE_FACTURA) " +
																				"	INNER JOIN SAM_VALES_EX AS V ON (V.CVE_VALE = F.CVE_VALE) "+
																				"WHERE	"+
																				"	M.CVE_FACTURA = ? "+
																				"GROUP BY "+
																				"	V.CVE_VALE, V.NUM_VALE, M.ID_PROYECTO, M.CLV_PARTID", new Object[]{cve_factura});
            			for(Map row: Vales)
            			{
            				if(Double.parseDouble(row.get("TOTAL").toString()) > Double.parseDouble(row.get("DISPONIBLE").toString()))
            					throw new RuntimeException("No se puede cerrar la factura, se excede el disponible en Programa y Partida del Vale: "+row.get("NUM_VALE").toString());
            			}
        			
        			}
            		
            		//Validar el Presupuesto del detalle de facturas
	    			for(Map movimiento : movimientos)
	    			{
	            		/*buscar el compometido a devengar*/
	            		BigDecimal comprometidoMes = (BigDecimal) getJdbcTemplate().queryForObject("SELECT dbo.getComprometido(?, ?, ?)", new Object[]{factura.get("PERIODO"), movimiento.get("ID_PROYECTO"), movimiento.get("CLV_PARTID")}, BigDecimal.class);
	            		BigDecimal presupuestoDisponible = new BigDecimal(0);

	            		/*Compromiso del documento*/
	            		if(!tipoDocto.equals(""))
	            			presupuestoDisponible = (BigDecimal) getJdbcTemplate().queryForObject("SELECT MONTO FROM VT_COMPROMISOS WHERE TIPO_DOC IN("+consulta+") AND ID_PROYECTO = ? AND CLV_PARTID = ? AND CONSULTA = 'COMPROMETIDO' AND PERIODO = ? AND CVE_DOC = ?", new Object[]{movimiento.get("ID_PROYECTO"), movimiento.get("CLV_PARTID"), factura.get("PERIODO"), factura.get(tipoDocto)}, BigDecimal.class);
	            		else
	            		{
	            			//Buscar el disponible del mes para sacar el presupuesto
	            			presupuestoDisponible = (BigDecimal)getJdbcTemplate().queryForObject("SELECT dbo.getDisponible(?,?,?)", new Object[]{factura.get("PERIODO"), movimiento.get("ID_PROYECTO"), movimiento.get("CLV_PARTID")}, BigDecimal.class);
	            		}
	            		
	            		presupuestoDisponible = presupuestoDisponible.setScale(2,BigDecimal.ROUND_HALF_UP);
	            		
	            		boolean presupuesto = false;
	            		
	            		//Validacion normal
	            		if(!factura.get("ID_TIPO").toString().equals("9") && (presupuestoDisponible.doubleValue() + comprobadoVale.doubleValue())>= Double.parseDouble(factura.get("TOTAL").toString()) )
	            			presupuesto = true;
	            		else if(factura.get("ID_TIPO").toString().equals("9") && presupuestoDisponible.doubleValue()>= ((BigDecimal) movimiento.get("IMPORTE")).doubleValue())
	            			presupuesto = true;
	            		else if(factura.get("ID_TIPO").toString().equals("6") && presupuestoDisponible.doubleValue()>= ((BigDecimal) movimiento.get("IMPORTE")).doubleValue())
	            			presupuesto = true;
	            		else
	            			presupuesto = false;
	            		
	            		//Si tiene presupuesto
	            		if(presupuesto)
	            		{ 
	            			Date fecha_finalizado = new Date();
	            			
	            			if(!tipoDocto.equals(""))
	            			{
		            			/*Revisar que tipo de documento se ha cerrado*/
		            			Map documento = getJdbcTemplate().queryForMap("SELECT * FROM VT_COMPROMISOS WHERE TIPO_DOC IN("+consulta+") AND ID_PROYECTO = ? AND CLV_PARTID = ? AND CONSULTA = 'COMPROMETIDO' AND PERIODO = ? AND CVE_DOC = ?", new Object[]{movimiento.get("ID_PROYECTO"), movimiento.get("CLV_PARTID"), factura.get("PERIODO"), factura.get(tipoDocto)});
		            			
		            			if(documento.get("TIPO_DOC").toString().equals("PED")&&(presupuestoDisponible.doubleValue() - Double.parseDouble(factura.get("TOTAL").toString()))<1)
		            				getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET FECHA_FINIQUITADO =?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_PED =?", new Object[]{fecha_finalizado, fecha_finalizado.getMonth()+1, fecha_finalizado.getDay(), documento.get("CVE_DOC")});
		            			
		            			if(documento.get("TIPO_DOC").toString().equals("O.S"))
		            			{
		            				int MesActual = gatewayMeses.getMesActivo(2015);
		            				Map OrdenServicio = getJdbcTemplate().queryForMap("SELECT * FROM SAM_REQUISIC WHERE CVE_REQ=?", new Object[]{documento.get("CVE_DOC")});
		            				//Orden de Servicio Calendarizada
		            				if(OrdenServicio.get("TIPO").toString().equals("8"))
		            				{
		            					BigDecimal CompromisoFuturo = gatewayRequisicion.getCompromisoFuturo(Long.parseLong(documento.get("CVE_DOC").toString()), MesActual);
		            					if(CompromisoFuturo==new BigDecimal(0))
		            						getJdbcTemplate().update("UPDATE SAM_REQUISIC SET FECHA_FINIQUITADO =?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_REQ =?", new Object[]{fecha_finalizado, fecha_finalizado.getMonth()+1, fecha_finalizado.getDay(), documento.get("CVE_DOC")});
		            				}
		            				else if((presupuestoDisponible.doubleValue() - Double.parseDouble(factura.get("TOTAL").toString()))<=1)
		            					getJdbcTemplate().update("UPDATE SAM_REQUISIC SET FECHA_FINIQUITADO =?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_REQ =?", new Object[]{fecha_finalizado, fecha_finalizado.getMonth()+1, fecha_finalizado.getDay(), documento.get("CVE_DOC")});
		            			}
		            			//Orden de Trabajo
		            			else if((documento.get("TIPO_DOC").toString().equals("O.T"))&&(presupuestoDisponible.doubleValue() - Double.parseDouble(factura.get("TOTAL").toString()))<=1)
		            				getJdbcTemplate().update("UPDATE SAM_REQUISIC SET FECHA_FINIQUITADO =?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_REQ =?", new Object[]{fecha_finalizado, fecha_finalizado.getMonth()+1, fecha_finalizado.getDay(), documento.get("CVE_DOC")});
		            			
		            			/*if(documento.get("TIPO_DOC").toString().equals("VAL")&&(comprometidoDoc.doubleValue() - Double.parseDouble(factura.get("TOTAL").toString()))<=0)
		            				getJdbcTemplate().update("UPDATE SAM_VALES_EX SET FECHA_FINIQUITADO =?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_REQ =?", new Object[]{fecha_finalizado, fecha_finalizado.getMonth()+1, fecha_finalizado.getDay(), documento.get("CVE_DOC")});
		            				*/
		            			
		            			//if(documento.get("TIPO_DOC").toString().equals("CON")&&(comprometidoDoc.doubleValue() - Double.parseDouble(factura.get("TOTAL").toString()))<=0)
		            			//	getJdbcTemplate().update("UPDATE SAM_FACTURAS SET FECHA_FINALIZADO =?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_FACTURA =?", new Object[]{fecha_finalizado, fecha_finalizado.getMonth()+1, fecha_finalizado.getDay(), documento.get("CVE_DOC")});

	            			}
	            			
	            			//Aqui estaba el cierre de la factura
	            			/*Guardar en la bitacora*/
	            		}
	            		else
	            			throw new RuntimeException("No se puede cerrar la factura, el importe de a devengar es mayor al compromiso");
            		
	            	}
	    			//Validar equi si se finiquita el contrato
	    			if(factura.get("CVE_CONTRATO")!=null) {
	    				BigDecimal presupuestoContrato = (BigDecimal) getJdbcTemplate().queryForObject("SELECT SUM(MONTO) FROM VT_COMPROMISOS WHERE CONSULTA='COMPROMETIDO' AND TIPO_DOC ='CON' AND CVE_DOC = ?", new Object[]{factura.get("CVE_CONTRATO")}, BigDecimal.class);
	    				if(Double.parseDouble(factura.get("TOTAL").toString())== presupuestoContrato.doubleValue())
	    				{
	    					//Finiquitar el contrato
	    					Date fecha_finalizado = new Date();
	    					getJdbcTemplate().update("UPDATE SAM_CONTRATOS SET FECHA_FINIQUITADO = ? WHERE CVE_CONTRATO =?",new Object[]{new Date(), factura.get("CVE_CONTRATO")});
	    				}
	    			}
	    			//Cierra la factura
            		Date fecha_finalizado = new Date();
            		getJdbcTemplate().update("UPDATE SAM_FACTURAS SET STATUS = ?, FECHA_CIERRE = ?, FECHA_FINALIZADO =?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_FACTURA =?",new Object[]{1, new Date(), fecha_finalizado, fecha_finalizado.getMonth()+1, fecha_finalizado.getDay(), cve_factura});
    				
            } 
         });
		}
		catch(Exception e)
		{
			throw new RuntimeException("La operacion ha fallado con el siguiente mensaje: " + e.getMessage());
		}
	}

	public List<Map> getDetalles(Long cve_factura){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SAM_FACTURA_DETALLE WHERE CVE_FACTURA = ?", new Object[]{cve_factura});
	}
	
	public List<Map> getListadoFacturas(Map m){
		String sql = "SELECT F.CVE_FACTURA " +
						      ",F.NUM_FACTURA" +
						      ",F.CVE_REQ" +
						      ",F.CVE_PED" +
						      ",F.ID_ENTRADA" +
						      ",F.CVE_OP" +
						      ",F.CVE_PERS" +
						      ",C.NCOMERCIA" +
						      ",R.NUM_REQ" +
						      ",P.NUM_PED" +
						      ",F.ID_DEPENDENCIA" +
						      ",F.NOTAS" +
						      ",F.EJERCICIO" +
						      ",F.PERIODO" +
						      ",F.FECHA" +
						      ",CONVERT(VARCHAR(10), F.FECHA_DOCUMENTO, 103) AS FECHA_DOCUMENTO" +
						      ",F.FECHA_CIERRE" +
						      ",F.FECHA_FINALIZADO" +
						      ",F.SUBTOTAL" +
						      ",F.IVA" +
						      ",F.TOTAL" +
						      ",F.STATUS" +
						      ",(CASE F.STATUS WHEN 0 THEN 'Edición' WHEN 1 THEN 'Cerrado' WHEN 2 THEN 'Cancelado' WHEN 3 THEN 'Finiquitado' END) AS STATUS_DESC " +
						  "FROM SAM_FACTURAS AS F " +
								"LEFT JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = F.CVE_REQ) " +
								"LEFT JOIN SAM_PEDIDOS_EX AS P ON (P.CVE_PED = F.CVE_PED) " +
								"LEFT JOIN SAM_VALES_EX AS V ON (V.CVE_VALE = F.CVE_VALE) "+
								"LEFT JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = (CASE ISNULL(F.CVE_PED,0) WHEN 0 THEN (CASE ISNULL(F.CVE_REQ,0) WHEN 0 THEN (SELECT VAL.CLV_BENEFI FROM SAM_VALES_EX AS VAL WHERE VAL.CVE_VALE = F.CVE_VALE) ELSE (SELECT OT.CLV_BENEFI FROM SAM_ORDEN_TRAB AS OT WHERE OT.CVE_REQ = F.CVE_REQ) END)  ELSE (SELECT PED.CLV_BENEFI FROM SAM_PEDIDOS_EX AS PED WHERE PED.CVE_PED = F.CVE_PED) END )) " +
								" WHERE F.STATUS IN("+m.get("estatus").toString()+") ";
		
		if(m.get("cbodependencia")!=null)
			sql += " AND F.ID_DEPENDENCIA =:cbodependencia";
		
		if(m.get("clv_benefi")!=null)
			if(!m.get("clv_benefi").equals("0"))
				sql+= " AND C.CLV_BENEFI =:clv_benefi";
		
		if (m.get("fechaInicial") != null && m.get("fechaFinal") != null ) 
			if (!m.get("fechaInicial").equals("") && !m.get("fechaFinal").equals("") ) 
				sql += " AND CONVERT(datetime,convert(varchar(10), F.FECHA_DOCUMENTO ,103),103) between :fechaInicial and :fechaFinal ";	
		
		if(m.get("numped")!=null)
			if(!m.get("numped").equals(""))
				sql += " AND P.NUM_PED LIKE '%"+m.get("numped").toString()+"%'";
		
		if(m.get("numreq")!=null)
			if(!m.get("numreq").equals(""))
				sql += " AND R.NUM_REQ LIKE '%"+m.get("numreq").toString()+"%'";
		
		if(m.get("numfactura")!=null)
			if(!m.get("numfactura").equals(""))
				sql += " AND F.NUM_FACTURA LIKE '%"+m.get("numfactura").toString()+"%'";
		
		sql+=" ORDER BY F.CVE_FACTURA ASC";
		return this.getNamedJdbcTemplate().queryForList(sql, m);	
	}
	
	public Map getFactura(Long cve_factura){
		try
		{
			String sql = "SELECT F.CVE_FACTURA " +
				      ",F.NUM_FACTURA" +
				      ",F.CVE_REQ" +
				      ",F.CVE_PED" +
				      ",F.CVE_VALE" +
				      ",F.CVE_CONTRATO"+
				      ",F.ID_TIPO "+
				     ",(CASE ISNULL(F.CLV_BENEFI,0) WHEN 0 THEN C.CLV_BENEFI ELSE F.CLV_BENEFI END) AS CLV_BENEFI"+
				     
				      ",F.ID_ENTRADA" +
				      ",(SELECT TOP 1 FOLIO FROM ENTRADAS WHERE ID_ENTRADA = F.ID_ENTRADA) AS FOLIO_ENTRADA"+
				       /*",(CASE ISNULL(F.CVE_PED,0) "+
						  "WHEN 0 THEN "+
						  "	(SELECT SR.ID_PROYECTO FROM SAM_REQUISIC AS SR WHERE SR.CVE_REQ = F.CVE_REQ) "+
						  " ELSE "+
						  "	(SELECT EN.ID_PROYECTO FROM ENTRADAS AS EN WHERE EN.ID_ENTRADA = F.ID_ENTRADA) END "+
					      "	  ) AS ID_PROYECTO "+
				      ",(CASE ISNULL(F.CVE_PED,0)  "+
						  "WHEN 0 THEN  "+
						  "	(SELECT SR.CLV_PARTID FROM SAM_REQUISIC AS SR WHERE SR.CVE_REQ = F.CVE_REQ)  "+
						  " ELSE  "+
						  " (SELECT EN.PARTIDA FROM ENTRADAS AS EN WHERE EN.ID_ENTRADA = F.ID_ENTRADA ) END "+
					      ") AS CLV_PARTID "+
					      */
				      ",F.CVE_OP" +
				      ",F.CVE_PERS" +
				      ",C.NCOMERCIA" +
				      ",R.NUM_REQ" +
				      ",P.NUM_PED" +
				      ",V.NUM_VALE"+
				      ",CON.NUM_CONTRATO"+
				      ",F.ID_DEPENDENCIA" +
				      ",F.NOTAS" +
				      ",F.EJERCICIO" +
				      ",F.PERIODO" +
				      ",F.FECHA" +
				      ",CONVERT(VARCHAR(10), F.FECHA_DOCUMENTO, 103) AS FECHA_DOCUMENTO" +
				      ",F.FECHA_CIERRE" +
				      ",F.FECHA_FINALIZADO" +
				      ",F.SUBTOTAL" +
				      ",F.IVA" +
				      ",F.TOTAL" +
				      ",(CASE ISNULL(F.CVE_PED,0) WHEN 0 THEN (SELECT OT.COSTO_TOTAL FROM SAM_ORDEN_TRAB AS OT WHERE OT.CVE_REQ = F.CVE_REQ) ELSE (SELECT TOP 1 E.TOTAL FROM ENTRADAS AS E WHERE E.ID_PEDIDO =F.CVE_PED) END) AS TOTAL_DOC"+
				      ",F.STATUS" +
				      ",(CASE F.STATUS WHEN 0 THEN 'Edición' WHEN 1 THEN 'Cerrado' END) AS STATUS_DESC " +
				  "FROM SAM_FACTURAS AS F " +
						"LEFT JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = F.CVE_REQ) " +
						"LEFT JOIN SAM_PEDIDOS_EX AS P ON (P.CVE_PED = F.CVE_PED) " +
						"LEFT JOIN SAM_VALES_EX AS V ON (V.CVE_VALE = F.CVE_VALE) "+
						"LEFT JOIN SAM_CONTRATOS AS CON ON (CON.CVE_CONTRATO = F.CVE_CONTRATO) "+
						"LEFT JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = (CASE ISNULL(F.CLV_BENEFI,0) WHEN 0 THEN (CASE ISNULL(F.CVE_PED,0) WHEN 0 THEN (CASE ISNULL(F.CVE_REQ,0) WHEN 0 THEN (SELECT VAL.CLV_BENEFI FROM SAM_VALES_EX AS VAL WHERE VAL.CVE_VALE = F.CVE_VALE) ELSE (SELECT OT.CLV_BENEFI FROM SAM_ORDEN_TRAB AS OT WHERE OT.CVE_REQ = F.CVE_REQ) END)  ELSE (SELECT PED.CLV_BENEFI FROM SAM_PEDIDOS_EX AS PED WHERE PED.CVE_PED = F.CVE_PED) END) ELSE F.CLV_BENEFI END )) " +
						" WHERE F.CVE_FACTURA =? ";
			
			return this.getJdbcTemplate().queryForMap(sql, new Object[]{cve_factura});
		}
		catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	private boolean tieneOrdenPago(Long idFactura){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_FACTURAS AS F LEFT JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = F.CVE_OP) WHERE F.CVE_FACTURA = ? AND F.STATUS IN (1) AND OP.STATUS IN (0,1)", new Object[]{idFactura})>0;
	}
	
	public void aperturarFacturas(final Long[] idFacturas, final int cve_pers)
	{
		if(!getPrivilegioEn(cve_pers, 130)){
			throw new RuntimeException("No cuenta por los privilegios suficientes para realiar esta operación");
		}
		
		 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	
	            	for(Long id : idFacturas)
	            	{
	            		//recuperar la factura
	            		Map factura = getFactura(id);
	            		
	            		Date fechaCierre = new Date();
	            		fechaCierre = (Date) factura.get("FECHA_CIERRE");
	            		Calendar c1 = Calendar.getInstance();
	            		System.out.print("MES ACTUAL: "+c1.get(Calendar.MONTH)+ " Mes:"+fechaCierre.getMonth());
	            		
	            		if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1))
	            		{
	            			throw new RuntimeException("No se puede aperturar la factura "+factura.get("NUM_FACTURA").toString()+" por que el periodo es diferente");
	            		}
	            		
	            		/*Verificar si tienen orden de pago e impedir si asi fuera*/
	            		if(tieneOrdenPago(id)){
	            			Map m = getJdbcTemplate().queryForMap("SELECT NUM_FACTURA FROM SAM_FACTURAS WHERE CVE_FACTURA =?", new Object[]{id});
	            			throw new RuntimeException("Imposible aperturar la factura "+m.get("NUM_FACTURA").toString()+", esta ya cuenta con una Orden de Pago");
	            		}
	            		if(factura.get("CVE_REQ")!=null)
	            			getJdbcTemplate().update("UPDATE SAM_REQUISIC SET FECHA_FINIQUITADO = ?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_REQ = ?", new Object[]{null, null, null, factura.get("CVE_REQ")});
	            		if(factura.get("CVE_PED")!=null)
	            			getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET FECHA_FINIQUITADO = ?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_PED = ?", new Object[]{null, null, null, factura.get("CVE_PED")});
	            		if(factura.get("CVE_VALE")!=null)
	            			getJdbcTemplate().update("UPDATE SAM_VALES_EX SET FECHA_FINIQUITADO = ? WHERE CVE_VALE = ?", new Object[]{null, factura.get("CVE_VALE")});
	            		if(factura.get("CVE_CONTRATO")!=null)
	            			getJdbcTemplate().update("UPDATE SAM_CONTRATOS SET FECHA_FINIQUITADO = ? WHERE CVE_CONTRATO = ?", new Object[]{null, factura.get("CVE_CONTRATO")});
	            		
	            		getJdbcTemplate().update("UPDATE SAM_FACTURAS SET STATUS = ? WHERE CVE_FACTURA = ?", new Object[]{0, id});
	            	}
	            }
		 });
	}
	
	public void cancelarFacturas(final Long[] idFacturas, final int cve_pers, final int ejercicio)
	{
		if(!getPrivilegioEn(cve_pers, 131)){
			throw new RuntimeException("No cuenta por los privilegios suficientes para realiar esta operación");
		}
		
		 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	for(Long id : idFacturas)
	            	{
	            		//recuperar la factura
	            		Map factura = getFactura(id);
	            		
	            		//Buscar si existe el Super Privilegio para Cancelar Facturas
	    				boolean privilegio = getPrivilegioEn(cve_pers, 138);
	    				
	    				Date fechaCierre = new Date();
	    		  		fechaCierre = (Date) factura.get("FECHA_CIERRE");
	    		  		Calendar c1 = Calendar.getInstance();
	    		  		//Si el contrato no es del periodo y no tiene super-privilegio entonces no dejar cancelarlo
	    		  		if(fechaCierre!=null&&privilegio==false)
	    			  		if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1))
	    			  		{
	    			  			throw new RuntimeException("No se puede cancelar la factura "+factura.get("NUM_FACTURA").toString()+", el periodo del documento es diferente al actual, consulte a su administrador");
	    			  		}
	            		
	            		/*if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE CVE_FACTURA =? AND STATUS>0 AND PERIODO<>?", new Object[]{id, gatewayMeses.getMesActivo(ejercicio)})>0&&privilegio==false){
	            			Map m = getJdbcTemplate().queryForMap("SELECT NUM_FACTURA FROM SAM_FACTURAS WHERE CVE_FACTURA =?", new Object[]{id});
	            			throw new RuntimeException("Imposible cancelar la factura "+m.get("NUM_FACTURA").toString()+", el periodo ya no es válido, consulte a su administrador del sistema");
	            		}*/
	            		
	            		/*Verificar si tienen orden de pago e impedir si asi fuera*/
	            		if(tieneOrdenPago(id)){
	            			Map m = getJdbcTemplate().queryForMap("SELECT NUM_FACTURA FROM SAM_FACTURAS WHERE CVE_FACTURA =?", new Object[]{id});
	            			throw new RuntimeException("Imposible cancelar la factura "+m.get("NUM_FACTURA").toString()+", esta ya cuenta con una Orden de Pago");
	            		}
	            		
	            		if(factura.get("CVE_REQ")!=null)
	            			getJdbcTemplate().update("UPDATE SAM_REQUISIC SET FECHA_FINIQUITADO = ?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_REQ = ?", new Object[]{null, null, null, factura.get("CVE_REQ")});
	            		if(factura.get("CVE_PED")!=null)
	            			getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET FECHA_FINIQUITADO = ?, MES_FINALIZADO=?, DIA_FINALIZADO=? WHERE CVE_PED = ?", new Object[]{null, null, null, factura.get("CVE_PED")});
	            		if(factura.get("CVE_VALE")!=null)
	            			getJdbcTemplate().update("UPDATE SAM_VALES_EX SET FECHA_FINIQUITADO = ? WHERE CVE_VALE = ?", new Object[]{null, factura.get("CVE_VALE")});
	            		/*if(factura.get("CVE_CONTRATO")!=null)
	            			getJdbcTemplate().update("UPDATE SAM_CONTRATOS SET FECHA_FINIQUITADO = ? WHERE CVE_CONTRATO = ?", new Object[]{null, factura.get("CVE_CONTRATO")});*/
	            		
	            		//Si factura = Edicion no poner fecha de Cancelado
	            		//Si factura es diferente de Edicion entnces poner fecha de cancelado
	            		if(factura.get("STATUS").toString().equals("0"))
	            			getJdbcTemplate().update("UPDATE SAM_FACTURAS SET STATUS = ?, FECHA_FINALIZADO=?, CVE_OP=? WHERE CVE_FACTURA = ?", new Object[]{2, null, null, id});
	            		else
	            			getJdbcTemplate().update("UPDATE SAM_FACTURAS SET STATUS = ?, FECHA_CANCELADO =?, FECHA_FINALIZADO=?, CVE_OP=? WHERE CVE_FACTURA = ?", new Object[]{2, new Date(), null, null, id});
	            	}
	            }
		 });
	}
	
	public Long guardarArchivo(Long cve_factura, String nombreArchivo, String path, Date fecha, String ext, Long size){
		this.getJdbcTemplate().update("INSERT INTO SAM_FACTURAS_ARCHIVOS(CVE_FACTURA, NOMBRE, RUTA, FECHA, EXT, TAMANO) VALUES(?,?,?,?,?,?)", new Object[]{
				cve_factura,
				nombreArchivo,
				path, 
				fecha,
				ext,
				size
		});
		
		return this.getJdbcTemplate().queryForLong("SELECT MAX(ID_ARCHIVO) AS N FROM SAM_FACTURAS_ARCHIVOS");
	}
	
	public List<Map> getArchivosFactura(Long cve_factura){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SAM_FACTURAS_ARCHIVOS WHERE CVE_FACTURA=?", new Object[]{cve_factura});
	}
	
	public void eliminarArchivoFactura(int cve_pers, Long idArchivo, HttpServletRequest request){
		//si tiene los privilegios elimina
		//if(!getPrivilegioEn(cve_pers, 129))
		//	throw new RuntimeException("No se puede eliminar el archivo, su usuario no cuenta con los privilegios suficientes");
		
		Map archivo = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_FACTURAS_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		/*eliminado fisico*/
		File fichero = new File(request.getSession().getServletContext().getRealPath("")+"/sam/facturas/archivos/["+archivo.get("ID_ARCHIVO")+"] "+archivo.get("NOMBRE").toString());
	   
		if (fichero.delete()){
			 System.out.println("El fichero ha sido borrado satisfactoriamente");
			 this.getJdbcTemplate().update("DELETE FROM SAM_FACTURAS_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		}
		else
			System.out.println("El fichero no puede ser borrado");
	}
	
	public List<Map> getListaFacturasOrdenPago(int idRecurso, int idDependencia, String clv_benefi, int cve_pers){
		return this.getJdbcTemplate().queryForList("SELECT CVE_FACTURA ,NUM_FACTURA, CVE_REQ, CVE_PED, ID_ENTRADA, NOTAS, SUM(TOTAL) AS TOTAL, TOTAL_DOC, STATUS, STATUS_DESC " +
					 " FROM ( SELECT F.CVE_FACTURA " +
						      ",F.NUM_FACTURA" +
						      ",F.CVE_REQ" +
						      ",F.CVE_PED" +
						      ",F.ID_ENTRADA" +
						      ",(SELECT TOP 1 FOLIO FROM ENTRADAS WHERE ID_ENTRADA = F.ID_ENTRADA) AS FOLIO_ENTRADA"+
						      ",M.ID_PROYECTO "+
						      ",M.CLV_PARTID "+
						      ",F.CVE_OP" +
						      ",F.CVE_PERS" +
						      ",C.CLV_BENEFI"+
						      ",C.NCOMERCIA" +
						      ",R.NUM_REQ" +
						      ",P.NUM_PED" +
						      ",F.ID_DEPENDENCIA" +
						      ",F.NOTAS" +
						      ",F.EJERCICIO" +
						      ",F.PERIODO" +
						      ",F.FECHA" +
						      ",CONVERT(VARCHAR(10), F.FECHA_DOCUMENTO, 103) AS FECHA_DOCUMENTO" +
						      ",F.FECHA_CIERRE" +
						      ",F.FECHA_FINALIZADO" +
						      ",F.SUBTOTAL" +
						      ",F.IVA" +
						      ",F.TOTAL" +
						      ",(CASE ISNULL(F.CVE_PED,0) WHEN 0 THEN (SELECT OT.COSTO_TOTAL FROM SAM_ORDEN_TRAB AS OT WHERE OT.CVE_REQ = F.CVE_REQ) ELSE (SELECT TOP 1 E.TOTAL FROM ENTRADAS AS E WHERE E.ID_PEDIDO =F.CVE_PED) END) AS TOTAL_DOC"+
						      ",F.STATUS" +
						      ",(CASE F.STATUS WHEN 0 THEN 'Edición' WHEN 1 THEN 'Cerrado' END) AS STATUS_DESC " +
						  "FROM SAM_FACTURAS AS F " +
						        "INNER JOIN SAM_FACTURA_DETALLE AS M ON (M.CVE_FACTURA = F.CVE_FACTURA) "+
								"LEFT JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = F.CVE_REQ) " +
								"LEFT JOIN SAM_PEDIDOS_EX AS P ON (P.CVE_PED = F.CVE_PED) " +
								"LEFT JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = M.ID_PROYECTO) " +
								"LEFT JOIN CAT_RECURSO AS CR ON (CR.ID = VP.ID_RECURSO) "+
								"LEFT JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = F.CLV_BENEFI) " +
								" WHERE F.STATUS=1 AND " +
								(idDependencia == 8 /*DECUR*/ ? "F.ID_DEPENDENCIA IN(?, 17, 18, 51) " : "F.ID_DEPENDENCIA =? ") +
								"AND C.CLV_BENEFI =? AND F.CVE_FACTURA NOT IN (SELECT M.CVE_FACTURA FROM SAM_MOV_OP AS M WHERE (M.CVE_FACTURA = F.CVE_FACTURA)) " + (idRecurso!=0 ? " AND CR.ID = "+idRecurso: "") +
								") AS R " +
						" GROUP BY CVE_FACTURA ,NUM_FACTURA, CVE_REQ, CVE_PED, ID_ENTRADA, NOTAS, TOTAL_DOC, STATUS, STATUS_DESC ", new Object[]{idDependencia, clv_benefi/*, idRecurso*/});
								
	}
	
	public List<Map> getListaAnexosArchivosFacturas(Long cve_op){
		return this.getJdbcTemplate().queryForList("select a.CVE_OP,a.ANX_CONS,a.T_DOCTO,a.NUMERO,a.NOTAS , b.DESCR, a.FILENAME, a.FILETYPE, a.FILELENGTH, a.FILEPATH from SAM_OP_ANEXOS a , tipodoc_op b where a.t_docto=b.t_docto and cve_op=? ORDER BY a.ANX_CONS ASC", new Object[]{cve_op});
	}
	
	public  void guardarRetencion( final Integer idRetencion, final String  retencion,final Double importe, final Long cve_factura){
		 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	  Double importeRetencion = importe;
	            	  int idRetencion2 = 0;
		              int existe = getJdbcTemplate().queryForInt("select count(*) from  SAM_FACTURA_MOV_RETENC where CVE_FACTURA=? and CLV_RETENC=? AND CONS!=? ", new Object []{cve_factura,retencion,idRetencion==null ?-1 :idRetencion });    	    
		      		  if (existe==0){
		      			  int tipoRetencion = getJdbcTemplate().queryForInt("SELECT COUNT(*) from CAT_RETENC  where CLV_RETENC=?  and tipo='CR' ", new Object[]{retencion});
		      			  
		      			  if (tipoRetencion==1)
		      					importeRetencion=importeRetencion*-1;
		      				
		      			  if (idRetencion == null) {
		      				  idRetencion2 = getJdbcTemplate().queryForInt("SELECT ISNULL(MAX(CONS),0) FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA=? ", new Object[]{cve_factura})+1;
		      				  
		      				  String folio=rellenarCeros(cve_factura.toString(),6);
		      					getJdbcTemplate().update("insert into SAM_FACTURA_MOV_RETENC (CVE_FACTURA, CONS, CLV_RETENC, IMPORTE) " +
		      							"VALUES (?,?,?,?)"
		      							, new Object[]{cve_factura,idRetencion2,retencion,importeRetencion});
		      			  }
		      			  else
		      			  {
		      				  String folio=rellenarCeros(cve_factura.toString(),6);
		      					getJdbcTemplate().update("UPDATE SAM_FACTURA_MOV_RETENC  SET  CLV_RETENC=?, IMPORTE=?  where CVE_FACTURA=? AND CONS=? "
		      						, new Object[]{retencion,importeRetencion,cve_factura,idRetencion});
		      			  }
		      		  }
		      		  else
		      			  throw new RuntimeException("La retención que intenta guardar ya existe");    	  
	            }
		 });    
    }
	
	public List getRetenciones(Long cve_factura) {	   
		   return this.getJdbcTemplate().queryForList("select M.CVE_FACTURA, " +
	            "M.CONS,M.CLV_RETENC,R.RETENCION,M.IMPORTE, " +
	            "M.CVE_FACTURA,R.TIPO " +
	            "from SAM_FACTURA_MOV_RETENC AS M, CAT_RETENC AS R " +
	            "where R.CLV_RETENC = M.CLV_RETENC AND M.CVE_FACTURA= ? ORDER BY M.CONS ASC", new Object[]{cve_factura});
	}
	
	public void  eliminarRetenciones( final List<Integer> retenciones,final  Long cve_factura ) {
		  try {                
			  this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Integer id :retenciones)
	                		getJdbcTemplate().update("DELETE FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA = ? AND CONS=? ", new Object[]{cve_factura,id});           			                	
	                } });
	                } catch (DataAccessException e) {                     
	                    throw new RuntimeException(e.getMessage(),e);
	          }	                	                		  	  
	  }
	
	public List<Map> getTipoFacturas(){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SAM_CAT_TIPO_FACTURAS WHERE STATUS=1 ORDER BY DESCRIPCION ASC");
	}
	
	public int getCountArchivos(Long cve_factura)
	{
		
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_FACTURAS_ARCHIVOS WHERE CVE_FACTURA = ?", new Object[]{cve_factura});
	}
	
	public void borrarDatosNomina(){
		this.getJdbcTemplate().update("DELETE FROM SAM_NOMINA");
		this.getJdbcTemplate().update("DELETE FROM SAM_NOMINA_DEDUCCIONES");
	}
	
	private void getPresupuestoInicializa()
	{
		presupuesto = getJdbcTemplate().queryForList("SELECT "+
																"ID_PROYECTO, "+
																"CLV_PARTID, "+
																"IMPORTE, "+
																"DISPONIBLE_MES,"+
																"(DISPONIBLE_MES - IMPORTE) AS DIFERENCIA,"+
																"MES "+
												 "FROM( "+
														"SELECT  "+
																"N.ID_PROYECTO, "+ 
																"N.CLV_PARTID, "+
																"SUM(N.IMPORTE) AS IMPORTE, "+
																"dbo.getDisponible(N.MES, N.ID_PROYECTO, N.CLV_PARTID) AS DISPONIBLE_MES, "+
																"N.MES "+
														"FROM SAM_NOMINA AS N "+
																"INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = N.ID_PROYECTO)  "+
																"INNER JOIN CAT_PARTID AS CP ON (CP.CLV_PARTID = N.CLV_PARTID)  "+
														"GROUP BY N.ID_PROYECTO, N.CLV_PARTID, CP.PARTIDA, N.MES "+
												") AS B "+
												"ORDER BY ID_PROYECTO, CLV_PARTID ASC");
		
	}
	
	private boolean getDisponiblePresupuesto(int idProyecto, String clv_partid, Double monto){
		for(Map row: presupuesto){
			if((Integer) row.get("ID_PROYECTO") == idProyecto && row.get("CLV_PARTID").toString().equals(clv_partid))
			{
				if(Double.parseDouble(row.get("DIFERENCIA").toString()) < 0)
					return false;
				if(Double.parseDouble(row.get("DIFERENCIA").toString()) >= 0)
					return true;
			}
		}
		return false;
	}

	public void crearFacturaOrdenPago(final int cve_pers, final int ejercicio, final int idDependencia, final int idGrupo){
		try {                
			  this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                	protected void   doInTransactionWithoutResult(TransactionStatus status) {	
	                	int idGrupoFirma=401; //(19) Grupo de firmas Direccion de Administracion
	                		System.out.println("Leemos los datos de la nomina para procesarlos");
	                		List<Map> nomina = getJdbcTemplate().queryForList("SELECT  N.TIPO_NOMINA, "+
																						"VP.ID_RECURSO, "+
																						"VP.CLV_UNIADM, "+
																						"VP.UNIDADADM, "+
																						"N.ID_PROYECTO,  "+
																						"VP.N_PROGRAMA,  "+
																						"VP.ACT_INST,  "+
																						"N.CLV_PARTID, "+
																						"CP.PARTIDA, "+
																						"SUM(N.IMPORTE) AS IMPORTE, "+
																						/*"dbo.getDisponible(N.MES, N.ID_PROYECTO, N.CLV_PARTID) AS DISPONIBLE_MES, "+*/
																						"N.MES, "+
																						"N.NOTA, "+
																						"CONVERT(varchar(10), N.FECHA_NOMINA, 103) AS FECHA_NOMINA "+
																				"FROM SAM_NOMINA AS N "+
																						"INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = N.ID_PROYECTO)  "+
																						"INNER JOIN CAT_PARTID AS CP ON (CP.CLV_PARTID = N.CLV_PARTID)  "+
																				"GROUP BY N.TIPO_NOMINA, VP.ID_RECURSO, VP.CLV_UNIADM, VP.UNIDADADM, N.ID_PROYECTO, VP.N_PROGRAMA, VP.ACT_INST, N.CLV_PARTID, CP.PARTIDA, N.MES, N.NOTA, FECHA_NOMINA  "+
																				"ORDER BY N.TIPO_NOMINA, VP.ID_RECURSO, VP.UNIDADADM, N.ID_PROYECTO, N.CLV_PARTID ASC");
	                		
	                		getPresupuestoInicializa();

	                		String tipoNomina = "";
	                		int idRecurso = 0;
	                		String clv_uniadm = "";
	                		
	                		Vector contenedor = new Vector();
	                		int idRecursoAnt = 0;
	                		int periodoAnt = 0;
	                		String fecha_nomina = "";
	                		String notaAnt = "";
	                		Double retencAnt = 0.00;
	                		Vector datos = new Vector();
	                		Long cve_factura = 0l;
	                		int valor = 0;
	                		int total_req = nomina.size();
	                		
	                		for(Map row: nomina){
	                			//Valida aqui el preupuesto del proyecto y partida para cerrar las facturas
	                			boolean disponible = getDisponiblePresupuesto((Integer) row.get("ID_PROYECTO"), row.get("CLV_PARTID").toString(), Double.parseDouble(row.get("IMPORTE").toString()));
	                			
	                			valor++;
	                			System.out.println("Leemos las deductivas");
	                			//Buscamos las deductivas de grupo de movimientos actuales
	                			List<Map> deductivas = getJdbcTemplate().queryForList("SELECT  A.TIPO_NOM, A.ID_RECURSO, A.RECINTO, CU.UNIDADADM, A.CLV_RETENC, SUM (A.TOTAL) AS IMPORTE FROM SAM_NOMINA_DEDUCCIONES AS A "+
																									"INNER JOIN CAT_UNIADM AS CU ON (CU.CLV_UNIADM = A.RECINTO) " +
																								"WHERE A.TIPO_NOM =? AND A.ID_RECURSO =? AND A.RECINTO =? "+
																								"GROUP BY A.TIPO_NOM, A.ID_RECURSO, A.RECINTO, CU.UNIDADADM, A.CLV_RETENC "+
																								"ORDER BY A.TIPO_NOM, A.ID_RECURSO, CU.UNIDADADM, A.CLV_RETENC ASC", new Object[]{row.get("TIPO_NOMINA"), row.get("ID_RECURSO"), row.get("CLV_UNIADM")});
	                			//Entramos por primera vez
	                			if(tipoNomina.equals("")&&idRecurso==0&&clv_uniadm.equals("")){
	                				tipoNomina = row.get("TIPO_NOMINA").toString();
	                				clv_uniadm = row.get("CLV_UNIADM").toString();
	                				idRecurso = (Integer) row.get("ID_RECURSO");
	                			}
	                			
	                			//Entrar para escribir las facturas
	                			if(tipoNomina.equals(row.get("TIPO_NOMINA").toString())&&clv_uniadm.equals(row.get("CLV_UNIADM").toString())&&idRecurso==((Integer) row.get("ID_RECURSO"))){
	                				
	                				getJdbcTemplate().update("INSERT INTO SAM_FACTURAS(NUM_FACTURA, ID_TIPO, ID_ENTRADA, CLV_BENEFI, CVE_PERS, ID_DEPENDENCIA, NOTAS, EJERCICIO, PERIODO, FECHA, FECHA_DOCUMENTO, FECHA_CIERRE, SUBTOTAL, IVA, TOTAL, STATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
	                						"NOM "+row.get("MES").toString()+" "+row.get("TIPO_NOMINA").toString()+row.get("ID_RECURSO").toString()+row.get("CLV_UNIADM").toString()+row.get("ID_PROYECTO").toString()+row.get("CLV_PARTID").toString()+row.get("N_PROGRAMA").toString(),
	                						1,
	                						0,
	                						"8000",
	                						/*row.get("ID_PROYECTO"),
	                						row.get("CLV_PARTID"),*/
	                						cve_pers,
	                						idDependencia,
	                						row.get("NOTA"),
	                						ejercicio,
	                						row.get("MES"),
	                						new Date(),
	                						new Date(),
	                						null,
	                						0,
	                						0,
	                						0,
	                						0 /*Validar aqui el finiquitado si tiene suficiencia*/
	                				});
	                				
	                				//Recupera la factura 
	                				cve_factura = getJdbcTemplate().queryForLong("SELECT MAX(CVE_FACTURA) FROM SAM_FACTURAS");
	                				
	                				//Guardar las movimientos en la tabla SAM_FACTURA_DETALLE
	                				getJdbcTemplate().update("INSERT INTO SAM_FACTURA_DETALLE(CVE_FACTURA, ID_PROYECTO, CLV_PARTID, IMPORTE) VALUES(?,?,?,?)", new Object[]{cve_factura, row.get("ID_PROYECTO"), row.get("CLV_PARTID"), row.get("IMPORTE")});
	                				
	                				//Guardar el Importe total
	                				getJdbcTemplate().update("UPDATE SAM_FACTURAS SET FECHA_CIERRE = ?, STATUS = ?, TOTAL = ? WHERE CVE_FACTURA =?", new Object[]{(disponible ? new Date():null), (disponible ? 1:0), row.get("IMPORTE"), cve_factura});
	                				
	                				//Guardar las retenciones de la factura
	                				idRecursoAnt = Integer.parseInt(row.get("ID_RECURSO").toString());
	                				periodoAnt = Integer.parseInt(row.get("MES").toString());
	                				fecha_nomina = row.get("FECHA_NOMINA").toString();
	                				notaAnt = row.get("NOTA").toString();
	                				retencAnt = 0.00;
	                				int c = 0;
	                				
	                				for(Map rw:deductivas){
	                					c++;
	                					retencAnt += Double.parseDouble(rw.get("IMPORTE").toString());
	                					getJdbcTemplate().update("INSERT INTO SAM_FACTURA_MOV_RETENC(CVE_FACTURA, CONS, CLV_RETENC, IMPORTE) VALUES (?,?,?,?)", new Object[]{
	                							cve_factura,
	                							c,
	                							rw.get("CLV_RETENC"),
	                							rw.get("IMPORTE")
	                					});
	                				}
	                				
	                				//Recuperamos la clave de factura para usar en la OP
	                				contenedor.add(cve_factura);
	                				//datos.addAll(row);
	                				
	                				if(total_req==valor){
	                							/**************************************************************************************************/
			                					//Entrar a escribir la ultima Orden de Pago
			                					String temp = contenedor.toString();
				                				temp = temp.replace("[", "");
				                				temp = temp.replace("]", "");
				                				Double importe = (Double) getJdbcTemplate().queryForObject("SELECT SUM(TOTAL) FROM SAM_FACTURAS WHERE CVE_FACTURA IN ("+temp.toString()+") ", Double.class);
				                				Double importeRetenc = (Double) getJdbcTemplate().queryForObject("SELECT SUM(IMPORTE) FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA IN ("+cve_factura+") ", Double.class);
				                				getJdbcTemplate().update("INSERT INTO SAM_ORD_PAGO (ID_DEPENDENCIA, ID_RECURSO, ID_GRUPO, EJERCICIO, PERIODO, FECHA, FECHA_CIERRE, TIPO, CLV_BENEFI, CVE_PERS, REEMBOLSOF, CONCURSO, IMPORTE, RETENCION, IMP_NETO, NOTA, STATUS, IVA, IMPORTE_IVA) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
				                						idDependencia,
				                						idRecursoAnt,
				                						idGrupoFirma,
				                						ejercicio,
				                						periodoAnt,
				                						fecha_nomina,
				                						(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE STATUS=0 AND CVE_FACTURA IN ("+temp.toString()+")") ==0 ? new Date(): null),
				                						9,
				                						"8000",
				                						cve_pers,
				                						"N",
				                						"",
				                						importe,
				                						retencAnt,
				                						(importe-retencAnt),
				                						notaAnt,
				                						(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE STATUS=0 AND CVE_FACTURA IN ("+temp.toString()+")") ==0 ? 0: -1) /*Estatus de la OP*/,
				                						0,
				                						0
				                				});
				                				
				                				Long cve_op = getJdbcTemplate().queryForLong("SELECT MAX(CVE_OP) FROM SAM_ORD_PAGO");
				                				Long cve_facturaTemp =0L;
					                			
					                			//Guarda los movimientos de la orden de Pago
					                			List<Map> facturas = getJdbcTemplate().queryForList("SELECT F.*, FAC.NUM_FACTURA FROM SAM_FACTURA_DETALLE AS F INNER JOIN SAM_FACTURAS AS FAC ON (FAC.CVE_FACTURA = F.CVE_FACTURA) WHERE F.CVE_FACTURA IN ("+temp+") ORDER BY F.CVE_FACTURA ASC");
					                			for(Map rw : facturas)
					                			{
					                				getJdbcTemplate().update("INSERT INTO SAM_MOV_OP(CVE_OP, CVE_FACTURA, ID_PROYECTO, CLV_PARTID, NOTA, TIPO, MONTO) VALUES(?,?,?,?,?,?,?)", new Object[]{
					                						cve_op,
					                						rw.get("CVE_FACTURA"),
					                						rw.get("ID_PROYECTO"),
					                						rw.get("CLV_PARTID"),
					                						"Soporta la Factura: "+rw.get("NUM_FACTURA").toString(),
					                						"LIBRE",
					                						rw.get("IMPORTE")
					                				});
					                				
					                				cve_facturaTemp = Long.parseLong(rw.get("CVE_FACTURA").toString());
					                				
					                				//Actualizamos la OP en facturas
					                				getJdbcTemplate().update("UPDATE SAM_FACTURAS SET CVE_OP = ? WHERE CVE_FACTURA =?", new Object[]{cve_op, rw.get("CVE_FACTURA")});
					                			}
					                			
					                			//Guardamos las deductivas de la OP
					                			int i =0;
					                			List<Map> retenciones = getJdbcTemplate().queryForList("SELECT *FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA =?", new Object[]{cve_facturaTemp});
					                			for(Map retenc : retenciones)
					                			{
					                				i++;
					                				getJdbcTemplate().update("INSERT INTO MOV_RETENC(CVE_OP, RET_CONS, CLV_RETENC, IMPORTE, PAGADO) VALUES(?,?,?,?,?)", new Object[]{
					                						cve_op,
					                						i,
					                						retenc.get("CLV_RETENC"),
					                						retenc.get("IMPORTE"),
					                						0
					                				});
					                			}
					                					
					                			/**********************************************************************/
					                			
	                				}

	                			}
	                			else //Entrar a escribir la Orden de Pago
	                			{
	                				String temp = contenedor.toString();
	                				temp = temp.replace("[", "");
	                				temp = temp.replace("]", "");
	                				Double importe = (Double) getJdbcTemplate().queryForObject("SELECT SUM(TOTAL) FROM SAM_FACTURAS WHERE CVE_FACTURA IN ("+temp.toString()+") ", Double.class);
	                				Double importeRetenc = (Double) getJdbcTemplate().queryForObject("SELECT SUM(IMPORTE) FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA IN ("+cve_factura+") ", Double.class);
	                				getJdbcTemplate().update("INSERT INTO SAM_ORD_PAGO (ID_DEPENDENCIA, ID_RECURSO, ID_GRUPO, EJERCICIO, PERIODO, FECHA, FECHA_CIERRE, TIPO, CLV_BENEFI, CVE_PERS, REEMBOLSOF, CONCURSO, IMPORTE, RETENCION, IMP_NETO, NOTA, STATUS, IVA, IMPORTE_IVA) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
	                						idDependencia,
	                						idRecursoAnt,
	                						idGrupoFirma,
	                						ejercicio,
	                						periodoAnt,
	                						fecha_nomina,
	                						(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE STATUS=0 AND CVE_FACTURA IN ("+temp.toString()+")") ==0 ? new Date(): null),
	                						9,
	                						"8000",
	                						cve_pers,
	                						"N",
	                						"",
	                						importe,
	                						retencAnt,
	                						(importe-retencAnt),
	                						notaAnt,
	                						(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE STATUS=0 AND CVE_FACTURA IN ("+temp.toString()+")") ==0 ? 0: -1) /*Estatus de la OP*/,
	                						0,
	                						0
	                				});
	                				
	                				Long cve_op = getJdbcTemplate().queryForLong("SELECT MAX(CVE_OP) FROM SAM_ORD_PAGO");
	                				Long cve_facturaTemp =0L;
	                				
		                			//Guarda los movimientos de la orden de Pago
	                				List<Map> facturas = getJdbcTemplate().queryForList("SELECT F.*, FAC.NUM_FACTURA FROM SAM_FACTURA_DETALLE AS F INNER JOIN SAM_FACTURAS AS FAC ON (FAC.CVE_FACTURA = F.CVE_FACTURA) WHERE F.CVE_FACTURA IN ("+temp+") ORDER BY F.CVE_FACTURA ASC");
		                			for(Map rw : facturas)
		                			{
		                				getJdbcTemplate().update("INSERT INTO SAM_MOV_OP(CVE_OP, CVE_FACTURA, ID_PROYECTO, CLV_PARTID, NOTA, TIPO, MONTO) VALUES(?,?,?,?,?,?,?)", new Object[]{
		                						cve_op,
		                						rw.get("CVE_FACTURA"),
		                						rw.get("ID_PROYECTO"),
		                						rw.get("CLV_PARTID"),
		                						"Soporta la Factura: "+rw.get("NUM_FACTURA").toString(),
		                						"LIBRE",
		                						rw.get("IMPORTE")
		                				});
		                				
		                				cve_facturaTemp = Long.parseLong(rw.get("CVE_FACTURA").toString());
		                				
		                				//Actualizamos la OP en facturas
		                				getJdbcTemplate().update("UPDATE SAM_FACTURAS SET CVE_OP = ? WHERE CVE_FACTURA =?", new Object[]{cve_op, rw.get("CVE_FACTURA")});
		                				
		                			}
		                			
		                			//Guardamos las deductivas de la OP
		                			int i =0;
		                			List<Map> retenciones = getJdbcTemplate().queryForList("SELECT *FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA =?", new Object[]{cve_facturaTemp});
		                			for(Map retenc : retenciones)
		                			{
		                				i++;
		                				getJdbcTemplate().update("INSERT INTO MOV_RETENC(CVE_OP, RET_CONS, CLV_RETENC, IMPORTE, PAGADO) VALUES(?,?,?,?,?)", new Object[]{
		                						cve_op,
		                						i,
		                						retenc.get("CLV_RETENC"),
		                						retenc.get("IMPORTE"),
		                						0
		                				});
		                			}
	                				
	                				contenedor = new Vector();
	                				idRecursoAnt = 0;
	                				periodoAnt = 0;
	                				fecha_nomina = "";
	                				notaAnt = "";
	                				retencAnt = 0.00;
	                				
	                				/************/
	                				
	                					deductivas = getJdbcTemplate().queryForList("SELECT  A.TIPO_NOM, A.ID_RECURSO, A.RECINTO, CU.UNIDADADM, A.CLV_RETENC, SUM (A.TOTAL) AS IMPORTE FROM SAM_NOMINA_DEDUCCIONES AS A "+
											"INNER JOIN CAT_UNIADM AS CU ON (CU.CLV_UNIADM = A.RECINTO) " +
										"WHERE A.TIPO_NOM =? AND A.ID_RECURSO =? AND A.RECINTO =? "+
										"GROUP BY A.TIPO_NOM, A.ID_RECURSO, A.RECINTO, CU.UNIDADADM, A.CLV_RETENC "+
										"ORDER BY A.TIPO_NOM, A.ID_RECURSO, CU.UNIDADADM, A.CLV_RETENC ASC", new Object[]{row.get("TIPO_NOMINA"), row.get("ID_RECURSO"), row.get("CLV_UNIADM")});

	                				
		                				getJdbcTemplate().update("INSERT INTO SAM_FACTURAS(NUM_FACTURA, ID_TIPO, ID_ENTRADA, CLV_BENEFI, CVE_PERS, ID_DEPENDENCIA, NOTAS, EJERCICIO, PERIODO, FECHA, FECHA_DOCUMENTO, FECHA_CIERRE, SUBTOTAL, IVA, TOTAL, STATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
		                						"NOM "+row.get("MES").toString()+" "+row.get("TIPO_NOMINA").toString()+row.get("ID_RECURSO").toString()+row.get("CLV_UNIADM").toString()+row.get("N_PROGRAMA").toString(),
		                						1,
		                						0,
		                						"8000",
		                						/*row.get("ID_PROYECTO"),
		                						row.get("CLV_PARTID"),
		                						*/
		                						cve_pers,
		                						idDependencia,
		                						row.get("NOTA"),
		                						ejercicio,
		                						row.get("MES"),
		                						new Date(),
		                						new Date(),
		                						null,
		                						0,
		                						0,
		                						0,
		                						0 /*Validar aqui el finiquitado si tiene suficiencia*/
		                				});
		                				
		                				//Recupera la factura 
		                				cve_factura = getJdbcTemplate().queryForLong("SELECT MAX(CVE_FACTURA) FROM SAM_FACTURAS");
		                				
		                				//Guardar las movimientos en la tabla SAM_FACTURA_DETALLE
		                				getJdbcTemplate().update("INSERT INTO SAM_FACTURA_DETALLE(CVE_FACTURA, ID_PROYECTO, CLV_PARTID, IMPORTE) VALUES(?,?,?,?)", new Object[]{cve_factura, row.get("ID_PROYECTO"), row.get("CLV_PARTID"), row.get("IMPORTE")});
		                				
		                				//Guardar el Importe total
		                				getJdbcTemplate().update("UPDATE SAM_FACTURAS SET FECHA_CIERRE = ?, STATUS = ?, TOTAL = ? WHERE CVE_FACTURA =?", new Object[]{(disponible ? new Date():null), (disponible ? 1:0), row.get("IMPORTE"), cve_factura});
		                				
		                				//Guardar las retenciones de la factura
		                				idRecursoAnt = Integer.parseInt(row.get("ID_RECURSO").toString());
		                				periodoAnt = Integer.parseInt(row.get("MES").toString());
		                				fecha_nomina = row.get("FECHA_NOMINA").toString();
		                				notaAnt = row.get("NOTA").toString();
		                				retencAnt = 0.00;
		                				int c = 0;
		                				cve_factura = getJdbcTemplate().queryForLong("SELECT MAX(CVE_FACTURA) FROM SAM_FACTURAS");
		                				
		                				for(Map rw:deductivas){
		                					c++;
		                					retencAnt += Double.parseDouble(rw.get("IMPORTE").toString());
		                					getJdbcTemplate().update("INSERT INTO SAM_FACTURA_MOV_RETENC(CVE_FACTURA, CONS, CLV_RETENC, IMPORTE) VALUES (?,?,?,?)", new Object[]{
		                							cve_factura,
		                							c,
		                							rw.get("CLV_RETENC"),
		                							rw.get("IMPORTE")
		                					});
		                				}
		                				
		                				//Recuperamos la clave de factura para usar en la OP
		                				contenedor.add(cve_factura);
		                			
		                				/************/
		                				if(total_req==valor){
                							/**************************************************************************************************/
		                					//Entrar a escribir la ultima Orden de Pago
		                					 temp = contenedor.toString();
			                				temp = temp.replace("[", "");
			                				temp = temp.replace("]", "");
			                				 importe = (Double) getJdbcTemplate().queryForObject("SELECT SUM(TOTAL) FROM SAM_FACTURAS WHERE CVE_FACTURA IN ("+temp.toString()+") ", Double.class);
			                				 importeRetenc = (Double) getJdbcTemplate().queryForObject("SELECT SUM(IMPORTE) FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA IN ("+cve_factura+") ", Double.class);
			                				getJdbcTemplate().update("INSERT INTO SAM_ORD_PAGO (ID_DEPENDENCIA, ID_RECURSO, ID_GRUPO, EJERCICIO, PERIODO, FECHA, FECHA_CIERRE, TIPO, CLV_BENEFI, CVE_PERS, REEMBOLSOF, CONCURSO, IMPORTE, RETENCION, IMP_NETO, NOTA, STATUS, IVA, IMPORTE_IVA) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
			                						idDependencia,
			                						idRecursoAnt,
			                						idGrupoFirma,
			                						ejercicio,
			                						periodoAnt,
			                						fecha_nomina,
			                						(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE STATUS=0 AND CVE_FACTURA IN ("+temp.toString()+")") ==0 ? new Date(): null),
			                						9,
			                						"8000",
			                						cve_pers,
			                						"N",
			                						"",
			                						importe,
			                						retencAnt,
			                						(importe-retencAnt),
			                						notaAnt,
			                						(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE STATUS=0 AND CVE_FACTURA IN ("+temp.toString()+")") ==0 ? 0: -1) /*Estatus de la OP*/,
			                						0,
			                						0
			                				});
			                				
			                				 cve_op = getJdbcTemplate().queryForLong("SELECT MAX(CVE_OP) FROM SAM_ORD_PAGO");
				                			
				                			//Guarda los movimientos de la orden de Pago
			                				facturas = getJdbcTemplate().queryForList("SELECT F.*, FAC.NUM_FACTURA FROM SAM_FACTURA_DETALLE AS F INNER JOIN SAM_FACTURAS AS FAC ON (FAC.CVE_FACTURA = F.CVE_FACTURA) WHERE F.CVE_FACTURA IN ("+temp+") ORDER BY F.CVE_FACTURA ASC");
				                			for(Map rw : facturas)
				                			{
				                				getJdbcTemplate().update("INSERT INTO SAM_MOV_OP(CVE_OP, CVE_FACTURA,  ID_PROYECTO, CLV_PARTID, NOTA, TIPO, MONTO) VALUES(?,?,?,?,?,?,?)", new Object[]{
				                						cve_op,
				                						rw.get("CVE_FACTURA"),
				                						rw.get("ID_PROYECTO"),
				                						rw.get("CLV_PARTID"),
				                						"Soporta la Factura: "+rw.get("NUM_FACTURA").toString(),
				                						"LIBRE",
				                						rw.get("IMPORTE")
				                				});
				                				
				                				cve_facturaTemp = Long.parseLong(rw.get("CVE_FACTURA").toString());
				                				
				                				//Actualizamos la OP en facturas
				                				getJdbcTemplate().update("UPDATE SAM_FACTURAS SET CVE_OP = ? WHERE CVE_FACTURA =?", new Object[]{cve_op, rw.get("CVE_FACTURA")});
				                			}
				                			
				                			//Guardamos las deductivas de la OP
				                			i =0;
				                			retenciones = getJdbcTemplate().queryForList("SELECT *FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA =?", new Object[]{cve_facturaTemp});
				                			for(Map retenc : retenciones)
				                			{
				                				i++;
				                				getJdbcTemplate().update("INSERT INTO MOV_RETENC(CVE_OP, RET_CONS, CLV_RETENC, IMPORTE, PAGADO) VALUES(?,?,?,?,?)", new Object[]{
				                						cve_op,
				                						i,
				                						retenc.get("CLV_RETENC"),
				                						retenc.get("IMPORTE"),
				                						0
				                				});
				                			}
				                			
		                				}
				                	   /**********************************************************************/	
	                			}

	                			//Devolvemos los valores a las variables de control
	                			tipoNomina = row.get("TIPO_NOMINA").toString();
                				clv_uniadm = row.get("CLV_UNIADM").toString();
                				idRecurso = (Integer) row.get("ID_RECURSO");
                				cve_factura = 0l;
	                		}
		                } 
	          });
			  presupuesto = null;
	                
		} catch (DataAccessException e) {                     
	      throw new RuntimeException(e.getMessage(),e);
	    }	   
	}
	
	public void guardarComprobacionVale(Long idMovVale, Long cve_factura, Long cve_vale, int idProyecto, String clv_partid, Double importe)
	{
		try
		{
			Date fecha = new Date();
			
			Double disponible = (Double) this.getJdbcTemplate().queryForObject("SELECT TOP 1 (dbo.getDisponibleDocumento('VAL', CVE_VALE, ID_PROYECTO, CLV_PARTID)) AS DISPONIBLE FROM SAM_MOV_VALES WHERE CVE_VALE =? AND ID_PROYECTO=? AND CLV_PARTID=?", new Object[]{cve_vale, idProyecto, clv_partid}, Double.class );
			
			if (importe > disponible)
				throw new RuntimeException("El importe de la comprobación no debe ser mayor al disponible del Vale actual");
				
			if (idMovVale ==0)
				this.getJdbcTemplate().update("INSERT INTO SAM_FACTURAS_VALES(CVE_FACTURA, CVE_VALE, ID_PROYECTO, CLV_PARTID, IMPORTE, FECHA) VALUES(?,?,?,?,?,?)", new Object[]{
					cve_factura,
					cve_vale,
					idProyecto,
					clv_partid,
					importe,
					fecha
				});
			else 
				this.getJdbcTemplate().update("UPDATE SAM_FACTURAS_VALES SET CVE_VALE = ?, ID_PROYECTO=?, CLV_PARTID=?, IMPORTE=? WHERE ID_MOVIMIENTO =?", new Object[]{
						cve_vale,
						idProyecto,
						clv_partid,
						importe,
						idMovVale
				});
		}
		catch (DataAccessException e) {                     
		      throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public List getListaVales (Long cve_factura) {
   	 	return this.getJdbcTemplate().queryForList("SELECT A.*, B.N_PROGRAMA, V.NUM_VALE FROM SAM_FACTURAS_VALES AS A INNER JOIN VPROYECTO AS B ON (B.ID_PROYECTO = A.ID_PROYECTO) INNER JOIN SAM_VALES_EX AS V ON (V.CVE_VALE=A.CVE_VALE) WHERE A.CVE_FACTURA =?", new Object[]{cve_factura});
	}

	public List<Map> getValesDisponibles (int idDependencia, int  idProyecto, String clv_partid ) {
   	 String sql = "SELECT V.CVE_VALE, V.NUM_VALE + ' > '+ CONVERT(VARCHAR, dbo.getDisponibleDocumento('VAL', V.CVE_VALE, M.ID_PROYECTO, M.CLV_PARTID), 1) AS DATOVALE " +
					"FROM SAM_MOV_VALES AS M  "+
					"	INNER JOIN SAM_VALES_EX AS V ON (V.CVE_VALE = M.CVE_VALE)   "+
					"WHERE  "+
					" V.ID_DEPENDENCIA =? AND M.ID_PROYECTO = ? AND M.CLV_PARTID = ? AND V.STATUS IN (4) "+
					"GROUP BY V.CVE_VALE, V.NUM_VALE, M.ID_PROYECTO, M.CLV_PARTID /*HAVING dbo.getDisponibleDocumento('VAL', V.CVE_VALE, M.ID_PROYECTO, M.CLV_PARTID)>0*/";
						
   	 return getJdbcTemplate().queryForList(sql,new Object []{idDependencia, idProyecto, clv_partid});
    }
	
	public void  eliminarVales( final List<Integer> lstVales, final int cve_pers) {
		  try { 

	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Integer idVale :lstVales) {	                		
	                		getJdbcTemplate().update("DELETE FROM SAM_FACTURAS_VALES WHERE ID_MOVIMIENTO =?", new Object[]{idVale});
	                	}
	                } 
	            });
	          } catch (DataAccessException e) {                        
	                throw new RuntimeException(e.getMessage(),e);
	         }	                	                		  	  
	  }
	
	public List<Map> getDetallesProyectoPartidaDocumento(Long cve_factura, int ejercicio)
	{
		Map factura = this.getFactura(cve_factura);
		int mesActivo = gatewayMeses.getMesActivo(ejercicio);
		
		if(factura.get("CVE_REQ")!=null)
			return getJdbcTemplate().queryForList("SELECT R.ID_PROYECTO, R.CLV_PARTID, P.N_PROGRAMA FROM SAM_REQUISIC AS R INNER JOIN VPROYECTO AS P ON (P.ID_PROYECTO = R.ID_PROYECTO) WHERE R.CVE_REQ =?", new Object[]{factura.get("CVE_REQ")});
		else if(factura.get("CVE_PED")!=null)  
			return getJdbcTemplate().queryForList("SELECT R.ID_PROYECTO, R.CLV_PARTID, V.N_PROGRAMA FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) INNER JOIN VPROYECTO AS V ON (V.ID_PROYECTO = R.ID_PROYECTO) WHERE P.CVE_PED =?", new Object[]{factura.get("CVE_PED")});
		else if(factura.get("CVE_CONTRATO")!=null) 
			return getJdbcTemplate().queryForList("SELECT * FROM VT_COMPROMISOS WHERE TIPO_DOC ='CON' AND CVE_DOC=? AND PERIODO =?", new Object[]{factura.get("CVE_CONTRATO"), mesActivo});
		else
			return null;
	}
	
	public List<Map> getMovimientosAjustadosFactura(Long cve_factura)
	{
		return this.getJdbcTemplate().queryForList("SELECT MOV.*, CONVERT(varchar(10), MOV.FECHA_MOVTO, 103) AS FECHA_MOVTO , VP.N_PROGRAMA FROM SAM_MOD_COMP AS MOV INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = MOV.ID_PROYECTO) WHERE MOV.TIPO_DOC = ? AND MOV.DOCUMENTO =? ORDER BY MOV.FECHA_MOVTO DESC",new Object[]{8, cve_factura});
	}
	
	public void guardarAjusteFacturaPeredo(Long id_sam_mod_comp, Long cve_factura, int idProyecto, String clv_partid, String fecha, Double importe)
	{
		if(id_sam_mod_comp==0)
			this.getJdbcTemplate().update("INSERT INTO SAM_MOD_COMP(TIPO_DOC, DOCUMENTO, ID_PROYECTO, CLV_PARTID, IMPORTE, FECHA_MOVTO) VALUES(?,?,?,?,?,?)", new Object[]{8, cve_factura, idProyecto, clv_partid, importe, this.formatoFecha(fecha)});
		else
			this.getJdbcTemplate().update("UPDATE SAM_MOD_COMP SET ID_PROYECTO=?, CLV_PARTID=?, IMPORTE=?, FECHA_MOVTO=? WHERE id_sam_mod_comp = ?", new Object[]{idProyecto, clv_partid, importe, this.formatoFecha(fecha), id_sam_mod_comp});
	}
	
	public void eliminarConceptoAjusteFactura(final Long id_sam_mod_comp)
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