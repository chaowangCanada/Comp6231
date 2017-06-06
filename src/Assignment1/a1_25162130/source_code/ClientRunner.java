package Assignment1;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Scanner;

import Assignment1.PublicParamters.*;

/**
 * Manager Client runner, 
 * 1. multi task test
 * 2. Text UI
 * @author Chao
 *
 */

public class ClientRunner {


	public static void main(String args[]) throws IOException, NotBoundException{
		
		ManagerClient mtlManager1 = new ManagerClient(Location.MTL);
		ManagerClient mtlManager2 = new ManagerClient(Location.MTL);
		ManagerClient mtlManager3 = new ManagerClient(Location.MTL);
		ManagerClient lvlManager1 = new ManagerClient(Location.LVL);
		ManagerClient lvlManager2 = new ManagerClient(Location.LVL);
		ManagerClient ddoManager1 = new ManagerClient(Location.DDO);
		ManagerClient ddoManager2 = new ManagerClient(Location.DDO);

		// test functionality
		mtlManager1.createSRecord("Student1", "Student1", Course.FRENCH, Status.ACTIVE, "2017-May-1");
		mtlManager1.createTRecord("Teacher1", "Teacher1", "ABC street", "123-456-7890", Specialization.FRENCH , Location.MTL);
		mtlManager2.createSRecord("Student2", "Student2", Course.MATHS, Status.INACTIVE, "2017-May-1");
		mtlManager2.createTRecord("Teacher2", "Teacher2", "ABC street", "123-456-7890", Specialization.MATHS , Location.MTL);
		
		lvlManager1.createSRecord("Student3", "Student3", Course.FRENCH, Status.ACTIVE, "2017-May-1");
		lvlManager1.createTRecord("Teacher3", "Teacher3", "ABC street", "123-456-7890", Specialization.SCIENCE , Location.LVL);
		lvlManager2.createSRecord("Student4", "Student2", Course.SCIENCE, Status.ACTIVE, "2017-May-1");
		lvlManager2.createTRecord("Teacher4", "Teacher2", "ABC street", "123-456-7890", Specialization.FRENCH , Location.LVL);
		
		ddoManager1.createSRecord("Student5", "Student5", Course.FRENCH, Status.ACTIVE, "2017-May-1");
		ddoManager1.createTRecord("Teacher5", "Teacher5", "ABC street", "123-456-7890", Specialization.FRENCH , Location.DDO);
	
		mtlManager1.getRecordCounts();
		lvlManager2.getRecordCounts();
		ddoManager2.getRecordCounts();

		ddoManager1.EditRecord("SR10008", "status date", "2010-01-01");
		ddoManager1.EditRecord("SR10008", "status", "inactive");
		ddoManager1.EditRecord("SR10008", "course", "maths");
		ddoManager1.EditRecord("SR10008", "course", "french");
		
		lvlManager1.EditRecord("TR10005", "phone", "098-765-4321");
		lvlManager1.EditRecord("TR10005", "address", "concordia street");
		lvlManager1.EditRecord("TR10005", "location", "ddo");

		// test concurrency
		mtlManager1.getRecordCounts();
		mtlManager1.EditRecord("SR10000", "status date", "2010-01-01");
		mtlManager3.createSRecord("Student6", "Student6", Course.MATHS, Status.ACTIVE, "2017-May-1");
		mtlManager2.EditRecord("SR10000", "course", "MATHS");
		mtlManager3.EditRecord("SR10000", "status", "INACTIVE");

		
		lvlManager2.getRecordCounts();
		lvlManager1.EditRecord("SR10004", "status date", "2010-01-01");
		lvlManager2.EditRecord("TR10007", "address", "SCIENCE street");
		
		ddoManager1.EditRecord("SR10008", "status date", "2010-01-01");
		ddoManager2.createSRecord("Student3", "Student3", Course.FRENCH, Status.ACTIVE, "2017-May-1");
		ddoManager2.getRecordCounts();

		/*
		// TUI menu
	try{
		int userChoice=0;
		String userInput="";
		Scanner keyboard = new Scanner(System.in);
		
	    HashMap<String, ManagerClient> managerList = new HashMap<String,ManagerClient>();
	   
	    
		showMenuLevel1();
		
		while(true)
		{
			Boolean valid = false;
			
			// Enforces a valid integer input.
			while(!valid)
			{
				try{
					userChoice=keyboard.nextInt();
					valid=true;
				}
				catch(Exception e)
				{
					System.out.println("Invalid Input, please enter an Integer");
					valid=false;
					keyboard.nextLine();
				}
			}
			
			// Manage user selection.
			switch(userChoice)
			{
			case 1: 
				System.out.println("Please enter your Locaion from: MTL, LVL, DDO");
				userInput=keyboard.next().toUpperCase();
				if (userInput.equals("MTL")||userInput.equals("LVL")||userInput.equals("DDO"))
				{
					Location loc = Location.valueOf(userInput.toUpperCase());
					ManagerClient client = new ManagerClient(loc);
					managerList.put(client.getManagerID(), client);
					client.writeToLog("Create new account. Your manager id is "+ client.getManagerID());
					System.out.println("Your account is created. Your manager id is "+ client.getManagerID());
					System.out.println("back to main menu");
				}
				else{
					System.out.println("Invalid input, please enter \"MTL\", \"LVL\", \"DDO\" ");
					System.out.println("back to main menu");					
				}
				//System.out.println(server.testInputText(userInput));
				showMenuLevel1();
				break;
			case 2:
				System.out.println("Please enter your manager id ");
				userInput=keyboard.next().toUpperCase();				
				if( managerList.containsKey(userInput)){
					managerList.get(userInput).writeToLog("manager "+userInput+" log in. ");
					menuLevel2(keyboard, managerList.get(userInput) ); //, server

				}
				else{
					System.out.println("Cannot find the user in database, back to main menu");
					showMenuLevel1();
				}

				break;
			case 3:
				System.out.println("Have a nice day!");
				keyboard.close();
				System.exit(0);
			default:
				System.out.println("Invalid Input, please try again.");
			}
		  } 	
		}
		catch (Exception e){
			e.getMessage();
		}
	}
	
	
	public static void showMenuLevel1()
	{
		System.out.println("****Welcome to Distributed Staff Management System****");
		System.out.println("Please select an option (1-2)");
		System.out.println("1. New Manager Register");
		System.out.println("2. Manager Login");
		System.out.println("3. Exit");
	}
	
	private static void menuLevel2(Scanner keyboard, ManagerClient client) throws ParseException, IOException, NotBoundException { //, DSMSInterface server
		int userChoice;
		while(true){
			System.out.println("Please enter your operation  (1-4) ");
			System.out.println("1. Create Teacher Record");
			System.out.println("2. Create Student Record");
			System.out.println("3. Get record counts");
			System.out.println("4. Edit record");
			System.out.println("5. Exit");
			userChoice=keyboard.nextInt();
		// Manage user selection.
			switch(userChoice)
			{
				case 1:
					System.out.println("Please enter the firstName, lastName, address, phone, specialization, location");
					System.out.println("Location must be MTL, LVL, DDO, Specialization: french/maths/science");
					System.out.println("Separate by ENTER key");
					keyboard.nextLine();
					String firstName = keyboard.nextLine();
					String lastName = keyboard.nextLine();
					String address = keyboard.nextLine();
					String phone = (keyboard.nextLine()).replaceAll("\\D+","");
					Specialization special = Specialization.valueOf(keyboard.nextLine().toUpperCase());
					Location location = Location.valueOf(keyboard.nextLine().substring(0, 3).toUpperCase());
					
					client.writeToLog("Manager create new Teacher Record"+ firstName +" "+ lastName +" " + address +" "+ 
											phone +" " +special.toString() +" " +location.toString());
					client.createTRecord(firstName, lastName, address, phone, special, location);
					break;
				case 2:
					System.out.println("Please enter the firstName, lastName, course registered, status, status date");
					System.out.println("Course:french/science/maths. Status: active/inactive. Date formate yyyy-mm-dd");
					System.out.println("Separate by ENTER key");
					keyboard.nextLine();
					String fn = keyboard.nextLine();
					String ln = keyboard.nextLine();
					Course course = Course.valueOf(keyboard.nextLine().toUpperCase());
					Status status = Status.valueOf(keyboard.nextLine().toUpperCase());
					String statusDate = keyboard.nextLine();

					client.writeToLog("Manager create new Student Record"+ fn +" "+ ln +" " + course.toString() +" "+ 
							status.toString() +" " +statusDate.toString() );
					client.createSRecord(fn, ln, course, status, statusDate);
					break;
				case 3:
					client.getRecordCounts();
					break;
				case 4:
					System.out.println("Please enter the record id, field name, and the new value. Separate by \"ENTER\" key");
					keyboard.nextLine();
					String recordID = keyboard.nextLine();
					String field = keyboard.nextLine();
					String newValue = keyboard.nextLine();
					client.writeToLog("Manager edit record " +" record ID "+ recordID +" filed "
											+field +" new value "+ newValue);
					client.EditRecord(recordID, field, newValue);
					break;
				case 5:
					System.out.println("Have a nice day!");
					keyboard.close();
					client.writeToLog("Manager exit");
					System.exit(0);
				default:
					System.out.println("Invalid Input, please try again.");
					
			}
		}	*/
	}

}
