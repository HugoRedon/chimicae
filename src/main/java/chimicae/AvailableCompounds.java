package chimicae;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import termo.component.Compound;

import compoundreader.CompoundReader;

@Named("availableCompounds")
@SessionScoped
public class AvailableCompounds implements Serializable {
	

	private static final long serialVersionUID = -2347316573365401831L;
	
	private List<Compound> compounds= new ArrayList<>();
	private List<Compound> compoundsFound = new ArrayList<>();
	
	private String compoundName;
	
	private List<Compound> availableCompounds = new ArrayList<>();
	
	public void removeCompound(Compound compound){
		if(compounds.contains(compound)){
			compounds.remove(compound);
		}
	}

	public void addSelected(Compound selectedCompound){
		if(!compounds.contains(selectedCompound)){
			compounds.add(selectedCompound);
		}
	}

	public void lookForCompounds(){
		compoundsFound.clear();
		List<Compound> namelikeCompounds = new ArrayList<>(); 
		for(Compound compound :availableCompounds){
			if(compound.getName().matches("\\w*"+compoundName +"\\w*")){
				compoundsFound.add(compound);
			}
		}
		compoundsFound.addAll(namelikeCompounds);
	}
	
	CompoundReader reader;
	@PostConstruct
	public void availableCompounds() {
		reader = new CompoundReader();
		availableCompounds =reader.getComponents();
		
		Compound water = reader.getCompoundByExactName("Water");
		Compound methanol = reader.getCompoundByExactName("Methanol");
		compounds.add(water);
		compounds.add(methanol);
	}
	
	
	
	
	
	
	
	public List<Compound> getAvailableCompounds() {
		return availableCompounds;
	}

	public void setAvailableCompounds(List<Compound> availableCompounds) {
		this.availableCompounds = availableCompounds;
	}
	
	public String getCompoundName() {
		return compoundName;
	}

	public void setCompoundName(String compoundName) {
		this.compoundName = compoundName;
	}

	public List<Compound> getCompoundsFound() {
		return compoundsFound;
	}

	public void setCompoundsFound(List<Compound> compoundsFound) {
		this.compoundsFound = compoundsFound;
	}

	public List<Compound> getCompounds() {
		return compounds;
	}

	public void setCompounds(List<Compound> compounds) {
		this.compounds = compounds;
	}


	

}
