package Assignment1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import Assignment1.PublicParamters.*;



public class ManagerClient {


	protected static int managerIDbase =1000;
	private String managerID;	
	private File log = null;
	private Registry registry;
	private DCMSInterface intrfc;
	
	public ManagerClient(Location l) throws IOException, NotBoundException{
		managerID = l.toString() + managerIDbase;
		log = new File(managerID+".txt");
		if(! log.exists())
			log.createNewFile();
		else{
			if(log.delete())
				log.createNewFile();
		}
		registry = LocateRegistry.getRegistry(l.getPort());
		managerIDbase++;
	}
	
	public String getManagerID(){
		return managerID;
	}
	
	public void writeToLog(String str) throws IOException{
		 FileWriter writer = new FileWriter(log,true);
		 Date date = new Date();
		 writer.write(PublicParamters.dateFormat.format(date) +" : " + str  +"\n");
		 writer.flush();
		 writer.close();
	}
	
	public void changeLocation(Location l){
		String tmp = managerID.substring(3);
		managerID = l.toString() + tmp;
	}
	
	public void createTRecord(String firstName, String lastName, String address, 
			  					String phone, Specialization special, Location loc) throws RemoteException, IOException, NotBoundException{
		intrfc = (DCMSInterface)registry.lookup(managerID.substring(0, 3));
		String reply = intrfc.createTRecord(firstName, lastName, address, phone, special, loc);
		System.out.println(reply);
		writeToLog(reply);
		
	}
	
	public void createSRecord(String firstName, String lastName, Course course, 
								Status status, String statusdate) throws IOException, RemoteException, NotBoundException{
		intrfc = (DCMSInterface)registry.lookup(managerID.substring(0, 3));
		String reply = intrfc.createSRecord(firstName, lastName, course, status, statusdate);
		System.out.println(reply);
		writeToLog(reply);
	}
	
	public void getRecordCounts() throws IOException, RemoteException, NotBoundException{
		intrfc = (DCMSInterface)registry.lookup(managerID.substring(0, 3));
		String reply = intrfc.getRecordCounts();
		System.out.println(reply);
		writeToLog(reply);
	}
	
	public void EditRecord(String recordID, String fieldName, String newValue) throws IOException, RemoteException, NotBoundException{
		intrfc = (DCMSInterface)registry.lookup(managerID.substring(0, 3));
		String reply = intrfc.EditRecord(recordID, fieldName, newValue);
		System.out.println(reply);
		writeToLog(reply);
	}

}
