package bookexamples;

import java.util.HashSet;
import java.util.Set;

import termo.activityModel.VanLaarActivityModel;
import termo.binaryParameter.ActivityModelBinaryParameter;
import termo.component.Compound;
import termo.eos.Cubic;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.MixingRules;
import termo.matter.HeterogeneousMixture;
import chimicae.AvailableCompounds;

public class MethanePentaneHVVanLaar extends BookExample{

	public MethanePentaneHVVanLaar(AvailableCompounds availableCompounds) {
		super(availableCompounds,files(
				"/data/methaneandnpentane/methaneandnpentane_310_liquid.txt",
				"/data/methaneandnpentane/methaneandnpentane_310_vapor.txt",
				"/data/methaneandnpentane/methaneandnpentane_377_liquid.txt",
				"/data/methaneandnpentane/methaneandnpentane_377_vapor.txt",
				"/data/methaneandnpentane/methaneandnpentane_444_liquid.txt",
				"/data/methaneandnpentane/methaneandnpentane_444_vapor.txt"
				));
	}

	@Override
	public void createCompoundsAndMixture() {
		
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
		
		k.getA_vanLaar().setValue(referenceCompound, nonReferenceCompound, 0.1201);
		k.getA_vanLaar().setValue(nonReferenceCompound, referenceCompound, 0.1430);
		
		Cubic eos = EquationsOfState.pengRobinson();
		 hm = new HeterogeneousMixture(eos,
				Alphas.getStryjekAndVeraExpression(),
				MixingRules.huronVidal(new VanLaarActivityModel(), eos),
				compounds, k);
		 hm.setTemperature(310);
	}

}
