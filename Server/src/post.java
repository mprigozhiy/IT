import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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
			sock = new Socket("localhost", Integer.parseInt(args[1]));
			group = args[2];
		} else if (args.length == 5){
			sock = new Socket(args[1], Integer.parseInt(args[3]));
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
			toServer.writeBytes(line + '\n'); //send username to server
		} else{
			if(result.equals("error: invalid command")){ //error from server
				System.out.println(result);
				System.exit(1);
			} else if(result.equals("error: invalid group name")){
				System.out.println(result);
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
			if(result.equals("error: invalid command")){
				System.out.println(result);
				System.exit(1);
			} else if(result.equals("error: invalid user name")){
				System.out.println(result);
				System.exit(1);
			}
		}
		
		
		//result = fromServer.readLine();
		System.out.println("Your message was received!");	//On exit message.
		sock.close();				// and we're done
	
	}
}
