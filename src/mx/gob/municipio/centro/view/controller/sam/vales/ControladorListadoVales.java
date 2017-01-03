/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.controller.sam.vales;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mx.gob.municipio.centro.model.gateways.sam.GatewayBitacora;
import mx.gob.municipio.centro.model.gateways.sam.GatewayOrdenDePagos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.model.gateways.sam.GatewayVales;
import mx.gob.municipio.centro.view.bases.ControladorBase;

@Controller
@RequestMapping("/sam/vales/lista_vales.action")

public class ControladorListadoVales extends ControladorBase {

	public ControladorListadoVales(){}
	
	final String STATUS_NUEVA="0"; 
	boolean apertura = false;
	
	public final static  int VER_TODAS_LAS_UNIDADES = 25;
	@Autowired
	private GatewayUnidadAdm gatewayUnidadAdm;
	@Autowired
	private GatewayPlanArbit gatewayPlanArbit;
	
	@Autowired
	public GatewayBitacora gatewayBitacora;
	
	@Autowired
	GatewayOrdenDePagos gatewayOrdenDePagos;
	
	@Autowired
	GatewayVales gatewayVales;
	
	@SuppressWarnings("unchecked")	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	public String  requestGetControlador( Map modelo, HttpServletRequest request ) {
		boolean privilegio = this.getPrivilegioEn(this.getSesion().getIdUsuario(), VER_TODAS_LAS_UNIDADES);
		String unidad="";
		String estatus=request.getParameter("status")==null ? gatewayVales.getEstatusNueva().toString(): this.arrayToString(request.getParameterValues("status"),",");
		String fechaIni=request.getParameter("fechaInicial");
		String fechaFin=request.getParameter("fechaFinal");
		String tipoGasto=request.getParameter("cbotipogasto");
		String verUnidad=request.getParameter("verUnidad");
		
		if(privilegio){
			if(request.getParameter("cbodependencia")==null)
				unidad = "0";
			if(request.getParameter("cbodependencia")!=null)
				unidad = request.getParameter("cbodependencia");
		}
		
		if(!privilegio){
			if(request.getParameter("cbodependencia")==null)
				unidad = this.getSesion().getClaveUnidad();
			if(request.getParameter("cbodependencia")!=null)
				unidad = request.getParameter("cbodependencia");
		}
		
		modelo.put("idUnidad", unidad);
		modelo.put("ejercicio",this.getSesion().getEjercicio());
		modelo.put("fechaInicial",fechaIni);
		modelo.put("fechaFinal",fechaFin);
		modelo.put("status",estatus);
		modelo.put("tipo_gto",tipoGasto );
		modelo.put("verUnidad",verUnidad);
		modelo.put("nombreUnidad",this.getSesion().getUnidad());
		modelo.put("vales",this.getListadoVales(unidad, estatus, fechaIni,fechaFin,this.getSesion().getEjercicio(),tipoGasto,this.getSesion().getIdUsuario(),verUnidad, privilegio));		
	    return "sam/vales/lista_vales.jsp";
	}
	

	@ModelAttribute("unidadesAdmiva")
    public List<Map> getUnidades(){
    	return gatewayUnidadAdm.getUnidadAdmTodos();	
    }
		
	@ModelAttribute("tipodeGasto")
    public List<Map> getTiposDeGasto(){
    	return gatewayPlanArbit.getTipodeGasto();
    }
		
	public List <Map>getListadoVales(String unidad, String  estatus , String fechaInicial, String fechaFinal , Integer ejercicio, String tipoGasto, Integer idUsuario, String verUnidad, Boolean privilegio){
		return this.gatewayVales.getListaDeValesPorEjemplo(unidad, estatus , this.formatoFecha(fechaInicial), this.formatoFecha(fechaFinal) , ejercicio, tipoGasto, idUsuario, verUnidad, privilegio);
	}
	
