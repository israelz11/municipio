package mx.gob.municipio.centro.view.controller.sam.reportes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sam/reportes/rpt_presupuesto_calendarizado.action")
public class ControladorReportePresupuestoCalendarizado extends ControladorBase {
	
	@Autowired
	public GatewayProyectoPartidas gatewayProyectoPartidas;
	
	public ControladorReportePresupuestoCalendarizado() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET}) 
	public ModelAndView  requestGetControlador( Map modelo, HttpServletRequest request) {
		//HttpSession miSesion = request.getSession(true);        
		Integer  unidad = (request.getParameter("unidad")!=null)? Integer.parseInt(request.getParameter("unidad").toString()): 0;
		String  tipoGasto = request.getParameter("tipoGasto");
		Long idproyecto = (request.getParameter("proyecto")!=null)? Long.parseLong(request.getParameter("proyecto").toString()): 0L;
		String partida = request.getParameter("partida");
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		String fecha = dateFormat.format(date); 
		modelo.put("FECHA_IMPRESION", fecha);
		
		if(unidad==0)
			unidad = Integer.parseInt(this.getSesion().getClaveUnidad());
		
		String unidad_adm = (String) this.getJdbcTemplate().queryForObject("SELECT CLV_UNIADM+' '+DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID = ?", new Object[]{unidad}, String.class);
		modelo.put("UNIDAD_SOLICITANTE", unidad_adm);
		
		if(tipoGasto!=null)
			if(!tipoGasto.equals("0"))
				modelo.put("TIPO_GASTO", (String) this.getJdbcTemplate().queryForObject("SELECT RECURSO FROM CAT_RECURSO WHERE ID =?", new Object[]{tipoGasto}, String.class));
		else
			modelo.put("TIPO_GASTO", "[Todos los tipos de gasto]");
		
		
		
		modelo.put("rs",new JRMapCollectionDataSource(getPresupuesto(unidad, tipoGasto, idproyecto, partida)));
		return new ModelAndView("rpt_presupuesto_calendarizado",modelo);
	}
	
	private List<Map> getPresupuesto(int unidad , String tipoGasto, Long  idproyecto, String partida ){		
		return gatewayProyectoPartidas.getPresupuestoCaledarizado(unidad,tipoGasto,this.getSesion().getIdUsuario(), Integer.parseInt(this.getSesion().getClaveUnidad()), idproyecto, partida);
	}
	

}
