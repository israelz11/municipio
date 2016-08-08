/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 05/10/2012
 * @Descriopcion metodos para catalogo de Minutarios
 */
package mx.gob.municipio.centro.model.gateways.correspondencia;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.bases.BaseGatewayCorrespondencia;

public class GatewayMinutarios extends BaseGatewayCorrespondencia {
	public GatewayMinutarios() {
		
	}

	public List<Map> getMinutarios(Long idDependencia){
		return this.getJdbcTemplate().queryForList("SELECT A.ID_CAT_MINUTARIO,"+
															"A.ID_SUBDIRECCION, "+
															"A.DESCRIPCION AS MINUTARIO, "+
															"B.DESCRIPCION AS SUBDIRECCION, A.STATUS, A.PREDETER "+
													"FROM SGD_CAT_MINUTARIOS AS A "+
														"INNER JOIN SGD_CAT_SUBDIRECCIONES AS B ON (B.ID_SUBDIRECCION = A.ID_SUBDIRECCION) "+
														"INNER JOIN CAT_DEPENDENCIAS AS C ON (C.ID = B.ID_DEPENDENCIA) "+
													"WHERE C.ID =? ORDER BY A.DESCRIPCION ASC", new Object[]{idDependencia});
	}
	
	public String guardarMinutario(Long idCatMinutario, Long idDependencia, Long idSubdireccion, String minutario, String status){
		if(idCatMinutario==0){
			this.getJdbcTemplate().update("INSERT INTO SGD_CAT_MINUTARIOS(ID_SUBDIRECCION, DESCRIPCION, STATUS) VALUES(?,?,?)",
					new Object[]{idSubdireccion, minutario, 1});
		}
		else{
			this.getJdbcTemplate().update("UPDATE SGD_CAT_MINUTARIOS SET ID_SUBDIRECCION = ?, DESCRIPCION =?, STATUS=? WHERE ID_CAT_MINUTARIO =?",
					new Object[]{idSubdireccion, minutario, (status.equals("true") ? 1:0), idCatMinutario});
		}
		return "";
	}
	
	public void eliminarMinutario(Long idMinutario){
		this.getJdbcTemplate().update("DELETE FROM SGD_CAT_MINUTARIOS WHERE ID_CAT_MINUTARIO =?", new Object[]{idMinutario});
	}
	
	public List <Map> getMinutariosDestino(Long idDependencia){
		return this.getJdbcTemplate().queryForList("SELECT PP.*, P.NOMBRE, P.APE_MAT, P.APE_PAT, SUB.DESCRIPCION AS SUBDIRECCION, SUB.CVE_PERS AS CVE_RESP FROM SGD_PERSONA AS PP "+ 
														"INNER JOIN PERSONAS AS P ON (P.CVE_PERS = PP.CVE_PERS) "+
														"LEFT JOIN SGD_CAT_SUBDIRECCIONES AS SUB ON (SUB.ID_SUBDIRECCION = PP.ID_SUBDIRECCION) "+
													"WHERE PP.ID_DEPENDENCIA = ? ORDER BY P.NOMBRE, P.APE_MAT, P.APE_PAT ", new Object[]{idDependencia});
	}
	
	public List<Map> getPersonasCorrespondencia(){
		  return this.getJdbcTemplate().queryForList("SELECT PP.*, P.NOMBRE, P.APE_MAT, P.APE_PAT, SUB.DESCRIPCION AS SUBDIRECCION, CT.DEPENDENCIA, SUB.CVE_PERS AS CVE_RESP FROM SGD_PERSONA AS PP "+
														"INNER JOIN PERSONAS AS P ON (P.CVE_PERS = PP.CVE_PERS) "+
														"INNER JOIN CAT_DEPENDENCIAS AS CT ON (CT.ID = PP.ID_DEPENDENCIA) "+
														"LEFT JOIN SGD_CAT_SUBDIRECCIONES AS SUB ON (SUB.ID_SUBDIRECCION = PP.ID_SUBDIRECCION)");		
	}
	
	public List<Map> getDocumentos(Integer idSubdireccion){
		return this.getJdbcTemplate().queryForList("SELECT SG.ID_CORRESPONDENCIA, SG.NUMERO FROM SGD_CORRESPONDENCIA AS SG INNER JOIN SGD_PERSONA AS P ON (P.CVE_PERS = SG.CVE_PERS_DESTINO) WHERE SG.ID_CAT_STATUS = ? AND P.ID_SUBDIRECCION=?", new Object[]{6, idSubdireccion});
	}
	
