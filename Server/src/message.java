/*Jonathan Caverly, Mikhail Prigozhiy, Jonathan J. Getahun*/

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class message {
	private String mess = "";
	private String user = "";
	private String stamp = "";
	private String location = "";
	 
	public message(String user, String mess, String location) {
		this.mess = mess;
		this.user = user;
		DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
		Calendar cal = Calendar.getInstance();
		this.stamp = dateFormat.format(cal.getTime());
		this.location = location;
	}
	
	public String toString() {
		return "\nfrom " + this.user + " " + this.location +  " " + this.stamp + "\n\n" + this.mess;
	}
}
