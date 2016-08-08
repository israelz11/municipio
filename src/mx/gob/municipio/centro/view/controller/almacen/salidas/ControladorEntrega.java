package mx.gob.municipio.centro.view.controller.almacen.salidas;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import mx.gob.municipio.centro.model.gateways.almacen.GatewayPartidasAgregadas;
import mx.gob.municipio.centro.model.gateways.almacen.GatewaySalidas;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayUsuariosAlmacen;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

import org.aspectj.util.LangUtil.ProcessController.Thrown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/almacen/salidas/entrega.action")

public class ControladorEntrega extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorEntrega.class.getName());

	@Autowired
	GatewayPartidasAgregadas gatewayPartidasAgregadas;
	
	@Autowired
	GatewayUsuariosAlmacen gatewayUsuariosAlmacen;
	
	@Autowired
	GatewaySalidas gatewaySalidas;
	
	public ControladorEntrega() {		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method =  RequestMethod.GET)    
	public String  requestGetControladorGet( Map modelo ) {
		List almacenes =gatewayUsuariosAlmacen.getAlmacenesUsuario(this.getSesion().getIdUsuario(), this.getSesion().getClaveUnidad()); 
		modelo.put("almacenes",almacenes);
		modelo.put("noAlmacenes",almacenes.size());
		if (almacenes.size() == 0 )
	        return "almacen/salidas/sin_almacen.jsp";
	    else
	    	return "almacen/salidas/entrega.jsp";    	
	}
		
	public List  getSolicitudesPorEstatus(Integer idAlmacen) {
	  return gatewaySalidas.getSolicitudesPorEstatus(idAlmacen, "AUTORIZADO");
	}
		     
	public List  getArticulosEntrega( Long idSalida) {
		return gatewaySalidas.getDetallesSalida(idSalida);
	 }
	
	public boolean  guardarSolicitudEntregada(  final String estatus, final Long idSalida ) {
		boolean exito=true;
		try {         
			final List<Map> articulos = gatewaySalidas.getDetallesSalida(idSalida);			
			if (estatus.equals("ENTREGADO")){
				for (Map articulo: articulos) {
					BigDecimal surtido=(BigDecimal)articulo.get("SURTIDO");
					BigDecimal existencia=(BigDecimal)articulo.get("EXISTENCIA");
					if (surtido.doubleValue() > existencia.doubleValue() &&  exito )
						exito=false;
				}				
			}
			if(exito)
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                	if (estatus.equals("ENTREGADO")){                	 
                	  for (Map articulo: articulos) {
                		  getJdbcTemplate().update("update INVENTARIO  set CANTIDAD=CANTIDAD-?  where  ID_INVENTARIO=? ", new Object[]{articulo.get("SURTIDO"),articulo.get("ID_INVENTARIO")});
                	  }
                	}                	
                getJdbcTemplate().update("update SALIDAS set ESTATUS=?,ID_PERSONA_ENTREGA=?,FECHA_ENTREGA=getdate()  where  ID_SALIDA=? ", new Object[]{estatus,getSesion().getIdUsuario(),idSalida});                                
                } });
        } catch (DataAccessException e) {            
            log.info("Los registros tienen relaciones con otras tablas ");	                    
            throw new RuntimeException(e.getMessage(),e);            
        }	                	                		  	  
	return exito; 
	  }	
	
}
