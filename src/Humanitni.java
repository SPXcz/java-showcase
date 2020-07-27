/**
 * @author Ondrej Chudacek (221548)
 * 
 * Trida urcena pro praci se studenty humanithiho oboru.
 */

public class Humanitni extends Student{

	private final static String ZNAMENI[] = {"kozoroh", "vodnar", "ryby", "beran", "byk", "blizenci", "rak", "lev", "panna", "vahy", "stir", "strelec"};
	private int mesic;
	private int den;
	private static int pocetHumanitni = 0;
	
	/**
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijemni studenta
	 * @param int rokNarozeni - rok narozeni studenta
	 * @param int mesic - mesic narozeni studenta
	 * @param int den - den v mesici narozeni studenta
	 * 
	 * Konstrukntor, ktery se vola pri manualnim pridani studenta (pres UI)
	 * Unikatni ID se automaticky inkrementuje. 
	 * Stejne tak se inkrementuje i mnozsvti studentu studujicich humanitni obor.
	 */
	public Humanitni(String jmeno, String prijmeni, int rokNarozeni, int mesic, int den) {
		super(jmeno, prijmeni, rokNarozeni);
		Humanitni.pocetHumanitni++;
		this.mesic = mesic;
		this.den = den;
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
	public Humanitni(String jmeno, String prijmeni, int rokNarozeni, int mesic, int den, int id) {
		super(jmeno, prijmeni, rokNarozeni, id);
		Humanitni.pocetHumanitni++;
		this.mesic = mesic;
		this.den = den;
	}
	
	/**
	 * Vraci mesic narozeni studenta.
	 * 
	 * @return int mesic - mesic narozeni studenta
	 */
	public int getMesic() {
		return mesic;
	}

	/**
	 * Vraci den v mesici narozeni studenta.
	 * 
	 * @return int den - den v mesici narozeni studenta
	 */
	public int getDen() {
		return den;
	}
	
	/**
	 * Funkce ze statickeho pole najde podle data narouzeni studenta jeho/jeji znameni.
	 * Jeloikoz jsou znameni matematicky rozlisna, je potreba k identifikaci vice podminek (porovnavani velikosti nebo delitelnost).
	 * Kazdy mesic je pak prakticky rozdelen na dve casti (zadne znameni nezasahuje do vidce nez dvou mesicu).
	 * 
	 * @return String znameni - vyrok studenta, ve kterem vyrkne svoje znameni.
	 */
	@Override
	public String vypisDovednosti() {
		
		if(this.mesic <= 4)
		{
			if(this.den <= 20)
			{
				return "Jsem " + Humanitni.ZNAMENI[this.mesic-1];
			}else return "Jsem " + Humanitni.ZNAMENI[this.mesic];
		}else
		if(this.mesic == 5 || this.mesic % 6 == 0)
		{
			if(this.den <= 21)
			{
				return "Jsem " + Humanitni.ZNAMENI[this.mesic-1];
			}else return "Jsem " + Humanitni.ZNAMENI[this.mesic];
		}else
		if(this.mesic != 10)
		{
			if(this.den <= 22)
			{
				return "Jsem " + Humanitni.ZNAMENI[this.mesic-1];
			}else return "Jsem " + Humanitni.ZNAMENI[this.mesic];
		}else
		{
			if(this.den <= 23)
			{
				return "Jsem " + Humanitni.ZNAMENI[this.mesic-1];
			}else return "Jsem " + Humanitni.ZNAMENI[this.mesic];
		}
		
	}
	
	/**
	 * Vraci pocet studentu v humanitnim obotu.
	 * Jelikoz je kombinavany obor potomkem humanitniho, je inkrementovan pocet studentu v obou oborech
	 * pri volani konstruktoru kombinovaneho oboru. Proto je jeste potreba odecist pocet studentu kombinovaneho oboru.
	 * 
	 * @return int pocetStudentu - pocet studentu humanitniho studia
	 */
	public static int getPocetHumanitni()
	{
		return Humanitni.pocetHumanitni-Kombinovany.getPocetKombinovany();
	}
	
	
}
