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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayGrupos;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/configuracion/configuracion_grupos_usuarios.action")
public class ControladorAsignacionGruposUsuarios  extends ControladorBase {

	private static Logger log = 
        Logger.getLogger(ControladorAsignacionGruposUsuarios.class.getName());
	@Autowired
	GatewayGrupos gatewayGrupos;
	
	@Autowired
	GatewayUsuarios gatewayUsuarios;
	
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	
	public ControladorAsignacionGruposUsuarios(){		
	}		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo ) {
		modelo.put("usuarios", gatewayUsuarios.getUsuariosActivoTodos());		
	    return "sam/configuracion/configuracion_grupos_usuarios.jsp";
	}
	
	public List<Map> getUsuariosPorUnidad(String org_id){
		return this.gatewayUsuarios.getUsuariosUnidad(org_id);
	}
	
	 @ModelAttribute("tipoGrupos")
	    public List<Map> getTipoGrupos(){
	    	return 	gatewayGrupos.getTipoGrupos();
	    }
	 /*Atributo Modelo para retornar las unidades administrativas como map*/
	 @ModelAttribute("unidadesAdmiva")
	 public List<Map> getUnidades(){
	    return gatewayUnidadAdm.getUnidadAdmTodos();	
	}
	 
	
	 public  void guardarUsuariosGrupo(Integer idUsuario, List<Map> grupos){
		 for (Map grupo:grupos) {			 
			 Integer idGrupo=Integer.parseInt((String)grupo.get("idGrupo"));
			 Integer idGrupoUsuario=((String)grupo.get("idGrupoUser")).equals("")? null:Integer.parseInt((String)grupo.get("idGrupoUser"));
			 int seleccionado=((String)grupo.get("checado")).equals("false") ?  0: 1;
			 Integer idGrupoDefault=Integer.parseInt((String)grupo.get("defaultGrupo"));
			 
			 getJdbcTemplate().update("DELETE FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = ?", new Object[]{idUsuario});
			 if (seleccionado==1 && idGrupoUsuario==null ) 
				  this.getJdbcTemplate().update("insert into SAM_GRUPO_CONFIG_USUARIO  (ID_USUARIO,ID_GRUPO_CONFIG,ASIGNADO) values  (?,?,?) ", new Object []{idUsuario,idGrupo,idGrupoDefault});
			 else
			   if (seleccionado==0 && idGrupoUsuario!=null ) 
		          this.getJdbcTemplate().update("delete from SAM_GRUPO_CONFIG_USUARIO where ID_GRUPO_CONFIG_USUARIO=? ", new Object []{idGrupoUsuario});
			   else 
			   if (seleccionado==1 && idGrupoUsuario!=null  ) 
			        this.getJdbcTemplate().update("update SAM_GRUPO_CONFIG_USUARIO set ASIGNADO=? where ID_GRUPO_CONFIG_USUARIO=? ", new Object []{idGrupoDefault,idGrupoUsuario});
		 		 }   
		 	getJdbcTemplate().update("INSERT INTO SAM_TEMP_USR_PROY_PART SELECT ID_USUARIO, ID_GRUPO_CONFIG, PROYECTO, ID_PROYECTO, CLV_PARTID, ID_DEPENDENCIA, ID_GRUPO_PROYECTO, ID_GRUPO_PARTIDA, ID_RECURSO FROM VT_SAM_USUARIO_PROYECTOS_PARTIDAS WHERE VT_SAM_USUARIO_PROYECTOS_PARTIDAS.ID_USUARIO = ?", new Object[]{idUsuario});
     }
     
     public List getGruposEstatus(Integer idUsuario, String tipo) {
      	  return this.getJdbcTemplate().queryForList(" SELECT    B.TIPO, B.GRUPO_CONFIG, B.ID_GRUPO_CONFIG, A.ID_USUARIO, "+ 
                      " A.ID_GRUPO_CONFIG_USUARIO, A.ASIGNADO "+
                      " FROM SAM_GRUPO_CONFIG_USUARIO A RIGHT OUTER JOIN "+
                      " SAM_GRUPO_CONFIG B ON A.ID_GRUPO_CONFIG = B.ID_GRUPO_CONFIG and ID_USUARIO=? where b.ESTATUS='ACTIVO' and TIPO=? ORDER BY GRUPO_CONFIG ASC", new Object []{idUsuario,tipo});
   	  }       
	
}
