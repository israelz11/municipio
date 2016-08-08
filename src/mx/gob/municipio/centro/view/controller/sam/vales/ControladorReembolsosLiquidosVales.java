/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.vales;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayCedulasTecnicas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayComprobacionesVales;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayVales;
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
@RequestMapping("/sam/vales/reembolsos.action")
public class ControladorReembolsosLiquidosVales extends ControladorBase {

	public ControladorReembolsosLiquidosVales(){		
	}
	
	@Autowired
	public GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	public GatewayVales gatewayVales;
	@Autowired
	public GatewayCedulasTecnicas gatewayCedulasTecnicas;
	@Autowired
	GatewayComprobacionesVales gatewayComprobacionesVales;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST })    
	public String  requestGetControlador( Map modelo , HttpServletRequest request) {
		Long clave =  request.getParameter("cve_val")==null ? null : request.getParameter("cve_val").equals("") ? null : Long.parseLong(request.getParameter("cve_val"))  ;		
		modelo.put("idUnidad",this.getSesion().getClaveUnidad());
		modelo.put("nombreUnidad",this.getSesion().getUnidad());	
		modelo.put("fechaActual",this.getfechaActualCadena());
		if (clave!= null ) {			
			 Map vale = gatewayVales.getVale(clave);
			 if (vale!=null){				 
			   modelo.put("vale",gatewayVales.getVale(clave));
			   modelo.put("idUnidad",vale.get("CLV_UNIADM"));
			   modelo.put("nombreUnidad",vale.get("UNIADM"));
			   modelo.put("regresar","SI");
			 }			
		}									  
		return "sam/vales/reembolsos.jsp";
	}
		
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidadesAdmivas(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
			
	 public void guardarComprobacion(final Integer idVale,final double importe,final double importeValeAnte,final  int idproyecto,final  String partida,final String fecha,final  String  fechaDeposito){
		 try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
                  protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                		Map m = gatewayComprobacionesVales.getDatosVale(Long.parseLong(idVale.toString()), idproyecto, partida);
	                		Double restante = redondea(Double.parseDouble(m.get("RESTANTE").toString()),2);
	                		Double dif = restante-importe;
	                		if(dif<0) 
	                			 throw new RuntimeException("El importe es mayor al restante disponible en programa/partida, revise y vuelva a intentar esta operación");
	                		else{
	                			gatewayComprobacionesVales.actualizarConceptoPrincipalVale( null,idVale, importe,importeValeAnte, null,idproyecto,partida,"RL",formatoFecha(fecha),formatoFecha(fechaDeposito), getSesion().getEjercicio(), getSesion().getIdUsuario());
		                		gatewayComprobacionesVales.verficaComprobacion(idVale);
	                		}
	                		
	                } });
         } catch (DataAccessException e) {            
             //log.info("Los registros tienen relaciones con otras tablas ");	                    
             throw new RuntimeException(e.getMessage(),e);
         }	  
    	 }
	
	  
	    
	    public List<Map> getComprobacionesVales(Integer idVale){
	    	return gatewayComprobacionesVales.getComprobacionesVales(idVale);	
	    }
	 		   
	   
	   public void  eliminarReembolso( final List<Integer> reembolsos,final Integer idVale ) {
			  try {                 
		            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
		                @Override
		    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
		                	for (Integer idVale :reembolsos) {		                		
		                		gatewayComprobacionesVales.eliminarVale(idVale, 0L, getSesion().getEjercicio(), getSesion().getIdUsuario());
		                	}
		                	gatewayComprobacionesVales.verficaComprobacion(idVale);
		                } });
		                } catch (DataAccessException e) {            
		                    //log.info("Los registros tienen relaciones con otras tablas ");	                    
		                    throw new RuntimeException(e.getMessage(),e);
		                }	                	                		  	  
		  }
	   
	   public List <Map> getMovimientoVales(Long cve_vale){
		   return  gatewayComprobacionesVales.getMovimientoVales(cve_vale);
	   }
	   
	   public Map getDatosVale(Long cve_vale, int idproyecto, String clv_partid){
		   return gatewayComprobacionesVales.getDatosVale(cve_vale, idproyecto, clv_partid);
	   }
	   
}
