package mx.gob.municipio.centro.view.controller.sam.facturas;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.municipio.centro.model.gateways.sam.GatewayFacturas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/facturas/lst_facturas.action")
public class ControladorListadoFacturas extends ControladorBase {

	public final static  int VER_TODAS_LAS_UNIDADES = 25;
	
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired 
	GatewayFacturas gatewayFacturas;
	
	public ControladorListadoFacturas(){
		
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);
		String verUnidad=request.getParameter("verUnidad");
		String idUnidad=request.getParameter("cbodependencia")==null ?this.getSesion().getClaveUnidad() : request.getParameter("cbodependencia");
		String estatus=request.getParameter("status")==null ? "0": this.arrayToString(request.getParameterValues("status"),",");
		String beneficiario=request.getParameter("txtbeneficiario");
		String clv_benefi=request.getParameter("clv_benefi");
		String numped=request.getParameter("txtpedido");
		String numreq=request.getParameter("txtnumreq");
		String num_factura=request.getParameter("txtfactura");
		
		modelo.put("numfactura", num_factura);
		modelo.put("numped", numped);
		modelo.put("numreq", numreq);
		modelo.put("clv_benefi", clv_benefi);
		modelo.put("estatus", estatus);
		modelo.put("cbodependencia",idUnidad);
		modelo.put("beneficiario", beneficiario);
		modelo.put("fechaInicial",(request.getParameter("txtfechaInicial")==null ? "": request.getParameter("txtfechaInicial")));
		modelo.put("fechaFinal",(request.getParameter("txtfechaFinal")==null ? "": request.getParameter("txtfechaFinal")));
		
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("listadoFacturas",this.gatewayFacturas.getListadoFacturas(modelo));
		
		return "sam/facturas/lst_facturas.jsp";
	}
	
	public void aperturarFacturas(final Long[] idFacturas){
		gatewayFacturas.aperturarFacturas(idFacturas, this.getSesion().getIdUsuario());
	}
	
	public void cancelarFacturas(final Long[] idFacturas){
		gatewayFacturas.cancelarFacturas(idFacturas, this.getSesion().getIdUsuario(), this.getSesion().getEjercicio());
	}
	
	public List<Map> getListaAnexosArchivosFactura(Long cve_factura)
	{
		return gatewayFacturas.getListaAnexosArchivosFacturas(cve_factura);
	}
	
	@ModelAttribute("unidadesAdmiva")
	public List<Map> getUnidadesAdmivas(){
	   	return gatewayUnidadAdm.getUnidadAdmTodos();	
	}
	
	public List<Map> getMovimientosAjustadosFactura(Long cve_factura)
	{
		return gatewayFacturas.getMovimientosAjustadosFactura(cve_factura);
	}
	
	public List<Map> getConceptosFactura(Long cve_factura)
	{
		return gatewayFacturas.getDetallesFactura(cve_factura);
	}
	
	public void guardarAjusteFacturaPeredo(Long id_sam_mod_comp, Long cve_factura, int idProyecto, String clv_partid, String fecha, Double importe)
	{
		gatewayFacturas.guardarAjusteFacturaPeredo(id_sam_mod_comp, cve_factura, idProyecto, clv_partid, fecha, importe);
	}
	
	public void eliminarConceptoAjusteFactura(Long id_sam_mod_comp)
	{
		gatewayFacturas.eliminarConceptoAjusteFactura(id_sam_mod_comp);
	}
}
