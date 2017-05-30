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
		recordData = new HashMap<Character, LinkedList<Record>>();
	}
	
	public void exportServer() throws Exception {
		Registry registry= LocateRegistry.createRegistry(location.getPort());
		registry.bind(location.toString(), this);
	}
	

	public void openUDPListener(){
		DatagramSocket aSocket = null;
		
		try{
			aSocket  = new DatagramSocket(this.location.getPort());
			byte[] buffer = new byte[1000];
			while(true){
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				if(request.getData() != null && request.getData().toString().equals("RecordCounts")){
					this.writeToLog("New thread starts for : "+request.getData().toString());
					new thread(aSocket, request, this);
				}
			}
		}catch (SocketException e ){System.out.println("Socket"+ e.getMessage());
		}catch (IOException e) {System.out.println("IO"+e.getMessage());
		}finally { if (aSocket !=null ) aSocket.close();}
	}
	
	static class thread extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		ClinicServer server = null;
		
		String recordCount ;
		
		public thread(DatagramSocket aSocket, DatagramPacket aRequest, ClinicServer threadServer)    {
			socket=aSocket;
			request = aRequest;
			server = threadServer;
			if(request.getData().toString().equals("RecordCounts"))
					recordCount = server.getLocation().toString() + " " + server.recordCount + ",";
			this.start();
		}
		
		@Override
		public void run() {
			try {
				DatagramPacket reply = new DatagramPacket(recordCount.getBytes(),recordCount.getBytes().length, request.getAddress(), request.getPort()); 
				socket.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	public String createTRecord(String firstName, String lastName, String adTRess, 
							  String phone, Specialization special, Location loc) throws IOException, RemoteException{
		this.writeToLog(location.toString() + " creates Teacher record.");
		Record docRecord = new TeacherRecord(firstName, lastName, adTRess, phone, special, loc);
		if(recordData.get(lastName.charAt(0)) == null){
			recordData.put(lastName.charAt(0), new LinkedList<Record>());
		}
		if(recordData.get(lastName.charAt(0)).add(docRecord)){
			String output = "Sucessfully write Teacher record. Record ID: "+docRecord.getRecordID();
			this.writeToLog(output);
			recordCount++;
			return output;
		}
		return "failed to write Teacher Record";
	}
	
	public String createSRecord(String firstName, String lastName, Designation designation, 
								Status status, String statusdate) throws IOException, RemoteException{
		this.writeToLog(location.toString() + " creates Teacher record.");
		Record nurRecord = new StudentRecord(firstName, lastName, designation, status, statusdate);
		if(recordData.get(lastName.charAt(0)) == null){
			recordData.put(lastName.charAt(0), new LinkedList<Record>());
		}
		if(recordData.get(lastName.charAt(0)).add(nurRecord)){
			String output = "Sucessfully write Teacher record. Record ID: "+nurRecord.getRecordID();
			this.writeToLog(output);
			recordCount++;
			return output;
		}
		return "failed to write Student Record";
	}
	
	public String getRecordCounts() throws IOException, RemoteException{
		this.writeToLog("try to count all record at "+ location.toString());
		String output = this.location.toString() + " " + recordCount + ", ";
		for(ClinicServer server : ServerManageSystem.serverList){
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
			aSocket.send(request);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);

			String str = reply.getData().toString();
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
			if(fieldName.equalsIgnoreCase("adTRess")||
					fieldName.equalsIgnoreCase("phone")||
					fieldName.equalsIgnoreCase("location")){
				output= traverseToEdit(recordID, fieldName, newValue, 'd');
				this.writeToLog(output);
			}
		} 
		else if(recordID.substring(0,2).equalsIgnoreCase("SR")){
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
						   if(fieldName.equalsIgnoreCase("adTRess")){
							   ((TeacherRecord)record).setAddress(newValue);
			        	  		return recordID+"'s adTRess is changed to "+((TeacherRecord)record).getAddress();
						   } 
						   else if(fieldName.equalsIgnoreCase("phone")){
							   ((TeacherRecord)record).setPhone(newValue);
			        	  		return recordID+"'s phone is changed to "+((TeacherRecord)record).getPhone();
						   }
						   else if(fieldName.equalsIgnoreCase("location")){
							   ((TeacherRecord)record).setLocation(newValue);
			        	  		String output = recordID+"'s location is changed to "+((TeacherRecord)record).getLocation().toString();
			        			for(ClinicServer server : ServerManageSystem.serverList){
			        				if(server.getLocation() == Location.valueOf(newValue)){
					        	  		requestCreateRecord(server, record);
			        				}
			        			}
			        	  		listIt.remove();
			        	  		return output;
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

	private void requestCreateRecord(ClinicServer server, Record record) {

		DatagramSocket aSocket = null;
		
		try{
			aSocket = new DatagramSocket();
			byte[] message = "CreateRecord".getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			int serverPort = server.getLocation().getPort();
			DatagramPacket request = new DatagramPacket(message, message.length, aHost , serverPort);
			aSocket.send(request);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);

			System.out.println( reply.getData().toString());
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
		 writer.write(str+"\n");
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
