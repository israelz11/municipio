/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayEvaluacionProyecto;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/utilerias/evaluacion_proyectos.action")
public class ControladorEvaluacionProyectos extends ControladorBase {

	public ControladorEvaluacionProyectos(){		
	}
		
	@Autowired
	public GatewayMeses gatewayMeses;
	
	@Autowired
	public GatewayEvaluacionProyecto gatewayEvaluacionProyecto;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})     
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), 27);
		modelo.put("ejercicio", this.getSesion().getEjercicio());
		modelo.put("id_proyecto", request.getParameter("proyecto")==null ? "": request.getParameter("proyecto"));
		if(!modelo.get("id_proyecto").equals("")) modelo.put("idEvaluacionProyecto", this.getJdbcTemplate().queryForInt("SELECT ISNULL(ID_EVALUACION_PROYECTO,0) AS N FROM SAM_EVAL_PROY_MENSUAL WHERE ID_PROYECTO = ?", new Object[]{modelo.get("id_proyecto").toString()}));
		modelo.put("meses",gatewayMeses.getTodosMesesEjercicioEvaluacion(this.getSesion().getEjercicio(), privilegio));
		if(!modelo.get("id_proyecto").equals("")) modelo.put("eval", this.getProyecto(modelo.get("id_proyecto").toString()));
	    return "sam/utilerias/evaluacion_proyectos.jsp";
	}
			
	public Map getProyecto(String proyecto){
		String sql = "SELECT a.ID_PROYECTO, a.N_PROGRAMA, a.ID_DEPENDENCIA, a.DECRIPCION,   b.EJERCICIO, convert(varchar(10),b.FECHA_INICIO,103) FECHA_INICIO , convert(varchar(10),b.FECHA_TERMINO,103) FECHA_TERMINO, convert(varchar(10),b.FECHA_ACTA,103) FECHA_ACTA, "+   
				"b.CANTIDAD, convert(varchar(10),b.FECHA_CONTRATO_INI,103) FECHA_CONTRATO_INI , convert(varchar(10),b.FECHA_CONTRATO_FIN,103) FECHA_CONTRATO_FIN,   b.ID_EVALUACION_PROYECTO , "+
				"(dbo.getAutorizadoProyecto(1, 12, a.ID_PROYECTO)) AS MONTOAUTORIZADO, "+
				/*"(CASE (3) "+
				"	WHEN 1 THEN (SELECT SUM(ENEPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 2 THEN (SELECT SUM(FEBPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 3 THEN (SELECT SUM(MARPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 4 THEN (SELECT SUM(ABRPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 5 THEN (SELECT SUM(MAYPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 6 THEN (SELECT SUM(JUNPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 7 THEN (SELECT SUM(JULPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 8 THEN (SELECT SUM(AGOPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 9 THEN (SELECT SUM(SEPPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 10 THEN (SELECT SUM(OCTPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 11 THEN (SELECT SUM(NOVPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 12 THEN (SELECT SUM(DICPREACT) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				" END) AS MONTOAUTORIZADO,   "+*/
				"(dbo.getEjercidoAnualProyecto(a.ID_PROYECTO)) AS MONTOEJERCIDO, "+
				/*"(CASE (3) "+
				"	WHEN 1 THEN (SELECT SUM(ENEPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 2 THEN (SELECT SUM(FEBPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 3 THEN (SELECT SUM(MARPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 4 THEN (SELECT SUM(ABRPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 5 THEN (SELECT SUM(MAYPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 6 THEN (SELECT SUM(JUNPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 7 THEN (SELECT SUM(JULPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 8 THEN (SELECT SUM(AGOPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 9 THEN (SELECT SUM(SEPPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 10 THEN (SELECT SUM(OCTPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 11 THEN (SELECT SUM(NOVPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"	WHEN 12 THEN (SELECT SUM(DICPREEJE) FROM SPARTIDAS WHERE ID_PROYECTO = a.ID_PROYECTO) "+
				"END) AS MONTOEJERCIDO,  "+
				*/
				"(SELECT ISNULL(SUM (AVANCE),0) FROM SAM_EVAL_PROY_MENS_DETALLE WHERE ID_EVALUACION_PROYECTO=B.id_evaluacion_proyecto AND TIPO = 'Fisica' ) AVANCE_FISICO, (SELECT ISNULL(SUM(AVANCE),0) FROM SAM_EVAL_PROY_MENS_DETALLE WHERE ID_EVALUACION_PROYECTO=B.id_evaluacion_proyecto AND TIPO = 'Contraloria' ) AVANCE_CONTRALORIA   FROM  "+  
				"	    			 dbo.CEDULA_TEC a LEFT OUTER  JOIN  SAM_EVAL_PROY_MENSUAL b ON a.ID_PROYECTO = b.ID_PROYECTO WHERE A.ID_PROYECTO=? "+
				"	    			 and A.ID_PROYECTO in (SELECT ID_PROYECTO FROM VT_PROYECTOS_USUARIOS WHERE ID_USUARIO =?) ";
		try  {
			Map resultado=this.getJdbcTemplate().queryForMap(sql,new Object[]{proyecto,this.getSesion().getIdUsuario()});
		    return resultado;
		} catch ( DataAccessException e) {
			return null;
		}
	}
	
		
	
	public Long guardarEvaluacion(Long idProyectoEvaluacion ,Long idproyecto, Integer ejercicio, String fecInicio, String fecTermino, String fecActa,Integer cantidad, String fecContratoIni , String fecContratoFin ) {
	      return gatewayEvaluacionProyecto.actualizarEvalucionProyectos(idProyectoEvaluacion ,idproyecto, ejercicio, this.formatoFecha(fecInicio), this.formatoFecha(fecTermino), this.formatoFecha(fecActa),cantidad, this.formatoFecha(fecContratoIni) , this.formatoFecha(fecContratoFin) );		
	}
	
	public  String  guardarEvalucionDetalle(Integer idDetalleEvaluacion ,Integer idProyectoEvaluacion, String fecha, Integer avance,String tipo, Integer mes){
		if (mesvalido(idDetalleEvaluacion,tipo,mes,idProyectoEvaluacion)) {
		    gatewayEvaluacionProyecto.actualizarEvalucionDetallePrincipal(idDetalleEvaluacion ,idProyectoEvaluacion, this.formatoFecha(fecha), avance,tipo,mes);
		    return "La actualización se realizo con exito";
		}else 
			return "El periodo ya esta dado de alta ";
		
	}
	
	public boolean mesvalido(Integer idDetalleEvaluacion,String tipo,Integer mes, Integer idEvaluacion) {
		Integer regEncontrado;
		if (idDetalleEvaluacion==null)
			regEncontrado = (Integer)this.getJdbcTemplate().queryForObject("select count(*) from SAM_EVAL_PROY_MENS_DETALLE where mes=? and tipo=? and ID_EVALUACION_PROYECTO=?  ",new Object[] {mes,tipo,idEvaluacion},Integer.class);
		else
			regEncontrado = (Integer)this.getJdbcTemplate().queryForObject("select count(*) from  SAM_EVAL_PROY_MENS_DETALLE where mes=? and tipo=?  and ID_EVALUACION_DETALLE !=? and ID_EVALUACION_PROYECTO=?",new Object[] {mes,tipo,idDetalleEvaluacion,idEvaluacion},Integer.class );
		return regEncontrado.equals(0);
	}
		
	
	public List getDetallesEvaluacionTipo(Integer idProyectoEvaluacion, String tipo,Integer ejercicio) {
		return gatewayEvaluacionProyecto.getDetallesEvaluacionTipo(idProyectoEvaluacion, tipo, ejercicio);
	}
	
}
