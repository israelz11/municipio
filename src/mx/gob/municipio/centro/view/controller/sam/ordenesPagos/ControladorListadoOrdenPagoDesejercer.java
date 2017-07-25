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
@RequestMapping("/sam/ordenesdepago/lista_ordenPagoDesejercer.action")

public class ControladorListadoOrdenPagoDesejercer  extends ControladorBase {
	
	
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		int mes = request.getParameter("mes")==null ?1 : Integer.parseInt(request.getParameter("mes"));
		int listado = 0;
		modelo.put("mes", request.getParameter("mes")==null ? "1": request.getParameter("mes"));
				
		modelo.put("lstOrdenesPagoDesejercer", this.ordenesPagoDesejercer(Integer.parseInt(modelo.get("mes").toString())));
	    return "sam/ordenesdepago/lista_ordenPagoDesejercer.jsp";
	}
	
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	public List<Map> ordenesPagoDesejercer(int mes){
		return this.gatewayOrdenDePagos.getListadoOrdenesPagoDesejercer(mes, this.getSesion().getEjercicio());
	}
	
	//Metodo transaccional para ejercer un listado de ordenes de pago
	public boolean desejercerOrdenPago(final List<Long> cve_op, final String texto){
		try {   
			//recuperamos el usuario de la sesion
			final int cve_pers = this.getSesion().getIdUsuario();
			final int ejercicio = this.getSesion().getEjercicio();
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {	    
	            	for(Long cve : cve_op){
	            		gatewayOrdenDePagos.desejercerOrdenPago(cve, texto, ejercicio, cve_pers);
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
	
	public boolean cancelarEjercidoOrdenPago(final Long cve_op, final String texto)
	{
		try {   
			final int cve_pers = this.getSesion().getIdUsuario();
			final int ejercicio = this.getSesion().getEjercicio();
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	            @Override
	            protected void   doInTransactionWithoutResult(TransactionStatus status) {	    
	            	gatewayOrdenDePagos.cancelarEjercidoOrdenPago(cve_op, texto, ejercicio, cve_pers);
	            } 
	         });
			return true;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException(e.getMessage(),e);
	   }
	}
}
