import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * Trida vylozene nepracuje s databazi, ale obsahuje konecne metody pro praci se studenty skoly.
 * Jedna se o jakysi vystup teto prace, na ktery muze navazat dalsi programmator.
 * Trida obsahuje atributy: Ukladajici studenty do mapy s klicem ve forme jejich unikatni ID (ID zacina od 1)
 * 							Instanci tridy pro praci s SQL databazi
 * 							Boolean ucujici, jestli je uzivatel prihlasen do databaze
 * 
 * @author Ondrej Chudacek (221548)
 */

public class Databaze{
	
	private HashMap<Integer, Student> studenti; //ID zacina od 1
	private DBOperations db;
	private boolean isLoggedIn;
	
	/**
	 * Konstruktor databze.
	 * Vytvari HashMap studentu a navazuje *pocatecni* spojeni s SQL databazi.
	 */
	public Databaze()
	{
		this.studenti = new HashMap<Integer, Student>();
		this.connectDB();
	}
	
	/**
	 * @return DBOperations db - vraci pristup k SQL databazi.
	 */
	public DBOperations getDb() {
		return db;
	}

	/**
	 * Vytvari instanci studenta technickeho oboru a pokud neni jeste v databazi, vlozi ho do SQL databaze.
	 * Nemuze byt vlozen student s duplikatnim klicem.
	 * 
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijmeni studenta
	 * @param int rok - rok narozeni studenta
	 * @param int id - unikatni ID studenta, pokud ho chcete nechat vygenerovat, zadejte do hlavy null
	 */
	public void addTechnicky(String jmeno, String prijmeni, int rok, int ...id)
	{
		if(id == null)
		{
			studenti.put(Student.getPocetStudentu()+1, new Technicky(jmeno, prijmeni, rok));
			this.db.ulozStudenta(this.studenti.get(Student.getPocetStudentu()));
		}else if(!this.studenti.containsKey(id))
		{
			studenti.put(id[0], new Technicky(jmeno, prijmeni, rok, id[0]));
		}else System.out.println("Duplikatni klic");
	}
	
	/**
	 * Vytvari instanci studenta humanitniho oboru a pokud neni jeste v databazi, vlozi ho do SQL databaze.
	 * Nemuze byt vlozen student s duplikatnim klicem.
	 * 
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijmeni studenta
	 * @param int rok - rok narozeni studenta
	 * @param int mesic - mesic narozeni studenta
	 * @param int den - den v mesici narozeni studenta
	 * @param int id - unikatni ID studenta, pokud ho chcete nechat vygenerovat, zadejte do hlavy null
	 */
	public void addHumanitni(String jmeno, String prijmeni, int rok, int mesic, int den, int ...id)
	{
		if(id == null)
		{
			studenti.put(Student.getPocetStudentu()+1, new Humanitni(jmeno, prijmeni, rok, mesic, den));
			this.db.ulozStudenta(this.studenti.get(Student.getPocetStudentu()));
		}else if(!this.studenti.containsKey(id))
		{
			studenti.put(id[0], new Humanitni(jmeno, prijmeni, rok, mesic, den, id[0]));
		}else System.out.println("Duplikatni klic");
	}
	
	/**
	 * Vytvari instanci studenta kombinovaneho oboru a pokud neni jeste v databazi, vlozi ho do SQL databaze.
	 * Nemuze byt vlozen student s duplikatnim klicem.
	 * 
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijmeni studenta
	 * @param int rok - rok narozeni studenta
	 * @param int mesic - mesic narozeni studenta
	 * @param int den - den v mesici narozeni studenta
	 * @param int id - unikatni ID studenta, pokud ho chcete nechat vygenerovat, zadejte do hlavy null
	 */
	public void addKombinovany(String jmeno, String prijmeni, int rok, int mesic, int den, int ...id)
	{
		if(id == null)
		{
			studenti.put(Student.getPocetStudentu()+1, new Kombinovany(jmeno, prijmeni, rok, mesic, den));
			this.db.ulozStudenta(this.studenti.get(Student.getPocetStudentu()));
		}else if(!this.studenti.containsKey(id))
		{
			studenti.put(id[0], new Kombinovany(jmeno, prijmeni, rok, mesic, den, id[0]));
		}else System.out.println("Duplikatni klic");
	}
	
	/**
	 * Metoda pridava znamku studentovi se zadanym ID. Znamka musi byt z rozsahu 1 az 5.
	 * Pokud je znamka pridavana pres UI, bude znamka take zapsana do SQL databaze.
	 * 
	 * @param int id - unikatni ID studenta
	 * @param int znamka - znamka, ktera ma byt vlozena do indexu studenta
	 * @param boolean nova - pokud znamka v SQL databazi existuje a znamka se z ni nacita, bude hodnota 'false'. Pokud se zadava nova znamka, bude hodnota 'true'.
	 */
	public void addZnamka(int id, int znamka, boolean nova)
	{
		if(!studenti.containsKey(id)) return;
		if(znamka >= 1 && znamka <= 5)
		{
			if(nova) {
				studenti.get(id).novaZnamka(znamka);
				this.db.ulozZnamku(znamka, id);
			}else studenti.get(id).novaZnamka(znamka);
		}else System.out.println("Znammka neni z rozsahu 1-5!");
	}
	
