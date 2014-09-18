package models;

import termo.component.Compound;
import termo.matter.HeterogeneousMixture;

public 
class MixtureEnvelope extends Envelope{
	HeterogeneousMixture heterogeneousMixture;
	Double volumeDifferenceTolerance = 0.07;
	
	public Double getVolumeDifferenceTolerance() {
		return volumeDifferenceTolerance;
	}


	public void setVolumeDifferenceTolerance(Double volumeDifferenceTolerance) {
		this.volumeDifferenceTolerance = volumeDifferenceTolerance;
	}


	public MixtureEnvelope(String name,HeterogeneousMixture heterogenous) {
		super(name);
		this.heterogeneousMixture = heterogenous;
	}	
	
	
	public double getMinimumCriticalPressure(){
		Compound anyCompound = heterogeneousMixture.getComponents().iterator().next();
		double minCriticalPressure =anyCompound.getCriticalPressure();
		for(Compound com: heterogeneousMixture.getComponents()){
			double criticalPressure = com.getCriticalPressure();
			if(criticalPressure < minCriticalPressure ){
				minCriticalPressure = criticalPressure;
			}
		}
		return minCriticalPressure;
	}

	public HeterogeneousMixture getHeterogeneousMixture() {
		return heterogeneousMixture;
	}

	public void setHeterogeneousMixture(HeterogeneousMixture heterogeneousMixture) {
		this.heterogeneousMixture = heterogeneousMixture;
	}

	@Override
	public void calculateEnvelope() {
		liquidLine.clear();
		vaporLine.clear();
		liquidLine();
		vaporLine();


	}
	
	public void liquidLine(){
		
		double minCriticalPressure =getMinimumCriticalPressure();		
		double pressure = minCriticalPressure*0.3;		
		
		heterogeneousMixture.setPressure(pressure);
		int bubbleIterations =heterogeneousMixture.bubbleTemperature();
		double bubbleTemperature = heterogeneousMixture.getTemperature();
		
		liquidLine.add(fillPointInfo(heterogeneousMixture.getLiquid()));				
		
		double pressureStep = (minCriticalPressure- pressure)/50d;
		
		double nextPressure = pressure + pressureStep;
		heterogeneousMixture.setPressure(nextPressure);
		bubbleIterations =heterogeneousMixture.bubbleTemperature(bubbleTemperature);
		double nextTemperature = heterogeneousMixture.getTemperature();
		liquidLine.add(fillPointInfo(heterogeneousMixture.getLiquid()));
		
		
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
			if(slope <2){  //bubble or dew point pressures
				nextTemperature = bubbleTemperature + temperatureStep;
				heterogeneousMixture.setTemperature(nextTemperature);
				bubbleIterations = heterogeneousMixture.bubblePressure(pressure);
				nextPressure = heterogeneousMixture.getPressure();
				
				System.out.println("bubblePressure, slope: "+slope);
			}else{//bubble or dew point temperature
				nextPressure = pressure + pressureStep;
				heterogeneousMixture.setPressure(nextPressure);
				bubbleIterations = heterogeneousMixture.bubbleTemperature(bubbleTemperature);
				nextTemperature = heterogeneousMixture.getTemperature();
				System.out.println("bubbleTemperature, slope: "+slope);
			}
			
			liquidLine.add(fillPointInfo(heterogeneousMixture.getLiquid()));
//			System.out.println("iterations: " +bubbleIterations + "("+ nextTemperature + ","+nextPressure+")" + volumeDifference);
			slope = (Math.log(nextPressure)-Math.log(pressure) )
					   /(Math.log(nextTemperature)-Math.log(bubbleTemperature));
			volumeDifference = Math.abs(heterogeneousMixture.getLiquid().calculateMolarVolume())
					-Math.abs(heterogeneousMixture.getVapor().calculateMolarVolume());
		}
		
		double minTemperature = liquidLine.get(0).getTemperature();
		double maxTemperature = liquidLine.get(0).getTemperature();
		double minPressure = liquidLine.get(0).getPressure();
		double maxPressure = liquidLine.get(0).getPressure();
		
