package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.municipio.centro.model.gateways.sam.GatewayVales;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/reportes/rpt_listado_vales.action")
public class ControladorReporteListadoVales extends ControladorBase {

	
	@Autowired
	GatewayVales gatewayVales;
	
	public final static  int VER_TODAS_LAS_UNIDADES = 25;
	
	/**
	 * @param args
	 */
	public ControladorReporteListadoVales() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET}) 
	
	public ModelAndView  requestGetControlador( Map modelo, HttpServletRequest request) {
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		String fecha = dateFormat.format(date); 
		modelo.put("FECHA_IMPRESION", fecha);
		
		String unidad="";
		String estatus=request.getParameter("status")==null ? gatewayVales.getEstatusNueva().toString(): this.arrayToString(request.getParameterValues("status"),",");
		String fechaIni=request.getParameter("fechaInicial");
		String fechaFin=request.getParameter("fechaFinal");
		String tipoGasto=request.getParameter("cbotipogasto");
		String verUnidad=request.getParameter("verUnidad");
		
		if(privilegio){
			if(request.getParameter("cbodependencia")==null)
				unidad = "0";
			if(request.getParameter("cbodependencia")!=null)
				unidad = request.getParameter("cbodependencia");
		}
		
		if(!privilegio){
			if(request.getParameter("cbodependencia")==null)
				unidad = this.getSesion().getClaveUnidad();
			if(request.getParameter("cbodependencia")!=null)
				unidad = request.getParameter("cbodependencia");
		}
		
		modelo.put("idUnidad", unidad);
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("fechaInicial",fechaIni);
		modelo.put("fechaFinal",fechaFin);
		modelo.put("status",estatus);
		modelo.put("tipo_gto",tipoGasto );
		modelo.put("verUnidad",verUnidad);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		
		String unidad_adm = (String) this.getJdbcTemplate().queryForObject("SELECT CLV_UNIADM+' '+DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID = ?", new Object[]{this.getSesion().getClaveUnidad()}, String.class);
		modelo.put("UNIDAD_SOLICITANTE", unidad_adm);
		
		modelo.put("rs",new JRMapCollectionDataSource(this.getListadoVales(unidad, estatus, fechaIni,fechaFin,this.getSesion().getEjercicio(),tipoGasto,this.getSesion().getIdUsuario(),verUnidad, privilegio)));	
		
		return new ModelAndView("rpt_listado_vales",modelo);
	}
	
	public List <Map>getListadoVales(String unidad, String  estatus , String fechaInicial, String fechaFinal , Integer ejercicio, String tipoGasto, Integer idUsuario, String verUnidad, Boolean privilegio){
		return this.gatewayVales.getListaDeValesPorEjemplo(unidad, estatus , this.formatoFecha(fechaInicial), this.formatoFecha(fechaFinal) , ejercicio, tipoGasto, idUsuario, verUnidad, privilegio);
	}

}
