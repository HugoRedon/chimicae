package chimicae;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import termo.binaryParameter.InteractionParameter;
import termo.component.Compound;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.VDWMixingRule;
import termo.matter.Homogeneous;
import termo.matter.Mixture;
import termo.matter.Substance;
import termo.matter.builder.MixtureBuilder;
import termo.phase.Phase;

@Named("homogeneousBean")
@SessionScoped
public class HomogeneousBean implements Serializable {

	private static final long serialVersionUID = -1688094863072127882L;
	
	PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	private List<Homogeneous> homogeneousList= new ArrayList<>();
	
	private Homogeneous selectedHomogeneous;
	
	@Inject AvailableCompounds availableCompounds;
	private Compound selectedCompound;
	
	
	public Compound getSelectedCompound() {
		return selectedCompound;
	}

	public void setSelectedCompound(Compound selectedCompound) {
		this.selectedCompound = selectedCompound;
	}

	@PostConstruct		
	public void init(){
		Compound water =null;
		Compound methanol = null;
		for(Compound com: availableCompounds.getCompounds()){
			if(com.getName().equals("water")){
				water = com;
			}else if(com.getName().equals("methanol")){
				methanol = com;
			}
		}
		
		Substance sub = new Substance(EquationsOfState.pengRobinson(),Alphas.getStryjekAndVeraExpression(),water,Phase.VAPOR);
		
		homogeneousList.add(sub);
		
		
		Mixture mix = new MixtureBuilder().setEquationOfState(EquationsOfState.pengRobinson())
				.setAlpha(Alphas.getStryjekAndVeraExpression())
				.addCompounds(water,methanol)
				.setInteractionParameter(new InteractionParameter())
				.setPhase(Phase.VAPOR).setMixingRule(new VDWMixingRule()).build();
		homogeneousList.add(mix);
		selectedHomogeneous=sub;
		
	}
	
	public List<Compound> selectedHomogeneousCompounds(){
		List<Compound> result = new ArrayList<>();
		if(isMixture()){
			for(Compound com: ((Mixture)selectedHomogeneous).getComponents()){
				result.add(com);
			}
		}
		return result;
	}
	public Converter converterCompoundForSelectedMixture(){
		return new Converter() {
			@Override
			public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
				
				return ((Compound)arg2).getName();
			}
			@Override
			public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
				Mixture mix = (Mixture)selectedHomogeneous;
				for(Compound compound: mix.getComponents()){
					if(compound.getName().equals(arg2)){
						return compound;
					}
				}
				return null;
			}
		};
	}
	
	
	public boolean isMixture(){
		return selectedHomogeneous instanceof Mixture;
	}
	
	public String instanceOf(Homogeneous homogeneous){
		if(homogeneous instanceof Mixture){
			return "Mezcla";
		}
		return "Substancia";
	}
	
	public Integer numberOfCompounds(Homogeneous homogeneous){
		if(homogeneous instanceof Substance){
			return 1;
		}
		return ((Mixture)homogeneous).getComponents().size();
	}
	
	public String alphaExpresion(Homogeneous homogeneous){
		if(homogeneous instanceof Substance){
			return ((Substance) homogeneous).getAlpha().getName();
		}
		Iterator<Substance> iterator = ((Mixture)homogeneous).getPureSubstances().iterator();
		String alphaName=null;
		while(iterator.hasNext()){
			Substance sub=iterator.next();
			String an =sub.getAlpha().getName();
			if(alphaName==null){
				alphaName = an;
				continue;
			}
			if(!alphaName.equals(an)){
				return "Diferentes";
			}
		}
		return alphaName;	
	}
	
	public Substance castToSubstance(Homogeneous homogeneous){
		return (Substance)homogeneous;
	}
	public Mixture castToMixture(Homogeneous homogeneous){
		return (Mixture)homogeneous;
	}
	
	public Substance getSelectedHomogeneousAsSubstance(){
		Substance result;
		try{
			result =(Substance)selectedHomogeneous; 
		}catch(Exception ex){
			result = new Substance();
		}
		return result;
	}
	public Mixture getSelectedHomogeneousAsMixture(){
		return(Mixture)selectedHomogeneous;
	}
	public void remove(Homogeneous homogeneous){
		if(homogeneousList.contains(homogeneous)){
			homogeneousList.remove(homogeneous);
		}
	}
	
	
	
	

	public Homogeneous getSelectedHomogeneous() {
		return selectedHomogeneous;
	}

	public void setSelectedHomogeneous(Homogeneous selectedHomogeneous) {
		this.selectedHomogeneous = selectedHomogeneous;
	}

	public List<Homogeneous> getHomogeneousList() {
		return homogeneousList;
	}

	public void setHomogeneousList(List<Homogeneous> homogeneous) {
		this.homogeneousList = homogeneous;
	}

	public HomogeneousBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void add(Homogeneous homogeneous){
		List<Homogeneous> homogeneousListOld = new ArrayList<>();
		for(Homogeneous h: homogeneousList){
			homogeneousListOld.add(h);
		}
		this.homogeneousList.add(homogeneous);
		pcs.firePropertyChange("homogeneous",  homogeneousListOld, this.homogeneousList);
	}
	public void addPropertyChangelistener(PropertyChangeListener pcl){
		pcs.addPropertyChangeListener(pcl);
	}
	

}
