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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import json.Position;
import models.Envelope;
import models.MixtureEnvelope;
import models.PointInfo;
import models.SubstanceEnvelope;

import org.junit.Test;

import plot.LinesPlotsBean;
import termo.component.Compound;
import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.matter.builder.HeterogeneousMixtureBuilder;

import com.google.gson.Gson;

@Named("phaseEnvelopeBean")
@SessionScoped
public class PhaseEnvelopeBean implements Serializable {

	private static final long serialVersionUID = 6940496006074883582L;
	public HeterogeneousMixture getHeterogeneous(Mixture mix){
		HeterogeneousMixture hetMixture =new HeterogeneousMixtureBuilder().fromMixture(mix);
		return hetMixture;
	}
	public HeterogeneousSubstance getHeterogeneous(Substance substance){
		return new HeterogeneousSubstance(substance.getCubicEquationOfState(),
				substance.getAlpha(),substance.getComponent());
	}
	public Heterogeneous getHeterogeneous(Homogeneous homogeneous){
		if(homogeneous instanceof Mixture)
			return getHeterogeneous((Mixture)homogeneous);
		else 
			return getHeterogeneous((Substance)homogeneous);
	}
	
	List<Envelope> envelopes = new ArrayList<>();
	public List<Envelope> getEnvelopes() {
		return envelopes;
	}
	public void setEnvelopes(List<Envelope> envelopes) {
		this.envelopes = envelopes;
	}

	Envelope selectedEnvelope ;
	
	public Envelope getSelectedEnvelope() {
		return selectedEnvelope;
	}
	public void setSelectedEnvelope(Envelope selectedEnvelope) {
		this.selectedEnvelope = selectedEnvelope;
	}
	@PostConstruct
	public void phaseEnvelopeBean() {
		
		for(Homogeneous homogeneous:homogeneousBean.getHomogeneousList()){
			Heterogeneous heterogeneous =getHeterogeneous(homogeneous);
			Envelope env;
			if(heterogeneous instanceof HeterogeneousSubstance){
				env = new SubstanceEnvelope(heterogeneous.toString(), 
						(HeterogeneousSubstance)heterogeneous);
			}else{
				env = new MixtureEnvelope(heterogeneous.toString(),
						(HeterogeneousMixture)heterogeneous);
			}
			dataTable(env);
			envelopes.add(env);
		}
		selectedEnvelope = envelopes.get(0);
	}
	
	@Inject HomogeneousBean homogeneousBean;
	@Inject LinesPlotsBean linesPlotsBean;
	
	public void draw(){
		
	}
	
	public String drawEnthalpy(){
		linesPlotsBean.setInfo("Temperatura-Entalpía-Presión");
		if(selectedEnvelope.getLiquidLine().size() ==0){
			linesPlotsBean.setInfo("Primero debe generar el envolvente de fases "
					+ "en la sección 'Envolvente de fases'");			
		}
		PointInfo[] points = new PointInfo[selectedEnvelope.getLiquidLine().size()];
		
		PointInfo[] vaporPoints = new PointInfo[selectedEnvelope.getVaporLine().size()];
		
		
		String liquidjson = new Gson().toJson( selectedEnvelope.getLiquidLine().toArray(points)).toString();
		String vaporjson = new Gson().toJson(selectedEnvelope.getVaporLine().toArray(vaporPoints));
		
		linesPlotsBean.setLiquidAreaTemperatureLines(liquidAreaTemperatureLinesJson());
		linesPlotsBean.setVaporAreaTemperatureLines(vaporAreaTemperatureLinesJson());
		
		linesPlotsBean.setVaporAreaPressureLines(vaporAreaPressureLinesJson());
		linesPlotsBean.setLiquidAreaPressureLines(liquidAreaPressureLinesJson());
		
		
		linesPlotsBean.setLiquidLineJson(liquidjson);
		linesPlotsBean.setVaporLineJson(vaporjson);
		
		return "webGLLinePlot";
	}
	
	public String liquidAreaTemperatureLinesJson(){
		PointInfo[][] liquidAreaTemperatureLines = 
				new PointInfo[selectedEnvelope.getLiquidAreaTemperatureLines().size()][];
		return new Gson().toJson(selectedEnvelope.getLiquidAreaTemperatureLines().toArray(liquidAreaTemperatureLines));
		
	}
	
