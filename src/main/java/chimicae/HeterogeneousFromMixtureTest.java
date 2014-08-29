package chimicae;

import org.junit.Test;

import termo.binaryParameter.InteractionParameter;
import termo.component.Compound;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alphas;
import termo.eos.mixingRule.VDWMixingRule;
import termo.matter.HeterogeneousMixture;
import termo.matter.Mixture;
import termo.matter.builder.HeterogeneousMixtureBuilder;
import termo.matter.builder.MixtureBuilder;
import termo.phase.Phase;

import compoundreader.CompoundReader;

public class HeterogeneousFromMixtureTest {

	@Test
	public void test() {
		CompoundReader reader = new CompoundReader();
		Compound water =reader.getCompoundByExactName("Water");
		Compound methanol = reader.getCompoundByExactName("Methanol");
		
		Mixture mix = new MixtureBuilder().setEquationOfState(EquationsOfState.pengRobinson())
				.setAlpha(Alphas.getStryjekAndVeraExpression())
				.addCompounds(water,methanol).setInteractionParameter(new InteractionParameter())
				.setPhase(Phase.VAPOR).setMixingRule(new VDWMixingRule()).build();
		
		HeterogeneousMixture hm=new HeterogeneousMixtureBuilder().fromMixture(mix);
	}

}
