package models;

import java.util.ArrayList;
import java.util.List;

import termo.matter.Homogeneous;

public abstract class  Envelope{
	String name;
	List<PointInfo> liquidLine = new ArrayList<>();
	List<PointInfo> vaporLine= new ArrayList<>();
	
	public Envelope(String name) {
		this.name = name;
	}	
	public abstract void calculateEnvelope();
	
	public PointInfo fillPointInfo(Homogeneous sub){
		PointInfo pi = new PointInfo();
		pi.setTemperature(sub.getTemperature());
		pi.setPressure(sub.getPressure());
		pi.setEnthalpy(sub.calculateEnthalpy());
		pi.setEntropy(sub.calculateEntropy());
		pi.setGibbs(sub.calculateGibbs());
		pi.setMolarVolume(sub.calculateMolarVolume());
		return pi;
	}
	
	
	public List<PointInfo> getLiquidLine() {
		return liquidLine;
	}
	public void setLiquidLine(List<PointInfo> liquidLine) {
		this.liquidLine = liquidLine;
	}
	public List<PointInfo> getVaporLine() {
		return vaporLine;
	}
	public void setVaporLine(List<PointInfo> vaporLine) {
		this.vaporLine = vaporLine;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
}