/**
 * @author ISC. Israel de la Cruz Hdez.
 * @version 1.0, Date: 15/Jun/2011
 *
 */
package mx.gob.municipio.centro.view.controller.sam.contratos;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayContratos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/contratos/lista_contratos.action")
public class ControladorListadoContratos extends ControladorBase {

	@Autowired
	private GatewayContratos gatewayContratos;
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewayPlanArbit gatewayPlanArbit;
	
	/**
	 * @param args
	 */
	public ControladorListadoContratos() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked") 
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		String unidad=request.getParameter("cbodependencia")==null ? this.getSesion().getClaveUnidad() : request.getParameter("cbodependencia");
		String estatus=request.getParameter("status")==null ? Integer.toString(gatewayContratos.CON_STATUS_EDICION): this.arrayToString(request.getParameterValues("status"),",");
		String tipoGasto=request.getParameter("cbotipogasto");
		String beneficiario=request.getParameter("txtprestadorservicio");
		String cve_benefi=request.getParameter("CLV_BENEFI");
		
		if(beneficiario==null ||beneficiario.equals("")) cve_benefi = "";
		String verUnidad=request.getParameter("verUnidad");
		modelo.put("idUnidad", unidad);
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("fechaInicial",(request.getParameter("fechaInicial")==null ? "": request.getParameter("fechaInicial")));
		modelo.put("fechaFinal",(request.getParameter("fechaFinal")==null ? "": request.getParameter("fechaFinal")));
		modelo.put("status",estatus);
		modelo.put("tipo_gto",tipoGasto );
		modelo.put("txtprestadorservicio",beneficiario );
		modelo.put("CLV_BENEFI",cve_benefi );
		modelo.put("verUnidad",verUnidad);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		Map xmod = new HashMap();
		xmod.putAll(modelo);
		modelo.put("listado", gatewayContratos.getListaContratos(xmod));
	    return "sam/contratos/lista_contratos.jsp";
	}
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	@ModelAttribute("tipodeGasto")
    public List<Map> getTiposDeGasto(){
    	return gatewayPlanArbit.getTipodeGasto();
    }
	
	public String aperturarContratos(List<Long> lst_contratos){
		return gatewayContratos.aperturarContratos(lst_contratos, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
	}
	
	public String cancelarContrato(List<Long> lst_contratos){
		return gatewayContratos.cancelarContrato(lst_contratos, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
	}
	
	public List<Map> getConceptosContrato(Long cve_contrato){
		return this.gatewayContratos.getConceptosContrato(cve_contrato);
	}
	
	public List<Map> getMovimientosAjustadosContrato(Long cve_contrato)
	{
		return this.gatewayContratos.getMovimientosAjustadosContrato(cve_contrato);
	}

}
