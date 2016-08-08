/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.seguridad;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;

public class UserDetailsService implements org.springframework.security.userdetails.UserDetailsService {

   private static Logger log = Logger.getLogger(UserDetailsService.class.getName());
   
   @Autowired 
   private Sesion sesion;   
   
   @Autowired
   GatewayUsuarios gatewayUsuarios;
   
   @Autowired
   GatewayMeses gatewayMeses;
   
   
    public UserDetailsService() {
    }

    public UserDetails loadUserByUsername(String nombreDeUsuario) {
        log.debug("Intentando cargar usuario:"+nombreDeUsuario);
        try {
            UserDetails usuario = cargarPorLogin(nombreDeUsuario);
            
                if(usuario!=null){
                    log.debug("Usuario cargado: "+usuario.getUsername());
                }            
            return usuario;            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
        
    private UserDetails cargarPorLogin(String loginParaCargar) throws ClassNotFoundException, SQLException {                                                                                
        UserDetails userDetails = null;        
        String password;        
        boolean  esUsuarioVigente=false;
        Integer idUsuario;
        Map  resultado= gatewayUsuarios.getUsuarioLogin(loginParaCargar);
        if (resultado != null    ) {        	
            password= (String)resultado.get("PASSWD");
            String  vigente = (String)resultado.get("ACTIVO");
            Boolean exclusivo = (resultado.get("EXCLUSIVO")!=null)? (Boolean) resultado.get("EXCLUSIVO"): false;
            if (vigente.equals("S"))
                esUsuarioVigente=true;
            if(exclusivo)
            	esUsuarioVigente=true;
            idUsuario= (Integer)resultado.get("CVE_PERS");            
            List<GrantedAuthority> grantedAuthorities = buscarPrivilegios(idUsuario.intValue());                
            cargarSesion(resultado,idUsuario);
            GrantedAuthority[] granted = new GrantedAuthority[grantedAuthorities.size()];
            grantedAuthorities.toArray(granted);
            userDetails =  new User(loginParaCargar,password, true, esUsuarioVigente, esUsuarioVigente, true,granted );
        }
            return userDetails;        
    }

private void cargarSesion(Map datosUsuario, Integer idUsuario  ){
	String nombre =(String)datosUsuario.get("NOMBRE_COMPLETO");
	String unidad =(String)datosUsuario.get("DEPENDENCIA");
	String idUnidad =(String)datosUsuario.get("ID_DEPENDENCIA").toString();
	String  claveUnidad =(String)datosUsuario.get("ID_DEPENDENCIA").toString();
    sesion.setIdUsuario(idUsuario);    
    sesion.setUsuario(nombre);
	sesion.setEjercicio(gatewayMeses.getEjercicioActivo());
	sesion.setIdUnidad(idUnidad);
	sesion.setUnidad(unidad);
	sesion.setClaveUnidad(claveUnidad);
	sesion.setIdGrupo(gatewayUsuarios.getGrupo(idUsuario));
}


    private List<GrantedAuthority> buscarPrivilegios(Integer sys_usuario) {
        SqlRowSet rs;
        String anteriGrupo = "";
        String prefijoRol="ROLE_"; 
        String anteriSistema = "";
        String anteriOpcion = "";
        String rolSegundoNivel;
        String rolTercerNivel;
        List<GrantedAuthority> grantedAuthorities;
        grantedAuthorities = new ArrayList<GrantedAuthority>();            
        grantedAuthorities.add(new GrantedAuthorityImpl("ROLE_ENTRAR"));
        log.info("ROLE_ENTRAR");
        int op = 0;
        log.info("Se van a buscar los privilegios...");
        rs = gatewayUsuarios.getPrivilegiosUsuario(sys_usuario);
        if (rs.next()) {
            boolean salir = true;
                do {                        
                    anteriSistema = rs.getString("SISTEMA");                        
                    GrantedAuthorityImpl grantedAuthorityImplSistema =  new GrantedAuthorityImpl (prefijoRol+anteriSistema.replaceAll(" ","_"));
                    grantedAuthorities.add(grantedAuthorityImplSistema);
                    log.info(prefijoRol+anteriSistema.replaceAll(" ","_"));
                    sistemaBr:
                    do {
                        anteriOpcion = rs.getString("MODULO");
                        rolSegundoNivel = (rs.getString("SISTEMA")+"_"+anteriOpcion).replaceAll(" ","_"); 
                        grantedAuthorities.add(new GrantedAuthorityImpl(prefijoRol+rolSegundoNivel));
                        log.info(prefijoRol+rolSegundoNivel);
                        grupoBr:
                        do {
                            op = 1;
                            if (!anteriSistema.equals(rs.getString("SISTEMA")))
                                break sistemaBr;
                            if (!anteriOpcion.equals(rs.getString("MODULO")))
                                break grupoBr;
                            op = 0;
                            rolTercerNivel=(rs.getString("SISTEMA")+"_"+rs.getString("MODULO")+"_"+rs.getString("PRIVILEGIO")).replaceAll(" ","_");
                            grantedAuthorities.add(new GrantedAuthorityImpl (prefijoRol+rolTercerNivel));
                            log.info(prefijoRol+rolTercerNivel);
                        } while (rs.next());
                        if (op == 0)
                            salir = false;
                    } while (salir);
                } while (salir);
        }
        log.info("Privilegios configurados: "+grantedAuthorities.size());
        return grantedAuthorities;
    }

}
