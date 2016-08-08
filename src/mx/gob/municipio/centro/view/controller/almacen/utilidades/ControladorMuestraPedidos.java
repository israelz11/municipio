package mx.gob.municipio.centro.view.controller.almacen.utilidades;

import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping("/almacen/consultas/muestra_pedidos.action")
public class ControladorMuestraPedidos extends ControladorBaseAlmacen {

	private static Logger log = Logger.getLogger(ControladorMuestraPedidos.class.getName());
	
	public ControladorMuestraPedidos() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		Integer idDependencia = Integer.parseInt(request.getParameter("idDependencia").toString());
		modelo.put("muestraPedidos", this.getListaPedidos(idDependencia));
	    return "almacen/consultas/muestra_pedidos.jsp";
	}
	
	 public List<Map> getListaPedidos(int idDependencia){
		List<Map> LstPedidos = new ArrayList();
		List<Map> Pedidos = this.getJdbcTemplate().queryForList("SELECT CVE_PED, "+
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
			"WHERE SAM_PEDIDOS_EX.STATUS IN(1,4,5) /*CVE_PED NOT IN (SELECT ID_PEDIDO FROM ENTRADAS WHERE STATUS IN(1,4,5))*/ AND CAT_DEPENDENCIAS.ID = ? ORDER BY CVE_PED ASC", new Object[]{idDependencia});
		for(Map Pedido: Pedidos)
		{
			String StatusLote = "Incompleto";
			//Buscar los lotes del pedido original
			List<Map> MovimientosPedido = this.getJdbcTemplate().queryForList("SELECT * FROM SAM_PED_MOVTOS WHERE CVE_PED =?", new Object[]{Pedido.get("CVE_PED")});
			
			//Buscar la sumatoria de lotes del pedido
			List<Map> MovimietosEntrada = this.getJdbcTemplate().queryForList("SELECT E.ID_PEDIDO, DET.ID_PED_MOVTO, SUM(DET.CANTIDAD) AS TCANTIDAD FROM ENTRADAS AS E "+
																				"	INNER JOIN DETALLES_ENTRADAS AS DET ON (DET.ID_ENTRADA = E.ID_ENTRADA) "+
																				" WHERE E.STATUS = 1 AND ID_PEDIDO = ? "+
																				" GROUP BY E.ID_PEDIDO, DET.ID_PED_MOVTO", new Object[]{Pedido.get("CVE_PED")});
			
			//Comparar cada lote del pedido con los encontrado en almacen
			for(Map mov_p: MovimientosPedido)
			{
				for(Map mov_e: MovimietosEntrada)
				{
					if(mov_e.get("ID_PED_MOVTO").toString().equals(mov_p.get("ID_PED_MOVTO").toString()))
					{
						if(mov_e.get("TCANTIDAD").toString().equals(mov_p.get("CANTIDAD").toString()))
							StatusLote = "Completo";
					}
				}
				
			}
			//Lotes incompleto por lo que se mostrara el pedido
			if(StatusLote.equals("Incompleto"))
				LstPedidos.addAll(Pedidos);
			
		}
		
		return LstPedidos;
	    /*return this.getJdbcTemplate().queryForList("SELECT CVE_PED, "+
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
														"WHERE SAM_PEDIDOS_EX.STATUS IN(1,4,5) /*CVE_PED NOT IN (SELECT ID_PEDIDO FROM ENTRADAS WHERE STATUS IN(1,4,5))AND CAT_DEPENDENCIAS.ID = ? ORDER BY CVE_PED ASC", new Object[]{idDependencia});
														*/
		
	}
	

}
