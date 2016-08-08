/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/muestra_presupuesto.action")
public class ControladorProyectoPartida extends ControladorBase {	
	private static Logger log = Logger.getLogger(ControladorProyectoPartida.class.getName());
	private ControladorProyectoPartida(){}
	@Autowired
	public GatewayProyectoPartidas gatewayProyectoPartidas;
	@Autowired
	private GatewayPlanArbit gatewayPlanArbit;
	@Autowired
	public GatewayUnidadAdm gatewayUnidadAdm;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {	
        //HttpSession miSesion = request.getSession(true);     
        Integer  unidad = (request.getParameter("unidad")!=null)? Integer.parseInt(request.getParameter("unidad").toString()): 0;
		String  proyecto = request.getParameter("proyecto");
		Long idproyecto = (request.getParameter("idproyecto")!=null)? Long.parseLong(request.getParameter("idproyecto").toString()): 0L;
		Long idVale = (request.getParameter("idVale")!=null)? Long.parseLong(request.getParameter("idVale").toString()): 0L;
		Long cve_contrato = (request.getParameter("cve_contrato")!=null)? Long.parseLong(request.getParameter("cve_contrato").toString()): 0L;
		Integer  tipoGasto = (request.getParameter("tipoGasto")!=null)? Integer.parseInt(request.getParameter("tipoGasto").toString()): 0;
		String ban = request.getParameter("ban");
		String partida = request.getParameter("partida")!=null  ? (request.getParameter("partida").equals("")) ? null: request.getParameter("partida").toString() : null ;
		Integer mes 	= request.getParameter("mes")!=null  ? ((String)request.getParameter("mes")).equals("") ? null:  Integer.parseInt((String)request.getParameter("mes")) : null ;
		if (mes==null||mes==0)
			 mes= this.getJdbcTemplate().queryForInt("select ISNULL(min(MES),0) from MESES where estatus='ACTIVO' ");
		String desMes="";
		if (mes!=null && !mes.equals(0) )
			desMes=getDesMes(mes);
		if(unidad==0) unidad = Integer.parseInt(this.getSesion().getIdUnidad());
		modelo.put("partidas", this.getPartidas(idproyecto));
		modelo.put("programas", this.getProgramas(unidad, tipoGasto));
		
		modelo.put("unidadesAdmiva",gatewayUnidadAdm.getUnidadAdmTodos());
		modelo.put("unidad", unidad);
		modelo.put("nombreUnidad", this.getSesion().getUnidad());
		modelo.put("idVale", idVale);
		modelo.put("cve_contrato", cve_contrato);
		modelo.put("proyecto", idproyecto);
		modelo.put("partida", partida);
		modelo.put("tipo_gasto",tipoGasto );
		modelo.put("ban", ban);
		modelo.put("mes",mes);
		modelo.put("desMes",desMes);
		if(idVale!=0)
			modelo.put("muestraPresupuesto",getPresupuestoVale(idproyecto, proyecto, partida, mes, tipoGasto, unidad, idVale));
		if(cve_contrato!=0)
			modelo.put("muestraPresupuesto",getPresupuestoContrato(idproyecto, proyecto, partida, mes, tipoGasto, unidad, cve_contrato));
		else
			modelo.put("muestraPresupuesto",getPresupuesto(idproyecto, proyecto, partida, mes, tipoGasto, unidad));
		
	    return "sam/consultas/muestra_presupuesto.jsp";
	}
	
	public List<Map> getPresupuestoContrato(Long idproyecto, String proyecto, String partida, Integer mes, int idTipoGasto, int idUnidad, Long cve_contrato){
		return gatewayProyectoPartidas.getPresupuestoContrato(idproyecto, proyecto, partida, mes,  this.getSesion().getIdUsuario(),idUnidad,idTipoGasto, cve_contrato);
	}
	
	public List<Map> getPresupuestoVale(Long idproyecto, String proyecto, String partida, Integer mes, int idTipoGasto, int idUnidad, Long idVale){
		return gatewayProyectoPartidas.getPresupuestoVale(idproyecto, proyecto, partida, mes,  this.getSesion().getIdUsuario(),idUnidad,idTipoGasto, idVale);
	}
	
