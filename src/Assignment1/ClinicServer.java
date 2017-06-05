package Assignment1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import Assignment1.PublicParamters.*;


public class ClinicServer extends UnicastRemoteObject implements DCMSInterface{	
	
	private File logFile = null;
	private HashMap<Character, LinkedList<Record>> recordData;
	private Location location;
	private int recordCount = 0; 
	
	public ClinicServer(Location loc)throws IOException{
		super();
		location = loc;
		logFile = new File(location+"_log.txt");
		if(! logFile.exists())
			logFile.createNewFile();
		else{
			if(logFile.delete())
				logFile.createNewFile();
		}
		recordData = new HashMap<Character, LinkedList<Record>>();
	}
	
	public void exportServer() throws Exception {
		Registry registry= LocateRegistry.createRegistry(location.getPort());
		registry.bind(location.toString(), this);
	}
	

	public void openUDPListener(){

		new UDPListenerThread(this).start();

	}
	
	private class UDPListenerThread extends Thread{

		private ClinicServer server = null;
		
		private String recordCount ;
		
		public UDPListenerThread(ClinicServer threadServer) {
			server = threadServer;
		}
		
		@Override
		public void run() {
			DatagramSocket aSocket = null;

			try {
				aSocket  = new DatagramSocket(server.location.getPort());
				byte[] buffer = new byte[1000];
				
				while(true){
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);
					if(request.getData() != null){
						String requestStr = new String(request.getData(), request.getOffset(),request.getLength());
						if(requestStr.equalsIgnoreCase("RecordCounts")){ 
							server.writeToLog("Receive UDP message for : "+ requestStr );
							recordCount = Integer.toString(server.recordCount);
							DatagramPacket reply = new DatagramPacket(recordCount.getBytes(),recordCount.getBytes().length, request.getAddress(), request.getPort()); 
							aSocket.send(reply);
						}
						else if (requestStr.substring(0, 13).equalsIgnoreCase("TeacherRecord")){
							server.writeToLog("Receive UDP message for creating : "+ requestStr.substring(0, 13));
							String[] info = requestStr.split("&");
							server.createTRecord(info[1], info[2], info[3], info[4], Specialization.valueOf(info[5]), Location.valueOf(info[6]));
							String replyStr = "Successfully create Teatcher Record";
							DatagramPacket reply = new DatagramPacket(replyStr.getBytes(),replyStr.getBytes().length, request.getAddress(), request.getPort()); 
							aSocket.send(reply);
						}
						else if (requestStr.substring(0, 13).equalsIgnoreCase("StudentRecord")){
							server.writeToLog("Receive UDP message for creating : "+ requestStr.substring(0, 13));
							String[] info = requestStr.split("&");
							server.createSRecord(info[1], info[2], Course.valueOf(info[3]), Status.valueOf(info[4]), info[5]);
							String replyStr = "Successfully create Teatcher Record";
							DatagramPacket reply = new DatagramPacket(replyStr.getBytes(),replyStr.getBytes().length, request.getAddress(), request.getPort()); 
							aSocket.send(reply);
						}
					}
				}
			}catch (SocketException e ){System.out.println("Socket"+ e.getMessage());
			}catch (IOException e) {System.out.println("IO"+e.getMessage());
			}finally { if (aSocket !=null ) aSocket.close();}
		}
	}
		
	public String createTRecord(String firstName, String lastName, String address, 
							  String phone, Specialization special, Location loc) throws IOException, RemoteException{
		if(loc == this.getLocation()){
			this.writeToLog(location.toString() + " creates Teacher record.");
			Record tchrRecord = new TeacherRecord(firstName, lastName, address, phone, special, loc);
			if(recordData.get(lastName.charAt(0)) == null){
				recordData.put(lastName.charAt(0), new LinkedList<Record>());
			}
			synchronized(recordData.get(lastName.charAt(0))){
				if(recordData.get(lastName.charAt(0)).add(tchrRecord)){
					String output = "Sucessfully write Teacher record. Record ID: "+tchrRecord.getRecordID();
					this.writeToLog(output);
					recordCount++;
					return output;
				}
			}
		}
		this.writeToLog("failed to write Teacher Record");
		System.out.println("failed to write Teacher Record");
		return "failed to write Teacher Record";
	}
	
	public String createSRecord(String firstName, String lastName, Course course, 
								Status status, String statusdate) throws IOException, RemoteException{
		this.writeToLog(location.toString() + " creates Student record.");
		Record studntRecord = new StudentRecord(firstName, lastName, course, status, statusdate);
		if(recordData.get(lastName.charAt(0)) == null){
			recordData.put(lastName.charAt(0), new LinkedList<Record>());
		}
		synchronized(recordData.get(lastName.charAt(0))){
			if(recordData.get(lastName.charAt(0)).add(studntRecord)){
				String output = "Sucessfully write Student record. Record ID: "+studntRecord.getRecordID();
				this.writeToLog(output);
				recordCount++;
				return output;
			}
		}
		this.writeToLog("failed to write Student Record");
		System.out.println("failed to write Student Record");
		return "failed to write Student Record";
	}
	
	public String getRecordCounts() throws IOException, RemoteException{
		this.writeToLog("try to count all record at "+ location.toString());
		String output = this.location.toString() + " " + recordCount + ", ";
		if(ServerRunner.serverList.size() ==1 ){
			return output;
		}
		for(ClinicServer server : ServerRunner.serverList){
			if(server.getLocation() !=this.getLocation()){
				output += server.getLocation().toString() + " " + requestRecordCounts(server) + ",";
			}
		}
		return output;
	}
	
	
	public String requestRecordCounts(ClinicServer server){
		DatagramSocket aSocket = null;
		
		try{
			aSocket = new DatagramSocket();
			byte[] message = "RecordCounts".getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			int serverPort = server.getLocation().getPort();
			DatagramPacket request = new DatagramPacket(message, message.length, aHost , serverPort);
			this.writeToLog("UDP message to "+ server.getLocation().toString());
			aSocket.send(request);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			this.writeToLog("Receive UDP reply from "+ server.getLocation().toString());
			String str = new String(reply.getData(), reply.getOffset(),reply.getLength());

			return str;
		}
		catch (SocketException e){
			System.out.println("Socket"+ e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: "+e.getMessage());
		}
		finally {
			if(aSocket != null ) 
				aSocket.close();
		}
		return null;
		
	}
	
	public String EditRecord(String recordID, String fieldName, String newValue) throws IOException, RemoteException{
		this.writeToLog("try to edit record for "+recordID);
		String output = new String();

		if(recordID.substring(0,2).equalsIgnoreCase("TR")){
			if(fieldName.equalsIgnoreCase("address")||
					fieldName.equalsIgnoreCase("phone")||
					fieldName.equalsIgnoreCase("location")){
				output= traverseToEdit(recordID, fieldName, newValue, 't');
				this.writeToLog(output);
			} else{
				output ="wrong fieldName";
			}
		} 
		else if(recordID.substring(0,2).equalsIgnoreCase("SR")){
			if(fieldName.equalsIgnoreCase("course")||
					fieldName.equalsIgnoreCase("status")||
					fieldName.equalsIgnoreCase("status Date")){
				output = traverseToEdit(recordID, fieldName, newValue, 's');
				this.writeToLog(output);
			}
			else{
				output ="wrong fieldName";
			}
		}
		else{
			output ="wrong recordID";
			this.writeToLog(output);
		}
		
		return output;

	}
	
	private String traverseToEdit(String recordID, String fieldName, String newValue, char RecordInit) {
		Iterator it = recordData.entrySet().iterator();
		while(it.hasNext()){
			   Entry entry = (Entry) it.next();
			   LinkedList<Record> recordList = (LinkedList<Record>) entry.getValue();
			   
			   synchronized(recordList){
				   Iterator listIt = recordList.iterator();
				   
				   while(listIt.hasNext()){
					   Record record = (Record) listIt.next();
					   if(record.getRecordID().equalsIgnoreCase(recordID)){
						   if(RecordInit == 't'){
							   if(fieldName.equalsIgnoreCase("address")){
								   ((TeacherRecord)record).setAddress(newValue);
				        	  		return recordID+"'s address is changed to "+((TeacherRecord)record).getAddress();
							   } 
							   else if(fieldName.equalsIgnoreCase("phone")){
								   ((TeacherRecord)record).setPhone(newValue);
				        	  		return recordID+"'s phone is changed to "+((TeacherRecord)record).getPhone();
							   }
							   else if(fieldName.equalsIgnoreCase("location")){
								   newValue = newValue.toUpperCase(); // location are all upper case
								   ((TeacherRecord)record).setLocation(newValue);
				        	  		String output = recordID+"'s location is changed to "+((TeacherRecord)record).getLocation().toString();
				        			for(ClinicServer server : ServerRunner.serverList){
				        				if(server.getLocation() == Location.valueOf(newValue)){
						        	  		requestCreateRecord(server, record);
						        	  		listIt.remove();
						        	  		recordCount --;
				        				}
				        			}
				        	  		return output;
							   }
						   } 
						   else if(RecordInit == 's'){
							   if(fieldName.equalsIgnoreCase("course")){
								   newValue = newValue.toUpperCase(); // course, status are all upper case
								   if(((StudentRecord)record).findCourse(newValue))
									   ((StudentRecord)record).addCourse(newValue);
								   else
									   ((StudentRecord)record).removeCourse(newValue);
				        	  		return recordID+"'s course is changed to "+((StudentRecord)record).getCourse();
							   } 
							   else if(fieldName.equalsIgnoreCase("status")){
								   newValue = newValue.toUpperCase(); // course, status are all upper case
								   ((StudentRecord)record).setStatus(newValue);
				        	  		return recordID+"'s status is changed to "+((StudentRecord)record).getStatus().toString();
							   }
							   else if(fieldName.equalsIgnoreCase("status date")){
								   ((StudentRecord)record).setStatusDate(newValue);
				        	  		return recordID+"'s status date is changed to "+((StudentRecord)record).getStatusDate();   
							   }
						   }
						   else{
							   return "RecordId has problem";
						   }
					   }
				   }
			   }
		}

		return "cannot find such record";
	}

	private void requestCreateRecord(ClinicServer server, Record record) {

		DatagramSocket aSocket = null;
		
		try{
			aSocket = new DatagramSocket();
			String recordString  = "";
		    if(record instanceof TeacherRecord) 
		    	recordString += "TeacherRecord&"+record.getFirstName()+"&"+record.getLastName()+"&"+((TeacherRecord)record).getAddress()+
		    					"&"+((TeacherRecord)record).getPhone()+"&"+((TeacherRecord)record).getSpecialization().toString()+
		    					"&"+((TeacherRecord)record).getLocation().toString();
		    if (record instanceof StudentRecord)
		    	recordString += "StudentRecord|"+record.getFirstName()+"&"+record.getLastName()+"&"+((StudentRecord)record).getCourse()+
								"&"+((StudentRecord)record).getStatus().toString()+"&"+
								"&"+((StudentRecord)record).getStatusDate();

			byte[] message = recordString.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			int serverPort = server.getLocation().getPort();
			DatagramPacket request = new DatagramPacket(message, message.length, aHost , serverPort);
			aSocket.send(request);
			
			byte[] buffer = new byte[5000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);

			String str = new String(reply.getData(), reply.getOffset(),reply.getLength());
			System.out.println( str);
		}
		catch (SocketException e){
			System.out.println("Socket"+ e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: "+e.getMessage());
		}
		finally {
			if(aSocket != null ) 
				aSocket.close();
		}
		
	}
	
	public synchronized void writeToLog(String str) throws IOException{
		 FileWriter writer = new FileWriter(logFile,true);
		 Date date = new Date();
		 writer.write(PublicParamters.dateFormat.format(date) +" : " + str  +"\n");
		 writer.flush();
		 writer.close();
	}

	public File getLogFile() {
		return logFile;
	}

	public void setLog(File log) {
		this.logFile = log;
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

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	
	
}
