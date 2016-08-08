package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayFacturas;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sam/consultas/muestra_pedidos_facturas.action")
public class ControladorMuestraPedidosFacturas extends ControladorBase {

	@Autowired
	GatewayFacturas gatewayFacturas;
	
	private static Logger log = Logger.getLogger(ControladorMuestraPedidosFacturas.class.getName());
	
	public ControladorMuestraPedidosFacturas() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Integer idDependencia = Integer.parseInt(request.getParameter("idDependencia").toString());
		modelo.put("CVE_PED", request.getParameter("CVE_PED"));
		modelo.put("NUM_PED", request.getParameter("NUM_PED"));
		modelo.put("CLV_BENEFI", request.getParameter("CLV_BENEFI"));
		modelo.put("muestraPedidos", this.getListaPedidos(idDependencia));
		if(request.getParameter("CVE_PED")!=null)
		{
			modelo.put("listaEntradas", gatewayFacturas.getListEntradasFactura(Long.parseLong(request.getParameter("CVE_PED").toString())));
		}
		modelo.put("idDependencia", idDependencia);
		
	    return "sam/consultas/muestra_pedidos_facturas.jsp";
	}
	
	 public List<Map> getListaPedidos(int idDependencia){
	    return this.getJdbcTemplate().queryForList("SELECT CVE_PED, "+
															"NUM_PED,  "+
															"SAM_PEDIDOS_EX.CVE_REQ, "+ 
															"SAM_REQUISIC.ID_PROYECTO, "+ 
															"CEDULA_TEC.N_PROGRAMA,  "+
															"SAM_REQUISIC.CLV_PARTID,  "+
															"SAM_PEDIDOS_EX.CLV_BENEFI,  "+
															"CAT_BENEFI.NCOMERCIA,  "+
															"SAM_REQUISIC.ID_DEPENDENCIA,  "+
															"CAT_DEPENDENCIAS.DEPENDENCIA,  "+
															"TOTAL,  "+
															"NOTAS,  "+
															"convert(varchar(10), FECHA_PED ,103) FECHA_PED  "+
														"FROM SAM_PEDIDOS_EX  "+
															"INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_PEDIDOS_EX.CVE_REQ)  "+
															"INNER JOIN CEDULA_TEC ON (CEDULA_TEC.ID_PROYECTO=SAM_REQUISIC.ID_PROYECTO)  "+
															"INNER JOIN CAT_BENEFI ON (CAT_BENEFI.CLV_BENEFI=SAM_PEDIDOS_EX.CLV_BENEFI)  "+
															"INNER JOIN CAT_DEPENDENCIAS ON (CAT_DEPENDENCIAS.ID = SAM_REQUISIC.ID_DEPENDENCIA)  "+
														"WHERE SAM_PEDIDOS_EX.STATUS IN(1, 4) " +
														/*"AND (SELECT COUNT(*) FROM SAM_FACTURAS WHERE STATUS IN(1, 3) AND CVE_PED = SAM_PEDIDOS_EX.CVE_PED)=0 " +*/
														/*"AND SAM_PEDIDOS_EX.CVE_PED IN (SELECT CVE_DOC FROM VT_COMPROMISOS WHERE TIPO_DOC ='PED' AND CONSULTA ='COMPROMETIDO' AND CVE_DOC = SAM_PEDIDOS_EX.CVE_PED AND MONTO >0) "*/
														"AND CAT_DEPENDENCIAS.ID = ? ORDER BY CVE_PED ASC", new Object[]{idDependencia});
	}
	

}
