package chimicae;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.activityModel.ActivityModel;
import termo.activityModel.NRTLActivityModel;
import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
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
	BinaryParameterModelList nrtl_alpha;
	

	
	
	public void createModels(ActivityModelBinaryParameter params){
		List<Compound> caf =  createMixture.getListOfCompounds();
		
		vanDerWaals = new BinaryParameterModelList(params,caf);
		wongSandler_b = new BinaryParameterModelList(params.getK(), caf);
		activityModel_A = new BinaryParameterModelList(params.getA(),caf);
		activityModel_B = new BinaryParameterModelList(params.getB(),caf);
		nrtl_alpha = new BinaryParameterModelList(params.getAlpha(), caf);
		
	}
	
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
		nrtl_alpha.save();
		return "mixture";
	}
	
	public String clean(){
		
		ActivityModelBinaryParameter params = createMixture.getK();
		
		createModels(params);
		return "parameters";
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
	public BinaryParameterModelList getNrtl_alpha() {
		return nrtl_alpha;
	}

	public void setNrtl_alpha(BinaryParameterModelList nrtl_alpha) {
		this.nrtl_alpha = nrtl_alpha;
	}


}
