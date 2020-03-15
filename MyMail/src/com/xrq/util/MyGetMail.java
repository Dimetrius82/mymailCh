package com.xrq.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.protocol.BASE64MailboxDecoder;

import android.os.Environment;

public class MyGetMail {
	    private MimeMessage mimeMessage = null;  //MimeMessage的功能十分强大，可以获取邮件上想要的几乎任何东西 
	    private String saveAttachPath = ""; //附件下载后的存放目录   
	    private StringBuffer bodytext = new StringBuffer();//g该对象存放邮件的所有内容包括HTML代码   
	    private String dateformat = "yy-MM-dd HH:mm"; //设置默认的日前显示格式，使日期能够清楚地在ListView界面中显示出来给用户   
	    private String TruefileName="NULL";
	    public MyGetMail(MimeMessage mimeMessage) {   
	        this.mimeMessage = mimeMessage;   
	    }   
	    public MyGetMail(){
	    	
	    }
	   public String getPath(){
		   return saveAttachPath;
	   }
	    public void setMimeMessage(MimeMessage mimeMessage) {   
	        this.mimeMessage = mimeMessage;   
	    }   
	    public static String base64Decode(String base64Code) throws Exception{  
	        return  new BASE64MailboxDecoder().decode(base64Code);  
	    }  
	      
