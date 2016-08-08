/**
 * @author LSC. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.almacen.configuracion;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayPartidasAgregadas;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayCatalogoPartidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/administracion/asignacion_partidas.action")

public class ControladorAgregarPartidas extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorAgregarPartidas.class.getName());
	@Autowired	
	GatewayCatalogoPartidas gatewayCatalogoPartidas;
	@Autowired
	GatewayPartidasAgregadas gatewayPartidasAgregadas;
	
	public ControladorAgregarPartidas() {		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControladorGet( Map modelo ) {
		modelo.put("partidas", getPartidasTodas());
		modelo.put("partidasAsignadas",gatewayPartidasAgregadas.getPartidasAgregadas());		
	    return "almacen/administracion/asignacion_partidas.jsp";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)    
	public String  requestGetControlador( Map modelo,  HttpServletRequest request  ) {
		String  partida=  request.getParameter("partida");
		String  partidasAgregada=  request.getParameter("partidasagregada"); 
		String  accion=  request.getParameter("accion");  		
		if ("agregar".equals(accion)) 
			gatewayPartidasAgregadas.inserta(partida);			
		else 
		 if ("quitar".equals(accion))
		 	gatewayPartidasAgregadas.eliminar(partidasAgregada);		
		modelo.put("mensaje",true);
		modelo.put("partidas", getPartidasTodas());
		modelo.put("partidasAsignadas",gatewayPartidasAgregadas.getPartidasAgregadas());		
	    return "almacen/administracion/asignacion_partidas.jsp";
	}
	public List  getPartidasTodas() {
	  return this.getJdbcTemplate().queryForList("Select ID, clv_partid CLAVE, partida DESCRIPCION from CAT_PARTID where clv_partid not in (select CLV_PARTID from PARTIDASAGREGADAS) ");
	}	
	
}
