package com.xrq.Async;

import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import com.xrq.mymail.R;
import com.xrq.util.MailItemUtil;
import com.xrq.util.MyGetMail;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncGetMail extends AsyncTask<Void, Integer, Boolean> {

	public Context mContext;
	public List<MailItemUtil> list;
	 Message message[] = null;
	public AsyncGetMail(Context c,List<MailItemUtil> l){
		mContext=c;
		list=l;
	}
	@Override
	protected Boolean doInBackground(Void... arg0) {
		//链接到服务器，并且获取数据		  
        Properties props = System.getProperties();  
        props.put("mail.imap.host", "imap.163.com");  
        props.put("mail.imap.auth", "true");  
        Session session = Session.getDefaultInstance(props, null);  
        URLName urln = new URLName("pop3", "pop3.163.com", 110, null,  
                "13927594696", "1995328mmmm");  
        Store store;
        Folder folder;
		try {
			store = session.getStore(urln);
		    store.connect();  
		    //store.isConnected()可用于判断是否
		    folder = store.getFolder("INBOX");  //获取某一个文件夹里面的邮件，这里取收信箱
		    folder.open(Folder.HOLDS_MESSAGES);  
		    message = folder.getMessages();  
		} catch (NoSuchProviderException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}  
        MyGetMail pmm = null; 
        MailItemUtil item;
        
        for (int i = 0; i < message.length; i++) {  
            pmm = new MyGetMail((MimeMessage) message[i]);  
            try {       
		        item=new MailItemUtil(i,pmm.getSubject(),pmm.getSentDate(),pmm.getReplySign(),pmm.isNew()
		        		             ,pmm.isContainAttach((Part)message[i]),pmm.getFrom(),pmm.getMessageId(),R.drawable.youxiang,pmm.getMailAddress("to"),"");
			    list.add(item);
            } catch (MessagingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}  
                }   

    
		return true;
	}
	protected void onPostExecute(boolean result){
		Toast.makeText(mContext, message.length, Toast.LENGTH_SHORT).show();
	}

}
