package mx.gob.municipio.centro.view.sam.webservices.consultas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mx.gob.municipio.centro.model.gateways.sam.webservices.GatewayWebServicePresupuesto;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

@Controller
@RequestMapping("/sam/webservices/consultas/presupuesto.action")
public class WebServiceControladorPresupuesto extends ControladorBase {
	 @Autowired
	 GatewayWebServicePresupuesto gatewayWebServicePresupuesto;
	public WebServiceControladorPresupuesto() {}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request) throws Exception {
		try{
			Gson gson = new Gson();
			String json = "";
			System.out.println("MOVIL-> URL: "+request.getQueryString());
			
			String passCodeTemp = request.getQueryString().substring(request.getQueryString().length()-16, request.getQueryString().length());
			String passCode = passCodeTemp.substring(5,12)+passCodeTemp.substring(12,16)+passCodeTemp.substring(0,5);
			String textOriginal = request.getQueryString().substring(0, request.getQueryString().length()-16);
			String urlDeCode = ControladorBase.decrypt(textOriginal, passCode);
			Map<String, String> Parametros = this.toMap(urlDeCode.split("&"));
			
			if(Parametros.get("metodo").equals("presupuesto"))
			{
				Map m = gatewayWebServicePresupuesto.getPresupuestoGeneral(Integer.parseInt(Parametros.get("idProyecto").toString()), Parametros.get("clv_partid").toString(), Integer.parseInt(Parametros.get("periodo").toString()));
				json = ControladorBase.encrypt(gson.toJson(m), passCode);
			}
			
			if(Parametros.get("metodo").equals("dependencias")){
				List<Map> m = gatewayWebServicePresupuesto.getListaDependencias(Integer.parseInt(Parametros.get("cve_pers").toString()));
				int idDependencia = gatewayWebServicePresupuesto.getDependenciaUser(Integer.parseInt(Parametros.get("cve_pers").toString()));
				Map tempID = new HashMap();
				tempID.put("ID_DEPENDENCIA_USER", idDependencia);
				m.add(tempID);
				json = ControladorBase.encrypt(gson.toJson(m), passCode);
			}
			
			if(Parametros.get("metodo").equals("tipoGasto")){
				List<Map> m = gatewayWebServicePresupuesto.getListaTipoGasto();
				json = ControladorBase.encrypt(gson.toJson(m), passCode);
			}
			
			if(Parametros.get("metodo").equals("programas")){
				List<Map> m = gatewayWebServicePresupuesto.getProgramas(Integer.parseInt(Parametros.get("cve_pers").toString()), Integer.parseInt(Parametros.get("idRecurso").toString()), Integer.parseInt(Parametros.get("idDependencia").toString()));
				json = ControladorBase.encrypt(gson.toJson(m), passCode);
			}
			
			if(Parametros.get("metodo").equals("partidas")){
				List<Map> m = gatewayWebServicePresupuesto.getListaPartidas(Integer.parseInt(Parametros.get("cve_pers").toString()), Integer.parseInt(Parametros.get("idProyecto").toString()));
				json = ControladorBase.encrypt(gson.toJson(m), passCode);
			}
			
			if(Parametros.get("metodo").equals("precom")){
				List<Map> m = gatewayWebServicePresupuesto.getDocumentosPresupuesto(Integer.parseInt(Parametros.get("periodo").toString()), "PRECOMPROMETIDO", Integer.parseInt(Parametros.get("idProyecto").toString()), Parametros.get("clv_partid").toString());
				json = ControladorBase.encrypt(gson.toJson(m), passCode);
			}
			
			if(Parametros.get("metodo").equals("comp")){
				List<Map> m = gatewayWebServicePresupuesto.getDocumentosPresupuesto(Integer.parseInt(Parametros.get("periodo").toString()), "COMPROMETIDO", Integer.parseInt(Parametros.get("idProyecto").toString()), Parametros.get("clv_partid").toString());
				json = ControladorBase.encrypt(gson.toJson(m), passCode);
			}
			
			if(Parametros.get("metodo").equals("ejer")){
				List<Map> m = gatewayWebServicePresupuesto.getDocumentosPresupuesto(Integer.parseInt(Parametros.get("periodo").toString()), "EJERCIDO", Integer.parseInt(Parametros.get("idProyecto").toString()), Parametros.get("clv_partid").toString());
				json = ControladorBase.encrypt(gson.toJson(m), passCode);
			}
			
			System.out.print(json.toString());
			modelo.put("contenido", json.toString());
			return "sam/webservices/consultas/presupuesto.jsp";
		}
		catch(DataAccessException e){	   
			return "sam/webservices/consultas/presupuesto.jsp";
		}
	}
	
}
