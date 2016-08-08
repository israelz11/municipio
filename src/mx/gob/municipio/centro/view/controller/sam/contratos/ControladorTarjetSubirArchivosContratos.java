/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 02/06/2015
 * @Descriopcion metodos para subir el archivo de contratos
 */
package mx.gob.municipio.centro.view.controller.sam.contratos;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.gob.municipio.centro.model.gateways.sam.GatewayContratos;
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
@RequestMapping("/sam/contratos/_subirArchivo.action")
public class ControladorTarjetSubirArchivosContratos extends ControladorBase {

	@Autowired 
	GatewayContratos gatewayContratos;
	
	public ControladorTarjetSubirArchivosContratos(){}
	
	/*Metodo para la carga de archivo*/
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST})  
	public String requestPostControlador(Map modelo, HttpServletRequest request, HttpServletResponse response, @RequestParam("archivo") MultipartFile file) throws IOException  {
		Gson gson = new Gson();
		Map m = new HashMap();
		String json = "";
		
		Long cve_contrato = ((request.getParameter("CVE_CONTRATO")==null||request.getParameter("CVE_CONTRATO")=="") ? 0L : Long.parseLong(request.getParameter("CVE_CONTRATO").toString()));
		if(cve_contrato!=0){
			int tcount = gatewayContratos.getCountArchivos(cve_contrato);
			if (file !=null && file.getSize()> 0 && tcount<=0){
				
			  	String nombreArchivo = removeSpecialChar(file.getOriginalFilename());
			  	Long size = file.getSize();
			  	String path = request.getSession().getServletContext().getRealPath("")+"/sam/contratos/archivos/";	  	
			    		     		    
			  	String tipoArchivo = file.getContentType();	 
			  	//String ext = gatewayMinutarios.getExtension(nombreArchivo);

			  	//guarda el archivo en registro
			  	Long idArchivo = gatewayContratos.guardarArchivo(cve_contrato, nombreArchivo, "/contratos/archivos/", new Date(), tipoArchivo, size);
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
		return "sam/contratos/_subirArchivo.jsp";
	}
}
