import java.util.Scanner;

/**
 * Trida urcena pro spusteni programu.
 * 
 * @author Ondrej Chudacek (221548)
 */

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Databaze db = new Databaze();
		int choice;
		
		
	do {
		if(db.isLoggedIn())
		{
			System.out.println(" ");
			System.out.println("Vyberte akci:");
			System.out.println("1 - Pridat studenta");
			System.out.println("2 - Zadat studentovi znamku");
			System.out.println("3 - Odebrat studenta");
			System.out.println("4 - Nalezt studenta podle ID");
			System.out.println("5 - Spustit dovednost studenta podle ID");
			System.out.println("6 - Abecedne seradit a rozradit studenty podle studia");
			System.out.println("7 - Vypis studijniho prumeru podle oboru");
			System.out.println("8 - Vypis celkoveho poctu studentu podle oboru");
			System.out.println("9 - Nacist konkretniho studenta z databaze (funkce je kvuli navrhu programu nadbytecna)");
			System.out.println(">=10 - Konec");
			System.out.println(" ");
		}
		
		choice = sc.nextInt();
		
		switch(choice)
		{
			case 1:
				System.out.println("Jmeno:");
				String jmeno = sc.next();
				System.out.println("Prijmeni:");
				String prijmeni = sc.next();
				System.out.println("Rok narozeni:");
				int rok = sc.nextInt();
				System.out.println("Studijni obor: 1 - Technicky, 2 - Humanitni, 3 - Kombinovany");
				int obor = sc.nextInt();
				switch(obor)
				{
					case 1: 
						db.addTechnicky(jmeno, prijmeni, rok, null);
						break;
					case 2:
						System.out.println("Mesic narozeni:");
						int mesic = sc.nextInt();
						System.out.println("Den narozeni:");
						int den = sc.nextInt();
						db.addHumanitni(jmeno, prijmeni, rok, mesic, den, null);
						break;
					case 3:
						System.out.println("Mesic narozeni:");
						int mesic1 = sc.nextInt();
						System.out.println("Den narozeni:");
						int den1 = sc.nextInt();
						db.addKombinovany(jmeno, prijmeni, rok, mesic1, den1, null);
						break;
				}
				break;
				
			case 2:
				System.out.println("Id studenta a znamka:");
				db.addZnamka(sc.nextInt(), sc.nextInt(), true);
				break;
			
			case 3:
				System.out.println("Id studenta pro odebrani:");
				db.propustit(sc.nextInt());
				break;
			
			case 4:
				System.out.println("Id studenta:");
				db.vypisJednotlivce(sc.nextInt());
				break;
			
			case 5:
				System.out.println("Id studenta:");
				db.vypisDovednost(sc.nextInt());
				break;
			
			case 6:
				db.vypisVse();
				break;
				
			case 7:
				db.vypisCelkovyPrumer();
				break;
				
			case 8:
				db.pocetVsechStudentu();
				break;
				
			case 9:
				System.out.println("Napiste jmeno a prijmeni studenta:");
				db.nactiPodleJmena(sc.next(), sc.next());
				break;
		}
	}while(choice < 10);
	
	}

}
