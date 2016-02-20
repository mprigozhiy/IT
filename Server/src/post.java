import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class post {
	public static void main(String args[]) throws Exception {
		
		
		System.out.println(args.length);
		
		
		String line;	// user input
		BufferedReader userdata = new BufferedReader(new InputStreamReader(System.in));
/*	
		Socket sock = new Socket("localhost", 12345);	// connect to localhost port 12345
		DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	
		line = "hello";		// read a line from the user
		toServer.writeBytes(line + '\n');	// send the line to the server
		String result = fromServer.readLine();	// read a one-line result
		System.out.println(result);		// print it
		sock.close();				// and we're done
	*/
	}
}
