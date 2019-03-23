package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class Serverlet {

	
	public void server(HttpRequest request, HttpResponse response ) {
		
		
		String name = request.getUserValue("username");
		String nickname = request.getUserValue("nickname");
		String phone = request.getUserValue("phone");
		String password = request.getUserValue("password");
		System.out.println(name + "," + nickname + "," + phone + "," + password);
		
		
		try {
			
			RandomAccessFile raf = new RandomAccessFile("user111.dat","rw");
			raf.seek(raf.length());
			
			byte[]data = name.getBytes("utf-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			data = nickname.getBytes("utf-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			data = phone.getBytes("utf-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			data = password.getBytes("utf-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			raf.close();
			
			response.setFile(new File("./webapps/myweb/reg_success.html"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Sign up finish!!");
		
	}

}
