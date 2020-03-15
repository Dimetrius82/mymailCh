package com.xrq.util;

import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.internet.MimeMessage;

import android.content.Context;
import android.os.Handler;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

public class MyThread extends Thread {
    private String type;
    public Message message[] = null;
    String sign="0";
    private int msg_id;
    public Handler handler;
    String name="",mima="";
    Context mContext;
    public MyThread(String t,int mi,Handler h,Context mc){
    	type=t;
        msg_id=mi;
        handler=h;
        mContext=mc;
    	sign="0";
    }
    public void run(){
  
    	 //链接到服务器，并且获取数据	
	    name=(new GetPwAndAcc(mContext)).getAcc();
	    mima=(new GetPwAndAcc(mContext)).getPw();
        Properties props = System.getProperties();  
        props=System.getProperties();
         props.put("mail.store.protocol", "imap");
         props.put("mail.imap.auth", "true"); //这样才能通过验证
         props.put("mail.imap.host", "imap.qq.com");	    
         props.put("mail.imap.port", "993");
         props.put("mail.imap.ssl.enable", "true");
         props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    	 props.put("mail.imap.socketFactory.fallback", "false");
         Session session=Session.getInstance(props, null);   
        Store store = null;
        Folder folder;
        Message message[] = null;
		try {
		
		    store=session.getStore("imap");
			store.connect(name,mima);
			folder=store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);  //尝试指定为可写与可读，这样改变已读或者删除。
	        message= folder.getMessages();  
	        message = folder.getMessages();
		    //store.isConnected()可用于判断是否
		} catch (NoSuchProviderException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}  
    		        MyGetMail pmm = null; 
    		        MailItemUtil item;
    		        if(message==null||!(store.isConnected())){
    		        	sign="0";
    		        	android.os.Message m=new android.os.Message();
				        m.obj=sign;
				        m.what=5;//表示操作不成功					
						handler.sendMessage(m);		
    		        }
    		        else{
    		        	if(type.equalsIgnoreCase("hasRead")){
    		        		try {
								message[msg_id].setFlag(Flag.SEEN, true);
							} catch (MessagingException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
								sign="0";
	        		        	android.os.Message m=new android.os.Message();
	    				        m.obj=sign;
	    				        m.what=5;//已读				
	    						handler.sendMessage(m);	
							}
    		        		sign="1";
        		        	android.os.Message m=new android.os.Message();
    				        m.obj=sign;
    				        m.what=6;//已读				
    						handler.sendMessage(m);		
    		        	}
    		        	else if(type.equalsIgnoreCase("hasNotRead")){
    		        		try {
								message[msg_id].setFlag(Flag.SEEN, false);
							} catch (MessagingException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
								sign="0";
	        		        	android.os.Message m=new android.os.Message();
	    				        m.obj=sign;
	    				        m.what=5;//已读				
	    						handler.sendMessage(m);
							}
    		        		sign="1";
        		        	android.os.Message m=new android.os.Message();
    				        m.obj=sign;
    				        m.what=7;//未读			
    						handler.sendMessage(m);		
    		        	}
	                    else if (type.equalsIgnoreCase("delete")){
	                    	try {
								message[msg_id].setFlag(Flag.DELETED, true);
							} catch (MessagingException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
								sign="0";
	        		        	android.os.Message m=new android.os.Message();
	    				        m.obj=sign;
	    				        m.what=5;//已读				
	    						handler.sendMessage(m);
							}
	                    	sign="1";
        		        	android.os.Message m=new android.os.Message();
    				        m.obj=sign;
    				        m.what=8;//删除			
    						handler.sendMessage(m);		
    		        	}
	                    else if (type.equalsIgnoreCase("download")){
	                    	pmm= new MyGetMail((MimeMessage) message[msg_id]); 
	                    	try {
								pmm.saveAttachMent((Part) message[msg_id]);
							} catch (Exception e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
								sign="0";
	        		        	android.os.Message m=new android.os.Message();
	    				        m.obj=sign;
	    				        m.what=5;			
	    						handler.sendMessage(m);
							}  
	                    	sign="1";
        		        	android.os.Message m=new android.os.Message();
    				        m.obj=sign;
    				        m.what=9;//下载				
    						handler.sendMessage(m);		
	                    }
    		        }
    		        
    }
}
