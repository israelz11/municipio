/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;

import mx.gob.municipio.centro.model.bases.BaseGateway;
import mx.gob.municipio.centro.view.seguridad.UserDetailsService;

public class GatewayUsuarios extends BaseGateway {
	private static Logger log = Logger.getLogger(GatewayUsuarios.class.getName());
	public GatewayUsuarios(){
		
	}
	
	public List<Map> getUsuariosActivoTodos(){		
	  return this.getJdbcTemplate().queryForList("SELECT     b.CVE_PERS, a.APE_PAT, a.APE_MAT, a.NOMBRE ,'('+b.LOGIN + ') ' + a.NOMBRE+' '+a.APE_PAT +' '+a.APE_MAT  NOMBRE_COMPLETO "+
	  		" FROM PERSONAS  a INNER JOIN  USUARIOS_EX b ON a.CVE_PERS = b.CVE_PERS where b.ACTIVO='S' ORDER BY NOMBRE_COMPLETO ");		
	}
	
	public List<Map> getUsuariosUnidad(String idUnidad){		
		  return this.getJdbcTemplate().queryForList("SELECT     b.CVE_PERS , '('+b.LOGIN + ') ' + a.NOMBRE+' '+a.APE_PAT +' '+a.APE_MAT  NOMBRE_COMPLETO "+
		  		" FROM PERSONAS  a INNER JOIN  USUARIOS_EX b ON a.CVE_PERS = b.CVE_PERS INNER JOIN  TRABAJADOR c ON  a.CVE_PERS = c.CVE_PERS  where c.ID_DEPENDENCIA=? ORDER BY NOMBRE_COMPLETO ", new Object []{idUnidad});		
		}
	public Map getUsuarioLogin(String usuario){
		  return this.getJdbcTemplate().queryForMap("SELECT     dbo.TRABAJADOR.ID_DEPENDENCIA, dbo.CAT_DEPENDENCIAS.DEPENDENCIA, dbo.CAT_DEPENDENCIAS.CLV_UNIADM, dbo.USUARIOS_EX.CVE_PERS, " +
													"           dbo.USUARIOS_EX.LOGIN, dbo.USUARIOS_EX.PASSWD, dbo.USUARIOS_EX.ACTIVO, dbo.USUARIOS_EX.EXCLUSIVO,  dbo.PERSONAS.TRATAMIENTO+ ' '+dbo.PERSONAS.NOMBRE+' '+dbo.PERSONAS.APE_PAT +' ' +dbo.PERSONAS.APE_MAT  as NOMBRE_COMPLETO, "+  
													"           dbo.PERSONAS.TRATAMIENTO , dbo.PERSONAS.NOMBRE, dbo.PERSONAS.APE_PAT , dbo.PERSONAS.APE_MAT  "+
													"FROM       dbo.PERSONAS "+
													"			INNER JOIN dbo.USUARIOS_EX ON (dbo.PERSONAS.CVE_PERS = dbo.USUARIOS_EX.CVE_PERS) "+ 
													"			INNER JOIN dbo.TRABAJADOR ON (dbo.PERSONAS.CVE_PERS = dbo.TRABAJADOR.CVE_PERS) "+
													"			INNER JOIN dbo.CAT_DEPENDENCIAS ON (dbo.CAT_DEPENDENCIAS.ID = dbo.TRABAJADOR.ID_DEPENDENCIA) "+
													"WHERE  USUARIOS_EX.LOGIN=? " , new Object []{usuario});						 
		}
	
	public boolean cambiarPassword(String passwordAnterior ,String  passwordNuevo, int cve_pers ){
		log.debug("Intentendo cambiar el password");
		Md5PasswordEncoder a = new Md5PasswordEncoder();
		Md5PasswordEncoder b = new Md5PasswordEncoder();
		String pasEncriptadoAnterior = a.encodePassword(passwordAnterior, null );
		String pasEncriptadoNuevo = a.encodePassword(passwordNuevo, null );
		
		if (getJdbcTemplate().queryForInt("select count(*) from usuarios_ex where PASSWD=? AND CVE_PERS =?",new Object[]{pasEncriptadoAnterior, cve_pers})==1)
		{
			getJdbcTemplate().update("Update USUARIOS_EX Set PASSWD=?, PWD=? where CVE_PERS=?",new Object[]{pasEncriptadoNuevo, passwordNuevo, cve_pers});
			log.debug("Password cambiado satisfactoriamente");
			return true;
		}
		return false;
	}
		
