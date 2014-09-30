package utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import termo.component.Compound;

public class BinaryParameterModel {

	private Compound compoundi;
	private Compound compoundj;
	private Double value;
	
	private boolean disabled=false;
	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	PropertyChangeSupport mpcs = new PropertyChangeSupport(this);
	public BinaryParameterModel(Compound compoundi, Compound compoundj,
			Double value) {	
		this.compoundi = compoundi;
		this.compoundj = compoundj;
		this.value = value;
	}
	public BinaryParameterModel(Compound compoundi, Compound compoundj,
			Double value,PropertyChangeListener pcl){
		this(compoundi,compoundj,value);
		mpcs.addPropertyChangeListener(pcl);
		
	}
	public Compound getCompoundi() {
		return compoundi;
	}
	public void setCompoundi(Compound compoundi) {
		this.compoundi = compoundi;
	}
	public Compound getCompoundj() {
		return compoundj;
	}
	public void setCompoundj(Compound compoundj) {
		this.compoundj = compoundj;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		Double oldValue = this.value;
		this.value = value;
		mpcs.firePropertyChange("value", oldValue, value);
		System.out.println(compoundi.getName()+compoundj.getName()+"setvalue");
	}	
	public void setValueWithoutFiringPropertyChange(Double value){
		this.value = value;
		System.out.println(compoundi.getName()+compoundj.getName()+"setValueWithoutFiringPropertyChange");	
	}
}
