package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.math.BigDecimal;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPedidos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import mx.gob.municipio.centro.view.componentes.RMCantidadEnLetras;

@Controller
@RequestMapping("/sam/reportes/rpt_pedido.action")
public class ControladorReportePedido extends ControladorBase {

	public ControladorReportePedido(){}
	
	@Autowired
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	@Autowired
	GatewayPedidos gatewayPedidos;
	@Autowired
	GatewayProyectoPartidas gatewayProyectoPartidas;
	@Autowired
	private RMCantidadEnLetras rmCantidadEnLetras;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView  requestGetControlador( Map modelo, @RequestParam("clavePedido")  Long  cve_ped ) {
		//Integra todos los elementos principales de pedido
		modelo = gatewayPedidos.getPedido(cve_ped);
		BigDecimal total = (BigDecimal) (modelo.get("TOTAL"));
		//double ttotal = redondea(total.doubleValue(),3);
		modelo.put("TOTAL_T", total.doubleValue());
		//Extrae la inf. de partida presupuestal
		modelo.putAll(gatewayProyectoPartidas.getLightPartidaPresupuestal(modelo.get("ID_PROYECTO").toString(), modelo.get("CLV_PARTID").toString()));
		//Obtenemos el grupo de firmas del documento
		Integer idGrupo=(Integer)modelo.get("ID_GRUPO");
		gatewayFirmasDocumentos.getFirmasDocumentos(idGrupo, modelo);
		//Extrae los detalles del pedido
		modelo.put("rs",new JRMapCollectionDataSource(gatewayPedidos.getConceptos(cve_ped)));
		//Configura la cantidad total en letras
		String cantidadLetra = convertirALetras(new BigDecimal(total.doubleValue()));
		//String cantidadLetra = convertirALetras((BigDecimal) modelo.get("TOTAL_T"));
		modelo.put("CANT_LETRA", cantidadLetra);
		return new ModelAndView("rpt_pedido",modelo);
	}		
	
	public double redondea(Double numero, int decimales) 
	{ 
	  Double resultado;
	  BigDecimal res;

	  res = new BigDecimal(numero).setScale(decimales, BigDecimal.ROUND_HALF_UP);
	  resultado = res.doubleValue();
	  return resultado; 
	}  
	
	/*Metodo usado para convertir una cantidad en letras*/
	/*private String convertirALetras(BigDecimal numero) {
		return rmCantidadEnLetras.convertir(numero.doubleValue())[0];
	}*/
}


