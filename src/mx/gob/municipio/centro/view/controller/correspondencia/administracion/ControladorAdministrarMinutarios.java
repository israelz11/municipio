/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Descriopcion Administra la interface de catalogo de minutarios
 */
package mx.gob.municipio.centro.view.controller.correspondencia.administracion;

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

import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayMinutarios;
import mx.gob.municipio.centro.model.gateways.correspondencia.GatewaySubdirecciones;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/correspondencia/administracion/cat_minutarios.action")
public class ControladorAdministrarMinutarios extends ControladorBaseAlmacen {

	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewaySubdirecciones gatewaySubdireccion;
	@Autowired
	private GatewayMinutarios gatewayMinutarios;
	
	public ControladorAdministrarMinutarios(){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Long idDependencia=(request.getParameter("cbodependencia")==null ? Long.parseLong(this.getSesion().getClaveUnidad()) : Long.parseLong(request.getParameter("cbodependencia").toString()));
		Long idSubdireccion = (request.getParameter("cbosubdireccion")==null ? 0L : Long.parseLong(request.getParameter("cbosubdireccion").toString()));
		modelo.put("idUnidad",idDependencia);
		modelo.put("idSubdireccion", idSubdireccion);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("subdirecciones", gatewaySubdireccion.getListaSubdirecciones(idDependencia));
		modelo.put("detalles", gatewayMinutarios.getMinutarios(idDependencia));
		return "correspondencia/administracion/cat_minutarios.jsp";
	}
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }

    public List<Map> getListaSubdirecciones(Long idDependencia){
    	return gatewaySubdireccion.getListaSubdirecciones(idDependencia);
    }
    
    public void guardarMinutario(Long idCatMinutario, Long idDependencia, Long idSubdireccion, String minutario, String status){
    	gatewayMinutarios.guardarMinutario(idCatMinutario, idDependencia, idSubdireccion, minutario, status);
    }
    
    public void eliminarMinutarios(final Long[] detalles){
		try{
				
				this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Long id :detalles)
	                		gatewayMinutarios.eliminarMinutario(id);
	                } 
	             });
			}
			catch (DataAccessException e) {            	                    
	            throw new RuntimeException(e.getMessage(),e);
	 }	
    }
	
}
