package Assignment1;

import Assignment1.PublicParamters.*;

public class DoctorRecord extends Record{

	private String address;
	private String phone;
	private Specialization specialization;
	private Location location;
	
	
	public DoctorRecord(String firstName, String lastName) {
		super(firstName, lastName);
		this.recordID = "DR"+Integer.toString(Record.baseID++);
	}
	
	public DoctorRecord() {
		this("N/A", "N/A");
	}

	public DoctorRecord(String firstName, String lastName, String address, String phone,Specialization special, Location loc) {
		super(firstName, lastName);
		this.recordID = "DR"+Integer.toString(Record.baseID++);
        this.address = address;
        this.phone = phone;
        this.specialization = special;
        this.location = loc;
	}
	
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getRecordID(){
		return recordID;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public Specialization getSpecialization() {
		return specialization;
	}


	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}

	public void setLocation(String newValue) {
		this.location = Location.valueOf(newValue);
	}
	
	

}
