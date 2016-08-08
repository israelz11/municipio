/**
 * @author ISC. Israel de la Cruz H.
 * @version 1.0
 *
 */

package mx.gob.municipio.centro.view.controller.sam.reportes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/reportes/rpt_relacion_envio.action")
public class ControladorReporteRelaciones extends ControladorBase {

	/**
	 * @param args
	 */
	public ControladorReporteRelaciones() {
		// TODO Auto-generated method stub
	}
	
	@Autowired
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	public ModelAndView  requestGetControlador( Map modelo, @RequestParam("id_relacion")  Long  idRelacion ) {
		String sql = "SELECT CASE MONTH(R.FECHA) "+
					"	WHEN '1' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Enero de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"	WHEN '2' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Febrero de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"	WHEN '3' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Marzo de '+CONVERT(varchar(4),YEAR(R.FECHA)) "+ 
					"	WHEN '4' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Abril de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"	WHEN '5' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Mayo de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"	WHEN '6' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Junio de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"	WHEN '7' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Julio de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"	WHEN '8' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Agosto de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"	WHEN '9' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Septiembre de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"	WHEN '10' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Octubre de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"	WHEN '11' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Noviembre de '+CONVERT(varchar(4),YEAR(R.FECHA))   "+
					"	WHEN '12' THEN CONVERT(varchar(2),DAY(R.FECHA)) + ' de Diciembre de '+CONVERT(varchar(4),YEAR(R.FECHA))  "+
					"END AS FECHA_TEXT, "+
					"(SELECT CD.DEPENDENCIA FROM CAT_DEPENDENCIAS AS CD WHERE CD.ID = R.ID_DEPENDENCIA_DEV) AS DEPENDENCIA_DEV," +
					"PERSONAS.ALIAS, "+
					"R.TIPO_RELACION, " +
					"ISNULL((SELECT SUM(IMPORTE) AS IMP FROM SAM_ORD_PAGO WHERE CVE_OP IN (SELECT CVE_OP FROM SAM_OP_RELACION_DETALLES WHERE ID_RELACION = R.ID_RELACION)),0) AS ACUMULADO  " +
				"FROM SAM_OP_RELACION R "+
				"	 INNER JOIN PERSONAS ON (PERSONAS.CVE_PERS = R.CVE_PERS) WHERE R.ID_RELACION = ? ";
		String[] Meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
		modelo = this.getJdbcTemplate().queryForMap(sql, new Object[]{idRelacion});
		if(modelo.get("TIPO_RELACION").equals("VALES_DEVOLUCION"))
			modelo.put("DEPENDENCIA_DEV", modelo.get("DEPENDENCIA_DEV").toString());
		else if(modelo.get("TIPO_RELACION").equals("DEVOLUCION"))
			modelo.put("DEPENDENCIA_DEV", "DEVOLUCIÓN DE ORDENES DE PAGO A " + modelo.get("DEPENDENCIA_DEV").toString());
		
		gatewayFirmasDocumentos.getFirmasDocumentos(1, modelo);
		List <Map> lst =  new ArrayList();       
		List <Map> lstdetalles = gatewayOrdenDePagos.cargarRelacionesDocumentos(idRelacion);
		String unidad = "";
		for(Map row: lstdetalles){
			if(modelo.get("TIPO_RELACION").equals("DEVOLUCION")||modelo.get("TIPO_RELACION").equals("ENVIO")){
				//calar la unidad administrativa
				Map OP = this.gatewayOrdenDePagos.getOrden(Long.parseLong(row.get("CVE_OP").toString()));
				if(OP.get("TIPO").toString().equals("12"))
				{
					Map temp = this.getJdbcTemplate().queryForMap("SELECT * FROM CAT_DEPENDENCIAS WHERE ID =?", new Object[]{OP.get("ID_DEPENDENCIA")});
					row.put("UNIDADADM", temp.get("CLV_UNIADM").toString()+" "+temp.get("DEPENDENCIA").toString());
					row.put("UNIADM",temp.get("CLV_UNIADM").toString());
				}
				else
				{
					List <Map> U = this.getJdbcTemplate().queryForList("SELECT DISTINCT C.ID_DEPENDENCIA, M.DEPENDENCIA FROM SAM_MOV_OP INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) INNER JOIN CAT_DEPENDENCIAS AS M ON (M.ID = C.ID_DEPENDENCIA) WHERE CVE_OP = ?", new Object[]{row.get("CVE_OP")});
					if(U.size()==1)//si no hay mas de una unidad en op poner una nueva
					{
						Map temp = this.getJdbcTemplate().queryForMap("SELECT DISTINCT M.CLV_UNIADM, M.DEPENDENCIA FROM SAM_MOV_OP INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) INNER JOIN CAT_DEPENDENCIAS AS M ON (M.ID = C.ID_DEPENDENCIA) WHERE CVE_OP = ?", new Object[]{row.get("CVE_OP")});
						row.put("UNIDADADM", temp.get("CLV_UNIADM").toString()+" "+temp.get("DEPENDENCIA").toString());
						row.put("UNIADM",temp.get("CLV_UNIADM").toString());	
					}
					else
					{
						Map temp = this.getJdbcTemplate().queryForMap("SELECT DISTINCT TOP 1 C.CLV_UNIADM, C.UNIDADADM FROM SAM_MOV_OP INNER JOIN VPROYECTO AS C ON (C.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) WHERE CVE_OP = ?", new Object[]{row.get("CVE_OP")});
						row.put("UNIDADADM", temp.get("CLV_UNIADM").toString()+" "+temp.get("UNIDADADM").toString());
						row.put("UNIADM",temp.get("CLV_UNIADM").toString());
					}
				}
			}
			else{
				Map temp = this.getJdbcTemplate().queryForMap("SELECT TOP 1 U.CLV_UNIADM, U.DEPENDENCIA FROM SAM_MOV_VALES AS V INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = V.ID_PROYECTO) INNER JOIN CAT_DEPENDENCIAS AS U ON (U.ID = C.ID_DEPENDENCIA) WHERE V.CVE_VALE = ?", new Object[]{row.get("CVE_OP")});
				row.put("UNIDADADM", temp.get("CLV_UNIADM").toString()+" "+temp.get("DEPENDENCIA").toString());
				row.put("UNIADM",temp.get("CLV_UNIADM").toString());
			}
			lst.add(row);
			
			//modelo.put("ACUMULADO", row.get("ACUMULADO"));
		}
		
		modelo.put("rs",new JRMapCollectionDataSource(lst));
		
		if(modelo.get("TIPO_RELACION").equals("ENVIO"))
			return new ModelAndView("rpt_relacion_envio",modelo);
		else if(modelo.get("TIPO_RELACION").equals("DEVOLUCION"))
		{
			return new ModelAndView("rpt_relacion_devolucion",modelo);
		}
		else if(modelo.get("TIPO_RELACION").equals("VALES_DEVOLUCION"))
		{
			return new ModelAndView("rpt_relacion_devolucion_vales",modelo);
		}
		else{
			sql = "SELECT ISNULL(SUM(IMPORTE),0) AS ACUMULADO FROM SAM_MOV_VALES WHERE SAM_MOV_VALES.CVE_VALE IN(SELECT CVE_OP FROM SAM_OP_RELACION_DETALLES WHERE ID_RELACION = ?) ";
			Map temp = this.getJdbcTemplate().queryForMap(sql, new Object[]{idRelacion});
			modelo.put("ACUMULADO", temp.get("ACUMULADO"));
			return new ModelAndView("rpt_relacion_vales",modelo);
		}
			
			
	}		

}
