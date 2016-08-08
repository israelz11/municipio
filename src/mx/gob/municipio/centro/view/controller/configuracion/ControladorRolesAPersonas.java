/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;


import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

@Controller
@RequestMapping("/sam/configuracion/rolesapersonas.action")
public class ControladorRolesAPersonas extends ControladorBase  {

	private static Logger log = Logger.getLogger(ControladorRolesAPersonas.class.getName());
	
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	GatewayUsuarios gatewayUsuarios;
	 public ControladorRolesAPersonas() {
	  }
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {
	    modelo.put("unidades",gatewayUnidadAdm.getUnidadAdmTodos());
	  return "sam/configuracion/rolesapersonas.jsp";
	}
	
	public List buscarRolesUsuarios( Integer idPersona ) {
		  return this.getJdbcTemplate().queryForList("SELECT     SAM_USUARIO_ROL.ID_USUARIO_ROL, SAM_ROL.ROL_DESCRIPCION, SAM_ROL.ID_ROL "+
				  	" FROM   SAM_USUARIO_ROL RIGHT OUTER JOIN "+
                    " SAM_ROL ON SAM_USUARIO_ROL.ID_ROL = SAM_ROL.ID_ROL and SAM_USUARIO_ROL.CVE_PERS=?  ", new Object[]{ idPersona});	        
	  }
		  
	   public void guardarRolUsuario(final List<Map<String,String>> idRolUsuariuos,final Integer idUsuario ){		   	  				
		   try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	          	  			  @Override
	                            protected void doInTransactionWithoutResult(TransactionStatus status) {	          	  			    
	          	  				for( Map<String,String> datos: idRolUsuariuos){
	          	  				    Integer idRolUsuario = !(datos.get("idRolUsuario")).equals("") ? Integer.parseInt(datos.get("idRolUsuario")) : null ;
	          	  				    Integer checado = !(datos.get("checado")).equals("") ? Integer.parseInt(datos.get("checado")): 0;
	          	  				    Integer idRol = !(datos.get("idRol")).equals("") ? Integer.parseInt(datos.get("idRol")): null;
	          	  				    if ( idRolUsuario== null && checado.equals(1) )
	          	  				      getJdbcTemplate().update("insert into SAM_USUARIO_ROL (CVE_PERS,ID_ROL) values (?,?) ", new Object []{idUsuario,idRol});
	          	  				    if ( idRolUsuario!= null && checado.equals(0) )
	          	  				      getJdbcTemplate().update("delete  from SAM_USUARIO_ROL where ID_USUARIO_ROL=?", new Object []{idRolUsuario});  
	          					   }	          						 	          				  
	          	  			  }      	
	                        });
	        } catch (DataAccessException e) {        		            
	        }
	   }
	   
	   public List<Map> getUsuariosUnidad(String idUnidad) {
		  return  gatewayUsuarios.getUsuariosUnidad(idUnidad);
	   }
}
