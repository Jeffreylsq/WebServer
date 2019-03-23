package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 *  Import dom4j 
 * @author Jeffrey Wei
 *
 */
public class HttpContext {
    
	private static final Map<String,String> contentMap = new HashMap<>();
	
	static {
		initial();
	}
	
	
	public static void initial() {
	    
	    try {
	    	SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("./conf/web.xml"));
			// find root element
			Element root = doc.getRootElement();
			//turn all "mine-mapping" into list
			List<Element> list = root.elements("mime-mapping");
			
			// add extension and mine-type value into  contentMap
			for(Element e : list) {
				contentMap.put(e.elementText("extension"), e.elementText("mime-type"));
			}
			
			
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static String getValue(String name) {
		return contentMap.get(name);
	}
	
	
	public static void main(String[] args) {

		HttpContext context = new HttpContext();
		 String str = "png";
		 System.out.println(context.getValue(str));
	}

}
