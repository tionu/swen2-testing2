package servers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTTP {
	
	public static void main(String[] args) {
		HTTP http = new HTTP(8000);
		http.start();
	}
	
	private Persistence persistence;
	private TcpIpConnection tcpip;
	private DateFormat dateFormat;
	
	public HTTP(int port) {
		persistence = new SQLiteDB();
		tcpip = new TcpIpConnection(port);
		dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	}
	
	public void start() {
		try {
			if(!tcpip.waitForIncomingConnection())
				return;
			String receivedData = tcpip.receiveData();
			String[] lines = receivedData.split("\r\n");
			String[] statusline = lines[0].split(" ");
			String[] path = statusline[1].split("/");
			
			String objName = path[1];
			String objIdString = path[2];
			
			if(!objName.equals("Patient")) {
				tcpip.close();
				return;
			}
			int objId = Integer.valueOf(objIdString);
			Patient patient = persistence.getPatient(objId);
			
			String content = getContent(patient);
			String response = 
					"HTTP/1.1 200 OK\r\n"
					+ "Content-Length: " + content.length() + "\r\n"
					+ "\r\n"
					+ content;
			tcpip.sendData(response);
			tcpip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getContent(Patient patient) {
		String webseite = 
				  "<html>\r\n"
				+ 	"<table>\r\n"
				+ 		"<tr><th>Eigenschaft</th><th>Wert</th></tr>\r\n"
				+ 		"<tr><td>Pat-ID</td><td>" + patient.getPaId() + "</td></tr>\r\n"
				+ 		"<tr><td>Aufnahme-ID</td><td>" + patient.getHoId() + "</td></tr>\r\n"
				+ 		"<tr><td>Vorname</td><td>" + patient.getVorname() + "</td></tr>\r\n"
				+ 		"<tr><td>Nachname</td><td>" + patient.getNachname() + "</td></tr>\r\n"
				+ 		"<tr><td>Geburtsdatum</td><td>" + formatDate(patient.getGeburtsDatum()) + "</td></tr>\r\n"
				+ 		"<tr><td>Fachbereich</td><td>" + patient.getFachbereich() + "</td></tr>\r\n"
				+ 		"<tr><td>Aufnahmedatum</td><td>" + formatDate(patient.getAufnahmeDatum()) + "</td></tr>\r\n"
				+ 		"<tr><td>Entlassdatum</td><td>" + formatDate(patient.getEntlassDatum()) + "</td></tr>\r\n"
				+ 	"</table>\r\n"
				+ "</html>\r\n";
		return webseite;
	}
	
	private String formatDate(Date date) {
		if(date == null)
			return "";
		return dateFormat.format(date);
	}
	
}
