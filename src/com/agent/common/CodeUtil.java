package com.agent.common;

public class CodeUtil {
	  
	   public static String  getFixCode(int  max){
		    if(max  <= 9 ){
		    	return "-0" + max;
		    }else{
		    	return  "-" + max ;
		    }
	   }
}
