package mx.gob.municipio.centro.view.controller.almacen.salidas;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import mx.gob.municipio.centro.model.gateways.almacen.GatewayPartidasAgregadas;
import mx.gob.municipio.centro.model.gateways.almacen.GatewaySalidas;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayUsuariosAlmacen;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/almacen/salidas/autorizacion.action")

public class ControladorAutorizacion extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorAutorizacion.class.getName());

	@Autowired
	GatewayPartidasAgregadas gatewayPartidasAgregadas;
	
	@Autowired
	GatewayUsuariosAlmacen gatewayUsuariosAlmacen;
	
	@Autowired
	GatewaySalidas gatewaySalidas;
	
	public ControladorAutorizacion() {		
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
	    	return "almacen/salidas/autorizacion.jsp";    	
	}
		
	public List  getSolicitudesPorEstatus(Integer idAlmacen) {
	  return gatewaySalidas.getSolicitudesPorEstatus(idAlmacen, "ENVIADO");
	}
		     
	public List  getArticulosSalida( Long idSalida) {		
		return this.getJdbcTemplate().queryForList("SELECT   f.ID_DETALLE_SALIDA, F.SURTIDO, a.ID_INVENTARIO, c.DESCRIPCION, e.UNIDMEDIDA, a.FOLIO, f.CANTIDAD AS SOLICITADO, a.PRECIO, a.CANTIDAD AS EXISTENCIA, ISNULL "+
				" ((SELECT     SUM(dbo.DETALLE_SALIDA.SURTIDO)  "+
                " FROM         dbo.DETALLE_SALIDA INNER JOIN "+
                " dbo.SALIDAS ON dbo.DETALLE_SALIDA.ID_SALIDA = dbo.SALIDAS.ID_SALIDA "+
                " WHERE     (dbo.DETALLE_SALIDA.ID_INVENTARIO = a.ID_INVENTARIO) AND (dbo.SALIDAS.ESTATUS = 'AUTORIZADO')), 0) AS AUTORIZADO, "+ 
                " ISNULL((SELECT     SUM(DETALLE_SALIDA_3.CANTIDAD)  "+
                " FROM         dbo.DETALLE_SALIDA AS DETALLE_SALIDA_3 INNER JOIN "+
                " dbo.SALIDAS AS SALIDAS_3 ON DETALLE_SALIDA_3.ID_SALIDA = SALIDAS_3.ID_SALIDA "+
                " WHERE     (DETALLE_SALIDA_3.ID_INVENTARIO = a.ID_INVENTARIO) AND (SALIDAS_3.ESTATUS = 'ENVIADO') and SALIDAS_3.ID_SALIDA!=f.ID_SALIDA ), 0) AS ENVIADO, "+
                " (SELECT     COUNT(ID_SALIDA) "+
        		" FROM          (SELECT DISTINCT SALIDAS_2.ID_SALIDA "+
                " FROM          dbo.DETALLE_SALIDA AS DETALLE_SALIDA_2 INNER JOIN "+
                " dbo.SALIDAS AS SALIDAS_2 ON DETALLE_SALIDA_2.ID_SALIDA = SALIDAS_2.ID_SALIDA "+
                " WHERE      (DETALLE_SALIDA_2.ID_INVENTARIO = a.ID_INVENTARIO) AND (SALIDAS_2.ESTATUS = 'ENVIADO') and SALIDAS_2.ID_SALIDA!=f.ID_SALIDA ) AS SAL) AS NO_PEDIDOS, "+ 
                " ISNULL ((SELECT     SUM(DETALLE_SALIDA_1.SURTIDO)  "+
                " FROM dbo.DETALLE_SALIDA AS DETALLE_SALIDA_1 INNER JOIN "+
                " dbo.SALIDAS AS SALIDAS_1 ON DETALLE_SALIDA_1.ID_SALIDA = SALIDAS_1.ID_SALIDA "+
				" WHERE     (DETALLE_SALIDA_1.ID_INVENTARIO = a.ID_INVENTARIO) AND (SALIDAS_1.ESTATUS = 'ENTREGADO') AND "+ 
                " (SALIDAS_1.ID_PERSONA_SOLICITA = G.ID_PERSONA_SOLICITA)), 0) AS ENTREGADOS "+
				" FROM         DETALLE_SALIDA AS f INNER JOIN "+
				" INVENTARIO AS a INNER JOIN "+
                " CAT_PROD AS b ON a.ID_ARTICULO = b.ID_ARTICULO INNER JOIN "+
                " SAM_CAT_ARTICULO AS c ON b.ID_CAT_ARTICULO = c.ID_CAT_ARTICULO INNER JOIN "+
                " CAT_PARTID AS d ON b.GRUPO = d.CLV_PARTID INNER JOIN "+
                " CAT_UNIMED AS e ON a.ID_UNIDAD_MEDIDA = e.CLV_UNIMED ON f.ID_INVENTARIO = a.ID_INVENTARIO INNER JOIN "+
                " SALIDAS AS G ON f.ID_SALIDA = G.ID_SALIDA "+
				" WHERE     f.ID_SALIDA = ? ",new Object []{idSalida} );
		
		}
	

				
	public void  guardarSolicitudAutoricion( final List<Map<String,String>> detallesSolicitudes, final String estatus, final Long idSalida ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                	getJdbcTemplate().update("update SALIDAS set ESTATUS=?, ID_PERSONA_ENTREGA=?,FECHA_ENTREGA=getdate() where  ID_SALIDA=? ", new Object[]{estatus,getSesion().getIdUsuario(),idSalida});
	                	for (Map<String,String> articulo :detallesSolicitudes){
	                		Long idDetalleSolicitud = !(articulo.get("idArt")).equals("") ? Long.parseLong(articulo.get("idArt")) : null ;
     	  				    Double cantidad = !(articulo.get("cantidad")).equals("") ? Double.parseDouble(articulo.get("cantidad")): 0;
	                		getJdbcTemplate().update("update DETALLE_SALIDA set SURTIDO=? where  ID_DETALLE_SALIDA=? ", new Object[]{cantidad,idDetalleSolicitud});
	                	}
	                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }	
	
}
