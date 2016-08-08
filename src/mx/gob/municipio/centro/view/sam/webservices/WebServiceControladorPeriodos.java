package mx.gob.municipio.centro.view.sam.webservices;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import mx.gob.municipio.centro.model.gateways.sam.webservices.GatewayWebServicePeriodos;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.gson.Gson;

@Controller
@RequestMapping("/sam/webservices/consultas/periodos.action")
public class WebServiceControladorPeriodos extends ControladorBase {
	 @Autowired
	 GatewayWebServicePeriodos gatewayWebServicePeriodos;
	 
	public WebServiceControladorPeriodos(){}
	
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
			
			if(Parametros.get("metodo").equals("periodos"))
			{
				List<Map> m = gatewayWebServicePeriodos.getPeriodos(Integer.parseInt(Parametros.get("ejercicio").toString()));
				json = ControladorBase.encrypt(gson.toJson(m), passCode);
			}
			
			if(Parametros.get("metodo").equals("abrir"))
			{
				boolean r = gatewayWebServicePeriodos.abrirCerrarPeriodos(Integer.parseInt(Parametros.get("periodo").toString()), Parametros.get("tipo").toString(), Integer.parseInt(Parametros.get("ejercicio").toString()));
				json = ControladorBase.encrypt(gson.toJson(r), passCode);
			}
			
			if(Parametros.get("metodo").equals("reiniciarPresupuestal"))
			{
				boolean r = gatewayWebServicePeriodos.reiniciarPeriodoPresupuestal(Integer.parseInt(Parametros.get("ejercicio").toString()));
				json = ControladorBase.encrypt(gson.toJson(r), passCode);
			}
			
			if(Parametros.get("metodo").equals("reiniciarEvaluacion"))
			{
				boolean r = gatewayWebServicePeriodos.reiniciarEvaluacion(Integer.parseInt(Parametros.get("ejercicio").toString()));
				json = ControladorBase.encrypt(gson.toJson(r), passCode);
			}
			
			System.out.print(json.toString());
			modelo.put("contenido", json.toString());
			return "sam/webservices/consultas/periodos.jsp";
		}
		catch(DataAccessException e){	   
			return "sam/webservices/consultas/periodos.jsp";
		}
	}
	

}
