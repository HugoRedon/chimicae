package compoundreader;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import termo.component.Compound;
import termo.cp.CpEquation;

public class CompoundReaderTest {

	@Test
	public void test() {
		CompoundReader reader = new CompoundReader();
		Compound compound = reader.getCompoundByExactName("Water");		
		assertEquals(true,(compound!=null));
	}
	@Test
	public void testcp() {
		CompoundReader reader = new CompoundReader();
		Compound compound = reader.getCompoundByExactName("Water");
		CpEquation cp =  compound.getCp();
		boolean isfinit =Double.isFinite(cp.cp(300));
		
		assertEquals(true,isfinit);
	}

}
