/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 19/01/2013
 * @Descriopcion metodos para subir el archivo de facturas
 */
package mx.gob.municipio.centro.view.controller.sam.facturas;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.municipio.centro.model.gateways.sam.GatewayFacturas;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;

@Controller
@RequestMapping("/sam/facturas/_subirArchivo.action")
public class ControladorTarjetSubirArchivosFacturas extends ControladorBase {

	@Autowired 
	GatewayFacturas gatewayFacturas;
	
	public ControladorTarjetSubirArchivosFacturas(){}
	
	/*Metodo para la carga de archivo*/
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST})  
	public String requestPostControlador(Map modelo, HttpServletRequest request, HttpServletResponse response, @RequestParam("archivo") MultipartFile file) throws IOException  {
		Gson gson = new Gson();
		Map m = new HashMap();
		String json = "";
		
		Long cve_factura = ((request.getParameter("CVE_FACTURA")==null||request.getParameter("CVE_FACTURA")=="") ? 0L : Long.parseLong(request.getParameter("CVE_FACTURA").toString()));
		if(cve_factura!=0){
			int tcount = gatewayFacturas.getCountArchivos(cve_factura);
			if (file !=null && file.getSize()> 0 && tcount<=0){
				
			  	String nombreArchivo = removeSpecialChar(file.getOriginalFilename());
			  	Long size = file.getSize();
			  	String path = request.getSession().getServletContext().getRealPath("")+"/sam/facturas/archivos/";	  	
			    		     		    
			  	String tipoArchivo = file.getContentType();	 
			  	//String ext = gatewayMinutarios.getExtension(nombreArchivo);

			  	//guarda el archivo en registro
			  	Long idArchivo = gatewayFacturas.guardarArchivo(cve_factura, nombreArchivo, "/facturas/archivos/", new Date(), tipoArchivo, size);
			  	String nombreFisico = "["+idArchivo.toString()+"] "+nombreArchivo;
			  	almacenarArchivoFisico(file,path,nombreFisico); 
			  	
			  	m.put("mensaje", true);
			  	json = gson.toJson(m);
			  	modelo.put("mensaje", json);
			}
			else
			{
				m.put("mensaje", false);
				json = gson.toJson(m);
			  	modelo.put("mensaje", json);
			}
		}
		return "sam/facturas/_subirArchivo.jsp";
	}
}
