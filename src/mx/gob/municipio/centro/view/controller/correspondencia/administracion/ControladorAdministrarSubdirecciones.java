/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Descriopcion Administra la interface de catalogo de subdirecciones
 */
package mx.gob.municipio.centro.view.controller.correspondencia.administracion;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.correspondencia.GatewaySubdirecciones;
import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayUnidadesAdministrativas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/correspondencia/administracion/cat_subdirecciones.action")
public class ControladorAdministrarSubdirecciones extends ControladorBaseAlmacen {
	
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewaySubdirecciones gatewaySubdireccion;
	
	public ControladorAdministrarSubdirecciones(){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Long idDependencia=(request.getParameter("cbodependencia")==null ? Long.parseLong(this.getSesion().getClaveUnidad()) : Long.parseLong(request.getParameter("cbodependencia").toString()));
		modelo.put("idUnidad",idDependencia);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("detalles", gatewaySubdireccion.getSubdirecciones(idDependencia));
		return "correspondencia/administracion/cat_subdirecciones.jsp";
	}
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	public String guardarSubdireccion(Long idDependencia, Long idSubdireccion, String subdireccion, String responsable, String status){
		return gatewaySubdireccion.guardarSubdireccion(idDependencia, ((idSubdireccion==null) ? 0L: idSubdireccion), subdireccion, responsable, status);
	}
	
	public Map getSubdirecionDetalle(Long idSubdireccion){
		return gatewaySubdireccion.getSubdirecionDetalle(idSubdireccion);
	}
	
	public void eliminarSubdireccion(final Long[] detalles){
			try{
					
					this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
		                @Override
		                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
		                	for (Long id :detalles)
		                		gatewaySubdireccion.eliminarSubdireccion(id);
		                } 
		             });
				}
				catch (DataAccessException e) {            	                    
		            throw new RuntimeException(e.getMessage(),e);
		 }	
	}
}
