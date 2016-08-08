/**
 * @author ISC. Israel de la Cruz Hdez.
 * @version 1.0, Date: 29/Jun/2011
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import mx.gob.municipio.centro.model.gateways.sam.GatewayContratos;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/muestra_contratos.action")
public class ControladorMuestraContratos extends ControladorBase {

	/**
	 * @param args
	 */
	
	@Autowired
	private GatewayContratos gatewayContratos;
	
	public ControladorMuestraContratos() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked") 
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		String num_contrato = request.getParameter("num_contrato");
		int idRecurso = (request.getParameter("tipo_gto")!=null ? Integer.parseInt(request.getParameter("tipo_gto").toString()): 0);
		Long cve_contrato = (request.getParameter("cve_contrato")!=null ? Long.parseLong(request.getParameter("cve_contrato").toString()): 0);
		int idDependencia = (request.getParameter("idDependencia")!=null ? Integer.parseInt(request.getParameter("idDependencia").toString()): Integer.parseInt(this.getSesion().getIdUnidad()));
		int mesActivo = this.getJdbcTemplate().queryForInt("select ISNULL(MAX(MES),0) from MESES where estatus='ACTIVO'");
		modelo.put("documentos", gatewayContratos.getListaContratos(cve_contrato, num_contrato, idDependencia, mesActivo, idRecurso));
	    return "sam/consultas/muestra_contratos.jsp";
	}

}
