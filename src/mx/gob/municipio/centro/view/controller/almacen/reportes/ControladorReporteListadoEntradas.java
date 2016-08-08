package mx.gob.municipio.centro.view.controller.almacen.reportes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayEntradasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBeneficiario;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/almacen/reportes/rpt_listado_entrada.action")
public class ControladorReporteListadoEntradas extends ControladorBaseAlmacen {

	@Autowired
	GatewayEntradasDocumentos gatewayEntradasDocumentos;
	
	@Autowired
	GatewayProyectoPartidas gatewayProyectoPartidas;
	
	@Autowired
	GatewayBeneficiario gatewayBeneficiario;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView requestGetControlador( Map modelo, HttpServletRequest request) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		String fecha = dateFormat.format(date); 
		
		modelo.put("id_entrada", (request.getParameter("id_entrada")==null ? 0: request.getParameter("id_entrada")));
		modelo.put("cbodependencia", request.getParameter("cbodependencia")==null ? 0: request.getParameter("cbodependencia"));
		modelo.put("id_almacen", request.getParameter("cboalmacen")==null ? 0: request.getParameter("cboalmacen"));
		modelo.put("id_tipo_documento", request.getParameter("cbotipodocumento")==null ? 0: request.getParameter("cbotipodocumento"));
		modelo.put("id_proveedor", request.getParameter("ID_PROVEEDOR")==null ? 0: request.getParameter("ID_PROVEEDOR"));
		modelo.put("proveedor", gatewayBeneficiario.getBeneficiario2(Long.parseLong(modelo.get("id_proveedor").toString())));
		modelo.put("id_pedido", request.getParameter("txtpedido")==null ? "": request.getParameter("txtpedido"));
		modelo.put("fechaInicial", request.getParameter("txtfechaInicial")==null ? "": request.getParameter("txtfechaInicial"));
		modelo.put("fechaFinal", request.getParameter("txtfechaFinal")==null ? "": request.getParameter("txtfechaFinal"));
		modelo.put("proyecto", request.getParameter("txtproyecto")==null ? "": request.getParameter("txtproyecto"));
		modelo.put("partida", request.getParameter("partida")==null ? "": request.getParameter("txtpartida"));
		modelo.put("num_documento", request.getParameter("txtdocumento")==null ? "": request.getParameter("txtdocumento"));
		modelo.put("folio", request.getParameter("txtfolio")==null ? "": request.getParameter("txtfolio"));
		
		modelo.put("UNIDAD_SOLICITANTE",this.getSesion().getUnidad());
		modelo.put("FECHA_IMPRESION", fecha);
		modelo.put("rs",new JRMapCollectionDataSource(gatewayEntradasDocumentos.getListadoDocumentos(modelo)));
		
		return new ModelAndView("rpt_listado_entradas", modelo);
		
	}

}
