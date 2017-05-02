/**
 * @author lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.model.gateways.sam;

import java.util.Date;
import java.util.logging.Logger;
import mx.gob.municipio.centro.model.bases.BaseGateway;

public class GatewayBitacora extends BaseGateway {
	private static Logger log = Logger.getLogger(GatewayBitacora.class.getName());
	
	/*PARA LAS REQUISICIONES*/
	public static int NUEVA_REQUISICION    = 1;
	public static int CAMBIOS_REQUISICION  = 2;
	public static int CANCELAR_REQUISICION = 3;
	public static int AGREGAR_MOV_REQ      = 4;
	public static int ELIMINAR_MOV_REQ     = 5;
	public static int CERRAR_REQUISICION   = 6;
	public static int APERTURA_REQUISICION = 7;
	public static int ACTUALIZO_CONCEPTO   = 8;
	
	/*PARA LOS PEDIDOS*/
	public static int PEDIDO_NUEVO       = 9;
	public static int CAMBIOS_PEDIDO  	 = 10;
	public static int CANCELA_PEDIDO   	 = 11;
	public static int AGREGO_MOV_PEDIDO	 = 12;
	public static int ELIMINO_MOV_PED    = 13;
	public static int CERRO_PEDIDO    	 = 14;
	public static int APERTURA_PEDIDO 	 = 15;
	public static int MOVIO_LOTE_PED	 = 16;
	public static int REACTIVA_PEDIDO    = 47;
	
	/*PARA LAS ORDENES DE PAGO*/
	public static int OP_EJERCER       	= 17;
	public static int OP_DESEJERCER    	= 18;
	public static int OP_CANCELA	 	= 19;
	public static int OP_NUEVA_ORDEN  	= 20;
	public static int OP_ACTUALIZAR    	= 21;
	public static int OP_CERRAR 	   	= 22;
	public static int OP_APERTURAR    	= 23;
	public static int OP_MOV_ELIMINA_ANEXO 		= 24;
	public static int OP_MOV_ELIMINA_CONCEPTO	= 25;
	public static int OP_MOV_ELIMINA_RETENCION 	= 26;
	public static int OP_MOV_ELIMINA_VALE 		= 27;
	public static int OP_MOV_ACTUALIZA_ANEXO	= 28;
	public static int OP_MOV_ACTUALIZA_CONCEPTO	= 29;
	public static int OP_MOV_ACTUALIZA_RETENCION= 30;
	public static int OP_MOV_ACTUALIZA_VALES	= 31;
	public static int OP_MOV_AGREGO_ANEXO		= 32;
	public static int OP_MOV_AGREGO_CONCEPTO	= 33;
	public static int OP_MOV_AGREGO_RETENCIONES	= 34;
	public static int OP_MOV_AGREGO_VALES		= 35;
	public static int OP_CAMBIA_FECHA			= 36;
	
	/*PARA LAS FACTURAS*/
	public static int FACTURA_NUEVA=54;
	public static int FACTURA_ACTUALIZAR =55;
	public static int FACTURA_CANCELADA =56;
	public static int FACTURA_CERRAR =57;
	public static int FACTURA_MOV_AGREGO_RETENCIONES =58;
	public static int FACTURA_MOV_ACTUALIZA_RETENCION =59;
	public static int FACTURA_MOV_ACTUALIZA_IMPORTE =61;
	
	
	/*PARA LOS VALES*/
	public static int VALE_NUEVO		= 37;
	public static int VALE_ACTUALIZA 	= 38;
	public static int VALE_CANCELA		= 39;
	public static int VALE_APERTURA 	= 40;
	public static int VALE_CERRAR		= 41;
	public static int VALE_APLICAR		= 42;
	public static int VALE_DESAPLICAR	= 43;
	public static int VALE_RECHAZAR		= 44;
	
	public static int OP_MUEVE_A_USUARIO	= 45;
	public static int OP_VALIDA_FINANZAS	= 46;
	public static int PED_REACTIVA			= 47;
	
	/*CONTRATOS*/
	
	public final static int CON_NUEVO = 48;
	public final static int CON_ACTUALIZO = 49;
	public final static int CON_CANCELO = 50;
	public final static int CON_APERTURO = 51;
	public final static int CON_TERMINO = 52;
	public final static int CON_CERRO = 53;
	
	/*ALMACEN*/
	
	public final static int Entrada_almacen = 57;
	
	/*Constructor*/
	public GatewayBitacora() {}
	
	public void guardarBitacora(int ID_MOVTO, int EJERCICIO, int CVE_PERS,  Long CVE_DOC, String NUM_DOC, String TIPO_DOC,  Date FECHA_DOC, String PROYECTO, String PARTIDA, String DESCRIPCION, Double MONTO){
		Date FECHA = new Date();
		String SQL = "INSERT INTO SAM_BITACORA (ID_MOVTO, EJERCICIO, CVE_PERS, CVE_DOC, NUM_DOC, TIPO_DOC, FECHA, FECHA_DOC, ID_PROYECTO, PARTIDA, DESCRIPCION, MONTO)"+
						 "values(?,?,?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(SQL, new Object[]{ID_MOVTO, EJERCICIO, CVE_PERS, CVE_DOC, NUM_DOC, TIPO_DOC, FECHA, FECHA_DOC, PROYECTO, PARTIDA, DESCRIPCION, MONTO} );
	}
	
	private Long getNumeroBitacora(Integer ejercicio ){
		return this.getJdbcTemplate().queryForLong("select max(cve_movto) as n from bitacora_movtos where ejercicio=?", new Object[]{ejercicio});
	}

}
