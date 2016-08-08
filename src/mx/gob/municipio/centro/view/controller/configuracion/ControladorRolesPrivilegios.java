/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;



import mx.gob.municipio.centro.model.gateways.sam.catalogos.RolGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.RolPrivilegioGateway;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.SistemaGateway;
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
@RequestMapping("/sam/configuracion/rolesPrivilegio.action")
public class ControladorRolesPrivilegios extends ControladorBase  {

	private static Logger log = Logger.getLogger(ControladorRolesPrivilegios.class.getName());

	 public ControladorRolesPrivilegios() {
	  }
	 @Autowired
	 RolGateway rolGateway;
	 @Autowired
	 SistemaGateway sistemaGateway;
	 @Autowired
	 RolPrivilegioGateway rolPrivilegioGateway;
		
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {
	  	modelo.put("sistemas", sistemaGateway.buscarTodos());
	  	modelo.put("roles", rolGateway.buscarTodos());
	  return "sam/configuracion/rolesPrivilegio.jsp";
	}
	
	public List buscarPrivilegiosRoles( Integer idRol, Integer idSistema ) {
		  return this.getJdbcTemplate().queryForList("SELECT     SAM_MODULO.MOD_DESCRIPCION, SAM_PRIVILEGIO.ID_PRIVILEGIO, SAM_PRIVILEGIO.PRI_DESCRIPCION, "+ 
                      "SAM_ROL_PRIVILEGIO.ID_ROL_PRIVILEGIO "+
                      "FROM SAM_MODULO INNER JOIN "+
                      "SAM_PRIVILEGIO ON SAM_MODULO.ID_MODULO = SAM_PRIVILEGIO.ID_MODULO AND "+ 
                      "SAM_MODULO.ID_SISTEMA = ? LEFT OUTER JOIN "+
                      "SAM_ROL_PRIVILEGIO ON SAM_PRIVILEGIO.ID_PRIVILEGIO = SAM_ROL_PRIVILEGIO.ID_PRIVILEGIO AND "+
                      "SAM_ROL_PRIVILEGIO.ID_ROL = ? order by SAM_MODULO.MOD_DESCRIPCION ", new Object[]{ idSistema,idRol});	        
	  }
		  
	   public void guardarPrivilegios(final List<Map<String,String>> idPrivilegios,final Integer idRol ){		   	  				
		   try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	          	  			  @Override
	                            protected void doInTransactionWithoutResult(TransactionStatus status) {	          	  			    
	          	  				for( Map<String,String> datos: idPrivilegios){
	          	  				    Integer idPriviRol = !(datos.get("idPriviRol")).equals("") ? Integer.parseInt(datos.get("idPriviRol")) : null ;
	          	  				    Integer checado = !(datos.get("checado")).equals("") ? Integer.parseInt(datos.get("checado")): 0;
	          	  				    Integer idPrivi = !(datos.get("idPrivi")).equals("") ? Integer.parseInt(datos.get("idPrivi")): null;
	          	  				    if ( idPriviRol== null && checado.equals(1) )
	          	  				      getJdbcTemplate().update("insert into SAM_ROL_PRIVILEGIO (ID_ROL,ID_PRIVILEGIO) values (?,?) ", new Object []{idRol,idPrivi});
	          	  				    if ( idPriviRol!= null && checado.equals(0) )
	          	  				      getJdbcTemplate().update("delete  from SAM_ROL_PRIVILEGIO where ID_ROL_PRIVILEGIO=?", new Object []{idPriviRol});  
	          					   }	          						 	          				  
	          	  			  }      	
	                        });
	        } catch (DataAccessException e) {        		            
	        }
	   }
}
