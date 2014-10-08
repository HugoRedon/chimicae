package bookexamples;

import hugo.productions.google.ChartType;
import hugo.productions.google.GoogleColumn;
import hugo.productions.google.GoogleColumnType;
import hugo.productions.google.GoogleDataTable;
import hugo.productions.google.GoogleGraphInfo;
import hugo.productions.google.GoogleOptionSerie;
import hugo.productions.google.GoogleOptions;
import hugo.productions.google.GoogleRow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.matter.HeterogeneousMixture;
import termo.phase.Phase;
import termo.utils.IterationInfo;
import chimicae.AvailableCompounds;

import com.google.gson.Gson;

import excel.SaturationPressureReport;

public abstract class BookExample {
	public Compound referenceCompound;
	public Compound nonReferenceCompound;
//	public List<Point> liquidLine = new ArrayList<>();
//	public List<Point> vaporLine = new ArrayList<>();
//	
	List<ListPoint> lines = new ArrayList<>();
	
	
	public HeterogeneousMixture hm;

	AvailableCompounds availableCompounds;
	
	String[] filesPath;
	
	public static String[] files(String... files){
		return files;
	}
	public BookExample(){
		
	}
	
	public BookExample(AvailableCompounds availableCompounds,String[] filesPath){
		this.filesPath = filesPath;
		this.availableCompounds = availableCompounds;
		createCompoundsAndMixture();
		readFiles();
	}
	
	public abstract void createCompoundsAndMixture(); 
	
	
	public void readFiles(){
	
		for(String filePath: filesPath){
			ListPoint liquidLines =readFile(filePath);
			lines.add(liquidLines);
		}		
	}
	public Point readLine(String line){
		String[] words = line.split(",");
		double x = Double.valueOf(words[0]);
		double pressure = 100000*(Double.valueOf(words[1]));		
		return new Point(x,pressure);
	}
	
