/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 2.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;


public class GatewayOrdenDePagos extends BaseGateway  {
		
	private static Logger log = Logger.getLogger(GatewayOrdenDePagos.class.getName());
	
	@Autowired
	public GatewayBitacora gatewayBitacora;
	
	@Autowired
	GatewayMeses gatewayMeses;
	@Autowired
	GatewayDetallesOrdenDePagos gatewayDetallesOrdenDePagos;
	@Autowired
	GatewayProyectoPartidas gatewayProyectoPartidas;
	
	@Autowired 
	GatewayFacturas gatewayFacturas;
	
	
	public GatewayOrdenDePagos() {
	
	}

  public static final int OP_ESTADO_BORRADA = -2;
  public static final int OP_ESTADO_EN_EDICION = -1;
  public static final int OP_ESTADO_NUEVA = 0;
  public static final int OP_ESTADO_EN_PROCESO = 1;
  public static final int OP_ESTADO_A_REVISION = 2;
  public static final int OP_ESTADO_CONDICIONADA = 3;
  public static final int OP_ESTADO_CANCELADA = 4;
  public static final int OP_ESTADO_AUTORIZADA = 5;
  public static final int OP_ESTADO_PAGADA = 6;	
	
  
public  Long actualizarPrincipalOrden(Long cveOp , int ejercicio, int tipo,Date fecha,String  pedido,String  cveBeneficiario,int  cvePersona,String  cveParbit, String reembolsoFondo,String  concurso,String  nota,int  status,Integer  cveRequisicion, double importeIva, int cveUnidad, Integer periodo, int tipoGasto, Integer idGrupo, Long cve_contrato  ){
	
	
	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	/*if(!getPrivilegioEn(cvePersona, 114)){
		throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	}*/
	
	reembolsoFondo = reembolsoFondo.equals("S") ? reembolsoFondo: "N";  
  if (cveOp == null){
	  //String clave_parbit = (String)this.getJdbcTemplate().queryForObject("SELECT CAT_UNIADM.UNIADM FROM USUARIOS_EX INNER JOIN TRABAJADOR ON (TRABAJADOR.CVE_PERS = USUARIOS_EX.CVE_PERS) INNER JOIN CAT_UNIADM ON (CAT_UNIADM.CLV_UNIADM=TRABAJADOR.ORG_ID) WHERE USUARIOS_EX.CVE_PERS = ?", new Object[]{cvePersona}, String.class);
	  cveOp= insertaOrden(ejercicio, tipo,fecha,pedido,cveBeneficiario,cvePersona,reembolsoFondo,concurso,nota,status,cveRequisicion, importeIva,cveUnidad,periodo,tipoGasto,idGrupo, cvePersona, cve_contrato );
  }
  else
	  actualizarOrden(cveOp ,tipo, fecha, pedido,cveBeneficiario,cvePersona, reembolsoFondo,concurso,nota,status,cveRequisicion, importeIva,periodo, tipoGasto,ejercicio, cve_contrato );
  
  return  cveOp; 
}

//-------------------------------------Metodo de generar la Orden de pago----------------------------------

public Long insertaOrden( int ejercicio, int tipo,Date  fecha,String  pedido,String  cveBeneficiario,int  cvePersona, String reembolsoFondo,String  concurso,String  nota,int  status,Integer  cveRequisicion, double importeIva, int cveUnidad , Integer periodo, int tipoGasto, Integer idGrupo, int cve_pers, Long cve_contrato   ){

	try{
		
		Boolean tieneIva = importeIva != 0 ;
		this.getJdbcTemplate().update("INSERT INTO SAM_ORD_PAGO (EJERCICIO, TIPO, FECHA,  CLV_BENEFI, CVE_PERS,  REEMBOLSOF, CONCURSO, NOTA,  STATUS, IVA, IMPORTE_IVA, ID_DEPENDENCIA, PERIODO, ID_RECURSO, ID_GRUPO, CVE_CONTRATO) " +
				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				, new Object[]{ejercicio,tipo,fecha,cveBeneficiario,cvePersona,reembolsoFondo,concurso,nota,status,tieneIva,importeIva,cveUnidad,periodo,tipoGasto,idGrupo, (cve_contrato==0 ? null:cve_contrato)});
		
		Long cveOp =getNumeroOrdenNuevo(ejercicio);
		String folio=rellenarCeros(cveOp.toString(),6);
		//guarda en la bitacora
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_NUEVA_ORDEN, ejercicio, cve_pers, cveOp, folio, "OP", fecha, null, null, null, null);
		return cveOp;
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
		return null;
	}
}

public void actualizarOrden(Long cveOp , int tipo,Date  fecha,String  pedido,String  cveBeneficiario,int  cvePersona, String reembolsoFondo,String  concurso,String  nota,int  status,Integer  cveRequisicion, Double importeIva , Integer periodo , int tipoGasto, int ejercicio, Long cve_contrato ){
	try{
		
		Boolean tieneIva =importeIva!=0;
		String folio=rellenarCeros(cveOp.toString(),6);
		this.getJdbcTemplate().update("UPDATE SAM_ORD_PAGO  set  TIPO=?, FECHA=?,  CLV_BENEFI=?, REEMBOLSOF=?, CONCURSO=?,  NOTA=?, STATUS=?,  IVA=?, IMPORTE_IVA=?, PERIODO=?, ID_RECURSO=?, CVE_CONTRATO=? where CVE_OP=? "
				, new Object[]{tipo,fecha,cveBeneficiario,reembolsoFondo,concurso,nota,status,tieneIva,importeIva,periodo, tipoGasto, cve_contrato, cveOp});
		Map op = getOp(cveOp);
		//guarda en la bitacora
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_ACTUALIZAR, ejercicio, cvePersona, cveOp, folio, "OP", fecha, (String) op.get("PROYECTO"), (String) op.get("CLV_PARTID"), null, Double.parseDouble(op.get("IMPORTE").toString()));
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}
}

public Map getOp(Long cve_op){
	try{
		return this.getJdbcTemplate().queryForMap("SELECT TOP 1 *, (SELECT ISNULL(IMPORTE,0) FROM SAM_ORD_PAGO WHERE CVE_OP = SAM_MOV_OP.CVE_OP) AS IMPORTE FROM SAM_MOV_OP WHERE CVE_OP = ?", new Object[]{cve_op});
	}
	catch ( DataAccessException e) {
		Map op = new HashMap();
		op.put("PROYECTO", null);
		op.put("CLV_PARTID", null);
		op.put("IMPORTE", 0.0);
		log.info(e.getMessage());
		return op;
	}
	
}


public void actualizarOrdenStatus(Long cveOp ,int  status, String status_actual, String motivo){
	if(status==4) 
	{
		if(status_actual.equals("-1"))
			this.getJdbcTemplate().update("UPDATE SAM_ORD_PAGO  SET STATUS=?, MOTIVO_CANCELADO=? where CVE_OP=?", new Object[]{status, motivo, cveOp});
		else
			this.getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET FECHA_CANCELADO=?, STATUS=?, MOTIVO_CANCELADO=? where CVE_OP=?", new Object[]{new Date(),status, motivo, cveOp});
	}
	else
		this.getJdbcTemplate().update("UPDATE SAM_ORD_PAGO  SET STATUS=? where CVE_OP=?", new Object[]{status,cveOp});

}

public Map getOrden(Long idOrden) {	   
	   return this.getJdbcTemplate().queryForMap("SELECT A.CVE_OP, A.EJERCICIO, A.NUM_OP, A.TIPO, CONVERT(varchar(10), A.FECHA, 103) AS FECHA, CONVERT(varchar(10), A.FECHA_CIERRE, 103) AS FECHA_CIERRE, A.FECHA_CIERRE AS FECHA_CIERRE2, A.FECHA_CIERRE AS FECHA_CIERRE2, A.FECHA AS V_FECHA, A.CLV_BENEFI, A.CVE_PERS, A.CVE_PED, A.CVE_REQ, A.ID_DEPENDENCIA, " + 
					"CASE MONTH(A.FECHA) " +
					"WHEN '1' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Enero de '+CONVERT(varchar(4),YEAR(A.FECHA))" + 
					"	WHEN '2' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Febrero de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"	WHEN '3' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Marzo de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"	WHEN '4' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Abril de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"	WHEN '5' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Mayo de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"	WHEN '6' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Junio de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"	WHEN '7' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Julio de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"	WHEN '8' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Agosto de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"	WHEN '9' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Septiembre de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"	WHEN '10' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Octubre de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"	WHEN '11' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Noviembre de '+CONVERT(varchar(4),YEAR(A.FECHA))  " +
					"	WHEN '12' THEN CONVERT(varchar(2),DAY(A.FECHA)) + ' de Diciembre de '+CONVERT(varchar(4),YEAR(A.FECHA)) " +
					"END AS FECHA_TEXT, " +
					"CASE (A.PERIODO)"+
					"	WHEN 1 THEN 'ENERO' "+
					"	WHEN 2 THEN 'FEBRERO' "+
					"	WHEN 3 THEN 'MARZO' "+
					"	WHEN 4 THEN 'ABRIL' "+
					"	WHEN 5 THEN 'MAYO' "+
					"	WHEN 6 THEN 'JUNIO' "+
					"	WHEN 7 THEN 'JULIO' "+
					"	WHEN 8 THEN 'AGOSTO' "+
					"	WHEN 9 THEN 'SEPTIEMBRE' "+
					"	WHEN 10 THEN 'OCTUBRE' "+
					"	WHEN 11 THEN 'NOVIEMBRE' "+
					"	WHEN 12 THEN 'DICIEMBRE' "+
					"END AS PERIODO_TEXT, " +
					"CT.NUM_CONTRATO, "+
					"CT.CVE_CONTRATO, "+
					
					/*"(CASE (R.PROYECTO) WHEN NULL THEN CT.ID_RECURSO ELSE (SELECT CC.ID_RECURSO FROM CEDULA_TEC AS CC WHERE CC.PROYECTO = R.PROYECTO) END) AS ID_RECURSO2,"+
					"(CASE (R.PROYECTO) WHEN NULL THEN CT.PROYECTO ELSE R.PROYECTO END) AS CPROYECTO, " +
					*/
					//"(CASE (R.CLV_PARTID) WHEN NULL THEN CT.CLV_PARTID ELSE R.CLV_PARTID END) AS CCLV_PARTID, " +
					"(SELECT DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE CAT_DEPENDENCIAS.ID = A.ID_DEPENDENCIA) AS UNIDAD_ELABORA,"+
					"(SELECT CAT_DEPENDENCIAS.CLV_UNIADM FROM USUARIOS_EX INNER JOIN TRABAJADOR ON (TRABAJADOR.CVE_PERS = USUARIOS_EX.CVE_PERS) INNER JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID=TRABAJADOR.ID_DEPENDENCIA) WHERE USUARIOS_EX.CVE_PERS = A.CVE_PERS) AS ELABORA, " +
					"C.CLV_FUENTE, C.CLV_RECURSO, (SELECT ISNULL(SUM(IMPORTE),0) AS IMPORTE_V FROM COMP_VALES WHERE COMP_VALES.CVE_OP = A.CVE_OP) AS DESCUENTO_VALES," + 
               " A.ID_RECURSO, A.REEMBOLSOF, A.CONCURSO, ISNULL(A.IMPORTE,0) AS IMPORTE, A.RETENCION, A.IMP_NETO, A.FE_PAGO, A.NOTA, A.STATUS, A.cve_req, A.ID_POLIZA_CH,  " +
			   " A.IVA, A.IMPORTE_IVA, A.PERIODO, B.NCOMERCIA, C.RECURSO, A.ID_GRUPO, " +
			   " isnull((select DISTINCT 'SI' from COMP_VALES  where  CVE_OP= A.CVE_OP),'NO') COMPRUEBAVALE, "+
			   " ROUND(isnull((select sum (MONTO) from SAM_MOV_OP where  CVE_OP= A.CVE_OP),0),2) TOTAL, "+
               " isnull((select sum (IMPORTE) from MOV_RETENC where CVE_OP=a.CVE_OP),0) RETENCIONES "+
			   " FROM SAM_ORD_PAGO AS A INNER JOIN CAT_BENEFI AS B ON A.CLV_BENEFI = B.CLV_BENEFI INNER JOIN " +
			   " CAT_RECURSO  C ON A.ID_RECURSO = C.ID " +
			   " LEFT JOIN SAM_CONTRATOS AS CT ON (CT.CVE_CONTRATO = A.CVE_CONTRATO) "+
			   //" LEFT JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = CT.CVE_REQ) " +
			   //" LEFT JOIN SAM_ORDEN_TRAB AS OT ON(OT.CVE_REQ = R.CVE_REQ) "+
			   //" LEFT JOIN CAT_BENEFI AS B2 ON (B2.CLV_BENEFI = (CASE (OT.CLV_BENEFI) WHEN NULL THEN CT.CLV_BENEFI ELSE OT.CLV_BENEFI END)) "+
			   " where  A.CVE_OP=? ", new Object[]{idOrden});
}

