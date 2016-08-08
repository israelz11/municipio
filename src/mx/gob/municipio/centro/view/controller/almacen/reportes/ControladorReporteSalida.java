package mx.gob.municipio.centro.view.controller.almacen.reportes;

import java.util.Map;

import mx.gob.municipio.centro.model.gateways.almacen.GatewaySalidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
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
@RequestMapping("/almacen/reportes/salidas.action")

public class ControladorReporteSalida extends ControladorBaseAlmacen {

public ControladorReporteSalida() {
		
	}
	
	@Autowired
	GatewaySalidas gatewaySalidas;
	
	@Autowired
	GatewayProyectoPartidas gatewayProyectoPartidas;
	
	@Autowired
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView requestGetControlador( Map modelo, @RequestParam("ID_SALIDA") Long id_salida ) {		
		modelo = gatewaySalidas.getSalida(id_salida);
		int idGrupo = Integer.parseInt(modelo.get("ID_GRUPO").toString());
		if(idGrupo!=0)
			gatewayFirmasDocumentos.getFirmasDocumentos(idGrupo, modelo);
		
		modelo.put("DESC_PART", gatewayProyectoPartidas.getPartidaCompleta(modelo.get("PARTIDA").toString()));
		modelo.put("TITULO", ((modelo.get("ALMACEN")==null) ? "10 Dirección de Administración": (String)this.getJdbcTemplate().queryForObject("SELECT CLV_UNIADM + ' ' + DEPENDENCIA AS DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID =?", new Object[]{modelo.get("ID_DEPENDENCIA")}, String.class)));
		modelo.put("rs",new JRMapCollectionDataSource(gatewaySalidas.getDetallesSalida(id_salida)));
		
		if(this.getPrivilegioEn(this.getSesion().getIdUsuario(), 126))
			return new ModelAndView("salidas_3Digitos",modelo);	
		else
			return new ModelAndView("salidas",modelo);
	}

}
