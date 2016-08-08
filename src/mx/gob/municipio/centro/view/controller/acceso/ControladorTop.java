/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.acceso;


import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/menu/top.action")
public class ControladorTop extends ControladorBase {
    
    private static Logger log =   Logger.getLogger(ControladorTop.class.getName());
    
    @Autowired 
    GatewayUsuarios gatewayUsuarios;
    
    public ControladorTop() {
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
    public String  handleRequest(Map modelo,  HttpServletRequest request) {
    	Integer main = request.getParameter("main") != null ?  request.getParameter("main").equals("")? null :  Integer.parseInt(request.getParameter("main")) : null  ;
    	String mainDesc=request.getParameter("mainDesc");
    	List sistemas =gatewayUsuarios.getPermisosSistemas(this.getSesion().getIdUsuario());
    	if (main == null && sistemas.size()>0 ) {
    		Map dato = (Map)sistemas.get(0);
    		main=(Integer)dato.get("ID_SISTEMA");
   			mainDesc=(String)dato.get("SIS_DESCRIPCION");
    	}    		
    	modelo.put("main",main);
    	modelo.put("usuario",this.getSesion().getUsuario());
    	modelo.put("unidad",this.getSesion().getUnidad());
    	modelo.put("mainDesc",mainDesc);
    	modelo.put("sistemas",sistemas);
    	modelo.put("menus",gatewayUsuarios.getMenuPrivilegiosUsuario(this.getSesion().getIdUsuario(),main) );
    	/*Seccion para automatizar el paso de documentos al siguiente periodo valido activo*/
    	int mesActivo = this.getJdbcTemplate().queryForInt("SELECT  min(MES) FROM MESES WHERE ESTATUS='ACTIVO' and ejercicio=?", new Object[]{this.getSesion().getEjercicio()});
    	int mesAnterior = mesActivo -1;
    	if(mesAnterior<=0) mesAnterior =1;
    	for(int i=mesAnterior; i<mesActivo;i++){
    		getJdbcTemplate().update("UPDATE SAM_REQUISIC SET PERIODO = ? WHERE STATUS IN (0,1,2) AND PERIODO <= ?", new Object[]{mesActivo, mesAnterior});
    		getJdbcTemplate().update("UPDATE SAM_REQUISIC SET PERIODO = ? WHERE CVE_REQ IN (SELECT P.CVE_REQ FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC R ON (R.CVE_REQ = P.CVE_REQ) WHERE R.PERIODO <= ? AND P.STATUS IN(1,4))", new Object[]{mesActivo, mesAnterior});
    		getJdbcTemplate().update("UPDATE SAM_COMP_REQUISIC SET PERIODO = ? WHERE CVE_REQ IN (SELECT CVE_DOC FROM VT_COMPROMISOS INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = VT_COMPROMISOS.CVE_DOC AND SAM_REQUISIC.TIPO NOT IN(7,8)) WHERE TIPO_DOC IN ('REQ','O.T','O.S') AND VT_COMPROMISOS.PERIODO <= ? AND CONSULTA IN('PRECOMPROMETIDO', 'COMPROMETIDO'))", new Object[]{mesActivo, mesAnterior});
    		getJdbcTemplate().update("UPDATE SAM_COMP_REQUISIC SET PERIODO = ? WHERE CVE_REQ IN (SELECT P.CVE_REQ FROM SAM_PEDIDOS_EX AS P INNER JOIN SAM_REQUISIC R ON (R.CVE_REQ = P.CVE_REQ) WHERE R.PERIODO <= ? AND P.STATUS IN(1,4))", new Object[]{mesActivo, mesAnterior});
    		getJdbcTemplate().update("UPDATE SAM_FACTURAS SET PERIODO = ? WHERE STATUS = 0 AND PERIODO <=?", new Object[]{mesActivo, mesAnterior});
    		//getJdbcTemplate().update("UPDATE SAM_VALES_EX SET MES =? WHERE STATUS = 4 AND MES <=?", new Object[]{mesActivo, mesAnterior});
    		getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET PERIODO = ? WHERE STATUS = -1 AND PERIODO <= ?", new Object[]{mesActivo, mesAnterior});
    		getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET PERIODO = ? WHERE CVE_OP IN (SELECT CVE_DOC FROM VT_COMPROMISOS WHERE TIPO_DOC IN ('OP') AND PERIODO <= ? AND CONSULTA ='COMPROMETIDO')", new Object[]{mesActivo, mesAnterior});
    		
    	}
    	
        return "menu/top.jsp";
    }   
        
    
}
