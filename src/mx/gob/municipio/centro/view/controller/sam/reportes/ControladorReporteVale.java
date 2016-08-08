/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayVales;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import mx.gob.municipio.centro.view.componentes.RMCantidadEnLetras;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sam/reportes/formato_vale.action")
public class ControladorReporteVale extends ControladorBase {

	public ControladorReporteVale(){		
	}		
	
	@Autowired
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	@Autowired
	GatewayVales gatewayVales;
	@Autowired
	private RMCantidadEnLetras rmCantidadEnLetras;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	public ModelAndView  requestGetControlador( Map modelo, @RequestParam("cve_val")  Long  idVale ) {		
		String[] Meses = {"ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
		modelo = gatewayVales.getValeTodosDatos(idVale);
		Integer idGrupo=(Integer)modelo.get("ID_GRUPO");
		String cantidadLetra = convertirALetras((BigDecimal)modelo.get("TOTAL"));
		modelo.put("IMPORTE_LETRAS", cantidadLetra);
		modelo.put("PERIODO", Meses[Integer.parseInt(modelo.get("MES").toString())-1]);
		Md5PasswordEncoder codigo = new Md5PasswordEncoder();
		String encriptado = codigo.encodePassword(modelo.get("NUM_VALE").toString()+modelo.get("ID_DEPENDENCIA").toString()+modelo.get("FECHA").toString()+modelo.get("CLV_BENEFI").toString()+modelo.get("ID_RECURSO").toString()+modelo.get("TOTAL").toString(), null );
		modelo.put("MD5", encriptado);

		gatewayFirmasDocumentos.getFirmasDocumentos(idGrupo, modelo);		

		modelo.put("rs",new JRMapCollectionDataSource(gatewayVales.getDetallesVales(idVale)));
		modelo.put("MD5", encriptado);
		return new ModelAndView("formato_vale",modelo);
	}		
	
	/*private String convertirALetras(BigDecimal numero) {
		return rmCantidadEnLetras.convertir(numero.doubleValue())[0];
	}*/
	
}
