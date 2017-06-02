package Assignment1;

import java.util.ArrayList;

import Assignment1.PublicParamters.Location;

public class ServerManageSystem {

	public static ArrayList<ClinicServer> serverList = new ArrayList<ClinicServer>();
	
	
	public static void main(String args[]){
		try{
			
			ClinicServer mtl = new ClinicServer(Location.MTL);
			ClinicServer lvl = new ClinicServer(Location.LVL);
			ClinicServer ddo = new ClinicServer(Location.DDO);
			
			serverList.add(mtl);
			serverList.add(lvl);
			serverList.add(ddo);

			// create registry, RMI binding
			mtl.exportServer();
			lvl.exportServer();
			ddo.exportServer();

			System.out.println("Servers are up and running ");
			// UDP waiting request thread
			mtl.openUDPListener();
			lvl.openUDPListener();
			ddo.openUDPListener();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
