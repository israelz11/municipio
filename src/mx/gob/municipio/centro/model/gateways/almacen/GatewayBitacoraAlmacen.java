package mx.gob.municipio.centro.model.gateways.almacen;

import java.util.Date;
import java.util.logging.Logger;

import mx.gob.municipio.centro.model.bases.BaseGatewayAlmacen;


public class GatewayBitacoraAlmacen extends BaseGatewayAlmacen {
	private static Logger log = Logger.getLogger(GatewayBitacoraAlmacen.class.getName());
	
	public final static int Nueva_Entrada = 1;
	public final static int Actualiza_Entrada = 2;
	public final static int Cancela_Entrada = 3;
	public final static int Cierra_Entrada = 4;
	public final static int Detalle_Entrada = 5;
	
	public GatewayBitacoraAlmacen() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void guardarBitacoraAlmacen(int IDMOVTO, Long CVE_DOC, int CVE_PERS, int ID_ALMACEN, int ID_DEPENDENCIA,  Long ID_PEDIDO, String TIPO_DOC, int ID_ARTICULO, int ID_PROVEEDOR ,Date FECHA_DOC, String DESCRIPCION, Double MONTO){
		Date FECHA = new Date();
		String SQL = "INSERT INTO BITACORA_ALMACEN (IDMOVTO,CVE_DOC, CVE_PERS, ID_ALMACEN, ID_DEPENDENCIA, ID_PEDIDO, TIPO_DOC, ID_ARTICULO, ID_PROVEEDOR, FECHA,FECHA_DOC,DESCRIPCION,MONTO)"+
						 "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(SQL, new Object[]{IDMOVTO,CVE_DOC, CVE_PERS, ID_ALMACEN,  ID_DEPENDENCIA, ID_PEDIDO, TIPO_DOC, ID_ARTICULO, ID_PROVEEDOR, FECHA, FECHA_DOC, DESCRIPCION, MONTO} );
	}
	
	private Long getNumeroBitacora(Integer ejercicio ){
		return this.getJdbcTemplate().queryForLong("select max(cve_movto) as n from ALMACEN_CAT_MOVIMIENTOS_BITACORA where ejercicio=?", new Object[]{ejercicio});
	}

}
