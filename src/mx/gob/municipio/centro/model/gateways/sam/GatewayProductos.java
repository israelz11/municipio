/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayProductos extends BaseGateway {
	public  GatewayProductos(){}
	
	public List<Map> getArticulosTodos(String alfabetico ){		 
		  return this.getJdbcTemplate().queryForList("SELECT   A.ULT_BENEFI,  A.CLV_UNIMED, A.DESCRIPCION, A.ULT_PRECIO, A.INVENT, A.CONSUM, A.ESTATUS, A.ID_CAT_ARTICULO, "+ 
					" B.UNIDMEDIDA, C.NCOMERCIA  FROM  SAM_CAT_ARTICULO AS A INNER JOIN  "+
                     " CAT_UNIMED AS B ON A.CLV_UNIMED = B.CLV_UNIMED LEFT OUTER JOIN  "+
					 " CAT_BENEFI  C ON A.ULT_BENEFI = C.CLV_BENEFI where DESCRIPCION like ? order by  DESCRIPCION ", new Object []{alfabetico+"%"} );		
		}
	
	public  void actualizarPrincipal(Integer idArticulo, String unidadMedida, String descripcion, String ult_benefi, Double precio ,int invetario, int consumible, String estatus ){
		  if (idArticulo == null) 		  
			  inserta( unidadMedida, descripcion, ult_benefi, precio,invetario, consumible, estatus);	  	  
		  else
			  actualizar(idArticulo,unidadMedida, descripcion, ult_benefi, precio, invetario, consumible, estatus);
		}

		public void inserta( String unidadMedida, String descripcion, String ult_benefi, Double precio, int invetario, int consumible, String estatus    ){
			//int consecu = this.getJdbcTemplate().queryForInt("select count(*)  from SAM_CAT_PROD where grupo=?  and subGrupo= ? ", new Object[]{grupo,subGrupo});
			//String concecutivo =rellenarCeros(Integer.toString(consecu),3); 
			this.getJdbcTemplate().update("insert into SAM_CAT_ARTICULO (CLV_UNIMED,DESCRIPCION, ULT_BENEFI, ULT_PRECIO,INVENT,CONSUM ,ESTATUS) " +
					"VALUES (?,?,?,?,?,?,?)"
					, new Object[]{ unidadMedida, descripcion, ult_benefi, precio, invetario, consumible, estatus  });
		}

		public void actualizar(Integer idArticulo,  String unidadMedida, String descripcion, String ult_benefi, Double precio, int invetario, int consumible, String estatus ){	
			this.getJdbcTemplate().update("update SAM_CAT_ARTICULO  set   CLV_UNIMED=?,DESCRIPCION=?, ULT_BENEFI=?,ULT_PRECIO=?,INVENT=?,CONSUM=? ,ESTATUS=?   where ID_CAT_ARTICULO=? "
					, new Object[]{ unidadMedida, descripcion, ult_benefi, precio, invetario, consumible, estatus,idArticulo });
		}	
		
		public void eliminar(Integer idArticulo  ){
			this.getJdbcTemplate().update("delete from SAM_CAT_ARTICULO where ID_CAT_ARTICULO= ?  ", new Object[]{idArticulo});
		}
		
		public Map getArticulo(Integer idArticulo  ){
			return this.getJdbcTemplate().queryForMap("SELECT      A.ULT_BENEFI,  A.CLV_UNIMED, A.DESCRIPCION, A.ULT_PRECIO, A.INVENT, A.CONSUM, A.ESTATUS, A.ID_CAT_ARTICULO, "+ 
					" B.UNIDMEDIDA, C.NCOMERCIA  FROM         SAM_CAT_ARTICULO AS A INNER JOIN  "+
                     " CAT_UNIMED AS B ON A.CLV_UNIMED = B.CLV_UNIMED LEFT OUTER JOIN  "+
					 " CAT_BENEFI  C ON A.ULT_BENEFI = C.CLV_BENEFI   where  A.ID_CAT_ARTICULO =? ", new Object[]{idArticulo});
		}
		
		public List getGrupos(String partida ) {	   
			   return this.getJdbcTemplate().queryForList(" select * from SAM_CAT_PROD where GRUPO=? and ID_CAT_ARTICULO is null ", new Object[]{partida} );
		}
		
		
}
