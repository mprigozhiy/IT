
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Server implements Runnable {

	static Map<String,List<message>> serv = new HashMap<String,List<message>>();

	public static void main(String[] args) throws IOException {

		ServerSocket svc = null;

		try{
			if (args.length != 0) {
				svc = new ServerSocket(Integer.parseInt(args[1]), 5);	
			}
			else {
				svc = new ServerSocket(12345, 5);	// listen on port 12345
			}
		} catch(NumberFormatException e){
			System.out.println("Invalid Port Number.");
			System.exit(1);
		}

		while(true){
			Socket conn = svc.accept();	// get a connection
			Thread t = new Thread(new Runnable(){
				
				Socket socket = conn;
				@Override
				public void run() {
					


					// get the input/output streams for the socket
					BufferedReader fromClient = null; 
					DataOutputStream toClient = null;

					try {
						fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						toClient =  new DataOutputStream(conn.getOutputStream());
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					// read the data
					String recMsg = null;
					String response;
					String token;

					try {
						recMsg = fromClient.readLine().toString();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					System.out.println("got line \"" + recMsg + "\"");

					StringTokenizer st = new StringTokenizer(recMsg);
					String status = st.nextToken();
					String groupName = st.nextToken();

					ArrayList<String> clientRequest = new ArrayList<String>(); //UserRequests are <= 6, so you can just base it off size
					clientRequest.add(status); //add all tokens here to store the entire request

					
				try {
					if(status.equalsIgnoreCase("post")){
							runPost(conn, groupName, fromClient, toClient);
					}
					else if (status.equals("get")){
						runGet(conn, groupName, fromClient, toClient);			
					}
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
				
		});
		t.start();
		
			





			/*	if (!status.equals("get") && !status.equals("post")) {
				response = "error: invalid command";
			}
			else {

				while (st.hasMoreTokens()) {
					token = st.nextToken();
					clientRequest.add(token);
					if (!st.hasMoreTokens()) {
						if (status.equals("post") && !serv.containsKey(token)) {
							ArrayList<message> messageList = new ArrayList<message>();
							serv.put(token, messageList);
							response = "ok\n";
							System.out.println("dickkk");
							toClient.writeBytes(response);
							String usrnm = fromClient.readLine();
							System.out.println("Username "+"'"+usrnm+"'"+" received.");
							response = "ok\n";
							toClient.writeBytes(response);
							String msge;
							//String finalMessage = "";

							/*while ((msge = fromClient.readLine()) != null) {
							finalMessage += msge;
						}
							msge = fromClient.readLine();

							System.out.println("Message "+"'"+msge+"'"+" received.");
							String loc = conn.getRemoteSocketAddress().toString();
							message m = new message(usrnm, msge, loc);
							serv.get(token).add(m);
							conn.close();		// close connection
						}
						else if (status.equalsIgnoreCase("get")) {
							//CODE THE GROUP EXISTS CHECK
							toClient.writeBytes("ok\n");
							toClient.writeBytes(serv.get(token).size() + " message(s) ");
							for(message mess:serv.get(token)) {
								toClient.writeBytes(mess.toString());
							}
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
			conn.close();
			//toClient.writeBytes(response);	// send the result
		}*/
		}
			//System.out.println("server exiting\n");
			// close connection
			//svc.close();		// stop listening
		
	}

	private synchronized static void runGet(Socket conn, String groupName, BufferedReader fromClient, DataOutputStream toClient) throws IOException {
		if (serv.containsKey(groupName)) {
			toClient.writeBytes("ok\n");
			toClient.writeBytes(serv.get(groupName).size() + " message(s)\n");
			for(message mess: serv.get(groupName)){
				toClient.writeBytes(mess.toString());
			}
			conn.close();
			return;
		} else {
			toClient.writeBytes("error: invalid group name");
			conn.close();
			return;
		}
	}

	private	synchronized static void runPost(Socket sock, String groupName, BufferedReader fromClient, DataOutputStream toClient) throws IOException{
		//CHECK FOR PROPER INPUTS - MIKHAIL 
		toClient.writeBytes("ok\n");
		String username = fromClient.readLine();
		System.out.println("username is: " + username);
		toClient.writeBytes("ok\n");
		String clientMsg = fromClient.readLine() + "\n";
		System.out.print("The message was received: " + clientMsg);

		if (serv.containsKey(groupName)){
			List<message> temp = serv.get(groupName);
			temp.add(new message(username, clientMsg, sock.getRemoteSocketAddress().toString()));
		}
		else {
			List<message> temp = Collections.synchronizedList(new ArrayList<message>());
			temp.add(new message(username, clientMsg, sock.getRemoteSocketAddress().toString()));
			serv.put(groupName, temp);
		}
		
		sock.close();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}


}
