/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Descriopcion Administra la interface de usuario para administrar unidades
 */
package mx.gob.municipio.centro.view.controller.correspondencia.administracion;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayUnidadesAdministrativas;
import mx.gob.municipio.centro.view.bases.ControladorBaseCorrespondencia;

@Controller
@RequestMapping("/correspondencia/administracion/cat_unidades.action")
public class ControladorAdministrarUnidades extends ControladorBaseCorrespondencia {
	private static Logger log = Logger.getLogger(ControladorAdministrarUnidades.class.getName());
	
	@Autowired
	private GatewayUnidadesAdministrativas gatewayUnidadesAdministrativas;
	
	/**
	 * @param args
	 */
	public ControladorAdministrarUnidades() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		modelo.put("detalles", gatewayUnidadesAdministrativas.getListaUnidades());
		return "correspondencia/administracion/cat_unidades.jsp";
	}
	
	
	public void guardarUnidadAdm(int cve_unidad, String descripcion, String prefijo, String responsable, String status ){
		gatewayUnidadesAdministrativas.guardarUnidadAdm(cve_unidad, descripcion, prefijo, responsable, status);
	}
	
	public Map getUnidadAdm(Long cve_unidad){
		return gatewayUnidadesAdministrativas.getUnidadAdm(cve_unidad);
	}
	
	public void eliminarUnidadAdm(final Long[] idUnidades){
		try{
			
			this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
                	for (Long idUnidad :idUnidades)
                		gatewayUnidadesAdministrativas.eliminarUnidadAdm(idUnidad);
                } 
             });
		}
		catch (DataAccessException e) {            
       	 log.info("Problemas al eliminar unidad");	                    
            throw new RuntimeException(e.getMessage(),e);
       }	
	}

}
