package models;

import termo.matter.HeterogeneousSubstance;

public class SubstanceEnvelope extends Envelope{
	public SubstanceEnvelope(String name,HeterogeneousSubstance heterogeneous) {
		super(name);		
		this.heterogeneousSubstance = heterogeneous;
	}

	HeterogeneousSubstance heterogeneousSubstance;
	

	public HeterogeneousSubstance getHeterogeneousSubstance() {
		return heterogeneousSubstance;
	}

	public void setHeterogeneousSubstance(
			HeterogeneousSubstance heterogeneousSubstance) {
		this.heterogeneousSubstance = heterogeneousSubstance;
	}

	@Override
	public void calculateEnvelope() {
		liquidLine.clear();
		vaporLine.clear();
		double ct = heterogeneousSubstance.getComponent()
				.getCriticalTemperature();
		double minT = ct*0.5;
		
		heterogeneousSubstance.setTemperature(minT);
		heterogeneousSubstance.saturationPressure();
		double pressure = heterogeneousSubstance.getPressure();
		
		double vv =  heterogeneousSubstance.getVapor().calculateMolarVolume();
		double lv =  heterogeneousSubstance.getLiquid().calculateMolarVolume();
		double volumeDiff = Math.abs(vv)-Math.abs(lv);

		Integer n = 40;
		
		double tempStep = (ct - minT)/n.doubleValue();
		double temperature = minT;
		while(Math.abs(volumeDiff) > 0.01){
			temperature +=tempStep;
			heterogeneousSubstance.setTemperature(temperature);
			heterogeneousSubstance.dewPressure(pressure);
			
			pressure = heterogeneousSubstance.getPressure();
						
			PointInfo liquidPoint = fillPointInfo(heterogeneousSubstance.getLiquid());
			PointInfo vaporPoint = fillPointInfo(heterogeneousSubstance.getVapor());
			liquidLine.add(liquidPoint);
			vaporLine.add(vaporPoint);
			volumeDiff = Math.abs(vaporPoint.molarVolume)- Math.abs(liquidPoint.molarVolume);
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
		
		for(PointInfo pi: liquidLine){
			double t = pi.getTemperature();
			double p = pi.getPressure();
			heterogeneousSubstance.setPressure(p);
			double tempstep = (maxTemperature*1.3 -t)/n.doubleValue();
			PointInfo[] vaporLine =new PointInfo[n];
			
			for(Integer i =0;i<n;i++){
				double temp = t + i.doubleValue()*tempstep;
				heterogeneousSubstance.setTemperature(temp);
				PointInfo vapP = fillPointInfo(heterogeneousSubstance.getVapor());
				vaporLine[i]=vapP;
			}
			vaporAreaTemperatureLines.add(vaporLine);
			tempstep = (t-minTemperature)/n.doubleValue();
			PointInfo[] liquidLine = new PointInfo[n];
			for(Integer i=0;i<n;i++){
				double temp = t -i.doubleValue()*tempstep;
				heterogeneousSubstance.setTemperature(temp);
				PointInfo liqP = fillPointInfo(heterogeneousSubstance.getLiquid());
				liquidLine[i] = liqP;
						
			}
			liquidAreaTemperatureLines.add(liquidLine);
		}
		
	
		
	}
	

}
