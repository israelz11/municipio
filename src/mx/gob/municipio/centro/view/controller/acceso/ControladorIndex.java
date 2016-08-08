/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.acceso;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.dao.DataAccessException;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/index.action")
public class ControladorIndex extends ControladorBase {
	private static Logger log = 
        Logger.getLogger(ControladorIndex.class.getName());
	
	 public ControladorIndex() {
	    }
	 
	    @SuppressWarnings("unchecked")
	    @RequestMapping(method = RequestMethod.GET)    
	    public String  handleRequest( Map modelo,  HttpServletRequest request  ) {
	    	Integer finSesion = request.getParameter("message")!= null ? Integer.parseInt(request.getParameter("message")) : 0;
	    	modelo.put("message",finSesion);
	        return "index.jsp";
	    }	    
	    
	    public String validarUsuario(String user, String pwd){
	    	try{
	    		Md5PasswordEncoder a = new Md5PasswordEncoder();
	    		String pasEncriptado = a.encodePassword(pwd, null );
	    		
	    		boolean encontrado = (this.getJdbcTemplate().queryForInt("SELECT count(*) as n FROM USUARIOS_EX WHERE LOGIN = ? AND PASSWD = ?", new Object[]{user, pasEncriptado})==1);
	    		
	    		if(encontrado){
	    			return "";
	    		}
	    		else
	    			return "El usuario ó contraseña escrito no es valido, vuelva a intentarlo";
	    	}
	    	catch (DataAccessException e) {	            	
                throw new RuntimeException(e.getMessage(),e);
           }
	    }
	    
	/*    public void cargarDatosSesion(String usuario,int idUsuario,String  unidad,String  Host,String  ip,int ejercicio,Integer idUnidad ,String  claveUnidad ) {	    	
	            getSesion().setEjercicio(ejercicio);
	            getSesion().setUnidad(unidad);
	            getSesion().setUsuario(usuario);              
	            getSesion().setIp(ip);
	            getSesion().setHost(Host);
	            getSesion().setIdUsuario(idUsuario);
	            getSesion().setClaveUnidad(claveUnidad);
	            getSesion().setIdUnidad(idUnidad);
	            getSesion().setIdGrupo(getGrupo(idUsuario));
	    }
	     
	    public Integer getGrupo(int idUsuario) {
	    return this.getJdbcTemplate().queryForInt("select ID_GRUPO_CONFIG from SAM_GRUPO_CONFIG_USUARIO where ASIGNADO =1 AND ID_USUARIO=?", new Object []{idUsuario});	 
	     }*/
	    
}