	/**
	 * Metoda smaze instanci studenta s id z HashMap databaze se vsemi studenty i SQL databaze.
	 * 
	 * @param int id - unikatni ID studenta k propusteni
	 */
	public void propustit(int id)
	{
		if(!studenti.containsKey(id)) return;
		studenti.remove(id);
		this.db.deleteStudent(id);
	}
	
	/**
	 * Metoda vypise jmeno, prijmeni, rok narozeni a studijni prumer studenta s id.
	 * 
	 * @param int id - unikatni ID studenta k vypsani jeho atributu
	 */
	public void vypisJednotlivce(int id)
	{
		if(!studenti.containsKey(id)) return;
		System.out.println(studenti.get(id).getJmeno()+" "+studenti.get(id).getPrijmeni()+" Rok narozeni: "+studenti.get(id).getRokNarozeni()+" Prumer: "+studenti.get(id).getPrumer());
	}
	
	/**
	 * Vypise vyrok s dovednosti studenta.
	 * Student technického studia vypise, jestli je jeho/jeji rok narozeni prestupny.
	 * Student humanitniho oboru vypise svoje znameni.
	 * Student kombinovaneho studia udela oboje.
	 * 
	 * @param int id - unikatni ID studenta k vypsanio jeho dovednosti
	 */
	public void vypisDovednost(int id)
	{
		if(!studenti.containsKey(id)) return;
		System.out.println(studenti.get(id).vypisDovednosti());
	}
	
	/**
	 * Metoda seradi studenty do jejich oboru (ArrayListu) a abecedne seradene je pak vypise.
	 */
	public void vypisVse()
	{
		ArrayList<Student> tmpTech = new ArrayList<>();
		ArrayList<Student> tmpHum = new ArrayList<>();
		ArrayList<Student> tmpKom = new ArrayList<>();
		
		for(Map.Entry<Integer,Student> student : studenti.entrySet()) //iterator
		{
			if(student.getValue() instanceof Technicky)
			{
				tmpTech.add(student.getValue());
			}else if(student.getValue() instanceof Kombinovany)
			{
				tmpKom.add(student.getValue());
			}else
			{
				tmpHum.add(student.getValue());
			}
		}
		
		tmpTech.sort(null);
		tmpHum.sort(null);
		tmpKom.sort(null);
		
		System.out.println(" ");
		System.out.println("Technicti:");
		for(Student student : tmpTech)
		{
			this.vypisJednotlivce(student.getId());
		}
		
		System.out.println(" ");
		System.out.println("Humanitni:");
		for(Student student : tmpHum)
		{
			this.vypisJednotlivce(student.getId());
		}
		
		System.out.println(" ");
		System.out.println("Kombinovani:");
		for(Student student : tmpKom)
		{
			this.vypisJednotlivce(student.getId());
		}
	}
	
	/**
	 * Metoda vypise celkovy prumer pro humanitni a technicky obor.
	 * Prumer je pocitan z aritmetickych prumeru studentu, ne ze vsech znamek.
	 * Pokud zadny student v databazi neni, zobrazi se chybova zprava.
	 * Pokud pouze jeden obor nema zadne studenty, prumer bude 0.0
	 * Pokud samotny student nema zadnou znamku, nebude do prumeru vubec pocitan.
	 */
	public void vypisCelkovyPrumer()
	{
		float sumaHum = 0;
		float sumaTech = 0;
		int nicHum = 0;
		int nicTech = 0;
		
		if(Student.getPocetStudentu() == 0)
		{
			System.out.println("Zadni studenti v databazi");
		}
		
		for(Map.Entry<Integer,Student> student : studenti.entrySet()) //iterator
		{
			if(student.getValue() instanceof Humanitni && !(student.getValue() instanceof Kombinovany))
			{
				if(student.getValue().getPrumer() != 0)
					{
							sumaHum = student.getValue().getPrumer() + sumaHum;
					}else nicHum++;
			}else if(student.getValue() instanceof Technicky)
			{
				if(student.getValue().getPrumer() != 0) 
					{
						sumaTech = student.getValue().getPrumer() + sumaTech;
					}else nicTech++;
			}
		}
		
		if(sumaTech == 0) nicTech--;
		if(sumaHum == 0) nicHum--;
		
		System.out.println("Prumer pro Humanitni: " + sumaHum/(Humanitni.getPocetHumanitni()-nicHum) + " Prumer pro Technicky: " + sumaTech/(Technicky.getPocetTechnicky()-nicTech));
	}
	/**
	 * Metoda vypise pocty studentu v jednotlivych oborech.
	 * Pocty jsou zjistovany pomoci internich pocitadel.
	 */
	public void pocetVsechStudentu()
	{
		System.out.println("Technicky: " + Technicky.getPocetTechnicky()+" Humanitni: " + Humanitni.getPocetHumanitni() + " Kombinovany: "+ Kombinovany.getPocetKombinovany());
	}
	
