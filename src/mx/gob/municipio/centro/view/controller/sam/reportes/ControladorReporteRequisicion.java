/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.util.Map;

import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayMovimientosRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sam/reportes/requisicion.action")
public class ControladorReporteRequisicion extends ControladorBase {

	public ControladorReporteRequisicion(){		
	}		
	
	@Autowired
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	@Autowired
	GatewayRequisicion gatewayRequisicion;
	@Autowired
	GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
	@Autowired
	GatewayProyectoPartidas gatewayProyectoPartidas;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	public ModelAndView  requestGetControlador( Map modelo, @RequestParam("claveRequisicion")  Long  idRequisicion ) {
		String[] Meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
		modelo = gatewayRequisicion.getRequisicion(idRequisicion);
		modelo.put("CALENDARIO", Meses[Integer.parseInt(modelo.get("PERIODO").toString())-1]);
		modelo.put("PRESUPUESTO_DISPONIBLE", gatewayProyectoPartidas.getDisponibleMes(Integer.parseInt(modelo.get("PERIODO").toString()), Long.parseLong(modelo.get("ID_PROYECTO").toString()), modelo.get("CLV_PARTID").toString()));
		
		Integer idGrupo=(Integer)modelo.get("ID_GRUPO");
		Integer tipoReq=Integer.parseInt((modelo.get("TIPO").toString()));
		
		modelo.put("rs",new JRMapCollectionDataSource(gatewayMovimientosRequisicion.getConceptos(idRequisicion)));
		
		gatewayFirmasDocumentos.getFirmasDocumentos(idGrupo, modelo);

		if(tipoReq.equals(gatewayRequisicion.REQ_TIPO_SERVICIOS_VEHICULO)||tipoReq.equals(gatewayRequisicion.REQ_TIPO_MAQUINARIA_PESADA))
			return new ModelAndView(modelo.get("STATUS").toString().equals("0") ? "ordenTrabajo2": "ordenTrabajo",modelo);
		else	
		if(tipoReq.equals(gatewayRequisicion.REQ_TIPO_SERVICIOS) || tipoReq.equals(gatewayRequisicion.REQ_TIPO_SERVICIO_BOMBAS) )				
			return new ModelAndView(modelo.get("STATUS").toString().equals("0") ? "ordenServicio2": "ordenServicio",modelo);
		else if(tipoReq.equals(gatewayRequisicion.REQ_TIPO_OS_CALENDARIZADA)){
			return new ModelAndView(modelo.get("STATUS").toString().equals("0") ? "ordenServicio2": "ordenServicio",modelo);
			//return new ModelAndView("ordenServicio",modelo);
		}
		else
			return new ModelAndView("requisicion",modelo);
	}		
	
}
