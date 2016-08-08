package mx.gob.municipio.centro.view.controller.almacen.salidas;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayEntradasDocumentos;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBeneficiario;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/almacen/salidas/lst_entradas_autorizacion.action")
public class ControladorListadoSalidasAutorizacion extends ControladorBaseAlmacen{

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
	
	public ControladorListadoSalidasAutorizacion() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		boolean privilegio = getPrivilegioEn(this.getSesion().getIdUsuario(), 117 );
		
		modelo.put("cbodependencia", (request.getParameter("cbodependencia")==null ? 0: request.getParameter("cbodependencia")));
		modelo.put("id_entrada", (request.getParameter("id_entrada")==null ? 0: request.getParameter("id_entrada")));
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
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("listadoDocumentos", this.getListadoDocumentosAutorizacion(modelo));
	    return "almacen/salidas/lst_entradas_autorizacion.jsp";
	}
	
	@ModelAttribute("tiposDocumentos")
    public List<Map> getListaTiposDocumentos(){
    	return gatewayTiposDocumentos.getListadoActivo();
    }
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	public List <Map>getAlmacenes(Long idDependencia){
		return gatewayAlmacen.getAlmacenesUnidad(idDependencia);
	}
	/*@ModelAttribute("almacenes")
	 public List<Map> getListaAlmacenes(){
	    	return gatewayAlmacen.getAlmacenesActivos(Integer.parseInt(this.getSesion().getIdUnidad()));
	}*/
	
	//@ModelAttribute("listadoDocumentos")
	public List<Map> getListadoDocumentosAutorizacion(Map parametros){
		String sql = "SELECT ENTRADAS.*, VPROYECTO.N_PROGRAMA, SAM_PEDIDOS_EX.CVE_PED, SAM_PEDIDOS_EX.NUM_PED, CONVERT(varchar(10), ENTRADAS.FECHA,103) AS FECHA_CREACION, (CASE ENTRADAS.STATUS WHEN 1 THEN 'CERRADO' WHEN 0 THEN 'CANCELADO' END) AS STATUS_DESC, CAT_BENEFI.NCOMERCIA FROM ENTRADAS LEFT JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI = ENTRADAS.ID_PROVEEDOR) INNER JOIN VPROYECTO ON (VPROYECTO.ID_PROYECTO = ENTRADAS.ID_PROYECTO) LEFT JOIN SAM_PEDIDOS_EX ON (SAM_PEDIDOS_EX.CVE_PED = ENTRADAS.ID_PEDIDO) ";
		String clausulas = "WHERE ENTRADAS.STATUS IN (1) AND ENTRADAS.FECHA_CIERRE IS NOT NULL AND /*ENTRADAS.ID_ENTRADA NOT IN(SELECT ID_ENTRADA FROM SALIDAS WHERE STATUS=1)*/ (SELECT SUM(M.CANTIDAD) FROM DETALLES_ENTRADAS AS M WHERE M.ID_ENTRADA = ENTRADAS.ID_ENTRADA) > (SELECT ISNULL(SUM(M.CANTIDAD) ,0) FROM DETALLE_SALIDA AS M INNER JOIN SALIDAS AS S ON (S.ID_SALIDA=M.ID_SALIDA) AND M.STATUS=1 AND S.ID_ENTRADA = ENTRADAS.ID_ENTRADA) ";
		if(!parametros.get("cbodependencia").toString().equals("0")) clausulas += " AND ENTRADAS.ID_DEPENDENCIA =:cbodependencia";
		if(!parametros.get("id_almacen").toString().equals("0")) clausulas += " AND ENTRADAS.ID_ALMACEN =:id_almacen";
		if(!parametros.get("id_tipo_documento").toString().equals("0")) clausulas += " AND ENTRADAS.ID_TIPO_DOCUMENTO =:id_tipo_documento";
		if(!parametros.get("id_proveedor").toString().equals("0")) clausulas += " AND ENTRADAS.ID_PROVEEDOR =:id_proveedor";
		if(!parametros.get("id_pedido").toString().equals("")) clausulas += " AND ENTRADAS.ID_PEDIDO =:id_pedido";
		if(!parametros.get("fechaInicial").toString().equals("")&&!parametros.get("fechaFinal").toString().equals("")) clausulas += " AND convert(datetime,convert(varchar(10), ENTRADAS.FECHA ,103),103) BETWEEN :fechaInicial AND :fechaFinal";
		if(!parametros.get("proyecto").toString().equals("")) clausulas += " AND ENTRADAS.PROYECTO =:proyecto";
		if(!parametros.get("partida").toString().equals("")) clausulas += " AND ENTRADAS.PARTIDA =:partida";
		if(!parametros.get("num_documento").toString().equals("")) clausulas += " AND ENTRADAS.DOCUMENTO =:num_documento";
		if(!parametros.get("folio").toString().equals("")) clausulas += " AND ENTRADAS.FOLIO like '%"+parametros.get("folio").toString()+"%' ";
		return this.getNamedJdbcTemplate().queryForList(sql+clausulas+" ORDER BY ENTRADAS.FOLIO ASC", parametros);
	}
	

}
