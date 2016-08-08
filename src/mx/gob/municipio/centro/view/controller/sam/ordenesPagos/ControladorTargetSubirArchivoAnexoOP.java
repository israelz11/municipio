package mx.gob.municipio.centro.view.controller.sam.ordenesPagos;

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
@RequestMapping("/sam/ordenesdepago/_subirArchivoAnexoOP.action")
public class ControladorTargetSubirArchivoAnexoOP extends ControladorBase {

	@Autowired
	private GatewayOrdenDePagos gatewayOrdenDePagos;
	
	public ControladorTargetSubirArchivoAnexoOP(){}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST})  
	public String requestPostControlador(Map modelo, HttpServletRequest request, HttpServletResponse response, @RequestParam("archivo") MultipartFile file) throws IOException  {
		Gson gson = new Gson();
		Map m = new HashMap();
		String json = "";
		
		Long cve_op = Long.parseLong(request.getParameter("id_orden").toString());
		String idDocumento = request.getParameter("idDocumento");
		String tipoMovDoc = request.getParameter("tipoMovDoc");
		String numeroDoc = request.getParameter("numeroDoc");
		String notaDoc = request.getParameter("notaDoc");
		
		m.put("mensaje", false);
		
		if(getPrivilegioEn(this.getSesion().getIdUsuario(), 114)){
			throw new RuntimeException("No cuenta por los privilegios suficientes para realizar esta operación, solo lectura");
		}
		
		if (Integer.parseInt(idDocumento.toString()) == 0) {
			  idDocumento = (String) this.getJdbcTemplate().queryForObject("select isnull(max(ANX_CONS),0)+1 from SAM_OP_ANEXOS where CVE_OP=? ", new Object[]{cve_op}, String.class);
			  gatewayOrdenDePagos.insertaDocumento(Integer.parseInt(idDocumento.toString()), tipoMovDoc, numeroDoc,notaDoc, cve_op, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());	  
		  }
		  else
			  gatewayOrdenDePagos.actualizarDocumento(Integer.parseInt(idDocumento.toString()),tipoMovDoc,numeroDoc,notaDoc, cve_op, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
		
		if (file !=null && file.getSize()> 0 ){
		  	String nombreArchivo = removeSpecialChar(file.getOriginalFilename());
		  	Long size = file.getSize();
		  	String path = request.getSession().getServletContext().getRealPath("")+"/sam/ordenesdepago/anexos/";	  	
		  	String tipoArchivo = file.getContentType();	 
		  	String ext = getExtension(nombreArchivo);
		  	String nombreFisico = cve_op.toString()+"_"+idDocumento.toString()+" "+nombreArchivo;
		  	almacenarArchivoFisico(file,path,nombreFisico); 
		  	this.getJdbcTemplate().update("UPDATE SAM_OP_ANEXOS SET FILENAME=?, FILEPATH = ?, DATEFILE = ?, FILETYPE = ?, FILELENGTH = ? WHERE CVE_OP = ? AND ANX_CONS = ?", new Object[]{nombreFisico, "anexos/", new Date(), tipoArchivo, size, cve_op, idDocumento});
		}

		
		m.put("mensaje", true);
		json = gson.toJson(m);
		modelo.put("mensaje", json);
		System.out.println(json);
		return "/sam/ordenesdepago/_subirArchivoAnexoOP.jsp";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.GET})  
	public String requestGetControlador(Map modelo, HttpServletRequest request, HttpServletResponse response) {
		modelo.put("mensaje", null);
		return "/sam/ordenesdepago/_subirArchivoAnexoOP.jsp";
	}
}
