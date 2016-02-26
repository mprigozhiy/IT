import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class get {
	public static void main(String args[]) throws Exception {
		String line = null;	
		Socket sock = null;
		
		/*
		 * Tries to setup the socket. This will catch if port number is invalid, 
		 * if hostname is invalid or if any post commands are invalid. 
		 */
		try {
			if(args.length == 1){
				sock = new Socket("localhost", 12345);
				line = args[0];
			} else if(args.length == 3 ){
				sock = new Socket("localhost", Integer.parseInt(args[1]));
				line = args[2];
			} else if (args.length == 5){
				sock = new Socket(args[1], Integer.parseInt(args[3]));
				line = args[4];
			} else {
				System.out.println("Invalid post arguments.");
				System.exit(1);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Port Number");
			System.exit(1);
		} catch (UnknownHostException e) {
			System.out.println("Invalid Host Name");
			System.exit(1);
		}
		
		/* 
		 * Create data streams to receive and send data to server
		 */
		DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		toServer.writeBytes("get " + line + '\n');	
		String result = "";	
		result = fromServer.readLine();
		
		/*
		 * If the group name does not exist, we will receive an error from server.
		 */
		if (result.equalsIgnoreCase("error: invalid group name") || result.equalsIgnoreCase("error: invalid command")) {
			System.out.println(result);
			sock.close();
			System.exit(1);
		}
		
		result = fromServer.readLine();
		
		/*
		 * If everything is good and the group name exists. We 
		 * will loop until the server stops sending us messages.
		 * Exit upon completion
		 */
		while (result != null) {
			System.out.println(result);	
			System.out.println("");
			result = fromServer.readLine();
		}
		sock.close();				
	}
}
