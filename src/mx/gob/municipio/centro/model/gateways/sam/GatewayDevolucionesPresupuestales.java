package mx.gob.municipio.centro.model.gateways.sam;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;

public class GatewayDevolucionesPresupuestales extends BaseGateway {
	
	@Autowired
	GatewayMeses gatewayMeses;
	
	String mensaje;
	boolean exito;
	
	public GatewayDevolucionesPresupuestales(){
		
	}
	
	public List<Map> getListaDevolucionesPresupuestales(String concepto, String numero, String status,int idUnidad){
		String clausula = "";
		
		if(concepto!=null) 
			if(!concepto.equals("")) clausula = " AND (DP.CONCEPTO LIKE '%"+concepto+"%' OR DP.DESCRIPCION LIKE '%"+concepto+"%')";
		
		if(numero!=null)
			if(!numero.equals("")) clausula += " AND (DP.NUM_DEVOLUCION LIKE '%"+numero+"%') ";
		if(idUnidad!=0)
			clausula +=" AND DP.ID_DEPENDENCIA IN("+idUnidad+")";
		
		return this.getJdbcTemplate().queryForList("SELECT "+
															"DP.*, D.DEPENDENCIA, CONVERT(VARCHAR(10), DP.FECHA, 103) AS FECHA_DESC, "+
															"ED.DESCRIPCION AS STATUS_DESC, (SELECT ISNULL(SUM(/*CASE DPD.TIPO WHEN 0 THEN */DPD.IMPORTE/* ELSE DPD.IMPORTE*-1 END*/),0) FROM SAM_DEVOLUCION_PRESUPUESTAL_DETALLES AS DPD WHERE DPD.ID_DEVOLUCION = DP.ID_DEVOLUCION AND (DPD.CLV_RETENC IS NULL OR DPD.CLV_RETENC=0)) AS IMPORTE "+
														"FROM SAM_DEVOLUCION_PRESUPUESTAL AS DP "+
														"INNER JOIN CAT_DEPENDENCIAS AS D ON (D.ID = DP.ID_DEPENDENCIA) " +
														"INNER JOIN SAM_ESTATUS_DEVOLUCION_PRESUPUESTAL AS ED ON (ED.ID_STATUS = DP.STATUS) WHERE DP.STATUS IN("+status+")  "+clausula);
	}
	
	public Long guardarDevolucionPresupuestal(Long idDevolucion, int idUnidad, int periodo, int idRecurso, String fecha, String concepto, String descripcion, int cve_pers, int ejercicio, int idGrupo){
		if(idDevolucion==null) idDevolucion = 0l;
			if(idDevolucion==0)
			{
				this.getJdbcTemplate().update("INSERT INTO SAM_DEVOLUCION_PRESUPUESTAL(ID_DEPENDENCIA, PERIODO, ID_RECURSO, FECHA, CONCEPTO, DESCRIPCION, CVE_PERS, EJERCICIO, ID_GRUPO, STATUS) VALUES(?,?,?,?,?,?,?,?,?,?)", new Object[]{idUnidad, periodo, idRecurso, fecha, concepto, descripcion, cve_pers, ejercicio, idGrupo, "0"});
				idDevolucion = this.getJdbcTemplate().queryForLong("SELECT MAX(ID_DEVOLUCION) AS N FROM SAM_DEVOLUCION_PRESUPUESTAL");
			}
			else
			{
				this.getJdbcTemplate().update("UPDATE SAM_DEVOLUCION_PRESUPUESTAL SET ID_DEPENDENCIA =?, PERIODO=?, ID_RECURSO=?,  FECHA=?, CONCEPTO=?, DESCRIPCION=? WHERE ID_DEVOLUCION = ?", new Object[]{idUnidad, periodo, idRecurso, fecha, concepto, descripcion, idDevolucion});
			}
		return idDevolucion;
	}
	
