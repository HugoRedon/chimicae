package chimicae;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.activityModel.ActivityModel;
import termo.activityModel.NRTLActivityModel;
import termo.eos.mixingRule.ExcessGibbsMixingRule;
import termo.eos.mixingRule.MixingRule;
import termo.eos.mixingRule.VDWMixingRule;
import termo.eos.mixingRule.WongSandlerMixingRule;
import utils.BinaryParameterModelList;

@Named("setParameters")
@SessionScoped
public class SetParameters implements Serializable {
	private static final long serialVersionUID = -8771915557992621006L;

	@Inject CreateMixture createMixture;
	
	BinaryParameterModelList vanDerWaals ;
	BinaryParameterModelList wongSandler_b;
	BinaryParameterModelList activityModel_A ;
	BinaryParameterModelList activityModel_B ;
	
	public BinaryParameterModelList getActivityModel_B() {
		return activityModel_B;
	}

	public void setActivityModel_B(BinaryParameterModelList activityModel_B) {
		this.activityModel_B = activityModel_B;
	}

	public boolean isVDWMixingRule(){
		MixingRule mr = createMixture.getMixingRule();
		return mr instanceof VDWMixingRule;
	}
	
	public boolean isWongSandlerMixingRule(){
		MixingRule mr = createMixture.getMixingRule();
		return mr instanceof WongSandlerMixingRule;
	}
	
	public boolean isExcessGibbsBasedMixingRule(){
		MixingRule mr = createMixture.getMixingRule();
		return mr instanceof ExcessGibbsMixingRule;
	}
	
	public boolean isNRTLActivityModel(){
		MixingRule mr = createMixture.getMixingRule();
		if(isExcessGibbsBasedMixingRule()){
			ActivityModel am = ((ExcessGibbsMixingRule)mr).getActivityModel();
			return am instanceof NRTLActivityModel; 
		}
		return false;			
	}
	
	public String save(){
		vanDerWaals.save();
		wongSandler_b.save();
		activityModel_A.save();
		activityModel_B.save();
		return "mixture";
	}
	
	
	public String cancel(){
		return "mixture";
	}
	public BinaryParameterModelList getVanDerWaals() {
		return vanDerWaals;
	}

	public void setVanDerWaals(BinaryParameterModelList vanDerWaals) {
		this.vanDerWaals = vanDerWaals;
	}

	public BinaryParameterModelList getWongSandler_b() {
		return wongSandler_b;
	}

	public void setWongSandler_b(BinaryParameterModelList wongSandler_b) {
		this.wongSandler_b = wongSandler_b;
	}

	public BinaryParameterModelList getActivityModel_A() {
		return activityModel_A;
	}

	public void setActivityModel_A(BinaryParameterModelList activityModel_A) {
		this.activityModel_A = activityModel_A;
	}


}
