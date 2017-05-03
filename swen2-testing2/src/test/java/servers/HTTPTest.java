package servers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import junit.framework.TestCase;

public class HTTPTest extends TestCase {
	
	@Mock
	private Persistence persistence;
	@Mock
	private TcpIpConnection tcpip;
	@InjectMocks
	private HTTP http = new HTTP(8080);
	
	private DateFormat dateFormat;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	}

	@Test
	public void testStart() throws IOException, ParseException {
		String html = 
				  "HTTP/1.1 200 OK\r\n"
				  + "Content-Length: 435\r\n"
				  + "\r\n"
				  + "<html>\r\n"
				+ 	"<table>\r\n"
				+ 		"<tr><th>Eigenschaft</th><th>Wert</th></tr>\r\n"
				+ 		"<tr><td>Pat-ID</td><td>5</td></tr>\r\n"
				+ 		"<tr><td>Aufnahme-ID</td><td>2</td></tr>\r\n"
				+ 		"<tr><td>Vorname</td><td>Hubert</td></tr>\r\n"
				+ 		"<tr><td>Nachname</td><td>Müller</td></tr>\r\n"
				+ 		"<tr><td>Geburtsdatum</td><td>13.11.1950</td></tr>\r\n"
				+ 		"<tr><td>Fachbereich</td><td>Chirurgie</td></tr>\r\n"
				+ 		"<tr><td>Aufnahmedatum</td><td>26.04.2017</td></tr>\r\n"
				+ 		"<tr><td>Entlassdatum</td><td></td></tr>\r\n"
				+ 	"</table>\r\n"
				+ "</html>\r\n";

		
		Patient patient = new Patient.PatientBuilder(5, 2)
				.name("Hubert", "Müller")
				.geburtsDatum(dateFormat.parse("13.11.1950"))
				.aufnahmeDatum(dateFormat.parse("26.04.2017"))
				.fachbereich("Chirurgie")
				.build();
		Mockito.doReturn(true).when(tcpip).waitForIncomingConnection();
		Mockito.doReturn("GET /Patient/2 HTTP/1.1\r\nHost:localhost\r\n\r\n").when(tcpip).receiveData();
		Mockito.doReturn(patient).when(persistence).getPatient(2);
		
		http.start();
		
		InOrder inOrder = Mockito.inOrder(tcpip, persistence);
		inOrder.verify(tcpip).waitForIncomingConnection();
		inOrder.verify(tcpip).receiveData();
		inOrder.verify(persistence).getPatient(2);
		inOrder.verify(tcpip).sendData(Mockito.anyString());
		inOrder.verify(tcpip).close();
	}

	@Test 
	public void testStartErfolglos() throws IOException {
		Mockito.doReturn(false).when(tcpip).waitForIncomingConnection();
		
		http.start();
		
		Mockito.verify(tcpip).waitForIncomingConnection();
		Mockito.verify(tcpip, Mockito.never()).receiveData();
		Mockito.verify(tcpip, Mockito.never()).close();
		Mockito.verify(persistence, Mockito.never()).getPatient(2);
		Mockito.verify(tcpip, Mockito.never()).sendData(Mockito.anyString());
	}

	@Test 
	public void testStartFalscherObjektname() throws IOException {
		Mockito.doReturn(true).when(tcpip).waitForIncomingConnection();
		Mockito.doReturn("GET /Arzt/2 HTTP/1.1\r\nHost:localhost\r\n\r\n").when(tcpip).receiveData();
		
		http.start();
		
		InOrder inOrder = Mockito.inOrder(tcpip);
		inOrder.verify(tcpip).waitForIncomingConnection();
		inOrder.verify(tcpip).receiveData();
		inOrder.verify(tcpip).close();
		Mockito.verify(persistence, Mockito.never()).getPatient(2);
		Mockito.verify(tcpip, Mockito.never()).sendData(Mockito.anyString());
	}

}
