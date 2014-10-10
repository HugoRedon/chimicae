package bookexamples;

import java.util.HashSet;
import java.util.Set;

import termo.Constants;
import termo.activityModel.NRTLActivityModel;
import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.eos.Cubic;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.MixingRules;
import termo.matter.HeterogeneousMixture;
import chimicae.AvailableCompounds;

public class IPropaneWaterWS extends BookExample {

	public IPropaneWaterWS(AvailableCompounds availableCompounds) {
		super(availableCompounds,files(
				"/data/2propanolWater/2propanolWater_353_liquid.txt",
				"/data/2propanolWater/2propanolWater_353_vapor.txt"));
	}

	
	public void createCompoundsAndMixture() {
		System.out.println("available" + availableCompounds);
		referenceCompound = availableCompounds.getCompoundByExactName("isopropanol");
		
		referenceCompound.setK_StryjekAndVera(0.23264);
		nonReferenceCompound = availableCompounds.getCompoundByExactName("water");
		nonReferenceCompound.setK_StryjekAndVera(-0.06635);
		
		Set<Compound> components = new HashSet<>();
		
		components.add(referenceCompound);
		components.add(nonReferenceCompound);
		
		Cubic equationOfState = EquationsOfState .pengRobinson();
		NRTLActivityModel nrtl = new NRTLActivityModel();
		
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		
		k.getAlpha().setSymmetric(true);
		k.getAlpha().setValue(referenceCompound, nonReferenceCompound, 0.2529);
		
		k.getA().setValue(referenceCompound, nonReferenceCompound, 0.1562*Constants.R*353);
		k.getA().setValue(nonReferenceCompound, referenceCompound, 2.7548*Constants.R*353);
		
		k.getK().setSymmetric(true);
		k.getK().setValue(referenceCompound, nonReferenceCompound, 0.2529);
		
		hm = new HeterogeneousMixture(equationOfState,
				Alphas.getStryjekAndVeraExpression(),
				MixingRules.wongSandler(nrtl, equationOfState), 
				components, k);
		
		hm.setTemperature(353);
		
		
	}

}
