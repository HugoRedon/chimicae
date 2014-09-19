package models;

import java.util.ArrayList;
import java.util.List;

import termo.matter.Homogeneous;

public abstract class  Envelope{
	String name;
	List<PointInfo> liquidLine = new ArrayList<>();
	List<PointInfo> vaporLine= new ArrayList<>();
	List<PointInfo[]> vaporAreaTemperatureLines = new ArrayList<>();
	List<PointInfo[]> liquidAreaTemperatureLines = new ArrayList<>();
	
	List<PointInfo[]> vaporAreaPressureLines = new ArrayList<>();
	List<PointInfo[]> liquidAreaPressureLines = new ArrayList<>();
	
	
	public List<PointInfo[]> getVaporAreaPressureLines() {
		return vaporAreaPressureLines;
	}
	public void setVaporAreaPressureLines(List<PointInfo[]> vaporAreaPressureLines) {
		this.vaporAreaPressureLines = vaporAreaPressureLines;
	}
	public List<PointInfo[]> getLiquidAreaPressureLines() {
		return liquidAreaPressureLines;
	}
	public void setLiquidAreaPressureLines(List<PointInfo[]> liquidAreaPressureLines) {
		this.liquidAreaPressureLines = liquidAreaPressureLines;
	}
	public List<PointInfo[]> getVaporAreaTemperatureLines() {
		return vaporAreaTemperatureLines;
	}
	public void setVaporAreaTemperatureLines(
			List<PointInfo[]> vaporAreaTemperatureLines) {
		this.vaporAreaTemperatureLines = vaporAreaTemperatureLines;
	}
	public List<PointInfo[]> getLiquidAreaTemperatureLines() {
		return liquidAreaTemperatureLines;
	}
	public void setLiquidAreaTemperatureLines(
			List<PointInfo[]> liquidAreaTemperatureLines) {
		this.liquidAreaTemperatureLines = liquidAreaTemperatureLines;
	}
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