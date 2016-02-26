import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Server {

	public static void main(String[] args) throws IOException {
		Map<String,ArrayList> serv = new HashMap<String,ArrayList>();
		
		ServerSocket svc = new ServerSocket(12345, 5);	// listen on port 12345

		Socket conn = svc.accept();	// get a connection

		// get the input/output streams for the socket
		BufferedReader fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		DataOutputStream toClient = new DataOutputStream(conn.getOutputStream());

		// read the data
		String message;
		message = fromClient.readLine();
		
		String response;
		
		System.out.println("got line \"" + message + "\"");

		StringTokenizer st = new StringTokenizer(message);
		String token = st.nextToken();
		if (!token.equals("get") || !token.equals("post")) {
			response = "error: invalid command";
			toClient.writeBytes(response);
		}
		else {
			
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				if (!st.hasMoreTokens()) {
					if (token.equals(""));
				}
			}
			
		}
		
		// do the work
		response = message.length() + ": " + message.toUpperCase() + '\n';

		toClient.writeBytes(response);	// send the result

		System.out.println("server exiting\n");
		conn.close();		// close connection
		svc.close();		// stop listening

	}

}
