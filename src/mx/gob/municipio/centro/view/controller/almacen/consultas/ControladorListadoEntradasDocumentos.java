/**
 * @author ISC. Israel de la Cruz Hernández.
 * @version: 1.0
 * @date: 14-July-2010
 * **/
package mx.gob.municipio.centro.view.controller.almacen.consultas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayEntradasDocumentos;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBeneficiario;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/consultas/lst_entradas.action")
public class ControladorListadoEntradasDocumentos extends ControladorBaseAlmacen {

	@Autowired
	GatewayTiposDocumentos gatewayTiposDocumentos;
	
	@Autowired
	GatewayAlmacen gatewayAlmacen;
	
	@Autowired
	GatewayBeneficiario gatewayBeneficiario;
	
	@Autowired
	GatewayEntradasDocumentos gatewayEntradasDocumentos;
	
	@Autowired 
	GatewayUnidadAdm gatewayUnidadAdm;
	
	public ControladorListadoEntradasDocumentos() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		boolean privilegio = getPrivilegioEn(this.getSesion().getIdUsuario(), 117 );
		
		modelo.put("id_entrada", (request.getParameter("id_entrada")==null ? 0: request.getParameter("id_entrada")));
		modelo.put("cbodependencia", request.getParameter("cbodependencia")==null ? 0: request.getParameter("cbodependencia"));
		modelo.put("id_almacen", request.getParameter("id_almacen")==null ? 0: request.getParameter("id_almacen"));
		modelo.put("id_tipo_documento", request.getParameter("id_tipo_documento")==null ? 0: request.getParameter("id_tipo_documento"));
		modelo.put("id_proveedor", request.getParameter("id_proveedor")==null ? 0: request.getParameter("id_proveedor"));
		modelo.put("proveedor", gatewayBeneficiario.getBeneficiario2(Long.parseLong(modelo.get("id_proveedor").toString())));
		modelo.put("id_pedido", request.getParameter("id_pedido")==null ? "": request.getParameter("id_pedido"));
		modelo.put("fechaInicial", request.getParameter("fechaInicial")==null ? "": request.getParameter("fechaInicial"));
		modelo.put("fechaFinal", request.getParameter("fechaFinal")==null ? "": request.getParameter("fechaFinal"));
		modelo.put("proyecto", request.getParameter("proyecto")==null ? "": request.getParameter("proyecto"));
		modelo.put("partida", request.getParameter("partida")==null ? "": request.getParameter("partida"));
		modelo.put("num_documento", request.getParameter("num_documento")==null ? "": request.getParameter("num_documento"));
		modelo.put("folio", request.getParameter("folio")==null ? "": request.getParameter("folio"));
		
		if(privilegio){
			if(request.getParameter("cbodependencia")==null)
				modelo.put("cbodependencia",0);
			if(request.getParameter("cbodependencia")!=null)
				modelo.put("cbodependencia",request.getParameter("cbodependencia"));
		}
		
		if(!privilegio){
			if(request.getParameter("cbodependencia")==null)
				modelo.put("cbodependencia",this.getSesion().getClaveUnidad());
			if(request.getParameter("cbodependencia")!=null)
				modelo.put("cbodependencia",request.getParameter("cbodependencia"));
		}
		
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		
		if(!modelo.get("cbodependencia").toString().equals("0")){
			modelo.put("almacenes", getAlmacenes(Long.parseLong(modelo.get("cbodependencia").toString())));
		}
		
		modelo.put("listadoDocumentos", this.gatewayEntradasDocumentos.getListadoDocumentos(modelo));
	    return "almacen/consultas/lst_entradas.jsp";
	}
	
	@ModelAttribute("tiposDocumentos")
    public List<Map> getListaTiposDocumentos(){
    	return gatewayTiposDocumentos.getListadoActivo();
    }
	
	
	public List <Map>getAlmacenes(Long idDependencia){
		return gatewayAlmacen.getAlmacenesUnidad(idDependencia);
	}
	/*@ModelAttribute("almacenes")
	 public List<Map> getListaAlmacenes(){
	    	return gatewayAlmacen.getAlmacenesActivos(Integer.parseInt(this.getSesion().getIdUnidad()));
	}*/
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	//@ModelAttribute("listadoDocumentos")
	
	
	public void cancelarDocumento(Long id_entrada){
		this.gatewayEntradasDocumentos.cancelarEntradaDocumento(id_entrada, this.getSesion().getIdUsuario());
	}
	
	public void aperturarEntrada(Long[] id_entrada){
		this.gatewayEntradasDocumentos.aperturarEntradas(id_entrada);
	}
	
	public void cancelarEntrada(Long[] id_entrada){
		this.gatewayEntradasDocumentos.cancelarEntradas(id_entrada);
	}
	
	public void validarEntrada(Long idEntrada){
		gatewayEntradasDocumentos.validarEntrada(idEntrada);
	}
}
