package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/_subirArchivoProyecto.action")
public class ControladorTargetSubirArchivoProyecto extends ControladorBase {
	
	public ControladorTargetSubirArchivoProyecto(){}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST})  
	public String requestPostControlador(Map modelo, HttpServletRequest request, HttpServletResponse response, @RequestParam("archivo") MultipartFile file) throws IOException  {
		Gson gson = new Gson();
		Map m = new HashMap();
		String json = "";
		
		Long ID_PROYECTO = Long.parseLong(request.getParameter("ID_PROYECTO").toString());
		String SHORTNAME = request.getParameter("txtShortName");
		
		m.put("mensaje", false);
		
		/*if(getPrivilegioEn(this.getSesion().getIdUsuario(), 114)){
			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
		}*/
		
		
		if (file !=null && file.getSize()> 0 ){
		  	String nombreArchivo = removeSpecialChar(file.getOriginalFilename());
		  	Long size = file.getSize();
		  	String path = request.getSession().getServletContext().getRealPath("")+"/sam/consultas/archivos/";	  	
		  	String tipoArchivo = file.getContentType();	 	
		  	String nombreFisico = "[" + ID_PROYECTO.toString()+"] "+nombreArchivo;
		  	almacenarArchivoFisico(file,path,nombreFisico); 
		  	this.getJdbcTemplate().update("INSERT INTO SAM_PROYECTOS_ARCHIVOS(ID_PROYECTO, SHORTNAME, NOMBRE, RUTA, FECHA, EXT, TAMANO) VALUES (?,?,?,?,?,?,?)", new Object[]{ID_PROYECTO,SHORTNAME, nombreFisico, "../consultas/archivos/", new Date(), tipoArchivo, size});
		}

		m.put("mensaje", true);
		json = gson.toJson(m);
		modelo.put("mensaje", json);
		System.out.println(json);
		return "/sam/consultas/_subirArchivoProyecto.jsp";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET})  
	public String requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {
		modelo.put("mensaje", null);
		return "/sam/consultas/_subirArchivoProyecto.jsp";
	}
}
