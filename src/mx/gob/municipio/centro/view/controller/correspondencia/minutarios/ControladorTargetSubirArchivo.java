/**
 * @author ISC. Israel de la Cruz Hdez
 * @version 1.0
 * @fecha 08/10/2012
 */
package mx.gob.municipio.centro.view.controller.correspondencia.minutarios;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.municipio.centro.model.gateways.correspondencia.GatewayMinutarios;
import mx.gob.municipio.centro.view.bases.ControladorBaseCorrespondencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

@Controller
@RequestMapping("/correspondencia/minutarios/_subirArchivo.action")
public class ControladorTargetSubirArchivo extends ControladorBaseCorrespondencia {

	@Autowired
	private GatewayMinutarios gatewayMinutarios;
	
	public ControladorTargetSubirArchivo(){
		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST})  
	public String requestPostControlador(Map modelo, HttpServletRequest request, HttpServletResponse response, @RequestParam("archivo") MultipartFile file) throws IOException  {
		Gson gson = new Gson();
		Map m = new HashMap();
		String json = "";
		Long idMinutario = ((request.getParameter("ID_MINUTARIO")==null||request.getParameter("ID_MINUTARIO")=="") ? 0L : Long.parseLong(request.getParameter("ID_MINUTARIO").toString()));
		m.put("mensaje", false);
		if(idMinutario!=0){
			if (file !=null && file.getSize()> 0 ){
				
			  	String nombreArchivo = removeSpecialChar(file.getOriginalFilename());
			  	Long size = file.getSize();
			  	String path = request.getSession().getServletContext().getRealPath("")+"/correspondencia/archivos/minutarios/";	  	
			    		     		    
			  	String tipoArchivo = file.getContentType();	 
			  	String ext = gatewayMinutarios.getExtension(nombreArchivo);

			  	/*guarda el archivo en registro*/
			  	Long idArchivo = gatewayMinutarios.guardarArchivo(0L, idMinutario, nombreArchivo, "/archivos/minutarios/", new Date(), ext, size);
			  	String nombreFisico = "["+idArchivo.toString()+"] "+nombreArchivo;
			  	almacenarArchivoFisico(file,path,nombreFisico); 
			  	m.put("mensaje", true);
			  	json = gson.toJson(m);
			  	
			  	modelo.put("mensaje", json);
			}
		}
		System.out.println(json);
		return "correspondencia/minutarios/_subirArchivo.jsp";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET})  
	public String requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {
		modelo.put("mensaje", null);
		return "correspondencia/minutarios/_subirArchivo.jsp";
	}
	
}
