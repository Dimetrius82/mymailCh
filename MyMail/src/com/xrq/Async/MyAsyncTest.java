package com.xrq.Async;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.AsyncTask;

public class MyAsyncTest extends AsyncTask<Void, Integer, Boolean> {

	@Override
	protected Boolean doInBackground(Void... params) {
		//Socket s=new Socket("www.baidu.cn", 8080);
		sendEmail();
		return true;
	}

	public void sendEmail(){
		// TODO 自动生成的方法存根
				boolean isSSL = true;
		        String host = "smtp.163.com";
		        int port = 465;
		        String from = "13927594696@163.com";
		        String to = "1251931015@qq.com";
		        boolean isAuth = true;
		        final String username = "13927594696@163.com";
		        final String password = "1995328mmmm";
		        
		        Properties props = System.getProperties();  
		        props.put("mail.imap.host", "imap.163.com");  
		        props.put("mail.imap.auth", "true");  
		    
		        props.put("mail.smtp.ssl.enable", isSSL);
		        props.put("mail.smtp.host", host);
		        props.put("mail.smtp.port", port);
		        props.put("mail.smtp.auth", isAuth);

		        Session session = Session.getDefaultInstance(props, new Authenticator() {
		            @Override
		            protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(username, password);
		            }
		        });

		        try {
		            Message message = new MimeMessage(session);

		            message.setFrom(new InternetAddress(from));
		            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		            message.setSubject("主题");
		            message.setText("内容");

		            Transport.send(message);
		        } catch (AddressException e) {
		            e.printStackTrace();
		        } catch (MessagingException e) {
		            e.printStackTrace();
		        }

	}//send mail
}
