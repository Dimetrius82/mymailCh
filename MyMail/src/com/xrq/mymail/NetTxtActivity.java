package com.xrq.mymail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.xrq.util.GetPwAndAcc;
import com.xrq.util.MyGetMail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class NetTxtActivity extends Activity {

	private Context mContext;
	String name="",mima="";
	private WebView txt_view;
	private ProgressDialog pd;
	int msg_id=2;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
		    case 1:
		    	StringBuffer sb=(StringBuffer)msg.obj;
		    	String str = "么么哒爱你哟";
				try {
					str = (new MyGetMail()).base64Decode(sb.toString()).getBytes().toString();
					
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
		    	txt_view.loadDataWithBaseURL(null,str, "text/html", "utf-8", null);  
		    	txt_view.getSettings().setJavaScriptEnabled(true);  
		    	txt_view.setWebChromeClient(new WebChromeClient());  
		        Toast.makeText(mContext, "文本加载完成", Toast.LENGTH_SHORT).show();
			pd.dismiss();
		    	break;
			default:
		    break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		this.setContentView(R.layout.net_txt);
		 mContext=this;
		 //从数据库获取密码等
		 name=(new GetPwAndAcc(mContext)).getAcc();
		 mima=(new GetPwAndAcc(mContext)).getPw();
		 findView();
		 Toast.makeText(mContext, name, Toast.LENGTH_LONG).show();
		 pd=new ProgressDialog(mContext);
         pd.setTitle("加载文本附件内容");
         pd.setMessage("正在加载....");
		 pd.setCancelable(true);
		 pd.show();		 
		 //获得详细的缓存数据
		 Intent msg=this.getIntent();
		 msg_id=Integer.parseInt(msg.getStringExtra("id"));
	     Toast.makeText(mContext, msg_id+"", Toast.LENGTH_SHORT).show();
         //加载文本文件内容
		 showTXT();
	}
	
	public void findView(){
		txt_view=(WebView)this.findViewById(R.id.txt_content); 
	}
	public void showTXT(){

		new Thread(){ 
			public void run(){
      ;   
        //连接到服务器，并且获取数据		  
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
        Store store;
        Folder folder;
        Message message[] = null;
		try {
		
		    store=session.getStore("imap");
			store.connect(name,mima);
			 System.out.println("111333333 ");
			folder=store.getFolder("INBOX");
			folder.open(Folder.HOLDS_MESSAGES);  
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
        pmm = new MyGetMail((MimeMessage) message[msg_id]);  
            InputStream is=null;
            BufferedInputStream bis=null;
            StringBuffer sb=new StringBuffer("");
			try {
				is = pmm.getTXTInputStream((Part) message[msg_id]);
		        bis = new BufferedInputStream(is);  
		      
			    byte c[]=new byte[1024]; 
			    int temp=0;
		        while((temp=bis.read(c))!=-1){
		        	sb=sb.append(c.toString());
		        }
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}    
			  finally {  		           
		        try {
		        	is.close();
					bis.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}  
		    }           
            android.os.Message m=new android.os.Message();
	        m.obj=sb;
	        m.what=1;					
			handler.sendMessage(m);		
			}  

		}.start();	
	}
	
}
