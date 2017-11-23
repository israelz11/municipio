/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.ordenesPagos;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayBeneficiario;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBitacora;
import mx.gob.municipio.centro.model.gateways.sam.GatewayComprobacionesVales;
import mx.gob.municipio.centro.model.gateways.sam.GatewayDetallesOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPedidos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayTipoOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/sam/ordenesdepago/orden_pago.action")
public class ControladorOrdenPago extends ControladorBase  {
	
	private static Logger log =Logger.getLogger(ControladorOrdenPago.class.getName());
   
	@Autowired
	private GatewayTipoOrdenDePagos gatewayTipoOrdenDePagos;
	
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	private GatewayBeneficiario gatewayBeneficiario;
	
	@Autowired
	private GatewayOrdenDePagos gatewayOrdenDePagos;
	
	@Autowired
	private GatewayPlanArbit gatewayPlanArbit;
	
	@Autowired
	private GatewayDetallesOrdenDePagos gatewayDetallesOrdenDePagos;
	
	@Autowired
	private GatewayPedidos gatewayPedidos;
	
	@Autowired
	private GatewayRequisicion gatewayRequisicion;
	
	@Autowired
	public GatewayBitacora gatewayBitacora;
	
	@Autowired
	private GatewayComprobacionesVales gatewayComprobacionesVales;
	@Autowired
	private GatewayMeses gatewayMeses;
	@Autowired 
	private GatewayProyectoPartidas gatewayProyectoPartidas;
	
	String mensaje;
	 public ControladorOrdenPago() {
	    }
	 
	    @SuppressWarnings("unchecked")
	    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})     
	    public String  handleRequest( Map modelo, HttpServletRequest request ) {
	    	modelo.put("cve_op",request.getParameter("cve_op"));
	    	modelo.put("accion",request.getParameter("accion"));
	    	modelo.put("ejercicio",this.getSesion().getEjercicio());
	    	modelo.put("idUnidad",this.getSesion().getClaveUnidad());
	    	modelo.put("nombreUnidad",this.getSesion().getUnidad());
	    	modelo.put("meses",gatewayMeses.getTodosMesesEjercicioActivos(getSesion().getEjercicio()));
	    	modelo.put("tipoDocumentosOp",gatewayTipoOrdenDePagos.getTipoDocumentosUsuario(getSesion().getIdUsuario()));
	    	modelo.put("clv_benefi",gatewayBeneficiario.getBeneficiariosTodos(0));
	    	if (this.getSesion().getIdGrupo() == null){
				modelo.put("mensaje","El usuario no tiene asignado un grupo de firmas ");
				return "insuficientes_permisos.jsp";
			} 
	        return "sam/ordenesdepago/orden_pago.jsp";
	    }
	    
	    @ModelAttribute("unidadesAdmiva")
	    public List<Map> getUnidadesAdmivas(){
	    	return gatewayUnidadAdm.getUnidadAdmTodos();	
	    }
	    
	    @ModelAttribute("tipoGastos")
	    public List<Map> getTiposGastos(){
	    	return gatewayPlanArbit.getTipodeGasto();	
	    }
	    
		@ModelAttribute("beneficiarios")
		public List<Map>getBeneficiarios(){
			return gatewayBeneficiario.getListaBeneficiarios();
		}
		
	    @ModelAttribute("tipoRetenciones")
	    public List<Map> getTodasTipoRetencionesTodas(){
	    	return gatewayOrdenDePagos.getTodasTipoRetencionesTodas();	
	    }
	   
	   /* @ModelAttribute("tipoDocumentosOp")
	    public List getTipoDocumentosTodosOp() {
	    	return gatewayTipoOrdenDePagos.getTipoOredenesPagosEstatusActivos();
	    }*/
	    
	    @ModelAttribute("tipoDocumentos")
	    public List getTipoDocumentosTodos() {	   
	 	   return this.getJdbcTemplate().queryForList("select T_DOCTO, DESCR   from  TIPODOC_OP order by DESCR ");
	 }
	     
	    //--------------------------Parametros recibidos desde el .js para guardar la cabecera de la Orden de Pago-------------------------------------------
	    public Long guardarOrden(Long cveOp ,  int tipo,String  fecha,String  cveBeneficiario,String  cveParbit, String reembolsoFondo,String  concurso,String  nota,int  status,Integer  cveRequisicion, double importeIva , int cveUnidad, Integer periodo,int tipoGasto, Long cve_contrato  ){
	    	Long idOrden= gatewayOrdenDePagos.actualizarPrincipalOrden(cveOp,getSesion().getEjercicio(), tipo,this.formatoFecha(fecha),null,cveBeneficiario,this.getSesion().getIdUsuario(),cveParbit, reembolsoFondo,concurso,nota,status,cveRequisicion, importeIva,cveUnidad,periodo,tipoGasto,this.getSesion().getIdGrupo(), cve_contrato); 	    		    	
	    return idOrden;
	    }	    
	    
	    
	    //--------------------------Parametros recibidos desde el .js para guardar la cabecera de la Orden de Pago desde el listado de Facturas-------------------------------------------
	    //Abraham gonzalez pruebas------------------20-05-17//Long cveOp , final Long[] cve_facturas,final int cve_pers, int ejercicio

	    public Long geraropxfacturas(int id_unidad , List<Long> cve_facturas){
	    	Long idOrden= gatewayOrdenDePagos.insertaOrdenxFacturas(id_unidad,cve_facturas,getSesion().getIdUsuario(),getSesion().getEjercicio(),getSesion().getIdGrupo()); 	    		    	
	    return idOrden;
	    		    	
	   

	    }	    
	    	    
