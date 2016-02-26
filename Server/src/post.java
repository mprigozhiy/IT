import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class post {
	public static void main(String args[]) throws Exception {
		String group = null;
		
		if(args.length == 1){
			//Socket sock = new Socket("localhost", 12345);
			System.out.println("localhost, 12345");
			group = args[0];
		} else if(args.length == 3 ){
			//Socket sock = new Socket("localhost", Integer.parseInt(args[1]));
			System.out.println("localhost, " + Integer.parseInt(args[1]));
			group = args[3];
		} else if (args.length == 5){
			//Socket sock = new Socket(args[1], Integer.parseInt(args[3]));
			System.out.println(args[1] + ", " + Integer.parseInt(args[3]));
			group = args[4];
		} else {
			System.out.println("Invalid post arguments.");
		}
		
		
		
		
		String line;	// user input
		
	
		Socket sock = new Socket("localhost", 12345);	// connect to localhost port 12345
		DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
		BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	
		line = "post " + group;		// read a line from the user
		toServer.writeBytes(line + '\n');	// send the line to the server
		String result = fromServer.readLine();	// read a one-line result
		
		if(result.equals("ok")){
			line = System.getProperty("user.name");
			System.out.println(System.getProperty("user.name"));
			toServer.writeBytes(line + '\n');
		} else if(result.equals("")){
			System.out.println("Invalid username.");
			System.exit(1);
		}
		
		result = fromServer.readLine();
		if(result.equals("ok")){
			System.out.println("Hello");
			BufferedReader userdata = new BufferedReader(new InputStreamReader(System.in));
			String enterIn = userdata.readLine();
			toServer.writeBytes(enterIn + "\n");
		}  else if(result.equals("")){
			System.out.println("Invalid username.");
			System.exit(1);
		}
		
		
		//result = fromServer.readLine();
		System.out.println(result);		// print it
		sock.close();				// and we're done
	
	}
}
