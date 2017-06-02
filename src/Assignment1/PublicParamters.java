package Assignment1;

public interface PublicParamters {

	
	enum Location{
		MTL(SERVER_PORT_MTL), 
		LVL(SERVER_PORT_LVL), 
		DDO(SERVER_PORT_DDO);
		
		private int port;
		
		Location(int port) {
	        this.port = port;
	    }
	
	    public int getPort() {
	        return port;
	    }
	};
	
		
	enum Specialization {FRENCH, MATHS, SCIENCE};
	enum Course {FRENCH, MATHS, SCIENCE};
	enum Status {ACTIVE, INACTIVE};
	
	final int SERVER_PORT_MTL = 7000;
	final int SERVER_PORT_LVL = 7001;
	final int SERVER_PORT_DDO = 7002;
	
}
