package Assignment1;

import java.util.ArrayList;

public class ManagerServer {

	public static ArrayList<ClinicServer> serverList;
	

	static int SERVER_PORT_MTL = 7000;
	static int SERVER_PORT_LVL = 7001;
	static int SERVER_PORT_DDO = 7002;
	
	public static void main(String args[]){
		try{
			
			ClinicServer mtl = new ClinicServer("MTL");
			ClinicServer lvl = new ClinicServer("LVL");
			ClinicServer ddo = new ClinicServer("DDO");
			serverList.add(mtl);
			serverList.add(lvl);
			serverList.add(ddo);

			mtl.exportServer();
			lvl.exportServer();
			ddo.exportServer();

			System.out.println("Servers are up and running ");
			mtl.openUDPListener();
			lvl.openUDPListener();
			ddo.openUDPListener();

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
