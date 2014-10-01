package utils;

import java.util.ArrayList;
import java.util.List;

import termo.binaryParameter.InteractionParameter;
import termo.component.Compound;

public class BinaryParameterModelList{
	List<BinaryParameterModel> list = new ArrayList<>();
	
	InteractionParameter params ;
	List<Compound> caf;
	String name ;
	
	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public BinaryParameterModelList(InteractionParameter params,List<Compound> caf){
		this.caf = caf;
		this.params = params;
		
		
		for(Compound cafi:caf){
			for(Compound cafj:caf){
				double value= params.getValue(cafi, cafj);
				
				Compound ci = cafi;
				Compound cj = cafj;
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

	public List<Compound> getCaf() {
		return caf;
	}

	public void setCaf(List<Compound> caf) {
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

	
}
