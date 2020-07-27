import static org.junit.Assert.*;

import org.junit.Test;

public class StudijniPrumerUnitTest {

	@Test
	public void test() {
		Technicky test = new Technicky("a", "b", 1999);
		float prazdnyIndex = test.getPrumer();
		assertEquals(0, prazdnyIndex, 0.0f);
		
		test.novaZnamka(1);
		test.novaZnamka(2);
		test.novaZnamka(3);
		
		float prumer = test.getPrumer();
		assertEquals(2, prumer, 0.0f);
	}

}