/*Revision del tipo de datos del listado de ordenes de pago*/
public List getOrdenesTipo( String idUnidad, Integer cve_pers, Integer Ejercicio, Integer status) {
		System.out.println("Dependencia: "+idUnidad );
		System.out.println("Persona: "+cve_pers );
		System.out.println("Ejercicio: "+Ejercicio );
		System.out.println("Status OP: "+status );
		
	   return this.getJdbcTemplate().queryForList("SELECT A.CVE_OP, A.EJERCICIO, A.NUM_OP, A.TIPO, convert(varchar(10),A.FECHA,103) FECHA, A.CVE_PED, A.CLV_BENEFI, A.CVE_PERS, A.ID_DEPENDENCIA, A.REEMBOLSOF, A.CONCURSO, A.IMPORTE,"+ 
                   " A.RETENCION, A.IMP_NETO, A.FE_PAGO, A.NOTA, A.STATUS,A.PERIODO,C.DESCRIPCION_ESTATUS, A.CVE_REQ, A.ID_POLIZA_CH, A.IVA, A.IMPORTE_IVA ,b.NCOMERCIA , D.DESCRIPCION AS TIPO_DOC  FROM SAM_ORD_PAGO A ,cat_benefi  B ,SAM_ESTATUS_OP C ,SAM_TIPO_ORDEN_PAGO D where a.clv_benefi=b.clv_benefi AND  A.STATUS=C.ID_ESTATUS  AND  A.TIPO=D.ID_TIPO_ORDEN_PAGO AND A.ID_DEPENDENCIA=? AND A.CVE_PERS = ? AND A.ejercicio=? and A.STATUS=? order by a.CVE_OP", new Object[]{idUnidad,cve_pers, Ejercicio,status});
}


public List<Map> getListaDeOrdenesPorEjemplo(String unidad, String  estatus , Date fInicial, Date fFinal ,String clv_benefi, Integer ejercicio, String tipoGasto, Integer idUsuario, String verUnidad, String tipo, String numop, String numped, boolean privilegio, String capitulo){
	Map parametros =  new HashMap<String,Object>();
	/*Asignacion de parametros*/
	parametros.put("unidad", unidad);
	parametros.put("fechaInicial", fInicial);
	parametros.put("fechaFinal", fFinal);
	parametros.put("ejercicio", ejercicio);
	parametros.put("tipoGasto", tipoGasto);
	parametros.put("clv_benefi", clv_benefi);
	parametros.put("idUsuario", idUsuario);	
	parametros.put("tipo", tipo);	
	parametros.put("capitulo", capitulo);	
	
	//determinar si solo puede filtrar capitulo 5000 en privilegios
	//boolean cap5000 = this.getPrivilegioEn(idUsuario, 113);
	
	String sql = "SELECT  DEP.CLV_UNIADM, "+
						"A.CVE_OP,  "+
						"OP.FECHA_PAGO,  "+
						"A.ID_DEPENDENCIA,  "+
						"CASE ISNULL(Convert(varchar,OP.FECHA_PAGO),'0') WHEN '0' THEN C.DESCRIPCION_ESTATUS ELSE 'PAGADO' END AS DESCRIPCION_ESTATUS, "+
						"A.STATUS, "+
						"A.EJERCICIO,  "+
						"A.NUM_OP,  "+
						"A.TIPO, CONVERT(varchar(10), A.FECHA, 103) AS FECHA,  "+
						"A.CVE_PED,  "+
						"A.CLV_BENEFI, "+ 
						"A.CVE_PERS,  "+
						"A.ID_RECURSO,  "+
						"A.REEMBOLSOF,  "+
						"A.CONCURSO,   "+
						"isnull((select sum (MONTO) from SAM_MOV_OP where  CVE_OP= A.CVE_OP),0)   "+
				        "IMPORTE,  "+
						"A.FE_PAGO, "+
						"A.NOTA,  "+
						"A.PERIODO,    "+
				        "A.cve_req,  "+
						"A.ID_POLIZA_CH,  "+
						"A.IVA,  "+
						"A.IMPORTE_IVA,  "+
						"B.NCOMERCIA,  "+
						"D.DESCRIPCION AS TIPO_DOC  "+
				"FROM  dbo.SAM_ORD_PAGO AS A  "+
					"LEFT JOIN ORDENDPAGO AS OP ON (OP.ID_OP = A.CVE_OP)   "+
					"INNER JOIN dbo.CAT_BENEFI AS B ON A.CLV_BENEFI = B.CLV_BENEFI  "+
					"LEFT JOIN dbo.SAM_ESTATUS_OP AS C ON A.STATUS = C.ID_ESTATUS  "+
					"INNER JOIN dbo.SAM_TIPO_ORDEN_PAGO AS D ON A.TIPO = D.ID_TIPO_ORDEN_PAGO  "+
				    "INNER JOIN CAT_DEPENDENCIAS DEP ON (DEP.ID = A.ID_DEPENDENCIA) "+
        (!capitulo.toString().equals("")&&!capitulo.toString().equals("0") ?  
        		" WHERE A.CVE_OP IN (SELECT M.CVE_OP FROM SAM_MOV_OP AS M INNER JOIN CAT_PARTID AS CP ON (CP.CLV_PARTID = M.CLV_PARTID AND CP.CLV_CAPITU =:capitulo)) " 
        			: 
        		" WHERE      A.ejercicio= "+ejercicio);

	
	if (verUnidad==null&&!privilegio)
		sql+= " AND A.CVE_PERS='"+idUsuario+"' ";
	if(verUnidad!=null&&!privilegio)
		sql+= " AND (A.CVE_PERS=:idUsuario OR A.ID_DEPENDENCIA="+unidad+") ";
	
	if (verUnidad==null&&privilegio&&unidad.equals("0"))
		sql+= " ";
	
	if (verUnidad==null&&privilegio&&!unidad.equals("0"))
		sql+= "AND A.ID_DEPENDENCIA ='"+unidad+"'";
	if(verUnidad!=null&&privilegio)
		sql+= " AND (A.CVE_PERS=:idUsuario OR A.ID_DEPENDENCIA="+unidad+") ";

		
	if(numop!=null&&!numop.equals("")){
		sql+= " AND A.NUM_OP LIKE '%"+numop+"%'";
	}
	
	if(numped!=null&&!numped.equals(""))
		sql+= " AND A.CVE_PED LIKE '%"+numped+"%'";
	
	if(tipo!=null)
		if(!tipo.equals("0")&&!tipo.equals("-1"))
			sql+= " AND A.TIPO =:tipo ";

	if(estatus.equals("7"))
		sql += " AND OP.FECHA_PAGO IS NOT NULL ";
	else if(estatus.equals("6,7"))
		{
			estatus = estatus.replace('7', '6');
			sql += " AND a.STATUS in ("+estatus+")   AND (OP.FECHA_PAGO IS NULL OR OP.FECHA_PAGO IS NOT NULL) ";
		}
	else{
		
			if(estatus.contains("7"))
				{
					estatus = estatus.replace('7', '6');
					sql += " AND a.STATUS in ("+estatus+")   AND (/*OP.FECHA_PAGO IS NULL OR */OP.FECHA_PAGO IS NOT NULL) ";
				}
			else			
				if (estatus!=null && !estatus.equals(""))			
					sql += " AND a.STATUS in ("+estatus+") AND  OP.FECHA_PAGO IS NULL ";
	}

	if (fInicial != null && fFinal != null ) 
		sql += " and convert(datetime,convert(varchar(10), a.fecha ,103),103) between :fechaInicial and :fechaFinal ";
	
	if(tipoGasto!=null&&!tipoGasto.equals("")&&!tipoGasto.equals("0")) 
		sql += " and A.ID_RECURSO=:tipoGasto ";
	
	if(clv_benefi!=null&&!clv_benefi.equals("")) 
		sql += " and A.CLV_BENEFI=:clv_benefi ";
	
	return this.getNamedJdbcTemplate().queryForList(sql+"order by A.NUM_OP DESC",parametros);
}

public void actualizaEstatusOrden(Long idOrden ,int estatus){
	String cierre="";
	if (estatus==0)
	  cierre=", a.IMPORTE = isnull((select sum(MONTO) from SAM_MOV_OP  where CVE_OP=a.CVE_OP  ),0) ,  a.RETENCION =isnull( (select sum (IMPORTE) from mov_retenc where CVE_OP=a.CVE_OP )  ,0)  ";
	getJdbcTemplate().update("update  a set  a.STATUS =? "+cierre+", a.FECHA_CIERRE =?  from SAM_ORD_PAGO a where a.CVE_OP=?  ",new Object[]{estatus,new Date(), idOrden});
}

private Long getNumeroOrdenNuevo(Integer ejercicio ){
	return this.getJdbcTemplate().queryForLong("select MAX(CVE_OP) as numero  from SAM_ORD_PAGO where ejercicio= ? ", new Object[]{ejercicio});	
}


//--------------------------------Actualizar retenciones----------------------------------------------
public  void actualizarPrincipalRetencion(Integer idRetencion,String  retencion,Double importeRetencion,String cveParbit,Long idOrden, int ejercicio, int cve_pers){
	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	/*if(getPrivilegioEn(cve_pers, 114)){
		throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	}*/
	int tipoRetencion = this.getJdbcTemplate().queryForInt("select count(*) from CAT_RETENC  where CLV_RETENC=?  and tipo='CR' ", new Object[]{retencion});
	if (tipoRetencion==1)
		importeRetencion=importeRetencion*-1;
	
  if (idRetencion == null) {
	  idRetencion=this.getJdbcTemplate().queryForInt("select isnull(max(ret_cons),0) from MOV_RETENC where CVE_OP=? ", new Object[]{idOrden})+1;
	  insertaRetencion(idRetencion,retencion,importeRetencion,cveParbit,idOrden, ejercicio, cve_pers);	  
  }
  else
	  actualizarRetencion(idRetencion,retencion,importeRetencion, idOrden, ejercicio, cve_pers);
}

public void insertaRetencion(Integer idRetencion,String  retencion,Double importeRetencion,String cveParbit, Long idOrden, int ejercicio, int cve_pers ){
	try{
		String folio=rellenarCeros(idOrden.toString(),6);
		this.getJdbcTemplate().update("insert into MOV_RETENC (CVE_OP,RET_CONS,CLV_RETENC, IMPORTE, PAGADO) " +
				"VALUES (?,?,?,?,?)"
				, new Object[]{idOrden,idRetencion,retencion,importeRetencion,0});
		//guarda en la bitacora
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_AGREGO_RETENCIONES, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Clv_Retenc: "+retencion+ " Cons: "+idRetencion, importeRetencion);
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}

}

public void actualizarRetencion(Integer idRetencion,String  retencion,Double importeRetencion, Long idOrden, int ejercicio, int cve_pers ){
	try{
		String folio=rellenarCeros(idOrden.toString(),6);
		this.getJdbcTemplate().update("update MOV_RETENC  set  CLV_RETENC=?, IMPORTE=?  where CVE_OP=? and RET_CONS=? "
			, new Object[]{retencion,importeRetencion,idOrden,idRetencion});
		//guarda en la bitacora
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_ACTUALIZA_RETENCION, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Clv_Retenc: "+retencion+ " Cons: "+idRetencion, importeRetencion);
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}
}

/*public List getRetencionOrden(Integer idOrden, Integer idRetencion) {	   
	   return this.getJdbcTemplate().queryForList("select M.CVE_OP, " +
               "M.RET_CONS,M.CLV_RETENC,R.RETENCION,M.IMPORTE, " +
               "M.PAGADO,M.CLV_PARBIT,M.CVE_OPPAGO,R.TIPO " +
               "from MOV_RETENC M, CAT_RETENC R " +
               "where R.CLV_RETENC=M.CLV_RETENC and M.CVE_OP=? and M.RET_CONS =?", new Object[]{idOrden,idRetencion});
}*/

public List getTodasRetencionesOrdenes(Integer idOrden) {	   
	   return this.getJdbcTemplate().queryForList("select M.CVE_OP, " +
            "M.RET_CONS,M.CLV_RETENC,R.RETENCION,M.IMPORTE, " +
            "M.PAGADO,M.CLV_PARBIT,M.CVE_OPPAGO,R.TIPO " +
            "from MOV_RETENC M, CAT_RETENC R " +
            "where R.CLV_RETENC=M.CLV_RETENC and M.CVE_OP= ? ", new Object[]{idOrden});
}

public List getTodasTipoRetencionesTodas() {	   
	   return this.getJdbcTemplate().queryForList("select CLV_RETENC,RETENCION,PORCENTAJE,TIPO,CVE_NOMINA,ID from CAT_RETENC ORDER BY RETENCION ASC");
}

public void eliminarRetencion(Long idOrden, Integer idRetencion, int ejercicio, int cve_pers ){
try{
	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	/*if(getPrivilegioEn(cve_pers, 114)){
		throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	}*/
	
	String folio=rellenarCeros(idOrden.toString(),6);
	this.getJdbcTemplate().update("delete from MOV_RETENC where CVE_OP= ? and RET_CONS=? ", new Object[]{idOrden,idRetencion});
	//guarda en la bitacora
	gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_ELIMINA_RETENCION, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Cons: "+idRetencion, 0D);
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}
}


/*Documentos*/

/*public List getTipoDocumentosTodos() {	   
	   return this.getJdbcTemplate().queryForList("select ID_TIPO_ORDEN_PAGO, DESCRIPCION  from SAM_TIPO_ORDEN_PAGO order by DESCRIPCION ");
}*/

