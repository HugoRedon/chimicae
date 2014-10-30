package refinery;

import com.google.gson.annotations.Expose;

import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Substance;

public class PropertiesReport{
	@Expose
	double temperature,
	pressure,
	enthalpy,
	entropy,
	gibbs,vF;
	
	public PropertiesReport(Heterogeneous het){
		het.calculateVF();
		enthalpy = het.calculateEnthalpy();
		entropy = het.calculateEntropy();
		gibbs = het.calculateGibbs();
		temperature = het.getTemperature();
		pressure = het.getPressure();
		vF = het.getvF();
		System.out.println(vF);
	}
	
	public static PropertiesReport calculateReport(Heterogeneous heterogeneous){
		PropertiesReport pr;
		if(heterogeneous instanceof HeterogeneousSubstance){
			pr=new SubstancePropertiesReport((HeterogeneousSubstance)heterogeneous);
		}else{
			pr=new MixturePropertiesReport((HeterogeneousMixture)heterogeneous);
		}		
		return pr;
	}
}

class SubstancePropertiesReport extends PropertiesReport{
	public SubstancePropertiesReport(HeterogeneousSubstance sub){
		super(sub);
		compoundName = sub.getComponent().getName();
	}
	@Expose
	String compoundName;
}

class MixturePropertiesReport extends PropertiesReport{
	@Expose
	CompoundAlphaNamesFraction[] caf;
	
	public MixturePropertiesReport(HeterogeneousMixture m){
		super(m);
		caf= new CompoundAlphaNamesFraction[m.getComponents().size()];
		int count=0;
		
		
		for(Substance sub:m.getLiquid().getPureSubstances()){
			
			String compoundName = sub.getComponent().getName();
			String alphaName = sub.getAlpha().getName();
			double fraction = m.getzFractions().get(sub.getComponent().getName());
			caf[count++] = new CompoundAlphaNamesFraction(compoundName
					,alphaName, fraction);
		}	
		
	}
}

class CompoundAlphaNamesFraction{
	@Expose
	String compoundName;
	@Expose
	String alphaName;
	@Expose
	double fraction;
	
	public CompoundAlphaNamesFraction(String cn,String an,double f){
		this.compoundName=cn;
		this.alphaName=an;
		this.fraction=f;
		
	}
}