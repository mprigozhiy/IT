import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {
		ServerSocket svc = new ServerSocket(12345, 5);	// listen on port 12345

		Socket conn = svc.accept();	// get a connection

		// get the input/output streams for the socket
		BufferedReader fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		DataOutputStream toClient = new DataOutputStream(conn.getOutputStream());

		// read the data
		String line = fromClient.readLine();
		System.out.println("got line \"" + line + "\"");

		// do the work
		String result = line.length() + ": " + line.toUpperCase() + '\n';

		toClient.writeBytes(result);	// send the result

		System.out.println("server exiting\n");
		conn.close();		// close connection
		svc.close();		// stop listening

	}

}
