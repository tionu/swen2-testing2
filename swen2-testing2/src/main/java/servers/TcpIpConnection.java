package servers;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpIpConnection {
	
	private Socket socket;
	private ServerSocket serverSocket;
	
	
	public TcpIpConnection(int port) {
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean waitForIncomingConnection() {
		try {
			socket = serverSocket.accept();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public String receiveData() {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			int b = in.read();
			if(b == -1)
				in.close();
			int length = in.available();
			byteStream.write(b);
			while(length > 0) {
				byte[] bytes = new byte[length];
				in.read(bytes, 0, length);
				byteStream.write(bytes);
				length = in.available();
			}
			String receivedData = new String(byteStream.toByteArray());
			System.out.println("Empfangen: " + receivedData);
			return receivedData;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendData(String data) throws IOException {
		socket.getOutputStream().write(data.getBytes());
		System.out.println("Gesendet: " + data);
	}
	
	public void closeServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
