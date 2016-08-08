/**
 * @author ISC. Israel de la Cruz Hdez.
 * @version 1.0, Date: 02/Jun/2011
 * @version 2.0, Date: 03/Feb/2013
 *
 */
package mx.gob.municipio.centro.view.controller.sam.contratos;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayContratos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/contratos/cap_contratos.action")
public class ControladorContratos extends ControladorBase {

	@Autowired
	private GatewayContratos gatewayContratos;
	
	@Autowired
	GatewayPlanArbit gatewayPlanArbit;
	
	@Autowired 
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	GatewayMeses gatewayMeses;
	
	/**
	 * @param args
	 */
	public ControladorContratos(){
		// TODO Auto-generated method stub
	}
	
	
	@SuppressWarnings("unchecked") 
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		modelo.put("cve_contrato", request.getParameter("cve_contrato")==null ? 0: request.getParameter("cve_contrato").equals("")? null: Long.parseLong(request.getParameter("cve_contrato")));
		String unidad = request.getParameter("cbodependencia")==null ?this.getSesion().getClaveUnidad() : request.getParameter("cbodependencia");
		String tipoGasto = request.getParameter("tipoGasto")==null ?this.getSesion().getClaveUnidad() : request.getParameter("tipoGasto");
		
		modelo.put("idUnidad",unidad);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		
		if(!modelo.get("cve_contrato").toString().equals("0"))
		{
			Map data = gatewayContratos.getContrato(Long.parseLong(modelo.get("cve_contrato").toString()));
			modelo.put("Contrato", data);
			modelo.put("idUnidad",data.get("ID_DEPENDENCIA").toString());
		}
	    return "sam/contratos/cap_contratos.jsp";
	}
	
	@ModelAttribute("tipoContratos")
    public List<Map> getTiposContratos(){
    	return gatewayContratos.getTiposContratos();
    }
	
    @ModelAttribute("tipoGastos")
    public List<Map> getTiposGastos(){
    	return gatewayPlanArbit.getTipodeGasto();	
    }
    
	 @ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidadesAdmivas(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	 @ModelAttribute("mesesActivos")
	public List<Map> getMes(){
		return gatewayMeses.getMesesRequisicion(this.getSesion().getEjercicio());
	}
	 
	/*public List <Map> getOrdenServicio(Long cve_req){
		return gatewayContratos.getOrdenServicio(cve_req);
	}*/
	
	public Long guardarContrato(Long cve_contrato, int idDependencia, String num_contrato, String fecha_ini, String fecha_fin, String oficio, String tiempo_entrega, int tipo, String concepto, Double anticipo, int idRecurso, String clv_benefi, Long cve_doc){
		return this.gatewayContratos.guardarContrato(cve_contrato, idDependencia, num_contrato, fecha_ini, fecha_fin, oficio, tiempo_entrega, tipo, concepto, anticipo, idRecurso, clv_benefi, cve_doc, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario(), getSesion().getIdGrupo());
	}
	
	public String cerrarContrato(Long cve_contrato){
		return this.gatewayContratos.cerrarContrato(cve_contrato, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
	}
	
	public List<Map> getConceptosContrato(Long cve_contrato){
		return this.gatewayContratos.getConceptosContrato(cve_contrato);
	}
	
	public void eliminarConceptosMovPeredo(final Long cve_contrato, final Long[] idDetalles)
	{
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	gatewayContratos.eliminarConcpetosContratoPeredo(cve_contrato, idDetalles);
            	gatewayContratos.eliminarConcpetos(cve_contrato, idDetalles);
            }
		 });
	}
	
	public void eliminarConceptos(final Long cve_contrato, final Long[] idDetalles)
	{
		this.gatewayContratos.eliminarConcpetos(cve_contrato, idDetalles);
	}
	
	public void guardarConcepto(Long idDetalle, Long cve_contrato, int idProyecto, String clv_partid, int mes, Double importe){
		this.gatewayContratos.guardarConcepto(idDetalle, cve_contrato, idProyecto, clv_partid, mes, importe);
	}
	
	public void guardarConceptoMovPeredo(final Long idDetalle, final Long cve_contrato, final int idProyecto, final String clv_partid, final int mes, final Double importe){
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
				gatewayContratos.guardarConceptoContratoPeredo(idDetalle, cve_contrato, idProyecto, clv_partid, importe);
				gatewayContratos.guardarConcepto(idDetalle, cve_contrato, idProyecto, clv_partid, mes, importe);
            }
		 });
	}
	
	public String getBeneficiarioContrato(String clv_benefi)
	{
		return gatewayContratos.getBeneficiarioContrato(clv_benefi);
	}
	
	public void guardarAjusteContratoPeredo(Long id_sam_mod_comp, Long cve_contrato, int idProyecto, String clv_partid, String fecha, Double importe)
	{
		gatewayContratos.guardarAjusteContratoPeredo(id_sam_mod_comp, cve_contrato, idProyecto, clv_partid, fecha, importe);
	}
	
	public void eliminarConceptoAjusteContrato(Long id_sam_mod_comp)
	{
		gatewayContratos.eliminarConceptoAjusteContrato(id_sam_mod_comp);
	}
	
	public List<Map> getArchivosContrato(Long cve_contrato)
	{
		return gatewayContratos.getArchivosContrato(cve_contrato);
	}
	
	public void eliminarArchivoContrato(Long idArchivo, HttpServletRequest request)
	{
		gatewayContratos.eliminarArchivoContrato(this.getSesion().getIdUsuario(), idArchivo, request);
	}
}
