import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class message {
	private String mess = "";
	private String user = "";
	Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	 
	public message(String user, String mess) {
		this.mess = mess;
		this.user = user;
		 
	}
	
	public String toString() {
		return "from " + this.user + "\n" + this.mess;
	}
}
