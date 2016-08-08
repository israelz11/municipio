package mx.gob.municipio.centro.view.controller.almacen.utilidades;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import mx.gob.municipio.centro.view.bases.ControladorBaseAlmacen;

@Controller
@RequestMapping("/almacen/consultas/muestra_entradas.action")
public class ControladorMuestraEntradas extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorMuestraEntradas.class.getName());
	
	public ControladorMuestraEntradas() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Integer idDependencia = Integer.parseInt(request.getParameter("idDependencia").toString());
		modelo.put("muestraPedidos", this.getListaPedidos(idDependencia));
	    return "almacen/consultas/muestra_entradas.jsp";
	}
	
	 public List<Map> getListaPedidos(int idDependencia){
	    return this.getJdbcTemplate().queryForList("SELECT ID_ENTRADA, FOLIO, CONVERT(varchar(10),FECHA,103) AS FECHA, DESCRIPCION, NUM_PED, ID_PEDIDO FROM ENTRADAS INNER JOIN  SAM_PEDIDOS_EX ON (SAM_PEDIDOS_EX.CVE_PED = ID_PEDIDO) WHERE ID_DEPENDENCIA =? AND ENTRADAS.STATUS=1 AND ID_ENTRADA_AGREGADA IS NULL AND ID_ENTRADA_CANCELADA IS NULL", new Object[]{idDependencia});
	}
}
