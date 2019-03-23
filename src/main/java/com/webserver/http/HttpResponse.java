package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpResponse {

	/*
	 * set default statusCode = 200
	 * default statusReason = "OK"
	 */
	private int statusCode = 200;
	private String statusReason = "OK";

	//Read current file
	private File current;
	/*
	 * Use socket to response to client;
	 */
	private Socket socket;

	/*
	 * Use OutputStream to write 
	 */
	private OutputStream out;
	/*
	 * Using map to contain Content-type and values
	 */
	private Map<String, String> HeaderRespon = new HashMap<>();

	public HttpResponse(Socket socket) {


		try {
			this.socket = socket;
			out = socket.getOutputStream();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void flush() {

		sendStatusLine();
		sendHeader();
		sendConext();
	}

	/*
	 * send status line to client
	 */
	public void sendStatusLine() {
		try {
			String str = "HTTP/1.1" + " " + statusCode + " " + statusReason;  
			out.write(str.getBytes("ISO8859-1"));
			out.write(13);
			out.write(10);
		}  catch (IOException e) {
			e.printStackTrace();
		}


	}

	/*
	 * send response header
	 */

	public void sendHeader() {

		try {
			Set<Entry<String, String>> entrySet = HeaderRespon.entrySet();

			for(Entry<String,String> e: entrySet) {
				String name = e.getKey();
				String value = e.getValue();
				String line = name + ":" + value;
				System.out.println(line);
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
			}
			
			out.write(13);
			out.write(10);

		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	/*
	 * send response context
	 */

	public void sendConext() {

		// if file exist
		if(current != null) {
			try(FileInputStream fis = new FileInputStream(current);){

				byte[] data = new byte[1024*10];
				int d = -1;

				while((d = fis.read(data))!= -1) {
					out.write(data, 0, d);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}	

		}
		
	}

	public void setFile(File file) {
		this.current = file;
		//split file name by : and take last element
		String fileName = file.getName();
		String [] arr = fileName.split("\\.");
		String key = arr[arr.length -1];
		String value = HttpContext.getValue(key);
		putHeader("Content-Type", value);
		putHeader("Content-Length", file.length()+"");

	}


	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	
	//set map as private, define putheader method to put key-values into map
	public void putHeader(String name, String value) {

		HeaderRespon.put(name, value);
	}
	public String getName(String name) {
		return HeaderRespon.get(name);
	}


}
