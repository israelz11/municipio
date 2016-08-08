/**
 * @author ISC. Israel de la Cruz H.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.utilerias;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/consultas/rpt_relacion_globalOP.action")
public class ControladorMuestraRelacionOPGlobal extends ControladorBase {

	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	
	public ControladorMuestraRelacionOPGlobal() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		String tipo_rel = request.getParameter("tipo");
		String mes = request.getParameter("cbomes");
		modelo.put("mes", mes);
		modelo.put("TIPO_REL", tipo_rel);
		List <Map> lst = new ArrayList();
		List <Map> lstdetalles = gatewayOrdenDePagos.cargarRelacionesGeneral(tipo_rel,mes);
		for(Map row: lstdetalles){
			if(!tipo_rel.equals("VALES")){
				//calar la unidad administrativa
				List <Map> U = this.getJdbcTemplate().queryForList("SELECT DISTINCT C.ID_DEPENDENCIA, M.DEPENDENCIA FROM SAM_MOV_OP INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) INNER JOIN CAT_DEPENDENCIAS AS M ON (M.ID = C.ID_DEPENDENCIA) WHERE CVE_OP = ?", new Object[]{row.get("CVE_OP")});
				if(U.size()==1)//si no hay mas de una unidad en op poner una nueva
				{
					Map temp = this.getJdbcTemplate().queryForMap("SELECT DISTINCT M.CLV_UNIADM, M.DEPENDENCIA FROM SAM_MOV_OP INNER JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = SAM_MOV_OP.ID_PROYECTO) INNER JOIN CAT_DEPENDENCIAS AS M ON (M.ID = C.ID_DEPENDENCIA) WHERE CVE_OP = ?", new Object[]{row.get("CVE_OP")});
					row.put("UNIDADADM", temp.get("CLV_UNIADM").toString()+" "+temp.get("DEPENDENCIA").toString());
					row.put("UNIADM",temp.get("CLV_UNIADM").toString());
				}
			}
			else{
				if(row.get("CVE_OP")!=null){
					Map temp = this.getJdbcTemplate().queryForMap("SELECT U.CLV_UNIADM, U.DEPENDENCIA FROM SAM_VALES_EX AS V LEFT JOIN CEDULA_TEC AS C ON (C.ID_PROYECTO = V.ID_PROYECTO) INNER JOIN CAT_DEPENDENCIAS AS U ON (U.ID = V.ID_DEPENDENCIA) WHERE V.CVE_VALE = ?", new Object[]{row.get("CVE_OP")});
					row.put("UNIDADADM", temp.get("CLV_UNIADM").toString()+" "+temp.get("DEPENDENCIA").toString());
					row.put("UNIADM",temp.get("CLV_UNIADM").toString());
				}
				
			}
			
			lst.add(row);
		}
		modelo.put("dat", lst);
		return "sam/consultas/rpt_relacion_globalOP.jsp";
	}
	
	

}
