package excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import termo.matter.HeterogeneousMixture;
import termo.matter.Substance;

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
		pressureTemperatureProperties();
		
		
		
	}
	public void pressureTemperatureProperties(){
		//String sheetName= heterogeneousMixture.getPressure()+"(Pa)," 
		//		+ heterogeneousMixture.getTemperature()+"(K) ";
		String sheetName = "Propiedades";
		String[] rowTitles={"Compuesto","Fracción molar del líquido (dato)",
				"Fracción molar del vapor (calculada)",
				"alfa","ai","bi",
				};
		for(int i = 0; i <rowTitles.length; i++){
			createCell(sheetName,0,i,rowTitles[i],null);
		}
		int count=1;
		for(Substance liquidSub: heterogeneousMixture.getLiquid().getPureSubstances()){
			
			int row =count++;
			Substance vaporSub =heterogeneousMixture.getVapor().getPureSubstance(liquidSub.getComponent());
			
			createCell(sheetName, row, 0,liquidSub.getComponent().getName(),null);
			createCell(sheetName, row, 1,liquidSub.getMolarFraction(),null);
			createCell(sheetName, row, 2,vaporSub.getMolarFraction(),null);
			createCell(sheetName, row, 3,liquidSub.getAlpha().alpha(liquidSub.getTemperature(), liquidSub.getComponent()),null);
			createCell(sheetName, row, 4,liquidSub.calculate_a_cubicParameter(),null);
			createCell(sheetName, row, 5,liquidSub.calculate_b_cubicParameter(),null);									
		}
		
		String[] rowMixtureTitles = {"a","b","z","v"};
		int headerrow=count++;
		createCell(sheetName,headerrow,1,"líquido",null);
		createCell(sheetName,headerrow,2,"vapor",null);
		
		
		int mixturerowStart =count;
		for(int i =0; i < rowMixtureTitles.length;i++){
			int row = count++;
			createCell(sheetName,row,0,rowMixtureTitles[i],null);
		}
		
		createCell(sheetName,mixturerowStart,0+1,heterogeneousMixture.getLiquid().calculate_a_cubicParameter(),null);			
		createCell(sheetName,mixturerowStart+1,0+1,heterogeneousMixture.getLiquid().calculate_b_cubicParameter(),null);
		createCell(sheetName,mixturerowStart+2,0+1,heterogeneousMixture.getLiquid().calculateCompresibilityFactor(),null);
		createCell(sheetName,mixturerowStart+3,0+1,heterogeneousMixture.getLiquid().calculateMolarVolume(),null);
		
		createCell(sheetName,mixturerowStart,0+2,heterogeneousMixture.getVapor().calculate_a_cubicParameter(),null);
		createCell(sheetName,mixturerowStart+1,0+2,heterogeneousMixture.getVapor().calculate_b_cubicParameter(),null);
		createCell(sheetName,mixturerowStart+2,0+2,heterogeneousMixture.getVapor().calculateCompresibilityFactor(),null);
		createCell(sheetName,mixturerowStart+3,0+2,heterogeneousMixture.getVapor().calculateMolarVolume(),null);
		
		
		int headertitles = count++;
		
		String[] headerTitles = {"Compuesto","(1/aN)(daN2/dNi)","(1/b)(dbN/dNi)","coeficiente de Fugacidad","razón de equilibrio Ki"};
		for(int i = 0; i < headerTitles.length;i++){
			createCell(sheetName,headertitles,i,headerTitles[i],null);
		}
		
		for(Substance liquidSub: heterogeneousMixture.getLiquid().getPureSubstances()){
			
			int row =count++;
			int vaporRow = count++;
			Substance vaporSub =heterogeneousMixture.getVapor().getPureSubstance(liquidSub.getComponent());
			
			createCell(sheetName, row, 0,liquidSub.getComponent().getName() + " líquido",null);
			createCell(sheetName, vaporRow, 0,vaporSub.getComponent().getName() + " vapor",null);
			
			createCell(sheetName, row, 1,heterogeneousMixture.getLiquid().oneOver_N_Parcial_a(liquidSub),null);
			createCell(sheetName, vaporRow, 1,heterogeneousMixture.getVapor().oneOver_N_Parcial_a(vaporSub),null);
			
			createCell(sheetName, row, 2,heterogeneousMixture.getLiquid().oneOver_N_Parcial_b(liquidSub),null);
			createCell(sheetName, vaporRow, 2,heterogeneousMixture.getVapor().oneOver_N_Parcial_b(vaporSub),null);
			
			createCell(sheetName, row, 3,heterogeneousMixture.getLiquid().calculateFugacityCoefficient(liquidSub.getComponent()),null);
			createCell(sheetName, vaporRow, 3,heterogeneousMixture.getVapor().calculateFugacityCoefficient(vaporSub.getComponent()),null);
			
			//createCell(sheetName, row, 4,heterogeneousMixture.equilibriumRelation(liquidSub.getComponent()),null);
			createCell(sheetName, vaporRow, 4,heterogeneousMixture.equilibriumRelation(vaporSub.getComponent()),null);
		}
		
		
		
	}
	
	public void pressureEstimateSheet(){
		
		Double temperatureForTitle = heterogeneousMixture.getTemperature();
		String sheetName ="P Burbuja" + temperatureForTitle + "(K)";
		
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
