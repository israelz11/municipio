package mx.gob.municipio.centro.view.controller.almacen.entradas;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.almacen.GatewayAlmacen;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayFamiliasArticulos;
import mx.gob.municipio.centro.model.gateways.almacen.GatewayTiposDocumentos;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/entradas/configura_entradaArticulos.action")
public class ControladorConfiguracionArticulosAlmacen extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorConfiguracionArticulosAlmacen.class.getName());
	
	@Autowired
	GatewayFamiliasArticulos gatewayFamiliasArticulos;
	
	@Autowired
	GatewayTiposDocumentos gatewayTiposDocumentos;
	
	@Autowired
	GatewayAlmacen gatewayAlmacen;
	
	public ControladorConfiguracionArticulosAlmacen() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		modelo.put("id_inventario", (request.getParameter("id_inventario")==null ? 0: request.getParameter("id_inventario")));
		modelo.put("inventario", this.getProductoAlmacen(Long.parseLong(modelo.get("id_inventario").toString())));
	    return "almacen/entradas/configura_entradaArticulos.jsp";
	}
	
	@ModelAttribute("tiposDocumentos")
    public List<Map> getListaTiposDocumentos(){
    	return gatewayTiposDocumentos.getListadoActivo();
    }
	
	@ModelAttribute("familiasArticulos")
	 public List<Map> getListaFamiliasArticulos(){
	    	return gatewayFamiliasArticulos.getListadoActivo();
	}
	
	@ModelAttribute("almacenes")
	 public List<Map> getListaAlmacenes(){
		return gatewayAlmacen.getAlmacenesActivos(Integer.parseInt(this.getSesion().getIdUnidad()));
	}
	
	public Map getProductoAlmacen(Long id_inventario){
		try
		{
			return this.getJdbcTemplate().queryForMap("SELECT INVENTARIO.*, CAT_DEPENDENCIAS.DEPENDENCIA, "+
																			"ALMACEN.DESCRIPCION AS ALMACEN, "+
																			"FAMILIAS.DESCRIPCION AS FAMILIA, "+
																			"SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, "+
																			"CAT_BENEFI.NCOMERCIA AS PROVEEDOR,"+
																			"CONVERT(varchar(10), INVENTARIO.FECHA,103) AS FECHA, "+
																			"(CASE INVENTARIO.STATUS WHEN 1 THEN 'ACTIVO' WHEN 0 THEN 'CANCELADO' END) AS STATUS_DESC,"+ 
																			"(CASE INVENTARIO.ALARMA WHEN 1 THEN 'ENCENDIDA' WHEN 0 THEN 'APAGADA' END) AS ALARMA_DESC, "+
																			"CAT_BENEFI.NCOMERCIA, CAT_UNIMED.UNIDMEDIDA AS UNIDAD_MEDIDA "+
																		"FROM INVENTARIO "+
																			"LEFT JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = INVENTARIO.ID_DEPENDENCIA) "+
																			"LEFT JOIN ALMACEN ON (ALMACEN.ID_ALMACEN = INVENTARIO.ID_ALMACEN) "+
																			"LEFT JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI = INVENTARIO.ID_PROVEEDOR) "+
																			"LEFT JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = INVENTARIO.ID_UNIDAD_MEDIDA) "+
																			"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = INVENTARIO.ID_ARTICULO) "+
																			"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO) "+
																			"LEFT JOIN FAMILIAS ON (FAMILIAS.ID_FAMILIA = INVENTARIO.ID_FAMILIA) WHERE INVENTARIO.ID_INVENTARIO = ?",new Object[]{id_inventario});
		}
	 catch (DataAccessException e) {
		 log.info(e.getMessage());
		 return null;
	 }
	}
	
	public boolean guardarArticuloAlmacen(Long id_inventario, int id_familia, long id_proveedor, String id_unidad_medida, Double precio, int existencia_minima, int alarma, int status){
		try
		{
			this.getJdbcTemplate().update("UPDATE INVENTARIO SET ID_FAMILIA = ?, ID_PROVEEDOR = ?, ID_UNIDAD_MEDIDA = ?, PRECIO = ?, EXISTENCIA_MINIMA = ?, ALARMA = ?, STATUS = ? WHERE ID_INVENTARIO = ?" ,new Object[]{ id_familia, id_proveedor, id_unidad_medida, precio, existencia_minima, alarma, status, id_inventario });
			return true;
		}
	 catch (DataAccessException e) {
		 log.info(e.getMessage());
		 return false;
	 }
	}

}
