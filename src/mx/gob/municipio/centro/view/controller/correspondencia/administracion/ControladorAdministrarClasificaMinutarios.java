/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @date 08/10/2012
 * @Descriopcion Administra la interface de catalogo de clasificacion de minutarios
 */
package mx.gob.municipio.centro.view.controller.correspondencia.administracion;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayClasificaMinutarios;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseCorrespondencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/correspondencia/administracion/cat_clasifica_minutarios.action")
public class ControladorAdministrarClasificaMinutarios extends ControladorBaseCorrespondencia {

	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewayClasificaMinutarios gatewayClasificaMinutarios;
	
	public ControladorAdministrarClasificaMinutarios(){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String requestGetControlador(Map modelo, HttpServletRequest request){
		Long idDependencia=(request.getParameter("cbodependencia")==null ? Long.parseLong(this.getSesion().getClaveUnidad()) : Long.parseLong(request.getParameter("cbodependencia").toString()));
		modelo.put("idUnidad",idDependencia);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("detalles", gatewayClasificaMinutarios.getClasificaMinutarios(idDependencia));
		return "correspondencia/administracion/cat_clasifica_minutarios.jsp";
	}
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	 public void guardarClasificaMinutario(Long idClasifica, Long idDependencia,  String descripcion, String status){
		 gatewayClasificaMinutarios.guardarClasificaMinutario(idClasifica, idDependencia, descripcion, status);
	  }
	   
	 public void eliminarClasificaMinutarios(final Long[] detalles){
			try{
					
					this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
		                @Override
		                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
		                	for (Long id :detalles)
		                		gatewayClasificaMinutarios.eliminarClasificaMinutarios(id);
		                } 
		             });
				}
				catch (DataAccessException e) {            	                    
		            throw new RuntimeException(e.getMessage(),e);
		 }	
	   }

	
}
