package Assignment1;


public class Record {

	protected static int baseID = 10000;
	private String firstName;
	private String lastName;
	protected String recordID;
	
	protected Record(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return	 firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRecordID(){
	    if(this instanceof TeacherRecord) 
	    	((TeacherRecord)this).getRecordID();
	    if (this instanceof StudentRecord)
	    	((StudentRecord)this).getRecordID();

		return "";
	}

}
