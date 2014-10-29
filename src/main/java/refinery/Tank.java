package refinery;

import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Substance;
import chimicae.CompoundAlphaFraction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;


public class Tank {

	@Expose
	private long id;
	@Expose
	private double capacity;
	@Expose
	private PropertiesReport pr;
	private Heterogeneous heterogeneous;

	
	public Tank(long id, Heterogeneous heterogeneous){
		this.id=id;
		this.heterogeneous=heterogeneous;
		calculateReport();
	}
	public long getId() {
		return id;
	}


	public Heterogeneous getHeterogeneous() {
		return heterogeneous;
	}

	public void setHeterogeneous(Heterogeneous heterogeneous) {
		this.heterogeneous = heterogeneous;
	}
	
	public void calculateReport(){
		if(heterogeneous instanceof HeterogeneousSubstance){
			pr=new SubstancePropertiesReport((HeterogeneousSubstance)heterogeneous);
		}else{
			pr=new MixturePropertiesReport((HeterogeneousMixture)heterogeneous);
		}		
	}
	
	String toJson(){
		Gson gson =new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create(); 
		return gson.toJson(this);
		
	}
	
}

class PropertiesReport{
	@Expose
	double temperature,
	pressure,
	enthalpy,
	entropy,
	gibbs,vf;
	
	public PropertiesReport(Heterogeneous het){
		enthalpy = het.calculateEntropy();
		entropy = het.calculateEntropy();
		gibbs = het.calculateGibbs();
		temperature = het.getTemperature();
		pressure = het.getPressure();
		vf = het.getvF();
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
			caf[count] = new CompoundAlphaNamesFraction(sub.getComponent().getName()
					, sub.getAlpha().getName(), 
					sub.getMolarFraction());
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