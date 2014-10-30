package refinery;

import termo.matter.Heterogeneous;
import termo.matter.HeterogeneousMixture;
import termo.matter.HeterogeneousSubstance;
import termo.matter.builder.HeterogeneousMixtureBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class HeatExchangerEquipment extends Equipment{
	@Expose
	double heatLoad;
	@Expose
	PropertiesReport pr;
	
	
	Heterogeneous inFluid;
	Heterogeneous outFluid;
	public HeatExchangerEquipment(long id,Tank previousEquipment,double newTemperature) {
		super(id);
		inFluid = previousEquipment.getHeterogeneous();
		outFluid= createFrom(inFluid);
		outFluid.setTemperature(newTemperature);
		outFluid.calculateVF();
		
		heatLoad = outFluid.calculateEnthalpy()-previousEquipment.getHeterogeneous().calculateEnthalpy();
		pr = PropertiesReport.calculateReport(outFluid);
		System.out.println("heatload: " + heatLoad);
	}
	public String toJson(){
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}
	public Heterogeneous createFrom(Heterogeneous ht){
		Heterogeneous result =null;
		if(ht instanceof HeterogeneousSubstance){
			result = new HeterogeneousSubstance(ht.getLiquid().getCubicEquationOfState(), ((HeterogeneousSubstance) ht).getAlpha()	, ((HeterogeneousSubstance) ht).getComponent());
		}else if (ht instanceof HeterogeneousMixture){
			result = new HeterogeneousMixtureBuilder().fromHeterogeneousMixture((HeterogeneousMixture)ht);
		}
		return result;
				
	}
	
	
}
