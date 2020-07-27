/**
 * @author Ondrej Chudacek (221548)
 * 
 * Trida urcena pro praci se studenty kombinovaneho oboru.
 * Trida dedi vlastnosti rodice "Humanitni".
 */

public class Kombinovany extends Humanitni{
	
	private static int pocetKombinovany = 0;
	
	/**
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijemni studenta
	 * @param int rokNarozeni - rok narozeni studenta
	 * @param int mesic - mesic narozeni studenta
	 * @param int den - den v mesici narozeni studenta
	 * 
	 * Konstrukntor, ktery se vola pri manualnim pridani studenta (pres UI)
	 * Unikatni ID se automaticky inkrementuje. 
	 * Stejne tak se inkrementuje i mnozsvti studentu studujicich kombinovany obor.
	 */
	public Kombinovany(String jmeno, String prijmeni, int rokNarozeni, int mesic, int den) {
		super(jmeno, prijmeni, rokNarozeni, mesic, den);
		Kombinovany.pocetKombinovany++;
	}
	
	/**
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijemni studenta
	 * @param int rokNarozeni - rok narozeni studenta
	 * @param int mesic - mesic narozeni studenta
	 * @param int den - den v mesici narozeni studenta
	 * @param id - unikatni ID studenta.
	 * 
	 * Konstrukntor, ktery se vola pri vyplnovani instance daty z databaze.
	 * ID je urceno podle ID z databaze.
	 */
	public Kombinovany(String jmeno, String prijmeni, int rokNarozeni, int mesic, int den, int id) {
		super(jmeno, prijmeni, rokNarozeni, mesic, den, id);
		Kombinovany.pocetKombinovany++;
	}
	
	/**
	 * Metoda je kombinaci volani metody rodice (super), ktera vraci znameni studenta a implementaci shodne funkce, 
	 * ktera vraci ujestli je rok narozeni studenta prestupny z objektu technickeho studenta.
	 * 
	 * @return String dovednost - vyrok studenta se svym znamenim a jestli je jeho rok narozeni prestupny
	 */
	public String vypisDovednosti() {
		if((super.getRokNarozeni() % 4) == 0 && (((super.getRokNarozeni() % 100) != 0) || (super.getRokNarozeni() % 400) == 0))
		{
			return "rok narozeni je prestupny a "+super.vypisDovednosti();
		}else return "rok narozeni neni prestupny a "+super.vypisDovednosti();
	}
	
	/**
	 * @return int pocetKombinovany - pocet studentu studujici kombinovany obor
	 */
	public static int getPocetKombinovany()
	{
		return Kombinovany.pocetKombinovany;
	}
	
}