public List getDocumentosOrdenes( Long idOrden ) {
    //return this.getJdbcTemplate().queryForList("select a.CVE_OP,a.ANX_CONS,a.T_DOCTO,a.NUMERO,a.NOTAS , b.DESCR, a.FILENAME, a.FILETYPE, a.FILELENGTH, a.FILEPATH from SAM_OP_ANEXOS a , tipodoc_op b where a.t_docto=b.t_docto and cve_op=? ", new Object  []{idOrden});
	return this.getJdbcTemplate().queryForList("select	a.CVE_OP, "+
												"		a.ANX_CONS,"+
												"		a.T_DOCTO,"+
												"		a.NUMERO,"+
												"		a.NOTAS , "+
												"		b.DESCR, "+
												"		a.FILENAME,"+ 
												"		a.FILETYPE, "+
												"		a.FILELENGTH, "+
												"		a.FILEPATH "+
												" from SAM_OP_ANEXOS a , tipodoc_op b "+
												" where a.t_docto = b.t_docto and cve_op=? "+
												" UNION ALL "+
												" SELECT  DISTINCT MOV_OP.CVE_OP,"+
												"		0 AS ANX_CONS,"+
												"		'CON' AS T_DOCTO,"+
												"		CON.NUM_CONTRATO AS NUMERO,"+
												"		'SE ANEXA ARCHIVO DE CONTRATO' AS NOTAS,"+
												"		'Contrato' AS DESCR,"+
												"		'[' + CONVERT(VARCHAR,ARCH_CON.ID_ARCHIVO) + '] ' + ARCH_CON.NOMBRE AS FILENAME, "+
												"		ARCH_CON.EXT AS FILETYPE, "+
												"		ARCH_CON.TAMANO AS FILELENGTH,"+
												"		'../' + ARCH_CON.RUTA AS FILEPATH "+
												" FROM SAM_CONTRATOS_ARCHIVOS AS ARCH_CON "+
												" INNER JOIN SAM_CONTRATOS AS CON ON (CON.CVE_CONTRATO = ARCH_CON.CVE_CONTRATO) "+
												" INNER JOIN SAM_FACTURAS AS FAC ON (FAC.CVE_CONTRATO = ARCH_CON.CVE_CONTRATO) "+
												" INNER JOIN SAM_MOV_OP AS MOV_OP ON (MOV_OP.CVE_FACTURA = FAC.CVE_FACTURA) "+
												" WHERE MOV_OP.CVE_OP =?", new Object  []{idOrden,idOrden});
}

public  void actualizarPrincipalDocumento(Integer idDocumento,String tipoMovDoc,String numeroDoc,String notaDoc,Long idOrden, int ejercicio, int cve_pers){	
  if (idDocumento == null || idDocumento == 0) {
	  idDocumento=this.getJdbcTemplate().queryForInt("select isnull(max(ANX_CONS),0) from SAM_OP_ANEXOS where CVE_OP=? ", new Object[]{idOrden})+1;
	  insertaDocumento(idDocumento,tipoMovDoc,numeroDoc,notaDoc, idOrden, ejercicio, cve_pers);	  
  }
  else
	  actualizarDocumento(idDocumento,tipoMovDoc,numeroDoc,notaDoc, idOrden, ejercicio, cve_pers);
}

//Inserta el anexo a la OP desde el DEVENGADO
public void insertaDocumento(Integer idDocumento,String tipoMovDoc,String numeroDoc,String notaDoc,Long idOrden, int ejercicio, int cve_pers ){
	try{
		String folio=rellenarCeros(idOrden.toString(),6);
		this.getJdbcTemplate().update("insert into SAM_OP_ANEXOS (CVE_OP,ANX_CONS,T_DOCTO,NUMERO,NOTAS  ) " +
				"VALUES (?,?,?,?,?)"
				, new Object[]{idOrden,idDocumento,tipoMovDoc,numeroDoc,notaDoc,});
		//guarda en la bitacora
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_AGREGO_ANEXO, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Cons: "+ idDocumento +" Tipo: "+tipoMovDoc+" Num_Doc: "+numeroDoc +" Nota: "+notaDoc, 0D);
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}
}

public void actualizarDocumento(Integer idDocumento,String tipoMovDoc,String numeroDoc,String notaDoc,Long idOrden, int ejercicio, int cve_pers ){
	try{
		String folio=rellenarCeros(idOrden.toString(),6);
		this.getJdbcTemplate().update("UPDATE SAM_OP_ANEXOS  set  T_DOCTO=?,NUMERO=?,NOTAS=?   where CVE_OP=? and ANX_CONS=? "
				, new Object[]{tipoMovDoc,numeroDoc,notaDoc,idOrden,idDocumento});
			//guarda en la bitacora
			gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_ACTUALIZA_ANEXO, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Tipo: "+tipoMovDoc+" Num_Doc: "+numeroDoc +" Nota: "+notaDoc, 0D);
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}
}

public void eliminarDocumento(Long idOrden, Integer idDocumento, int ejercicio, int cve_pers, String rutaReal  ){
	try{
		Map archivo = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_OP_ANEXOS WHERE CVE_OP= ? and ANX_CONS=? ", new Object[]{idOrden,idDocumento});
		String folio=rellenarCeros(idOrden.toString(),6);
		/*eliminar el archivo si existe*/
		if(archivo.get("FILENAME")!=null){
			File fichero = new File(rutaReal+"/sam/ordenesdepago/anexos/"+archivo.get("FILENAME").toString());
			if (fichero.delete()){
				 System.out.println("El fichero ha sido borrado satisfactoriamente");
			}
			else
				System.out.println("El fichero no puede ser borrado");
		}
		
		this.getJdbcTemplate().update("delete from SAM_OP_ANEXOS where CVE_OP= ? and ANX_CONS=? ", new Object[]{idOrden,idDocumento});
		
		//guarda en la bitacora
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_ELIMINA_ANEXO, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Cons: "+idDocumento, 0D);
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}
}


/*Vales*/


public List getValesOrdenes( Long idOrden ) {
    return this.getJdbcTemplate().queryForList("SELECT  CV.ID_VALE,"+
														"CV.CVE_VALE, "+
														"V.NUM_VALE,"+
														"CV.CONS_VALE,"+
														"CV.CVE_OP,"+
														"CV.TIPO,"+
														"CV.ID_PROYECTO,"+
														"CV.CLV_PARTID,"+
														"C.N_PROGRAMA,"+
														"CV.IMP_ANTERIOR,"+
														"CV.IMP_PENDIENTE,"+
														"CV.IMPORTE,"+
														"CV.FECHA, "+
														"(SELECT top 1 MES FROM CONCEP_VALE WHERE CONCEP_VALE.CVE_VALE = CV.CVE_VALE) AS MES "+
												"FROM COMP_VALES AS CV "+
														"INNER JOIN SAM_VALES_EX AS V ON (V.CVE_VALE = CV.CVE_VALE) "+
														"LEFT JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = CV.ID_PROYECTO) "+
												"WHERE CV.CVE_OP = ? ORDER BY CV.ID_VALE ASC", new Object  []{idOrden});
}	


public void aperturarOrdenes(final List<Long> cvOrdenes, final int ejercicio, final int cve_pers){
	try {                 
        this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	
            	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
        		/*if(getPrivilegioEn(cve_pers, 114)){
        			throw new RuntimeException("No cuenta por los privilegios suficientes para realiar esta operación, solo lectura");
        		}*/
            	
/*Valida aqui si el documento esta en el periodo actual para permitir aperturar*/
		      	
            	
        		
            	for(Long cveOrden:cvOrdenes){
            		BigDecimal importe = new BigDecimal("0.0");

            		Map orden =getOrden(cveOrden);
    		      	
    		      	Date fechaCierre = new Date();
    		  		fechaCierre = (Date) orden.get("FECHA_CIERRE2");
    		  		Calendar c1 = Calendar.getInstance();
    		  		if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1) && /*Super Privilegio de Aperturar*/getPrivilegioEn(cve_pers, 142)==false )
    		  		{
    		  			throw new RuntimeException("No se puede aperturar la Orden de Pago "+orden.get("NUM_OP").toString()+", el periodo del documento es diferente al actual, consulte a su administrador");
        		  	}
    		  		
            		int ejercicio =(Short)orden.get("EJERCICIO");
            		int mes = gatewayMeses.getMesActivo(ejercicio);
	                int tipo =(Integer )orden.get("TIPO");
	                List<Map> detalles = gatewayDetallesOrdenDePagos.getDetallesOrdenes(cveOrden);	                
	                for ( Map detalle: detalles ) {
	                	Long  proyecto = Long.parseLong(detalle.get("ID_PROYECTO").toString());
	                	String partida =detalle.get("CLV_PARTID").toString();
	                	importe = (BigDecimal)detalle.get("MONTO");		               
	            		if (tipo==2 ){
	            			//gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, mes, importe.doubleValue(), "COMPROMETER");
	            			//gatewayProyectoPartidas.comprometerPresupuesto(proyecto, partida, mes,importe.doubleValue(),"REDUCCION");
	            		}else if (tipo!=5 && tipo!=0){
	            			//gatewayProyectoPartidas.comprometerPresupuesto(proyecto, partida, mes,importe.doubleValue(),"REDUCCION");
	            		}            		
	                }
            		actualizarOrdenStatus(cveOrden ,OP_ESTADO_EN_EDICION, orden.get("STATUS").toString(), "");
            		/*por si hay algo en contratos*/
            		//getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_DOC = ? AND TIPO_DOC = ? AND TIPO_MOV =?", new Object[]{cveOrden, "OP","LIBERACION"});
            		/*por si hay vales*/
            		//getJdbcTemplate().update("DELETE FROM SAM_COMP_VALES WHERE CVE_DOC = ? AND TIPO_DOC=? AND TIPO_MOV=?", new Object[]{cveOrden, "OP", "LIBERACION"});
           
            		//guarda en la bitacora
            		Map pre = getProyectoPartidaOP(cveOrden);
            		String folio=rellenarCeros(cveOrden.toString(),6);
            		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_APERTURAR, ejercicio, cve_pers, cveOrden, folio, "OP", (Date) orden.get("V_FECHA"), pre.get("ID_PROYECTO").toString(), pre.get("CLV_PARTID").toString(), null, importe.doubleValue());
            		//Revisar en los pedidos y requisiciones si existe contrato en los conceptos 25/ago/2011
            	            		
            	}
            } 
         });
       
        } catch (DataAccessException e) {                               
             throw new RuntimeException(e.getMessage(),e);
        }	               	
}
public Map getProyectoPartidaOP(Long cve_op){
	try{
		Map row = new HashMap();
		row.put("ID_PROYECTO", "0");
		row.put("CLV_PARTID", "0");
		if(this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_MOV_OP WHERE CVE_OP = ?", new Object[]{cve_op})>0){
			Map temp = this.getJdbcTemplate().queryForMap("SELECT TOP 1 *FROM SAM_MOV_OP WHERE CVE_OP = ?", new Object[]{cve_op});
			row.put("ID_PROYECTO", temp.get("ID_PROYECTO").toString());
			row.put("CLV_PARTID", temp.get("CLV_PARTID").toString());
		}
		return row;
	}
	catch (DataAccessException e) {            
        log.info("Error, Problema al obtener proyecto y partida para la bitacora");	                    
        throw new RuntimeException(e.getMessage(),e);
    }	
	
}

public List<Map> getListadoOrdenesPagoEjercer(int mes, int listado, int ejercicio){
	Map parametros =  new HashMap<String,Object>();
	/*Asignacion de parametros*/
	parametros.put("ejercicio", ejercicio);
	parametros.put("mes", mes);
	String where = "";
	
	switch(listado){
		case 1: where = "WHERE SAM_ORD_PAGO.STATUS = "+this.OP_ESTADO_NUEVA;
			break;
		case 2: where = "WHERE SAM_ORD_PAGO.STATUS = "+this.OP_ESTADO_PAGADA;
			break;
		case 3: where = "WHERE SAM_ORD_PAGO.STATUS IN ("+this.OP_ESTADO_NUEVA+","+this.OP_ESTADO_PAGADA+")";
			break;
	}
	
	String sql = "SELECT CVE_OP, NUM_OP, SAM_ORD_PAGO.TIPO, SAM_ORD_PAGO.STATUS, SAM_ORD_PAGO.NOTA, CAT_BENEFI.NCOMERCIA, convert(varchar(10), SAM_ORD_PAGO.FECHA ,103) FECHA, convert(varchar(10), ORDENDPAGO.FECHA_EJER ,103) FECHA_EJER, YEAR(SAM_ORD_PAGO.FECHA) AS ANIO, MONTH(SAM_ORD_PAGO.FECHA) AS MES, DAY(SAM_ORD_PAGO.FECHA) AS DIA, SAM_ORD_PAGO.IMPORTE FROM SAM_ORD_PAGO INNER JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI = SAM_ORD_PAGO.CLV_BENEFI) LEFT JOIN ORDENDPAGO ON (ORDENDPAGO.ID_OP = SAM_ORD_PAGO.CVE_OP) " + where;
	
	switch(listado){
	case 1: if(mes!=0) sql += " and (MONTH(SAM_ORD_PAGO.FECHA)=:mes)";
		break;
	case 2: if(mes!=0) sql += " and (MONTH(ORDENDPAGO.FECHA_EJER)=:mes)";
		break;
	case 3: if(mes!=0) sql += " and (MONTH(SAM_ORD_PAGO.FECHA)=:mes OR MONTH(ORDENDPAGO.FECHA_EJER)=:mes)";
		break;
	}

	sql += " ORDER BY CVE_OP ASC";
	
	return this.getNamedJdbcTemplate().queryForList(sql, parametros);	
}

