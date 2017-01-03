/**
 * @author ISC. Israel de la Cruz H.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.ordenesPagos;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/ordenesdepago/lista_ordenPagoEjercer.action")

public class ControladorListadoOrdenPagoEjercido extends ControladorBase {
	public ControladorListadoOrdenPagoEjercido(){}
	
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		int mes = request.getParameter("mes")==null ?1 : Integer.parseInt(request.getParameter("mes"));
		int listado = 0;
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("ejercidas", request.getParameter("ejercidas")==null ? "false": request.getParameter("ejercidas"));
		modelo.put("por_ejercer", request.getParameter("por_ejercer")==null ? "false": request.getParameter("por_ejercer"));
		modelo.put("mes", request.getParameter("mes")==null ? "1": request.getParameter("mes"));
		modelo.put("fecha_ejercer", request.getParameter("fecha_ejercer")==null ? "false": request.getParameter("fecha_ejercer"));
		modelo.put("fecha", request.getParameter("fecha"));
		modelo.put("ordenesPagoEjercer", this.ordenesPagoEjercer(mes, listado));
		
		if(modelo.get("por_ejercer").equals("false")&&modelo.get("ejercidas").equals("false")) modelo.put("por_ejercer", "true");
		
		if(modelo.get("por_ejercer").equals("true")) listado = 1;
		if(modelo.get("ejercidas").equals("true")) listado = 2; 
		if(modelo.get("por_ejercer").equals("true")&&modelo.get("ejercidas").equals("true")) listado = 3; 
				
		modelo.put("lstOrdenesPagoEjercer", this.ordenesPagoEjercer(Integer.parseInt(modelo.get("mes").toString()), listado));
	    return "sam/ordenesdepago/lista_ordenPagoEjercer.jsp";
	}
			
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	public List<Map> ordenesPagoEjercer(int mes, int listado){
		return this.gatewayOrdenDePagos.getListadoOrdenesPagoEjercer(mes, listado, this.getSesion().getEjercicio());
	}
	
	//Metodo transaccional para ejercer un listado de ordenes de pago
	public boolean ejercerOrdenPago(final List<Long> cve_op, final boolean bfecha, final String fecha_ejerce){
		final Date fecha = this.formatoFecha(fecha_ejerce);
		try {   
			//recuperamos el usuario de la sesion
			final int cve_pers = this.getSesion().getIdUsuario();
			final int ejercicio = this.getSesion().getEjercicio();
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {	    
	            	for(Long cve : cve_op){
	            		//Ejecuta el metodo ejercerOrdenPago en el gateway gatewayOrdenDePagos para ejercer la OP
	            		gatewayOrdenDePagos.ejercerOrdenPago(cve, bfecha, fecha, ejercicio, cve_pers);
	            	}
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException(e.getMessage(),e);
	   }	               	
	}
	
	public Date formatoFecha (String fecha) {
	   	 Date fechaResultado =null;
	   	 if (fecha!=null && !fecha.equals("")){
			  try {
				  fechaResultado =  new Date(new SimpleDateFormat("dd/MM/yyyy").parse(fecha).getTime());          
				  } catch (Throwable ex) {				  					  
					  throw new IllegalArgumentException(ex.getMessage(), ex);
				  }           	
	   	 }
	        return fechaResultado;
	   }
	
	public boolean cambiarFechaOrdenPago(final List<Long> lst_cve_op, final String fechaNueva){
		try {   
			final int cve_pers = this.getSesion().getIdUsuario();
			final int ejercicio = this.getSesion().getEjercicio();
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {	    
	            	for(Long cve_op : lst_cve_op){
	            		gatewayOrdenDePagos.cambiarFechaOrdenPago(cve_op, fechaNueva,  ejercicio, cve_pers);
	            	}
	            } 
	         });
			return true;

		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException(e.getMessage(),e);
	   }	
	}
	
	public List<Map> cargarRelaciones(String tipo){
		return this.gatewayOrdenDePagos.cargarRelaciones(tipo);
	}
	
	public List<Map> cargarRelacionesDocumentos(Long id_relacion){
		return this.gatewayOrdenDePagos.cargarRelacionesDocumentos(id_relacion);
	}
	
	public boolean abrirRelacion(int id_relacion, String status){
		try{
			this.getJdbcTemplate().update("UPDATE SAM_OP_RELACION SET CERRADA =? WHERE ID_RELACION = ? ", new Object[]{status, id_relacion});
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException(e.getMessage(),e);
	   }
	}
	
	public String agregarOpRelacion(List<Long> lst_cve_op, int id_relacion, String texto) throws Exception{
		String errores = "";
		if(texto.equals("")||texto.equals("{}")) texto = "";
		java.util.Date fecha = new java.util.Date();
		String tipo = (String) this.getJdbcTemplate().queryForObject("SELECT TIPO_RELACION FROM SAM_OP_RELACION WHERE ID_RELACION = ?", new Object[]{id_relacion}, String.class);
		Map relacion = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_OP_RELACION WHERE ID_RELACION =?", new Object[]{id_relacion});
        for(Long cve_op : lst_cve_op){
        	String sql = "SELECT COUNT(*) AS N FROM SAM_ORD_PAGO WHERE CVE_OP = ?";
        	if(tipo.equals("VALES")) sql = "SELECT COUNT(*) AS N FROM SAM_VALES_EX WHERE CVE_VALE = ?";
        	//comprobar que realmente existe
        	if(tipo.equals("VALES")){
        		if(this.getJdbcTemplate().queryForInt(sql, new Object[]{cve_op})==0)
        		return "El Vale "+cve_op+" no existe en la base de datos, vuelva a escribir un numero valido";
        	}
        	else{
        		if(this.getJdbcTemplate().queryForInt(sql, new Object[]{cve_op})==0)
            		return "El documento "+cve_op+" no existe en la base de datos, vuelva a escribir un numero valido";
        	}
        	//comprobar la existencia de la orden de pago
        	if(this.getJdbcTemplate().queryForInt("SELECT COUNT(*) AS N FROM SAM_OP_RELACION_DETALLES WHERE ID_RELACION = ? AND CVE_OP = ?", new Object[]{id_relacion, cve_op})!=0)
        		errores +=rellenarCeros(cve_op.toString(),6)+",";
        	else
        	{
        		if(!relacion.get("TIPO_RELACION").toString().equals("VALES_DEVOLUCION"))
        			this.getJdbcTemplate().update("INSERT INTO SAM_OP_RELACION_DETALLES (ID_RELACION, CVE_OP, FECHA, DEVOLUCION, OBSERVACIONES) VALUES(?,?,?,?,?)", new Object[]{id_relacion, cve_op, fecha, "N", texto});
        		else
	        		if(errores.equals("") && /*relacion.get("ID_DEPENDENCIA_DEV").toString().equals("0") &&*/ relacion.get("TIPO_RELACION").toString().equals("VALES_DEVOLUCION")){
	        			//comprobar antes si el detalle corresponde a la Unidad de Negocio que tiene el reporte.
	        			if(!relacion.get("ID_DEPENDENCIA_DEV").toString().equals(getUnidadValeDetalle(cve_op)) &&  !relacion.get("ID_DEPENDENCIA_DEV").toString().equals("0"))
	        				throw new Exception("El Vale que intenta agregar tiene una dependencia diferente a los Vales agregados en el listado");
	        			
	        			this.getJdbcTemplate().update("INSERT INTO SAM_OP_RELACION_DETALLES (ID_RELACION, CVE_OP, FECHA, DEVOLUCION, OBSERVACIONES) VALUES(?,?,?,?,?)", new Object[]{id_relacion, cve_op, fecha, "N", texto});
	                	
	                	if(relacion.get("ID_DEPENDENCIA_DEV").toString().equals("0"))
	                	{
	                		int idDependenciaVale = this.getJdbcTemplate().queryForInt("SELECT ID_DEPENDENCIA FROM SAM_VALES_EX WHERE CVE_VALE =?", new Object[]{cve_op});
	                		this.getJdbcTemplate().update("UPDATE SAM_OP_RELACION SET ID_DEPENDENCIA_DEV =? WHERE ID_RELACION=?", new Object[]{idDependenciaVale, id_relacion});
	                	}
	                }
        	}
        }
        
        if(errores!="") errores = "Las siguientes documentos ya se encuentran en la Relación actual: "+errores.substring(0, errores.length()-1);
		return errores;
	}
	
	public String getUnidadValeDetalle(Long cve_vale)
	{
		Map vale = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_VALES_EX WHERE CVE_VALE =?", new Object[]{cve_vale});
		return vale.get("ID_DEPENDENCIA").toString();
	}
	
	public String eliminarOpRelacion(List<Long> lst_det_op){
		try{
			String errores = "";
			java.util.Date fecha = new java.util.Date();
			int idRelacion = 0;
			int cdetalles = 0;
	        for(Long id_det : lst_det_op){
	        	idRelacion = this.getJdbcTemplate().queryForInt("SELECT ID_RELACION FROM SAM_OP_RELACION_DETALLES WHERE ID_DETALLE =?", new Object[]{id_det});
	        	this.getJdbcTemplate().update("DELETE FROM SAM_OP_RELACION_DETALLES WHERE ID_DETALLE = ? ", new Object[]{id_det});
	        }
	        
	        Map relacion = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_OP_RELACION WHERE ID_RELACION =?", new Object[]{idRelacion});
	        cdetalles = this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM SAM_OP_RELACION_DETALLES WHERE ID_RELACION =?", new Object[]{idRelacion});
	        if(cdetalles == 0 && relacion.get("TIPO_RELACION").toString().equals("VALES_DEVOLUCION"))
	        {
	        	this.getJdbcTemplate().update("UPDATE SAM_OP_RELACION SET ID_DEPENDENCIA_DEV =? WHERE ID_RELACION=?", new Object[]{0, idRelacion});
	        }
	        
	        return "";
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException(e.getMessage(),e);
	   }
	}
	
	public Map cargarDetalle(Integer id_detalle){
		return this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_OP_RELACION_DETALLES WHERE ID_DETALLE = ?",new Object[]{id_detalle});
	}
	
	public String guardarOpDetalle(Integer id_detalle, String texto, String devolucion, int idDependencia){
		try{
			this.getJdbcTemplate().update("UPDATE SAM_OP_RELACION_DETALLES SET OBSERVACIONES = ?, DEVOLUCION = ?, ID_DEPENDENCIA_DEV=? WHERE  ID_DETALLE = ?", new Object[]{texto, devolucion, idDependencia, id_detalle});
			return "";
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException(e.getMessage(),e);
	   }
	}
	
	public Integer nuevaRelacion(String fecha, int tipo, int idDependencia){
		Date f = formatoFecha (fecha);
		String tipos = "";
		if(tipo==1) tipos= "ENVIO";
		if(tipo==2) tipos = "DEVOLUCION";
		if(tipo==3) tipos = "VALES";
		if(tipo==4) tipos = "VALES_DEVOLUCION";
		
		Integer cve = (this.getJdbcTemplate().queryForInt("SELECT MAX(NUMERO) AS N FROM SAM_OP_RELACION WHERE EJERCICIO = ? AND TIPO_RELACION = ? ", new Object[]{this.getSesion().getEjercicio(), tipos}))+1;
		String numero = this.rellenarCeros(cve.toString(), 4);
		this.getJdbcTemplate().update("INSERT INTO SAM_OP_RELACION(NUMERO, FOLIO, EJERCICIO, FECHA, CVE_PERS, TIPO_RELACION, ACTIVO, CERRADA, DEVUELTO, ID_GRUPO, ID_DEPENDENCIA_DEV) VALUES(?,?,?,?,?,?,?,?,?,?,?)", new Object[]{cve, numero, this.getSesion().getEjercicio(), f, this.getSesion().getIdUsuario(), tipos, "S", "N", "N",1, idDependencia });
		return this.getJdbcTemplate().queryForInt("SELECT MAX(ID_RELACION) AS ID FROM SAM_OP_RELACION WHERE EJERCICIO = ? AND TIPO_RELACION = ? ",new Object[]{this.getSesion().getEjercicio(), tipos});
	}
	
	public String cambiarFechaRelacion(String fecha, Long idRelacion, int IdDependencia){
		try{
			Date f = formatoFecha (fecha);
			if(IdDependencia!=0)
				this.getJdbcTemplate().update("UPDATE SAM_OP_RELACION SET FECHA =?, ID_DEPENDENCIA_DEV = ? WHERE ID_RELACION = ?", new Object[]{f, IdDependencia, idRelacion});
			else
				this.getJdbcTemplate().update("UPDATE SAM_OP_RELACION SET FECHA =? WHERE ID_RELACION = ?", new Object[]{f, idRelacion});
			
			return "";
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException(e.getMessage(),e);
	   }
	}
}
