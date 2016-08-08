package mx.gob.municipio.centro.view.bases;

import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import mx.gob.municipio.centro.view.seguridad.Sesion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

public class ControladorBaseCorrespondencia {

	 private static Logger log = Logger.getLogger(ControladorBaseCorrespondencia.class.getName());
	    @Autowired 
	    @Qualifier("jdbcTemplateCorrespondencia")
	    private JdbcTemplate jdbcTemplateCorrespondencia;    
	    
	    @Autowired 
	    @Qualifier("namedJdbcTemplateCorrespondencia")
	    private NamedParameterJdbcTemplate namedJdbcTemplateCorrespondencia;
	    
	    @Autowired 
	    @Qualifier("transactionTemplateAlmacen")
	    private TransactionTemplate transactionTemplateCorrespondencia;
	    
	    @Autowired 
	    @Qualifier("sesion")
	    private Sesion sesion;
	    
	    /* M�todos para obtener el context, request, HttpSession de un m�todo del controlador */

	/***********************************************************************************************************/   

	    public JdbcTemplate getJdbcTemplate() {
	        return jdbcTemplateCorrespondencia;
	    }

	    public TransactionTemplate getTransactionTemplate() {
	        return transactionTemplateCorrespondencia;
	    }

	    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
	        return namedJdbcTemplateCorrespondencia;
	    }

		public Sesion getSesion() {
			return sesion;
		}	
		
		public  String rellenarCeros(String cad, int lng){
		  	  String pattern = "000000000000000000000000000000000";

		  	  if ( cad.equals("") ) return cad;

		  	  return pattern.substring(0, lng - cad.length()) + cad;
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
		
		public void  almacenarArchivoFisico(MultipartFile file,String path , String fileName ) throws IOException   {
			 File dir_carpeta = new File(path);
		      if(!dir_carpeta.exists())
		           dir_carpeta.mkdir();
			InputStream inputStream = file.getInputStream();  
			OutputStream outputStream = new FileOutputStream(path+fileName);
			int readBytes = 0;  
			byte[] buffer = new byte[10000];  
			while ((readBytes = inputStream.read(buffer, 0 , 10000))!=-1){  
			    outputStream.write(buffer, 0, readBytes);  
			   }  
			outputStream.close();  
			inputStream.close();		
			
		}
		
		public static String removeSpecialChar(String input) {
		    // Cadena de caracteres original a sustituir.
		    String original = "áàäéèëíìïóòöúùuñÃ³ÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		    // Cadena de caracteres ASCII que reemplazarán los originales.
		    String ascii = "aaaeeeiiiooouuunA3AAAEEEIIIOOOUUUNcC";
		    String output = input;
		    for (int i=0; i<original.length(); i++) {
		        // Reemplazamos los caracteres especiales.
		        output = output.replace(original.charAt(i), ascii.charAt(i));
		    }//for i
		    return output;
		}
	    

}
