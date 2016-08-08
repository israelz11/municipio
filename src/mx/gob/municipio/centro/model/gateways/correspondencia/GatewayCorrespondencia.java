/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 25/NOV/2012
 * @Descriopcion metodos para el uso de la correspondencia
 */
package mx.gob.municipio.centro.model.gateways.correspondencia;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.bases.BaseGatewayCorrespondencia;

import org.springframework.beans.factory.annotation.Autowired;

public class GatewayCorrespondencia extends BaseGatewayCorrespondencia {
	@Autowired
	GatewayMinutarios gatewayMinutarios;
	
	public GatewayCorrespondencia(){}
	
	public List<Map> getListTipoCorrespondencia(){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SGD_CAT_TIPO_CORRESPONDENCIA WHERE STATUS=? ORDER BY DESCRIPCION ASC", new Object[]{true});
	}
	
	public List<Map> getListPrioridadCorrespondencia(){
		return this.getJdbcTemplate().queryForList("SELECT ID_CAT_PRIORIDAD, DESCRIPCION FROM SGD_CAT_PRIORIDAD WHERE STATUS=? ORDER BY DESCRIPCION ASC", new Object[]{true});
	}
	
	public List<Map> getListTipoOrigen(){
		return this.getJdbcTemplate().queryForList("SELECT * FROM SGD_CAT_ORIGEN WHERE STATUS=? ORDER BY DESCRIPCION ASC", new Object[]{true});
	}
	
	public List<Map> getListTipoAvisos(){
		return this.getJdbcTemplate().queryForList("SELECT * FROM SGD_CAT_AVISO WHERE STATUS=? ORDER BY DESCRIPCION ASC", new Object[]{true});
	}
	
