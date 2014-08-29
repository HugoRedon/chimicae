package chimicae;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.eos.Cubic;
import termo.eos.EquationsOfState;
import termo.eos.mixingRule.MixingRule;
import termo.eos.mixingRule.VDWMixingRule;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.matter.builder.MixtureBuilder;
import termo.phase.Phase;

@Named("createMixture")
@SessionScoped
public class CreateMixture implements Serializable {

	private static final long serialVersionUID = -8857751799359596484L;
	private Cubic cubic= EquationsOfState.pengRobinson();
	private Phase phase;
	private MixingRule mixingRule =new VDWMixingRule();
	private ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();

	public MixingRule getMixingRule() {
		return mixingRule;
	}

	public void setMixingRule(MixingRule mixingRule) {
		this.mixingRule = mixingRule;
	}
	private List<CompoundAlphaFraction> compoundAlphaFractions = new ArrayList<>();
	private List<Compound> remainingCompounds = new ArrayList<>();
	@Inject AvailableCompounds availableCompounds;
	@Inject HomogeneousBean homogeneousBean;
	
	public void mixtureTableAction(){
		System.out.println("mixtureTableAction");
		System.out.println("compoundsAlphaFractions size : "+ compoundAlphaFractions.size());
	}
	
	@PostConstruct
	public void init(){
		
		if(availableCompounds.getCompounds().size() ==0){
			Compound comp = availableCompounds.getAvailableCompounds().get(6);
			Compound comp1  = availableCompounds.getAvailableCompounds().get(14);
			availableCompounds.getCompounds().add(comp);
			availableCompounds.getCompounds().add(comp1);
		}
		remainingCompounds = availableCompounds.getCompounds();
	}
	
	public void addRow(){

		
		List<Compound>  compounds = availableCompounds.getCompounds();
		List<Compound> selected= selectedCompounds();
		
		for(Compound next: compounds){
			if(!selected.contains(next)){
				CompoundAlphaFraction com = new CompoundAlphaFraction();
				com.setCompound(next);
				compoundAlphaFractions.add(com);
				break;
			}
		}
	}
	
	public void removeRow(CompoundAlphaFraction row){
		if(compoundAlphaFractions.contains(row)){
			compoundAlphaFractions.remove(row);
		}
	}
	
	
	
	public List<Compound> selectedCompounds(){
		List<Compound> result = new ArrayList<>();
		for(CompoundAlphaFraction caf: compoundAlphaFractions){
			result.add(caf.getCompound());
		}
		return result;
	}
	
	public String create(){
		MixtureBuilder mix = new MixtureBuilder()
					.setEquationOfState(cubic)
					.setPhase(phase)
					.setMixingRule(mixingRule)
					.setInteractionParameter(k);
		
		for(CompoundAlphaFraction row :compoundAlphaFractions){
			mix.addCompound(row.getCompound(), row.getAlpha(),row.getFraction());
		}
		
		mixture = mix.build();
		homogeneousBean.getHomogeneousList().add(mixture);
		
		return "viewMixture";
	}
	Mixture mixture;

	public List<Substance> getListAsSet(Set<Substance> set) {
		  return new ArrayList<Substance>(set);
	}
	public List<Compound> getListCAsSet(Set<Compound> set) {
		  return new ArrayList<Compound>(set);
	}
	
	
	public Mixture getMixture() {
		return mixture;
	}

	public void setMixture(Mixture mixture) {
		this.mixture = mixture;
	}

	public ActivityModelBinaryParameter getK() {
		return k;
	}

	public void setK(ActivityModelBinaryParameter k) {
		this.k = k;
	}
	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}
	public List<CompoundAlphaFraction> getCompoundAlphaFractions() {
		return compoundAlphaFractions;
	}
	public void setCompoundAlphaFractions(
			List<CompoundAlphaFraction> compoundAlphaFractions) {
		this.compoundAlphaFractions = compoundAlphaFractions;
	}
	public List<Compound> getRemainingCompounds() {
		return remainingCompounds;
	}
	public void setRemainingCompounds(List<Compound> remainingCompounds) {
		this.remainingCompounds = remainingCompounds;
	}

	public Cubic getCubic() {
		return cubic;
	}
	public void setCubic(Cubic cubic) {
		this.cubic = cubic;
	}


}
