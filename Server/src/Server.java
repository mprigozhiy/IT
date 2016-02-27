/*Jonathan Caverly, Mikhail Prigozhiy, Jonathan J. Getahun*/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements Runnable {

	//HashMap containing array lists of messages (groups), with group names being the keys
	static Map<String,List<message>> serv = new HashMap<String,List<message>>();

	public static void main(String[] args) throws IOException {

		ServerSocket svc = null;

		try{
			if (args.length != 0) { //If there is an argument
				svc = new ServerSocket(Integer.parseInt(args[1]), 5); //Listen for the inputted port number
			}
			else {
				svc = new ServerSocket(12345, 5); //Listen for port 12345
			}
		} catch(NumberFormatException e){ //If the argument cannot be parsed to an integer
			System.out.println("Invalid Port Number.");
			System.exit(1); //Exit the program
		}

		while(true){
			final Socket conn = svc.accept(); //Waits for a connection
			Thread t = new Thread(new Runnable(){ //Creates a new thread

				Socket socket = conn; //Creates a socket
				@Override
				public void run() {

					// Get the input/output streams for the socket
					BufferedReader fromClient = null; 
					DataOutputStream toClient = null;

					try {
						fromClient = new BufferedReader(new InputStreamReader(conn.getInputStream())); //Messages from the client
						toClient =  new DataOutputStream(conn.getOutputStream()); //Messages to the client
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					String recMsg = "";

					try {
						recMsg = fromClient.readLine(); //Gets the message from the client
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					int firstSpace = recMsg.indexOf(" ");
										
					String status = recMsg.substring(0, firstSpace).trim(); //Gets the status from the client's message
					
					String groupName = "";
					
					try {
						if(status.equalsIgnoreCase("post")){ //If the client's status is "post"
							groupName = recMsg.substring(firstSpace).trim(); //Gets the group name from the client's message
							for (int i = 0; i < groupName.length(); i++) { //Iterates through the client's group name
								if (Character.isISOControl(groupName.charAt(i))) { //If client's group name contains a control character
									try {
										toClient.writeBytes("error: invalid group name"); //Send error message to client
										System.exit(1); //Exit the thread
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							runPost(conn, groupName, fromClient, toClient); //Continue talking with client
						}
						else if (status.equalsIgnoreCase("get")){ //If the client's status is "post"
							groupName = recMsg.substring(firstSpace).trim(); //Gets the group name from the client's message
							for (int i = 0; i < groupName.length(); i++) { //Iterates through the client's group name
								if (Character.isISOControl(groupName.charAt(i))) { //If the client's group name contains a control character
									try {
										toClient.writeBytes("error: invalid group name"); //Send error message to client
										System.exit(1); //Exit the thread
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							runGet(conn, groupName, fromClient, toClient); //Continue talking with client
						}
					else { //If the client's status is neither "post" nor "get"
						try {
							toClient.writeBytes("error: invalid command"); //Send error message to client
							System.exit(1); //Exit the thread
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}});
			t.start(); //Starts the thread
		}
	}

	//Server communicating with get client
	private static void runGet(Socket conn, String groupName, BufferedReader fromClient, DataOutputStream toClient) throws IOException {
		if (serv.containsKey(groupName)) { //If the requested group name is in the hash map
			toClient.writeBytes("ok\n"); //Tell the client "ok"
			toClient.writeBytes(serv.get(groupName).size() + " message(s)\n"); //Tell the client how many messages are in the group
			for(message mess: serv.get(groupName)){ //For each message in the group 
				toClient.writeBytes(mess.toString()); //Send each message to the client
			}
			conn.close(); //Ends connection with client
			return;
		} else { //If the requested group name is not in the hash map
			toClient.writeBytes("error: invalid group name"); //Sends error message to the client
			conn.close(); //Ends connection with client
			return;
		}
	}

	//Server communicating with post client
	private static void runPost(Socket sock, String groupName, BufferedReader fromClient, DataOutputStream toClient) throws IOException{
		toClient.writeBytes("ok\n"); //Tells the client "ok"
		String username = fromClient.readLine(); //Gets username from client
		toClient.writeBytes("ok\n"); //Tells the client "ok"
		String clientMsg = "";
        String add = "";

        while((add = fromClient.readLine()) != null){ //While the client is continuing to write their message
        	clientMsg = clientMsg + add + "\n"; //Update their message
        }		

		if (serv.containsKey(groupName)){ //If the requested group name is in the hash map
			List<message> temp = serv.get(groupName); //Get the corresponding list of messages
			temp.add(new message(username, clientMsg, sock.getRemoteSocketAddress().toString())); //Add the client's message to the list
		}
		else { //If the requested group name is not in the hash map
			List<message> temp = new ArrayList<message>(); //Create a new group
			temp.add(new message(username, clientMsg, sock.getRemoteSocketAddress().toString())); //Add the client's message to the group
			serv.put(groupName, temp); //Add the group to the hash map
		}

		sock.close(); //Ends connection with client
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}


}
