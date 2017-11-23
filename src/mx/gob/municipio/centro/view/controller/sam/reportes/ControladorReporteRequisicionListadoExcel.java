package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayMovimientosRequisicion;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayReportePresupestoDisp;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/reportes/rpt_listado_requisicionesExcel.xls")
public class ControladorReporteRequisicionListadoExcel extends ControladorBase {

	private static Logger log = Logger.getLogger(ControladorReporteRequisicionListadoExcel.class.getName());
		public ControladorReporteRequisicionListadoExcel(){
			
		}
		
		@Autowired
		GatewayFirmasDocumentos gatewayFirmasDocumentos;
		@Autowired
		GatewayRequisicion gatewayRequisicion;
		@Autowired
		GatewayMovimientosRequisicion gatewayMovimientosRequisicion;
		@Autowired
		GatewayProyectoPartidas gatewayProyectoPartidas;
		
		@SuppressWarnings("unchecked")
		@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})     
		public ModelAndView  requestGetControlador( Map modelo, HttpServletRequest request) {
			String listadoReq=request.getParameter("txtlistado");
			String temp = "";
			if(listadoReq!=null&&!listadoReq.equals("")){
				String[] arreglo = listadoReq.split(",");
				for(String item: arreglo){
					temp+="'"+item.trim()+"',";
				}
				temp = temp.substring(0, temp.length()-1)+")";

			}
			
			List <Map> listado = this.getJdbcTemplate().queryForList("SELECT SAM_REQ_MOVTOS.ID_REQ_MOVTO, SAM_REQ_MOVTOS.CVE_REQ, SAM_REQUISIC.NUM_REQ, SAM_REQ_MOVTOS.REQ_CONS, CANTIDAD_TEMP, CANTIDAD, NOTAS, SAM_REQ_MOVTOS.STATUS, SAM_CAT_ARTICULO.DESCRIPCION, SAM_CAT_ARTICULO.DESCRIPCION AS ARTICULO, CAT_UNIMED.UNIDMEDIDA AS UNIDAD, PRECIO_EST, (CANTIDAD*PRECIO_EST) AS IMPORTE, isnull(SAM_REQ_ANEXO.TEXTO, '') AS TEXTO  FROM SAM_REQ_MOVTOS " + 
					"INNER JOIN SAM_REQUISIC ON (SAM_REQUISIC.CVE_REQ = SAM_REQ_MOVTOS.CVE_REQ) "+
					"LEFT JOIN SAM_CAT_PROD ON (SAM_CAT_PROD.ID_ARTICULO = SAM_REQ_MOVTOS.ID_ARTICULO)"+
					"LEFT JOIN SAM_REQ_ANEXO ON (SAM_REQ_ANEXO.ID_REQ_MOVTO = SAM_REQ_MOVTOS.ID_REQ_MOVTO) "+
					"LEFT JOIN SAM_CAT_ARTICULO ON (SAM_CAT_ARTICULO.ID_CAT_ARTICULO = SAM_CAT_PROD.ID_CAT_ARTICULO)"+ 
					"INNER JOIN CAT_UNIMED ON (CAT_UNIMED.CLV_UNIMED = SAM_REQ_MOVTOS.CLV_UNIMED) WHERE SAM_REQUISIC.NUM_REQ IN("+temp+" ORDER BY SAM_REQUISIC.NUM_REQ, REQ_CONS ASC");
			modelo.put("rs",new JRMapCollectionDataSource(listado));
			return new ModelAndView("rpt_listado_requisicionesExcel",modelo);
		}		
}
