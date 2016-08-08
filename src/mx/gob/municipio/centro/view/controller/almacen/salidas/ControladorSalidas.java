package mx.gob.municipio.centro.view.controller.almacen.salidas;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayEntradasDocumentos;
import mx.gob.municipio.centro.model.gateways.almacen.GatewaySalidas;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBeneficiario;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/almacen/salidas/salidas.action")
public class ControladorSalidas extends ControladorBaseAlmacen {

	@Autowired
	GatewayTiposDocumentos gatewayTiposDocumentos;
	
	@Autowired
	GatewayAlmacen gatewayAlmacen;
	
	@Autowired
	GatewayBeneficiario gatewayBeneficiario;
	
	@Autowired
	GatewayEntradasDocumentos gatewayEntradasDocumentos;
	
	@Autowired
	GatewaySalidas gatewaySalidas;
	
	public ControladorSalidas() {
		// TODO Auto-generated method stub

	}
	
	Long id = 0l;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Long id_entrada = request.getParameter("id_entrada")==null ? 0L: Long.parseLong(request.getParameter("id_entrada").toString());
		Long id_salida = request.getParameter("id_salida")==null ? 0L: Long.parseLong(request.getParameter("id_salida").toString());
		
		modelo.put("datos", this.gatewayEntradasDocumentos.getEntradaDocumento(id_entrada));
		if(id_salida!=0){
			Map salida = gatewaySalidas.getSalida(id_salida);
			modelo.put("TIPO_SALIDA", salida.get("ID_TIPO_SALIDA"));
			modelo.put("ID_SALIDA",id_salida);
			modelo.put("FOLIO",salida.get("FOLIO"));
			modelo.put("FECHA_ENTREGA", salida.get("FECHA_ENTREGA"));
			modelo.put("CONCEPTO", salida.get("CONCEPTO"));
			modelo.put("TIPO_IVA", salida.get("TIPO_IVA"));
			modelo.put("DESCUENTO", salida.get("DESCUENTO"));
			modelo.put("IVA", salida.get("IVA"));
			modelo.put("TOTAL", salida.get("TOTAL"));

		}
		modelo.put("tiposSalidas", this.getTiposEntradas());
	    return "almacen/salidas/salidas.jsp";
	}
	
	public Long guardarSalida(final Long id_salida,final Long id_entrada,final String fecha,final String concepto, final int tipo_salida, Double subtotal, Double descuento, Double iva, int tipoIva){
        return gatewaySalidas.guardarSalida(id_salida, id_entrada, fecha, concepto,  this.getSesion().getIdUsuario(), tipo_salida, subtotal, descuento, iva, tipoIva);
        
	}
	
	public List<Map> getTiposEntradas(){
    	return gatewayTiposDocumentos.getTiposEntradas();
	}
	
	public List<Map> getConceptos(Long idSalida){
		return  gatewaySalidas.gatewaySalidas(idSalida);
	}
	
	public void guardarCantidadDetalles(Long id_entrada, Long[] ids, Double[] cantidades, Double subtotal, Double descuento, Double iva, int tipoIva){
		this.gatewaySalidas.guardarCantidadDetalles(id_entrada, ids, cantidades, subtotal, descuento, iva, tipoIva);
	}
	
	public void cerrarSalida(Long idSalida){
		this.gatewaySalidas.cerrarSalida(idSalida);
	}
	
	public void eliminarConceptos(final List<Long> lst_id_detalle){
		for (Long id :lst_id_detalle) 
			this.gatewaySalidas.eliminarConceptos(id);
	}

}
