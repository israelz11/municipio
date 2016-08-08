package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/utilerias/sumenuAdmon.action")
public class ControladorMuestraSubmenuAdmon extends ControladorBase {

	/**
	 * @param args
	 */
	public ControladorMuestraSubmenuAdmon() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Map dato = new HashMap();
		modelo.put("modulo", request.getParameter("modulo")==null ? "": request.getParameter("modulo"));
		modelo.put("cve_doc", request.getParameter("cve_doc")==null ? "0": request.getParameter("cve_doc"));
		modelo.put("cve_pers2", request.getParameter("cve_pers")==null ? "0": request.getParameter("cve_pers"));
		modelo.put("cve_pers", this.getSesion().getIdUsuario());
		if(modelo.get("modulo").equals("req"))
			dato = this.getJdbcTemplate().queryForMap("SELECT NUM_REQ AS NUM_DOC FROM SAM_REQUISIC WHERE CVE_REQ =?", new Object[]{modelo.get("cve_doc").toString()});
		if(modelo.get("modulo").equals("ped"))
			dato = this.getJdbcTemplate().queryForMap("SELECT NUM_PED AS NUM_DOC FROM SAM_PEDIDOS_EX WHERE CVE_PED =?", new Object[]{modelo.get("cve_doc").toString()});
		if(modelo.get("modulo").equals("fac"))
			dato = this.getJdbcTemplate().queryForMap("SELECT NUM_FACTURA AS NUM_DOC FROM SAM_FACTURAS WHERE CVE_FACTURA =?", new Object[]{modelo.get("cve_doc").toString()});
		if(modelo.get("modulo").equals("op"))
			dato = this.getJdbcTemplate().queryForMap("SELECT NUM_OP AS NUM_DOC FROM SAM_ORD_PAGO WHERE CVE_OP =?", new Object[]{modelo.get("cve_doc").toString()});
		if(modelo.get("modulo").equals("val"))
			dato = this.getJdbcTemplate().queryForMap("SELECT NUM_VALE AS NUM_DOC FROM SAM_VALES_EX WHERE CVE_VALE =?", new Object[]{modelo.get("cve_doc").toString()});
		if(modelo.get("modulo").equals("con"))
			dato = this.getJdbcTemplate().queryForMap("SELECT NUM_CONTRATO AS NUM_DOC FROM SAM_CONTRATOS WHERE CVE_CONTRATO =?", new Object[]{modelo.get("cve_doc").toString()});
		
		modelo.put("NUM_DOC", dato.get("NUM_DOC").toString());
		return "sam/utilerias/sumenuAdmon.jsp";
	}

}
