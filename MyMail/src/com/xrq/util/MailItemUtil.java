package com.xrq.util;

public class MailItemUtil {

	private int imageid;
	private int msg_id;
	private String subject;
	private String date;
	boolean reply_sign;
	boolean hasread;
	boolean containAttachment;
	private String from;
	private String ID;
	private String to;
	private String fileName;
	public MailItemUtil(int m,String s,String d,boolean r
			,boolean h,boolean c,String f,String i,
			int ii,String t,String fn){
		msg_id=m;
		subject=s;
		date=d;
		reply_sign=r;
		hasread=h;
		containAttachment=c;
		from=f;
		ID=i;
		imageid=ii;
		to=t;
		fileName=fn;
	}
	public String getFileName(){
		
		return fileName;
	}
	public String getTo(){
		return to;
	}
	public int getMsg_id(){
		return msg_id;
	}
	
	public String getSub(){
		return subject;
	}
	public String getDate(){
		return date;
	}
	
	public boolean getReplySign(){
		return reply_sign;
	}
	
	public boolean getHasRead(){
		return hasread;
	}	
	
	public boolean getContainAttach(){
		return containAttachment;
	}
	
	public String getFrom(){
		return from;
	}
	
	public String getID(){
		return ID;
	}
	
	public int getImageId(){
		return imageid;
	}
	
	
	
	
}
