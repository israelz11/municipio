/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @date: 30/June/2010
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayFamiliasArticulos;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/configuracion/cat_familias_articulos.action")
public class ControladorFamiliasArticulos extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorFamiliasArticulos.class.getName());
	
	@Autowired
	GatewayFamiliasArticulos gatewayFamiliasArticulos;
	
	public ControladorFamiliasArticulos() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {		
	    return "almacen/configuracion/cat_familias_articulos.jsp";
	}
	
	 public boolean guardarFamiliasArticulos(int id_familia, String descripcion, boolean status){
			return this.gatewayFamiliasArticulos.guardarFamiliasArticulos(id_familia, descripcion, status); 
	}
		
	 public List<Map> getListaFamiliasArticulos(){
	    	return gatewayFamiliasArticulos.getListaFamiliasArticulos();
	}
	 
	 public Map getFamilia(int id)
	 {
		 return this.gatewayFamiliasArticulos.getFamilia(id);
	 }
	 
	 public void eliminarFamiliasArticulos(final List<Long> id_familias)
	 {
		 try {    
				final int cve_pers = this.getSesion().getIdUsuario();
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Long id :id_familias)
	                		gatewayFamiliasArticulos.eliminarFamiliasArticulos(id, cve_pers);
	                } 
	             });
	           
	            } catch (DataAccessException e) {            
	            	 log.info("Los registros tienen relaciones con otras tablas ");	                    
	                 throw new RuntimeException(e.getMessage(),e);
	            }	               
		 
	 }

}
