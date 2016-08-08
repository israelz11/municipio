/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.ibm.icu.util.Calendar;
import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayEvaluacionProyecto  extends BaseGateway {
	private static Logger log = Logger.getLogger(GatewayEvaluacionProyecto.class.getName());
	/*Constructor*/
	public GatewayEvaluacionProyecto() {
	
	}
	
	/*Metodo usado para determinar si se guarda una nueva requisicion o se actualiza una existente*/
	public  Long actualizarEvalucionProyectos(Long idProyectoEvaluacion ,Long idproyecto, Integer ejercicio, Date fecInicio, Date fecTermino, Date fecActa,Integer cantidad, Date fecContratoIni , Date fecContratoFin ){			
		  if (idProyectoEvaluacion == null)
			  idProyectoEvaluacion= guardar(idproyecto, ejercicio,fecInicio, fecTermino, fecActa,cantidad, fecContratoIni , fecContratoFin);  
		  else
			  actualizar(idProyectoEvaluacion,idproyecto, ejercicio, fecInicio, fecTermino, fecActa,cantidad, fecContratoIni , fecContratoFin);
	  return  idProyectoEvaluacion; 
	}	
	
	public Long guardar( final Long idproyecto,final Integer ejercicio,final Date fecInicio,final Date fecTermino,final Date fecActa,final Integer cantidad,final Date fecContratoIni ,final Date fecContratoFin    ) {
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	Long key_orden=null;
    	final String INSERT_SQL ="INSERT INTO SAM_EVAL_PROY_MENSUAL (proyecto,ejercicio,fecha_inicio,fecha_termino,fecha_acta,cantidad,fecha_contrato_ini,fecha_contrato_fin)  VALUES (?,?,?,?,?,?,?,?)";
        try{ 
            this.getJdbcTemplate().update(
                             new PreparedStatementCreator() {
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                             PreparedStatement ps =
                                 connection.prepareStatement(INSERT_SQL, new String[]{ "id_evaluacion_proyecto"} );
                                         ps.setLong( 1, idproyecto );                                         
                                         ps.setInt( 2,ejercicio );
                                         ps.setDate( 3, fecInicio );                                         
                                         ps.setDate(4, fecTermino);                                           
                                         ps.setDate(5, fecActa);
                                         ps.setInt(6, cantidad);
                                         ps.setDate(7, fecContratoIni);
                                         ps.setDate(8, fecContratoFin);              
                                 return ps;
                            }
                     },
            keyHolder);
            key_orden = new Long(keyHolder.getKey().longValue());
        }catch( DataAccessException ex) {
        	log.info("Fallo la inserción del sql");
        	throw ex;                     
        }
        return key_orden;        
    }
	
	

	/*Metodo para actualizar los datos de la requisicion*/
	public void actualizar(Long idProyectoEvaluacion ,Long idproyecto, Integer ejercicio, Date fecInicio, Date fecTermino, Date fecActa,Integer cantidad, Date fecContratoIni , Date fecContratoFin ){
		String SQL = "UPDATE SAM_EVAL_PROY_MENSUAL set  ID_PROYECTO=?,ejercicio=?,fecha_inicio=?,fecha_termino=?,fecha_acta=?,cantidad=?,fecha_contrato_ini=?,fecha_contrato_fin=?  where id_evaluacion_proyecto=? ";
		this.getJdbcTemplate().update(SQL, new Object[]{idproyecto, ejercicio,  fecInicio ,  fecTermino , fecActa  ,cantidad,fecContratoIni   , fecContratoFin  ,idProyectoEvaluacion});
	}
	
	
	
		
	
	public  void  actualizarEvalucionDetallePrincipal(Integer idDetalleEvaluacion ,Integer idProyectoEvaluacion, Date fecha, Integer avance,String tipo,Integer mes){		
		  if (idDetalleEvaluacion == null)
			  guardarDetalle(idProyectoEvaluacion, fecha, avance,tipo,mes);  
		  else
			  actualizarDetalle(idDetalleEvaluacion , fecha, avance);		
	}	
	
	
	public void guardarDetalle(Integer idProyectoEvaluacion, Date fecha, Integer avance,String tipo, Integer mes) {
		String SQL = " insert into SAM_EVAL_PROY_MENS_DETALLE (ID_EVALUACION_PROYECTO,FECHA,AVANCE,TIPO,MES) values (?,?,?,?,?) ";
		this.getJdbcTemplate().update(SQL, new Object[]{idProyectoEvaluacion,fecha,avance,tipo,mes});        
  }
	
	

	/*Metodo para actualizar los datos de la requisicion*/
	public void actualizarDetalle(Integer idDetalleEvaluacion , Date fecha, Integer avance ){
		String SQL = "UPDATE SAM_EVAL_PROY_MENS_DETALLE  set FECHA=?,AVANCE=?  where ID_EVALUACION_DETALLE=? ";
		this.getJdbcTemplate().update(SQL, new Object[]{fecha,avance,idDetalleEvaluacion});
	}
	
	public List getDetallesEvaluacionTipo(Integer  idProyectoEvaluacion,  String tipo, Integer ejercicio ){
		return this.getJdbcTemplate().queryForList("select a.ID_EVALUACION_DETALLE,a.ID_EVALUACION_PROYECTO,a.AVANCE,a.TIPO,a.MES, convert(varchar(10),a.FECHA,103) FECHA , b.DESCRIPCION  DESMES , B.ESTATUSEVA from SAM_EVAL_PROY_MENS_DETALLE a  , meses b where a.ID_EVALUACION_PROYECTO =? and a.TIPO=?  and a.mes=b.mes  and b.ejercicio=? ",new Object []{idProyectoEvaluacion,tipo,ejercicio});
	}
	
	public List getProyectos(int cve_pers, String status, int cve_unidad, Integer mes, String cve_unidad_usr){
		String Clausulas = "";
		if(status.equals("1")) Clausulas = " AND PRESUPUEST.TERMINADO ='S'";
		if(status.equals("2")) Clausulas = " AND PRESUPUEST.TERMINADO ='N'";
		if(status.equals("3")) Clausulas = " ";
		//if(!cve_unidad.equals(cve_unidad_usr)){
		
		Clausulas +=" AND PRESUPUEST.APROBADO ='S' AND A.ID_PROYECTO IN (SELECT ID_PROYECTO FROM VT_PROYECTOS_USUARIOS WHERE ID_USUARIO = "+cve_pers+")";
		
		if(cve_unidad!=0) 
				Clausulas += " AND a.ID_DEPENDENCIA IN ('"+cve_unidad+"')";
		//}
		
		
		
		String sql = "SELECT a.ID_PROYECTO, "+ 
							"VP.RECURSO,"+
							"VP.PROG_PRESUP,"+
							"(VP.CLV_ACTINST+' '+VP.ACT_INST) AS ACTIVIDAD_INST,"+
							"(VP.CLV_LOCALIDAD+' '+VP.LOCALIDAD) AS LOCALIDAD, "+
							"a.N_PROGRAMA, "+
							"ISNULL(VP.K_PROYECTO_T, '') AS K_PROYECTO_T,"+
							"a.ID_DEPENDENCIA, "+
							"a.DECRIPCION, "+
							"PRESUPUEST.APROBADO,  "+ 
							"b.EJERCICIO, "+
							"convert(varchar(10),"+
							"b.FECHA_INICIO,103) FECHA_INICIO ,"+
							"convert(varchar(10),"+
							"b.FECHA_TERMINO,103) FECHA_TERMINO, "+
							"convert(varchar(10),b.FECHA_ACTA,103) FECHA_ACTA,   "+
							"convert(varchar(10),a.FEINIPER,103) FEINIPER,"+
							"convert(varchar(10),a.FETERPER,103) FETERPER,"+
							"b.CANTIDAD, convert(varchar(10),"+
							"b.FECHA_CONTRATO_INI,103) FECHA_CONTRATO_INI , "+
							"convert(varchar(10),"+
							"b.FECHA_CONTRATO_FIN,103) FECHA_CONTRATO_FIN,"+
							"b.ID_EVALUACION_PROYECTO , "+
							"0 MONTOAUTORIZADO ,  "+
							"0 MONTOEJERCIDO, "+
							"ISNULL((SELECT SUM (AVANCE) FROM SAM_EVAL_PROY_MENS_DETALLE WHERE ID_EVALUACION_PROYECTO=B.id_evaluacion_proyecto AND MES <= ? AND TIPO = 'Fisica'),0) AVANCE_FISICO,	"+
							"ISNULL((SELECT SUM (AVANCE) FROM SAM_EVAL_PROY_MENS_DETALLE WHERE ID_EVALUACION_PROYECTO=B.id_evaluacion_proyecto AND MES <= ? AND TIPO = 'Contraloria'),0) AVANCE_CONTRALORIA   "+
						"FROM   "+
							" dbo.CEDULA_TEC a INNER JOIN  SAM_EVAL_PROY_MENSUAL b ON a.ID_PROYECTO = b.ID_PROYECTO "+
							" INNER JOIN PRESUPUEST ON (PRESUPUEST.ID_PROYECTO = A.ID_PROYECTO)"+
							" INNER JOIN VPROYECTO AS VP ON (VP.ID_PROYECTO = a.ID_PROYECTO) "+
						" WHERE 0=0 "+ Clausulas+
						" ORDER BY A.N_PROGRAMA ASC";
		return this.getJdbcTemplate().queryForList(sql, new Object[]{mes, mes});
	}

}
