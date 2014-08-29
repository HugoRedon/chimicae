package chimicae;

import termo.component.Compound;
import termo.eos.alpha.Alpha;
import termo.eos.alpha.Alphas;

public class CompoundAlphaFraction {
	private Boolean selected;
	private Compound compound;
	private Alpha alpha =Alphas.getStryjekAndVeraExpression();//default
	private Double fraction;
	
	public CompoundAlphaFraction() {
		// TODO Auto-generated constructor stub
	}
	
	
	public CompoundAlphaFraction(Compound compound, Alpha alpha, Double fraction) {
		super();
		this.compound = compound;
		this.alpha = alpha;
		this.fraction = fraction;
	}


	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
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
	public Double getFraction() {
		return fraction;
	}
	public void setFraction(Double fraction) {
		this.fraction = fraction;
	}

}
