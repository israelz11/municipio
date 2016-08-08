package mx.gob.municipio.centro.view.controller.almacen.reportes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/almacen/consultas/rpt_libroAlmacen.action")
public class ControladorReporteLibroAlmacen extends ControladorBaseAlmacen {

	@Autowired
	GatewayAlmacen gatewayAlmacen;
	
	public ControladorReporteLibroAlmacen(){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	public ModelAndView  requestGetControlador(Map modelo, HttpServletRequest request ) {
		Date now = new Date();
		String[] months = {"Enero", "Febrero",
	            "Marzo", "Abril", "Mayo", "Junio", "Julio",
	            "Agosto", "Septiembre", "Octubre", "Noviembre",
	            "Diciembre"};
		
		Calendar calendario = GregorianCalendar.getInstance();
		Date fecha = calendario.getTime();
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("HH:ss");
		
		modelo.put("PERIODO_TEXT", "AL "+formatoDeFecha.getDateTimeInstance(formatoDeFecha.SHORT, formatoDeFecha.SHORT).format(now).substring(0, 2)+" DE "+months[fecha.getMonth()].toUpperCase()+" DE "+(fecha.getYear()+1900));
		modelo.put("FECHA_HORA", formatoDeFecha.getDateTimeInstance(formatoDeFecha.SHORT, formatoDeFecha.SHORT).format(now).substring(9));
		modelo.put("rs",new JRMapCollectionDataSource(gatewayAlmacen.getListaLibroAlmacen()));
		return new ModelAndView("rpt_libroAlmacen",modelo);
	}		
	
	
}
