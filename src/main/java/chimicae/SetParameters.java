package chimicae;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import utils.BinaryParameterModel;
import java.io.Serializable;

@Named("setParameters")
@SessionScoped
public class SetParameters implements Serializable {
	private static final long serialVersionUID = -8771915557992621006L;

	@Inject CreateMixture createMixture;
	
	List<BinaryParameterModel> vanDerWaals = new ArrayList<>();
	
	public BinaryParameterModel binariesForVDW(Compound compoundi,Compound compoundj){
		for(BinaryParameterModel bpm: vanDerWaals){
			if(bpm.getCompoundi().equals(compoundi) &&bpm.getCompoundj().equals(compoundj)){
				return bpm;
			}
		}
		return null;
	}
	
	public String clean(){
		vanDerWaals.clear();
		
		ActivityModelBinaryParameter k = createMixture.getK();
		for(CompoundAlphaFraction cafi:createMixture.getCompoundAlphaFractions()){
			for(CompoundAlphaFraction cafj:createMixture.getCompoundAlphaFractions()){
				Compound compoundi = cafi.getCompound();
				Compound compoundj = cafj.getCompound();
				
				Double value = k.getValue(compoundi, compoundj);
				BinaryParameterModel bpm = new BinaryParameterModel(compoundi, compoundj, value);
				vanDerWaals.add(bpm);
			}
		}
		
		
		return "parameters";//for h:link
	}
	
	
	
	
	public String save(){
		for(BinaryParameterModel p: vanDerWaals){
			createMixture.getK().setValue(p.getCompoundi(), p.getCompoundj(), p.getValue());
		}
		return "mixture";
	}
	public String cancel(){
		return "mixture";
	}
	


	public SetParameters() {
		// TODO Auto-generated constructor stub
	}

}
