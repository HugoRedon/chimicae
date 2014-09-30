package utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import termo.binaryParameter.InteractionParameter;
import termo.component.Compound;
import chimicae.CompoundAlphaFraction;

public class BinaryParameterModelList implements PropertyChangeListener{
	List<BinaryParameterModel> list = new ArrayList<>();
	
	InteractionParameter params ;
	List<CompoundAlphaFraction> caf;
	
	public BinaryParameterModelList(InteractionParameter params,List<CompoundAlphaFraction> caf){
		this.caf = caf;
		this.params = params;
		
		
		for(CompoundAlphaFraction cafi:caf){
			for(CompoundAlphaFraction cafj:caf){
				double value= params.getValue(cafi.getCompound(), cafj.getCompound());
				
				Compound ci = cafi.getCompound();
				Compound cj = cafj.getCompound();
				BinaryParameterModel bpm =null;
				
				if(params.isSymmetric()){
					if(binaryFor(cj, ci)!=null){
						System.out.println("continue");
						continue;
					}	
				}
				bpm = new BinaryParameterModel(ci, cj, value);
				if(ci.equals(cj)){
					bpm.setDisabled(true);
				}
				list.add(bpm);
			}
		}
	}
	public boolean isEmpty(Compound ci,Compound cj){
		boolean result = binaryFor(ci, cj)==null; 
		System.out.println(result);
		
		return result;
	}
	
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
	
//	public void clean(){
//		list.clear();
//		
//		
//		for(CompoundAlphaFraction cafi:caf){
//			for(CompoundAlphaFraction cafj:caf){
//				Compound compoundi = cafi.getCompound();
//				Compound compoundj = cafj.getCompound();
//				
//				Double value = params.getValue(compoundi, compoundj);
//				BinaryParameterModel bpm = new BinaryParameterModel(compoundi, compoundj, value);
//				list.add(bpm);
//			}
//		}				
//	}
	public void save(){
		for(BinaryParameterModel p: list){
			params.setValue(p.getCompoundi(), p.getCompoundj(), p.getValue());
		}		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		BinaryParameterModel bpm = (BinaryParameterModel) evt.getSource();
		Double newValue = (Double)evt.getNewValue();
		//System.out.println("newValue"+newValue);
	//	binaryFor(bpm.getCompoundj(), bpm.getCompoundi()).setValueWithoutFiringPropertyChange(newValue);
		//System.out.println("ci"+bpm.getCompoundi());
		//System.out.println("cj"+bpm.getCompoundj());
		
	}
}
