/**
 * @author ISC. Israel de la Cruz Hdez.
 * @version 1.0, Date: 27/Ago/2009
 *
 */

package mx.gob.municipio.centro.view.controller.sam.requisiciones;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayAnexoConceptosRequisiciones;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBitacora;
import mx.gob.municipio.centro.model.gateways.sam.GatewayMovimientosRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayVales;
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
@RequestMapping("/sam/requisiciones/capturarRequisicion.action")
public class ControladorRequisicion extends ControladorBase {
	
	private static final List List = null;
	
/*	public static int REQ_STATUS_NUEVO = 0;
	public static int REQ_STATUS_PENDIENTE = 1;
	public static int REQ_STATUS_EN_PROCESO = 2;
	public static int REQ_STATUS_A_REVISAR = 3;
	public static int REQ_STATUS_CANCELADA = 4;
	public static int REQ_STATUS_FINIQUITADA = 5;*/
	
	
	private static Logger log = Logger.getLogger(ControladorRequisicion.class.getName());
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewayRequisicion gatewayRequisicion;
	@Autowired
	private GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
	@Autowired
	private GatewayMeses gatewayMeses;
	@Autowired
	private GatewayBitacora gatewayBitacora;
	@Autowired
	private GatewayVales gatewayVales;
	@Autowired
	private GatewayAnexoConceptosRequisiciones gatewayAnexoConceptosRequisiciones;
	@Autowired
	private  GatewayProyectoPartidas gatewayProyectoPartidas;

	/*Metodo de errores desactivado*/
	@SuppressWarnings("unchecked")
	/*Mapeo para la pagina de donde se recibira los GET*/ 
	@RequestMapping(method = RequestMethod.GET)  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		modelo.put("idUnidad",this.getSesion().getClaveUnidad());
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("cve_req", request.getParameter("cve_req"));
		Date fecha = new Date();
		modelo.put("xfecha", fecha);
		modelo.put("tipoRequisicion", getTipoRequisicion(this.getSesion().getIdUsuario()));
		//modelo.put("idAreaRequisicion",0);
		modelo.put("mes",0);
		if (this.getSesion().getIdGrupo() == null){
			modelo.put("mensaje","El usuario no tiene asignado un grupo de firmas ");
			return "insuficientes_permisos.jsp";
		}
	    return "sam/requisiciones/capturarRequisicion.jsp";
	}
	
	public ControladorRequisicion(){}
	
	/*Atributo Modelo para retornar las unidades administrativas como map*/
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	public List<Map> getTipoRequisicion(Integer idUsuario){
	    	return this.getJdbcTemplate().queryForList(" select * from SAM_CAT_TIPO_REQ where status=1  and  Id_TipoRequisicion in  (SELECT  b.ID_TIPO_REQ  FROM SAM_GRUPO_CONFIG_USUARIO a INNER JOIN SAM_GRUPO_TIPO_REQ b ON a.ID_GRUPO_CONFIG = b.ID_GRUPO_CONFIG  where a.ID_USUARIO =?   )  ", new Object []{idUsuario});			   
	}
	
	@ModelAttribute("areaRequisicion")
	public List<Map> getAreaRequisicion(){
		return this.getJdbcTemplate().queryForList("select * from SAM_CAT_AREA_REQ where Status=1 order by Numero asc");
	}
	
	@ModelAttribute("mesRequisicion")
	public List<Map> getMes(){
		return gatewayMeses.getMesesRequisicion(this.getSesion().getEjercicio());
	}
	
	public int getMesActivo(){
		return gatewayMeses.getMesActivo(this.getSesion().getEjercicio());
	}
	
	public boolean comprobarExistencia(String num_req){
		return this.gatewayRequisicion.comprobarExistencia(num_req);
	}
	
	public Long guardarRequisicion(Long id_proyecto, Long cve_req , Long cve_contrato, Long cve_vale, String num_req,  String cve_unidad, String  fecha, int  tipo, String  notas, int mes, int anualizada, String proyecto, String partida , String cve_benefi,  String area, String tipo_bien, String marca, String modelo, String placas, String num_inv, String color, String usuario, int cve_concurso ){	    	
	   	Long idReq= gatewayRequisicion.actualizarRequisicion(id_proyecto, cve_req, cve_contrato, cve_vale, num_req, cve_unidad,this.formatoFecha(fecha), tipo, notas, mes, gatewayRequisicion.REQ_STATUS_NUEVO, 0, this.getSesion().getEjercicio() ,this.getSesion().getIdUsuario(), anualizada, proyecto, partida, this.getSesion().getIdGrupo(),cve_benefi,  area, tipo_bien, marca, modelo, placas, num_inv, color, usuario, cve_concurso); 	    		    	
	    return idReq;
	}
	
