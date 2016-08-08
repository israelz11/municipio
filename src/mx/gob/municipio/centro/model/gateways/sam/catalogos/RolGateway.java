/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam.catalogos;

import java.util.*;

import mx.gob.municipio.centro.model.bases.BaseGateway;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class RolGateway extends BaseGateway {
    public RolGateway() {
    }

    public void actualiza(Integer idTipoRol,String descripcion,String estatus ) {
        this.getJdbcTemplate().update("update SAM_ROL set ROL_DESCRIPCION=?,ROL_ESTADO=? where ID_ROL = ? ",new Object[]{descripcion,estatus,idTipoRol});
    }
    
    public void insertar(String descripcion,String estatus) {
        this.getJdbcTemplate().update("insert into SAM_ROL  ( ROL_DESCRIPCION,ROL_ESTADO ) values (?,?)",new Object[]{descripcion,estatus});
    }
    
    public void guardarPrincipal(final Integer idTipoRol,final String descripcion,final String estatus){    	
    	try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
          	  			  @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {     
                            if (idTipoRol==null )
                            	insertar(descripcion,estatus);                            	
                             else
                            	 actualiza(idTipoRol,descripcion,estatus);
          	  			  }      	
                        });
        } catch (DataAccessException e) {        	
            //return "Error: El registro no se inserto ";            
            //throw new RuntimeException(e.getMessage(),e);
        }
        //return "Se registro satisfactoriamente el registro";
        
    	
    }
        
    public Map buscarPorID(Integer id) {
        return this.getJdbcTemplate().queryForMap("select * from SAM_ROL where  ID_ROL = ? ",new Object[]{id});
    }
    
    public List<Map> buscarTodos() {
        return this.getJdbcTemplate().queryForList("select * from SAM_ROL   ");
    }
    
    
    public void eliminar(Integer id) {
    	this.getJdbcTemplate().update("delete from SAM_ROL where ID_ROL=?  ",new Object[] {id});
    }    
}
