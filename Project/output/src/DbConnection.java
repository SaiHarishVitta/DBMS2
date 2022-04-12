import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbConnection {
	private static final String USERNAME = "saiharishkumarvitta", PASSWORD = "";
	private static final String DBNAME = "postgres";
	private static final String URL = "jdbc:postgresql://localhost:5432/" + DBNAME; 
	
	public static void loadDriver() {
		try {
			Class.forName("org.postgresql.Driver"); // Loads the required driver
			System.out.println("Success loading Driver!");
		} catch (Exception exception) {
			System.out.println("Fail loading Driver!");
			exception.printStackTrace();
		}
	}
	
	private static Sale extractRow(ResultSet rs) throws Exception {
		Sale sale = new Sale(rs.getString(1), 
					rs.getString(2), 
					rs.getInt(3), 
					rs.getInt(4),
					rs.getInt(5),
					rs.getString(6),
					rs.getInt(7)
				);
		return sale;
	}

	public static void retreive(EMFStructure emf) {
		loadDriver();
		try {
			Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD); // connect to the database using the password
																			// and username
			Statement st = con.createStatement(); // statement created to execute the query
			String ret = "SELECT * FROM SALES";
			ResultSet rs = st.executeQuery(ret); // executing the query
			
			int count = 0;
			while(rs.next()) {
				Sale sale = extractRow(rs);
				count++;
				emf.process(sale);
			}
			rs.close();
			st.close();
			con.close();
			System.out.println("Number of records " + count);
		} catch (Exception exception) {
			System.out.println("Fail loading Driver!");
			exception.printStackTrace();
		}
	}
	
	public static void processData(EMFStructure emf) {
		retreive(emf);
	}
}
