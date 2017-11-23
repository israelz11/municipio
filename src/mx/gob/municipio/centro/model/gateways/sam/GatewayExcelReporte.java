package mx.gob.municipio.centro.model.gateways.sam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;

public class GatewayExcelReporte extends AbstractExcelView {

	
	public GatewayExcelReporte(Map<String, Object> model) {
		this.modelDataSource= (List<Map>) model;
	}
	
	public List <Map> modelDataSource = new ArrayList<Map>();
	
	
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		HSSFSheet excelSheet = workbook.createSheet("Lista Presupuesto");
		setExcelHeader(excelSheet);
		
		List<Map> NewModel = (List<Map>)model.get("listadomovimientos");
		
		//List<Animal> animalList = (List<Animal>) model.get("animalList");
		setExcelRows(excelSheet,(Map) NewModel);
	}
	
	public void setExcelHeader(HSSFSheet excelSheet) {
		HSSFRow excelHeader = excelSheet.createRow(0);
		excelHeader.createCell ((short) 0).setCellValue ("ID_PROYECTO");  
		excelHeader.createCell ((short) 1).setCellValue ("ID_RECURSO");  
		excelHeader.createCell ((short) 2).setCellValue ("DECRIPCION");  
		excelHeader.createCell ((short) 3).setCellValue ("CLV_PARTID");  
		excelHeader.createCell ((short) 4).setCellValue ("INICIAL");  
		excelHeader.createCell ((short) 5).setCellValue ("COMPROMETIDO");  
		excelHeader.createCell ((short) 6).setCellValue ("DEVENGADO");  
		excelHeader.createCell ((short) 7).setCellValue ("EJERCIDO");
	}
	
	public void setExcelRows(HSSFSheet excelSheet, Map model){
		int record = 1;
		List<Map> NewModel = (List<Map>)model.get("listadomovimientos");
		
		for (Map p : NewModel) {
			HSSFRow excelRow = excelSheet.createRow(record++);
			excelRow.createCell(0).setCellValue(p.get("ID_PROYECTO").toString());
		}
	}
	
	/*
	public void setExcelRows(HSSFSheet excelSheet, Map model){
		int record = 1;
		 int rowNum = 1;  
	        
	        List<Map> NewModel = (List<Map>)model.get("listadomovimientos");
	        for(Map rows : NewModel){
	        	HSSFRow excelRow = excelSheet.createRow(record++);
	        	excelRow.createCell(0).setCellValue("ID_PROYECTO");
				excelRow.createCell(1).setCellValue("ID_RECURSO");
				excelRow.createCell(2).setCellValue("DECRIPCION");
				excelRow.createCell(3).setCellValue("CLV_PARTID");
				excelRow.createCell(4).setCellValue("COMPROMETIDO");
				excelRow.createCell(5).setCellValue("DEVENGADO"); 
				excelRow.createCell(6).setCellValue("EJERCIDO");
	        }
		
	}*/

}
