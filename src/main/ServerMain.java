package main;

public class ServerMain  {

	public static void startServer()throws Exception{
			//new RunUprocesser(port, backlog, requestHandlerClassName, maxQueueLength, minThreads, maxThreads)
			new Runprocesser(1111,2,"kre.main.SessionProcess",7,2,20 );
	}
	
	public static void main(String[] args) {
		
		try {
				startServer();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while server is initilizing............");
		}
		

	}

	
	
		
	

}
