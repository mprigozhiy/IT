/*Jonathan Caverly, Mikhail Prigozhiy, Jonathan J. Getahun*/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class get {
	public static void main(String args[]) throws Exception {
		String line = null;	
		Socket sock = null;
		String group = null;
		
		/*
		 * Tries to setup the socket. This will catch if port number is invalid, 
		 * if hostname is invalid or if any post commands are invalid. 
		 */
		try{ //Conditions for optional flags with error checking
			if(args.length == 1){
				sock = new Socket("localhost", 12345);
				group = args[0];
			} else if(args.length == 3 ){
				if(args[0].equals("-h")){
					sock = new Socket(args[1], 12345);
				} else if(args[0].equals("-p")){
					sock = new Socket("localhost", Integer.parseInt(args[1]));
				} else {
					System.out.println("Error: Invalid flag.");
					System.exit(1);
				}
				
				group = args[2];
			} else if (args.length == 5){
			
				if(args[0].equals("-p") && args[2].equals("-h")){
					sock = new Socket(args[3], Integer.parseInt(args[1]));
				} else if (args[0].equals("-h") && args[2].equals("-p")){
					sock = new Socket(args[1], Integer.parseInt(args[3]));
				} else {
					System.out.println("Error: Invalid flag.");
					System.exit(1);
				}
				
				group = args[4];
			} else {
				System.out.println("Invalid post arguments.");
				System.exit(1);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Port Number.");
			System.exit(1);
		} catch (UnknownHostException e) {
			System.out.println("Invalid Host Name.");
			System.exit(1);
		} catch (ConnectException e) {
			System.out.println("ConnectException: Unable to connect.");
			System.exit(1);
		}
			
		/*
		 * Checks for invalid groupname
		 */
		for (int i = 0; i < group.length(); i++) {
			if (Character.isISOControl(group.charAt(i))) {
				System.out.println("error: invalid group name");
				System.exit(1);
			}
		}
		
		/* 
		 * Create data streams to receive and send data to server
		 */
		DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		toServer.writeBytes("get " + group + '\n');	
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
		System.out.println(result);	
		result = fromServer.readLine();
		
		/*
		 * If everything is good and the group name exists. We 
		 * will loop until the server stops sending us messages.
		 * Exit upon completion
		 */
		while (result != null) {
			//System.out.println("");
			System.out.println(result);	
			result = fromServer.readLine();
		}
		sock.close();				
	}
}
