package chimicae;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import json.Position;
import plot.PlotBean;
import termo.component.Compound;
import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.matter.builder.HeterogeneousMixtureBuilder;

import com.google.gson.Gson;

@Named("saturationTemperatureBean")
@SessionScoped
public class SaturationTemperatureBean implements Serializable {

	@Inject PlotBean plotBean;
	@Inject HomogeneousBean homogeneousBean;
	
	public Heterogeneous getHeterogeneous(Mixture mix){
		HeterogeneousMixture hetMixture =new HeterogeneousMixtureBuilder().fromMixture(mix);
		return hetMixture;
	}
	
	public Heterogeneous getHeterogeneous(Substance substance){
		return new HeterogeneousSubstance(substance.getCubicEquationOfState(),
				substance.getAlpha(),substance.getComponent());
	}

	
	

	public double[] maxminPressureForSaturationTemp(Homogeneous homogeneous){
		double minCPressure =0;
		double maxCPressure =0;
		
		Heterogeneous het= null;
		if(homogeneous instanceof Mixture){
			Mixture mix = ((Mixture) homogeneous);
			double minCriticalPressure =mix.getComponents().iterator().next().getCriticalPressure();
			double maxCriticalPressure =mix.getComponents().iterator().next().getCriticalPressure();
			
			for(Compound co: mix.getComponents()){
				double pc =co.getCriticalPressure(); 
				if(pc <minCriticalPressure){
					minCriticalPressure= pc;
				}if(pc > maxCriticalPressure){
					maxCriticalPressure = pc;
				}
			}
			maxCPressure = maxCriticalPressure;
			minCPressure =minCriticalPressure;
			het=getHeterogeneous(mix);
		}else if(homogeneous instanceof Substance){
			minCPressure = ((Substance) homogeneous).getComponent().getCriticalPressure();
			maxCPressure = ((Substance) homogeneous).getComponent().getCriticalPressure();
			het=getHeterogeneous(((Substance) homogeneous));
		}
		
		minCPressure = minCPressure*0.4;
		
		double ridiculousPressure = maxCPressure *2;
		double pressureStep= (ridiculousPressure-minCPressure)/100d;
		
		double pressure = minCPressure;
		het.setPressure(pressure);
		
		satTemperature(het);
		
		double lEnthalpy = het.getLiquid().calculateMolarVolume();
		double vEnthalpy = het.getVapor().calculateMolarVolume();
		
		double enthalpyDiff = Math.abs(vEnthalpy)-Math.abs(lEnthalpy);
		
		while(Math.abs(enthalpyDiff) > 0.01 && pressure < ridiculousPressure){
			het.setPressure(pressure);
			satTemperature(het);
			lEnthalpy = het.getLiquid().calculateMolarVolume();
			vEnthalpy = het.getVapor().calculateMolarVolume();
			 enthalpyDiff = Math.abs(vEnthalpy)-Math.abs(lEnthalpy);
			 pressure+= pressureStep;
		}
		
		double[] result={pressure,minCPressure};
		return result;
	}
	
	
	public void satTemperature(Heterogeneous heterogeneous){
		if(heterogeneous instanceof HeterogeneousMixture){			
				((HeterogeneousMixture) heterogeneous).dewTemperature();			
		}else if (heterogeneous instanceof HeterogeneousSubstance){
			double temperatureEstimate = heterogeneous.getTemperature();
			if(temperatureEstimate ==0){
				((HeterogeneousSubstance) heterogeneous).saturationTemperature();
			}else{
				((HeterogeneousSubstance) heterogeneous).saturationTemperature(temperatureEstimate);
			}
		
		}
		
	}
	
	
	
	public String plotSaturationTemp(){
		Homogeneous homogeneous = homogeneousBean.getSelectedHomogeneous();
		double[] maxMin = maxminPressureForSaturationTemp(homogeneous); 
		Heterogeneous het = null;
		if(homogeneous instanceof Substance){
			het = getHeterogeneous((Substance)homogeneous);
			plotLine((HeterogeneousSubstance)het,maxMin[0],maxMin[1]);
		}
		
		return "webGLPlotTemplate";
	}
	
	public void plotLine(HeterogeneousSubstance substance,double maxP,double minP){
		

//		double minP = substance.getComponent().getCriticalPressure()*0.6;
//		double maxP = substance.getComponent().getCriticalPressure()*0.9;
		
		System.out.println("Pc: " + substance.getComponent().getCriticalPressure());
		System.out.println("minP: " + minP);
		System.out.println("maxP: " + maxP);
		
		Integer n = 50;
		double pressStep = (maxP-minP)/n.doubleValue();
		
		Position[] pl = new Position[n];
		Position[] pv = new Position[n];
		for(Integer i = 0; i < n;i++){
			double press = minP + i.doubleValue()* pressStep;
			substance.setPressure(press);
			satTemperature(substance);
			double temperature = substance.getTemperature();
			double vl = substance.getLiquid().calculateMolarVolume();
			double vv = substance.getVapor().calculateMolarVolume();

			System.out.println("vl: " + vl);
			System.out.println("vv: " + vv);
			System.out.println("temperature: " + temperature);
			pl[i] = new Position(press,vl,temperature);
			pv[i] = new Position(press,vv,temperature);
			
		}
		
		plotBean.setFirstPlot(false);
		plotBean.setSecondPlot(false);
		plotBean.setThirdPlot(false);
		
		plotBean.setLine(true);
		plotBean.setLineJson(new Gson().toJson(pl));
		
		plotBean.setSecondLine(true);
		plotBean.setSecondLineJson(new Gson().toJson(pv));
		
		
		
		plotBean.setxAxisLabel("Presión [Pa]");
		plotBean.setyAxisLabel("Volumen molar [m³/kmol]");
		plotBean.setzAxisLabel("Temperatura [K]");
		
		
	}
	
}
