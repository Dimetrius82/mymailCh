package com.xrq.mymail;

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

import com.xrq.mydb.MyDatabaseHelper;
import com.xrq.util.MyBroadcastReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Context mContext;
	private RelativeLayout getmail;
	private RelativeLayout sendmail;
	private RelativeLayout addOne;
	private RelativeLayout showall;
	private RelativeLayout l1;
	private MyDatabaseHelper dbhelper;
	private TextView title;
	private BroadcastReceiver myNetReceive;
	String acc;
	String pw;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		this.setContentView(R.layout.mail_main);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
	    this.findView();
	    Intent i=this.getIntent();
	    acc=i.getStringExtra("truename");
	    pw=i.getStringExtra("truepassword");
	    //更新账户和密码
	    updatemsg();
	    //添加动态
	    showAnim();
	    createDatabase();//创建数据库和数据表，用于储存联系人信息。
	     //动态监测网络变化
		//动态监测网络变化
		myNetReceive=(new MyBroadcastReceiver(mContext)).getReceiver();
		IntentFilter mFilter = new IntentFilter();  
	    mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
	    this.registerReceiver(myNetReceive, mFilter); 
	}
	public void updatemsg(){
		dbhelper=new MyDatabaseHelper(mContext,"mail.db",null,1);
		SQLiteDatabase db=dbhelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("account", acc);
		values.put("password", pw);
		db.update("user_msg", values, "id=?", new String[]{"1"});
		values.clear();
		db.close();
	}
	public void saveaccountandpassword(){
		String create_pwAndAcc="create table user_msg ("
                +"account text primary key, "
                +"password text)";	
	}
	 @Override
	 protected void onDestroy(){
		 super.onDestroy();
		    if(myNetReceive!=null){  
	            this.unregisterReceiver(myNetReceive);  
	        }  
	 }
	
	public void createDatabase(){
		dbhelper=new MyDatabaseHelper(mContext,"mail.db",null,1);
		dbhelper.getWritableDatabase();
		Toast.makeText(mContext, "DB OK",Toast.LENGTH_SHORT).show();
	}
	public void showAnim(){
		Animation anim=AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		l1.startAnimation(anim);//设置动画效果。
	}
	public void findView(){
		mContext=this;
		l1=(RelativeLayout)this.findViewById(R.id.l1);
		title=(TextView )this.findViewById(R.id.title);
		title.setText(acc);
		//收信箱
		getmail=(RelativeLayout)this.findViewById(R.id.ll_dynamic);
		getmail.setClickable(true);
		getmail.setOnClickListener(rlayoutListener);
		//发信箱
		sendmail=(RelativeLayout)this.findViewById(R.id.xieyoujian);
		sendmail.setClickable(true);
		sendmail.setOnClickListener(rlayoutListener);
		//添加联系人
		addOne=(RelativeLayout)this.findViewById(R.id.tianjia);
		addOne.setClickable(true);
		addOne.setOnClickListener(rlayoutListener);
		//地址簿
		showall=(RelativeLayout)this.findViewById(R.id.wifi);
		showall.setClickable(true);
		showall.setOnClickListener(rlayoutListener);
		//注册

	}
	
	public void test(){
		
		new Thread(){
			public void run(){
				boolean isSSL = true;
		        String host = "pop3.163.com";
		        int port = 465;
		        String from = "13927594696@163.com";
		        String to = "1251931015@qq.com";
		        boolean isAuth = true;
		        final String username = "13927594696@163.com";
		        final String password = "1995328mmmm";
		        
		        Properties props = System.getProperties();  
		        props.put("mail.pop3.host", "pop3.163.com");  
		        props.put("mail.pop3.auth", "true");  
		    
		    

		        Session session = Session.getInstance(props, new Authenticator() {//default也可以！！
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

		        System.out.println("发送完毕！发送完毕！发送完毕！发送完毕！发送完毕！"
		        		+ "发送完毕！发送完毕！发送完毕！发送完毕！发送完毕！发送完毕！");
			}
		}.start();
	}
	private OnClickListener rlayoutListener =new OnClickListener(){

		@Override
		public void onClick(View v) {
			int id;
			id=v.getId();
			switch(id){
			case R.id.ll_dynamic:
				//去到收信箱活动
				Intent intent3=new Intent(mContext,GetMailActivity.class);
				intent3.putExtra("name", "13927594696@163.com");
				intent3.putExtra("mima", "1995328mmmm");
				startActivity(intent3);
				break;
			case R.id.xieyoujian:
				//跳到写邮箱
				Intent intent2=new Intent(mContext,SendMailActivity.class);
				startActivity(intent2);
				break;
			case R.id.tianjia:
				//跳到AddContactActivity活动界面
				Intent intent=new Intent(mContext,AddContactActivity.class);
				startActivity(intent);
				break;
			case R.id.wifi:
				//跳到联系人列表
				Intent intent1=new Intent(mContext,ShowAddressActivity.class);
				startActivity(intent1);
				break;
	
			default:
				Toast.makeText(mContext, "还在开发，请期待！", Toast.LENGTH_SHORT).show();
				break;
			}
			
		}
		
	};
}