	public List cerrarVale(final List<Long> vales) {               
		 List resultado =(List)this.getTransactionTemplate().execute(new TransactionCallback(){
	    			@Override
					public Object doInTransaction(TransactionStatus arg0) {
						List resultados = new ArrayList();
	                	for (Long idVale :vales) {
	                		boolean cerrar = false;
	                		Map result = new HashMap<String,String>();
	                		Map vale = gatewayVales.getVale(idVale);	                		
	                		result.put("DATO", vale.get("NUM_VALE"));
	                		int periodo = Integer.parseInt(vale.get("MES").toString());
	                		List <Map> mov = gatewayVales.getDetallesVales(idVale);
	                		for(Map row: mov){
	                			
	                			int idproyecto = 0;
	                			String partida =  "";
	                			BigDecimal importe =  new BigDecimal(0);	
	                			BigDecimal disponible = new BigDecimal(0);
	                			
	                			
	                			idproyecto = Integer.parseInt(row.get("ID_PROYECTO").toString());
                				partida =  row.get("CLV_PARTID").toString();
	                			
	                			if(!vale.get("CVE_CONTRATO").toString().equals("0"))
	                			{
	                				//Modificado aqui Israel 19-May-2013 para integrar la validacion por contrato
	                				//idproyecto = Integer.parseInt(row.get("ID_PROYECTO").toString());
	                				//partida =  row.get("CLV_PARTID").toString();
	                				
	                			    importe =  (BigDecimal)row.get("IMPORTE");	
	                			    disponible = (BigDecimal)getJdbcTemplate().queryForObject("SELECT MONTO FROM VT_COMPROMISOS WHERE CVE_DOC=? AND TIPO_DOC=? AND PERIODO=? AND ID_PROYECTO=? AND CLV_PARTID=?", new Object[]{vale.get("CVE_CONTRATO"), "CON", vale.get("MES"), idproyecto,partida}, BigDecimal.class);
	                			}
	                			else
	                			{    			                		
			                		/* Modificado aqui Israel 29/02/2012 validar compromiso del vale*/
			                		Map m = getJdbcTemplate().queryForMap("SELECT ID_PROYECTO, N_PROGRAMA, '"+partida+"' AS CLV_PARTID,  ISNULL(dbo.getAutorizado(?,?,?,?),0) AS AUTORIZADO, ISNULL(dbo.getPrecomprometido(?,?,?),0) AS PRECOMPROMETIDO, ISNULL(dbo.getComprometido(?,?,?),0) AS COMPROMETIDO, ISNULL(dbo.getEjercido(?,?,?),0) AS EJERCIDO, ISNULL(dbo.getDisponible(?,?,?),0) AS DISPONIBLE FROM CEDULA_TEC AS CT WHERE CT.ID_PROYECTO = ? ", new Object[]{periodo, periodo, idproyecto, partida, periodo, idproyecto, partida,  periodo, idproyecto, partida, periodo, idproyecto, partida, periodo, idproyecto, partida, idproyecto});
			                		
	                				
	                			    importe =  (BigDecimal)row.get("IMPORTE");	
	                			    disponible = (BigDecimal)m.get("DISPONIBLE");
	                			}
	                			
		                		if(importe.doubleValue() <= disponible.doubleValue()){
		                			cerrar = true;
		                		}
		                		else
		                			cerrar = false;
		                		
	                		}
	                		
	                		if(cerrar){
	                			gatewayVales.actualizarEstatusVale(idVale, gatewayVales.getEstatusPendiente(),getSesion().getEjercicio(), getSesion().getIdUsuario());
	                			result.put("ESTADO","SI");
	                		}
	                		else
	                			result.put("ESTADO","NO");
		                		
	                		resultados.add(result);	                		
	                	}
	                	return resultados;
					} });
		return resultado;
	}
	
	public void  cancelarVale( final List<Long> vales ) {
		//Buscar si existe el Super Privilegio para Cancelar Vales
		boolean privilegio = getPrivilegioEn(getSesion().getIdUsuario(), 140);
		
		cambioEstatusVales( vales  ,gatewayVales.getEstatusCancelado());
	  }
	
