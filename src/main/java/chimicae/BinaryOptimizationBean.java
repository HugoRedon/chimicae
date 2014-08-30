package chimicae;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.matter.builder.HeterogeneousMixtureBuilder;

@Named("binaryOptimizationBean")
@SessionScoped
public class BinaryOptimizationBean implements Serializable,PropertyChangeListener {


	private static final long serialVersionUID = 1555348924467092561L;
	@Inject HomogeneousBean homogeneousBean;
	
	@PostConstruct
	public void init(){
		homogeneousBean.addPropertyChangelistener(this);
	}
	
	List<HeterogeneousMixture> hmList = new ArrayList<>();
	HeterogeneousMixture selectedMix;
	
	
	public boolean isMixtureWithTwoCompounds(Homogeneous homogeneous){
		if(homogeneous instanceof Substance){
			return false;
		}
		Mixture mix = (Mixture)homogeneous;
		if(mix.getComponents().size() != 2){
			return false;
		}
		return true;
	}

	public void update(){
		hmList.clear();
		for(Homogeneous homogeneous:homogeneousBean.getHomogeneousList()){
			if(isMixtureWithTwoCompounds(homogeneous)){
				HeterogeneousMixture hm = getHeterogeneous((Mixture)homogeneous);
				hmList.add(hm);
			}
		}
	}
	
	public List<HeterogeneousMixture> getHmList() {
		return hmList;
	}

	public void setHmList(List<HeterogeneousMixture> hmList) {
		this.hmList = hmList;
	}

	public HeterogeneousMixture getHeterogeneous(Mixture mix){
		HeterogeneousMixture hetMixture =new HeterogeneousMixtureBuilder().fromMixture(mix);
		return hetMixture;
	}
	
	public Heterogeneous getHeterogeneous(Substance substance){
		return new HeterogeneousSubstance(substance.getCubicEquationOfState(),
				substance.getAlpha(),substance.getComponent());
	}
	public List<ParameterViewModel> getParameterList(){
		List<ParameterViewModel> result= new ArrayList<>();
		return result;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		update();
	}

	public HeterogeneousMixture getSelectedMix() {
		return selectedMix;
	}

	public void setSelectedMix(HeterogeneousMixture selectedMix) {
		this.selectedMix = selectedMix;
	}
}
