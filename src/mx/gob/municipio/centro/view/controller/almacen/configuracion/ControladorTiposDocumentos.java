/**
 * @author ISC. Israel de la Cruz Hernández.
 * @version 1.0
 * @Date: 28/June/2010
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

import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/configuracion/cat_tipos_documentos.action")

public class ControladorTiposDocumentos extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorTiposDocumentos.class.getName());
	
	@Autowired
	GatewayTiposDocumentos gatewayTiposDocumentos;
	
	public ControladorTiposDocumentos() {
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {
		int cve_pers = this.getSesion().getIdUsuario();
		modelo.put("documentos", gatewayTiposDocumentos.getDocumentos());		
	    return "almacen/configuracion/cat_tipos_documentos.jsp";
	}
	
	 @ModelAttribute("tiposDocumentos")
	    public List<Map> getListaTiposDocumentos(){
	    	return gatewayTiposDocumentos.getDocumentos();
	    }
	 
	 public boolean guardarTipoDocumento(int id_tipo_documento, String descripcion, boolean status){
		return this.gatewayTiposDocumentos.guardarTipoDocumento(id_tipo_documento, descripcion, status); 
	 }
	 
	 public Map getTipoDocumento(int id)
	 {
		 return this.getJdbcTemplate().queryForMap("SELECT *FROM TIPOS_DOCUMENTOS WHERE ID_TIPO_DOCUMENTO = ?", new Object[]{id});
	 }
	 
	 public void eliminarTiposDocumentos(final List<Long> id_tipos_documentos)
	 {
		 try {    
				final int cve_pers = this.getSesion().getIdUsuario();
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Long id :id_tipos_documentos)
	                		gatewayTiposDocumentos.eliminarTiposDocumentos(id, cve_pers);
	                } 
	             });
	           
	            } catch (DataAccessException e) {            
	            	 log.info("Los registros tienen relaciones con otras tablas ");	                    
	                 throw new RuntimeException(e.getMessage(),e);
	            }	               
		 
	 }
	 
}
