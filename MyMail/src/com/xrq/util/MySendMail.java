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
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.content.Context;
import android.os.Handler;

public class MySendMail extends Thread {

	public Context mContext;
	public String name="",mima="";
	public String subject="",from="",to="",content="";
	public List<String> list;
	public Handler handler;
	final static int HAS_SENT=1,HAS_NOT_SENT=0;
   	public MySendMail(String s,String f,String t,String c,Context con,List<String> l,Handler h){
   		subject=s;
   		from=f;
   		to=t;
   		content=c;
   		mContext=con;
   		list=l;
   	    name=(new GetPwAndAcc(mContext)).getAcc();
	    mima=(new GetPwAndAcc(mContext)).getPw();
	    handler=h;
   	}

	public void run(){
		//首先要配置好连接到服务器的属性和参数
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.qq.com");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");
				props.put("mail.smtp.ssl.enable", "true");
			    
			   	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			   	props.put("mail.smtp.socketFactory.fallback", "false");
			   	props.put("mail.transport.protocol", "smtp"); 
			   	//配置好以下参数
			   	MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		        CommandMap.setDefaultCommandMap(mc);
			   	
		try {
			   Session session = Session.getDefaultInstance(props, new Authenticator() {//default也可以！！
		            @Override
		            protected PasswordAuthentication getPasswordAuthentication() {//	//创建一个认证类，通过服务器端的认证
		                return new PasswordAuthentication(name, mima);//打开与服务器连接的“窗口”
		            }
		        });
			session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			//"1251931015@qq.com","cqhwthrdqytnjdif"
			Address addressFrom = new InternetAddress(name);//设置发件人的地址以及昵称。
			Address addressTo = new InternetAddress(to);//设置收件人的地址。
			message.setText(content);//设置发件人想要发的文字内容
			message.setSubject(subject);
			message.setFrom(addressFrom);//发件人作为MImeMessage的参数传入
			message.addRecipient(Message.RecipientType.TO, addressTo);//设置收件人地址
	
			//加上附件
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
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.qq.com", name,mima);//传入账号和密码，然后与SMTP服务器进行连接
		    mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
	        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
	        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
	        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
	        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
	        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
	        CommandMap.setDefaultCommandMap(mc);
	        message.setText(content);
	        message.setContent(multipart);//Multipart对象里面包含了多个附件组成
	        message.saveChanges();
			transport.send((Message)message);//利用Transport类把MimeMessage对象传出去，里面包括了邮件的主体内容、附件、以及
			//transport.close();
		} catch (Exception e) {
			e.printStackTrace();
			 //不成功发完邮件就异步返回成功发送与否的信息给主线程
			android.os.Message m=new android.os.Message();
			m.obj="hehe";
	        m.what=HAS_NOT_SENT;					
			handler.sendMessage(m);	
		}
		
		//成功发完邮件就异步返回成功发送与否的信息给主线程
		android.os.Message m=new android.os.Message();
		m.obj="hehe";
        m.what=HAS_SENT;					
		handler.sendMessage(m);	
	}
}