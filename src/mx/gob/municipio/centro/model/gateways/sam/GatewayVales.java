/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.io.File;
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

public class GatewayVales  extends BaseGateway {
	private static Logger log = Logger.getLogger(GatewayVales.class.getName());
	final  int VAL_ESTATUS_NUEVO =0;
	final  int VAL_ESTATUS_PENDIENTE =1;
	final  int VAL_ESTATUS_CANCELADO =2;
	final  int VAL_ESTATUS_COMPROBADO =3;
	final  int VAL_ESTATUS_PAGADO =4;
	
	
	@Autowired
	public GatewayBitacora gatewayBitacora;
	
	/*Constructor*/
	public GatewayVales() {		
	}
	
	/*Metodo usado para determinar si se guarda una nueva requisicion o se actualiza una existente*/
	public  Long actualizarVales(Long clave,Date fecha,String  tipoVale,String claveBeneficiario,String justificacion,/*String proyecto, String partida,*/ Integer mes,Date fechaInicial,Date fechaFinal,Date fechaMaxima,String documentacion, Long cve_contrato, Integer ejercicio, Integer idUsuario , String  claveUnidad, int idrecurso,Integer idGrupo){
		
		  if (clave == null)
			  clave= guardar(fecha,tipoVale,claveBeneficiario,justificacion,mes,fechaInicial,fechaFinal,fechaMaxima, documentacion,  cve_contrato, ejercicio, idUsuario, claveUnidad, idrecurso ,idGrupo);  
		  else
			  actualizar(clave,fecha,tipoVale,claveBeneficiario,justificacion,mes,fechaInicial,fechaFinal,fechaMaxima, documentacion, cve_contrato, rellenarCeros(clave.toString(),6), ejercicio, idUsuario, claveUnidad,idrecurso,idGrupo);
	  return  clave; 
	}	
	