	public List<Map> getPersonasLista(String[] cve_pers){
		return this.getJdbcTemplate().queryForList("SELECT P.CVE_PERS, P.NOMBRE, P.APE_PAT, P.APE_MAT, D.DEPENDENCIA, CT.DESCRIPCION AS SUBDIRECCION FROM PERSONAS AS P INNER JOIN SGD_PERSONA AS PP ON (PP.CVE_PERS = P.CVE_PERS) INNER JOIN CAT_DEPENDENCIAS AS D ON (D.ID = PP.ID_DEPENDENCIA) INNER JOIN SGD_CAT_SUBDIRECCIONES AS CT ON (CT.ID_SUBDIRECCION=PP.ID_SUBDIRECCION) WHERE P.CVE_PERS IN("+arrayToString(cve_pers,",")+")");
	}
	
	public static String arrayToString(String[] a, String separator) {
	    String result = "";
	    if (a.length > 0) {
	        result = a[0];    // start with the first element
	        for (int i=1; i<a.length; i++) {
	            result+= separator + "'"+a[i]+"'";
	        }
	    }
	    if(result=="") result="0";
	    return result;
	}
	
	public Long getNumeroMinutario(Long id_cat_minutario, int año){
		return this.getJdbcTemplate().queryForLong("SELECT MAX(NUMERO) AS N FROM SGD_NUMEROS_MINUTARIOS WHERE AÑO = ? AND ID_CAT_MINUTARIO=?", new Object[]{año,id_cat_minutario })+1;
	}
	
	public Map guardarNumeroMinutario(Long idMinutario, int año, Long idDependenciaFuente, Long idMinutarioFuente, Long idDependenciaDestino, Long cve_persDestino, Long idClasifica, Long idCorrespondencia, String asunto, String cve_persCCP, int cve_pers){
		Map m = new HashMap();
		if(idMinutario==0){
			/*Obtener el numero siguiente en el minutario*/
			Long numero = getNumeroMinutario(idMinutarioFuente, año);
			this.getJdbcTemplate().update("INSERT INTO SGD_NUMEROS_MINUTARIOS(ID_CORRESPONDENCIA, ID_CAT_MINUTARIO, ID_CAT_CLASIFICACION_MINUTARIO, ID_DEPENDENCIA_DEST, CVE_PERS, CVE_PERS_DEST, AÑO, NUMERO, ASUNTO, FECHA, REACTIVADO, STATUS) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
					idCorrespondencia,
					idMinutarioFuente,
					idClasifica,
					idDependenciaDestino,
					cve_pers,
					cve_persDestino,
					año,
					numero,
					asunto, 
					new Date(),
					false,
					0
			});
			
			idMinutario = this.getJdbcTemplate().queryForLong("SELECT MAX(ID_MINUTARIO) AS N FROM SGD_NUMEROS_MINUTARIOS");

			/*Guardar detalle_ccp*/
			if(cve_persCCP!=null)
				if(!cve_persCCP.equals(""))
					this.getJdbcTemplate().update("INSERT INTO SGD_DETALLE_CCP SELECT ?, CVE_PERS, ?, ? FROM PERSONAS WHERE CVE_PERS IN("+cve_persCCP+")", new Object[]{idMinutario, new Date(), null});
			
			m.put("NUMERO", this.rellenarCeros(numero.toString(),6));
		}
		else{
			this.getJdbcTemplate().update("UPDATE SGD_NUMEROS_MINUTARIOS SET ID_CORRESPONDENCIA=?, ID_CAT_MINUTARIO=?, ID_CAT_CLASIFICACION_MINUTARIO=?, ID_DEPENDENCIA_DEST=?, CVE_PERS=?, CVE_PERS_DEST=?, ASUNTO=? WHERE ID_MINUTARIO=?", new Object[]{
					idCorrespondencia,
					idMinutarioFuente,
					idClasifica,
					idDependenciaDestino,
					cve_pers,
					cve_persDestino,
					asunto,
					idMinutario
			});
			
			m.put("NUMERO", this.rellenarCeros(((String)this.getJdbcTemplate().queryForObject("SELECT NUMERO FROM SGD_NUMEROS_MINUTARIOS WHERE ID_MINUTARIO=?", new Object[]{idMinutario}, String.class)),6));
		}
		
		/*Recuperar el CCP*/
		List <Map> lstccp = this.getJdbcTemplate().queryForList("SELECT * FROM SGD_DETALLE_CCP WHERE ID_MINUTARIO = ? ", new Object[]{idMinutario});
		String temp = "";
		for(Map p: lstccp){
			temp+= p.get("CVE_PERS").toString()+",";
		}
		
