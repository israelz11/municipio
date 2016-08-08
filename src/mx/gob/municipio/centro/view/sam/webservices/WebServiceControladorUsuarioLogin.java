package mx.gob.municipio.centro.view.sam.webservices;

import java.util.Map;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import mx.gob.municipio.centro.model.gateways.sam.webservices.GatewayWebServiceUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.gson.Gson;

@Controller
@RequestMapping("/sam/webservices/configuracion/login.action")
public class WebServiceControladorUsuarioLogin extends ControladorBase {
	 @Autowired
	 GatewayWebServiceUsuarios gatewayServiceUsuarios;
	public WebServiceControladorUsuarioLogin() {}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})  
	public String  requestGetControlador( Map<String, String> modelo, HttpServletRequest request) throws Exception{
		try{
			
			Gson gson = new Gson();
			String json = "";
			Map<String, String> Parametros = this.toMap(URLDecoder.decode(request.getQueryString(), "UTF-8").split("&"));
			Map m = null;
			
			if(Parametros.size()>1){
				//valida el acceso al usuario
				System.out.println("MOVIL-> URL: "+request.getQueryString());
				if (Parametros.get("metodo").equals("login")&&Parametros.size()>1){
					String username = Parametros.get("username").toString();
					m = gatewayServiceUsuarios.getLogin(username);
					m.put("PRIVILEGIOS", gatewayServiceUsuarios.getListPrivilegiosUsuarioMovil(Integer.parseInt(m.get("CVE_PERS").toString())));
					String code = m.get("PWD").toString().substring(0,16);
					json = ControladorBase.encrypt(gson.toJson(m), code);
					modelo.put("contenido", json);
				}
			}
			else if(Parametros.size()==1){
				System.out.println("MOVIL-> URL: "+request.getQueryString());
				String passCodeTemp = request.getQueryString().substring(request.getQueryString().length()-16, request.getQueryString().length());
				String passCode = passCodeTemp.substring(5,12)+passCodeTemp.substring(12,16)+passCodeTemp.substring(0,5);
				String textOriginal = request.getQueryString().substring(0, request.getQueryString().length()-16);
				String urlDeCode = ControladorBase.decrypt(textOriginal, passCode);
				Map<String, String> Parametros2 = this.toMap(urlDeCode.split("&"));
				
				//cambia el password del usuario
				if(Parametros2.get("metodo").equals("pwd")){
					String passwordAnterior = Parametros2.get("pwd_ant").toString();
					String passwordNuevo = Parametros2.get("pwd_new").toString();
					int cve_pers = Integer.parseInt(Parametros2.get("id").toString());
					
					boolean v = gatewayServiceUsuarios.cambiarPassword(passwordAnterior, passwordNuevo, cve_pers);
					json = ControladorBase.encrypt(gson.toJson(v), passCode);
					modelo.put("contenido", json);
				}

			}
			
			System.out.println(json.toString());
			
			return "sam/webservices/configuracion/login.jsp";
		}
		catch(DataAccessException e){	   
			return "sam/webservices/configuracion/login.jsp";
		}
	}
	
}
