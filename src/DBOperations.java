import java.sql.*;

/**
 * @author Ondrej Chudacek (221548)
 * 
 * Trida obsahuje metody pro praci s SQL databazi a je urcena pro implementaci ve tride "Daztabaze"
 * Trida v podstate zapouzdruje SQL databazi, ikdyz metody obcas vraci ResultSet (realne nejde menit databazi bez metod teto tridy).
 */

public class DBOperations {
	
	private static volatile Connection conn;
	
	public DBOperations(){}
	
	/**
	 * Metoda vytvari tabulku v SQL databazi podle zadaneho prikazu.
	 * 
	 * @param String query - prikaz SQL, kterym chcete tabulku vytvorit
	 * 
	 * @return boolean - true - tabulka byla vytvorena, false - nastal problem
	 */
	public boolean createTable(String query)
	{
		try {
			
			Statement stmt = DBOperations.conn.createStatement();
	        return !stmt.execute(query);
	        
		}catch(SQLException e)
		{
            System.out.println(e.getMessage());
            return false;
		}
	}
	
	/**
	 * Metoda ziskava spojeni s SQL datbazi. 
	 * Vola se vzdy, pokud je potreba nejak interagovat s databazi.
	 * Metoda take vytvari tabulky databaze a vklada pocatecni jmeno a heslo. 
	 * Tato funkcionalita metody se prakticky ozyva pouze, pokud databaze nebyla inicializovana.
	 * 
	 * @return true - pripojeni k databazi bylo uspesne, false - nastala chyba
	 */
	public boolean connectDB()
	{
		DBOperations.conn = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			DBOperations.conn = DriverManager.getConnection("jdbc:sqlite:MyDB.sql");
			if(DBOperations.conn == null) return false;
			
			
			if(!this.createTable("CREATE TABLE IF NOT EXISTS login (jmeno varchar(255), heslo varchar(255) NOT NULL, PRIMARY KEY (jmeno));")) return false;
            
            if(!this.createTable("CREATE TABLE IF NOT EXISTS znamky (id INTEGER PRIMARY KEY, znamka tinyint NOT NULL, id_studenti NOT NULL);")) return false;
            
            if(!this.createTable("CREATE TABLE IF NOT EXISTS studenti (id int NOT NULL, jmeno varchar(255) NOT NULL, prijmeni varchar(255) NOT NULL, rok integer NOT NULL, obor tinyint NOT NULL, mesic tinyint, den tinyint, PRIMARY KEY (id));")) return false;
            
            
            String query = "INSERT OR IGNORE INTO login (jmeno, heslo) VALUES (?, ?);";
            PreparedStatement prStmt;
            
            try{
            	prStmt = DBOperations.conn.prepareStatement(query);
                prStmt.setString(1, "defaultUser");
                prStmt.setString(2, "defaultPassword");

                prStmt.executeUpdate();
                prStmt.close();
                return true;
              } catch (SQLException e) {
            	this.disconnectDB();
                e.printStackTrace();
                return false;
              }
            
		}catch(SQLException e)
			{
	            System.out.println(e.getMessage());
	            return false;
			} 
		catch (ClassNotFoundException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
		
	}
	
	/**
	 * Metoda perusuje spojeni s SQL databazi.
	 * Metodu je potreba volat vzdy po skonceni komunikace s SQL databazi.
	 * 
	 * @return true - spojeni bylo uspesne preruseno, false - nastala chyba
	 */
	public boolean disconnectDB()
	{
		if(conn != null)
		{
			try {
				conn.close();
				return true;
			}catch(SQLException e)
				{
		            System.out.println(e.getMessage());
		            return false;
				}
		}else return false;
		
	}
	
	/** 
	 * Metoda overi, zda existuje v databazi zaznam se stejnym jmenem a heslem.
	 * Pokud se takovy zaznam najde, bude se prihlaseni povazovat za uspesne. 
	 * 
	 * @param String jmeno - uzivatelske jmeno
	 * @param String heslo - heslo pro prihlaseni k databazi
	 * 
	 * @return true - prihlaseni bylo uspesne, false - doslo k chybe
	 */
	public boolean login(String jmeno, String heslo)
	{
		this.connectDB();
		if(conn != null)
		{
			try {
				String query = "SELECT * FROM login WHERE jmeno = ? AND heslo = ?;";
				PreparedStatement psmt = conn.prepareStatement(query);
				psmt.setString(1, jmeno);
	            psmt.setString(2, heslo);
	            ResultSet rs = psmt.executeQuery();
	            if(rs.next())
	            {
	            	psmt.close();
	            	return true;
	            }
	            psmt.close();
	            this.disconnectDB();
				return false;
			}catch(SQLException e)
			{
		        System.out.println(e.getMessage());
		        this.disconnectDB();
		        return false;
			}
		}else return false;
	}
	
