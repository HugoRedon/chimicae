package refinery;

import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.Substance;
import chimicae.CompoundAlphaFraction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;


public class Tank extends Equipment {

	
	@Expose
	private double capacity;
	@Expose
	private PropertiesReport pr;
	private Heterogeneous heterogeneous;

	
	public Tank(long id, Heterogeneous heterogeneous){
		super(id);
		this.heterogeneous=heterogeneous;
		pr = PropertiesReport.calculateReport(heterogeneous);
	}



	public Heterogeneous getHeterogeneous() {
		return heterogeneous;
	}

	public void setHeterogeneous(Heterogeneous heterogeneous) {
		this.heterogeneous = heterogeneous;
	}
	

	
	String toJson(){
		Gson gson =new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create(); 
		return gson.toJson(this);
		
	}
	
}


