/*Jonathan Caverly, Mikhail Prigozhiy, Jonathan J. Getahun*/

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class post {
	public static void main(String args[]) throws Exception {
		String group = null; //Target group name
		Socket sock = null;  //Socket

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



		for (int i = 0; i < group.length(); i++) {
			if (Character.isISOControl(group.charAt(i))) {
				System.out.println("error: invalid group name");
				sock.close();
				System.exit(1);
			}
		}




		String line;	// user input


		//Socket sock = new Socket("localhost", 12345);	// connect to localhost port 12345
		DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		line = "post " + group;		// "post groupname"
		toServer.writeBytes(line + '\n');	// send post request to server
		String result = fromServer.readLine();	// read server response

		if(result.equals("ok")){	// Server approves request
			line = System.getProperty("user.name"); // set username
			
			if(line.equals("")){
				System.out.println("error: invalid user name");
				sock.close();
				System.exit(1);
			}
			
			for (int i = 0; i < line.length(); i++) {
				if (Character.isISOControl(line.charAt(i))) {
					System.out.println("error: invalid user name");
					sock.close();
					System.exit(1);
				}
			}
			
			toServer.writeBytes(line + '\n'); //send username to server
		} else{
			if(result.equalsIgnoreCase("error: invalid command")){ //error from server
				System.out.println(result);
				sock.close();
				System.exit(1);
			} else if(result.equalsIgnoreCase("error: invalid group name")){
				System.out.println(result);
				sock.close();
				System.exit(1);
			}
		}

		result = fromServer.readLine(); // Server response
		if(result.equals("ok")){ // Server accepted username.
			BufferedReader userdata = new BufferedReader(new InputStreamReader(System.in)); //read from input
			//System.out.println("Username verified. Please enter your message: ");
			String enterIn = "";

			while((enterIn = userdata.readLine()) != null){
				
				toServer.writeBytes(enterIn + "\n"); //send input stream
			}

		} else{ //error conditions (exit)
			if(result.equalsIgnoreCase("error: invalid command")){
				System.out.println(result);
				sock.close();
				System.exit(1);
			} else if(result.equalsIgnoreCase("error: invalid user name")){
				System.out.println(result);
				sock.close();
				System.exit(1);
			}
		}


		//result = fromServer.readLine();
		System.out.println("Your message was received!");	//On exit message.
		sock.close();				// and we're done

	}
}
