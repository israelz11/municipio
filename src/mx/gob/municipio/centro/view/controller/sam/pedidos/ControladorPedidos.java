/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.pedidos;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayMovimientosRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPedidos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/pedidos/capturarPedidos.action")

public class ControladorPedidos extends ControladorBase {
	private static Logger log = Logger.getLogger(ControladorPedidos.class.getName());
	@Autowired
	private GatewayRequisicion gatewayRequisicion;
	@Autowired
	private GatewayProyectoPartidas gatewayProyectoPartida;
	@Autowired
	private GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
	@Autowired
	private GatewayPedidos gatewayPedidos;
	@Autowired
	GatewayMeses gatewayMeses;
	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	
	String mensaje;
	Map datos;
	/*Metodo de errores desactivado*/
	@SuppressWarnings("unchecked")
	/*Mapeo para la pagina de donde se recibira los GET*/ 
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST })  
	public String  requestGetControlador(Map modelo, HttpServletRequest request) {
		Long cvePedido=request.getParameter("cve_ped")==null ? null: request.getParameter("cve_ped").equals("")? null: Long.parseLong(request.getParameter("cve_ped"));  
		Long cveRequisicion=request.getParameter("claveRequisicion")==null ? null: request.getParameter("claveRequisicion").equals("")? null: Long.parseLong(request.getParameter("claveRequisicion"));
		//comprobar si existe pedido y no existe la requisicion
		if(cvePedido!=null&&cveRequisicion==null){
			cveRequisicion = this.getJdbcTemplate().queryForLong("SELECT CVE_REQ FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{cvePedido});
		}
		
		modelo.put("cve_ped",cvePedido);
		modelo.put("cve_req", cveRequisicion);
		if (cvePedido!=null )
		modelo.put("map", gatewayPedidos.getPedido(cvePedido));
		
		Date fecha = new Date();
		modelo.put("fecha", fecha);
		
		//Condicionar si traigo cve_ped y no traigo cve_req entrar al if, de caso cotrario buscar la cve_req del pedido en else
		Map requisicion= this.getRequisicion(cveRequisicion);
		int mesActivo =gatewayMeses.getMesActivo(this.getSesion().getEjercicio());
		modelo.put("modelo", requisicion);		
		modelo.put("mesActivo", this.getNombreMes(mesActivo));
		modelo.put("mes", mesActivo);	
		modelo.put("num_contrato", requisicion.get("NUM_CONTRATO"));
		modelo.put("cve_contrato", requisicion.get("CVE_CONTRATO"));
		modelo.put("observa", requisicion.get("OBSERVA"));
		
		
		modelo.put("presupuesto", this.getJdbcTemplate().queryForList("SELECT ID_PROYECTO, N_PROGRAMA, '"+requisicion.get("CLV_PARTID").toString()+"' AS CLV_PARTID,  ISNULL(dbo.getAutorizado(?,?,?,?),0) AS AUTORIZADO, ISNULL(dbo.getPrecomprometido(?,?,?),0) AS PRECOMPROMETIDO, ISNULL(dbo.getComprometido(?,?,?),0) AS COMPROMETIDO, ISNULL(dbo.getEjercido(?,?,?),0) AS EJERCIDO, ISNULL(dbo.getDisponible(?,?,?),0) AS DISPONIBLE FROM CEDULA_TEC AS CT WHERE CT.ID_PROYECTO = ? ", new Object[]{mesActivo, mesActivo, requisicion.get("ID_PROYECTO"), requisicion.get("CLV_PARTID"),  mesActivo, requisicion.get("ID_PROYECTO"), requisicion.get("CLV_PARTID"),  mesActivo, requisicion.get("ID_PROYECTO"), requisicion.get("CLV_PARTID"),  mesActivo, requisicion.get("ID_PROYECTO"), requisicion.get("CLV_PARTID"),  mesActivo, requisicion.get("ID_PROYECTO"), requisicion.get("CLV_PARTID"), requisicion.get("ID_PROYECTO")}));//gatewayProyectoPartida.getPresupuesto(Long.parseLong(requisicion.get("ID_PROYECTO").toString()), "", requisicion.get("CLV_PARTID").toString(), mesActivo, this.getSesion().getIdUsuario(), 0,0) );
		
		if( cvePedido==null && cveRequisicion!=null )
			//si es requi calendarizada mostrar lotes validados de diferente manera
			if(requisicion.get("TIPO").toString().equals("7"))
				modelo.put("mov", gatewayMovimientosRequisicion.getConceptos3(cveRequisicion));
			else
				modelo.put("mov", gatewayMovimientosRequisicion.getConceptos2(cveRequisicion));
		else{
			cveRequisicion =gatewayPedidos.getNumRequisicion(cvePedido);
			modelo.put("cve_req",cveRequisicion  );			
			modelo.put("mov", gatewayPedidos.getConceptos(cvePedido));
		}		
	    return "sam/pedidos/capturarPedidos.jsp";
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
	@SuppressWarnings("deprecation")
	
	public Map getRequisicion(Long cve_req){
			return this.gatewayRequisicion.getRequisicion(cve_req);
	}
	
	/*Metodo usado para obtener los lotes del pedido*/
	public List <Map> getConceptos(Long cve_ped){
		return this.gatewayPedidos.getConceptos(cve_ped);
	}
	
	/*Metodo usado para guardar el pedido*/
	public Map guardarPedido(final  Long cve_ped,final   Long cve_req,final   String fecha,final   String contrato,final   int cve_concurso,final   String fecha_entrega,final   String cve_beneficiario,final   String condicion_pago,final   String lugar_entrega,final   String notas,final   List<Long> id_req_movtos,final   List<Double> cantidades,final   List<String> conceptos,final   List<Double> precios_unit,final   Double iva, final int tipo_iva, final Double descuento ){
		final Date fecha_ped = formatoFecha(fecha);		
		datos = new HashMap();
		try {    
			final int cve_pers = this.getSesion().getIdUsuario();
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	    
                	datos = gatewayPedidos.guardarEditarPedidos(cve_ped, cve_req, fecha_ped, contrato, cve_concurso, fecha_entrega, cve_beneficiario, getSesion().getIdUsuario(), condicion_pago, lugar_entrega, notas, id_req_movtos, cantidades, conceptos, precios_unit, iva, tipo_iva, descuento, getSesion().getEjercicio(), getSesion().getIdGrupo());
                } 
             });
           
            } catch (DataAccessException e) {	            	
                 throw new RuntimeException(e.getMessage(),e);
            }
		return datos;
	}

	/*Metodo para  lotes de un pedido*/
	public void eliminarLotesPedido(final List<Long> lst_id_ped_movto, final Long cve_ped, final Double descuento){
		try {    
			final int cve_pers = this.getSesion().getIdUsuario();
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
                	for (Long id_ped_movto :lst_id_ped_movto){
                		gatewayPedidos.eliminarLotesPedido(id_ped_movto, cve_ped, cve_pers, getSesion().getEjercicio());
                	}
                } 
             });
            
            //recalcular el total del pedido
            this.gatewayPedidos.actualizarTotalesPedido(cve_ped, 0D, descuento);
            //reenumerar los lotes del pedido
            this.gatewayPedidos.reenumerarLotes(cve_ped);
            } catch (DataAccessException e) {            	                    
                 throw new RuntimeException(e.getMessage(),e);
            }	               
	}
	
	public void cerrarPedido(final Long cve_ped, final int tipo, final Double iva){
		try {    
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
                	gatewayPedidos.cerrarPedido(cve_ped, tipo, iva, getSesion().getIdUsuario(), getSesion().getEjercicio());
                
                } 
             });
           
            } catch (DataAccessException e) {            	                    
                 throw new RuntimeException(e.getMessage(),e);
            }
		
	}
	
	public void moverLotes(final List<Long> lst_id_ped_movto, final Long cve_ped_fuente, final Long cve_ped_destino, final Double descuento){
		try {    
			final int cve_pers = this.getSesion().getIdUsuario();
			final int ejercicio = this.getSesion().getEjercicio();			 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void  doInTransactionWithoutResult(TransactionStatus status) {
                	int ped_cons = getJdbcTemplate().queryForInt("SELECT MAX(PED_CONS) AS N FROM SAM_PED_MOVTOS WHERE CVE_PED = ?", new Object[]{cve_ped_destino});
                	//Mueve cada uno de los lotes
                	for (Long id_ped_movto :lst_id_ped_movto){
                		ped_cons++;
                		gatewayPedidos.moverLotes(id_ped_movto, cve_pers, cve_ped_fuente, cve_ped_destino, ped_cons, ejercicio);
                   	}
                	//Actualizar con el nuevo total de lotes
                	gatewayPedidos.actualizarTotalesPedido(cve_ped_fuente, 0D, (Double) getJdbcTemplate().queryForObject("SELECT DESCUENTO FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{cve_ped_fuente}, Double.class));
                	gatewayPedidos.actualizarTotalesPedido(cve_ped_destino, 0D, (Double) getJdbcTemplate().queryForObject("SELECT DESCUENTO FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{cve_ped_destino}, Double.class));
                	getJdbcTemplate().update("UPDATE SAM_PED_MOVTOS SET PED_CONS = (SELECT SAM_REQ_MOVTOS.REQ_CONS FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.ID_REQ_MOVTO = SAM_PED_MOVTOS.ID_REQ_MOVTO) WHERE CVE_PED = ?", new Object[]{cve_ped_destino});
                } 
             });
           
            } catch (DataAccessException e) {            	                    
                 throw new RuntimeException(e.getMessage(),e);
            }	        
	}
	
//-----------------------------APERURA DE PEDIDOS-------------------------------------------------------------------	
	public boolean aperturarPedidos(final List<Long> cvePed){
		boolean exito=false;
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
                	for(Long cve_ped:cvePed) 
                		gatewayPedidos.aperturarPedido(cve_ped, getSesion().getIdUsuario(), getSesion().getEjercicio());
                } 
            });
            exito=true;
           } catch (DataAccessException e) {  
                throw new RuntimeException(e.getMessage(),e);
           }	      
        return exito;
	}
	
