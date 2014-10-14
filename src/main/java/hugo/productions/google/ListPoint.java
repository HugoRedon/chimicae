package hugo.productions.google;

import java.util.ArrayList;
import java.util.List;

import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.phase.Phase;

public class ListPoint{
	boolean show = true;
	String label;
	String id ;
	double temperature;
	Phase phase ;
	double k;
	
	Double minX;
	Double maxX;
	
	Integer nForCalculation =25; 
	boolean lineToBeCalculated =true;
	boolean otherPhaseToBeCalculated;
	
	List<Point> list = new ArrayList<>();
	double Temperature ;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<Point> getList() {
		return list;
	}
	public void setList(List<Point> list) {
		this.list = list;
	}
	public double getTemperature() {
		return Temperature;
	}
	public void setTemperature(double temperature) {
		Temperature = temperature;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Phase getPhase() {
		return phase;
	}
	public void setPhase(Phase phase) {
		this.phase = phase;
	}
	public Integer getNForCalculation() {
		return nForCalculation;
	}
	public void setNForCalculation(Integer n) {
		this.nForCalculation = n;
	}
	public boolean isLineToBeCalculated() {
		return lineToBeCalculated;
	}
	public void setLineToBeCalculated(boolean calculateLine) {
		this.lineToBeCalculated = calculateLine;
	}
	public boolean isOtherPhaseToBeCalculated() {
		return otherPhaseToBeCalculated;
	}
	public void setOtherPhaseToBeCalculated(boolean calculateOtherPhaseLine) {
		this.otherPhaseToBeCalculated = calculateOtherPhaseLine;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public double getK() {
		return k;
	}
	public void setK(double k) {
		this.k = k;
	}
	public Double getMinX() {
		return minX;
	}
	public void setMinX(Double minX) {
		this.minX = minX;
	}
	public Double getMaxX() {
		return maxX;
	}
	public void setMaxX(Double maxX) {
		this.maxX = maxX;
	}

	
}