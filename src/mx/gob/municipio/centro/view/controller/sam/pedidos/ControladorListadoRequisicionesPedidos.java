package mx.gob.municipio.centro.view.controller.sam.pedidos;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/pedidos/lst_req_captura.action")
public class ControladorListadoRequisicionesPedidos extends ControladorBase {

	public ControladorListadoRequisicionesPedidos(){}
	
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired
	GatewayRequisicion gatewayRequisicion;
	
	@Autowired
	GatewayPlanArbit gatewayPlanArbit;
	
	final String STATUS_NUEVA="0";
	final String STATUS_PENDIENTE="1";
	
	/*Metodo de errores desactivado*/
	@SuppressWarnings("unchecked")
	/*Mapeo para la pagina de donde se recibira los GET*/ 
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		String numreq=request.getParameter("txtrequisicion");
		String proyecto=request.getParameter("txtproyecto");
		String partida=request.getParameter("txtpartida");
		String clv_benefi=request.getParameter("CVE_BENEFI");
		String beneficiario=request.getParameter("txtprestadorservicio");
		String tipogto=request.getParameter("cbotipogasto")==null ? "0" : request.getParameter("cbotipogasto");
		modelo.put("cbotipogasto", tipogto);
		modelo.put("txtprestadorservicio", beneficiario);
		modelo.put("CVE_BENEFI", clv_benefi);
		modelo.put("txtrequisicion", numreq);
		modelo.put("txtproyecto", proyecto);
		modelo.put("txtpartida", partida);
		modelo.put("idUnidad",request.getParameter("cbodependencia"));
		modelo.put("cbodependencia", request.getParameter("cbodependencia"));
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("status", (request.getParameter("status"))==null ? STATUS_PENDIENTE: request.getParameter("status").toString());
		modelo.put("fechaInicial", (request.getParameter("fechaInicial")==null) ? "":request.getParameter("fechaInicial"));
		modelo.put("fechaFinal", (request.getParameter("fechaFinal")==null) ? "":request.getParameter("fechaFinal"));
		if(modelo.get("status")==null) modelo.put("status", STATUS_NUEVA);
		if(modelo.get("idUnidad")==null) modelo.put("idUnidad", 0); 	
		modelo.put("requisicionesUnidad",this.getRequisiciones(modelo.get("idUnidad").toString(), modelo.get("status").toString(), modelo.get("fechaInicial").toString(), modelo.get("fechaFinal").toString(), this.getSesion().getEjercicio(), numreq, proyecto, partida, tipogto) );
		if (this.getSesion().getIdGrupo() == null){
			modelo.put("mensaje","El usuario no tiene asignado un grupo de firmas ");
			return "insuficientes_permisos.jsp";
		}
	    return "sam/pedidos/lst_req_captura.jsp";
	}
	
	@ModelAttribute("tipodeGasto")
    public List<Map> getTiposDeGasto(){
    	return gatewayPlanArbit.getTipodeGasto();
    }
	
	public List<Map> getRequisiciones(String unidad , String estatus,String fechaInicial,String fechaFinal,Integer ejercicio, String numreq, String proyecto, String partida, String tipogto){
    	return  gatewayRequisicion.getListaDeRequisicionesPedidos(unidad, estatus, this.formatoFecha(fechaInicial), this.formatoFecha(fechaFinal), ejercicio,  this.getSesion().getIdUsuario(), numreq, proyecto, partida, tipogto);	
    }
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	 @ModelAttribute("tipoRequisicion")
	public List<Map> getTipoRequisicion(){
		return this.getJdbcTemplate().queryForList("select * from SAM_CAT_TIPO_REQ where Status=1");
	}
	 
		
}
