package chimicae;

import hugo.productions.google.ChartType;
import hugo.productions.google.GoogleColumn;
import hugo.productions.google.GoogleColumnType;
import hugo.productions.google.GoogleDataTable;
import hugo.productions.google.GoogleGraphInfo;
import hugo.productions.google.GoogleOptions;
import hugo.productions.google.GoogleRow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.MixingRules;
import termo.matter.HeterogeneousMixture;
import termo.utils.IterationInfo;

@Named("bookExamples")
@SessionScoped
public class BookExamples implements Serializable {
	private static final long serialVersionUID = -456711596676697713L;
	
	Compound referenceCompound;
	Compound nonReferenceCompound;
	List<Point> liquidLine = new ArrayList<>();
	List<Point> vaporLine= new ArrayList<>();
	HeterogeneousMixture hm;
	
	@Inject AvailableCompounds availableCompounds;

	
	public void printProperties(Compound compound){
		double tc = compound.getCriticalTemperature();
		double pc = compound.getCriticalPressure();
		double w = compound.getAcentricFactor();
		
		System.out.println("tc: " + tc + " pc: " + pc + " w: " + w);
	}
	@PostConstruct
	public void bookExamples() {
		
		referenceCompound = availableCompounds.getCompoundByExactName("methane");
		referenceCompound.setK_StryjekAndVera(-0.00159);
		
		printProperties(referenceCompound);
		nonReferenceCompound = availableCompounds.getCompoundByExactName("n-pentane");
		nonReferenceCompound.setK_StryjekAndVera(0.03946);
		
		printProperties(nonReferenceCompound);
		Set<Compound> compounds = new HashSet<>();
		compounds.add(referenceCompound);
		compounds.add(nonReferenceCompound);
		
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		k.setSymmetric(true);
		k.setValue(referenceCompound, nonReferenceCompound, 0.0215);
		
		 hm = new HeterogeneousMixture(EquationsOfState.pengRobinson(),
				Alphas.getStryjekAndVeraExpression(),
				MixingRules.vanDerWaals(),
				compounds, k);
		 hm.setTemperature(310);
		addMethanePentaneHeterogeneousMixtureAndCompounds();
	}
	public void addMethanePentaneHeterogeneousMixtureAndCompounds(){
		String liquid310FilePath = "/data/methaneandnpentane/methaneandnpentane_310_liquid.txt";
		String vapor310FilePath = "/data/methaneandnpentane/methaneandnpentane_310_vapor.txt";
		
		List<String> liquidLines =readFile(liquid310FilePath);
		List<String> vaporLines = readFile(vapor310FilePath);
		
		liquidLines.forEach(l-> liquidLine.add(readLine(l)));
		vaporLines.forEach(l-> vaporLine.add(readLine(l)));			
	}

	public Point readLine(String line){
		String[] words = line.split(",");
		double x = Double.valueOf(words[0]);
		double pressure = 100000*(Double.valueOf(words[1]));		
		return new Point(x,pressure);
	}
	
