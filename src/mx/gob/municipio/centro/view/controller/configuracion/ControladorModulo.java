/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;

import mx.gob.municipio.centro.model.gateways.sam.catalogos.ModuloGateway;
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
@RequestMapping("/sam/configuracion/modulo.action")
public class ControladorModulo extends ControladorBase  {

	private static Logger log = Logger.getLogger(ControladorModulo.class.getName());

	 public ControladorModulo() {
	  }
	 @Autowired
	 ModuloGateway moduloGateway;
	 @Autowired
	 SistemaGateway sistemaGateway;
	 
	 @SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.GET)    
		public String  requestGetControlador( Map modelo ) {
		     modelo.put("sistemas", buscarSistemas());
		    return "sam/configuracion/modulo.jsp";
		}
	 
	/*@SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request, 
                                      HttpServletResponse response) {
        log.debug("Invalidando sesion actual!");

        /*String  descripcion = getValorString(request.getParameter("descripcion"));
        String  estatus= getValorString(request.getParameter("estatus"));
        Integer responsable = getValorInteger(request.getParameter("responsable"));
        Integer idSistema = getValorInteger(request.getParameter("id"));
        Map model = new HashMap();
               		
		
   	   model.put("sistemas",buscarSistemas());
   	   //model.put("estatus",this.getGateways().getEstatusAdminGateway().buscarPorTipo("SISTEMA"));   	   
        return new ModelAndView("jsp/administracion/admin/modulo.jsp", "model", model);
    }*/
	
	public Map  buscarModulo(Integer idModulo) {
	    return this.moduloGateway.buscarPorID(idModulo);  
	  }

   public void guardarModulo(Integer idModulo,String descripcion,String estatus,Integer sistema) {
	       this.moduloGateway.guardarPrincipal(idModulo,descripcion,estatus,sistema);
	 }

   public void eliminarModulo(List<Integer> idModulos){
	  for( Integer idModulo: idModulos)
	     this.moduloGateway.eliminar(idModulo);
	  }
	  
	  public List<Map> buscarModulos(Integer idSistema) {
	  return this.moduloGateway.buscarPorSistema(idSistema);
	  }
	  
	  public List<Map> buscarSistemas() {
	  return this.sistemaGateway.buscarTodos();
	  }
	  
	
}
