package application;

import java.io.IOException;
import java.net.UnknownHostException;

import socket.SmtpClientSocket;

public class ApplicationClass {

	private final static String SERVER = "212.85.110.159";
	private final static int PORT = 587; //25 shouldn't be used so instead used 587
    private final static String FROM = "test@test.test";
    private final static String TO = "maco@ciesla.pl";
    
	public static void main(String[] args) throws IOException, UnknownHostException {
		

		SmtpClientSocket smtpClient = new SmtpClientSocket();
		if (smtpClient != null) {
        smtpClient.setUpConnection(SERVER, PORT);
        smtpClient.sendMessage(FROM, TO);
        smtpClient.shutdownConnection();
		}
	}

}