	public List<String> readFile(String filePath) {
		List<String> lines =new ArrayList<>();
		try{
			InputStream file = this.getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(file));
			String linew =null;
			while ((linew = br.readLine()) != null) {
				if(!linew.substring(0, 1).equals("#")){
					lines.add(linew);
				}
			}
		}catch(IOException e){
			System.out.println("Cant read file " + filePath);
		}
		return lines;
	}
	public double getMinX(List<Point> list){
		double minX = list.get(0).getX();
		for(Point p : list){
			if(p.getX()<minX){
				minX = p.getX();
			}
		}
		return minX;
	}
	public double getMaxX(List<Point> list){
		double maxX = list.get(0).getX();
		for(Point p : list){
			if(p.getX()>maxX){
				maxX= p.getX();
			}
		}
		return maxX;
	}
	public double getMaxY(List<Point> list){
		double maxY = list.get(0).getY();
		for(Point p : list){
			if(p.getY()>maxY){
				maxY= p.getY();
			}
		}
		return maxY;
		
	}
	
	public GoogleGraphInfo dataTable(){
        System.out.println("paint");
       GoogleDataTable table = new GoogleDataTable( );
      
       table.addColumns(
           new GoogleColumn("molarFraction", "Fracción molar " + referenceCompound , GoogleColumnType.number),
           new GoogleColumn("Texp", "Presión experimental [K] (liquido)" , GoogleColumnType.number),
           new GoogleColumn("Tcalc", "Presión calculada [K] (liquido)" , GoogleColumnType.number),
           new GoogleColumn("TexpV", "Presión experimental [K] (vapor)", GoogleColumnType.number),
           new GoogleColumn("TcalcV", "Presión calculada [K] (vapor)", GoogleColumnType.number)
       );
     
       
       
       calculateLiquidLine(table);
       
       for (Point point: liquidLine){
           double liquidFraction = point.getX();
           double pressure = point.getY();                            
           table.addRow(new GoogleRow(liquidFraction,pressure));           
       }
       
       double minX = getMinX(vaporLine);
       double maxX = getMaxX(vaporLine);
       
       double maxY = getMaxY(vaporLine);
       Integer n = 50;
       
       double xStep = (maxX - minX)/n.doubleValue();
       for(Integer i =0;i < n; i++ ){
    	   double vaporMolarFraction = minX  + i.doubleValue() * xStep;
    	   
    	   Map<String,Double>vaporFractions = hm.getLiquid().getFractions();
    	   double pressureEstimate = hm.getPressure();
    	   
    	   hm.setZFraction(referenceCompound, vaporMolarFraction);
    	   hm.setZFraction(nonReferenceCompound, 1-vaporMolarFraction);
    	   
    	   if(i==0){
    		   hm.dewPressure();
    	   }else{
    		   hm.dewPressure(pressureEstimate,vaporFractions);
    	   }
    	   Double pressure = hm.getPressure();
    	   if(!pressure.isNaN()){
    		   table.addRow(new GoogleRow(vaporMolarFraction,null,null,null,pressure));
    	   }else{
    		   pressure = pressureEstimate;
    		   for(IterationInfo ii:hm.getCalculationReport()){
    			   System.out.println("pressure: " + ii.getPressure() + " pressure_: "+ ii.getPressure_() 
    					   + " temperature: " + ii.getTemperature() + " newPressure : " + ii.getNewPressure() 
    					   + " error: " +ii.getError() + " error_: " + ii.getError_());
    		   }
    	   }
       }
       
       
       double minPressure = hm.getPressure();
       double temperature = hm.getTemperature();
       n = 25;
       
       double pressureStep = (maxY-minPressure)/n.doubleValue();
       
       for(Integer i = 0; i < n ; i ++){
    	   double pressure = minPressure  + pressureStep* i.doubleValue(); 
    	   
    	   hm.flash(temperature, pressure ,hm.getVapor().getFractions(),hm.getLiquid().getFractions(),1);
           Double vmf =hm.getVapor().getReadOnlyFractions().get(referenceCompound);
           
           if(!vmf.isNaN()){
        	   table.addRow(new GoogleRow(vmf,null,null,null,pressure));
           }else{
        	   
        	   System.out.println("flash molar fraction : " + vmf);   
           }
       }
       
       
       
       
       for (Point point: vaporLine){
            double vaporFraction = point.getX();   
            double p = point.getY();
                 
            table.addRow( new GoogleRow(vaporFraction,null, null,p));
       }
//       for (TemperatureMixtureErrorData row: selectedMix.getErrorfunction().getErrorForEachExperimentalData()){
//           double expT = row.getExperimentalTemperature();   
//           double expY = row.getExperimentalVaporFraction();
//           Double calcT = row.getCalculatedTemperature();
//           Double calcY = row.getCalculatedVaporFraction();
//
//           calcT = (Double.isNaN(calcT)?null: calcT);
//           calcY = (Double.isNaN(calcY)? null: calcY);
//           table.addRow( new GoogleRow(calcY,null, null,null,  calcT));
//           
//       }
            
       
       
       
       GoogleGraphInfo pressureComparison = new GoogleGraphInfo();
       pressureComparison.setData(table);
        pressureComparison.setOptions(GoogleOptions.googleOptions(
              "Fracción molar vs Presión", 
              "Fracción molar", 
              "Presión[Pa]",ChartType.scatterFunctionScatterFunction));
       return pressureComparison;
   }
	
	
	public void calculateLiquidLine(GoogleDataTable table){
		double minX = getMinX(liquidLine);
	       double maxX = getMaxX(liquidLine);
	       
	       Integer n = 20;
	       double xStep = (maxX - minX)/n.doubleValue();
	       for(Integer i =0;i < n; i++ ){
	    	   double liquidMolarFraction = minX  + i.doubleValue() * xStep;
	    	   
	    	   Map<String,Double>vaporFractions = hm.getVapor().getFractions();
	    	   double pressureEstimate = hm.getPressure();
	    	   
	    	   hm.setZFraction(referenceCompound, liquidMolarFraction);
	    	   hm.setZFraction(nonReferenceCompound, 1-liquidMolarFraction);
	    	   
	    	   if(i==0){
	    		   hm.bubblePressure();
	    	   }else{
	    		   hm.bubblePressure(pressureEstimate,vaporFractions);
	    	   }
	    	   Double pressure = hm.getPressure();
	    	   if(!pressure.isNaN()){
	    		   table.addRow(new GoogleRow(liquidMolarFraction,null,pressure));
	    	   }else{
	    		   pressure = pressureEstimate;
	    		   for(IterationInfo ii:hm.getCalculationReport()){
	    			   System.out.println("pressure: " + ii.getPressure() + " pressure_: "+ ii.getPressure_() 
	    					   + " temperature: " + ii.getTemperature() + " newPressure : " + ii.getNewPressure() 
	    					   + " error: " +ii.getError() + " error_: " + ii.getError_());
	    		   }
	    	   }
	       }
	}

}

class Point{
	double x;
	double y;
	
	public Point() {
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
}