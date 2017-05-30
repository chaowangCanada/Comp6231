package Assignment1;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import Assignment1.PublicParamters.*;


public interface DCMSInterface extends Remote{
	
	public String createTRecord(String firstName, String lastName, String adTRess, 
			  String phone, Specialization special, Location loc) throws RemoteException, IOException;
	
	public String createSRecord(String firstName, String lastName, Designation designation, 
			Status status, String statusdate) throws IOException, RemoteException;
	
	public String getRecordCounts() throws IOException, RemoteException;
	
	public String EditRecord(String recordID, String fieldName, String newValue) throws IOException, RemoteException;	

}
