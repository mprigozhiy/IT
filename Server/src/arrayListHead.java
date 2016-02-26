import java.util.ArrayList;

public class arrayListHead {
	private int inUse;
	private ArrayList<message> ref;
	
	public arrayListHead (ArrayList<message> ref) {
		inUse = 1;
		this.ref = ref;
	}
	
	public ArrayList<message> getArray() {
		inUse = 1;
		return this.ref;
	}
	
	public boolean inUse () {
		return inUse == 1;
	}
	
	public void setOpen() {
		inUse = 0;
	}
}
