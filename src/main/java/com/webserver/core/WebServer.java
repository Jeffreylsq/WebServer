package com.webserver.core;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * To simulate how webServer and client working with each other
 * This project provide a simple WebServer which can handle several web net page such as 
 *  login page,login successfully 404 no found page, main web page, and students shopping 
 *  website. I also import dom4j package to make our Http response Content-type good enough
 *  
 * @author Jeffrey Wei
 *
 */
public class WebServer {


	private ServerSocket server;

	public WebServer() {

		/*
		 * Set WebServer a port which is 12345
		 */
		try {
			System.out.println("Starting WebServer....");
			server = new ServerSocket(12001);
			System.out.println("WebServer started!!");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void start() {

		/*
		 * To make a socket to accept client request
		 * and catch IOException
		 */
		// Server will always accept clients'request 
		
		while(true) {
			try {
				System.out.println("Waiting for client....");
				Socket socket = server.accept();
				System.out.println("Clients connected with WebServer!!");
                
				ClientHandler handler = new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	public static void main(String[] args) {
        // instantiated Object
		 WebServer server = new WebServer();
			 server.start();
	}

}
