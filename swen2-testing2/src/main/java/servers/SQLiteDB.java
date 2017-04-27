package servers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SQLiteDB implements Persistence {

	
	private SimpleDateFormat dateFormat;
	
	public SQLiteDB() {
		dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}


	@Override
	public Patient getPatient(int paId) {
		Connection connection = null;
		try {
			connection = openConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM PAPatient "
					+ "JOIN HOHospitalisation ON PAID = HOPAID "
					+ "JOIN FAFachbereich ON HOFAID = FAID "
					+ "WHERE PAID = " + paId + " ORDER BY HOID DESC");
			if(resultSet.next()) {
				String nachname = resultSet.getString("PANachname");
				String vorname = resultSet.getString("PAVorname");
				String gebDatString = resultSet.getString("PAGeburtsdatum");
				Date gebDat = dateFormat.parse(gebDatString);
				int hoId = resultSet.getInt("HOID");
				String aufnahmeDatString = resultSet.getString("HOAufnahmeDatum");
				Date aufnahmeDatum = (aufnahmeDatString == null || aufnahmeDatString.isEmpty()) ? null : dateFormat.parse(aufnahmeDatString);
				String entlassDatString = resultSet.getString("HOEntlassDatum");
				Date entlassDatum = (entlassDatString == null || entlassDatString.isEmpty()) ? null : dateFormat.parse(entlassDatString);
				String fachbereich = resultSet.getString("FAName");
				Patient patient = new Patient.PatientBuilder(paId, hoId)
						.name(nachname, vorname)
						.geburtsDatum(gebDat)
						.aufnahmeDatum(aufnahmeDatum)
						.entlassDatum(entlassDatum)
						.fachbereich(fachbereich)
						.build();
				return patient;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return null;
	}
	
	@Override
	public Arzt getArzt(String username) {
		Connection connection = null;
		try {
			connection = openConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM ARArzt WHERE ARUsername = '" + username + "'");
			if(resultSet.next()) {
				int id = resultSet.getInt("ARID");
				String nachname = resultSet.getString("ARNachname");
				String vorname = resultSet.getString("ARVorname");
				String titel = resultSet.getString("ARTitel");
				Fachbereich fachbereich = Fachbereich.getFachbereichById(resultSet.getInt("ARFAID"));
				String password = resultSet.getString("ARPassword");
				Arzt arzt = new Arzt.ArztBuilder()
						.id(id)
						.nachname(nachname)
						.vorname(vorname)
						.titel(titel)
						.fachbereich(fachbereich)
						.username(username)
						.password(password)
						.build();
				return arzt;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return null;
	}

	

	private Connection openConnection() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:Patienten.db");
		return connection;
	}
	
	private void closeConnection(Connection connection) {
		try {
			if(connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public List<Patient> getPatientenInFachbereich(Fachbereich fachbereich) {
		Connection connection = null;
		try {
			connection = openConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM HOHospitalisation WHERE HOFAID = " + fachbereich.getId());
			List<Integer> paIds = new LinkedList<>();
			while(resultSet.next()) {
				int id = resultSet.getInt("HOPAID");
				paIds.add(id);
			}
			List<Patient> patienten = new LinkedList<>();
			for(Integer paId : paIds) {
				Patient patient = getPatient(paId);
				patienten.add(patient);
			}
			return patienten;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
		}
		return null;
	}


}
