package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.Serverlet;

/**
 *  Accept and response to Client request.
 *  By using parallel processing implements Runnable interface
 * @author Jeffrey Wei
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {

		this.socket = socket;
	}
	//Override run() method
	public void run() {

		try {
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);

			String path = request.getRequestURI();
   System.out.println("-----------------------------" + path);
			if("/myweb/reg".equals(path)) {
				
				Serverlet let =  new Serverlet();
				let.server(request, response);
				
          
			}else {


				File file = new File("./webapps" + path);

				if(file.exists()) {
					System.out.println("File exits");
					response.setFile(file);
				}else {
					System.out.println("Source not found");
					//file not exist, turn to 404 net page
					response.setFile(new File("./webapps/root/404.html"));
					//change status code and status condition
					response.setStatusCode(404);
					response.setStatusReason("Not found");
				}
			}

			response.flush();

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



	}










}
