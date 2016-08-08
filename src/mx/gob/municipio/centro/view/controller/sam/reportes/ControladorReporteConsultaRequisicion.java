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

import mx.gob.municipio.centro.model.gateways.sam.GatewayMovimientosRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/reportes/rpt_InformeRequisicion.action")
public class ControladorReporteConsultaRequisicion extends ControladorBase {
	
	public ControladorReporteConsultaRequisicion(){
		
	}
	
	@Autowired
	GatewayRequisicion gatewayRequisicion;
	@Autowired
	GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	public ModelAndView  requestGetControlador( Map modelo, @RequestParam("claveRequisicion")  Long  cve_req ) {
		modelo = gatewayRequisicion.getRequisicion(cve_req);
		modelo.put("rs",new JRMapCollectionDataSource(gatewayMovimientosRequisicion.getConceptos(cve_req)));
		Integer tipoReq=Integer.parseInt((modelo.get("TIPO").toString()));
		BigDecimal importe = ((BigDecimal)modelo.get("TOTAL_REQ"));
		String cantidadLetra = convertirALetras(importe);
		
		modelo.put("IMPORTELETRAS", cantidadLetra.substring(1, cantidadLetra.length()-1));
		
		if(tipoReq.equals(gatewayRequisicion.REQ_TIPO_SERVICIOS_VEHICULO)||tipoReq.equals(gatewayRequisicion.REQ_TIPO_MAQUINARIA_PESADA))
			modelo.put("TIPO_DOC", "O.T");
		else	
		if(tipoReq.equals(gatewayRequisicion.REQ_TIPO_SERVICIOS) || tipoReq.equals(gatewayRequisicion.REQ_TIPO_SERVICIO_BOMBAS) )				
			modelo.put("TIPO_DOC", "O.S");
		else if(tipoReq.equals(gatewayRequisicion.REQ_TIPO_OS_CALENDARIZADA)){
			modelo.put("TIPO_DOC", "O.S");
		}
		else
			modelo.put("TIPO_DOC", "REQ.");
		
		return new ModelAndView("rpt_InformeRequisicion",modelo);
	}

}
