/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.seguridad;

public class PasswordEncoder implements org.springframework.security.providers.encoding.PasswordEncoder  {

    public PasswordEncoder() {
    }
       
    public String encodePassword(String passCrudo, Object salt) {
       //return  gatewaySeguridad.buscarCuenta(passCrudo);
    	return  passCrudo;
    }

    public boolean isPasswordValid(String passEncr, String passCrudo, Object salt) {
        return passEncr.equals(encodePassword(passCrudo,salt));
    }


    
    
}
