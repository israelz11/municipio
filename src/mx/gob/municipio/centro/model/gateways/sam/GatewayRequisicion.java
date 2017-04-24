/**
 * @author Lsc. Mauricio Hernandez Leon, ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.controller.sam.pedidos.StoreProcedurePedidos;
import mx.gob.municipio.centro.view.controller.sam.requisiciones.StoreProcedureRequisiciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class GatewayRequisicion  extends BaseGateway {
	private static Logger log = Logger.getLogger(GatewayRequisicion.class.getName());
	
	public final static  int REQ_STATUS_NUEVO = 0;
	public final static int REQ_STATUS_PENDIENTE = 1;
	public final static int REQ_STATUS_EN_PROCESO = 2;
	public final static int REQ_STATUS_A_REVISAR = 3;
	public final static int REQ_STATUS_CANCELADA = 4;
	public final static int REQ_STATUS_FINIQUITADA = 5;
	
	public final static int REQ_TIPO_BIENES = 1;
	public final static int REQ_TIPO_SERVICIOS = 2;
	public final static int REQ_TIPO_SERVICIOS_VEHICULO = 3;
	public final static int REQ_TIPO_MAQUINARIA_PESADA = 4;
	public final static int REQ_TIPO_SERVICIO_BOMBAS = 5;
	public final static int REQ_TIPO_PAQUETES = 6;
	public final static int REQ_TIPO_CALENDARIZADA = 7;
	public final static int REQ_TIPO_OS_CALENDARIZADA = 8;
	
	public final static int REQ_MOVTO_EDICION = 0;
	public final static int REQ_MOVTO_SOLICITADO = 1;
	public final static int REQ_MOVTO_CORRECCIONES = 2;
	public final static int REQ_MOVTO_RECHAZADO = 3;
	public final static int REQ_MOVTO_PEDIDO = 4;
	public final static int REQ_MOVTO_SURTIDO = 5;
	public String error="";
	boolean exito;
	Long cveReq;
	public static String REQ_ERROR_TIENE_PEDIDO = "La Requisicion/O.T./O.S. que intenta eliminar ya esta relacionado a un Pedido";
	
	
	@Autowired
	public GatewayBitacora gatewayBitacora;
	@Autowired
	public GatewayProyectoPartidas gatewayProyectoPartidas;
	@Autowired
	public GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
	@Autowired
	public GatewayMeses gatewayMeses;
	/*Constructor*/
	public GatewayRequisicion() {
	
	}
	
	/*Metodo usado para determinar si se guarda una nueva requisicion o se actualiza una existente*/
	public  Long actualizarRequisicion(final Long id_proyecto, final Long cve_req,final Long cve_contrato, final Long cve_vale, final  String num_req,final  String cve_unidad,final  Date f,final  int tipo, final String notas,
			final int mes, final int statusReq, final int compromete, final int ejercicio , final int cve_pers,final  int anualizada,final  String proyecto,final  String partida,final  int id_grupo,final String cve_benefi,final   String area,final  String tipo_bien,final  String marca,final  String modelo,final  String placas,final  String num_inv,final  String color, 
			final String usuario, final int cve_concurso){
		 cveReq=cve_req;
		 this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
             @Override
         protected void   doInTransactionWithoutResult(TransactionStatus status) {            	  	  
			  int tipoAnualizada=anualizada;
	            	 if(tipo==7) tipoAnualizada = 1; //Requisicion anualizada
			  
			  if (cveReq == null){
				  cveReq= guardar(id_proyecto, num_req, cve_contrato, cve_vale, cve_unidad, f, tipo, notas, mes, statusReq,  ejercicio , cve_pers, tipoAnualizada, proyecto, partida, id_grupo);
				  if(tipo!=1 && tipo!=7)
				    insertarOT(cveReq, cve_benefi,  area, tipo_bien, marca, modelo, placas, num_inv, color, usuario, cve_concurso, tipo);
			  }
			  else{
				  /*volver a verificar si existe OT/OS*/
				  if(tipo!=1 && tipo!=7){
					  if(getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_ORDEN_TRAB WHERE CVE_REQ = ?", new Object[]{cveReq})==0)
					  insertarOT(cveReq, cve_benefi,  area, tipo_bien, marca, modelo, placas, num_inv, color, usuario, cve_concurso, tipo);
				  }
				
				actualizar(cveReq, num_req, cve_contrato, cve_vale, cve_unidad, f, tipo, notas, mes, statusReq,  ejercicio , cve_pers, id_proyecto, partida);
				if(tipo!=1 && tipo!=7)
					actualizarOT(cveReq, cve_benefi,  area, tipo_bien, marca, modelo, placas, num_inv, color, usuario, cve_concurso, tipo);
				//Israel: Elimina si es diferente de OT
				if(tipo==1 && tipo==7)
					getJdbcTemplate().update("DELETE FROM SAM_ORDEN_TRAB WHERE CVE_REQ = ?", new Object [] {cve_req});
			  	}
			 } 
        });    	
     
	  return  cveReq; 
	}
	
	public void actualizarOT(Long cve_req, String cve_benefi,  String area, String tipo_bien, String marca, String modelo, String placas, String num_inv, String color, String usuario, int cve_concurso, int tipoReq ){
		if(tipoReq==2||tipoReq==3||tipoReq==4||tipoReq==5||tipoReq==6||tipoReq==8)
			this.getJdbcTemplate().update("UPDATE SAM_ORDEN_TRAB SET CLV_BENEFI=?, TIPO=?, COSTO_TOTAL=(SELECT SUM(CANTIDAD*PRECIO_EST) AS TOTAL FROM SAM_REQ_MOVTOS WHERE CVE_REQ = ?), AREA=?, MARCA=?,  MODELO=?, PLACAS=?, VEHICULO=?, NUM_INV=?, COLOR=?, CVE_CONCURSO=?, USUARIO=? WHERE CVE_REQ=?", new Object [] {cve_benefi, tipoReq, cve_req,  area,  marca, modelo, placas, tipo_bien, num_inv, color, cve_concurso, usuario, cve_req });		
	}
	
	public void insertarOT(Long cve_req, String cve_benefi,  String area, String tipo_bien, String marca, String modelo, String placas, String num_inv, String color, String usuario, int cve_concurso, int tipoReq ){
		 this.getJdbcTemplate().update("INSERT INTO SAM_ORDEN_TRAB(CVE_REQ, CLV_BENEFI, TIPO,  AREA, VEHICULO, MARCA, MODELO, PLACAS, NUM_INV, COLOR, USUARIO, CVE_CONCURSO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ", new Object [] {cve_req, (cve_benefi =="" ? "NULL": cve_benefi), tipoReq,  area, tipo_bien, marca, modelo, placas, num_inv, color, usuario, cve_concurso});
	}		
	
	/*Metodo usado para generar una nueva requisicion*/
	public Long guardar(Long id_proyecto, String num_req, Long cve_contrato, Long cve_vale, String cve_unidad, Date fecha, int tipo, String notas, int mes, int status, int ejercicio , int cve_pers, int anualizada, String proyecto, String partida, int id_grupo){
		Long cve_req = 0L;
		Date fecha_cap = new Date();
		Date fecha_ingreso = new Date();
		//comprobar la disponibilidad del numero de requisicion
		boolean c = comprobarExistencia(num_req);
		if(!c){
			cve_req = getNumeroRequisicion(ejercicio)+1;
			String SQL = "INSERT INTO SAM_REQUISIC (CVE_REQ, NUM_REQ, CVE_CONTRATO, CVE_VALE, EJERCICIO, ID_PROYECTO, CLV_PARTID, ID_DEPENDENCIA, FECHA, TIPO, OBSERVA, FECHA_CAP, FECHA_INGRESO, CVE_PERS, STATUS, COMPROMETE, PERIODO, ANUALIZADA, ID_GRUPO ) " +
						 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			this.getJdbcTemplate().update(SQL, new Object[]{cve_req, num_req, (cve_contrato==0 ? null:cve_contrato), (cve_vale==0 ? null:cve_vale), ejercicio, id_proyecto, partida, cve_unidad, fecha, tipo, notas, fecha_cap, fecha_ingreso, cve_pers, status, 0, mes, anualizada, id_grupo});
			//Guardar en la bitacora
			gatewayBitacora.guardarBitacora(gatewayBitacora.NUEVA_REQUISICION, ejercicio, cve_pers, cve_req, num_req, "REQ", fecha, id_proyecto.toString(), partida, null, 0D);
			return cve_req;
		}
		return cve_req;
	}

	/*Metodo para actualizar los datos de la requisicion*/
	public void actualizar(Long cve_req,  String num_req, Long cve_contrato, Long cve_vale, String cve_unidad, Date fecha, int tipo, String notas, int mes, int status,  int ejercicio , int cve_pers, Long idproyecto, String partida ){
		String SQL = "UPDATE SAM_REQUISIC SET  NUM_REQ=?, CVE_CONTRATO =?, CVE_VALE=?, EJERCICIO=?,  ID_DEPENDENCIA=?, FECHA=?, CVE_PERS=?, TIPO=?, OBSERVA=?,  PERIODO=?, STATUS=?, ID_PROYECTO=?, CLV_PARTID = ? WHERE CVE_REQ=? ";
		this.getJdbcTemplate().update(SQL, new Object[]{num_req, (cve_contrato==null ? null:(cve_contrato!=0) ? cve_contrato:null),(cve_vale==null ? null:(cve_vale!=0) ? cve_vale:null) ,ejercicio, cve_unidad, fecha, cve_pers, tipo, notas, mes, status, idproyecto, partida, cve_req});
		//Guardar en la bitacora
		Map requisicion = this.getJdbcTemplate().queryForMap("SELECT (SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ ) AS IMPORTE, ID_PROYECTO FROM SAM_REQUISIC WHERE CVE_REQ = ? AND STATUS < = ?", new Object []{cve_req, this.REQ_MOVTO_SOLICITADO});
		if(requisicion.get("IMPORTE")==null) requisicion.put("IMPORTE", 0);
		gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_REQUISICION, ejercicio, cve_pers, cve_req, num_req, "REQ", fecha, idproyecto.toString(), partida, null, Double.parseDouble(requisicion.get("IMPORTE").toString()));
	}
	
	/*Metodo para obtener el autoincremento de los numero de la requisicion*/
	private Long getNumeroRequisicion(Integer ejercicio ){
		return this.getJdbcTemplate().queryForLong("select max(cve_req) as numero FROM SAM_REQUISIC where ejercicio= ? ", new Object[]{ejercicio});	
	}
	
	/*Metodo para comprobar la existencia de una REQ*/
	public boolean comprobarExistencia(String num_req){
		return (this.getJdbcTemplate().queryForInt("SELECT COUNT(NUM_REQ) AS N FROM SAM_REQUISIC WHERE NUM_REQ =?",new Object[]{num_req}))!=0;
	}
	
	private String formatEstatus(String [] estatus ){
		String estatusFormat="";
		String coma="";
		for (String estatu :estatus) {
			estatusFormat +=coma+""+estatu+"";
			coma=",";
		}
		return estatusFormat;
	}

	/*Metodo  que genera el listado de requisiciones para pedidos y busca mendiante los parametros*/
	public List<Map> getListaDeRequisicionesPedidos(String unidad, String estatus , Date fInicial, Date fFinal , Integer ejercicio,Integer idUsuario, String numreq, String proyecto, String clv_partid, String tipogto){
		Map parametros =  new HashMap<String,Object>();
		/*Asignacion de parametros*/
		parametros.put("unidad", unidad);
		parametros.put("fechaInicial", fInicial);
		parametros.put("fechaFinal", fFinal);
		parametros.put("ejercicio", ejercicio);
		parametros.put("estatus", estatus);
		parametros.put("tipogto", tipogto);
		
		String sql = "";
		if (fInicial != null && fFinal != null ) sql = " AND CONVERT(datetime,convert(varchar(10), R.FECHA ,103),103) between :fechaInicial and :fechaFinal ";		
		if(!unidad.equals("0")) sql+= " AND R.ID_DEPENDENCIA=:unidad ";
		
		if(numreq!=null){
			sql+= " AND R.NUM_REQ LIKE '%"+numreq+"%'";
		}
		
		if(proyecto!=null)
			sql+= " AND C.N_PROGRAMA LIKE '%"+proyecto+"%'";
		if(clv_partid!=null)
			sql+= " AND R.CLV_PARTID LIKE '%"+clv_partid+"%'";
		
		if(tipogto!=null)
			if(!tipogto.equals("0"))
				sql+= " AND P.ID =:tipogto";
	
		return this.getNamedJdbcTemplate().queryForList("SELECT P.RECURSO, R.CVE_REQ, R.NUM_REQ, U.DEPENDENCIA AS UNIDAD_ADM, R.CVE_PERS, R.ID_PROYECTO, C.N_PROGRAMA, R.CLV_PARTID, R.ID_DEPENDENCIA, R.OBSERVA, R.TIPO, "+
																"TIPO_REQ = (CASE r.TIPO WHEN 1 THEN 'REQ.' WHEN 2 THEN 'O.S.' WHEN 3 THEN 'O.T.' WHEN 4 THEN 'O.T.M.P.' WHEN 5 THEN 'O.S.BOMBAS' WHEN 6 THEN 'PAQUETE' WHEN 7 THEN 'REQ. CALEN' WHEN 8 THEN 'OS. CALEN' END), "+
																"R.PERIODO, (SELECT TOP 1 MES FROM MESES WHERE ESTATUS='ACTIVO') AS PERIODO_ACTUAL,  R.STATUS , convert(varchar(10), R.FECHA ,103) AS FECHA, "+
																"ISNULL((SELECT SUM(CANTIDAD *PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = R.CVE_REQ),0) AS IMPORTE, "+
																"ISNULL((SELECT SUM(cantidad_temp * precio_est ) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = R.CVE_REQ),0) AS IMPORTE2,  S.DESCRIPCION_ESTATUS "+
														"FROM SAM_REQUISIC AS R "+
																"INNER JOIN SAM_ESTATUS_REQ AS S ON (S.ID_ESTATUS = R.STATUS) "+
																"LEFT JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = R.ID_PROYECTO) "+
																"LEFT JOIN CAT_RECURSO AS P ON (P.ID = C.ID_RECURSO) "+
																"LEFT JOIN SAM_ORDEN_TRAB AS OT ON (OT.CVE_REQ = R.CVE_REQ) "+
																"LEFT JOIN CAT_DEPENDENCIAS AS U ON (U.ID = R.ID_DEPENDENCIA) "+
														"WHERE R.EJERCICIO =:ejercicio AND R.STATUS =:estatus AND R.TIPO IN (1,7) "+sql+" ORDER BY R.FECHA DESC", parametros);
				
				/*"select  r.CVE_REQ, r.NUM_REQ, r.PROYECTO, r.CLV_PARTID, "+  
				" r.CLV_UNIADM,uni.NOMBRE UNIDAD_ADM, r.TIPO, TIPO_REQ = CASE r.TIPO WHEN 1 THEN 'REQ.' WHEN 2 THEN 'O.S.' WHEN 3 THEN 'O.T.' WHEN 4 THEN 'O.T.M.P.' WHEN 5 THEN 'O.S.BOMBAS' WHEN 6 THEN 'PAQUETE' WHEN 7 THEN 'REQ. ANUAL' END, r.PERIODO, r.STATUS ,  "+
				" convert(varchar(10), r.fecha ,103)  FECHA , "+
				" isnull ( (select sum (cantidad * precio_est ) from req_movtos where cve_req=r.cve_req and comprometido=1  and req_movtos.status in ("+this.REQ_MOVTO_SOLICITADO+") ),0) IMPORTE, "+ 
				" s.DESCRIPCION_ESTATUS  from requisic r , SAM_ESTATUS_REQ s  , UNIDAD_ADM UNI "+
				" where r.ejercicio=:ejercicio and  r.STATUS =:estatus  and  r.TIPO IN(1,7) "+sql+"  and  r.STATUS=s.ID_ESTATUS  and UNI.ORG_ID=r.CLV_UNIADM "+
				" order by  r.cve_req desc   "*/
	}
	
	/*Metodo que genera el listado de requisiciones y busca mendiante los parametros*/
	public List<Map> getListaDeRequisicionesPorEjemplo(String unidad, String  estatus , Date fInicial, Date fFinal , Integer ejercicio, Integer tipo, String  verUnidad, String numreq, Integer idUsuario, String unidad_usuario, String proyecto, String clv_partid, String tipogto, String beneficiario, boolean privilegio, String cboconOP, String listadoReq){
		Map parametros =  new HashMap<String,Object>();
		/*Asignacion de parametros*/
		parametros.put("unidad", unidad);
		parametros.put("fechaInicial", fInicial);
		parametros.put("fechaFinal", fFinal);
		parametros.put("ejercicio", ejercicio);
		parametros.put("tipo", tipo);
		parametros.put("cve_pers", idUsuario);
		parametros.put("tipogto", tipogto);
		parametros.put("beneficiario", beneficiario);
		
		String sql = "";
		
		if (fInicial != null && fFinal != null ) 
				sql = " AND CONVERT(datetime,CONVERT(varchar(10), R.FECHA ,103),103) between :fechaInicial and :fechaFinal ";
		
		if(tipo != null&&tipo!=0 ) 
			sql+= " and R.TIPO=:tipo ";
		
		if(cboconOP!=null&&!cboconOP.equals("0")){
			if(cboconOP.equals("1"))
				sql+= " and R.CVE_REQ IN (SELECT CVE_REQ FROM SAM_OP_COMPROBACIONES WHERE CVE_REQ = R.CVE_REQ AND CVE_PED IS NULL) ";
			if(cboconOP.equals("2"))
				sql+= " and R.CVE_REQ NOT IN (SELECT CVE_REQ FROM SAM_OP_COMPROBACIONES WHERE CVE_REQ = R.CVE_REQ AND CVE_PED IS NULL) ";
		}
		
		if (verUnidad==null&&!privilegio)
			sql+= " AND R.CVE_PERS=:cve_pers ";
		if(verUnidad!=null&&!privilegio)
			sql+= " AND (R.CVE_PERS=:cve_pers OR R.ID_DEPENDENCIA=:unidad) ";
		
		if (verUnidad==null&&privilegio&&!unidad.equals("0"))
			sql+= " AND (R.ID_DEPENDENCIA=:unidad) ";
		if (verUnidad==null&&privilegio&&!unidad.equals("0"))
			sql+= "AND (R.ID_DEPENDENCIA=:unidad)";
		if(verUnidad!=null&&privilegio&&unidad.equals("0"))
			sql+= " AND (R.CVE_PERS=:cve_pers OR R.ID_DEPENDENCIA=:unidad)";

			
		if(numreq!=null&&!numreq.equals("")){
			sql+= " AND R.NUM_REQ LIKE '%"+numreq+"%'";
		}
		
		if(listadoReq!=null&&!listadoReq.equals("")){
			String[] arreglo = listadoReq.split(",");
			String temp = "";
			sql+= " AND R.NUM_REQ IN(";
			for(String item: arreglo){
				temp+="'"+item.trim()+"',";
			}
			temp = temp.substring(0, temp.length()-1);
			sql+= temp+")";
		}
		
		if(proyecto!=null&&!proyecto.equals(""))
			sql+= " AND C.N_PROGRAMA LIKE '%"+proyecto+"%'";
		if(clv_partid!=null&&!clv_partid.equals(""))
			sql+= " AND R.CLV_PARTID LIKE '%"+clv_partid+"%'";
		
		if(tipogto!=null&&!tipogto.equals(""))
			if(!tipogto.equals("0"))
				sql+= " AND P.ID =:tipogto";
		if(beneficiario!=null&&!beneficiario.equals(""))
			if(!beneficiario.equals("0")&&!beneficiario.equals(""))
				sql+= " AND B.CLV_BENEFI =:beneficiario";
		if (estatus.contains("1") && estatus.contains("5")) //STATUS =1 Y 5 CON FECHA DE FINIQUITADO...
				sql+=" AND R.FECHA_FINIQUITADO IS NOT NULL ";
		if (estatus.contains("1")) //STATUS = CERRADO
			sql+=" AND R.FECHA_CIERRE IS NOT NULL ";
		if (estatus.contains("0")) //STATUS = CERRADO
			sql+=" AND R.FECHA_CIERRE IS NULL ";
		if (estatus.contains("2")) //STATUS = CERRADO
			sql+=" AND R.FECHA_CIERRE IS NOT NULL ";
		if (estatus.contains("4")) //STATUS = CERRADO
			sql+=" AND R.STATUS=4 ";
		if (estatus.contains("9")) //STATUS = CERRADO
			estatus = "0,1,2,4,5 ";
		
		return this.getNamedJdbcTemplate().queryForList("SELECT R.CVE_REQ, R.NUM_REQ, R.CVE_PERS, C.ID_PROYECTO, C.N_PROGRAMA, R.CLV_PARTID, R.ID_DEPENDENCIA, R.OBSERVA, R.TIPO, "+
																"TIPO_REQ = (CASE r.TIPO WHEN 1 THEN 'REQ.' WHEN 2 THEN 'O.S.' WHEN 3 THEN 'O.T.' WHEN 4 THEN 'O.T.M.P.' WHEN 5 THEN 'O.S.BOMBAS' WHEN 6 THEN 'PAQUETE' WHEN 7 THEN 'REQ. CALEN' WHEN 8 THEN 'OS. CALEN' END), "+
																"R.PERIODO, (SELECT TOP 1 MES FROM MESES WHERE ESTATUS='ACTIVO') AS PERIODO_ACTUAL,  R.STATUS , convert(varchar(10), R.FECHA ,103) AS FECHA, "+
																"ISNULL((SELECT SUM(CANTIDAD *PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = R.CVE_REQ),0) AS IMPORTE, "+
																"ISNULL((SELECT SUM(cantidad_temp * precio_est ) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = R.CVE_REQ),0) AS IMPORTE2,  S.DESCRIPCION_ESTATUS,R.FECHA_FINIQUITADO "+
														"FROM SAM_REQUISIC AS R "+
																"INNER JOIN SAM_ESTATUS_REQ AS S ON (S.ID_ESTATUS = R.STATUS) "+
																"LEFT JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = R.ID_PROYECTO) "+
																"LEFT JOIN CAT_RECURSO AS P ON (P.ID = C.ID_RECURSO) "+
																"LEFT JOIN SAM_ORDEN_TRAB AS OT ON (OT.CVE_REQ = R.CVE_REQ) "+
																"LEFT JOIN CAT_BENEFI AS B ON (B.CLV_BENEFI = OT.CLV_BENEFI) "+
														"WHERE R.EJERCICIO =:ejercicio AND R.STATUS IN ("+estatus+") "+sql+" ORDER BY R.FECHA DESC", parametros);
	}
	
	/*Metodo que genera el listado de requisiciones y busca mendiante los parametros*/
	public List<Map> getListaDeRequisicionesPorEjemplo2(String unidad, String  estatus , Date fInicial, Date fFinal , Integer ejercicio, Integer tipo, String  verUnidad, String numreq, Integer idUsuario, String unidad_usuario, String proyecto, String clv_partid, String tipogto, String beneficiario, boolean privilegio, String cboconOP){
		Map parametros =  new HashMap<String,Object>();
		/*Asignacion de parametros*/
		parametros.put("unidad", unidad);
		parametros.put("fechaInicial", fInicial);
		parametros.put("fechaFinal", fFinal);
		parametros.put("ejercicio", ejercicio);
		parametros.put("tipo", tipo);
		parametros.put("cve_pers", idUsuario);
		parametros.put("tipogto", tipogto);
		parametros.put("beneficiario", beneficiario);
		
		String sql = "";
		
		if (fInicial != null && fFinal != null ) 
				sql = " AND CONVERT(datetime,CONVERT(varchar(10), R.FECHA ,103),103) between :fechaInicial and :fechaFinal ";
		
		if(tipo != null&&tipo!=0 ) 
			sql+= " and R.TIPO=:tipo ";
		
		if(cboconOP!=null&&!cboconOP.equals("0")){
			if(cboconOP.equals("1"))
				sql+= " and R.CVE_REQ IN (SELECT CVE_REQ FROM SAM_OP_COMPROBACIONES WHERE CVE_REQ = R.CVE_REQ AND CVE_PED IS NULL) ";
			if(cboconOP.equals("2"))
				sql+= " and R.CVE_REQ NOT IN (SELECT CVE_REQ FROM SAM_OP_COMPROBACIONES WHERE CVE_REQ = R.CVE_REQ AND CVE_PED IS NULL) ";
		}
		
		if (verUnidad==null&&!privilegio)
			sql+= " AND R.CVE_PERS=:cve_pers ";
		if(verUnidad!=null&&!privilegio)
			sql+= " AND (R.CVE_PERS=:cve_pers OR R.ID_DEPENDENCIA=:unidad) ";
		
		if (verUnidad==null&&privilegio&&!unidad.equals("0"))
			sql+= " AND (R.ID_DEPENDENCIA=:unidad) ";
		if (verUnidad==null&&privilegio&&!unidad.equals("0"))
			sql+= "AND (R.ID_DEPENDENCIA=:unidad)";
		if(verUnidad!=null&&privilegio&&unidad.equals("0"))
			sql+= " AND (R.CVE_PERS=:cve_pers OR R.ID_DEPENDENCIA=:unidad)";

			
		if(numreq!=null&&!numreq.equals("")){
			sql+= " AND R.NUM_REQ LIKE '%"+numreq+"%'";
		}
		
		if(proyecto!=null&&!proyecto.equals(""))
			sql+= " AND C.N_PROGRAMA LIKE '%"+proyecto+"%'";
		if(clv_partid!=null&&!clv_partid.equals(""))
			sql+= " AND R.CLV_PARTID LIKE '%"+clv_partid+"%'";
		
		if(tipogto!=null&&!tipogto.equals(""))
			if(!tipogto.equals("0"))
				sql+= " AND P.ID =:tipogto";
		if(beneficiario!=null&&!beneficiario.equals(""))
			if(!beneficiario.equals("0")&&!beneficiario.equals(""))
				sql+= " AND B.CLV_BENEFI =:beneficiario";
		
		return this.getNamedJdbcTemplate().queryForList("SELECT R.CVE_REQ, R.NUM_REQ, OP.NUM_OP, CONVERT(varchar(10), OP.FECHA ,103) AS FECHA_OP, (CASE (OP.TIPO) WHEN 0 THEN 'ADQ.' WHEN 1 THEN 'OBRAS' WHEN 2 THEN 'SERV.' WHEN 4 THEN 'MULT. PROY.' WHEN 5 THEN 'DEVOL.' WHEN 6 THEN 'E. AJENOS' WHEN 7 THEN 'COMPEN.' WHEN 8 THEN 'COMBUST.' WHEN 9 THEN 'F. FIJO' WHEN 10 THEN 'MULT. PED.' WHEN 11 THEN 'VALES' END) AS TIPO_OP,  " +
																"OP.NOTA, OP.IMPORTE AS IMPORTE_OP, SOP.DESCRIPCION_ESTATUS AS STATUS_OP, " +
																"R.CVE_PERS, C.ID_PROYECTO, C.N_PROGRAMA, R.CLV_PARTID, R.ID_DEPENDENCIA, R.OBSERVA, R.TIPO, "+
																"TIPO_REQ = (CASE r.TIPO WHEN 1 THEN 'REQ.' WHEN 2 THEN 'O.S.' WHEN 3 THEN 'O.T.' WHEN 4 THEN 'O.T.M.P.' WHEN 5 THEN 'O.S.BOMBAS' WHEN 6 THEN 'PAQUETE' WHEN 7 THEN 'REQ. CALEN' WHEN 8 THEN 'OS. CALEN' END), "+
																"R.PERIODO, (SELECT TOP 1 MES FROM MESES WHERE ESTATUS='ACTIVO') AS PERIODO_ACTUAL,  R.STATUS , convert(varchar(10), R.FECHA ,103) AS FECHA, "+
																"ISNULL((SELECT SUM(CANTIDAD *PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = R.CVE_REQ),0) AS IMPORTE, "+
																"ISNULL((SELECT SUM(cantidad_temp * precio_est ) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = R.CVE_REQ),0) AS IMPORTE2,  S.DESCRIPCION_ESTATUS "+
														"FROM SAM_REQUISIC AS R "+
																"INNER JOIN SAM_ESTATUS_REQ AS S ON (S.ID_ESTATUS = R.STATUS) "+
																"INNER JOIN SAM_OP_COMPROBACIONES AS COM ON (COM.CVE_REQ = R.CVE_REQ AND COM.CVE_PED IS NULL) " +
																"INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = COM.CVE_OP) " +
																"INNER JOIN SAM_ESTATUS_OP AS SOP ON (SOP.ID_ESTATUS = OP.STATUS) "+
																"LEFT JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = R.ID_PROYECTO) "+
																"LEFT JOIN CAT_RECURSO AS P ON (P.ID = C.ID_RECURSO) "+
																"LEFT JOIN SAM_ORDEN_TRAB AS OT ON (OT.CVE_REQ = R.CVE_REQ) "+
																"LEFT JOIN CAT_BENEFI AS B ON (B.CLV_BENEFI = OT.CLV_BENEFI) "+
														"WHERE R.EJERCICIO =:ejercicio AND R.STATUS IN ("+estatus+") "+sql+" ORDER BY R.NUM_REQ, OP.FECHA ASC", parametros);
	}
	
	/*Metodo para obteber los datos presupuestales de la requisicion*/
	public Map getRequisicion(Long cve_req){
		return this.getJdbcTemplate().queryForMap("SELECT DISTINCT "+
															"SAM_REQUISIC.CVE_REQ,"+
															"SAM_REQUISIC.NUM_REQ,"+
															"SAM_REQUISIC.CVE_CONTRATO,"+
															"SAM_REQUISIC.FECHA_CIERRE AS FECHA_CIERRE2,"+
															"CONVERT(varchar(10), SAM_REQUISIC.FECHA_CIERRE, 103) AS FECHA_CIERRE, "+
															"SAM_REQUISIC.EJERCICIO,"+
															"SAM_REQUISIC.ID_PROYECTO,"+
															"VPROYECTO.N_PROYECTO, "+
															"CEDULA_TEC.N_PROGRAMA,"+
															"SAM_REQUISIC.CLV_PARTID,"+  
															"SAM_REQUISIC.ID_DEPENDENCIA,"+
															"CAT_DEPENDENCIAS.CLV_UNIADM,"+
															"CAT_RECURSO.CODIGO_FF," + 
															"SAM_REQUISIC.CVE_PERS,"+ 
															"CONVERT(varchar(10),SAM_REQUISIC.FECHA,103) AS FECHA,"+
															"SAM_REQUISIC.TIPO,"+
															"CONVERT(varchar(10),SAM_REQUISIC.FECHA_CAP,103) AS FECHA_CAP,"+
															"CONVERT(varchar(10),SAM_REQUISIC.FECHA_INGRESO,103) AS FECHA_INGRESO,"+
															"SAM_REQUISIC.OBSERVA,"+
															"SAM_REQUISIC.STATUS,"+
															"SAM_REQUISIC.PERIODO,"+ 
															"SAM_REQUISIC.COMPROMETE,"+ 
															"SAM_REQUISIC.ANUALIZADA,"+
															"SAM_REQUISIC.ID_GRUPO,"+
															"(SELECT DESCRIPCION FROM MESES WHERE MES = SAM_REQUISIC.PERIODO) AS MES, "+
															"(SELECT SUM(CANTIDAD*PRECIO_EST) AS TOTAL FROM SAM_REQ_MOVTOS WHERE CVE_REQ = SAM_REQUISIC.CVE_REQ) AS TOTAL_REQ,"+
															"SAM_ORDEN_TRAB.CLV_BENEFI,"+ 
															"SAM_ORDEN_TRAB.COSTO_TOTAL,"+ 
															"SAM_ORDEN_TRAB.AREA,"+ 
															"SAM_ORDEN_TRAB.VEHICULO,"+ 
															"SAM_ORDEN_TRAB.MARCA,"+   
															"SAM_ORDEN_TRAB.MODELO,"+ 
															"SAM_ORDEN_TRAB.PLACAS,"+ 
															"SAM_ORDEN_TRAB.NUM_INV,"+ 
															"SAM_ORDEN_TRAB.COLOR,"+ 
															"SAM_ORDEN_TRAB.USUARIO,"+ 
															"SAM_ORDEN_TRAB.CVE_CONCURSO,"+  
															"CAT_BENEFI.NCOMERCIA,"+
															"CAT_PARTID.PARTIDA,"+  
															"CAT_RECURSO.RECURSO AS TIPO_GASTO,"+
															"VPROYECTO.PROG_PRESUP,"+
															"VPROYECTO.CLV_ACTINST,"+
															"VPROYECTO.ACT_INST,"+
															"VPROYECTO.CLV_FINALIDAD, "+
															"VPROYECTO.FINALIDAD, "+
															"VPROYECTO.CLV_FUNCION, "+
															"VPROYECTO.FUNCION, "+
															"VPROYECTO.CLV_SUBFUNCION, "+
															"VPROYECTO.SUBFUNCION, "+
															"VPROYECTO.SUBSUBFUNCION, "+
															"VPROYECTO.CLV_SUBSUBFUNCION, "+
															"VPROYECTO.CLV_PROGRAMA, "+
															"VPROYECTO.PROGRAMA, "+
															"(LEFT(SAM_REQUISIC.CLV_PARTID,1)) AS CLV_OBJETOGASTO, "+
															"(SELECT CAPITULO FROM CAT_CAPITU WHERE CLV_CAPITU = (LEFT(SAM_REQUISIC.CLV_PARTID,1)+'000')) AS OBJETO_GASTO, "+
															"CEDULA_TEC.DECRIPCION,"+
															"CEDULA_TEC.CLV_PROGRAMA,"+
															"CEDULA_TEC.CLV_LOCALIDAD,"+
															/*"CAT_PROGRAMA.PROGRAMA,"+*/
															"CAT_LOCALIDAD.LOCALIDAD,"+
															"CAT_DEPENDENCIAS.DEPENDENCIA AS UNIDAD_SOLICITANTE, "+
															"(CAT_DEPENDENCIAS.CLV_UNIADM + ' ' + CAT_DEPENDENCIAS.DEPENDENCIA) AS NOMBRE_UNIDAD "+
														"FROM SAM_REQUISIC "+
															"LEFT JOIN SAM_ORDEN_TRAB  ON (SAM_ORDEN_TRAB.CVE_REQ=SAM_REQUISIC.CVE_REQ) "+ 
															"LEFT JOIN PARTIDAS ON (SAM_REQUISIC.ID_PROYECTO = PARTIDAS.ID_PROYECTO AND PARTIDAS.CLV_PARTID = SAM_REQUISIC.CLV_PARTID) "+
															"LEFT JOIN PART_EXT2 ON (PARTIDAS.ID_PROYECTO = PART_EXT2.ID_PROYECTO AND PART_EXT2.CLV_PARTID = PARTIDAS.CLV_PARTID) "+
															"LEFT JOIN CAT_PARTID  ON (SAM_REQUISIC.CLV_PARTID=CAT_PARTID.CLV_PARTID ) "+
															"LEFT JOIN CAT_BENEFI  ON (SAM_ORDEN_TRAB.clv_benefi=CAT_BENEFI.clv_benefi) "+
															"LEFT JOIN CEDULA_TEC  ON (SAM_REQUISIC.ID_PROYECTO=CEDULA_TEC.ID_PROYECTO) "+
															"LEFT JOIN CAT_RECURSO ON (CEDULA_TEC.ID_RECURSO=CAT_RECURSO.ID )"+ 
															/*"LEFT JOIN CAT_PROGRAMA  ON (CEDULA_TEC.CLV_PROGRAMA=CAT_PROGRAMA.CLV_PROGRAMA ) "+*/
															/*"LEFT JOIN CAT_SUBPRO  ON (CEDULA_TEC.CLV_SUBPRO=CAT_SUBPRO.CLV_SUBPRO and CEDULA_TEC.CLV_PROGRA=CAT_SUBPRO.CLV_PROGRA) "+*/ 
															/*"LEFT JOIN CAT_SUBSEC  ON (CEDULA_TEC.CLV_SUBSEC=CAT_SUBSEC.CLV_SUBSEC ) "+*/
															/*"LEFT JOIN CAT_FINALIDAD ON (CAT_FINALIDAD.ID = CEDULA_TEC.ID_FINALIDAD) " +*/
															"LEFT JOIN VPROYECTO ON  (VPROYECTO.ID_PROYECTO = CEDULA_TEC.ID_PROYECTO) " +
															"LEFT JOIN CAT_LOCALIDAD  ON (CEDULA_TEC.CLV_LOCALIDAD=CAT_LOCALIDAD.CLV_LOCALIDAD ) "+
															"LEFT JOIN CAT_DEPENDENCIAS ON (SAM_REQUISIC.ID_DEPENDENCIA=CAT_DEPENDENCIAS.ID ) "+
															/*"LEFT JOIN SAM_CONTRATOS ON (SAM_CONTRATOS.CVE_CONTRATO = SAM_REQUISIC.CVE_CONTRATO) "+*/
														"WHERE SAM_REQUISIC.CVE_REQ = ?", new Object[]{cve_req});
	}
	
	/*Metodo para  cancelar las requisiciones seleccionadas*/
	public String cancelarRequisicion(final Long[] cveReq, final int cve_pers){
		error = "";
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
                	for(Long id : cveReq)
                		error = _cancelarRequisicion(id, cve_pers);
                	
                } 
             });
            return error;
            } catch (DataAccessException e) {            
            	 log.info("Las Requisiciones no se han podido cancelar");	   
            	 error = e.getMessage();
                 throw new RuntimeException(e.getMessage(),e);
            }
            
	}
	
	
	
	/*Metodo para validar que la requisicion tenga un pedido*/
	public boolean tienePedido(Long cve_req){
		return (this.getJdbcTemplate().queryForInt("SELECT TOP 1 COUNT(*) AS N FROM SAM_PEDIDOS_EX WHERE STATUS IN(1,2,4,5) AND CVE_REQ = ?", new Object[]{cve_req})>0);
	}
	
	
	
	/*Metodo para cerrar la Requisicion*/
	public boolean cerrarRequisicion(final Long cve_req, final Integer idUsuario, final String claveUnidad,final List<Map<String,String>> calendario){
		/*Determinar si tiene presupuesto*/
		exito=false;
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                	                
					Map requisicion = getRequisicPresupuesto(cve_req);
					Integer periodo=Integer.parseInt(requisicion.get("PERIODO").toString());
					Long idproyecto = Long.parseLong(requisicion.get("ID_PROYECTO").toString());
					int tipo_req = Integer.parseInt(requisicion.get("TIPO").toString());
					
					
					boolean cerrarOtOs = getPrivilegioEn(idUsuario, 109);
					//si no es requisicion entra
					if(tipo_req!=1&&tipo_req!=7){
						if(!cerrarOtOs)
							throw new RuntimeException("No cuenta con privilegios suficientes para realizar esta operación (Cerrar OT/OS)");
					}
					
					Map req = getRequisicion(cve_req);
					
					//validar si la OT/OS tiene beneficiario
					if(requisicion.get("CLV_BENEFI")==null && (tipo_req != 1 && tipo_req != 7))
					{
						throw new RuntimeException("No se puede cerrar el documento por que no se ha especificado un beneficiario válido");
					}
					
					/*if((tipo_req !=1 && tipo_req !=7)) 
						if(req.get("CLV_BENEFI")== null)
							throw new RuntimeException("No se puede cerrar el documento por que no se ha especificado un beneficiario válido");
					*/
					String proyecto="";//(String)requisicion.get("PROYECTO");
					String partida =requisicion.get("CLV_PARTID").toString();
					Double importe= (((BigDecimal)requisicion.get("IMPORTE"))).doubleValue();
					int tipo =Integer.parseInt(requisicion.get("TIPO").toString());
					Long cve_contrato = (requisicion.get("CVE_CONTRATO")==null) ? 0L: Long.parseLong(requisicion.get("CVE_CONTRATO").toString());
					Long cve_vale = (requisicion.get("CVE_VALE")==null) ? 0L: Long.parseLong(requisicion.get("CVE_VALE").toString());
					boolean presupuest = false;
					
					if(idproyecto==0) throw new RuntimeException("Fallo desconocido no se pudo completar la operacion al cerrar el documento, contacte a su administrador");       
					if(importe<=0) throw new RuntimeException("No es posible cerrar el documento con importe total en "+importe.toString());       
					
					presupuest = verificarPresupuestoReq(idproyecto, proyecto, partida, periodo, idUsuario, importe,calendario,tipo, cve_contrato, cve_vale); 
					
					if(presupuest){
						//Agregado Israel de la Cruz, 10/09/2015
						getJdbcTemplate().update("DELETE FROM SAM_COMP_REQUISIC WHERE CVE_REQ=?",new Object []{cve_req});
						
						if(tipo_req == 5 || tipo_req == 8)
						{
							getJdbcTemplate().update("UPDATE SAM_ORDEN_TRAB SET SAM_ORDEN_TRAB.COSTO_TOTAL=(SELECT SUM(SAM_REQ_MOVTOS.CANTIDAD*SAM_REQ_MOVTOS.PRECIO_EST) AS TOTAL FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = ?) WHERE SAM_ORDEN_TRAB.CVE_REQ = ? ", new Object[]{cve_req, cve_req});
						}
						
						//si la requisicion es anualizada 
						if(tipo_req==7){
							//obtener todos los conceptos para despues actualizar el temporal de cantidades
							List <Map> mov = gatewayMovimientosRequisicion.getConceptos(cve_req);
							for(Map row: mov){
								getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET CANTIDAD_TEMP = CANTIDAD WHERE ID_REQ_MOVTO = ?", new Object[]{row.get("ID_REQ_MOVTO").toString()});
							}
						}
						/*Requisiciones, Ordenes de Trabajo y Servicio normal*/
						if (tipo_req!=7&&tipo_req!=8){
							    //gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, periodo, importe, "COMPROMETER");
							    insertarCompromisoRequisicion( cve_req,importe,"COMPROMISO",null, periodo);
							  //Solo si se compromete atravez de contrato
								if(cve_contrato!=null&&cve_contrato!=0){
									String tipo_doc = "";
									if (requisicion.get("TIPO").equals("1")) tipo_doc = "REQ";
									if (requisicion.get("TIPO").equals("2")) tipo_doc = "OS";
									if (requisicion.get("TIPO").equals("3")) tipo_doc = "OT";
									if (requisicion.get("TIPO").equals("4")) tipo_doc = "OT";
									if (requisicion.get("TIPO").equals("5")) tipo_doc = "OS";
									if (requisicion.get("TIPO").equals("6")) tipo_doc = "OS";
									if (requisicion.get("TIPO").equals("7")) tipo_doc = "REQ";
									if (requisicion.get("TIPO").equals("8")) tipo_doc = "OS";
									
									getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)", new Object[]{cve_contrato, "LIBERACION", tipo_doc,cve_req, periodo, importe});
								}
						    }
						else{
							//REQ. CALENDARIZADA Y OS. CALENDARIZADA
								calendario.indexOf(0);
								for (Map<String,String> dato:calendario ) {
									int mes = Integer.parseInt(dato.get("mes"));
									double importeMes = Double.parseDouble(dato.get("importe"));
									gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, mes, importeMes, "COMPROMETER");
									insertarCompromisoRequisicion( cve_req,importeMes,"COMPROMISO",null, mes);
									//solo si se compromete atravez de contrato
									if(cve_contrato!=null&&cve_contrato!=0){
										String tipo_doc = "";
										if (requisicion.get("TIPO").equals("7")) tipo_doc = "REQ";
										if (requisicion.get("TIPO").equals("8")) tipo_doc = "OS";
										getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)", new Object[]{cve_contrato, "LIBERACION", tipo_doc,cve_req, mes, importeMes});
									}
								}				
						}
						
						//comprobar si hay pedidos en edicion de la requisicion
						Integer cont_ped = getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_PEDIDOS_EX WHERE CVE_REQ = ? AND STATUS = ?", new Object[]{cve_req, 0});
						Integer status_req = 0;
						//cerrar normalmente
						if(cont_ped==0)
							status_req = REQ_STATUS_PENDIENTE;
						else 
							status_req = REQ_STATUS_EN_PROCESO;
						
						Date fecha_cierre = new Date();
						getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS=?, FECHA_CIERRE=?, COMPROMETE=1 WHERE CVE_REQ = ?", new Object[]{status_req,fecha_cierre, cve_req});
						//getJdbcTemplate().update("UPDATE REQ_MOVTOS SET STATUS=?, COMPROMETIDO=1 WHERE CVE_REQ = ?", new Object[]{REQ_MOVTO_SOLICITADO, cve_req});
						getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET STATUS = ?, COMPROMETIDO = ? WHERE CVE_REQ = ?", new Object[]{REQ_MOVTO_SOLICITADO, 1, cve_req});
						_guardarEnBitacoraReq(cve_req, gatewayBitacora.CERRAR_REQUISICION, idUsuario);
						exito=true;
						
						//SE AGREGA PROCEDIMIENTO ALMACENADO DE PEREDO PARA CONTABILIZAR PEDIDOS 28/JUL/2013
						//StoreProcedureRequisiciones sp = new StoreProcedureRequisiciones(getJdbcTemplate().getDataSource());
						//Map result = sp.execute(cve_req, 1);
					   //TERMINA PROCEDIMIENT0
						
					}
					else{
						/*Regresa falso si no hay presupuesto*/
						exito=false;
					}		
                } 
            });
          
           } catch (DataAccessException e) {            
           	 log.info("Las requisiciones no se han podido cerrar");	                    
                throw new RuntimeException(e.getMessage(),e);                
           }	
           return exito;
	}

	public void insertarCompromisoRequisicion( Long cve_req,double importe, String tipo,Long idPedido, int periodo ){
		//getJdbcTemplate().update("DELETE FROM SAM_COMP_REQUISIC WHERE CVE_REQ=?",new Object []{cve_req});
		getJdbcTemplate().update("INSERT INTO SAM_COMP_REQUISIC (CVE_REQ,IMPORTE,TIPO,CVE_PED,PERIODO) values(?,?,?,?,?)",new Object[]{cve_req,importe, tipo,idPedido, periodo});
	}
	public Map getRequisicPresupuesto(Long cve_req){
		return getJdbcTemplate().queryForMap("SELECT NUM_REQ, CVE_CONTRATO, CVE_VALE, OT.CLV_BENEFI, ID_PROYECTO, CLV_PARTID, PERIODO, FECHA, SAM_REQUISIC.TIPO, STATUS, EJERCICIO,  ISNULL((SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ),0) AS IMPORTE FROM SAM_REQUISIC LEFT JOIN SAM_ORDEN_TRAB AS OT ON (OT.CVE_REQ = SAM_REQUISIC.CVE_REQ) WHERE SAM_REQUISIC.CVE_REQ = ?", new Object []{cve_req});		
	}
	
	private boolean verificarPresupuestoReq(Long idproyecto, String proyecto,String partida,Integer periodo, Integer idUsuario, Double importe,List<Map<String,String>> calendario, int tipo, Long cve_contrato, Long cve_vale){
		Boolean tienePresupuesto=null;
		switch(tipo) { // Elige la opcion acorde al numero de mes
		case 7: 
			for (Map<String,String> dato:calendario ) {
				int mes = Integer.parseInt(dato.get("mes"));
				double importeMes = Double.parseDouble(dato.get("importe"));
				/*Validando la parte de contratos*/
				if(cve_contrato!=null&&cve_contrato!=0){
					BigDecimal total_comprometido = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(MONTO,0) FROM VT_COMPROMISOS WHERE TIPO_DOC = ? AND CVE_DOC = ? AND PERIODO = ?", new Object[]{"CON",cve_contrato, mes}, BigDecimal.class);
					if(total_comprometido.doubleValue()>=importeMes)
						tienePresupuesto = true;
					else
						tienePresupuesto = false;
				}
				else{
					boolean  tienePresupuestoMes=gatewayProyectoPartidas.verificarPresupuesto(idproyecto, proyecto, partida, mes, idUsuario,  importeMes );				
					if (tienePresupuestoMes && tienePresupuesto==null )
						tienePresupuesto=true;
					else
						if (!tienePresupuestoMes)
						tienePresupuesto=false;
				}
			}
			break;
		case 8:
			for (Map<String,String> dato:calendario ) {
				int mes = Integer.parseInt(dato.get("mes"));
				double importeMes = Double.parseDouble(dato.get("importe"));
				if(cve_contrato!=null&&cve_contrato!=0){
					BigDecimal total_comprometido = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(MONTO,0) FROM VT_COMPROMISOS WHERE TIPO_DOC = ? AND CVE_DOC = ? AND PERIODO = ?", new Object[]{"CON",cve_contrato, mes}, BigDecimal.class);
					if(total_comprometido.doubleValue()>=importeMes)
						tienePresupuesto = true;
					else
						tienePresupuesto = false;
				}
				else{
					boolean  tienePresupuestoMes=gatewayProyectoPartidas.verificarPresupuesto(idproyecto, proyecto, partida, mes, idUsuario,  importeMes );				
					if (tienePresupuestoMes && tienePresupuesto==null )
						tienePresupuesto=true;
					else
						if (!tienePresupuestoMes)
						tienePresupuesto=false;	
				}
			}
			break;
		default: 
			/*Validacion del Presupuesto para los demas tipos de requisiciones*/
			/*Validando el compromiso en contrato*/
			if(cve_contrato!=null&&cve_contrato!=0){
				BigDecimal total_comprometido = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(MONTO,0) FROM VT_COMPROMISOS WHERE TIPO_DOC = ? AND CVE_DOC = ? AND PERIODO = ?", new Object[]{"CON",cve_contrato, periodo}, BigDecimal.class);
				if(total_comprometido.doubleValue()>=importe)
					tienePresupuesto = true;
				else
					tienePresupuesto = false;
				}
			/*validando el compromiso del vale*/
			else if(cve_vale!=null&&cve_vale!=0){
				BigDecimal disponible_vale = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(MONTO,0) FROM VT_COMPROMISOS WHERE TIPO_DOC = ? AND CVE_DOC = ? AND PERIODO = ? AND ID_PROYECTO = ? AND CLV_PARTID = ? AND CONSULTA = 'PRECOMPROMETIDO'", new Object[]{"VAL",cve_vale, periodo, idproyecto, partida}, BigDecimal.class);
				BigDecimal disponible_presupuesto = (BigDecimal) this.getJdbcTemplate().queryForObject("SELECT ISNULL(dbo.getDisponible(?,?,?),0) AS DISPONIBLE FROM CEDULA_TEC AS CT WHERE CT.ID_PROYECTO = ? ", new Object[]{periodo, idproyecto, partida, idproyecto}, BigDecimal.class);
				if((disponible_vale.doubleValue()+disponible_presupuesto.doubleValue())>=importe)
					tienePresupuesto = true;
				else
					tienePresupuesto = false;
				
			}/*validacion normal de presupuesto*/
			else{
				tienePresupuesto=gatewayProyectoPartidas.verificarPresupuesto(idproyecto, proyecto, partida, periodo, idUsuario, importe );
				}
		break;
		 
		}		
	return tienePresupuesto.booleanValue();	
	}
	
	private void descomprometerReq(final Long cve_req, Integer ejercicio,String proyecto, String partida){
		int mes = gatewayMeses.getMesActivo(ejercicio);
		List<Map<String,Object> > compromisos= this.getJdbcTemplate().queryForList("select  * from SAM_COMP_REQUISIC  where CVE_REQ=? ",new Object []{cve_req});
		BigDecimal importeComTotal=new BigDecimal("0.0");
	    for (Map<String,Object> dato:compromisos ){
		   int mesReq=(Integer)dato.get("PERIODO");
		   BigDecimal importeReq=(BigDecimal)dato.get("IMPORTE");
		   importeComTotal=importeComTotal.add(importeReq);
		   if ( mesReq >= mes ){
			   gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, mesReq,importeComTotal.doubleValue() , "REDUCCION");			        
			   importeComTotal=new BigDecimal("0.0");
		   }
	     }
		if (importeComTotal.equals(new BigDecimal("0.0"))){
			gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, mes,importeComTotal.doubleValue() , "REDUCCION");
		}	
		
		
    }
	/*Metodo privado que apertura la requisicion*/
	public void aperturarRequisicion(final Long cve_req, final  int cve_pers, final int ejercicio){
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                	/*comprobar que no esta agarrada de una Orden de pago*/
            
                	
                	/*Valida aqui si el documento esta en el periodo actual*/
                	Map requisicion = getJdbcTemplate().queryForMap("SELECT NUM_REQ, FECHA_CIERRE, CVE_CONTRATO, ID_PROYECTO, CLV_PARTID, PERIODO, FECHA, TIPO, STATUS, EJERCICIO, ISNULL((SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ ),0) AS IMPORTE FROM SAM_REQUISIC WHERE CVE_REQ = ? ", new Object []{cve_req});
                	
                	Date fechaCierre = new Date();
            		fechaCierre = (Date) requisicion.get("FECHA_CIERRE");
            		Calendar c1 = Calendar.getInstance();
            		if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1) && !requisicion.get("TIPO").toString().equals("1"))
            		{
            			throw new RuntimeException("No se puede aperturar la requisicion "+requisicion.get("NUM_REQ").toString()+" por que el periodo es diferente");
            		}
            		
            		//Impide o Fuerza la Aperura de OS, OT, agregado por Israel a solicitud de abraham, 08/10/2016 
            		/* --------------------------------------
            		 * Id_TipoRequisicion	Descripcion
            		 * --------------------------------------
							2				Orden de Servicio
							3				O.T. a Vehiculos
							4				O.T. Maq. Pesada
							5				O.S. Bombas
							6				O.S. Paquetes 
            		 */
            		boolean ForzarAperturaOSOT = getPrivilegioEn(cve_pers, 153);
            		if((requisicion.get("TIPO").toString().equals("2") || requisicion.get("TIPO").toString().equals("3") || requisicion.get("TIPO").toString().equals("4") || requisicion.get("TIPO").toString().equals("5") || requisicion.get("TIPO").toString().equals("6")) && !ForzarAperturaOSOT)
            		{
            			throw new RuntimeException("Actualmente ya no se permite aperturar Ordenes de Servicio y Ordenes de Trabajo cerradas, consulte con su administrador del sistema.");
            		}
            		
            		if(existeFactura(cve_req)){
                		throw new RuntimeException("No se puede aperturar, el documento ya tiene una factura asociada");      
                	}
                	
                	if(existeContrato(cve_req)){
                		throw new RuntimeException("No se puede aperturar, el documento ya tiene un contrato asociado");      
                	}
                	
		    	    Integer op = getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM  SAM_ORD_PAGO WHERE CVE_REQ = ? AND STATUS IN (0,1,6) ", new Object[]{cve_req});
		    	    Integer ped = getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM  SAM_PEDIDOS_EX WHERE CVE_REQ = ? AND STATUS IN (1, 4, 5) ", new Object[]{cve_req});
		    	    boolean abrirReq = getPrivilegioEn(cve_pers, 23);
		    	    boolean abrirOtOs = getPrivilegioEn(cve_pers, 108);
		    	    boolean exitoApert = false;
		    	    
		    	    if(abrirReq&&(requisicion.get("TIPO").toString().equals("1")||requisicion.get("TIPO").toString().equals("2")))
		    	    	exitoApert = true;
		    	    if(!exitoApert)
		    	    	throw new RuntimeException("No cuenta con privilegios suficientes para realizar esta operación (Apertura OT/OS)");
		    	    	
		    	    if(op>0)
		    	    	throw new RuntimeException("La Requisicion "+requisicion.get("NUM_REQ").toString()+" no se puede aperturar por que esta relacionada a una Orden de Pago");
		    	    if(ped>0)
		    	    	throw new RuntimeException("La Requisicion "+requisicion.get("NUM_REQ").toString()+" no se puede aperturar por que esta relacionada a un Pedido");
		    	    	//decomprometer desde el contrato si aplica
		    	    	if(requisicion.get("CVE_CONTRATO")!=null){
		    	    		String tipo_doc = "";
							if (requisicion.get("TIPO").equals("1")) tipo_doc = "REQ";
							if (requisicion.get("TIPO").equals("2")) tipo_doc = "OS";
							if (requisicion.get("TIPO").equals("3")) tipo_doc = "OT";
							if (requisicion.get("TIPO").equals("4")) tipo_doc = "OT";
							if (requisicion.get("TIPO").equals("5")) tipo_doc = "OS";
							if (requisicion.get("TIPO").equals("6")) tipo_doc = "OS";
							if (requisicion.get("TIPO").equals("7")) tipo_doc = "REQ";
							if (requisicion.get("TIPO").equals("8")) tipo_doc = "OS";
		    	    		getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_DOC = ? AND TIPO_DOC = ? AND TIPO_MOV =?", new Object[]{cve_req, tipo_doc,"LIBERACION"});
		    	    	}
		    	    	
	                	descomprometerReq(cve_req, Integer.parseInt(requisicion.get("EJERCICIO").toString()), requisicion.get("ID_PROYECTO").toString(),requisicion.get("CLV_PARTID").toString());
	                	
	                	getJdbcTemplate().update("DELETE FROM SAM_COMP_REQUISIC WHERE CVE_REQ=?",new Object []{cve_req});
	                	getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS=?, FECHA_CIERRE=NULL, PERIODO =?, COMPROMETE=0 WHERE CVE_REQ = ?", new Object []{REQ_STATUS_NUEVO, gatewayMeses.getMesActivo(ejercicio), cve_req});
	                	getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET STATUS=?, COMPROMETIDO=0 WHERE CVE_REQ = ? ",new Object[]{REQ_MOVTO_EDICION, cve_req});
					//Guardar en bitacora
	                	_guardarEnBitacoraReq(cve_req, gatewayBitacora.APERTURA_REQUISICION, cve_pers);
		    	    }
            });
          
           } catch (DataAccessException e) {            
           	 log.info("Las requisiciones no se han podido aperturar");	                    
                throw new RuntimeException(e.getMessage(),e);                
           }	
	}
	
	public boolean existeFactura(Long cve_req){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE CVE_REQ =? AND STATUS IN (1,3)", new Object[]{cve_req})>0;
	}
	
	public boolean existeContrato(Long cve_req){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_CONTRATOS WHERE CVE_DOC =? AND ID_TIPO IN(1,2,3,6) AND STATUS IN (1,3)", new Object[]{cve_req})>0;
	}
	
	/*Metodo privado que cancela la requisicion*/
	private String _cancelarRequisicion(final Long cve_req, final int cve_pers){
		//comprobar que la requisicion no tenga un pedido
		error="";
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                
            	/*Valida aqui si el documento esta en el periodo actual para permitir aperturar*/
                	
                //Buscar si existe el Super Privilegio para Cancelar Requisiciones
		      	boolean privilegio = getPrivilegioEn(cve_pers, 136);
		      	
		      	Map req = getRequisicion(cve_req);
		      	Date fechaCierre = new Date();
		  		fechaCierre = (Date) req.get("FECHA_CIERRE2");
		  		Calendar c1 = Calendar.getInstance();
		  		
		  		if(fechaCierre!=null&&privilegio==false)
			  		if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1))
			  		{

			  			throw new RuntimeException("No se puede cancelar la Requisición "+req.get("NUM_REQ").toString()+", el periodo del documento es diferente al actual, consulte a su administrador");
	    		  	}
    		  		
                if(existeFactura(cve_req)){
                	throw new RuntimeException("No se puede cancelar, el documento ya tiene una factura");      
                }
                	
             	if(tienePedido(cve_req)) {
				       error= REQ_ERROR_TIENE_PEDIDO;
				       log.info("La Requisicion tiene pedido, no se puede Cancelar");
			       }
		       else{
		    	   /*comprobar que no esta agarrada de una Orden de pago*/
		    	   Map requisicion = getJdbcTemplate().queryForMap("SELECT NUM_REQ, CVE_CONTRATO, ID_PROYECTO, CLV_PARTID, PERIODO, FECHA, TIPO, STATUS, EJERCICIO, (SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ ) AS IMPORTE FROM SAM_REQUISIC WHERE CVE_REQ = ? ", new Object []{cve_req});
		    	    Integer cont = getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM  SAM_ORD_PAGO WHERE CVE_REQ = ? AND STATUS NOT IN (- 1, - 2, 3, 4, 5) ", new Object[]{cve_req});
		    	    
		    	    boolean cancelarOtOs = getPrivilegioEn(cve_pers, 112);
	            	//si no es requisicion entra
	            	if(!requisicion.get("TIPO").equals("1")&&!requisicion.get("TIPO").equals("7")){
	            		if(!cancelarOtOs)
	            			throw new RuntimeException("No cuenta con privilegios suficientes para realizar esta operación (Cancelar OT/OS)");
	            	}
			
		    	    
		    	    if(cont>0)
		    	    	throw new RuntimeException("La Requisicion "+requisicion.get("NUM_REQ").toString()+" no se puede cancelar por que esta relacionada a una Orden de Pago");
		    	    else{
		    	    	//realiza esta accion cuando tiene un contrato
		    	    	if(requisicion.get("CVE_CONTRATO")!=null){
		    	    		String tipo_doc = "";
							if (requisicion.get("TIPO").equals("1")) tipo_doc = "REQ";
							if (requisicion.get("TIPO").equals("2")) tipo_doc = "OS";
							if (requisicion.get("TIPO").equals("3")) tipo_doc = "OT";
							if (requisicion.get("TIPO").equals("4")) tipo_doc = "OT";
							if (requisicion.get("TIPO").equals("5")) tipo_doc = "OS";
							if (requisicion.get("TIPO").equals("6")) tipo_doc = "OS";
							if (requisicion.get("TIPO").equals("7")) tipo_doc = "REQ";
							if (requisicion.get("TIPO").equals("8")) tipo_doc = "OS";
		    	    		getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_DOC = ? AND TIPO_DOC = ? AND TIPO_MOV =?", new Object[]{cve_req, tipo_doc,"LIBERACION"});
		    	    	}
		    	    	
			        	descomprometerReq(cve_req, Integer.parseInt(requisicion.get("EJERCICIO").toString()), requisicion.get("ID_PROYECTO").toString(),requisicion.get("CLV_PARTID").toString());
			        	
			        	Date fecha_cancelacion = new Date();
			        	if(requisicion.get("STATUS").toString().equals("0"))
			        		getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS=? WHERE CVE_REQ = ?", new Object []{REQ_STATUS_CANCELADA, cve_req});
			        	else
			        		getJdbcTemplate().update("UPDATE SAM_REQUISIC SET STATUS=?, FECHA_CANCELADO=? WHERE CVE_REQ = ?", new Object []{REQ_STATUS_CANCELADA, fecha_cancelacion, cve_req});
						
						getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET STATUS=? WHERE CVE_REQ = ?", new Object[]{REQ_MOVTO_RECHAZADO, cve_req});
						//Guardar en bitacora
						_guardarEnBitacoraReq(cve_req, gatewayBitacora.CANCELAR_REQUISICION, cve_pers);
						log.info("Requisicion Cancelada");
						error="";
						
						//SE AGREGA PROCEDIMIENTO ALMACENADO DE PEREDO PARA CONTABILIZAR CANCELADO EN REQUISICIONES 02/AGO/2013
						//StoreProcedureRequisiciones sp = new StoreProcedureRequisiciones(getJdbcTemplate().getDataSource());
						//Map result = sp.execute(cve_req, 0);
					   //TERMINA PROCEDIMIENT0
		    	    }
					
		       	  }
                } 
            });
          return error;
           } catch (DataAccessException e) {            
           	 log.info("Las requisiciones no se han podido cancelar");
           	 error = e.getMessage();
                throw new RuntimeException(e.getMessage(),e);                
           }	
           
	}
	
	
	/*Metodo light para obtener la requisicion*/
	public List <Map> getLightRequisicion(Long cve_req){
		return this.getJdbcTemplate().queryForList("SELECT " +
						"R.CVE_REQ, " +
						"R.NUM_REQ, " +
						"R.CVE_CONTRATO, " +
						"R.CVE_VALE, " +
						"C.N_PROGRAMA,"+
						"R.EJERCICIO, " +
						"R.ID_PROYECTO, " +
						"R.CLV_PARTID, " +
						"R.ID_DEPENDENCIA, " +
						"R.CVE_PERS, " +
						"B.NCOMERCIA AS PROVEEDOR, " +
						"convert(varchar(10),R.FECHA,103) AS FECHA, " +
						"R.TIPO,  " +
						"R.FECHA_CAP, " +
						"R.OBSERVA, " +
						"R.STATUS, " +
						"R.PERIODO, " +
						"R.COMPROMETE, " +
						"R.ANUALIZADA,  " +
						"OT.CLV_BENEFI, " +
						"OT.COSTO_TOTAL, " +
						"OT.AREA, " +
						"OT.VEHICULO, " +
						"OT.MARCA, " +
						"OT.MODELO, " +
						"OT.PLACAS, " +
						"OT.NUM_INV, " +
						"OT.COLOR, " +
						"OT.USUARIO, " +
						"OT.CVE_CONCURSO, " +
						//"SC.NUM_CONTRATO, " +
						//"(CASE (R.PROYECTO) WHEN NULL THEN SC.PROYECTO ELSE R.PROYECTO END) AS CPROYECTO, " +
						//"(CASE (R.CLV_PARTID) WHEN NULL THEN SC.CLV_PARTID ELSE R.CLV_PARTID END) AS CCLV_PARTID, " +
						//"(CASE (OT.CLV_BENEFI) WHEN NULL THEN SC.CLV_BENEFI ELSE OT.CLV_BENEFI END) AS CCLV_BENEFI, " +
						"C.ID_RECURSO " +
					"FROM SAM_REQUISIC AS R " +
						"LEFT JOIN SAM_ORDEN_TRAB AS OT ON(OT.CVE_REQ=R.CVE_REQ) " +
						"LEFT JOIN CAT_BENEFI AS B ON (B.CLV_BENEFI = OT.CLV_BENEFI) " +
						//"LEFT JOIN SAM_CONTRATOS AS SC ON (SC.CVE_CONTRATO = R.CVE_CONTRATO) " +
						"LEFT JOIN CAT_BENEFI AS A ON (A.CLV_BENEFI = OT.CLV_BENEFI ) " +
						"LEFT JOIN CEDULA_TEC  AS C ON (C.ID_PROYECTO = R.ID_PROYECTO) " +
					"WHERE R.CVE_REQ  = ?", new Object[]{cve_req});
	}
	
	/*Metodo para comprobar la existencia de una orden de trabajo*/
	private boolean buscarOrdenTrabajo(Long cve_req){
		return (this.getJdbcTemplate().queryForInt("SELECT COUNT(CVE_REQ) AS N FROM SAM_REQUISIC WHERE CVE_REQ =?",new Object[]{cve_req}))!=0;
	}
	
	private void _guardarEnBitacoraReq(Long cve_req, int tipo_mov, int cve_pers){
		//Guardar en bitacora
		Date fecha = new Date();
		Map requisicion = this.getJdbcTemplate().queryForMap("SELECT NUM_REQ, ID_PROYECTO, CLV_PARTID, PERIODO, FECHA, TIPO, STATUS, EJERCICIO, ISNULL((SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ ),0.00) AS IMPORTE FROM SAM_REQUISIC WHERE CVE_REQ = ? ", new Object []{cve_req});
		gatewayBitacora.guardarBitacora(tipo_mov, Integer.parseInt(requisicion.get("EJERCICIO").toString()), cve_pers, cve_req, requisicion.get("NUM_REQ").toString(), "REQ", (Date)requisicion.get("FECHA"), requisicion.get("ID_PROYECTO").toString(), requisicion.get("CLV_PARTID").toString(), "", Double.parseDouble(requisicion.get("IMPORTE").toString()));	
	}
			
	/*Metodo para finiquitar un elemento de la requisicion*/
	public void cambiaStatusMovimientoRequisicion(Long cve_req, Long id_req_movto, int status){
		this.getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET STATUS=? WHERE ID_REQ_MOVTO = ? AND CVE_REQ =?", new Object []{status, id_req_movto, cve_req});
	}
	
	/*Metodo para finiquitar varios elementos de la requisicion*/
	public void cambiaStatusMovimientoRequisicion(Long cve_req, int status){
		this.getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET STATUS=? WHERE CVE_REQ = ?", new Object []{status, cve_req});
	}
	
	/*Metodo para que verifica y finiquita una requisicion si sus elementos ya estan pedidos totalmente en requisicion Anualizada tipo(7)*/
	public boolean comprobarParaFiniquitar(Long cve_req){
		boolean finiquitar = true;
		//obtener los lostes de la requisicion
		List <Map> lotes = this.gatewayMovimientosRequisicion.getConceptos(cve_req);
		for(Map row: lotes){
			if( ((Short)row.get("STATUS")).intValue() ==this.REQ_MOVTO_SOLICITADO  ) {
				finiquitar = false;
			}
		}
		return  finiquitar;
	}
	
	/*Metodo para que verificar y cerrar una requisicion si sus elementos ya estan pedidos totalmente apartados en pedidos en requisicion Anualizada tipo(7)*/
	//1: Si la requisicion no tiene mas pedidos cerrar
	//2: Si la requisicion ya tiene otro pedido entonces en proceso
	public void cambiarStatusRequisicion(Long cve_req){
		boolean cerrar = true;
		//obtener los lostes de la requisicion
		List <Map> lotes = this.gatewayMovimientosRequisicion.getConceptos(cve_req);
		//recorrer lotes para comprobar
		for(Map row: lotes){
			if(Double.parseDouble(row.get("CANTIDAD").toString()) > 0 && row.get("STATUS").equals("0")) {
				//no cerrar aun
				cerrar = false;
			}
		}
		int mas = this.tieneMasPedidos(cve_req);
		//cerrar si corresponde o poner en proceso
		if(cerrar==true&&mas>=1) 
			this.getJdbcTemplate().update("UPDATE REQUISIC SET STATUS = ?, COMPROMETE =? WHERE CVE_REQ = ?", new Object []{this.REQ_STATUS_EN_PROCESO, 1, cve_req});
		else {
				this.getJdbcTemplate().update("UPDATE REQUISIC SET STATUS = ?, COMPROMETE =?  WHERE CVE_REQ = ?", new Object []{this.REQ_STATUS_PENDIENTE, 1, cve_req});
		}
	}
	
	//Metodo para determinar si existen mas pedidos en una requisicion
	private int tieneMasPedidos(Long cve_req){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM PEDIDOS_EX WHERE STATUS IN(0,1) AND CVE_REQ = ?", new Object[]{cve_req});
	}
	
	public double getCompromisoHastaMes (Long idRequisicion, int mes ){
		Map Req = this.getRequisicion(idRequisicion);
		//se agrego el filtro de periodo al tipo de requisicion 7: Israel de la Cruz, 10/09/2016
		if(Req.get("TIPO").toString().equals("7"))
			return (Double)this.getJdbcTemplate().queryForObject("SELECT SUM(MONTO) FROM VT_COMPROMISOS WHERE TIPO_DOC = 'REQ' AND CVE_DOC = ? AND CONSULTA ='PRECOMPROMETIDO' AND PERIODO =?",new Object[]{idRequisicion, mes},Double.class);
		else 
			return (Double)this.getJdbcTemplate().queryForObject("select sum(  CASE TIPO  WHEN 'COMPROMISO' THEN importe else (importe*-1)  END )  from  SAM_COMP_REQUISIC WHERE CVE_REQ=? AND PERIODO<=? ",new Object[]{idRequisicion,mes},Double.class);
	}
	
	public double getCompromisoMes (Long idRequisicion, int mes ){
		return (Double)this.getJdbcTemplate().queryForObject("select sum(  CASE TIPO  WHEN 'COMPROMISO' THEN importe else (importe*-1)  END )  from  SAM_COMP_REQUISIC WHERE CVE_REQ=? AND PERIODO=? ",new Object[]{idRequisicion,mes},Double.class);
	}
	
	public List getCompromisoPorMesReq(Long idRequisicion, int mes ){
		return this.getJdbcTemplate().queryForList("select sum(  CASE TIPO  WHEN 'COMPROMISO' THEN importe else (importe*-1)  END ) AS  IMPORTE , PERIODO from  SAM_COMP_REQUISIC WHERE CVE_REQ=? AND PERIODO<=? group by periodo ",new Object[]{idRequisicion,mes});
	}
	
	//Metodo para comprobar si el documento fue cerrado alguna vez
	public boolean comprobarCerradoBitacora(Long cve_req){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_BITACORA WHERE TIPO_DOC = 'REQ' AND CVE_DOC = ? AND ID_MOVTO = ?", new Object[]{cve_req, this.gatewayBitacora.CERRAR_REQUISICION})>0;
	}
	
	//Metodo para guardar lotes de una req a un pedido especificado
	public String enviarLotesPedido(Long id_movto, Long cve_ped, int ejercicio, int cve_pers){
		try{
			int num_lote = this.getJdbcTemplate().queryForInt("SELECT ISNULL(MAX(PED_CONS),0)+1 AS N FROM SAM_PED_MOVTOS WHERE CVE_PED = ?", new Object[]{cve_ped});
			Map lotes = gatewayMovimientosRequisicion.getConcepto(id_movto);
			Map req = this.getJdbcTemplate().queryForMap("SELECT NUM_REQ, FECHA, ID_PROYECTO, CLV_PARTID FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{lotes.get("CVE_REQ").toString()});
			//guardar un nuevo lote de pedido apartir de la movto de la req
			this.getJdbcTemplate().update("INSERT INTO SAM_PED_MOVTOS(ID_REQ_MOVTO, CVE_PED, PED_CONS, DESCRIP, CANTIDAD, PRECIO_UNI, STATUS) VALUES(?,?,?,?,?,?,?)", 
								new Object[]{id_movto, cve_ped, num_lote, lotes.get("NOTAS").toString(), lotes.get("CANTIDAD"), lotes.get("PRECIO_EST"), 0});
			//cambiar los estatus de los lotes en la requisicion
			this.getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET STATUS = ? WHERE ID_REQ_MOVTO = ?", new Object[]{this.REQ_MOVTO_SOLICITADO,id_movto});
			//Guardar registro en bitacora
			gatewayBitacora.guardarBitacora(gatewayBitacora.MOVIO_LOTE_PED, ejercicio, cve_pers, Long.parseLong(lotes.get("CVE_REQ").toString()), req.get("NUM_REQ").toString(), "REQ", (Date) req.get("FECHA"), req.get("ID_PROYECTO").toString(), req.get("CLV_PARTID").toString(), null, 0D);
			return "";
		}
		catch (DataAccessException e) {            
          	 log.info("Error encontrado al enviar lotes de la requisicion al pedido");
          	 throw new RuntimeException(e.getMessage(),e);                
          }	
	}
	
	public Map getImporteDisponibleRequisicion(Long cve_req){
		return this.getJdbcTemplate().queryForMap("SELECT CVE_REQ, NUM_REQ, (SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = SAM_REQUISIC.CVE_REQ) AS TOTAL, ((SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = SAM_REQUISIC.CVE_REQ) - (SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_ORD_PAGO WHERE CVE_REQ = SAM_REQUISIC.CVE_REQ AND STATUS IN (0,1))) AS DISPONIBLE FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{cve_req});
	}
	
	public Map getFechaPeriodoRequisicion(Long cve_req){
		return this.getJdbcTemplate().queryForMap("SELECT SAM_REQUISIC.NUM_REQ, CONVERT(varchar(10),SAM_REQUISIC.FECHA,103) AS FECHA, SAM_REQUISIC.PERIODO FROM SAM_REQUISIC WHERE SAM_REQUISIC.CVE_REQ = ?",new Object[]{cve_req});
	}
	
	public boolean cambiarFechaPeriodo(final Long cve_req, final String fechaNueva, final int periodo, final int cve_pers, final int ejercicio){
		try {   
			
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	Date fecha = formatoFecha(fechaNueva);
	            	/*Graba en bitacora el cambio*/
	            	Map req = getJdbcTemplate().queryForMap("SELECT NUM_REQ, CONVERT(varchar(10),FECHA,103) AS FECHA, ID_PROYECTO, CLV_PARTID, (SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ) AS TOTAL FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{cve_req.toString()});
	            	gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_REQUISICION, ejercicio, cve_pers, Long.parseLong(cve_req.toString()), req.get("NUM_REQ").toString(), "REQ", fecha, req.get("ID_PROYECTO").toString(), req.get("CLV_PARTID").toString(), "Cambió la fecha de la Requisición de "+req.get("FECHA").toString()+" a "+fechaNueva, Double.parseDouble(req.get("TOTAL").toString()));
	            	/*Realiza el cambio*/
	            	getJdbcTemplate().update("UPDATE SAM_REQUISIC SET FECHA = ?, PERIODO =? WHERE CVE_REQ = ?", new Object[]{fecha, periodo, cve_req});
	            	getJdbcTemplate().update("UPDATE SAM_COMP_REQUISIC SET PERIODO = ? WHERE CVE_REQ = ? AND CVE_PED IS NULL", new Object[]{periodo, cve_req});
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, puede que la fecha este escrita incorrectamente, verifique nuevamente el formato y vuelta a intentar esta operación",e);
	   }	
	  
	}
	
	public boolean moverRequisiciones(final Long[] cve_req, final int cve_pers_fuente, final int cve_pers_dest, final int ejercicio){
		try {   
			
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	for(Long id: cve_req){
	            		/*Graba en bitacora el cambio*/
		            	Map req = getJdbcTemplate().queryForMap("SELECT NUM_REQ, CONVERT(varchar(10),FECHA,103) AS FECHA, ID_PROYECTO, CLV_PARTID, (SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ) AS TOTAL FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{id});
		            	Map persona = getJdbcTemplate().queryForMap("SELECT TOP 1 (SELECT '('+US1.LOGIN+') '+NOMBRE+' '+APE_PAT+' '+APE_MAT FROM PERSONAS INNER JOIN USUARIOS_EX AS US1 ON (US1.CVE_PERS = PERSONAS.CVE_PERS) WHERE US1.CVE_PERS = ?) AS PERSONA1, (SELECT '('+US1.LOGIN+') '+NOMBRE+' '+APE_PAT+' '+APE_MAT FROM PERSONAS INNER JOIN USUARIOS_EX AS US1 ON (US1.CVE_PERS = PERSONAS.CVE_PERS) WHERE US1.CVE_PERS = ?) AS PERSONA2 FROM USUARIOS_EX", new Object[]{cve_pers_fuente, cve_pers_dest});
		            	gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_REQUISICION, ejercicio, cve_pers_fuente, id, req.get("NUM_REQ").toString(), "REQ", formatoFecha(req.get("FECHA").toString()), req.get("ID_PROYECTO").toString(), req.get("CLV_PARTID").toString(), "Cambio de usuario en el documento de: "+persona.get("PERSONA1").toString()+" a: "+persona.get("PERSONA2").toString(), Double.parseDouble(req.get("TOTAL").toString()));
		            	/*Realiza el cambio*/
		            	getJdbcTemplate().update("UPDATE SAM_REQUISIC SET CVE_PERS = ?, ID_DEPENDENCIA =(SELECT ID_DEPENDENCIA FROM TRABAJADOR WHERE TRABAJADOR.CVE_PERS = ?) WHERE CVE_REQ = ?", new Object[]{cve_pers_dest, cve_pers_dest, id});
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
			return this.getJdbcTemplate().queryForMap("SELECT R.NUM_REQ, CLV_BENEFI, (SELECT CAT_BENEFI.NCOMERCIA FROM CAT_BENEFI WHERE CAT_BENEFI.CLV_BENEFI = SAM_ORDEN_TRAB.CLV_BENEFI) AS BENEFICIARIO FROM SAM_ORDEN_TRAB INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = SAM_ORDEN_TRAB.CVE_REQ) WHERE SAM_ORDEN_TRAB.CVE_REQ = ?", new Object[]{cve_doc});
		}
		catch (DataAccessException e) {                               
	       return null;
	   }
	}
	
	public boolean cambiarBeneficiario(final Long cve_req, final String clv_benefi, final int ejercicio, final int cve_pers){
		try {   
			
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	
	            		/*Comprobar algunos datos antes*/
		            	Map req = getJdbcTemplate().queryForMap("SELECT NUM_REQ, CONVERT(varchar(10),FECHA,103) AS FECHA, ID_PROYECTO, CLV_PARTID, (SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ ) AS TOTAL, OT.CLV_BENEFI, C.NCOMERCIA, (SELECT COUNT(*) AS N FROM SAM_ORDEN_TRAB O WHERE O.CVE_REQ = SAM_REQUISIC.CVE_REQ) AS CONT "+  
																	"FROM SAM_REQUISIC "+ 
																		"LEFT JOIN SAM_ORDEN_TRAB AS OT ON (OT.CVE_REQ = SAM_REQUISIC.CVE_REQ) "+ 
																		"LEFT JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = OT.CLV_BENEFI) "+
																		"WHERE SAM_REQUISIC.CVE_REQ = ?", new Object[]{cve_req});
		            	Map ben = getJdbcTemplate().queryForMap("SELECT NCOMERCIA FROM CAT_BENEFI WHERE CLV_BENEFI =?", new Object[]{clv_benefi});
		            	if(Integer.parseInt(req.get("CONT").toString())<=0)
		            		throw new RuntimeException("La operación ha fallado, el cambio de beneficiario no se ha podido realizar, intente editando el documento directamente");
		            	else
		            	{
		            		String texto ="";
		            		if(req.get("CLV_BENEFI")!=null)
		            			texto = "Cambió el beneficiario CLV_BENEFI: ['"+req.get("CLV_BENEFI").toString()+"' "+req.get("NCOMERCIA").toString()+"] a: ['"+clv_benefi+"' "+ben.get("NCOMERCIA").toString()+"]";
		            		else
		            			texto = "Cambió el beneficiario CLV_BENEFI: ['' ] a: ['"+clv_benefi+"' "+ben.get("NCOMERCIA").toString()+"]";
		            		
			            	/*Graba en bitacora el cambio*/
			            	gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_REQUISICION, ejercicio, cve_pers, cve_req, req.get("NUM_REQ").toString(), "REQ", formatoFecha(req.get("FECHA").toString()), req.get("ID_PROYECTO").toString(), req.get("CLV_PARTID").toString(), texto, Double.parseDouble(req.get("TOTAL").toString()));
			            	/*Realiza el cambio*/
			            	getJdbcTemplate().update("UPDATE SAM_ORDEN_TRAB SET CLV_BENEFI = ? WHERE CVE_REQ = ?", new Object[]{clv_benefi, cve_req});
		            	}
		            	
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, el cambio de beneficiario no se ha podido realizar, intente editando el documento directamente",e);
	   }	
	  
	}
	
	public boolean reenumerarLotesDesde(final Long cve_req, final int num, final int cve_pers, final int ejercicio){
		try {   
					
					this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
			            @Override
			            protected void   doInTransactionWithoutResult(TransactionStatus status) {
				            	int n = num;
				            	List <Map> lotes = gatewayMovimientosRequisicion.getConceptos(cve_req);
				            	for(Map lote : lotes){
				            			getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET REQ_CONS = ? WHERE ID_REQ_MOVTO = ?", new Object[]{n, lote.get("ID_REQ_MOVTO")});
				            			n +=1;
				            	}
				            	
				            	Map req = getJdbcTemplate().queryForMap("SELECT NUM_REQ, CONVERT(varchar(10),FECHA,103) AS FECHA, ID_PROYECTO, CLV_PARTID, (SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ) AS TOTAL FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{cve_req});
				            	/*Graba en bitacora el cambio*/
				            	gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_REQUISICION, ejercicio, cve_pers, cve_req, req.get("NUM_REQ").toString(), "REQ", formatoFecha(req.get("FECHA").toString()), req.get("ID_PROYECTO").toString(), req.get("CLV_PARTID").toString(), "Reenumeracion de consecutivos en lotes", Double.parseDouble(req.get("TOTAL").toString()));
				          
			            	}
			            	 
			         });
					return true;
				}
				catch (DataAccessException e) {                               
			        throw new RuntimeException("La operación ha fallado, no se pudieron reenumerar los lotes",e);
			   }	
		}
	
	public Map getFechaIngreso(Long cve_req){
		return this.getJdbcTemplate().queryForMap("SELECT SAM_REQUISIC.NUM_REQ, CONVERT(varchar(10),SAM_REQUISIC.FECHA_INGRESO,103) AS FECHA FROM SAM_REQUISIC WHERE SAM_REQUISIC.CVE_REQ = ?",new Object[]{cve_req});
	}
	
	public boolean cambiarFechaIngreso(final Long cve_req, final String fechaNueva, final int cve_pers, final int ejercicio){
		try {   
			if(fechaNueva=="") throw new RuntimeException("La operación ha fallado, puede que la fecha este escrita incorrectamente, verifique nuevamente el formato y vuelta a intentar esta operación");
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	Date fecha = formatoFecha(fechaNueva);
	            	/*Graba en bitacora el cambio*/
	            	Map req = getJdbcTemplate().queryForMap("SELECT NUM_REQ, CONVERT(varchar(10),FECHA,103) AS FECHA, ID_PROYECTO, CLV_PARTID, (SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ) AS TOTAL FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{cve_req.toString()});
	            	gatewayBitacora.guardarBitacora(gatewayBitacora.CAMBIOS_REQUISICION, ejercicio, cve_pers, Long.parseLong(cve_req.toString()), req.get("NUM_REQ").toString(), "REQ", fecha, req.get("ID_PROYECTO").toString(), req.get("CLV_PARTID").toString(), "Cambió la fecha de la Requisición de "+req.get("FECHA").toString()+" a "+fechaNueva, Double.parseDouble(req.get("TOTAL").toString()));
	            	/*Realiza el cambio*/
	            	getJdbcTemplate().update("UPDATE SAM_REQUISIC SET FECHA_INGRESO = ? WHERE CVE_REQ = ?", new Object[]{fecha, cve_req});
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, puede que la fecha este escrita incorrectamente, verifique nuevamente el formato y vuelta a intentar esta operación",e);
	   }	
	  
	}
	
	public Map getReembolsoRequisiciones(Long cve_req){
		return this.getJdbcTemplate().queryForMap("SELECT NUM_REQ, ISNULL((SELECT MONTO FROM VT_COMPROMISOS WHERE TIPO_DOC ='REQ' AND CVE_DOC = SAM_REQUISIC.CVE_REQ)-ISNULL(REEMBOLSO,0),0) AS REEMBOLSO_LIQ, ISNULL(REEMBOLSO,0) AS REEMBOLSO FROM SAM_REQUISIC WHERE CVE_REQ =?", new Object[]{cve_req});
	}
	
	public void guardarReembolsoRequisicion(Long cve_ped, Double monto){
		if(monto==0){
			Map m = this.getReembolsoRequisiciones(cve_ped);
			monto = Double.parseDouble(m.get("REEMBOLSO_LIQ").toString());
		}	
		this.getJdbcTemplate().update("UPDATE SAM_REQUISIC SET REEMBOLSO =? WHERE CVE_REQ =?", new Object[]{monto, cve_ped});
	}
	
	public void quitarReembolso(Long cve_ped){
		this.getJdbcTemplate().update("UPDATE SAM_REQUISIC SET REEMBOLSO = ? WHERE CVE_REQ =?", new Object[]{null,cve_ped});
	}
	
	public BigDecimal getCompromisoFuturo(Long cve_req, int periodoActual)
	{
		BigDecimal Total = new BigDecimal(0);
		List<Map> lista = this.getJdbcTemplate().queryForList("SELECT VT_COMPROMISOS.* from VT_COMPROMISOS INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = VT_COMPROMISOS.CVE_DOC) WHERE SAM_REQUISIC.TIPO = 8 and VT_COMPROMISOS.TIPO_DOC in ('O.S') AND VT_COMPROMISOS.CVE_DOC =? AND VT_COMPROMISOS.MONTO>0 AND VT_COMPROMISOS.PERIODO>?", new Object[]{cve_req, periodoActual});
		for(Map row : lista)
		{
			Total = Total.add((BigDecimal) row.get("MONTO"));
		}
		return Total;
	}
	
}
