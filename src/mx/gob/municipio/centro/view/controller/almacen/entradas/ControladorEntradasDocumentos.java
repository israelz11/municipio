/**
 * @author ISC. Israel de la Cruz Hernández.
 * @version: 1.0
 * @date: 01-July-2010
 * **/

package mx.gob.municipio.centro.view.controller.almacen.entradas;

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
import mx.gob.municipio.centro.model.gateways.almacen.GatewayEntradasDocumentos;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayFamiliasArticulos;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPedidos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;
import mx.gob.municipio.centro.view.controller.almacen.configuracion.ControladorFamiliasArticulos;

@Controller
@RequestMapping("/almacen/entradas/captura_documentos.action")
public class ControladorEntradasDocumentos extends ControladorBaseAlmacen  {

	private static Logger log = Logger.getLogger(ControladorEntradasDocumentos.class.getName());
	
	@Autowired
	GatewayFamiliasArticulos gatewayFamiliasArticulos;
	
	@Autowired
	GatewayTiposDocumentos gatewayTiposDocumentos;
	
	@Autowired
	GatewayAlmacen gatewayAlmacen;
	
	@Autowired
	GatewayPedidos gatewayPedidos;
	
	@Autowired
	GatewayEntradasDocumentos gatewayEntradasDocumentos;
	
	@Autowired 
	GatewayUnidadAdm gatewayUnidadAdm;
	
	public ControladorEntradasDocumentos() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		boolean privilegio = getPrivilegioEn(this.getSesion().getIdUsuario(), 117 );
		
		modelo.put("id_entrada", (request.getParameter("id_entrada")==null ? 0: request.getParameter("id_entrada")));
		Map m = this.getEntradaDocumento(Long.parseLong(modelo.get("id_entrada").toString()));
		
		if(m!=null)
		{
			if(m.get("ID_ENTRADA_CANCELADA")!=null)
				m.put("FOLIO_CANCELADO", (String)this.getJdbcTemplate().queryForObject("SELECT FOLIO FROM ENTRADAS WHERE ID_ENTRADA = ?", new Object[]{m.get("ID_ENTRADA_CANCELADA")}, String.class));
			if(m.get("ID_ENTRADA_AGREGADA")!=null)
				m.put("FOLIO_AGREGADO", (String)this.getJdbcTemplate().queryForObject("SELECT FOLIO FROM ENTRADAS WHERE ID_ENTRADA = ?", new Object[]{m.get("ID_ENTRADA_AGREGADA")}, String.class));
		}
			modelo.put("documento", m);
			modelo.put("cbodependencia",(!modelo.get("id_entrada").toString().equals("0")) ? m.get("ID_DEPENDENCIA").toString(): 0);
		
		
		if(privilegio){
			if(request.getParameter("cbodependencia")==null)
				modelo.put("cbodependencia",0);
			if(request.getParameter("cbodependencia")!=null)
				modelo.put("cbodependencia",request.getParameter("cbodependencia"));
			
			if(!modelo.get("id_entrada").toString().equals("0")){
				modelo.put("cbodependencia",  m.get("ID_DEPENDENCIA").toString());
			}
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
		
		modelo.put("tiposEntrada", this.getTiposEntradas());
	    return "almacen/entradas/captura_documentos.jsp";
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
	
	public List <Map>getAlmacenes(Long idDependencia){
		return gatewayAlmacen.getAlmacenesUnidad(idDependencia);
	}
	
	 public List<Map> getTiposEntradas(){
	    	return gatewayTiposDocumentos.getTiposEntradas();
	}
	
	public Long guardarEntradaDocumento(Long id_entrada, int id_dependencia, int id_almacen, String id_proveedor, Long num_ped, int id_tipo_documento, String num_documento, String proyecto, String partida, String descripcion, String fecha_documento, int tipoEntrada, Double subtotal, Double descuento, Double iva, int tipoIva, int tipoEfecto, int movimiento, Long idEntrada2){
		return gatewayEntradasDocumentos.guardarEntradaDocumento(id_entrada, id_dependencia, id_almacen, id_proveedor, num_ped, id_tipo_documento, num_documento, proyecto, partida, descripcion, this.formatoFecha(fecha_documento), tipoEntrada, subtotal, descuento, iva, tipoIva, this.getSesion().getIdUsuario(), tipoEfecto, movimiento, idEntrada2);
	}
	
	public Map getEntradaDocumento(Long id_entrada){
		return this.gatewayEntradasDocumentos.getEntradaDocumento(id_entrada);
	}
	
	public List <Map> getConceptos(Long cve_ped){
		return  this.gatewayEntradasDocumentos.getConceptos(cve_ped);
	}
	
	public Map getConcepto(Long id_detalle_entrada){
		return this.gatewayEntradasDocumentos.getConcepto(id_detalle_entrada);
	}
	
	public boolean guardarDetallesEntradaDocumentos(Long id_entrada, Long id_detalle_entrada, int id_articulo, String id_unidad_medida, int id_familia, double cantidad, double precio, String descripcion){
		return this.gatewayEntradasDocumentos.guardarDetallesEntradaDocumentos(id_entrada, id_detalle_entrada, id_articulo, id_unidad_medida, id_familia, cantidad, precio, descripcion);
	}
	
	/*Metodo para  eliminar los conceptos del documento*/
	public void eliminarConceptos(final List<Long> lst_id_detalle){
		try {    
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
                	for (Long id :lst_id_detalle) 
                		gatewayEntradasDocumentos.eliminarConceptos(id);
                } 
             });
           
            } catch (DataAccessException e) {            
            	 log.info(e.getMessage());      
                 throw new RuntimeException(e.getMessage(),e);
            }	               
	}
	
	public String cerrarEntradaDocumento(Long id_entrada){
		return this.gatewayEntradasDocumentos.cerrarEntradaDocumento(id_entrada);
	}
	
	public void guardarCantidadDetalles(Long id_entrada, Long[] ids, Double[] cantidades, Double subtotal, Double descuento, Double iva, int tipoIva){
		this.gatewayEntradasDocumentos.guardarCantidadDetalles(id_entrada, ids, cantidades, subtotal, descuento, iva, tipoIva);
	}
	
	public String getFolioEntrada(Long idEntrada){
		return (String) this.getJdbcTemplate().queryForObject("SELECT FOLIO FROM ENTRADAS WHERE ID_ENTRADA =?", new Object[]{idEntrada}, String.class);
	}

}
