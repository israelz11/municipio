/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/utilerias/cambio_estatus_periodo.action")
public class ControladorCambioEstatusPeriodos extends ControladorBase {

	public ControladorCambioEstatusPeriodos(){		
	}
	
	@Autowired	
	public GatewayMeses gatewayMeses;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})    
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		
		String  acciones= request.getParameter("acciones");
		/*if ("guardar".equals(acciones)) {
			Integer idMes= request.getParameter("idMes")==null ? null: request.getParameter("idMes").equals("")? null: Integer.parseInt(request.getParameter("idMes"));
			String  estatus= request.getParameter("estatus");
			String  tipoEstatus= request.getParameter("tipoEstatus");
			estatus = estatus.equals("ACTIVO") ? "INACTIVO": "ACTIVO";
			if (tipoEstatus.equals("DOC"))
				gatewayMeses.actializarEstatusDoc(idMes,estatus);
			else
				gatewayMeses.actializarEstatusEva(idMes,estatus);
		}	*/	
		
		modelo.put("meses",getMeses());
		modelo.put("mesactivo",mesActivo());
		
	    return "sam/utilerias/cambio_estatus_periodo.jsp";
	}
		
	
    private List<Map> getMeses(){
		return this.getJdbcTemplate().queryForList(" SELECT      MES, DESCRIPCION, ESTATUS, ESTATUSEVA, ID_MES , "+ 
				" CASE ESTATUS WHEN 'ACTIVO' THEN 'Cerrar'  ELSE 'Abrir' END AS ACCION, "+
				" CASE ESTATUSEVA WHEN 'ACTIVO' THEN 'Cerrar'  ELSE 'Abrir' END AS ACCIONEVA "+
				" FROM        MESES where ejercicio=? "+
				" ORDER BY MES ", new Object []{this.getSesion().getEjercicio()});    		
    }
	
    
	public int mesActivo(){
		return gatewayMeses.getMesActivo(this.getSesion().getEjercicio());
	}	
	
	public String cerrarPeriodo(final Integer idMes, final String estatus){
		try {    
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	    
                	
	                if(estatus.equals("ACTIVO")){
	                		int mesAct = mesActivo();
	                		int mes = mesAct + 1;
	                		
	                		//Migrar los Devengados en Facturas
	                		migrarDevengado(mes, mesAct);
	                		
		                	//Migrar la seccion de Contratos
	                		migrarContratos(mes, mesAct);
	                		
	                		//Migrar el saldo de las Requisiciones y Orden de Trabajo
		                	migrarRequisiciones(mes, mesAct);
		                	
	            	}
                	//Cambiamos el periodo al siguiente mes
                	gatewayMeses.actializarEstatusDoc(idMes, estatus);
                } 
             });
            return "";
           
            } catch (DataAccessException e) {	            	
                 throw new RuntimeException(e.getMessage(),e);
            }
		
	}
	
	public void migrarRequisiciones(int mes, int mesAct)
	{
		List<Map> Requisiciones = getJdbcTemplate().queryForList("SELECT SUM(A.IMPORTE) AS COMPROMETIDO_MES, " +
																		"A.PERIODO,  " +
																		"B.ID_PROYECTO,  " +
																		"B.CLV_PARTID,  " +
																		"A.CVE_REQ,  " +
																		"B.TIPO, " +
																		"C.TIPO_DOC, " +
																		"C.MONTO AS OCUPADO_MES, " +
																		"(SUM(A.IMPORTE) - C.MONTO) AS DIFERENCIA_MES " +
																"FROM SAM_COMP_REQUISIC AS A  " +
																"	INNER JOIN SAM_REQUISIC AS B ON (B.CVE_REQ = A.CVE_REQ) " +
																"	LEFT JOIN VT_COMPROMISOS AS C ON (C.CVE_DOC = A.CVE_REQ AND C.ID_PROYECTO = B.ID_PROYECTO AND C.CLV_PARTID = B.CLV_PARTID AND C.PERIODO = A.PERIODO) " +
																"WHERE A.PERIODO = ?  " +
																"	GROUP BY A.CVE_REQ, B.TIPO, C.TIPO_DOC, A.PERIODO, B.ID_PROYECTO, B.CLV_PARTID, C.MONTO " +
																"	HAVING (SUM(A.IMPORTE))>0 AND C.MONTO >0  " +
																"	AND (SUM(A.IMPORTE) - C.MONTO)>0", new Object[]{mesAct});
		for(Map c: Requisiciones)
		{
			Long cve_req = Long.parseLong( c.get("CVE_REQ").toString());
			BigDecimal totalComprometidoMes = ((BigDecimal) c.get("COMPROMETIDO_MES"));
			BigDecimal totalOcupadoMes = ((BigDecimal) c.get("OCUPADO_MES"));
			BigDecimal diferenciaMes = ((BigDecimal) c.get("DIFERENCIA_MES"));
			
			//Deshabilita los importe anteriores
			this.getJdbcTemplate().update("UPDATE SAM_COMP_REQUISIC SET PERIODO_ANT = PERIODO, IMPORTE_ANT =IMPORTE, IMPORTE =0 WHERE CVE_REQ =? AND PERIODO = ? AND TIPO =?", new Object[]{cve_req,mesAct, "COMPROMISO"});
			//Inserta el total ocupado en el mes actual
			this.getJdbcTemplate().update("INSERT INTO SAM_COMP_REQUISIC(CVE_REQ, TIPO, IMPORTE, PERIODO) VALUES(?,?,?,?)", new Object[]{cve_req, "COMPROMISO", totalOcupadoMes, mesAct});
			//Inserta la diferencia en el siguiente mes
			this.getJdbcTemplate().update("INSERT INTO SAM_COMP_REQUISIC(CVE_REQ, TIPO, IMPORTE, PERIODO) VALUES(?,?,?,?)", new Object[]{cve_req, "COMPROMISO", diferenciaMes, mes});
		}
	}
	
	public void migrarContratos(int mes, int mesAct)
	{

    	List<Map> Contratos = getJdbcTemplate().queryForList("SELECT SUM(IMPORTE) AS COMPROMETIDO_MES, PERIODO, ID_PROYECTO, CLV_PARTID, CVE_CONTRATO FROM SAM_COMP_CONTRATO "+
															 " WHERE PERIODO = ? AND CVE_CONTRATO = 2421 " +
															 " GROUP BY CVE_CONTRATO, PERIODO, ID_PROYECTO, CLV_PARTID ", new Object[]{mesAct});
    	for(Map c: Contratos)
    	{
    		//El totalComprometido quedara solo comprmetiendo en el mes actual
    		BigDecimal totalComprometido = ((BigDecimal) c.get("COMPROMETIDO_MES")) ;
    		//importeTotalMes Es toda la comprobacion de documentos que restan al totalComprometido
    		BigDecimal importeTotalMes = (BigDecimal) getJdbcTemplate().queryForObject("SELECT TOP 1	" +
					"ISNULL(("+   
								"/*Resta el importe del periodo desde la Req*/ "+
								  "(SELECT ISNULL(SUM(VT.MONTO),0) FROM VT_COMPROMISOS_EXTERNA AS VT INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ =VT.CVE_DOC) WHERE VT.TIPO_DOC IN('O.S', 'O.T') AND VT.CONSULTA = 'COMPROMETIDO' AND R.CVE_CONTRATO = SC.CVE_CONTRATO AND VT.ID_PROYECTO = SC.ID_PROYECTO AND VT.CLV_PARTID = SC.CLV_PARTID AND VT.PERIODO = SC.PERIODO ) "+
								  "/*Resta del total de pedido*/ " +
								  "+ (SELECT ISNULL(SUM(VT.MONTO),0) FROM VT_COMPROMISOS_EXTERNA AS VT INNER JOIN SAM_PEDIDOS_EX AS P ON (P.CVE_PED = VT.CVE_DOC) INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ AND R.CVE_CONTRATO = SC.CVE_CONTRATO) WHERE VT.CONSULTA='COMPROMETIDO' AND VT.TIPO_DOC ='PED' AND R.PERIODO = SC.PERIODO AND R.ID_PROYECTO = SC.ID_PROYECTO AND R.CLV_PARTID = SC.CLV_PARTID ) "+ 
								  "/*Resta ejercido de la Orden de Pago*/ " +
								  "/*+ (SELECT ISNULL(SUM(VT.MONTO),0) FROM VT_COMPROMISOS_EXTERNA AS VT INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = VT.CVE_DOC AND OP.CVE_CONTRATO = SC.CVE_CONTRATO) WHERE VT.CONSULTA IN ('COMPROMETIDO_OP','EJERCIDO') AND VT.TIPO_DOC ='OP' AND OP.PERIODO = SC.PERIODO AND VT.ID_PROYECTO = SC.ID_PROYECTO AND VT.CLV_PARTID = SC.CLV_PARTID AND (OP.CVE_CONTRATO IS NOT NULL AND CVE_CONTRATO <>0))*/ "+
								  "/*Resta compromiso de la Orden de Pago que viene a travez de facturas y de Contrato*/ "+
								  "+ (SELECT  ISNULL(SUM(M.MONTO),0) FROM SAM_MOV_OP AS M INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = M.CVE_OP) INNER JOIN SAM_FACTURAS AS F ON (F.CVE_FACTURA = M.CVE_FACTURA AND F.CVE_CONTRATO = SC.CVE_CONTRATO) AND M.ID_PROYECTO = SC.ID_PROYECTO AND M.CLV_PARTID = SC.CLV_PARTID AND OP.PERIODO = SC.PERIODO AND OP.STATUS IN (0,6)) "+
								  "/*Resta de los Vales*/ "+
								  "+ (SELECT ISNULL(SUM(VT.MONTO),0) FROM VT_COMPROMISOS_EXTERNA AS VT INNER JOIN SAM_VALES_EX AS V ON (V.CVE_VALE = VT.CVE_DOC AND V.CVE_CONTRATO = SC.CVE_CONTRATO) INNER JOIN SAM_MOV_VALES AS MOV ON (MOV.CVE_VALE = V.CVE_VALE) WHERE VT.CONSULTA ='COMPROMETIDO' AND VT.TIPO_DOC ='VAL' AND V.MES = SC.PERIODO AND MOV.ID_PROYECTO = SC.ID_PROYECTO AND MOV.CLV_PARTID = SC.CLV_PARTID) "+
								  "/*Resta de las facturas*/ "+
								  "+ (SELECT ISNULL(SUM(VT.MONTO),0) FROM VT_COMPROMISOS_EXTERNA AS VT INNER JOIN SAM_FACTURAS AS FAC ON (FAC.CVE_FACTURA = VT.CVE_DOC AND FAC.CVE_CONTRATO = SC.CVE_CONTRATO) INNER JOIN SAM_FACTURA_DETALLE AS FD ON (FD.CVE_FACTURA = FAC.CVE_FACTURA) WHERE VT.CONSULTA ='DEVENGADO' AND VT.TIPO_DOC ='FAC' AND FAC.PERIODO = SC.PERIODO AND FD.ID_PROYECTO = SC.ID_PROYECTO AND FD.CLV_PARTID = SC.CLV_PARTID) "+
					 "),0) AS IMPORTE_MES "+
					 " FROM SAM_COMP_CONTRATO AS SC WHERE SC.PERIODO = ? AND SC.CVE_CONTRATO = ? AND SC.ID_PROYECTO =? AND SC.CLV_PARTID =?", new Object[]{mesAct, c.get("CVE_CONTRATO"), c.get("ID_PROYECTO"), c.get("CLV_PARTID")}, BigDecimal.class);
    		
    		//La diferencia se enviara al siguiente mes
    		BigDecimal diferencia = totalComprometido.subtract(importeTotalMes); 
    		
    		//Se reinician los importes que comprometen a Cero para generer uno por el total que compromete el periodo
    		getJdbcTemplate().update("UPDATE SAM_COMP_CONTRATO SET IMPORTE = ? WHERE CVE_CONTRATO = ? AND PERIODO = ? AND ID_PROYECTO =? AND CLV_PARTID =?", new Object[]{0, c.get("CVE_CONTRATO"), mesAct, c.get("ID_PROYECTO"), c.get("CLV_PARTID")});
    		
    		//Se inserta el totalComprometido que quedara en el mes actual antes de cerrar
    		getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, ID_PROYECTO, CLV_PARTID, PERIODO, IMPORTE) VALUES (?,?,?,?,?,?)", new Object[]{c.get("CVE_CONTRATO"), "COMPROMISO", c.get("ID_PROYECTO"), c.get("CLV_PARTID"), mesAct, importeTotalMes});
    		
    		//Se inserta un nuevo registro de compromiso con la diferencia al siguiente mes
    		getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, ID_PROYECTO, CLV_PARTID, PERIODO, IMPORTE) VALUES (?,?,?,?,?,?)", new Object[]{c.get("CVE_CONTRATO"), "COMPROMISO", c.get("ID_PROYECTO"), c.get("CLV_PARTID"), (mesAct+1), diferencia});
    		
    		
    	}
    	

	}
	
	public void migrarDevengado(int mes, int mesAct)
	{
		if(mes<=12)
		{
    		List<Map> facturas = getJdbcTemplate().queryForList("SELECT CVE_FACTURA FROM SAM_FACTURAS WHERE CVE_FACTURA IN (SELECT CVE_DOC FROM VT_COMPROMISOS WHERE TIPO_DOC ='FAC' AND PERIODO = ?)", new Object[]{mesAct});
    		for(Map row: facturas)
    		{
    			getJdbcTemplate().update("UPDATE SAM_FACTURAS SET PERIODO =? WHERE CVE_FACTURA =?", new Object[]{mes, row.get("CVE_FACTURA")});
    		}
		}
	}
	
	public String cerrarEval(final Integer idMes, final String estatus){
		try {    
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	    
                	gatewayMeses.actializarEstatusEva(idMes, estatus);
                } 
             });
            return "";
           
            } catch (DataAccessException e) {	            	
                 throw new RuntimeException(e.getMessage(),e);
            }
		
	}
}
