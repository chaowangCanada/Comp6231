package Assignment1;

import java.text.SimpleDateFormat;
import java.util.Date;

import Assignment1.PublicParamters.*;

public class StudentRecord extends Record{

	private Designation designation;
	private Status status;
	private String statusDate;

	public StudentRecord(String firstName, String lastName, Designation designa, Status stat, String date) {
		super(firstName, lastName);
		this.recordID = "NR"+Integer.toString(Record.baseID++);
        this.statusDate = date;
        this.designation = designa;
        this.status = stat;
	}
	
	public StudentRecord(String firstName, String lastName) {
		super(firstName, lastName);
		this.recordID = "NR"+Integer.toString(Record.baseID++);
	}

	public StudentRecord() {
		this("N/A", "N/A");
	}

	public String getRecordID(){
		return recordID;
	}
	
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}
	
	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public void setDesignation(String newValue) {
		this.designation = Designation.valueOf(newValue);
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd"); 
		Date date = new Date();
		this.statusDate = dt.format(date);
	}
	

	public void setStatus(String newValue) {
		setStatus(Status.valueOf(newValue));
	}

	public String getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}

	
	
	
}
