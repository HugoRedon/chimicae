package bookexamples;

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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import chimicae.AvailableCompounds;
import excel.SaturationPressureReport;
import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.MixingRules;
import termo.matter.HeterogeneousMixture;
import termo.utils.IterationInfo;

public abstract class BookExample {
	public Compound referenceCompound;
	public Compound nonReferenceCompound;
	public List<Point> liquidLine = new ArrayList<>();
	public List<Point> vaporLine = new ArrayList<>();
	public HeterogeneousMixture hm;

	AvailableCompounds availableCompounds;
	
	String liquidFilePath;
	String vaporFilePath;
	
	public BookExample(AvailableCompounds availableCompounds,String liquidFilePath,String vaporFilePath){
		this.liquidFilePath = liquidFilePath;
		this.vaporFilePath = vaporFilePath;
		this.availableCompounds = availableCompounds;
		createCompoundsAndMixture();
		readFiles();
	}
	
	public abstract void createCompoundsAndMixture(); 
	
	
	public void readFiles(){
	
		List<String> liquidLines =readFile(liquidFilePath);
		List<String> vaporLines = readFile(vaporFilePath);
		
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
       calculateVaporLine(table);
       
      
       
       for (Point point: liquidLine){
           double liquidFraction = point.getX();
           double pressure = point.getY();                            
           table.addRow(new GoogleRow(liquidFraction,pressure));           
       }
       
       for (Point point: vaporLine){
            double vaporFraction = point.getX();   
            double p = point.getY();
                 
            table.addRow( new GoogleRow(vaporFraction,null, null,p));
       }
                 
       GoogleGraphInfo pressureComparison = new GoogleGraphInfo();
       pressureComparison.setData(table);
        pressureComparison.setOptions(GoogleOptions.googleOptions(
              "Fracción molar vs Presión", 
              "Fracción molar", 
              "Presión[Pa]",ChartType.scatterFunctionScatterFunction));
       return pressureComparison;
   }
	
	
	public void calculateVaporLine(GoogleDataTable table){
		 
	       
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

	public void download(double temperature, double referenceFraction) throws IOException {
	    FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    ec.responseReset(); 
	    ec.setResponseContentType("text/csv"); 
	    //
	    String fileName = "report.xlsx";
	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); 

	    OutputStream output = ec.getResponseOutputStream();
	    
	    HSSFWorkbook workbook = bubblePressureReport(temperature, referenceFraction);
	    workbook.write(output);
	    output.close();
	    
	    //output.write("hola".getBytes());
	    
	    // Now you can write the InputStream of the file to the above OutputStream the usual way.
	    // ...
	    output.close();
	    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
	}
	
	public HSSFWorkbook bubblePressureReport(double temperature, double referenceFraction){
		hm.setTemperature(temperature);
		hm.setZFraction(referenceCompound, referenceFraction);
		hm.setZFraction(nonReferenceCompound, 1-referenceFraction);
		hm.bubblePressure();
		
		double ahm12 = ((ActivityModelBinaryParameter)hm.getInteractionParameters()).getA().getValue(referenceCompound, nonReferenceCompound);
		double al12 = ((ActivityModelBinaryParameter)hm.getLiquid().getBinaryParameters()).getA().getValue(referenceCompound, nonReferenceCompound);
		double av12 = ((ActivityModelBinaryParameter)hm.getVapor().getBinaryParameters()).getA().getValue(referenceCompound, nonReferenceCompound);
		
		//((ActivityModelBinaryParameter)hm.getInteractionParameters()).getA().setValue(referenceCompound, nonReferenceCompound, 0.25);
		System.out.println("ahm12: " + ahm12);
		System.out.println("al12: " + al12);
		System.out.println("av12: " + av12);
		return new SaturationPressureReport().createReport(hm);
	}

}