		/*Agregar Maps*/
		m.put("ID_MINUTARIO", idMinutario);
		
		m.put("CCP", (!temp.equals("")) ? temp.substring(0, (temp.length()-1)): "");
		
		return m;
	}
	
	public Map getMinutarioGeneral(Long idMinutario){
		return this.getJdbcTemplate().queryForMap("SELECT A.*, CONVERT(VARCHAR(10), A.FECHA, 103) AS FECHA_MINUTARIO, RIGHT('000000'+CONVERT(VARCHAR, A.NUMERO), 6) AS FOLIO, "+
															"(CASE A.STATUS WHEN 0 THEN 'EDICIÓN' WHEN 1 THEN 'CERRADDO' WHEN 2 THEN 'CANCELADO' END) AS STATUS_DESC, "+
															"H.DESCRIPCION AS MINUTARIO_FUENTE, "+
															"F.ID AS ID_DEPENDENCIA_FUENTE, "+
															"(F.DEPENDENCIA) AS DEPENDENCIA_FUENTE, "+
															"(B.NOMBRE + ' '+B.APE_PAT + ' '+B.APE_MAT) AS PERSONA_FUENTE, "+
															"D.ID AS ID_DEPENDENCIA_DESTINO, "+
															"(C.NOMBRE + ' '+C.APE_PAT + ' '+C.APE_MAT) AS PERSONA_DESTINO, "+
															"(D.DEPENDENCIA) AS DEPENDENCIA_DESTINO "+
														"FROM SGD_NUMEROS_MINUTARIOS AS A "+
															"INNER JOIN PERSONAS AS B ON (B.CVE_PERS = A.CVE_PERS) "+
															"LEFT JOIN PERSONAS AS C ON (C.CVE_PERS = A.CVE_PERS_DEST) "+
															"INNER JOIN CAT_DEPENDENCIAS AS D ON (D.ID = A.ID_DEPENDENCIA_DEST) "+
															"INNER JOIN SGD_PERSONA AS E ON (E.CVE_PERS = A.CVE_PERS) "+
															"INNER JOIN CAT_DEPENDENCIAS AS F ON (F.ID = E.ID_DEPENDENCIA) "+
															"INNER JOIN SGD_CAT_MINUTARIOS AS H ON (H.ID_CAT_MINUTARIO = A.ID_CAT_MINUTARIO) "+
															"WHERE A.ID_MINUTARIO=? "+
														"ORDER BY A.ID_MINUTARIO ASC", new Object[]{idMinutario});
	}
	
	public String agregarCCP(int cve_pers, Long idMinutario){
		this.getJdbcTemplate().update("INSERT INTO SGD_DETALLE_CCP(ID_MINUTARIO, CVE_PERS, FECHA) VALUES(?,?,?)", new Object[]{idMinutario, cve_pers, new Date()});
		return this.getClavesPersonasCCP(idMinutario);
	}
	
	public void eliminarCCP(int cve_pers, Long idMinutario){
		this.getJdbcTemplate().update("DELETE FROM SGD_DETALLE_CCP WHERE ID_MINUTARIO=? AND CVE_PERS=?", new Object[]{idMinutario, cve_pers});
	}
	
	public Long guardarArchivo(Long idCorrespondencia, Long idMinutario, String nombreArchivo, String path, Date fecha, String ext, Long size){
		this.getJdbcTemplate().update("INSERT INTO SGD_ARCHIVOS(ID_CORRESPONDENCIA, ID_MINUTARIO, NOMBRE, RUTA, FECHA, EXT, TAMAÑO) VALUES(?,?,?,?,?,?,?)", new Object[]{
				idCorrespondencia, 
				idMinutario,
				nombreArchivo,
				path, 
				fecha,
				ext,
				size
		});
		
		return this.getJdbcTemplate().queryForLong("SELECT MAX(ID_ARCHIVO) AS N FROM SGD_ARCHIVOS");
	}
	
	public Map getCargarMinutario(Long idMinutario)
	{
		return this.getJdbcTemplate().queryForMap("SELECT *FROM SGD_NUMEROS_MINUTARIOS WHERE ID_MINUTARIO = ?", new Object[]{idMinutario});
	}
	
	public String getMinutariosDestinoCombo(Long idDependencia, int cve_persDest){
    	String cadena = "";
    	String c = ""; Integer x=0;
    	List <Map> lst =  getMinutariosDestino(idDependencia);
    	for(Map m: lst){
    		if(!c.equals(m.get("SUBDIRECCION").toString())){
    			if(x!=0) cadena+= "</optgroup>";
    			cadena+= "<optgroup label='"+m.get("SUBDIRECCION").toString()+"'>";
    			c = m.get("SUBDIRECCION").toString();
    		}
    		
    		cadena+= "<option value='"+m.get("CVE_PERS").toString()+"'"+((cve_persDest==Integer.parseInt(m.get("CVE_PERS").toString()))? "selected":"")+">"+((m.get("CVE_PERS").toString().equals(m.get("CVE_RESP").toString())) ? ">":"  ")+m.get("NOMBRE").toString()+" "+m.get("APE_PAT").toString()+" "+m.get("APE_MAT").toString()+"</option>";
    		
    		x++;
    	}
    	cadena+= "</optgroup>";
    	return cadena;
    }
	
	public String getMinutariosCombo(Long idDependencia, Long idCatMinutario){
    	String cadena = "<option value='-1' selected>[Seleccione]</option>";
    	String c = ""; Integer x=0;
    	List <Map> lst =  getMinutarios(idDependencia);
    	for(Map m: lst){
    		if(idCatMinutario==0) {
    				if(Boolean.parseBoolean(m.get("PREDETER").toString())){
    					idCatMinutario = Long.parseLong(m.get("ID_CAT_MINUTARIO").toString());
    				}
    			}
    		if(!c.equals(m.get("SUBDIRECCION").toString())){
    			if(x!=0) cadena+= "</optgroup>";
    			cadena+= "<optgroup label='"+m.get("SUBDIRECCION").toString()+"'>";
    			c = m.get("SUBDIRECCION").toString();
    		}
    		
    			cadena+= "<option value='"+m.get("ID_CAT_MINUTARIO").toString()+"' "+((idCatMinutario==Long.parseLong(m.get("ID_CAT_MINUTARIO").toString()))? "selected":"")+">"+m.get("MINUTARIO").toString()+"</option>";
    		
    		x++;
    	}
    	cadena+= "</optgroup>";
    	return cadena;
    }
	
	public List<Map> getAutocompleteDocumentos(int cve_pers){
		Integer idSubdireccion = this.getJdbcTemplate().queryForInt("SELECT ISNULL(ID_SUBDIRECCION,0) FROM SGD_PERSONA WHERE CVE_PERS =?", new Object[]{cve_pers}); 
		return getDocumentos(idSubdireccion);
	}
	
	public String getClavesPersonasCCP(Long idMinutario){
		List <Map> lstccp = this.getJdbcTemplate().queryForList("SELECT * FROM SGD_DETALLE_CCP WHERE ID_MINUTARIO = ? ", new Object[]{idMinutario});
		String temp = "";
		for(Map p: lstccp){
			temp+= p.get("CVE_PERS").toString()+",";
		}
		return (!temp.equals("")) ? temp.substring(0, (temp.length()-1)): "";
	}
	
	public List<Map> getArchivosMinutario(Long idMinutario){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SGD_ARCHIVOS WHERE ID_MINUTARIO=?", new Object[]{idMinutario});
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
	
	public void eliminarArchivoMinutario(Long idArchivo,  HttpServletRequest request){
		Map archivo = this.getJdbcTemplate().queryForMap("SELECT *FROM SGD_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		/*eliminado fisico*/
		File fichero = new File(request.getSession().getServletContext().getRealPath("")+"/correspondencia/archivos/minutarios/["+archivo.get("ID_ARCHIVO")+"] "+archivo.get("NOMBRE").toString());
	   
		System.out.println(fichero.getName());
	    System.out.println(fichero.getPath());
	    
		if (fichero.delete()){
			 System.out.println("El fichero ha sido borrado satisfactoriamente");
			 this.getJdbcTemplate().update("DELETE FROM SGD_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		}
		else
			System.out.println("El fichero no puede ser borrado");
		/*eliminado en base de datos*/
		
		
	}
	
	public Long getMinutarioDefault(Long idDependencia){
		return this.getJdbcTemplate().queryForLong("SELECT TOP 1 A.ID_CAT_MINUTARIO FROM SGD_CAT_MINUTARIOS AS A INNER JOIN SGD_CAT_SUBDIRECCIONES AS B ON (B.ID_SUBDIRECCION=A.ID_SUBDIRECCION) WHERE A.STATUS=1 AND B.ID_DEPENDENCIA=? AND A.PREDETER=?", new Object[]{idDependencia, true});
	}
	
	public List<Map> getListadoNumeros(Map param){
		String sql = "SELECT A.*,  CONVERT(VARCHAR(10), A.FECHA, 103) AS FECHA_MINUTARIO, RIGHT('000000'+CONVERT(VARCHAR, A.NUMERO), 6) AS FOLIO,"+
						"	(CASE A.STATUS WHEN 0 THEN 'EDICIÓN' WHEN 1 THEN 'CERRADDO' WHEN 2 THEN 'CANCELADO' END) AS STATUS_DESC, "+
						"	H.DESCRIPCION AS MINUTARIO_FUENTE, "+
						"	(F.DEPENDENCIA) AS DEPENDENCIA_FUENTE, "+
						"	(B.NOMBRE + ' '+B.APE_PAT + ' '+B.APE_MAT) AS PERSONA_FUENTE, "+
						"	(C.NOMBRE + ' '+C.APE_PAT + ' '+C.APE_MAT) AS PERSONA_DESTINO, "+
						"	(D.DEPENDENCIA) AS DEPENDENCIA_DESTINO "+
						"FROM SGD_NUMEROS_MINUTARIOS AS A "+
						"	INNER JOIN PERSONAS AS B ON (B.CVE_PERS = A.CVE_PERS) "+
						"	LEFT JOIN PERSONAS AS C ON (C.CVE_PERS = A.CVE_PERS_DEST) "+
						"	INNER JOIN CAT_DEPENDENCIAS AS D ON (D.ID = A.ID_DEPENDENCIA_DEST) "+
						"	INNER JOIN SGD_PERSONA AS E ON (E.CVE_PERS = A.CVE_PERS) "+
						"	INNER JOIN CAT_DEPENDENCIAS AS F ON (F.ID = E.ID_DEPENDENCIA) "+
						"	INNER JOIN SGD_CAT_MINUTARIOS AS H ON (H.ID_CAT_MINUTARIO = A.ID_CAT_MINUTARIO) "+
						"WHERE 0=0 " +
							(Long.parseLong(param.get("idUnidad").toString()) !=0 ? " AND E.ID_DEPENDENCIA=:idUnidad": "")+ 
							(Integer.parseInt(param.get("year").toString()) !=0 ? " AND A.AÑO=:year":"")+
							((Long.parseLong(param.get("idMinutario").toString()) !=0 && Integer.parseInt(param.get("idMinutario").toString())!=-1) ? " AND A.ID_CAT_MINUTARIO=:idMinutario":"")+
							(Integer.parseInt(param.get("idDependenciaDestino").toString()) !=0 ? " AND A.ID_DEPENDENCIA_DEST=:idDependenciaDestino":"")+
							(Integer.parseInt(param.get("cve_persDestino").toString()) !=0 ? " AND A.CVE_PERS_DEST=:cve_persDestino":"")+
							(param.get("fechaInicial")!=null&&param.get("fechaFinal")!=null ? 
										(!param.get("fechaInicial").toString().equals("")&&!param.get("fechaFinal").toString().equals("") ? " AND CONVERT(datetime, CONVERT(varchar(10), A.FECHA, 103), 103) BETWEEN :fechaInicial AND :fechaFinal":"")
									:
										"")+
							(param.get("numero")!=null ? 
										(!param.get("numero").toString().equals("numero") ? " AND RIGHT('000000'+CONVERT(VARCHAR, A.NUMERO), 6) LIKE '%"+param.get("numero").toString()+"%'" : "")
									:
										"")+
						" ORDER BY A.ID_MINUTARIO ASC";
		return this.getNamedJdbcTemplate().queryForList(sql, param);
	}
	
	public void cerrarMinutario(Long idMinutario){
		this.getJdbcTemplate().update("UPDATE SGD_NUMEROS_MINUTARIOS SET STATUS=? WHERE ID_MINUTARIO=?", new Object[]{1, idMinutario});
	}
	
	public List<Map> getPeriodos(){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SGD_PERIODOS WHERE STATUS=?", new Object[]{true});
	}
	
	public void cancelarMinutario(Long idMinutario){
		this.getJdbcTemplate().update("UPDATE SGD_NUMEROS_MINUTARIOS SET STATUS=? WHERE ID_MINUTARIO=?", new Object[]{2, idMinutario});
	}
	
	public void reactivarMinutario(Long idMinutario){
		this.getJdbcTemplate().update("UPDATE SGD_NUMEROS_MINUTARIOS SET STATUS=? WHERE ID_MINUTARIO=?", new Object[]{0, idMinutario});
	}
	
	public void aperturarMinutario(Long[] idMinutario){
		for(Long id: idMinutario){
			this.getJdbcTemplate().update("UPDATE SGD_NUMEROS_MINUTARIOS SET STATUS=? WHERE ID_MINUTARIO=?", new Object[]{0, id});
		}
		
	}
	
}
