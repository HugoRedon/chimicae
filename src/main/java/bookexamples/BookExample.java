package bookexamples;

import hugo.productions.google.GoogleChartArea;
import hugo.productions.google.GoogleColumn;
import hugo.productions.google.GoogleColumnType;
import hugo.productions.google.GoogleGraphInfo;
import hugo.productions.google.GoogleOptionSerie;
import hugo.productions.google.GoogleOptions;
import hugo.productions.google.GooglePosition;
import hugo.productions.google.ListPoint;
import hugo.productions.google.Point;

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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.matter.HeterogeneousMixture;
import termo.matter.Substance;
import termo.phase.Phase;
import termo.utils.IterationInfo;
import chimicae.AvailableCompounds;

import com.google.gson.Gson;

import excel.SaturationPressureReport;

public class BookExample {
	public Compound referenceCompound;
	public Compound nonReferenceCompound;

	List<ListPoint> lines = new ArrayList<>();
	GoogleGraphInfo dataTable;	
	
	public HeterogeneousMixture hm;
	String divId ;
	String imagePath;

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

	public void createCompoundsAndMixture(){
		
	}
	public BookExample(AvailableCompounds availableCompounds){
		this.availableCompounds = availableCompounds;
	}
	
	public BookExample(MixtureSystem ms,String divId,String imagePath){
		this.divId = divId;
		this.imagePath = imagePath;
		this.lines = ms.getExperimentalLines();
		this.referenceCompound =ms.getReferenceCompound();
		this.nonReferenceCompound = ms.getNonReferenceCompound();
		Set<Compound> compounds = new HashSet<>();
		compounds.add(referenceCompound);
		compounds.add(nonReferenceCompound);
		
		hm = new HeterogeneousMixture(ms.eos, ms.alpha, ms.mr, compounds, ms.k);
	}
	
	
	//public abstract void createCompoundsAndMixture(); 
	
	
	
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
	public void calculateDataTable(){
		System.out.println("paint");
        GoogleOptions options = GoogleOptions.googleOptions(
                hm.getMixingRule().getName() + " " + "(" +referenceCompound.getName()+ "," + nonReferenceCompound.getName()+")" , 
                "Fracción molar "+referenceCompound.getName(), 
                "Presión[Pa]",GooglePosition.right);
        GoogleChartArea area = new GoogleChartArea();
        area.setWidth("50%");
        options.setChartArea(area);
        dataTable = new GoogleGraphInfo();
       dataTable.setOptions(options);
       dataTable.getData().addColumn(
           new GoogleColumn("molarFraction", "Fracción molar " + referenceCompound , GoogleColumnType.number)
       );
     
   
       for(ListPoint lp: lines){
    	   if(lp.isShow()){
	    	   int index =dataTable.getData().addColumn(new GoogleColumn(lp.getId(), lp.getLabel() + " " + lp.getPhase(), GoogleColumnType.number));
	    	   dataTable.addListPoint(lp.getList(), index, GoogleOptionSerie.SCATTER);
    	   }
    	   if(lp.isLineToBeCalculated()){
    		   int i =dataTable.getData().addColumn(new GoogleColumn(lp.getId()+"c","Calculada " 
    	   + lp.getTemperature() + " [K] "  + lp.getPhase(), GoogleColumnType.number));
    		   List<Point> calculatedLine =calculateLine(lp,false);
    		   
    		   dataTable.addListPoint(calculatedLine, i, GoogleOptionSerie.FUNCTION);
    	   }if(lp.isOtherPhaseToBeCalculated()){
    		   Phase phase = (lp.getPhase().equals(Phase.VAPOR))?Phase.LIQUID:Phase.VAPOR;
    		   int i =dataTable.getData().addColumn(new GoogleColumn(lp.getId()+ "co","Calculada " 
    	   + lp.getTemperature() + " [K]" + phase,GoogleColumnType.number));
    		   List<Point> otherPhaseCalculatedLine = calculateLine(lp,true);
    		   dataTable.addListPoint(otherPhaseCalculatedLine, i, GoogleOptionSerie.FUNCTION);
    	   }
       }              
	}
	
