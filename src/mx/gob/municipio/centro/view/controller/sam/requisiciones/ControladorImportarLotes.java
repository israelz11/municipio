package mx.gob.municipio.centro.view.controller.sam.requisiciones;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayMovimientosRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/requisiciones/muestraImportar.action")
public class ControladorImportarLotes extends ControladorBase {
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	public GatewayOrdenDePagos gatewayOrdenDePagos;
	@Autowired
	private GatewayMeses gatewayMeses;
	@Autowired
	private GatewayRequisicion gatewayRequisicion;
	
	@Autowired
	private GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
	
	public static void ControladorImportarLotes() {
		// TODO Auto-generated method stub
	}
	
	@RequestMapping(method = {RequestMethod.GET , RequestMethod.POST} )   
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Long cve_req = (request.getParameter("cve_req")==null) ? 0L: Long.parseLong(request.getParameter("cve_req").toString());
		String unidad = request.getParameter("idUnidad");
		String  num_req = request.getParameter("num_req");
		String  status = request.getParameter("status");
		if(unidad==null) 
			modelo.put("idUnidad",this.getSesion().getClaveUnidad());
		else
			modelo.put("idUnidad",unidad);
		if(num_req==null) num_req = "";
		if(status==null) status="1,2";
		if(unidad==null) unidad = this.getSesion().getClaveUnidad();
		modelo.put("status", status);
		modelo.put("num_req", num_req);
		modelo.put("accion", request.getParameter("accion"));
		modelo.put("cve_req", cve_req);
		
		List <Map> lst = this.getJdbcTemplate().queryForList("SELECT "+ 
					"C.CVE_CONTRATO,"+
					"C.ID_PROYECTO,"+
					"CT.N_PROGRAMA,"+
					"C.CLV_PARTID, "+
					"C.NUM_REQ,   "+
					"C.CVE_REQ, "+
					"C.OBSERVA , "+
					"isnull ( (select sum (cantidad * precio_est ) from SAM_REQ_MOVTOS where cve_req=C.cve_req  ),0) IMPORTE "+
					"FROM "+
					"	SAM_REQUISIC AS C INNER JOIN CEDULA_TEC AS CT ON (CT.ID_PROYECTO = C.ID_PROYECTO) WHERE C.STATUS IN("+status+") AND C.TIPO IN(1, 7) AND C.NUM_REQ LIKE '%"+num_req+"%'"+((unidad.toString().equals("0"))? "":" AND C.ID_DEPENDENCIA IN("+unidad+")")); 
		modelo.put("documentos",lst);
		if(cve_req!=0){
			modelo.put("movimientos", this.gatewayMovimientosRequisicion.getConceptos(cve_req));
		}
		
	    return "sam/requisiciones/muestraImportar.jsp";
	}
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
}
