package chimicae;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.component.Compound;
import termo.eos.Cubic;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alpha;
import termo.eos.alpha.Alphas;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Substance;
import termo.phase.Phase;

@Named("createSubstance")
@SessionScoped
public class CreateSubstance implements Serializable {
	private static final long serialVersionUID = 8487770688741770013L;
	
	private Cubic cubic = EquationsOfState.pengRobinson();//default
	private Alpha alpha = Alphas.getStryjekAndVeraExpression();//default
	private Compound compound;
	private Phase phase;
	@Inject AvailableCompounds availableCompounds;
	@Inject HomogeneousBean homogeneousBean;
	
	@PostConstruct
	public void init(){
		if(availableCompounds.getCompounds().size() ==0){
			Compound com = availableCompounds.getAvailableCompounds().get(6);
			availableCompounds.getCompounds().add(com);
		}
		compound =availableCompounds.getCompounds().get(0);
	}
	
	public String create(){
		substance = new Substance(cubic, alpha, compound, phase);
		homogeneousBean.getHomogeneousList().add(substance);
		homogeneousBean.setSelectedHomogeneous(substance);
		return "viewSubstance";
	}
	
	Substance substance;
	
	
	
	public Substance getSubstance() {
		return substance;
	}

	public void setSubstance(Substance substance) {
		this.substance = substance;
	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}
	public Compound getCompound() {
		return compound;
	}

	public void setCompound(Compound compound) {
		this.compound = compound;
	}

	public Alpha getAlpha() {
		return alpha;
	}

	public void setAlpha(Alpha alpha) {
		this.alpha = alpha;
	}

	public Cubic getCubic() {
		return cubic;
	}

	public void setCubic(Cubic cubic) {
		this.cubic = cubic;
	}
}
