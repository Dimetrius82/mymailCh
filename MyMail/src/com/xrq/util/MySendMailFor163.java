package com.xrq.util;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;

import android.os.Handler;


public class MySendMailFor163 extends Thread {
	
	public String name="",mima="";
	public String subject="",from="",to="",content="";
	public List<String> list;
	public Handler handler;
	public final static int HAS_SENT=1,HAS_NOT_SENT=0;
   	public MySendMailFor163 (String s,String f,String t,String c,List<String> l,Handler h){
   		subject=s;
   		from=f;
   		to=t;
   		content=c;
   		list=l;
   		handler=h;
   	    name = "13927594696@163.com";
        mima = "1995328mmmm";//授权码，这侧为了测试方便，可以固定写下来
   	}

	public void run(){
		        String host = "smtp.163.com";
		        boolean isAuth = true;     
		        Properties props = System.getProperties();  
		       
		   	  
		        //
				props.put("mail.smtp.host", "smtp.163.com");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");
				props.put("mail.smtp.ssl.enable", "true");
			   	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			   	props.put("mail.smtp.socketFactory.fallback", "false");
			   	props.put("mail.transport.protocol", "smtp"); 

		   	    MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		        CommandMap.setDefaultCommandMap(mc);
		        
		        Session session = Session.getInstance(props, new Authenticator() {//default也可以！！
		            @Override
		            protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(name, mima);
		            }
		        });

		        try {
		            Message message = new MimeMessage(session);//一个MimeMessage对象可以包裹着邮箱详细内容、邮箱附件等！
		            session.setDebug(true);
		            message.setFrom(new InternetAddress(from));//设置发件人的邮箱地址
		            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));//设置收件人
		            message.setSubject(subject);//设置邮箱的主题
		            message.setText(content);//设置邮箱的内容
		         /*   //加上附件
		            Multipart multipart = new MimeMultipart(); //这个Multipart对象可以添加多个附件实体
		            for(int i=0;i<list.size();i++){// //根据List指向的内容来把指定目录下的文件作为附件加到MultiPart对象里面
		            BodyPart messageBodyPart = new MimeBodyPart();// BodyPart对象作为附件信息的一个封装           
		            DataSource source = new FileDataSource(list.get(i));
		            messageBodyPart.setDataHandler(new DataHandler(source));
		            File file=new File(list.get(i));
		            messageBodyPart.setFileName(file.getName());//得到文件的名字
		            multipart.addBodyPart(messageBodyPart);	
		            }
		            // 最后把包含附件的邮件内容加到MimeMessage对象里面
		            message.setContent(multipart);//Multipart对象里面包含了多个附件组成
		            //加上附件
		             * */
		            
		        	Transport transport = session.getTransport("smtp");
					transport.connect("smtp.163.com", name,mima);//传入账号和密码，然后与SMTP服务器进行连接

			   	     mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
			        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			        CommandMap.setDefaultCommandMap(mc);
					transport.send(message);//利用Transport类把MimeMessage对象传出去，里面包括了邮件的主体内容、附件、以及
					//transport.close();
		            SMTPTransport.send(message);//发送邮件
		        } catch (AddressException e) {
		            e.printStackTrace();
		            //不成功发完邮件就异步返回成功发送与否的信息给主线程
					android.os.Message m=new android.os.Message();
			        m.what=HAS_NOT_SENT;					
					handler.sendMessage(m);	
		        } catch (MessagingException e) {
		            e.printStackTrace();
		            //不成功发完邮件就异步返回成功发送与否的信息给主线程
					android.os.Message m=new android.os.Message();
			        m.what=HAS_NOT_SENT;					
					handler.sendMessage(m);	
		        }

		        //成功发完邮件就异步返回成功发送与否的信息给主线程
				android.os.Message m=new android.os.Message();
		        m.what=HAS_SENT;					
				handler.sendMessage(m);	
	}


}
