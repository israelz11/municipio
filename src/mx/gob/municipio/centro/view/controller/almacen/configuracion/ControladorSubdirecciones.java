/**
 * @author ISC. Israel de la Cruz Hernández.
 * @version 1.0
 * @date: 29/June/2010
 */
package mx.gob.municipio.centro.view.controller.almacen.configuracion;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.almacen.GatewaySubdirecciones;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/configuracion/cat_subdirecciones.action")
public class ControladorSubdirecciones extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorSubdirecciones.class.getName());
	
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	GatewaySubdirecciones gatewaySubdirecciones;
	
	public ControladorSubdirecciones()  {
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {
		modelo.put("idUnidad", this.getSesion().getIdUnidad());
		modelo.put("nombreUnidad", this.getSesion().getUnidad());
	    return "almacen/configuracion/cat_subdirecciones.jsp";
	}
	
	public boolean guardarSubdireccion(int id_subdireccion, int id_unidad, String descripcion, String encargado, String puesto, boolean status)
	{
		return gatewaySubdirecciones.guardarSubdireccion(id_subdireccion, id_unidad, descripcion, encargado, puesto, status);
	}
	
	@ModelAttribute("ListaSubdirecciones")
    public List<Map> getListaSubdirecciones(){
    	return gatewaySubdirecciones.getSubdirecciones();
    }
	
	public Map getSubdireccion(int id_subdireccion){
		return this.gatewaySubdirecciones.getSubdireccion(id_subdireccion);
	}
	
	@ModelAttribute("unidades")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();
    }
	
	public void eliminarSubdirecciones(final List<Long> id_subdirecciones)
	 {
		 try {    
				final int cve_pers = this.getSesion().getIdUsuario();
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Long id :id_subdirecciones)
	                		gatewaySubdirecciones.eliminarSubdirecciones(id, cve_pers);
	                } 
	             });
	           
	            } catch (DataAccessException e) {            
	            	 log.info("Los registros tienen relaciones con otras tablas ");	                    
	                 throw new RuntimeException(e.getMessage(),e);
	            }	               
		 
	 }

}
