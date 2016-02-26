import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class get {
	public static void main(String args[]) throws Exception {
		String line;	// user input
		
		//BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		//line = br.readLine();
		
		//System.out.println(line);
		Socket sock = null;
		if(args.length == 1){
			sock = new Socket("localhost", 12345);
			line = args[0];
			System.out.println(line);
		} else if(args.length == 3 ){
			sock = new Socket("localhost", Integer.parseInt(args[1]));
			line = args[3];
		} else if (args.length == 5){
			sock = new Socket(args[1], Integer.parseInt(args[3]));
			line = args[4];
		} else {
			System.out.println("Invalid post arguments.");
		}
		
		//BufferedReader userdata = new BufferedReader(new InputStreamReader(System.in));
	
		//Socket sock = new Socket("localhost", 12345);	// connect to localhost port 12345
		DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	
		line = "hello";		// read a line from the user
		
		toServer.writeBytes(line + '\n');	// send the line to the server
		
		String result = "";	// read a one-line result
		
		while (result != null) {
			result = fromServer.readLine();
			System.out.println(result);	
		}
		
		//System.out.println(result);		// print it
		sock.close();				// and we're done*/
	}
}
