/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/muestra_productos.action")

public class ControladorProductos extends ControladorBase {
	private static Logger log = Logger.getLogger(ControladorProductos.class.getName());
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)    
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {	
        HttpSession miSesion = request.getSession(true);
        
		String  producto = request.getParameter("producto");
		int  partida = (Integer.parseInt(request.getParameter("partida")));				
		modelo.put("muestraProductos",getProductos(producto, partida));
	    return "sam/consultas/muestra_productos.jsp";
	}
	
	public ControladorProductos(){}
	
	public List<Map> getProductos(String producto, Integer partida ){		
		Map parametros = new HashMap();
		parametros.put("producto", "%"+producto+"%");
		parametros.put("partida", partida);
		//parametros.put("grupo", "%"+grupo+"%");
		//parametros.put("subgrupo","%"+subgrupo+"%");
		//parametros.put("consecutivo", "%"+consecutivo+"%");
		String SQL ="" ;
		if (producto!=null && !producto.equals(""))
			SQL= " AND ( AR.DESCRIPCION like :producto) ";
				
		return this.getNamedJdbcTemplate().queryForList("SELECT     p.ID_ARTICULO, m.UNIDMEDIDA,  p.SUBGRUPO, p.GRUPO, AR.DESCRIPCION DESCRIP , AR.CLV_UNIMED, AR.ULT_PRECIO "+
					  " FROM  SAM_CAT_ARTICULO AR INNER JOIN  CAT_UNIMED AS m ON AR.CLV_UNIMED = m.CLV_UNIMED INNER JOIN "+
                      " SAM_CAT_PROD AS p ON AR.ID_CAT_ARTICULO = p.ID_CAT_ARTICULO "+
" where  P.GRUPO=:partida and P.SUBGRUPO!='000' AND  P.ESTATUS='ACTIVO' AND  AR.ESTATUS='ACTIVO'  "+SQL+"  order by  P.SUBGRUPO "  ,parametros);
	}
	
}
