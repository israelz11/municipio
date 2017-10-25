/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.ordenesPagos;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayBeneficiario;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayTipoOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/ordenesdepago/lista_ordenPago.action")

public class ControladorListadoOrdenesDePago extends ControladorBase {

	public ControladorListadoOrdenesDePago(){}
	
	
	public final static  int VER_TODAS_LAS_UNIDADES = 25;
//	final String STATUS_NUEVA="0"; 
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewayPlanArbit gatewayPlanArbit;
	
	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	
	@Autowired 
	GatewayTipoOrdenDePagos gatewayTipoOrdenDePagos;
	
	@Autowired
	private GatewayBeneficiario gatewayBeneficiario;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);
		String unidad=request.getParameter("cbodependencia")==null ?this.getSesion().getClaveUnidad() : request.getParameter("cbodependencia");
		String estatus=request.getParameter("status")==null ? Integer.toString(gatewayOrdenDePagos.OP_ESTADO_EN_EDICION): this.arrayToString(request.getParameterValues("status"),",");
		modelo.put("cbocapitulo", (request.getParameter("cbocapitulo")==null ? "": request.getParameter("cbocapitulo")));
		String fechaIni=request.getParameter("fechaInicial");
		String fechaFin=request.getParameter("fechaFinal");
		String tipoGasto=request.getParameter("cbotipogasto");
		//String beneficiario=request.getParameter("txtprestadorservicio");
		//String cve_benefi=request.getParameter("CVE_BENEFI");
		String cve_benefi= request.getParameter("cboprestadorservicio");
		String beneficiario=request.getParameter("cboprestadorservicio");//beneficiario
		String tipo=request.getParameter("cbotipo");
		String numop = request.getParameter("txtnumop");
		String numped = request.getParameter("txtpedido");
		
		//determinar si solo puede filtrar capitulo 5000 en  privilegios
		if(this.getPrivilegioEn(this.getSesion().getIdUsuario(), 113)){
			modelo.put("cbocapitulo" ,5000);
		}
		
		if(privilegio){
			if(request.getParameter("cbodependencia")==null)
				unidad = "0";
			if(request.getParameter("cbodependencia")!=null)
				unidad = request.getParameter("cbodependencia");
		}
		
		if(!privilegio){
			
			if(request.getParameter("cbodependencia")==null)
				unidad = this.getSesion().getClaveUnidad();
			if(request.getParameter("cbodependencia")!=null)
				unidad = request.getParameter("cbodependencia");
		}
		
		if(beneficiario==null ||beneficiario.equals("")) cve_benefi = "";
		
		String verUnidad=request.getParameter("verUnidad");
		
		if(verUnidad!=null&&privilegio)
			unidad = this.getSesion().getClaveUnidad();
		
		modelo.put("idUnidad", unidad);
		
		modelo.put("cbotipo", tipo);
		modelo.put("txtnumop", numop);
		modelo.put("txtpedido", numped);
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("fechaInicial",fechaIni);
		modelo.put("fechaFinal",fechaFin);
		modelo.put("status",estatus);
		modelo.put("tipo_gto",tipoGasto );
		modelo.put("txtprestadorservicio",beneficiario );
		modelo.put("CVE_BENEFI",cve_benefi );
		modelo.put("clv_benefi",gatewayBeneficiario.getBeneficiariosTodos(0));
		modelo.put("verUnidad",verUnidad);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		List <Map> lista = this.getListadoOrdenes(unidad, estatus, fechaIni,fechaFin, cve_benefi, this.getSesion().getEjercicio(),tipoGasto,this.getSesion().getIdUsuario(),verUnidad, tipo, numop, numped, privilegio, modelo.get("cbocapitulo").toString());
		modelo.put("ordenes",lista);
		modelo.put("CONTADOR", lista.size());
	    return "sam/ordenesdepago/lista_ordenPago.jsp";
	}
	
	@ModelAttribute("capitulos")
    public List<Map> getCapitulos(){
    	return gatewayUnidadAdm.getCapitulos();	
    }
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
		
	@ModelAttribute("tipodeGasto")
    public List<Map> getTiposDeGasto(){
    	return gatewayPlanArbit.getTipodeGasto();
    }
		
	@ModelAttribute("beneficiarios")
	public List<Map>getBeneficiarios(){
		return gatewayBeneficiario.getListaBeneficiarios();
	}
	
	public List <Map>getListadoOrdenes(String unidad, String  estatus , String fechaInicial, String fechaFinal , String clv_benefi, Integer ejercicio, String tipoGasto, Integer idUsuario, String verUnidad, String tipo, String numop, String numped, boolean privilegio, String capitulo){
		return this.gatewayOrdenDePagos.getListaDeOrdenesPorEjemplo(unidad, estatus , this.formatoFecha(fechaInicial), this.formatoFecha(fechaFinal) , clv_benefi, ejercicio, tipoGasto, idUsuario, verUnidad, tipo, numop, numped, privilegio, capitulo);
	}	
	@ModelAttribute("tipoDocumentosOp")
    public List getTipoDocumentosTodosOp() {
    	return gatewayTipoOrdenDePagos.getTipoOredenesPagosEstatusActivos();
    }
	
}
