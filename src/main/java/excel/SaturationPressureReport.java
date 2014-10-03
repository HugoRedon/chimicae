package excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

import termo.component.Compound;
import termo.matter.HeterogeneousMixture;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.utils.IterationInfo;

public class SaturationPressureReport extends ExcelReport {
	
	HeterogeneousMixture heterogeneousMixture;
	
	
	public HSSFWorkbook createReport(HeterogeneousMixture hm){
		this.heterogeneousMixture= hm;
		doWork();
		return workbook;
	}
	@Override
	public void doSpecialWork() {
		heterogeneousMixture.bubblePressureEstimate();
		
		pressureEstimateSheet();
		for(IterationInfo ii: heterogeneousMixture.getCalculationReport()){
			iterationPropertiesSheet(0,ii);	
		}
		createMixtureSheets(heterogeneousMixture.getLiquid());
		
		//next column 7
		
	}
	
	public int addTitles(String sheetName,int row,int colStart, String[] titles){
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		
		int colCount = colStart;
		for(int i =0; i < titles.length;i++){
			super.titles.add(createCell(sheetName,row,colCount++,titles[i],cellStyle));
		}
		return colCount;
	}
	
	public int substancesProperties(String sheetName,int rowStart,int columnStart){
		int count =rowStart;
		String[] rowTitles={"Compuesto","x (dato)",
				"y (calculada)",
				"alfa","ai","bi",
				};
		
		addTitles(sheetName,count++,columnStart,rowTitles);
		
		
		for(Substance liquidSub: heterogeneousMixture.getLiquid().getPureSubstances()){
			
			int row =count++;
			Substance vaporSub =heterogeneousMixture.getVapor().getPureSubstance(liquidSub.getComponent());
			
			createCell(sheetName, row, columnStart+0,liquidSub.getComponent().getName(),null);
			createCell(sheetName, row, columnStart+1,liquidSub.getMolarFraction(),null);
			createCell(sheetName, row, columnStart+2,vaporSub.getMolarFraction(),null);
			createCell(sheetName, row, columnStart+3,liquidSub.getAlpha().alpha(liquidSub.getTemperature(), liquidSub.getComponent()),null);
			createCell(sheetName, row, columnStart+4,liquidSub.calculate_a_cubicParameter(),null);
			createCell(sheetName, row, columnStart+5,liquidSub.calculate_b_cubicParameter(),null);									
		}
		return count;
	}
	
	public int addRowTitles(String sheetName,int startRow,int column,String[] titles){
		int count = startRow;
		int headerrow=count++;
		createCell(sheetName,headerrow,column+1,"líquido",null);
		createCell(sheetName,headerrow,column+2,"vapor",null);		
		for(int i =0; i < titles.length;i++){
			int row = count++;
			createCell(sheetName,row,column,titles[i],null);
		}
		
		return count;
	}
	
	public int heterogeneousMixtureProperties(String sheetName,int mixturerowStart,int startColumn){
		String[] rowMixtureTitles = {"a","b","z","v"};
		
		
		int count =addRowTitles(sheetName,mixturerowStart,0,rowMixtureTitles);
		
		int liquidColumn = startColumn+1;
		int vaporColumn = startColumn +2;
		
		homogenousProperties(sheetName,mixturerowStart+1,liquidColumn,heterogeneousMixture.getLiquid());
		homogenousProperties(sheetName,mixturerowStart+1,vaporColumn,heterogeneousMixture.getVapor());
		
		return count;
	}
	
	
	public void homogenousProperties(String sheetName,int rowStart,int column,Mixture mix){
		createCell(sheetName,rowStart,column,mix.calculate_a_cubicParameter(),null);			
		createCell(sheetName,rowStart+1,column,mix.calculate_b_cubicParameter(),null);
		createCell(sheetName,rowStart+2,column,mix.calculateCompresibilityFactor(),null);
		createCell(sheetName,rowStart+3,column,mix.calculateMolarVolume(),null);
	}
	
	
	
	public void iterationPropertiesSheet(int columnStart,IterationInfo ii){
		String sheetName = "Iteración " + ii.getIteration();
		
		iterationProperties(sheetName, 0, columnStart, ii.getTemperature(), ii.getPressure(),ii.getError());
		int count =iterationProperties(sheetName, 0, columnStart+7, ii.getTemperature(), ii.getPressure_(),ii.getError_());
		
		
		String[] titles = {"compuesto","razon de equilibrio"};
		addTitles(sheetName, count++, columnStart, titles);
		for(Compound c: heterogeneousMixture.getComponents()){
			int row = count++;
			createCell(sheetName,row,columnStart,c.getName(),null);
			createCell(sheetName,row,columnStart+1,heterogeneousMixture.equilibriumRelation(c),null);
		}
		createCell(sheetName,count,columnStart,"Nueva presión",null);
		createCell(sheetName,count++,columnStart+1,ii.getNewPressure(),null);
		
		
		
	}
	
