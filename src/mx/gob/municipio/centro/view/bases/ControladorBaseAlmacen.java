/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.bases;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import mx.gob.municipio.centro.view.seguridad.Sesion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

public class ControladorBaseAlmacen {

    private static Logger log = Logger.getLogger(ControladorBaseAlmacen.class.getName());
    @Autowired 
    @Qualifier("jdbcTemplateAlmacen")
    private JdbcTemplate jdbcTemplateAlmacen;    
    
    @Autowired 
    @Qualifier("namedJdbcTemplateAlmacen")
    private NamedParameterJdbcTemplate namedJdbcTemplateAlmacen;
    
    @Autowired 
    @Qualifier("transactionTemplateAlmacen")
    private TransactionTemplate transactionTemplateAlmacen;
    
    @Autowired 
    @Qualifier("sesion")
    private Sesion sesion;
    
    /* M�todos para obtener el context, request, HttpSession de un m�todo del controlador */

/***********************************************************************************************************/   

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplateAlmacen;
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplateAlmacen;
    }

    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
        return namedJdbcTemplateAlmacen;
    }

	public Sesion getSesion() {
		return sesion;
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

	public String getfechaActualCadena() {
		SimpleDateFormat  fechaResultado =new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fecha = new java.util.Date();
	    return  fechaResultado.format(fecha);
	   }
	
	public Date getfechaActual() {
		String fecha = getfechaActualCadena();		
	    return  formatoFecha(fecha);
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
	
	public boolean getPrivilegioEn(int cve_pers, int idprivilegio){
		return this.getJdbcTemplate().queryForInt("SELECT  COUNT(*) AS N "+
													"FROM SAM_USUARIO_ROL "+
														"INNER JOIN SAM_ROL ON (SAM_ROL.ID_ROL = SAM_USUARIO_ROL.ID_ROL) "+
														"INNER JOIN SAM_ROL_PRIVILEGIO ON (SAM_ROL_PRIVILEGIO.ID_ROL = SAM_ROL.ID_ROL) "+
														"INNER JOIN SAM_PRIVILEGIO ON (SAM_PRIVILEGIO.ID_PRIVILEGIO = SAM_ROL_PRIVILEGIO.ID_PRIVILEGIO) "+
													"WHERE SAM_USUARIO_ROL.CVE_PERS = ? AND SAM_ROL_PRIVILEGIO.ID_PRIVILEGIO = ?", new Object[]{cve_pers, idprivilegio})!=0;
	}
	
	public Integer obtenerAnio(Date date){
		if (null == date){
			return 0;
		}
		else{

		String formato = "yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		return Integer.parseInt(dateFormat.format(date));

		}
	}
    
	
}

