package mx.gob.municipio.centro.view.controller.almacen.reportes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayFamiliasArticulos;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayInventario;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBeneficiario;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadMedidas;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/almacen/reportes/almacen.action")
public class ControladorReporteAlmacen extends ControladorBaseAlmacen {

	public ControladorReporteAlmacen() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	GatewayFamiliasArticulos gatewayFamiliasArticulos;
	
	@Autowired
	GatewayTiposDocumentos gatewayTiposDocumentos;
	
	@Autowired
	GatewayBeneficiario gatewayBeneficiario;
	
	@Autowired
	GatewayAlmacen gatewayAlmacen;
	
	@Autowired
	GatewayInventario gatewayInventario;
	
	@Autowired
	GatewayUnidadMedidas gatewayUnidadMedidas;
	
	@Autowired 
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView requestGetControlador( Map modelo, HttpServletRequest request ) {	
		//@RequestParam("ID_ENTRADA")
		Calendar calendario = GregorianCalendar.getInstance();
		Date fecha = calendario.getTime();
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
		//System.out.println(formatoDeFecha.format(fecha));
		
		Map modelo2 = new HashMap();
		modelo.put("cbodependencia", (request.getParameter("cbodependencia")==null ? 0: request.getParameter("cbodependencia")));
		modelo.put("id_almacen", request.getParameter("cboalmacen")==null ? 0: request.getParameter("cboalmacen"));
		modelo.put("id_familia", request.getParameter("cbofamilia")==null ? 0: request.getParameter("cbofamilia"));
		modelo.put("id_proveedor", request.getParameter("ID_PROVEEDOR")==null ? 0: request.getParameter("ID_PROVEEDOR"));
		modelo.put("id_unidad_medida", request.getParameter("ID_UNIDAD_MEDIDA")==null ? 0: request.getParameter("ID_UNIDAD_MEDIDA"));
		modelo.put("descripcion", request.getParameter("txtdescripcion")==null ? "": request.getParameter("txtdescripcion"));
		modelo.put("folio", request.getParameter("txtfolio")==null ? "": request.getParameter("txtfolio"));
		
		modelo2.put("DEPENDENCIA",(!modelo.get("cbodependencia").toString().equals("0")) ? gatewayUnidadAdm.getNombreUnidad(Integer.parseInt(modelo.get("cbodependencia").toString())) : this.getSesion().getUnidad());
		modelo2.put("FECHA_DOCUMENTO", formatoDeFecha.format(fecha));
		if(!modelo.get("id_almacen").toString().equals("0")){
			modelo2.put("ALMACEN", gatewayAlmacen.getAlmacen(Integer.parseInt(modelo.get("id_almacen").toString())));
		}
		
		modelo2.put("FAMILIA", gatewayFamiliasArticulos.getFamiliaCompleta(Integer.parseInt(modelo.get("id_familia").toString())));
		modelo2.put("PROVEEDOR", gatewayBeneficiario.getBeneficiario2(Long.parseLong(modelo.get("id_proveedor").toString())));
		modelo2.put("DESCRIPCION", modelo.get("descripcion").toString());
		modelo2.put("UNIDAD_MEDIDA", gatewayUnidadMedidas.getUnidadMedida(modelo.get("id_unidad_medida").toString()));
		modelo2.put("FOLIO", modelo.get("folio").toString());
		
		modelo2.put("rs",new JRMapCollectionDataSource(gatewayInventario.getListadoArticulosInventario(modelo)));
		return new ModelAndView("almacen",modelo2);		
	}

}
