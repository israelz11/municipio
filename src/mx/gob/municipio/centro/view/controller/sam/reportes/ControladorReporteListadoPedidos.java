package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.GatewayPedidos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayRequisicion;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sam/reportes/rpt_listado_pedidos.action")
public class ControladorReporteListadoPedidos extends ControladorBase {

	public final static  int VER_TODAS_LAS_UNIDADES = 25;
	
	@Autowired
	GatewayPedidos gatewayPedidos;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET}) 
	
	public ModelAndView  requestGetControlador( Map modelo, HttpServletRequest request) {
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		String fecha = dateFormat.format(date); 
		modelo.put("FECHA_IMPRESION", fecha);
		
		String verAlmacen=request.getParameter("verAlmacen");
		String verUnidad=request.getParameter("verUnidad");
		String idUnidad=request.getParameter("cbodependencia")==null ?this.getSesion().getClaveUnidad() : request.getParameter("cbodependencia");
		String estatus=request.getParameter("status")==null ? "0": this.arrayToString(request.getParameterValues("status"),",");
		String beneficiario=request.getParameter("txtprestadorservicio");
		String cve_benefi=request.getParameter("CVE_BENEFI");
		String numped=request.getParameter("txtpedido");
		String numreq=request.getParameter("txtrequisicion");
		String cboconOP = request.getParameter("cboconOP");
		
		if(privilegio){
			if(request.getParameter("cbodependencia")==null)
				idUnidad = "0";
			if(request.getParameter("cbodependencia")!=null)
				idUnidad = request.getParameter("cbodependencia");
		}
		
		if(!privilegio){
			if(request.getParameter("cbodependencia")==null)
				idUnidad = this.getSesion().getClaveUnidad();
			if(request.getParameter("cbodependencia")!=null)
				idUnidad = request.getParameter("cbodependencia");
		}
		
		if(beneficiario==null ||beneficiario.equals("")) cve_benefi = "";
		
		modelo.put("txtprestadorservicio",beneficiario );
		modelo.put("txtpedido", numped);
		modelo.put("txtrequisicion", numreq);
		modelo.put("CVE_BENEFI",cve_benefi );
		modelo.put("cboconOP", cboconOP);
		modelo.put("idUnidad", idUnidad);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("verUnidad",verUnidad);
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("fechaInicial",(request.getParameter("fechaInicial")==null ? "": request.getParameter("fechaInicial")));
		modelo.put("fechaFinal",(request.getParameter("fechaFinal")==null ? "": request.getParameter("fechaFinal")));
		modelo.put("status",estatus);
		modelo.put("tipo_gto", (request.getParameter("cbotipogasto")==null ? "": request.getParameter("cbotipogasto")));
		modelo.put("cbocapitulo", (request.getParameter("cbocapitulo")==null ? "": request.getParameter("cbocapitulo")));
		modelo.put("cboalmacen", (request.getParameter("cboalmacen")==null ? "": request.getParameter("cboalmacen")));
		
		List <Map> lista = null;
		
		if(cboconOP.equals("1"))
			lista = gatewayPedidos.getListadoPedidos2(idUnidad, estatus, this.formatoFecha(modelo.get("fechaInicial").toString()), this.formatoFecha(modelo.get("fechaFinal").toString()), cve_benefi, modelo.get("tipo_gto").toString(), this.getSesion().getEjercicio(),this.getSesion().getIdUsuario(), verUnidad,  this.getSesion().getClaveUnidad(), numped, numreq, privilegio, modelo.get("cbocapitulo").toString(), Integer.parseInt(modelo.get("cboalmacen").toString()), cboconOP);
		else
			lista = this.getListadoPedidos(idUnidad,estatus , modelo.get("fechaInicial").toString(), modelo.get("fechaFinal").toString(), cve_benefi, modelo.get("tipo_gto").toString(), this.getSesion().getEjercicio(), this.getSesion().getIdUsuario(), verUnidad, this.getSesion().getClaveUnidad() , numped, numreq, privilegio,  modelo.get("cbocapitulo").toString(), Integer.parseInt(modelo.get("cboalmacen").toString()), cboconOP);
		
		String unidad_adm = (String) this.getJdbcTemplate().queryForObject("SELECT CLV_UNIADM+' '+DEPENDENCIA FROM CAT_DEPENDENCIAS WHERE ID = ?", new Object[]{this.getSesion().getClaveUnidad()}, String.class);
		modelo.put("UNIDAD_SOLICITANTE", unidad_adm);
		modelo.put("rs",new JRMapCollectionDataSource(lista));	
		
		if(cboconOP.equals("1"))
			return new ModelAndView("rpt_listado_pedidos_conOP",modelo);
		else
			return new ModelAndView("rpt_listado_pedidos",modelo);
	}
	
	public List <Map>getListadoPedidos(String unidad , String estatus, String fechaInicial, String fechaFinal, String cve_benefi, String tipo_gto, Integer ejercicio, Integer idUsuario, String  verUnidad, String cve_uniusr, String numped, String numreq, boolean privilegio, String  capitulo, int almacen, String cboconOP){
		return this.gatewayPedidos.getListadoPedidos(unidad, estatus,this.formatoFecha(fechaInicial) , this.formatoFecha(fechaFinal), cve_benefi, tipo_gto, ejercicio,idUsuario, verUnidad, cve_uniusr, numped, numreq, privilegio, capitulo, almacen, cboconOP);
	}

}
