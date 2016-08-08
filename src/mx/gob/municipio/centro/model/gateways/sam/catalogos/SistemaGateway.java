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

public class SistemaGateway extends BaseGateway {
    public SistemaGateway() {
    }

    public void actualiza(Integer idSistema,String descripcion,String estatus) {
        this.getJdbcTemplate().update("update SAM_SISTEMA set SIS_DESCRIPCION=?,SIS_ESTATUS=? where ID_SISTEMA = ? ",new Object[]{descripcion,estatus,idSistema});
    }
    
    public void insertar(String descripcion,String estatus ) {
        this.getJdbcTemplate().update("insert into SAM_SISTEMA  ( SIS_DESCRIPCION,SIS_ESTATUS ) values (?,?)",new Object[]{descripcion,estatus});
    }
    
    public void guardarPrincipal(final Integer idSistema,final String descripcion,final String estatus){    	
    	try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
          	  			  @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {     
                            if (idSistema==null )
                            	insertar(descripcion,estatus);                            	
                             else
                            	 actualiza(idSistema,descripcion,estatus);
          	  			  }      	
                        });
        } catch (DataAccessException e) {        	
            //return "Error: El registro no se inserto ";            
            //throw new RuntimeException(e.getMessage(),e);
        }
        //return "Se registro satisfactoriamente el registro";
        
    	
    }
    
    
    public Map buscarPorID(Integer id) {
        return this.getJdbcTemplate().queryForMap("select * from SAM_SISTEMA where  ID_SISTEMA = ? ",new Object[]{id});
    }
    
    public List<Map> buscarTodos() {
        return this.getJdbcTemplate().queryForList("select * from SAM_SISTEMA   ");
    }
    
    
    public void eliminar(Integer id) {
    	this.getJdbcTemplate().update("delete from SAM_SISTEMA where ID_SISTEMA=?  ",new Object[] {id});
    }
    
    
}
