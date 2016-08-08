/**
 * @author Mauricio Hernandez Leon.
 * @version 1.0, Date: 12/May/2010
 *
 */

package mx.gob.municipio.centro.view.controller.sam.requisiciones;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProductos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/requisiciones/cat_articulo.action")
public class ControladorArticulos extends ControladorBase {
	private static Logger log = Logger.getLogger(ControladorArticulos.class.getName());
	
	@Autowired
	private GatewayProyectoPartidas gatewayProyectoPartidas;
		
	@Autowired
	GatewayProductos gatewayProductos;
	
	public ControladorArticulos(){}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)  
	public String  requestGetControlador(  HttpServletRequest request) {
	    return "sam/requisiciones/cat_articulo.jsp";
	}
	
	@ModelAttribute("partidas")
    public List<Map> getUnidades(){
    	return gatewayProyectoPartidas.getPartidas();	
    }
	
	
	 public  void guardarArticulo(Integer idArticulo,  String unidadMedida, String descripcion, String ult_benefi, Double precio , int invetario, int consumible, String estatus){
		 gatewayProductos.actualizarPrincipal(idArticulo, unidadMedida, descripcion,ult_benefi, precio, invetario, consumible, estatus);
	     }
	     
		  public void  eliminarArticulo( final List<Integer> productos ) {
			  try {                 
		            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
		                @Override
		    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
		                	for (Integer producto :productos )
		                		gatewayProductos.eliminar(producto);	                			                	
		                } });
		                } catch (DataAccessException e) {            
		                    log.info("Los registros tienen relaciones con otras tablas ");	                    
		                    throw new RuntimeException(e.getMessage(),e);
		                }	                	                		  	  
		  }
		  
			public Map getArticulo(Integer idArticulo  ){
					return gatewayProductos.getArticulo(idArticulo);
			}
			
			
			public List getArticulosTodos(String alfabetico) {	   
				return gatewayProductos.getArticulosTodos(alfabetico);
			} 
			
			public boolean getExistenciaDocumentos(Integer id_articulo){				
				return this.getJdbcTemplate().queryForInt("SELECT COUNT(ID_ARTICULO) AS N FROM SAM_REQ_MOVTOS WHERE ID_ARTICULO = ?", new Object[]{id_articulo})!=0;
			}
	     
	    
}
