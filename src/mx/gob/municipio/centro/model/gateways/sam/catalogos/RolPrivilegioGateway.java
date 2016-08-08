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

public class RolPrivilegioGateway extends BaseGateway {
    public RolPrivilegioGateway() {
    }

    public void eliminaTodosPorSistemaTipoRol(Integer idTipoRol, Integer idSistema ) {
        this.getJdbcTemplate().update("delete  from ADM_ROL where  ID_TIPO_ROL=? and ID_MODULO IN (SELECT ID_MODULO FROM ADM_MODULO WHERE ID_SISTEMA=? ) ",new Object[]{idTipoRol,idSistema});
    }
    
    public void insertar(Integer idTipoRol,Integer  idModulo,Integer idPrivilegio) {
        this.getJdbcTemplate().update("insert into ADM_ROL  (ID_TIPO_ROL,ID_MODULO,ID_PERFIL  ) values (?,?,?)",new Object[]{idTipoRol,idModulo,idPrivilegio});
    }
        
    
}
