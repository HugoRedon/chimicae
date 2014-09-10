package chimicae;

import hugo.productions.google.ChartType;
import hugo.productions.google.GoogleColumn;
import hugo.productions.google.GoogleColumnType;
import hugo.productions.google.GoogleDataTable;
import hugo.productions.google.GoogleGraphInfo;
import hugo.productions.google.GoogleOptions;
import hugo.productions.google.GoogleRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.component.Compound;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.matter.builder.HeterogeneousMixtureBuilder;

@Named("phaseEnvelopeBean")
@SessionScoped
public class PhaseEnvelopeBean implements Serializable {

	private static final long serialVersionUID = 6940496006074883582L;

	public PhaseEnvelopeBean() {
		// TODO Auto-generated constructor stub
	}
	
	@Inject HomogeneousBean homogeneousBean;
	
	public GoogleGraphInfo dataTable(){
		Homogeneous homogeneous = homogeneousBean.getSelectedHomogeneous();
		
		
		
		GoogleGraphInfo graphInfo = new GoogleGraphInfo();
		GoogleDataTable data = new GoogleDataTable();
		
		data.addColumns(new GoogleColumn("temp", "Temperatura [K]", GoogleColumnType.number),
				new GoogleColumn("press", "Presión burbuja[Pa]", GoogleColumnType.number),
				new GoogleColumn("press", "Presión rocío[Pa]", GoogleColumnType.number));
								
		Collection<GoogleRow> rows = getRows(homogeneous,data);
		data.addRows(rows);
		
		graphInfo.setData(data);
		graphInfo.setOptions(GoogleOptions.googleOptions(
				"Envolvente de fases", 
				"Temperatura [K]", 
				"Presión [Pa]", ChartType.multipleFunction));
		return graphInfo;
	}
	public HeterogeneousSubstance getHeterogeneous(Substance substance){
		return new HeterogeneousSubstance(substance.getCubicEquationOfState(),
				substance.getAlpha(),substance.getComponent());
	}

	
	public Collection<GoogleRow> getRows(Homogeneous homogeneous,GoogleDataTable data){
		Collection<GoogleRow> rows = new ArrayList<>();
		
		if(homogeneous instanceof Substance){
			
			rows = getRows((Substance)homogeneous,1,1); 
			
		}else if(homogeneous instanceof Mixture){
			rows = getRows((Mixture)homogeneous);
			rows.addAll(getRowsDew((Mixture)homogeneous));
			int nc = ((Mixture) homogeneous).getComponents().size();
			
			Iterator<Substance> iterator  = ((Mixture) homogeneous).getPureSubstances().iterator(); 
			for(int i = 3; i <= nc + 2; i++){
				Substance sub = iterator.next();
				data.addColumn(new GoogleColumn("comp" +sub.getComponent().getName(),
					sub.getComponent().getName(), GoogleColumnType.number));
				rows.addAll(getRows(sub,i ,nc));
			}
			
		}
		return rows;
	}
	
	public HeterogeneousMixture getHeterogeneous(Mixture mix){
		HeterogeneousMixture hetMixture =new HeterogeneousMixtureBuilder().fromMixture(mix);
		return hetMixture;
	}
	
	

	
	
	
	public Collection<GoogleRow> getRows(Mixture mix){
		Collection<GoogleRow> liquidlineRows = new ArrayList<>();
		Collection<GoogleRow> vaporlineRows = new ArrayList<>();
		
		double minCriticalPressure =getMinimumCriticalPressure(mix);		
		double minCriticalTemperature =getMinimumCriticalTemperature(mix);		
		HeterogeneousMixture heterogeneousMixture = getHeterogeneous(mix);
		
		double pressure = minCriticalPressure*0.3;		
		
		
		heterogeneousMixture.setPressure(pressure);
		int bubbleIterations =heterogeneousMixture.bubbleTemperature();
		double bubbleTemperature = heterogeneousMixture.getTemperature();
		
//		int dewIterations = heterogeneousMixture.dewTemperature();
//		double dewTemperature = heterogeneousMixture.getTemperature();
		
				
		liquidlineRows.add(new GoogleRow(bubbleTemperature,pressure));
//		vaporlineRows.add(new GoogleRow(dewTemperature,pressure));
		
		System.out.println("iterations: " +bubbleIterations + "("+ bubbleTemperature + ","+pressure+")");
		
		double pressureStep = (minCriticalPressure- pressure)/50d;
		
		double nextPressure = pressure + pressureStep;
		heterogeneousMixture.setPressure(nextPressure);
		 bubbleIterations =heterogeneousMixture.bubbleTemperature(bubbleTemperature);
		double nextTemperature = heterogeneousMixture.getTemperature();
		
		liquidlineRows.add(new GoogleRow(nextTemperature,nextPressure));
		
		double slope = (Math.log(nextPressure)-Math.log(pressure) )
					   /(Math.log(nextTemperature)-Math.log(bubbleTemperature));
		
		
		double volumeDifference = Math.abs(heterogeneousMixture.getVapor().calculateMolarVolume())
				-Math.abs(heterogeneousMixture.getLiquid().calculateMolarVolume());
		
		System.out.println("iterations: " +bubbleIterations + "("+ bubbleTemperature + ","+pressure+")" + volumeDifference);
		
		
		double temperatureStep = 10;
		while(Math.abs(volumeDifference) > volumeDifferenceTolerance  ){
			pressure = nextPressure;
			bubbleTemperature = nextTemperature;
			bubbleIterations = 0;
			if(slope < 2){//bubble or dew point pressures
				nextTemperature = bubbleTemperature + temperatureStep;
				heterogeneousMixture.setTemperature(nextTemperature);
				bubbleIterations = heterogeneousMixture.bubblePressure(pressure);
				nextPressure = heterogeneousMixture.getPressure();
				
				
			}else{//bubble or dew point temperature
				nextPressure = pressure + pressureStep;
				heterogeneousMixture.setPressure(nextPressure);
				bubbleIterations = heterogeneousMixture.bubbleTemperature(bubbleTemperature);
				nextTemperature = heterogeneousMixture.getTemperature();				
			}
			System.out.println("iterations"+bubbleIterations);
			liquidlineRows.add(new GoogleRow(nextTemperature,nextPressure));
			System.out.println("iterations: " +bubbleIterations + "("+ nextTemperature + ","+nextPressure+")" + volumeDifference);
			slope = (Math.log(nextPressure)-Math.log(pressure) )
					   /(Math.log(nextTemperature)-Math.log(bubbleTemperature));
			volumeDifference = Math.abs(heterogeneousMixture.getLiquid().calculateMolarVolume())
					-Math.abs(heterogeneousMixture.getVapor().calculateMolarVolume());
		}
		
		
		
		
		
		
		return liquidlineRows;	
		
	
	}
	double volumeDifferenceTolerance = 0.07;
	
	

