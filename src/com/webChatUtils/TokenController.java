package com.webChatUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/token")
public class TokenController extends HttpServlet {

	private static final long serialVersionUID = 5876545999576021601L;
		// 自定义 token
	    private String TOKEN = "cmrp";

	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
	       System.out.println("123");
	    	String signature = request.getParameter("signature");  
	        String timestamp = request.getParameter("timestamp");  
	        String nonce = request.getParameter("nonce");  
	        String echostr = request.getParameter("echostr");  
	          
	        PrintWriter out = response.getWriter();  
	        if (checkSignature(signature, timestamp, nonce)){  
	            System.out.println("check ok");  
	            out.print(echostr);  
	        }  
	        out.close();  
	    } 
	    
	    
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    		throws ServletException, IOException {
	    	 System.out.println("123");
		    	String signature = request.getParameter("signature");  
		        String timestamp = request.getParameter("timestamp");  
		        String nonce = request.getParameter("nonce");  
		        String echostr = request.getParameter("echostr");  
		          
		        PrintWriter out = response.getWriter();  
		        if (checkSignature(signature, timestamp, nonce)){  
		            System.out.println("check ok");  
		            out.print(echostr);  
		        }  
		        out.close();  
	    }
	    
	    public  boolean checkSignature(String signature, String timestamp,  
	            String nonce) {  
	        String[] arr = new String[] { TOKEN, timestamp, nonce };  
	        // sort  
	        Arrays.sort(arr);  
	  
	        // generate String  
	        String content = arr[0]+arr[1]+arr[2];  
	          
	          
	        // shal code  
	        String temp = new SHA1().getDigestOfString(content.getBytes());  
	  
	        return temp.equalsIgnoreCase(signature);  
	    }  
}