	public List<Map> getPresupuesto(Long idproyecto, String proyecto, String partida, Integer mes, int idTipoGasto, int idUnidad ){
		return gatewayProyectoPartidas.getPresupuesto(idproyecto, proyecto, partida, mes,  this.getSesion().getIdUsuario(),idUnidad,idTipoGasto);
	}
	
	public String getDesMes(Integer mes) {
		return (String)this.getJdbcTemplate().queryForObject("select DESCRIPCION from MESES where mes = ?",new Object[]{mes},String.class);
	}
	
	@ModelAttribute("tipodeGasto")
    public List<Map> getTiposDeGasto(){
    	return gatewayPlanArbit.getTipodeGasto();
    }
	
	public List<Map> getProgramas(int idDependencia, int tipoGasto){
		String clausula = (tipoGasto!=0) ? "AND C.ID_RECURSO = "+tipoGasto:"";
    	return this.getJdbcTemplate().queryForList("SELECT DISTINCT SAM_TEMP_USR_PROY_PART.ID_PROYECTO, SAM_TEMP_USR_PROY_PART.PROYECTO, C.DECRIPCION FROM SAM_TEMP_USR_PROY_PART INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = SAM_TEMP_USR_PROY_PART.ID_PROYECTO) WHERE ID_USUARIO =? AND C.ID_DEPENDENCIA = ? "+clausula+"  ORDER BY PROYECTO ASC", new Object[]{this.getSesion().getIdUsuario(), idDependencia});
    }
	
    public List<Map> getPartidas(Long idproyecto){
    	if(idproyecto!=0){
    		return this.getJdbcTemplate().queryForList("SELECT DISTINCT  P.CLV_PARTID, CP.PARTIDA FROM  dbo.VPARTIDAS AS p  "+
														"	INNER JOIN       dbo.CEDULA_TEC AS c ON c.ID_PROYECTO = p.ID_PROYECTO "+
														"INNER JOIN CAT_PARTID AS CP ON(CP.CLV_PARTID = p.CLV_PARTID) "+
														"WHERE  	CAST(c.ID_PROYECTO AS NVARCHAR)+CAST(p.CLV_PARTID AS NVARCHAR) IN (SELECT CAST(ID_PROYECTO AS NVARCHAR)+CAST(CLV_PARTID AS VARCHAR) FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = ?) AND c.ID_PROYECTO = ? "+   
														"ORDER BY p.CLV_PARTID ASC", new Object[]{this.getSesion().getIdUsuario(), idproyecto});
    	}
    	else
    		return null;
    }
    
