package servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTP {
	
	public static void main(String[] args) {
		HTTP http = new HTTP(8000);
		http.start();
	}
	
	private int port;
	private Persistence persistence;
	
	public HTTP(int port) {
		this.port = port;
		persistence = new SQLiteDB();
	}
	
	public void start() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			Socket socket = serverSocket.accept();
			String receivedData = Utilities.receiveData(socket);
			String[] lines = receivedData.split("\r\n");
			String[] statusline = lines[0].split(" ");
			String[] path = statusline[1].split("/");
			
			String objName = path[1];
			String objIdString = path[2];
			
			if(!objName.equals("Patient")) {
				serverSocket.close();
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
			Utilities.sendData(socket, response);
			serverSocket.close();
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
				+ 		"<tr><td>Geburtsdatum</td><td>" + patient.getGeburtsDatum() + "</td></tr>\r\n"
				+ 		"<tr><td>Fachbereich</td><td>" + patient.getFachbereich() + "</td></tr>\r\n"
				+ 		"<tr><td>Aufnahmedatum</td><td>" + patient.getAufnahmeDatum() + "</td></tr>\r\n"
				+ 		"<tr><td>Entlassdatum</td><td>" + patient.getEntlassDatum() + "</td></tr>\r\n"
				+ 	"</table>\r\n"
				+ "</html>\r\n";
		return webseite;
	}
	
}
