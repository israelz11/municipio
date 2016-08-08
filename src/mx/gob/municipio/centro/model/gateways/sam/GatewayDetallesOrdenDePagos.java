/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayDetallesOrdenDePagos extends BaseGateway  {

@Autowired
public GatewayBitacora gatewayBitacora;
@Autowired
public GatewayOrdenDePagos gatewayOrdenDePagos;

private static Logger log = Logger.getLogger(GatewayDetallesOrdenDePagos.class.getName());

public GatewayDetallesOrdenDePagos() {
		
}

public List<Map> getDetallesOrdenes(Long IdOrden) {	   
	   return this.getJdbcTemplate().queryForList("SELECT CVE_OP, CVE_FACTURA, CVE_VALE, CAT_DEPENDENCIAS.ID, CAT_DEPENDENCIAS.DEPENDENCIA, CEDULA_TEC.ID_PROYECTO, CEDULA_TEC.N_PROGRAMA, SAM_MOV_OP.CLV_PARTID,MONTO,NOTA,CEDULA_TEC.N_PROGRAMA +'-'+SAM_MOV_OP.CLV_PARTID AS PROYECTOPARTIDA , ID_MOV_OP, TIPO  FROM SAM_MOV_OP LEFT OUTER JOIN CEDULA_TEC ON (CEDULA_TEC.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) LEFT OUTER JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = CEDULA_TEC.ID_DEPENDENCIA) where CVE_OP=?", new Object[]{IdOrden});
}

public boolean esOrdenTipoReintegros(Long idOrden){
	
int 	tipo=  this.getJdbcTemplate().queryForInt("select count(*) from SAM_ORD_PAGO where CVE_OP = ? and TIPO=5  ", new Object[]{idOrden});
return tipo > 0;
}

public  boolean  actualizarPrincipalDetalle(Integer idDetalle,Long idOrden,String proyecto, String partida, String nota, Double importe, Long idVale, int ejercicio, int cve_pers, String tipo){
	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	if(getPrivilegioEn(cve_pers, 114)){
		throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	}
	
	boolean exito = true;
	if (esOrdenTipoReintegros(idOrden))
		importe = importe *- 1;
	
	Map op = this.gatewayOrdenDePagos.getOrden(idOrden);
	
	//Si la OP es de fondo fijo validar que el capitulo 5000 no se mezcle con los demas
	if(op.get("TIPO").toString().equals("9"))
	{
		boolean cap5000 = false;
		List<Map> conceptos = this.gatewayOrdenDePagos.getMovimientosOP(idOrden);
		int capitulo = this.getJdbcTemplate().queryForInt("SELECT CLV_CAPITU FROM CAT_PARTID WHERE CLV_PARTID =?", new Object[]{partida});
		
		for(Map concepto: conceptos)
		{
			if(Integer.parseInt(concepto.get("CLV_CAPITU").toString())==5000)
				cap5000 = true;
			
			if(Integer.parseInt(concepto.get("CLV_CAPITU").toString()) != 5000 && capitulo==5000)
				throw new RuntimeException("No se puede mezclar el capitulo 5000 con otros capitulos");
		}
		
		if(cap5000)
			throw new RuntimeException("No se puede mezclar el capitulo 5000 con otros capitulos");
	}
	
	if (idDetalle == null){
		  int numeroDetalle = getJdbcTemplate().queryForInt("SELECT  COUNT(*) FROM SAM_MOV_OP WHERE ID_PROYECTO = ? AND CLV_PARTID = ? AND CVE_OP = ? ",new Object []{proyecto,partida,idOrden}); 
		  if (numeroDetalle == 0)
			 inserta(idOrden,proyecto, partida, nota, importe, ejercicio, cve_pers, tipo, idVale);
		  else{
			  //Orden de pago tipo de vales, insertar detalles extra 09/Nov/12
			  if(this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_ORD_PAGO WHERE CVE_OP=? AND TIPO=?", new Object[]{idOrden, 11})==1){
				  inserta(idOrden,proyecto, partida, nota, importe, ejercicio, cve_pers, tipo, idVale);
			  }
			  else
			   exito = false;
		  }
					  
	  }
	  else
		  actualizar(idDetalle,proyecto, partida, nota, importe, ejercicio, cve_pers, tipo, idVale);
	  return exito;
}

	public void inserta(Long idOrden,String proyecto, String partida, String nota, Double importe, int ejercicio, int cve_pers, String tipo, Long idVale ){
		try{
			String folio=rellenarCeros(idOrden.toString(),6);
			this.getJdbcTemplate().update("insert into SAM_MOV_OP (CVE_OP,ID_PROYECTO,CLV_PARTID,NOTA,MONTO, TIPO, CVE_VALE ) " +
					"VALUES (?,?,?,?,?,?,?)"
					, new Object[]{idOrden,proyecto, partida, (idVale!=0) ? "Concepto de Vale: "+this.rellenarCeros(idVale.toString(), 6): nota, importe, tipo, (idVale!=0) ? idVale: null});
			//Guardar en bitacora
			gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_AGREGO_CONCEPTO, ejercicio, cve_pers, idOrden, folio, "OP", null, proyecto, partida.toString(), "Proyecto id: "+proyecto+ " Clv_Partid: "+partida+" Descripcion: "+nota, importe);
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
		}
	}

	public void actualizar(Integer idDetalle,String proyecto, String partida, String nota, Double importe,int ejercicio, int cve_pers, String tipo, Long idVale  ){
		try{
			Long op = this.getJdbcTemplate().queryForLong("SELECT CVE_OP FROM SAM_MOV_OP WHERE ID_MOV_OP = ?", new Object[]{idDetalle});
			String folio=rellenarCeros(op.toString(),6);
			this.getJdbcTemplate().update("update SAM_MOV_OP  set ID_PROYECTO=?,CLV_PARTID=?,NOTA=?,MONTO=?, TIPO=?  where ID_MOV_OP=? "
					, new Object[]{proyecto, partida, (idVale!=null) ? "Concepto de Vale: "+this.rellenarCeros(idVale.toString(), 6): nota, importe, tipo, idDetalle});
			//Guardar en bitacora
			gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_ACTUALIZA_CONCEPTO, ejercicio, cve_pers, op, folio, "OP", null, proyecto, partida.toString(), "Proyecto id: "+proyecto+ " Clv_Partid: "+partida+" Descripcion: "+nota, importe);
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
		}
	}

	public List getDetalleOrdenReporte(Long idOrden) {	 
		List lista1 = this.getJdbcTemplate().queryForList("SELECT  B.CODIGO_FF, B.ID_PROYECTO, B.CLV_ACTINST,   B.N_PROGRAMA, A.CLV_PARTID, B.CLV_PROGRAMA, NULL AS CLV_ACTIVIDAD, B.CLV_FINALIDAD, B.CLV_FUNCION, B.CLV_SUBFUNCION, B.CLV_SUBSUBFUNCION, B.CLV_UNIADM, "+
																	"A.MONTO, C.PARTIDA, C.CLV_CAPITU, CAT_DEPENDENCIAS.CLV_UNIADM, CAT_DEPENDENCIAS.DEPENDENCIA, 'OP' TIPO ,B.CLV_LOCALIDAD, CAT_LOCALIDAD.LOCALIDAD, '304' AS MPIO, B.PROG_PRESUP AS DESC_PROY, B.DECRIPCION AS DESC_PROG, K_PROYECTO_T AS PROYECTO "+ 
															"FROM dbo.SAM_MOV_OP A "+
																	"INNER JOIN VPROYECTO B ON (A.ID_PROYECTO = B.ID_PROYECTO) "+ 
																	"INNER JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = B.ID_DEPENDENCIA) "+ 
																	"INNER JOIN CAT_PARTID  C ON (A.CLV_PARTID = C.CLV_PARTID) "+
																	"INNER JOIN CAT_LOCALIDAD ON (CAT_LOCALIDAD.CLV_LOCALIDAD = B.CLV_LOCALIDAD) "+ 
															"WHERE A.CVE_OP=? ORDER BY B.N_PROGRAMA, A.CLV_PARTID ASC", new Object[]{idOrden});
		List lista2 = this.getJdbcTemplate().queryForList("SELECT NULL AS CODIGO_FF, NULL N_PROGRAMA,  NULL CLV_PARTID, NULL CLV_PROGRAMA, NULL CLV_ACTIVIDAD, NULL CLV_FINALIDAD, NULL CLV_FUNCION, NULL CLV_SUBFUNCION, NULL CLV_SUBSUBFUNCION, NULL CLV_UNIADM, "+ 
				   " M.IMPORTE MONTO , R.RETENCION PARTIDA , NULL CLV_UNIADM, NULL DEPENDENCIA,  'RT' TIPO ,NULL CLV_LOCALIDAD, NULL LOCALIDAD, NULL MPIO, NULL DESC_PROY, NULL AS DESC_PROG, NULL AS PROYECTO "+ 
				   " FROM MOV_RETENC M, CAT_RETENC R   where R.CLV_RETENC=M.CLV_RETENC   and M.CVE_OP= ?  ", new Object[]{idOrden});
		lista1.addAll(lista2);
		return lista1;
	}

	public void eliminar(Integer idDetalle, int ejercicio, int cve_pers ){
		try{
			Map det = this.getJdbcTemplate().queryForMap("SELECT SAM_MOV_OP.* FROM SAM_MOV_OP WHERE ID_MOV_OP = ?", new Object[]{idDetalle});
			String folio=rellenarCeros(det.get("CVE_OP").toString(),6);
			//Guardar en bitacora
			gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_ELIMINA_CONCEPTO, ejercicio, cve_pers, Long.parseLong(det.get("CVE_OP").toString()), folio, "OP", null, det.get("ID_PROYECTO").toString(),det.get("CLV_PARTID").toString(), "Id_Detalle: "+idDetalle+ " Proyecto: "+det.get("ID_PROYECTO").toString() + "Clv_Partid: "+det.get("CLV_PARTID").toString()+" Descripcion: "+det.get("NOTA").toString(), Double.parseDouble(det.get("MONTO").toString()));
			this.getJdbcTemplate().update("delete from SAM_MOV_OP where ID_MOV_OP= ?  ", new Object[]{idDetalle});
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
		}
	}
	
	public void generarDetallesOT(List<Integer> detallesOT, Long idOrden, int ejercicio, int cve_pers){
		String folio=rellenarCeros(idOrden.toString(),6);
		for (Integer documento :detallesOT){	
			
	   		 Map detalleReq = getJdbcTemplate().queryForMap("select a.ID_PROYECTO,a.CLV_PARTID, a.NUM_REQ,isnull ( (select sum (cantidad * precio_est ) from SAM_REQ_MOVTOS where cve_req=a.cve_req  ),0) IMPORTE  from SAM_REQUISIC a where a.cve_req =?  ",new Object []{documento});
	   		 String proyecto=detalleReq.get("ID_PROYECTO").toString();
	   		 String cuenta=detalleReq.get("CLV_PARTID").toString();
	   		 String nota= "Soporta la requisicion No.  "+(String)detalleReq.get("NUM_REQ"); 
	   		 BigDecimal importe=(BigDecimal)detalleReq.get("IMPORTE");
	   		 List detalleOP = getJdbcTemplate().queryForList("select ID_MOV_OP from SAM_MOV_OP  where CVE_OP=?  and ID_PROYECTO=? and CLV_PARTID=? ",new Object []{idOrden,proyecto,cuenta});
	   		 Integer idDetalleOp=null;
	   		 if (detalleOP.size()>0) {
	   			 Map detalle = (Map)detalleOP.get(0);
	   			 idDetalleOp=Integer.parseInt(detalle.get("ID_MOV_OP").toString());
	   		 }
	   		 if (detalleOP.size()>1)
	   			 throw new RuntimeException("La orden de pago tiene problemas con sus detalles");
	   		 if (idDetalleOp==null )	 	                		 
	   			 getJdbcTemplate().update("insert into SAM_MOV_OP (CVE_OP,ID_PROYECTO,CLV_PARTID,MONTO,NOTA,TIPO) values (?,?,?,?,?,'REQUISICION') ", new Object []{idOrden,proyecto,cuenta,importe,nota});
	   		 else
	   			 getJdbcTemplate().update("update SAM_MOV_OP set  MONTO= MONTO+ ? , NOTA=NOTA+'\n '+? WHERE ID_MOV_OP=? ", new Object []{importe,nota,idDetalleOp});	 
	   		 
	   		 getJdbcTemplate().update("insert into SAM_OP_COMPROBACIONES (CVE_OP,CVE_REQ) values (?,?) ", new Object []{idOrden,documento});	                		 
	   		 getJdbcTemplate().update("update SAM_REQUISIC set STATUS=2  WHERE cve_req=? ", new Object []{documento});
	   		 //quitar la clave del requisicion por si las moscas el usuario no comprueba
	   		 getJdbcTemplate().update("update SAM_ORD_PAGO set CVE_REQ = ?  WHERE CVE_OP = ?", new Object []{null, idOrden});
	   		 
	   		//Guardar en bitacora
	   		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_AGREGO_CONCEPTO, ejercicio, cve_pers, idOrden, folio, "OP", null, proyecto, cuenta, "Proyecto: "+proyecto+ " Clv_Partid: "+cuenta+" Descripcion: "+nota, importe.doubleValue());
		}
	}
	
	public void generarDetallesRequisicionParcial(Long cve_req, Long idOrden, Double importe_op, int ejercicio, int cve_pers){
		String folio=rellenarCeros(idOrden.toString(),6);
   		 Map detalleReq = getJdbcTemplate().queryForMap("SELECT ID_PROYECTO, TIPO, CLV_PARTID, NUM_REQ, ((SELECT ISNULL(SUM(R.CANTIDAD*R.PRECIO_EST),0) FROM SAM_REQ_MOVTOS R WHERE R.CVE_REQ = SAM_REQUISIC.CVE_REQ) - (SELECT ISNULL(SUM(M.MONTO),0) FROM SAM_MOV_OP AS M INNER JOIN SAM_ORD_PAGO AS O ON(O.CVE_OP = M.CVE_OP) WHERE O.CVE_REQ = SAM_REQUISIC.CVE_REQ AND O.STATUS NOT IN (-1,4))) AS IMPORTE FROM SAM_REQUISIC WHERE CVE_REQ = ?",new Object []{cve_req});
   		 Integer proyecto = Integer.parseInt(detalleReq.get("ID_PROYECTO").toString());
   		 String cuenta=(String)detalleReq.get("CLV_PARTID");
   		 String nota= "Soporta la requisicion No.  "+(String)detalleReq.get("NUM_REQ");
   		 BigDecimal importe=(BigDecimal)detalleReq.get("IMPORTE");
   		 Double diferencia = importe.doubleValue() - importe_op;
  		 if(importe.doubleValue()>importe_op) importe = new BigDecimal(importe_op);
  		 
   		 List detalleOP = getJdbcTemplate().queryForList("select ID_MOV_OP from SAM_MOV_OP  where CVE_OP=?  and ID_PROYECTO=? and CLV_PARTID=? ",new Object []{idOrden,proyecto,cuenta});
   		 Integer idDetalleOp=null;
   		 if (detalleOP.size()>0) {
   			 Map detalle = (Map)detalleOP.get(0);
   			 idDetalleOp=Integer.parseInt(detalle.get("ID_MOV_OP").toString());
   		 }
   		 if (detalleOP.size()>1)
   			 throw new RuntimeException("La orden de pago tiene problemas con sus detalles");
   		 if (idDetalleOp==null )	 	                		 
   			 getJdbcTemplate().update("insert into SAM_MOV_OP (CVE_OP,ID_PROYECTO,CLV_PARTID,MONTO,NOTA,TIPO) values (?,?,?,?,?,'REQUISICION') ", new Object []{idOrden,proyecto,cuenta,importe.doubleValue(),nota});
   		 else
   			 getJdbcTemplate().update("update SAM_MOV_OP set  MONTO= MONTO+ ? , NOTA=NOTA+'\n '+? WHERE ID_MOV_OP=? ", new Object []{importe,nota,idDetalleOp});	 
   		 
   		 getJdbcTemplate().update("insert into SAM_OP_COMPROBACIONES (CVE_OP,CVE_REQ) values (?,?) ", new Object []{idOrden,cve_req});
   		 
   		 getJdbcTemplate().update("update SAM_REQUISIC set STATUS=1  WHERE CVE_REQ=?", new Object []{cve_req});
   		 
   		 //integrar a la orden de pago la clave del pedido
   		 getJdbcTemplate().update("update SAM_ORD_PAGO set CVE_REQ = ?  WHERE CVE_OP = ?", new Object []{cve_req, idOrden});
   		 
   		//Guardar en bitacora
   		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_AGREGO_CONCEPTO, ejercicio, cve_pers, idOrden, folio, "OP", null, proyecto.toString(), cuenta, "Proyecto: "+proyecto+ " Clv_Partid: "+cuenta+" Descripcion: "+nota, importe.doubleValue());
		
	}
	
	public void generarDetallesPedidos(List<Integer> detallesPedido, Long idOrden, int ejercicio, int cve_pers){
		String folio=rellenarCeros(idOrden.toString(),6);
		for (Integer documento :detallesPedido){	                		
	   		 Map detalleReq = getJdbcTemplate().queryForMap("select b.CVE_REQ, b.ID_PROYECTO,b.CLV_PARTID PARTIDA, a.NUM_PED,a.TOTAL  from SAM_PEDIDOS_EX a , SAM_REQUISIC b where  a.CVE_REQ=b.CVE_REQ and a.CVE_PED =?  ",new Object []{documento});
	   		 String proyecto=(String)detalleReq.get("PROYECTO");
	   		 String cuenta=(String)detalleReq.get("PARTIDA");
	   		 String nota= "Soporta el Pedido No.  "+(String)detalleReq.get("NUM_PED"); 
	   		 BigDecimal importe=(BigDecimal)detalleReq.get("TOTAL");
	   		 List detalleOP = getJdbcTemplate().queryForList("select ID_MOV_OP from SAM_MOV_OP  where CVE_OP=?  and PROYECTO=? and CLV_PARTID=? ",new Object []{idOrden,proyecto,cuenta});
	   		 Integer idDetalleOp=null;
	   		 if (detalleOP.size()>0) {
	   			 Map detalle = (Map)detalleOP.get(0);
	   			 idDetalleOp=((BigDecimal)detalle.get("ID_MOV_OP")).intValue();
	   		 }
	   		 if (detalleOP.size()>1)
	   			 throw new RuntimeException("La orden de pago tiene problemas con sus detalles");
	   		 if (idDetalleOp==null )	 	                		 
	   			 getJdbcTemplate().update("insert into SAM_MOV_OP (CVE_OP,ID_PROYECTO,CLV_PARTID,MONTO,NOTA,TIPO) values (?,?,?,?,?,'PEDIDO') ", new Object []{idOrden,proyecto,cuenta,importe,nota});
	   		 else
	   			 getJdbcTemplate().update("update SAM_MOV_OP set  MONTO= MONTO+ ? , NOTA=NOTA+'\n '+? WHERE ID_MOV_OP=? ", new Object []{importe,nota,idDetalleOp});	 
	   		 
	   		 getJdbcTemplate().update("insert into SAM_OP_COMPROBACIONES (CVE_OP, CVE_PED, CVE_REQ) values (?,?,?)", new Object []{idOrden,documento, detalleReq.get("CVE_REQ")});	                		 
	   		 getJdbcTemplate().update("update SAM_PEDIDOS_EX set STATUS=4  WHERE CVE_PED=? ", new Object []{documento});
	   		 //por si las moscas
	   		 getJdbcTemplate().update("update SAM_ORD_PAGO set CVE_PED = ?  WHERE CVE_OP = ?", new Object []{null, idOrden});
	   		 //Guardar en bitacora
	   		 gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_AGREGO_CONCEPTO, ejercicio, cve_pers, idOrden, folio, "OP", null, proyecto, cuenta, "Proyecto: "+proyecto+ " Clv_Partid: "+cuenta+" Descripcion: "+nota, importe.doubleValue());
   	     
		}
	}
	
	public void generarDetallesPedidosParcial(Long cve_ped, Long idOrden, Double importe_op, int ejercicio, int cve_pers){
			String folio=rellenarCeros(idOrden.toString(),6);
	   		 Map detalleReq = getJdbcTemplate().queryForMap("select b.CVE_REQ, b.ID_PROYECTO,b.CLV_PARTID PARTIDA, a.NUM_PED, (a.TOTAL - (SELECT ISNULL(SUM(M.MONTO),0) FROM SAM_MOV_OP AS M INNER JOIN SAM_ORD_PAGO AS O ON(O.CVE_OP = M.CVE_OP) WHERE O.CVE_PED = a.CVE_PED AND O.STATUS NOT IN (-1,4))) as TOTAL  from SAM_PEDIDOS_EX a , SAM_REQUISIC b where  a.CVE_REQ=b.CVE_REQ and a.CVE_PED =?  ",new Object []{cve_ped});
	   		 Integer idproyecto= Integer.parseInt(detalleReq.get("ID_PROYECTO").toString());
	   		 String cuenta=(String)detalleReq.get("PARTIDA");
	   		 String nota= "Soporta el Pedido No.  "+(String)detalleReq.get("NUM_PED"); 
	   		 BigDecimal importe = (BigDecimal)detalleReq.get("TOTAL");
	   		 Double diferencia = importe.doubleValue() - importe_op;
	   		 if(importe.doubleValue()>importe_op) importe = new BigDecimal(importe_op);
	   		 
	   		 List detalleOP = getJdbcTemplate().queryForList("select ID_MOV_OP from SAM_MOV_OP  where CVE_OP=?  and ID_PROYECTO=? and CLV_PARTID=? ",new Object []{idOrden,idproyecto,cuenta});
	   		 Integer idDetalleOp=null;
	   		 if (detalleOP.size()>0) {
	   			 Map detalle = (Map)detalleOP.get(0);
	   			 idDetalleOp=((BigDecimal)detalle.get("ID_MOV_OP")).intValue();
	   		 }
	   		 if (detalleOP.size()>1)
	   			 throw new RuntimeException("Problemas en los Movimientos de la Orden de Pago");
	   		 if (idDetalleOp==null )	 	                		 
	   			 getJdbcTemplate().update("insert into SAM_MOV_OP (CVE_OP,ID_PROYECTO,CLV_PARTID,MONTO,NOTA,TIPO) values (?,?,?,?,?,'PEDIDO') ", new Object []{idOrden,idproyecto,cuenta,importe.doubleValue(),nota});
	   		 else
	   			 getJdbcTemplate().update("update SAM_MOV_OP set  MONTO= MONTO+ ? , NOTA=NOTA+'\n '+? WHERE ID_MOV_OP=? ", new Object []{importe,nota,idDetalleOp});	 
	   		 
	   		 getJdbcTemplate().update("insert into SAM_OP_COMPROBACIONES (CVE_OP, CVE_PED, CVE_REQ) values (?,?,?)", new Object []{idOrden,cve_ped, detalleReq.get("CVE_REQ")});
	   		 /*Revisar aqui si el pedido ha quedado en ceros, si es asi finiquitarlo, de caso contrario dejarlo comprometiendo*/
	   		 if(diferencia==0)
	   			 getJdbcTemplate().update("update SAM_PEDIDOS_EX set STATUS=4  WHERE CVE_PED=? ", new Object []{cve_ped});
	   		 else
	   			getJdbcTemplate().update("update SAM_PEDIDOS_EX set STATUS=1  WHERE CVE_PED=?", new Object []{cve_ped});
	   		 //integrar a la orden de pago la clave del pedido
	   		 getJdbcTemplate().update("update SAM_ORD_PAGO set CVE_PED = ?  WHERE CVE_OP = ?", new Object []{cve_ped, idOrden});
	   		 //Guardar en bitacora
	   		 gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_AGREGO_CONCEPTO, ejercicio, cve_pers, idOrden, folio, "OP", null, idproyecto.toString(), cuenta, "Proyecto id: "+idproyecto+ " Clv_Partid: "+cuenta+" Descripcion: "+nota, importe.doubleValue());
	}
	
	/*Metodo para obtener un listado de los anexos de la OP*/
	public List getAnexosOP(Long idOrden) {
		try{
			//return this.getJdbcTemplate().queryForList("SELECT SAM_OP_ANEXOS.*, TIPODOC_OP.DESCR FROM SAM_OP_ANEXOS INNER JOIN TIPODOC_OP ON (TIPODOC_OP.T_DOCTO = SAM_OP_ANEXOS.T_DOCTO) WHERE CVE_OP = ? AND SAM_OP_ANEXOS.T_DOCTO <>'XML' ORDER BY ANX_CONS ASC", new Object[]{idOrden});
			return this.getJdbcTemplate().queryForList("SELECT SAM_OP_ANEXOS.*, TIPODOC_OP.DESCR "+
														"FROM SAM_OP_ANEXOS "+
														"	INNER JOIN TIPODOC_OP ON (TIPODOC_OP.T_DOCTO = SAM_OP_ANEXOS.T_DOCTO) "+ 
														"	WHERE CVE_OP = ? AND SAM_OP_ANEXOS.T_DOCTO <>'XML' "+
														" UNION ALL "+
														"SELECT DISTINCT SAM_MOV_OP.CVE_OP, 0, 'CON', SAM_CONTRATOS.NUM_CONTRATO, 'SE ANEXA ARCHIVO DE CONTRATO', '[' + CONVERT(VARCHAR,SAM_CONTRATOS_ARCHIVOS.ID_ARCHIVO) + '] ' +  SAM_CONTRATOS_ARCHIVOS.NOMBRE, SAM_CONTRATOS_ARCHIVOS.RUTA, SAM_CONTRATOS_ARCHIVOS.FECHA, SAM_CONTRATOS_ARCHIVOS.EXT, SAM_CONTRATOS_ARCHIVOS.TAMANO, TIPODOC_OP.DESCR  FROM SAM_MOV_OP "+ 
														"	INNER JOIN SAM_FACTURAS ON (SAM_FACTURAS.CVE_FACTURA = SAM_MOV_OP.CVE_FACTURA) "+
														"	INNER JOIN SAM_CONTRATOS ON (SAM_CONTRATOS.CVE_CONTRATO = SAM_FACTURAS.CVE_CONTRATO) "+
														"	INNER JOIN SAM_CONTRATOS_ARCHIVOS ON (SAM_CONTRATOS_ARCHIVOS.CVE_CONTRATO = SAM_CONTRATOS.CVE_CONTRATO) "+
														"	INNER JOIN TIPODOC_OP ON (TIPODOC_OP.T_DOCTO = 'CON') "+
														"WHERE SAM_MOV_OP.CVE_OP = ? "+
														"ORDER BY ANX_CONS ASC", new Object[]{idOrden, idOrden});
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			return null;
		}
	}
	
	/*Metodo para obtener un listado de los values de la OP*/
	public List getComprobacionVales(Long idOrden) {
		try{
			return this.getJdbcTemplate().queryForList("SELECT DISTINCT COMP_VALES.ID_VALE, COMP_VALES.CVE_VALE, COMP_VALES.CONS_VALE, COMP_VALES.CVE_OP, COMP_VALES.TIPO, SAM_VALES_EX.NUM_VALE, CT.N_PROGRAMA, COMP_VALES.ID_PROYECTO, COMP_VALES.CLV_PARTID, (SELECT ISNULL(SUM(M.IMPORTE),0) FROM SAM_MOV_VALES AS M WHERE M.CVE_VALE = SAM_VALES_EX.CVE_VALE) AS IMPORTE, COMP_VALES.IMP_ANTERIOR, COMP_VALES.IMPORTE AS DESCONTADO, COMP_VALES.IMP_PENDIENTE FROM COMP_VALES "+
														"INNER JOIN SAM_VALES_EX ON (SAM_VALES_EX.CVE_VALE = COMP_VALES.CVE_VALE) "+
														"LEFT JOIN CEDULA_TEC AS CT ON (CT.ID_PROYECTO = COMP_VALES.ID_PROYECTO) " +
														"INNER JOIN CONCEP_VALE ON (CONCEP_VALE.CVE_VALE = COMP_VALES.CVE_VALE) "+
														"WHERE COMP_VALES.CVE_OP = ? AND COMP_VALES.TIPO NOT IN ('CF')", new Object[]{idOrden});
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			return null;
		}
	}
	

}
