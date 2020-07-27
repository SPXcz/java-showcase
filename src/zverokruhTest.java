import static org.junit.Assert.*;

import org.junit.Test;

public class zverokruhTest {

	@Test
	public void test() {
		Humanitni test = new Humanitni("a", "b", 1999, 3, 19);
		assertEquals("Jsem ryby", test.vypisDovednosti());
		Humanitni test2 = new Humanitni("a", "b", 1999, 5, 21);
		assertEquals("Jsem byk", test2.vypisDovednosti());
		Humanitni test3 = new Humanitni("a", "b", 1999, 10, 23);
		assertEquals("Jsem vahy", test3.vypisDovednosti());
	}

}
