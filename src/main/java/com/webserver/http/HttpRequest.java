package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Analysis Client request 
 * @author soft01
 *
 */
public class HttpRequest {
	/*
	 * Defined three fields 
	 * method url protocol to accept client request
	 */
	private String method;
	private String url;
	private String protocol;

	/*
	 *myweb/reg?username=weitianyu+&nickname=jeffrey&Phone=2772&password=4041
	 *As an example above
	 * left-hand side of ? put in requestURI
	 * right-hand side of ? put in queryString
	 * split right-hand sige values and put them into map  
	 */
	private String requestURI;
	private String queryString;
	private Map<String,String> parameter = new HashMap<>();


	/*
	 * Defined a socket which can read clients' request 
	 */
	private Socket socket;
	/*
	 * Using inputStream to read socket
	 */
	private InputStream in;

	/*
	 * Define a hashMap to contains all header names and values
	 */
	private Map<String,String> headerInfo = new HashMap<>();

	public HttpRequest(Socket socket) {

		this.socket = socket;
		//socket pass client's request information to InputStream
		try {
			in = socket.getInputStream();

			parseLine();
			parseHeader();
			parseContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * parse request line
	 */

	public void parseLine() {

		//read request Line

		try {
			String line = readLine();
			//split by "space"
			String [] subline = line.split("\\s");
			System.out.println(Arrays.toString(subline));
			this.method = subline[0];
			this.url = subline[1];
			System.out.println(url + "-------------------------");
			parseURI();
			this.protocol = subline[2];

		} catch (IOException e) {
			e.printStackTrace();
		}

		//print out all parts of request line
		System.out.println("Method: " + method);
		System.out.println("URL: " + url);
		System.out.println("Protocol: " + protocol);
		System.out.println("Parse request line finished");
	}

	//myweb/reg?username=weitianyu+&nickname=jeffrey&Phone=2772&password=4041
	private void parseURI() {

		if(url.contains("?")) {
			String [] data = url.split("\\?");
			this.requestURI = data[0];

			if(data.length > 1) {
				this.queryString = data[1];

				data = queryString.split("[&]");

				for(int i = 0; i < data.length ;i++) {

					String [] data2 = data[i].split("[=]");

					if(data2.length > 1) {
						parameter.put(data2[0], data2[1]);
					}else {
						parameter.put(data[0], null);
					}
				}
			}
		}else {
			this.requestURI = this.url;
		}
		System.out.println("requestURI: " + requestURI);
		System.out.println("queryString: " + queryString);
		System.out.println(parameter);

	}







	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}

	

	/*
	 * parse request Header
	 * We can get lots of set which combine with request header name and
	 * request header values, in this case need to define a hashMap, to put all 
	 * those values in
	 */
	public void parseHeader() {


		while(true) {

			try {
				String header = readLine();

				if("".equals(header)) {
					break;
				}
				//define a temporary array contain names and values 
				String [] temp = header.split(": ");
				//put in the map
				headerInfo.put(temp[0],temp[1]);

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		System.out.println("Finish to parse header");
	}

	/*
	 * parse requets Content
	 */
	public void parseContent() {

	}

	/*
	 * Defining readline() method to read each line in the client request 
	 */
	public String readLine() throws IOException{

		StringBuilder builder = new StringBuilder();
		int c1 = -1 , c2 = -1;

		try {
			//read each char until no character in the request
			while((c2 = in.read()) != -1) {
				/*
				 * break when we last two characters is CR and LF
				 * CR: 13 LF: 10, break.
				 */
				if(c1 == 13 && c2 == 10) {
					break;
				}
				//append all chars
				builder.append((char)c2);
				c1 = c2;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		//because last char in the builder is CR, need to delete
		return builder.toString().trim();
	}




	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}


	//Get request header values by using key
	public String getValue(String name) {

		return headerInfo.get(name);
	}

	public String getRequestURI() {
		return requestURI;
	}
    // get parameter, get user input information value
	public String getUserValue(String name) {
		return parameter.get(name);
	}


}
