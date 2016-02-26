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
		
		try {
			if(args.length == 1){
				sock = new Socket("localhost", 12345);
				line = args[0];
			} else if(args.length == 3 ){
				sock = new Socket("localhost", Integer.parseInt(args[1]));
				line = args[3];
			} else if (args.length == 5){
				sock = new Socket(args[1], Integer.parseInt(args[3]));
				line = args[4];
			} else {
				System.out.println("Invalid post arguments.");
				return;
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid Port Number");
			return;
		} catch (UnknownHostException e) {
			System.out.println("Invalid Host Name");
			return;
		}
				
		DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	
		
		
		toServer.writeBytes("get " + line + '\n');	// send the line to the server
		
		String result = "";	// read a one-line result
		
		result = fromServer.readLine();
		
		if (result.equalsIgnoreCase("error: invalid group name") || result.equalsIgnoreCase("error: invalid command")) {
			System.out.println(result);
			sock.close();
			return;
		}
		
		result = fromServer.readLine();
		
		while (result != null) {
			System.out.println(result);	
			System.out.println("");
			result = fromServer.readLine();
		}
		
		//System.out.println(result);		// print it
		sock.close();				// and we're done*/
	}
}
