package mx.gob.municipio.centro.view.controller.sam.ordenesPagos;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import mx.gob.municipio.centro.model.gateways.sam.GatewayFacturas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayPlanArbit;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadAdm;
import mx.gob.municipio.centro.view.bases.ControladorBase;


@Controller
@RequestMapping("/sam/consultas/muestra_dev_op.action")
public class ControladorMuestraDevengadoOrdenDePago  extends ControladorBase{
	public final static  int VER_TODAS_LAS_UNIDADES = 25;
	
	@Autowired
	GatewayUnidadAdm gatewayUnidadAdm;
	
	@Autowired 
	GatewayFacturas gatewayFacturas;
	
	@Autowired 
	GatewayPlanArbit gatewayPlanArbit;
	
	public ControladorMuestraDevengadoOrdenDePago() {
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked") 
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})  
	
	
	public String  requestGetControlador( Map modelo, HttpServletRequest request) {
		
		
	
		//listaFacturas es el objeto que recorre el JSP-------------------
		int  idtipoGasto = Integer.parseInt((request.getParameter("idtipogasto").toString()));
		//Log.info(idtipoGasto);
		
		modelo.put("tipoGastos",gatewayPlanArbit.getTipodeGasto());
		
		modelo.put("tipoGasto", idtipoGasto);
		modelo.put("listaFacturas", gatewayFacturas.getListadoFacturas_OP(modelo));
		
		
	    return "sam/consultas/muestra_dev_op.jsp";
	    
	}
	
	

}
