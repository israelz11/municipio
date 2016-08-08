/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.view.controller.sam.ordenesPagos.ControladorOrdenPago;

public class GatewayFirmasDocumentos extends BaseGateway {
	private static Logger log = 
	        Logger.getLogger(GatewayFirmasDocumentos.class.getName());
	public GatewayFirmasDocumentos(){
		
	}
	
	
	public void getFirmasDocumentos(Integer grupo, Map datos){		
	  List<Map> resultado = this.getJdbcTemplate().queryForList("SELECT     B.REPRESENTANTE, B.CARGO, B.TIPO, "+ 
                " A.GRUPO_CONFIG, B.ID_FIRMA_GRUPO,   A.ID_GRUPO_CONFIG "+
	  			" FROM  SAM_GRUPO_CONFIG A  INNER JOIN "+
	  			" SAM_GRUPO_FIRMAS  B ON A.ID_GRUPO_CONFIG = B.ID_GRUPO_CONFIG and  A.ID_GRUPO_CONFIG=? ", new Object[] {grupo} );
	    	for (Map firma : resultado){
	    		String tipo =firma.get("TIPO").toString(); 	    		 
	    	  datos.put(tipo+"_CARGO",firma.get("CARGO").toString());
	    	  datos.put(tipo+"_REPRESENTANTE",firma.get("REPRESENTANTE").toString());
	    	  //log.info(tipo+"_CARGO "+ firma.get("CARGO").toString());
	    	  //log.info(tipo+"_REPRESENTANTE "+firma.get("REPRESENTANTE").toString());
	    	}
	}
}
