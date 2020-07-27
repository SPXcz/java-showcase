import java.util.HashSet;

/**
 * @author Ondrej Chudacek (221548)
 * 
 * Abstraktni trida udavajici vlastnosti a mantinely pro potomky - obory univerzity.
 */

public abstract class Student implements Comparable<Student>{

	private int id;
	private String jmeno;
	private String prijmeni;
	private int rokNarozeni;
	private HashSet<Integer> index;
	private static int pocetStudentu = 0;
	
	/**
	 * Vraci index (HashSet znamek)
	 * @return Hashset index
	 */
	public HashSet<Integer> getIndex() {
		return index;
	}

	/**
	 * Vraci unikatni ID studenta
	 * @return int id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Vraci krestni jmeno studenta
	 * @return String jmeno
	 */
	public String getJmeno() {
		return jmeno;
	}

	/**
	 * Vraci prijmeni studenta
	 * @return String prijmeni
	 */
	public String getPrijmeni() {
		return prijmeni;
	}

	/**
	 * Vraci rok naroyeni studenta
	 * @return int rokNarozeni
	 */
	public int getRokNarozeni() {
		return rokNarozeni;
	}

	/**
	 * Vraci pocet studentu celkem (staticka funkce)
	 * @return int Student.pocetStudentu
	 */
	public static int getPocetStudentu() {
		return Student.pocetStudentu;
	}

	/**
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijmeni studenta
	 * @param int rokNaroyeni - rok narozeni studenta
	 * 
	 * Konstrukntor, ktery se vola pri manualnim pridani studenta (pres UI)
	 * Unikatni ID se automaticky inkrementuje.
	 */
	public Student(String jmeno, String prijmeni, int rokNarozeni)
	{
		this.id = ++Student.pocetStudentu;
		this.jmeno = jmeno;
		this.prijmeni = prijmeni;
		this.rokNarozeni = rokNarozeni;
		this.index = new HashSet<Integer>();
	}
	
	/**
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijmeni studenta
	 * @param int rokNarozeni - rok narozeni studenta
	 * @param int id - unikatni ID studenta. Uzivatel pri pouziti konstruktoru musi davat pozor, aby nedoslo ke kolizi.
	 * 
	 * Konstrukntor, ktery se vola pri vyplnovani instance daty z databaze. 
	 * ID je urceno podle ID z databaze.
	 */
	public Student(String jmeno, String prijmeni, int rokNarozeni, int id)
	{
		this.id = id;
		this.jmeno = jmeno;
		this.prijmeni = prijmeni;
		this.rokNarozeni = rokNarozeni;
		this.index = new HashSet<Integer>();
		Student.pocetStudentu++;
	}
	
	/**
	 * @param int znamka - nova znamka k pridani do HashSetu
	 * 
	 * Pridava znamku do indexu.
	 */
	public void novaZnamka(int znamka)
	{
		this.index.add(znamka);
	}
	
	/**
	 * Vraci aritmeticky prumer studenta podle znamek z indexu.
	 * Pro studenta, ktery jeste zadnou znamku nedostal vraci 0.
	 * 
	 * @return float prumer - aritmeticky prumer studenta.
	 */
	public float getPrumer()
	{
		if(this.index.size() == 0) return 0;
		
		float sum = 0;
		
		for(Integer znamka : this.index)
		{
			sum = sum + (float)znamka;
		}
		
		return sum/(float)this.index.size();
	}
	
	/**
	 * Komparator pro instance tridy Student.
	 * Porovnava prijemni studentu podle abecedy (A-Z).
	 */
	@Override
	public int compareTo(Student o) {
		// TODO Auto-generated method stub
		return this.getPrijmeni().compareTo(o.getPrijmeni());
	}
	/**
	 * Vraci schopnost studenta.
	 * 
	 * @return String dovednost - vyrok studenta ohledne sebe v ramci jeho/jeji schopnosti.
	 */
	abstract String vypisDovednosti();
}
