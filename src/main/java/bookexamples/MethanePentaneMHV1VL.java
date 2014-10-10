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

public class MethanePentaneMHV1VL extends BookExample {
//	public MethanePentaneMHV1VL(AvailableCompounds availableCompounds) {
//		super(availableCompounds);
//		createLists(files("/data/methaneandnpentane/methaneandnpentane_377_liquid.txt",
//				//"/data/methaneandnpentane/methaneandnpentane_444_liquid.txt",
//				"/data/methaneandnpentane/methaneandnpentane_310_liquid.txt"
//				));
//		createLists(files("/data/methaneandnpentane/methaneandnpentane_310_vapor.txt",
//				"/data/methaneandnpentane/methaneandnpentane_377_vapor.txt"
//				//"/data/methaneandnpentane/methaneandnpentane_444_vapor.txt"
//				)
//				,true,true,150);
//		createCompoundsAndMixture();
//	}
//
//	@Override
//	public void createCompoundsAndMixture() {
//		
//		referenceCompound = availableCompounds.getCompoundByExactName("methane");
//		referenceCompound.setK_StryjekAndVera(-0.00159);
//		
//		//printProperties(referenceCompound);
//		nonReferenceCompound = availableCompounds.getCompoundByExactName("n-pentane");
//		nonReferenceCompound.setK_StryjekAndVera(0.03946);
//		
//		//printProperties(nonReferenceCompound);
//		Set<Compound> compounds = new HashSet<>();
//		compounds.add(referenceCompound);
//		compounds.add(nonReferenceCompound);
//		
//		ActivityModelBinaryParameter k = new ActivityModelBinaryParameter();
//		
//		k.getA_vanLaar().setValue(referenceCompound, nonReferenceCompound, -0.428);
//		k.getA_vanLaar().setValue(nonReferenceCompound, referenceCompound, -0.632);
//		
//		Cubic eos = EquationsOfState.pengRobinson();
//		 hm = new HeterogeneousMixture(eos,
//				Alphas.getStryjekAndVeraExpression(),
//				MixingRules.ModifiedHuronVidalFirstOrderMixingRule(new VanLaarActivityModel(), eos),
//				compounds, k);
//		 hm.setTemperature(310);
//	}
}
