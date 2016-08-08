/**
 * @author ISC. Israel de la Cruz Hernandez.
 * @version 1.0
 * @Fecha 19/01/2013
 * @Descriopcion metodos para subir el archivo de facturas
 */
package mx.gob.municipio.centro.view.controller.sam.facturas;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;
import mx.gob.municipio.centro.model.gateways.sam.GatewayFacturas;
import mx.gob.municipio.centro.view.bases.ControladorBase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;


@Controller
@RequestMapping("/sam/facturas/_subirArchivoNominaDeductivas.action")
public class ControladorTarjetSubirArchivosNominaDeductivas extends ControladorBase {

	@Autowired 
	GatewayFacturas gatewayFacturas;
	
	public ControladorTarjetSubirArchivosNominaDeductivas(){}
	
	/*Metodo para la carga de archivo*/
	@SuppressWarnings("unchecked")
	@RequestMapping(method = {RequestMethod.POST})  
	public String requestPostControlador(Map modelo, HttpServletRequest request, HttpServletResponse response, @RequestParam("fileNomina") MultipartFile fileNomina) throws IOException, BiffException  {
		Gson gson = new Gson();
		Map m = new HashMap();
		Date fecha = new Date();
		String json = "";
		String path = request.getSession().getServletContext().getRealPath("")+"/sam/facturas/nomina/";	 
	  	//guarda el archivo en registro
	  	Integer idArchivo = fecha.getDay();
	  	Integer mes = fecha.getMonth()+1;
	  	Integer anio = fecha.getYear();
	  	
	  	String nombreArchivo = removeSpecialChar(fileNomina.getOriginalFilename());
	  	String nombreFisico = "["+idArchivo.toString()+"_"+mes+"_"+anio+"] "+nombreArchivo;
	  	String rutaCompleta = path + nombreFisico;
	  	almacenarArchivoFisico(fileNomina,path,nombreFisico);
	  	
	  	Vector cellVectorHolder = new Vector();

	  	try{
	        /** Creating Input Stream**/
	        //InputStream myInput= ReadExcelFile.class.getResourceAsStream( fileName );
	        FileInputStream myInput = new FileInputStream(rutaCompleta);
	 
	        /** Create a POIFSFileSystem object**/
	        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
	 
	        /** Create a workbook using the File System**/
	         HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
	 
	         /** Get the first sheet from workbook**/
	        HSSFSheet mySheetNomina = myWorkBook.getSheetAt(0);
	 
	        /** We now need something to iterate through the cells.**/
	          Iterator rowIter = mySheetNomina.rowIterator();
	 
	          while(rowIter.hasNext()){
	              HSSFRow myRow = (HSSFRow) rowIter.next();
	              Iterator cellIter = myRow.cellIterator();
	              Vector cellStoreVector=new Vector();
	              while(cellIter.hasNext()){
	                  HSSFCell myCell = (HSSFCell) cellIter.next();
	                  cellStoreVector.addElement(myCell);
	              }
	              cellVectorHolder.addElement(cellStoreVector);
	          }
	          
	          //Guarda la informacion de la nomina
	          guardarDatosNomina(cellVectorHolder);
	          
	          cellVectorHolder = new Vector();
	          
	          /** Get the first sheet from workbook**/
		        HSSFSheet mySheetDeductivas = myWorkBook.getSheetAt(1);
		 
		        /** We now need something to iterate through the cells.**/
		          Iterator rowIterDeductivas = mySheetDeductivas.rowIterator();
		 
		          while(rowIterDeductivas.hasNext()){
		              HSSFRow myRow = (HSSFRow) rowIterDeductivas.next();
		              Iterator cellIter = myRow.cellIterator();
		              Vector cellStoreVector=new Vector();
		              while(cellIter.hasNext()){
		                  HSSFCell myCell = (HSSFCell) cellIter.next();
		                  cellStoreVector.addElement(myCell);
		              }
		              cellVectorHolder.addElement(cellStoreVector);
		          }
		          
	          //Guarda la informacion de deductivas
	          guardarDatosDeductivas(cellVectorHolder);
	          
	          m.put("mensaje", true);
	  	  	 json = gson.toJson(m);
	  	  	 modelo.put("mensaje", json);
	    }
	  	catch (Exception e){
	  		 m.put("mensaje", false);
	  	  	 json = gson.toJson(m);
	  	  	 modelo.put("mensaje", json);
	  		e.printStackTrace(); 
	  }
		return "sam/facturas/_subirArchivoNominaDeductivas.jsp";
	}
	
	
	private void guardarDatosNomina(final Vector dataHolder) {
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	getJdbcTemplate().update("DELETE FROM SAM_NOMINA");
		        for (int i=0;i<dataHolder.size();i++) {
		        	String stringCellValue = null;
		            Vector cellStoreVector=(Vector)dataHolder.elementAt(i);
		            Map m = new HashMap();
		            for (int j=0; j< cellStoreVector.size();j++){
		                HSSFCell myCell = (HSSFCell)cellStoreVector.elementAt(j);
		                stringCellValue = myCell.toString();
		                if(i>0){
		                	
		                	if(stringCellValue==null) break;
		                	
			                if(j==0)
			                	m.put("TIPO_NOMINA", stringCellValue);
			                if(j==1)
			                	m.put("ID_RECURSO", (int) myCell.getNumericCellValue());
			                if(j==2)
			                	m.put("ID_DEPENDENCIA", (int) myCell.getNumericCellValue());
			                if(j==3)
			                	m.put("ID_PROYECTO", (int) myCell.getNumericCellValue());
			                if(j==4)
			                	m.put("CLV_PARTID", stringCellValue);
			                if(j==5)
			                	m.put("IMPORTE", Double.parseDouble(stringCellValue.replace(",", "")));
			                if(j==6)
			                	m.put("MES", (int) myCell.getNumericCellValue());
			                if(j==7)
			                	m.put("NUM_QUINCENA", (int) myCell.getNumericCellValue());
			                if(j==8)
			                	m.put("FECHA_NOMINA", formatoFecha(stringCellValue));
			                if(j==9)
			                	m.put("NOTA", stringCellValue);
		                }
		                
		                System.out.print(stringCellValue+"\t");
		            }
		            //Guardamos los datos aqui
		            if(i>0&&stringCellValue!=null){  	
		            	getJdbcTemplate().update("INSERT INTO SAM_NOMINA(ID_PROYECTO, CLV_PARTID, ID_RECURSO, ID_DEPENDENCIA, TIPO_NOMINA, NUM_QUINCENA, MES, FECHA_NOMINA, IMPORTE, NOTA) VALUES (?,?,?,?,?,?,?,?,?,?)",
		            			new Object[]{
		            				m.get("ID_PROYECTO"),
		            				m.get("CLV_PARTID"),
		            				m.get("ID_RECURSO"),
		            				m.get("ID_DEPENDENCIA"),
		            				m.get("TIPO_NOMINA"),
		            				m.get("NUM_QUINCENA"),
		            				m.get("MES"),
		            				m.get("FECHA_NOMINA"),
		            				m.get("IMPORTE"),
		            				m.get("NOTA")
		            	});
		            }
		            
		            
		            System.out.println(m.values());

		        }
            }
		});	
    }
	
	
	private void guardarDatosDeductivas(final Vector dataHolder) {
		this.getTransactionTemplate().execute(new TransactionCallbackWithoutResult(){
            @Override
            protected void   doInTransactionWithoutResult(TransactionStatus status) {
            	getJdbcTemplate().update("DELETE FROM SAM_NOMINA_DEDUCCIONES");
		        for (int i=0;i<dataHolder.size();i++) {                  
		            Vector cellStoreVector=(Vector)dataHolder.elementAt(i);
		            Map m = new HashMap();
		            for (int j=0; j< cellStoreVector.size();j++){
		                HSSFCell myCell = (HSSFCell)cellStoreVector.elementAt(j);
		                String stringCellValue = myCell.toString();
		                if(i>0){
			                if(j==0)
			                	m.put("TIPO_NOM", stringCellValue);
			                if(j==1)
			                	m.put("ID_RECURSO", (int) myCell.getNumericCellValue());
			                if(j==2)
			                	m.put("RECINTO",  stringCellValue);
			                if(j==3)
			                	m.put("CLV_RETENC", myCell.toString());
			                if(j==4)
			                	m.put("TOTAL", Double.parseDouble(stringCellValue.replace(",", "")));
			                
		                }
		                
		                System.out.print(stringCellValue+"\t");
		            }
		            //Guardamos los datos aqui
		            if(i>0){  	
		            	getJdbcTemplate().update("INSERT INTO SAM_NOMINA_DEDUCCIONES(TIPO_NOM, ID_RECURSO, RECINTO, CLV_RETENC, TOTAL) VALUES (?,?,?,?,?)",
		            			new Object[]{
		            				m.get("TIPO_NOM"),
		            				m.get("ID_RECURSO"),
		            				m.get("RECINTO"),
		            				m.get("CLV_RETENC"),
		            				m.get("TOTAL")
		            	});
		            }
		            
		            
		            System.out.println();

		        }
            }
		});	
    }
	
}
