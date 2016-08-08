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

public class ModuloGateway extends BaseGateway {
    public ModuloGateway() {
    }

    public void actualiza(Integer idModulo,String descripcion,String estatus,Integer sistema ) {
        this.getJdbcTemplate().update("update SAM_MODULO set MOD_DESCRIPCION=?,MOD_ESTATUS=?,ID_SISTEMA=?  where ID_MODULO = ? ",new Object[]{descripcion,estatus,sistema,idModulo});
    }
    
    public void insertar(String descripcion,String estatus,Integer sistema ) {
        this.getJdbcTemplate().update("insert into SAM_MODULO  ( MOD_DESCRIPCION,MOD_ESTATUS,ID_SISTEMA ) values (?,?,?)",new Object[]{descripcion,estatus,sistema});
    }
    
    public void guardarPrincipal(final Integer idModulo,final String descripcion,final String estatus,final Integer sistema){    	
    	try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
          	  			  @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {     
                            if (idModulo==null )
                            	insertar(descripcion,estatus,sistema);                            	
                             else
                            	 actualiza(idModulo,descripcion,estatus,sistema);
          	  			  }      	
                        });
        } catch (DataAccessException e) {        	
            //return "Error: El registro no se inserto ";            
            //throw new RuntimeException(e.getMessage(),e);
        }
        //return "Se registro satisfactoriamente el registro";
        
    	
    }
    
    
    public Map buscarPorID(Integer id) {
        return this.getJdbcTemplate().queryForMap("select * from SAM_MODULO where  ID_MODULO = ? ",new Object[]{id});
    }
    
    public List<Map> buscarTodos() {
        return this.getJdbcTemplate().queryForList("select a.*,b.SIS_DESCRIPCION as SISTEMA  from SAM_MODULO a , SAM_SISTEMA b where ID_SISTEMA = b.ID_SISTEMA  ");
    }
    
    public List<Map> buscarPorSistema(Integer idSistema) {
        return this.getJdbcTemplate().queryForList("select a.*,b.SIS_DESCRIPCION as SISTEMA  from SAM_MODULO a , SAM_SISTEMA b where a.ID_SISTEMA = b.ID_SISTEMA  and a.id_sistema=?  " , new Object []{idSistema});
    }
    
    public void eliminar(Integer id) {
    	this.getJdbcTemplate().update("delete from SAM_MODULO where ID_MODULO=?  ",new Object[] {id});
    }
    
    
}
