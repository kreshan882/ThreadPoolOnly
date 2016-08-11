package pool;

import java.net.*;


public class RequestThread extends Thread{
   
    private RequestQueue queue;
    private boolean running;
    private boolean processing = false;
    private int threadNumber;
    private RequestHandlerInterface requestHandler;

    
    public RequestThread( RequestQueue queue, int threadNumber, String requestHandlerClassName )
    {
	        this.queue = queue;
	        this.threadNumber = threadNumber;
	        try{
	            this.requestHandler = ( RequestHandlerInterface )( Class.forName( requestHandlerClassName ).newInstance() );
	        
	        }catch( Exception e ){
	        	e.printStackTrace();
	        }
    }

    public void run(){
    	
        this.running = true;
        while( running ){
        	
            try{
                Object o = queue.getNextObject();
               
                if( running ){
                    
                    Socket socket = ( Socket )o;
                    this.processing = true;
                    System.out.println( "Thread_Number[" + threadNumber + "] : Processing request handeler Started..." );
                    this.requestHandler.handleRequest( socket );
                    this.processing = false;
                    System.out.println( "Thread_Number[" + threadNumber + "] : Processing request hendeler End..." );
                }
            }
            catch( Exception e ){
            	this.processing = false;
            }finally{
            	
            	 System.out.println("Thread_Number[" + threadNumber + "] : Returns to the thread pool...");
            	 this.processing = false;
            }
        }

        System.out.println( "Thread_Number[" + threadNumber + "] : Shutting down..." );
    }
    
    
    
    public boolean isProcessing(){
        return this.processing;
    }

    
    public void killThread(){
        System.out.println( "[" + threadNumber + "]: Attempting to kill thread..." );
        this.running = false;
    }
}