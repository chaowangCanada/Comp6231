package Assignment1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import Assignment1.PublicParamters.*;

public class StudentRecord extends Record{

	private ArrayList<Course> courseList;
	private Status status;
	private String statusDate;

	public StudentRecord(String firstName, String lastName, Course course, Status stat, String date) {
		super(firstName, lastName);
		this.recordID = "NR"+Integer.toString(Record.baseID++);
        this.statusDate = date;
        this.courseList = new ArrayList<Course>();
        this.courseList.add(course);
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
	
	public boolean findCourse(String newValue) {
		Iterator it = courseList.iterator();
		while(it.hasNext()){
			if(Course.valueOf(newValue) == it.next())
				return true;
		}
		return false;
	}

	public void addCourse(Course newCourse) {
		Iterator<Course> it = courseList.iterator();
		while(it.hasNext()){
			if(newCourse == it.next())
				return;
		}
		courseList.add(newCourse);
		
	}
	
	public void addCourse(String newValue) {
		Iterator it = courseList.iterator();
		while(it.hasNext()){
			if(Course.valueOf(newValue) == it.next())
				return;
		}
		courseList.add(Course.valueOf(newValue));
	}
	
	public void removeCourse(Course newCourse) {
		Iterator<Course> it = courseList.iterator();
		while(it.hasNext()){
			if(newCourse == it.next())
				it.remove();
		}		
	}
	
	public void removeCourse(String newValue) {
		Iterator it = courseList.iterator();
		while(it.hasNext()){
			if(Course.valueOf(newValue) == it.next())
				it.remove();
		}
	}
	
	public String getCourse(){
		return Arrays.toString(courseList.toArray());
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