	public String vaporAreaTemperatureLinesJson(){
		PointInfo[][] vaporAreaTemperatureLines = 
				new PointInfo[selectedEnvelope.getVaporAreaTemperatureLines().size()][];
		return new Gson().toJson(selectedEnvelope.getVaporAreaTemperatureLines().toArray(vaporAreaTemperatureLines));
		
	}
	public String vaporAreaPressureLinesJson(){
		PointInfo[][] vaporAreaPressureLines = 
				new PointInfo[selectedEnvelope.getVaporAreaPressureLines().size()][];
		return new Gson().toJson(selectedEnvelope.getVaporAreaPressureLines().toArray(vaporAreaPressureLines));
		
	}
	public String liquidAreaPressureLinesJson(){
		PointInfo[][] liquidAreaPressureLines = 
				new PointInfo[selectedEnvelope.getLiquidAreaPressureLines().size()][];
		return new Gson().toJson(selectedEnvelope.getLiquidAreaPressureLines().toArray(liquidAreaPressureLines));
		
	}
	
	
	@Test
	public void test(){
		String position = new Gson().toJson(new Position());
		String pointInfo = new Gson().toJson(new PointInfo());
		PointInfo[] list = new PointInfo[1];
		list[0]=new PointInfo();
		String listPointInfo = new Gson().toJson(list);
		
		System.out.println(position);
		System.out.println(pointInfo);
		System.out.println(listPointInfo);
		
	}
	
	public boolean isMixture(){
		return selectedEnvelope instanceof MixtureEnvelope;
	}
	
	public MixtureEnvelope asMixtureEnvelope(){
		if(isMixture()){
			return (MixtureEnvelope) selectedEnvelope;
		}
		return null;
	}
	
	public GoogleGraphInfo dataTable(Envelope envelope){
		
		GoogleGraphInfo graphInfo = new GoogleGraphInfo();
		GoogleDataTable data = new GoogleDataTable();
		
		data.addColumns(new GoogleColumn("temp", "Temperatura [K]", GoogleColumnType.number),
				new GoogleColumn("press", "Presión burbuja[Pa]", GoogleColumnType.number),
				new GoogleColumn("press", "Presión rocío[Pa]", GoogleColumnType.number));
								
		Collection<GoogleRow> rows = getRows(data,envelope);
		data.addRows(rows);
		
		graphInfo.setData(data);
		graphInfo.setOptions(GoogleOptions.googleOptions(
				"Envolvente de fases", 
				"Temperatura [K]", 
				"Presión [Pa]", ChartType.multipleFunction));
		return graphInfo;
	}


	
	public Collection<GoogleRow> getRows(GoogleDataTable data,Envelope envelope){
		Collection<GoogleRow> rows = new ArrayList<>();			
		if(envelope instanceof SubstanceEnvelope){			
			rows = getRows((SubstanceEnvelope)envelope,1,1); 			
		}else if(envelope instanceof MixtureEnvelope){
			rows = getRows((MixtureEnvelope)envelope);
			//rows.addAll(getRowsDew((Mixture)homogeneous));
			
			int nc = ((MixtureEnvelope) envelope)
					.getHeterogeneousMixture().getComponents().size();
			
			Iterator<Substance> iterator  = ((MixtureEnvelope) envelope)
					.getHeterogeneousMixture().getLiquid()
					.getPureSubstances().iterator(); 
			for(int i = 3; i <= nc + 2; i++){
				Substance sub = iterator.next();
				HeterogeneousSubstance heterogeneous= getHeterogeneous(sub);
				SubstanceEnvelope subEnv = new SubstanceEnvelope(heterogeneous.toString()
						, heterogeneous);
				data.addColumn(new GoogleColumn("comp" +sub.getComponent().getName(),
					sub.getComponent().getName(), GoogleColumnType.number));
				rows.addAll(getRows(subEnv,i ,nc));
			}
			
		}
		return rows;
	}
	
	public Collection<GoogleRow> getRows(MixtureEnvelope mix){
		Collection<GoogleRow> rows = new ArrayList<>();
		
		mix.calculateEnvelope();
		for(PointInfo pi : mix.getLiquidLine()){
			rows.add(new GoogleRow(pi.getTemperature(),pi.getPressure()));
		}
		for(PointInfo pi : mix.getVaporLine()){
			rows.add(new GoogleRow(pi.getTemperature(),null,pi.getPressure()));
		}			
		return rows;			
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
		//double minCriticalTemperature =getMinimumCriticalTemperature(mix);
		
					
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
	
	public Collection<GoogleRow> getRows(SubstanceEnvelope sub ,int index,int nc){
		Collection<GoogleRow> rows = new ArrayList<>();
		sub.calculateEnvelope();
		for (PointInfo pi:sub.getLiquidLine()){
			Double[] numbers = new Double[3+nc];
			numbers[0] = pi.getTemperature();
			numbers[index] = pi.getPressure();
			rows.add(new GoogleRow(numbers));
		}
		return rows;
	}

}



