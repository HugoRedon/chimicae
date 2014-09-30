package utils;

import java.util.ArrayList;
import java.util.List;

import termo.binaryParameter.InteractionParameter;
import termo.component.Compound;
import chimicae.CompoundAlphaFraction;

public class BinaryParameterModelList {
	List<BinaryParameterModel> list = new ArrayList<>();
	
	InteractionParameter params ;
	List<CompoundAlphaFraction> caf;
	
	public InteractionParameter getParams() {
		return params;
	}

	public void setParams(InteractionParameter params) {
		this.params = params;
	}

	public List<CompoundAlphaFraction> getCaf() {
		return caf;
	}

	public void setCaf(List<CompoundAlphaFraction> caf) {
		this.caf = caf;
	}

	public BinaryParameterModelList(InteractionParameter params,List<CompoundAlphaFraction> caf){
		this.caf = caf;
		this.params = params;
	}

	public List<BinaryParameterModel> getList() {
		return list;
	}

	public void setList(List<BinaryParameterModel> list) {
		this.list = list;
	}
	
	public BinaryParameterModel binaryFor(Compound ci,Compound cj){
		for(BinaryParameterModel bpm: list){
			if(bpm.getCompoundi().equals(ci) &&bpm.getCompoundj().equals(cj)){
				return bpm;
			}
		}
		return null;
	}
	
	public void clean(){
		list.clear();
		
		
		for(CompoundAlphaFraction cafi:caf){
			for(CompoundAlphaFraction cafj:caf){
				Compound compoundi = cafi.getCompound();
				Compound compoundj = cafj.getCompound();
				
				Double value = params.getValue(compoundi, compoundj);
				BinaryParameterModel bpm = new BinaryParameterModel(compoundi, compoundj, value);
				list.add(bpm);
			}
		}				
	}
	public void save(){
		for(BinaryParameterModel p: list){
			params.setValue(p.getCompoundi(), p.getCompoundj(), p.getValue());
		}		
	}
}
