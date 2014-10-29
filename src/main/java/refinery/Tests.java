package refinery;

import org.junit.Test;

import termo.component.Compound;
import termo.eos.EquationsOfState;
import termo.eos.alpha.Alphas;
import termo.matter.HeterogeneousSubstance;

import com.google.gson.Gson;

public class Tests {

	@Test
	public void test(){
		HeterogeneousSubstance sub = new HeterogeneousSubstance(EquationsOfState.pengRobinson(), Alphas.getStryjekAndVeraExpression(), new Compound("some"));
		String json = new Gson().toJson(sub);
		System.out.println(json);
	}
}
