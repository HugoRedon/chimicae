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

@Named("enthalpyBean")
@SessionScoped
public class EnthalpyBean implements Serializable {
	private static final long serialVersionUID = -8370453661486436697L;

	@Inject HomogeneousBean homogeneousBean;
	@Inject PlotBean plotBean; 
	

	public Heterogeneous getHeterogeneous(Mixture mix){
		HeterogeneousMixture hetMixture =new HeterogeneousMixtureBuilder().fromMixture(mix);
		return hetMixture;
	}
	
	public Heterogeneous getHeterogeneous(Substance substance){
		return new HeterogeneousSubstance(substance.getCubicEquationOfState(),
				substance.getAlpha(),substance.getComponent());
	}
	
	public double[] maxTemp(Homogeneous homogeneous){
		double minCTemperature =0;
		double maxCT =0;
		
		Heterogeneous het= null;
		if(homogeneous instanceof Mixture){
			Mixture mix = ((Mixture) homogeneous);
			double minCriticalTemp =mix.getComponents().iterator().next().getCriticalTemperature();
			double maxCriticalTemp =mix.getComponents().iterator().next().getCriticalTemperature();
			for(Compound co: mix.getComponents()){
				double ct =co.getCriticalTemperature(); 
				if(ct <minCriticalTemp){
					minCriticalTemp= ct;
				}if(ct > maxCriticalTemp){
					maxCriticalTemp = ct;
				}
			}
			maxCT = maxCriticalTemp;
			minCTemperature =minCriticalTemp;
			het=getHeterogeneous(mix);
		}else if(homogeneous instanceof Substance){
			minCTemperature = ((Substance) homogeneous).getComponent().getCriticalTemperature();
			maxCT = ((Substance) homogeneous).getComponent().getCriticalTemperature();
			het=getHeterogeneous(((Substance) homogeneous));
		}
		
		minCTemperature = minCTemperature*0.4;
		
		
		double tempStep= 10;
		
		double temperature = minCTemperature;
		het.setTemperature(temperature);
		
		satPressure(het);
		
		double lEnthalpy = het.getLiquid().calculateEnthalpy();
		double vEnthalpy = het.getVapor().calculateEnthalpy();
		double enthalpyDiff = Math.abs(vEnthalpy)-Math.abs(lEnthalpy);
		while(Math.abs(enthalpyDiff) > 10 && temperature < maxCT*2 ){
			het.setTemperature(temperature);
			satPressure(het);
			lEnthalpy = het.getLiquid().calculateEnthalpy();
			vEnthalpy = het.getVapor().calculateEnthalpy();
			 enthalpyDiff = Math.abs(vEnthalpy)-Math.abs(lEnthalpy);
			 temperature += tempStep;
		}
		
		double[] result={temperature,minCTemperature};
		return result;
	}
	
	
	public double[] maxTempForEntropy(Homogeneous homogeneous){
		double minCTemperature =0;
		double maxCT =0;
		
		Heterogeneous het= null;
		if(homogeneous instanceof Mixture){
			Mixture mix = ((Mixture) homogeneous);
			double minCriticalTemp =mix.getComponents().iterator().next().getCriticalTemperature();
			double maxCriticalTemp =mix.getComponents().iterator().next().getCriticalTemperature();
			for(Compound co: mix.getComponents()){
				double ct =co.getCriticalTemperature(); 
				if(ct <minCriticalTemp){
					minCriticalTemp= ct;
				}if(ct > maxCriticalTemp){
					maxCriticalTemp = ct;
				}
			}
			maxCT = maxCriticalTemp;
			minCTemperature =minCriticalTemp;
			het=getHeterogeneous(mix);
		}else if(homogeneous instanceof Substance){
			minCTemperature = ((Substance) homogeneous).getComponent().getCriticalTemperature();
			maxCT = ((Substance) homogeneous).getComponent().getCriticalTemperature();
			het=getHeterogeneous(((Substance) homogeneous));
		}
		
		minCTemperature = minCTemperature*0.4;
		
		
		double tempStep= 10;
		
		double temperature = minCTemperature;
		het.setTemperature(temperature);
		
		satPressure(het);
		
		double lEnthalpy = het.getLiquid().calculateEntropy();
		double vEnthalpy = het.getVapor().calculateEntropy();
		double enthalpyDiff = Math.abs(vEnthalpy)-Math.abs(lEnthalpy);
		while(Math.abs(enthalpyDiff) > 10 && temperature < maxCT*2 ){
			het.setTemperature(temperature);
			satPressure(het);
			lEnthalpy = het.getLiquid().calculateEntropy();
			vEnthalpy = het.getVapor().calculateEntropy();
			 enthalpyDiff = Math.abs(vEnthalpy)-Math.abs(lEnthalpy);
			 temperature += tempStep;
		}
		
		double[] result={temperature,minCTemperature};
		return result;
	}
	
	
	public double[] maxTempForGibbs(Homogeneous homogeneous){
		double minCTemperature =0;
		double maxCT =0;
		
		Heterogeneous het= null;
		if(homogeneous instanceof Mixture){
			Mixture mix = ((Mixture) homogeneous);
			double minCriticalTemp =mix.getComponents().iterator().next().getCriticalTemperature();
			double maxCriticalTemp =mix.getComponents().iterator().next().getCriticalTemperature();
			for(Compound co: mix.getComponents()){
				double ct =co.getCriticalTemperature(); 
				if(ct <minCriticalTemp){
					minCriticalTemp= ct;
				}if(ct > maxCriticalTemp){
					maxCriticalTemp = ct;
				}
			}
			maxCT = maxCriticalTemp;
			minCTemperature =minCriticalTemp;
			het=getHeterogeneous(mix);
		}else if(homogeneous instanceof Substance){
			minCTemperature = ((Substance) homogeneous).getComponent().getCriticalTemperature();
			maxCT = ((Substance) homogeneous).getComponent().getCriticalTemperature();
			het=getHeterogeneous(((Substance) homogeneous));
		}
		
		minCTemperature = minCTemperature*0.4;
		
		
		double tempStep= 1;
		
		double temperature = minCTemperature;
		het.setTemperature(temperature);
		
		satPressure(het);
		
		double lEnthalpy = het.getLiquid().calculateMolarVolume();
		double vEnthalpy = het.getVapor().calculateMolarVolume();
		double enthalpyDiff = Math.abs(vEnthalpy)-Math.abs(lEnthalpy);
		while(Math.abs(enthalpyDiff) > .001 && temperature < maxCT*2 ){
			het.setTemperature(temperature);
			satPressure(het);
			lEnthalpy = het.getLiquid().calculateMolarVolume();
			vEnthalpy = het.getVapor().calculateMolarVolume();
			 enthalpyDiff = Math.abs(vEnthalpy)-Math.abs(lEnthalpy);
			 temperature += tempStep;
		}
		
		double[] result={temperature,minCTemperature};
		return result;
	}
	