    public List<Map> getListaValesPresupuesto(Long cve_vale, int idproyecto, String clv_partid, int idRecurso, int mes){
    	String clausula = ""; 
    	if(idRecurso!=0) clausula = " AND C.ID_RECURSO = "+idRecurso;
    	
		return this.getJdbcTemplate().queryForList("SELECT "+
															"SAM_VALES_EX.CVE_VALE, "+
															"ROUND((SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_MOV_VALES WHERE CVE_VALE = SAM_VALES_EX.CVE_VALE AND ID_PROYECTO = M.ID_PROYECTO AND CLV_PARTID = M.CLV_PARTID),2) AS TOTAL, "+ 
															"ROUND("+
															"		(SELECT ISNULL(SUM(IMPORTE),0) FROM SAM_MOV_VALES WHERE CVE_VALE = SAM_VALES_EX.CVE_VALE AND ID_PROYECTO = M.ID_PROYECTO AND CLV_PARTID = M.CLV_PARTID) - "+  	
															"			( 	"+
															"				(SELECT ISNULL(SUM(MV.MONTO),0) FROM SAM_MOV_OP AS MV INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = MV.CVE_OP) INNER JOIN SAM_REQUISIC AS RR ON (RR.CVE_REQ = OP.CVE_REQ) WHERE RR.CVE_VALE = SAM_VALES_EX.CVE_VALE AND RR.PERIODO = SAM_VALES_EX.MES AND RR.ID_PROYECTO = M.ID_PROYECTO AND RR.CLV_PARTID = M.CLV_PARTID AND OP.STATUS NOT IN (-1, -2, 4)	) "+
															"				+(SELECT ISNULL(SUM(M.MONTO),0) FROM SAM_MOV_OP AS M INNER JOIN SAM_ORD_PAGO AS OP ON (OP.CVE_OP = M.CVE_OP) WHERE M.CVE_VALE = M.CVE_VALE AND M.ID_PROYECTO = M.ID_PROYECTO AND M.CLV_PARTID = M.CLV_PARTID AND OP.PERIODO = SAM_VALES_EX.MES AND OP.STATUS NOT IN (-1, -2, 4)) "+
															"				+(SELECT ISNULL(SUM(R.IMPORTE),0) FROM SAM_COMP_REQUISIC AS R INNER JOIN SAM_REQUISIC AS RR ON (RR.CVE_REQ = R.CVE_REQ) WHERE RR.CVE_VALE = M.CVE_VALE AND R.PERIODO = SAM_VALES_EX.MES AND RR.ID_PROYECTO =M.ID_PROYECTO AND RR.CLV_PARTID = M.CLV_PARTID AND RR.STATUS NOT IN (0, 4, 5)) "+ 	
															"				+(SELECT ISNULL(SUM(P.TOTAL),0) FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC AS R ON (R.CVE_REQ = P.CVE_REQ) WHERE R.ID_PROYECTO =M.ID_PROYECTO AND R.CLV_PARTID = M.CLV_PARTID AND  R.PERIODO = SAM_VALES_EX.MES AND R.CVE_VALE = SAM_VALES_EX.CVE_VALE AND  P.STATUS NOT IN (0, 3, 5)) "+
															"			) "+
															"	,2) AS DISPONIBLE," +
															"ROUND((SELECT ISNULL(SUM(MONTO),0) FROM SAM_MOV_OP WHERE CVE_VALE = SAM_VALES_EX.CVE_VALE AND ID_PROYECTO = M.ID_PROYECTO AND CLV_PARTID = M.CLV_PARTID),2) AS COMPROBADO "+ 
														"FROM SAM_MOV_VALES AS M "+
														"	INNER JOIN SAM_VALES_EX ON (SAM_VALES_EX.CVE_VALE = M.CVE_VALE) "+
														"	INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = M.ID_PROYECTO) "+
														"WHERE STATUS IN (4) AND SAM_VALES_EX.CVE_VALE = ? AND SAM_VALES_EX.MES = ? AND M.ID_PROYECTO = ? AND M.CLV_PARTID = ? "+clausula, new Object[]{cve_vale, mes, idproyecto, clv_partid});
	}
    
    public void eliminarArchivo(Long idArchivo, HttpServletRequest request){
		eliminarArchivoFisico(this.getSesion().getIdUsuario(), idArchivo, request);
	}
    
    public void eliminarArchivoFisico(int cve_pers, Long idArchivo, HttpServletRequest request){
		//si tiene los privilegios elimina
		//if(!getPrivilegioEn(cve_pers, 129))
		//	throw new RuntimeException("No se puede eliminar el archivo, su usuario no cuenta con los privilegios suficientes");
		
		Map archivo = this.getJdbcTemplate().queryForMap("SELECT *FROM SAM_PROYECTOS_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		/*eliminado fisico*/
		File fichero = new File(request.getSession().getServletContext().getRealPath("")+"/sam/consultas/archivos/"+archivo.get("NOMBRE").toString());
	   
		if (fichero.delete()){
			 System.out.println("El fichero ha sido borrado satisfactoriamente");
			 this.getJdbcTemplate().update("DELETE FROM SAM_PROYECTOS_ARCHIVOS WHERE ID_ARCHIVO =?", new Object[]{idArchivo});
		}
		else
			System.out.println("El fichero no puede ser borrado");
	}
	
}
