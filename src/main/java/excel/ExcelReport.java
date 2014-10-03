package excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Envelope;
import models.MixtureEnvelope;
import models.PointInfo;
import models.SubstanceEnvelope;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.junit.Test;

import termo.component.Compound;
import termo.eos.alpha.Alpha;
import termo.eos.mixingRule.MixingRule;
import termo.matter.Mixture;
import termo.matter.Substance;
import cp.Eqn16IGHeatCapacity;

public class ExcelReport {
	protected  HSSFWorkbook workbook;
	protected  List<HSSFCell> titles =new ArrayList<>();	
	Envelope selectedEnvelope;
	public  HSSFWorkbook createReport(Envelope selectedEnvelope){
		this.selectedEnvelope=selectedEnvelope;
		doWork();
		return workbook;
	}
	
	public void doSpecialWork(){
		createPhaseEnvelopeSheet();
	}
	public void doWork(){
		workbook = new HSSFWorkbook();
		titles.clear();
		doSpecialWork();
		createSystemSheets();
		
		for (HSSFCell hssfCell : titles) {
			HSSFSheet worksheet = hssfCell.getSheet();
			worksheet.autoSizeColumn(hssfCell.getColumnIndex());
		}
	}
	
	public void createSystemSheets(){
		if(selectedEnvelope !=null){
			if(selectedEnvelope instanceof SubstanceEnvelope){
				SubstanceEnvelope ev=(SubstanceEnvelope)selectedEnvelope;
				createSubstanceSheet(ev.getHeterogeneousSubstance().getLiquid());
			}else {
				MixtureEnvelope me =(MixtureEnvelope)selectedEnvelope;
				createMixtureSheets(me.getHeterogeneousMixture().getLiquid());
			}
		}
		
	}
	
	public void createMixtureSheets(Mixture mix){
		HSSFSheet s = workbook.createSheet("Mezcla");
		
		int sheet = workbook.getSheetIndex(s);
		
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		createCell(sheet, 0, 0, "Ecuación cúbica", cellStyle);
		createCell(sheet,0,1,mix.getCubicEquationOfState().getName(),null);
				
		int nextRow = molarFractionsForMixture(sheet,2,0,mix);
		
		mixingRuleCells(sheet,nextRow +1,0,mix);
	}
	
	public int mixingRuleCells(int sheet,int startRow,int column ,Mixture mix){
		int count =startRow;
		MixingRule mr = mix.getMixingRule();
		createCell(sheet, count, column, "Regla de mezclado", null);
		createCell(sheet, count++,column+ 1, mix.getMixingRule().getName(), null);
		
		for(int i=0;i<mr.numberOfParameters();i++){
			int row = count++;//primera fila nombres de compuestos
			int col = column;
			String paramName = mr.getParameterName(i);
			createCell(sheet,row,col++,paramName,null);
			for(Substance sub: mix.getPureSubstances()){
				createCell(sheet,row,col++,sub.getComponent().getName(),null);
			}
			//final de primera fila			
			for(Substance isub: mix.getPureSubstances()){
				int columnCount = column;
				row=count++;
				
				createCell(sheet,row,columnCount++,isub.getComponent().getName(),null);
				
				for(Substance jsub: mix.getPureSubstances()){
					double value = mr.getParameter(isub.getComponent(), jsub.getComponent(), mix.getBinaryParameters(), i);
					createCell(sheet, row, columnCount++, value, null);
				}
				
			}
		}
		
		return count;
	}
	
	public int molarFractionsForMixture(int sheet,int startRow,int column,Mixture mix){
		int count=startRow;
		createCell(sheet, count, column, "Compuesto", null);
		createCell(sheet, count++,column+ 1, "Fracción molar", null);
		
				
		for(Substance sub: mix.getPureSubstances()){
			createSubstanceSheet(sub);
			String name = sub.getComponent().getName();
			double molarFraction = sub.getMolarFraction();
			int row = count++;
			createCell(sheet,row,column,name,null);
			createCell(sheet,row,column+1,molarFraction,null);
		}
		return count;
	}
	
	public void createSubstanceSheet(Substance substance){
		//HeterogeneousSubstance substance =substanceEnvelope.getHeterogeneousSubstance();
		Compound compound = substance.getComponent();
		Eqn16IGHeatCapacity cp =(Eqn16IGHeatCapacity) compound.getCp();
		
		HSSFSheet s=workbook.createSheet(compound.getName());
		int sheet = workbook.getSheetIndex(s);
		titles.add(createCell(sheet, 0, 0, "Propiedad", null));
		titles.add(createCell(sheet, 0, 1, "Valor", null));
		
		
		int nextIndex =propertiesTitles(sheet, 1, 0);
		propertiesValues(sheet,1,1,compound,substance);
		nextIndex =createAlphaParameters(sheet,nextIndex +1, 0,substance);				
		
		createCpCells(sheet, nextIndex+1, 0, cp);
						
	}
	public int propertiesValues(int sheet, int rowStart,int column,Compound compound,Substance sub){
		createCell(sheet,rowStart++,column,compound.getCriticalTemperature(),null);			
		createCell(sheet,rowStart++,column,compound.getCriticalPressure(),null);			
		createCell(sheet,rowStart++,column,compound.getAcentricFactor(),null);
		createCell(sheet,rowStart++,column,compound.getEnthalpyofFormationofIdealgasat298_15Kand101325Pa(),null);
		createCell(sheet,rowStart++,column,compound.getAbsoluteEntropyofIdealGasat298_15Kand101325Pa(),null);
		createCell(sheet,rowStart++,column,sub.getCubicEquationOfState().getName(),null);		
		
		return rowStart;
	}
	
