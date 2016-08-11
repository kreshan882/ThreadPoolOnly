package pool;

import java.net.Socket;
import java.util.*;

public class RequestQueue{
    private String requestHandlerClass;
    private int maxQLen;
    private int minThrd;
    private int maxThrd;
    
    private int currentThreads = 0;
    private LinkedList <Socket> queue = new LinkedList <Socket>();
    private List <RequestThread> threadPool = new ArrayList <RequestThread> ();
    private boolean running = true;

    
    public RequestQueue( String requestHandlerClass, int maxQLen, int minThrd, int maxThrd ) {
       
	        this.requestHandlerClass = requestHandlerClass;
	        this.maxQLen = maxQLen;
	        this.minThrd = minThrd;
	        this.maxThrd = maxThrd;
	        this.currentThreads = this.minThrd;
	
	        
	        for( int i=0; i<this.minThrd; i++ ){
	            RequestThread thread = new RequestThread( this, i, requestHandlerClass );
	            thread.start();
	            this.threadPool.add( thread );
	        }
    }

   
    public synchronized void add( Socket o ) {

        if( queue.size() > this.maxQLen ){
            System.out.println( "Request Queue Max size = " + this.maxQLen +" is full" );
        }
        queue.addLast( o );
   
        boolean availableThread = false;
        for( Iterator <RequestThread> i=this.threadPool.iterator(); i.hasNext(); ) {
           
        	RequestThread rt = ( RequestThread )i.next();
            if( !rt.isProcessing() ){
                System.out.println( "Found an available thread" );
                availableThread = true;
                break;
            }
            System.out.println( "Thread is busy " );
        }

      
        if( !availableThread ){  //if thread are busy ,get one more
        	
            if( this.currentThreads < this.maxThrd ){
                System.out.println( "Creating a new thread to satisfy the incoming request" );
                RequestThread thread = new RequestThread( this, currentThreads++, this.requestHandlerClass );
                thread.start();
                this.threadPool.add( thread );
            }
            else{
                System.out.println( "cant get thread pool, thread reach maximum. Have to wait" );
            }
        }
        notifyAll();
    }

    
    public synchronized Object getNextObject(){
       
        while( queue.isEmpty() ){
            try{
                if( !running ) {    
                    return null;
                }
                wait();  //If queue empty, thread waiting ,until request come
            }
            catch( InterruptedException ie ) {}
        }

        return queue.removeFirst();
    }

    
    public synchronized void shutdown(){
        System.out.println( "Shutting down Server & Request threads..." );

        this.running = false;
        for( Iterator <RequestThread> i=this.threadPool.iterator(); i.hasNext(); ){
            RequestThread rt = ( RequestThread )i.next();
            rt.killThread();
        }

        notifyAll();
    }
    
    
    public String getRequestHandlerClassName() {
        return this.requestHandlerClass;
    }
}            