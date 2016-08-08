/**
 * @author Mauricio Hernandez Leon.
 * @version 1.0, Date: 12/May/2010
 *
 */

package mx.gob.municipio.centro.view.controller.sam.requisiciones;

import java.util.ArrayList;
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
@RequestMapping("/sam/requisiciones/productos.action")
public class ControladorArticulosPartida extends ControladorBase {
	private static Logger log = Logger.getLogger(ControladorArticulosPartida.class.getName());
	
	@Autowired
	private GatewayProyectoPartidas gatewayProyectoPartidas;
	
	List erroresArticulos;
		
	@Autowired
	GatewayProductos gatewayProductos;
	
	public ControladorArticulosPartida(){}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)  
	public String  requestGetControlador(  HttpServletRequest request) {
	    return "sam/requisiciones/productos.jsp";
	}
	
	@ModelAttribute("partidas")
    public List<Map> getUnidades(){
    	return gatewayProyectoPartidas.getPartidas();	
    }
	
		      
		 public String guardarArticulos( final List<Map<String,String>> articulos, final Integer partida ,final  String grupo ) {
			 erroresArticulos = new ArrayList<Object>();
			  try {                 
		            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
		                @Override
		    protected void   doInTransactionWithoutResult(TransactionStatus status) {
		                	/* falta validar */
		                	for (Map<String,String> articulo :articulos ){
		                		
	          	  				    Integer idArt = !(articulo.get("idArt")).equals("") ? Integer.parseInt(articulo.get("idArt")) : null ;
	          	  				    Integer checado = !(articulo.get("checado")).equals("") ? Integer.parseInt(articulo.get("checado")): 0;
	          	  				    Integer idArticulo = !(articulo.get("idArticulo")).equals("") ? Integer.parseInt(articulo.get("idArticulo")): null;
	          	  				    String estatus = articulo.get("estatus");	          	  				    
	          	  				    
	          	  				    if ( idArticulo== null && checado.equals(1) ) {
	          	  				    	if (!estaEnPartida(partida, idArt ) ) 
	          	  				           getJdbcTemplate().update("insert into SAM_CAT_PROD (GRUPO,SUBGRUPO,ESTATUS,ID_CAT_ARTICULO) values (?,?,?,?) ", new Object []{partida,grupo,estatus,idArt});
	          	  				    	else
	          	  				    	   erroresArticulos.add(idArt);	 
	          	  				    }
	          	  				    if ( idArticulo!= null && checado.equals(0) ) {
	          	  				    	if (!fueUsado(idArticulo )) 
	          	  				       getJdbcTemplate().update("delete  from SAM_CAT_PROD where ID_ARTICULO=? ", new Object []{idArticulo});
	          	  				    	else
	          	  				    	erroresArticulos.add(idArt);
	          	  				       
	          	  				    }
	          	  				    
	          	  			  	   if ( idArticulo!= null && checado.equals(1) )
	          	  				       getJdbcTemplate().update("update SAM_CAT_PROD set ESTATUS=?    where ID_ARTICULO=? ", new Object []{estatus,idArticulo});
	          	  					
	          					   }	
		                	
		                } });
		                } catch (DataAccessException e) {            
		                    log.info("Los registros tienen relaciones con otras tablas ");	                    
		                    throw new RuntimeException(e.getMessage(),e);
		                }	
		                
		            return "";    
		  }
		  /* MODIFICACION ABRAHAM*/
		 private boolean estaEnPartida(Integer partida, Integer idArt ) {
			  int reg =  this.getJdbcTemplate().queryForInt("select count (*) from SAM_CAT_PROD where  GRUPO=? and ID_CAT_ARTICULO =?  ", new Object []{partida,idArt});
			  return  reg != 0;
		}
		 private boolean fueUsado(Integer idArticulo ) {	   
			 int reg =  this.getJdbcTemplate().queryForInt("select count (*) from SAM_REQ_MOVTOS where ID_ARTICULO =?  ", new Object []{idArticulo});
			  return  reg > 0;
		}
		 
			
			public List getGrupos(String partida ) {	   
				  return gatewayProductos.getGrupos(partida);
			}
			
			
			public List getArticulosPartidaGrupo(Integer partida, String alfabetico ) {	   
 				   return this.getJdbcTemplate().queryForList(" SELECT      B.ID_ARTICULO,  A.ULT_PRECIO, A.ID_CAT_ARTICULO, A.CLV_UNIMED, B.SUBGRUPO, A.DESCRIPCION, B.ESTATUS,C.UNIDMEDIDA "+
					" FROM         SAM_CAT_ARTICULO AS A LEFT OUTER JOIN  CAT_UNIMED C ON A.CLV_UNIMED = C.CLV_UNIMED LEFT OUTER JOIN "+
					" SAM_CAT_PROD AS B ON A.ID_CAT_ARTICULO = B.ID_CAT_ARTICULO  AND B.GRUPO = ?  "+
					" WHERE     (A.DESCRIPCION LIKE ? )  "+
					" ORDER BY A.DESCRIPCION ", new Object []{partida,  alfabetico+'%'  } );
			}	
			
			public List getArticulosPartidaGrupo2(Integer partida, Integer grupo ) {	   
				   return this.getJdbcTemplate().queryForList(" SELECT      B.ID_ARTICULO, A.ULT_PRECIO, A.ID_CAT_ARTICULO, A.CLV_UNIMED, B.SUBGRUPO, A.DESCRIPCION, B.ESTATUS,C.UNIDMEDIDA "+
					" FROM         SAM_CAT_ARTICULO AS A LEFT OUTER JOIN  CAT_UNIMED C ON A.CLV_UNIMED = C.CLV_UNIMED LEFT OUTER JOIN "+
					" SAM_CAT_PROD AS B ON A.ID_CAT_ARTICULO = B.ID_CAT_ARTICULO  WHERE B.GRUPO = ? AND B.SUBGRUPO = ?"+
					" ORDER BY A.DESCRIPCION ", new Object []{partida, grupo});
			}	
}
