package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/reportes/rpt_presupuesto_mensual.action")
public class ControladorReportePresupuestoMensual extends ControladorBase {

	@Autowired
	public GatewayProyectoPartidas gatewayProyectoPartidas;
	
	/**
	 * @param args
	 */
	public ControladorReportePresupuestoMensual() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET}) 
	public ModelAndView  requestGetControlador( Map modelo, HttpServletRequest request) {
		 //HttpSession miSesion = request.getSession(true);        
			Long idproyecto = (request.getParameter("idproyecto")!=null)? Long.parseLong(request.getParameter("idproyecto").toString()): 0L;
			String  proyecto = request.getParameter("proyecto");
			Integer  tipoGasto = (request.getParameter("tipoGasto")!=null)? Integer.parseInt(request.getParameter("tipoGasto").toString()): 0;
			String ban = request.getParameter("ban");
			String partida = request.getParameter("partida")!=null  ? (request.getParameter("partida").equals("")) ? null: request.getParameter("partida").toString() : null ;
			Integer mes 	= request.getParameter("mes")!=null  ? ((String)request.getParameter("mes")).equals("") ? null:  Integer.parseInt((String)request.getParameter("mes")) : null ;
			
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
			String fecha = dateFormat.format(date); 
			modelo.put("FECHA_IMPRESION", fecha);
			
			String unidad_adm = (String) this.getJdbcTemplate().queryForObject("SELECT CLV_UNIADM+' '+DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID = ?", new Object[]{this.getSesion().getClaveUnidad()}, String.class);
			modelo.put("UNIDAD_SOLICITANTE", unidad_adm);
			
			if(tipoGasto!=0)
					modelo.put("TIPO_GASTO", (String) this.getJdbcTemplate().queryForObject("SELECT RECURSO FROM CAT_RECURSO WHERE ID =?", new Object[]{tipoGasto}, String.class));
			else
				modelo.put("TIPO_GASTO", "[Todos los tipos de gasto]");
			
			if (mes==null)
				 mes= this.getJdbcTemplate().queryForInt("select ISNULL(min(MES),0) from MESES where estatus='ACTIVO' ");
			String desMes="";
			if (mes!=null && !mes.equals(0) )
				desMes=getDesMes(mes);
			modelo.put("ban", ban);
			modelo.put("mes",mes);
			modelo.put("desMes",desMes);
			modelo.put("rs",new JRMapCollectionDataSource(getPresupuesto(idproyecto, proyecto, partida, mes,tipoGasto)));
		return new ModelAndView("rpt_presupuesto_mensual",modelo);
	}
	
	public List<Map> getPresupuesto(Long idpresupuesto, String proyecto, String partida, Integer mes, int idTipoGasto ){
		return gatewayProyectoPartidas.getPresupuesto(idpresupuesto, proyecto, partida, mes,  this.getSesion().getIdUsuario(),Integer.parseInt(this.getSesion().getClaveUnidad()),idTipoGasto);
	}
	
	public String getDesMes(Integer mes) {
		return (String)this.getJdbcTemplate().queryForObject("select DESCRIPCION from MESES where mes = ?",new Object[]{mes},String.class);
	}

}
