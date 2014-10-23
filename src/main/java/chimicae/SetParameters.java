package chimicae;

import java.util.ArrayList;
import java.util.List;

import termo.activityModel.ActivityModel;
import termo.activityModel.NRTLActivityModel;
import termo.activityModel.UNIQUACActivityModel;
import termo.activityModel.VanLaarActivityModel;
import termo.activityModel.WilsonActivityModel;
import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.eos.mixingRule.ExcessGibbsMixingRule;
import termo.eos.mixingRule.MixingRule;
import termo.eos.mixingRule.TwoParameterVanDerWaals;
import termo.eos.mixingRule.VDWMixingRule;
import termo.eos.mixingRule.WongSandlerMixingRule;
import utils.BinaryParameterModelList;

//@Named("setParameters")
//@SessionScoped
public class SetParameters  {
//	private static final long serialVersionUID = -8771915557992621006L;

	//@Inject CreateMixture createMixture;
	List<Compound> compounds;// = new ArrayList<>();
	MixingRule mr ;
	ActivityModelBinaryParameter k;
	//String saveString = "mixture";
	
//	BinaryParameterModelList vanDerWaals ;
//	BinaryParameterModelList wongSandler_b;
//	BinaryParameterModelList activityModel_A ;
//	BinaryParameterModelList activityModel_B ;
//	BinaryParameterModelList nrtl_alpha;
	
	
	List<BinaryParameterModelList> list = new ArrayList<>();
	
	public SetParameters(List<Compound>compounds,
			MixingRule mr, ActivityModelBinaryParameter k ){
		this.compounds= compounds;
		this.mr = mr;
		this.k = k;
		
	
		createModels();
	}

	
	
	public void createModels(){
		
		//List<Compound> compounds =  createMixture.getListOfCompounds();
		if(isVDWMixingRule()){
			list.add(new BinaryParameterModelList(k, compounds,"k"));			
		}else if(isVDW2P()){
			list.add(new BinaryParameterModelList(k.getTwoParameterVanDerWaals(), compounds, "k"));
		}else if(isExcessGibbsBasedMixingRule()){
			if(isWongSandlerMixingRule()){
				list.add(new BinaryParameterModelList(k.getK(), compounds, "k ws"));
			}
			if( isNRTLActivityModel()){
				list.add(new BinaryParameterModelList(k.getAlpha(), compounds,"alfa"));
				list.add(new BinaryParameterModelList(k.getA(),compounds,"A"));
				list.add( new BinaryParameterModelList(k.getB(),compounds,"B"));
			}
			if(isWilsonMR()){
				list.add(new BinaryParameterModelList(k.getA(),compounds,"A"));
				list.add( new BinaryParameterModelList(k.getB(),compounds,"B"));
			}
			if(isVanLaarMR()){
				list.add(new BinaryParameterModelList(k.getA_vanLaar(),compounds,"A"));
			}
			if(isUniquac()){
				list.add(new BinaryParameterModelList(k.getA(), compounds, "U"));
			}
		}
//		vanDerWaals = new BinaryParameterModelList(k,compounds);
//		wongSandler_b = new BinaryParameterModelList(k.getK(), compounds);
//		activityModel_A = new BinaryParameterModelList(k.getA(),compounds);
//		activityModel_B = new BinaryParameterModelList(k.getB(),compounds);
//		nrtl_alpha = new BinaryParameterModelList(k.getAlpha(), compounds);
		
	}
	
//	public BinaryParameterModelList getActivityModel_B() {
//		return activityModel_B;
//	}
//
//	public void setActivityModel_B(BinaryParameterModelList activityModel_B) {
//		this.activityModel_B = activityModel_B;
//	}
//	
	
	public boolean isUniquac(){
		if(isExcessGibbsBasedMixingRule()){
			ActivityModel am = ((ExcessGibbsMixingRule)mr).getActivityModel();
			return am instanceof UNIQUACActivityModel; 
		}
		return false;	
	}
	public boolean isVanLaarMR(){
		if(isExcessGibbsBasedMixingRule()){
			ActivityModel am = ((ExcessGibbsMixingRule)mr).getActivityModel();
			return am instanceof VanLaarActivityModel; 
		}
		return false;	
	}

	public boolean isWilsonMR(){
		if(isExcessGibbsBasedMixingRule()){
			ActivityModel am = ((ExcessGibbsMixingRule)mr).getActivityModel();
			return am instanceof WilsonActivityModel; 
		}
		return false;	
	}
	public boolean isVDWMixingRule(){
		//MixingRule mr = createMixture.getMixingRule();
		return mr instanceof VDWMixingRule;
	}
	public boolean isVDW2P(){
		return mr instanceof TwoParameterVanDerWaals;
	}
	
	public boolean isWongSandlerMixingRule(){
		//MixingRule mr = createMixture.getMixingRule();
		return mr instanceof WongSandlerMixingRule;
	}
	
	public boolean isExcessGibbsBasedMixingRule(){
	//	MixingRule mr = createMixture.getMixingRule();
		return mr instanceof ExcessGibbsMixingRule;
	}
	
	public boolean isNRTLActivityModel(){
		//MixingRule mr = createMixture.getMixingRule();
		if(isExcessGibbsBasedMixingRule()){
			ActivityModel am = ((ExcessGibbsMixingRule)mr).getActivityModel();
			return am instanceof NRTLActivityModel; 
		}
		return false;			
	}
	
	public void save(){
		for(BinaryParameterModelList bpml: list){
			bpml.save();
		}
//		vanDerWaals.save();
//		wongSandler_b.save();
//		activityModel_A.save();
//		activityModel_B.save();
//		nrtl_alpha.save();		
	}
	
	public String clean(){
		
		//ActivityModelBinaryParameter params = createMixture.getK();
		
		createModels();
		return "parameters";
	}
	
	public String cancel(){
		return "mixture";
	}
//	public BinaryParameterModelList getVanDerWaals() {
//		return vanDerWaals;
//	}
//
//	public void setVanDerWaals(BinaryParameterModelList vanDerWaals) {
//		this.vanDerWaals = vanDerWaals;
//	}
//
//	public BinaryParameterModelList getWongSandler_b() {
//		return wongSandler_b;
//	}
//
//	public void setWongSandler_b(BinaryParameterModelList wongSandler_b) {
//		this.wongSandler_b = wongSandler_b;
//	}
//
//	public BinaryParameterModelList getActivityModel_A() {
//		return activityModel_A;
//	}
//
//	public void setActivityModel_A(BinaryParameterModelList activityModel_A) {
//		this.activityModel_A = activityModel_A;
//	}
//	public BinaryParameterModelList getNrtl_alpha() {
//		return nrtl_alpha;
//	}
//
//	public void setNrtl_alpha(BinaryParameterModelList nrtl_alpha) {
//		this.nrtl_alpha = nrtl_alpha;
//	}



	public List<BinaryParameterModelList> getList() {
		return list;
	}



	public void setList(List<BinaryParameterModelList> list) {
		this.list = list;
	}





}
