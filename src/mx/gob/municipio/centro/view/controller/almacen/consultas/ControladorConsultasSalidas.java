/**
 * @author ISC. Israel de la Cruz Hernández.
 * @version: 1.0
 * @date: 15-July-2010
 * **/
package mx.gob.municipio.centro.view.controller.almacen.consultas;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import mx.gob.municipio.centro.model.gateways.almacen.GatewaySalidas;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayUsuariosAlmacen;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;
import mx.gob.municipio.centro.view.controller.almacen.salidas.ControladorAutorizacion;

@Controller
@RequestMapping("/almacen/consultas/salidas.action")
public class ControladorConsultasSalidas extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorConsultasSalidas.class.getName());
	@Autowired
	GatewaySalidas gatewaySalidas;
	
	@Autowired
	GatewayUsuariosAlmacen gatewayUsuariosAlmacen;
	
	public ControladorConsultasSalidas() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET) 
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {		
		modelo.put("ejercicios",gatewaySalidas.getEjercicios( ) );
		modelo.put("estatus", gatewaySalidas.getEstatusSalida());	
		List almacenes =gatewayUsuariosAlmacen.getAlmacenesUsuario(this.getSesion().getIdUsuario(), this.getSesion().getClaveUnidad()); 
		modelo.put("almacenes",almacenes);
		modelo.put("noAlmacenes",almacenes.size());
		if (almacenes.size() == 0 )
	        return "almacen/salidas/sin_almacen.jsp";
	    else
	    	return "almacen/consultas/salidas.jsp";
	}
	public List getSalidasPorEjemplo(Long idAlmacen, Integer folio, Integer  year, String estatus , String tipo  ){
		Map parametros = new HashMap();
		parametros.put("idAlmacen",idAlmacen );
		parametros.put("folio", folio);
		parametros.put("year", year);
		parametros.put("estatus",estatus );
		parametros.put("idUsuario",this.getSesion().getIdUsuario() );
		
		String sql="";
		if (folio != null )
			sql = sql +" and folio= :folio ";
		if (estatus!=null && !"".equals(estatus) && !"TODOS".equals(estatus) )
			sql = sql +" and a.ESTATUS = :estatus ";
		if (year != null && !year.equals(0) )
			sql = sql +" and DATENAME(year,A.FECHA_ENTREGA)  = :year ";
		
		if (tipo==null )
			sql = sql +" and A.ID_PERSONA_SOLICITA= :idUsuario ";					
		
   return this.getNamedJdbcTemplate().queryForList("SELECT     A.ID_SALIDA, A.CONCEPTO, A.CLV_PARTID, A.ID_PERSONA_SOLICITA, A.FOLIO, B.PARTIDA,CONVERT(varchar(10), A.FECHA_ENTREGA, 103) AS FECHA_ENTREGA, "+ 
				  " C.NOMBRE + ' ' + C.APE_PAT + ' ' + C.APE_MAT AS NOMBRE_COMPLETO, e.NOMBRE AS DEPTO ,CONVERT(varchar(10), A.FECHA, 103) AS FECHA,a.ESTATUS "+
				  " FROM   dbo.SALIDAS AS A INNER JOIN "+
				  " dbo.CAT_PARTID AS B ON A.CLV_PARTID = B.CLV_PARTID INNER JOIN "+
				  " dbo.PERSONAS AS C ON A.ID_PERSONA_SOLICITA = C.CVE_PERS INNER JOIN "+
				  " dbo.TRABAJADOR AS D ON C.CVE_PERS = D.CVE_PERS INNER JOIN "+
				  " dbo.UNIDAD_ADM AS e ON d.CVE_UNIDAD = e.CVE_UNIDAD WHERE  a.ID_ALMACEN = :idAlmacen "+sql, parametros );		
	}
	
	public String cancelarSolicitud(final Long idSalida, boolean permiso ) {
	    String     mensaje="El proceso se realizo satisfactoriamente";
	    try {
	    	
	    	boolean fechaValida =estaEnFecha(idSalida);	    	
	    	if (fechaValida || permiso)
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {
                	getJdbcTemplate().update("update a set a.CANTIDAD=a.CANTIDAD+b.surtido from inventario a,DETALLE_SALIDA B where a.ID_INVENTARIO=b.ID_INVENTARIO and B.ID_SALIDA=? ", new Object[]{idSalida});
                     getJdbcTemplate().update("update SALIDAS set ESTATUS=?,ID_PERSONA_ENTREGA=?,FECHA_ENTREGA=getdate()  where  ID_SALIDA=? ", new Object[]{"CANCELADO",getSesion().getIdUsuario(),idSalida});                                
                } });
	    	else
	    		mensaje="El proceso no se puede realizar por que esta fuera de fecha";
        } catch (DataAccessException e) {            
            log.info("Los registros tienen relaciones con otras tablas ");	                    
            throw new RuntimeException(e.getMessage(),e);            
        }
	return mensaje;
	}
	public boolean estaEnFecha(Long idSalida){
		int valor=this.getJdbcTemplate().queryForInt("select  count(*)  from salidas where DATEPART(year, FECHA_ENTREGA )=DATEPART(year, getdate()) and "+ 
			" DATEPART(month, FECHA_ENTREGA )=DATEPART(month, getdate()) and id_salida=? ", new Object []{idSalida});
		return valor > 0;  
	}
}
