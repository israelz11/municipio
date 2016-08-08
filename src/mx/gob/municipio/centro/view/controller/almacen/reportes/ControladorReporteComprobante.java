/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.almacen.reportes;

import java.util.Map;

import mx.gob.municipio.centro.model.gateways.almacen.GatewaySalidas;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/almacen/reportes/comprobante.action")
public class ControladorReporteComprobante extends ControladorBaseAlmacen {

	public ControladorReporteComprobante(){		
	}		
	@Autowired
	GatewaySalidas gatewaySalidas;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	public ModelAndView  requestGetControlador( Map modelo, @RequestParam("idSalida")  Long  idSalida ) {		
		modelo = gatewaySalidas.getSolicitud(idSalida);
		modelo.put("UNIDAD",this.getSesion().getUnidad());
		modelo.put("rs",new JRMapCollectionDataSource(gatewaySalidas.getDetallesSalida(idSalida)));
		return new ModelAndView("comprobante",modelo);		
	}		
	
}