	public double getVolumeDifferenceTolerance() {
		return volumeDifferenceTolerance;
	}
	public void setVolumeDifferenceTolerance(double volumeDifferenceTolerance) {
		this.volumeDifferenceTolerance = volumeDifferenceTolerance;
	}
	public Collection<GoogleRow> getRowsDew(Mixture mix){
		Collection<GoogleRow> rows = new ArrayList<>();
		
		double minCriticalPressure =getMinimumCriticalPressure(mix);
		double minCriticalTemperature =getMinimumCriticalTemperature(mix);
		
					
		HeterogeneousMixture heterogeneousMixture = getHeterogeneous(mix);
		
		double pressure = minCriticalPressure*0.3;
		//double startTemperature = minCriticalTemperature*0.5;
		
																																																															
		heterogeneousMixture.setPressure(pressure);
		int iterations =heterogeneousMixture.dewTemperature();
		double temperature = heterogeneousMixture.getTemperature();
		
		rows.add(new GoogleRow(temperature,null,pressure));
		System.out.println("iterations: " +iterations + "("+ temperature + ","+pressure+")");
		
		double pressureStep = (minCriticalPressure- pressure)/100d;
		
		double nextPressure = pressure + pressureStep;
		heterogeneousMixture.setPressure(nextPressure);
		 iterations =heterogeneousMixture.dewTemperature(temperature);
		//iterations =heterogeneousMixture.dewTemperature();
		double nextTemperature = heterogeneousMixture.getTemperature();
		
		rows.add(new GoogleRow(nextTemperature,null,nextPressure));
		
		double slope = (Math.log(nextPressure)-Math.log(pressure) )
					   /(Math.log(nextTemperature)-Math.log(temperature));
		
		
		double volumeDifference = Math.abs(heterogeneousMixture.getVapor().calculateMolarVolume())
				-Math.abs(heterogeneousMixture.getLiquid().calculateMolarVolume());
		
		System.out.println("iterations: " +iterations + "("+ temperature + ","+pressure+")" + volumeDifference);
		
		
		
		double temperatureStep = 1;
		while(Math.abs(volumeDifference) > volumeDifferenceTolerance){
			pressure = nextPressure;
			temperature = nextTemperature;
			 iterations = 0;
			if(slope < 2){//bubble or dew point pressures
				nextTemperature = temperature + temperatureStep;
				heterogeneousMixture.setTemperature(nextTemperature);
				 iterations = heterogeneousMixture.dewPressure(pressure);
				nextPressure = heterogeneousMixture.getPressure();
				
				
			}else{//bubble or dew point temperature
				nextPressure = pressure + pressureStep;
				heterogeneousMixture.setPressure(nextPressure);
				iterations = heterogeneousMixture.dewTemperature(temperature);
				//iterations =heterogeneousMixture.dewTemperature();
				nextTemperature = heterogeneousMixture.getTemperature();				
			}
			System.out.println("iterations"+iterations);
			rows.add(new GoogleRow(nextTemperature,null,nextPressure));
			System.out.println("iterations dew: " +iterations + "("+ nextTemperature + ","+nextPressure+")" + volumeDifference );
			slope = (Math.log(nextPressure)-Math.log(pressure) )
					   /(Math.log(nextTemperature)-Math.log(temperature));
			volumeDifference = Math.abs(heterogeneousMixture.getLiquid().calculateMolarVolume())
					-Math.abs(heterogeneousMixture.getVapor().calculateMolarVolume());
		}
		
		
		
		
		
		
		return rows;	
		
	
	}
	
	
	
	
	
	
	double delta= 0.0001;
	public double bubblePressure_Slope(HeterogeneousMixture heterogeneousMixture,double temperature,Double pressureEstimate){
		double originalTemperature = heterogeneousMixture.getTemperature();
		double originalPressure = heterogeneousMixture.getPressure();
		
		heterogeneousMixture.setTemperature(temperature);
		if(pressureEstimate.equals(0)){
			heterogeneousMixture.bubblePressure();
		}else{
			heterogeneousMixture.bubblePressure(pressureEstimate);
		}
		double pressure = heterogeneousMixture.getPressure();
		
		double temperature_ = temperature +delta;
		
		heterogeneousMixture.setTemperature(temperature_);
		heterogeneousMixture.bubblePressure(pressure);
		
		double pressure_ = heterogeneousMixture.getPressure();
		
		double slope = (Math.log(pressure_) - Math.log(pressure))
		         /(Math.log(temperature_)-Math.log(temperature));
		
		heterogeneousMixture.setTemperature(originalTemperature);
		heterogeneousMixture.setPressure(originalPressure);
		
		return slope;
	}
	
	
	public double dewPressure_Slope(HeterogeneousMixture heterogeneousMixture,double temperature,Double pressureEstimate){
		double originalTemperature = heterogeneousMixture.getTemperature();
		double originalPressure = heterogeneousMixture.getPressure();
		
		
		heterogeneousMixture.setTemperature(temperature);
		if(pressureEstimate.equals(0)){
			heterogeneousMixture.dewPressure();
		}else{
			heterogeneousMixture.dewPressure(pressureEstimate);
		}
		double pressure = heterogeneousMixture.getPressure();
		
		double temperature_ = temperature +delta;
		
		heterogeneousMixture.setTemperature(temperature_);
		heterogeneousMixture.dewPressure(pressure);
		
		double pressure_ = heterogeneousMixture.getPressure();
		
		double slope = (Math.log(pressure_) - Math.log(pressure))
		         /(Math.log(temperature_)-Math.log(temperature));
		
		
		heterogeneousMixture.setTemperature(originalTemperature);
		heterogeneousMixture.setPressure(originalPressure);
		return slope;
	}
	
	
	public double getMaximumCriticalTemperature(Mixture mix){
		Compound anyCompound = mix.getComponents().iterator().next();
		
		double maxCriticalTemperature= anyCompound.getCriticalTemperature();
		
		for(Compound com: mix.getComponents()){
			
			double criticalTemperature= com.getCriticalTemperature();
			if(criticalTemperature > maxCriticalTemperature){
				maxCriticalTemperature = criticalTemperature;
			}
		}
		return maxCriticalTemperature;
	}
	
	
	
