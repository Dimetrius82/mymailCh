package com.xrq.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MyBroadcastReceiver {
	private Context mContext;
	private ConnectivityManager mConnectivityManager;  
    private NetworkInfo netInfo; 
	public BroadcastReceiver myNetReceiver;
	public MyBroadcastReceiver(Context c){
		mContext=c;
		init();
	}
	
	public BroadcastReceiver getReceiver(){
		return myNetReceiver;
	}
	public void init(){
     myNetReceiver = new BroadcastReceiver() {  
		  
		
		 @Override  
		 public void onReceive(Context context, Intent intent) {  
		    
		  String action = intent.getAction();  
		        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {  
		             
		            mConnectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);  
		            netInfo = mConnectivityManager.getActiveNetworkInfo();    
		            if(netInfo != null && netInfo.isAvailable()) {  
		  
		                 /////////////网络连接  
		                String name = netInfo.getTypeName();  
		                  
		                if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){  
		                 /////WiFi网络  
		                	Toast.makeText(mContext, "网络连接成功",Toast.LENGTH_SHORT).show();
		  
		                }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){  
		                /////有线网络  
		                	Toast.makeText(mContext, "网络连接成功",Toast.LENGTH_SHORT).show();
		                }else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){  
		               /////////3g网络  
		                	Toast.makeText(mContext, "网络连接成功",Toast.LENGTH_SHORT).show();

		                }  
		              } else {  
		             ////////网络断开  
		            	  Toast.makeText(mContext, "无网络连接",Toast.LENGTH_SHORT).show();
		            }  
		        }  
		  
		   } 
	};
	}
}
