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
				.getCriticalTemperature()*0.98;
		double minT = ct*0.5;
		
		heterogeneousSubstance.setTemperature(minT);
		heterogeneousSubstance.saturationPressure();
		double pressure = heterogeneousSubstance.getPressure();

		Integer n = 40;
		double tempStep = (ct - minT)/n.doubleValue();
		for(Integer i = 0; i< n; i++){
			double temperature = minT + i.doubleValue()*tempStep;
			heterogeneousSubstance.setTemperature(temperature);
			heterogeneousSubstance.saturationPressure(pressure);
			pressure = heterogeneousSubstance.getPressure();
						
			PointInfo liquidPoint = fillPointInfo(heterogeneousSubstance.getLiquid());
			PointInfo vaporPoint = fillPointInfo(heterogeneousSubstance.getVapor());
			liquidLine.add(liquidPoint);
			vaporLine.add(vaporPoint);
			
		}		
	}
	

}