	public Map getDevolucionPresupuestal(Long idDevolucion){
		return this.getJdbcTemplate().queryForMap("SELECT "+
				"DP.*, CR.RECURSO, (D.CLV_UNIADM + ' '+D.DEPENDENCIA) AS UNIDAD_SOLICITANTE, D.DEPENDENCIA, CONVERT(VARCHAR(10), DP.FECHA, 103) AS FECHA_DESC, "+
				"ED.DESCRIPCION AS STATUS_DESC, "+
				"(SELECT SUM(IMPORTE)"+
				"FROM SAM_DEVOLUCION_PRESUPUESTAL_DETALLES "+ 
				"WHERE ID_DEVOLUCION = DP.ID_DEVOLUCION) AS TOTAL "+ 
			"FROM SAM_DEVOLUCION_PRESUPUESTAL AS DP "+
			"INNER JOIN CAT_RECURSO AS CR ON (CR.ID = DP.ID_RECURSO) "+
			"INNER JOIN CAT_DEPENDENCIAS AS D ON (D.ID = DP.ID_DEPENDENCIA) " +
			"INNER JOIN SAM_ESTATUS_DEVOLUCION_PRESUPUESTAL AS ED ON (ED.ID_STATUS = DP.STATUS) WHERE DP.ID_DEVOLUCION = ?", new Object[]{idDevolucion});
	}
	
	public String agregarConcepto(Long idDetalle, Long idDevolucion, Long idproyecto, String clv_partid, Double importe, String nota, String tipo){
		int periodo = this.getJdbcTemplate().queryForInt("SELECT PERIODO FROM SAM_DEVOLUCION_PRESUPUESTAL WHERE ID_DEVOLUCION = ?", new Object[]{idDevolucion});
		Double ejercido = (Double) this.getJdbcTemplate().queryForObject("SELECT dbo.getEjercido("+periodo+", "+idproyecto+", '"+clv_partid+"')", Double.class);
		if(idDetalle==null) idDetalle = 0l;
		if(importe>ejercido) 
			return "No es posible devolver más presupuesto del que se tiene ejercido en el programa y partida especificos";
		else{
			if(idDetalle==0){
				this.getJdbcTemplate().update("INSERT INTO SAM_DEVOLUCION_PRESUPUESTAL_DETALLES(ID_DEVOLUCION, ID_PROYECTO, CLV_PARTID, DESCRIPCION, IMPORTE, CLV_RETENC) VALUES(?,?,?,?,?, ?)", new Object[]{idDevolucion, idproyecto, clv_partid, nota, importe, tipo });
			}
			else
			{
				this.getJdbcTemplate().update("UPDATE SAM_DEVOLUCION_PRESUPUESTAL_DETALLES SET ID_PROYECTO = ?, CLV_PARTID = ?, DESCRIPCION=?, IMPORTE=?, CLV_RETENC=? WHERE ID_DETALLE_DEVOLUCION =?", new Object[]{idproyecto, clv_partid, nota, importe, tipo, idDetalle });
			}
			return "";
		}
	}
	
	public List<Map> getDetallesDevolucion(Long idDevolucion){
		return this.getJdbcTemplate().queryForList("SELECT DV.*, ISNULL(OP.NUM_OP, '') AS NUM_OP, ISNULL(CAT_RETENC.RETENCION,'') AS RETENCION, CT.N_PROGRAMA, CT.ID_DEPENDENCIA, D.DEPENDENCIA FROM SAM_DEVOLUCION_PRESUPUESTAL_DETALLES AS DV "+
															"INNER JOIN CEDULA_TEC AS CT ON (CT.ID_PROYECTO = DV.ID_PROYECTO) "+
															"LEFT JOIN CAT_RETENC ON (CAT_RETENC.CLV_RETENC = DV.CLV_RETENC) " +
															"LEFT JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = DV.CVE_OP) "+
															"INNER JOIN CAT_DEPENDENCIAS AS D ON (D.ID = CT.ID_DEPENDENCIA) WHERE DV.ID_DEVOLUCION = ?", new Object[]{idDevolucion});
	}
	
	public List<Map> getDetallesDevolucionReporte(Long idDevolucion){
		return this.getJdbcTemplate().queryForList("SELECT DV.ID_DETALLE_DEVOLUCION, DV.ID_DEVOLUCION, DV.ID_PROYECTO, DV.CLV_PARTID, DV.TIPO, DV.DESCRIPCION, (CASE DV.TIPO WHEN 0 THEN DV.IMPORTE ELSE (DV.IMPORTE*-1) END) AS IMPORTE,  CT.N_PROGRAMA, CT.ID_DEPENDENCIA, D.DEPENDENCIA FROM SAM_DEVOLUCION_PRESUPUESTAL_DETALLES AS DV "+
															"INNER JOIN CEDULA_TEC AS CT ON (CT.ID_PROYECTO = DV.ID_PROYECTO) "+
															"INNER JOIN CAT_DEPENDENCIAS AS D ON (D.ID = CT.ID_DEPENDENCIA) WHERE DV.ID_DEVOLUCION = ?", new Object[]{idDevolucion});
	}
	
