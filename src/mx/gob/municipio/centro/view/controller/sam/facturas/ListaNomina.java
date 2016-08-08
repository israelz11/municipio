package mx.gob.municipio.centro.view.controller.sam.facturas;

import java.math.BigDecimal;

public class ListaNomina {

	public int ID_PROYECTO;
	public String CLV_PARTID;
	public BigDecimal IMPORTE_MES;
	public BigDecimal IMPORTE_ANIO;
	private BigDecimal TOTAL;
	
	public ListaNomina(int idproyecto, String clv_partid, BigDecimal importe_mes, BigDecimal importe_anio)
	{
		this.ID_PROYECTO = idproyecto;
		this.CLV_PARTID = clv_partid;
		this.IMPORTE_MES = importe_mes;
		this.IMPORTE_ANIO = importe_anio;
		this.TOTAL = new BigDecimal(0);
	}
	
	public ListaNomina()
	{
		
	}
}