	public ListPoint readFile(String filePath) {
		ListPoint listPoint =new ListPoint();
		try{
			InputStream file = this.getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(file));
			String linew =null;
			while ((linew = br.readLine()) != null) {
				if(linew.substring(0, 1).equals("#")){
					if(linew.substring(1,5).equals("info")){
						String json = linew.substring(6);
						Class<?> classs = Info.class;
						Info info =(Info) new Gson().fromJson(json,classs);
						Double temp = info.getTemperature();
						Phase phase = info.getPhase();
						listPoint.setLabel("Experimental " + temp +" [K] "+ phase);
						
						if(info.getPhase()==null){
							System.out.println("phase nula para el archivo " + filePath);
						}
						
						listPoint.setPhase(phase);
						listPoint.setId(phase+ temp.toString());
						listPoint.setTemperature(temp);
						
					}			
				}else{
					listPoint.getList().add(readLine(linew));
				}
			}
		}catch(IOException e){
			System.out.println("Cant read file " + filePath);
		}
		return listPoint;
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
           new GoogleColumn("molarFraction", "Fracción molar " + referenceCompound , GoogleColumnType.number)
       );
     
       GoogleOptions options = GoogleOptions.googleOptions(
               "Fracción molar vs Presión", 
               "Fracción molar", 
               "Presión[Pa]",ChartType.scatterFunctionScatterFunction);
       for(ListPoint lp: lines){
    	   table.addColumn(new GoogleColumn(lp.getId(), lp.getLabel(), GoogleColumnType.number));
    	   table.addColumn(new GoogleColumn(lp.getId()+"c","Presión calculada [Pa] " + lp.getTemperature() + " [K]" , GoogleColumnType.number));
    	   
    	   addPoints(lp, table,lines.indexOf(lp),options);
       }
       
                 
       GoogleGraphInfo pressureComparison = new GoogleGraphInfo();
       pressureComparison.setData(table);
        pressureComparison.setOptions(options);
       return pressureComparison;
   }
	public void addPoints(ListPoint lp,GoogleDataTable table,int index,GoogleOptions options){
		options.addSerie(index, GoogleOptionSerie.SCATTER);
		 for (Point point: lp.getList()){
	           double fraction = point.getX();
	           double pressure = point.getY(); 
	           Number[] columns = new Number[index+2];
	           columns[0] = fraction;
        	   columns[index+ 1] = pressure;
        	   
	           table.addRow( new GoogleRow(columns));
	           	           
	       }
		 int functionSerieIndex = lines.size()+index;
		 options.addSerie(functionSerieIndex, GoogleOptionSerie.FUNCTION);
		 calculateLine(table, lp,functionSerieIndex);
	}
	
	public void calculateLine(GoogleDataTable table,ListPoint listPoint,int index){
		 	
			List<Point> line = listPoint.getList();
			hm.setTemperature(listPoint.getTemperature());
	       
	       double minX = getMinX(line);
	       double maxX = getMaxX(line);
	       
	       double maxY = getMaxY(line);
	       Integer n = 25;
	       
	       
	       double xStep = (maxX - minX)/n.doubleValue();
	       for(Integer i =0;i < n; i++ ){
	    	   double molarFraction = minX  + i.doubleValue() * xStep;
	    	   
	    	   Map<String,Double>estimateFractions ;
	    	   double pressureEstimate = hm.getPressure();
	    	   
	    	   hm.setZFraction(referenceCompound, molarFraction);
	    	   hm.setZFraction(nonReferenceCompound, 1-molarFraction);
	    	   
	    	   
	    	   if(listPoint.getPhase().equals(Phase.VAPOR)){
	    		   estimateFractions = hm.getLiquid().getFractions();
		    	   if(i==0 ){
		    		   hm.dewPressure();
		    		 
		    	   }else{
		    		   hm.dewPressure(pressureEstimate,estimateFractions);
		    	   }
	    	   }else{
	    		   estimateFractions = hm.getVapor().getFractions();
	    		   if(i==0){
		    		   hm.bubblePressure();
		    		
		    	   }else{
		    		   hm.bubblePressure(pressureEstimate,estimateFractions);
		    	   }
	    	   }
	    	   
	    	   Double pressure = hm.getPressure();
	    	   if(pressure.equals(pressureEstimate)){
	    		   if(listPoint.getPhase().equals(Phase.VAPOR)){
	    			   hm.dewPressure();
	    		   }else{
	    			   hm.bubblePressure();
	    		   }
	    		   
	    	   }
	    	   if(!pressure.isNaN()){
	    		   Number[] columns = new Number[index+2];
	    		   columns[0] =molarFraction;
	    		   columns[index+1] = pressure;
	    		   table.addRow(new GoogleRow(columns));
	    	   }else{
	    		   pressure = pressureEstimate;
	    		   for(IterationInfo ii:hm.getCalculationReport()){
	    			   System.out.println("pressure: " + ii.getPressure() + " pressure_: "+ ii.getPressure_() 
	    					   + " temperature: " + ii.getTemperature() + " newPressure : " + ii.getNewPressure() 
	    					   + " error: " +ii.getError() + " error_: " + ii.getError_());
	    		   }
	    	   }
	       }
	       
	       
	       if(listPoint.getPhase().equals(Phase.VAPOR)){
		       double minPressure = hm.getPressure();
		       double temperature = hm.getTemperature();
		       n = 25;
		       
		       double pressureStep = (maxY-minPressure)/n.doubleValue();
		       
		       for(Integer i = 0; i < n ; i ++){
		    	   double pressure = minPressure  + pressureStep* i.doubleValue(); 
		    	   
		    	   hm.flash(temperature, pressure ,hm.getVapor().getFractions(),hm.getLiquid().getFractions(),1);
		           Double vmf =hm.getVapor().getReadOnlyFractions().get(referenceCompound);
		           
		           if(!vmf.isNaN()){
		        	   
		        	   Number[] columns = new Number[index+2];
		    		   columns[0] =vmf;
		    		   columns[index+1] = pressure;
		    		   table.addRow(new GoogleRow(columns));
		           }else{
		        	   
		        	   System.out.println("flash molar fraction : " + vmf);   
		           }
		       }
	       }
	       
	}

//	public void calculateLiquidLine(GoogleDataTable table){
//		double minX = getMinX(liquidLine);
//	       double maxX = getMaxX(liquidLine);
//	       
//	       Integer n = 100;
//	       double xStep = (maxX - minX)/n.doubleValue();
//	       for(Integer i =0;i < n; i++ ){
//	    	   double liquidMolarFraction = minX  + i.doubleValue() * xStep;
//	    	   
//	    	   Map<String,Double>vaporFractions = hm.getVapor().getFractions();
//	    	   double pressureEstimate = hm.getPressure();
//	    	   
//	    	   hm.setZFraction(referenceCompound, liquidMolarFraction);
//	    	   hm.setZFraction(nonReferenceCompound, 1-liquidMolarFraction);
//	    	   
//	    	   if(i==0){
//	    		   hm.bubblePressure();
//	    	   }else{
//	    		   hm.bubblePressure(pressureEstimate,vaporFractions);
//	    	   }
//	    	   Double pressure = hm.getPressure();
//	    	   if(!pressure.isNaN()){
//	    		   table.addRow(new GoogleRow(liquidMolarFraction,null,pressure));
//	    	   }else{
//	    		   pressure = pressureEstimate;
//	    		   for(IterationInfo ii:hm.getCalculationReport()){
//	    			   System.out.println("pressure: " + ii.getPressure() + " pressure_: "+ ii.getPressure_() 
//	    					   + " temperature: " + ii.getTemperature() + " newPressure : " + ii.getNewPressure() 
//	    					   + " error: " +ii.getError() + " error_: " + ii.getError_());
//	    		   }
//	    	   }
//	       }
//	}

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

class Info{
	String label;
	double temperature;
	Phase phase;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public Phase getPhase() {
		return phase;
	}
	public void setPhase(Phase phase) {
		this.phase = phase;
	}
}