package mx.gob.municipio.centro.view.controller.almacen.reportes;

import java.util.Map;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayEntradasDocumentos;
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
@RequestMapping("/almacen/reportes/entradas.action")
public class ControladorReporteEntrada extends ControladorBaseAlmacen {

	
	public ControladorReporteEntrada() {
		
	}
	
	@Autowired
	GatewayEntradasDocumentos gatewayEntradasDocumentos;
	
	@Autowired
	GatewayProyectoPartidas gatewayProyectoPartidas;
	
	@Autowired
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView requestGetControlador( Map modelo, @RequestParam("ID_ENTRADA") Long id_entrada ) {		
		modelo = gatewayEntradasDocumentos.getEntradaDocumento(id_entrada);
		//modelo.put("UNIDAD",this.getSesion().getUnidad());
		modelo.put("DESC_PROY", gatewayProyectoPartidas.getProyectoCompleto(modelo.get("ID_PROYECTO").toString()));
		modelo.put("DESC_PART", gatewayProyectoPartidas.getPartidaCompleta(modelo.get("PARTIDA").toString()));
		modelo.put("TITULO", ((modelo.get("ALMACEN")==null) ? "10 Dirección de Administración": (String)this.getJdbcTemplate().queryForObject("SELECT CLV_UNIADM + ' ' + DEPENDENCIA AS DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID =?", new Object[]{modelo.get("ID_DEPENDENCIA")}, String.class)));
		modelo.put("SUBTITULO", ((modelo.get("ALMACEN")==null) ? "Subdirección de Adquisiciones": ""));
			
		int idGrupo = Integer.parseInt(modelo.get("ID_GRUPO").toString());
				
		if(idGrupo!=0)
			gatewayFirmasDocumentos.getFirmasDocumentos(idGrupo, modelo);
		modelo.put("rs",new JRMapCollectionDataSource(gatewayEntradasDocumentos.getConceptos(id_entrada)));
		
		if(this.getPrivilegioEn(this.getSesion().getIdUsuario(), 126))
			return new ModelAndView("entradas_3Digitos",modelo);
		else
			return new ModelAndView("entradas",modelo);
	}

}
