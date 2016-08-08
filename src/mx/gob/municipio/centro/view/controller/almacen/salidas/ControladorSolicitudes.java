package mx.gob.municipio.centro.view.controller.almacen.salidas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayPartidasAgregadas;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayUsuariosAlmacen;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/almacen/salidas/solicitudes.action")

public class ControladorSolicitudes extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorSolicitudes.class.getName());

	@Autowired
	GatewayPartidasAgregadas gatewayPartidasAgregadas;
	
	@Autowired
	GatewayAlmacen gatewayAlmacen;
	
	@Autowired
	GatewayUsuariosAlmacen gatewayUsuariosAlmacen;
	public ControladorSolicitudes() {		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method =  RequestMethod.GET)    
	public String  requestGetControladorGet( Map modelo ) {
		modelo.put("partidasAsignadas",gatewayPartidasAgregadas.getPartidasAgregadas());
		List almacenes =gatewayAlmacen.getAlmacenesActivos(Integer.parseInt(this.getSesion().getIdUnidad())); 
		modelo.put("almacenes",almacenes);
		modelo.put("noAlmacenes",almacenes.size());
		if (almacenes.size() == 0 )
	        return "almacen/salidas/sin_almacen.jsp";
	    else
	    	return "almacen/salidas/solicitudes.jsp";
	}
		
	public List  getSolicitudesPendientes(Integer idAlmacen) {
	  return this.getJdbcTemplate().queryForList("SELECT  ID_SALIDA,   CONCEPTO, CLV_PARTID, ID_PERSONA_SOLICITA "+
			  " FROM         SALIDAS "+
			  " WHERE     ESTATUS = 'PENDIENTE' AND ID_ALMACEN = ?  AND ID_PERSONA_SOLICITA = ?  ",new Object []{idAlmacen,this.getSesion().getIdUsuario()} );
	}
	
	public List  getArticulosPartida(Integer idAlmacen, String partida, Integer idSalida) {		
		return this.getJdbcTemplate().queryForList("SELECT     a.ID_INVENTARIO, c.DESCRIPCION, e.UNIDMEDIDA, a.FOLIO, f.CANTIDAD,a.PRECIO "+
				" FROM         (SELECT     ID_INVENTARIO, CANTIDAD FROM          dbo.DETALLE_SALIDA  WHERE  ID_SALIDA =? ) AS f RIGHT OUTER JOIN "+
				" INVENTARIO AS a INNER JOIN "+
				" SAM_CAT_PROD AS b ON a.ID_ARTICULO = b.ID_ARTICULO INNER JOIN "+
				" SAM_CAT_ARTICULO AS c ON b.ID_CAT_ARTICULO = c.ID_CAT_ARTICULO INNER JOIN "+
				" CAT_PARTID AS d ON b.GRUPO = d.CLV_PARTID INNER JOIN "+
				" CAT_UNIMED AS e ON a.ID_UNIDAD_MEDIDA = e.CLV_UNIMED ON f.ID_INVENTARIO = a.ID_INVENTARIO "+
				" WHERE     (b.GRUPO = ?) AND (a.ID_ALMACEN = ?) ",new Object []{idSalida,partida,idAlmacen} );
		
		}
	
	private Long getFolioSalida(Integer  idAlmacen){
        this.getJdbcTemplate().update("update ALMACEN set FOLIO=FOLIO+1 where  ID_ALMACEN=? ", new Object[]{idAlmacen});
        return this.getJdbcTemplate().queryForLong(" select FOLIO from ALMACEN where ID_ALMACEN=? ",new Object[]{idAlmacen});
	}
	
	
	private Long guardaEncabezadoSalida(final String concepto, final String estatus,final Integer  idAlmacen, final String partida,final Integer idUsuario,final Long folio){		
		KeyHolder keyHolder = new GeneratedKeyHolder();
    	Long key_orden=null;
    	final String INSERT_SQL ="INSERT INTO SALIDAS (ID_PERSONA_SOLICITA,FOLIO,CONCEPTO,ESTATUS,ID_ALMACEN,CLV_PARTID)  VALUES (?,?,?,?,?,?)";
        try{ 
            this.getJdbcTemplate().update(
                             new PreparedStatementCreator() {
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                             PreparedStatement ps =
                                 connection.prepareStatement(INSERT_SQL, new String[]{ "ID_SALIDA"} );
                                         ps.setInt( 1, idUsuario);
                                         if (folio==null)
                                        	 ps.setNull(2,Types.INTEGER);
                                         else
                                         ps.setLong( 2,folio );
                                         ps.setString( 3, concepto );                                         
                                         ps.setString(4, estatus);                                           
                                         ps.setInt(5, idAlmacen);
                                         ps.setString(6, partida);                                                       
                                 return ps;
                            }
                     },
            keyHolder);
            key_orden = new Long(keyHolder.getKey().longValue());
        }catch( DataAccessException ex) {
        	log.info("Fallo la inserción del sql");
        	throw ex;                     
        }
        return key_orden;
	}
	
	public String guardarSolicitud( final List<Map<String,String>> articulos, final Integer idAlmacen ,final  String partida, final String concepto ,final  String tipo,final Long  idSalida ) {		 
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {
	                	Integer idUsuario=getSesion().getIdUsuario();		
	            		Long folio =null;
	            		if (tipo.equals("ENVIADO")){
	            			folio =getFolioSalida(idAlmacen);
	            		}	            		
	                	Long idSalidaLocal =idSalida;
	                	if (idSalida==null)
	                		idSalidaLocal =guardaEncabezadoSalida(concepto, tipo,idAlmacen, partida,idUsuario,folio) ;
	                	else {
	                		getJdbcTemplate().update("update SALIDAS set FOLIO=?,CONCEPTO=?,ESTATUS=?,FECHA=getdate()  where  ID_SALIDA=? ", new Object[]{folio,concepto,tipo,idSalidaLocal});
	                		getJdbcTemplate().update("delete from  DETALLE_SALIDA where ID_SALIDA=? ", new Object[]{idSalidaLocal});
	                	}
	                		                		
	                	for (Map<String,String> articulo :articulos ){	                		
         	  				    Long idArt = !(articulo.get("idArt")).equals("") ? Long.parseLong(articulo.get("idArt")) : null ;
         	  				    Double cantidad = !(articulo.get("cantidad")).equals("") ? Double.parseDouble(articulo.get("cantidad")): 0;         	  				             	  				           
         	  				    Double precio = !(articulo.get("precio")).equals("") ? Double.parseDouble(articulo.get("precio")): 0;
         	  				    getJdbcTemplate().update("insert into  DETALLE_SALIDA (ID_SALIDA,ID_INVENTARIO,CANTIDAD,PRECIO) values (?,?,?,?) ", new Object []{idSalidaLocal,idArt,cantidad,precio});
         					   }	
	                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	
	                
	            return "";    
	  }
	
	public void  cancelarSolicitud( final List<Integer> solicitudes ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Integer solicitud :solicitudes)
	                		getJdbcTemplate().update("update SALIDAS set ESTATUS='CANCELADO',ID_PERSONA_ENTREGA=?,FECHA_ENTREGA=getdate() where ID_SALIDA=? ", new Object[]{getSesion().getIdUsuario(),solicitud});            			                	
	                } });
	                } catch (DataAccessException e) {            
	                    log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }	                	                		  	  
	  }
	
	
}
