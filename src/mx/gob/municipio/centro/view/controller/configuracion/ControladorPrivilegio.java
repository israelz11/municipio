/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;

import mx.gob.municipio.centro.model.gateways.sam.catalogos.ModuloGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.PrivilegioGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.SistemaGateway;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;



@Controller
@RequestMapping("/sam/configuracion/privilegio.action")
public class ControladorPrivilegio extends ControladorBase  {

	private static Logger log = Logger.getLogger(ControladorPrivilegio.class.getName());

	 public ControladorPrivilegio() {
	  }
	 @Autowired
	 ModuloGateway moduloGateway;
	 @Autowired
	 SistemaGateway sistemaGateway;
	 @Autowired
	 PrivilegioGateway privilegioGateway;
	
	 @SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.GET)    
		public String  requestGetControlador( Map modelo ) {
		 	modelo.put("sistemas", buscarSistemas());
		    return "sam/configuracion/privilegio.jsp";
		}
	 
	public Map  buscarPrivilegio(Integer idPrivilegio) {
	    return this.privilegioGateway.buscarPorID(idPrivilegio);  
	  }

   public void guardarPrivilegio(Integer idModulo,String descripcion,String estatus,Integer modulo, String tipo, String url, Integer orden) {
	       this.privilegioGateway.guardarPrincipal(idModulo,descripcion,estatus,modulo,tipo,url,orden);
	 }

   public void eliminarPrivilegio(List<Integer> idPrivilegios){
	  for( Integer idPrivilegio: idPrivilegios)
	     this.privilegioGateway.eliminar(idPrivilegio);
	  }
	  
	  public List<Map> buscarPrivilegios(Integer idModulo) {
	  return this.privilegioGateway.buscarPorModulo(idModulo);
	  }
	  
	  public List<Map> buscarSistemas() {
	  return this.sistemaGateway.buscarTodos();
	  }
	  	  
	
}
