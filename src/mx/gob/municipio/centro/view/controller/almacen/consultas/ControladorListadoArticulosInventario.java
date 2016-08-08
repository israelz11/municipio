/**
 * @author ISC. Israel de la Cruz Hernández.
 * @version: 1.0
 * @date: 03-Agosto-2010
 * **/
package mx.gob.municipio.centro.view.controller.almacen.consultas;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayFamiliasArticulos;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayInventario;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBeneficiario;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadMedidas;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;
import mx.gob.municipio.centro.view.controller.almacen.entradas.ControladorEntradasDocumentos;

@Controller
@RequestMapping("/almacen/consultas/lst_articulos.action")

public class ControladorListadoArticulosInventario extends ControladorBaseAlmacen {
	private static Logger log = Logger.getLogger(ControladorListadoArticulosInventario.class.getName());
	
	@Autowired
	GatewayFamiliasArticulos gatewayFamiliasArticulos;
	
	@Autowired
	GatewayTiposDocumentos gatewayTiposDocumentos;
	
	@Autowired
	GatewayBeneficiario gatewayBeneficiario;
	
	@Autowired
	GatewayAlmacen gatewayAlmacen;
	
	@Autowired
	GatewayInventario gatewayInventario;
	
	@Autowired
	GatewayUnidadMedidas gatewayUnidadMedidas;
	
	@Autowired 
	GatewayUnidadAdm gatewayUnidadAdm;
	
	public ControladorListadoArticulosInventario() {
		// TODO Auto-generated method 
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		try
		{
			boolean privilegio = getPrivilegioEn(this.getSesion().getIdUsuario(), 117 );
		modelo.put("cbodependencia", request.getParameter("cbodependencia")==null ? 0: request.getParameter("cbodependencia"));
		modelo.put("id_almacen", request.getParameter("id_almacen")==null ? 0: request.getParameter("id_almacen"));
		modelo.put("id_familia", request.getParameter("id_familia")==null ? 0: request.getParameter("id_familia"));
		modelo.put("id_proveedor", request.getParameter("id_proveedor")==null ? 0: request.getParameter("id_proveedor"));
		modelo.put("id_unidad_medida", request.getParameter("id_unidad_medida")==null ? 0: request.getParameter("id_unidad_medida"));
		modelo.put("descripcion", request.getParameter("descripcion")==null ? "": request.getParameter("descripcion"));
		modelo.put("folio", request.getParameter("folio")==null ? "": request.getParameter("folio"));
		modelo.put("unidad_medida", gatewayUnidadMedidas.getUnidadMedida(modelo.get("id_unidad_medida").toString()));
		
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
		modelo.put("proveedor", gatewayBeneficiario.getBeneficiario2(Long.parseLong(modelo.get("id_proveedor").toString())));
		modelo.put("listadoArticulos", gatewayInventario.getListadoArticulosInventario(modelo));
	    return "almacen/consultas/lst_articulos.jsp";
		}
		catch ( DataAccessException e) {
			log.info(e.getMessage());
			return null;
		}		
	}
	
	
	public List <Map>getAlmacenes(Long idDependencia){
		return gatewayAlmacen.getAlmacenesUnidad(idDependencia);
	}
	
	@ModelAttribute("tiposDocumentos")
    public List<Map> getListaTiposDocumentos(){
    	return gatewayTiposDocumentos.getListadoActivo();
    }
	
	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
	
	@ModelAttribute("familiasArticulos")
	 public List<Map> getListaFamiliasArticulos(){
	    	return gatewayFamiliasArticulos.getListadoActivo();
	}
	
	/*@ModelAttribute("almacenes")
	 public List<Map> getListaAlmacenes(){
		return gatewayAlmacen.getAlmacenesActivos(Integer.parseInt(this.getSesion().getIdUnidad()));
	}*/
	
	public void cancelarArticulosInventario(final List<Long> id_inventario){
		try {                 
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {	 
                	for (Long id :id_inventario)
                		gatewayInventario.cancelarArticulo(id);
                } 
             });
           
            } catch (DataAccessException e) {            
            	 log.info("Los articulos no se pudieron cancelar: "+e.getMessage());	                    
                 throw new RuntimeException(e.getMessage(),e);
            }	      
		
	}
	
}