	public Long guardar( Date fecha,String  tipoVale,String claveBeneficiario,String justificacion,/*String proyecto, String partida,*/
		Integer mes,Date fechaInicial,Date fechaFinal,Date fechaMaxima,String documentacion, Long cve_contrato, Integer ejercicio, Integer idUsuario , String  claveUnidad ,int idrecurso, Integer idGrupo  ) {
		
    	String INSERT_SQL ="INSERT INTO SAM_VALES_EX (CVE_CONTRATO, FECHA,TIPO,CLV_BENEFI,JUSTIF,MES,FECHA_INI,FECHA_FIN,FECHA_MAX,DOCTO_COMP,EJERCICIO,CVE_PERS,ID_DEPENDENCIA,ID_RECURSO,STATUS,ID_GRUPO)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    	this.getJdbcTemplate().update(INSERT_SQL,new Object[]{(cve_contrato!=0 ? cve_contrato: null), fecha,tipoVale,claveBeneficiario,justificacion,mes,fechaInicial,fechaFinal,fechaMaxima,documentacion,ejercicio,idUsuario,claveUnidad,idrecurso,VAL_ESTATUS_NUEVO,idGrupo});
    	Long clave=getMaxVale();
		String numVale=rellenarCeros(clave.toString(),6);
		String folio=rellenarCeros(clave.toString(),6);
		Map vale = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_VALES_EX WHERE CVE_VALE = ? ", new Object[]{clave});
		Double imp = (vale.get("IMPORTE")!=null) ? Double.parseDouble(vale.get("IMPORTE").toString()): 0D;
		//String sproyecto = vale.get("ID_PROYECTO").toString();
		//String spartida = (String)vale.get("CLV_PARTID");
		/*if(sproyecto==null) sproyecto ="";
		if(spartida==null) spartida ="";*/
    	//Guardar en Bitacora
    	gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_NUEVO, ejercicio, idUsuario, clave, folio, "VAL", (Date) vale.get("FECHA"), "", "", null, imp);
        return clave;
    }
	
	public Long getMaxVale(){
		return this.getJdbcTemplate().queryForLong("select max (CVE_VALE) from SAM_VALES_EX ");
	}
	
	/*Metodo para actualizar los datos de la requisicion*/
	public void actualizar( Long clave,Date fecha,String  tipoVale,String claveBeneficiario,String justificacion,/*String proyecto, String partida,*/
		Integer mes,Date fechaInicial,Date fechaFinal,Date fechaMaxima,String documentacion, Long cve_contrato, String numVale, Integer ejercicio, Integer idUsuario , String  claveUnidad ,int idrecurso, Integer id_grupo){
		/*String idGrupoVale = (String) this.getJdbcTemplate().queryForObject("SELECT ID_GRUPO FROM VALES_EX WHERE CVE_VALE = ?", new Object[]{clave}, String.class);
		if(idGrupoVale!=null&&idGrupoVale.equals("")) 
			id_grupo = Integer.parseInt(idGrupoVale.toString());*/
		
		String SQL = "UPDATE SAM_VALES_EX set CVE_CONTRATO=?, FECHA=?,TIPO=?,CLV_BENEFI=?,JUSTIF=?,MES=?,FECHA_INI=?,FECHA_FIN=?,FECHA_MAX=?,DOCTO_COMP=?,EJERCICIO=?,CVE_PERS=?,ID_DEPENDENCIA=?,ID_RECURSO=?  where CVE_VALE=? ";
		this.getJdbcTemplate().update(SQL, new Object[]{(cve_contrato!=0 ? cve_contrato: null), fecha,tipoVale,claveBeneficiario,justificacion,mes,fechaInicial,fechaFinal,fechaMaxima,documentacion,ejercicio,idUsuario,claveUnidad,idrecurso,clave});
		Map vale = this.getJdbcTemplate().queryForMap("SELECT *, (SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_MOV_VALES WHERE SAM_MOV_VALES.CVE_VALE = SAM_VALES_EX.CVE_VALE)AS IMPORTE FROM SAM_VALES_EX WHERE CVE_VALE = ? ", new Object[]{clave});
		String folio=rellenarCeros(clave.toString(),6);
		/*String sproyecto = vale.get("ID_PROYECTO").toString();
		String spartida = (String)vale.get("CLV_PARTID");
		if(sproyecto==null) sproyecto ="";
		if(spartida==null) spartida ="";*/
    	//Guardar en Bitacora
    	gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_ACTUALIZA, ejercicio, idUsuario, clave, folio, "VAL", (Date) vale.get("FECHA"), "", "", null, Double.parseDouble(vale.get("IMPORTE").toString()));
	}	
	
	public Map getVale(Long  clave ){
		try {
		return this.getJdbcTemplate().queryForMap("SELECT     A.NUM_VALE, A.CVE_VALE, ISNULL(D.CVE_CONTRATO, 0) AS CVE_CONTRATO, ISNULL(D.NUM_CONTRATO, '') AS NUM_CONTRATO, A.EJERCICIO, A.ID_DEPENDENCIA, A.CVE_PERS, A.CLV_BENEFI, A.ID_RECURSO, convert(varchar(10),A.FECHA,103)FECHA, A.FECHA AS FECHA2,"+ 
                      " A.TIPO, A.MES, (SELECT SUM(IMPORTE) FROM SAM_MOV_VALES WHERE SAM_MOV_VALES.CVE_VALE = A.CVE_VALE) AS IMPORTE, A.JUSTIF, A.DOCTO_COMP, convert(varchar(10),A.FECHA_INI,103) FECHA_INI, convert(varchar(10),A.FECHA_FIN,103) FECHA_FIN , convert(varchar(10),A.FECHA_MAX,103)  FECHA_MAX, A.STATUS, A.POLIZA_CH, B.NCOMERCIA,  "+
                      " C.DEPENDENCIA as UNIADM  FROM  SAM_VALES_EX AS A INNER JOIN "+
                      " CAT_BENEFI AS B ON A.CLV_BENEFI = B.CLV_BENEFI INNER JOIN "+
                      " CAT_DEPENDENCIAS C ON A.ID_DEPENDENCIA = C.ID LEFT JOIN SAM_CONTRATOS AS D ON (D.CVE_CONTRATO = A.CVE_CONTRATO) where A.CVE_VALE=? ",new Object []{clave});
		}catch (DataAccessException e ) {
		   return null;
		}		
	}	
	
	public Map getValeTodosDatos(Long  clave ){
		return this.getJdbcTemplate().queryForMap("SELECT     A.NUM_VALE, A.CVE_VALE, A.EJERCICIO, A.ID_DEPENDENCIA,A.CVE_PERS, A.CLV_BENEFI, A.ID_RECURSO, G.RECURSO, "+
				" 		CONVERT(varchar(10), A.FECHA, 103) AS FECHA, A.TIPO, A.MES, A.JUSTIF, A.DOCTO_COMP, convert(varchar(10),A.FECHA_INI,103) FECHA_INI, convert(varchar(10),A.FECHA_FIN,103) FECHA_FIN , convert(varchar(10),A.FECHA_MAX,103)  FECHA_MAX,  "+
                      " A.STATUS, A.POLIZA_CH,A.ID_GRUPO, B.NCOMERCIA, C.DEPENDENCIA AS UNIADM, D.DESCRIPCION_ESTATUS, E.TIPO_VALE, (SELECT ISNULL(SUM(SAM_MOV_VALES.IMPORTE),0) FROM SAM_MOV_VALES WHERE SAM_MOV_VALES.CVE_VALE = A.CVE_VALE) AS TOTAL, "+
                      " G.RECURSO AS TIPO_GASTO, C.CLV_UNIADM "+
                      " FROM         SAM_VALES_EX AS A INNER JOIN "+
                      " CAT_BENEFI AS B ON A.CLV_BENEFI = B.CLV_BENEFI INNER JOIN "+
                      " CAT_DEPENDENCIAS AS C ON A.ID_DEPENDENCIA = C.ID INNER JOIN "+
                      " SAM_ESTATUS_VALE AS D ON A.STATUS = D.ID_ESTATUS INNER JOIN "+
                      " SAM_CAT_TIPO_VALE AS E ON A.TIPO = E.CLAVE_VALE "+
                      "LEFT OUTER JOIN "+
                      " CAT_RECURSO as G ON A.ID_RECURSO = G.ID where A.CVE_VALE = ?",new Object []{clave});
	}	
	
	
	public List<Map> getListaDeValesPorEjemplo(String unidad, String  estatus , Date fInicial, Date fFinal , Integer ejercicio, String tipoGasto, Integer idUsuario, String verUnidad, Boolean privilegio){
		Map parametros =  new HashMap<String,Object>();
		/*Asignacion de parametros*/
		parametros.put("unidad", unidad);
		parametros.put("fechaInicial", fInicial);
		parametros.put("fechaFinal", fFinal);
		parametros.put("ejercicio", ejercicio);
		parametros.put("tipoGasto", tipoGasto);
		parametros.put("idUsuario", idUsuario);		
		
		String sql = "SELECT     A.NUM_VALE, A.CVE_VALE, A.EJERCICIO, A.ID_DEPENDENCIA, A.CVE_PERS, A.CLV_BENEFI, A.ID_RECURSO, CR.RECURSO, "+ 
					 " CONVERT(varchar(10), A.FECHA, 103) AS FECHA, A.TIPO, A.MES, (SELECT ISNULL(SUM(IMPORTE),0) FROM  SAM_MOV_VALES WHERE SAM_MOV_VALES.CVE_VALE =A.CVE_VALE) AS TOTAL, A.JUSTIF, A.DOCTO_COMP, A.FECHA_INI, A.FECHA_FIN, A.FECHA_MAX, "+ 
                      " A.STATUS, A.POLIZA_CH, B.NCOMERCIA, C.DEPENDENCIA AS UNIADM, D.DESCRIPCION_ESTATUS, "+ 
                      " E.TIPO_VALE "+
                      " FROM  SAM_VALES_EX AS A INNER JOIN "+
                      " CAT_BENEFI AS B ON A.CLV_BENEFI = B.CLV_BENEFI INNER JOIN "+
                      " CAT_DEPENDENCIAS AS C ON A.ID_DEPENDENCIA = C.ID INNER JOIN "+
                      " SAM_ESTATUS_VALE D ON A.STATUS = D.ID_ESTATUS INNER JOIN "+
                      " SAM_CAT_TIPO_VALE E ON A.TIPO = E.CLAVE_VALE INNER JOIN CAT_RECURSO CR ON (CR.ID = A.ID_RECURSO)  where A.EJERCICIO=:ejercicio  ";
		
		if (estatus!=null && !estatus.equals("") )			
			sql += " AND a.STATUS in ("+estatus+")  ";
		
		
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
		
		/*if (verUnidad!=null && !verUnidad.equals("") ){
			if (!unidad.equals("")&&!unidad.equals("0"))
		      sql += " and A.ID_DEPENDENCIA = :unidad ";
		}
		else
			sql += " and A.CVE_PERS= :idUsuario ";	
		*/
		
		if (fInicial != null && fFinal != null ) 
			sql += " and convert(datetime,convert(varchar(10), A.FECHA ,103),103) between :fechaInicial and :fechaFinal ";
		
		if(tipoGasto!=null)
			if(!tipoGasto.equals("0"))
				sql += " and A.ID_RECURSO=:tipoGasto ";
		
		return this.getNamedJdbcTemplate().queryForList(sql+" ORDER BY A.NUM_VALE DESC",parametros);
	}
	
	public void actualizarEstatusVale( Long clave, Integer idStatus, Integer ejercicio, Integer cve_pers){
		Map vale = this.getJdbcTemplate().queryForMap("SELECT SAM_VALES_EX.*, (SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_MOV_VALES WHERE CVE_VALE =SAM_VALES_EX.CVE_VALE) AS IMPORTE FROM SAM_VALES_EX WHERE CVE_VALE = ? ", new Object[]{clave});
		//Integer sproyecto = 	Integer.parseInt(vale.get("ID_PROYECTO").toString());
		//String spartida = (String)vale.get("CLV_PARTID");
		
		//if(spartida==null) spartida ="";
		
		String folio=rellenarCeros(clave.toString(),6);
		String SQL = "UPDATE SAM_VALES_EX set  STATUS=?  where CVE_VALE=? ";
		if(idStatus!=5) this.getJdbcTemplate().update(SQL, new Object[]{idStatus,clave});
		//Guardar en la bitacora
		if(idStatus==0)
			gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_RECHAZAR, ejercicio, cve_pers, clave, folio, "VAL", (Date) vale.get("FECHA"), "", "", null, Double.parseDouble(vale.get("IMPORTE").toString()));
		if(idStatus==1) 
			gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_CERRAR, ejercicio, cve_pers, clave, folio, "VAL", (Date) vale.get("FECHA"), "", "", null, Double.parseDouble(vale.get("IMPORTE").toString()));
		if(idStatus==2)
			gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_CANCELA, ejercicio, cve_pers, clave, folio, "VAL", (Date) vale.get("FECHA"), "", "", null, Double.parseDouble(vale.get("IMPORTE").toString()));
		if(idStatus==4)
			gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_APLICAR, ejercicio, cve_pers, clave, folio, "VAL", (Date) vale.get("FECHA"), "", "", null, Double.parseDouble(vale.get("IMPORTE").toString()));
		if(idStatus==5){
			//libera todos los recursos en SAM_COMP_VALES
			//this.getJdbcTemplate().update("DELETE FROM SAM_COMP_VALES WHERE CVE_VALE = ?", new Object[]{clave});
			this.getJdbcTemplate().update("UPDATE SAM_VALES_EX set  STATUS=?  where CVE_VALE=? ", new Object[]{0,clave});
			gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_APERTURA, ejercicio, cve_pers, clave, folio, "VAL", (Date) vale.get("FECHA"), "", "", null, Double.parseDouble(vale.get("IMPORTE").toString()));
		}
	}
	
	  public List<Map> getValesPagadosNoComprobados(String unidad, int ejercicio ){
	    	return this.getJdbcTemplate().queryForList("SELECT  DISTINCT  A.NUM_VALE, A.CVE_VALE, A.EJERCICIO, A.ID_DEPENDENCIA,  A.CVE_PERS, A.CLV_BENEFI, A.ID_RECURSO, "+ 
					 " CONVERT(varchar(10), A.FECHA, 103) AS FECHA, A.TIPO, A.MES, (SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_MOV_VALES WHERE CVE_VALE = A.CVE_VALE) AS IMPORTE, A.JUSTIF, A.DOCTO_COMP, A.FECHA_INI, A.FECHA_FIN, A.FECHA_MAX, "+ 
                    " A.STATUS, A.POLIZA_CH, B.NCOMERCIA, C.DEPENDENCIA AS UNIADM, D.DESCRIPCION_ESTATUS, "+ 
                    " E.TIPO_VALE    "+
                    " FROM  SAM_VALES_EX AS A INNER JOIN SAM_MOV_VALES AS MV ON (MV.CVE_VALE = A.CVE_VALE) "+
                    " LEFT JOIN CEDULA_TEC AS CT ON (CT.ID_PROYECTO = MV.ID_PROYECTO) "+
                    " INNER JOIN CAT_BENEFI AS B ON A.CLV_BENEFI = B.CLV_BENEFI INNER JOIN "+
                    " CAT_DEPENDENCIAS AS C ON A.ID_DEPENDENCIA = C.ID INNER JOIN "+
                    " SAM_ESTATUS_VALE D ON A.STATUS = D.ID_ESTATUS INNER JOIN "+
                    " SAM_CAT_TIPO_VALE E ON A.TIPO = E.CLAVE_VALE  where A.EJERCICIO = ? and A.ID_DEPENDENCIA = ?  and A.STATUS=?  ", new Object []{ejercicio, unidad,getEstatusPagado()});	
	    }
	
	public Integer  getEstatusPendiente(){
		return this.VAL_ESTATUS_PENDIENTE;
	}
	
	public Integer  getEstatusNueva(){
		return this.VAL_ESTATUS_NUEVO;
	}
			
	public Integer  getEstatusCancelado(){
		return this.VAL_ESTATUS_CANCELADO;
	}
	
	public Integer  getEstatusPagado(){
		return this.VAL_ESTATUS_PAGADO;
	}	
	
	public boolean cambiarFechaPeriodo(final Long cve_vale, final String fechaNueva, final int periodo, final int cve_pers, final int ejercicio){
		try {   
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	Date fecha = formatoFecha(fechaNueva);
	            	/*Graba en bitacora el cambio*/
	            	Map val = getJdbcTemplate().queryForMap("SELECT NUM_VALE, CONVERT(varchar(10),FECHA,103) AS FECHA, IMPORTE, ID_PROYECTO, CLV_PARTID FROM SAM_VALES_EX WHERE CVE_VALE = ?", new Object[]{cve_vale});
	            	gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_ACTUALIZA, ejercicio, cve_pers, Long.parseLong(cve_vale.toString()), val.get("NUM_VALE").toString(), "VAL", fecha, ((val.get("ID_PROYECTO")!=null) ? val.get("ID_PROYECTO").toString():  null), ((val.get("CLV_PARTID")!=null) ? val.get("CLV_PARTID").toString(): null), "Cambió la fecha del Vale de "+val.get("FECHA").toString()+" a "+fechaNueva, Double.parseDouble(val.get("IMPORTE").toString()));
	            	/*Realiza el cambio*/
	            	getJdbcTemplate().update("UPDATE SAM_VALES_EX SET FECHA = ?, MES =? WHERE CVE_VALE = ?", new Object[]{fecha, periodo, cve_vale});
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, puede que la fecha este escrita incorrectamente, verifique nuevamente el formato y vuelta a intentar esta operación",e);
	   }	
	}
	

	public boolean moverVales(final Long[] cve_val, final int cve_pers_fuente, final int cve_pers_dest, final int ejercicio){
		try {   
			
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            	for(Long id: cve_val){
	            		/*Graba en bitacora el cambio*/
	            		Map Val = getJdbcTemplate().queryForMap("SELECT NUM_VALE, CONVERT(varchar(10),FECHA,103) AS FECHA, IMPORTE, ID_PROYECTO, CLV_PARTID FROM SAM_VALES_EX WHERE CVE_VALE = ?", new Object[]{id});
	            		Map persona = getJdbcTemplate().queryForMap("SELECT TOP 1 (SELECT '('+US1.LOGIN+') '+NOMBRE+' '+APE_PAT+' '+APE_MAT FROM PERSONAS INNER JOIN USUARIOS_EX AS US1 ON (US1.CVE_PERS = PERSONAS.CVE_PERS) WHERE US1.CVE_PERS = ?) AS PERSONA1, (SELECT '('+US1.LOGIN+') '+NOMBRE+' '+APE_PAT+' '+APE_MAT FROM PERSONAS INNER JOIN USUARIOS_EX AS US1 ON (US1.CVE_PERS = PERSONAS.CVE_PERS) WHERE US1.CVE_PERS = ?) AS PERSONA2 FROM USUARIOS_EX", new Object[]{cve_pers_fuente, cve_pers_dest});
		            	gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_ACTUALIZA, ejercicio, cve_pers_fuente, id, Val.get("NUM_VALE").toString(), "VAL", formatoFecha(Val.get("FECHA").toString()), (Val.get("ID_PROYECTO")!=null) ? Val.get("ID_PROYECTO").toString():null, (Val.get("CLV_PARTID")!=null) ? Val.get("CLV_PARTID").toString(): null, "Cambio de usuario en el documento de: "+persona.get("PERSONA1").toString()+" a: "+persona.get("PERSONA2").toString(),Double.parseDouble(Val.get("IMPORTE").toString()));
		            	/*Realiza el cambio*/
		            	getJdbcTemplate().update("UPDATE SAM_VALES_EX SET CVE_PERS = ?, ID_DEPENDENCIA =(SELECT TOP 1 ID_DEPENDENCIA FROM TRABAJADOR WHERE TRABAJADOR.CVE_PERS = ?)  WHERE CVE_VALE = ?", new Object[]{cve_pers_dest,cve_pers_dest, id});
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
			return this.getJdbcTemplate().queryForMap("SELECT NUM_VALE, CLV_BENEFI, (SELECT TOP 1 CAT_BENEFI.NCOMERCIA FROM CAT_BENEFI WHERE CAT_BENEFI.CLV_BENEFI = SAM_VALES_EX.CLV_BENEFI) AS BENEFICIARIO FROM SAM_VALES_EX WHERE CVE_VALE = ?", new Object[]{cve_doc}); 
		}
		catch (DataAccessException e) {     
	       return null;
	   }
	}
	
	public boolean cambiarBeneficiario(final Long cve_doc, final String clv_benefi, final int ejercicio, final int cve_pers){
		try {   
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {
	            		/*Comprobar algunos datos antes*/
		            	Map val = getJdbcTemplate().queryForMap("SELECT NUM_VALE, CONVERT(varchar(10),FECHA,103) AS FECHA, IMPORTE, ID_PROYECTO, CLV_PARTID, SAM_VALES_EX.CLV_BENEFI, C.NCOMERCIA "+  
																	"FROM SAM_VALES_EX "+  
																		"LEFT JOIN CAT_BENEFI AS C ON (C.CLV_BENEFI = SAM_VALES_EX.CLV_BENEFI) "+
																		"WHERE SAM_VALES_EX.CVE_VALE= ?", new Object[]{cve_doc});
		            	Map ben = getJdbcTemplate().queryForMap("SELECT NCOMERCIA FROM CAT_BENEFI WHERE CAT_BENEFI.CLV_BENEFI =?", new Object[]{clv_benefi});
	            		String texto ="";
	            		if(val.get("CLV_BENEFI")!=null)
	            			texto = "Cambió el beneficiario CLV_BENEFI: ['"+val.get("CLV_BENEFI").toString()+"' "+val.get("NCOMERCIA").toString()+"] a: ['"+clv_benefi+"' "+ben.get("NCOMERCIA").toString()+"]";
	            		else
	            			texto = "Cambió el beneficiario CLV_BENEFI: ['' ] a: ['"+clv_benefi+"' "+ben.get("NCOMERCIA").toString()+"]";
		            	/*Graba en bitacora el cambio*/
		            	gatewayBitacora.guardarBitacora(gatewayBitacora.OP_ACTUALIZAR, ejercicio, cve_pers, cve_doc, val.get("NUM_VALE").toString(), "VAL", formatoFecha(val.get("FECHA").toString()), val.get("ID_PROYECTO").toString(), ((val.get("CLV_PARTID")!=null)?  val.get("CLV_PARTID").toString():null), texto, Double.parseDouble(val.get("IMPORTE").toString()));
		            	/*Realiza el cambio*/
		            	getJdbcTemplate().update("UPDATE SAM_VALES_EX SET CLV_BENEFI = ? WHERE CVE_VALE = ?", new Object[]{clv_benefi, cve_doc});
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException("La operación ha fallado, el cambio de beneficiario no se ha podido realizar, intente editando el documento directamente",e);
	   }	
	  
	}
	
	public String agregarConcepto(Long idDetalle, Long cve_vale, Long idproyecto, String clv_partid, Double importe, String nota){
		if(idDetalle==null) 
			idDetalle = 0l;
			
			if(idDetalle==0){
				boolean existe = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_MOV_VALES WHERE ID_PROYECTO=? AND CLV_PARTID =? AND CVE_VALE=?", new Object[]{idproyecto, clv_partid, cve_vale})>0;
				if (existe)
					this.getJdbcTemplate().update("UPDATE SAM_MOV_VALES SET IMPORTE=IMPORTE+? WHERE ID_PROYECTO=? AND CLV_PARTID =? AND CVE_VALE=?", new Object[]{importe, idproyecto, clv_partid, cve_vale});
				else
					this.getJdbcTemplate().update("INSERT INTO SAM_MOV_VALES(CVE_VALE, ID_PROYECTO, CLV_PARTID, DESCRIPCION, IMPORTE) VALUES(?,?,?,?,?)", new Object[]{cve_vale, idproyecto, clv_partid, nota, importe });
			}
			else
			{
				this.getJdbcTemplate().update("UPDATE SAM_MOV_VALES SET ID_PROYECTO = ?, CLV_PARTID = ?, DESCRIPCION=?, IMPORTE=? WHERE ID_MOV_VALE =?", new Object[]{idproyecto, clv_partid, nota, importe, idDetalle });
			}
			return "";
	}
	
	public List<Map> getDetallesVales(Long cve_vale){
		return this.getJdbcTemplate().queryForList("SELECT DV.*, VP.PROG_PRESUP, CP.PARTIDA, VP.DECRIPCION AS DESCRIPCION_PROY, VP.K_PROYECTO_T AS PROYECTO,  CT.N_PROGRAMA, CT.ID_DEPENDENCIA, D.DEPENDENCIA, dbo.getClaveProgramatica (DV.ID_PROYECTO, DV.CLV_PARTID) AS CLAVE FROM SAM_MOV_VALES AS DV "+
															"LEFT JOIN CEDULA_TEC AS CT ON (CT.ID_PROYECTO = DV.ID_PROYECTO) "+
															"LEFT JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = CT.ID_PROYECTO) "+
															"LEFT JOIN CAT_PARTID AS CP ON (CP.CLV_PARTID = DV.CLV_PARTID) "+
															"LEFT JOIN CAT_DEPENDENCIAS AS D ON (D.ID = CT.ID_DEPENDENCIA) WHERE DV.CVE_VALE = ?", new Object[]{cve_vale});
	}
	
	public void eliminarDetalles(final Long[] idDetalles, Long cve_vale){
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	for(Long id: idDetalles){
            		getJdbcTemplate().update("DELETE FROM SAM_MOV_VALES WHERE ID_MOV_VALE =?", new Object[]{id});
            	}
            } 
        });
		
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

	public int getCountArchivos(Long cve_vale)
	{	
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_VALES_ARCHIVOS WHERE CVE_VALE = ?", new Object[]{cve_vale});
	}
	
	public Long guardarArchivo(Long cve_vale, String nombreArchivo, String path, Date fecha, String ext, Long size){
		this.getJdbcTemplate().update("INSERT INTO SAM_VALES_ARCHIVOS(CVE_VALE, NOMBRE, RUTA, FECHA, EXT, TAMANO) VALUES(?,?,?,?,?,?)", new Object[]{
				cve_vale,
				nombreArchivo,
				path, 
				fecha,
				ext,
				size
		});
		
		return this.getJdbcTemplate().queryForLong("SELECT MAX(ID_ARCHIVO) AS N FROM SAM_VALES_ARCHIVOS");
	}
	
	public List<Map> getArchivosVale(Long cve_vale){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SAM_VALES_ARCHIVOS WHERE CVE_VALE=?", new Object[]{cve_vale});
	}
	
	public void eliminarArchivoVale(int cve_pers, Long idArchivo, HttpServletRequest request){
		//si tiene los privilegios elimina
		//if(!getPrivilegioEn(cve_pers, 129))
		//	throw new RuntimeException("No se puede eliminar el archivo, su usuario no cuenta con los privilegios suficientes");
		
		Map archivo = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_VALES_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		/*eliminado fisico*/
		File fichero = new File(request.getSession().getServletContext().getRealPath("")+"/sam/vales/archivos/["+archivo.get("ID_ARCHIVO")+"] "+archivo.get("NOMBRE").toString());
	   
		if (fichero.delete()){
			 System.out.println("El fichero ha sido borrado satisfactoriamente");
			 this.getJdbcTemplate().update("DELETE FROM SAM_VALES_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		}
		else
		{
			this.getJdbcTemplate().update("DELETE FROM SAM_VALES_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
			System.out.println("El fichero no puede ser borrado " + fichero.getAbsolutePath());
		}
	}
	
	public String getArchivoAnexoVale(int cve_vale)
	{
		if(this.getJdbcTemplate().queryForInt("SELECT count(*) FROM SAM_VALES_ARCHIVOS WHERE CVE_VALE = ?", new Object[]{cve_vale})>0)
			return (String) this.getJdbcTemplate().queryForObject("SELECT TOP 1 '[' + CONVERT(NVARCHAR, ID_ARCHIVO) + '] ' + NOMBRE FROM SAM_VALES_ARCHIVOS WHERE CVE_VALE = ?", new Object[]{cve_vale}, String.class);
		else
			return "";
	}
	
}