public List<Map> getListadoOrdenesPagoDesejercer(int mes, int ejercicio){
	Map parametros =  new HashMap<String,Object>();
	/*Asignacion de parametros*/
	parametros.put("ejercicio", ejercicio);
	parametros.put("mes", mes);
	
	String sql = "SELECT SAM_ORD_PAGO.CVE_OP, SAM_ORD_PAGO.NUM_OP, SAM_ORD_PAGO.TIPO, SAM_ORD_PAGO.STATUS, SAM_ORD_PAGO.NOTA, CAT_BENEFI.NCOMERCIA, convert(varchar(10), ORDENDPAGO.FECHA ,103) FECHA, convert(varchar(10), ORDENDPAGO.FEINI_TSOR ,103) FEINI_TSOR, convert(varchar(10), ORDENDPAGO.FECHA_PAGO ,103) FECHA, ORDENDPAGO.FE_CANCELA_EJER, convert(varchar(10), ORDENDPAGO.FECHA_PAGO ,103) FECHA_PAGO, YEAR(ORDENDPAGO.FECHA) AS ANIO, MONTH(ORDENDPAGO.FECHA) AS MES, DAY(ORDENDPAGO.FECHA) AS DIA, ORDENDPAGO.IMPORTE FROM SAM_ORD_PAGO INNER JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI = SAM_ORD_PAGO.CLV_BENEFI) INNER JOIN ORDENDPAGO ON (ORDENDPAGO.ID_OP = SAM_ORD_PAGO.CVE_OP) ";
	if(mes!=0) sql += " and MONTH(ORDENDPAGO.FECHA)=:mes";
	sql += " ORDER BY SAM_ORD_PAGO.CVE_OP ASC";
	
	return this.getNamedJdbcTemplate().queryForList(sql, parametros);	
}

public List <Map> getMovimientosOPPresupuest(Long cve_op){
	List <Map> lista = this.getJdbcTemplate().queryForList("SELECT M.ID_PROYECTO, M.CLV_PARTID, SUM(M.MONTO) AS IMPORTE  FROM SAM_MOV_OP AS M "+
															"	INNER JOIN SAM_ORD_PAGO AS OP ON (M.CVE_OP = OP.CVE_OP) "+
															"WHERE M.CVE_OP = ? "+
															"GROUP BY M.ID_PROYECTO, M.CLV_PARTID", new Object[]{cve_op});
	List lista2 =  new ArrayList();
	
	for(Map row: lista){
		Map temp = this.getJdbcTemplate().queryForMap("SELECT TOP 1 C.N_PROGRAMA, SAM_ORD_PAGO.PERIODO, SAM_MOV_OP.CVE_OP, SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID, (?) AS MONTO, dbo.getDisponibleAlPeriodo(SAM_ORD_PAGO.PERIODO, SAM_ORD_PAGO.PERIODO, SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS ACTUAL, dbo.getComprometidoOP(SAM_ORD_PAGO.PERIODO, SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS COMPROMETIDO, dbo.getDevengado(SAM_ORD_PAGO.PERIODO, SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS DEVENGADO, dbo.getEjercidoAlPeriodoProyecto(SAM_ORD_PAGO.PERIODO,SAM_ORD_PAGO.PERIODO,SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS EJERCIDO, dbo.getDisponibleAlPeriodo(1,12,SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS ANUAL  FROM SAM_MOV_OP INNER JOIN SAM_ORD_PAGO ON (SAM_ORD_PAGO.CVE_OP = SAM_MOV_OP.CVE_OP) INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) WHERE SAM_MOV_OP.CVE_OP = ? AND SAM_MOV_OP.ID_PROYECTO =? AND SAM_MOV_OP.CLV_PARTID = ?", new Object []{row.get("IMPORTE"), cve_op, row.get("ID_PROYECTO"), row.get("CLV_PARTID")});
		lista2.add(temp);
	}
	
	return lista2;
	//return this.getJdbcTemplate().queryForList("SELECT C.N_PROGRAMA,SAM_ORD_PAGO.PERIODO, SAM_MOV_OP.*, dbo.getDisponibleAlPeriodo(SAM_ORD_PAGO.PERIODO, SAM_ORD_PAGO.PERIODO, SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS ACTUAL, dbo.getComprometidoOP(SAM_ORD_PAGO.PERIODO, SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS COMPROMETIDO, dbo.getDevengado(SAM_ORD_PAGO.PERIODO, SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS DEVENGADO, dbo.getEjercidoAlPeriodoProyecto(SAM_ORD_PAGO.PERIODO,SAM_ORD_PAGO.PERIODO,SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS EJERCIDO, dbo.getDisponibleAlPeriodo(1,12,SAM_MOV_OP.ID_PROYECTO, SAM_MOV_OP.CLV_PARTID) AS ANUAL  FROM SAM_MOV_OP INNER JOIN SAM_ORD_PAGO ON (SAM_ORD_PAGO.CVE_OP = SAM_MOV_OP.CVE_OP) INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) WHERE SAM_MOV_OP.CVE_OP = ?", new Object []{cve_op});
}

public List <Map> getMovimientosOP(Long cve_op){
	return this.getJdbcTemplate().queryForList("SELECT CEDULA_TEC.ID_DEPENDENCIA, CEDULA_TEC.N_PROGRAMA, SAM_MOV_OP.*, CAT_PARTID.CLV_CAPITU, CAT_PARTID.PARTIDA, CAT_DEPENDENCIAS.CLV_UNIADM FROM SAM_MOV_OP INNER JOIN CEDULA_TEC ON (CEDULA_TEC.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) INNER JOIN CAT_PARTID ON (CAT_PARTID.CLV_PARTID = SAM_MOV_OP.CLV_PARTID) INNER JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = CEDULA_TEC.ID_DEPENDENCIA) WHERE SAM_MOV_OP.CVE_OP = ? ORDER BY CEDULA_TEC.N_PROGRAMA, SAM_MOV_OP.CLV_PARTID ASC", new Object []{cve_op});
}

