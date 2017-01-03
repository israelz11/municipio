/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/configuracion/personas_usuarios.action")
public class ControladorUsuarios  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorUsuarios.class.getName());
	@Autowired
	GatewayUsuarios gatewayUsuarios;
	
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	public ControladorUsuarios(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {
		modelo.put("profesiones",this.getProfesiones());
		modelo.put("unidades",gatewayUnidadAdm.getUnidadAdmTodos());
	    return "sam/configuracion/personas_usuarios.jsp";
	}
		
	 public  void guardarUsuario(final Long idPersona,final String nombre,final String apaterno,final String amaterno,final String curp,final String rfc,final String profesion,final Integer idArea,final 
			 int idUnidad,final String login,final String password,final String estatus){
	      try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	          	  			  @Override
	                            protected void doInTransactionWithoutResult(TransactionStatus status) {	          	  				  
	          	  			  
		 Long idPersona2=gatewayUsuarios.actualizarPersonaPrincipal(idPersona,nombre,apaterno,amaterno,curp,rfc,profesion);
		 
		 //if (idArea!=null)
		gatewayUsuarios.actualizarTrabajadorPrincipal((idPersona!=null) ? idPersona:0L ,idPersona2, idUnidad);
		 if (login!=null && !login.equals("") )
			 gatewayUsuarios.actualizarUsuarioPrincipal((idPersona!=null) ? idPersona:0L, idPersona2, login, password , estatus);

	          	  			 }                                      
                });
} catch (DataAccessException e) {
	log.error("Error El registro no se inserto ", e);
  //  return "Error: El registro no se inserto ";            
    //throw new RuntimeException(e.getMessage(),e);
}

     }
       
     public List getUsuariosPorEjemplo(String  nombre , String aPaterno , String aMaterno) {
      	  return gatewayUsuarios.getPersonasPorEjemplo(nombre, aPaterno, aMaterno);
   	  } 
     
    public List getProfesiones(){    
    	return this.getJdbcTemplate().queryForList("SELECT * FROM SAM_PROFESION");
    }
    
    public List getAreasUnidad(String orId){    
    	return this.gatewayUnidadAdm.getAreasPorUnidad(orId);
    }
     
}
