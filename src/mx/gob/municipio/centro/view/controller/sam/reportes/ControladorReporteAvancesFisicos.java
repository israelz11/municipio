/**
 * @author Isc. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 01/Noviembre/2011
 */
package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mx.gob.municipio.centro.model.gateways.sam.GatewayEvaluacionProyecto;
import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayMeses;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/reportes/rpt_avances_fisicos.action")
public class ControladorReporteAvancesFisicos extends ControladorBase{
	
	@Autowired
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	@Autowired
	GatewayEvaluacionProyecto gatewayEvaluacionProyecto;
	@Autowired
	GatewayMeses gatewayMeses;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	
	public ModelAndView  requestGetControlador( Map modelo, HttpServletRequest request, @RequestParam("cbomes") int mes, @RequestParam("cbostatus") String  estatus) {
		
		Integer  cve_unidad = (request.getParameter("cbodependencia")!=null)? Integer.parseInt(request.getParameter("cbodependencia").toString()): 0;
		if(estatus==null) estatus = "3";
		if(mes==0) mes = gatewayMeses.getMesActivo(this.getSesion().getEjercicio());
		
		if(cve_unidad==0) cve_unidad = Integer.parseInt(this.getSesion().getIdUnidad());
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		String fecha = dateFormat.format(date); 
		modelo.put("FECHA", fecha);
		
		int idGrupo = this.getJdbcTemplate().queryForInt("SELECT B.ID_GRUPO_CONFIG FROM SAM_GRUPO_CONFIG_USUARIO A JOIN  SAM_GRUPO_CONFIG B ON A.ID_GRUPO_CONFIG = B.ID_GRUPO_CONFIG and ID_USUARIO=? where b.ESTATUS='ACTIVO' and B.TIPO='FIRMA' AND A.ASIGNADO =1", new Object[]{this.getSesion().getIdUsuario()});
		if(idGrupo!=0)
			gatewayFirmasDocumentos.getFirmasDocumentos(idGrupo, modelo);
		
		modelo = this.getJdbcTemplate().queryForMap("SELECT top 1 (SELECT CLV_UNIADM+' '+DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID =?) AS UNIDAD_SOLICITANTE, (CASE (?) WHEN 1 THEN 'Enero' WHEN 2 THEN 'Febrero' WHEN 3 THEN 'Marzo' WHEN 4 THEN 'Abril' WHEN 5 THEN 'Mayo' WHEN 6 THEN 'Junio' WHEN 7 THEN 'Julio' WHEN 8 THEN 'Agosto' WHEN 9 THEN 'Septiembre' WHEN 10 THEN 'Octubre' WHEN 11 THEN 'Noviembre' WHEN 12 THEN 'Diciembre' END) AS MES_DESCRIPCION "+
															"FROM VT_EVAL_PROY_MENSUAL AS V "+
															"	INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = V.ID_PROYECTO) "+
															"WHERE V.ID_PROYECTO IN (SELECT ID_PROYECTO FROM VT_PROYECTOS_USUARIOS WHERE ID_USUARIO = ?)", new Object[]{cve_unidad, mes, this.getSesion().getIdUsuario()});
		List <Map> dt = gatewayEvaluacionProyecto.getProyectos(this.getSesion().getIdUsuario(), estatus, cve_unidad, mes, this.getSesion().getClaveUnidad()); 
		
		modelo.put("rs",new JRMapCollectionDataSource(dt));		
		return new ModelAndView("rpt_avances_fisicos",modelo);
	}

}