	public void eliminarDetallesDevolucion(final Long[] idDetalles, Long idDevolucion){
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	for(Long id: idDetalles){
            		getJdbcTemplate().update("DELETE FROM SAM_DEVOLUCION_PRESUPUESTAL_DETALLES WHERE ID_DETALLE_DEVOLUCION =?", new Object[]{id});
            	}
            } 
        });
		
	}
	
	public String cerrarDevolucion(final Long idDevolucion, final int ejercicio){
		mensaje = "";
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	boolean cerrar = true;
            	Double ejercido = 0d;
            	Map dato = getJdbcTemplate().queryForMap("SELECT (SELECT ISNULL(SUM(DT.IMPORTE),0) FROM SAM_DEVOLUCION_PRESUPUESTAL_DETALLES AS DT WHERE DT.ID_DEVOLUCION = SAM_DEVOLUCION_PRESUPUESTAL.ID_DEVOLUCION) AS IMPORTE, PERIODO FROM SAM_DEVOLUCION_PRESUPUESTAL WHERE ID_DEVOLUCION = ?", new Object[]{idDevolucion});
            	int periodo = Integer.parseInt(dato.get("PERIODO").toString());
            	/*Verificar que el periodo sea el mismo en q se cierra el documento*/
            	/*if(periodo!= gatewayMeses.getMesActivo(ejercicio)){
            		throw new RuntimeException("El periodo de la devolucion no es valido, cierre con el periodo actual");
            	}*/
            	Double importe = 0d;
            	List <Map> dt = getDetallesDevolucion(idDevolucion);
            	for(Map row : dt){
            		importe = Double.parseDouble(row.get("IMPORTE").toString());
            		ejercido = (Double) getJdbcTemplate().queryForObject("SELECT ISNULL(dbo.getEjercido("+periodo+", "+row.get("ID_PROYECTO").toString()+", '"+row.get("CLV_PARTID").toString()+"'),0)", Double.class);
            		if(importe>ejercido) 
            			cerrar = false;
            	}
            	if(cerrar){
            		getJdbcTemplate().update("UPDATE SAM_DEVOLUCION_PRESUPUESTAL SET STATUS =1 WHERE ID_DEVOLUCION =?", new Object[]{idDevolucion});
            		//guardar en bitacora
            	}
            	else
            		mensaje = "No es posible cerrar la devolucion por que el importe a devolver excede al presupuesto ejercido en el periodo, programas y partidas,  verificar conceptos nuevamente";
            	
            } 
            
        });
		return mensaje;
	}
	
	public String cancelarDevolucion(Long idDevolucion){
		try{
			this.getJdbcTemplate().update("UPDATE SAM_DEVOLUCION_PRESUPUESTAL SET STATUS = 3 WHERE ID_DEVOLUCION =?", new Object[]{idDevolucion});
			//Guardar en la bitacora
			return "";
		}
		catch(Exception e){
			return e.getMessage();
		}
		
	}
	
	public String aplicarDevolucion(final Long[] idDevoluciones){
		mensaje = "";
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	for(Long id : idDevoluciones){
            		boolean cerrar = true;
            		Double ejercido = 0d;
                	Map dato = getJdbcTemplate().queryForMap("SELECT STATUS, (SELECT ISNULL(SUM((DT.IMPORTE)),0) FROM SAM_DEVOLUCION_PRESUPUESTAL_DETALLES AS DT WHERE DT.ID_DEVOLUCION = SAM_DEVOLUCION_PRESUPUESTAL.ID_DEVOLUCION) AS IMPORTE, PERIODO FROM SAM_DEVOLUCION_PRESUPUESTAL WHERE ID_DEVOLUCION = ?", new Object[]{id});
                	int periodo = Integer.parseInt(dato.get("PERIODO").toString());
                	Double importe = 0d;
                	
                	if(!dato.get("STATUS").toString().equals("1")) {
                		mensaje = "La devolucion presupuestal que intenta aplicar no es valida o no se encuentra cerrada, verifique y vuelva a intentar esta operación";
                		throw new RuntimeException(mensaje);
                	}
                	
                	List <Map> dt = getDetallesDevolucion(id);
                	for(Map row : dt){
                		importe = (row.get("CLV_RETENC")!=null) ? Double.parseDouble(row.get("IMPORTE").toString()): 0d;
                		ejercido = (Double) getJdbcTemplate().queryForObject("SELECT ISNULL(dbo.getEjercido("+periodo+", "+row.get("ID_PROYECTO").toString()+", '"+row.get("CLV_PARTID").toString()+"'),0)", Double.class);
                		if(importe>ejercido) 
                			cerrar = false;
                	}
                	if(cerrar){
                		getJdbcTemplate().update("UPDATE SAM_DEVOLUCION_PRESUPUESTAL SET STATUS =2 WHERE ID_DEVOLUCION =?", new Object[]{id});
                		//guardar en bitacora
                	}
                	else{
                		mensaje = "No es posible aplicar la devolucion por que se el importe a devolver excede al presupuesto ejercido en el periodo, programas y partida,  verificar conceptos de devolucion "+rellenarCeros(id.toString(), 6);
                		throw new RuntimeException(mensaje);
                	}
            	}
            }
		 });
		return mensaje;
	}
	
	public String aperturarDevolucion(final Long[] idDevoluciones){
		exito = false;
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	for(Long id : idDevoluciones){
            		Map dato = getJdbcTemplate().queryForMap("SELECT STATUS FROM SAM_DEVOLUCION_PRESUPUESTAL WHERE ID_DEVOLUCION = ?", new Object[]{id});
            		if(!dato.get("STATUS").toString().equals("1")) {
            			exito = false;
                		mensaje = "La devolucion presupuestal que intenta aperturar no es valida o no se encuentra cerrada, verifique y vuelva a intentar esta operación";
                		throw new RuntimeException(mensaje);
                	}
            		getJdbcTemplate().update("UPDATE SAM_DEVOLUCION_PRESUPUESTAL SET STATUS =0 WHERE ID_DEVOLUCION =?", new Object[]{id});
            		//guardar en bitacora
            	}
            	exito = true;
            }
		 });
		if(exito)
			return "";
		else
			return mensaje;
	}
	
	public String desaplicarDevolucion(final Long[] idDevoluciones){
		exito = false;
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	for(Long id : idDevoluciones){
            		Map dato = getJdbcTemplate().queryForMap("SELECT STATUS FROM SAM_DEVOLUCION_PRESUPUESTAL WHERE ID_DEVOLUCION = ?", new Object[]{id});
            		if(!dato.get("STATUS").toString().equals("2")) {
            			exito = false;
                		mensaje = "La devolucion presupuestal que intenta desaplicar no es valida o no se encuentra aplicada, verifique y vuelva a intentar esta operación";
                		throw new RuntimeException(mensaje);
                	}
            		getJdbcTemplate().update("UPDATE SAM_DEVOLUCION_PRESUPUESTAL SET STATUS =1 WHERE ID_DEVOLUCION =?", new Object[]{id});
            		//guardar en bitacora
            	}
            	exito = true;
            }
		 });
		if(exito)
			return "";
		else
			return mensaje;
	}
	
	public void cargarMovimientosOrdenPago(final Long[] idMovtos, final Long idDevolucion){
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	for(Long id : idMovtos){
            		Map movto = getJdbcTemplate().queryForMap("SELECT * FROM SAM_MOV_OP WHERE ID_MOV_OP = ?", new Object[]{id});
            		
            		getJdbcTemplate().update("INSERT INTO SAM_DEVOLUCION_PRESUPUESTAL_DETALLES(ID_DEVOLUCION, CVE_OP, ID_PROYECTO, CLV_PARTID, CLV_RETENC, DESCRIPCION, IMPORTE) VALUES(?,?,?,?,?,?,?)", new Object[]{idDevolucion, movto.get("CVE_OP"), movto.get("ID_PROYECTO"), movto.get("CLV_PARTID"), 0, movto.get("NOTA"), movto.get("MONTO")});
            		//guardar en bitacora
            	}
            	exito = true;
            }
		 });
	}
	
}