//---------------------------------- CERRAR ORDEN DE PAGO ------------------------------------------------------------------------------------------------	    
   
	    public String  cerrarOrden(final Long idOrden  ){
	    	 try {                 
		            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
		                @Override
		    protected void   doInTransactionWithoutResult(TransactionStatus status) {
		                
	                	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	            		/*if(getPrivilegioEn(getSesion().getIdUsuario(), 114)){
	            			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	            		}*/
		            		
		               	mensaje="exito"; 	
		               	               	
		               	BigDecimal importe = new BigDecimal("0.0");
		                Boolean tienenPresupuesto = null;
		                Map orden =gatewayOrdenDePagos.getOrden(idOrden);
		                int mes =(Integer )orden.get("PERIODO");
		                int tipo =(Integer )orden.get("TIPO");
		                boolean contrato_conceptos = false;
		                
		                Long cve_vale_req = 0L;
		                Long cve_vale_pedido = 0L;
		                
		                List <Map> lstobj = getJdbcTemplate().queryForList("SELECT (SELECT TOP 1 R.CVE_VALE FROM SAM_REQUISIC AS R INNER JOIN SAM_ORD_PAGO AS O ON (O.CVE_REQ = R.CVE_REQ) WHERE O.CVE_OP  = OP.CVE_OP) AS CVE_VALE_REQ, (SELECT TOP 1 R.CVE_VALE FROM SAM_REQUISIC AS R INNER JOIN SAM_PEDIDOS_EX AS P ON (P.CVE_REQ = R.CVE_REQ) INNER JOIN SAM_ORD_PAGO AS O ON (O.CVE_PED = P.CVE_PED) WHERE O.CVE_OP = OP.CVE_OP) AS CVE_VALE_PED FROM SAM_ORD_PAGO AS OP WHERE OP.CVE_OP =?", new Object[]{idOrden});
		                for(Map row: lstobj)
        	    		{
		                	if(row.get("CVE_VALE_REQ")!=null)  cve_vale_req = Long.parseLong(row.get("CVE_VALE_REQ").toString());
		                	if(row.get("CVE_VALE_PED")!=null)  cve_vale_pedido = Long.parseLong(row.get("CVE_VALE_PED").toString());
        	    		}
        	    		
		                Long cve_factura = 0L;
		                Long cve_contrato = 0L;
		                Long cve_req = 0L;
		                Long cve_ped = 0L;
		                
		                List<Map> detalles = gatewayDetallesOrdenDePagos.getDetallesOrdenes(idOrden);
		                lstobj = getJdbcTemplate().queryForList("SELECT OP.CVE_CONTRATO, (SELECT CVE_CONTRATO FROM SAM_REQUISIC WHERE CVE_REQ = C.CVE_REQ) AS CVE_CONTRATO_REQ, (SELECT R.CVE_CONTRATO FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) WHERE P.CVE_PED = C.CVE_PED) AS CVE_CONTRATO_PED, C.CVE_REQ, C.CVE_PED FROM SAM_OP_COMPROBACIONES AS C INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = C.CVE_OP) WHERE C.CVE_OP = ?", new Object[]{idOrden});
		                if (gatewayMeses.esMesActivo(mes,getSesion().getEjercicio()) && tipo!=5 ){		
		                	
					    /*1.- si es de pedidos no se debe de comprometer nada  0 */            
			            /*2.- si es de Requisicion  se debe de comprometer  2 */
			            /*3.- si es normal debe de comprometer  las demas. */
		                /*4.- si es negativa  no hacer nada  5 */
		                	
		                //validacion extra en el contrato de la orden de pago con el del documento (Pedido o OT,OS) 24/AGO/2011
		                
        	    		for(Map row: lstobj)
        	    		{
        	    			/*if(row.get("CVE_REQ")!=null&&row.get("CVE_CONTRATO")==null&&row.get("CVE_CONTRATO_REQ")!=null)
        	    				if((getJdbcTemplate().queryForInt("SELECT ISNULL(CVE_CONTRATO,0) AS N FROM SAM_REQUISIC WHERE CVE_REQ = ?",new Object[]{row.get("CVE_REQ")}))!=0)
        	    					contrato_conceptos = true;*/
        	    			/*if(row.get("CVE_PED")!=null&&row.get("CVE_CONTRATO")==null&&row.get("CVE_CONTRATO_PED")!=null)
        	    				if((getJdbcTemplate().queryForInt("SELECT ISNULL(CVE_CONTRATO,0) AS N FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) WHERE P.CVE_PED = ?",new Object[]{row.get("CVE_PED")}))!=0)
        	    					contrato_conceptos = true;*/
        	    			
        	    			//if(row.get("CVE_CONTRATO_PED")!=null) cve_contrato_doc = Long.parseLong(row.get("CVE_CONTRATO_PED").toString());
        	    			
        	    			if(row.get("CVE_REQ")!=null) cve_req = Long.parseLong(row.get("CVE_REQ").toString()); 
        	    			//if(row.get("CVE_VALE")!=null) cve_vale_concepto = Long.parseLong(row.get("CVE_VALE").toString()); 
        	    			if(row.get("CVE_PED")!=null) cve_ped = Long.parseLong(row.get("CVE_PED").toString()); 
        	    			//if(row.get("CVE_VALE_PED")!=null) cve_vale_pedido = Long.parseLong(row.get("CVE_VALE_PED").toString()); 
        	    			
        	    		}	
        	    		
		                for (Map detalle: detalles ) {
		                	Long cve_vale = (detalle.get("CVE_VALE")!=null) ? Long.parseLong(detalle.get("CVE_VALE").toString()): 0L;
		                	Long  proyecto = Long.parseLong(detalle.get("ID_PROYECTO").toString());
		                	String partida = detalle.get("CLV_PARTID").toString();
		                	importe = (BigDecimal)detalle.get("MONTO");
		                	List <Map> resultado = null;


		                	//Verificar que el proyecto tenga el mismo tipo de gasto que la Orden de pago
		                	int idRecurso = getJdbcTemplate().queryForInt("SELECT ID_RECURSO FROM VPROYECTO WHERE ID_PROYECTO = ?", new Object[]{proyecto});
		            		if(idRecurso !=  Integer.parseInt(orden.get("ID_RECURSO").toString()))
		            			throw new RuntimeException("El programas con ID: "+proyecto.toString()+" no pertenecen al mismo tipo de gasto que la Orden de Pago");
		                	
		                	 if(orden.get("TIPO").toString().equals("12")){
				                	//es una factura 
				                	cve_factura = Long.parseLong(detalle.get("CVE_FACTURA").toString());
				             }
		                	 
		                	 if(orden.get("TIPO").toString().equals("13")){
				                	//es una factura 
				                	cve_contrato = Long.parseLong(orden.get("CVE_CONTRATO").toString());
				             }
		                	 
		                	
		                    if(cve_vale_req!=0) //Busca en OS, OT desde Vales
		                		resultado = getJdbcTemplate().queryForList("SELECT ISNULL(MONTO,0) AS TOTAL FROM VT_COMPROMISOS WHERE TIPO_DOC IN('REQ', 'O.S', 'O.T') AND CVE_DOC = ? AND PERIODO = ? AND ID_PROYECTO = ? AND CLV_PARTID = ?", new Object[]{cve_req, mes, proyecto, partida});
		                	else if(cve_vale_pedido!=0) //Busca en Ped desde Vales
		                		resultado = getJdbcTemplate().queryForList("SELECT ISNULL(MONTO,0) AS TOTAL FROM VT_COMPROMISOS WHERE TIPO_DOC IN('PED') AND CVE_DOC = ? AND PERIODO = ? AND ID_PROYECTO = ? AND CLV_PARTID = ?", new Object[]{cve_ped, mes, proyecto, partida});
		                	else if(cve_factura!=0)
		                		resultado = getJdbcTemplate().queryForList("SELECT ISNULL(MONTO,0) AS TOTAL FROM VT_COMPROMISOS WHERE TIPO_DOC IN('FAC') AND CVE_DOC = ? AND PERIODO = ? AND ID_PROYECTO = ? AND CLV_PARTID = ?", new Object[]{cve_factura, mes, proyecto, partida});
		                	else if(cve_contrato!=0)
		                		resultado = getJdbcTemplate().queryForList("SELECT ISNULL(MONTO,0) AS TOTAL FROM VT_COMPROMISOS WHERE TIPO_DOC IN('CON') AND CVE_DOC = ? AND PERIODO = ? AND ID_PROYECTO = ? AND CLV_PARTID = ?", new Object[]{cve_contrato, mes, proyecto, partida});
		                    
		                	else{
		                		if(cve_vale==0)  
		                				resultado =  gatewayProyectoPartidas.getPresupuesto(proyecto, detalle.get("N_PROGRAMA").toString(), partida, mes, getSesion().getIdUsuario(), 0,0);
		                		else
		                				resultado = gatewayProyectoPartidas.getPresupuestoValeDetalle(proyecto, partida, mes, cve_vale);
		                	}
		                   
		                	BigDecimal disponible= new BigDecimal("0.0");
		                	BigDecimal comprometido= new BigDecimal("0.0");
		                	BigDecimal devengado= new BigDecimal("0.0");
		                	BigDecimal preComprometido= new BigDecimal("0.0");
		                	BigDecimal totalDetalleVale = new BigDecimal("0.0");
		                	
		                	
		            		for (Map result :resultado ) {
		            			/*SI HAY CONTRATO AGARRAR EL DISPONIBLE DEL CONTRATO*/
		            			if(orden.get("CVE_CONTRATO")!=null&&contrato_conceptos==false){
		            				BigDecimal total_contrato = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(MONTO,0) FROM VT_COMPROMISOS WHERE TIPO_DOC = ? AND CVE_DOC = ? AND PERIODO = ?", new Object[]{"CON",orden.get("CVE_CONTRATO"), mes}, BigDecimal.class);
		            				disponible = total_contrato;
		            				
		            				preComprometido = total_contrato;
		            				comprometido = total_contrato;
		            			}
		            			/*SI NO HAY CONTRATO*/
		            			else if(orden.get("CVE_CONTRATO")==null&&contrato_conceptos==true){
		            				//Buscar los compromisos en el contrato correspondiente 24/AGO/2011
		            				/*BigDecimal total_ped = new BigDecimal("0.0");
		            				BigDecimal total_req = new BigDecimal("0.0");
		            				
		            				for(Map row: lstobj)
			        	    		{
		            					if(row.get("CVE_REQ")!=null&&tipo==2)
		            						total_req = (BigDecimal) getJdbcTemplate().queryForObject("SELECT IMPORTE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO = ? AND CVE_DOC = ? AND TIPO_DOC IN ('OS','OT') AND TIPO_MOV = ?", new Object[]{cve_contrato_doc, row.get("CVE_REQ"), "LIBERACION" }, BigDecimal.class);
		            					if(row.get("CVE_PED")!=null&&tipo==0)
		            						total_ped = (BigDecimal) getJdbcTemplate().queryForObject("SELECT IMPORTE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO = ? AND CVE_DOC = ? AND TIPO_DOC = ? AND TIPO_MOV = ?", new Object[]{cve_contrato_doc, row.get("CVE_PED"), "PED", "LIBERACION" }, BigDecimal.class);
			        	    		}
		            				if(tipo==0) comprometido = total_ped;
		            				if(tipo==2) preComprometido = total_req;*/
		            			}
		            			else{ /* DE CASO CONTRARIO*/
		            					
				            			preComprometido=(BigDecimal)result.get("PREREQ");
				            			comprometido=(BigDecimal)result.get("PRECOM");
				            			if(detalle.get("CVE_VALE")!=null)
					                		disponible = (BigDecimal) getJdbcTemplate().queryForObject("SELECT ISNULL(dbo.getDisponible(?,?,?),0) AS DISPONIBLE FROM CEDULA_TEC AS CT WHERE CT.ID_PROYECTO = ? ", new Object[]{mes, proyecto, partida, proyecto}, BigDecimal.class);
				            			else
				            				disponible=(BigDecimal)result.get("DISPONIBLE");
				            			
				            			if(cve_vale_req!=0)
				            				preComprometido=(BigDecimal)result.get("TOTAL");
				            			
				            			if(cve_vale_pedido!=0)
				            				comprometido=(BigDecimal)result.get("TOTAL");
				            			
				            			if(cve_factura!=0)
				            				devengado = (BigDecimal)result.get("TOTAL");
				            			
		            				}
		            			
		            			totalDetalleVale = (BigDecimal) result.get("MONTO");
		            		}
		            		
		            		if(totalDetalleVale==null) totalDetalleVale = new BigDecimal("0.0");
		            		
		            		boolean presupuest= false;		            			
		            		if (tipo==0 ){
		            			if (comprometido.doubleValue()>= importe.doubleValue())
		            			    presupuest= true;
		            		}
		            		else if (tipo==2 ){
		            			if (preComprometido.doubleValue()>= importe.doubleValue())
		            			presupuest= true;
		            		    }	            				
		            		else if (tipo==5){
		            			presupuest= true;
		            		}
		            		else if(tipo==11){
		            			/*validacion con presupuesto del vale 09/03/2012*/
		            			if((totalDetalleVale.doubleValue()+disponible.doubleValue())>=importe.doubleValue())
		            				presupuest= true;
		            		}
		            		else if(tipo==12){
		            			if(devengado.doubleValue()>=importe.doubleValue())
		            				presupuest= true;
		            		}
		            		else{
		            			if (disponible.doubleValue()>= importe.doubleValue())
		            			   presupuest= true;				            			
		            		}
		                	//Se verifica si uno de los detalles no cumple
		                	if (presupuest){
		                	 if (tienenPresupuesto==null)	
		                		 tienenPresupuesto= true;
		                	 } else{
		                		 DecimalFormat formateador = new DecimalFormat("###,###,###.##");
		                		 tienenPresupuesto= false;
		                		 throw new RuntimeException("No hay suficiente presupuesto para aprobar la Orden de Pago.\n<strong>Unidad Administrativa:</strong> "+ detalle.get("DEPENDENCIA").toString() +" | <strong>Programa:</strong> ["+detalle.get("ID_PROYECTO").toString()+"] "+ detalle.get("PROYECTOPARTIDA").toString() +" | <strong>Disponible Mes:</strong> $ "+formateador.format(disponible)+" | <strong>Importe:</strong> $ "+formateador.format(importe));
		                	 }

		                	
				          } /*termina detalles*/
				           
		                } else 
		                	 if (tipo != 5)
		                	   mensaje="El periodo de la Orden de Pago no esta activo";
		                	/*verificar los reintegros*/		                
		                if (tienenPresupuesto!=null && tienenPresupuesto.equals(true) || tipo==5 ) {
		                	if (tipo!=5) comprometerOrden(detalles,tipo,mes);	
			                gatewayOrdenDePagos.actualizaEstatusOrden(idOrden ,0);		                
			                getJdbcTemplate().update("update SAM_ORD_PAGO  set  IMP_NETO = IMPORTE-RETENCION   where CVE_OP= ? ",new Object[]{idOrden});
			                //AQUI CUANDO ES UN PEDIDO PARCIAL O NO, MODIFICADO 05/05/2011
			                if (tipo==0){
			                	Map valor = getJdbcTemplate().queryForMap("SELECT CVE_PED FROM SAM_ORD_PAGO WHERE CVE_OP = ?", new Object[]{idOrden});
			                	Double temp = (valor.get("CVE_PED")!=null) ? (Double) getJdbcTemplate().queryForObject("SELECT (TOTAL - (SELECT ISNULL(SUM(M.MONTO),0) FROM SAM_MOV_OP AS M INNER JOIN SAM_ORD_PAGO AS O ON(O.CVE_OP = M.CVE_OP) WHERE O.CVE_PED = SAM_PEDIDOS_EX.CVE_PED AND O.STATUS NOT IN (-1,4))) AS DISPONIBLE FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{orden.get("CVE_PED")}, Double.class): new Double(0);
			                	if((temp)>0)
			                		getJdbcTemplate().update("update SAM_PEDIDOS_EX set STATUS=1 where  CVE_PED in (select CVE_PED from SAM_OP_COMPROBACIONES where CVE_OP= ? ) ",new Object[]{idOrden});
			                	else
			                		getJdbcTemplate().update("update SAM_PEDIDOS_EX set STATUS=5 where  CVE_PED in (select CVE_PED from SAM_OP_COMPROBACIONES where CVE_OP= ? ) ",new Object[]{idOrden});
			                }
			                //AQUI CUANDO ES UNA OS/OT PARCIAL O NO, MODIFICADO 31/05/2011
			                if (tipo==2){		     	
			                	Map valor = getJdbcTemplate().queryForMap("SELECT CVE_REQ FROM SAM_ORD_PAGO WHERE CVE_OP = ?", new Object[]{idOrden});
			                	Double temp = (valor.get("CVE_REQ")!=null) ? (Double) getJdbcTemplate().queryForObject("SELECT ((SELECT ISNULL(SUM(R.CANTIDAD*R.PRECIO_EST),0) FROM SAM_REQ_MOVTOS R WHERE R.CVE_REQ = SAM_REQUISIC.CVE_REQ) - (SELECT ISNULL(SUM(M.MONTO),0) FROM SAM_MOV_OP AS M INNER JOIN SAM_ORD_PAGO AS O ON(O.CVE_OP = M.CVE_OP) WHERE O.CVE_REQ = SAM_REQUISIC.CVE_REQ AND O.STATUS NOT IN (-1,4))) AS DISPONIBLE FROM SAM_REQUISIC WHERE CVE_REQ = ?", new Object[]{orden.get("CVE_REQ")}, Double.class): new Double(0);
			                	if(temp>0)
			                		getJdbcTemplate().update("update SAM_REQUISIC set STATUS=1  WHERE cve_req in (select CVE_REQ from SAM_OP_COMPROBACIONES where CVE_OP= ? ) ",new Object[]{idOrden});
			                	else
			                		getJdbcTemplate().update("update SAM_REQUISIC set STATUS=5  WHERE cve_req in (select CVE_REQ from SAM_OP_COMPROBACIONES where CVE_OP= ? ) ",new Object[]{idOrden});
			                	
			                }
			                /*CIERRA DESDE FACTURAS*/
			                if(tipo==12){
			                	List<Map> movimientos = gatewayDetallesOrdenDePagos.getDetallesOrdenes(idOrden);
			                	for(Map det: movimientos)
			                		getJdbcTemplate().update("UPDATE SAM_FACTURAS SET CVE_OP =? WHERE CVE_FACTURA =?",new Object[]{idOrden, det.get("CVE_FACTURA")});
			                }
			                /*CIERRA DESDE CONTRATOS*/
			                /*if(tipo==12){
			                	List<Map> movimientos = gatewayDetallesOrdenDePagos.getDetallesOrdenes(idOrden);
			                	for(Map det: movimientos)
			                		getJdbcTemplate().update("UPDATE SAM_FACTURAS SET CVE_OP =? WHERE CVE_FACTURA =?",new Object[]{idOrden, det.get("CVE_FACTURA")});
			                		
			                }*/
			                
			                /*AQUI CUANDO SE CIERRA ATRAVEZ DEL PRESUPUESTO DE UN VALE 09/03/2012
			                if(tipo==11){
			                	BigDecimal importeOP = (BigDecimal) orden.get("IMPORTE");
			                	
			                	 List <Map> lst_vales = getJdbcTemplate().queryForList("SELECT DISTINCT CVE_VALE, ISNULL(SUM(MONTO),0) AS MONTO FROM SAM_MOV_OP WHERE CVE_OP=? GROUP BY CVE_VALE", new Object[]{idOrden});
			                	 for(Map row: lst_vales){
			                		getJdbcTemplate().update("INSERT INTO SAM_COMP_VALES(CVE_VALE, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)",new Object[]{row.get("CVE_VALE"), "LIBERACION", "OP", idOrden, mes, row.get("MONTO")});
			                	 }

			                	
			                }*/
			                /*SI HAY CONTRATO GENERAR EL MOVIMIENTOTO DE COMPROBACION*/
			                /*if(orden.get("CVE_CONTRATO")!=null&&contrato_conceptos==false){
			                	getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)", new Object[]{orden.get("CVE_CONTRATO"), "LIBERACION", "OP",idOrden, mes, importe});
			                }*/
			                
			                /*if(orden.get("CVE_CONTRATO")==null&&contrato_conceptos==true){
			                	
			                	for(Map row: lstobj)
		        	    		{
			                		if(row.get("CVE_REQ")!=null&&tipo==2)
			                			getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO = ? AND CVE_DOC = ? AND TIPO_MOV =? AND TIPO_DOC IN ('OS', 'OT')", new Object[]{cve_contrato_doc, row.get("CVE_REQ"), "LIBERACION"});
			                		if(row.get("CVE_PED")!=null&&tipo==0)
			                			getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_CONTRATO = ? AND CVE_DOC = ? AND TIPO_MOV =? AND TIPO_DOC = ?", new Object[]{cve_contrato_doc, row.get("CVE_PED"), "LIBERACION", "PED"});
		        	    		}
			                	getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?)", new Object[]{cve_contrato_doc, "LIBERACION", "OP",idOrden, mes, importe});
			                }*/
			                
			                //GUARDAR EN LA BITACORA
			                String folio=rellenarCeros(idOrden.toString(),6);
			                
			                Map pre = gatewayOrdenDePagos.getProyectoPartidaOP(idOrden);
			                gatewayBitacora.guardarBitacora(gatewayBitacora.OP_CERRAR, getSesion().getEjercicio(), getSesion().getIdUsuario(), idOrden, folio, "OP", (Date) orden.get("V_FECHA"), pre.get("ID_PROYECTO").toString(), pre.get("CLV_PARTID").toString(), null, importe.doubleValue());
		                }
		                else
		                	mensaje="No hay suficiente presupuesto para aprobar la Orden de Pago o el período no esta vigente";
	    	            	    	             
		                } 
		                });
		       return mensaje;     
             } catch (DataAccessException e) {   
            	 
                 log.info("Error,Problemas con la aprobacion del documento ");	                    
                 throw new RuntimeException(e.getMessage(),e);
             }	    	
    }

    private void  comprometerOrden(List<Map> detalles , int tipo, int mes){	
	detalles.indexOf(0);
	for ( Map detalle: detalles ) {
    	Long  proyecto = Long.parseLong(detalle.get("ID_PROYECTO").toString());
    	String partida = detalle.get("CLV_PARTID").toString();
    	BigDecimal importe = (BigDecimal)detalle.get("MONTO");				            			
		if (tipo==2 ){
			//gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, mes, importe.doubleValue(), "REDUCCION");
			//gatewayProyectoPartidas.comprometerPresupuesto(proyecto, partida, mes,importe.doubleValue(),"COMPROMETER");
		}	            				
		/*else
			if (tipo!=0)
			   gatewayProyectoPartidas.comprometerPresupuesto(proyecto, partida, mes,importe.doubleValue(),"COMPROMETER");	*/	
     }	
}
    

	   public Map getOrden(Long idOrden) {		   
		   return gatewayOrdenDePagos.getOrden(idOrden);
	   }
	    
	   public List getOrdenesTipo( String idUnidad, Integer Ejercicio,Integer estatus) {	   
		   return gatewayOrdenDePagos.getOrdenesTipo( idUnidad, this.getSesion().getIdUsuario(), Ejercicio,estatus);
	}

    public List getDetallesOrdenes(Long IdOrden) {	   
    	   return gatewayDetallesOrdenDePagos.getDetallesOrdenes(IdOrden);
    }	
    
      public  boolean  guardarDetalles(Integer idDetalle,Long idOrden,String proyecto, String partida, String nota, Double importe, Long idVale){   	     	        	  
    	  return gatewayDetallesOrdenDePagos.actualizarPrincipalDetalle( idDetalle,idOrden, proyecto, partida, nota, importe, idVale, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario(), "LIBRE");    	      	 
      }            
      
      public List getPedidosPorUnidad (String idtipoGasto,String unidad ,String claveBeneficiario) {
    	  Map parametros =  new HashMap<String,Object>();
    	  Integer cve_pers = this.getSesion().getIdUsuario();
    	  parametros.put("unidad", unidad);
    	  parametros.put("cve_pers", cve_pers);
    	  parametros.put("idtipoGasto", idtipoGasto);
    	  parametros.put("claveBeneficiario", claveBeneficiario);    	  
     	 return this.getNamedJdbcTemplate().queryForList("SELECT     c.CVE_PED,c.NUM_PED, c.FECHA_PED, c.STATUS, c.TOTAL, " + 
     			  " b.CLV_PARBIT, A.DESC_TGASTO, c.NOTAS, b.N_PROGRAMA, d.CLV_PARTID  as PARTIDA "+ 
     			  " FROM         dbo.CEDULA_TEC b INNER JOIN "+
                  " dbo.PLAN_ARBIT  a ON b.CLV_PARBIT = a.CLV_PARBIT INNER JOIN "+
     			  " dbo.SAM_REQUISIC d INNER JOIN "+
     			  " dbo.PEDIDOS_EX  as c ON d.CVE_REQ = c.CVE_REQ ON b.ID_PROYECTO = d.ID_PROYECTO "+
     			  " where b.CLV_PARBIT=:idtipoGasto and c.clv_benefi=:claveBeneficiario "+  
     			  " and b.ID_DEPENDENCIA IN() ",parametros);
     			  //"SELECT DISTINCT ID_DEPENDENCIA FROM CEDULA_TEC WHERE ID_PROYECTO IN (SELECT PROYECTO FROM SAM_GRUPO_PROYECTOS WHERE ID_GRUPO_CONFIG IN (SELECT B.ID_GRUPO_CONFIG FROM SAM_GRUPO_CONFIG_USUARIO A RIGHT OUTER JOIN  SAM_GRUPO_CONFIG B ON A.ID_GRUPO_CONFIG = B.ID_GRUPO_CONFIG where b.ESTATUS='ACTIVO' and TIPO='PROYECTO' AND  A.ASIGNADO =0 and ID_USUARIO=:cve_pers))) and  c.status=1  ",
     	 
      }
      
      
      public List getOTPorUnidad(String idtipoGasto,String unidad ,String claveBeneficiario) {
    	  
    	  Map parametros =  new HashMap<String,Object>();
    	  
    	  Integer cve_pers = this.getSesion().getIdUsuario();
    	  
    	  parametros.put("cve_pers", cve_pers);
    	  
    	  parametros.put("unidad", unidad);
    	  
    	  parametros.put("idtipoGasto", idtipoGasto);
    	  
    	  parametros.put("claveBeneficiario", claveBeneficiario);    	  
     	 return this.getNamedJdbcTemplate().queryForList("SELECT  B.CLV_PARBIT, A.DESC_TGASTO, C.N_PROGRAMA, C.CLV_PARTID, C.NUM_REQ,  "+
     			 " C.CVE_REQ, C.OBSERVA , isnull ( (select sum (cantidad * precio_est ) from req_movtos where cve_req=C.cve_req  ),0) IMPORTE "+ 
     			 " FROM         PLAN_ARBIT  A INNER JOIN "+
     			 " CEDULA_TEC  B ON A.CLV_PARBIT = B.CLV_PARBIT INNER JOIN "+
     			 " REQUISIC C ON B.ID_PROYECTO = C.ID_PROYECTO INNER JOIN "+
     			 " ORDEN_TRAB D ON C.CVE_REQ = D.CVE_REQ "+
     	 		 " WHERE     B.ID_RECURSO = :idtipoGasto AND " +
     			 " B.ID_DEPENDENCIA IN(SELECT DISTINCT ID_DEPENDENCIA FROM CEDULA_TEC WHERE ID_PROYECTO IN (SELECT ID_PROYECTO FROM VT_PROYECTOS_USUARIOS WHERE ID_USUARIO =:cve_pers)) AND D.CLV_BENEFI =:claveBeneficiario and C.STATUS IN(1,2)",parametros);
     	 		 //"B.ID_DEPENDENCIA IN(SELECT DISTINCT CLV_UNIADM FROM CEDULA_TEC WHERE ID_PROYECTO IN (SELECT ID_PROYECTO FROM SAM_GRUPO_PROYECTOS WHERE ID_GRUPO_CONFIG IN (SELECT B.ID_GRUPO_CONFIG FROM SAM_GRUPO_CONFIG_USUARIO A RIGHT OUTER JOIN  SAM_GRUPO_CONFIG B ON A.ID_GRUPO_CONFIG = B.ID_GRUPO_CONFIG where b.ESTATUS='ACTIVO' and TIPO='PROYECTO' AND  A.ASIGNADO =0 and ID_USUARIO=:cve_pers))) AND D.CLV_BENEFI = :claveBeneficiario and c.status=1 "
      }
      
 /*---------------------------- Eliminiar conceptos desde la orden pago cuando esta agregado el moviemineto de una factura como movimiento de la orden de pago*/           
      public boolean  eliminarDetalle( final List<Integer> detallesOp,final  Long idOrden ) {
    	  boolean exito=false;
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                	
	                	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	            		
	                	if(getPrivilegioEn(getSesion().getIdUsuario(), 114)){
	            			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	            		}
	            		
	            		Map Orden = getOrden(Long.parseLong(idOrden.toString()));
	            		
	                	for (Integer documento :detallesOp){
	                		
	                		Map detalle = getJdbcTemplate().queryForMap("select ID_PROYECTO, CLV_PARTID, TIPO,CVE_FACTURA  from SAM_MOV_OP  where ID_MOV_OP=?  ",new Object []{documento});
	                		String tipo= (String)detalle.get("TIPO");
	                		List<Map> CompVales = new ArrayList<Map>(); 
	                		//Buscar todos los vales de la factura
	                		List<Map> Vales = getJdbcTemplate().queryForList("SELECT * FROM SAM_FACTURAS_VALES WHERE CVE_FACTURA = ?", new Object[]{detalle.get("CVE_FACTURA")});
	                		for (Map vale : Vales)
	                		{
	                			//Buscar cada vale de la factura en la tabla COMP_VALE
	                			List<Map> Temp = getJdbcTemplate().queryForList("SELECT * FROM COMP_VALES WHERE CVE_OP = ? AND CVE_VALE = ?", new Object[]{idOrden, vale.get("CVE_VALE")});
	                			CompVales.addAll(Temp);
	                		}
	                		
	                		//Eliminar los vales de COMP_VALE
	                		for (Map v : CompVales)
	                		{
	                			//getJdbcTemplate().update("DELETE FROM CONCEP_VALE WHERE CVE_VALE=? AND CONS_VALE=?", new Object [] {v.get("CVE_VALE"),v.get("CONS_VALE")});
	                			getJdbcTemplate().update("UPDATE COMP_VALES SET TIPO='FA', CVE_OP=?  WHERE CVE_OP=? AND CVE_VALE=? AND CONS_VALE=?", new Object[]{detalle.get("CVE_FACTURA"),idOrden,v.get("CVE_VALE"),v.get("CONS_VALE")});
	                		}
	                	                		
	                		//Buscar todas las retenciones de la factura
	                		List<Map> CollectionRetenc = new ArrayList<Map>();
	                		List<Map> Retenciones = getJdbcTemplate().queryForList("SELECT * FROM SAM_FACTURA_MOV_RETENC WHERE CVE_FACTURA = ?", new Object[]{detalle.get("CVE_FACTURA")});
	                		for (Map retencion : Retenciones)
	                		{
	                			//Buscar cada retencion de la factura en la tabla MOV_RETENC
	                			List<Map> Temp = getJdbcTemplate().queryForList("SELECT * FROM MOV_RETENC WHERE CVE_OP = ? AND CLV_RETENC = ?", new Object[]{idOrden, retencion.get("CLV_RETENC")});
	                			CollectionRetenc.addAll(Temp);
	                		}
	                		//Eliminar las retenciones de MOV_RETENC
	                		for (Map r : CollectionRetenc)
	                		{
	                			//Map OrdenPago = getJdbcTemplate().queryForMap("SELECT * FROM SAM_ORD_PAGO WHERE CVE_OP =?", new Object[]{idOrden});
	                			Integer ejercicio =getJdbcTemplate().queryForInt("SELECT EJERCICIO FROM SAM_ORD_PAGO WHERE CVE_OP =?", new Object[]{idOrden});
	                			Integer cve_pers = getSesion().getIdUsuario();
	                			
	                	 		getJdbcTemplate().update("DELETE FROM MOV_RETENC WHERE CVE_OP=? AND CLV_RETENC=?", new Object [] {idOrden,r.get("CLV_RETENC")});
	                	 		
	                	 		String folio=rellenarCeros(idOrden.toString(),6);
	                	 		
	                	 		//guarda en la bitacora
	                	 		gatewayBitacora.guardarBitacora(gatewayBitacora.OP_MOV_ELIMINA_RETENCION, ejercicio, cve_pers, idOrden, folio, "OP", null, null, null, "Cons: "+r.get("RET_CONS"), 0D);
	                		}
	               
	                		//Buscar todas los archivos de la factura
	                		List<Map> Archivos = new ArrayList<Map>();
	                		
	                		List<Map> ArchivosF = getJdbcTemplate().queryForList("SELECT * FROM SAM_FACTURAS_ARCHIVOS WHERE CVE_FACTURA = ?", new Object[]{detalle.get("CVE_FACTURA")});
	                		for (Map archivo : ArchivosF)
	                		{
	                			//Buscar cada archivo de la factura en la tabla SAM_OP_ANEXOS
	                			List<Map> Temp = getJdbcTemplate().queryForList("SELECT * FROM SAM_OP_ANEXOS WHERE CVE_OP = ? AND FILENAME LIKE '%"+archivo.get("NOMBRE")+"%'", new Object[]{idOrden});
	                			Archivos.addAll(Temp);
	                		}
	                		
	                		//Eliminar las retenciones de MOV_RETENC
	                		
	                		for (Map a : Archivos)
	                		{
	                			getJdbcTemplate().update("DELETE FROM SAM_OP_ANEXOS WHERE CVE_OP=? AND ANX_CONS LIKE '%"+a.get("ANX_CONS")+"%'", new Object [] {idOrden});
	                		}
	                		
	                		getJdbcTemplate().update("delete from SAM_MOV_OP where ID_MOV_OP= ?", new Object[]{documento});
	                	        	    			
	                	}
	                	
	                	
	                } });
	                exito=true;
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas "+e.getMessage());	                    
	                    //throw new RuntimeException(e.getMessage(),e);
	                }
	                catch (RuntimeException e) {   
	                	log.info("Hay vale con relacioneado con los detalles que intenta eliminar "+e.getMessage());
	                }
	                return exito;
	  }  
      
      
      public boolean  generarDetallesOT( final List<Integer> detallesOT,final  Long idOrden ) {
    	  boolean exito=false;
		  try {   
				//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	      		if(getPrivilegioEn(getSesion().getIdUsuario(), 114)){
	      			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	      		}
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                	gatewayDetallesOrdenDePagos.generarDetallesOT(detallesOT, idOrden, getSesion().getEjercicio(), getSesion().getIdUsuario());
	                } });
	                exito=true;
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas "+e.getMessage());	                    
	                    //throw new RuntimeException(e.getMessage(),e);
	                }
	                catch (RuntimeException e) {   
	                	log.info("Fallo al agregar OT/OS a la Orden de pago "+e.getMessage());
	                }
	                return exito;
	  }    

      public boolean  generarDetallesPedidos( final List<Integer> detallesPedido,final  Long idOrden ) {
    	  boolean exito=false;
		  try {                 
				//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	      		if(getPrivilegioEn(getSesion().getIdUsuario(), 114)){
	      			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	      		}
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                	gatewayDetallesOrdenDePagos.generarDetallesPedidos(detallesPedido, idOrden, getSesion().getEjercicio(), getSesion().getIdUsuario());
	                } });
	                exito=true;
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    //throw new RuntimeException(e.getMessage(),e);
	                }
	                catch (RuntimeException e) {   
	                	log.info("Hay vale con relacioneado con los detalles que intenta eliminar ");
	                }
	                return exito;
	  }    
      
      
      public boolean  generarDetallesPedidosParcial( final Long cve_ped, final  Long idOrden, final Double importe_op  ) {
    	  boolean exito=false;
		  try {  
				//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	      		if(getPrivilegioEn(getSesion().getIdUsuario(), 114)){
	      			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	      		}
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                	gatewayDetallesOrdenDePagos.generarDetallesPedidosParcial(cve_ped, idOrden, importe_op, getSesion().getEjercicio(), getSesion().getIdUsuario());
	                } });
	                exito=true;
	                } catch (DataAccessException e) {            
	                    log.info(e.getMessage());	                    
	                    //throw new RuntimeException(e.getMessage(),e);
	                }
	                catch (RuntimeException e) {   
	                	log.info(e.getMessage());
	                }
	                return exito;
	  }    
      
      public boolean  generarDetallesRequisicionParcial( final Long cve_req, final  Long idOrden, final Double importe_op  ) {
    	  boolean exito=false;
		  try {                
				//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	      		if(getPrivilegioEn(getSesion().getIdUsuario(), 114)){
	      			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	      		}
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                	gatewayDetallesOrdenDePagos.generarDetallesRequisicionParcial(cve_req, idOrden, importe_op, getSesion().getEjercicio(), getSesion().getIdUsuario());
	                } });
	                exito=true;
	                } catch (DataAccessException e) {            
	                    log.info(e.getMessage());	                    
	                    //throw new RuntimeException(e.getMessage(),e);
	                }
	                catch (RuntimeException e) {   
	                	log.info(e.getMessage());
	                }
	                return exito;
	  }  
      
      public  boolean guardarRetencion(Integer idRetencion,String  retencion,Double importeRetencion,String cveParbit,Long idOrden){
	    	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
	  		if(getPrivilegioEn(getSesion().getIdUsuario(), 114)){
	  			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
	  		}
    	  boolean resultado=true;    	  
    	  int existe = this.getJdbcTemplate().queryForInt("select count(*) from  MOV_RETENC where CVE_OP=? and CLV_RETENC=? and  RET_CONS!=? ", new Object []{idOrden,retencion,idRetencion==null ?-1 :idRetencion });    	    
    	  if (existe==0)
    	       gatewayOrdenDePagos.actualizarPrincipalRetencion(idRetencion, retencion, importeRetencion, cveParbit, idOrden, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
    	  else
    		  resultado=false;
    	  return resultado;    	  
      }
      
	  public void  eliminarRetenciones( final List<Integer> retenciones,final  Long idOrden ) {
		  try {                
			//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
      		if(getPrivilegioEn(getSesion().getIdUsuario(), 114)){
      			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
      		}
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Integer retencion :retenciones)
	                		gatewayOrdenDePagos.eliminarRetencion(idOrden, retencion, getSesion().getEjercicio(), getSesion().getIdUsuario());	                			                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
	  
      @SuppressWarnings("unchecked")
	public List getTodasRetencionesOrdenes(Integer idOrden) {
    	  return gatewayOrdenDePagos.getTodasRetencionesOrdenes(idOrden);
    	  }  
      
      /* Documentos */
     public List getDocumentosOrdenes (Long idOrden) {
    	 return gatewayOrdenDePagos.getDocumentosOrdenes(idOrden);
     }
     
     public void guardarDocumento(Integer idDocumento,String tipoMovDoc,String numeroDoc,String notaDoc,Long idOrden){
    	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
			if(getPrivilegioEn(this.getSesion().getIdUsuario(), 114)){
				throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
			}
    	 gatewayOrdenDePagos.actualizarPrincipalDocumento(idDocumento, tipoMovDoc, numeroDoc, notaDoc, idOrden,  getSesion().getEjercicio(), getSesion().getIdUsuario());
     }
     
     public void  eliminarDocumentos( final List<Integer> documentos,final  Long idOrden, final HttpServletRequest request ) {
		  try {                 
			//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
      		if(getPrivilegioEn(getSesion().getIdUsuario(), 114)){
      			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
      		}
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Integer documento :documentos)
	                		gatewayOrdenDePagos.eliminarDocumento(idOrden, documento, getSesion().getEjercicio(), getSesion().getIdUsuario(),request.getSession().getServletContext().getRealPath(""));	                			                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }     
     
     /* Vales */
     public List getValesOrdenes (Long idOrden) {
    	 return gatewayOrdenDePagos.getValesOrdenes(idOrden);
     }
     
     public String guardarVale(Integer idVale,Integer vale,double importe,double importeVale,Long idOrden, int idproyecto, String partida,double importeValeAnte){
    	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
			if(getPrivilegioEn(this.getSesion().getIdUsuario(), 114)){
				throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
			}
    	 return gatewayComprobacionesVales.actualizarConceptoPrincipalVale(idVale, vale, importe,importeValeAnte, idOrden,idproyecto,partida,"OP",getfechaActual(),null, getSesion().getEjercicio(), getSesion().getIdUsuario()); 
    }
     
     public void  eliminarVales( final List<Integer> documentos , final Long cve_op) {
		  try { 
			//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
				if(getPrivilegioEn(this.getSesion().getIdUsuario(), 114)){
					throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
				}
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Integer idVale :documentos) {	                		
	                		gatewayComprobacionesVales.eliminarVale( idVale, cve_op, getSesion().getEjercicio(), getSesion().getIdUsuario());
	                	}
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
          
     public List getValesDisponibles (int  idProyecto, String Partida ) {
    	 String sql = "SELECT VALES.ID_POLIZA_CH, VALES.ID_PAGO_ELEC, C.N_PROGRAMA, ex.CVE_VALE, NUM_VALE + ' - $ ' + CONVERT(varchar, ex.IMPORTE - (SELECT ISNULL(SUM(IMPORTE), 0) FROM COMP_VALES where CVE_VALE = ex.CVE_VALE AND ID_PROYECTO = ex.ID_PROYECTO AND CLV_PARTID = ex.CLV_PARTID ), 1) AS DATOVALE FROM dbo.SAM_MOV_VALES AS ex INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = ex.ID_PROYECTO) INNER JOIN SAM_VALES_EX AS V ON (V.CVE_VALE = ex.CVE_VALE) LEFT JOIN VALES ON (VALES.ID_VALE = V.CVE_VALE) WHERE V.STATUS=4 AND (VALES.ID_POLIZA_CH IS NOT NULL OR VALES.ID_PAGO_ELEC IS NOT NULL OR VALES.COMPROBADO IS NOT NULL OR VALES.FE_PAGO IS NOT NULL) and ex.ID_PROYECTO=? and ex.clv_partid = ? "+ 
					  " UNION "+ 
					  " SELECT VALES.ID_POLIZA_CH, VALES.ID_PAGO_ELEC, C.N_PROGRAMA, ex.CVE_VALE, NUM_VALE + ' - $ ' + CONVERT(varchar, ex.IMPORTE - (SELECT ISNULL(SUM(IMPORTE), 0) FROM COMP_VALES where CVE_VALE = ex.CVE_VALE ), 1) AS DATOVALE FROM dbo.SAM_MOV_VALES AS ex INNER JOIN SAM_VALES_EX AS V ON (V.CVE_VALE = ex.CVE_VALE) INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = ex.ID_PROYECTO) LEFT JOIN VALES ON (VALES.ID_VALE = V.CVE_VALE) WHERE V.STATUS =4 AND (VALES.ID_POLIZA_CH IS NOT NULL OR VALES.ID_PAGO_ELEC IS NOT NULL OR VALES.COMPROBADO IS NOT NULL OR VALES.FE_PAGO IS NOT NULL) and ex.ID_PROYECTO=? and ex.clv_partid IS NULL "+ 
					  " ORDER BY C.N_PROGRAMA ASC ";
    	 return this.getJdbcTemplate().queryForList(sql,new Object []{idProyecto,Partida,idProyecto});
     }
     
     public void aperturarOrdenes(final List<Long> cveOrdenes){
 		this.gatewayOrdenDePagos.aperturarOrdenes(cveOrdenes, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
 	}             
     
     public void cancelarOrden(final List<Long> lstcveOrden, final String motivo){
   	  try {     
   		  		final BigDecimal temp_importe = new BigDecimal("0.0");
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {	 
	                
	                //Buscar si existe el Super Privilegio para Cancelar Requisiciones
	    			boolean privilegio = getPrivilegioEn(getSesion().getIdUsuario(), 139);
	    				
                	//Verfificar si tiene los privilegios en solo lectura Ordenes de Pago
            		if(getPrivilegioEn(getSesion().getIdUsuario(), 114)&&privilegio==false){
            			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
            		}
            		
            		if(motivo==null || motivo.trim().equals(""))
            			throw new RuntimeException("No se puede eliminar la Orden de Pago si no se especifica el motivo de cancelación");
            		
	                /* Eliminar comprobaciones de pedidos y de requisiciones.*/	                
	                for(Long cveOrden: lstcveOrden){
				           	Map orden = getOrden(cveOrden);
		    		      	
		    		      	Date fechaCierre = new Date();
		    		  		fechaCierre = (Date) orden.get("FECHA_CIERRE2");
		    		  		Calendar c1 = Calendar.getInstance();
		    		  		//Si al Orden de pago no es del periodo y no tiene super-privilegio entonces no dejar cancelarla
		    		  		if(fechaCierre!=null&&privilegio==false)
			    		  		if((c1.get(Calendar.MONTH)+1) != ((fechaCierre.getMonth())+1) && !orden.get("STATUS").toString().equals("-1"))
			    		  		{
			    		  			throw new RuntimeException("No se puede cancelar la Orden de Pago "+orden.get("NUM_OP").toString()+", el periodo del documento es diferente al actual, consulte a su administrador");
			        		  	}
		    		  		
				           	BigDecimal importe = new BigDecimal("0.0");
				       		int ejercicio =(Short)orden.get("EJERCICIO");
				       		int mes = gatewayMeses.getMesActivo(ejercicio);
				            int tipo =(Integer )orden.get("TIPO");
				            int estatus = Integer.parseInt(orden.get("STATUS").toString());
				            temp_importe.add((BigDecimal) orden.get("IMPORTE")!=null ? (BigDecimal) orden.get("IMPORTE"): new BigDecimal("0"));
				            if ( estatus ==0  ){
					           	 List<Map> detalles = gatewayDetallesOrdenDePagos.getDetallesOrdenes(cveOrden);
					             for ( Map detalle: detalles ) {
					          		Long  proyecto = Long.parseLong(detalle.get("ID_PROYECTO").toString());
					              	String partida = detalle.get("CLV_PARTID").toString();
					               	importe = (BigDecimal)detalle.get("MONTO");
					          		if (tipo==2 ){
					           			//gatewayProyectoPartidas.actualizarPreCompromisoPresupuesto(proyecto, partida, mes, importe.doubleValue(), "COMPROMETER");
					           			//gatewayProyectoPartidas.comprometerPresupuesto(proyecto, partida, mes,importe.doubleValue(),"REDUCCION");
					           		}else if (tipo!=5 && tipo!=0){
					           			//gatewayProyectoPartidas.comprometerPresupuesto(proyecto, partida, mes,importe.doubleValue(),"REDUCCION");
					           		} 
					            }
					         }
				            
				            /*VERIFICAR FACTURAS*/
				            List<Map> movtos = getJdbcTemplate().queryForList("SELECT CVE_FACTURA, ID_MOV_OP FROM SAM_MOV_OP WHERE CVE_OP =? ", new Object[]{cveOrden});
				            for(Map row: movtos){
				            	if(row.get("CVE_FACTURA")!=null){
				            		getJdbcTemplate().update("UPDATE SAM_FACTURAS SET STATUS = ?, CVE_OP=? WHERE CVE_FACTURA = ?", new Object[]{1, null, row.get("CVE_FACTURA")});
				            		getJdbcTemplate().update("UPDATE SAM_MOV_OP SET CVE_FACTURA =NULL WHERE ID_MOV_OP =?", new Object[]{row.get("ID_MOV_OP")});
				            	}
				            }
				            
				            /*VERIFICAR SI LA OP APARTA EN OTRO PERIODO AL ACTUAL Y ADEMAS TIENE CONTRATO*/
				            if(getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_ORD_PAGO WHERE CVE_OP =? AND CVE_CONTRATO IS NOT NULL AND PERIODO<>?", new Object[]{cveOrden, gatewayMeses.getMesActivo(ejercicio)})>0){
		            			throw new RuntimeException("Imposible cancelar la Orden de Pago, ya hay un contrato asignado con un periodo diferente al mes actual, consulte a su administrador del sistema");
		            		}
				            	
				            /*PARA LOS CONTRATOS 25/08/2011*/
				            /*if (tipo==0){ //Pedidos
				                getJdbcTemplate().update("update SAM_PEDIDOS_EX set STATUS=1 where  CVE_PED in (select CVE_PED from SAM_OP_COMPROBACIONES where CVE_OP= ? ) ",new Object[]{cveOrden});
				                /*List <Map> lst_doc = getJdbcTemplate().queryForList("SELECT R.PERIODO, P.TOTAL, (SELECT R.CVE_CONTRATO FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) WHERE P.CVE_PED = C.CVE_PED) AS CVE_CONTRATO_PED, C.CVE_PED FROM SAM_OP_COMPROBACIONES AS C INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = C.CVE_OP) INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = C.CVE_REQ) INNER JOIN SAM_PEDIDOS_EX AS P ON (P.CVE_PED = C.CVE_PED) WHERE C.CVE_OP = ?", new Object[]{cveOrden});
				                for(Map row: lst_doc)
					    		{
				                	if(row.get("CVE_CONTRATO_PED")!=null)
				                		getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?) ", new Object[]{row.get("CVE_CONTRATO_PED"), "LIBERACION", "PED", row.get("CVE_PED"), row.get("PERIODO"), row.get("TOTAL")});
					    		}*/
				                
				    		/*}
				    		if (tipo==2){//Requisiciones    
				    			String tipo_doc = "";
				            	getJdbcTemplate().update("update SAM_REQUISIC set STATUS=2  WHERE cve_req in (select CVE_REQ from SAM_OP_COMPROBACIONES where CVE_OP= ? ) ",new Object[]{cveOrden});
				            	/*List <Map> lst_doc = getJdbcTemplate().queryForList("SELECT R.PERIODO, R.TIPO, (SELECT ISNULL(SUM(CANTIDAD*PRECIO_EST),0) AS N FROM SAM_REQ_MOVTOS AS M WHERE M.CVE_REQ = C.CVE_REQ) AS TOTAL, R.CVE_CONTRATO AS CVE_CONTRATO_REQ, C.CVE_REQ FROM SAM_OP_COMPROBACIONES AS C INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = C.CVE_OP) INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = C.CVE_REQ) WHERE C.CVE_OP = ?", new Object[]{cveOrden});
				                for(Map row: lst_doc)
					    		{
				                	if(row.get("TIPO").equals("2")) tipo_doc = "OS";
				                	if(row.get("TIPO").equals("3")) tipo_doc = "OT";
				                	if(row.get("TIPO").equals("4")) tipo_doc = "OT";
				                	if(row.get("TIPO").equals("5")) tipo_doc = "OS";
				                	if(row.get("TIPO").equals("6")) tipo_doc = "OS";
				                	if(row.get("TIPO").equals("8")) tipo_doc = "OS";
				                	
				                	if(row.get("CVE_CONTRATO_REQ")!=null)
				                		getJdbcTemplate().update("INSERT INTO SAM_COMP_CONTRATO(CVE_CONTRATO, TIPO_MOV, TIPO_DOC, CVE_DOC, PERIODO, IMPORTE) VALUES(?,?,?,?,?,?) ", new Object[]{row.get("CVE_CONTRATO_REA"), "LIBERACION", tipo_doc, row.get("CVE_REQ"), row.get("PERIODO"), row.get("TOTAL")});
					    		}
				    		}*/
				    	
						getJdbcTemplate().update("update r set r.STATUS=? from SAM_REQUISIC r , SAM_OP_COMPROBACIONES a  WHERE r.cve_req=a.CVE_REQ and a.CVE_OP=? ", new Object []{gatewayRequisicion.REQ_STATUS_PENDIENTE,cveOrden });
						getJdbcTemplate().update("update r set r.STATUS=? from SAM_PEDIDOS_EX r , SAM_OP_COMPROBACIONES a  WHERE r.CVE_PED=a.CVE_PED and a.CVE_OP=? ", new Object []{gatewayPedidos.PED_STATUS_PENDIENTE,cveOrden});		
						getJdbcTemplate().update("delete from SAM_OP_COMPROBACIONES where CVE_OP=? ", new Object []{cveOrden});
						
						/*por si hay algo en contratos*/
						//getJdbcTemplate().update("DELETE FROM SAM_COMP_CONTRATO WHERE CVE_DOC = ? AND TIPO_DOC = ? AND TIPO_MOV =?", new Object[]{cveOrden, "OP","LIBERACION"});
						
						/*Eliminacion de relaciones con vales.*/    	    		
						List<Map> vales = gatewayOrdenDePagos.getValesOrdenes( cveOrden ); 		
						for (Map vale :vales) {
							gatewayComprobacionesVales.eliminarVale((Integer)vale.get("ID_VALE"), cveOrden, getSesion().getEjercicio(), getSesion().getIdUsuario());
						} 				   
				 		gatewayOrdenDePagos.actualizarOrdenStatus(cveOrden ,gatewayOrdenDePagos.OP_ESTADO_CANCELADA, orden.get("STATUS").toString(), motivo);
				 		
				 		//guarda en la bitacora
						Map pre = gatewayOrdenDePagos.getProyectoPartidaOP(cveOrden);
						String folio=rellenarCeros(cveOrden.toString(),6);
						gatewayBitacora.guardarBitacora(gatewayBitacora.OP_CANCELA, ejercicio, getSesion().getIdUsuario(), cveOrden, folio, "OP", (Date) orden.get("V_FECHA"), pre.get("ID_PROYECTO").toString(), pre.get("CLV_PARTID").toString(), null, importe.doubleValue());
				 		//gatewayOrdenDePagos.guardaBitacora(cveOrden, getSesion().getEjercicio(), getSesion().getIdUsuario(), orden);
	                }
		
	          } });
         } catch (DataAccessException e) {            
             log.info("Los registros tienen relaciones con otras tablas ");	                    
             throw new RuntimeException(e.getMessage(),e);
         }	      
 	}
     
     public String getListUsuarios(int cve_pers){
 		return this.gatewayOrdenDePagos.getListUsuarios(cve_pers);
 	}
     
    public boolean moverOrdenesPago(final List<Long> lst_ordenes, final int cve_pers_dest){
    	try{
    		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
                	for (Long cve_op :lst_ordenes) {	                		
                		 gatewayOrdenDePagos.moverOrdenes(cve_op, cve_pers_dest, getSesion().getEjercicio(), getSesion().getIdUsuario());
                	}
                } });
    		return  true;
    	}
    	catch(DataAccessException e){
    		throw new RuntimeException(e.getMessage(), e);
    	}
    	
    }
    
    public String validarTipoGasto(Long cve_op, String tipo_gto, String ban){
    	try{
    		String texto = "";
    		List <Map> lst = this.getJdbcTemplate().queryForList("SELECT DISTINCT C.ID_RECURSO, PA.RECURSO FROM SAM_MOV_OP AS M "+
																"		INNER JOIN SAM_ORD_PAGO AS O ON (O.CVE_OP = M.CVE_OP)  "+
																"		INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = M.ID_PROYECTO) "+ 
																"		INNER JOIN CAT_RECURSO AS PA ON (PA.ID = C.ID_RECURSO) "+ 
																"	WHERE M.CVE_OP =? "+
																"	ORDER BY PA.RECURSO", new Object[]{cve_op});
    		if(lst.size()>1) 
    			texto = "El tipo de gasto guardado en la Orden de Pago es erroneo, esta operacion no puede continuar, consulte a su administrador del SAM";
    		if(lst.size()==1){
    			for(Map row:lst){
    				if(!row.get("ID_RECURSO").toString().equals(tipo_gto))
    					texto ="El tipo de gasto de la Orden de Pago no concuerda con los conceptos introducidos en ella no es válido, consulte a su administrador del SAM";
    			}
    		}
    		
    		if(!ban.equals("")){
	    		//validacion extra en el contrato de la orden de pago con el del documento (Pedido o OT,OS)
	    		List <Map> lstobj = getJdbcTemplate().queryForList("SELECT OP.CVE_CONTRATO, C.CVE_REQ, C.CVE_PED FROM SAM_OP_COMPROBACIONES AS C INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = C.CVE_OP) WHERE C.CVE_OP = ?", new Object[]{cve_op});
	    		for(Map row: lstobj)
	    		{
	    			if(row.get("CVE_REQ")!=null&&row.get("CVE_CONTRATO")!=null)
	    				if((getJdbcTemplate().queryForInt("SELECT ISNULL(CVE_CONTRATO,0) AS N FROM SAM_REQUISIC WHERE CVE_REQ = ?",new Object[]{row.get("CVE_REQ")}))!=0)
	    					texto ="Las Ordenes de Trabajo/Servicio de esta Orden de Pago ya estan relacionadas a un contrato, por favor quite el contrato de esta Orden de Pago ó verifique los conceptos y vuela a intentar esta operación";
	    			if(row.get("CVE_PED")!=null&&row.get("CVE_CONTRATO")!=null)
	    				if((getJdbcTemplate().queryForInt("SELECT CVE_CONTRATO FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) WHERE P.CVE_PED = ?",new Object[]{row.get("CVE_PED")}))!=0)
	    					texto ="Los Pedidos de esta Orden de Pago ya estan relacionadas a un contrato, por favor quite el contrato de esta Orden de Pago ó verifique los conceptos y vuela a intentar esta operación";
	    		}
	    		//////////////////////////////////////////////////////////////
    		}
    		
    		return  texto;
    	}
    	catch(DataAccessException e){
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }
    
    public Map getImporteDisponiblePedido(Long cve_ped){
    	return this.gatewayPedidos.getImporteDisponiblePedido(cve_ped);
    }
    
    public Map getImporteDisponibleRequisicion(Long cve_req){
    	return this.gatewayRequisicion.getImporteDisponibleRequisicion(cve_req);
    }
    
    public Map getNombreProveedor(String clv_benefi){
    	return this.getJdbcTemplate().queryForMap("SELECT CLV_BENEFI, NCOMERCIA FROM CAT_BENEFI WHERE CLV_BENEFI=?", new Object[]{clv_benefi});
    }
    
    public Map getFechaPeriodoOp(Long cve_op){
		  return this.getJdbcTemplate().queryForMap("SELECT NUM_OP, CONVERT(varchar(10), FECHA,103) AS FECHA, PERIODO FROM SAM_ORD_PAGO WHERE CVE_OP = ?", new Object[]{cve_op});
	  }
    
    public boolean cambiarFechaPeriodo(Long cve_op, String fechaNueva, int periodo){
		  return this.gatewayOrdenDePagos.cambiarFechaPeriodo(cve_op, fechaNueva, periodo, this.getSesion().getIdUsuario(), this.getSesion().getEjercicio());
	  }
    
    public Map getBeneficiario(Long cve_doc){
		  return this.gatewayOrdenDePagos.getBeneficiario(cve_doc);
	  }
    
    public boolean cambiarBeneficiario(Long cve_doc, String clv_benefi){ 
		return gatewayOrdenDePagos.cambiarBeneficiario(cve_doc, clv_benefi, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
	}
    
    public List<Map> getListaAnexosArchivosOP(Long cve_op){
    	return gatewayOrdenDePagos.getListaAnexosArchivosOP(cve_op);
    }
    
//---------------------------------- Clase que carga las facturas desde la lista en las OP ---------------------- //
    
    public String guardarFacturasEnOrdenPago(final Long cve_op, final Long[] cve_facturas){
    	return gatewayOrdenDePagos.guardarFacturasEnOrdenPago(cve_op, cve_facturas, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
    }
    
    
    public void generarDetallesContrato(String num_contrato, Long cve_contrato, Long cve_op, Double importe_op,  int proyecto, String clv_partid)
    {
    	gatewayOrdenDePagos.generarDetallesContrato(num_contrato, cve_contrato, cve_op, importe_op, proyecto, clv_partid);
    }
    
    public Double obtenerIvaOrdenPago(Long cve_op)
    {
    	return gatewayOrdenDePagos.obtenerIvaOrdenPago(cve_op);
    }
    
    
}