	public List<Map> getPersonasPorEjemplo(String nombre, String aPaterno, String aMaterno ){
		Map parametros = new HashMap();
		parametros.put("nombre","%"+nombre+"%");
		parametros.put("aPaterno","%"+aPaterno+"%");
		parametros.put("aMaterno","%"+aMaterno+"%");
		String sql="";
		String instruc=" WHERE ";
		if (!nombre.equals("")){
			sql +=instruc+" P.NOMBRE like :nombre";
			instruc="  AND ";
		}
		if (!nombre.equals("")) {
			sql +=instruc+"  P.APE_PAT like :aPaterno";
			instruc="  AND ";
		}
		if (!nombre.equals(""))
			sql +=instruc+" P.APE_MAT like :aMaterno";
		
		  return this.getNamedJdbcTemplate().queryForList("SELECT   P.CVE_PERS, P.TRATAMIENTO, P.NOMBRE, P.APE_MAT, P.APE_PAT, "+
																	"P.RFC, P.CURP, T.ID_DEPENDENCIA, U.LOGIN, U.PASSWD, U.ACTIVO, C.DEPENDENCIA "+
															"FROM PERSONAS AS P "+
																	"INNER JOIN TRABAJADOR AS T ON (T.CVE_PERS = P.CVE_PERS) "+
																	"LEFT JOIN CAT_DEPENDENCIAS AS C ON (C.ID = T.ID_DEPENDENCIA) "+
																	"INNER JOIN USUARIOS_EX AS U ON (U.CVE_PERS = P.CVE_PERS)"+sql,parametros);		
		}
	
	public  Long actualizarPersonaPrincipal(Long idPersona,String nombre,String apaterno,String amaterno,String curp,String rfc,String profesion){
		  if (idPersona == null) 		  
			  idPersona= insertarPersona(nombre,apaterno,amaterno,curp,rfc,profesion);	  	  
		  else
			  actualizarPersona(idPersona,nombre,apaterno,amaterno,curp,rfc,profesion);
		  return idPersona; 
		}

