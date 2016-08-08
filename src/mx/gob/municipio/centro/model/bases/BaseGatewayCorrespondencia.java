/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 *
 */

package mx.gob.municipio.centro.model.bases;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

public class BaseGatewayCorrespondencia {

private static Logger log = Logger.getLogger(BaseGatewayCorrespondencia.class.getName());
    
    @Autowired
    @Qualifier("jdbcTemplateCorrespondencia")
    private JdbcTemplate jdbcTemplateCorrespondencia;
    
    @Autowired
    @Qualifier("namedJdbcTemplateCorrespondencia")
    private NamedParameterJdbcTemplate namedJdbcTemplatCorrespondencia;    
    
    @Autowired 
    @Qualifier("transactionTemplateAlmacen")
    private TransactionTemplate transactionTemplateCorrespondecia;
    
    public BaseGatewayCorrespondencia() {
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplateCorrespondencia;
    }
    
    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
        return namedJdbcTemplatCorrespondencia;
    }
   
    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplateCorrespondecia;
    }
    
    public  String rellenarCeros(String cad, int lng){
	  	  String pattern = "000000000000000000000000000000000";

	  	  if ( cad.equals("") ) return cad;

	  	  return pattern.substring(0, lng - cad.length()) + cad;
	  	}
    
	public static String arrayToString(String[] a, String separator) {
	    StringBuffer result = new StringBuffer();
	    if (a.length > 0) {
	        result.append(a[0]);
	        for (int i=1; i<a.length; i++) {
	            result.append(separator);
	            result.append(a[i]);
	        }
	    }
	    return result.toString();
	}
    
    public Date formatoFecha (String fecha) {
     	 Date fechaResultado =null;
     	 if (fecha!=null && !fecha.equals("")){
   		  try {
   			  fechaResultado =  new Date(new SimpleDateFormat("dd/MM/yyyy").parse(fecha).getTime());          
   			  } catch (Throwable ex) {				  					  
   				  throw new IllegalArgumentException(ex.getMessage(), ex);
   			  }           	
     	 }
          return fechaResultado;
     }

    public boolean getPrivilegioEn(int cve_pers, int idprivilegio){
		return this.getJdbcTemplate().queryForInt("SELECT  COUNT(*) AS N "+
													"FROM SAM_USUARIO_ROL "+
														"INNER JOIN SAM_ROL ON (SAM_ROL.ID_ROL = SAM_USUARIO_ROL.ID_ROL) "+
														"INNER JOIN SAM_ROL_PRIVILEGIO ON (SAM_ROL_PRIVILEGIO.ID_ROL = SAM_ROL.ID_ROL) "+
														"INNER JOIN SAM_PRIVILEGIO ON (SAM_PRIVILEGIO.ID_PRIVILEGIO = SAM_ROL_PRIVILEGIO.ID_PRIVILEGIO) "+
													"WHERE SAM_USUARIO_ROL.CVE_PERS = ? AND SAM_ROL_PRIVILEGIO.ID_PRIVILEGIO = ?", new Object[]{cve_pers, idprivilegio})!=0;
	}

}
