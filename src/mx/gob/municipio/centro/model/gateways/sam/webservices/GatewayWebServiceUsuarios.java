package mx.gob.municipio.centro.model.gateways.sam.webservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;

public class GatewayWebServiceUsuarios extends BaseGateway {
	private static Logger log = Logger.getLogger(GatewayWebServiceUsuarios.class.getName());
	@Autowired
	public GatewayUsuarios gatewayUsuarios;
	
	public GatewayWebServiceUsuarios(){}
	
	public Map getLogin(String username){
		System.out.println("MOVIL-> Recuperando informacion de login: "+username);
		return this.getJdbcTemplate().queryForMap("SELECT U.CVE_PERS, U.LOGIN, (P.NOMBRE + ' '+P.APE_PAT + ' '+P.APE_MAT) AS NOMBRE, U.PASSWD AS PWD, U.ACTIVO, ISNULL(U.EXCLUSIVO,'') AS EXCLUSIVO FROM USUARIOS_EX AS U INNER JOIN PERSONAS AS P ON (P.CVE_PERS = U.CVE_PERS ) WHERE U.LOGIN =? ", new Object[]{username});
	}
	
	public String getListPrivilegiosUsuarioMovil(Integer idUsuario){
		System.out.println("MOVIL-> Recuperando privilegios de Usuario: "+idUsuario);
		final List<Map> m = this.getJdbcTemplate().queryForList("SELECT distinct SAM_PRIVILEGIO.ID_PRIVILEGIO "+
				  " FROM SAM_ROL_PRIVILEGIO INNER JOIN "+
				  " SAM_USUARIO_ROL ON SAM_ROL_PRIVILEGIO.ID_ROL = SAM_USUARIO_ROL.ID_ROL INNER JOIN "+
				  " SAM_MODULO INNER JOIN "+
				  " SAM_PRIVILEGIO ON SAM_MODULO.ID_MODULO = SAM_PRIVILEGIO.ID_MODULO AND SAM_MODULO.MOD_ESTATUS='ACTIVO'  INNER JOIN "+
				  " SAM_SISTEMA ON SAM_MODULO.ID_SISTEMA = SAM_SISTEMA.ID_SISTEMA AND SAM_SISTEMA.SIS_ESTATUS='ACTIVO' ON  "+
				  " SAM_ROL_PRIVILEGIO.ID_PRIVILEGIO = SAM_PRIVILEGIO.ID_PRIVILEGIO AND SAM_PRIVILEGIO.PRI_ESTATUS='ACTIVO'INNER JOIN "+
				  " SAM_ROL ON SAM_USUARIO_ROL.ID_ROL = SAM_ROL.ID_ROL AND SAM_ROL.ROL_ESTADO='ACTIVO' "+
		  		  " where SAM_USUARIO_ROL.CVE_PERS=? /*AND SAM_PRIVILEGIO.TIPO='APLICACION'*/ order by SAM_PRIVILEGIO.ID_PRIVILEGIO ASC ", new Object []{idUsuario});		
		
		 List list = new ArrayList<Integer>() {
			  {
				  for(Map row: m){
					  add(Integer.parseInt(row.get("ID_PRIVILEGIO").toString()));
				  }
			  }
			};
			
		  return list.toString();
		  
		}
	
	public boolean cambiarPassword(String pasEncriptadoAnterior ,String  pasEncriptadoNuevo, int cve_pers ){
		System.out.println("MOVIL-> Intentendo cambiar el password cve_pers: "+cve_pers);
		Md5PasswordEncoder a = new Md5PasswordEncoder();
		Md5PasswordEncoder b = new Md5PasswordEncoder();
		
		if (this.getJdbcTemplate().queryForInt("select count(*) from usuarios_ex where PASSWD=? AND CVE_PERS =?",new Object[]{pasEncriptadoAnterior, cve_pers})==1)
		{
			this.getJdbcTemplate().update("Update USUARIOS_EX Set PASSWD=?, PWD=? where CVE_PERS=?",new Object[]{pasEncriptadoNuevo, "", cve_pers});
			System.out.println("MOVIL-> Password cambiado satisfactoriamente cve_pers: "+cve_pers);
			return true;
		}
		else
		{
			System.out.println("MOVIL-> Imposible cambiar el Password cve_pers: "+cve_pers);
			return false;
		}
		
	}

		
}
