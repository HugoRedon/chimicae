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
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;

import com.google.gson.Gson;

@Named("pressureBean")
@SessionScoped
public class PressureBean implements Serializable {
	
	private static final long serialVersionUID = -6290200569610658624L;

	@Inject HomogeneousBean homogeneousBean;
	@Inject PlotBean plotBean;
	
	private Double minVolume=0.26;
	private Double maxVolume=1.2;
	private Double minTemperature=400d;
	private Double maxTemperature=700d;
	
	private Double minPressure= 2.2064E+07*0.1;
	
	private Double maxPressure = 2.2064E+07*7;
	
	
	@PostConstruct
	public void init() {
		
	}
	
	
	
	public String generateCompresibilityFactorPlot(){
		Integer n = 100;
		
		Position[] vertices = new Position[n*n];			
		
		double pressureStep = (maxPressure-minPressure)/n.doubleValue();
		double tempStep =(maxTemperature-minTemperature)/n.doubleValue();
		
		Integer count =0;
		for(Integer i = 0; i <n ; i++){
			for(Integer j = 0; j<n ; j++){
				double pressure=minPressure + i.doubleValue() * pressureStep;
				double temperature = minTemperature + j.doubleValue() * tempStep;
				Homogeneous homogeneous = homogeneousBean.getSelectedHomogeneous();
				homogeneous.setPressure(pressure);
				homogeneous.setTemperature(temperature);
				double z = homogeneous.calculateCompresibilityFactor();
				Position pos = new Position(temperature,pressure,z);
				vertices[count++] =pos;
			}
		}
		
		String verticesJson = new Gson().toJson(vertices);
		plotBean.setxAxisLabel("Temperatura [K]");
		plotBean.setyAxisLabel("Presión [Pa]");
		plotBean.setzAxisLabel("Factor de compresibilidad");
		plotBean.setPlotInfo("Z-Presión-Temperatura");
		plotBean.setJsonData(verticesJson);
		plotBean.setLine(false);
		plotBean.setSecondPlot(false);
		plotBean.setThirdPlot(false);
		return "webGLPlotTemplate";
	}
	
	public String generateMolarVolumePlot(){
		Integer n = 100;
		
		Position[] vertices = new Position[n*n];			
		
		double pressureStep = (maxPressure-minPressure)/n.doubleValue();
		double tempStep =(maxTemperature-minTemperature)/n.doubleValue();
		
		Integer count =0;
		for(Integer i = 0; i <n ; i++){
			for(Integer j = 0; j<n ; j++){
				double pressure=minPressure + i.doubleValue() * pressureStep;
				double temperature = minTemperature + j.doubleValue() * tempStep;
				Homogeneous homogeneous = homogeneousBean.getSelectedHomogeneous();
				homogeneous.setPressure(pressure);
				homogeneous.setTemperature(temperature);
				double volume = homogeneous.calculateMolarVolume();
				Position pos = new Position(temperature,pressure,volume);
				vertices[count++] =pos;
			}
		}
		
		String verticesJson = new Gson().toJson(vertices);
		plotBean.setJsonData(verticesJson);
		plotBean.setPlotInfo("Presión-Volumen-Temperatura");
		plotBean.setSecondPlot(false);
		plotBean.setThirdPlot(false);
		plotBean.setLine(false);
		
		plotBean.setxAxisLabel("Temperatura [K]");
		plotBean.setyAxisLabel("Presión [Pa]");
		plotBean.setzAxisLabel("Volumen Molar [m³/kmol]");
		return "webGLPlotTemplate";
	}
	
	
	public String generatePlot(){
		Integer n = 100;
		
		Position[] vertices = new Position[n*n];
		
		System.out.println("minVol: " + minVolume);
		
		double volStep = (maxVolume-minVolume)/n.doubleValue();
		double tempStep =(maxTemperature-minTemperature)/n.doubleValue();
		
		Integer count =0;
		for(Integer i = 0; i <n ; i++){
			for(Integer j = 0; j<n ; j++){
				double volume=minVolume + i.doubleValue() * volStep;
				double temperature = minTemperature + j.doubleValue() * tempStep;
				double p = homogeneousBean.getSelectedHomogeneous().calculatePressure(temperature, volume);
				Position pos = new Position(temperature,volume, p);
				vertices[count++] =pos;
			}
		}
		
		String verticesJson = new Gson().toJson(vertices);
		plotBean.setJsonData(verticesJson);
		plotBean.setSecondPlot(false);
		plotBean.setThirdPlot(false);
		plotBean.setLine(false);
		plotBean.setxAxisLabel("Temperatura [K]");
		plotBean.setzAxisLabel("Presión [Pa]");
		plotBean.setyAxisLabel("Volumen Molar [m³/kmol]");
		return "webGLPlotTemplate";
	}
	
	
	public Double getMinVolume() {
		return minVolume;
	}
	public void setMinVolume(Double minVolume) {
		this.minVolume = minVolume;
	}
	public Double getMaxVolume() {
		return maxVolume;
	}
	public void setMaxVolume(Double maxVolume) {
		this.maxVolume = maxVolume;
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