	public Long insertarPersona(final String nombre,final String apaterno,final String amaterno,final String curp,final String rfc,final String profesion ) {
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	Long key_orden=null;
    	final String INSERT_SQL ="INSERT INTO PERSONAS (NOMBRE,APE_PAT,APE_MAT,CURP,RFC,TRATAMIENTO)  VALUES (?,?,?,?,?,?)";
        try{ 
            this.getJdbcTemplate().update(
                             new PreparedStatementCreator() {
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                             PreparedStatement ps =
                                 connection.prepareStatement(INSERT_SQL, new String[]{ "CVE_PERS"} );
                                         ps.setString( 1, nombre );                                         
                                         ps.setString( 2,apaterno );
                                         ps.setString( 3, amaterno );                                         
                                         ps.setString(4, curp);                                           
                                         ps.setString(5, rfc);
                                         ps.setString(6, profesion);
                                                       
                                 return ps;
                            }
                     },
            keyHolder);
            key_orden = new Long(keyHolder.getKey().longValue());
        }catch( DataAccessException ex) {
        	//log.info("Fallo la inserción del sql");
        	throw ex;                     
        }
        return key_orden;        
    }
	

		public void actualizarPersona(Long idPersona,String nombre,String apaterno,String amaterno,String curp,String rfc,String profesion ){	
			this.getJdbcTemplate().update("update  PERSONAS set NOMBRE=?,APE_PAT=?,APE_MAT=?,CURP=?,RFC=?,TRATAMIENTO=?  where CVE_PERS=? "
					, new Object[]{nombre,apaterno,amaterno,curp,rfc,profesion,idPersona});
		}	
		
		public  void actualizarTrabajadorPrincipal(Long idTrabajador ,Long idPersona, int idUnidad){
			  if (idTrabajador == 0) 		  
				  insertaTrabajador(idPersona, idUnidad);	  	  
			  else
				  actualizarTrabajador(idPersona, idUnidad);
			}
		
    
		public void insertaTrabajador( Long idPersona, int idUnidad ){
			//int  area = this.getJdbcTemplate().queryForInt("SELECT TOP 1 ID_DEPENDENCIA FROM TRABAJADOR WHERE CVE_PERS = ?", new Object[]{idPersona});
			this.getJdbcTemplate().update("insert into TRABAJADOR (CVE_PERS,ID_DEPENDENCIA,ACTIVO ) " +
					"VALUES (?,?,'S')"
					, new Object[]{idPersona, idUnidad});
		}

		public void actualizarTrabajador(Long idPersona, int  idUnidad ){	
			//int  area = this.getJdbcTemplate().queryForInt("SELECT TOP 1 CVE_UNIDAD FROM UNIDAD_ADM WHERE ORG_ID = ?", new Object[]{idUnidad});
			this.getJdbcTemplate().update("update  TRABAJADOR set ID_DEPENDENCIA=?, ACTIVO='S'  where CVE_PERS=? "
					, new Object[]{idUnidad,idPersona});
		}
		
		public List getTrabajadoresUnidad(String unidad) {
			return this.getJdbcTemplate().queryForList("SELECT  b.CVE_PERS , '('+b.LOGIN + ') ' + a.NOMBRE+' '+a.APE_PAT +' '+a.APE_MAT  NOMBRE_COMPLETO, d.DEPENDENCIA, d.ID AS ID_DEPENDENCIA   FROM PERSONAS  a "+ 
																"INNER JOIN  USUARIOS_EX b ON (b.CVE_PERS = a.CVE_PERS) "+
																"INNER JOIN  TRABAJADOR c ON  (c.CVE_PERS = a.CVE_PERS) "+
																"INNER JOIN CAT_DEPENDENCIAS AS d ON (d.ID = c.ID_DEPENDENCIA) "+  
															"WHERE c.ID_DEPENDENCIA=? "+
															"ORDER BY NOMBRE_COMPLETO ",new Object []{unidad});			
		}
		
	
		public  void actualizarUsuarioPrincipal(Long idUsuario, Long idPersona, String login,String  password ,String  estatus){
			  if (idUsuario == 0) 		  
				  insertaUsuario(idPersona,login, password,estatus);	  	  
			  else
				  actualizarUsuario(idUsuario,login, password,estatus);
			}		
  
		public void insertaUsuario( Long idPersona, String login,  String password , String estatus ){			
			Md5PasswordEncoder a = new Md5PasswordEncoder();
			String pasEncriptado = a.encodePassword(password, null );			
			this.getJdbcTemplate().update("insert into USUARIOS_EX (CVE_PERS,LOGIN,PASSWD, PWD, ACTIVO) " +
					"VALUES (?,?,?,?,?)"					
					, new Object[]{idPersona,login, pasEncriptado, password, estatus});
			
			
		}

		public void actualizarUsuario(Long idPersona, String login,  String password , String estatus ){
			if (password!=null && !password.equals("")){
				Md5PasswordEncoder a = new Md5PasswordEncoder();
				String pasEncriptado = a.encodePassword(password, null );
				this.getJdbcTemplate().update("update  USUARIOS_EX set LOGIN=?,ACTIVO=? , PASSWD=?, PWD=?  where CVE_PERS=? "
						, new Object[]{login, estatus,pasEncriptado,password,idPersona});
				}				
			else
				this.getJdbcTemplate().update("update  USUARIOS_EX set LOGIN=?,ACTIVO=?  where CVE_PERS=? "
						, new Object[]{login, estatus,idPersona});
		}
		
		
		public SqlRowSet getPrivilegiosUsuario(Integer idUsuario){		
			  return this.getJdbcTemplate().queryForRowSet("SELECT distinct SAM_PRIVILEGIO.PRI_DESCRIPCION PRIVILEGIO, SAM_MODULO.MOD_DESCRIPCION MODULO,  SAM_SISTEMA.SIS_DESCRIPCION SISTEMA"+
					  " FROM SAM_ROL_PRIVILEGIO INNER JOIN "+
					  " SAM_USUARIO_ROL ON SAM_ROL_PRIVILEGIO.ID_ROL = SAM_USUARIO_ROL.ID_ROL INNER JOIN "+
					  " SAM_MODULO INNER JOIN "+
					  " SAM_PRIVILEGIO ON SAM_MODULO.ID_MODULO = SAM_PRIVILEGIO.ID_MODULO AND SAM_MODULO.MOD_ESTATUS='ACTIVO'  INNER JOIN "+
					  " SAM_SISTEMA ON SAM_MODULO.ID_SISTEMA = SAM_SISTEMA.ID_SISTEMA AND SAM_SISTEMA.SIS_ESTATUS='ACTIVO' ON  "+
					  " SAM_ROL_PRIVILEGIO.ID_PRIVILEGIO = SAM_PRIVILEGIO.ID_PRIVILEGIO AND SAM_PRIVILEGIO.PRI_ESTATUS='ACTIVO'INNER JOIN "+
					  " SAM_ROL ON SAM_USUARIO_ROL.ID_ROL = SAM_ROL.ID_ROL AND SAM_ROL.ROL_ESTADO='ACTIVO' "+
			  		  " where SAM_USUARIO_ROL.CVE_PERS=? order by SISTEMA,MODULO ", new Object []{idUsuario});		
			}	
		
		
		public List getMenuPrivilegiosUsuario(Integer idUsuario, Integer idSistema){		
			  return this.getJdbcTemplate().queryForList("SELECT distinct D.PRI_DESCRIPCION PRIVILEGIO, C.MOD_DESCRIPCION,C.IMAGEN , D.URL,C.ORDEN ORDEN_MODULO ,D.ORDEN ORDEN_PRIVILEGIO "+
					  	" FROM SAM_ROL_PRIVILEGIO a  INNER JOIN "+ 
						" SAM_USUARIO_ROL  B ON A.ID_ROL = B.ID_ROL INNER JOIN  "+
						" SAM_MODULO C INNER JOIN  "+
						" SAM_PRIVILEGIO D ON C.ID_MODULO = D.ID_MODULO AND C.MOD_ESTATUS='ACTIVO' AND IMAGEN IS NOT NULL  INNER JOIN "+ 
						" SAM_SISTEMA  E ON C.ID_SISTEMA = E.ID_SISTEMA AND E.SIS_ESTATUS='ACTIVO' ON   "+
						" A.ID_PRIVILEGIO = D.ID_PRIVILEGIO AND D.PRI_ESTATUS='ACTIVO' AND TIPO='MENU' INNER JOIN "+ 
						" SAM_ROL ON B.ID_ROL = SAM_ROL.ID_ROL AND SAM_ROL.ROL_ESTADO='ACTIVO'  "+
						" where B.CVE_PERS=? and E.ID_SISTEMA =?  ORDER BY C.ORDEN,D.ORDEN   ", new Object []{idUsuario,idSistema});		
			}
		
		
		public List getPermisosSistemas(Integer idUsuario){		
			  return this.getJdbcTemplate().queryForList("SELECT distinct E.ID_SISTEMA , E.SIS_DESCRIPCION   "+
					  	" FROM SAM_ROL_PRIVILEGIO a  INNER JOIN "+ 
						" SAM_USUARIO_ROL  B ON A.ID_ROL = B.ID_ROL INNER JOIN  "+
						" SAM_MODULO C INNER JOIN  "+
						" SAM_PRIVILEGIO D ON C.ID_MODULO = D.ID_MODULO AND C.MOD_ESTATUS='ACTIVO' AND IMAGEN IS NOT NULL  INNER JOIN "+ 
						" SAM_SISTEMA  E ON C.ID_SISTEMA = E.ID_SISTEMA AND E.SIS_ESTATUS='ACTIVO' ON   "+
						" A.ID_PRIVILEGIO = D.ID_PRIVILEGIO AND D.PRI_ESTATUS='ACTIVO' AND TIPO='MENU' INNER JOIN "+ 
						" SAM_ROL ON B.ID_ROL = SAM_ROL.ID_ROL AND SAM_ROL.ROL_ESTADO='ACTIVO'  "+
						" where B.CVE_PERS=?  ORDER BY E.ID_SISTEMA   ", new Object []{idUsuario});		
			}				
		
		public Integer getGrupo(int idUsuario) {
			int tieneGrupo=this.getJdbcTemplate().queryForInt("select count(*) from SAM_GRUPO_CONFIG_USUARIO where ASIGNADO =1 AND ID_USUARIO=?", new Object []{idUsuario});
			if (tieneGrupo > 0)
			   return this.getJdbcTemplate().queryForInt("select ID_GRUPO_CONFIG from SAM_GRUPO_CONFIG_USUARIO where ASIGNADO =1 AND ID_USUARIO=?", new Object []{idUsuario});
			else
				return null;
			
			 }
		
}
