package pool;

import java.net.*;
import java.util.StringTokenizer;

import javax.net.*;



public abstract class AbstractServerReq extends Thread
{
   
    protected ServerSocket serverSocket;
    protected boolean running;
    protected int port;
    protected int backlog;
    protected RequestQueue requestQueue;
    
    public AbstractServerReq(){ }

    public AbstractServerReq( int port, int backlog, String requestHandlerClass, int maxQLen, int minThrd, int maxThrd ){    
        this.port = port;
        this.backlog = backlog;

       
        this.requestQueue = new RequestQueue( requestHandlerClass, maxQLen, minThrd, maxThrd );
	        StringTokenizer token = new StringTokenizer(requestQueue+"","@");
			token.nextToken();
			String pool = token.nextToken();
			System.out.println("Thread pool Id()  " +pool);	
    }

    
    public void startServer(){
        try{

            ServerSocketFactory ssf = ServerSocketFactory.getDefault();
            serverSocket = ssf.createServerSocket( this.port, this.backlog );
            this.start();
        }
        catch( Exception e )
        {
        	
            e.printStackTrace();
        }
    }

   
    public void stopServer(){
        try {
            this.running = false;
            this.serverSocket.close();
        
        }catch( Exception e ) {
            e.printStackTrace();
        }
    }

    
    public void run(){
        // Start the server
    	
        System.out.println( "Server Started, listening on port: " + this.port );
        System.out.println("waitting for recive request....................");
        this.running = true;
        while( running ){
            try{ 
               
                Socket s = serverSocket.accept();
                InetAddress addr = s.getInetAddress();
                System.out.println( "Received a new connection from: " + addr.getHostAddress());
                this.requestQueue.add( s );
                
            } catch( Exception e ){
                e.printStackTrace();
            }
        }
        System.out.println( "Shutting down..." );

      
        this.requestQueue.shutdown();
    }
}