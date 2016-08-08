/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 19/01/2013
 * @Descriopcion metodos para subir el archivo de facturas
 */
package mx.gob.municipio.centro.view.controller.sam.vales;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;
import mx.gob.municipio.centro.model.gateways.sam.GatewayFacturas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayVales;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;


@Controller
@RequestMapping("/sam/vales/_subirArchivo.action")
public class ControladorTarjetSubirArchivoVales extends ControladorBase {

	@Autowired 
	GatewayVales gatewayVales;
	
	public ControladorTarjetSubirArchivoVales(){}
	
	/*Metodo para la carga de archivo*/
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST})  
	public String requestPostControlador(Map modelo, HttpServletRequest request, HttpServletResponse response, @RequestParam("archivo") MultipartFile file, @RequestParam("cve_val") Long CVE_VALE) throws IOException, BiffException  {
		Gson gson = new Gson();
		Map m = new HashMap();
		String json = "";
		
		Long cve_vale = ((request.getParameter("cve_val")==null||request.getParameter("cve_val")=="") ? 0L : Long.parseLong(request.getParameter("cve_val").toString()));
		if(cve_vale!=0){
			int tcount = gatewayVales.getCountArchivos(cve_vale);
			if (file !=null && file.getSize()> 0 && tcount<=0){
				
			  	String nombreArchivo = removeSpecialChar(file.getOriginalFilename());
			  	Long size = file.getSize();
			  	String path = request.getSession().getServletContext().getRealPath("")+"/sam/vales/archivos/";	  	
			    		     		    
			  	String tipoArchivo = file.getContentType();	 
			  	//String ext = gatewayMinutarios.getExtension(nombreArchivo);

			  	//guarda el archivo en registro
			  	Long idArchivo = gatewayVales.guardarArchivo(cve_vale, nombreArchivo, "/vales/archivos/", new Date(), tipoArchivo, size);
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
		return "sam/vales/_subirArchivo.jsp";
	}
	

}
