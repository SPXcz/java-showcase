/**
 * @author Ondrej Chudacek (221548)
 * 
 * Trida urcena pro praci se studenty technickeho oboru.
 */

public class Technicky extends Student{
	
	private static int pocetTechnicky = 0;

	/**
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijmeni studenta
	 * @param int rokNarozeni - rok narozeni studenta
	 * 
	 * Konstrukntor, ktery se vola pri manualnim pridani studenta (pres UI)
	 * Unikatni ID se automaticky inkrementuje. 
	 * Stejne tak pocet studentu v technickem oboru.
	 */
	public Technicky(String jmeno, String prijmeni, int rokNarozeni) {
		super(jmeno, prijmeni, rokNarozeni);
		Technicky.pocetTechnicky++;
	}
	/**
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijmeni studenta
	 * @param int rokNarozeni - rok narozeni studenta
	 * @param int id - unikatni ID studenta. Uzivatel pri pouziti konstruktoru musi davat pozor, aby nedoslo ke kolizi.
	 * 
	 * Konstrukntor, ktery se vola pri vyplnovani instance daty z databaze. 
	 * ID je urceno podle ID z databaze.
	 * Pocet studentu v technickem oboru se inkrementuje.
	 */
	public Technicky(String jmeno, String prijmeni, int rokNarozeni, int id) {
		super(jmeno, prijmeni, rokNarozeni, id);
		Technicky.pocetTechnicky++;
	}
	/**
	 * @param String vypisDovednosti - vypisuje vyrok studenta o prestupnosti jeho roku narozeni
	 * 
	 * Vypise prestupnost roku narozeni studenta. 
	 * Prestupny rok je takovy, ktery je deliteny 4, neni delitelny 100, pokud neni delitelny 400.
	 */
	@Override
	public String vypisDovednosti() {
		if((super.getRokNarozeni() % 4) == 0 && (((super.getRokNarozeni() % 100) != 0) || (super.getRokNarozeni() % 400) == 0))
		{
			return "rok narozeni je prestupny";
		}else return "rok narozeni neni prestupny";
	}
	
	/**
	 * Vraci pocet pocet studentu technickeho oboru.
	 * 
	 * @return int pocetTechnickych - pocet studentu technickeho oboru
	 */
	public static int getPocetTechnicky()
	{
		return Technicky.pocetTechnicky;
	}
}
