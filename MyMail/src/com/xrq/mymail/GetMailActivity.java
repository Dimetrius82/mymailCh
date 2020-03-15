package com.xrq.mymail;

import java.util.ArrayList;
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

import com.xrq.adapter.GetMailAdapter;
import com.xrq.util.GetPwAndAcc;
import com.xrq.util.MailItemUtil;
import com.xrq.util.MyGetMail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
public class GetMailActivity extends Activity {
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
		case 1:
			MailItemUtil l=(MailItemUtil) msg.obj;
			if(l!=null){
			list.add(l);
			GetMailAdapter adapter=new GetMailAdapter(mContext,R.layout.get_mail_item,list);
			listView.setAdapter(adapter);
			}
			else{
				Toast.makeText(mContext, "无法连接到服务器，网络中断", Toast.LENGTH_SHORT).show();

			}
				break;
		case 2:
			Toast.makeText(mContext, "无法链接到服务器，请检查网络连接", Toast.LENGTH_SHORT).show();
         default:
            	break;
				
			}
		}
	};//handler变量
	private String name="";
	private String mima="";
	private ListView listView;
	public List<MailItemUtil> list =new ArrayList<MailItemUtil>();
	private GetMailAdapter adapter;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		this.setContentView(R.layout.get_mail_list_view);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		mContext=this;
        name=(new GetPwAndAcc(mContext)).getAcc();
        mima=(new GetPwAndAcc(mContext)).getPw();
		findView();
		//得到数据
		//testinit();
		getAllEmailData();
		Toast.makeText(mContext, "OK", Toast.LENGTH_SHORT).show();
		//然后显示出来
		listView.setAdapter(adapter);
		showAnim();
		
	}
	

	//动画效果
	public void showAnim(){
		Animation anim=AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		listView.startAnimation(anim);//设置动画效果。
	}

	public void findView(){
		listView=(ListView)this.findViewById(R.id.mail_list_view);
		listView.setOnItemClickListener(itemListener);
		adapter=new GetMailAdapter(mContext,R.layout.get_mail_item,list);
	}
	//监听点击listView的item事件
	private OnItemClickListener itemListener =new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO 自动生成的方法存根
			MailItemUtil item=list.get(position);
			Intent i=new Intent(mContext,DetailActivity.class);
			//把主题，发件人，收件人，时间，附件等内容通过intent都发送到DetailActivity
			i.putExtra("subject", item.getSub());
			i.putExtra("from", item.getFrom());
			i.putExtra("to", item.getTo());
			i.putExtra("date", item.getDate());
			i.putExtra("attach", "附件");
			i.putExtra("id",item.getMsg_id()+"");
			startActivity(i);
		}
		
		
	};
	private void getAllEmailData(){
	   Thread t=new Thread(){
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
		        Store store;
		        Folder folder;
		        Message message[] = null;
				try {
				
				    store=session.getStore("imap");
					store.connect(name,mima);
					folder=store.getFolder("INBOX");
					folder.open(Folder.READ_WRITE);  
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
		        int len=0;
		        if(message!=null){
		        	if(message.length<=30)
		        		len=message.length;
		        	else
		        		len=36;
		        for (int i = message.length-1; i >= message.length-len; i--) {  
		            pmm = new MyGetMail((MimeMessage) message[i]);  
		            try {       
				        item=new MailItemUtil(i,pmm.getSubject(),
				        		pmm.getSentDate(),
				        		pmm.getReplySign(),
				        		pmm.isNew(),
				        	    true,                 //这里不要调用判断附件是否存在的逻辑，会耗流量！
				        	    pmm.getFrom(),
				        		pmm.getMessageId(),
				        	    GetMailActivity.getImageId(pmm.getFrom()),pmm.getMailAddress("to"),"");
					            //
				        android.os.Message m=new android.os.Message();
				        m.obj=item;
				        m.what=1;					
						handler.sendMessage(m);			        
		            } catch (MessagingException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}  
		                } 
		        }
		        else{//为空，也就是没链接上或者网络不畅通
		        	 android.os.Message m=new android.os.Message();
				      
				        m.what=2;					
						handler.sendMessage(m);		
		        }
		   }
	   };
	   t.start();
	}
	private void testinit(){
	}
	
	public static int getImageId(String str){
		if(str.contains("service"))
			return R.drawable.service;
		else if(str.contains("edu.cn"))
			return R.drawable.szu;
		else if(str.contains("qq"))
			return R.drawable.youxiang;
		else if(str.contains("163")||str.contains("139")||str.contains("126"))
			return R.drawable.wangyi;
		else 
			return R.drawable.user;
	}
}
