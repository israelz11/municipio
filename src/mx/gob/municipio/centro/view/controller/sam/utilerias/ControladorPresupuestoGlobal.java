package mx.gob.municipio.centro.view.controller.sam.utilerias;
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
@RequestMapping("/sam/consultas/lst_presupuestoGlobal.action")
public class ControladorPresupuestoGlobal extends ControladorBase {	
	private static Logger log = Logger.getLogger(ControladorPresupuestoGlobal.class.getName());
	private ControladorPresupuestoGlobal(){}
	@Autowired
	public GatewayProyectoPartidas gatewayProyectoPartidas;
	
	@Autowired
	public GatewayPlanArbit gatewayPlanArbit;
	
	@Autowired
	public GatewayUnidadAdm gatewayUnidadAdm;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET , RequestMethod.POST} )    
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {	
        //HttpSession miSesion = request.getSession(true);        
		Integer  unidad = (request.getParameter("unidad")!=null)? Integer.parseInt(request.getParameter("unidad").toString()): 0;
		String  tipoGasto = request.getParameter("tipoGasto");
		//String  proyecto = request.getParameter("proyecto");
		Long idproyecto = (request.getParameter("proyecto")!=null)? Long.parseLong(request.getParameter("proyecto").toString()): 0L;
		String partida = request.getParameter("partida");
		
		if (unidad==0)
			unidad =Integer.parseInt(this.getSesion().getClaveUnidad());
		List tiposDeGasto = gatewayPlanArbit.getTipodeGasto();
		if (!(unidad!=0) && tiposDeGasto.size()>0){
			Map tipo = (Map)tiposDeGasto.get(0);
			tipoGasto=(String)tipo.get("ID_RECURSO");
		}
		
		//if (proyecto!=null) if(!proyecto.equals("0")) 
		//	idproyecto = this.getJdbcTemplate().queryForLong("SELECT TOP 1 ID_PROYECTO FROM CEDULA_TEC WHERE N_PROGRAMA = ?", new Object[]{proyecto});
		modelo.put("partidas", this.getPartidas(idproyecto));
		modelo.put("programas", this.getProgramas(unidad, tipoGasto));
		
		modelo.put("tiposGastos",tiposDeGasto);		
		modelo.put("unidadesAdmiva",gatewayUnidadAdm.getUnidadAdmTodos());
		modelo.put("presupuesto",getPresupuesto(unidad, tipoGasto, idproyecto, partida));
		modelo.put("unidad",unidad);
		modelo.put("tipoGasto",tipoGasto);
		modelo.put("proyecto", idproyecto);
		modelo.put("partida", partida);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
	    return "sam/consultas/lst_presupuestoGlobal.jsp";
	}
	
	private List<Map> getPresupuesto(int unidad , String tipoGasto, Long idproyecto, String partida ){		
		return gatewayProyectoPartidas.getPresupuestoCaledarizado(unidad, tipoGasto, this.getSesion().getIdUsuario(), Integer.parseInt(this.getSesion().getClaveUnidad()), idproyecto, partida);
	}

    public List<Map> getProgramas(int idDependencia, String tipoGasto){
    	return this.getJdbcTemplate().queryForList("SELECT DISTINCT SAM_TEMP_USR_PROY_PART.ID_PROYECTO, SAM_TEMP_USR_PROY_PART.PROYECTO, C.DECRIPCION, V.PROG_PRESUP  FROM SAM_TEMP_USR_PROY_PART INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = SAM_TEMP_USR_PROY_PART.ID_PROYECTO) INNER JOIN VPROYECTO AS V ON (V.ID_PROYECTO = C.ID_PROYECTO) WHERE ID_USUARIO = ? AND C.ID_DEPENDENCIA = ? AND C.ID_RECURSO = ? ORDER BY PROYECTO ASC", new Object[]{this.getSesion().getIdUsuario(), idDependencia,tipoGasto});
    }
	
	public List<Map> getPartidas(Long idproyecto){
    	if(idproyecto!=0){
    		//int id = this.getJdbcTemplate().queryForInt("SELECT ID_PROYECTO FROM CEDULA_TEC WHERE ")
    		return this.getJdbcTemplate().queryForList("SELECT DISTINCT  P.CLV_PARTID, CP.PARTIDA FROM  dbo.VPARTIDAS AS p  "+
														"	INNER JOIN       dbo.CEDULA_TEC AS c ON c.ID_PROYECTO = p.ID_PROYECTO "+
														"INNER JOIN CAT_PARTID AS CP ON(CP.CLV_PARTID = p.CLV_PARTID) "+
														"WHERE  	CAST(c.ID_PROYECTO AS NVARCHAR)+CAST(p.CLV_PARTID AS NVARCHAR) IN (SELECT CAST(ID_PROYECTO AS NVARCHAR)+CAST(CLV_PARTID AS VARCHAR) FROM SAM_TEMP_USR_PROY_PART WHERE ID_USUARIO = ?) AND c.ID_PROYECTO = ? "+   
														"ORDER BY p.CLV_PARTID ASC", new Object[]{this.getSesion().getIdUsuario(), idproyecto});
    	}
    	else
    		return null;
    	
    }
}
