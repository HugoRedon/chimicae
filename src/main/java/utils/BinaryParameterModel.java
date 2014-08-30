package utils;

import termo.component.Compound;
import termo.eos.mixingRule.MixingRule;

public class BinaryParameterModel {
	private Compound compoundi;
	private Compound compoundj;
	private Double value;
	public BinaryParameterModel(Compound compoundi, Compound compoundj,
			Double value) {
		super();
		this.compoundi = compoundi;
		this.compoundj = compoundj;
		this.value = value;
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
		this.value = value;
	}	
}
