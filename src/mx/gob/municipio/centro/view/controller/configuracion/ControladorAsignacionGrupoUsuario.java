/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.configuracion;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGrupos;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/configuracion/configuracion_grupo_usuario.action")
public class ControladorAsignacionGrupoUsuario  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorAsignacionGrupoUsuario.class.getName());
	@Autowired
	GatewayGrupos gatewayGrupos;
	
	@Autowired
	GatewayUsuarios gatewayUsuarios;
	
	public ControladorAsignacionGrupoUsuario(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {
		modelo.put("idUsuario", this.getSesion().getIdUsuario());
		modelo.put("grupos", getGruposEstatus(this.getSesion().getIdUsuario()));
	    return "sam/configuracion/configuracion_grupo_usuario.jsp";
	}
	
	
	 public  void guardarUsuarioGrupo(Integer idUsuario, Integer idGrupo){		 
	        this.getJdbcTemplate().update("update SAM_GRUPO_CONFIG_USUARIO set ASIGNADO=0 where ID_USUARIO=? ", new Object []{idUsuario});   		
	        this.getJdbcTemplate().update("update SAM_GRUPO_CONFIG_USUARIO set ASIGNADO=1 where ID_USUARIO=? and ID_GRUPO_CONFIG=? ", new Object []{idUsuario,idGrupo});
     }
     
     public List getGruposEstatus(Integer idUsuario) {
      	  return this.getJdbcTemplate().queryForList(" SELECT    B.GRUPO_CONFIG, B.ID_GRUPO_CONFIG, A.ID_USUARIO, "+ 
                      " A.ID_GRUPO_CONFIG_USUARIO, A.ASIGNADO "+
                      " FROM SAM_GRUPO_CONFIG_USUARIO A JOIN "+
                      " SAM_GRUPO_CONFIG B ON A.ID_GRUPO_CONFIG = B.ID_GRUPO_CONFIG and ID_USUARIO=? where b.ESTATUS='ACTIVO' and B.TIPO='FIRMA' ", new Object []{idUsuario});
   	  }       
	
}