	public GoogleGraphInfo dataTable(){
        if(dataTable==null){
        	calculateDataTable();
        }
       return dataTable;
   }

	
	
	public List<Point> calculateLine(ListPoint listPoint,boolean otherPhase){
		 	
			List<Point> experimentalLine = listPoint.getList();
			hm.setTemperature(listPoint.getTemperature());
			if(listPoint.getK()!=0){
				hm.getInteractionParameters().setValue(referenceCompound, nonReferenceCompound, listPoint.getK());
			}
	       
	       double minX = getMinX(experimentalLine);
	       double maxX = getMaxX(experimentalLine);
	       
	       if(listPoint.getMinX() !=null){
	    	   minX = listPoint.getMinX();
	       }
	       if(listPoint.getMaxX() !=null){
	    	   maxX = listPoint.getMaxX();
	       }
	       
	       double maxY = getMaxY(experimentalLine);
	       Integer n = listPoint.getNForCalculation();
	       
	       List<Point> calculatedLine = new ArrayList<>();
	       
	       
	       double xStep = (maxX - minX)/n.doubleValue();
	       for(Integer i =0;i < n; i++ ){
	    	   double molarFraction = minX  + i.doubleValue() * xStep;
	    	   
	    	   Map<String,Double>estimateFractions ;
	    	   double pressureEstimate = hm.getPressure();
	    	   
	    	   hm.setZFraction(referenceCompound, molarFraction);
	    	   hm.setZFraction(nonReferenceCompound, 1-molarFraction);
	    	   
	    	   int iterations = 0;
	    	   if(listPoint.getPhase().equals(Phase.VAPOR)){
	    		   estimateFractions = hm.getLiquid().getFractions();
		    	   if(i==0 ){
		    		  iterations= hm.dewPressure();
		    		 
		    	   }else{
		    		   iterations= hm.dewPressure(pressureEstimate,estimateFractions);
		    	   }
	    	   }else{
	    		   estimateFractions = hm.getVapor().getFractions();
	    		   if(i==0){
		    		   iterations = hm.bubblePressure();
		    		
		    	   }else{
		    		   	iterations = hm.bubblePressure(pressureEstimate,estimateFractions);
		    	   }
	    	   }
	    	   
	    	   Double pressure = hm.getPressure();
	    	   System.out.println("iterations : " + iterations);

	    	   Double vaporFraction = hm.getVapor().getFractions().get(referenceCompound.getName());
	    	   Double liquidFraction = hm.getLiquid().getFractions().get(referenceCompound.getName());
	    	   
	    	   boolean forEndCondition = (i !=0 )?!(Math.abs(liquidFraction- vaporFraction) <1e-4):true;
	    	   if(!pressure.isNaN()&& !vaporFraction.isNaN()&& !liquidFraction.isNaN() &&!(pressure < 0)
	    			   && forEndCondition ){
	    		   Point p ;
	    		   if(otherPhase){
	    			   double fraction = 0;
	    			   if(listPoint.getPhase().equals(Phase.VAPOR)){
	    				   fraction = liquidFraction;
	    			   }else{
	    				   fraction = vaporFraction;
	    			   }
	    			   
	    			    p = new Point(fraction,pressure);
	    		   }else{
		    		    p = new Point(molarFraction, pressure);
		    		   
	    		   }
	    		   calculatedLine.add(p);
	    	   }else{
	    		   if(pressureEstimate>0){
	    			   if(listPoint.getPhase().equals(Phase.VAPOR) && referenceCompound.getName().equals("methane")){
	    				   for(Substance sub: hm.getLiquid().getPureSubstances()){
	    					   hm.getLiquid().setFraction(sub.getComponent(), estimateFractions.get(sub.getComponent().getName()));
	    				   }
	    			   }else{
	    				   for(Substance sub: hm.getVapor().getPureSubstances()){
	    					   hm.getVapor().setFraction(sub.getComponent(), estimateFractions.get(sub.getComponent().getName()));
	    				   } 
	    			   }
	    			   hm.setPressure(pressureEstimate);
	    		   }
	    		   for(IterationInfo ii:hm.getCalculationReport()){
	    			   System.out.println("pressure: " + ii.getPressure() + " pressure_: "+ ii.getPressure_() 
	    					   + " temperature: " + ii.getTemperature() + " newPressure : " + ii.getNewPressure() 
	    					   + " error: " +ii.getError() + " error_: " + ii.getError_());
	    		   }
	    	   }
	       }
	       
	       
	       if(listPoint.getPhase().equals(Phase.VAPOR)&& referenceCompound.getName().equals("methane")){
		       double minPressure = hm.getPressure();
		       double temperature = hm.getTemperature();
		       n = listPoint.getNForCalculation();
		       
		       double pressureStep = (maxY-minPressure)/n.doubleValue();
		       
		     
		       
		       
		       for(Integer i = 0; i < n ; i ++){
		    	   double pressure = minPressure  + pressureStep* i.doubleValue(); 
		    	   Map<String,Double>yestimates =hm.getVapor().getFractions();
		    	   Map<String,Double>xestimates = hm.getLiquid().getFractions();
		    	   
		    	   double y = hm.getVapor().getPureSubstance(referenceCompound).getMolarFraction();
			       double x = hm.getLiquid().getPureSubstance(referenceCompound).getMolarFraction();
			       double z = (y+ x)/2d;
			       hm.setZFraction(referenceCompound, z);
			       hm.setZFraction(nonReferenceCompound, 1-z);
		    	   
		    	   
		    	   double vF = hm.flash(temperature, pressure ,yestimates,xestimates,0.5);
		           
//		           if( vF >1.1 || vF <-0.1|| hm.getPressure()<0 ){		        	   
//		        	   break;
//		           }
		           System.out.println("vf" + vF);
		           
		           Double vaporFraction = hm.getVapor().getFractions().get(referenceCompound.getName());
		    	   Double liquidFraction = hm.getLiquid().getFractions().get(referenceCompound.getName());
		    	   if( !vaporFraction.isNaN()&& !liquidFraction.isNaN()
		    			   && !(Math.abs(liquidFraction- vaporFraction) <1e-4)){
		        	   Point p ;
		        	   if(otherPhase){
		        		   Double fraction;
		        		   if(listPoint.getPhase().equals(Phase.VAPOR)){
		        			   fraction = liquidFraction;
		        		   }else{
		        			   fraction = vaporFraction;
		        		   }
		        		   if(!fraction.isNaN()){
		        			   p = new Point(fraction,pressure);
		        		   }else{
		        			   continue;
		        		   }
		        	   }else{
		        		   p = new Point(vaporFraction,pressure);
		        	   }
		        	   calculatedLine.add(p);		        	   
		           }else{
		        	   
		        	   System.out.println("flash molar fraction : " + vaporFraction);
		        	   break;
		           }
		       }
		       
	       }
	       return calculatedLine;
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
	public void readFiles(){
		for(String filePath: filesPath){
			lines.add(readFile(filePath));
		}		
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
						listPoint.setLabel("Experimental " + temp +" [K] ");
						
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
	public Point readLine(String line){
		String[] words = line.split(",");
		double x = Double.valueOf(words[0]);
		double pressure = 100000*(Double.valueOf(words[1]));		
		return new Point(x,pressure);
	}

	public List<ListPoint> getLines() {
		return lines;
	}
	public void setLines(List<ListPoint> lines) {
		this.lines = lines;
	}
	public String getDivId() {
		return divId;
	}
	public void setDivId(String divId) {
		this.divId = divId;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


}

class Info{
	String label;
	double temperature;
	Phase phase;
	
	double k;
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
	public double getK() {
		return k;
	}
	public void setK(double k) {
		this.k = k;
	}
}