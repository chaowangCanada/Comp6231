package Tutorial1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPClient {
	
	@SuppressWarnings("null")
	public static void main(String[] args){
		DatagramSocket aSocket = null;

		try {
			aSocket = new DatagramSocket();
			byte [] m = "Hello".getBytes();
			InetAddress aHost = InetAddress.getByName("Localhost");
			int serverPort = 6789;
			
			DatagramPacket request = new DatagramPacket(m, "Hello".length(), aHost, serverPort);
			aSocket.send(request);
			
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
			aSocket.receive(reply);
			System.out.println("Reply" + new String(reply.getData()));
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(aSocket !=null ) aSocket.close();
		}
		

	}
	
}