public void ejercerOrdenPagoFinal(Long cve_op, Date fecha_ejerce, int ejercicio, int cve_pers) throws ParseException{
		
		if(fecha_ejerce==null) fecha_ejerce = new Date();
		
		
		String[] months = {"Enero", "Febrero",
	            "Marzo", "Abril", "Mayo", "Junio", "Julio",
	            "Agosto", "Septiembre", "Octubre", "Noviembre",
	            "Diciembre"};
		//mes del ejercido
		String month = months[fecha_ejerce.getMonth()];
	    
		Date fecha = new Date();
		
		String cve_unidad = "";
		String uniAdm = "";
		String mes = "";
		String SQL = "";
		Integer i=0;
	
		//Cargar los datos de la orden de pago
		Map OrdenPago = new HashMap();
		OrdenPago = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_ORD_PAGO where CVE_OP = ?", new Object []{cve_op});
		
		//Validar si que la fecha de ejercido sea menor o igual a la fecha de cierre de la orden de pago
		//Agregado el 09 de abril de 2014 por Israel de la Cruz
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaCierreOP = formateador.parse(OrdenPago.get("FECHA").toString());
		Date fechaEjercer = formateador.parse(fecha_ejerce.toString());
		
		if(fecha_ejerce.before(fechaCierreOP) || fechaCierreOP == fecha_ejerce)
			throw new RuntimeException("No es posible ejercer la Orden de Pago: "+OrdenPago.get("NUM_OP").toString() + " ya que la fecha de ejercido es menor a la fecha de cierre o devengado de la OP.");
		
		//Termina validacion del ejercido
		
		//obtener los anexos de la OP
		List<Map> anexos = new ArrayList();
		anexos = this.getDocumentosOrdenes(Long.parseLong(cve_op.toString()));
		
		//obtener los vales
		Double total_vales = 0D;
		List<Map> vales = new ArrayList();
		vales = this.getValesOrdenes(cve_op);
		
		//Seccion movida aqui el 10/09/2016 por Israel, por instrucciones de Peredo
		String clv_part_vale = null;
		for(Map row: vales){
			if(row.get("CLV_PARTID")!=null) clv_part_vale = row.get("CLV_PARTID").toString();
			if(!this.comprobarValeSIAM(row)){
				log.info("Numero de vale existe en SIAM: " + row.get("NUM_VALE").toString());
				if(row.get("CLV_PARTID")!=null) clv_part_vale = row.get("CLV_PARTID").toString();
				//Inserta consecutivo de vales de la OP
				SQL = "insert into conc_vale(vale, nconc_vale, ID_PROYECTO, clv_partid, mes, importe, descontado) values(?,?,?,?,?,?,?)";
				this.getJdbcTemplate().update(SQL, new Object[]{row.get("NUM_VALE").toString(), this.rellenarCeros(row.get("CONS_VALE").toString(), 3), row.get("ID_PROYECTO"), clv_part_vale, months[Integer.parseInt(row.get("MES").toString())-1].toString().substring(0,3).toUpperCase(), row.get("IMPORTE").toString(), 0});
			} else 
				throw new RuntimeException("El numero de Vale: " + row.get("NUM_VALE").toString()+". No existe en SIAM, o la operacion ha fallado al tratar de guardar movimientos duplicados. Consulte a su administrador");
			
			//Inserta vales de Orden de pago
			SQL = "insert into op_vale(vale, nconc_vale, id_op, importe) values(?,?,?,?)";
			this.getJdbcTemplate().update(SQL, new Object[]{row.get("NUM_VALE").toString(), this.rellenarCeros(row.get("CONS_VALE").toString(), 3), OrdenPago.get("CVE_OP"), row.get("IMPORTE").toString()});
		}
		
		
		//Obtener el total del monto vales
		for(Map row: vales){
			total_vales += Double.parseDouble(row.get("IMPORTE").toString());
		}
		
		String notas = OrdenPago.get("NOTA").toString();
		if(notas.length()>299) notas = notas.substring(0, 299);
		
		/*SEGUN NUEVA BASE 2012 AQUI LAS SECCIONES COMENTADAS DEJARON DE FUNCIONAR PARA PEREDO 13/DIC/11 
		//insertar en la tabla de reqisicion el registro del ejercido de la OP
		this.getJdbcTemplate().update("Insert into reqisicion (reqisicion, tipo, fecha, notas, finiquitado) values(?, ?, ?, ?, ?)", new Object[]{OrdenPago.get("num_op").toString(), 1, fecha_ejerce, notas, "S"});
		
		//obtener los movimientos de la OP y crear la tabla temporal de datos*/
		//List<Map> mov = this.getMovimientosOP(cve_op);
		
		/*Insertar los consecutivos de requisicion de lado de peredo
		String nota = OrdenPago.get("nota").toString();
		if(nota.length()>240) nota = nota.substring(0, 239);
		*/
		String concurso = OrdenPago.get("CONCURSO").toString();
		if(concurso.length()>10) concurso = concurso.substring(0, 9);
		
		/*for(Map row: mov)
		{	i++;
			//Obten la clave de la unidad
			if(i==1) cve_unidad = row.get("ID_DEPENDENCIA").toString();
			
			uniAdm = row.get("CLV_UNIADM").toString();
			String folio = rellenarCeros(i.toString(),3);
			
			SQL = "insert into conc_req(reqisicion, tipo, nconcepto, proyecto, clv_partid, cantidad, descripcio, notas, precio_uni, mes) values(?,?,?,?,?,?,?,?,?,?)";
			this.getJdbcTemplate().update(SQL, new Object []{OrdenPago.get("num_op").toString(), "1", folio, row.get("PROYECTO").toString(), row.get("CLV_PARTID").toString(), "1", row.get("PARTIDA").toString(), nota, row.get("MONTO").toString(), month.toString().substring(0, 3).toUpperCase()});	
		}*/
		
		String mes_op = OrdenPago.get("FECHA").toString().substring(5, 7);
		String sMes = month.toString().substring(0, 3).toLowerCase();
		
		/*Inserta un detalle de OP como pedido de lado de peredo
		SQL = "insert into pedidos (pedido, tipo, mes, fecha, clv_benefi, importe, iva, total, notas, finiquitado) values(?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(SQL, new Object[]{OrdenPago.get("num_op").toString(), "1", rellenarCeros(mes_op,2), fecha_ejerce, OrdenPago.get("clv_benefi").toString(), OrdenPago.get("importe").toString(), 0, OrdenPago.get("importe").toString(), nota, "S"});
		*/
		Object poliza_ch = null; 
		Object idpagoelect = null;
		if(OrdenPago.get("ID_POLIZA_CH")!=null) poliza_ch = OrdenPago.get("ID_POLIZA_CH");
		if(OrdenPago.get("ID_PAGO_ELEC")!=null) idpagoelect = OrdenPago.get("ID_PAGO_ELEC");
		
		Double impNeto = Double.parseDouble(OrdenPago.get("IMPORTE").toString())-Double.parseDouble(OrdenPago.get("RETENCION").toString())-total_vales;
		//Guarda en la tabla de OP de lado de peredo
		//SQL = "insert into ordendpago (ordenpago, pedido, tipo, clv_uniadm, uniadm, poliza_ch, fecha, folio, reembolsof, clv_benefi, ID_RECURSO, retencion, concurso, importe, impneto, fecha_ejer, mes, iva, importe_iva) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQL = "INSERT INTO ORDENDPAGO(ID_OP, ID_RECURSO ,ID_DEPENDENCIA, CLV_BENEFI, FECHA, FECHA_EJER, REEMBOLSOF, ID_POLIZA_CH, ID_PAGO_ELEC, CONCURSO, IMPORTE, RETENCION, IMPNETO, CUOTA_ISSET, DESC_PART, DESCPART, NOTA, FEINI_TSOR, FECHA_PAGO, FE_CONPAS, FE_CONTAB, FE_EGRESOS, CHEQUE, MES, MES_PAGO, FE_PASIVO, FEINI_FINANZAS, IVA,IMPORTE_IVA,COMPROBADA) "+
				"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(SQL, new Object[]{cve_op, OrdenPago.get("ID_RECURSO"), OrdenPago.get("ID_DEPENDENCIA"), OrdenPago.get("CLV_BENEFI"), OrdenPago.get("FECHA"), (Date) fecha_ejerce, OrdenPago.get("REEMBOLSOF"), poliza_ch, idpagoelect, concurso, OrdenPago.get("IMPORTE"), OrdenPago.get("RETENCION"), impNeto, null, null, 0, OrdenPago.get("NOTA"), null, null, null, null, null, null, sMes.toUpperCase(), null, null, null, OrdenPago.get("IVA"), OrdenPago.get("IMPORTE_IVA"), null});
		//this.getJdbcTemplate().update(SQL, new Object[]{OrdenPago.get("num_op").toString(), OrdenPago.get("num_op").toString(), 1, cve_unidad, uniAdm, poliza_ch, OrdenPago.get("fecha"), OrdenPago.get("num_op").toString(), OrdenPago.get("reembolsof").toString(), OrdenPago.get("clv_benefi").toString(), OrdenPago.get("clv_parbit").toString(), OrdenPago.get("retencion").toString(), consurso, OrdenPago.get("importe").toString(), impNeto, (Date) fecha_ejerce, sMes.toUpperCase(), ((OrdenPago.get("iva").toString().equals("true")) ? "1":"0"), OrdenPago.get("importe_iva").toString()});
		
		/*Guarda las retenciones
		SQL = "insert into retenciones (ordenpago, n_retenci, clv_retenc, importe) select SAM_ORD_PAGO.num_op as ordenpago, cast(mov_retenc.ret_cons as varchar) n_retenc, mov_retenc.clv_retenc, mov_retenc.importe from mov_retenc inner join SAM_ORD_PAGO on (SAM_ORD_PAGO.cve_op = mov_retenc.cve_op) and SAM_ORD_PAGO.cve_op = ?";
		this.getJdbcTemplate().update(SQL, new Object []{OrdenPago.get("cve_op").toString()});
		
		//Inserta los Anexos de la OP
		for(Map row: anexos){
			SQL = "insert into op_anexos (ordenpago, anexo, tipo_docto, descrip) values(?,?,?,?)";
			this.getJdbcTemplate().update(SQL, new Object[]{OrdenPago.get("num_op").toString(), row.get("ANX_CONS").toString(), this.getTipoDocumentoOP(row.get("T_DOCTO").toString())+" "+row.get("NUMERO").toString(), row.get("NOTAS").toString()});
		}
		*/
		
		//Antes aqui estaba la seccion para escribir  vales en op_vale
		
		//Cambia el estatus de la orden de pago
		SQL = "update SAM_ORD_PAGO set status = ? where cve_op = ?";
		this.getJdbcTemplate().update(SQL, new Object[]{this.OP_ESTADO_PAGADA, Integer.parseInt(OrdenPago.get("cve_op").toString())});
		String folio=rellenarCeros(cve_op.toString(),6);
		
		//Comrpobamos si existen facturas en la Orden
		if(existenFacturas(cve_op)){
			Date fecha_finalizado = new Date();
			List<Map> conceptos = this.gatewayDetallesOrdenDePagos.getDetallesOrdenes(cve_op);
			for(Map item : conceptos){
				if(item.get("CVE_FACTURA")!=null)
				{
					this.getJdbcTemplate().update("UPDATE SAM_FACTURAS SET FECHA_FINALIZADO =?, MES_FINALIZADO=?, DIA_FINALIZADO =?, STATUS = ? WHERE CVE_FACTURA =?", new Object[]{fecha_finalizado, fecha_finalizado.getMonth()+1, fecha_finalizado.getDay(), 3, item.get("CVE_FACTURA") });
				}
			}
		}
		//Guardar la bitacora de la OP
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_EJERCER, ejercicio, cve_pers, cve_op, folio, "OP", (Date) OrdenPago.get("FECHA"), null, null, null, Double.parseDouble(OrdenPago.get("IMPORTE").toString()));
		//this.gatewayBitacora.guardarBitacora(OrdenPago.get("num_op").toString(), Long.parseLong(OrdenPago.get("cve_op").toString()), "OP", (Date)OrdenPago.get("fecha"), Double.parseDouble(OrdenPago.get("importe").toString()), "E", OrdenPago.get("clv_benefi").toString(), "NoIm", "NoIm", Integer.parseInt(OrdenPago.get("ejercicio").toString()), cve_pers, this.gatewayBitacora.OP_EJERCER);
		log.info("Orden de pago ejercida con éxito: "+OrdenPago.get("num_op").toString());
	}

	public boolean existenFacturas(Long cve_op){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS WHERE CVE_OP = ? AND STATUS IN (1, 3) ", new Object[]{cve_op})>0;
	}

	public boolean desejercerOrdenPago(final Long cve_op, final String texto, final int ejercicio, final int cve_pers){
		try {   
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {	    
	            	desejercerOrdenPagoFinal(cve_op, texto, ejercicio, cve_pers);
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {  
	        throw new RuntimeException(e.getMessage(),e);
	   }	               	
	  }
	
	public boolean cancelarEjercidoOrdenPago(final Long cve_op, final String texto, final int ejercicio, final int cve_pers){
		try {   
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	Date fecha_cancelado = new Date();
	            	Map OrdenPago = getJdbcTemplate().queryForMap("SELECT *, (SELECT COUNT(*) FROM CONTROL_PAGOS WHERE TIPO_DOC = 53 AND DOCUMENTO = ?)AS PAID FROM SAM_ORD_PAGO WHERE CVE_OP = ?", new Object []{cve_op, cve_op});
	            	//Comprobar que la Orden de pago no contenga ningun pago en CONTROL_PAGOS del Sr.Peredo de lado de Finanzas
	            	if(!OrdenPago.get("PAID").toString().equals("0"))
	            		throw new RuntimeException("No se puede Cancelar el Ejercido de la Orden de Pago: " + OrdenPago.get("NUM_OP").toString() + ", por que ya existen pagos realizados por Finanzas");
	            	
	            	//Poner fecha de cancelado a la Orden de Pago del lado de Peredo
	    			getJdbcTemplate().update("UPDATE ORDENDPAGO SET FE_CANCELA_EJER =? WHERE ID_OP = ?", new Object []{fecha_cancelado, cve_op});
	    			//Poner en Status CANCELADA la Orden de Pago
	    			getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET STATUS=?, MOTIVO_CANCELADO=? WHERE CVE_OP = ?", new Object []{7, texto, cve_op});
	    			//Poner las facturas nuevamente en devengado
	    			//Obtener lo movimientos de la OP
	    			List<Map> mov = getMovimientosOP(cve_op);
	    			//Comrpobamos si existen facturas en la Orden para devolver el Devengado
	    			if(existenFacturas(cve_op)){
	    				Date fecha_finalizado = new Date();
	    				List<Map> conceptos = gatewayDetallesOrdenDePagos.getDetallesOrdenes(cve_op);
	    				for(Map item : conceptos){
	    					if(item.get("CVE_FACTURA")!=null)
	    					{
	    						getJdbcTemplate().update("UPDATE SAM_FACTURAS SET FECHA_FINALIZADO =?, MES_FINALIZADO=?, DIA_FINALIZADO =?, STATUS = ? WHERE CVE_FACTURA =?", new Object[]{null,null, null, 1, item.get("CVE_FACTURA") });
	    					}
	    				}
	    			}
	    			//Guardar en bitacora
	    			gatewayBitacora.guardarBitacora(gatewayBitacora.OP_DESEJERCER, ejercicio, cve_pers, cve_op,OrdenPago.get("NUM_OP").toString(), "OP", (Date) OrdenPago.get("FECHA"), null, null, null, Double.parseDouble(OrdenPago.get("IMPORTE").toString()));
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {  
	        throw new RuntimeException(e.getMessage(),e);
	   }	               	
	  }
	
		public void desejercerOrdenPagoFinal(Long cve_op, String texto, int ejercicio, int cve_pers)
		{
			String SQL = "";
			String[] months = {"Enero", "Febrero",
		            "Marzo", "Abril", "Mayo", "Junio", "Julio",
		            "Agosto", "Septiembre", "Octubre", "Noviembre",
		            "Deciembre"};
			//Cargar los datos de la orden de pago
			Map OrdenPago = new HashMap();
			OrdenPago = this.getJdbcTemplate().queryForMap("SELECT * FROM SAM_ORD_PAGO where CVE_OP = ?", new Object []{cve_op});
			String poliza_ch = (String) this.getJdbcTemplate().queryForObject("SELECT ID_POLIZA_CH FROM ORDENDPAGO WHERE ID_OP=?", new Object[]{OrdenPago.get("CVE_OP").toString()}, String.class);
			//Obtiene la fecha del ejercicio de la OP
			SQL = "select year(fecha_ejer) as Anio, month(fecha_ejer) as Mes, day(fecha_ejer) as Dia from ordendpago where ID_OP = ?";
			Map data = this.getJdbcTemplate().queryForMap(SQL, new Object []{OrdenPago.get("CVE_OP").toString()});
			
			//mes del ejercido
			String month = months[Integer.parseInt(data.get("Mes").toString())-1].substring(0, 3);
			
			//Obtener lo movimientos de la OP
			List<Map> mov = this.getMovimientosOP(cve_op);
			
			/*Elimina los anexos de la OP
			SQL = "delete from op_anexos where ordenpago=?";
			this.getJdbcTemplate().update(SQL, new Object []{OrdenPago.get("num_op").toString()});
			*/
			//Elimina los vales de la OP de lado de peredo
			SQL = "delete from op_vale where ID_OP = ?";
			this.getJdbcTemplate().update(SQL, new Object []{OrdenPago.get("CVE_OP").toString()});
			
			/*Elimina las retenciones
			SQL = "delete from retenciones where ordenpago = ?";
			this.getJdbcTemplate().update(SQL, new Object []{OrdenPago.get("num_op").toString()});
			*/
			
			//Elimina de la tabla Ordendpago de lado de peredo
			SQL = "delete from ordendpago where ID_OP = ?";
			this.getJdbcTemplate().update(SQL, new Object []{OrdenPago.get("CVE_OP").toString()});
			
			/*Elimina el consecutivo de pedido
			SQL = "delete from conc_ped where pedido = ?";
			this.getJdbcTemplate().update(SQL, new Object[]{OrdenPago.get("num_op").toString()});
			
			
			//Elimna de la tabla de pedidos
			SQL = "delete from pedidos where pedido = ?";
			this.getJdbcTemplate().update(SQL, new Object[]{OrdenPago.get("num_op").toString()});
			
			//Elimina el consecutivo de la reqisicion
			SQL = "delete from conc_req where reqisicion = ?";
			this.getJdbcTemplate().update(SQL, new Object []{OrdenPago.get("num_op").toString()});
			
			//Elimina de la reqisicion de lado de peredo
			SQL = "delete from reqisicion where reqisicion = ?";
			this.getJdbcTemplate().update(SQL, new Object[]{OrdenPago.get("num_op").toString()});
			*/
			/*for(Map row: mov){
				//Revertir en la tabla de partidas
				SQL = "update partidas set "+month.toLowerCase()+"preeje=(select isnull(sum(precio_uni*cantidad),0) monto from conc_req "+
					  "where proyecto = ? and clv_partid = ? and mes = ?) from partidas where proyecto = ? and clv_partid = ?";
				this.getJdbcTemplate().update(SQL, new Object []{row.get("proyecto").toString(), row.get("clv_partid").toString(), month.toUpperCase(), row.get("proyecto").toString(), row.get("clv_partid").toString()});
			}*/
			
			//Comrpobamos si existen facturas en la Orden para devolver el Devengado
			if(existenFacturas(cve_op)){
				Date fecha_finalizado = new Date();
				List<Map> conceptos = this.gatewayDetallesOrdenDePagos.getDetallesOrdenes(cve_op);
				for(Map item : conceptos){
					if(item.get("CVE_FACTURA")!=null)
					{
						this.getJdbcTemplate().update("UPDATE SAM_FACTURAS SET FECHA_FINALIZADO =?, MES_FINALIZADO=?, DIA_FINALIZADO =?, STATUS = ? WHERE CVE_FACTURA =?", new Object[]{null,null, null, 1, item.get("CVE_FACTURA") });
					}
				}
			}
			
			String clausula = "";
			if(poliza_ch!=null) clausula += ", ID_POLIZA_CH="+poliza_ch; 
			//Comprometido la Orden de Pago nuevamente
			this.getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET STATUS = ?, MOTIVO_DESEJERCIDO =? "+clausula+" WHERE CVE_OP = ?", new Object[]{OP_ESTADO_NUEVA, texto, cve_op});
			String folio=rellenarCeros(cve_op.toString(),6);
			//Guardar en bitacora
			gatewayBitacora.guardarBitacora(gatewayBitacora.OP_DESEJERCER, ejercicio, cve_pers, cve_op, folio, "OP", (Date) OrdenPago.get("FECHA"), null, null, null, Double.parseDouble(OrdenPago.get("IMPORTE").toString()));
			log.info("Orde de pago desejercida: "+OrdenPago.get("num_op").toString());
		}
		
	public boolean comprobarValeSIAM(Map vale){
		String SQL = "select count(vale) as n from conc_vale where vale=? and nconc_vale=?";
		return (this.getJdbcTemplate().queryForInt(SQL, new Object[]{vale.get("NUM_VALE").toString(), this.rellenarCeros(vale.get("CONS_VALE").toString(),3)})!=0);
	}
	

	public String getTipoDocumentoOP(String sclave){
		Map p = this.getJdbcTemplate().queryForMap("select descr from tipodoc_op where t_docto=?", new Object []{sclave});
		return p.get("descr").toString();
	}
	
public boolean ejercerOrdenPago(final Long cve_op, final boolean bfecha, final Date fecha_ejerce, final int ejercicio, final int cve_pers){
	try {   
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {	    
            	try {
					ejercerOrdenPagoFinal(cve_op, fecha_ejerce, ejercicio, cve_pers);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } 
         });
		return true;
	}
	catch (DataAccessException e) {  
        throw new RuntimeException(e.getMessage(),e);
   }	               	
  }


public void guardaBitacora(Long cve_orden, int ejercicio, int cve_pers, Map orden){
	try{
		String folio=rellenarCeros(cve_orden.toString(),6);
		//guarda en la bitacora
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_CANCELA, ejercicio, cve_pers, cve_orden, folio, "OP", (Date) orden.get("V_FECHA"), null, null, null, Double.parseDouble(orden.get("IMPORTE").toString()));
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}
}

/*Metodo para cambiar la fecha de la Orden de Pago*/
@SuppressWarnings("deprecation")
public boolean cambiarFechaOrdenPago(Long cve_op, String fechaNueva, int ejercicio, int cve_pers){
	try{
		Date f = this.formatoFecha(fechaNueva);
		Integer periodo = f.getMonth()+1;
		String folio=rellenarCeros(cve_op.toString(),6);
		this.getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET FECHA=?, PERIODO=? WHERE CVE_OP = ?", new Object[]{this.formatoFecha(fechaNueva), periodo, cve_op});
		//guarda en la bitacora
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_CAMBIA_FECHA, ejercicio, cve_pers, cve_op, folio, "OP", this.formatoFecha(fechaNueva), null, null, null, Double.parseDouble("0".toString()));
		return true;
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
		return false;
	}
}

	
	public String getListUsuarios(int cve_pers){
		try{
			String unidad_ant = "";
			String s = "";
			
			int v = 0;
			List <Map> lst = this.getJdbcTemplate().queryForList("SELECT PERSONAS.CVE_PERS, '('+USUARIOS_EX.LOGIN+')  ' + PERSONAS.NOMBRE + ' '+PERSONAS.APE_PAT+' '+PERSONAS.APE_MAT AS NOMBRE_COMPLETO,  CAT_DEPENDENCIAS.DEPENDENCIA  FROM PERSONAS INNER JOIN TRABAJADOR ON (TRABAJADOR.CVE_PERS = PERSONAS.CVE_PERS) INNER JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = TRABAJADOR.ID_DEPENDENCIA) INNER JOIN USUARIOS_EX ON (USUARIOS_EX.CVE_PERS = PERSONAS.CVE_PERS) WHERE USUARIOS_EX.ACTIVO ='S' ORDER BY CAT_DEPENDENCIAS.DEPENDENCIA, PERSONAS.NOMBRE ASC");
			s+="<option value='0'>[Seleccione]</option>";
			for(Map row: lst){
				if(!unidad_ant.equals(row.get("DEPENDENCIA").toString())){
					s+="</optgroup>";
					v=0;
				}
				unidad_ant = row.get("DEPENDENCIA").toString();
				if(v==0) s+= "<optgroup label='"+row.get("DEPENDENCIA").toString()+"'></optgroup>";
				s+="<option value='"+row.get("CVE_PERS").toString()+"' "+((cve_pers==Integer.parseInt(row.get("CVE_PERS").toString()))? "Selected":"")+" >&nbsp;&nbsp;&nbsp;"+row.get("NOMBRE_COMPLETO").toString()+"</option>";
				v++;
			}
			return s;
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			return "";
		}
	}

	public void moverOrdenes(Long cve_op, int cve_pers_dest, int ejercicio, int cve_pers){
		Date fecha = new Date();
		String folio=rellenarCeros(cve_op.toString(),6);
		String cve_unidad = (String)this.getJdbcTemplate().queryForObject("SELECT TRABAJADOR.ID_DEPENDENCIA FROM USUARIOS_EX INNER JOIN TRABAJADOR ON (TRABAJADOR.CVE_PERS = USUARIOS_EX.CVE_PERS) WHERE USUARIOS_EX.CVE_PERS = ?", new Object[]{cve_pers_dest}, String.class);
		this.getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET CVE_PERS = ?, ID_DEPENDENCIA = ? WHERE CVE_OP = ?", new Object[]{cve_pers_dest, cve_unidad, cve_op});
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MUEVE_A_USUARIO, ejercicio, cve_pers, cve_op, folio, "OP", fecha, null, null, "Movido de cve_pers_fuente = "+cve_pers+" a cve_pers_destino = "+cve_pers_dest, Double.parseDouble("0".toString()));
	}
	
	public void validarOP(Long cve_op, String fecha, int ejercicio, int cve_pers){
		Date fechaActual = new Date();
		String folio=rellenarCeros(cve_op.toString(),6);
		this.getJdbcTemplate().update("UPDATE ORDENDPAGO SET FEINI_TSOR=? WHERE ORDENPAGO=?", new Object[]{this.formatoFecha(fecha), folio});
		//this.formatoFecha
		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_VALIDA_FINANZAS, ejercicio, cve_pers, cve_op, folio, "OP", fechaActual, null, null, "", Double.parseDouble("0".toString()));
	}
	
	public List getDetallesNomina(Long cve_op){
		String sql = "SELECT SAM_MOV_OP.ID_PROYECTO, "+
							"CEDULA_TEC.N_PROGRAMA,"+ 
							"SUM(MONTO) AS MONTO,"+
							"CEDULA_TEC.DECRIPCION, "+
							"(SELECT dbo.getClaveProgramatica(SAM_MOV_OP.ID_PROYECTO,0)) AS CLAVE_PRESUPUESTAL "+ 
					"FROM SAM_MOV_OP "+
					"INNER JOIN CEDULA_TEC ON (CEDULA_TEC.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) "+ 
					"WHERE CVE_OP = ? "+
					"GROUP BY SAM_MOV_OP.ID_PROYECTO, CEDULA_TEC.N_PROGRAMA, CEDULA_TEC.DECRIPCION "+
					"ORDER BY CEDULA_TEC.N_PROGRAMA ASC";
		return this.getJdbcTemplate().queryForList(sql, new Object[]{cve_op});
	}
	
	public List<Map> cargarRelaciones(String tipo){
		List <Map> lst =this.getJdbcTemplate().queryForList("SELECT R.ID_RELACION, (R.FOLIO+' | '+U.LOGIN) AS DESCRIPCION, NUMERO, FOLIO, CONVERT(VARCHAR(10), FECHA, 103) AS FECHA, R.TIPO_RELACION, R.CVE_PERS, ID_GRUPO  FROM SAM_OP_RELACION R INNER JOIN USUARIOS_EX U ON (U.CVE_PERS = R.CVE_PERS) WHERE R.ACTIVO ='S' AND R.TIPO_RELACION =? ORDER BY R.NUMERO DESC", new Object[]{tipo});  
		return lst;
	}
	
	public List<Map> cargarRelacionesDocumentos(Long id_relacion){
		try{
				String sql = "";
				//verificar si la relacion es un vale
				String tipo = (String) this.getJdbcTemplate().queryForObject("SELECT TIPO_RELACION FROM SAM_OP_RELACION WHERE ID_RELACION = ?", new Object[]{id_relacion}, String.class); 
				if(tipo.equals("VALES") || tipo.equals("VALES_DEVOLUCION")){
					sql = "SELECT R.FOLIO, R.ID_RELACION, R.FECHA AS FECHA3, R.FECHA AS FECHA2, CONVERT(VARCHAR(10), R.FECHA,103) AS FECHA, R.CERRADA, R.DEVUELTO, D.ID_DETALLE, D.CVE_OP AS CVE_OP, V.NUM_VALE AS NUM_OP, V.NUM_VALE AS NUM_OP2, (SELECT ISNULL(SUM(M.IMPORTE),0) AS IMPORTE FROM SAM_MOV_VALES AS M WHERE M.CVE_VALE = V.CVE_VALE) AS IMPORTE, (U.CLV_UNIADM + ' ' + U.DEPENDENCIA) AS UNIDADADM, U.CLV_UNIADM, CONVERT(VARCHAR(10), V.FECHA,103) AS VAL_FECHA, C.NCOMERCIA, D.DEVOLUCION, D.OBSERVACIONES "+
								"FROM SAM_OP_RELACION R  "+
								"	LEFT JOIN SAM_OP_RELACION_DETALLES D ON (D.ID_RELACION = R.ID_RELACION)" +
								"	LEFT JOIN SAM_VALES_EX V ON (V.CVE_VALE = D.CVE_OP) "+
								"	LEFT JOIN CAT_BENEFI C ON (C.CLV_BENEFI = V.CLV_BENEFI) " +
								"	LEFT JOIN CAT_DEPENDENCIAS U ON (U.ID = V.ID_DEPENDENCIA) "+
								" WHERE TIPO_RELACION ='"+tipo+"' AND R.ID_RELACION = ? ORDER BY D.CVE_OP ASC";
				}
				else{
					sql = "SELECT R.FOLIO, R.ID_RELACION, R.FECHA AS FECHA3, R.FECHA AS FECHA2, CONVERT(VARCHAR(10), R.FECHA,103) AS FECHA, R.CERRADA, R.DEVUELTO, D.ID_DETALLE, D.CVE_OP, O.NUM_OP AS NUM_OP2, O.IMPORTE, (CASE ISNULL(D.OBSERVACIONES,'') WHEN '' THEN O.NUM_OP ELSE O.NUM_OP+' - '+D.OBSERVACIONES END) AS NUM_OP, (U.CLV_UNIADM + ' ' + U.DEPENDENCIA) AS UNIDADADM, U.CLV_UNIADM, R.ID_DEPENDENCIA_DEV, CONVERT(VARCHAR(10), O.FECHA,103) AS OP_FECHA, C.NCOMERCIA, D.DEVOLUCION, D.OBSERVACIONES "+ 
								"FROM SAM_OP_RELACION R  "+
								"	LEFT JOIN SAM_OP_RELACION_DETALLES D ON (D.ID_RELACION = R.ID_RELACION) "+ 
								"	LEFT JOIN SAM_ORD_PAGO O ON (O.CVE_OP = D.CVE_OP) "+
								"	LEFT JOIN CAT_BENEFI C ON (C.CLV_BENEFI = O.CLV_BENEFI) "+
								"	LEFT JOIN CAT_DEPENDENCIAS U ON (U.ID = O.ID_DEPENDENCIA) "+
								"WHERE R.ID_RELACION = ? ORDER BY D.CVE_OP ASC";
				}		
				return this.getJdbcTemplate().queryForList(sql, new Object[]{id_relacion});
		}
		catch ( DataAccessException e) {
			return null;
		}
	}
	
	public List<Map> cargarRelacionesGeneral(String tipo, String mes){
		String sql = "";
		String clausula = ((mes!=null&&mes!=""&&!mes.equals("0"))? " AND MONTH(R.FECHA) = "+mes:"");
		if(tipo.equals("VALES")){
			sql = "SELECT R.FOLIO, R.ID_RELACION, R.FECHA AS FECHA3, R.FECHA AS FECHA2, CONVERT(VARCHAR(10), R.FECHA,103) AS FECHA, R.CERRADA, R.DEVUELTO, D.ID_DETALLE, D.CVE_OP AS CVE_OP, V.NUM_VALE AS NUM_OP, V.IMPORTE, (U.CLV_UNIADM + ' ' + U.DEPENDENCIA) AS UNIDADADM, U.CLV_UNIADM, CONVERT(VARCHAR(10), V.FECHA,103) AS VAL_FECHA, C.NCOMERCIA, D.DEVOLUCION, D.OBSERVACIONES "+
			"FROM SAM_OP_RELACION R  "+
			"	LEFT JOIN SAM_OP_RELACION_DETALLES D ON (D.ID_RELACION = R.ID_RELACION)" +
			"	LEFT JOIN SAM_VALES_EX V ON (V.CVE_VALE = D.CVE_OP) "+
			"	LEFT JOIN CAT_BENEFI C ON (C.CLV_BENEFI = V.CLV_BENEFI) " +
			"	LEFT JOIN CAT_DEPENDENCIAS U ON (U.ID = V.ID_DEPENDENCIA) "+
			" WHERE TIPO_RELACION = ? "+clausula+" ORDER BY D.CVE_OP ASC";
		}
		else{
			sql = "SELECT R.FOLIO, R.ID_RELACION, R.FECHA AS FECHA3, R.FECHA AS FECHA2, CONVERT(VARCHAR(10), R.FECHA,103) AS FECHA, R.CERRADA, R.DEVUELTO, D.ID_DETALLE, D.CVE_OP, O.NUM_OP AS NUM_OP2, O.IMPORTE, O.NUM_OP, (U.CLV_UNIADM + ' ' + U.DEPENDENCIA) AS UNIDADADM, CONVERT(VARCHAR(10), O.FECHA,103) AS OP_FECHA, C.NCOMERCIA, D.DEVOLUCION, D.OBSERVACIONES "+ 
			"FROM SAM_OP_RELACION R  "+
			"	LEFT JOIN SAM_OP_RELACION_DETALLES D ON (D.ID_RELACION = R.ID_RELACION) "+ 
			"	LEFT JOIN SAM_ORD_PAGO O ON (O.CVE_OP = D.CVE_OP) "+
			"	LEFT JOIN CAT_BENEFI C ON (C.CLV_BENEFI = O.CLV_BENEFI) "+
			"	LEFT JOIN CAT_DEPENDENCIAS U ON (U.ID = O.ID_DEPENDENCIA) "+
			" WHERE TIPO_RELACION = ? "+clausula+" ORDER BY D.CVE_OP ASC";
		}
			
		return this.getJdbcTemplate().queryForList(sql, new Object[]{tipo});
	}
	
	public List getOTPorUnidad(String idtipoGasto,String unidad ,String claveBeneficiario, int cve_pers, String proyecto, String clv_partid) {
  	  Map parametros =  new HashMap<String,Object>();
  	  parametros.put("cve_pers", cve_pers);
  	  parametros.put("unidad", unidad);
  	  parametros.put("idtipoGasto", idtipoGasto);
  	  parametros.put("claveBeneficiario", claveBeneficiario); 
  	  parametros.put("proyecto", proyecto);
  	  parametros.put("clv_partid", clv_partid);
  	  
   	 return this.getNamedJdbcTemplate().queryForList("SELECT  C.CVE_CONTRATO, B.ID_DEPENDENCIA, A.RECURSO, B.N_PROGRAMA, C.CLV_PARTID, C.NUM_REQ,  "+
   			 " C.CVE_REQ, C.OBSERVA , isnull ( (select sum (cantidad * precio_est ) from SAM_REQ_MOVTOS where cve_req=C.cve_req  ),0) IMPORTE "+ 
   			 " FROM         CAT_RECURSO  A INNER JOIN "+
   			 " CEDULA_TEC  B ON A.ID = B.ID_RECURSO INNER JOIN "+
   			 " SAM_REQUISIC C ON B.ID_PROYECTO = C.ID_PROYECTO INNER JOIN "+
   			 " SAM_ORDEN_TRAB D ON C.CVE_REQ = D.CVE_REQ "+
   	 		 " WHERE     B.ID_RECURSO = :idtipoGasto " +
   	 		 "AND B.ID_DEPENDENCIA IN(SELECT DISTINCT ID_DEPENDENCIA FROM CEDULA_TEC WHERE ID_PROYECTO IN (SELECT ID_PROYECTO FROM VT_PROYECTOS_USUARIOS WHERE ID_USUARIO =:cve_pers)) AND D.CLV_BENEFI = :claveBeneficiario and c.status IN (1,2) "+(proyecto.equals("")&&clv_partid.equals("")? "":" AND B.N_PROGRAMA=:proyecto AND C.CLV_PARTID =:clv_partid "),parametros);
    }
	
	public List getPedidosPorUnidad (String idtipoGasto,String unidad ,String claveBeneficiario, int cve_pers, String proyecto, String clv_partid) {
	  	  Map parametros =  new HashMap<String,Object>();
	  	  parametros.put("unidad", unidad);
	  	  parametros.put("cve_pers", cve_pers);
	  	  parametros.put("idtipoGasto", idtipoGasto);
	  	  parametros.put("claveBeneficiario", claveBeneficiario);
	  	  parametros.put("proyecto", proyecto);
	  	  parametros.put("clv_partid", clv_partid);
	  	  
	   	 return this.getNamedJdbcTemplate().queryForList("SELECT     c.CVE_PED,c.NUM_PED, d.CVE_CONTRATO, c.FECHA_PED, c.STATUS, c.TOTAL, " + 
	   			  " b.ID_RECURSO, A.RECURSO, c.NOTAS, b.N_PROGRAMA, d.CLV_PARTID  as PARTIDA "+ 
	   			  " FROM         dbo.CEDULA_TEC b INNER JOIN "+
	              " dbo.CAT_RECURSO  a ON b.ID_RECURSO = a.ID INNER JOIN "+
	   			  " dbo.SAM_REQUISIC d INNER JOIN "+
	   			  " dbo.SAM_PEDIDOS_EX  as c ON d.CVE_REQ = c.CVE_REQ ON b.ID_PROYECTO = d.ID_PROYECTO "+
	   			  " where b.ID_RECURSO=:idtipoGasto and c.clv_benefi=:claveBeneficiario "+  
	   			  " and b.ID_DEPENDENCIA IN(SELECT DISTINCT ID_DEPENDENCIA FROM CEDULA_TEC WHERE ID_PROYECTO IN (SELECT ID_PROYECTO FROM VT_PROYECTOS_USUARIOS WHERE ID_USUARIO =:cve_pers)) AND C.CLV_BENEFI = :claveBeneficiario and c.status IN (1,2) "+(proyecto.equals("")&&clv_partid.equals("")? "":" AND b.N_PROGRAMA=:proyecto AND d.CLV_PARTID =:clv_partid "),parametros);
	    }
	
	public boolean cambiarFechaPeriodo(final Long cve_op, final String fechaNueva, final int periodo, final int cve_pers, final int ejercicio){
		try {   
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	Date fecha = formatoFecha(fechaNueva);
	            	/*Graba en bitacora el cambio*/
	            	Map op = getJdbcTemplate().queryForMap("SELECT NUM_OP, CONVERT(varchar(10),FECHA,103) AS FECHA, ISNULL(IMPORTE,0) AS IMPORTE FROM SAM_ORD_PAGO WHERE CVE_OP = ?", new Object[]{cve_op});
	            	gatewayBitacora.guardarBitacora(gatewayBitacora.OP_ACTUALIZAR, ejercicio, cve_pers, Long.parseLong(cve_op.toString()), op.get("NUM_OP").toString(), "OP", fecha, null, null, "Cambió la fecha de la Orden  de Pago de "+op.get("FECHA").toString()+" a "+fechaNueva, Double.parseDouble(op.get("IMPORTE").toString()));
	            	/*Realiza el cambio*/
	            	getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET FECHA = ?, PERIODO =? WHERE CVE_OP = ?", new Object[]{fecha, periodo, cve_op});
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, puede que la fecha este escrita incorrectamente, verifique nuevamente el formato y vuelta a intentar esta operación",e);
	   }	
	}
	
	public Map getBeneficiario(Long cve_doc){
		try {   
			return this.getJdbcTemplate().queryForMap("SELECT NUM_OP, CLV_BENEFI, (SELECT CAT_BENEFI.NCOMERCIA FROM CAT_BENEFI WHERE CAT_BENEFI.CLV_BENEFI = SAM_ORD_PAGO.CLV_BENEFI) AS BENEFICIARIO FROM SAM_ORD_PAGO WHERE CVE_OP = ?", new Object[]{cve_doc});
		}
		catch (DataAccessException e) {                               
	       return null;
	   }
	}
	
	
	public boolean cambiarBeneficiario(final Long cve_op, final String clv_benefi, final int ejercicio, final int cve_pers){
		try {   
			
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            		/*Comprobar algunos datos antes*/
		            	Map op = getJdbcTemplate().queryForMap("SELECT NUM_OP, CONVERT(varchar(10),FECHA,103) AS FECHA, IMPORTE, SAM_ORD_PAGO.CLV_BENEFI, C.NCOMERCIA "+  
																	"FROM SAM_ORD_PAGO "+  
																		"LEFT JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = SAM_ORD_PAGO.CLV_BENEFI) "+
																		"WHERE SAM_ORD_PAGO.CVE_OP = ?", new Object[]{cve_op});
		            	Map ben = getJdbcTemplate().queryForMap("SELECT NCOMERCIA FROM CAT_BENEFI WHERE CAT_BENEFI.CLV_BENEFI =?", new Object[]{clv_benefi});
	            		String texto ="";
	            		if(op.get("CLV_BENEFI")!=null)
	            			texto = "Cambió el beneficiario CLV_BENEFI: ['"+op.get("CLV_BENEFI").toString()+"' "+op.get("NCOMERCIA").toString()+"] a: ['"+clv_benefi+"' "+ben.get("NCOMERCIA").toString()+"]";
	            		else
	            			texto = "Cambió el beneficiario CLV_BENEFI: ['' ] a: ['"+clv_benefi+"' "+ben.get("NCOMERCIA").toString()+"]";
		            	/*Graba en bitacora el cambio*/
		            	gatewayBitacora.guardarBitacora(gatewayBitacora.OP_ACTUALIZAR, ejercicio, cve_pers, cve_op, op.get("NUM_OP").toString(), "OP", formatoFecha(op.get("FECHA").toString()), null, null, texto, Double.parseDouble(op.get("IMPORTE").toString()));
		            	/*Realiza el cambio*/
		            	getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET CLV_BENEFI = ? WHERE CVE_OP = ?", new Object[]{clv_benefi, cve_op});
		            	
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, el cambio de beneficiario no se ha podido realizar, intente editando el documento directamente",e);
	   }	
	 }
	
	public List<Map> getListaAnexosArchivosOP(Long cve_op){
		return this.getJdbcTemplate().queryForList("select a.CVE_OP,a.ANX_CONS,a.T_DOCTO,a.NUMERO,a.NOTAS , b.DESCR, a.FILENAME, a.FILETYPE, a.FILELENGTH, a.FILEPATH from SAM_OP_ANEXOS a , tipodoc_op b where a.t_docto=b.t_docto and cve_op=? ORDER BY a.ANX_CONS ASC", new Object[]{cve_op});
	}
	
	public boolean existeDevengadoFactura(Long cve_factura){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM VT_COMPROMISOS WHERE CVE_DOC = ? AND TIPO_DOC =? AND CONSULTA =?", new Object[]{cve_factura, "FAC", "DEVENGADO"})>0;
	}
	
	public boolean existeEnOtrasOrdenPago(Long cve_factura){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_ORD_PAGO AS OP INNER JOIN SAM_MOV_OP AS M ON (M.CVE_OP = OP.CVE_OP) WHERE OP.STATUS IN (0, 6) AND M.CVE_FACTURA =?", new Object[]{cve_factura})>0;
	}
	
	
	public boolean exitsteMovimiento(Long cve_factura, Long cve_op){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_MOV_OP WHERE CVE_FACTURA = ? AND CVE_OP =?", new Object[]{cve_factura, cve_op})>0;
	}
	
	
	//------------------------------------- CARGA LA LISTA DE FACTURAS SELECCIONADAS EN LOS DETALLES DE ORDEN DE PAGO---------------------------------------------------------
	public String guardarFacturasEnOrdenPago(final Long cve_op, final Long[] cve_facturas, final int ejercicio, final int cve_pers){
		try { 
			final String result = ""; 
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	/*Comprobar algunos datos antes*/
	            	
	            	
	            	int numAnexo = getJdbcTemplate().queryForInt("SELECT MAX(ANX_CONS) FROM SAM_OP_ANEXOS WHERE CVE_OP = ?", new Object[]{cve_op});
	            	
	            	for(Long cve_factura: cve_facturas){
	            		
	            		numAnexo++;
	            		Map factura = gatewayFacturas.getFactura(cve_factura);
	            		
	            		List<Map> facturaMovtos = gatewayFacturas.getDetalles(cve_factura);
	            		List<Map> facturaRetenc = gatewayFacturas.getRetencion(cve_factura);
	            		List<Map> facturaVales = gatewayFacturas.getVales(cve_factura);
	            			            		
	            		//Listado de las retenciones
	            		
	            		if(!factura.get("STATUS").toString().equals("1"))
	            			throw new RuntimeException("El Estatus de la factura "+factura.get("NUM_FACTURA").toString()+" no es valido ó no esta devengada en el presupuesto");
	            		if(!existeDevengadoFactura(Long.parseLong(factura.get("CVE_FACTURA").toString())))
	            			throw new RuntimeException("El Presupuesto Devengado de la factura "+factura.get("NUM_FACTURA").toString()+" no existe, error de insuficiencia presupuestal");
	            		if(existeEnOtrasOrdenPago(Long.parseLong(factura.get("CVE_FACTURA").toString())))
	            			throw new RuntimeException("La factura "+factura.get("NUM_FACTURA").toString()+" ya existe en otra Orden de Pago");
	            		/*Comprobar que tampoco se repida el movimiento*/
	            		if(exitsteMovimiento(Long.parseLong(factura.get("CVE_FACTURA").toString()), cve_op))
	            			throw new RuntimeException("La factura "+factura.get("NUM_FACTURA").toString()+" ya existe en los movimientos de la Orden de Pago");
	            		
	            		
	            		//Guarda los movimiento de la factura en la OP
	            		
	            		for(Map detalleFac: facturaMovtos)
	            		{
		            		getJdbcTemplate().update("INSERT INTO SAM_MOV_OP(CVE_OP, ID_PROYECTO, CLV_PARTID, CVE_FACTURA, NOTA, TIPO, MONTO) VALUES(?,?,?,?,?,?,?)", new Object[]{cve_op, detalleFac.get("ID_PROYECTO"), detalleFac.get("CLV_PARTID"), cve_factura, "Soporta la factura No. "+factura.get("NUM_FACTURA"), "FACTURA", detalleFac.get("IMPORTE")});
		            	}
	            		
	            		
	            		/*Guardar las deductivas de la factura en Orden de pago si existen*/
	            		/*Retenciones de la Orden de pago*/
	            		
	            		int cont =getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA =?", new Object[]{cve_factura});;
	            		List <Map> detalles = getJdbcTemplate().queryForList("SELECT *FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA =?", new Object[]{cve_factura});
	            		for(Map row: detalles){
	            			cont++;
	            			getJdbcTemplate().update("INSERT INTO MOV_RETENC (CVE_OP, RET_CONS, CLV_RETENC, IMPORTE, PAGADO) " +
	            					"VALUES (?,?,?,?,?)"
	            					, new Object[]{cve_op, (cont), row.get("CLV_RETENC"), row.get("IMPORTE"), 0});
	            		}
	            		
	            		/*Guarda los archivos de la factura en la OP siempre y cuando la factura tenga archivos*/
	            		if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_FACTURAS_ARCHIVOS WHERE CVE_FACTURA =?", new Object[]{cve_factura})>0)
	            		{
	            			//Guarda un nuevo anexo
	            			insertaDocumento(numAnexo, "FAC", factura.get("NUM_FACTURA").toString(), factura.get("NOTAS").toString(), cve_op, ejercicio, cve_pers);
	            			
	            			Map det = getJdbcTemplate().queryForMap("SELECT top 1 *FROM SAM_FACTURAS_ARCHIVOS WHERE CVE_FACTURA =?", new Object[]{cve_factura});
	            			getJdbcTemplate().update("UPDATE SAM_OP_ANEXOS SET FILENAME =?, FILEPATH=?, DATEFILE=?, FILETYPE=?, FILELENGTH=? WHERE CVE_OP =? AND ANX_CONS=?", new Object[]{"["+det.get("ID_ARCHIVO") + "] "+ det.get("NOMBRE"), "../"+det.get("RUTA"), new Date(), det.get("EXT"), det.get("TAMANO"), cve_op, numAnexo});
	            		}
	            		
	            		      			            		
	            		/*Guardar las comprobaciones de vale de la Factura en la Orden de Pago si existen*/
	            		int vale= getJdbcTemplate().queryForInt( "SELECT FV.CVE_VALE FROM SAM_FACTURAS_VALES FV INNER JOIN SAM_FACTURAS F ON F.CVE_FACTURA=FV.CVE_FACTURA WHERE F.CVE_FACTURA=? ", new Object[]{cve_factura});
	            		int contv =0;
	            		int fact=0;
	            		Float IMP_PENDIENTE =0F;
	            		List <Map> detallesVales = getJdbcTemplate().queryForList("SELECT FV.CVE_FACTURA,FV.CVE_VALE,FV.ID_PROYECTO,FV.CLV_PARTID,ISNULL(FV.IMPORTE,0.00) IMPORTE, MOP.CVE_OP, " +
																				  "ISNULL(MV.IMPORTE- (SELECT ISNULL(SUM(FV.IMPORTE),0.00)COMPROBADO FROM SAM_FACTURAS_VALES FV INNER JOIN SAM_FACTURAS F ON F.CVE_FACTURA=FV.CVE_FACTURA WHERE FV.CVE_VALE=? AND F.STATUS IN (3)),0.00) IMP_ANTERIOR " +
																				  //"ISNULL(MV.IMPORTE- (SELECT ISNULL(SUM(FV.IMPORTE),0.00)COMPROBADO FROM SAM_FACTURAS_VALES FV INNER JOIN SAM_FACTURAS F ON F.CVE_FACTURA=FV.CVE_FACTURA WHERE FV.CVE_VALE=? AND F.STATUS IN (1,3)),0.00) IMP_PENDIENTE " +
																				  "FROM SAM_FACTURAS_VALES FV " +
																				  "INNER JOIN SAM_FACTURAS F ON F.CVE_FACTURA=FV.CVE_FACTURA " + 
																				  "INNER JOIN SAM_MOV_VALES MV ON MV.CVE_VALE=FV.CVE_VALE AND MV.ID_PROYECTO=FV.ID_PROYECTO AND MV.CLV_PARTID=FV.CLV_PARTID " + 
																				  "INNER JOIN SAM_VALES_EX V ON V.CVE_VALE=MV.CVE_VALE " +
																				  "LEFT JOIN SAM_MOV_OP MOP ON MOP.CVE_FACTURA=F.CVE_FACTURA " +
																				  "WHERE F.STATUS IN (1,3) AND V.STATUS=4 AND FV.CVE_FACTURA=? AND MOP.CVE_OP=? "+
																				  "GROUP BY FV.CVE_FACTURA,FV.CVE_VALE,FV.ID_PROYECTO,FV.CLV_PARTID,FV.IMPORTE,MV.IMPORTE,MOP.CVE_OP", new Object[]{vale,cve_factura,cve_op});
	            		
	            		for(Map row: detallesVales){
	            			
	            			contv=getJdbcTemplate().queryForInt( "SELECT COUNT(CONS_VALE) FROM COMP_VALES WHERE CVE_VALE=? ", new Object[]{vale});
	            			
	            			if (contv==0){
	            				
	            				String imp =row.get("IMPORTE").toString(); //COMPROBANDO
		            			String impant=row.get("IMP_ANTERIOR").toString(); //COMPROBADO
		            			IMP_PENDIENTE=Float.parseFloat(impant)-Float.parseFloat(imp);
		            			contv++;
		            			getJdbcTemplate().update("INSERT INTO COMP_VALES (CVE_VALE, CONS_VALE, CVE_OP, TIPO, ID_PROYECTO, CLV_PARTID, IMPORTE, IMP_ANTERIOR, IMP_PENDIENTE, FECHA) " +
	            					"VALUES (?,?,?,?,?,?,?,?,?,?)"
	            					, new Object[]{row.get("CVE_VALE"), contv, cve_op, "OP", row.get("ID_PROYECTO"), row.get("CLV_PARTID"), row.get("IMPORTE"), row.get("IMP_ANTERIOR"),IMP_PENDIENTE, new Date()});
	            			}
	            			else
	            				
	            					
	            				getJdbcTemplate().update("UPDATE COMP_VALES SET TIPO='OP', CVE_OP=?  WHERE CVE_OP=? AND TIPO='FA'", new Object[]{cve_op,cve_factura});
	            		}
	            	}
	       
	            	
	            } 
	        });
			
			//Guardar el Iva de las facturas existentes solo cuando no existe IVA en la OP
        	List<Map> DetFacturasOP = getJdbcTemplate().queryForList("SELECT * FROM SAM_MOV_OP WHERE CVE_OP = ? AND CVE_FACTURA IS NOT NULL", new Object[]{cve_op});
        	Map OrdenPago = getJdbcTemplate().queryForMap("SELECT * FROM SAM_ORD_PAGO WHERE CVE_OP =?", new Object[]{cve_op});
        	if(OrdenPago.get("IMPORTE_IVA")!=null)
        	{
	        	if(Double.parseDouble(OrdenPago.get("IMPORTE_IVA").toString())==0)
	        	{
	        		double Iva = 0.00;
	        		for(Map row: DetFacturasOP)
	        		{
	        			Map factura = gatewayFacturas.getFactura(Long.parseLong(row.get("CVE_FACTURA").toString()));
	        			if(factura.get("IVA")!=null)
	        			{
		        			if(((BigDecimal)factura.get("IVA")).doubleValue()>0)
		        				Iva = Iva +  ((BigDecimal)factura.get("IVA")).doubleValue();
	        			}
	        		}
	        		//Guardar el IVA en la OP
	        		if (Iva>0)
	        		{
	        			getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET IVA = 1, IMPORTE_IVA = ? WHERE CVE_OP =?", new Object[]{Iva, cve_op});
	        		}
	        	}
        	}
        	
			return result;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, error al cargar las facturas a la Orden de Pago",e);
	   }
	}
	
	//-------------------------------------Metodo de generar la Orden de pago desde la lista de Facturas----------------------------------
	
	
	//Pruebas de Abraham-------------------------------20-05-17

	public Long insertaOrdenxFacturas(final int id_unidad , final List<Long> cve_facturas, final int cve_pers, final int ejercicio, final int id_grupo ){
		
		//, int tipo,Date fecha,String  pedido,String  cveBeneficiario,int  cvePersona,String  cveParbit, String reembolsoFondo,String  concurso,String  nota,int  status,Integer  cveRequisicion, double importeIva, String cveUnidad, Integer periodo, int tipoGasto, Integer idGrupo, Long cve_contrato
		
		try {
			 Long  cve_op  = 0L;
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
		            protected void doInTransactionWithoutResult(TransactionStatus status) {
	            	
	            	Long cveOp=0L;
					String clv_benefi=null;
					Double iva = 0D;
					Integer periodo=0;
					//String arraycve_fac[]=cve_facturas.split(",");
					String reembolso=null;

					for( Long cve_factura: cve_facturas ){
						Map factura = gatewayFacturas.getFactura((cve_factura));
	            		clv_benefi = factura.get("CLV_BENEFI").toString();
	            		iva = iva+ Double.parseDouble((factura.get("IVA")!= null ? factura.get("IVA").toString(): "0"));
	            		periodo=Integer.parseInt(factura.get("PERIODO").toString());
					}
					
					if (reembolso==null)
						reembolso="N";
					else
						reembolso="S";

					//Crear la Orden de Pago Y Generar los detalles de la orde de pago
					//int ejercicio, int tipo,Date  fecha,String  pedido,String  cveBeneficiario,int  cvePersona, String reembolsoFondo,String  concurso,String  nota,int  status,Integer  cveRequisicion, double importeIva, int cveUnidad , Integer periodo, int tipoGasto, Integer idGrupo, int cve_pers, Long cve_contrato   ){
				
					cveOp = insertaOrden( ejercicio, 12, new Date(), null, clv_benefi,cve_pers, reembolso, null, "", -1, 0, iva,id_unidad,periodo,1, 0, id_grupo,Long.parseLong("0"));
					guardarFacturasEnOrdenPago(cveOp, (Long[])cve_facturas.toArray(new Long[cve_facturas.size()]), ejercicio, cve_pers);
					
		            }
	            });
			//*BUSCAR AQUI LA ULTIMA CVE_OP QUE SE CREO PARA REGRESAR EL CV_OP*/
			cve_op = getJdbcTemplate().queryForLong("SELECT MAX(CVE_OP) FROM SAM_ORD_PAGO", new Object[]{});
			return  cve_op;

		} catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, error al cargar las facturas a la Orden de Pago",e);
	   }
	}

	
	
	public void generarDetallesContrato(String num_contrato, Long cve_contrato, Long cve_op, Double importe_op,  int proyecto, String clv_partid)
	{
		try 
		{ 
			int cont = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_MOV_OP WHERE CVE_OP = ? AND ID_PROYECTO=? AND CLV_PARTID=?", new Object[]{cve_op, proyecto, clv_partid});
			if(cont>0) throw new RuntimeException("No se permiten mas de un concepto de contrato en la Orden de Pago");
				
			this.getJdbcTemplate().update("INSERT INTO SAM_MOV_OP (CVE_OP, ID_PROYECTO, CLV_PARTID, NOTA, TIPO, MONTO) VALUES(?,?,?,?,?,?)", new Object[]{
					cve_op,
					proyecto,
					clv_partid,
					"Concepto de Contrato: "+num_contrato,
					"LIBRE",
					importe_op
			});
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La ha fallado para generar los detalles de contratos en Orden de Pago",e);
	   }
	}
	
	public List<Map> getListaORdenPagoEstatus(int idDependencia, int status)
	{
		return this.getJdbcTemplate().queryForList("SELECT  DEP.CLV_UNIADM, "+
						"A.CVE_OP,  "+
						"A.ID_DEPENDENCIA,  "+
						"A.STATUS, "+
						"A.EJERCICIO,  "+
						"A.NUM_OP,  "+
						"A.TIPO, CONVERT(varchar(10), A.FECHA, 103) AS FECHA,  "+
						"A.CVE_PED,  "+
						"A.CLV_BENEFI, "+ 
						"A.CVE_PERS,  "+
						"A.ID_RECURSO,  "+
						"A.REEMBOLSOF,  "+
						"A.CONCURSO,   "+
				        "A.IMPORTE,  "+
						"A.FE_PAGO, "+
						"A.NOTA,  "+
						"A.PERIODO,    "+
				        "A.cve_req,  "+
						"A.ID_POLIZA_CH,  "+
						"A.IVA,  "+
						"A.IMPORTE_IVA  "+
				"FROM  dbo.SAM_ORD_PAGO AS A  "+
				    "INNER JOIN CAT_DEPENDENCIAS DEP ON (DEP.ID = A.ID_DEPENDENCIA) WHERE A.ID_DEPENDENCIA=? AND A.STATUS IN(?) ", new Object[]{idDependencia, status});
		
				    
	}
	
	public Double obtenerIvaOrdenPago(Long cve_op)
	{
		Map OrdenPago = this.getJdbcTemplate().queryForMap("SELECT IMPORTE_IVA FROM SAM_ORD_PAGO WHERE CVE_OP =? ", new Object[]{cve_op});
	    if(OrdenPago.get("IMPORTE_IVA")!=null)
	    	return ((BigDecimal)OrdenPago.get("IMPORTE_IVA")).doubleValue();
	    else
	    	return 0.0;
	}
	
}