/**
 * @author Isc. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 08/Noviembre/2011
 */
package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/reportes/rpt_listado_requisiciones.action")
public class ControladorReporteListadoRequisiciones extends ControladorBase {

	public final static  int VER_TODAS_LAS_UNIDADES = 25;
	
	@Autowired
	GatewayRequisicion gatewayRequisicion;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	
	public ModelAndView  requestGetControlador( Map modelo, HttpServletRequest request) {
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		String fecha = dateFormat.format(date); 
		modelo.put("FECHA_IMPRESION", fecha);
		String listadoReq=request.getParameter("txtlistado");
		String todo = request.getParameter("todo");
		String numreq=request.getParameter("txtrequisicion");
		String proyecto=request.getParameter("txtproyecto");
		String partida=request.getParameter("txtpartida");
		String clv_benefi=request.getParameter("CVE_BENEFI");
		String beneficiario=request.getParameter("txtprestadorservicio");
		String tipogto=request.getParameter("cbotipogasto")==null ? "0" : request.getParameter("cbotipogasto");
		String unidad=request.getParameter("dependencia")==null ?this.getSesion().getClaveUnidad() : request.getParameter("dependencia");
		String cboconOP = request.getParameter("cboconOP");
		
		if(privilegio){
			if(request.getParameter("dependencia")==null)
				unidad = "0";
			if(request.getParameter("dependencia")!=null)
				unidad = request.getParameter("dependencia");
		}
		
		if(!privilegio){
			if(request.getParameter("dependencia")==null)
				unidad = this.getSesion().getClaveUnidad();
			if(request.getParameter("dependencia")!=null)
				unidad = request.getParameter("dependencia");
		}
		
		String estatus=request.getParameter("status")==null ? Integer.toString(gatewayRequisicion.REQ_STATUS_NUEVO): this.arrayToString(request.getParameterValues("status"),",");
		String fechaIni=request.getParameter("fechaInicial");
		String fechaFin=request.getParameter("fechaFinal");
		String verUnidad=request.getParameter("verUnidad");
		Integer tipo = request.getParameter("cbotipo") != null ?  request.getParameter("cbotipo").equals("")? null :  Integer.parseInt(request.getParameter("cbotipo")) : null  ;
		modelo.put("idUnidad",unidad);
		modelo.put("cve_pers",this.getSesion().getIdUsuario());
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("tipo", tipo );
		modelo.put("status", estatus );
		modelo.put("cbotipogasto", tipogto);
		modelo.put("cboconOP", cboconOP);
		modelo.put("txtprestadorservicio", beneficiario);
		modelo.put("CVE_BENEFI", clv_benefi);
		modelo.put("txtrequisicion", numreq);
		modelo.put("txtproyecto", proyecto);
		modelo.put("txtpartida", partida);
		modelo.put("fechaInicial",fechaIni);
		modelo.put("fechaFinal",fechaFin);
		modelo.put("verUnidad",verUnidad);
		String unidad_adm = (String) this.getJdbcTemplate().queryForObject("SELECT CLV_UNIADM+' '+DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID = ?", new Object[]{this.getSesion().getClaveUnidad()}, String.class);
		modelo.put("UNIDAD_SOLICITANTE", unidad_adm);
		
		List <Map> lista = null;
		
		if(cboconOP.equals("1"))
			lista = gatewayRequisicion.getListaDeRequisicionesPorEjemplo2(unidad, estatus, this.formatoFecha(fechaIni), this.formatoFecha(fechaFin), this.getSesion().getEjercicio(), tipo, verUnidad, numreq, this.getSesion().getIdUsuario(), this.getSesion().getClaveUnidad(), proyecto, partida, tipogto, beneficiario, privilegio, cboconOP);
		else
			lista = gatewayRequisicion.getListaDeRequisicionesPorEjemplo(unidad, estatus, this.formatoFecha(fechaIni), this.formatoFecha(fechaFin), this.getSesion().getEjercicio(), tipo, verUnidad, numreq, this.getSesion().getIdUsuario(), this.getSesion().getClaveUnidad(), proyecto, partida, tipogto, beneficiario, privilegio, cboconOP, listadoReq);
		
		modelo.put("rs",new JRMapCollectionDataSource(lista));		
		if(cboconOP.equals("1"))
			return new ModelAndView("rpt_listado_requisiciones_conOP",modelo);
		else
			return new ModelAndView("rpt_listado_requisiciones",modelo);
	}
}
