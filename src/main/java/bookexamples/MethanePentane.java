package bookexamples;

import java.util.HashSet;
import java.util.Set;

import chimicae.AvailableCompounds;
import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.MixingRules;
import termo.matter.HeterogeneousMixture;

public class MethanePentane extends BookExample{
	
	
	
	public MethanePentane(AvailableCompounds availableCompounds) {		
		super(availableCompounds,
				"/data/methaneandnpentane/methaneandnpentane_310_liquid.txt",
				"/data/methaneandnpentane/methaneandnpentane_310_vapor.txt");
	}

	@Override
	public void createCompoundsAndMixture(){
		referenceCompound = availableCompounds.getCompoundByExactName("methane");
		referenceCompound.setK_StryjekAndVera(-0.00159);
		
		//printProperties(referenceCompound);
		nonReferenceCompound = availableCompounds.getCompoundByExactName("n-pentane");
		nonReferenceCompound.setK_StryjekAndVera(0.03946);
		
		//printProperties(nonReferenceCompound);
		Set<Compound> compounds = new HashSet<>();
		compounds.add(referenceCompound);
		compounds.add(nonReferenceCompound);
		
		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
		k.setSymmetric(true);
		k.setValue(referenceCompound, nonReferenceCompound, 0.0215);
		
		 hm = new HeterogeneousMixture(EquationsOfState.pengRobinson(),
				Alphas.getStryjekAndVeraExpression(),
				MixingRules.vanDerWaals(),
				compounds, k);
		 hm.setTemperature(310);
		
	}
}