	public void  rechazarVale(List<Long> vales ) {
		cambioEstatusVales( vales  ,gatewayVales.getEstatusNueva());
		       
	  }
	private void cambioEstatusVales( final List<Long> vales  ,final int estatus ) {
		 try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Long idVale :vales)
	                		gatewayVales.actualizarEstatusVale( idVale, estatus, getSesion().getEjercicio(), getSesion().getIdUsuario());
	                		
	                } });
	                } catch (DataAccessException e) {            
	                    //log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }   
	}
	
	public void aperturarVales(final List<Long>lst_cve_vales){
		try {    
			final int cve_pers = this.getSesion().getIdUsuario();
            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
                @Override
                protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
                	for (Long idVale :lst_cve_vales) {
                		//comprueba que no hay algo comprometiendo en el vale
                		Map comp_vale = getJdbcTemplate().queryForMap("SELECT  (SELECT ISNULL(COUNT(*),0) FROM SAM_REQUISIC WHERE CVE_VALE=V.CVE_VALE AND STATUS IN(1,2,5)) AS REQ, "+
																				"(SELECT ISNULL(COUNT(*),0) FROM SAM_PEDIDOS_EX AS CV WHERE CV.CVE_VALE = V.CVE_VALE AND STATUS IN (1,2,4,6)) AS PED, "+ 
																				"(SELECT ISNULL(COUNT(*),0) FROM SAM_MOV_OP AS M INNER JOIN SAM_ORD_PAGO AS O ON (O.CVE_OP = M.CVE_OP) WHERE M.CVE_VALE = V.CVE_VALE AND O.STATUS IN (0,1,6)) AS OP "+
																		"FROM SAM_VALES_EX AS V WHERE CVE_VALE = ?", new Object[]{idVale});
                		if(comp_vale.get("REQ").toString().equals("0")&&comp_vale.get("PED").toString().equals("0")&&comp_vale.get("OP").toString().equals("0"))
                			gatewayVales.actualizarEstatusVale(idVale, 5, getSesion().getEjercicio(), cve_pers);
                		else
                			throw new RuntimeException("No es posible aperturar el Vale por que se encuentra relacionados a otros documentos que comprometen el recurso, verifique las relaciones y vuelva a intentar esta operación");
                	}
                } 
             });
           
            } catch (DataAccessException e) {            	                    
                 throw new RuntimeException(e.getMessage(),e);
         }
	}
	
	public void  aplicarVale( final List<Long> vales ) {
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Long idVale :vales) {
	                		gatewayVales.actualizarEstatusVale( idVale, gatewayVales.getEstatusPagado(), getSesion().getEjercicio(), getSesion().getIdUsuario());
	                		getJdbcTemplate().update("insert into vales  (vale, clv_benefi, ID_RECURSO, tipo_vale, fe_vale, notas, vale_impor, descontado) " +
	                								 " select num_vale,clv_benefi,ID_RECURSO,tipo,fecha,justif,(SELECT ISNULL(SUM(V.IMPORTE),0) FROM SAM_MOV_VALES AS V WHERE V.CVE_VALE = ?) AS importe,0 from SAM_VALES_EX where cve_vale=?", new Object []{idVale,idVale});
	                	}
	                		
	                } });
	                } catch (DataAccessException e) {            
	                    //log.info("Los registros tienen relaciones con otras tablas ");	                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }         
	  }
	
	public void  desAplicarVale( final List<Long> vales ) {
		
		  try {                 
	            this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
	                @Override
	    protected void   doInTransactionWithoutResult(TransactionStatus status) {	                	
	                	for (Long idVale :vales) {	 
	                		Map vale = gatewayVales.getVale(idVale);	                		
	                		String numeroVale=(String)vale.get("NUM_VALE");
	                		getJdbcTemplate().update("delete from op_vale where vale=?", new Object []{numeroVale});
	                		getJdbcTemplate().update("delete from conc_vale where vale= ?", new Object []{numeroVale});
	                		getJdbcTemplate().update("delete from VALES where vale= ?", new Object []{numeroVale});
	                		getJdbcTemplate().update("update SAM_VALES_EX set STATUS=?  where cve_vale= ?", new Object []{gatewayVales.getEstatusPendiente(),idVale});	
	                		//gatewayBitacora
	                		gatewayBitacora.guardarBitacora(gatewayBitacora.VALE_DESAPLICAR, getSesion().getEjercicio(), getSesion().getIdUsuario(), idVale, vale.get("NUM_VALE").toString(), "VAL", (Date) vale.get("FECHA2"), "", "", null, Double.parseDouble(vale.get("IMPORTE").toString()));
	                	}
	                		
	                } });
	                } catch (DataAccessException e) {                    
	                    throw new RuntimeException(e.getMessage(),e);
	                }         
	  }
	
	public Map getFechaPeriodoVale(Long cve_vale){
		  return this.getJdbcTemplate().queryForMap("SELECT NUM_VALE, CONVERT(varchar(10), FECHA,103) AS FECHA, MES FROM SAM_VALES_EX WHERE CVE_VALE = ?", new Object[]{cve_vale});
	  }
	
	public boolean cambiarFechaPeriodo(Long cve_ped, String fechaNueva, int periodo){
		  return this.gatewayVales.cambiarFechaPeriodo(cve_ped, fechaNueva, periodo, this.getSesion().getIdUsuario(), this.getSesion().getEjercicio());
	  }
	
	 public String getListUsuarios(int cve_pers){
	 		return this.gatewayOrdenDePagos.getListUsuarios(cve_pers);
	 }
	 
	public boolean moverVales(Long[] cve_ped, int cve_pers_dest){
		return this.gatewayVales.moverVales(cve_ped, this.getSesion().getIdUsuario(), cve_pers_dest, this.getSesion().getEjercicio());
	  }
	
	public boolean getBeneficiario(Long[] cve_doc, int cve_pers_dest){
		return this.gatewayVales.moverVales(cve_doc, this.getSesion().getIdUsuario(), cve_pers_dest, this.getSesion().getEjercicio());
	}
	
	public Map getBeneficiarioVale(Long cve_doc){
		return gatewayVales.getBeneficiario(cve_doc);
	}
	
	public boolean cambiarBeneficiario(Long cve_doc, String clv_benefi){
		return gatewayVales.cambiarBeneficiario(cve_doc, clv_benefi, this.getSesion().getEjercicio(), this.getSesion().getIdUsuario());
	}
	
	public String getArchivoAnexoVale(int cve_vale)
	{
		return gatewayVales.getArchivoAnexoVale(cve_vale);
	}
	
}
