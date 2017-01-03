/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.sam.componentes.autocomplete;

import java.util.List;
import java.util.Map;

import mx.gob.municipio.centro.model.gateways.sam.GatewayBancos;
import mx.gob.municipio.centro.model.gateways.sam.GatewayBeneficiario;
import mx.gob.municipio.centro.model.gateways.sam.GatewayCedulasTecnicas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUnidadMedidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayProyectoPartidas;
import mx.gob.municipio.centro.model.gateways.sam.GatewayUsuarios;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.springframework.beans.factory.annotation.Autowired;

public class AutocompleteDiversos  extends ControladorBase  {

	public AutocompleteDiversos() {		 
	}	
	
	@Autowired
	private GatewayBeneficiario gatewayBeneficiario;
		
	@Autowired
	GatewayCedulasTecnicas gatewayCedulasTecnicas;
	
	@Autowired
	GatewayProyectoPartidas gatewayProyectoPartidas;
	
	@Autowired
	GatewayUnidadMedidas gatewayUnidadMedidas;
	
	@Autowired
	GatewayBancos gatewayBancos;
	
	@Autowired
	private GatewayUsuarios gatewayUsuarios;

	
	public List<Map> getBeneficiariosTodos(Integer tipo){		
		return gatewayBeneficiario.getBeneficiariosTodos(tipo);
	}
 
	public List<Map> getPartidasPorProyecto(String proyecto){		
		return gatewayProyectoPartidas.getPartidasPorProyectos(proyecto);
	}
	
	
	public List<Map> getProyectosPorUnidad(String claveUnidad){		
		return gatewayCedulasTecnicas.getProyectosPorUnidad(claveUnidad);
	}
	
	public List<Map> getProyectosTodos(){		
		return gatewayCedulasTecnicas.getProyectosTodos();
	}
	
	public List<Map> getUnidadMedidasTodas(){
		return gatewayUnidadMedidas.getUnidadMedidasTodas();
	}
	
	public List<Map> getBancosTodos(){
		return gatewayBancos.getBancosTodos();
	}
	
	public List<Map> getPersonas(){
		return gatewayUsuarios.getPersonasPorEjemplo("", "", "");
	}
	
	public List<Map> getProyectosEval(){
		return this.gatewayProyectoPartidas.getTodosProyectos();
	}
	
}