	public int propertiesTitles(int sheet, int rowStart,int column){
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		createCell(sheet, rowStart++, column, "Temperatura crítica [K]", cellStyle);
		createCell(sheet, rowStart++, column, "Presión crítica [K]", cellStyle);
		createCell(sheet, rowStart++, column, "Factor acéntrico [1]", cellStyle);
		createCell(sheet,rowStart++,column,"Entalpía de formación del GI a 298.15[K] y 101325[Pa] ",cellStyle);
		createCell(sheet,rowStart++,column,"Entropía absoluta del GI a 298.15[K] y 101325[Pa] ",cellStyle);
		createCell(sheet, rowStart++, column, "Ecuación cúbica", cellStyle);
		
		
		return rowStart;

	}
	
	public int createCpCells(int sheet, int startRow,
			int column, Eqn16IGHeatCapacity cp ){
		
		System.out.println(cp.cp(298.15));
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		int count=0;
		int np = cp.numberOfParameters();
		
		createCell(sheet, startRow + count++ , column, "Ecuación capacidad calorífica", cellStyle);
		
		Map<String,String> params = new HashMap<>();
		for(int i =0; i<np;i++){
			double paramValue = cp.getParameter(i);
			String paramName = cp.getParameterName(i);
			int row = startRow +count++;
			createCell(sheet, row , column, paramName, cellStyle);
			createCell(sheet,row ,column + 1,paramValue,null);
			params.put(paramName, getCellReference(sheet, row, column +1, true, true));
		}
		
		int temperatureRowIndex =startRow+count++; 
		createCell(sheet,temperatureRowIndex,column ,"Temperatura [K]",cellStyle);
		createCell(sheet,temperatureRowIndex,column +1,298.15,null);
		
		params.put("T", getCellReference(sheet, temperatureRowIndex,
				column+1, false, false));
		createCell(sheet,temperatureRowIndex-1,column + 2,"Fórmula cp para excel",cellStyle);
		createCell(sheet,temperatureRowIndex, column +2, getCpEquationForExcellCell(params),null );
		
		String formulaCellReference = getCellReference(sheet, temperatureRowIndex, column+2, false, false);
		CellReference cellReference = new CellReference(formulaCellReference);
		HSSFSheet s =workbook.getSheetAt(sheet); 
		Row row = s.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol()); 

		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		    evaluator.evaluateFormulaCell(cell);	
		return startRow + count;
	}
	public String getCpEquationForExcellCell(Map<String,String> params){
		String A = params.get("A");
		String B=params.get("B");
		String C=params.get("C");
		String D=params.get("D");
		String E=params.get("E");
		String T=params.get("T");
		
		StringBuilder sb = new StringBuilder();
		sb.append("=");
		sb.append(A);
		sb.append("+");
		sb.append("exp(");
		sb.append(B +"/"+ T+ "+" +C +"+"+ D +"*" +T+ "+"); 
		sb.append(E + "*" + T +"^"+ 2);
		sb.append(")");
		
		return sb.toString();
	}
	private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	public String getCellReference(int sheet,Integer row,int col, boolean fixCol,boolean fixRow){
		
		String sheetName=workbook.getSheetName(sheet);
		String columnString = String.valueOf(alphabet[col]);
		if(fixCol){
			columnString ="$"+ columnString;
		}
		String rowString = ((Integer)(row+1)).toString();
		if(fixRow){
			rowString = "$" + rowString;
		}
		
		return "'"+ sheetName+ "'." + columnString + rowString;
		

	}
	
	public int createAlphaParameters(int sheet,int startRow,
		int column,
		Substance sub){
		Alpha alpha = sub.getAlpha();
		Compound com = sub.getComponent();
		
		
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.LAVENDER.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		int count = startRow;
		int np = alpha.numberOfParameters();
		createCell(sheet,count, column, "Expresión de alfa", cellStyle);
		createCell(sheet,count++,column+1,alpha.getName(),null);
		for(int i =0; i< np;i++){
			double param = alpha.getParameter(com,i);
			String paramName = alpha.getParameterName(i);
			
			createCell(sheet, count, column, paramName, cellStyle);
			createCell(sheet,count,column+1,param,null);
			count++;
		}
		return count;
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
		
						
		
	}
	
	public void fillRows(int startRow,int startColumn, List<PointInfo> list){
		int i = startRow;
		for(PointInfo p : list){
			int col =startColumn;
			createCell(0, i, col++, p.getTemperature(), null);
			createCell(0, i, col++, p.getPressure(), null);
			createCell(0, i, col++, p.getMolarVolume(), null);
			createCell(0, i, col++, p.getEnthalpy(), null);
			createCell(0, i, col++, p.getEntropy(), null);
			createCell(0, i, col++, p.getGibbs(), null);
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
	
	public HSSFCell createCell(String sheetName, int i , int j ,Object value, CellStyle cellStyle){
		HSSFSheet hs =workbook.getSheet(sheetName); 
		if(hs==null){
//			WorkbookUtil wu = new WorkbookUtil();
//			wu.validateSheetName(sheetName);
			if(sheetName.toCharArray().length > 31){
				System.out.println("posible error por extension del nombre de la hoja");
			}
			hs = workbook.createSheet(sheetName);
			System.out.println(sheetName + "created");
		}
		int sheetIndex = workbook.getSheetIndex(hs);
		return createCell(sheetIndex, i, j, value, cellStyle);
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