		for(PointInfo pi: liquidLine){
			double t =pi.getTemperature();
			double p = pi.getPressure();
			if(t<minTemperature){
				minTemperature=t;
			}if(t>maxTemperature){
				maxTemperature=t;
			}if(p<minPressure){
				minPressure=p;
			}if(p>maxPressure){
				maxPressure=p;
			}
		}
		Integer n = 40;
		for(PointInfo pi: liquidLine){
			double t = pi.getTemperature();
			double p = pi.getPressure();
			heterogeneousMixture.setPressure(p);
			double tempstep = (maxTemperature*1.3 -t)/n.doubleValue();
			PointInfo[] vaporLine =new PointInfo[n];
			
//			for(Integer i =0;i<n;i++){
//				double temp = t + i.doubleValue()*tempstep;
//				heterogeneousSubstance.setTemperature(temp);
//				PointInfo vapP = fillPointInfo(heterogeneousSubstance.getVapor());
//				vaporLine[i]=vapP;
//			}
//			vaporAreaTemperatureLines.add(vaporLine);
			tempstep = (t-minTemperature)/n.doubleValue();
			PointInfo[] liquidLine = new PointInfo[n];
			for(Integer i=0;i<n;i++){
				double temp = t -i.doubleValue()*tempstep;
				heterogeneousMixture.setTemperature(temp);
				PointInfo liqP = fillPointInfo(heterogeneousMixture.getLiquid());
				liquidLine[i] = liqP;
						
			}
			liquidAreaTemperatureLines.add(liquidLine);
		}
		
		
		
	}
	
	public void vaporLine(){
		double minCriticalPressure =getMinimumCriticalPressure();		
		double pressure = minCriticalPressure*0.3;		
		
		heterogeneousMixture.setPressure(pressure);
		int dewIterations =heterogeneousMixture.dewTemperature();
		double dewTemperature = heterogeneousMixture.getTemperature();
		
		vaporLine.add(fillPointInfo(heterogeneousMixture.getVapor()));				
		
		double pressureStep = (minCriticalPressure- pressure)/50d;
		
		double nextPressure = pressure + pressureStep;
		heterogeneousMixture.setPressure(nextPressure);
		dewIterations =heterogeneousMixture.dewTemperature(dewTemperature);
		double nextTemperature = heterogeneousMixture.getTemperature();
		vaporLine.add(fillPointInfo(heterogeneousMixture.getVapor()));
		
		
		double slope = (Math.log(nextPressure)-Math.log(pressure) )
					   /(Math.log(nextTemperature)-Math.log(dewTemperature));
		
		
		double volumeDifference = Math.abs(heterogeneousMixture.getVapor().calculateMolarVolume())
				-Math.abs(heterogeneousMixture.getLiquid().calculateMolarVolume());
		
		System.out.println("iterations: " +dewIterations + "("+ dewTemperature + ","+pressure+")" + volumeDifference);
		
		
		double temperatureStep = 10;
		while(Math.abs(volumeDifference) > volumeDifferenceTolerance  ){
			pressure = nextPressure;
			dewTemperature = nextTemperature;
			dewIterations = 0;
			if(slope < 2){//bubble or dew point pressures
				nextTemperature = dewTemperature + temperatureStep;
				heterogeneousMixture.setTemperature(nextTemperature);
				dewIterations = heterogeneousMixture.dewPressure(pressure);
				nextPressure = heterogeneousMixture.getPressure();
				
				
			}else{//bubble or dew point temperature
				nextPressure = pressure + pressureStep;
				heterogeneousMixture.setPressure(nextPressure);
				dewIterations = heterogeneousMixture.dewTemperature(dewTemperature);
				nextTemperature = heterogeneousMixture.getTemperature();				
			}
			System.out.println("iterations"+dewIterations);
			vaporLine.add(fillPointInfo(heterogeneousMixture.getVapor()));
			System.out.println("iterations: " +dewIterations + "("+ nextTemperature + ","+nextPressure+")" + volumeDifference);
			slope = (Math.log(nextPressure)-Math.log(pressure) )
					   /(Math.log(nextTemperature)-Math.log(dewTemperature));
			volumeDifference = Math.abs(heterogeneousMixture.getLiquid().calculateMolarVolume())
					-Math.abs(heterogeneousMixture.getVapor().calculateMolarVolume());
		}
		
		
		double minTemperature = vaporLine.get(0).getTemperature();
		double maxTemperature = vaporLine.get(0).getTemperature();
		double minPressure = vaporLine.get(0).getPressure();
		double maxPressure = vaporLine.get(0).getPressure();
		
		for(PointInfo pi: vaporLine){
			double t =pi.getTemperature();
			double p = pi.getPressure();
			if(t<minTemperature){
				minTemperature=t;
			}if(t>maxTemperature){
				maxTemperature=t;
			}if(p<minPressure){
				minPressure=p;
			}if(p>maxPressure){
				maxPressure=p;
			}
		}
		Integer n = 40;
		for(PointInfo pi: vaporLine){
			double t = pi.getTemperature();
			double p = pi.getPressure();
			heterogeneousMixture.setPressure(p);
			double tempstep = (maxTemperature*1.3 -t)/n.doubleValue();
			PointInfo[] vaporLine =new PointInfo[n];
			
			for(Integer i =0;i<n;i++){
				double temp = t + i.doubleValue()*tempstep;
				heterogeneousMixture.setTemperature(temp);
				PointInfo vapP = fillPointInfo(heterogeneousMixture.getVapor());
				vaporLine[i]=vapP;
			}
			vaporAreaTemperatureLines.add(vaporLine);
//			tempstep = (t-minTemperature)/n.doubleValue();
//			PointInfo[] liquidLine = new PointInfo[n];
//			for(Integer i=0;i<n;i++){
//				double temp = t -i.doubleValue()*tempstep;
//				heterogeneousMixture.setTemperature(temp);
//				PointInfo liqP = fillPointInfo(heterogeneousMixture.getLiquid());
//				liquidLine[i] = liqP;
//						
//			}
//			liquidAreaTemperatureLines.add(liquidLine);
		}
		
		
	}
	
	
	
}