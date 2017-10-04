/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;


import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayComprobacionesVales extends BaseGateway  {
	private static Logger log = Logger.getLogger(GatewayComprobacionesVales.class.getName());
	@Autowired
	public GatewayBitacora gatewayBitacora;
	
	
	public GatewayComprobacionesVales() {
		
	}
	
	final  int VAL_ESTATUS_COMPROBADO =3;
	final  int VAL_ESTATUS_PAGADO =4;
	
public void insertaVale(Integer idCons,Integer vale,double importe,Long idOrden, String tipo, Date fecha, Date fechaDeposito, int ejercicio, int cve_pers, int proyecto, String clv_partid ){
	try{
		//obtener el vale 
		idCons = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM COMP_VALES WHERE CVE_VALE = ?", new Object[]{vale})+1;
		Map Detvale = this.getJdbcTemplate().queryForMap("SELECT *, (SELECT ISNULL(SUM(M.IMPORTE),0) FROM SAM_MOV_VALES AS M WHERE M.CVE_VALE = SAM_VALES_EX.CVE_VALE) AS IMPORTE, ((SELECT ISNULL(SUM(M.IMPORTE),0) FROM SAM_MOV_VALES AS M WHERE M.CVE_VALE = SAM_VALES_EX.CVE_VALE) -(ISNULL((SELECT SUM(COMP_VALES.IMPORTE) FROM COMP_VALES WHERE COMP_VALES.CVE_VALE = SAM_VALES_EX.CVE_VALE),0)))AS IMPORTE_ANTERIOR , ISNULL((SELECT SUM(COMP_VALES.IMPORTE) FROM COMP_VALES WHERE COMP_VALES.CVE_VALE = SAM_VALES_EX.CVE_VALE),0) AS SALDO_PENDIENTE FROM SAM_VALES_EX WHERE CVE_VALE = ?", new Object[]{vale});
		this.getJdbcTemplate().update("insert into COMP_VALES (CVE_OP, CVE_VALE, CONS_VALE, TIPO, IMPORTE, IMP_ANTERIOR, IMP_PENDIENTE, FECHA, FE_DEPOSI, ID_PROYECTO, CLV_PARTID) " +
				"VALUES (?,?,?,?,?,?,?,?,?,?,?)"
				, new Object[]{idOrden,vale,idCons,tipo,importe, Detvale.get("IMPORTE_ANTERIOR"), (Double.parseDouble(Detvale.get("IMPORTE").toString())- (Double.parseDouble(Detvale.get("SALDO_PENDIENTE").toString())+importe)), fecha,fechaDeposito, proyecto, clv_partid});
		if(idOrden!=null){
			String folio=rellenarCeros(idOrden.toString(),6);
			//guarda en la bitacora
			gatewayBitacora.guardarBitacora(GatewayBitacora.OP_MOV_AGREGO_VALES, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Cve_vale: "+vale+ " Cons: "+idCons, importe);
		}
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}
}

public void actualizarVale(Integer idVale,Double importe, int ejercicio, int cve_pers, Long idOrden){
	try{
		this.getJdbcTemplate().update("UPDATE COMP_VALES  SET  IMPORTE = ?, FECHA = GETDATE(), IMP_PENDIENTE = (SELECT ISNULL((SELECT IMPORTE FROM SAM_VALES_EX WHERE SAM_VALES_EX.CVE_VALE = COMP_VALES.CVE_VALE),0)-(ISNULL((SELECT SUM(A.IMPORTE) FROM COMP_VALES A WHERE A.CVE_VALE = COMP_VALES.CVE_VALE AND A.ID_VALE <> COMP_VALES.ID_VALE),0)+ISNULL(CAST('"+importe.toString() +"' AS MONEY),0)) AS PENDIENTE) WHERE COMP_VALES.ID_VALE = ?"
				, new Object[]{importe, idVale});
		if(idOrden!=null){
			//guarda en la bitacora
			String folio=rellenarCeros(idOrden.toString(),6);
			gatewayBitacora.guardarBitacora(GatewayBitacora.OP_MOV_ACTUALIZA_VALES, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Id_Vale: "+idVale, importe);
		}
	}
	catch ( DataAccessException e) {
		log.info(e.getMessage());
	}
}

public  String actualizarConceptoPrincipalVale(Integer idVale,Integer vale,double importe,double importeValeAnte,Long idOrden, int idproyecto, String partida, String tipo,  Date fecha, Date fechaDeposito, int ejercicio, int cve_pers){
	
	//Comprobar que el vale no sobrepasa en lo comprobado al neto de la OP en proyecto y partida
	if(idOrden!=null){
		//Correccion agregada en codigo SQL [AND CVE_VALE =?] para filtrar desde lista de anexos de vales en multiples vales
		Double neto_op = (Double) this.getJdbcTemplate().queryForObject("SELECT "+
																				"ROUND((ISNULL((SELECT SUM(MONTO) FROM SAM_MOV_OP WHERE CVE_OP =  M.CVE_OP AND ID_PROYECTO = M.ID_PROYECTO AND CLV_PARTID = M.CLV_PARTID),0) - "+
																				"ISNULL((SELECT SUM(IMPORTE) FROM COMP_VALES WHERE CVE_OP = M.CVE_OP AND ID_PROYECTO = M.ID_PROYECTO AND CLV_PARTID = M.CLV_PARTID AND ID_VALE<>ISNULL(?,0)),0)),2) AS NETO "+ 
																		"FROM SAM_MOV_OP AS M "+
																		"WHERE CVE_OP =? AND ID_PROYECTO = ? AND CLV_PARTID = ? AND "+vale+" IN( SELECT M.CVE_VALE FROM SAM_MOV_VALES AS M INNER JOIN SAM_VALES_EX AS V ON (V.CVE_VALE = M.CVE_VALE) WHERE M.ID_PROYECTO = ? AND M.CLV_PARTID = ? AND V.STATUS=4)", new Object[]{vale, idOrden, idproyecto, partida, idproyecto, partida}, Double.class);
				
		Double dif = neto_op-importe;
		if((idVale==null)&&importe>neto_op||(dif<0)) return "El importe a comprobar del Vale es mayor al importe Neto restante de la Orden de Pago";
	}
	
	  actualizarConcepto(vale,importe,idproyecto,partida,importeValeAnte);		           		  
	  if (idVale == null )
	       insertaVale(1,vale,importe,idOrden,tipo,fecha, fechaDeposito, ejercicio, cve_pers, idproyecto, partida);		        
	  else 
		   actualizarVale(idVale,importe, ejercicio, cve_pers, idOrden);
	  return "";
}



public void actualizarConcepto(Integer idVale,double importe, int proyecto, String partida,double importeValeAnte){
	boolean existe=false;
	existe=this.getJdbcTemplate().queryForInt("select count(*) from CONCEP_VALE where CVE_VALE=? AND ID_PROYECTO=? AND CLV_PARTID =?", new Object[]{idVale, proyecto, partida}) != 0;
	BigDecimal importeVale =(BigDecimal)this.getJdbcTemplate().queryForObject("select (SELECT ISNULL(SUM(M.IMPORTE),0) FROM SAM_MOV_VALES AS M WHERE M.CVE_VALE = V.CVE_VALE) AS IMPORTE from SAM_VALES_EX AS V where V.CVE_VALE=? ", new Object[]{idVale},BigDecimal.class);
	if (!existe) {
		   insertaConceptoVale(idVale,importe,importeVale.longValue(),proyecto, partida);	      
	    }
	  else {
		   actualizarConceptoVale(idVale,importe,importeValeAnte, proyecto, partida);		  
	  }
}

	public void insertaConceptoVale(Integer vale,double importe,double importeVale,int proyecto, String partida){
		//Busca el siguiente numero de concepto 
		int cons = (this.getJdbcTemplate().queryForInt("SELECT MAX(CONS_VALE) FROM CONCEP_VALE WHERE CVE_VALE = ?", new Object[]{vale}))+1;
		this.getJdbcTemplate().update("insert into CONCEP_VALE (CVE_VALE,CONS_VALE,ID_PROYECTO,CLV_PARTID,MES,IMPORTE,DESCONTADO) " +
				"VALUES (?,?,?,?,Month(GETDATE()),?,?)"
				, new Object[]{vale,cons, proyecto,partida,importeVale,importe});
	}
	
	public void verficaComprobacion(Integer vale){
		Double importe = (Double)this.getJdbcTemplate().queryForObject("select IMPORTE - DESCONTADO  from  CONCEP_VALE  where CVE_VALE=? and CONS_VALE=1 ",new Object []{vale},Double.class);
		if (importe.doubleValue()== 0 )
		   this.getJdbcTemplate().update("update SAM_VALES_EX   set  STATUS=? where   CVE_VALE=? ", new Object[]{ VAL_ESTATUS_COMPROBADO,vale});
		else
			this.getJdbcTemplate().update("update SAM_VALES_EX  set  STATUS=? where   CVE_VALE=?  ", new Object[]{ VAL_ESTATUS_PAGADO,vale});
	} 

	/*  -------------------      Actualiza los detalles del vale en la tabla concep vale         ------------------------------*/
	public void actualizarConceptoVale(Integer vale,double importeVale, double importeValeAnte, int proyecto, String partida){	
		this.getJdbcTemplate().update("update CONCEP_VALE  set  DESCONTADO=DESCONTADO+?-?   where  CONS_VALE=1 AND CVE_VALE=? AND ID_PROYECTO =? AND CLV_PARTID =? "
				, new Object[]{importeVale,importeValeAnte,vale, proyecto, partida});
	}
	
	 public List<Map> getComprobacionesVales(Integer idVale){
	    	return this.getJdbcTemplate().queryForList("SELECT CVE_VALE,CONS_VALE,CVE_OP,TIPO,IMPORTE, COMP_VALES.ID_PROYECTO, C.N_PROGRAMA, CLV_PARTID ,CONVERT(varchar(10),COMP_VALES.FECHA, 103) FECHA, CONVERT(varchar(10),FE_DEPOSI, 103)  FE_DEPOSI , ID_VALE  from COMP_VALES INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = COMP_VALES.ID_PROYECTO) where CVE_VALE=? ",new Object[]{idVale});	
	    }

	 public void eliminarVale(Integer idVale, Long  idOrden, int ejercicio, int cve_pers ){
		 try{
			 String folio=rellenarCeros(idOrden.toString(),6);
			    getJdbcTemplate().update("update r  set r.DESCONTADO=r.DESCONTADO-a.importe  from CONCEP_VALE r , comp_vales a where  r.CONS_VALE= a.CONS_VALE AND r.CVE_VALE=a.CVE_VALE and a.ID_VALE=?  ", new Object[]{idVale});
				this.getJdbcTemplate().update("delete from COMP_VALES where ID_VALE=?  ", new Object[]{idVale});
				//guarda en la bitacora
				gatewayBitacora.guardarBitacora(GatewayBitacora.OP_MOV_ELIMINA_VALE, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Id_Vale: "+idVale, 0D);
			}
			catch ( DataAccessException e) {
				log.info(e.getMessage());
			}
		}
	 
	 
	 public List <Map> getMovimientoVales(Long cve_vale){
		 try{
			 return this.getJdbcTemplate().queryForList("SELECT ('['+CONVERT(VARCHAR,M.ID_PROYECTO)+'] '+C.N_PROGRAMA+' / '+M.CLV_PARTID+' = $'+CONVERT(VARCHAR, ISNULL(M.IMPORTE,0))) AS PROYECTOPARTIDA, (CONVERT(VARCHAR, M.ID_PROYECTO)+'/'+M.CLV_PARTID) AS VALOR FROM SAM_MOV_VALES AS M INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = M.ID_PROYECTO) WHERE M.CVE_VALE = ?", new Object[]{cve_vale});
		 }
		 catch ( DataAccessException e) {
				log.info(e.getMessage());
				return null;
			}
	   }
	 
	 public Map getDatosVale(Long cve_vale, int idproyecto, String clv_partid){
		Map m = this.getJdbcTemplate().queryForMap("SELECT TOP 1 "+
																"(SELECT ISNULL(SUM(IMPORTE),0)  FROM COMP_VALES WHERE CVE_VALE = V.CVE_VALE AND ID_PROYECTO = ? AND CLV_PARTID = ?) AS COMPROBADO,"+ 
																"(SELECT ISNULL(SUM(IMPORTE),0)  FROM SAM_MOV_VALES WHERE CVE_VALE = V.CVE_VALE AND ID_PROYECTO = ? AND CLV_PARTID = ?) AS TOTAL FROM SAM_VALES_EX AS V "+ 
														"WHERE V.CVE_VALE = ? ", new Object[]{idproyecto, clv_partid, idproyecto, clv_partid, cve_vale});
		m.put("RESTANTE", Double.parseDouble(m.get("TOTAL").toString()) - Double.parseDouble(m.get("COMPROBADO").toString()));
		return m;
	 }
	 
}
