package com.xrq.mymail;

import android.app.Activity;
import android.app.ProgressDialog;

import com.xrq.mymail.WelcomeActivity;
import com.xrq.util.MailItemUtil;
import com.xrq.util.MyBroadcastReceiver;
import com.xrq.util.MyGetMail;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.xrq.adapter.GetMailAdapter;
import com.xrq.mymail.R;
import com.xrq.view.TextURLView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

public class MyLoginActivity extends Activity {

	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private Button register;
	private TextURLView mTextViewURL;
	public ProgressDialog pd;
	//my add
	private TableLayout table;
	public EditText acc;
	public EditText pw;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private CheckBox box;
	private Button r;
	private BroadcastReceiver myNetReceiver;
	String account,password;
	private boolean Login=false;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
		case 1:
			boolean sign=(Boolean) msg.obj;
			    if(sign){
			    	Intent intent=new Intent(mContext,MainActivity.class);
					intent.putExtra("truename",acc.getText().toString());
					intent.putExtra("truepassword", pw.getText().toString());
					pd.dismiss();
					startActivity(intent);
					finish();
			   
			    }
			    else{
			    	pd.dismiss();
					Toast.makeText(mContext, "登陆失败", Toast.LENGTH_SHORT).show();

			    }
				break;
		case 2:
         default:
            	break;
				
			}
		}
	};//handler变量
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉自带的标题栏
		setContentView(R.layout.mail_login);	
		mContext=this;
		findView();
		initTvUrl();
		init();
		//动态监测网络变化
		myNetReceiver=(new MyBroadcastReceiver(mContext)).getReceiver();
		IntentFilter mFilter = new IntentFilter();  
	    mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
	    mContext.registerReceiver(myNetReceiver, mFilter); 
	}
	 @Override
	 protected void onDestroy(){
		 super.onDestroy();
		    if(myNetReceiver!=null){  
	            this.unregisterReceiver(myNetReceiver);  
	        }  
	 }
	private void findView(){
		rl_user=(RelativeLayout) findViewById(R.id.rl_user);
		mLogin=(Button) findViewById(R.id.login);
		acc=(EditText)this.findViewById(R.id.account);
		pw=(EditText)this.findViewById(R.id.password);
		register=(Button) findViewById(R.id.register);
		mTextViewURL=(TextURLView) findViewById(R.id.tv_forget_password);
		box=(CheckBox)this.findViewById(R.id.r_p);
		table=(TableLayout)this.findViewById(R.id.table);
		//注册
		r=(Button)this.findViewById(R.id.register);
		r.setOnClickListener(loginOnClickListener);
	}

	private void init(){
		Animation anim=AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);
		table.startAnimation(anim);//设置动画效果。
		//设置数据库
		pref=PreferenceManager.getDefaultSharedPreferences(this);
		boolean isRe=pref.getBoolean("r_p", false);
		if(isRe){
			//把密码账号设置
			String account=pref.getString("account", "");
			String password=pref.getString("password", "");
			//在这里尝试连接服务器，当
			acc.setText(account);
			pw.setText(password);
			box.setChecked(true);
			
		}
		mLogin.setOnClickListener(loginOnClickListener);
	}
	
	private void initTvUrl(){
		mTextViewURL.setText("用户必读");
	}
	
	private OnClickListener loginOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			//保存密码
			case  R.id.login:
			if(acc.getText().toString().trim().equals("")||pw.getText().toString().equals("")){
				Toast.makeText(mContext, "请输入账号和密码",Toast.LENGTH_LONG).show();
			}
			else{
			if(box.isChecked()){
				editor=pref.edit();
				editor.putBoolean("r_p",true);
				editor.putString("account", acc.getText().toString());
				editor.putString("password", pw.getText().toString());			
			}
			else{
				editor=pref.edit();
				editor.putBoolean("r_p",false);
				editor.putString("account", "");
				editor.putString("password", "");	
				editor.clear();
			}
		
			editor.commit();
		   //开启线程
			pd=new ProgressDialog(mContext);
			pd.setTitle("登陆窗口");
			pd.setMessage("正在登录....");
			pd.show();
			pd.setCancelable(true);
			System.out.println("00001333333 ");
			new LoginThread().start();
			}
			
			break;
			
			case R.id.register:
				Intent i=new Intent(mContext,RegisterActivity.class);
				startActivity(i);
			}
		}
	};//一个变量，监听变量
	
	//内部线程类
	class LoginThread extends Thread{
	
		
		public void run(){
			 account=acc.getText().toString();
			 password=pw.getText().toString();
			 Log.d("account", account);
			 Log.d("password",password);
			 Properties props=null;
			 Store store = null;
		     Folder folder=null;
		     Message message[] = null;
			 Session session=null;
		     URLName urln=null;
			//链接到服务器，并且获取数据	
		     
		     {
		    	 props=System.getProperties();
		         props.put("mail.store.protocol", "imap");
		         props.put("mail.imap.auth", "true"); //这样才能通过验证
		         props.put("mail.imap.host", "imap.qq.com");	    
		         props.put("mail.imap.port", "993");
		         props.put("mail.imap.ssl.enable", "true");
		         props.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        	 props.put("mail.imap.socketFactory.fallback", "false");
		         session=Session.getInstance(props, null);              					
					try {
					    store=session.getStore("imap");
						store.connect(account,password);
						 System.out.println("111333333 ");
						folder=store.getFolder("INBOX");
						folder.open(Folder.HOLDS_MESSAGES);  
				        message= folder.getMessages();  
				        message = folder.getMessages();
				        System.out.println("444444333333 ");
				        System.out.println("HAHAAHHAMessages's length: " + message.length);
					} catch (MessagingException e) {
						// TODO 自动生成的 catch 块
				        System.out.println("HAHAAHHAMessages's length: ");

						e.printStackTrace();
					}
					System.out.println("77773333 ");
					 android.os.Message m=new android.os.Message();		      
				     m.what=1;	
				     if(store.isConnected()&&message!=null){
				    	 m.obj=true;
				     }
				     else
				    	 m.obj=false;
				     handler.sendMessage(m);	
					  
			     
		     }
		}
	}

	
}

