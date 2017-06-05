package Tutorial1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatagramSocket aSocket = null;
		try{
			aSocket = new DatagramSocket(6789);
			byte[] buffer = new byte[1000];
			
			while(true){
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				DatagramPacket reply = new DatagramPacket(request.getData(), request.getData().length,
											request.getAddress(), request.getPort());
				aSocket.send(reply);
			}
		}
		catch(SocketException e){
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(aSocket != null ) aSocket.close();
		}
	}

}
