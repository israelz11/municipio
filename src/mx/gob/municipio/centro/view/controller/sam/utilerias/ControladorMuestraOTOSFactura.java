package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.view.bases.ControladorBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/consultas/muestra_OT_OS_facturas.action")
public class ControladorMuestraOTOSFactura extends ControladorBase {

	public ControladorMuestraOTOSFactura(){
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Integer idDependencia = Integer.parseInt(request.getParameter("idDependencia").toString());
		modelo.put("muestraRequisiciones", this.getListaOTOS(idDependencia));
		
	    return "sam/consultas/muestra_OT_OS_facturas.jsp";
	}
	
	 public List<Map> getListaOTOS(int idDependencia){
	    return this.getJdbcTemplate().queryForList("SELECT SAM_REQUISIC.CVE_REQ, "+
															"SAM_REQUISIC.NUM_REQ, "+
															"SAM_REQUISIC.ID_PROYECTO,"+ 
															"CEDULA_TEC.N_PROGRAMA, "+
															"SAM_REQUISIC.CLV_PARTID,  "+
															"SAM_ORDEN_TRAB.CLV_BENEFI, "+
															"CAT_BENEFI.NCOMERCIA,  "+
															"SAM_REQUISIC.ID_DEPENDENCIA, "+
															"(SELECT SUM(SAM_REQ_MOVTOS.CANTIDAD*SAM_REQ_MOVTOS.PRECIO_EST) FROM SAM_REQ_MOVTOS WHERE SAM_REQ_MOVTOS.CVE_REQ = SAM_REQUISIC.CVE_REQ) TOTAL,  "+
															"OBSERVA,  "+
															"convert(varchar(10), SAM_REQUISIC.FECHA_CAP ,103) FECHA_CAP  "+
														"FROM SAM_REQUISIC  "+
															"INNER JOIN SAM_ORDEN_TRAB ON (SAM_ORDEN_TRAB.CVE_REQ = SAM_REQUISIC.CVE_REQ) "+
															"INNER JOIN CEDULA_TEC ON (CEDULA_TEC.ID_PROYECTO=SAM_REQUISIC.ID_PROYECTO)  "+
															"INNER JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI=SAM_ORDEN_TRAB.CLV_BENEFI)  "+
															"INNER JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = SAM_REQUISIC.ID_DEPENDENCIA)  "+
														"WHERE SAM_REQUISIC.STATUS IN(1,2) AND SAM_REQUISIC.TIPO NOT IN(1, 7) " +
														" AND SAM_REQUISIC.CVE_REQ IN(SELECT CVE_DOC FROM VT_COMPROMISOS WHERE TIPO_DOC IN('O.S', 'O.T') AND CONSULTA ='COMPROMETIDO' AND CVE_DOC = SAM_REQUISIC.CVE_REQ AND MONTO > 0) " +
														/*"AND (SELECT COUNT(*) FROM SAM_FACTURAS WHERE STATUS IN(1, 3) AND SAM_FACTURAS.CVE_REQ = SAM_REQUISIC.CVE_REQ)=0 " +*/
														"AND CAT_DEPENDENCIAS.ID = ? ORDER BY SAM_REQUISIC.CVE_REQ ASC", new Object[]{idDependencia});
	}
	

}
