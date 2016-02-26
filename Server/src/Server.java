import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Server {

	public static void main(String[] args) throws IOException {
		Map<String,ArrayList<message>> serv = new HashMap<String,ArrayList<message>>();
		ServerSocket svc;
		
		if (args.length != 0) {
			svc = new ServerSocket(Integer.parseInt(args[0].substring(3, args[0].length())), 5);	// listen on port 12345
		}
		else {
			svc = new ServerSocket(12345, 5);	// listen on port 12345
		}

		Socket conn = svc.accept();	// get a connection

		// get the input/output streams for the socket
		BufferedReader fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		DataOutputStream toClient = new DataOutputStream(conn.getOutputStream());

		// read the data
		String msg;
		msg = fromClient.readLine();
		
		String response;
		
		System.out.println("got line \"" + msg + "\"");

		StringTokenizer st = new StringTokenizer(msg);
		String token = st.nextToken();
		String initialToken = token;
		if (!initialToken.equals("get") || !initialToken.equals("post")) {
			response = "error: invalid command";
			toClient.writeBytes(response);
		}
		else {
			
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				if (!st.hasMoreTokens()) {
					if (initialToken.equals("post") && !serv.containsKey(initialToken)) {
						ArrayList<message> messageList = new ArrayList<message>();
						serv.put(token, messageList);
						response = "ok";
						toClient.writeBytes(response);
						String usrnm = fromClient.readLine();
						System.out.println("Username "+"'"+usrnm+"'"+" received.");
						response = "ok";
						String msge;
						String finalMessage = "";
						while ((msge = fromClient.readLine()) != null) {
							finalMessage += msge;
						}
						System.out.println("Message "+"'"+finalMessage+"'"+" received.");
						String loc = conn.getRemoteSocketAddress().toString();
						message m = new message(usrnm, finalMessage, loc);
						serv.get(initialToken).add(m);
						conn.close();		// close connection
					}
					if (!serv.containsKey(token)) {
						response = "error: in";
						//toClient.writeByte(response);
					}
				}
			}
			
		}
		
		// do the work
		//response = message.length() + ": " + message.toUpperCase() + '\n';

		//toClient.writeBytes(response);	// send the result

		System.out.println("server exiting\n");
		conn.close();		// close connection
		svc.close();		// stop listening

	}

}
