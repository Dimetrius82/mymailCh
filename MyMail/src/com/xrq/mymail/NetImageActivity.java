package com.xrq.mymail;

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
import javax.mail.URLName;
import javax.mail.internet.MimeMessage;

import com.xrq.util.GetPwAndAcc;
import com.xrq.util.MyBroadcastReceiver;
import com.xrq.util.MyGetMail;
import com.xrq.view.ScaleImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NetImageActivity extends Activity {

	private ImageView iv;
	String name="",mima="";
	public Bitmap bm;
	int msg_id=1;
	ProgressDialog pd;
	private Context mContext;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
		    case 1:
		    	Bitmap image=(Bitmap)msg.obj;
		    	if(image!=null)
		    	iv.setImageBitmap(image);
		    	else
		    		Toast.makeText(mContext, "图片加载完成", Toast.LENGTH_SHORT).show();
			pd.dismiss();
		    	break;
			default:
		    break;
			}
		}
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		this.setContentView(R.layout.net_image);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
	    this.findView();
	    mContext=this;
	    //从数据库获取密码等
		name=(new GetPwAndAcc(mContext)).getAcc();
		mima=(new GetPwAndAcc(mContext)).getPw();
	    showImage();
	    msg_id=Integer.parseInt(this.getIntent().getStringExtra("id"));
	    pd=new ProgressDialog(mContext);
	    pd.setTitle("加载图片");
	    pd.setMessage("正在加载......");
	    pd.show();
	}
	public void findView(){		
		iv=(ImageView)this.findViewById(R.id.net_imageView);
		
	}
	public void showImage(){
		
		new Thread(){
			public void run(){
				 
			        Bitmap bitmap = null;   
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
					    System.out.println("1111222221 ");
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
						try {
							is = pmm.getImageInputStream((Part) message[msg_id]);
						    bitmap = BitmapFactory.decodeStream(is);		        
						} catch (Exception e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}   
						finally{
						    try {
								is.close();
							} catch (IOException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						}
			            
			            android.os.Message m=new android.os.Message();
				        m.obj=bitmap;
				        m.what=1;					
						handler.sendMessage(m);		
			            
			}
		}.start();
	}
	
}
