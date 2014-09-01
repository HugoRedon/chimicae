package chimicae;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import json.Position;
import plot.PlotBean;
import termo.component.Compound;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.phase.Phase;

import com.google.gson.Gson;

@Named("fugacityBean")
@SessionScoped
public class FugacityBean implements Serializable {
	private static final long serialVersionUID = -7363298590381139387L;

	@Inject HomogeneousBean homogeneousBean;
	@Inject PlotBean plotBean;
	
	private Double minTemperature=350d;
	private Double maxTemperature=500d;
	
	private Double minPressure= 101325d;
	
	private Double maxPressure =2060908.0625;
	
	
	

	@PostConstruct
	public void init() {
		
	}
	
	public String generateFugacityLiquidVaporPlot(){
		Homogeneous homogeneous = homogeneousBean.getSelectedHomogeneous();
	
		Phase originalPhase = homogeneous.getPhase();
		
		Integer n = 100;
		Double pressureStep = (maxPressure-minPressure)/n.doubleValue();
		List<Position > list = new ArrayList<>();
		List<Position>  vaporlist= new ArrayList<>();
		List<Position> line= new ArrayList<>();
		if(homogeneous instanceof Substance){
			HeterogeneousSubstance substance = new HeterogeneousSubstance(homogeneous.getCubicEquationOfState(),
					((Substance) homogeneous).getAlpha(),((Substance) homogeneous).getComponent());
			for(Integer i = 0; i <n ; i++){
				Double p = minPressure + i.doubleValue()*pressureStep;
				substance.setPressure(p);
				substance.bubblePressure();
				double temperature = substance.getTemperature();
				double fug = substance.getLiquid().calculateFugacity();
				line.add(new Position(temperature, p, fug));
				Position[] position = new Position[n];
				plotBean.setLine(false);
				//plotBean.setLineJson(new Gson().toJson(line.toArray(position)));
			}
		}
		
		
		homogeneous.setPhase(Phase.LIQUID);
		for(Integer i = 0; i < n ; i++){
			Double pressure = minPressure + i.doubleValue()* pressureStep;
			appendLinesForPressure(n,list,homogeneous, pressure);
		}
		homogeneous.setPhase(Phase.VAPOR);
		for(Integer i = 0; i < n ; i++){
			Double pressure = minPressure + i.doubleValue()* pressureStep;
			appendLinesForPressure(n,vaporlist,homogeneous, pressure);
		}
		
		homogeneous.setPhase(originalPhase);
		Position[] vertices = new Position[n*n];
		
		Position[] vaporVertices = new Position[n*n];
		
		plotBean.setJsonData(new Gson().toJson(list.toArray(vertices)));
		plotBean.setSecondJson(new Gson().toJson(vaporlist.toArray(vaporVertices)));
		plotBean.setPlotInfo("Fugacidad-Presión-Temperatura: Vapor(plano rojo),Líquido(plano azul)");
		plotBean.setxAxisLabel("Temperatura [K]");
		plotBean.setyAxisLabel("Presión [Pa]");
		plotBean.setzAxisLabel("Fugacidad");
		plotBean.setThirdPlot(false);
		plotBean.setSecondPlot(true);
		
		return "webGLPlotTemplate";
	}
	
	public void appendLinesForPressure(Integer n,List<Position>list,Homogeneous homogeneous,Double pressure){
		homogeneous.setPressure(pressure);
	
		
		Double tempStep = (maxTemperature -minTemperature)/n.doubleValue();
		for(Integer i =0 ; i < n ; i++){
			Double temperature = minTemperature + i* tempStep;
			homogeneous.setTemperature(temperature);
			Double fugacity=0d;
			if(homogeneous instanceof Substance){
				fugacity = ((Substance)homogeneous).calculateFugacity();
			}else if(homogeneous instanceof Mixture){
				Compound selectedCompound = homogeneousBean.getSelectedCompound();
				Mixture mix = ((Mixture)homogeneous);
				if(selectedCompound ==null){
					Compound com=mix.getComponents().iterator().next();
					homogeneousBean.setSelectedCompound(com);
					selectedCompound = com;
				}
				fugacity = mix.calculateFugacity(selectedCompound);
			}
			list.add(new Position(temperature, pressure, fugacity));
		}
		
	}	

	
	public Double getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(Double minTemperature) {
		this.minTemperature = minTemperature;
	}

	public Double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(Double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public Double getMinPressure() {
		return minPressure;
	}

	public void setMinPressure(Double minPressure) {
		this.minPressure = minPressure;
	}

	public Double getMaxPressure() {
		return maxPressure;
	}

	public void setMaxPressure(Double maxPressure) {
		this.maxPressure = maxPressure;
	}


}
