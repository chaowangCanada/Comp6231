package Assignment1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import Assignment1.PublicParamters.*;

public class ClinicServer {

	private File log = null;
	private HashMap<Character, LinkedList<Record>> recordData;
	private Location location;
	private int drCount = 0, nrCount = 0; 
	
	public ClinicServer(String loc)throws IOException{
		location = Location.valueOf(loc);
		log = new File(location+"_log.txt");
		if(! log.exists())
			log.createNewFile();
		recordData = new HashMap<Character, LinkedList<Record>>();
	}
	
	public String createDRecord(String firstName, String lastName, String address, 
							  String phone, Specialization special, Location loc) throws IOException{
		this.writeToLog(location.toString() + " creates Doctor record.");
		Record docRecord = new TeacherRecord(firstName, lastName, address, phone, special, loc);
		if(recordData.get(lastName.charAt(0)) == null){
			recordData.put(lastName.charAt(0), new LinkedList<Record>());
		}
		if(recordData.get(lastName.charAt(0)).add(docRecord)){
			String output = "Sucessfully write Doctor record. Record ID: "+docRecord.getRecordID();
			this.writeToLog(output);
			drCount++;
			return output;
		}
		return "failed to write Doctor Record";
	}
	
	public String createNRecord(String firstName, String lastName, Designation designation, 
								Status status, String statusdate) throws IOException{
		this.writeToLog(location.toString() + " creates Doctor record.");
		Record nurRecord = new StudentRecord(firstName, lastName, designation, status, statusdate);
		if(recordData.get(lastName.charAt(0)) == null){
			recordData.put(lastName.charAt(0), new LinkedList<Record>());
		}
		if(recordData.get(lastName.charAt(0)).add(nurRecord)){
			String output = "Sucessfully write Doctor record. Record ID: "+nurRecord.getRecordID();
			this.writeToLog(output);
			nrCount++;
			return output;
		}
		return "failed to write Nurse Record";
	}
	
	public String getRecordCounts(String recordType) throws IOException{
		this.writeToLog("try to count all record for "+recordType);
		String output = new String();
		if(recordType.equalsIgnoreCase("DR")){
			output += this.location.toString() + " " + drCount + ",";
			for(ClinicServer server : ManagerServer.serverList){
				if(server.getLocation() !=this.getLocation()){
					output += server.getLocation().toString() + " " + server.getDrCount() + ",";
				}
			}
		}
		else if(recordType.equalsIgnoreCase("NR")){
			output += this.location.toString() + " " + nrCount + ",";
			for(ClinicServer server : ManagerServer.serverList){
				if(server.getLocation() !=this.getLocation()){
					output += server.getLocation().toString() + " " + server.getNrCount() + ",";
				}
			}
		}
		else{
			output += this.location.toString() + " " + (nrCount+drCount) + ",";
			for(ClinicServer server : ManagerServer.serverList){
				if(server.getLocation() !=this.getLocation()){
					output += server.getLocation().toString() + " " + (server.getNrCount()+server.getDrCount()) + ",";
				}
			}
		}
		return output;

	}
	
	
	public String EditRecord(String recordID, String fieldName, String newValue) throws IOException{
		this.writeToLog("try to edit record for "+recordID);
		String output = new String();

		if(recordID.substring(0,2).equalsIgnoreCase("DR")){
			if(fieldName.equalsIgnoreCase("address")||
					fieldName.equalsIgnoreCase("phone")||
					fieldName.equalsIgnoreCase("location")){
				output= traverseToEdit(recordID, fieldName, newValue, 'd');
				this.writeToLog(output);
			}
		} 
		else if(recordID.substring(0,2).equalsIgnoreCase("NR")){
			if(fieldName.equalsIgnoreCase("designation")||
					fieldName.equalsIgnoreCase("status")||
					fieldName.equalsIgnoreCase("statusDate")){
				output = traverseToEdit(recordID, fieldName, newValue, 'n');
				this.writeToLog(output);
			}
		}
		else{
			output +="wrong recordID, or wrong fieldName";
			this.writeToLog(output);
		}
		
		return output;

	}
	
	private String traverseToEdit(String recordID, String fieldName, String newValue, char RecordInit) {
		Iterator it = recordData.entrySet().iterator();
		while(it.hasNext()){
			   Entry entry = (Entry) it.next();
			   LinkedList<Record> recordList = (LinkedList<Record>) entry.getValue();
			   Iterator listIt = recordList.iterator();
			   
			   while(listIt.hasNext()){
				   Record record = (Record) listIt.next();
				   if(record.getRecordID().equalsIgnoreCase(recordID)){
					   if(RecordInit == 'd'){
						   if(fieldName.equalsIgnoreCase("address")){
							   ((TeacherRecord)record).setAddress(newValue);
			        	  		return recordID+"'s address is changed to "+((TeacherRecord)record).getAddress();
						   } 
						   else if(fieldName.equalsIgnoreCase("phone")){
							   ((TeacherRecord)record).setPhone(newValue);
			        	  		return recordID+"'s phone is changed to "+((TeacherRecord)record).getPhone();
						   }
						   else if(fieldName.equalsIgnoreCase("location")){
							   ((TeacherRecord)record).setLocation(newValue);
			        	  		return recordID+"'s location is changed to "+((TeacherRecord)record).getLocation().toString();
						   }
					   } 
					   else if(RecordInit == 'n'){
						   if(fieldName.equalsIgnoreCase("designation")){
							   ((StudentRecord)record).setDesignation(newValue);
			        	  		return recordID+"'s designation is changed to "+((StudentRecord)record).getDesignation().toString();
						   } 
						   else if(fieldName.equalsIgnoreCase("status")){
							   ((StudentRecord)record).setStatus(newValue);
			        	  		return recordID+"'s status is changed to "+((StudentRecord)record).getStatus().toString();
						   }
						   else if(fieldName.equalsIgnoreCase("status date")){
							   ((StudentRecord)record).setStatusDate(newValue);
			        	  		return recordID+"'s status date is changed to "+((StudentRecord)record).getStatusDate();   
						   }
					   }
				   }
			   }
		}

		return null;
	}

	public void writeToLog(String str) throws IOException{
		 FileWriter writer = new FileWriter(log,true);
		 writer.write(str+"\n");
		 writer.flush();
		 writer.close();
	}

	public File getLog() {
		return log;
	}

	public void setLog(File log) {
		this.log = log;
	}

	public HashMap<Character, LinkedList<Record>> getRecordData() {
		return recordData;
	}

	public void setRecordData(HashMap<Character, LinkedList<Record>> recordData) {
		this.recordData = recordData;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getDrCount() {
		return drCount;
	}

	public void setDrCount(int drCount) {
		this.drCount = drCount;
	}

	public int getNrCount() {
		return nrCount;
	}

	public void setNrCount(int nrCount) {
		this.nrCount = nrCount;
	}
	
	
}