	public double getMinimumCriticalTemperature(Mixture mix){
		Compound anyCompound = mix.getComponents().iterator().next();		
		double minCriticalTemperautre =anyCompound.getCriticalTemperature();		
		for(Compound com: mix.getComponents()){
			
			double criticalTemperature= com.getCriticalTemperature();
			if(criticalTemperature < minCriticalTemperautre){
				minCriticalTemperautre = criticalTemperature;
			}
		}
		return minCriticalTemperautre;
	}
	
	public double getMaximumCriticalPressure(Mixture mix){
		Compound anyCompound = mix.getComponents().iterator().next();
		
		double maxCriticalPressure = anyCompound.getCriticalPressure();		
		for(Compound com: mix.getComponents()){
			double criticalPressure = com.getCriticalPressure();
			
			if(criticalPressure > maxCriticalPressure){
				maxCriticalPressure = criticalPressure;
			}
		}
		return maxCriticalPressure;
	}
	
	public double getMinimumCriticalPressure(Mixture mix){
		Compound anyCompound = mix.getComponents().iterator().next();
		double minCriticalPressure =anyCompound.getCriticalPressure();
		for(Compound com: mix.getComponents()){
			double criticalPressure = com.getCriticalPressure();
			if(criticalPressure < minCriticalPressure ){
				minCriticalPressure = criticalPressure;
			}
		}
		return minCriticalPressure;
	}
	
	public Collection<GoogleRow> getRows(Substance sub ,int index,int nc){
		Collection<GoogleRow> rows = new ArrayList<>();
		double ct = sub.getComponent().getCriticalTemperature();
		double minT = ct*0.5;
		
		HeterogeneousSubstance heterogeneousSubstance = getHeterogeneous(sub);
		
		Integer n = 40;
		double tempStep = (ct - minT)/n.doubleValue();
		for(Integer i = 0; i< n; i++){
			double temperature = minT + i.doubleValue()*tempStep;
			heterogeneousSubstance.setTemperature(temperature);
			heterogeneousSubstance.saturationPressure();
			double pressure = heterogeneousSubstance.getPressure();
			Double[] numbers = new Double[3+nc];
			numbers[0]= temperature;
			numbers[index] =pressure ;
			rows.add(new GoogleRow(numbers));
		}
		return rows;
	}

}
