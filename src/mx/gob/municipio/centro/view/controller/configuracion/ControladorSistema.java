/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;

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
@RequestMapping("/sam/configuracion/sistema.action")
public class ControladorSistema extends ControladorBase {

	private static Logger log = Logger.getLogger(ControladorSistema.class.getName());

	 public ControladorSistema() {
	  }
	 @Autowired
	 SistemaGateway sistemaGateway;
	 
	 @SuppressWarnings("unchecked")
		@RequestMapping(method = RequestMethod.GET)    
		public String  requestGetControlador( Map modelo ) {
		 	modelo.put("sistemas",buscarSistemas());
		    return "sam/configuracion/sistema.jsp";
		}
	 
	/*@SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request, 
                                      HttpServletResponse response) {
        log.debug("Invalidando sesion actual!");		       
        //String  acciones = getValorString(request.getParameter("acciones"));
        /*String  descripcion = getValorString(request.getParameter("descripcion"));
        String  estatus= getValorString(request.getParameter("estatus"));
        Integer responsable = getValorInteger(request.getParameter("responsable"));
        Integer idSistema = getValorInteger(request.getParameter("id"));
        Map model = new HashMap();
               		
		/*
   	   model.put("sistemas",buscarSistemas());
   	   model.put("estatus",this.estatusAdminGateway().buscarPorTipo("SISTEMA"));
   	   model.put("personas",this.personasGateway().buscarTodos());
        return new ModelAndView("jsp/administracion/admin/sistema.jsp", "model", model);
    }*/
	
	public Map  buscarSistema(Integer idSistema) {
	    return this.sistemaGateway.buscarPorID(idSistema);  
	  }

	  public void guardarSistema(Integer idSistema,String descripcion,String estatus) {
	       this.sistemaGateway.guardarPrincipal(idSistema,descripcion,estatus);
	 }

	  public void eliminarSistema(List<Integer> idSistemas){
	  for( Integer idSistema: idSistemas)
	     this.sistemaGateway.eliminar(idSistema);
	  }
	  
	  public List<Map> buscarSistemas() {
	  return this.sistemaGateway.buscarTodos();
	  }
	  
	
}
