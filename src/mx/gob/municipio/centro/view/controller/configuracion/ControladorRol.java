/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;
import java.util.HashMap;

import mx.gob.municipio.centro.model.gateways.sam.catalogos.RolGateway;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/configuracion/roles.action")
public class ControladorRol extends ControladorBase  {

	private static Logger log = Logger.getLogger(ControladorRol.class.getName());

	 public ControladorRol() {
	  }
	 @Autowired
	 RolGateway rolGateway;
	 /*
	@SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request, 
                                      HttpServletResponse response) {
        log.debug("Invalidando sesion actual!");
        Map model = new HashMap();
   	   //model.put("estatus",this.getGateways().getEstatusAdminGateway().buscarPorTipo("SISTEMA"));
   	   //model.put("tiposRoles",buscarTiposRoles());
   	
        return new ModelAndView("jsp/administracion/admin/tipoRol.jsp", "model", model);
    }*/
	 
	 @SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.GET)    
		public String  requestGetControlador( Map modelo ) {
		 modelo.put("roles",buscarTiposRoles());
		    return "sam/configuracion/roles.jsp";
		}
	
	public Map  buscarTipoRol(Integer idTipoRol) {
	    return this.rolGateway.buscarPorID(idTipoRol);  
	  }

   public void guardarTipoRol(Integer idTipoRol,String descripcion,String estatus) {
	       this.rolGateway.guardarPrincipal(idTipoRol,descripcion,estatus);
	 }

   public void eliminarTipoRol(List<Integer> idTiposRoles){
	  for( Integer idTipoRol: idTiposRoles)
	     this.rolGateway.eliminar(idTipoRol);
	  }
	  
	  public List<Map> buscarTiposRoles() {
	  return this.rolGateway.buscarTodos();
	  }
	  	
}
