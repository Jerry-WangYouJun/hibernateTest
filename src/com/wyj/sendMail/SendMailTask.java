package com.wyj.sendMail;



public class SendMailTask {
	
	 public void myExecutor(){      
          EmailSendService service = new EmailSendService();
          try {
			service.sendMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }  
}