	/**
	 * Multifuncni metoda pro ziskavani informaci z databaze.
	 * Prakticky se pouziva pouze pro dotazy bez konktertnich hodnot zadavanych uzivatelem kvuli SQL injection.
	 * 
	 * @param String query - SQL prikaz urceny k vykonani funkci
	 * 
	 * @return ResultSet rs - ResultSet s vysledky dotazu, pro rs == null doslo k chybe
	 */
	public ResultSet loadDB(String query)
	{
		if(DBOperations.conn != null)
		{
			try {
				Statement stmt = DBOperations.conn.createStatement();
				return stmt.executeQuery(query);
				
			}catch(SQLException e)
			{
		        System.out.println(e.getMessage());
		        return null;
			}
		}else return null;
	}
	/**
	 * Metoda ulozi studenta do databaze a urci mu podle instance objektu Student i cislo urcujici jeho/jeji obor.
	 * Kazdy student se v ramci metody uklada samostatne.
	 * Jelikoz maji studenti humanitniho a kombinovaneho oboru i mesic a den narozeni, musi ukladani castecne probihat oddelene.
	 * 
	 * @param Student student - student urceny k ulozeni do SQL databaze
	 * 
	 * @return true - student byl ulozen, false - nastala chyba
	 */
	public boolean ulozStudenta(Student student)
	{
		this.connectDB();
		String query;
		PreparedStatement psmt;
		
		try {
			
			if(student instanceof Technicky)
			{
				query = "INSERT INTO studenti (id, jmeno, prijmeni, rok, obor) VALUES (?, ?, ?, ?, ?);";
				psmt = DBOperations.conn.prepareStatement(query);
				psmt.setInt(5, 1);
				
			}else
				{
					query = "INSERT INTO studenti (id, jmeno, prijmeni, rok, obor, mesic, den) VALUES (?, ?, ?, ? , ?, ?, ?);";
					psmt = DBOperations.conn.prepareStatement(query);
					psmt.setInt(6, ((Humanitni)student).getMesic());
					psmt.setInt(7, ((Humanitni)student).getDen());
				}
			psmt.setInt(1, student.getId());
            psmt.setString(2, student.getJmeno());
            psmt.setString(3, student.getPrijmeni());
            psmt.setInt(4, student.getRokNarozeni());
            if(student instanceof Kombinovany) {psmt.setInt(5, 3);} else if(student instanceof Humanitni) psmt.setInt(5, 2);
            psmt.executeUpdate();
            psmt.close();
            this.disconnectDB();
			return true;
		}catch(SQLException e)
		{
			this.disconnectDB();
	        System.out.println(e.getMessage());
	        return false;
		}
		
		
	}
	
	/**
	 * Metoda ulozi zadanou znamku studenta s id do SQL databaze (tabulky pro znamky).
	 * Znamka je svazana se studentem pomoci relace znamky.id_studenti = studenti.id
	 * 
	 * @param int znamka - znamka, ktera ma byt ulozena do SQL databaze
	 * @param int id - unikatni ID studenta, kteremu znamka nalezi.
	 * 
	 * @return true - znamka byla ulozena, false - doslo k chybe
	 */
	public boolean ulozZnamku(int znamka, int id)
	{
		this.connectDB();
		try 
		{
			String query = "INSERT INTO znamky (znamka, id_studenti) VALUES (?, ?)";
			PreparedStatement psmt = DBOperations.conn.prepareStatement(query);
			psmt.setInt(1, znamka);
			psmt.setInt(2, id);
			psmt.executeUpdate();
			psmt.close();
			return true;
		}catch(SQLException e)
		{
			this.disconnectDB();
	        System.out.println(e.getMessage());
	        return false;
		}
	}
	
	/**
	 * Metoda smaze studenta s id z databaze.
	 * Jeho/jeji znamky budou smazany take (pomoci relace znamky.id_studenti = studenti.id).
	 * 
	 * @param int id - unikatni ID studenta, ktery je urcen ke smazani
	 * 
	 * @return true - student byl smazan, false - nastala chyba
	 */
	public boolean deleteStudent(int id)
	{
		this.connectDB();
		try {
			String query = "DELETE FROM studenti WHERE id = ?;";
			PreparedStatement psmt = DBOperations.conn.prepareStatement(query);
			psmt.setInt(1, id);
			psmt.executeUpdate();
			
			
			query = "DELETE FROM znamky WHERE id_studenti = ?;";
			psmt = DBOperations.conn.prepareStatement(query);
			psmt.setInt(1, id);
			psmt.executeUpdate();
			this.disconnectDB();
			return true;
		}catch(SQLException e)
		{
			this.disconnectDB();
	        System.out.println(e.getMessage());
	        return false;
		}
	}
	
	/**
	 * Metoda nacte studenta z databaze na zaklade jeho/jejiho krestniho jmena a prijmeni.
	 * Vzhledem k navrhu programu nema metoda zadne realne vyuziti.
	 * 
	 * @param String jmeno - krestni jmeno studenta
	 * @param String prijmeni - prijmeni studenta
	 * 
	 * @return ResultSet rs - ResultSet s vysledky hledani v databazi. Pokud rs = null, pri hledani doslo k chybe nebo student nebyl nalezen.
	 */
	
	public ResultSet loadExactStudent(String jmeno, String prijmeni)
	{
		try {
			String query = "SELECT * FROM studenti WHERE jmeno = ? AND prijmeni = ?;";
			PreparedStatement psmt = DBOperations.conn.prepareStatement(query);
			psmt.setString(1, jmeno);
			psmt.setString(2, prijmeni);
			
			return psmt.executeQuery();
		}catch(SQLException e)
		{
	        System.out.println(e.getMessage());
	        return null;
		}
	}
	
}
