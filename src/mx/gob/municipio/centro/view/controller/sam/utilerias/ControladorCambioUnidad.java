/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/utilerias/cambioUnidad.action")
public class ControladorCambioUnidad extends ControladorBase {

	public ControladorCambioUnidad(){		
	}
	
	@Autowired
	public GatewayUnidadAdm gatewayUnidadAdm;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {
		modelo.put("idUnidad",this.getSesion().getClaveUnidad());	
	    return "sam/utilerias/cambioUnidad.jsp";
	}
	
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidadesAdmivas(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	@ModelAttribute("ejercicio")
    public List<Map> getEjercicio(){
		return this.getJdbcTemplate().queryForList("select distinct EJERCICIO  from MESES ");    		
    }
		
	public void cambioDeEntidad(int ejercicio, Integer idUnidad ){
		gatewayUnidadAdm.getUnidadAdmTodos();
		getSesion().setEjercicio(ejercicio);
		getSesion().setIdUnidad(idUnidad.toString());
		getSesion().setClaveUnidad(Integer.toString(idUnidad));
		Map resultado = this.getJdbcTemplate().queryForMap("select ID, DEPENDENCIA from CAT_DEPENDENCIAS where ID=?  ",new Object []{idUnidad});
		getSesion().setUnidad((String)resultado.get("DEPENDENCIA"));
		
	}
	
}
