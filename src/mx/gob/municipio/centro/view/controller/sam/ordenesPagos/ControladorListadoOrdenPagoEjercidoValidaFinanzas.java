/**
 * @author ISC. Israel de la Cruz H.
 * @version 1.0
 * @Date 12/02/2011
 */
package mx.gob.municipio.centro.view.controller.sam.ordenesPagos;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
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
@RequestMapping("/sam/ordenesdepago/lista_ordenPagoValidacionFinanzas.action")
public class ControladorListadoOrdenPagoEjercidoValidaFinanzas extends ControladorBase {

	private static Logger log = Logger.getLogger(ControladorListadoOrdenPagoEjercidoValidaFinanzas.class.getName());
	public ControladorListadoOrdenPagoEjercidoValidaFinanzas() {
		// TODO Auto-generated method stub

	}
	
	
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
	    return "sam/ordenesdepago/lista_ordenPagoValidacionFinanzas.jsp";
	}
			
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	public List<Map> ordenesPagoDesejercer(int mes){
		return this.gatewayOrdenDePagos.getListadoOrdenesPagoDesejercer(mes, this.getSesion().getEjercicio());
	}
	
	public void fechaValidacionOrdenPago(final List<Long> lst_ordenes, final String fecha){
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
    protected void   doInTransactionWithoutResult(TransactionStatus status) {
                	for (Long cve_op :lst_ordenes) {	                		
               		 gatewayOrdenDePagos.validarOP(cve_op, fecha, getSesion().getEjercicio(), getSesion().getIdUsuario());
                	}
                } });
                } catch (DataAccessException e) {            
                    log.info("La Operacion de Validacion en Ordenes de Pago Ejercidas ha fallado");	                    
                    throw new RuntimeException(e.getMessage(),e);
                }
                
	}
	
	

}