	    //得到文件名
	    public String  getTrueFileName(Part part) throws Exception {   
	    	  String fileName =new String(" NULL");
		        if (part.isMimeType("multipart/*")) {   
		            Multipart mp = (Multipart) part.getContent();   
		            for (int i = 0; i < mp.getCount(); i++) {   
		                BodyPart mpart = mp.getBodyPart(i);   
		                String disposition = mpart.getDisposition();   
		                if ((disposition != null)   
		                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition   
		                                .equals(Part.INLINE)))) {   
		                    fileName = mpart.getFileName(); //对文件名进行解码  
		                    if (fileName.toLowerCase().indexOf("gb") != -1) { //解码  
		                        fileName = MimeUtility.decodeText(fileName);   
		                    }                            
		                } 
		                else {   
		                    fileName = mpart.getFileName();   
		                    if ((fileName != null)   
		                            && (fileName.toLowerCase().indexOf("GB") != -1)) {   
		                        fileName = MimeUtility.decodeText(fileName);     
		                    }   
		                }   //else
		            }  //for 
		        } 
		        return fileName;
	    }
	    public static void copy(InputStream is, OutputStream os) throws Exception {  
	        byte[] bytes = new byte[1024];  
	        int len = 0;  
	        while ((len=is.read(bytes)) != -1 ) {  
	        	bytes=base64Decode(new String(bytes)).getBytes();
	            os.write(bytes, 0, len);  
	        }  
	        if (os != null)  
	            os.close();  
	        if (is != null)  
	            is.close();  
	    }  
	    // 获得发件人的地址和姓名  
	     
	    public String getFrom() throws Exception {   
	        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();  //发送人所有的信息以一个InternetAddress 
	        String from = address[0].getAddress();         //对象包裹起来。
	        if (from == null)   
	            from = "";   
	        String personal = address[0].getPersonal();   
	        if (personal == null)   
	            personal = "";   
	        String fromaddr = personal + "<" + from + ">";   
	        return fromaddr;   
	    }   
	  
	    //获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址  
	      
	    public String getMailAddress(String type) throws Exception {   
	        String mailaddr = "";   
	        String addtype = type.toUpperCase();   
	        InternetAddress[] address = null;   
	        if (addtype.equals("TO") || addtype.equals("CC")|| addtype.equals("BCC")) {   
	            if (addtype.equals("TO")) {   
	                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);   
	            } else if (addtype.equals("CC")) {   
	                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);   
	            } else {   
	                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);   
	            }   
	            if (address != null) {   
	                for (int i = 0; i < address.length; i++) {   
	                    String email = address[i].getAddress();   
	                    if (email == null)   
	                        email = "";   
	                    else {   
	                        email = MimeUtility.decodeText(email);   
	                    }   
	                    String personal = address[i].getPersonal();   
	                    if (personal == null)   
	                        personal = "";   
	                    else {   
	                        personal = MimeUtility.decodeText(personal);   
	                    }   
	                    String compositeto = personal + "<" + email + ">";   
	                    mailaddr += "," + compositeto;   
	                }   
	                mailaddr = mailaddr.substring(1);   
	            }   
	        } else {   
	            throw new Exception("Error emailaddr type!");   
	        }   
	        return mailaddr;   
	    }   
	  
	    // 获得邮件主题  
	      
	    public String getSubject() throws MessagingException {   
	        String subject = "";   
	        try {   
	            subject = MimeUtility.decodeText(mimeMessage.getSubject());   
	            if (subject == null)   
	                subject = "";   
	        } catch (Exception exce) {}   
	        return subject;   
	    }   
	  
	    //获得邮件发送日期  ，并且把日期编程格式化  
	    public String getSentDate() throws Exception {   
	        Date sentdate = mimeMessage.getSentDate();   
	        SimpleDateFormat format = new SimpleDateFormat(dateformat);   
	        return format.format(sentdate);   
	    }   
	  
	    //以字符串形式 获得邮件正文内容    
	    public String getBodyText() {   
	        return bodytext.toString();   
	    }   
	 
	    // 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析  
	    //注意：这里得到的是字符串，然后可以转化为字节数组来放置到WebView控件中！  
	    public void getMailContent(Part part) throws Exception {   
	        String contenttype = part.getContentType();  
	        int nameindex = contenttype.indexOf("name");   
	        boolean conname = false;   
	        if (nameindex != -1)   
	            conname = true;   
	       // System.out.println("CONTENTTYPE: " + contenttype);   //从IMAP协议返回的报文头部中获得文件内容的MIME类型
	        if (part.isMimeType("text/plain") && !conname) {   
	            bodytext.append((String) part.getContent());   
	        } else if (part.isMimeType("text/html") && !conname) {   
	            bodytext.append((String) part.getContent());   
	        } else if (part.isMimeType("multipart/*")) {   //如果该邮件是包含多种文件的MIME类型，则执行递归操作
	            Multipart multipart = (Multipart) part.getContent();   
	            int counts = multipart.getCount();   
	            for (int i = 0; i < counts; i++) {   
	                getMailContent(multipart.getBodyPart(i));   
	            }   
	        } else if (part.isMimeType("message/rfc822")) {   
	            getMailContent((Part) part.getContent());   
	        } else {}   
	    }   
	   
	     //判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"  
	    
	    public boolean getReplySign() throws MessagingException {   
	        boolean replysign = false;   
	        String needreply[] = mimeMessage   
	                .getHeader("Disposition-Notification-To");   
	        if (needreply != null) {   
	            replysign = true;   
	        }   
	        return replysign;   
	    }   
	    
	     // 获得此邮件的Message-ID  
	    
	    public String getMessageId() throws MessagingException {   
	        return mimeMessage.getMessageID();   //邮件的messageID是唯一的，因此我们可以根据这一点来获得某一个特定邮件的
	    }   
	  
	   
	      //判断此邮件是否已读，如果未读返回返回false,反之返回true
	      
	    public boolean isNew() throws MessagingException {   
	        boolean isnew = false;   
	        Flags flags = ((Message) mimeMessage).getFlags();   
	        Flags.Flag[] flag = flags.getSystemFlags();   
	        //System.out.println("flags's length: " + flag.length);   
	        for (int i = 0; i < flag.length; i++) {   
	            if (flag[i] == Flags.Flag.SEEN) {   
	                isnew = true;   
	                //System.out.println("seen Message.......");   
	                break;   
	            }   
	        }   
	        return isnew;   
	    }   
	  
	 //判断是否包含附件  
	    public boolean isContainAttach(Part part) throws Exception {   //Part也就是MimeMessage/Message类型的对象，
	    	//                                                           邮件的内容都会在里面
	        boolean attachflag = false;   
	        String contentType = part.getContentType();   
	        if (part.isMimeType("multipart/*")) {   //说明该邮件包含附件，MIME多媒体类型
	            Multipart mp = (Multipart) part.getContent();   
	            for (int i = 0; i < mp.getCount(); i++) {   
	                BodyPart mpart = mp.getBodyPart(i);   
	                String disposition = mpart.getDisposition();   
	                if ((disposition != null)   
	                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition   
	                                .equals(Part.INLINE))))   
	                    attachflag = true;   
	                else if (mpart.isMimeType("multipart/*")) {   
	                    attachflag = isContainAttach((Part) mpart);   
	                } else {   
	                    String contype = mpart.getContentType();   
	                    if (contype.toLowerCase().indexOf("application") != -1)   
	                        attachflag = true;   
	                    if (contype.toLowerCase().indexOf("name") != -1)   
	                        attachflag = true;   
	                }   
	            }   
	        } else if (part.isMimeType("message/rfc822")) {   
	            attachflag = isContainAttach((Part) part.getContent());   
	        }   
	        return attachflag;   
	    }   
	  
	    public InputStream getTXTInputStream(Part part) throws Exception{
	    	   String fileName="";
	    	   if (part.isMimeType("multipart/*")) {
	            Multipart mp = (Multipart) part.getContent();   
	            for (int i = 0; i < mp.getCount(); i++) {   
	                BodyPart mpart = mp.getBodyPart(i);   
	                String disposition = mpart.getDisposition();   
	                if ((disposition != null)   
	                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition   
	                                .equals(Part.INLINE)))) {   
	                    fileName = mpart.getFileName(); //对文件名进行解码  
	                    if (fileName.toLowerCase().indexOf("gb") != -1||fileName.toLowerCase().indexOf("GB") != -1) { //解码  
	                        fileName = MimeUtility.decodeText(fileName);   
	                    }   
	                    if(fileName.contains("txt")||fileName.contains("doc")||fileName.contains("pdf")||fileName.contains("docx"))
	                    
	                         return mpart.getInputStream();
	                    else
	                    	return null;
	                } 
	                else if (mpart.isMimeType("multipart/*")) {   
	                          saveAttachMent(mpart);//表明该邮件包含多附件   
	                } 
	                else {   
	                    fileName = mpart.getFileName();   
	                    if ((fileName != null)   
	                            && (fileName.toLowerCase().indexOf("GB") != -1)) {   
	                        fileName = MimeUtility.decodeText(fileName);   
	                        if(fileName.contains("txt")||fileName.contains("doc")||fileName.contains("pdf")||fileName.contains("docx"))    	                    
		                         return mpart.getInputStream();
		                    else
		                    	return null;
	                    }   
	                }   
	            }   
	    	   }
	    	   else
	    		   return null;
			return null;           
	    }
	    public void getTXTContent(InputStream in) throws Exception{
	    	BufferedInputStream bis=null;
	    	  try {    
		            bis = new BufferedInputStream(in);  
		            byte c[]=new byte[2048]; 
		            int temp=0;
		            do{	            	
		            	temp=bis.read(c);
		            }while(temp!=-1);
		           
		        } catch (Exception exception) {  
		            exception.printStackTrace();  
		            throw new Exception("文件预览失败!");  
		        } finally {  		           
		            bis.close();  
		        }  
	    }
	    
	     public InputStream getImageInputStream(Part part)throws Exception{
	    	   String fileName="";
	    	   if (part.isMimeType("multipart/*")) {
	            Multipart mp = (Multipart) part.getContent();   
	            for (int i = 0; i < mp.getCount(); i++) {   
	                BodyPart mpart = mp.getBodyPart(i);   
	                String disposition = mpart.getDisposition();   
	                if ((disposition != null)   
	                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition   
	                                .equals(Part.INLINE)))) {   
	                    fileName = mpart.getFileName(); //对文件名进行解码  
	                    if (fileName.toLowerCase().indexOf("gb") != -1) { //解码  
	                        fileName = MimeUtility.decodeText(fileName);   
	                    }   
	                    if(fileName.contains("jpg")||fileName.contains("png")||fileName.contains("bmp"))
	                    
	                         return mpart.getInputStream();
	                    else
	                    	return null;
	                } 
	                else if (mpart.isMimeType("multipart/*")) {   
	                          saveAttachMent(mpart);//表明该邮件包含多附件   
	                } 
	                else {   
	                    fileName = mpart.getFileName();   
	                    if ((fileName != null)   
	                            && (fileName.toLowerCase().indexOf("GB") != -1)) {   
	                        fileName = MimeUtility.decodeText(fileName);   
	                        if(fileName.contains("jpg")||fileName.contains("png")||fileName.contains("bmp"))
	    	                    
		                         return mpart.getInputStream();
		                    else
		                    	return null;
	                    }   
	                }   
	            }   
	    	   }
	    	   else
	    		   return null;
			return null;
	        
	     }
	     //保存附件        
	    public void saveAttachMent(Part part) throws Exception {   
	      String fileName;
	        if (part.isMimeType("multipart/*")) {   
	            Multipart mp = (Multipart) part.getContent();   
	            for (int i = 0; i < mp.getCount(); i++) {   
	                BodyPart mpart = mp.getBodyPart(i);   
	                String disposition = mpart.getDisposition();   
	                if ((disposition != null)   
	                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition   
	                                .equals(Part.INLINE)))) {   
	                    fileName = mpart.getFileName(); //对文件名进行解码  
	                    if (fileName.toLowerCase().indexOf("gb") != -1) { //解码  
	                        fileName = MimeUtility.decodeText(fileName);   
	                    }   
	                    
	                          saveFile(fileName, mpart.getInputStream());  //单文件附件时 
	                } 
	                else if (mpart.isMimeType("multipart/*")) {   
	                          saveAttachMent(mpart);//表明该邮件包含多附件   
	                } 
	                else {   
	                    fileName = mpart.getFileName();   
	                    if ((fileName != null)   
	                            && (fileName.toLowerCase().indexOf("GB") != -1)) {   
	                        fileName = MimeUtility.decodeText(fileName);   
	                        saveFile(fileName, mpart.getInputStream());   
	                    }   
	                }   
	            }   
	        } else if (part.isMimeType("message/rfc822")) {   
	            saveAttachMent((Part) part.getContent());   
	        }   
	    }  
	      
	    //设置日期显示格式 	      
	    public void setDateFormat(String format) throws Exception {   
	        this.dateformat = format;   
	    }   
	  
	    
	     //获得附件存放路径     
	    public String getAttachPath() {   
	        return saveAttachPath;   
	    }   
	  
	    
	     //真正的保存附件到指定目录里
	    private void saveFile(String fileName, InputStream in) throws Exception {    
	    	String savedir=Environment.getExternalStorageDirectory()+"/"+"MyMail"+"/";
			File dir=new File(savedir);
			if(!dir.exists()){//把所有的下载文件都放在存储下的dlfiledir文件夹里面。
				dir.mkdir();
			}   
	        String separator = "";   
	        separator = "/";    
	        File storefile = new File(savedir + separator + fileName);
	        saveAttachPath=storefile.getAbsolutePath();
	        if(!storefile.exists()){
	        BufferedOutputStream bos = null;  
	        BufferedInputStream bis = null;  
	        try {  
	            bos = new BufferedOutputStream(new FileOutputStream(storefile));  
	            bis = new BufferedInputStream(in);  
	            byte c[]=new byte[4096]; 
	            int temp=0;
	            do{
	            	temp=bis.read(c);
	            	bos.write(c);  
	                bos.flush();                    	
	            }while(temp==-1);
	           
	        } catch (Exception exception) {  
	            exception.printStackTrace();  
	            throw new Exception("文件保存失败!");  
	        } finally {  
	        	 bis.close();  
	            bos.close();  
	           
	        }  
	    }  
	    }
	 
}
