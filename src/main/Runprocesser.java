package main;

import pool.AbstractServerReq;



public class Runprocesser extends AbstractServerReq{
	
	public Runprocesser(int port, int backlog, String requestHandlerClass,int maxQLen, int minThrd, int maxThrd) {
		super(port, backlog, requestHandlerClass, maxQLen, minThrd, maxThrd);
		startServer();
	}

}
