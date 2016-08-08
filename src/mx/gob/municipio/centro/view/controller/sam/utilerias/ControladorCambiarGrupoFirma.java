package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.catalogos.GatewayFirmasGrupos;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/utilerias/cambiarFirmas.action")
public class ControladorCambiarGrupoFirma extends ControladorBase {

	public ControladorCambiarGrupoFirma()  {
		// TODO Auto-generated method stub

	}
	@Autowired
	GatewayFirmasGrupos gatewayFirmasGrupos;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		try
		{
			Map dato = new HashMap();
			modelo.put("cve_pers", this.getSesion().getIdUsuario());
			modelo.put("grupo", request.getParameter("grupo")==null ? "0": request.getParameter("grupo"));
			modelo.put("modulo", request.getParameter("modulo")==null ? "0": request.getParameter("modulo"));
			modelo.put("cve_doc", request.getParameter("cve_doc")==null ? "0": request.getParameter("cve_doc"));
			Map grupo_doc = getGrupoActual(Long.parseLong(modelo.get("cve_doc").toString()), modelo.get("modulo").toString());
			
			Integer id_grupo =0;
			id_grupo = (modelo.get("grupo").equals("0")) ? Integer.parseInt(grupo_doc.get("ID_GRUPO_CONFIG").toString()): Integer.parseInt(modelo.get("grupo").toString());
			
			modelo.put("GRUPO_ACTUAL_DOC", grupo_doc.get("GRUPO_CONFIG").toString());
			List <Map> detalles =  this.gatewayFirmasGrupos.getGruposEstatus(id_grupo);
			for(Map det: detalles){
				/*ordenes de pago*/
				if(det.get("TIPO").equals("OP_PAGADOR")) {
					modelo.put("OP_PAGADOR_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("OP_PAGADOR_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("OP_PROGRAMACION")){
					modelo.put("OP_PROGRAMACION_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("OP_PROGRAMACION_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("OP_REVISO")){
					modelo.put("OP_REVISO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("OP_REVISO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("OP_TITULAR")){
					modelo.put("OP_TITULAR_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("OP_TITULAR_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("OP_VOBO")){
					modelo.put("OP_VOBO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("OP_VOBO_CARGO", det.get("CARGO").toString());
				}
				/*pedidos*/
				if(det.get("TIPO").equals("PED_AUTORIZO")) {
					modelo.put("PED_AUTORIZO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("PED_AUTORIZO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("PED_ELABORO")){
					modelo.put("PED_ELABORO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("PED_ELABORO_CARGO", det.get("CARGO").toString());
				}
				/*requisiciones*/
				if(det.get("TIPO").equals("REQ_AUTORIZO")) {
					modelo.put("REQ_AUTORIZO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_AUTORIZO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("REQ_SOLICITO")) {
					modelo.put("REQ_SOLICITO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_SOLICITO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("REQ_VOBO")) {
					modelo.put("REQ_VOBO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_VOBO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("REQ_VOBOADMIVO")) {
					modelo.put("REQ_VOBOADMIVO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_VOBOADMIVO_CARGO", det.get("CARGO").toString());
				}
				/*orden de servicio*/
				if(det.get("TIPO").equals("REQ_OS_AUTORIZO")) {
					modelo.put("REQ_OS_AUTORIZO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_OS_AUTORIZO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("REQ_OS_SOLICITO")) {
					modelo.put("REQ_OS_SOLICITO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_OS_SOLICITO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("REQ_OS_VOBO")) {
					modelo.put("REQ_OS_VOBO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_OS_VOBO_CARGO", det.get("CARGO").toString());
				}
				/*orden de trabajo*/
				if(det.get("TIPO").equals("REQ_OT_AUTORIZO")) {
					modelo.put("REQ_OT_AUTORIZO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_OT_AUTORIZO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("REQ_OT_SOLICITO")) {
					modelo.put("REQ_OT_SOLICITO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_OT_SOLICITO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("REQ_OT_VOBO")) {
					modelo.put("REQ_OT_VOBO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("REQ_OT_VOBO_CARGO", det.get("CARGO").toString());
				}
				/*vales*/
				if(det.get("TIPO").equals("VAL_PAGUESE")) {
					modelo.put("VAL_PAGUESE_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("VAL_PAGUESE_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("VAL_REVISO")) {
					modelo.put("VAL_REVISO_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("VAL_REVISO_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("VAL_SOLICITANTE")) {
					modelo.put("VAL_SOLICITANTE_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("VAL_SOLICITANTE_CARGO", det.get("CARGO").toString());
				}
				if(det.get("TIPO").equals("VAL_VOBO_PROGRAMACION")) {
					modelo.put("VAL_VOBO_PROGRAMACION_NOMBRE", det.get("REPRESENTANTE").toString());
					modelo.put("VAL_VOBO_PROGRAMACION_CARGO", det.get("CARGO").toString());
				}
				
			}
			modelo.put("grupos", getGruposUsuario(Integer.parseInt(modelo.get("cve_pers").toString())));
			return "sam/utilerias/cambiarFirmas.jsp";
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException(e.getMessage(),e);
	   }

	}
	
	/*carga un list de los grupos de firmas que tiene asignado el usuario*/
	public List getGruposUsuario(Integer cve_pers){
		return this.getJdbcTemplate().queryForList("SELECT A.* FROM SAM_GRUPO_CONFIG A WHERE /*A.ID_GRUPO_CONFIG IN (SELECT ID_GRUPO_CONFIG FROM SAM_GRUPO_CONFIG_USUARIO WHERE ID_USUARIO = ?) AND*/ A.TIPO ='FIRMA' AND A.ESTATUS ='ACTIVO' ORDER BY GRUPO_CONFIG ASC", new Object[]{});
	}
	
	/*carga la informacion del grupo actual del documento*/
	public Map getGrupoActual(Long cve_doc, String modulo){
		try{
			if(modulo.equals("req"))
				return this.getJdbcTemplate().queryForMap("SELECT A.* FROM SAM_GRUPO_CONFIG A WHERE A.ID_GRUPO_CONFIG IN (SELECT ID_GRUPO FROM SAM_REQUISIC WHERE CVE_REQ = ?) AND A.TIPO ='FIRMA' AND A.ESTATUS ='ACTIVO' ORDER BY GRUPO_CONFIG ASC ", new Object[]{cve_doc});
			if(modulo.equals("ped"))
				return this.getJdbcTemplate().queryForMap("SELECT A.* FROM SAM_GRUPO_CONFIG A WHERE A.ID_GRUPO_CONFIG IN (SELECT ID_GRUPO FROM SAM_PEDIDOS_EX WHERE CVE_PED = ?) AND A.TIPO ='FIRMA' AND A.ESTATUS ='ACTIVO' ORDER BY GRUPO_CONFIG ASC ", new Object[]{cve_doc});
			if(modulo.equals("op"))
				return this.getJdbcTemplate().queryForMap("SELECT A.* FROM SAM_GRUPO_CONFIG A WHERE A.ID_GRUPO_CONFIG IN (SELECT ID_GRUPO FROM SAM_ORD_PAGO WHERE CVE_OP = ?) AND A.TIPO ='FIRMA' AND A.ESTATUS ='ACTIVO' ORDER BY GRUPO_CONFIG ASC ", new Object[]{cve_doc});
			if(modulo.equals("val"))
				return this.getJdbcTemplate().queryForMap("SELECT A.* FROM SAM_GRUPO_CONFIG A WHERE A.ID_GRUPO_CONFIG IN (SELECT ID_GRUPO FROM SAM_VALES_EX WHERE CVE_VALE = ?) AND A.TIPO ='FIRMA' AND A.ESTATUS ='ACTIVO' ORDER BY GRUPO_CONFIG ASC ", new Object[]{cve_doc});
			return null;
		}
		catch (DataAccessException e) {                               
	        throw new RuntimeException(e.getMessage(),e);
	   }
	}
	
	/*METODO PARA CAMBIAR EL GRUPO DE FIRMAS DE UN DOCUMENTO*/
	public void cambiarGrupo(Long cve_doc, String modulo, Integer id_grupo){
		if(modulo.equals("req"))
			this.getJdbcTemplate().update("UPDATE SAM_REQUISIC SET ID_GRUPO = ? WHERE CVE_REQ = ?", new Object[]{id_grupo, cve_doc});
		if(modulo.equals("ped"))
			this.getJdbcTemplate().update("UPDATE SAM_PEDIDOS_EX SET ID_GRUPO = ? WHERE CVE_PED = ?", new Object[]{id_grupo, cve_doc});
		if(modulo.equals("op"))
			this.getJdbcTemplate().update("UPDATE SAM_ORD_PAGO SET ID_GRUPO = ? WHERE CVE_OP = ?", new Object[]{id_grupo, cve_doc});
		if(modulo.equals("val"))
			this.getJdbcTemplate().update("UPDATE SAM_VALES_EX SET ID_GRUPO = ? WHERE CVE_VALE = ?", new Object[]{id_grupo, cve_doc});
	}
	

}
