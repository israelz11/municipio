package mx.gob.municipio.centro.model.gateways.sam;

import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.record.formula.functions.Cell;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
*/
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import org.springframework.web.servlet.view.xslt.AbstractXsltView;

import com.ibm.icu.math.BigDecimal;

public class GatewayExcelReporte extends AbstractExcelView {

	CellStyle rowStyle = null;
	String[] headers = null;
    int rowNum = 0;
    int colNum = 0;
    
	
	/*public GatewayExcelReporte(Map<String, Object> model) {
		//this.modelDataSource= (List<Map>) model;
	}*/
	
	public List <Map> modelDataSource = new ArrayList<Map>();
	
	
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response ) throws Exception {
	
		//response.setHeader("Content-Disposition", "attachmen: file =\"Presupuesto_disponible.xls\"");
		
		//La hoja donde pondremos los datos
		HSSFSheet excelSheet = workbook.createSheet("Presupuesto Disponible");
      
        setExcelHeader(excelSheet, workbook);
       	
       	//sheet.autoSizeColumn(columnNumber) 
		response.setHeader("Content-disposition", "attachment;filename=\"" + excelSheet.getSheetName() + ".xls\"");
		
		
		//autoSizeColumns(workbook);	
		setExcelRows(excelSheet,model, workbook);
		
		
		excelSheet.autoSizeColumn(0);
		excelSheet.autoSizeColumn(1);
	 	excelSheet.autoSizeColumn(2);
	 	excelSheet.autoSizeColumn(3);
	 	excelSheet.autoSizeColumn(4);
	 	excelSheet.autoSizeColumn(5);
	 	excelSheet.autoSizeColumn(6);
	 	excelSheet.autoSizeColumn(7);
	 	excelSheet.autoSizeColumn(8);
	 	excelSheet.autoSizeColumn(9);
	 	excelSheet.autoSizeColumn(10);
	 	excelSheet.autoSizeColumn(11);
	 	excelSheet.autoSizeColumn(12);
	 	excelSheet.autoSizeColumn(13);
	 	excelSheet.autoSizeColumn(14);
	 	excelSheet.autoSizeColumn(15);
	
	}
	
	
	public void setExcelHeader(HSSFSheet excelSheet, Workbook woorkbook) {
		
		CellStyle headerStyle = null;
	
		
		// create style for header cells
		//
	    headerStyle = excelSheet.getWorkbook().createCellStyle();
	    
	    DataFormat formato = woorkbook.createDataFormat();
	    Font fonts = excelSheet.getWorkbook().createFont();
	    fonts.setFontName("Arial");
	    fonts.setBold(true);
	    fonts.setFontHeightInPoints((short)10); //Tamaño de letra
	    headerStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);//Fondo de la celda
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    headerStyle.setFont(fonts);
	  
	    
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);//setAlignment(HorizontalAlignment align)
	 		 
		// create header row
		HSSFRow excelHeader = excelSheet.createRow(0);

       	excelHeader.createCell ((short) 0).setCellValue ("ID_REC");
		excelHeader.getCell(0).setCellStyle(headerStyle);
		
		excelHeader.createCell ((short) 1).setCellValue ("RECURSO"); 
		excelHeader.getCell(1).setCellStyle(headerStyle);
		
	 	excelHeader.createCell ((short) 2).setCellValue ("ID_DEP");
		excelHeader.getCell(2).setCellStyle(headerStyle);
			
		excelHeader.createCell ((short) 3).setCellValue ("DEPENDENCIA"); 
		excelHeader.getCell(3).setCellStyle(headerStyle);
			
		excelHeader.createCell ((short) 4).setCellValue ("ID_PRO"); 
		excelHeader.getCell(4).setCellStyle(headerStyle);
		
		excelHeader.createCell ((short) 5).setCellValue ("DECRIPCION"); 
		excelHeader.getCell(5).setCellStyle(headerStyle);
		
		excelHeader.createCell ((short) 6).setCellValue ("ID_PAR");  
		excelHeader.getCell(6).setCellStyle(headerStyle);
		
		excelHeader.createCell ((short) 7).setCellValue ("PARTIDA");  
		excelHeader.getCell(7).setCellStyle(headerStyle);
		
		excelHeader.createCell ((short) 8).setCellValue ("INICIAL"); 
		excelHeader.getCell(8).setCellStyle(headerStyle);
		
		
		excelHeader.createCell ((short) 9).setCellValue ("PRESUPUESTO");
		excelHeader.getCell(9).setCellStyle(headerStyle);
		//headerStyle.setDataFormat(formato.getFormat("0.0"));
		
		excelHeader.createCell ((short) 10).setCellValue ("COMPROMETIDO");
		excelHeader.getCell(10).setCellStyle(headerStyle);
		//headerStyle.setDataFormat(formato.getFormat("0.0"));
		
		//HSSFSheet
		excelHeader.createCell ((short) 11).setCellValue ("DEVENGADO"); 
		excelHeader.getCell(11).setCellStyle(headerStyle);
		//headerStyle.setDataFormat(formato.getFormat("#,##0.0000"));
		
		excelHeader.createCell ((short) 12).setCellValue ("EJERCIDO");
		excelHeader.getCell(12).setCellStyle(headerStyle);
		
		//excelSheet.setColumnWidth(2,40);
		excelHeader.createCell ((short) 13).setCellValue ("PRECOMPROMISO");
		excelHeader.getCell(13).setCellStyle(headerStyle);
				
		//excelSheet.setColumnWidth(2,40);
		excelHeader.createCell ((short) 14).setCellValue ("DISPONIBLE");
		excelHeader.getCell(14).setCellStyle(headerStyle);
		
		
	}
	
	
	
	public void setExcelRows(HSSFSheet excelSheet, Map model, Workbook woorkbook){
	 	
		HSSFRow fila;
		
		//celda-columna que queramos usar
		HSSFCell celda = null; 
		
		
		Row row;
		
		Double disponible = 0d;
		rowStyle = excelSheet.getWorkbook().createCellStyle();
		DataFormat format = woorkbook.createDataFormat();
		Font fonts = excelSheet.getWorkbook().createFont();
		fonts.setFontHeightInPoints((short)8); //Tamaño de letra
		fonts.setFontName("ARIAL");
	    rowStyle.setFont(fonts);
		
		short numFila = 1;
		short colNum = 0;
		
		List<Map> NewModel = (List<Map>)model.get("listadomovimientos");
		
		rowStyle = woorkbook.createCellStyle();
		
		/*----ESTILO COLUMNA ID_REC----*/
		CellStyle rowStyle_01 = woorkbook.createCellStyle();
		rowStyle_01.setAlignment(HorizontalAlignment.LEFT);
		rowStyle_01.setDataFormat(format.getFormat("0"));
		/*----------------------------*/
		/*----ESTILO COLUMNA RECURSO----*/
		CellStyle rowStyle_02 = woorkbook.createCellStyle();
		rowStyle_02.setAlignment(HorizontalAlignment.LEFT);
		rowStyle_02.setDataFormat(format.getFormat("text"));
		/*----------------------------*/
		/*----ESTILO COLUMNA ID_DEP----*/
		CellStyle rowStyle_03 = woorkbook.createCellStyle();
		rowStyle_03.setAlignment(HorizontalAlignment.LEFT);
		rowStyle_03.setDataFormat(format.getFormat("0"));
		/*----------------------------*/
		/*----ESTILO COLUMNA DEPENDENCIA----*/
		CellStyle rowStyle_04 = woorkbook.createCellStyle();
		rowStyle_04.setAlignment(HorizontalAlignment.LEFT);
		rowStyle_04.setDataFormat(format.getFormat("text"));
		/*----------------------------*/
		/*----ESTILO COLUMNA ID_PRO----*/
		CellStyle rowStyle_05 = woorkbook.createCellStyle();
		rowStyle_05.setAlignment(HorizontalAlignment.LEFT);
		rowStyle_05.setDataFormat(format.getFormat("0"));
		/*----------------------------*/
		/*----ESTILO COLUMNA DECRIPCION----*/
		CellStyle rowStyle_06 = woorkbook.createCellStyle();
		rowStyle_06.setAlignment(HorizontalAlignment.LEFT);
		rowStyle_06.setDataFormat(format.getFormat("text"));
		/*----------------------------*/
		/*----ESTILO COLUMNA ID_PAR----*/
		CellStyle rowStyle_07 = woorkbook.createCellStyle();
		rowStyle_07.setAlignment(HorizontalAlignment.LEFT);
		rowStyle_07.setDataFormat(format.getFormat("text"));
		/*----------------------------*/
		/*----ESTILO COLUMNA PARTIDA----*/
		CellStyle rowStyle_08 = woorkbook.createCellStyle();
		rowStyle_08.setAlignment(HorizontalAlignment.LEFT);
		rowStyle_08.setDataFormat(format.getFormat("text"));
		/*----------------------------*/
		/*----ESTILO COLUMNA INICIAL----*/
		CellStyle rowStyle_09 = woorkbook.createCellStyle();
		rowStyle_09.setAlignment(HorizontalAlignment.RIGHT);
		rowStyle_09.setDataFormat(format.getFormat("#,##0.00_);[Red](#,##0.00)"));
		/*----------------------------*/
		/*----ESTILO COLUMNA PRESUPUESTO----*/
		CellStyle rowStyle_10 = woorkbook.createCellStyle();
		rowStyle_10.setAlignment(HorizontalAlignment.RIGHT);
		rowStyle_10.setDataFormat(format.getFormat("#,##0.00_);[Red](#,##0.00)"));
		/*----------------------------*/
		/*----ESTILO COLUMNA COMPROMETIDO----*/
		CellStyle rowStyle_11 = woorkbook.createCellStyle();
		rowStyle_11.setAlignment(HorizontalAlignment.RIGHT);
		rowStyle_11.setDataFormat(format.getFormat("#,##0.00_);[Red](#,##0.00)"));
		/*----------------------------*/
		/*----ESTILO COLUMNA DEVENGADO----*/
		CellStyle rowStyle_12 = woorkbook.createCellStyle();
		rowStyle_12.setAlignment(HorizontalAlignment.RIGHT);
		rowStyle_12.setDataFormat(format.getFormat("#,##0.00_);[Red](#,##0.00)"));
		/*----------------------------*/
		/*----ESTILO COLUMNA EJERCIDO----*/
		CellStyle rowStyle_13 = woorkbook.createCellStyle();
		rowStyle_13.setAlignment(HorizontalAlignment.RIGHT);
		rowStyle_13.setDataFormat(format.getFormat("#,##0.00_);[Red](#,##0.00)"));
		/*----------------------------*/
		/*----ESTILO COLUMNA PRECOMPROMISO----*/
		CellStyle rowStyle_14 = woorkbook.createCellStyle();
		rowStyle_14.setAlignment(HorizontalAlignment.RIGHT);
		rowStyle_14.setDataFormat(format.getFormat("#,##0.00_);[Red](#,##0.00)"));
		/*----------------------------*/
		/*----ESTILO COLUMNA DISPONIBLE----*/
		CellStyle rowStyle_15 = woorkbook.createCellStyle();
		rowStyle_15.setAlignment(HorizontalAlignment.RIGHT);
		rowStyle_15.setDataFormat(format.getFormat("#,##0.00_);[Red](#,##0.00)"));
		/*----------------------------*/
		
		for (Map p : NewModel) {
			disponible = Double.parseDouble(p.get("PRESUPUESTO").toString()) - Double.parseDouble(p.get("COMPROMETIDO").toString())-Double.parseDouble(p.get("PRECOMPROMISO").toString());
			fila = excelSheet.createRow(numFila++); //La hoja debemos añadirle las filas que deseemos. La numeración empieza en cero.
			
			celda = (HSSFCell) fila.createCell((short)0);
			HSSFRichTextString idrecurso = new HSSFRichTextString((p.get("ID_RECURSO").toString()));
			celda.setCellValue(idrecurso);
			celda.setCellStyle(rowStyle_01);
			
			celda = (HSSFCell) fila.createCell((short)1);
			HSSFRichTextString desrecurso = new HSSFRichTextString((p.get("RECURSO").toString()));
			celda.setCellValue(desrecurso);
			celda.setCellStyle(rowStyle_02);
			
			celda = (HSSFCell) fila.createCell((short)2);
			HSSFRichTextString iddepen = new HSSFRichTextString((p.get("ID_DEPENDENCIA").toString()));
			celda.setCellValue(iddepen);
			celda.setCellStyle(rowStyle_03);
			
			celda = (HSSFCell) fila.createCell((short)3);
			HSSFRichTextString descdep = new HSSFRichTextString((p.get("DEPENDENCIA").toString()));
			celda.setCellValue(descdep);
			celda.setCellStyle(rowStyle_04);
			
			celda = (HSSFCell) fila.createCell((short)4);
			HSSFRichTextString idproy = new HSSFRichTextString((p.get("ID_PROYECTO").toString()));
			celda.setCellValue(idproy);
			celda.setCellStyle(rowStyle_05);
			
			celda = (HSSFCell) fila.createCell((short)5);
			HSSFRichTextString descproy = new HSSFRichTextString((p.get("DECRIPCION").toString()));
			celda.setCellValue(descproy);
			celda.setCellStyle(rowStyle_06);
			
			celda = (HSSFCell) fila.createCell((short)6);
			HSSFRichTextString idpar = new HSSFRichTextString((p.get("CLV_PARTID").toString()));
			celda.setCellValue(idpar);
			celda.setCellStyle(rowStyle_07);
			
			celda = (HSSFCell) fila.createCell((short)7);
			HSSFRichTextString descpar = new HSSFRichTextString((p.get("PARTIDA").toString()));
			celda.setCellValue(descpar);
			celda.setCellStyle(rowStyle_08);
			
			celda = (HSSFCell) fila.createCell((short)8);
			celda.setCellValue(Double.parseDouble(p.get("INICIAL").toString()));
			celda.setCellStyle(rowStyle_09);
			
			celda = (HSSFCell) fila.createCell((short)9);
			celda.setCellValue(Double.parseDouble(p.get("PRESUPUESTO").toString()));
			celda.setCellStyle(rowStyle_10);
			
			celda = (HSSFCell) fila.createCell((short)10);
			celda.setCellValue(Double.parseDouble(p.get("COMPROMETIDO").toString()));
			celda.setCellStyle(rowStyle_11);
			
			celda = (HSSFCell) fila.createCell((short)11);
			celda.setCellValue(Double.parseDouble(p.get("DEVENGADO").toString()));
			celda.setCellStyle(rowStyle_12);
			
			celda = (HSSFCell) fila.createCell((short)12);
			celda.setCellValue(Double.parseDouble(p.get("EJERCIDO").toString()));
			celda.setCellStyle(rowStyle_13);
		
			celda = (HSSFCell) fila.createCell((short)13);
			celda.setCellValue(Double.parseDouble(p.get("PRECOMPROMISO").toString()));
			celda.setCellStyle(rowStyle_14);
			
			celda = (HSSFCell) fila.createCell((short)14);
			celda.setCellValue(disponible);
			celda.setCellStyle(rowStyle_15);
			
		}
	}
	
	
}