/*	public boolean guardarOrdenTrabajo(Long cve_req, String cve_benefi,  String area, String tipo_bien, String marca, String modelo, String placas, String num_inv, String color, String usuario, int cve_concurso){
		return this.gatewayRequisicion.OrdenTrabajo(cve_req, cve_benefi, area, tipo_bien, marca, modelo, placas, num_inv, color, usuario, cve_concurso, this.getSesion().getIdUsuario());
	}
	*/

	public boolean guardarConcepto(Long cve_req, int tipo, Long id_req_movto, int consecutivo,  int id_articulo, String cve_unidad_med, String producto, Double precio_est, Double cantidad, String descripcion){
		return gatewayMovimientosRequisicion.actualizarConcepto(cve_req, tipo, id_req_movto, consecutivo, id_articulo, cve_unidad_med, producto, precio_est, cantidad, descripcion, this.getSesion().getIdUsuario());
	}
	
	public List <Map>getConceptosRequisicion(Long cve_req){
		return this.gatewayMovimientosRequisicion.getConceptos4(cve_req);
	}
	
	public Map getConceptoRequisicion(Long id_req_movto){
		return this.gatewayMovimientosRequisicion.getConcepto(id_req_movto);
	}
	
	public Map getRequisicion(Long cve_req){
		return this.gatewayRequisicion.getRequisicion(cve_req);
	}
	
	public List<Map> getLightRequisicion(Long cve_req){
		return this.gatewayRequisicion.getLightRequisicion(cve_req);	
	}
			
	public Map getAnexoConceptoRequisicion(Long id_req_movto){
		return this.gatewayAnexoConceptosRequisiciones.getAnexoConceptoRequisicion(id_req_movto);
	}
	
	public boolean guardarAnexoConceptoRequisicion(Long id_req_movto, String texto){
		return this.gatewayAnexoConceptosRequisiciones.guardarAnexoConceptoRequisicion(id_req_movto, texto);
	}
	
	/*Metodo para cerrar la requisicion*/
	public boolean cerrarRequisicion(Long cve_req, List<Map<String,String>> calendario){
		return this.gatewayRequisicion.cerrarRequisicion(cve_req, this.getSesion().getIdUsuario(),this.getSesion().getClaveUnidad(),calendario);
	}
	
	/*Metodo para  eliminar los conceptos de una requisicion*/
	public void eliminarMovimientoRequisicion(final List<Long> lst_id_req_movto, final Long cve_req){
		try {    
			final int cve_pers = this.getSesion().getIdUsuario();
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
                	for (Long id_req_movto :lst_id_req_movto)
                		gatewayMovimientosRequisicion.eliminarMovimientoRequisicion(id_req_movto, cve_req, cve_pers);
                } 
             });
           
            } catch (DataAccessException e) {            
            	 log.info("Los registros tienen relaciones con otras tablas ");	                    
                 throw new RuntimeException(e.getMessage(),e);
            }	               
	}
	
	public Map getPresupuestoReq(Long cve_req){
		   Map requisicion = gatewayRequisicion.getRequisicPresupuesto(cve_req);
			String proyecto= requisicion.get("ID_PROYECTO").toString();
			String partida = requisicion.get("CLV_PARTID").toString();
			Double importe= ((BigDecimal)requisicion.get("IMPORTE")).doubleValue();
			Map requisic = new HashMap();
			if(requisicion.get("CVE_CONTRATO")!=null)
				requisic = this.getJdbcTemplate().queryForMap("SELECT     [ID_PROYECTO], [CLV_PARTID], ISNULL([1], 0) AS ENERO, ISNULL([2], 0) AS FEBRERO, ISNULL([3], 0) AS MARZO, ISNULL([4], 0) AS ABRIL, "+ 
																"         ISNULL([5], 0) AS MAYO, ISNULL([6], 0) AS JUNIO, ISNULL([7], 0) AS JULIO, ISNULL([8], 0) AS AGOSTO, "+
																"         ISNULL([9], 0) AS SEPTIEMBRE, ISNULL([10], 0) AS OCTUBRE, ISNULL([11], 0) AS NOVIEMBRE, ISNULL([12], 0) AS DICIEMBRE "+
																"FROM     (SELECT PERIODO,[PROYECTO], [CLV_PARTID], SUM([MONTO]) AS APARTADO "+
																"                  FROM VT_COMPROMISOS  "+
																"                 WHERE TIPO_DOC IN ('CON') AND CVE_DOC =   ?  "+
																"                 GROUP BY PERIODO, [ID_PROYECTO], [CLV_PARTID]) AS CONTRATO "+
																"			PIVOT ( "+
																"				  SUM(APARTADO) "+
																"				  FOR PERIODO IN ([1], [2], [3], [4], [5], [6], [7], [8], [9], [10], [11], [12]) "+
																"             )  AS X", new Object[]{requisicion.get("CVE_CONTRATO")});
			else
				requisic = gatewayProyectoPartidas.getPresupuestoProyectoPartida( proyecto, partida);
			requisic.put("importe", importe);
			return   requisic;
		}
	
	public boolean comprobarCerradoBitacora(Long cve_req){
		return this.gatewayRequisicion.comprobarCerradoBitacora(cve_req);
	}
	
	public String enviarLotesPedido(final List<Long> lst_mov_req, final Long cve_ped, final Long cve_req){
		final String exito="";
		try {
				final boolean pedido_valido = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_PEDIDOS_EX WHERE CVE_PED = ? AND STATUS = ?", new Object[]{cve_ped,0})!=0;
				if(pedido_valido){
					/*Buscar los ids en otros pedidos*/
					String IDS = lst_mov_req.toString().substring(1, lst_mov_req.toString().length()-1);
					String sql = "SELECT COUNT(*) AS N FROM SAM_PED_MOVTOS WHERE ID_REQ_MOVTO IN("+IDS+")";
					if(getJdbcTemplate().queryForInt(sql)>0)
						return "El lote que intenta enviar ya se encuentra en un pedido, la operacion no es válida";
					
						this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
		                @Override
		                protected void   doInTransactionWithoutResult(TransactionStatus status) {
		                	for (Long id_movto :lst_mov_req){
		                		gatewayRequisicion.enviarLotesPedido(id_movto, cve_ped, getSesion().getEjercicio(), getSesion().getIdUsuario());
		                	}
		                	getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET CVE_REQ = ? WHERE CVE_PED =? ", new Object[]{cve_req, cve_ped});
		                } 
		             });
					
				}
				else
					return "<strong>El número de Pedido al que intenta exportar los lotes no cumple con los criterios necesarios, esto puede deberse a lo siguiente:</strong>\n-El número de pedido no es válido ó no existe\n-El Pedido no se encuentra reactivado";
            
            } catch (DataAccessException e) {            
            	 log.info("Fallo la operacion al enviar lotes al pedido");	                    
                 throw new RuntimeException(e.getMessage(),e);
            }	
            
		return exito;
	}
	
	public String getBeneficiario(String cve_benefi){
		return (String) this.getJdbcTemplate().queryForObject("SELECT NCOMERCIA FROM CAT_BENEFI WHERE CLV_BENEFI = ?", new Object[]{cve_benefi}, String.class);
	}
	
	public boolean importarNuevosLotes(final List<Long> lotes, final Long cve_req){
		boolean exito = false;
		try {    
			final String IDS = lotes.toString().substring(1, lotes.toString().length()-1);
			final int cve_pers = this.getSesion().getIdUsuario();
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                		final int clote = getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_REQ_MOVTOS WHERE CVE_REQ = ?", new Object[]{cve_req})+1;
                		/*guardar los lotes nuevos*/
                		
                		getJdbcTemplate().update("INSERT INTO SAM_REQ_MOVTOS (CVE_REQ, CLV_UNIMED, ID_ARTICULO, REQ_CONS, CANTIDAD, PRECIO_EST, NOTAS, STATUS, COMPROMETIDO) " + 
                									"SELECT "+cve_req+", CLV_UNIMED, ID_ARTICULO, REQ_CONS, CANTIDAD, PRECIO_EST, NOTAS, 0, 0 FROM SAM_REQ_MOVTOS WHERE ID_REQ_MOVTO IN ("+IDS+") ORDER BY ID_REQ_MOVTO ASC");
                		/*reenumerar los lotes*/
                		int cont = 0;
                		List <Map> mov = gatewayMovimientosRequisicion.getConceptos(cve_req);
                		for(Map m: mov){
                			cont++;
                			//getJdbcTemplate().update("UPDATE REQ_MOVTOS SET REQ_CONS = ? WHERE ID_REQ_MOVTO = ?", new Object[]{cont, m.get("ID_REQ_MOVTO")});
                		}
                 		/*Guardar en la bitacora*/
                		Date fecha = new Date();
            			Map requisicion = getJdbcTemplate().queryForMap("SELECT NUM_REQ, ID_PROYECTO, CLV_PARTID, PERIODO, FECHA, TIPO, STATUS, EJERCICIO, ISNULL((SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ ),0.00) AS IMPORTE FROM SAM_REQUISIC WHERE CVE_REQ = ? ", new Object []{cve_req});
            			gatewayBitacora.guardarBitacora(gatewayBitacora.AGREGAR_MOV_REQ, Integer.parseInt(requisicion.get("EJERCICIO").toString()), cve_pers, cve_req, requisicion.get("NUM_REQ").toString(), "REQ", null, requisicion.get("ID_PROYECTO").toString(), requisicion.get("CLV_PARTID").toString(), "Lotes importados ID_REQ_MOVTO ", 0D);
                }
               
             });
			exito=true;
            } catch (DataAccessException e) {                               
                 throw new RuntimeException(e.getMessage(),e);
            }
		return exito;	   
	}
	
	public boolean reenumerarLotes(final Long[] idLotes, final Long[] valLotes, final Long cve_req){
		boolean exito = false;
		try {    
			
			final int cve_pers = this.getSesion().getIdUsuario();
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                		int cont = 0;
                		/*Recorrer a cada uno de los lotes y actualizar con nuevo consecutivo*/
                		for(Long id: idLotes){
                			getJdbcTemplate().update("UPDATE SAM_REQ_MOVTOS SET REQ_CONS = ? WHERE ID_REQ_MOVTO = ?", new Object[]{valLotes[cont], id});
                			cont++;
                		}
                 		/*Guardar en la bitacora*/
                		Date fecha = new Date();
            			Map requisicion = getJdbcTemplate().queryForMap("SELECT NUM_REQ, ID_PROYECTO, CLV_PARTID, PERIODO, FECHA, TIPO, STATUS, EJERCICIO, ISNULL((SELECT SUM(CANTIDAD*PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ ),0.00) AS IMPORTE FROM SAM_REQUISIC WHERE CVE_REQ = ? ", new Object []{cve_req});
            			gatewayBitacora.guardarBitacora(gatewayBitacora.ACTUALIZO_CONCEPTO, Integer.parseInt(requisicion.get("EJERCICIO").toString()), cve_pers, cve_req, requisicion.get("NUM_REQ").toString(), "REQ", null, requisicion.get("ID_PROYECTO").toString(), requisicion.get("CLV_PARTID").toString(), "Lotes Reenumerados ID_REQ_MOVTO: "+idLotes, 0D);
                }
               
             });
			exito=true;
            } catch (DataAccessException e) {                               
                 throw new RuntimeException(e.getMessage(),e);
            }
		return exito;	   
	}
	
	public Map getInformacionPedido(Long id_ped_movto){
		return this.getJdbcTemplate().queryForMap("SELECT P.NUM_PED, P.TOTAL, CONVERT(VARCHAR(10),P.FECHA_PED, 103) FECHA_PED, UA.CLV_UNIADM, UA.DEPENDENCIA, (CR.RECURSO) TIPO_GASTO, CB.NCOMERCIA, P.TOTAL, P.NOTAS, RM.PED_CONS, RM.CANTIDAD, CU.UNIDMEDIDA, SA.DESCRIPCION AS ARTICULO, M.DESCRIP, RM.PRECIO_EST,    FROM SAM_PEDIDOS_EX P "+
															"INNER JOIN SAM_REQUISIC R ON (R.CVE_REQ = P.CVE_REQ) "+
															"INNER JOIN SAM_REQ_MOVTOS RM ON (RM.CVE_REQ = R.CVE_REQ) "+
															"INNER JOIN SAM_PED_MOVTOS M ON (M.ID_REQ_MOVTO = RM.ID_REQ_MOVTO) "+ 
															"INNER JOIN CEDULA_TEC C ON (C.PROYECTO = R.PROYECTO) "+
															"LEFT JOIN CAT_BENEFI CB ON (CB.CLV_BENEFI = P.CLV_BENEFI) "+
															"LEFT JOIN CAT_UNIMED CU ON (CU.CLV_UNIMED = RM.CLV_UNIMED) "+
															"LEFT JOIN CAT_DEPENDENCIAS UA ON (UA.ID = R.ID_DEPENDENCIA) "+
															"LEFT JOIN CAT_RECURSO CR ON (CR.ID = C.ID_RECURSO) "+
															"LEFT JOIN SAM_CAT_ARTICULO SA ON (SA.ID_ARTICULO = RM.ID_ARTICULO) "+
														"WHERE M.ID_PED_MOVTO = ?", new Object[]{id_ped_movto});
	}
	
	public List <Map> getListaValesPresupuesto(Long cve_vale, String clv_benefi, int idRecurso, int tipo_doc, int idDependencia){
		if(clv_benefi==null) clv_benefi = "0";
		if(clv_benefi.equals("")) clv_benefi = "0";
		if(idDependencia==0) idDependencia = 0;
		return gatewayVales.getListaValesPresupuesto(cve_vale, clv_benefi, idRecurso, tipo_doc, this.getSesion().getIdUsuario(), idDependencia);
	}
	
	
}
