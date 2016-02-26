import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class message {
	private String mess = "";
	private String user = "";
	private String stamp = "";
	 
	public message(String user, String mess) {
		this.mess = mess;
		this.user = user;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		this.stamp = dateFormat.format(cal.getTime());
	}
	
	public String toString() {
		return "from " + this.user + "\n" + this.mess;
	}
}
