/**
 * @author Ing. Israel de la Cruz Hdez.
 * @version 1.0, Date: 27/Ago/2009
 *
 */

package mx.gob.municipio.centro.view.controller.sam.requisiciones;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
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
@RequestMapping("/sam/requisiciones/lst_req_total.action")

public class ControladorListadoRequisiciones extends ControladorBase {
	
	public ControladorListadoRequisiciones(){}
	private static Logger log = Logger.getLogger(ControladorListadoRequisiciones.class.getName());
		
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	GatewayRequisicion gatewayRequisicion;
	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	@Autowired
	GatewayMeses gatewayMeses;
	@Autowired
	GatewayPlanArbit gatewayPlanArbit;
	
	public final static  int VER_TODAS_LAS_UNIDADES = 25;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  // Cacha por el envio del formulario o envio de parametros
	
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);
		String numreq=request.getParameter("txtrequisicion");
		String listadoReq=request.getParameter("txtlistado");
		String proyecto=request.getParameter("txtproyecto");
		String partida=request.getParameter("txtpartida");
		String clv_benefi=request.getParameter("CVE_BENEFI");
		String beneficiario=request.getParameter("txtprestadorservicio");
		String tipogto=request.getParameter("cbotipogasto")==null ? "0" : request.getParameter("cbotipogasto");
		String unidad=request.getParameter("dependencia")==null ?this.getSesion().getClaveUnidad() : request.getParameter("dependencia");
		String cboconOP = request.getParameter("cboconOP");
		if(privilegio){
			if(request.getParameter("dependencia")==null)
				unidad = "0";
			if(request.getParameter("dependencia")!=null)
				unidad = request.getParameter("dependencia");
		}
		
		if(!privilegio){
			if(request.getParameter("dependencia")==null)
				unidad = this.getSesion().getClaveUnidad();
			if(request.getParameter("dependencia")!=null)
				unidad = request.getParameter("dependencia");
		}
		
		//String estatus=request.getParameter("status")==null ? Integer.toString(gatewayRequisicion.REQ_STATUS_NUEVO): this.arrayToString(request.getParameterValues("status"),",");//Componente status
		String estatus=request.getParameter("cboFilterStatus")==null ? Integer.toString(gatewayRequisicion.REQ_STATUS_NUEVO): this.arrayToString(request.getParameterValues("cboFilterStatus"),",");//Componente status
		String fechaIni=request.getParameter("fechaInicial");
		String fechaFin=request.getParameter("fechaFinal");
		String verUnidad=request.getParameter("verUnidad");
		String listar=request.getParameter("chklistar");
		Integer tipo = request.getParameter("cbotipo") != null ?  request.getParameter("cbotipo").equals("")? null :  Integer.parseInt(request.getParameter("cbotipo")) : null  ;
		modelo.put("idUnidad",unidad);
		modelo.put("cve_pers",this.getSesion().getIdUsuario());
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("tipo", tipo );
		modelo.put("status", estatus );
		modelo.put("cbotipogasto", tipogto);
		modelo.put("txtlistado", listadoReq);
		modelo.put("cboconOP", cboconOP);
		modelo.put("txtprestadorservicio", beneficiario);
		modelo.put("CVE_BENEFI", clv_benefi);
		modelo.put("txtrequisicion", numreq);
		modelo.put("txtproyecto", proyecto);
		modelo.put("txtpartida", partida);
		modelo.put("fechaInicial",fechaIni);
		modelo.put("fechaFinal",fechaFin);
		modelo.put("verUnidad",verUnidad);
		modelo.put("chklistar", listar);
		if(listar==null||listar.equals(""))
			listadoReq = "";
		List <Map> lista = this.getRequisicionesUnidadPorEjemplo(unidad, estatus, fechaIni,fechaFin, this.getSesion().getEjercicio(), tipo, verUnidad, numreq, proyecto, partida, tipogto, clv_benefi, privilegio, cboconOP, listadoReq);
		modelo.put("CONTADOR", lista.size());
		modelo.put("requisicionesUnidad", lista);				
	    return "sam/requisiciones/lst_req_total.jsp";
	}
	
	@ModelAttribute("tipodeGasto")
    public List<Map> getTiposDeGasto(){
    	return gatewayPlanArbit.getTipodeGasto();
    }
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	//Genera el ArrayList del listado de Requisiciones	
    public List<Map> getRequisicionesUnidadPorEjemplo(String unidad , String  estatus,String fechaInicial,String fechaFinal,Integer ejercicio, Integer tipo, String  verUnidad, String numreq, String proyecto, String clv_partid, String tipogto, String beneficiario, boolean privilegio, String cboconOP, String listadoReq){
    	return  gatewayRequisicion.getListaDeRequisicionesPorEjemplo(unidad, estatus, this.formatoFecha(fechaInicial), this.formatoFecha(fechaFinal), ejercicio, tipo, verUnidad, numreq, this.getSesion().getIdUsuario(), this.getSesion().getClaveUnidad(), proyecto, clv_partid, tipogto, beneficiario, privilegio, cboconOP, listadoReq);	
    }
    
    @ModelAttribute("tipoRequisicion")
	public List<Map> getTipoRequisicion(){
		return this.getJdbcTemplate().queryForList("select * from SAM_CAT_TIPO_REQ where Status=1");
	}
	
    /*Metodo para  aperturar las requisiciones seleccionadas*/
	public void aperturarRequisiciones(final List<Long> lst_cve_req){
		for(Long cve_req:lst_cve_req) 
			this.gatewayRequisicion.aperturarRequisicion(cve_req, this.getSesion().getIdUsuario(), this.getSesion().getEjercicio());		
	}
	
	 /*Metodo para  cancelar las requisiciones seleccionadas*/
	public String cancelarRequisiciones(Long[] cve_req){
		return this.gatewayRequisicion.cancelarRequisicion(cve_req, this.getSesion().getIdUsuario());
	}
	
	/*Metodo para obtener datos y usarlos en el cambio de fecha y periodo*/
	public Map getFechaPeriodoRequisicion(Long cve_req){
		return this.gatewayRequisicion.getFechaPeriodoRequisicion(cve_req);
	}
	
	public Map getFechaIngreso(Long cve_req){
		return this.gatewayRequisicion.getFechaIngreso(cve_req);
	}
	
	public boolean cambiarFechaPeriodo(Long cve_req, String fecha, int periodo){
		int cve_pers = this.getSesion().getIdUsuario();
		int ejercicio = this.getSesion().getEjercicio();
		return this.gatewayRequisicion.cambiarFechaPeriodo(cve_req, fecha, periodo, cve_pers, ejercicio);
	}
	
	public String getListUsuarios(int cve_pers){
 		return this.gatewayOrdenDePagos.getListUsuarios(cve_pers);
 	}
	
	public boolean moverRequisiciones(Long[] cve_req, int cve_pers_dest){
		return this.gatewayRequisicion.moverRequisiciones(cve_req, this.getSesion().getIdUsuario(), cve_pers_dest, this.getSesion().getEjercicio());
	}
	
	public Map getBeneficiario(Long cve_doc){
		return gatewayRequisicion.getBeneficiario(cve_doc);
	}
	
	public boolean cambiarBeneficiario(Long cve_req, String clv_benefi){
		return gatewayRequisicion.cambiarBeneficiario(cve_req, clv_benefi, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
	}
	
	public boolean reenumerarLotesDesde(Long cve_req, int num){
		return gatewayRequisicion.reenumerarLotesDesde(cve_req, num, this.getSesion().getIdUsuario(), this.getSesion().getEjercicio());
	}
	
	public void cambiarFechaIngreso(Long cve_req, String fecha){
		this.gatewayRequisicion.cambiarFechaIngreso(cve_req, fecha,  this.getSesion().getIdUsuario(), this.getSesion().getEjercicio());
	}
	
	public Map getReembolsoRequisiciones(Long cve_req){
		return gatewayRequisicion.getReembolsoRequisiciones(cve_req);
	}
	
	public void guardarReembolsoRequisicion(Long cve_ped, Double monto){
		gatewayRequisicion.guardarReembolsoRequisicion(cve_ped, monto);
	}
	
	public void quitarReembolso(Long cve_req){
		gatewayRequisicion.quitarReembolso(cve_req);
	}
	
}
