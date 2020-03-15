package com.xrq.mymail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.xrq.util.MyBroadcastReceiver;
import com.xrq.util.SpUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
	protected static final String TAG = "WelcomeActivity";
	private Context mContext;
	private ImageView mImageView;
	private SharedPreferences sp;
	private ConnectivityManager mConnectivityManager;  
    private NetworkInfo netInfo;  
    private Context c=this;
	private BroadcastReceiver myNetReceiver;
	private Thread t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		findView();
		init();
		
		test();
	
	}

	private void findView() {
		mImageView = (ImageView) findViewById(R.id.iv_welcome);
	}
	 @Override
	 protected void onDestroy(){
		 super.onDestroy();
		    if(myNetReceiver!=null){  
	            c.unregisterReceiver(myNetReceiver);  
	        }  
	 }
	
	@SuppressWarnings("static-access")
	private void init() {
		mImageView.postDelayed(new Runnable() {
			@Override
			public void run() {
					
					Intent intent = new Intent(mContext, MyLoginActivity.class);
					startActivity(intent);
					finish();//销毁该活动，也就是不会到欢迎页		
			
			}
		},2000);//欢迎页两秒之后到WELCOMNE
		
	}
	
	private void test(){
		t=new Thread(){
			public void run(){
				
			}
		};
		
		t.start();
	}

}