//-------------------------------------CANCELAR PEDIDOS-------------------------------------	
	public void cancelarPedido(final Long[] cve_ped){
		  try {    
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	
	                	for(Long id: cve_ped)
	                	gatewayPedidos.cancelaPedido(id ,getSesion().getIdUsuario(), getSesion().getEjercicio());
	                } 
	             });
	           
	            } catch (DataAccessException e) {            	                    
	                 throw new RuntimeException(e.getMessage(),e);
	            }
	  	}
	  
  public String getComboPedidosRequisicion(Long cve_req, Long cve_ped){
	  return this.gatewayPedidos.getComboPedidosRequisicion(cve_req, cve_ped);
  }
  
  public Boolean reactivarPedido(Long cve_ped){
	return gatewayPedidos.reactivarPedido(cve_ped, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario()); 
  }
  
  public Map getFechaPeriodoPedido(Long cve_ped){
	  return this.getJdbcTemplate().queryForMap("SELECT NUM_PED, CONVERT(varchar(10), FECHA_PED,103) AS FECHA_PED FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?", new Object[]{cve_ped});
  }
  
  public boolean cambiarFechaPeriodo(Long cve_ped, String fechaNueva){
	  return this.gatewayPedidos.cambiarFechaPeriodo(cve_ped, fechaNueva, this.getSesion().getIdUsuario(), this.getSesion().getEjercicio());
  }
  
  public String getListUsuarios(int cve_pers){
 		return this.gatewayOrdenDePagos.getListUsuarios(cve_pers);
 }
	
  public boolean moverPedidos(Long[] cve_req, int cve_pers_dest){
	return this.gatewayPedidos.moverPedidos(cve_req, this.getSesion().getIdUsuario(), cve_pers_dest, this.getSesion().getEjercicio());
  }
  
  public Map getBeneficiario(Long cve_doc){
	  return this.gatewayPedidos.getBeneficiario(cve_doc);
  }
  
  public boolean cambiarBeneficiario(Long cve_req, String clv_benefi){
		return gatewayPedidos.cambiarBeneficiario(cve_req, clv_benefi, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
  }
	  
	public Map getReembolsoPedido(Long cve_ped){
		return gatewayPedidos.getReembolsoPedido(cve_ped);
	}
	
	public void guardarReembolsoPedido(Long cve_ped, Double monto){
		gatewayPedidos.gatewayPedidos(cve_ped, monto);
	}
	
	public void quitarReembolso(Long cve_ped){
		gatewayPedidos.quitarReembolso(cve_ped);
	}
	
	public List<Map> getMovimientosAjustadosPedidos(Long cve_ped)
	{
		return gatewayPedidos.getMovimientosAjustadosPedidos(cve_ped);
	}
	
	public Map getProyectoPartidaPedido(Long cve_ped)
	{
		return gatewayPedidos.getProyectoPartidaPedido(cve_ped);
	}
	
	public void guardarAjustePedidoPeredo(Long id_sam_mod_comp, Long cve_factura, int idProyecto, String clv_partid, String fecha, Double importe)
	{
		gatewayPedidos.guardarAjustePedidoPeredo(id_sam_mod_comp, cve_factura, idProyecto, clv_partid, fecha, importe);
	}
	
	public void eliminarConceptoAjustePedido(Long id_sam_mod_comp)
	{
		gatewayPedidos.eliminarConceptoAjustePedido(id_sam_mod_comp);
	}

}