	/**
	 * Metoda nejdrive nacte jmeno a heslo ze souboru a zkusi se prihlasit do databaze.
	 * Pokud se podari prihlasit, nactou se data z datbaze a isLoggedIn se na stavi na true.
	 * Pokud se nepodari prihlasit, vypise se chybova zprava a vrati se false.
	 * 
	 * @return true - pokud byl uzivatel prihlasen, false - pokud se nepodarilo prihlasit
	 */
	public boolean connectDB()
	{	
		try
		{
			FileReader fr;
			BufferedReader in;
			fr = new FileReader("login.txt");
			in = new BufferedReader(fr);
			String jmeno = in.readLine();
			String heslo = in.readLine();
			in.close();
			fr.close();
			System.out.println(jmeno);
			this.db = new DBOperations();
			
			if(this.db.login(jmeno, heslo))
			{
				this.isLoggedIn = true;
				System.out.println("Byl jste prihlasen!");
				this.nactiDB();
				return true;
			}else
				{
					this.isLoggedIn = false;
					System.out.println("Prihlaseni bylo neuspesne!");
					this.db = null;
					return false;
				}
		}
		catch (IOException e) {
			System.out.println("Soubor pro cteni prihlasovacich udaju nejde nacist");
			return false;
		}
	}
	
	/**
	 * Metoda nacte vsechna data z datbaze do jednotlivych instaci studentu.
	 * Nejdrive se nactou studenti (podle jednotlivych oboru), pak jejich indexy.
	 * Jelikoz se v teto metode pracuje s SQL databzi samotnou (pres ResultSet), je potreba i otevrit a zavrit spojeni s SQL databazi.
	 * Take z tohoto duvodu muze dojit chybe na strane SQL. Toto je ovsem osetreno. 
	 */
	public void nactiDB()
	{
		db.connectDB();
		try
		{
			if(this.db != null)
			{
				ResultSet rsStudenti = this.db.loadDB("SELECT * FROM studenti;");
				ResultSet rsIndex = this.db.loadDB("SELECT * FROM znamky;");
				
				if(rsStudenti != null)
				{
					while(rsStudenti.next())
					{
						switch(rsStudenti.getInt("obor"))
						{
							case 1:
								this.addTechnicky(rsStudenti.getString("jmeno"), rsStudenti.getString("prijmeni"), rsStudenti.getInt("rok"), rsStudenti.getInt("id"));
								break;
							case 2:
								this.addHumanitni(rsStudenti.getString("jmeno"), rsStudenti.getString("prijmeni"), rsStudenti.getInt("rok"), rsStudenti.getInt("mesic"), rsStudenti.getInt("den"), rsStudenti.getInt("id"));
								break;
							case 3:
								this.addKombinovany(rsStudenti.getString("jmeno"), rsStudenti.getString("prijmeni"), rsStudenti.getInt("rok"), rsStudenti.getInt("mesic"), rsStudenti.getInt("den"), rsStudenti.getInt("id"));
								break;
						}
					}
					
				}else System.out.println("Zadny student v databazi.");
				
				while(rsIndex.next())
				{
					this.addZnamka(rsIndex.getInt("id_studenti"), rsIndex.getInt("znamka"), false);
				}
				
				rsStudenti.close();
				rsIndex.close();
				db.disconnectDB();
			}else {System.out.println("Nejste prihlasen!");
					db.disconnectDB();}
		}catch(SQLException e)
		{
			db.disconnectDB();
	        System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Metoda nacte konkretniho studenta z databaze a ulozi jej jako instanci objektu Student.
	 * Jelikoz se cela SQL databze nacita pri vytvoreni instance objektu Databaze, realne nema tato metoda zadne vyuziti.
	 * Opet se zde pracuje s SQL databazi a proto se navazuje a ukoncuje spojeni. 
	 * Metoda je osetrena proti chybe na strane SQL.
	 * 
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijmeni studenta
	 */
	public void nactiPodleJmena(String jmeno, String prijmeni)
	{
		db.connectDB();
		try
		{
			ResultSet rs = this.db.loadExactStudent(jmeno, prijmeni);
			if(rs != null)
			{
				switch(rs.getInt("obor"))
				{
					case 1:
						this.addTechnicky(rs.getString("jmeno"), rs.getString("prijmeni"), rs.getInt("rok"), rs.getInt("id"));
						break;
					case 2:
						this.addHumanitni(rs.getString("jmeno"), rs.getString("prijmeni"), rs.getInt("rok"), rs.getInt("mesic"), rs.getInt("den"), rs.getInt("id"));
						break;
					case 3:
						this.addKombinovany(rs.getString("jmeno"), rs.getString("prijmeni"), rs.getInt("rok"), rs.getInt("mesic"), rs.getInt("den"), rs.getInt("id"));
						break;
				}
				db.disconnectDB();
			}else {System.out.println("Student nebyl nalezen");
					db.disconnectDB();}
		}catch(SQLException e)
		{
			db.disconnectDB();
	        System.out.println(e.getMessage());
		}
	}
	
	/**
	 * @return boolean isLoggedIn - vraci jestli je uzivatel prihlasen do SQL databaze.
	 */
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
}