	public void satPressure(Heterogeneous heterogeneous){
		if(heterogeneous instanceof HeterogeneousMixture){
			double pressure =heterogeneous.getPressure();
			if(pressure == 0){
				((HeterogeneousMixture) heterogeneous).dewPressure();
			}else{
				int iterations =((HeterogeneousMixture) heterogeneous).dewPressure(pressure);	
			}
			
		}else if (heterogeneous instanceof HeterogeneousSubstance){
			int iterations =((HeterogeneousSubstance) heterogeneous).dewPressure();
		}
		
	}
	
	public Heterogeneous prepareHeterogeneous(Homogeneous homogeneous){
		Heterogeneous ht= null;
		if(homogeneous instanceof Mixture){
			ht= getHeterogeneous((Mixture)homogeneous);
		}else if (homogeneous instanceof Substance){
			ht=getHeterogeneous((Substance) homogeneous);
		}
		return ht;
	}
	
	
	public String enthalpyPlot(){
//		if(homogeneousBean.getSelectedHomogeneous() instanceof Mixture){
//			return "notimplemented";
//		}
		Heterogeneous substance= prepareHeterogeneous(homogeneousBean.getSelectedHomogeneous());
				
		double[] maxMin = maxTemp(homogeneousBean.getSelectedHomogeneous());
		double min_temp = maxMin[1];
		double max_temp = maxMin[0];
		
		double min_pressure = 0;
		double max_pressure =0;
		
		Integer n = 40;
		double tempPass = (max_temp - min_temp)/n.doubleValue();
		
		Position[] po = new Position[n*n];
		Integer count =0;
		for(Integer i = 0; i < n; i++){
			double temp = min_temp + i.doubleValue()* tempPass;
			substance.setTemperature(temp);
			satPressure(substance);
			//substance.dewPressure();
			
			Double pressure = substance.getPressure();
			min_pressure = (min_pressure == 0)?pressure: min_pressure;
			min_pressure = (pressure < min_pressure)? pressure: min_pressure;
			max_pressure = (pressure > max_pressure)? pressure: max_pressure;
			
			Double liquidEnthalpy = substance.getLiquid().calculateEnthalpy();
			Double vaporEnthalpy = substance.getVapor().calculateEnthalpy();
//			double liquidVolume = substance.getLiquid().calculateMolarVolume();
//			double vaporVolume = substance.getVapor().calculateMolarVolume();
			//list.add(new Position(pressure,liquidEnthalpy,  temp));

						
			double enthalpyStep  = (vaporEnthalpy- liquidEnthalpy)/n.doubleValue(); 
			for(Integer j = 0; j< n; j++){
				double enthalpy = liquidEnthalpy + j.doubleValue() * enthalpyStep;
				Position position = new Position(pressure,enthalpy,  temp);
				po[count++]=position;
			}
			//list.add(new Position(pressure,vaporEnthalpy,  temp));
		}
		
		
		
		double min_pressureSaved = min_pressure;
		max_pressure = max_pressure *1.3;
		min_pressure = 0.9* min_pressure;
		
		Position[] positionsl = new Position[n*n];
		Integer countl =0;
		for(Integer i = 0; i < n; i++){
			double temp = min_temp + i.doubleValue()* tempPass;
			substance.setTemperature(temp);
			satPressure(substance);
			//substance.dewPressure();
			double pressure = substance.getPressure();						
			double pressPass = (max_pressure- pressure) / n.doubleValue();
			for(Integer j = 0 ; j < n;j++){
				double press= pressure + j.doubleValue() * pressPass;
				substance.setPressure(press);
				double liquidEnthalpy = substance.getLiquid().calculateEnthalpy();
				//double liquidVolume = substance.getLiquid().calculateMolarVolume();
				positionsl[countl++] = new Position(press,liquidEnthalpy,temp);
			}
		}
		
		
		//double aboveCritical = max_temp * 1.05; 
		
		Position[] positionsV= new Position[n*n]; 
		Integer countv =0;
		for(Integer i = 0; i < n; i++){
			double temp = min_temp + i.doubleValue()* tempPass;
			substance.setTemperature(temp);
			satPressure(substance);
			//substance.dewPressure();
			double pressure = substance.getPressure();
			
			
			double pressPass= (pressure- min_pressure)/Double.valueOf(n);	
			for(Integer j=0 ; j < n; j++){
					double press = pressure - j.doubleValue() * pressPass;
					substance.setPressure(press);
					double vaporEnthalpy = substance.getVapor().calculateEnthalpy();	
					//double vaporVolume = substance.getVapor().calculateMolarVolume();
					positionsV[countv++] = new Position(press,vaporEnthalpy,temp);
			}
			
		}		
		
		plotBean.setThirdPlot(true);
		plotBean.setThirdJson(new Gson().toJson(positionsV));		
		plotBean.setSecondPlot(true);
		plotBean.setSecondJson(new Gson().toJson(po));
		
		plotBean.setJsonData(new Gson().toJson( positionsl));
		
		
		plotBean.setxAxisLabel("Presión [Pa]");
		plotBean.setyAxisLabel("Entalpía []");
		plotBean.setzAxisLabel("Temperatura [K]");
		
		return "webGLPlotTemplate";
	}
	
	
	public String entropyPlot(){

		Heterogeneous substance= prepareHeterogeneous(homogeneousBean.getSelectedHomogeneous());
				
		double[] maxMin = maxTempForEntropy(homogeneousBean.getSelectedHomogeneous());
		double min_temp = maxMin[1];
		double max_temp = maxMin[0];
		
		double min_pressure = 0;
		double max_pressure =0;
		
		Integer n = 40;
		double tempPass = (max_temp - min_temp)/n.doubleValue();
		
		Position[] po = new Position[n*n];
		Integer count =0;
		for(Integer i = 0; i < n; i++){
			double temp = min_temp + i.doubleValue()* tempPass;
			substance.setTemperature(temp);
			satPressure(substance);
			
			Double pressure = substance.getPressure();
			min_pressure = (min_pressure == 0)?pressure: min_pressure;
			min_pressure = (pressure < min_pressure)? pressure: min_pressure;
			max_pressure = (pressure > max_pressure)? pressure: max_pressure;
			
			Double liquidEnthalpy = substance.getLiquid().calculateEntropy();
			Double vaporEnthalpy = substance.getVapor().calculateEntropy();
					
			double enthalpyStep  = (vaporEnthalpy- liquidEnthalpy)/n.doubleValue(); 
			for(Integer j = 0; j< n; j++){
				double enthalpy = liquidEnthalpy + j.doubleValue() * enthalpyStep;
				Position position = new Position(pressure,enthalpy,  temp);
				po[count++]=position;
			}
			//list.add(new Position(pressure,vaporEnthalpy,  temp));
		}
		
		
		
		double min_pressureSaved = min_pressure;
		max_pressure = max_pressure *1.3;
		min_pressure = 0.9* min_pressure;
		
		Position[] positionsl = new Position[n*n];
		Integer countl =0;
		for(Integer i = 0; i < n; i++){
			double temp = min_temp + i.doubleValue()* tempPass;
			substance.setTemperature(temp);
			satPressure(substance);
			//substance.dewPressure();
			double pressure = substance.getPressure();						
			double pressPass = (max_pressure- pressure) / n.doubleValue();
			for(Integer j = 0 ; j < n;j++){
				double press= pressure + j.doubleValue() * pressPass;
				substance.setPressure(press);
				double liquidEnthalpy = substance.getLiquid().calculateEntropy();
				//double liquidVolume = substance.getLiquid().calculateMolarVolume();
				positionsl[countl++] = new Position(press,liquidEnthalpy,temp);
			}
		}
		
		
		//double aboveCritical = max_temp * 1.05; 
		
		Position[] positionsV= new Position[n*n]; 
		Integer countv =0;
		for(Integer i = 0; i < n; i++){
			double temp = min_temp + i.doubleValue()* tempPass;
			substance.setTemperature(temp);
			satPressure(substance);
			//substance.dewPressure();
			double pressure = substance.getPressure();
			
			
			double pressPass= (pressure- min_pressure)/Double.valueOf(n);	
			for(Integer j=0 ; j < n; j++){
					double press = pressure - j.doubleValue() * pressPass;
					substance.setPressure(press);
					double vaporEnthalpy = substance.getVapor().calculateEntropy();	
					//double vaporVolume = substance.getVapor().calculateMolarVolume();
					positionsV[countv++] = new Position(press,vaporEnthalpy,temp);
			}
			
		}
		plotBean.setThirdPlot(true);
		plotBean.setThirdJson(new Gson().toJson(positionsV));

		plotBean.setSecondPlot(true);
		plotBean.setSecondJson(new Gson().toJson(po));
		
		plotBean.setJsonData(new Gson().toJson( positionsl));
		
		plotBean.setxAxisLabel("Presión [Pa]");
		plotBean.setyAxisLabel("Entropía []");
		plotBean.setzAxisLabel("Temperatura [K]");
		
		return "webGLPlotTemplate";
	}
	
	
	
