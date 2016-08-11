package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import pool.RequestHandlerInterface;



public class SessionProcess implements RequestHandlerInterface {

	private Socket clientSocket 		= null;
	private byte []TPDU 				= null;
	private byte []request 				= null;
	private byte []actualRequest		= null;
	private byte []printRequest 		= null;
	private DataInputStream in 			= null;
	private DataOutputStream out		= null;
	
	
	public void handleRequest(Socket socket) {
		
		try {
			
			this.clientSocket = socket;
			clientSocket.setKeepAlive(true);
			clientSocket.setSoTimeout(8000);
			
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			out.flush();
			
			TPDU 	= new byte[5]; 
			request = new byte[2048];
			
			int relen = in.read(request, 0,2048);
			
			InetAddress addr = clientSocket.getInetAddress();
			System.out.println("clientIP: "+addr.getHostAddress());
			
			//keyWD.setDataOutputStream(out);
			
			
			if(relen != -1){
				if(relen > 2){
					actualRequest = new byte[relen-7];
					printRequest  = new byte[relen];
					
					for(int i=0 ; i<relen;i++){
						printRequest[i]=request[i];
						if(i>1){
							
							if(i<7){
								
								TPDU[i-2]= request[i];
							}else{
								actualRequest[i-7]=request[i];
							}
						}
					}

					
					//System.out.println("Recived from POS > "+ISOUtil.hexString(printRequest));
					
				
					System.out.println("Received rquest length is -> "+relen);
					System.out.println("do processing.....................................");
					Thread.sleep(10000);
//					ProcessingRequest initOB = new ProcessingRequest();
//					initOB.setREQUESTPACKET(actualRequest);
//					initOB.setREQUESTTPDU(TPDU);
//					initOB.setREQUESTLENGTH(relen);
//					initOB.setKeyWD(keyWD);
//					RecvRequest recv = new RecvRequest();
//					recv.handleRequestFordeman(initOB);	
				}
			}			
		} catch (Exception e) {
			System.out.println("Error is occurred  with  connecion....");
			e.printStackTrace();
		}finally{
				try{
					if(out !=null){
						out.flush();
						out.close();
						out= null;
					}	
					if(clientSocket !=null ){
						clientSocket.close();
						clientSocket = null;
					}	
					in = null;
					System.out.println("request handeler session close succussfully...");
				}catch(Exception e){}
				
							
		}		
	}
}
