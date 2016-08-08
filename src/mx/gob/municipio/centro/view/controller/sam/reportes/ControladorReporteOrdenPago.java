/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.reportes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import mx.gob.municipio.centro.model.gateways.sam.GatewayDetallesOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayFirmasDocumentos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import mx.gob.municipio.centro.view.componentes.RMCantidadEnLetras;
import mx.gob.municipio.centro.view.controller.sam.ordenesPagos.ControladorOrdenPago;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.util.JRLoader;

import net.sf.jasperreports.engine.JasperRunManager;



@Controller
@RequestMapping("/sam/reportes/formato_orden_pago.action")
public class ControladorReporteOrdenPago extends ControladorBase {
	private static Logger log = 
	        Logger.getLogger(ControladorReporteOrdenPago.class.getName());
	
	public ControladorReporteOrdenPago(){		
	}		
	
	@Autowired
	GatewayFirmasDocumentos gatewayFirmasDocumentos;
	@Autowired
	GatewayDetallesOrdenDePagos gatewayDetallesOrdenDePagos;	
	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	@Autowired
	private RMCantidadEnLetras rmCantidadEnLetras;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})    
	public ModelAndView  requestGetControlador( Map modelo, @RequestParam("cve_op")  Long  idOrden ) {	
		Map temp1 = new HashMap();
		temp1.put("PROYECTO", "");
		modelo = gatewayOrdenDePagos.getOrden(idOrden);
		List <Map> t_mov = this.getJdbcTemplate().queryForList("SELECT DISTINCT  N_PROGRAMA FROM SAM_MOV_OP INNER JOIN CEDULA_TEC ON (CEDULA_TEC.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) WHERE CVE_OP = ?",new Object[]{idOrden});
		if(t_mov==null) t_mov.add(temp1);
		modelo.putAll(this.getJdbcTemplate().queryForMap("SELECT TOP 1 '204' AS MUNICIPIO, "+(t_mov.size()> 1 ? "''":"(CAT_LOCALIDAD.CLV_LOCALIDAD) ") +" AS LOCALIDAD_1, "+(t_mov.size()> 1 ? "''":"(CAT_LOCALIDAD.LOCALIDAD) ")+" AS LOCALIDAD_2 FROM SAM_MOV_OP INNER JOIN CEDULA_TEC ON (CEDULA_TEC.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) INNER JOIN CAT_LOCALIDAD ON (CAT_LOCALIDAD.CLV_LOCALIDAD = CEDULA_TEC.CLV_LOCALIDAD) WHERE CVE_OP = ?", new Object[]{idOrden}));
		Md5PasswordEncoder codigo = new Md5PasswordEncoder();
		log.info("MD5 > "+modelo.get("NUM_OP").toString()+modelo.get("ID_DEPENDENCIA").toString()+modelo.get("FECHA").toString()+modelo.get("CLV_BENEFI").toString()+modelo.get("ID_RECURSO").toString()+modelo.get("IMPORTE").toString());
		String encriptado = codigo.encodePassword(modelo.get("NUM_OP").toString()+modelo.get("ID_DEPENDENCIA").toString()+modelo.get("FECHA").toString()+modelo.get("CLV_BENEFI").toString()+modelo.get("ID_RECURSO").toString()+modelo.get("IMPORTE").toString(), null );
		modelo.put("MD5", encriptado);
		Integer idGrupo=(Integer)modelo.get("ID_GRUPO");
		List detalles=gatewayDetallesOrdenDePagos.getDetalleOrdenReporte(idOrden);
		Boolean capitulo1000 = true;
		Boolean opCFE = false;
		for(Iterator iterador = detalles.listIterator();iterador.hasNext();){
			Map m =  (Map) iterador.next();
			if(m.get("N_PROGRAMA")==null&&m.get("CLV_PARTID")==null) break;
			if(m.get("CLV_PARTID")!=null) if(Integer.parseInt(m.get("CLV_CAPITU").toString())>=2000) capitulo1000 = false;
			
			//Validamos si es OP de CFE
			if(m.get("CLV_PARTID").toString().equals("3111")) opCFE = true; else opCFE = false;
		}
		//en caso de ser capitulo 1000 todas las partidas mandar 1 al parametro SUB3 para el subreporte de detalles
		modelo.put("SUB3", (capitulo1000.equals(true))? "1":"0");
		List anexos = gatewayDetallesOrdenDePagos.getAnexosOP(idOrden);
		List nomina = gatewayOrdenDePagos.getDetallesNomina(idOrden);
		if(capitulo1000){
			if(nomina.size()>anexos.size()){
				int dif = ((nomina.size()-anexos.size())*2)-10;
				Map m = new HashMap();
				m.put("ANX_CONS", "");
				m.put("T_DOCTO", "");
				m.put("NUMERO", "");
				m.put("NOTAS", "");
				m.put("CVE_OP", "");
				m.put("DESCR", "");
				
				for(int j=0;j<dif; j++){
					anexos.add(m);
				}
			}
		}
		List <Map> comp = this.getJdbcTemplate().queryForList("SELECT DISTINCT NUM_VALE FROM COMP_VALES INNER JOIN SAM_VALES_EX ON (SAM_VALES_EX.CVE_VALE = COMP_VALES.CVE_VALE) WHERE CVE_OP = ? AND COMP_VALES.TIPO NOT IN ('CF')", new Object[]{idOrden});
		String temp = "";
		for (Map val: comp){
			temp+=val.get("NUM_VALE").toString()+",";
		}
		if (temp.length()>0) temp = temp.substring(0, temp.length()-1);
		
		modelo.put("VALES_COMPROBADOS", temp);
		List vales=gatewayDetallesOrdenDePagos.getComprobacionVales(idOrden);
		BigDecimal importe = ((BigDecimal)modelo.get("TOTAL"));
		String cantidadLetra = convertirALetras(importe);
		
		modelo.put("IMPORTELETRAS", cantidadLetra.substring(1, cantidadLetra.length()-1));
		modelo.put("COMPRUEBAVALE", tieneVale(idOrden));
		gatewayFirmasDocumentos.getFirmasDocumentos(idGrupo, modelo);		
		
		modelo.put("rs",new JRMapCollectionDataSource(detalles));
		
		modelo.put("DT1",anexos);
		modelo.put("DT2",vales);
		if(capitulo1000) modelo.put("DT3",nomina);
		
		if(opCFE)
			return new ModelAndView("formato_orden_pago_CFE",modelo);
		else
			return new ModelAndView("formato_orden_pago",modelo);
	}			
	
	/*private String convertirALetras(BigDecimal numero) {
		return rmCantidadEnLetras.convertir(numero.doubleValue())[0];
	}*/
	
	private String tieneVale(Long idOrden) {
		String tieneVales="NO";
		int vales= this.getJdbcTemplate().queryForInt("select  count(*) from COMP_VALES a , SAM_VALES_EX  b   where  a.CVE_VALE=b.CVE_VALE and  a.cve_op=? ",new Object []{idOrden});
		if (vales > 0)
			tieneVales="SI";
		return tieneVales;		
	}
	
	
}
