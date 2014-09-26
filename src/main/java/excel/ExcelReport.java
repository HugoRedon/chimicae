package excel;

import java.util.ArrayList;
import java.util.List;

import models.Envelope;
import models.PointInfo;
import models.SubstanceEnvelope;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.Test;

import termo.component.Compound;

public class ExcelReport {
	private  HSSFWorkbook workbook;
	private  List<HSSFCell> titles =new ArrayList<>();
	Envelope selectedEnvelope;
	public  HSSFWorkbook createReport(Envelope selectedEnvelope){
		this.selectedEnvelope=selectedEnvelope;
		workbook = new HSSFWorkbook();
		titles.clear();
		createPhaseEnvelopeSheet();
		createSystemSheets();
		return workbook;
		
	}
	
	public void createSystemSheets(){
		if(selectedEnvelope instanceof SubstanceEnvelope){
			createSubstanceEnvelopeSheets((SubstanceEnvelope)selectedEnvelope);
		}
	}
	public void createSubstanceEnvelopeSheets(SubstanceEnvelope substanceEnvelope){
		Compound compound = substanceEnvelope.getHeterogeneousSubstance().getComponent();
		
		
	}
	
	public void createPhaseEnvelopeSheet(){
		HSSFSheet worksheet = workbook.createSheet("Envolvente de fases");
		
		// index from 0,0... cell A1 is cell(0,0)
		List<PointInfo> liquidPoints = selectedEnvelope.getLiquidLine();
		List<PointInfo> vaporPoints =selectedEnvelope.getVaporLine();
		
		
		
		HSSFCellStyle liquidCellStyle = workbook.createCellStyle();
		liquidCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		liquidCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		liquidCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		titles.add(createCell(0, 0, 0, "Líquido", liquidCellStyle));
		worksheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
		
		HSSFCellStyle vaporCellStyle = workbook.createCellStyle();
		vaporCellStyle.setFillForegroundColor(HSSFColor.RED.index);
		vaporCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		vaporCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		titles.add(createCell(0, 0, 6, "Vapor", vaporCellStyle));
		worksheet.addMergedRegion(new CellRangeAddress(0,0,6,11));
		
		int nextIndex = createEnvelopeTitles(1,0,liquidCellStyle);
		createEnvelopeTitles(1, nextIndex, vaporCellStyle);	
		
		
		fillRows(2, 0, liquidPoints);
		fillRows(2,nextIndex,vaporPoints);
		
						
		for (HSSFCell hssfCell : titles) {
			worksheet.autoSizeColumn(hssfCell.getColumnIndex());
		}
	}
	
	public void fillRows(int startRow,int startColumn, List<PointInfo> list){
		int i = startRow;
		for(PointInfo p : list){
			int row =startColumn;
			createCell(0, i, row++, p.getTemperature(), null);
			createCell(0, i, row++, p.getPressure(), null);
			createCell(0, i, row++, p.getMolarVolume(), null);
			createCell(0, i, row++, p.getEnthalpy(), null);
			createCell(0, i, row++, p.getEntropy(), null);
			createCell(0, i, row++, p.getGibbs(), null);
			i++;
		}	
	}
	
	public  int createEnvelopeTitles(int rowIndex,int startColumnIndex,CellStyle cellStyle){
		int i = startColumnIndex;
		titles.add(createCell(0,1,i++,"Temperatura [K]",cellStyle));
		titles.add(createCell(0,1,i++,"Presión [Pa]",cellStyle));
		titles.add(createCell(0,1,i++,"Volumen molar [m^3/kmol]",cellStyle));
		titles.add(createCell(0,1,i++,"Entalpía molar [J/kmol]",cellStyle));
		titles.add(createCell(0,1,i++,"Entropía molar [J/ (kmol K)]",cellStyle));
		titles.add(createCell(0,1,i++,"E. Gibbs molar [J/kmol]",cellStyle));
		return i ;
	}
	
	public HSSFCell createCell(int sheet,int i, int j,Object value,CellStyle cellStyle){
		if(workbook ==null){
			workbook = new HSSFWorkbook();
		}
		int numberOfSheets = workbook.getNumberOfSheets();
		while(numberOfSheets <=sheet){
			workbook.createSheet();
			numberOfSheets = workbook.getNumberOfSheets();
		}
		
		if(workbook.getSheetAt(sheet).getRow(i)==null){
			workbook.getSheetAt(sheet).createRow(i);
		}
		if(workbook.getSheetAt(sheet).getRow(i).getCell(j)==null){
			workbook.getSheetAt(sheet).getRow(i).createCell(j);
		}
		HSSFCell cell= workbook.getSheetAt(sheet).getRow(i).getCell(j);
		if(value instanceof String){
			cellValue(cell,(String)value);
		}else{
			cellValue(cell,(double)value);
		}
		//cell.setCellValue(value);
		if(cellStyle !=null){
			cell.setCellStyle(cellStyle);
		}
		return cell;
		//System.out.println("is null" + (cell==null));
		
	}
	
	public void cellValue(HSSFCell cell,String value){
		cell.setCellValue(value);
	}
	public void cellValue(HSSFCell cell,double value){
		cell.setCellValue(value);
	}
	
	
	@Test
	public void test(){
		new ExcelReport().createCell(0,0, 0, "hola",null);
		
	}
}
