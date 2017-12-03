package socket;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class implementing Email Client Socket with basic behavior like sending requests and getting response.
 * @author Grzegorz Zyrek
 *
 */
public class SmtpClientSocket {
	
	Socket smtpSocket = null;
	PrintWriter outputStream = null;
	InputStream inputStream = null;
	
   private static final String SMTP_GREETING = "EHLO ";
   private static final String SMTP_GREETING_MSG = "hi";
   private static final String SMTP_GODBYE = "QUIT ";
   private static final String SMTP_END_OF_MESSAGE = "\r\n";
   private static final String SERVER = "S: ";
   private static final String CLIENT = "C: ";
   private static final String IO_SERVER_RESPONSE_EXCEPTION = "Server response exception: unexpected response";
   private static final String UNKNOWN_HOST_EXCEPTION = "Unknown host: ";
   private static final String OUTPUT_STREAM_ENCODING = "UTF-8";
   private static final int CONNECTION_ESTABILISHED = 220;
   private static final int SERVER_SUCCESS_CODE = 250;
   
	/**
	 * Method responsible for setting up connection with server socket 
	 * @param server - IP address or hostname of the server
	 * @param port - port of the SMTP service
	 * @throws IOException 
	 */
	public void setUpConnection (String server, int port) throws IOException {
		 try {
			  smtpSocket = new Socket(Inet4Address.getByName(server), port);		 
		      outputStream = new PrintWriter( new OutputStreamWriter(smtpSocket.getOutputStream(), OUTPUT_STREAM_ENCODING), true);
		      inputStream = smtpSocket.getInputStream();
		      getResponse(CONNECTION_ESTABILISHED);
		      sendRequest(SMTP_GREETING + SMTP_GREETING_MSG, SERVER_SUCCESS_CODE);
		    } catch (UnknownHostException e) {
		        System.err.println(UNKNOWN_HOST_EXCEPTION + server);
		        closeSocketAndStreams(1);    
		    } catch (IOException e) {
		        System.err.println(e);
		        closeSocketAndStreams(2);
		    } 
	}
	/**
	 * Method responsible for sending sample email message
	 * @param from - sender of message
	 * @param to - receiver of message
	 */
	public void sendMessage(String from, String to) {
		String sender = "MAIL FROM:<" + from + ">";
		String receiver = "RCPT TO:<" + to + ">";
	    try {
	      sendRequest(sender, SERVER_SUCCESS_CODE);
	      sendRequest(receiver, SERVER_SUCCESS_CODE);
	    } catch (IOException e) {
	        System.err.println(e);
	        closeSocketAndStreams(2);
	    }
	  }
	/**
	 * Method responsible for sending single requests to the Server Socket
	 * @param request - client request to the server
	 * @param responseCodeToCheck - response code of server (positive response)
	 * @throws IOException
	 */
	private void sendRequest(String request, int responseCodeToCheck)
            throws IOException {
             outputStream.println(request);
             System.out.println(CLIENT + request);
             getResponse(responseCodeToCheck);

}

	/**
	 * Method that gets response from Server Socket. Actually only known and possible implementation to get exact result as descripted in task brief.
	 * @param responseCodeToCheck - response code of the server (positive response)
	 * @throws IOException
	 */
	private void getResponse(int responseCodeToCheck) throws IOException {
	    byte[] readBytes = new byte[10000];
	    int numberOfBytes = inputStream.read(readBytes);
	    String responseMessage = new String(readBytes, 0, numberOfBytes);
	    System.out.println(SERVER + responseMessage);
	    if (!responseMessage.startsWith(String.valueOf(responseCodeToCheck)))
	     throw new IOException(IO_SERVER_RESPONSE_EXCEPTION);
	  }
	
	/**
	 * Method handles connection shutdown process.
	 */
	public void shutdownConnection() {
		    try {
		      sendRequest(SMTP_GODBYE + SMTP_END_OF_MESSAGE, 221);
		    } catch (Exception e) {
		      System.err.println(e);
		      closeSocketAndStreams(2);
		    }
		    closeSocketAndStreams(0);
		  }
	  
	/**
	 * Method that handles closing sockets and streams.
	 * @param code - exit code (0 - ok, positive/ negative = error)
	 */
	private void closeSocketAndStreams(int code) {
	    try {
	      inputStream.close();
	      outputStream.close();
	      smtpSocket.close();
	    }
	    catch (Exception e) {}
	    System.exit(code);
	  }
	
}
