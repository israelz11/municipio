package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.municipio.centro.model.gateways.sam.GatewayDevolucionesPresupuestales;
import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import mx.gob.municipio.centro.view.componentes.RMCantidadEnLetras;

@Controller
@RequestMapping("/sam/reportes/rpt_devolucion_presupuestal.action")
public class ControladorReporteDevolucionPresupuestal extends ControladorBase {
	
	@Autowired
	GatewayDevolucionesPresupuestales gatewayDevolucionesPresupuestales;
	
	@Autowired 
	GatewayMeses gatewayMeses;
	
	@Autowired
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	
	@Autowired
	private RMCantidadEnLetras rmCantidadEnLetras;
	
	public ControladorReporteDevolucionPresupuestal(){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView  requestGetControlador( Map modelo, @RequestParam("idDevolucion")  Long  idDevolucion ) {
		String[] Meses = {"ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
		modelo = gatewayDevolucionesPresupuestales.getDevolucionPresupuestal(idDevolucion);
		BigDecimal temp = (BigDecimal) (modelo.get("TOTAL"));
		Double total = redondea(temp.doubleValue(),2);
		//modelo.put("TOTAL", total);
		
		BigDecimal importe = ((BigDecimal)modelo.get("TOTAL"));
		String cantidadLetra = convertirALetras(importe);
		
		modelo.put("IMPORTELETRAS", cantidadLetra.substring(1, cantidadLetra.length()-1));
		
		modelo.put("MES_DESCRIPCION", Meses[Integer.parseInt(modelo.get("PERIODO").toString())-1]);
		
		Integer idGrupo=(Integer)modelo.get("ID_GRUPO");
		gatewayFirmasDocumentos.getFirmasDocumentos(idGrupo, modelo);
		List <Map> detalles = gatewayDevolucionesPresupuestales.getDetallesDevolucion(idDevolucion);
		modelo.put("rs",new JRMapCollectionDataSource(detalles));
		
		return new ModelAndView("rpt_devolucion_presupuestal",modelo);
	}
	
	
}