	public String gePersonasUnidadCombo(Long idDependencia, int cve_persDest){
    	String cadena = "";
    	String c = ""; Integer x=0;
    	List <Map> lst =  gatewayMinutarios.getMinutariosDestino(idDependencia);
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
	
	public Map guardarCorrespondencia(Long idCorrespondencia, String numeroDocumento, int idDependenciaFuente, int idDependenciaDestino, int cve_persDestino, int idTipoDocumento, int idPrioridad, int idOrigen, int idTipoAviso, String asunto, String acuerdo, String fechaRecepcion, String fechaDocumento, String fechaLimite, int cve_pers, String CVE_PERS ){
		Map m = new HashMap();
		if(idCorrespondencia==0){
			this.getJdbcTemplate().update("INSERT INTO SGD_CORRESPONDENCIA(ID_DEPENDENCIA_FUENTE, ID_DEPENDENCIA_DESTINO, ID_CAT_BANDEJA, ID_CAT_TIPO, ID_CAT_AVISO, ID_CAT_PRIORIDAD, ID_CAT_STATUS, ID_CAT_ORIGEN, CVE_PERS, CVE_PERS_DESTINO, NUMERO, ASUNTO, ACUERDO, FECHA_CREACION, FECHA_RECEPCION, FECHA_DOCUMENTO, FECHA_LIMITE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
					idDependenciaFuente,
					idDependenciaDestino,
					1,
					idTipoDocumento,
					idTipoAviso,
					idPrioridad,
					0,
					idOrigen, 
					cve_pers,
					cve_persDestino,
					numeroDocumento,
					asunto,
					acuerdo,
					new Date(),
					this.formatoFecha(fechaRecepcion),
					this.formatoFecha(fechaDocumento),
					this.formatoFecha(fechaLimite)
			});
			
			idCorrespondencia = this.getJdbcTemplate().queryForLong("SELECT MAX(ID_CORRESPONDENCIA) AS N FROM SGD_CORRESPONDENCIA");
			m.put("ID_CORRESPONDENCIA", idCorrespondencia);
			/*Guardar detalle_ccp*/
			if(CVE_PERS!=null)
				if(!CVE_PERS.equals(""))
					this.getJdbcTemplate().update("INSERT INTO SGD_DESTINATARIOS(ID_CORRESPONDENCIA, CVE_PERS, ID_CAT_BANDEJA, FECHA) SELECT ?, CVE_PERS, ?, ? FROM PERSONAS WHERE CVE_PERS IN("+CVE_PERS+")", new Object[]{idCorrespondencia, 1, new Date()});
			
			
			
		}
		else
		{
			this.getJdbcTemplate().update("UPDATE SGD_CORRESPONDENCIA SET ID_DEPENDENCIA_FUENTE=?, ID_DEPENDENCIA_DESTINO=?, ID_CAT_TIPO=?, ID_CAT_AVISO=?, ID_CAT_PRIORIDAD=?, ID_CAT_ORIGEN=?, CVE_PERS_DESTINO=?, NUMERO=?, ASUNTO=?, ACUERDO=?, FECHA_RECEPCION=?, FECHA_DOCUMENTO=?, FECHA_LIMITE=? WHERE ID_CORRESPONDENCIA =?",new Object[]{
					idDependenciaFuente,
					idDependenciaDestino,
					idTipoDocumento,
					idTipoAviso,
					idPrioridad,
					idOrigen, 
					cve_persDestino,
					numeroDocumento,
					asunto,
					acuerdo,
					this.formatoFecha(fechaRecepcion),
					this.formatoFecha(fechaDocumento),
					this.formatoFecha(fechaLimite),
					idCorrespondencia
			});
		}
		
		/*Recuperar el CCP*/
		List <Map> lstccp = this.getJdbcTemplate().queryForList("SELECT * FROM SGD_DESTINATARIOS WHERE ID_CORRESPONDENCIA = ? ", new Object[]{idCorrespondencia});
		String temp = "";
		for(Map p: lstccp){
			temp+= p.get("CVE_PERS").toString()+",";
		}
		
		m.put("CCP", (!temp.equals("")) ? temp.substring(0, (temp.length()-1)): "");
		
		return m;
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
	
	public List<Map> getArchivosCorrespondencia(Long idCorrespondencia){
		return this.getJdbcTemplate().queryForList("SELECT *FROM SGD_ARCHIVOS WHERE ID_CORRESPONDENCIA=?", new Object[]{idCorrespondencia});
	}
	
	public void eliminarArchivoCorrespondencia(int cve_pers, Long idArchivo, HttpServletRequest request){
		//si tiene los privilegios elimina
		if(!getPrivilegioEn(cve_pers, 129))
			throw new RuntimeException("No se puede eliminar el archivo, su usuario no cuenta con los privilegios suficientes");
		
		Map archivo = this.getJdbcTemplate().queryForMap("SELECT *FROM SGD_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		/*eliminado fisico*/
		File fichero = new File(request.getSession().getServletContext().getRealPath("")+"/correspondencia/archivos/correspondencia/["+archivo.get("ID_ARCHIVO")+"] "+archivo.get("NOMBRE").toString());
	   
		if (fichero.delete()){
			 System.out.println("El fichero ha sido borrado satisfactoriamente");
			 this.getJdbcTemplate().update("DELETE FROM SGD_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		}
		else
			System.out.println("El fichero no puede ser borrado");
	}
	
	public void eliminarDestinatario(int cve_pers, Long idCorrespondencia){
		this.getJdbcTemplate().update("DELETE FROM SGD_DESTINATARIOS WHERE CVE_PERS=? AND ID_CORRESPONDENCIA=?", new Object[]{cve_pers, idCorrespondencia});
	}
	
	public void enviarCorrespondencia(Long idCorrespondencia){
		int cve_pers_dest = this.getJdbcTemplate().queryForInt("SELECT ISNULL(CVE_PERS_DESTINO,0) FROM SGD_CORRESPONDENCIA WHERE ID_CORRESPONDENCIA = ?", new Object[]{idCorrespondencia});
		if(cve_pers_dest ==0){
			throw new RuntimeException("No se puede enviar el documento, hace falta seleccionar un turnado");
		}
		else
		{
			int status = this.getJdbcTemplate().queryForInt("SELECT ID_CAT_STATUS FROM SGD_CORRESPONDENCIA WHERE ID_CORRESPONDENCIA=?", new Object[]{idCorrespondencia});
			if(status==0){
				//pasar a pendiente - En este estatus entra a revision de secretaria documental
				this.getJdbcTemplate().update("UPDATE SGD_CORRESPONDENCIA SET ID_CAT_STATUS=? WHERE ID_CORRESPONDENCIA=?", new Object[]{1, idCorrespondencia});
			}
			if(status==1){
				//pasar a enviado
				this.getJdbcTemplate().update("UPDATE SGD_CORRESPONDENCIA SET ID_CAT_STATUS=? WHERE ID_CORRESPONDENCIA=?", new Object[]{2, idCorrespondencia});
			}
		}
	}
	
	public List<Map> getListadoCorrespondencia(Map m){
		return this.getNamedJdbcTemplate().queryForList("SELECT ID_CORRESPONDENCIA "+
													      ",ID_DEPENDENCIA_FUENTE "+
													      ",SGD.ID_DEPENDENCIA_DESTINO "+
													      ",SGD.ID_CAT_BANDEJA "+
													      ",SGD.ID_CAT_TIPO "+
													      ",SGD.ID_CAT_AVISO "+
													      ",SGD.ID_CAT_PRIORIDAD "+
													      ",SGD.ID_CAT_STATUS "+
													      ",SGD.ID_CAT_ORIGEN "+
													      ",SGD.CVE_PERS "+
													      ",SGD.CVE_PERS_APRUEBA "+
													      ",SGD.CVE_PERS_DESTINO "+
													      ",SGD.NUMERO "+
													      ",SGD.ASUNTO "+
													      ",SGD.ACUERDO "+
													      ",CONVERT(VARCHAR, SGD.FECHA_DOCUMENTO, 103) AS FECHA_DOCUMENTO "+
													      ",CONVERT(VARCHAR, SGD.FECHA_CREACION, 103) AS FECHA_CREACION "+
													      ",CONVERT(VARCHAR, SGD.FECHA_RECEPCION, 103) AS FECHA_RECEPCION "+
													      ",CONVERT(VARCHAR, SGD.FECHA_CONCLUIDO, 103) AS FECHA_CONCLUIDO "+
													      ",CONVERT(VARCHAR, SGD.FECHA_AUTORIZACION, 103) AS FECHA_AUTORIZACION "+
													      ",CONVERT(VARCHAR, SGD.FECHA_LIMITE, 103) AS FECHA_LIMITE "+
													      ",SGD.RAZON_REMITE "+
													      ",SGD.DESCRIPCION_TURNADO "+
													      ",A.DEPENDENCIA AS DEPENDENCIA_FUENTE "+
													      ",B.DEPENDENCIA AS DEPENDENCIA_DESTINO "+
													      ",C.DESCRIPCION AS TIPO_CORRESPONDENCIA "+
													      ",D.DESCRIPCION AS TIPO_AVISO "+
													      ",E.DESCRIPCION AS NIVEL_PRIORIDAD "+
													      ",F.DESCRIPCION AS STATUS_CORRESPONDENCIA "+
													      ",G.DESCRIPCION AS ORIGEN_CORRESPONDENCIA "+
													      ",(H.NOMBRE + ' ' + H.APE_PAT + ' ' + H.APE_MAT) AS PERSONA_APRUEBA "+
													      ",(I.NOMBRE + ' ' + I.APE_PAT + ' ' + I.APE_MAT) AS PERSONA_DESTINO "+
													      ",(J.NOMBRE + ' ' + J.APE_PAT + ' ' + J.APE_MAT) AS PERSONA_FUENTE "+
													  "FROM SGD_CORRESPONDENCIA AS SGD "+
													  "INNER JOIN CAT_DEPENDENCIAS AS A ON (A.ID = SGD.ID_DEPENDENCIA_FUENTE) "+
													  "INNER JOIN CAT_DEPENDENCIAS AS B ON (B.ID = SGD.ID_DEPENDENCIA_DESTINO) "+
													  "INNER JOIN SGD_CAT_TIPO_CORRESPONDENCIA AS C ON (C.ID_CAT_TIPO_ENVIO = SGD.ID_CAT_TIPO) "+
													  "INNER JOIN SGD_CAT_AVISO AS D ON (D.ID_CAT_AVISO = SGD.ID_CAT_AVISO) "+
													  "INNER JOIN SGD_CAT_PRIORIDAD AS E ON (E.ID_CAT_PRIORIDAD = SGD.ID_CAT_PRIORIDAD) "+
													  "INNER JOIN SGD_CAT_ESTATUS_CORRESPONDENCIA AS F ON (F.ID_STATUS = SGD.ID_CAT_STATUS) "+
													  "INNER JOIN SGD_CAT_ORIGEN AS G ON (G.ID_CAT_ORIGEN = SGD.ID_CAT_ORIGEN) "+
													  "LEFT JOIN PERSONAS AS H ON (H.CVE_PERS = SGD.CVE_PERS_APRUEBA) "+
													  "LEFT JOIN PERSONAS AS I ON (I.CVE_PERS = SGD.CVE_PERS_DESTINO) "+
													  "LEFT JOIN PERSONAS AS J ON (J.CVE_PERS = SGD.CVE_PERS_DESTINO) "+
													  "WHERE SGD.ID_CAT_STATUS IN(:status) " +
													  		(Integer.parseInt(m.get("idDependenciaFuente").toString())!=0 ? "AND SGD.ID_DEPENDENCIA_FUENTE =:idDependenciaFuente ":"") +
													  		(Integer.parseInt(m.get("idDependenciaDestino").toString())!=0 ? "AND SGD.ID_DEPENDENCIA_DESTINO =:idDependenciaDestino ":"") +
													  		(Integer.parseInt(m.get("idPersonaDestino").toString())!=0 ? "AND SGD.CVE_PERS_DESTINO =:idPersonaDestino":"") +
													  		(Integer.parseInt(m.get("idPrioridad").toString())!=0 ? "AND SGD.CVE_PERS_DESTINO =:idPrioridad":"") +
													  		(m.get("asunto")!=null ? "AND SGD.ASUNTO LIKE '%"+m.get("asunto").toString()+"%'":"") +
													  		(m.get("numero")!=null ? "AND SGD.NUMERO LIKE '%"+m.get("numero").toString()+"%'":"") +
													  		(m.get("fechaInicial")!=null&&m.get("fechaFinal")!=null ? 
																(!m.get("fechaInicial").toString().equals("")&&!m.get("fechaFinal").toString().equals("") ? " AND CONVERT(datetime, CONVERT(varchar(10), SGD.FECHA, 103), 103) BETWEEN :fechaInicial AND :fechaFinal":"")
															:
																"")+
													  "", m);
	}
}
