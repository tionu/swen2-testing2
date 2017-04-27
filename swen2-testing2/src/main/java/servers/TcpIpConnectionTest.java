package servers;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TcpIpConnectionTest {
	
	@Mock
	private ServerSocket serverSocket;
	@Mock
	private Socket socket;
	@InjectMocks
	private TcpIpConnection tcpip = new TcpIpConnection(12345);

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testWaitForIncomingConnection() throws IOException {
		Socket socket = new Socket();
		Mockito.doReturn(socket).when(serverSocket).accept();
		
		boolean connected = tcpip.waitForIncomingConnection();
		
		assertTrue(connected);
	}
	
	@Test
	public void testWaitForIncomingConnectionException() throws IOException {
		Mockito.doThrow(new IOException()).when(serverSocket).accept();
		
		boolean connected = tcpip.waitForIncomingConnection();
		
		assertFalse(connected);
	}

	@Test
	public void testReceiveData() throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream("abcdefg".getBytes());
		Mockito.doReturn(inputStream).when(socket).getInputStream();
		
		String receivedData = tcpip.receiveData();
		
		assertEquals("abcdefg", receivedData);
		Mockito.verify(socket, Mockito.times(1)).getInputStream();
	}

	@Test
	public void testSendData() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Mockito.doReturn(outputStream).when(socket).getOutputStream();
		
		tcpip.sendData("abcdefg");
		
		String empfangeneDaten = outputStream.toString();
		assertEquals("abcdefg", empfangeneDaten);
		Mockito.verify(socket, Mockito.times(1)).getOutputStream();
	}

}