	public int iterationProperties(String sheetName,int rowStart,int columnStart,double temp, double press,double error){
		heterogeneousMixture.setTemperature(temp);
		heterogeneousMixture.setPressure(press);
		int count = rowStart;
		
		createCell(sheetName, count, columnStart, "Temperatura [K]", null);
		createCell(sheetName,count++,columnStart+1,"Presión [Pa]",null);
		
		createCell(sheetName,count,columnStart,temp,null);
		createCell(sheetName,count++,columnStart+1,press,null);
		
		
		count = substancesProperties(sheetName,count++,columnStart);
		count = heterogeneousMixtureProperties(sheetName,count++,columnStart);
		count =substanceDependentMixtureProperties(sheetName, count++, columnStart);
		
		createCell(sheetName,count,columnStart,"Error",null);
		createCell(sheetName,count++,columnStart+1,error,null);
		return count++;
		
	}
	
	public int substanceDependentMixtureProperties(String sheetName,int rowStart,int columnStart){
		String[] headerTitles = {"Compuesto","(1/aN)(daN2/dNi)","(1/b)(dbN/dNi)","coeficiente de Fugacidad"};
		int count = rowStart;
		addTitles(sheetName, count++, columnStart, headerTitles);
		
		for(Substance liquidSub: heterogeneousMixture.getLiquid().getPureSubstances()){
			
			int row =count++;
			int vaporRow = count++;
			Substance vaporSub =heterogeneousMixture.getVapor().getPureSubstance(liquidSub.getComponent());
			
			createCell(sheetName, row,columnStart+ 0,liquidSub.getComponent().getName() + " líquido",null);
			createCell(sheetName, row, columnStart+1,heterogeneousMixture.getLiquid().oneOver_N_Parcial_a(liquidSub),null);
			createCell(sheetName, row, columnStart+2,heterogeneousMixture.getLiquid().oneOver_N_Parcial_b(liquidSub),null);
			createCell(sheetName, row, columnStart+3,heterogeneousMixture.getLiquid().calculateFugacityCoefficient(liquidSub.getComponent()),null);
			
			createCell(sheetName, vaporRow, columnStart+0,vaporSub.getComponent().getName() + " vapor",null);			
			createCell(sheetName, vaporRow, columnStart+1,heterogeneousMixture.getVapor().oneOver_N_Parcial_a(vaporSub),null);
			createCell(sheetName, vaporRow, columnStart+2,heterogeneousMixture.getVapor().oneOver_N_Parcial_b(vaporSub),null);
			createCell(sheetName, vaporRow, columnStart+3,heterogeneousMixture.getVapor().calculateFugacityCoefficient(vaporSub.getComponent()),null);
			
//			//createCell(sheetName, row, 4,heterogeneousMixture.equilibriumRelation(liquidSub.getComponent()),null);
//			createCell(sheetName, vaporRow, 4,heterogeneousMixture.equilibriumRelation(vaporSub.getComponent()),null);
		}
		
		return count++;
	}
	
	
	
	
	public void pressureEstimateSheet(){
		
		String sheetName ="Estimado P Burbuja";
		
		String[] rowTitles={"Compuesto","Fracción molar del líquido (dato)",
				"presión de vapor según factor acéntrico","Fracción molar del vapor (calculada)"};
		for(int i = 0; i <rowTitles.length; i++){
			createCell(sheetName,0,i,rowTitles[i],null);
			createCell(sheetName,0,i,rowTitles[i],null);
		}
		
		int count=1;
		heterogeneousMixture.bubblePressureEstimate();
		for(Substance sub: heterogeneousMixture.getLiquid().getPureSubstances()){
			int row =count++;
			createCell(sheetName, row, 0,sub.getComponent().getName(),null);
			createCell(sheetName,row,1,sub.getMolarFraction(),null);			
			createCell(sheetName, row, 2,sub.calculatetAcentricFactorBasedVaporPressure(),null);
			double y = heterogeneousMixture.getVapor().getPureSubstance(sub.getComponent()).getMolarFraction();
			createCell(sheetName, row, 3,y,null);
		}
		
		createCell(sheetName,count,0,"presión estimada",null);		
		double estimatedPressure = heterogeneousMixture.getPressure();
		createCell(sheetName,count,1,estimatedPressure,null);
		
	}
	
	
}
