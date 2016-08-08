/**
 * @author ISC. Israel de la Cruz Hdez.
 * @version 1.0, Date: 21/Jun/2011
 *
 */
package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.gateways.sam.GatewayContratos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import mx.gob.municipio.centro.view.componentes.RMCantidadEnLetras;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sam/reportes/rpt_contrato.action")
public class ControladorReporteContrato extends ControladorBase {

	public ControladorReporteContrato() {
		// TODO Auto-generated method stub

	}
	
	@Autowired
	private RMCantidadEnLetras rmCantidadEnLetras;
	@Autowired
	private GatewayContratos gatewayContratos;
	@Autowired
	private GatewayProyectoPartidas gatewayProyectoPartidas;
	@Autowired 
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	/**
	 * @param args
	 */
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	public ModelAndView  requestGetControlador( Map modelo, @RequestParam("cve_contrato")  Long  cve_contrato ) {		
		modelo = gatewayContratos.getContrato(cve_contrato);
		if(comprobarExistenDocumentos(cve_contrato))
		{
			Map temp = this.CalendarioPresupuestal(cve_contrato);
			modelo.putAll(temp);
		}
		
		List <Map> detalles = this.gatewayContratos.getConceptosContratoAgrupados(cve_contrato);
		
		BigDecimal total_comprobado = new BigDecimal("0.00");
		for(Map row: detalles){
			total_comprobado = total_comprobado.add((BigDecimal) row.get("IMPORTE"));
		}
		
		modelo.put("IMPORTE", total_comprobado);
		
		if(modelo.get("ID_TIPO").equals("7"))
			modelo.put("TIPO_DOC", "PED. No.");
		else
			modelo.put("TIPO_DOC", "O.S. No.");
		
		Integer idGrupo=(Integer)modelo.get("ID_GRUPO");
		gatewayFirmasDocumentos.getFirmasDocumentos(idGrupo, modelo);
		
		modelo.put("TOTAL_COMPROBADO", total_comprobado);
		
		BigDecimal importe = ((BigDecimal)modelo.get("TOTAL_COMPROBADO"));
		String cantidadLetra = convertirALetras(importe);
		
		modelo.put("IMPORTELETRAS", cantidadLetra.substring(1, cantidadLetra.length()-1));
		
		modelo.put("rs",new JRMapCollectionDataSource(detalles));
		return new ModelAndView("rpt_contrato",modelo);
	}		
	
	private boolean comprobarExistenDocumentos(Long cve_contrato){
		return this.getJdbcTemplate().queryForInt("SELECT COUNT(*) FROM VT_SAM_CONTRATO_DOCUMENTOS WHERE CVE_CONTRATO = ?", new Object[]{cve_contrato})>0;
	}

	private Map CalendarioPresupuestal(Long cve_contrato){
		try
		{
			return this.getJdbcTemplate().queryForMap("SELECT "+
															"[CVE_CONTRATO], "+
															"ISNULL([1],0) AS ENERO, "+
															"ISNULL([2],0) AS FEBRERO, "+
															"ISNULL([3],0) AS MARZO, "+
															"ISNULL([4],0) AS ABRIL, "+
															"ISNULL([5],0) AS MAYO, "+
															"ISNULL([6],0) AS JUNIO, "+
															"ISNULL([7],0) AS JULIO, "+
															"ISNULL([8],0) AS AGOSTO, "+
															"ISNULL([9],0) AS SEPTIEMBRE, "+ 
															"ISNULL([10],0) AS OCTUBRE, "+
															"ISNULL([11],0) AS NOVIEMBRE, "+
															"ISNULL([12],0) AS DICIEMBRE "+
													"FROM	( "+
																"SELECT VT.PERIODO, VT.CVE_CONTRATO, SUM(VT.IMPORTE) AS TOTAL  FROM VT_SAM_CONTRATO_DOCUMENTOS AS VT "+
																"GROUP BY VT.PERIODO, VT.CVE_CONTRATO "+
															") AS C PIVOT (SUM(C.TOTAL) FOR C.PERIODO IN ([1],[2],[3],[4],[5],[6],[7],[8],[9],[10],[11],[12])) AS A "+
													"WHERE A.CVE_CONTRATO = ?", new Object[]{cve_contrato});
		}
		catch (DataAccessException e) {                                
		     throw new RuntimeException(e.getMessage(),e);
		}	
		
	}

}
