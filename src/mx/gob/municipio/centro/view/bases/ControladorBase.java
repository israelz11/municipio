/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.bases;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import mx.gob.municipio.centro.view.componentes.RMCantidadEnLetras;
import mx.gob.municipio.centro.view.seguridad.Sesion;
import mx.gob.municipio.centro.view.seguridad.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

public class ControladorBase {

	private final String characterEncoding = "UTF-8";
	private final String cipherTransformation = "AES/CBC/PKCS5Padding";
	private final String aesEncryptionAlgorithm = "AES";
	
	 /*private static final String ALGORITHM = "AES";
	    private static final int ITERATIONS = 1;
	    */
	//private static String keyValue = "oiuytrefghjkl936";
	    /*private static final String salt = "qwertyuiop102357";*/
	    
    private static Logger log = Logger.getLogger(ControladorBase.class.getName());
    @Autowired 
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;    
    @Autowired 
    @Qualifier("namedJdbcTemplate")
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    @Autowired 
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;
    
    @Autowired
	private RMCantidadEnLetras rmCantidadEnLetras;
    
   /* @Autowired
	private BaseGateway baseGateway;*/
    
    private Sesion sesion;
    
    /* Métodos para obtener el context, request, HttpSession de un método del controlador */

/***********************************************************************************************************/   

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
        return namedJdbcTemplate;
    }

	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
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
	
	public Map<String, String> toMap(String[] array) {
	    Map<String, String> map = new HashMap<String, String>();
	    //String[] array = input.split(",");
	    for (String str : array) {
	        String[] pair = str.split("=");
	        map.put(pair[0], pair[1]);
	    }
	    return map;
	}
	

	
	public  String rellenarCeros(String cad, int lng){
  	  String pattern = "000000000000000000000000000000000";

  	  if ( cad.equals("") ) return cad;

  	  return pattern.substring(0, lng - cad.length()) + cad;
  	}
	
	public boolean getPrivilegioEn(int cve_pers, int idprivilegio){
		return this.getJdbcTemplate().queryForInt("SELECT  COUNT(*) AS N "+
													"FROM SAM_USUARIO_ROL "+
														"INNER JOIN SAM_ROL ON (SAM_ROL.ID_ROL = SAM_USUARIO_ROL.ID_ROL) "+
														"INNER JOIN SAM_ROL_PRIVILEGIO ON (SAM_ROL_PRIVILEGIO.ID_ROL = SAM_ROL.ID_ROL) "+
														"INNER JOIN SAM_PRIVILEGIO ON (SAM_PRIVILEGIO.ID_PRIVILEGIO = SAM_ROL_PRIVILEGIO.ID_PRIVILEGIO) "+
													"WHERE SAM_USUARIO_ROL.CVE_PERS = ? AND SAM_ROL_PRIVILEGIO.ID_PRIVILEGIO = ?", new Object[]{cve_pers, idprivilegio})!=0;
	}
	
	public double redondea(Double numero, int decimales) 
	{ 
	  Double resultado;
	  BigDecimal res;

	  res = new BigDecimal(numero).setScale(decimales, BigDecimal.ROUND_HALF_UP);
	  resultado = res.doubleValue();
	  return resultado; 
	}  
	
	public String convertirALetras(BigDecimal numero) {
		return rmCantidadEnLetras.convertir(numero.doubleValue())[0];
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
	
	
	public static String encrypt(String cleartext, String key) throws Exception {
        byte[] rawKey = getRawKey(key.getBytes("UTF-8"));
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toBase64(result);
	}
	
	public static String decrypt(String encrypted, String key) throws Exception {
	        byte[] rawKey = getRawKey(key.getBytes("UTF-8"));
	        byte[] enc = fromBase64(encrypted);
	        byte[] result = decrypt(rawKey, enc);
	        return new String(result);
	}
	
	private static byte[] getRawKey(byte[] seed) throws Exception {
	    SecretKey skey = new SecretKeySpec(seed, "AES");
	
	    byte[] raw = skey.getEncoded();
	
	    return raw;
	}
	
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
	    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    IvParameterSpec ivParameterSpec = new IvParameterSpec(raw);
	
	    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
	    byte[] encrypted = cipher.doFinal(clear);
	    return encrypted;
	}
	
	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
	    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    IvParameterSpec ivParameterSpec = new IvParameterSpec(raw);
	
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
	    byte[] decrypted = cipher.doFinal(encrypted);
	    return decrypted;
	}
	
	public static String toBase64(byte[] buf)
	{
	    return Base64.encodeBytes(buf);
	}
	
	public static byte[] fromBase64(String str) throws Exception
	{
	    return Base64.decode(str);
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
    
	public String getExtension(String texto){
		texto.substring((texto.length()-3), texto.length());
		int tam = texto.length();
		int i=0;
		String car = ""; String temp = "";
		for(i=tam;i==0;i--){
			car = texto.substring(i, i);
			if(car==".") break;
			temp+=car;
		}
		return temp;
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
}