	public String gibbsPlot(){

		Heterogeneous substance= prepareHeterogeneous(homogeneousBean.getSelectedHomogeneous());
				
		double[] maxMin = maxTempForGibbs(homogeneousBean.getSelectedHomogeneous());
		double min_temp = maxMin[1];
		double max_temp = maxMin[0];
		
		double min_pressure = 0;
		double max_pressure =0;
		
		Integer n = 40;
		double tempPass = (max_temp - min_temp)/n.doubleValue();
		
		Position[] po = new Position[n*n];
		Integer count =0;
		for(Integer i = 0; i < n; i++){
			double temp = min_temp + i.doubleValue()* tempPass;
			substance.setTemperature(temp);
			satPressure(substance);
			
			Double pressure = substance.getPressure();
			min_pressure = (min_pressure == 0)?pressure: min_pressure;
			min_pressure = (pressure < min_pressure)? pressure: min_pressure;
			max_pressure = (pressure > max_pressure)? pressure: max_pressure;
			
			Double liquidEnthalpy = substance.getLiquid().calculateGibbs();
			Double vaporEnthalpy = substance.getVapor().calculateGibbs();
					
			double enthalpyStep  = (vaporEnthalpy- liquidEnthalpy)/n.doubleValue(); 
			for(Integer j = 0; j< n; j++){
				double enthalpy = liquidEnthalpy + j.doubleValue() * enthalpyStep;
				Position position = new Position(pressure,enthalpy,  temp);
				po[count++]=position;
			}
			//list.add(new Position(pressure,vaporEnthalpy,  temp));
		}
		
		
		
		double min_pressureSaved = min_pressure;
		max_pressure = max_pressure *1.3;
		min_pressure = 0.9* min_pressure;
		
		Position[] positionsl = new Position[n*n];
		Integer countl =0;
		for(Integer i = 0; i < n; i++){
			double temp = min_temp + i.doubleValue()* tempPass;
			substance.setTemperature(temp);
			satPressure(substance);
			//substance.dewPressure();
			double pressure = substance.getPressure();						
			double pressPass = (max_pressure- pressure) / n.doubleValue();
			for(Integer j = 0 ; j < n;j++){
				double press= pressure + j.doubleValue() * pressPass;
				substance.setPressure(press);
				double liquidEnthalpy = substance.getLiquid().calculateGibbs();
				//double liquidVolume = substance.getLiquid().calculateMolarVolume();
				positionsl[countl++] = new Position(press,liquidEnthalpy,temp);
			}
		}
		
		
		//double aboveCritical = max_temp * 1.05; 
		
		Position[] positionsV= new Position[n*n]; 
		Integer countv =0;
		for(Integer i = 0; i < n; i++){
			double temp = min_temp + i.doubleValue()* tempPass;
			substance.setTemperature(temp);
			satPressure(substance);
			//substance.dewPressure();
			double pressure = substance.getPressure();
			
			
			double pressPass= (pressure- min_pressure)/Double.valueOf(n);	
			for(Integer j=0 ; j < n; j++){
					double press = pressure - j.doubleValue() * pressPass;
					substance.setPressure(press);
					double vaporEnthalpy = substance.getVapor().calculateGibbs();	
					//double vaporVolume = substance.getVapor().calculateMolarVolume();
					positionsV[countv++] = new Position(press,vaporEnthalpy,temp);
			}
			
		}
		plotBean.setThirdPlot(true);
		plotBean.setThirdJson(new Gson().toJson(positionsV));

		plotBean.setSecondPlot(true);
		plotBean.setSecondJson(new Gson().toJson(po));
		
		plotBean.setJsonData(new Gson().toJson( positionsl));
		
		plotBean.setxAxisLabel("Presión [Pa]");
		plotBean.setyAxisLabel("E. Gibbs []");
		plotBean.setzAxisLabel("Temperatura [K]");
		
		return "webGLPlotTemplate";
	}	
	

}
