package Assignment1;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

import Assignment1.PublicParamters.*;

// how did you editRecord location?
// how to multithread getRecordCount()?
// how to synchronized, LinkedList is not thread safe.Try ConcurrentLinkedQueue or LinkedBlockingDeque, ConcurrentHashMap
// Whenever a manager performs an operation, the system must identify the center that manager belongs to by looking at the managerID

public interface DCMSInterface extends Remote{
	
	public String createTRecord(String firstName, String lastName, String address, 
			  String phone, Specialization special, Location loc) throws RemoteException, IOException;
	
	public String createSRecord(String firstName, String lastName, Course course, 
			Status status, String statusdate) throws IOException, RemoteException;
	
	public String getRecordCounts() throws IOException, RemoteException, InterruptedException, ExecutionException;
	
	public String EditRecord(String recordID, String fieldName, String newValue) throws IOException, RemoteException;	

}
