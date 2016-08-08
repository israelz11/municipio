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

public class PrivilegioGateway extends BaseGateway {
    public PrivilegioGateway() {
    }

    public void actualiza(Integer idPrivilegio,String descripcion,String estatus, String tipo, String url,Integer orden) {
        this.getJdbcTemplate().update("update SAM_PRIVILEGIO set PRI_DESCRIPCION=?,PRI_ESTATUS=?,TIPO=?, URL=? , ORDEN=? where ID_PRIVILEGIO = ? ",new Object[]{descripcion,estatus,tipo,url,orden,idPrivilegio});
    }
    
    public void insertar(String descripcion,String estatus,Integer modulo, String tipo , String url, Integer orden) {
        this.getJdbcTemplate().update("insert into SAM_PRIVILEGIO  ( PRI_DESCRIPCION,PRI_ESTATUS,ID_MODULO,TIPO,URL,ORDEN) values (?,?,?,?,?,?)",new Object[]{descripcion,estatus,modulo,tipo,url,orden});
    }
    
    public void guardarPrincipal(final Integer idPrivilegio,final String descripcion,final String  estatus,final Integer modulo,final String tipo, final String url, final Integer orden){    	
    	try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
          	  			  @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {     
                            if (idPrivilegio==null )
                            	insertar(descripcion,estatus,modulo,tipo,url,orden);                            	
                             else
                            	 actualiza(idPrivilegio,descripcion,estatus,tipo,url,orden);
          	  			  }      	
                        });
        } catch (DataAccessException e) {        	
            //return "Error: El registro no se inserto ";            
            //throw new RuntimeException(e.getMessage(),e);
        }
        //return "Se registro satisfactoriamente el registro";
        
    	
    }
    
    
    public Map buscarPorID(Integer id) {
        return this.getJdbcTemplate().queryForMap("select a.*, b.ID_SISTEMA from SAM_PRIVILEGIO a,SAM_MODULO b where  a.ID_PRIVILEGIO = ? and a.ID_MODULO=b.ID_MODULO",new Object[]{id});
    }
    
    
    public List<Map> buscarPorModulo(Integer idModulo) {
        return this.getJdbcTemplate().queryForList("select a.*,b.MOD_DESCRIPCION as MODULO   from SAM_PRIVILEGIO a, SAM_MODULO b where a.ID_MODULO = b.ID_MODULO and  a.ID_MODULO=?  ORDER BY a.ORDEN ASC" , new Object []{idModulo});
    }
    
    public void eliminar(Integer id) {
    	this.getJdbcTemplate().update("delete from SAM_PRIVILEGIO where ID_PRIVILEGIO=?  ",new Object[] {id});
    }
    
    
}
