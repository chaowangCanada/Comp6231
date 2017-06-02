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
		registry = LocateRegistry.getRegistry(l.getPort());
		managerIDbase++;
	}
	
	public String getManagerID(){
		return managerID;
	}
	
	public void writeToLog(String str) throws IOException{
		 FileWriter writer = new FileWriter(log,true);
		 writer.write(str+"\n");
		 writer.flush();
		 writer.close();
	}
	
	public void changeLocation(Location l){
		String tmp = managerID.substring(3);
		managerID = l.toString() + tmp;
	}
	
	public void createTRecord(String firstName, String lastName, String adTRess, 
			  					String phone, Specialization special, Location loc) throws RemoteException, IOException, NotBoundException{
		intrfc = (DCMSInterface)registry.lookup(managerID.substring(0, 3));
		String reply = intrfc.createTRecord(firstName, lastName, adTRess, phone, special, loc);
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

	public static void main(String args[]) throws IOException, NotBoundException{
		
		ManagerClient mtlManager1 = new ManagerClient(Location.MTL);
		ManagerClient mtlManager2 = new ManagerClient(Location.MTL);
		ManagerClient lvlManager1 = new ManagerClient(Location.LVL);
		ManagerClient lvlManager2 = new ManagerClient(Location.LVL);
		ManagerClient ddoManager1 = new ManagerClient(Location.DDO);
		ManagerClient ddoManager2 = new ManagerClient(Location.DDO);

		mtlManager1.createSRecord("Student", "Student", Course.FRENCH, Status.ACTIVE, "2017-May-1");
		mtlManager2.createTRecord("Teacher", "Teacher", "ABC", "123", Specialization.FRENCH , Location.MTL);
		lvlManager1.getRecordCounts();
		lvlManager2.getRecordCounts();
		mtlManager1.EditRecord("TR10000", "Specialization", "MATHS");
		mtlManager2.EditRecord("SR10001", "COURSE", "MATHS");

//	try{
//		int userChoice=0;
//		String userInput="";
//		Scanner keyboard = new Scanner(System.in);
//		
//	    HashMap<String, ManagerClient> managerList = new HashMap<String,ManagerClient>();
//	   
//	    
//		showMenuLevel1();
//		
//		while(true)
//		{
//			Boolean valid = false;
//			
//			// Enforces a valid integer input.
//			while(!valid)
//			{
//				try{
//					userChoice=keyboard.nextInt();
//					valid=true;
//				}
//				catch(Exception e)
//				{
//					System.out.println("Invalid Input, please enter an Integer");
//					valid=false;
//					keyboard.nextLine();
//				}
//			}
//			
//			// Manage user selection.
//			switch(userChoice)
//			{
//			case 1: 
//				System.out.println("Please enter your Locaion from: MTL, LVL, DDO");
//				userInput=keyboard.next().toUpperCase();
//				if (userInput.equals("MTL")||userInput.equals("LVL")||userInput.equals("DDO"))
//				{
//					Location local = Location.valueOf(userInput.toUpperCase());
//					ManagerClient client = new ManagerClient(local);
//					managerList.put(client.getManagerID(), client);
//					client.writeToLog("Create new account. \nYour account is created. Your manager id is "+ client.getManagerID());
//					System.out.println("Your account is created. Your manager id is "+ client.getManagerID());
//					System.out.println("back to main menu");
//				}
//				else{
//					System.out.println("Invalid input, please enter \"MTL\", \"LVL\", \"DDO\" ");
//					System.out.println("back to main menu");					
//				}
//				//System.out.println(server.testInputText(userInput));
//				showMenuLevel1();
//				break;
//			case 2:
//				System.out.println("Please enter your manager id ");
//				userInput=keyboard.next().toUpperCase();
//				Location local = Location.valueOf(userInput.substring(0, 3));
//				
//				if( managerList.containsKey(userInput)){
//					managerList.get(userInput).writeToLog("manager "+userInput+" log in. ");
//					DSMSInterface server = (DSMSInterface) Naming.lookup(("rmi://localhost:"
//							+getPort(local)+"/"+local.getLocation()).toString());
//					menuLevel2(keyboard, server, managerList.get(userInput) ); //, server
//
//				}
//				else{
//					System.out.println("Cannot find the user in database, back to main menu");
//					showMenuLevel1();
//				}
//
//				break;
//			case 3:
//				System.out.println("Have a nice day!");
//				keyboard.close();
//				System.exit(0);
//			default:
//				System.out.println("Invalid Input, please try again.");
//			}
//		  } 	
//		}
//		catch (Exception e){
//		e.printStackTrace();
//		}
//	}
//	
//	
//	public static void showMenuLevel1()
//	{
//		System.out.println("****Welcome to Distributed Staff Management System****");
//		System.out.println("Please select an option (1-2)");
//		System.out.println("1. New Manager Register");
//		System.out.println("2. Exist Manager Login");
//		System.out.println("3. Exit");
//	}
//	
//	private static void menuLevel2(Scanner keyboard, DSMSInterface server, ManagerClient client) throws ParseException, IOException { //, DSMSInterface server
//		int userChoice;
//		while(true){
//			System.out.println("Please enter your operation  (1-4) ");
//			System.out.println("1. Create Doctor Record");
//			System.out.println("2. Create Nurse Record");
//			System.out.println("3. Get record counts");
//			System.out.println("4. Edit record");
//			System.out.println("5. Exit");
//			userChoice=keyboard.nextInt();
//		// Manage user selection.
//			switch(userChoice)
//			{
//				case 1:
//					System.out.println("Please enter the firstName, lastName, address, phone, specialization, location");
//					System.out.println("Location must be MTL, LVL, DDO");
//					System.out.println("Separate by ENTER key");
//					keyboard.nextLine();
//					String firstName = keyboard.nextLine();
//					String lastName = keyboard.nextLine();
//					String address = keyboard.nextLine();
//					String phone = (keyboard.nextLine()).replaceAll("\\D+","");
//					String specialization = keyboard.nextLine();
//					Location location = Location.valueOf(keyboard.nextLine().substring(0, 3));
//					
//					client.writeToLog("Manager create new Doctor Record"+ firstName +" "+ lastName +" " + address +" "+ 
//											phone +" " +specialization +" " +location.getLocation());
//					String str1 = server.createDRecord(firstName, lastName, address
//											, phone, specialization, location);
//					client.writeToLog(str1);
//					System.out.println(str1);
//					break;
//				case 2:
//					System.out.println("Please enter the firstName, lastName, designation, status, status date");
//					System.out.println("Designation:junior/senior. Status: active/terminated. Date formate yyyy-mm-dd");
//					System.out.println("Separate by ENTER key");
//					keyboard.nextLine();
//					String fn = keyboard.nextLine();
//					String ln = keyboard.nextLine();
//					Designation designation = Designation.valueOf(keyboard.nextLine().toLowerCase());
//					Status status = Status.valueOf(keyboard.nextLine().toLowerCase());
//					Date statusDate = new SimpleDateFormat("yyyy-MM-dd").parse(keyboard.nextLine().toString());
//
//					client.writeToLog("Manager create new Nurse Record"+ fn +" "+ ln +" " + designation.getDesignation() +" "+ 
//							status.getStatus() +" " +statusDate.toString() );
//	
//					String str2 = server.createNRecord(fn, ln, designation, status, statusDate);
//					client.writeToLog(str2);
//					System.out.println(str2);
//					break;
//				case 3:
//					System.out.println("Please enter the record type DR/NR");
//					keyboard.nextLine();
//					String type = keyboard.nextLine();
//					client.writeToLog("Manager account record type " +type);
//					String str3=server.getRecordCounts(type);
//					client.writeToLog(str3);
//					System.out.println(str3);
//					break;
//				case 4:
//					System.out.println("Please enter the record id, field name, and the new value. Separate by \"ENTER\" key");
//					keyboard.nextLine();
//					String recordID = keyboard.nextLine();
//					String field = keyboard.nextLine();
//					String newValue = keyboard.nextLine();
//					client.writeToLog("Manager edit record " +" record ID "+ recordID +" filed "
//											+field +" new value "+ newValue);
//					String str4 = server.editRecord(recordID, field, newValue);
//					client.writeToLog(str4);
//					System.out.println(str4);
//					break;
//				case 5:
//					System.out.println("Have a nice day!");
//					keyboard.close();
//					client.writeToLog("Manager exit");
//					client.log.exists();
//					System.exit(0);
//				default:
//					System.out.println("Invalid Input, please try again.");
//					
//			}
//		}
//	}
//	

	}
}
